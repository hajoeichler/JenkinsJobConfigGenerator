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
		  <description>ï¿½c.descriptionï¿½</description>
		  ï¿½IF c.displayName != nullï¿½
		  <displayName>ï¿½c.displayNameï¿½</displayName>
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
		  <disabled>ï¿½c.disabledï¿½</disabled>
		  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
		  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
		  ï¿½triggers(c)ï¿½
		  <concurrentBuild>ï¿½c.concurrentBuildï¿½</concurrentBuild>
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
		  <daysToKeep>ï¿½obh.daysToKeepï¿½</daysToKeep>
		  ï¿½ENDIFï¿½
		  ï¿½IF obh.maxNumberOfBuilds > 0ï¿½
		  <numToKeep>ï¿½obh.maxNumberOfBuildsï¿½</numToKeep>
		  ï¿½ENDIFï¿½
		  ï¿½IF obh.daysToKeepArtifact > 0ï¿½
		  <artifactDaysToKeep>ï¿½obh.daysToKeepArtifactï¿½</artifactDaysToKeep>
		  ï¿½ENDIFï¿½
		  ï¿½IF obh.maxNumberOfBuildsWithArtifact > 0ï¿½
		  <artifactNumToKeep>ï¿½obh.maxNumberOfBuildsWithArtifactï¿½</artifactNumToKeep>
		  ï¿½ENDIFï¿½
		</logRotator>
	'''

	def gitHub(Config c) '''
		ï¿½val gitUrl = getGitUrl(c)ï¿½
		ï¿½IF gitUrl != nullï¿½
		<com.coravy.hudson.plugins.github.GithubProjectProperty>
		  <projectUrl>ï¿½gitUrl.normalizeï¿½</projectUrl>
		</com.coravy.hudson.plugins.github.GithubProjectProperty>
		ï¿½ENDIFï¿½
	'''

	def restrictTo(Config c) '''
		ï¿½val r = getRestrictTo(c)ï¿½
		ï¿½IF r == nullï¿½
		<canRoam>true</canRoam>
		ï¿½ELSEï¿½
		<assignedNode>ï¿½rï¿½</assignedNode>
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
		  <name>ï¿½p.nameï¿½</name>
		  <description>ï¿½p.descriptionï¿½</description>
		  <defaultValue>ï¿½s.value.normalizeï¿½</defaultValue>
		</hudson.model.StringParameterDefinition>
	'''

	def dispatch param(Parameter p, BooleanParam b) '''
		<hudson.model.BooleanParameterDefinition>
		  <name>ï¿½p.nameï¿½</name>
		  <description>ï¿½p.descriptionï¿½</description>
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
		  <name>ï¿½p.nameï¿½</name>
		  <description>ï¿½p.descriptionï¿½</description>
		  <choices class="java.util.Arrays$ArrayList">
		    <a class="string-array">
		    ï¿½FOR s:c.choices.split("\n")ï¿½
		      <string>ï¿½sï¿½</string>
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
		      <url>ï¿½git.url.normalizeï¿½</url>
		    </hudson.plugins.git.UserRemoteConfig>
		  </userRemoteConfigs>
		  <branches>
		    <hudson.plugins.git.BranchSpec>
		      ï¿½IF git.branch != nullï¿½
		      <name>ï¿½git.branchï¿½</name>
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
		  <excludedRegions>ï¿½git.regions.excludedRegions.normalizeï¿½</excludedRegions>
		  ï¿½ELSEï¿½
		  <excludedRegions></excludedRegions>
		  ï¿½ENDIFï¿½
		  <excludedUsers></excludedUsers>
		  <gitConfigName></gitConfigName>
		  <gitConfigEmail></gitConfigEmail>
		  <skipTag>false</skipTag>
		  ï¿½IF git.regions != nullï¿½
		  <includedRegions>ï¿½git.regions.includedRegions.normalizeï¿½</includedRegions>
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
		        <remote>ï¿½svn.url.normalizeï¿½</remote>
		        <local>ï¿½svn.localDir.normalizeï¿½</local>
		      </hudson.scm.SubversionSCM_-ModuleLocation>
		    </locations>
		    ï¿½IF svn.regions != nullï¿½
		    <excludedRegions>ï¿½svn.regions.excludedRegions.normalizeï¿½</excludedRegions>
		    <includedRegions>ï¿½svn.regions.includedRegions.normalizeï¿½</includedRegions>
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
		  <cvsroot>ï¿½cvs.rootï¿½</cvsroot>
		  <module>ï¿½cvs.modulesï¿½</module>
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
		  <spec>ï¿½t.timerï¿½</spec>
		</hudson.triggers.TimerTrigger>
	'''

	def dispatch trigger(PollScmTrigger t) '''
		<hudson.triggers.SCMTrigger>
		  <spec>ï¿½t.pollï¿½</spec>
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
		      <name>ï¿½l.lock.nameï¿½</name>
		    </hudson.plugins.locksandlatches.LockWrapper_-LockWaitConfig>
		  </locks>
		</hudson.plugins.locksandlatches.LockWrapper>
	'''

	def dispatch wrapper(Timeout t) '''
		<hudson.plugins.build__timeout.BuildTimeoutWrapper>
		  <timeoutMinutes>ï¿½t.tï¿½</timeoutMinutes>
		  <failBuild>ï¿½t.failBuildï¿½</failBuild>
		</hudson.plugins.build__timeout.BuildTimeoutWrapper>
	'''

	def dispatch wrapper(ExclusiveExecution e) '''
		<hudson.plugins.execution.exclusive.ExclusiveBuildWrapper/>
	'''

	def dispatch wrapper(MatrixTieParent m) '''
		<matrixtieparent.BuildWrapperMtp>
		  <labelName>ï¿½m.matrixParentï¿½</labelName>
		</matrixtieparent.BuildWrapperMtp>
	'''

	def dispatch wrapper(AnsiColor a) '''
		<hudson.plugins.ansicolor.AnsiColorBuildWrapper/>
	'''

	def dispatch wrapper(Release r) '''
	<hudson.plugins.release.ReleaseWrapper>
	  <releaseVersionTemplate></releaseVersionTemplate>
	  <doNotKeepLog>ï¿½r.notKeepForeverï¿½</doNotKeepLog>
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
		    <name>ï¿½e.keyï¿½</name>
		    <values>
		      ï¿½FOR v:e.valueï¿½
		      <string>ï¿½vï¿½</string>
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
		  <targets>ï¿½m.mavenGoalsï¿½</targets>
		  <mavenName>ï¿½m.version.nameï¿½</mavenName>
		  ï¿½IF m.mavenPOM != nullï¿½
		  <pom>ï¿½m.mavenPOM.normalizeï¿½</pom>
		  ï¿½ENDIFï¿½
		  ï¿½IF m.mavenProperties != nullï¿½
		  <properties>ï¿½m.mavenPropertiesï¿½</properties>
		  ï¿½ENDIFï¿½
		  <usePrivateRepository>ï¿½m.mavenPrivateRepoï¿½</usePrivateRepository>
		</hudson.tasks.Maven>
	'''

	def dispatch build (Shell s) '''
		<hudson.tasks.Shell>
		  <command>ï¿½s.shellScript.normalizeï¿½</command>
		</hudson.tasks.Shell>
	'''

	def dispatch build (Batch b) '''
		<hudson.tasks.BatchFile>
		  <command>ï¿½b.batchScript.normalizeï¿½</command>
		</hudson.tasks.BatchFile>
	'''

	def dispatch build (Ant a) '''
		<hudson.tasks.Ant>
		  <targets></targets>
		  <antName>ï¿½a.version.nameï¿½</antName>
		  <buildFile>ï¿½a.buildFileï¿½</buildFile>
		</hudson.tasks.Ant>
	'''

	def dispatch build (SystemGroovy sg) '''
		<hudson.plugins.groovy.SystemGroovy>
		  <scriptSource class="hudson.plugins.groovy.StringScriptSource">
		    <command>ï¿½sg.groovyScript.normalizeï¿½</command>
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
		  <projects>ï¿½tb.builds.fqnï¿½</projects>
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
		  <recipientList>ï¿½getTo(em)ï¿½</recipientList>
		  <configuredTriggers>
		    ï¿½val m = new LinkedHashMap<String, MailTrigger>()ï¿½
		    ï¿½FOR mt:getAllMailTriggers(em, m).valuesï¿½
		    ï¿½mailTrigger(mt)ï¿½
		    ï¿½ENDFORï¿½
		  </configuredTriggers>
		  ï¿½IF em.type == nullï¿½
		  <contentType>default</contentType>
		  ï¿½ELSEï¿½
		  <contentType>ï¿½em.typeï¿½</contentType>
		  ï¿½ENDIFï¿½
		  ï¿½val subject = getSubject(em)ï¿½
		  ï¿½IF subject == nullï¿½
		  <defaultSubject>$DEFAULT_SUBJECT</defaultSubject>
		  ï¿½ELSEï¿½
		  <defaultSubject>ï¿½subjectï¿½</defaultSubject>
		  ï¿½ENDIFï¿½
		  ï¿½val content = getContent(em)ï¿½
		  ï¿½IF content == nullï¿½
		  <defaultContent>$DEFAULT_CONTENT</defaultContent>
		  ï¿½ELSEï¿½
		  <defaultContent>ï¿½contentï¿½</defaultContent>
		  ï¿½ENDIFï¿½
		  <attachmentsPattern>ï¿½getAttachments(em)ï¿½</attachmentsPattern>
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
		    <recipientList>ï¿½mt.toï¿½</recipientList>
		    ï¿½ENDIFï¿½
		    ï¿½IF mt.mailConfig != null && mt.mailConfig.subject == nullï¿½
		    <subject>$PROJECT_DEFAULT_SUBJECT</subject>
		    ï¿½ELSEï¿½
		    <subject>ï¿½mt.mailConfig.subjectï¿½</subject>
		    ï¿½ENDIFï¿½
		    ï¿½IF mt.mailConfig != null && mt.mailConfig.content == nullï¿½
		    <body>$PROJECT_DEFAULT_CONTENT</body>
		    ï¿½ELSEï¿½
		    <body>ï¿½mt.mailConfig.contentï¿½</body>
		    ï¿½ENDIFï¿½
		    <sendToDevelopers>ï¿½mt.toCommiterï¿½</sendToDevelopers>
		    <sendToRequester>ï¿½mt.toRequesterï¿½</sendToRequester>
		    <includeCulprits>ï¿½mt.toCulpritsï¿½</includeCulprits>
		    <sendToRecipientList>ï¿½mt.toListï¿½</sendToRecipientList>
		    ï¿½IF mt.mailConfig != nullï¿½
		    <attachmentsPattern>ï¿½mt.mailConfig.attachmentsï¿½</attachmentsPattern>
		    ï¿½mailConfig(mt.mailConfig)ï¿½
		    ï¿½ENDIFï¿½
		  </email>
		</hudson.plugins.emailext.plugins.trigger.ï¿½mt.type.replace("-", "")ï¿½Trigger>
	'''

	def mailConfig(MailConfig mc) '''
		<attachBuildLog>ï¿½mc.attachBuildLogï¿½</attachBuildLog>
	'''

	def dispatch publisher (TestResult t) '''
		ï¿½IF isNotEmpty(t.testresults)ï¿½
		<hudson.tasks.junit.JUnitResultArchiver>
		  <testResults>ï¿½t.testresults.normalizeï¿½</testResults>
		  <keepLongStdio>ï¿½t.longIOï¿½</keepLongStdio>
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
		  <artifacts>ï¿½a.artifacts.normalizeï¿½</artifacts>
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
		  <pushOnlyIfSuccess>ï¿½g.onlyOnSuccessï¿½</pushOnlyIfSuccess>
		  <branchesToPush>
		    <hudson.plugins.git.GitPublisher_-BranchToPush>
		      <targetRepoName>ï¿½g.originï¿½</targetRepoName>
		      <branchName>ï¿½g.branchï¿½</branchName>
		    </hudson.plugins.git.GitPublisher_-BranchToPush>
		  </branchesToPush>
		</hudson.plugins.git.GitPublisher>
	'''

	def dispatch publisher(Gatling g) '''
		<com.excilys.ebi.gatling.jenkins.GatlingPublisher plugin="gatling@1.0.0">
		  <simulation>
		    <name>ï¿½g.resultprefix.normalizeï¿½</name>
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
		    <type>ï¿½vc.typeï¿½</type>
		    <min>ï¿½vc.minï¿½</min>
		    <max>ï¿½vc.maxï¿½</max>
		    <unstable>ï¿½vc.unstableï¿½</unstable>
		    <usePattern>false</usePattern>
		    <pattern>ï¿½vc.patternï¿½</pattern>
		  </hudson.plugins.violations.TypeConfig>
		</entry>
	'''

	def dispatch publisher (HTMLPublisher h) '''
		<htmlpublisher.HtmlPublisher>
		  <reportTargets>
		    <htmlpublisher.HtmlPublisherTarget>
		      <reportName>ï¿½h.nameï¿½</reportName>
		      <reportDir>ï¿½h.dirï¿½</reportDir>
		      <reportFiles>ï¿½h.filesï¿½</reportFiles>
		      <keepAll>ï¿½h.keepPastï¿½</keepAll>
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
		    <unstableTotalAll>ï¿½w.unstableTotalAllï¿½</unstableTotalAll>
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
		    <failedTotalAll>ï¿½w.failTotalAllï¿½</failedTotalAll>
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
		    <string>ï¿½w.parser.nameï¿½</string>
		  </consoleLogParsers>
		</hudson.plugins.warnings.WarningsPublisher>
	'''

	def dispatch publisher (Claim c) '''
		<hudson.plugins.claim.ClaimPublisher/>
	'''

	def dispatch publisher (HipChat h) '''
		<jenkins.plugins.hipchat.HipChatPublisher>
		  <room>ï¿½h.roomï¿½</room>
		</jenkins.plugins.hipchat.HipChatPublisher>
	'''

	def dispatch publisher (PlayAutoTestReport p) '''
		<com.gmail.ikeike443.PlayTestResultPublisher/>
	'''

	def dispatch publisher (JaCoCo j) '''
		<hudson.plugins.jacoco.JacocoPublisher>
		  <execPattern>ï¿½j.execPatternï¿½</execPattern>
		  <classPattern>ï¿½j.classPatternï¿½</classPattern>
		  <sourcePattern>ï¿½j.sourcePatternï¿½</sourcePattern>
		  <inclusionPattern>ï¿½j.inclusionPatternï¿½</inclusionPattern>
		  <exclusionPattern>ï¿½j.exclusionPatternï¿½</exclusionPattern>
		  <minimumInstructionCoverage>ï¿½j.minimumInstructionCoverageï¿½</minimumInstructionCoverage>
		  <minimumBranchCoverage>ï¿½j.minimumBranchCoverageï¿½</minimumBranchCoverage>
		  <minimumComplexityCoverage>ï¿½j.minimumComplexityCoverageï¿½</minimumComplexityCoverage>
		  <minimumLineCoverage>ï¿½j.minimumLineCoverageï¿½</minimumLineCoverage>
		  <minimumMethodCoverage>ï¿½j.minimumMethodCoverageï¿½</minimumMethodCoverage>
		  <minimumClassCoverage>ï¿½j.minimumClassCoverageï¿½</minimumClassCoverage>
		  <maximumInstructionCoverage>ï¿½j.maximumInstructionCoverageï¿½</maximumInstructionCoverage>
		  <maximumBranchCoverage>ï¿½j.maximumBranchCoverageï¿½</maximumBranchCoverage>
		  <maximumComplexityCoverage>ï¿½j.maximumComplexityCoverageï¿½</maximumComplexityCoverage>
		  <maximumLineCoverage>ï¿½j.maximumLineCoverageï¿½</maximumLineCoverage>
		  <maximumMethodCoverage>ï¿½j.maximumMethodCoverageï¿½</maximumMethodCoverage>
		  <maximumClassCoverage>ï¿½j.maximumClassCoverageï¿½</maximumClassCoverage>
		  <changeBuildStatus>ï¿½j.changeBuildStatusï¿½</changeBuildStatus>
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
		  <coberturaReportFile>ï¿½c.xmlreportï¿½</coberturaReportFile>
		  <onlyStable>ï¿½c.onlyStableï¿½</onlyStable>
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
		  <reportDir>ï¿½r.reportDirï¿½</reportDir>
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
		  <projects>ï¿½b.builds.fqnï¿½</projects>
		  <condition>ï¿½b.condition.translateConditionï¿½</condition>
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
		  <propertiesFile>ï¿½p.propertyFileï¿½</propertiesFile>
		</hudson.plugins.parameterizedtrigger.FileBuildParameters>
	'''

	def dispatch triggerParam(PredefinedTriggerParams p) '''
		<hudson.plugins.parameterizedtrigger.PredefinedBuildParameters>
		  <properties>ï¿½p.predefined.normalizeï¿½</properties>
		</hudson.plugins.parameterizedtrigger.PredefinedBuildParameters>
	'''
}
