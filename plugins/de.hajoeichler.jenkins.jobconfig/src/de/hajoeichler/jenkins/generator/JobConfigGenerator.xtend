package de.hajoeichler.jenkins.generator

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.generator.IFileSystemAccess
import de.hajoeichler.jenkins.jobConfig.*
import java.util.*

import static extension org.eclipse.xtext.xtend2.lib.ResourceExtensions.*
import de.hajoeichler.jenkins.jobConfig.impl.ParameterSectionImpl

import org.eclipse.emf.core.*
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.xtend2.lib.ResourceExtensions

class JobConfigGenerator implements IGenerator {

	Config currentConfig

	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		for(config: resource.allContentsIterable.filter(typeof(Config))) {
			if (!config.abstract) {
				currentConfig = config
				fsa.generateFile(config.fileName, config.content)
			}
		}
	}

	def normalize (String s) {
		if (s == null) {
			return s;
		}
		s.replaceJobName(currentConfig).escape()
	}

	def escape (String s) {
		var r = s.replaceAll("&", "&amp;")
		r = r.replaceAll("\"", "&quot;")
		r = r.replaceAll("'", "&apos;")
		r = r.replaceAll(">", "&gt;")
		r = r.replaceAll("<", "&lt;")
	}

	def replaceJobName(String s, Config c) {
		s.replaceAll("@@jobName@@", c.name)
	}

	def fileName(Config c) {
		fqn(c) + "/config.xml"
	}

	def dispatch String fqn(Group g) {
		g.name
	}

	def dispatch String fqn(Config c) {
		if (c.eContainer instanceof Group) {
			fqn(c.eContainer as Group) + c.name
		} else {
			c.name
		}
	}

	def Config getMyConfig(EObject any) {
		if (any instanceof Config) {
			return any as Config
		}
		if (any.eContainer != null) {
			return getMyConfig(any.eContainer)
		}
	}

	def String getGitUrl(Config c) {
		if (c.gitUrl != null) {
			c.gitUrl
		} else if (c.parentConfig != null) {
			getGitUrl(c.parentConfig)
		}
	}

	def OldBuildHandling getAnyOldBuildHandling (Config c) {
		if (c.oldBuildHandling != null) {
			c.oldBuildHandling
		} else if (c.parentConfig != null) {
			getAnyOldBuildHandling(c.parentConfig)
		}
	}

	def List<ParameterSection> getAllParameterSections (Config c) {
		val l = new ArrayList<ParameterSection>()
		if (c.parentConfig != null) {
			l.addAll(getAllParameterSections(c.parentConfig))
		}
		if (c.paramSection != null) {
			l.add(c.paramSection)
		}
		return l
	}

	def Scm getAnyScm(Config c) {
		if (c.scm != null) {
			c.scm
		} else if (c.parentConfig != null) {
			getAnyScm(c.parentConfig)
		}
	}

	def getAllTriggers (Config c, Map<EClass, EObject> m) {
		if (c.trigger != null) {
			for (t : c.trigger.buildtriggers) {
				if (!m.containsKey(t.eClass)) {
					m.put(t.eClass, t)
				}
			}
		}
		if (c.parentConfig != null) {
			getAllTriggers(c.parentConfig, m)
		}
		return m
	}

	def getAllWrappers (Config c, Map<EClass, EObject> m) {
		if (c.wrapper != null) {
			for (w : c.wrapper.wrappers) {
				if (!m.containsKey(w.eClass)) {
					m.put(w.eClass, w)
				}
			}
		}
		if (c.parentConfig != null) {
			getAllWrappers(c.parentConfig, m)
		}
		return m
	}

	def getAllBuilders (Config c, List<EObject> l) {
		if (c.parentConfig != null) {
			getAllBuilders(c.parentConfig, l)
		}
		if (c.buildSection != null) {
			l.addAll(c.buildSection.builds)
		}
		return l
	}

	def getAllPublishers (Config c, Map<EClass, EObject> m) {
		if (c.publisherSection != null) {
			for (p : c.publisherSection.publishers) {
				if (!m.containsKey(p.eClass)) {
					m.put(p.eClass, p)
				}
			}
		}
		if (c.parentConfig != null) {
			getAllPublishers(c.parentConfig, m)
		}
		return m
	}

	def content(Config c) '''
		<?xml version='1.0' encoding='UTF-8'?>
		«IF c.isMatixJob»
		<matrix-project>
		«ELSE»
		<project>
		«ENDIF»
		  <actions/>
		  <description>«c.description»</description>
		  «IF c.getAnyOldBuildHandling != null»
		  «logRotator(c.getAnyOldBuildHandling)»
		  «ENDIF»
		  <keepDependencies>false</keepDependencies>
		  <properties>
		    «gitHub(c)»
		    «FOR ps:c.getAllParameterSections»
		    «parameterSection(ps)»
		    «ENDFOR»
		  </properties>
		  «IF c.getAnyScm == null»
		  <scm class="hudson.scm.NullSCM"/>
		  «ELSE»
		  «scm(c.getAnyScm)»
		  «ENDIF»
		  «IF c.restrictTo == null»
		  <canRoam>true</canRoam>
		  «ELSE»
		  <canRoam>«c.restrictTo»</canRoam>
		  «ENDIF»
		  <disabled>«c.disabled»</disabled>
		  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
		  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
		  «triggers(c)»
		  <concurrentBuild>«c.concurrentBuild»</concurrentBuild>
		  «builders(c)»
		  «publishers(c)»
		  «wrappers(c)»
		«IF c.isMatixJob»
		</matrix-project>
		«ELSE»
		</project>
		«ENDIF»
	'''

	def logRotator(OldBuildHandling obh) '''
		<logRotator>
		  <daysToKeep>«obh.daysToKeep»</daysToKeep>
		  <numToKeep>«obh.maxNumberOfBuilds»</numToKeep>
		  <artifactDaysToKeep>«obh.daysToKeepArtifact»</artifactDaysToKeep>
		  <artifactNumToKeep>«obh.maxNumberOfBuildsWithArtifact»</artifactNumToKeep>
		</logRotator>
	'''

	def gitHub(Config c) '''
		«val gitUrl = getGitUrl(c)»
		«IF gitUrl != null»
		<com.coravy.hudson.plugins.github.GithubProjectProperty>
		  <projectUrl>«gitUrl»</projectUrl>
		</com.coravy.hudson.plugins.github.GithubProjectProperty>
		«ENDIF»
	'''

	def parameterSection(ParameterSection ps) '''
		<hudson.model.ParametersDefinitionProperty>
		  <parameterDefinitions>
		  «FOR p:ps.parameters»
		  «param(p, p.type)»
		  «ENDFOR»
		</parameterDefinitions>
		</hudson.model.ParametersDefinitionProperty>
	'''

	def dispatch param(Parameter p, StringParam s) '''
		<hudson.model.StringParameterDefinition>
		  <name>«p.name»</name>
		  <description>«p.description»</description>
		  <defaultValue>«s.value»</defaultValue>
		</hudson.model.StringParameterDefinition>
	'''

	def dispatch param(Parameter p, BooleanParam b) '''
	'''

	def dispatch param(Parameter p, ChoiceParam c) '''
	'''

	def dispatch scm(ScmGit git) '''
		<scm class="hudson.plugins.git.GitSCM">
		  <configVersion>2</configVersion>
		  <userRemoteConfigs>
		    <hudson.plugins.git.UserRemoteConfig>
		      <name>origin</name>
		      <refspec>+refs/heads/*:refs/remotes/origin/*</refspec>
		      <url>«git.url»</url>
		    </hudson.plugins.git.UserRemoteConfig>
		  </userRemoteConfigs>
		  <branches>
		    <hudson.plugins.git.BranchSpec>
		      «IF git.branch != null»
		      <name>«git.branch»</name>
		      «ELSE»
		      <name>origin/master</name>
		      «ENDIF»
		    </hudson.plugins.git.BranchSpec>
		  </branches>
		  <recursiveSubmodules>false</recursiveSubmodules>
		  <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>
		  <authorOrCommitter>false</authorOrCommitter>
		  <clean>false</clean>
		  «IF git.wipeOutWorkspace»
		  <wipeOutWorkspace>true</wipeOutWorkspace>
		  «ELSE»
		  <wipeOutWorkspace>false</wipeOutWorkspace>
		  «ENDIF»
		  <pruneBranches>false</pruneBranches>
		  <remotePoll>false</remotePoll>
		  <buildChooser class="hudson.plugins.git.util.DefaultBuildChooser"/>
		  <gitTool>Default</gitTool>
		  <submoduleCfg class="list"/>
		  <relativeTargetDir></relativeTargetDir>
		  <excludedRegions>«git.excludedRegions.normalize»</excludedRegions>
		  <excludedUsers></excludedUsers>
		  <gitConfigName></gitConfigName>
		  <gitConfigEmail></gitConfigEmail>
		  <skipTag>false</skipTag>
		  <scmName></scmName>
		</scm>
	'''

	def dispatch scm(ScmCVS cvs) '''
		<scm class="hudson.scm.CVSSCM">
		  <cvsroot>«cvs.root»</cvsroot>
		  <module>«cvs.modules»</module>
		  <canUseUpdate>true</canUseUpdate>
		  <flatten>false</flatten>
		  <isTag>false</isTag>
		  <excludedRegions></excludedRegions>
		</scm>
	'''

	def triggers(Config c) '''
		<triggers class="vector">
		  «val m = new LinkedHashMap<EClass, EObject>()»
		  «FOR t:getAllTriggers(c, m).values»
		  «trigger(t)»
		  «ENDFOR»
		</triggers>
	'''

	def dispatch trigger(TimerTrigger t) '''
		<hudson.triggers.TimerTrigger>
		  <spec>«t.timer»</spec>
		</hudson.triggers.TimerTrigger>
	'''

	def dispatch trigger(PollScmTrigger t) '''
		<hudson.triggers.SCMTrigger>
		  <spec>«t.poll»</spec>
		</hudson.triggers.SCMTrigger>
	'''

	def dispatch trigger(FirstStartTrigger t) '''
		<org.jvnet.hudson.plugins.triggers.startup.HudsonStartupTrigger>
		  <spec></spec>
		</org.jvnet.hudson.plugins.triggers.startup.HudsonStartupTrigger>
	'''

	def wrappers(Config c) '''
		<buildWrappers>
		  «val m = new LinkedHashMap<EClass, EObject>()»
		  «FOR w:getAllWrappers(c, m).values»
		  «wrapper(w)»
		  «ENDFOR»
		</buildWrappers>
	'''

	def dispatch wrapper(Lock l) '''
		<hudson.plugins.locksandlatches.LockWrapper>
		  <locks>
		    <hudson.plugins.locksandlatches.LockWrapper_-LockWaitConfig>
		      <name>«l.lock»</name>
		    </hudson.plugins.locksandlatches.LockWrapper_-LockWaitConfig>
		  </locks>
		</hudson.plugins.locksandlatches.LockWrapper>
	'''

	def dispatch wrapper(Timeout t) '''
		<hudson.plugins.build__timeout.BuildTimeoutWrapper>
		  <timeoutMinutes>«t.t»</timeoutMinutes>
		  <failBuild>true</failBuild>
		</hudson.plugins.build__timeout.BuildTimeoutWrapper>
	'''

	def builders(Config c) '''
		<builders>
		  «val l = new ArrayList<EObject>()»
		  «FOR b:getAllBuilders(c, l)»
		  «build(b)»
		  «ENDFOR»
		</builders>
	'''

	def dispatch build (Maven m) '''
		<hudson.tasks.Maven>
		  <targets>«m.mavenGoals»</targets>
		  <mavenName>«m.version.name»</mavenName>
		  «IF m.mavenPOM != null»
		  <pom>«m.mavenPOM»</pom>
		  «ENDIF»
		  «IF m.mavenProperties != null»
		  <properties>«m.mavenProperties»</properties>
		  «ENDIF»
		  <usePrivateRepository>«m.mavenPrivateRepo»</usePrivateRepository>
		</hudson.tasks.Maven>
	'''

	def dispatch build (Shell s) '''
		<hudson.tasks.Shell>
		  <command>«s.shellScript.normalize»</command>
		</hudson.tasks.Shell>
	'''

	def publishers(Config c) '''
		<publishers>
		  «val m = new LinkedHashMap<EClass, EObject>()»
		  «FOR p:getAllPublishers(c, m).values»
		  «publisher(p)»
		  «ENDFOR»
		</publishers>
	'''

	def getTo(ExtMail em) {
		if (em.mergeWithSuperConfig == true) {
			val pm = getParentExtMail(em)
			if (pm != null) {
				return em.to + ' ' + getTo(pm)
			}
		}
		em.to
	}

	def ExtMail getParentExtMail(ExtMail em) {
		val c = getMyConfig(em)
		if (c.parentConfig != null) {
			if (c.parentConfig.publisherSection != null) {
				for (p : c.parentConfig.publisherSection.publishers) {
					if (p instanceof ExtMail) {
						return p as ExtMail
					}
				}
			}
		}
	}

	def getAllMailTriggers (ExtMail em, Map<String, MailTrigger> m) {
		for (mt : em.mailTrigger) {
			if (!m.containsKey(mt.type)) {
				m.put(mt.type, mt)
			}
		}
		if (em.mergeWithSuperConfig == true) {
			val pm = getParentExtMail(em)
			if (pm != null) {
				getAllMailTriggers(pm, m)
			}
		}
		return m
	}

	def dispatch publisher (ExtMail em) '''
		<hudson.plugins.emailext.ExtendedEmailPublisher>
		  <recipientList>«getTo(em)»</recipientList>
		  <configuredTriggers>
		    «val m = new LinkedHashMap<String, MailTrigger>()»
		    «FOR mt:getAllMailTriggers(em, m).values»
		    «mailTrigger(mt)»
		    «ENDFOR»
		  </configuredTriggers>
		  «IF em.type == null»
		  <contentType>default</contentType>
		  «ELSE»
		  <contentType>«em.type»</contentType>
		  «ENDIF»
		  «IF em.subject == null»
		  <defaultSubject>${DEFAULT_SUBJECT}</defaultSubject>
		  «ELSE»
		  <defaultSubject>«em.subject»</defaultSubject>
		  «ENDIF»
		  «IF em.content == null»
		  <defaultContent>${DEFAULT_CONTENT}</defaultContent>
		  «ELSE»
		  <defaultContent>«em.content»</defaultContent>
		  «ENDIF»
		  <attachmentsPattern></attachmentsPattern>
		</hudson.plugins.emailext.ExtendedEmailPublisher>
	'''

	def mailTrigger(MailTrigger mt) '''
		<hudson.plugins.emailext.plugins.trigger.«mt.type.replace("-", "")»Trigger>
		  <email>
		    «IF mt.to == null»
		    <recipientList>$PROJECT_DEFAULT_RECIPIENTS</recipientList>
		    «ELSE»
		    <recipientList>«mt.to»</recipientList>
		    «ENDIF»
		    «IF mt.subject == null»
		    <subject>$PROJECT_DEFAULT_SUBJECT</subject>
		    «ELSE»
		    <subject>«mt.subject»</subject>
		    «ENDIF»
		    «IF mt.content == null»
		    <body>$PROJECT_DEFAULT_CONTENT</body>
		    «ELSE»
		    <body>«mt.content»</body>
		    «ENDIF»
		    <sendToDevelopers>«mt.toCommiter»</sendToDevelopers>
		    <sendToRequester>«mt.toRequester»</sendToRequester>
		    <includeCulprits>«mt.toCulprits»</includeCulprits>
		    <sendToRecipientList>«mt.toList»</sendToRecipientList>
		  </email>
		</hudson.plugins.emailext.plugins.trigger.«mt.type.replace("-", "")»Trigger>
	'''

	// TODO: claim of tests?
	def dispatch publisher (TestResult t) '''
		<hudson.tasks.junit.JUnitResultArchiver>
		  <testResults>«t.testresults»</testResults>
		  <keepLongStdio>«t.longIO»</keepLongStdio>
		  <testDataPublishers/>
		</hudson.tasks.junit.JUnitResultArchiver>
	'''

	def dispatch publisher (DownStream d) '''
		<hudson.plugins.parameterizedtrigger.BuildTrigger>
		  <configs>
		  «FOR b:d.builds»
		  «downStreamBuild(b)»
		  «ENDFOR»
		  </configs>
		</hudson.plugins.parameterizedtrigger.BuildTrigger>
	'''

	// TODO: <hudson.plugins.git.GitRevisionBuildParameters/>
	def downStreamBuild (DownStreamBuild b) '''
		<hudson.plugins.parameterizedtrigger.BuildTriggerConfig>
		  <configs>
		  «FOR p:b.triggerParams»
		  «triggerParam(p)»
		  «ENDFOR»
		  </configs>
		  <projects>«b.builds»</projects>
		  <condition>«b.condition»</condition>
		  «IF b.triggerParams.empty»
		  <triggerWithNoParameters>true</triggerWithNoParameters>
		  «ENDIF»
		</hudson.plugins.parameterizedtrigger.BuildTriggerConfig>
	'''

	// TODO
	def dispatch triggerParam(CurrentTriggerParams p) '''
	'''

	def dispatch triggerParam(PropertyFileTriggerParams p) '''
		<hudson.plugins.parameterizedtrigger.FileBuildParameters>
		  <propertiesFile>«p.propertyFile»</propertiesFile>
		</hudson.plugins.parameterizedtrigger.FileBuildParameters>
	'''

	def dispatch triggerParam(PredefinedTriggerParams p) '''
		<hudson.plugins.parameterizedtrigger.PredefinedBuildParameters>
		  <properties>«p.predefined»</properties>
		</hudson.plugins.parameterizedtrigger.PredefinedBuildParameters>
	'''

}
