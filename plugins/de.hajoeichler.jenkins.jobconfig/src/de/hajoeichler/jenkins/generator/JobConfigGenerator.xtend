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
import de.hajoeichler.jenkins.jobConfig.JaCoCo
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
import de.hajoeichler.jenkins.jobConfig.Gatling
import de.hajoeichler.jenkins.jobConfig.MailConfig
import de.hajoeichler.jenkins.jobConfig.GitPublisher
import de.hajoeichler.jenkins.jobConfig.Checkstyle
import de.hajoeichler.jenkins.jobConfig.FindBugs
import de.hajoeichler.jenkins.jobConfig.PMD

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
		ï¿½IF c.isMatrixJobï¿½
		<matrix-project>
		ï¿½ELSEï¿½
		<project>
		ï¿½ENDIFï¿½
		  <actions/>
		  <description>«c.description»</description>
		  ï¿½IF c.displayName != nullï¿½
		  <displayName>«c.displayName»</displayName>
		  ï¿½ENDIFï¿½
		  ï¿½IF c.getAnyOldBuildHandling != nullï¿½
		  ï¿½logRotator(c.getAnyOldBuildHandling)ï¿½
		  ï¿½ENDIFï¿½
		  <keepDependencies>false</keepDependencies>
		  <properties>
		    ï¿½gitHub(c)ï¿½
		    ï¿½parameters(c)ï¿½
		  </properties>
		  ï¿½IF c.getAnyScm == nullï¿½
		  <scm class="hudson.scm.NullSCM"/>
		  ï¿½ELSEï¿½
		  ï¿½scm(c.getAnyScm)ï¿½
		  ï¿½ENDIFï¿½
		  ï¿½restrictTo(c)ï¿½
		  <disabled>«c.disabled»</disabled>
		  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
		  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
		  ï¿½triggers(c)ï¿½
		  <concurrentBuild>«c.concurrentBuild»</concurrentBuild>
		  ï¿½IF c.isMatrixJobï¿½
		  ï¿½matrix(c)ï¿½
		  ï¿½ENDIFï¿½
		  ï¿½builders(c)ï¿½
		  ï¿½publishers(c)ï¿½
		  ï¿½wrappers(c)ï¿½
		ï¿½IF c.isMatrixJobï¿½
		</matrix-project>
		ï¿½ELSEï¿½
		</project>
		ï¿½ENDIFï¿½
	'''

	def logRotator(OldBuildHandling obh) '''
		<logRotator>
		  ï¿½IF obh.daysToKeep > 0ï¿½
		  <daysToKeep>«obh.daysToKeep»</daysToKeep>
		  ï¿½ENDIFï¿½
		  ï¿½IF obh.maxNumberOfBuilds > 0ï¿½
		  <numToKeep>«obh.maxNumberOfBuilds»</numToKeep>
		  ï¿½ENDIFï¿½
		  ï¿½IF obh.daysToKeepArtifact > 0ï¿½
		  <artifactDaysToKeep>«obh.daysToKeepArtifact»</artifactDaysToKeep>
		  ï¿½ENDIFï¿½
		  ï¿½IF obh.maxNumberOfBuildsWithArtifact > 0ï¿½
		  <artifactNumToKeep>«obh.maxNumberOfBuildsWithArtifact»</artifactNumToKeep>
		  ï¿½ENDIFï¿½
		</logRotator>
	'''

	def gitHub(Config c) '''
		ï¿½val gitUrl = getGitUrl(c)ï¿½
		ï¿½IF gitUrl != nullï¿½
		<com.coravy.hudson.plugins.github.GithubProjectProperty>
		  <projectUrl>«gitUrl.normalize»</projectUrl>
		</com.coravy.hudson.plugins.github.GithubProjectProperty>
		ï¿½ENDIFï¿½
	'''

	def restrictTo(Config c) '''
		ï¿½val r = getRestrictTo(c)ï¿½
		ï¿½IF r == nullï¿½
		<canRoam>true</canRoam>
		ï¿½ELSEï¿½
		<assignedNode>«r»</assignedNode>
		<canRoam>false</canRoam>
		ï¿½ENDIFï¿½
	'''

	def parameters(Config c) '''
		ï¿½val m = new LinkedHashMap<String, Parameter>()ï¿½
		ï¿½val v = getAllParameters(c, m).valuesï¿½
		ï¿½IF v.empty == falseï¿½
		<hudson.model.ParametersDefinitionProperty>
		  <parameterDefinitions>
		    ï¿½FOR p:vï¿½
		    ï¿½param(p, p.type)ï¿½
		    ï¿½ENDFORï¿½
		  </parameterDefinitions>
		</hudson.model.ParametersDefinitionProperty>
		ï¿½ENDIFï¿½
	'''

	def dispatch param(Parameter p, StringParam s) '''
		<hudson.model.StringParameterDefinition>
		  <name>«p.name»</name>
		  <description>«p.description»</description>
		  <defaultValue>«s.value.normalize»</defaultValue>
		</hudson.model.StringParameterDefinition>
	'''

	def dispatch param(Parameter p, BooleanParam b) '''
		<hudson.model.BooleanParameterDefinition>
		  <name>«p.name»</name>
		  <description>«p.description»</description>
		  ï¿½IF b.checkedï¿½
		  <defaultValue>true</defaultValue>
		  ï¿½ENDIFï¿½
		  ï¿½IF b.notCheckedï¿½
		  <defaultValue>false</defaultValue>
		  ï¿½ENDIFï¿½
		</hudson.model.BooleanParameterDefinition>
	'''

	def dispatch param(Parameter p, ChoiceParam c) '''
		<hudson.model.ChoiceParameterDefinition>
		  <name>«p.name»</name>
		  <description>«p.description»</description>
		  <choices class="java.util.Arrays$ArrayList">
		    <a class="string-array">
		    ï¿½FOR s:c.choices.split("\n")ï¿½
		      <string>«s»</string>
		    ï¿½ENDFORï¿½
		    </a>
		  </choices>
		</hudson.model.ChoiceParameterDefinition>
	'''

	def dispatch scm(ScmGit git) '''
		<scm class="hudson.plugins.git.GitSCM">
		  <configVersion>2</configVersion>
		  <userRemoteConfigs>
		    <hudson.plugins.git.UserRemoteConfig>
		      <name>origin</name>
		      <refspec>+refs/heads/*:refs/remotes/origin/*</refspec>
		      <url>«git.url.normalize»</url>
		    </hudson.plugins.git.UserRemoteConfig>
		  </userRemoteConfigs>
		  <branches>
		    <hudson.plugins.git.BranchSpec>
		      ï¿½IF git.branch != nullï¿½
		      <name>«git.branch»</name>
		      ï¿½ELSEï¿½
		      <name>origin/master</name>
		      ï¿½ENDIFï¿½
		    </hudson.plugins.git.BranchSpec>
		  </branches>
		  <disableSubmodules>false</disableSubmodules>
		  <recursiveSubmodules>false</recursiveSubmodules>
		  <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>
		  <authorOrCommitter>false</authorOrCommitter>
		  <clean>false</clean>
		  ï¿½IF git.wipeOutWorkspaceï¿½
		  <wipeOutWorkspace>true</wipeOutWorkspace>
		  ï¿½ELSEï¿½
		  <wipeOutWorkspace>false</wipeOutWorkspace>
		  ï¿½ENDIFï¿½
		  <pruneBranches>false</pruneBranches>
		  <remotePoll>false</remotePoll>
		  <ignoreNotifyCommit>false</ignoreNotifyCommit>
		  <buildChooser class="hudson.plugins.git.util.DefaultBuildChooser"/>
		  <gitTool>Default</gitTool>
		  <submoduleCfg class="list"/>
		  <relativeTargetDir></relativeTargetDir>
		  <reference></reference>
		  ï¿½IF git.regions != nullï¿½
		  <excludedRegions>«git.regions.excludedRegions.normalize»</excludedRegions>
		  ï¿½ELSEï¿½
		  <excludedRegions></excludedRegions>
		  ï¿½ENDIFï¿½
		  <excludedUsers></excludedUsers>
		  <gitConfigName></gitConfigName>
		  <gitConfigEmail></gitConfigEmail>
		  <skipTag>false</skipTag>
		  ï¿½IF git.regions != nullï¿½
		  <includedRegions>«git.regions.includedRegions.normalize»</includedRegions>
		  ï¿½ELSEï¿½
		  <includedRegions></includedRegions>
		  ï¿½ENDIFï¿½
		  <scmName></scmName>
		</scm>
	'''

	def dispatch scm(ScmSVN svn) '''
		  <scm class="hudson.scm.SubversionSCM">
		    <locations>
		      <hudson.scm.SubversionSCM_-ModuleLocation>
		        <remote>«svn.url.normalize»</remote>
		        <local>«svn.localDir.normalize»</local>
		      </hudson.scm.SubversionSCM_-ModuleLocation>
		    </locations>
		    ï¿½IF svn.regions != nullï¿½
		    <excludedRegions>«svn.regions.excludedRegions.normalize»</excludedRegions>
		    <includedRegions>«svn.regions.includedRegions.normalize»</includedRegions>
		    ï¿½ELSEï¿½
		    <excludedRegions></excludedRegions>
		    <includedRegions></includedRegions>
		    ï¿½ENDIFï¿½
		    <excludedUsers></excludedUsers>
		    <excludedRevprop></excludedRevprop>
		    <excludedCommitMessages></excludedCommitMessages>
		    <workspaceUpdater class="hudson.scm.subversion.UpdateUpdater"/>
		  </scm>
	'''

	def dispatch scm(ScmCVS cvs) '''
		<scm class="hudson.scm.CVSSCM">
		  <cvsroot>«cvs.root»</cvsroot>
		  <module>«cvs.modules»</module>
		  <canUseUpdate>false</canUseUpdate>
		  <useHeadIfNotFound>false</useHeadIfNotFound>
		  <flatten>false</flatten>
		  <isTag>false</isTag>
		  <excludedRegions></excludedRegions>
		</scm>
	'''

	def triggers(Config c) '''
		<triggers class="vector">
		  ï¿½val m = new LinkedHashMap<EClass, EObject>()ï¿½
		  ï¿½FOR t:getAllTriggers(c, m).valuesï¿½
		  ï¿½trigger(t)ï¿½
		  ï¿½ENDFORï¿½
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

	def dispatch trigger(GitHubPushTrigger t) '''
		<com.cloudbees.jenkins.GitHubPushTrigger>
		  <spec></spec>
		</com.cloudbees.jenkins.GitHubPushTrigger>
	'''

	def wrappers(Config c) '''
		<buildWrappers>
		  ï¿½val m = new LinkedHashMap<EClass, EObject>()ï¿½
		  ï¿½FOR w:getAllWrappers(c, m).valuesï¿½
		  ï¿½wrapper(w)ï¿½
		  ï¿½ENDFORï¿½
		</buildWrappers>
	'''

	def dispatch wrapper(Lock l) '''
		<hudson.plugins.locksandlatches.LockWrapper>
		  <locks>
		    <hudson.plugins.locksandlatches.LockWrapper_-LockWaitConfig>
		      <name>«l.lock.name»</name>
		    </hudson.plugins.locksandlatches.LockWrapper_-LockWaitConfig>
		  </locks>
		</hudson.plugins.locksandlatches.LockWrapper>
	'''

	def dispatch wrapper(Timeout t) '''
		<hudson.plugins.build__timeout.BuildTimeoutWrapper>
		  <timeoutMinutes>«t.t»</timeoutMinutes>
		  <failBuild>«t.failBuild»</failBuild>
		</hudson.plugins.build__timeout.BuildTimeoutWrapper>
	'''

	def dispatch wrapper(ExclusiveExecution e) '''
		<hudson.plugins.execution.exclusive.ExclusiveBuildWrapper/>
	'''

	def dispatch wrapper(MatrixTieParent m) '''
		<matrixtieparent.BuildWrapperMtp>
		  <labelName>«m.matrixParent»</labelName>
		</matrixtieparent.BuildWrapperMtp>
	'''

	def dispatch wrapper(AnsiColor a) '''
		<hudson.plugins.ansicolor.AnsiColorBuildWrapper/>
	'''

	def dispatch wrapper(Release r) '''
	<hudson.plugins.release.ReleaseWrapper>
	  <releaseVersionTemplate></releaseVersionTemplate>
	  <doNotKeepLog>«r.notKeepForever»</doNotKeepLog>
	  <overrideBuildParameters>false</overrideBuildParameters>
	  <parameterDefinitions>
	    ï¿½IF r.paramSection != nullï¿½
	    ï¿½FOR p:r.paramSection.parametersï¿½
	    ï¿½param(p, p.type)ï¿½
	    ï¿½ENDFORï¿½
	    ï¿½ENDIFï¿½
	  </parameterDefinitions>
	  <preBuildSteps>
	    ï¿½IF r.preBuildSection != nullï¿½
	    ï¿½FOR b:r.preBuildSection.buildsï¿½
	    ï¿½build(b)ï¿½
	    ï¿½ENDFORï¿½
	    ï¿½ENDIFï¿½
	  </preBuildSteps>
	  <postBuildSteps>
	    ï¿½IF r.finalBuildSection != nullï¿½
	    ï¿½FOR b:r.finalBuildSection.buildsï¿½
	    ï¿½build(b)ï¿½
	    ï¿½ENDFORï¿½
	    ï¿½ENDIFï¿½
	  </postBuildSteps>
	  <postSuccessfulBuildSteps>
	    ï¿½IF r.successBuildSection != nullï¿½
	    ï¿½FOR b:r.successBuildSection.buildsï¿½
	    ï¿½build(b)ï¿½
	    ï¿½ENDFORï¿½
	    ï¿½ENDIFï¿½
	  </postSuccessfulBuildSteps>
	  <postFailedBuildSteps>
	    ï¿½IF r.failedBuildSection != nullï¿½
	    ï¿½FOR b:r.failedBuildSection.buildsï¿½
	    ï¿½build(b)ï¿½
	    ï¿½ENDFORï¿½
	    ï¿½ENDIFï¿½
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
		ï¿½val r = new LinkedHashMap<String, List<String>>()ï¿½
		ï¿½getMatrixes(c, r)ï¿½
		ï¿½FOR e:r.entrySetï¿½
		<axes>
		  <hudson.matrix.LabelAxis>
		    <name>«e.key»</name>
		    <values>
		      ï¿½FOR v:e.valueï¿½
		      <string>«v»</string>
		      ï¿½ENDFORï¿½
		    </values>
		  </hudson.matrix.LabelAxis>
		</axes>
		ï¿½ENDFORï¿½
	'''

	def builders(Config c) '''
		<builders>
		  ï¿½val l = new ArrayList<EObject>()ï¿½
		  ï¿½FOR b:getAllBuilders(c, l)ï¿½
		  ï¿½build(b)ï¿½
		  ï¿½ENDFORï¿½
		</builders>
	'''

	def dispatch build (Maven m) '''
		<hudson.tasks.Maven>
		  <targets>«m.mavenGoals»</targets>
		  <mavenName>«m.version.name»</mavenName>
		  ï¿½IF m.mavenPOM != nullï¿½
		  <pom>«m.mavenPOM.normalize»</pom>
		  ï¿½ENDIFï¿½
		  ï¿½IF m.mavenProperties != nullï¿½
		  <properties>«m.mavenProperties»</properties>
		  ï¿½ENDIFï¿½
		  <usePrivateRepository>«m.mavenPrivateRepo»</usePrivateRepository>
		</hudson.tasks.Maven>
	'''

	def dispatch build (Shell s) '''
		<hudson.tasks.Shell>
		  <command>«s.shellScript.normalize»</command>
		</hudson.tasks.Shell>
	'''

	def dispatch build (Batch b) '''
		<hudson.tasks.BatchFile>
		  <command>«b.batchScript.normalize»</command>
		</hudson.tasks.BatchFile>
	'''

	def dispatch build (Ant a) '''
		<hudson.tasks.Ant>
		  <targets></targets>
		  <antName>«a.version.name»</antName>
		  <buildFile>«a.buildFile»</buildFile>
		</hudson.tasks.Ant>
	'''

	def dispatch build (SystemGroovy sg) '''
		<hudson.plugins.groovy.SystemGroovy>
		  <scriptSource class="hudson.plugins.groovy.StringScriptSource">
		    <command>«sg.groovyScript.normalize»</command>
		  </scriptSource>
		  <bindings></bindings>
		  <classpath></classpath>
		</hudson.plugins.groovy.SystemGroovy>
	'''

	def dispatch build (TriggerBuilderSection tbs) '''
		<hudson.plugins.parameterizedtrigger.TriggerBuilder>
		  <configs>
		    ï¿½FOR tb:tbs.triggeredBuildsï¿½
		    ï¿½triggeredBuild(tb)ï¿½
		    ï¿½ENDFORï¿½
		  </configs>
		</hudson.plugins.parameterizedtrigger.TriggerBuilder>
	'''

	def triggeredBuild(TriggeredBuild tb) '''
		<hudson.plugins.parameterizedtrigger.BlockableBuildTriggerConfig>
		  <configs>
		    ï¿½FOR p:tb.triggerParamsï¿½
		    ï¿½triggerParam(p)ï¿½
		    ï¿½ENDFORï¿½
		  </configs>
		  <projects>«tb.builds.fqn»</projects>
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
		  ï¿½val m = new LinkedHashMap<EClass, EObject>()ï¿½
		  ï¿½FOR p:getAllPublishers(c, m).valuesï¿½
		  ï¿½publisher(p)ï¿½
		  ï¿½ENDFORï¿½
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

	def String getSubject(ExtMail em) {
		if (em.mailConfig != null && em.mailConfig.subject != null) {
			return em.mailConfig.subject
		}
		if (em.mergeWithSuperConfig == true) {
			val pm = getParentExtMail(em)
			if (pm != null) {
			  return getSubject(pm)
			}
		}
	}

	def String getContent(ExtMail em) {
		if (em.mailConfig != null && em.mailConfig.content != null) {
			return em.mailConfig.content
		}
		if (em.mergeWithSuperConfig == true) {
			val pm = getParentExtMail(em)
			if (pm != null) {
			  return getContent(pm)
			}
		}
	}

	def String getAttachments(ExtMail em) {
		if (em.mailConfig != null && em.mailConfig.attachments != null) {
			return em.mailConfig.attachments
		}
		if (em.mergeWithSuperConfig == true) {
			val pm = getParentExtMail(em)
			if (pm != null) {
			  return getAttachments(pm)
			}
		}
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
		  <recipientList>«getTo(em)»</recipientList>
		  <configuredTriggers>
		    ï¿½val m = new LinkedHashMap<String, MailTrigger>()ï¿½
		    ï¿½FOR mt:getAllMailTriggers(em, m).valuesï¿½
		    ï¿½mailTrigger(mt)ï¿½
		    ï¿½ENDFORï¿½
		  </configuredTriggers>
		  ï¿½IF em.type == nullï¿½
		  <contentType>default</contentType>
		  ï¿½ELSEï¿½
		  <contentType>«em.type»</contentType>
		  ï¿½ENDIFï¿½
		  ï¿½val subject = getSubject(em)ï¿½
		  ï¿½IF subject == nullï¿½
		  <defaultSubject>$DEFAULT_SUBJECT</defaultSubject>
		  ï¿½ELSEï¿½
		  <defaultSubject>«subject»</defaultSubject>
		  ï¿½ENDIFï¿½
		  ï¿½val content = getContent(em)ï¿½
		  ï¿½IF content == nullï¿½
		  <defaultContent>$DEFAULT_CONTENT</defaultContent>
		  ï¿½ELSEï¿½
		  <defaultContent>«content»</defaultContent>
		  ï¿½ENDIFï¿½
		  <attachmentsPattern>«getAttachments(em)»</attachmentsPattern>
		  ï¿½IF em.mailConfig != nullï¿½
		  ï¿½mailConfig(em.mailConfig)ï¿½
		  ï¿½ENDIFï¿½
		</hudson.plugins.emailext.ExtendedEmailPublisher>
	'''

	def mailTrigger(MailTrigger mt) '''
		<hudson.plugins.emailext.plugins.trigger.ï¿½mt.type.replace("-", "")ï¿½Trigger>
		  <email>
		    ï¿½IF mt.to == nullï¿½
		    <recipientList></recipientList>
		    ï¿½ELSEï¿½
		    <recipientList>«mt.to»</recipientList>
		    ï¿½ENDIFï¿½
		    ï¿½IF mt.mailConfig != null && mt.mailConfig.subject == nullï¿½
		    <subject>$PROJECT_DEFAULT_SUBJECT</subject>
		    ï¿½ELSEï¿½
		    <subject>«mt.mailConfig.subject»</subject>
		    ï¿½ENDIFï¿½
		    ï¿½IF mt.mailConfig != null && mt.mailConfig.content == nullï¿½
		    <body>$PROJECT_DEFAULT_CONTENT</body>
		    ï¿½ELSEï¿½
		    <body>«mt.mailConfig.content»</body>
		    ï¿½ENDIFï¿½
		    <sendToDevelopers>«mt.toCommiter»</sendToDevelopers>
		    <sendToRequester>«mt.toRequester»</sendToRequester>
		    <includeCulprits>«mt.toCulprits»</includeCulprits>
		    <sendToRecipientList>«mt.toList»</sendToRecipientList>
		    ï¿½IF mt.mailConfig != nullï¿½
		    <attachmentsPattern>«mt.mailConfig.attachments»</attachmentsPattern>
		    ï¿½mailConfig(mt.mailConfig)ï¿½
		    ï¿½ENDIFï¿½
		  </email>
		</hudson.plugins.emailext.plugins.trigger.ï¿½mt.type.replace("-", "")ï¿½Trigger>
	'''

	def mailConfig(MailConfig mc) '''
		<attachBuildLog>«mc.attachBuildLog»</attachBuildLog>
	'''

	def dispatch publisher (TestResult t) '''
		ï¿½IF isNotEmpty(t.testresults)ï¿½
		<hudson.tasks.junit.JUnitResultArchiver>
		  <testResults>«t.testresults.normalize»</testResults>
		  <keepLongStdio>«t.longIO»</keepLongStdio>
		  <testDataPublishers>
		  ï¿½IF t.claimï¿½
		    <hudson.plugins.claim.ClaimTestDataPublisher/>
		  ï¿½ENDIFï¿½
		  </testDataPublishers>
		</hudson.tasks.junit.JUnitResultArchiver>
		ï¿½ENDIFï¿½
	'''

	def dispatch publisher (DownStream d) '''
		<hudson.plugins.parameterizedtrigger.BuildTrigger>
		  <configs>
		    ï¿½FOR b:d.buildsï¿½
		    ï¿½downStreamBuild(b)ï¿½
		    ï¿½ENDFORï¿½
		  </configs>
		</hudson.plugins.parameterizedtrigger.BuildTrigger>
	'''

	def dispatch publisher (Artifacts a) '''
		<hudson.tasks.ArtifactArchiver>
		  <artifacts>«a.artifacts.normalize»</artifacts>
		  ï¿½IF falseï¿½
		  <latestOnly>true</latestOnly>
		  ï¿½ELSEï¿½
		  <latestOnly>false</latestOnly>
		  ï¿½ENDIFï¿½
		</hudson.tasks.ArtifactArchiver>
	'''

	def dispatch publisher(GitPublisher g) '''
		<hudson.plugins.git.GitPublisher>
		  <configVersion>2</configVersion>
		  <pushMerge>false</pushMerge>
		  <pushOnlyIfSuccess>«g.onlyOnSuccess»</pushOnlyIfSuccess>
		  <branchesToPush>
		    <hudson.plugins.git.GitPublisher_-BranchToPush>
		      <targetRepoName>«g.origin»</targetRepoName>
		      <branchName>«g.branch»</branchName>
		    </hudson.plugins.git.GitPublisher_-BranchToPush>
		  </branchesToPush>
		</hudson.plugins.git.GitPublisher>
	'''

	def dispatch publisher(Gatling g) '''
		<com.excilys.ebi.gatling.jenkins.GatlingPublisher plugin="gatling@1.0.0">
		  <simulation>
		    <name>«g.resultprefix.normalize»</name>
		  </simulation>
		</com.excilys.ebi.gatling.jenkins.GatlingPublisher>
	'''

	def dispatch publisher(Violations v) '''
		<hudson.plugins.violations.ViolationsPublisher>
		  <config>
		    <suppressions class="tree-set">
		      <no-comparator/>
		    </suppressions>
		    <typeConfigs>
		      <no-comparator/>
		      ï¿½FOR vc:v.violationsï¿½
		      ï¿½violationsConfig(vc)ï¿½
		      ï¿½ENDFORï¿½
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
		    <type>«vc.type»</type>
		    <min>«vc.min»</min>
		    <max>«vc.max»</max>
		    <unstable>«vc.unstable»</unstable>
		    <usePattern>false</usePattern>
		    <pattern>«vc.pattern»</pattern>
		  </hudson.plugins.violations.TypeConfig>
		</entry>
	'''

	def dispatch publisher (HTMLPublisher h) '''
		<htmlpublisher.HtmlPublisher>
		  <reportTargets>
		    <htmlpublisher.HtmlPublisherTarget>
		      <reportName>«h.name»</reportName>
		      <reportDir>«h.dir»</reportDir>
		      <reportFiles>«h.files»</reportFiles>
		      <keepAll>«h.keepPast»</keepAll>
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
		    ï¿½IF w.unstableTotalAll > 0ï¿½
		    <unstableTotalAll>«w.unstableTotalAll»</unstableTotalAll>
		    ï¿½ELSEï¿½
		    <unstableTotalAll></unstableTotalAll>
		    ï¿½ENDIFï¿½
		    <unstableTotalHigh></unstableTotalHigh>
		    <unstableTotalNormal></unstableTotalNormal>
		    <unstableTotalLow></unstableTotalLow>
		    <unstableNewAll></unstableNewAll>
		    <unstableNewHigh></unstableNewHigh>
		    <unstableNewNormal></unstableNewNormal>
		    <unstableNewLow></unstableNewLow>
		    ï¿½IF w.failTotalAll > 0ï¿½
		    <failedTotalAll>«w.failTotalAll»</failedTotalAll>
		    ï¿½ELSEï¿½
		    <failedTotalAll></failedTotalAll>
		    ï¿½ENDIFï¿½
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
		    <string>«w.parser.name»</string>
		  </consoleLogParsers>
		</hudson.plugins.warnings.WarningsPublisher>
	'''

	def dispatch publisher (Claim c) '''
		<hudson.plugins.claim.ClaimPublisher/>
	'''

	def dispatch publisher (HipChat h) '''
		<jenkins.plugins.hipchat.HipChatPublisher>
		  <room>«h.room»</room>
		</jenkins.plugins.hipchat.HipChatPublisher>
	'''

	def dispatch publisher (PlayAutoTestReport p) '''
		<com.gmail.ikeike443.PlayTestResultPublisher/>
	'''

	def dispatch publisher (JaCoCo j) '''
		<hudson.plugins.jacoco.JacocoPublisher>
		  <execPattern>«j.execPattern»</execPattern>
		  <classPattern>«j.classPattern»</classPattern>
		  <sourcePattern>«j.sourcePattern»</sourcePattern>
		  <inclusionPattern>«j.inclusionPattern»</inclusionPattern>
		  <exclusionPattern>«j.exclusionPattern»</exclusionPattern>
		  <minimumInstructionCoverage>«j.minimumInstructionCoverage»</minimumInstructionCoverage>
		  <minimumBranchCoverage>«j.minimumBranchCoverage»</minimumBranchCoverage>
		  <minimumComplexityCoverage>«j.minimumComplexityCoverage»</minimumComplexityCoverage>
		  <minimumLineCoverage>«j.minimumLineCoverage»</minimumLineCoverage>
		  <minimumMethodCoverage>«j.minimumMethodCoverage»</minimumMethodCoverage>
		  <minimumClassCoverage>«j.minimumClassCoverage»</minimumClassCoverage>
		  <maximumInstructionCoverage>«j.maximumInstructionCoverage»</maximumInstructionCoverage>
		  <maximumBranchCoverage>«j.maximumBranchCoverage»</maximumBranchCoverage>
		  <maximumComplexityCoverage>«j.maximumComplexityCoverage»</maximumComplexityCoverage>
		  <maximumLineCoverage>«j.maximumLineCoverage»</maximumLineCoverage>
		  <maximumMethodCoverage>«j.maximumMethodCoverage»</maximumMethodCoverage>
		  <maximumClassCoverage>«j.maximumClassCoverage»</maximumClassCoverage>
		  <changeBuildStatus>«j.changeBuildStatus»</changeBuildStatus>
		</hudson.plugins.jacoco.JacocoPublisher>
	'''
	
	def dispatch publisher (Checkstyle c) '''
	    <hudson.plugins.checkstyle.CheckStylePublisher>
	      <healthy>«c.healthy»</healthy>
	      <unHealthy>«c.unHealthy»</unHealthy>
	      <thresholdLimit>«c.thresholdLimit»</thresholdLimit>
	      <pluginName>[CHECKSTYLE] </pluginName>
	      <defaultEncoding>«c.defaultEncoding»</defaultEncoding>
	      <canRunOnFailed>«c.canRunOnFailed»</canRunOnFailed>
	      <useStableBuildAsReference>«c.useStableBuildAsReference»</useStableBuildAsReference>
	      <useDeltaValues>«c.useDeltaValues»</useDeltaValues>
	      <thresholds>
	        <unstableTotalAll>«c.thresholds.unstableTotalAll»</unstableTotalAll>
	        <unstableTotalHigh>«c.thresholds.unstableTotalHigh»</unstableTotalHigh>
	        <unstableTotalNormal>«c.thresholds.unstableTotalNormal»</unstableTotalNormal>
	        <unstableTotalLow>«c.thresholds.unstableTotalLow»</unstableTotalLow>
	        <failedTotalAll>«c.thresholds.failedTotalAll»</failedTotalAll>
	        <failedTotalHigh>«c.thresholds.failedTotalHigh»</failedTotalHigh>
	        <failedTotalNormal>«c.thresholds.failedTotalNormal»</failedTotalNormal>
	        <failedTotalLow>«c.thresholds.failedTotalLow»</failedTotalLow>
	      </thresholds>
	      <shouldDetectModules>«c.shouldDetectModules»</shouldDetectModules>
	      <dontComputeNew>«c.dontComputeNew»</dontComputeNew>
	      <doNotResolveRelativePaths>«c.doNotResolveRelativePaths»</doNotResolveRelativePaths>
	      <pattern>«c.pattern»</pattern>
	    </hudson.plugins.checkstyle.CheckStylePublisher>
	'''
	
	def dispatch publisher (PMD p) '''
	    <hudson.plugins.checkstyle.CheckStylePublisher>
	      <healthy>«p.healthy»</healthy>
	      <unHealthy>«p.unHealthy»</unHealthy>
	      <thresholdLimit>«p.thresholdLimit»</thresholdLimit>
	      <pluginName>[CHECKSTYLE] </pluginName>
	      <defaultEncoding>«p.defaultEncoding»</defaultEncoding>
	      <canRunOnFailed>«p.canRunOnFailed»</canRunOnFailed>
	      <useStableBuildAsReference>«p.useStableBuildAsReference»</useStableBuildAsReference>
	      <useDeltaValues>«p.useDeltaValues»</useDeltaValues>
	      <thresholds>
	        <unstableTotalAll>«p.thresholds.unstableTotalAll»</unstableTotalAll>
	        <unstableTotalHigh>«p.thresholds.unstableTotalHigh»</unstableTotalHigh>
	        <unstableTotalNormal>«p.thresholds.unstableTotalNormal»</unstableTotalNormal>
	        <unstableTotalLow>«p.thresholds.unstableTotalLow»</unstableTotalLow>
	        <failedTotalAll>«p.thresholds.failedTotalAll»</failedTotalAll>
	        <failedTotalHigh>«p.thresholds.failedTotalHigh»</failedTotalHigh>
	        <failedTotalNormal>«p.thresholds.failedTotalNormal»</failedTotalNormal>
	        <failedTotalLow>«p.thresholds.failedTotalLow»</failedTotalLow>
	      </thresholds>
	      <shouldDetectModules>«p.shouldDetectModules»</shouldDetectModules>
	      <dontComputeNew>«p.dontComputeNew»</dontComputeNew>
	      <doNotResolveRelativePaths>«p.doNotResolveRelativePaths»</doNotResolveRelativePaths>
	      <pattern>«p.pattern»</pattern>
	    </hudson.plugins.checkstyle.CheckStylePublisher>
	'''
	
	def dispatch publisher (FindBugs f) '''
	    <hudson.plugins.checkstyle.CheckStylePublisher>
	      <healthy>«f.healthy»</healthy>
	      <unHealthy>«f.unHealthy»</unHealthy>
	      <thresholdLimit>«f.thresholdLimit»</thresholdLimit>
	      <pluginName>[CHECKSTYLE] </pluginName>
	      <defaultEncoding>«f.defaultEncoding»</defaultEncoding>
	      <canRunOnFailed>«f.canRunOnFailed»</canRunOnFailed>
	      <useStableBuildAsReference>«f.useStableBuildAsReference»</useStableBuildAsReference>
	      <useDeltaValues>«f.useDeltaValues»</useDeltaValues>
	      <thresholds>
	        <unstableTotalAll>«f.thresholds.unstableTotalAll»</unstableTotalAll>
	        <unstableTotalHigh>«f.thresholds.unstableTotalHigh»</unstableTotalHigh>
	        <unstableTotalNormal>«f.thresholds.unstableTotalNormal»</unstableTotalNormal>
	        <unstableTotalLow>«f.thresholds.unstableTotalLow»</unstableTotalLow>
	        <failedTotalAll>«f.thresholds.failedTotalAll»</failedTotalAll>
	        <failedTotalHigh>«f.thresholds.failedTotalHigh»</failedTotalHigh>
	        <failedTotalNormal>«f.thresholds.failedTotalNormal»</failedTotalNormal>
	        <failedTotalLow>«f.thresholds.failedTotalLow»</failedTotalLow>
	      </thresholds>
	      <shouldDetectModules>«f.shouldDetectModules»</shouldDetectModules>
	      <dontComputeNew>«f.dontComputeNew»</dontComputeNew>
	      <doNotResolveRelativePaths>«f.doNotResolveRelativePaths»</doNotResolveRelativePaths>
	      <pattern>«f.pattern»</pattern>
	      <isRankActivated>«f.isRankActivated»</isRankActivated>
	      <excludePattern>«f.excludePattern»</excludePattern>
	      <includePattern>«f.includePattern»</includePattern>
	    </hudson.plugins.checkstyle.CheckStylePublisher>
	'''

	def dispatch publisher (Cobertura c) '''
		<hudson.plugins.cobertura.CoberturaPublisher>
		  <coberturaReportFile>«c.xmlreport»</coberturaReportFile>
		  <onlyStable>«c.onlyStable»</onlyStable>
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
		  <reportDir>«r.reportDir»</reportDir>
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
		  ï¿½IF b.triggerParams.emptyï¿½
		  <configs class="java.util.Collections$EmptyList"/>
		  <triggerWithNoParameters>true</triggerWithNoParameters>
		  ï¿½ELSEï¿½
		  <configs>
		    ï¿½FOR p:b.triggerParamsï¿½
		    ï¿½triggerParam(p)ï¿½
		    ï¿½ENDFORï¿½
		  </configs>
		  ï¿½ENDIFï¿½
		  <projects>«b.builds.fqn»</projects>
		  <condition>«b.condition.translateCondition»</condition>
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
		  <propertiesFile>«p.propertyFile»</propertiesFile>
		</hudson.plugins.parameterizedtrigger.FileBuildParameters>
	'''

	def dispatch triggerParam(PredefinedTriggerParams p) '''
		<hudson.plugins.parameterizedtrigger.PredefinedBuildParameters>
		  <properties>«p.predefined.normalize»</properties>
		</hudson.plugins.parameterizedtrigger.PredefinedBuildParameters>
	'''
}
