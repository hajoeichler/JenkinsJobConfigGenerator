package de.hajoeichler.jenkins.generator

import de.hajoeichler.jenkins.jobConfig.Ant
import de.hajoeichler.jenkins.jobConfig.Artifacts
import de.hajoeichler.jenkins.jobConfig.Batch
import de.hajoeichler.jenkins.jobConfig.BooleanParam
import de.hajoeichler.jenkins.jobConfig.ChoiceParam
import de.hajoeichler.jenkins.jobConfig.Claim
import de.hajoeichler.jenkins.jobConfig.Cobertura
import de.hajoeichler.jenkins.jobConfig.Config
import de.hajoeichler.jenkins.jobConfig.CurrentTriggerParams
import de.hajoeichler.jenkins.jobConfig.DownStream
import de.hajoeichler.jenkins.jobConfig.DownStreamBuild
import de.hajoeichler.jenkins.jobConfig.ExclusiveExecution
import de.hajoeichler.jenkins.jobConfig.ExtMail
import de.hajoeichler.jenkins.jobConfig.FirstStartTrigger
import de.hajoeichler.jenkins.jobConfig.GitCommitParam
import de.hajoeichler.jenkins.jobConfig.Group
import de.hajoeichler.jenkins.jobConfig.HTMLPublisher
import de.hajoeichler.jenkins.jobConfig.HipChat
import de.hajoeichler.jenkins.jobConfig.Lock
import de.hajoeichler.jenkins.jobConfig.MailTrigger
import de.hajoeichler.jenkins.jobConfig.MatrixTieParent
import de.hajoeichler.jenkins.jobConfig.Maven
import de.hajoeichler.jenkins.jobConfig.OldBuildHandling
import de.hajoeichler.jenkins.jobConfig.Parameter
import de.hajoeichler.jenkins.jobConfig.PollScmTrigger
import de.hajoeichler.jenkins.jobConfig.PredefinedTriggerParams
import de.hajoeichler.jenkins.jobConfig.PropertyFileTriggerParams
import de.hajoeichler.jenkins.jobConfig.Rcov
import de.hajoeichler.jenkins.jobConfig.Release
import de.hajoeichler.jenkins.jobConfig.Scm
import de.hajoeichler.jenkins.jobConfig.ScmCVS
import de.hajoeichler.jenkins.jobConfig.ScmGit
import de.hajoeichler.jenkins.jobConfig.ScmSVN
import de.hajoeichler.jenkins.jobConfig.Shell
import de.hajoeichler.jenkins.jobConfig.StringParam
import de.hajoeichler.jenkins.jobConfig.SystemGroovy
import de.hajoeichler.jenkins.jobConfig.TestResult
import de.hajoeichler.jenkins.jobConfig.Timeout
import de.hajoeichler.jenkins.jobConfig.TimerTrigger
import de.hajoeichler.jenkins.jobConfig.TriggerBuilderSection
import de.hajoeichler.jenkins.jobConfig.TriggeredBuild
import de.hajoeichler.jenkins.jobConfig.Warnings
import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.List
import java.util.Map
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator

import static extension org.eclipse.xtext.xtend2.lib.ResourceExtensions.*
import de.hajoeichler.jenkins.jobConfig.AnsiColor
import de.hajoeichler.jenkins.jobConfig.GitHubPushTrigger
import de.hajoeichler.jenkins.jobConfig.PlayAutoTestReport
import de.hajoeichler.jenkins.jobConfig.Violations
import de.hajoeichler.jenkins.jobConfig.ViolationsConfig

class JobConfigGenerator implements IGenerator {

	Config currentConfig

	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		println("Processing " + resource.URI)
		for(config: resource.allContentsIterable.filter(typeof(Config))) {
			if (!config.abstract) {
				currentConfig = config
				println("Writing config to " + config.fileName)
				fsa.generateFile(config.fileName, config.content)
			}
		}
	}

	def normalize (String s) {
		if (s == null) {
			return s
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

	def isNotEmpty(String s) {
		s != null && !s.empty
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

	def isMatrixJob(Config c) {
		return !c.matrixes.empty
	}

	def String getGitUrl(Config c) {
		if (c.gitUrl != null) {
			c.gitUrl
		} else if (c.parentConfig != null) {
			getGitUrl(c.parentConfig)
		}
	}

	def String getRestrictTo(Config c) {
		if (c.restrictTo != null) {
			c.restrictTo
		} else if (c.parentConfig != null) {
			getRestrictTo(c.parentConfig)
		}
	}

	def OldBuildHandling getAnyOldBuildHandling (Config c) {
		if (c.oldBuildHandling != null) {
			c.oldBuildHandling
		} else if (c.parentConfig != null) {
			getAnyOldBuildHandling(c.parentConfig)
		}
	}

	def getAllParameters (Config c, Map<String, Parameter> m) {
		if (c.paramSection != null) {
			for (p : c.paramSection.parameters) {
				if (!m.containsKey(p.name)) {
					m.put(p.name, p)
				}
			}
		}
		if (c.parentConfig != null) {
			getAllParameters(c.parentConfig, m)
		}
		return m
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
		�IF c.isMatrixJob�
		<matrix-project>
		�ELSE�
		<project>
		�ENDIF�
		  <actions/>
		  <description>�c.description�</description>
		  �IF c.displayName != null�
		  <displayName>�c.displayName�</displayName>
		  �ENDIF�
		  �IF c.getAnyOldBuildHandling != null�
		  �logRotator(c.getAnyOldBuildHandling)�
		  �ENDIF�
		  <keepDependencies>false</keepDependencies>
		  <properties>
		    �gitHub(c)�
		    �parameters(c)�
		  </properties>
		  �IF c.getAnyScm == null�
		  <scm class="hudson.scm.NullSCM"/>
		  �ELSE�
		  �scm(c.getAnyScm)�
		  �ENDIF�
		  �restrictTo(c)�
		  <disabled>�c.disabled�</disabled>
		  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
		  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
		  �triggers(c)�
		  <concurrentBuild>�c.concurrentBuild�</concurrentBuild>
		  �IF c.isMatrixJob�
		  �matrix(c)�
		  �ENDIF�
		  �builders(c)�
		  �publishers(c)�
		  �wrappers(c)�
		�IF c.isMatrixJob�
		</matrix-project>
		�ELSE�
		</project>
		�ENDIF�
	'''

	def logRotator(OldBuildHandling obh) '''
		<logRotator>
		  �IF obh.daysToKeep > 0�
		  <daysToKeep>�obh.daysToKeep�</daysToKeep>
		  �ENDIF�
		  �IF obh.maxNumberOfBuilds > 0�
		  <numToKeep>�obh.maxNumberOfBuilds�</numToKeep>
		  �ENDIF�
		  �IF obh.daysToKeepArtifact > 0�
		  <artifactDaysToKeep>�obh.daysToKeepArtifact�</artifactDaysToKeep>
		  �ENDIF�
		  �IF obh.maxNumberOfBuildsWithArtifact > 0�
		  <artifactNumToKeep>�obh.maxNumberOfBuildsWithArtifact�</artifactNumToKeep>
		  �ENDIF�
		</logRotator>
	'''

	def gitHub(Config c) '''
		�val gitUrl = getGitUrl(c)�
		�IF gitUrl != null�
		<com.coravy.hudson.plugins.github.GithubProjectProperty>
		  <projectUrl>�gitUrl.normalize�</projectUrl>
		</com.coravy.hudson.plugins.github.GithubProjectProperty>
		�ENDIF�
	'''

	def restrictTo(Config c) '''
		�val r = getRestrictTo(c)�
		�IF r == null�
		<canRoam>true</canRoam>
		�ELSE�
		<assignedNode>�r�</assignedNode>
		<canRoam>false</canRoam>
		�ENDIF�
	'''

	def parameters(Config c) '''
		�val m = new LinkedHashMap<String, Parameter>()�
		�val v = getAllParameters(c, m).values�
		�IF v.empty == false�
		<hudson.model.ParametersDefinitionProperty>
		  <parameterDefinitions>
		    �FOR p:v�
		    �param(p, p.type)�
		    �ENDFOR�
		  </parameterDefinitions>
		</hudson.model.ParametersDefinitionProperty>
		�ENDIF�
	'''

	def dispatch param(Parameter p, StringParam s) '''
		<hudson.model.StringParameterDefinition>
		  <name>�p.name�</name>
		  <description>�p.description�</description>
		  <defaultValue>�s.value.normalize�</defaultValue>
		</hudson.model.StringParameterDefinition>
	'''

	def dispatch param(Parameter p, BooleanParam b) '''
		<hudson.model.BooleanParameterDefinition>
		  <name>�p.name�</name>
		  <description>�p.description�</description>
		  �IF b.checked�
		  <defaultValue>true</defaultValue>
		  �ENDIF�
		  �IF b.notChecked�
		  <defaultValue>false</defaultValue>
		  �ENDIF�
		</hudson.model.BooleanParameterDefinition>
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
		      <url>�git.url.normalize�</url>
		    </hudson.plugins.git.UserRemoteConfig>
		  </userRemoteConfigs>
		  <branches>
		    <hudson.plugins.git.BranchSpec>
		      �IF git.branch != null�
		      <name>�git.branch�</name>
		      �ELSE�
		      <name>origin/master</name>
		      �ENDIF�
		    </hudson.plugins.git.BranchSpec>
		  </branches>
		  <disableSubmodules>false</disableSubmodules>
		  <recursiveSubmodules>false</recursiveSubmodules>
		  <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>
		  <authorOrCommitter>false</authorOrCommitter>
		  <clean>false</clean>
		  �IF git.wipeOutWorkspace�
		  <wipeOutWorkspace>true</wipeOutWorkspace>
		  �ELSE�
		  <wipeOutWorkspace>false</wipeOutWorkspace>
		  �ENDIF�
		  <pruneBranches>false</pruneBranches>
		  <remotePoll>false</remotePoll>
		  <ignoreNotifyCommit>false</ignoreNotifyCommit>
		  <buildChooser class="hudson.plugins.git.util.DefaultBuildChooser"/>
		  <gitTool>Default</gitTool>
		  <submoduleCfg class="list"/>
		  <relativeTargetDir></relativeTargetDir>
		  <reference></reference>
		  <excludedRegions>�git.regions.excludedRegions.normalize�</excludedRegions>
		  <excludedUsers></excludedUsers>
		  <gitConfigName></gitConfigName>
		  <gitConfigEmail></gitConfigEmail>
		  <skipTag>false</skipTag>
		  <includedRegions>�git.regions.includedRegions.normalize�</includedRegions>
		  <scmName></scmName>
		</scm>
	'''

	def dispatch scm(ScmSVN svn) '''
		  <scm class="hudson.scm.SubversionSCM">
		    <locations>
		      <hudson.scm.SubversionSCM_-ModuleLocation>
		        <remote>�svn.url.normalize�</remote>
		        <local>�svn.localDir.normalize�</local>
		      </hudson.scm.SubversionSCM_-ModuleLocation>
		    </locations>
		    <excludedRegions>�svn.regions.excludedRegions.normalize�</excludedRegions>
		    <includedRegions>�svn.regions.includedRegions.normalize�</includedRegions>
		    <excludedUsers></excludedUsers>
		    <excludedRevprop></excludedRevprop>
		    <excludedCommitMessages></excludedCommitMessages>
		    <workspaceUpdater class="hudson.scm.subversion.UpdateUpdater"/>
		  </scm>
	'''

	def dispatch scm(ScmCVS cvs) '''
		<scm class="hudson.scm.CVSSCM">
		  <cvsroot>�cvs.root�</cvsroot>
		  <module>�cvs.modules�</module>
		  <canUseUpdate>false</canUseUpdate>
		  <useHeadIfNotFound>false</useHeadIfNotFound>
		  <flatten>false</flatten>
		  <isTag>false</isTag>
		  <excludedRegions></excludedRegions>
		</scm>
	'''

	def triggers(Config c) '''
		<triggers class="vector">
		  �val m = new LinkedHashMap<EClass, EObject>()�
		  �FOR t:getAllTriggers(c, m).values�
		  �trigger(t)�
		  �ENDFOR�
		</triggers>
	'''

	def dispatch trigger(TimerTrigger t) '''
		<hudson.triggers.TimerTrigger>
		  <spec>�t.timer�</spec>
		</hudson.triggers.TimerTrigger>
	'''

	def dispatch trigger(PollScmTrigger t) '''
		<hudson.triggers.SCMTrigger>
		  <spec>�t.poll�</spec>
		</hudson.triggers.SCMTrigger>
	'''

	def dispatch trigger(FirstStartTrigger t) '''
		<org.jvnet.hudson.plugins.triggers.startup.HudsonStartupTrigger>
		  <spec></spec>
		</org.jvnet.hudson.plugins.triggers.startup.HudsonStartupTrigger>
	'''

	def dispatch trigger(GitHubPushTrigger t) '''
		<com.cloudbees.jenkins.GitHubPushTrigger>
		  <spec></spec>
		</com.cloudbees.jenkins.GitHubPushTrigger>
	'''

	def wrappers(Config c) '''
		<buildWrappers>
		  �val m = new LinkedHashMap<EClass, EObject>()�
		  �FOR w:getAllWrappers(c, m).values�
		  �wrapper(w)�
		  �ENDFOR�
		</buildWrappers>
	'''

	def dispatch wrapper(Lock l) '''
		<hudson.plugins.locksandlatches.LockWrapper>
		  <locks>
		    <hudson.plugins.locksandlatches.LockWrapper_-LockWaitConfig>
		      <name>�l.lock.name�</name>
		    </hudson.plugins.locksandlatches.LockWrapper_-LockWaitConfig>
		  </locks>
		</hudson.plugins.locksandlatches.LockWrapper>
	'''

	def dispatch wrapper(Timeout t) '''
		<hudson.plugins.build__timeout.BuildTimeoutWrapper>
		  <timeoutMinutes>�t.t�</timeoutMinutes>
		  <failBuild>�t.failBuild�</failBuild>
		</hudson.plugins.build__timeout.BuildTimeoutWrapper>
	'''

	def dispatch wrapper(ExclusiveExecution e) '''
		<hudson.plugins.execution.exclusive.ExclusiveBuildWrapper/>
	'''

	def dispatch wrapper(MatrixTieParent m) '''
		<matrixtieparent.BuildWrapperMtp>
		  <labelName>�m.matrixParent�</labelName>
		</matrixtieparent.BuildWrapperMtp>
	'''

	def dispatch wrapper(AnsiColor a) '''
		<hudson.plugins.ansicolor.AnsiColorBuildWrapper/>
	'''

	def dispatch wrapper(Release r) '''
	<hudson.plugins.release.ReleaseWrapper>
	  <releaseVersionTemplate></releaseVersionTemplate>
	  <doNotKeepLog>�r.notKeepForever�</doNotKeepLog>
	  <overrideBuildParameters>false</overrideBuildParameters>
	  <parameterDefinitions>
	    �IF r.paramSection != null�
	    �FOR p:r.paramSection.parameters�
	    �param(p, p.type)�
	    �ENDFOR�
	    �ENDIF�
	  </parameterDefinitions>
	  <preBuildSteps>
	    �IF r.preBuildSection != null�
	    �FOR b:r.preBuildSection.builds�
	    �build(b)�
	    �ENDFOR�
	    �ENDIF�
	  </preBuildSteps>
	  <postBuildSteps>
	    �IF r.finalBuildSection != null�
	    �FOR b:r.finalBuildSection.builds�
	    �build(b)�
	    �ENDFOR�
	    �ENDIF�
	  </postBuildSteps>
	  <postSuccessfulBuildSteps>
	    �IF r.successBuildSection != null�
	    �FOR b:r.successBuildSection.builds�
	    �build(b)�
	    �ENDFOR�
	    �ENDIF�
	  </postSuccessfulBuildSteps>
	  <postFailedBuildSteps>
	    �IF r.failedBuildSection != null�
	    �FOR b:r.failedBuildSection.builds�
	    �build(b)�
	    �ENDFOR�
	    �ENDIF�
	  </postFailedBuildSteps>
	</hudson.plugins.release.ReleaseWrapper>
	'''

	def getMatrixes(Config c, Map<String, List<String>> r) {
		for (m : c.matrixes) {
			for (a : m.matrix.axes) {
				if (!r.containsKey(a.label)) {
					r.put(a.label, new ArrayList())
				}
				val l = r.get(a.label)
				for (v : a.values) {
					l.add(v)
				}
			}
		}
	}

	def matrix(Config c) '''
		�val r = new LinkedHashMap<String, List<String>>()�
		�getMatrixes(c, r)�
		�FOR e:r.entrySet�
		<axes>
		  <hudson.matrix.LabelAxis>
		    <name>�e.key�</name>
		    <values>
		      �FOR v:e.value�
		      <string>�v�</string>
		      �ENDFOR�
		    </values>
		  </hudson.matrix.LabelAxis>
		</axes>
		�ENDFOR�
	'''

	def builders(Config c) '''
		<builders>
		  �val l = new ArrayList<EObject>()�
		  �FOR b:getAllBuilders(c, l)�
		  �build(b)�
		  �ENDFOR�
		</builders>
	'''

	def dispatch build (Maven m) '''
		<hudson.tasks.Maven>
		  <targets>�m.mavenGoals�</targets>
		  <mavenName>�m.version.name�</mavenName>
		  �IF m.mavenPOM != null�
		  <pom>�m.mavenPOM.normalize�</pom>
		  �ENDIF�
		  �IF m.mavenProperties != null�
		  <properties>�m.mavenProperties�</properties>
		  �ENDIF�
		  <usePrivateRepository>�m.mavenPrivateRepo�</usePrivateRepository>
		</hudson.tasks.Maven>
	'''

	def dispatch build (Shell s) '''
		<hudson.tasks.Shell>
		  <command>�s.shellScript.normalize�</command>
		</hudson.tasks.Shell>
	'''

	def dispatch build (Batch b) '''
		<hudson.tasks.BatchFile>
		  <command>�b.batchScript.normalize�</command>
		</hudson.tasks.BatchFile>
	'''

	def dispatch build (Ant a) '''
		<hudson.tasks.Ant>
		  <targets></targets>
		  <antName>�a.version.name�</antName>
		  <buildFile>�a.buildFile�</buildFile>
		</hudson.tasks.Ant>
	'''

	def dispatch build (SystemGroovy sg) '''
		<hudson.plugins.groovy.SystemGroovy>
		  <scriptSource class="hudson.plugins.groovy.StringScriptSource">
		    <command>�sg.groovyScript.normalize�</command>
		  </scriptSource>
		  <bindings></bindings>
		  <classpath></classpath>
		</hudson.plugins.groovy.SystemGroovy>
	'''

	def dispatch build (TriggerBuilderSection tbs) '''
		<hudson.plugins.parameterizedtrigger.TriggerBuilder>
		  <configs>
		    �FOR tb:tbs.triggeredBuilds�
		    �triggeredBuild(tb)�
		    �ENDFOR�
		  </configs>
		</hudson.plugins.parameterizedtrigger.TriggerBuilder>
	'''

	def triggeredBuild(TriggeredBuild tb) '''
		<hudson.plugins.parameterizedtrigger.BlockableBuildTriggerConfig>
		  <configs>
		    �FOR p:tb.triggerParams�
		    �triggerParam(p)�
		    �ENDFOR�
		  </configs>
		  <projects>�tb.builds.fqn�</projects>
		  <condition>ALWAYS</condition>
		  <triggerWithNoParameters>false</triggerWithNoParameters>
		  <block>
		    <buildStepFailureThreshold>
		      <name>UNSTABLE</name>
		      <ordinal>1</ordinal>
		      <color>YELLOW</color>
		    </buildStepFailureThreshold>
		    <unstableThreshold>
		      <name>UNSTABLE</name>
		      <ordinal>1</ordinal>
		      <color>YELLOW</color>
		    </unstableThreshold>
		    <failureThreshold>
		      <name>FAILURE</name>
		      <ordinal>2</ordinal>
		      <color>RED</color>
		    </failureThreshold>
		  </block>
		</hudson.plugins.parameterizedtrigger.BlockableBuildTriggerConfig>
	'''

	def publishers(Config c) '''
		<publishers>
		  �val m = new LinkedHashMap<EClass, EObject>()�
		  �FOR p:getAllPublishers(c, m).values�
		  �publisher(p)�
		  �ENDFOR�
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
		var c = getMyConfig(em)
		while (c.parentConfig != null) {
			if (c.parentConfig.publisherSection != null) {
				for (p : c.parentConfig.publisherSection.publishers) {
					if (p instanceof ExtMail) {
						return p as ExtMail
					}
				}
			}
			c = c.parentConfig
		}
		return null
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
		  <recipientList>�getTo(em)�</recipientList>
		  <configuredTriggers>
		    �val m = new LinkedHashMap<String, MailTrigger>()�
		    �FOR mt:getAllMailTriggers(em, m).values�
		    �mailTrigger(mt)�
		    �ENDFOR�
		  </configuredTriggers>
		  �IF em.type == null�
		  <contentType>default</contentType>
		  �ELSE�
		  <contentType>�em.type�</contentType>
		  �ENDIF�
		  �IF em.subject == null�
		  <defaultSubject>$DEFAULT_SUBJECT</defaultSubject>
		  �ELSE�
		  <defaultSubject>�em.subject�</defaultSubject>
		  �ENDIF�
		  �IF em.content == null�
		  <defaultContent>$DEFAULT_CONTENT</defaultContent>
		  �ELSE�
		  <defaultContent>�em.content�</defaultContent>
		  �ENDIF�
		  <attachmentsPattern></attachmentsPattern>
		</hudson.plugins.emailext.ExtendedEmailPublisher>
	'''

	def mailTrigger(MailTrigger mt) '''
		<hudson.plugins.emailext.plugins.trigger.�mt.type.replace("-", "")�Trigger>
		  <email>
		    �IF mt.to == null�
		    <recipientList></recipientList>
		    �ELSE�
		    <recipientList>�mt.to�</recipientList>
		    �ENDIF�
		    �IF mt.subject == null�
		    <subject>$PROJECT_DEFAULT_SUBJECT</subject>
		    �ELSE�
		    <subject>�mt.subject�</subject>
		    �ENDIF�
		    �IF mt.content == null�
		    <body>$PROJECT_DEFAULT_CONTENT</body>
		    �ELSE�
		    <body>�mt.content�</body>
		    �ENDIF�
		    <sendToDevelopers>�mt.toCommiter�</sendToDevelopers>
		    <sendToRequester>�mt.toRequester�</sendToRequester>
		    <includeCulprits>�mt.toCulprits�</includeCulprits>
		    <sendToRecipientList>�mt.toList�</sendToRecipientList>
		  </email>
		</hudson.plugins.emailext.plugins.trigger.�mt.type.replace("-", "")�Trigger>
	'''

	def dispatch publisher (TestResult t) '''
		�IF isNotEmpty(t.testresults)�
		<hudson.tasks.junit.JUnitResultArchiver>
		  <testResults>�t.testresults�</testResults>
		  <keepLongStdio>�t.longIO�</keepLongStdio>
		  <testDataPublishers>
		  �IF t.claim�
		    <hudson.plugins.claim.ClaimTestDataPublisher/>
		  �ENDIF�
		  </testDataPublishers>
		</hudson.tasks.junit.JUnitResultArchiver>
		�ENDIF�
	'''

	def dispatch publisher (DownStream d) '''
		<hudson.plugins.parameterizedtrigger.BuildTrigger>
		  <configs>
		    �FOR b:d.builds�
		    �downStreamBuild(b)�
		    �ENDFOR�
		  </configs>
		</hudson.plugins.parameterizedtrigger.BuildTrigger>
	'''

	def dispatch publisher (Artifacts a) '''
		<hudson.tasks.ArtifactArchiver>
		  <artifacts>�a.artifacts.normalize�</artifacts>
		  �IF false�
		  <latestOnly>true</latestOnly>
		  �ELSE�
		  <latestOnly>false</latestOnly>
		  �ENDIF�
		</hudson.tasks.ArtifactArchiver>
	'''

	def dispatch publisher(Violations v) '''
		<hudson.plugins.violations.ViolationsPublisher>
		  <config>
		    <suppressions class="tree-set">
		      <no-comparator/>
		    </suppressions>
		    <typeConfigs>
		      <no-comparator/>
		      �FOR vc:v.violations�
		      �violationsConfig(vc)�
		      �ENDFOR�
		    </typeConfigs>
		    <limit>100</limit>
		    <sourcePathPattern></sourcePathPattern>
		    <fauxProjectPath></fauxProjectPath>
		    <encoding>default</encoding>
		  </config>
		</hudson.plugins.violations.ViolationsPublisher>
	'''

	def violationsConfig (ViolationsConfig vc) '''
		<entry>
		  <string>checkstyle</string>
		  <hudson.plugins.violations.TypeConfig>
		    <type>�vc.type�</type>
		    <min>�vc.min�</min>
		    <max>�vc.max�</max>
		    <unstable>�vc.unstable�</unstable>
		    <usePattern>false</usePattern>
		    <pattern>�vc.pattern�</pattern>
		  </hudson.plugins.violations.TypeConfig>
		</entry>
	'''

	def dispatch publisher (HTMLPublisher h) '''
		<htmlpublisher.HtmlPublisher>
		  <reportTargets>
		    <htmlpublisher.HtmlPublisherTarget>
		      <reportName>�h.name�</reportName>
		      <reportDir>�h.dir�</reportDir>
		      <reportFiles>�h.files�</reportFiles>
		      <keepAll>�h.keepPast�</keepAll>
		      <wrapperName>htmlpublisher-wrapper.html</wrapperName>
		    </htmlpublisher.HtmlPublisherTarget>
		  </reportTargets>
		</htmlpublisher.HtmlPublisher>
	'''

	def dispatch publisher (Warnings w) '''
		<hudson.plugins.warnings.WarningsPublisher>
		  <healthy></healthy>
		  <unHealthy></unHealthy>
		  <thresholdLimit>low</thresholdLimit>
		  <pluginName>[WARNINGS] </pluginName>
		  <defaultEncoding></defaultEncoding>
		  <canRunOnFailed>false</canRunOnFailed>
		  <useDeltaValues>false</useDeltaValues>
		  <thresholds>
		    �IF w.unstableTotalAll > 0�
		    <unstableTotalAll>�w.unstableTotalAll�</unstableTotalAll>
		    �ELSE�
		    <unstableTotalAll></unstableTotalAll>
		    �ENDIF�
		    <unstableTotalHigh></unstableTotalHigh>
		    <unstableTotalNormal></unstableTotalNormal>
		    <unstableTotalLow></unstableTotalLow>
		    <unstableNewAll></unstableNewAll>
		    <unstableNewHigh></unstableNewHigh>
		    <unstableNewNormal></unstableNewNormal>
		    <unstableNewLow></unstableNewLow>
		    �IF w.failTotalAll > 0�
		    <failedTotalAll>�w.failTotalAll�</failedTotalAll>
		    �ELSE�
		    <failedTotalAll></failedTotalAll>
		    �ENDIF�
		    <failedTotalHigh></failedTotalHigh>
		    <failedTotalNormal></failedTotalNormal>
		    <failedTotalLow></failedTotalLow>
		    <failedNewAll></failedNewAll>
		    <failedNewHigh></failedNewHigh>
		    <failedNewNormal></failedNewNormal>
		    <failedNewLow></failedNewLow>
		  </thresholds>
		  <shouldDetectModules>false</shouldDetectModules>
		  <dontComputeNew>true</dontComputeNew>
		  <parserConfigurations/>
		  <consoleLogParsers>
		    <string>�w.parser.name�</string>
		  </consoleLogParsers>
		</hudson.plugins.warnings.WarningsPublisher>
	'''

	def dispatch publisher (Claim c) '''
		<hudson.plugins.claim.ClaimPublisher/>
	'''

	def dispatch publisher (HipChat h) '''
		<jenkins.plugins.hipchat.HipChatPublisher>
		  <room>�h.room�</room>
		</jenkins.plugins.hipchat.HipChatPublisher>
	'''

	def dispatch publisher (PlayAutoTestReport p) '''
		<com.gmail.ikeike443.PlayTestResultPublisher/>
	'''

	def dispatch publisher (Cobertura c) '''
		<hudson.plugins.cobertura.CoberturaPublisher>
		  <coberturaReportFile>�c.xmlreport�</coberturaReportFile>
		  <onlyStable>�c.onlyStable�</onlyStable>
		  <healthyTarget>
		    <targets class="enum-map" enum-type="hudson.plugins.cobertura.targets.CoverageMetric">
		      <entry>
		        <hudson.plugins.cobertura.targets.CoverageMetric>LINE</hudson.plugins.cobertura.targets.CoverageMetric>
		        <int>80</int>
		      </entry>
		    </targets>
		  </healthyTarget>
		  <unhealthyTarget>
		    <targets class="enum-map" enum-type="hudson.plugins.cobertura.targets.CoverageMetric">
		      <entry>
		        <hudson.plugins.cobertura.targets.CoverageMetric>LINE</hudson.plugins.cobertura.targets.CoverageMetric>
		        <int>0</int>
		      </entry>
		    </targets>
		  </unhealthyTarget>
		  <failingTarget>
		    <targets class="enum-map" enum-type="hudson.plugins.cobertura.targets.CoverageMetric">
		      <entry>
		        <hudson.plugins.cobertura.targets.CoverageMetric>LINE</hudson.plugins.cobertura.targets.CoverageMetric>
		        <int>0</int>
		      </entry>
		    </targets>
		  </failingTarget>
		  <sourceEncoding>UTF_8</sourceEncoding>
		</hudson.plugins.cobertura.CoberturaPublisher>
	'''

	def dispatch publisher (Rcov r) '''
		<hudson.plugins.rubyMetrics.rcov.RcovPublisher>
		  <reportDir>�r.reportDir�</reportDir>
		  <targets>
		    <hudson.plugins.rubyMetrics.rcov.model.MetricTarget>
		      <metric>TOTAL_COVERAGE</metric>
		      <healthy>80</healthy>
		      <unhealthy>0</unhealthy>
		      <unstable>0</unstable>
		    </hudson.plugins.rubyMetrics.rcov.model.MetricTarget>
		    <hudson.plugins.rubyMetrics.rcov.model.MetricTarget>
		      <metric>CODE_COVERAGE</metric>
		      <healthy>80</healthy>
		      <unhealthy>0</unhealthy>
		      <unstable>0</unstable>
		    </hudson.plugins.rubyMetrics.rcov.model.MetricTarget>
		  </targets>
		</hudson.plugins.rubyMetrics.rcov.RcovPublisher>
	'''

	def getListOfFqNames(List<Config> builds) {
		var s = ""
		var first = true
		for (c : builds) {
			if (first) {
				first = false
			} else {
				s = s + ""
			}
			s = s + c.fqn
		}
		return s
	}

	def translateCondition(String c) {
		if ("Stable".equals(c)) {
			return "SUCCESS"
		}
		if ("Unstable".equals(c)) {
			return "UNSTABLE"
		}
		if ("Not-Failed".equals(c)) {
			return "UNSTABLE_OR_BETTER"
		}
		if ("Failed".equals(c)) {
			return "FAILED"
		}
		if ("Complete".equals(c)) {
			return "ALWAYS"
		}
	}

	def downStreamBuild (DownStreamBuild b) '''
		<hudson.plugins.parameterizedtrigger.BuildTriggerConfig>
		  �IF b.triggerParams.empty�
		  <configs class="java.util.Collections$EmptyList"/>
		  <triggerWithNoParameters>true</triggerWithNoParameters>
		  �ELSE�
		  <configs>
		    �FOR p:b.triggerParams�
		    �triggerParam(p)�
		    �ENDFOR�
		  </configs>
		  �ENDIF�
		  <projects>�b.builds.fqn�</projects>
		  <condition>�b.condition.translateCondition�</condition>
		</hudson.plugins.parameterizedtrigger.BuildTriggerConfig>
	'''

	// TODO
	def dispatch triggerParam(CurrentTriggerParams p) '''
	'''

	def dispatch triggerParam(GitCommitParam p) '''
		<hudson.plugins.git.GitRevisionBuildParameters/>
	'''

	def dispatch triggerParam(PropertyFileTriggerParams p) '''
		<hudson.plugins.parameterizedtrigger.FileBuildParameters>
		  <propertiesFile>�p.propertyFile�</propertiesFile>
		</hudson.plugins.parameterizedtrigger.FileBuildParameters>
	'''

	def dispatch triggerParam(PredefinedTriggerParams p) '''
		<hudson.plugins.parameterizedtrigger.PredefinedBuildParameters>
		  <properties>�p.predefined�</properties>
		</hudson.plugins.parameterizedtrigger.PredefinedBuildParameters>
	'''
}