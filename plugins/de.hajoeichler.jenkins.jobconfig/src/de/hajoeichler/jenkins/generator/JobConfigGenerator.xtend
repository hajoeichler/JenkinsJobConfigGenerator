package de.hajoeichler.jenkins.generator

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.generator.IFileSystemAccess
import de.hajoeichler.jenkins.jobConfig.*
import java.util.*

import static extension org.eclipse.xtext.xtend2.lib.ResourceExtensions.*
import de.hajoeichler.jenkins.jobConfig.impl.ParameterSectionImpl
import de.hajoeichler.jenkins.jobConfig.impl.TriggerSectionImpl

import org.eclipse.emf.core.*
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject

class JobConfigGenerator implements IGenerator {

	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		for(config: resource.allContentsIterable.filter(typeof(Config))) {
			fsa.generateFile(config.fileName, config.content)
		}
	}

	def fileName(Config c) {
		c.name
	}

	def OldBuildHandling getAnyOldBuildHandling (Config c) {
		if (c.oldBuildHandling != null) {
			c.oldBuildHandling
		} else if (c.parentConfig != null) (
			getAnyOldBuildHandling(c.parentConfig)
		)
	}

	def List<ParameterSection> getAllParameterSections (Config c) {
		val l = new ArrayList<ParameterSection>();
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
		} else if (c.parentConfig != null) (
			getAnyScm(c.parentConfig)
		)
	}

	def List<TriggerSection> getAllTriggerSections (Config c) {
		val l = new ArrayList<TriggerSection>();
		if (c.trigger != null) {
			l.add(c.trigger)
		}
		if (c.parentConfig != null) {
			l.addAll(getAllTriggerSections(c.parentConfig))
		}
		return l
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
	}

	def List<PublisherSection> getAllPublishers (Config c) {
		val l = new ArrayList<PublisherSection>();
		if (c.publisherSection != null) {
			l.add(c.publisherSection)
		}
		if (c.parentConfig != null) {
			l.addAll(getAllPublishers(c.parentConfig))
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
		  <properties>
		    «IF c.gitUrl != null»
		    «gitHub(c.gitUrl)»
		    «ENDIF»
		    «FOR ps:c.getAllParameterSections»
		    «parameterSection(ps)»
		    «ENDFOR»
		  </properties>
		  «IF c.getAnyScm == null»
		  <scm class="hudson.scm.NullSCM"/>
		  «ELSE»
		  «scm(c.getAnyScm)»
		  «ENDIF»
		  «IF c.trigger != null»
		  «triggerSection(c.trigger)»
		  «ENDIF»
		  «IF c.restrictTo != null»
		  <assignedNode>«c.restrictTo»</assignedNode>
		  <canRoam>false</canRoam>
		  «ELSE»
		  <canRoam>true</canRoam>
		  «ENDIF»
		  <disabled>«c.isDisabled»</disabled>
		  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
		  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
		  <concurrentBuild>«c.concurrentBuild»</concurrentBuild>
		  «wrappers(c)»
		  «builder(c)»
		  «publishers(c)»
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

	def gitHub(String gitUrl) '''
		<com.coravy.hudson.plugins.github.GithubProjectProperty>
		  <projectUrl>«gitUrl»</projectUrl>
		</com.coravy.hudson.plugins.github.GithubProjectProperty>
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
		  <configVersion>1</configVersion>
		  <remoteRepositories>
		    <org.spearce.jgit.transport.RemoteConfig>
		      <string>origin</string>
		      <int>5</int>
		      <string>fetch</string>
		      <string>+refs/heads/*:refs/remotes/origin/*</string>
		      <string>receivepack</string>
		      <string>git-upload-pack</string>
		      <string>uploadpack</string>
		      <string>git-upload-pack</string>
		      <string>url</string>
		      <string>«git.url»</string>
		      <string>tagopt</string>
		      <string></string>
		    </org.spearce.jgit.transport.RemoteConfig>
		  </remoteRepositories>
		  <branches>
		    <hudson.plugins.git.BranchSpec>
		      «IF git.branch != null»
		      <name>«git.branch»</name>
		      «ELSE»
		      <name>origin/master</name>
		      «ENDIF»
		    </hudson.plugins.git.BranchSpec>
		  </branches>
		  <mergeOptions/>
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
		  <buildChooser class="hudson.plugins.git.util.DefaultBuildChooser"/>
		  <gitTool>Default</gitTool>
		  <submoduleCfg class="list"/>
		  <relativeTargetDir></relativeTargetDir>
		  <excludedRegions>«git.excludedRegions»</excludedRegions>
		  <excludedUsers></excludedUsers>
		  <skipTag>false</skipTag>
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

	def triggerSection(TriggerSection ts) '''
		<triggers class="vector">
		«FOR t:ts.buildtriggers»
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
		  «var m = new HashMap<EClass, EObject>()»
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

	def builder(Config c) '''
		<builders>
		  «var l = new ArrayList<EObject>()»
		  «getAllBuilders(c, l)»
		  «FOR b:l»
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
		  <command>«s.shellScript»</command>
		</hudson.tasks.Shell>
	'''

	def publishers(Config c) '''
		<publishers>
		  «var m = new HashMap<EClass, EObject>()»
		  «FOR p:getAllPublishers(c, m).values»
		  «publisher(p)»
		  «ENDFOR»
		</publishers>
	'''

	def dispatch publisher (ExtMail m) '''
		<hudson.plugins.emailext.ExtendedEmailPublisher>
		  <recipientList>«m.to»</recipientList>
		  <configuredTriggers>
		    «FOR mt:m.mailTrigger»
		    «mailTrigger(mt)»
		    «ENDFOR»
		  </configuredTriggers>
		  «IF m.type == null»
		  <contentType>default</contentType>
		  «ELSE»
		  <contentType>«m.type»</contentType>
		  «ENDIF»
		  «IF m.subject == null»
		  <defaultSubject>«m.subject»</defaultSubject>
		  «ELSE»
		  <defaultSubject>${DEFAULT_SUBJECT}</defaultSubject>
		  «ENDIF»
		  «IF m.content == null»
		  <defaultContent>«m.content»</defaultContent>
		  «ELSE»
		  <defaultContent>${DEFAULT_CONTENT}</defaultContent>
		  «ENDIF»
		</hudson.plugins.emailext.ExtendedEmailPublisher>
	'''

	// TODO: different trigger types 
	// <hudson.plugins.emailext.plugins.trigger.UnstableTrigger>
	// <hudson.plugins.emailext.plugins.trigger.FixedTrigger>
	def mailTrigger(MailTrigger mt) '''
		«IF mt.type.equals("Failure")»
		<hudson.plugins.emailext.plugins.trigger.FailureTrigger>
		«ENDIF»
		  <email>
		    <recipientList>«mt.to»</recipientList>
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
		«IF mt.type.equals("Failure")»
		</hudson.plugins.emailext.plugins.trigger.FailureTrigger>
		«ENDIF»
	'''

	// TODO: claim of tests?
	def dispatch publisher (TestResult t) '''
		<hudson.tasks.junit.JUnitResultArchiver>
		  <testResults>«t.testresults»</testResults>
		  <keepLongStdio>«t.longIO»</keepLongStdio>
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
