package de.hajoeichler.jenkins.generator;

import de.hajoeichler.jenkins.jobConfig.AnsiColor;
import de.hajoeichler.jenkins.jobConfig.Ant;
import de.hajoeichler.jenkins.jobConfig.Artifacts;
import de.hajoeichler.jenkins.jobConfig.AxisDecl;
import de.hajoeichler.jenkins.jobConfig.Batch;
import de.hajoeichler.jenkins.jobConfig.BooleanParam;
import de.hajoeichler.jenkins.jobConfig.BuildSection;
import de.hajoeichler.jenkins.jobConfig.Checkstyle;
import de.hajoeichler.jenkins.jobConfig.ChoiceParam;
import de.hajoeichler.jenkins.jobConfig.Claim;
import de.hajoeichler.jenkins.jobConfig.Cobertura;
import de.hajoeichler.jenkins.jobConfig.Config;
import de.hajoeichler.jenkins.jobConfig.CurrentTriggerParams;
import de.hajoeichler.jenkins.jobConfig.DownStream;
import de.hajoeichler.jenkins.jobConfig.DownStreamBuild;
import de.hajoeichler.jenkins.jobConfig.ExclusiveExecution;
import de.hajoeichler.jenkins.jobConfig.ExtMail;
import de.hajoeichler.jenkins.jobConfig.FindBugs;
import de.hajoeichler.jenkins.jobConfig.FirstStartTrigger;
import de.hajoeichler.jenkins.jobConfig.Gatling;
import de.hajoeichler.jenkins.jobConfig.GitCommitParam;
import de.hajoeichler.jenkins.jobConfig.GitHubPushTrigger;
import de.hajoeichler.jenkins.jobConfig.GitPublisher;
import de.hajoeichler.jenkins.jobConfig.Group;
import de.hajoeichler.jenkins.jobConfig.HTMLPublisher;
import de.hajoeichler.jenkins.jobConfig.HipChat;
import de.hajoeichler.jenkins.jobConfig.JaCoCo;
import de.hajoeichler.jenkins.jobConfig.Lock;
import de.hajoeichler.jenkins.jobConfig.MailConfig;
import de.hajoeichler.jenkins.jobConfig.MailTrigger;
import de.hajoeichler.jenkins.jobConfig.Matrix;
import de.hajoeichler.jenkins.jobConfig.MatrixDecl;
import de.hajoeichler.jenkins.jobConfig.MatrixTieParent;
import de.hajoeichler.jenkins.jobConfig.Maven;
import de.hajoeichler.jenkins.jobConfig.OldBuildHandling;
import de.hajoeichler.jenkins.jobConfig.PMD;
import de.hajoeichler.jenkins.jobConfig.Parameter;
import de.hajoeichler.jenkins.jobConfig.ParameterSection;
import de.hajoeichler.jenkins.jobConfig.ParameterType;
import de.hajoeichler.jenkins.jobConfig.PlayAutoTestReport;
import de.hajoeichler.jenkins.jobConfig.PollScmTrigger;
import de.hajoeichler.jenkins.jobConfig.PredefinedTriggerParams;
import de.hajoeichler.jenkins.jobConfig.PropertyFileTriggerParams;
import de.hajoeichler.jenkins.jobConfig.PublisherSection;
import de.hajoeichler.jenkins.jobConfig.Rcov;
import de.hajoeichler.jenkins.jobConfig.Release;
import de.hajoeichler.jenkins.jobConfig.Scm;
import de.hajoeichler.jenkins.jobConfig.ScmCVS;
import de.hajoeichler.jenkins.jobConfig.ScmGit;
import de.hajoeichler.jenkins.jobConfig.ScmSVN;
import de.hajoeichler.jenkins.jobConfig.Shell;
import de.hajoeichler.jenkins.jobConfig.StringParam;
import de.hajoeichler.jenkins.jobConfig.SystemGroovy;
import de.hajoeichler.jenkins.jobConfig.TestResult;
import de.hajoeichler.jenkins.jobConfig.Thresholds;
import de.hajoeichler.jenkins.jobConfig.Timeout;
import de.hajoeichler.jenkins.jobConfig.TimerTrigger;
import de.hajoeichler.jenkins.jobConfig.TriggerBuilderSection;
import de.hajoeichler.jenkins.jobConfig.TriggerSection;
import de.hajoeichler.jenkins.jobConfig.TriggeredBuild;
import de.hajoeichler.jenkins.jobConfig.Violations;
import de.hajoeichler.jenkins.jobConfig.ViolationsConfig;
import de.hajoeichler.jenkins.jobConfig.Warnings;
import de.hajoeichler.jenkins.jobConfig.WrapperSection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.BooleanExtensions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.eclipse.xtext.xtend2.lib.ResourceExtensions;

@SuppressWarnings("all")
public class JobConfigGenerator implements IGenerator {
  private Config currentConfig;
  
  public void doGenerate(final Resource resource, final IFileSystemAccess fsa) {
      URI _uRI = resource.getURI();
      String _operator_plus = StringExtensions.operator_plus("Processing ", _uRI);
      InputOutput.<String>println(_operator_plus);
      Iterable<EObject> _allContentsIterable = ResourceExtensions.allContentsIterable(resource);
      Iterable<Config> _filter = IterableExtensions.<Config>filter(_allContentsIterable, de.hajoeichler.jenkins.jobConfig.Config.class);
      for (final Config config : _filter) {
        boolean _isAbstract = config.isAbstract();
        boolean _operator_not = BooleanExtensions.operator_not(_isAbstract);
        if (_operator_not) {
          {
            this.currentConfig = config;
            String _fileName = this.fileName(config);
            String _operator_plus_1 = StringExtensions.operator_plus("Writing config to ", _fileName);
            InputOutput.<String>println(_operator_plus_1);
            String _fileName_1 = this.fileName(config);
            CharSequence _content = this.content(config);
            fsa.generateFile(_fileName_1, _content);
          }
        }
      }
  }
  
  public String normalize(final String s) {
    String _xblockexpression = null;
    {
      boolean _operator_equals = ObjectExtensions.operator_equals(s, null);
      if (_operator_equals) {
        return s;
      }
      String _replaceJobName = this.replaceJobName(s, this.currentConfig);
      String _escape = this.escape(_replaceJobName);
      _xblockexpression = (_escape);
    }
    return _xblockexpression;
  }
  
  public String escape(final String s) {
    String _xblockexpression = null;
    {
      String _replaceAll = s.replaceAll("&", "&amp;");
      String r = _replaceAll;
      String _replaceAll_1 = r.replaceAll("\"", "&quot;");
      r = _replaceAll_1;
      String _replaceAll_2 = r.replaceAll("\'", "&apos;");
      r = _replaceAll_2;
      String _replaceAll_3 = r.replaceAll(">", "&gt;");
      r = _replaceAll_3;
      String _replaceAll_4 = r.replaceAll("<", "&lt;");
      String _r = r = _replaceAll_4;
      _xblockexpression = (_r);
    }
    return _xblockexpression;
  }
  
  public boolean isNotEmpty(final String s) {
    boolean _operator_and = false;
    boolean _operator_notEquals = ObjectExtensions.operator_notEquals(s, null);
    if (!_operator_notEquals) {
      _operator_and = false;
    } else {
      boolean _isEmpty = s.isEmpty();
      boolean _operator_not = BooleanExtensions.operator_not(_isEmpty);
      _operator_and = BooleanExtensions.operator_and(_operator_notEquals, _operator_not);
    }
    return _operator_and;
  }
  
  public String replaceJobName(final String s, final Config c) {
    String _name = c.getName();
    String _replaceAll = s.replaceAll("@@jobName@@", _name);
    return _replaceAll;
  }
  
  public String fileName(final Config c) {
    String _fqn = this.fqn(c);
    String _operator_plus = StringExtensions.operator_plus(_fqn, "/config.xml");
    return _operator_plus;
  }
  
  protected String _fqn(final Group g) {
    String _name = g.getName();
    return _name;
  }
  
  protected String _fqn(final Config c) {
    String _xifexpression = null;
    EObject _eContainer = c.eContainer();
    if ((_eContainer instanceof Group)) {
      EObject _eContainer_1 = c.eContainer();
      String _fqn = this.fqn(((Group) _eContainer_1));
      String _name = c.getName();
      String _operator_plus = StringExtensions.operator_plus(_fqn, _name);
      _xifexpression = _operator_plus;
    } else {
      String _name_1 = c.getName();
      _xifexpression = _name_1;
    }
    return _xifexpression;
  }
  
  public Config getMyConfig(final EObject any) {
    Config _xblockexpression = null;
    {
      if ((any instanceof Config)) {
        return ((Config) any);
      }
      Config _xifexpression = null;
      EObject _eContainer = any.eContainer();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_eContainer, null);
      if (_operator_notEquals) {
        EObject _eContainer_1 = any.eContainer();
        Config _myConfig = this.getMyConfig(_eContainer_1);
        return _myConfig;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public boolean isMatrixJob(final Config c) {
    EList<Matrix> _matrixes = c.getMatrixes();
    boolean _isEmpty = _matrixes.isEmpty();
    boolean _operator_not = BooleanExtensions.operator_not(_isEmpty);
    return _operator_not;
  }
  
  public String getGitUrl(final Config c) {
    String _xifexpression = null;
    String _gitUrl = c.getGitUrl();
    boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_gitUrl, null);
    if (_operator_notEquals) {
      String _gitUrl_1 = c.getGitUrl();
      _xifexpression = _gitUrl_1;
    } else {
      String _xifexpression_1 = null;
      Config _parentConfig = c.getParentConfig();
      boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_parentConfig, null);
      if (_operator_notEquals_1) {
        Config _parentConfig_1 = c.getParentConfig();
        String _gitUrl_2 = this.getGitUrl(_parentConfig_1);
        _xifexpression_1 = _gitUrl_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public String getRestrictTo(final Config c) {
    String _xifexpression = null;
    String _restrictTo = c.getRestrictTo();
    boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_restrictTo, null);
    if (_operator_notEquals) {
      String _restrictTo_1 = c.getRestrictTo();
      _xifexpression = _restrictTo_1;
    } else {
      String _xifexpression_1 = null;
      Config _parentConfig = c.getParentConfig();
      boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_parentConfig, null);
      if (_operator_notEquals_1) {
        Config _parentConfig_1 = c.getParentConfig();
        String _restrictTo_2 = this.getRestrictTo(_parentConfig_1);
        _xifexpression_1 = _restrictTo_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public OldBuildHandling getAnyOldBuildHandling(final Config c) {
    OldBuildHandling _xifexpression = null;
    OldBuildHandling _oldBuildHandling = c.getOldBuildHandling();
    boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_oldBuildHandling, null);
    if (_operator_notEquals) {
      OldBuildHandling _oldBuildHandling_1 = c.getOldBuildHandling();
      _xifexpression = _oldBuildHandling_1;
    } else {
      OldBuildHandling _xifexpression_1 = null;
      Config _parentConfig = c.getParentConfig();
      boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_parentConfig, null);
      if (_operator_notEquals_1) {
        Config _parentConfig_1 = c.getParentConfig();
        OldBuildHandling _anyOldBuildHandling = this.getAnyOldBuildHandling(_parentConfig_1);
        _xifexpression_1 = _anyOldBuildHandling;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public Map<String,Parameter> getAllParameters(final Config c, final Map<String,Parameter> m) {
      ParameterSection _paramSection = c.getParamSection();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_paramSection, null);
      if (_operator_notEquals) {
        ParameterSection _paramSection_1 = c.getParamSection();
        EList<Parameter> _parameters = _paramSection_1.getParameters();
        for (final Parameter p : _parameters) {
          String _name = p.getName();
          boolean _containsKey = m.containsKey(_name);
          boolean _operator_not = BooleanExtensions.operator_not(_containsKey);
          if (_operator_not) {
            String _name_1 = p.getName();
            m.put(_name_1, p);
          }
        }
      }
      Config _parentConfig = c.getParentConfig();
      boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_parentConfig, null);
      if (_operator_notEquals_1) {
        Config _parentConfig_1 = c.getParentConfig();
        this.getAllParameters(_parentConfig_1, m);
      }
      return m;
  }
  
  public Scm getAnyScm(final Config c) {
    Scm _xifexpression = null;
    Scm _scm = c.getScm();
    boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_scm, null);
    if (_operator_notEquals) {
      Scm _scm_1 = c.getScm();
      _xifexpression = _scm_1;
    } else {
      Scm _xifexpression_1 = null;
      Config _parentConfig = c.getParentConfig();
      boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_parentConfig, null);
      if (_operator_notEquals_1) {
        Config _parentConfig_1 = c.getParentConfig();
        Scm _anyScm = this.getAnyScm(_parentConfig_1);
        _xifexpression_1 = _anyScm;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public Map<EClass,EObject> getAllTriggers(final Config c, final Map<EClass,EObject> m) {
      TriggerSection _trigger = c.getTrigger();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_trigger, null);
      if (_operator_notEquals) {
        TriggerSection _trigger_1 = c.getTrigger();
        EList<EObject> _buildtriggers = _trigger_1.getBuildtriggers();
        for (final EObject t : _buildtriggers) {
          EClass _eClass = t.eClass();
          boolean _containsKey = m.containsKey(_eClass);
          boolean _operator_not = BooleanExtensions.operator_not(_containsKey);
          if (_operator_not) {
            EClass _eClass_1 = t.eClass();
            m.put(_eClass_1, t);
          }
        }
      }
      Config _parentConfig = c.getParentConfig();
      boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_parentConfig, null);
      if (_operator_notEquals_1) {
        Config _parentConfig_1 = c.getParentConfig();
        this.getAllTriggers(_parentConfig_1, m);
      }
      return m;
  }
  
  public Map<EClass,EObject> getAllWrappers(final Config c, final Map<EClass,EObject> m) {
      WrapperSection _wrapper = c.getWrapper();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_wrapper, null);
      if (_operator_notEquals) {
        WrapperSection _wrapper_1 = c.getWrapper();
        EList<EObject> _wrappers = _wrapper_1.getWrappers();
        for (final EObject w : _wrappers) {
          EClass _eClass = w.eClass();
          boolean _containsKey = m.containsKey(_eClass);
          boolean _operator_not = BooleanExtensions.operator_not(_containsKey);
          if (_operator_not) {
            EClass _eClass_1 = w.eClass();
            m.put(_eClass_1, w);
          }
        }
      }
      Config _parentConfig = c.getParentConfig();
      boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_parentConfig, null);
      if (_operator_notEquals_1) {
        Config _parentConfig_1 = c.getParentConfig();
        this.getAllWrappers(_parentConfig_1, m);
      }
      return m;
  }
  
  public List<EObject> getAllBuilders(final Config c, final List<EObject> l) {
      Config _parentConfig = c.getParentConfig();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_parentConfig, null);
      if (_operator_notEquals) {
        Config _parentConfig_1 = c.getParentConfig();
        this.getAllBuilders(_parentConfig_1, l);
      }
      BuildSection _buildSection = c.getBuildSection();
      boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_buildSection, null);
      if (_operator_notEquals_1) {
        BuildSection _buildSection_1 = c.getBuildSection();
        EList<EObject> _builds = _buildSection_1.getBuilds();
        l.addAll(_builds);
      }
      return l;
  }
  
  public Map<EClass,EObject> getAllPublishers(final Config c, final Map<EClass,EObject> m) {
      PublisherSection _publisherSection = c.getPublisherSection();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_publisherSection, null);
      if (_operator_notEquals) {
        PublisherSection _publisherSection_1 = c.getPublisherSection();
        EList<EObject> _publishers = _publisherSection_1.getPublishers();
        for (final EObject p : _publishers) {
          EClass _eClass = p.eClass();
          boolean _containsKey = m.containsKey(_eClass);
          boolean _operator_not = BooleanExtensions.operator_not(_containsKey);
          if (_operator_not) {
            EClass _eClass_1 = p.eClass();
            m.put(_eClass_1, p);
          }
        }
      }
      Config _parentConfig = c.getParentConfig();
      boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_parentConfig, null);
      if (_operator_notEquals_1) {
        Config _parentConfig_1 = c.getParentConfig();
        this.getAllPublishers(_parentConfig_1, m);
      }
      return m;
  }
  
  public CharSequence content(final Config c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\'1.0\' encoding=\'UTF-8\'?>");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDIF c.isMatrixJob\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("<matrix-project>");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("<project>");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<actions/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<description>\u00EF\u00BF\u00BDc.description\u00EF\u00BF\u00BD</description>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF c.displayName != null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<displayName>\u00EF\u00BF\u00BDc.displayName\u00EF\u00BF\u00BD</displayName>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF c.getAnyOldBuildHandling != null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDlogRotator(c.getAnyOldBuildHandling)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<keepDependencies>false</keepDependencies>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<properties>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDgitHub(c)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDparameters(c)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</properties>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF c.getAnyScm == null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<scm class=\"hudson.scm.NullSCM\"/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDscm(c.getAnyScm)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDrestrictTo(c)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<disabled>\u00EF\u00BF\u00BDc.disabled\u00EF\u00BF\u00BD</disabled>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDtriggers(c)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<concurrentBuild>\u00EF\u00BF\u00BDc.concurrentBuild\u00EF\u00BF\u00BD</concurrentBuild>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF c.isMatrixJob\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDmatrix(c)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDbuilders(c)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDpublishers(c)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDwrappers(c)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDIF c.isMatrixJob\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("</matrix-project>");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("</project>");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence logRotator(final OldBuildHandling obh) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<logRotator>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF obh.daysToKeep > 0\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<daysToKeep>\u00EF\u00BF\u00BDobh.daysToKeep\u00EF\u00BF\u00BD</daysToKeep>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF obh.maxNumberOfBuilds > 0\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<numToKeep>\u00EF\u00BF\u00BDobh.maxNumberOfBuilds\u00EF\u00BF\u00BD</numToKeep>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF obh.daysToKeepArtifact > 0\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<artifactDaysToKeep>\u00EF\u00BF\u00BDobh.daysToKeepArtifact\u00EF\u00BF\u00BD</artifactDaysToKeep>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF obh.maxNumberOfBuildsWithArtifact > 0\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<artifactNumToKeep>\u00EF\u00BF\u00BDobh.maxNumberOfBuildsWithArtifact\u00EF\u00BF\u00BD</artifactNumToKeep>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("</logRotator>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence gitHub(final Config c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\u00EF\u00BF\u00BDval gitUrl = getGitUrl(c)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDIF gitUrl != null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("<com.coravy.hudson.plugins.github.GithubProjectProperty>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<projectUrl>\u00EF\u00BF\u00BDgitUrl.normalize\u00EF\u00BF\u00BD</projectUrl>");
    _builder.newLine();
    _builder.append("</com.coravy.hudson.plugins.github.GithubProjectProperty>");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence restrictTo(final Config c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\u00EF\u00BF\u00BDval r = getRestrictTo(c)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDIF r == null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("<canRoam>true</canRoam>");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("<assignedNode>\u00EF\u00BF\u00BDr\u00EF\u00BF\u00BD</assignedNode>");
    _builder.newLine();
    _builder.append("<canRoam>false</canRoam>");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence parameters(final Config c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\u00EF\u00BF\u00BDval m = new LinkedHashMap<String, Parameter>()\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDval v = getAllParameters(c, m).values\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDIF v.empty == false\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("<hudson.model.ParametersDefinitionProperty>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<parameterDefinitions>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDFOR p:v\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDparam(p, p.type)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</parameterDefinitions>");
    _builder.newLine();
    _builder.append("</hudson.model.ParametersDefinitionProperty>");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _param(final Parameter p, final StringParam s) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.model.StringParameterDefinition>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<name>\u00EF\u00BF\u00BDp.name\u00EF\u00BF\u00BD</name>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<description>\u00EF\u00BF\u00BDp.description\u00EF\u00BF\u00BD</description>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<defaultValue>\u00EF\u00BF\u00BDs.value.normalize\u00EF\u00BF\u00BD</defaultValue>");
    _builder.newLine();
    _builder.append("</hudson.model.StringParameterDefinition>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _param(final Parameter p, final BooleanParam b) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.model.BooleanParameterDefinition>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<name>\u00EF\u00BF\u00BDp.name\u00EF\u00BF\u00BD</name>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<description>\u00EF\u00BF\u00BDp.description\u00EF\u00BF\u00BD</description>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF b.checked\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<defaultValue>true</defaultValue>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF b.notChecked\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<defaultValue>false</defaultValue>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("</hudson.model.BooleanParameterDefinition>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _param(final Parameter p, final ChoiceParam c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.model.ChoiceParameterDefinition>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<name>\u00EF\u00BF\u00BDp.name\u00EF\u00BF\u00BD</name>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<description>\u00EF\u00BF\u00BDp.description\u00EF\u00BF\u00BD</description>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<choices class=\"java.util.Arrays$ArrayList\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<a class=\"string-array\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDFOR s:c.choices.split(\"\\n\")\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<string>\u00EF\u00BF\u00BDs\u00EF\u00BF\u00BD</string>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</a>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</choices>");
    _builder.newLine();
    _builder.append("</hudson.model.ChoiceParameterDefinition>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _scm(final ScmGit git) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<scm class=\"hudson.plugins.git.GitSCM\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<configVersion>2</configVersion>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<userRemoteConfigs>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<hudson.plugins.git.UserRemoteConfig>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<name>origin</name>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<refspec>+refs/heads/*:refs/remotes/origin/*</refspec>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<url>\u00EF\u00BF\u00BDgit.url.normalize\u00EF\u00BF\u00BD</url>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</hudson.plugins.git.UserRemoteConfig>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</userRemoteConfigs>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<branches>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<hudson.plugins.git.BranchSpec>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("\u00EF\u00BF\u00BDIF git.branch != null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<name>\u00EF\u00BF\u00BDgit.branch\u00EF\u00BF\u00BD</name>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<name>origin/master</name>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</hudson.plugins.git.BranchSpec>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</branches>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<disableSubmodules>false</disableSubmodules>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<recursiveSubmodules>false</recursiveSubmodules>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<authorOrCommitter>false</authorOrCommitter>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<clean>false</clean>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF git.wipeOutWorkspace\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<wipeOutWorkspace>true</wipeOutWorkspace>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<wipeOutWorkspace>false</wipeOutWorkspace>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<pruneBranches>false</pruneBranches>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<remotePoll>false</remotePoll>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<ignoreNotifyCommit>false</ignoreNotifyCommit>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<buildChooser class=\"hudson.plugins.git.util.DefaultBuildChooser\"/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<gitTool>Default</gitTool>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<submoduleCfg class=\"list\"/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<relativeTargetDir></relativeTargetDir>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<reference></reference>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF git.regions != null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<excludedRegions>\u00EF\u00BF\u00BDgit.regions.excludedRegions.normalize\u00EF\u00BF\u00BD</excludedRegions>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<excludedRegions></excludedRegions>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<excludedUsers></excludedUsers>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<gitConfigName></gitConfigName>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<gitConfigEmail></gitConfigEmail>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<skipTag>false</skipTag>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF git.regions != null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<includedRegions>\u00EF\u00BF\u00BDgit.regions.includedRegions.normalize\u00EF\u00BF\u00BD</includedRegions>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<includedRegions></includedRegions>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<scmName></scmName>");
    _builder.newLine();
    _builder.append("</scm>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _scm(final ScmSVN svn) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<scm class=\"hudson.scm.SubversionSCM\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<locations>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<hudson.scm.SubversionSCM_-ModuleLocation>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<remote>\u00EF\u00BF\u00BDsvn.url.normalize\u00EF\u00BF\u00BD</remote>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<local>\u00EF\u00BF\u00BDsvn.localDir.normalize\u00EF\u00BF\u00BD</local>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</hudson.scm.SubversionSCM_-ModuleLocation>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</locations>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF svn.regions != null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<excludedRegions>\u00EF\u00BF\u00BDsvn.regions.excludedRegions.normalize\u00EF\u00BF\u00BD</excludedRegions>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<includedRegions>\u00EF\u00BF\u00BDsvn.regions.includedRegions.normalize\u00EF\u00BF\u00BD</includedRegions>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<excludedRegions></excludedRegions>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<includedRegions></includedRegions>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<excludedUsers></excludedUsers>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<excludedRevprop></excludedRevprop>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<excludedCommitMessages></excludedCommitMessages>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<workspaceUpdater class=\"hudson.scm.subversion.UpdateUpdater\"/>");
    _builder.newLine();
    _builder.append("</scm>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _scm(final ScmCVS cvs) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<scm class=\"hudson.scm.CVSSCM\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<cvsroot>\u00EF\u00BF\u00BDcvs.root\u00EF\u00BF\u00BD</cvsroot>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<module>\u00EF\u00BF\u00BDcvs.modules\u00EF\u00BF\u00BD</module>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<canUseUpdate>false</canUseUpdate>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<useHeadIfNotFound>false</useHeadIfNotFound>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<flatten>false</flatten>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<isTag>false</isTag>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<excludedRegions></excludedRegions>");
    _builder.newLine();
    _builder.append("</scm>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence triggers(final Config c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<triggers class=\"vector\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDval m = new LinkedHashMap<EClass, EObject>()\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDFOR t:getAllTriggers(c, m).values\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDtrigger(t)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("</triggers>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _trigger(final TimerTrigger t) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.triggers.TimerTrigger>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<spec>\u00EF\u00BF\u00BDt.timer\u00EF\u00BF\u00BD</spec>");
    _builder.newLine();
    _builder.append("</hudson.triggers.TimerTrigger>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _trigger(final PollScmTrigger t) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.triggers.SCMTrigger>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<spec>\u00EF\u00BF\u00BDt.poll\u00EF\u00BF\u00BD</spec>");
    _builder.newLine();
    _builder.append("</hudson.triggers.SCMTrigger>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _trigger(final FirstStartTrigger t) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<org.jvnet.hudson.plugins.triggers.startup.HudsonStartupTrigger>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<spec></spec>");
    _builder.newLine();
    _builder.append("</org.jvnet.hudson.plugins.triggers.startup.HudsonStartupTrigger>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _trigger(final GitHubPushTrigger t) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<com.cloudbees.jenkins.GitHubPushTrigger>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<spec></spec>");
    _builder.newLine();
    _builder.append("</com.cloudbees.jenkins.GitHubPushTrigger>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence wrappers(final Config c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<buildWrappers>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDval m = new LinkedHashMap<EClass, EObject>()\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDFOR w:getAllWrappers(c, m).values\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDwrapper(w)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("</buildWrappers>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _wrapper(final Lock l) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.locksandlatches.LockWrapper>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<locks>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<hudson.plugins.locksandlatches.LockWrapper_-LockWaitConfig>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<name>\u00EF\u00BF\u00BDl.lock.name\u00EF\u00BF\u00BD</name>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</hudson.plugins.locksandlatches.LockWrapper_-LockWaitConfig>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</locks>");
    _builder.newLine();
    _builder.append("</hudson.plugins.locksandlatches.LockWrapper>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _wrapper(final Timeout t) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.build__timeout.BuildTimeoutWrapper>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<timeoutMinutes>\u00EF\u00BF\u00BDt.t\u00EF\u00BF\u00BD</timeoutMinutes>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<failBuild>\u00EF\u00BF\u00BDt.failBuild\u00EF\u00BF\u00BD</failBuild>");
    _builder.newLine();
    _builder.append("</hudson.plugins.build__timeout.BuildTimeoutWrapper>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _wrapper(final ExclusiveExecution e) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.execution.exclusive.ExclusiveBuildWrapper/>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _wrapper(final MatrixTieParent m) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<matrixtieparent.BuildWrapperMtp>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<labelName>\u00EF\u00BF\u00BDm.matrixParent\u00EF\u00BF\u00BD</labelName>");
    _builder.newLine();
    _builder.append("</matrixtieparent.BuildWrapperMtp>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _wrapper(final AnsiColor a) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.ansicolor.AnsiColorBuildWrapper/>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _wrapper(final Release r) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.release.ReleaseWrapper>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<releaseVersionTemplate></releaseVersionTemplate>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<doNotKeepLog>\u00EF\u00BF\u00BDr.notKeepForever\u00EF\u00BF\u00BD</doNotKeepLog>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<overrideBuildParameters>false</overrideBuildParameters>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<parameterDefinitions>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDIF r.paramSection != null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDFOR p:r.paramSection.parameters\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDparam(p, p.type)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</parameterDefinitions>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<preBuildSteps>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDIF r.preBuildSection != null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDFOR b:r.preBuildSection.builds\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDbuild(b)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</preBuildSteps>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<postBuildSteps>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDIF r.finalBuildSection != null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDFOR b:r.finalBuildSection.builds\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDbuild(b)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</postBuildSteps>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<postSuccessfulBuildSteps>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDIF r.successBuildSection != null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDFOR b:r.successBuildSection.builds\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDbuild(b)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</postSuccessfulBuildSteps>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<postFailedBuildSteps>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDIF r.failedBuildSection != null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDFOR b:r.failedBuildSection.builds\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDbuild(b)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</postFailedBuildSteps>");
    _builder.newLine();
    _builder.append("</hudson.plugins.release.ReleaseWrapper>");
    _builder.newLine();
    return _builder;
  }
  
  public void getMatrixes(final Config c, final Map<String,List<String>> r) {
    EList<Matrix> _matrixes = c.getMatrixes();
    for (final Matrix m : _matrixes) {
      MatrixDecl _matrix = m.getMatrix();
      EList<AxisDecl> _axes = _matrix.getAxes();
      for (final AxisDecl a : _axes) {
        {
          String _label = a.getLabel();
          boolean _containsKey = r.containsKey(_label);
          boolean _operator_not = BooleanExtensions.operator_not(_containsKey);
          if (_operator_not) {
            String _label_1 = a.getLabel();
            ArrayList<String> _arrayList = new ArrayList<String>();
            r.put(_label_1, _arrayList);
          }
          String _label_2 = a.getLabel();
          List<String> _get = r.get(_label_2);
          final List<String> l = _get;
          EList<String> _values = a.getValues();
          for (final String v : _values) {
            l.add(v);
          }
        }
      }
    }
  }
  
  public CharSequence matrix(final Config c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\u00EF\u00BF\u00BDval r = new LinkedHashMap<String, List<String>>()\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDgetMatrixes(c, r)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDFOR e:r.entrySet\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("<axes>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<hudson.matrix.LabelAxis>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<name>\u00EF\u00BF\u00BDe.key\u00EF\u00BF\u00BD</name>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<values>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("\u00EF\u00BF\u00BDFOR v:e.value\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<string>\u00EF\u00BF\u00BDv\u00EF\u00BF\u00BD</string>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</values>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</hudson.matrix.LabelAxis>");
    _builder.newLine();
    _builder.append("</axes>");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence builders(final Config c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<builders>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDval l = new ArrayList<EObject>()\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDFOR b:getAllBuilders(c, l)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDbuild(b)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("</builders>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _build(final Maven m) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.tasks.Maven>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<targets>\u00EF\u00BF\u00BDm.mavenGoals\u00EF\u00BF\u00BD</targets>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<mavenName>\u00EF\u00BF\u00BDm.version.name\u00EF\u00BF\u00BD</mavenName>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF m.mavenPOM != null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<pom>\u00EF\u00BF\u00BDm.mavenPOM.normalize\u00EF\u00BF\u00BD</pom>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF m.mavenProperties != null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<properties>\u00EF\u00BF\u00BDm.mavenProperties\u00EF\u00BF\u00BD</properties>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<usePrivateRepository>\u00EF\u00BF\u00BDm.mavenPrivateRepo\u00EF\u00BF\u00BD</usePrivateRepository>");
    _builder.newLine();
    _builder.append("</hudson.tasks.Maven>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _build(final Shell s) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.tasks.Shell>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<command>\u00EF\u00BF\u00BDs.shellScript.normalize\u00EF\u00BF\u00BD</command>");
    _builder.newLine();
    _builder.append("</hudson.tasks.Shell>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _build(final Batch b) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.tasks.BatchFile>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<command>\u00EF\u00BF\u00BDb.batchScript.normalize\u00EF\u00BF\u00BD</command>");
    _builder.newLine();
    _builder.append("</hudson.tasks.BatchFile>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _build(final Ant a) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.tasks.Ant>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<targets></targets>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<antName>\u00EF\u00BF\u00BDa.version.name\u00EF\u00BF\u00BD</antName>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<buildFile>\u00EF\u00BF\u00BDa.buildFile\u00EF\u00BF\u00BD</buildFile>");
    _builder.newLine();
    _builder.append("</hudson.tasks.Ant>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _build(final SystemGroovy sg) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.groovy.SystemGroovy>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<scriptSource class=\"hudson.plugins.groovy.StringScriptSource\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<command>\u00EF\u00BF\u00BDsg.groovyScript.normalize\u00EF\u00BF\u00BD</command>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</scriptSource>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<bindings></bindings>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<classpath></classpath>");
    _builder.newLine();
    _builder.append("</hudson.plugins.groovy.SystemGroovy>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _build(final TriggerBuilderSection tbs) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.parameterizedtrigger.TriggerBuilder>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<configs>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDFOR tb:tbs.triggeredBuilds\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDtriggeredBuild(tb)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</configs>");
    _builder.newLine();
    _builder.append("</hudson.plugins.parameterizedtrigger.TriggerBuilder>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence triggeredBuild(final TriggeredBuild tb) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.parameterizedtrigger.BlockableBuildTriggerConfig>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<configs>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDFOR p:tb.triggerParams\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDtriggerParam(p)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</configs>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<projects>\u00EF\u00BF\u00BDtb.builds.fqn\u00EF\u00BF\u00BD</projects>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<condition>ALWAYS</condition>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<triggerWithNoParameters>false</triggerWithNoParameters>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<block>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<buildStepFailureThreshold>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<name>UNSTABLE</name>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<ordinal>1</ordinal>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<color>YELLOW</color>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</buildStepFailureThreshold>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<unstableThreshold>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<name>UNSTABLE</name>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<ordinal>1</ordinal>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<color>YELLOW</color>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</unstableThreshold>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<failureThreshold>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<name>FAILURE</name>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<ordinal>2</ordinal>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<color>RED</color>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</failureThreshold>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</block>");
    _builder.newLine();
    _builder.append("</hudson.plugins.parameterizedtrigger.BlockableBuildTriggerConfig>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence publishers(final Config c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<publishers>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDval m = new LinkedHashMap<EClass, EObject>()\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDFOR p:getAllPublishers(c, m).values\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDpublisher(p)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("</publishers>");
    _builder.newLine();
    return _builder;
  }
  
  public String getTo(final ExtMail em) {
    String _xblockexpression = null;
    {
      boolean _isMergeWithSuperConfig = em.isMergeWithSuperConfig();
      boolean _operator_equals = BooleanExtensions.operator_equals(_isMergeWithSuperConfig, true);
      if (_operator_equals) {
        {
          ExtMail _parentExtMail = this.getParentExtMail(em);
          final ExtMail pm = _parentExtMail;
          boolean _operator_notEquals = ObjectExtensions.operator_notEquals(pm, null);
          if (_operator_notEquals) {
            String _to = em.getTo();
            String _operator_plus = StringExtensions.operator_plus(_to, " ");
            String _to_1 = this.getTo(pm);
            String _operator_plus_1 = StringExtensions.operator_plus(_operator_plus, _to_1);
            return _operator_plus_1;
          }
        }
      }
      String _to_2 = em.getTo();
      _xblockexpression = (_to_2);
    }
    return _xblockexpression;
  }
  
  public String getSubject(final ExtMail em) {
    String _xblockexpression = null;
    {
      boolean _operator_and = false;
      MailConfig _mailConfig = em.getMailConfig();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_mailConfig, null);
      if (!_operator_notEquals) {
        _operator_and = false;
      } else {
        MailConfig _mailConfig_1 = em.getMailConfig();
        String _subject = _mailConfig_1.getSubject();
        boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_subject, null);
        _operator_and = BooleanExtensions.operator_and(_operator_notEquals, _operator_notEquals_1);
      }
      if (_operator_and) {
        MailConfig _mailConfig_2 = em.getMailConfig();
        String _subject_1 = _mailConfig_2.getSubject();
        return _subject_1;
      }
      String _xifexpression = null;
      boolean _isMergeWithSuperConfig = em.isMergeWithSuperConfig();
      boolean _operator_equals = BooleanExtensions.operator_equals(_isMergeWithSuperConfig, true);
      if (_operator_equals) {
        String _xblockexpression_1 = null;
        {
          ExtMail _parentExtMail = this.getParentExtMail(em);
          final ExtMail pm = _parentExtMail;
          String _xifexpression_1 = null;
          boolean _operator_notEquals_2 = ObjectExtensions.operator_notEquals(pm, null);
          if (_operator_notEquals_2) {
            String _subject_2 = this.getSubject(pm);
            return _subject_2;
          }
          _xblockexpression_1 = (_xifexpression_1);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public String getContent(final ExtMail em) {
    String _xblockexpression = null;
    {
      boolean _operator_and = false;
      MailConfig _mailConfig = em.getMailConfig();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_mailConfig, null);
      if (!_operator_notEquals) {
        _operator_and = false;
      } else {
        MailConfig _mailConfig_1 = em.getMailConfig();
        String _content = _mailConfig_1.getContent();
        boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_content, null);
        _operator_and = BooleanExtensions.operator_and(_operator_notEquals, _operator_notEquals_1);
      }
      if (_operator_and) {
        MailConfig _mailConfig_2 = em.getMailConfig();
        String _content_1 = _mailConfig_2.getContent();
        return _content_1;
      }
      String _xifexpression = null;
      boolean _isMergeWithSuperConfig = em.isMergeWithSuperConfig();
      boolean _operator_equals = BooleanExtensions.operator_equals(_isMergeWithSuperConfig, true);
      if (_operator_equals) {
        String _xblockexpression_1 = null;
        {
          ExtMail _parentExtMail = this.getParentExtMail(em);
          final ExtMail pm = _parentExtMail;
          String _xifexpression_1 = null;
          boolean _operator_notEquals_2 = ObjectExtensions.operator_notEquals(pm, null);
          if (_operator_notEquals_2) {
            String _content_2 = this.getContent(pm);
            return _content_2;
          }
          _xblockexpression_1 = (_xifexpression_1);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public String getAttachments(final ExtMail em) {
    String _xblockexpression = null;
    {
      boolean _operator_and = false;
      MailConfig _mailConfig = em.getMailConfig();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_mailConfig, null);
      if (!_operator_notEquals) {
        _operator_and = false;
      } else {
        MailConfig _mailConfig_1 = em.getMailConfig();
        String _attachments = _mailConfig_1.getAttachments();
        boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_attachments, null);
        _operator_and = BooleanExtensions.operator_and(_operator_notEquals, _operator_notEquals_1);
      }
      if (_operator_and) {
        MailConfig _mailConfig_2 = em.getMailConfig();
        String _attachments_1 = _mailConfig_2.getAttachments();
        return _attachments_1;
      }
      String _xifexpression = null;
      boolean _isMergeWithSuperConfig = em.isMergeWithSuperConfig();
      boolean _operator_equals = BooleanExtensions.operator_equals(_isMergeWithSuperConfig, true);
      if (_operator_equals) {
        String _xblockexpression_1 = null;
        {
          ExtMail _parentExtMail = this.getParentExtMail(em);
          final ExtMail pm = _parentExtMail;
          String _xifexpression_1 = null;
          boolean _operator_notEquals_2 = ObjectExtensions.operator_notEquals(pm, null);
          if (_operator_notEquals_2) {
            String _attachments_2 = this.getAttachments(pm);
            return _attachments_2;
          }
          _xblockexpression_1 = (_xifexpression_1);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public ExtMail getParentExtMail(final ExtMail em) {
      Config _myConfig = this.getMyConfig(em);
      Config c = _myConfig;
      Config _parentConfig = c.getParentConfig();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_parentConfig, null);
      boolean _while = _operator_notEquals;
      while (_while) {
        {
          Config _parentConfig_1 = c.getParentConfig();
          PublisherSection _publisherSection = _parentConfig_1.getPublisherSection();
          boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_publisherSection, null);
          if (_operator_notEquals_1) {
            Config _parentConfig_2 = c.getParentConfig();
            PublisherSection _publisherSection_1 = _parentConfig_2.getPublisherSection();
            EList<EObject> _publishers = _publisherSection_1.getPublishers();
            for (final EObject p : _publishers) {
              if ((p instanceof ExtMail)) {
                return ((ExtMail) p);
              }
            }
          }
          Config _parentConfig_3 = c.getParentConfig();
          c = _parentConfig_3;
        }
        Config _parentConfig_4 = c.getParentConfig();
        boolean _operator_notEquals_2 = ObjectExtensions.operator_notEquals(_parentConfig_4, null);
        _while = _operator_notEquals_2;
      }
      return null;
  }
  
  public Map<String,MailTrigger> getAllMailTriggers(final ExtMail em, final Map<String,MailTrigger> m) {
      EList<MailTrigger> _mailTrigger = em.getMailTrigger();
      for (final MailTrigger mt : _mailTrigger) {
        String _type = mt.getType();
        boolean _containsKey = m.containsKey(_type);
        boolean _operator_not = BooleanExtensions.operator_not(_containsKey);
        if (_operator_not) {
          String _type_1 = mt.getType();
          m.put(_type_1, mt);
        }
      }
      boolean _isMergeWithSuperConfig = em.isMergeWithSuperConfig();
      boolean _operator_equals = BooleanExtensions.operator_equals(_isMergeWithSuperConfig, true);
      if (_operator_equals) {
        {
          ExtMail _parentExtMail = this.getParentExtMail(em);
          final ExtMail pm = _parentExtMail;
          boolean _operator_notEquals = ObjectExtensions.operator_notEquals(pm, null);
          if (_operator_notEquals) {
            this.getAllMailTriggers(pm, m);
          }
        }
      }
      return m;
  }
  
  protected CharSequence _publisher(final ExtMail em) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.emailext.ExtendedEmailPublisher>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<recipientList>\u00EF\u00BF\u00BDgetTo(em)\u00EF\u00BF\u00BD</recipientList>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<configuredTriggers>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDval m = new LinkedHashMap<String, MailTrigger>()\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDFOR mt:getAllMailTriggers(em, m).values\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDmailTrigger(mt)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</configuredTriggers>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF em.type == null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<contentType>default</contentType>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<contentType>\u00EF\u00BF\u00BDem.type\u00EF\u00BF\u00BD</contentType>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDval subject = getSubject(em)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF subject == null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<defaultSubject>$DEFAULT_SUBJECT</defaultSubject>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<defaultSubject>\u00EF\u00BF\u00BDsubject\u00EF\u00BF\u00BD</defaultSubject>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDval content = getContent(em)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF content == null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<defaultContent>$DEFAULT_CONTENT</defaultContent>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<defaultContent>\u00EF\u00BF\u00BDcontent\u00EF\u00BF\u00BD</defaultContent>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<attachmentsPattern>\u00EF\u00BF\u00BDgetAttachments(em)\u00EF\u00BF\u00BD</attachmentsPattern>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF em.mailConfig != null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDmailConfig(em.mailConfig)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("</hudson.plugins.emailext.ExtendedEmailPublisher>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence mailTrigger(final MailTrigger mt) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.emailext.plugins.trigger.\u00EF\u00BF\u00BDmt.type.replace(\"-\", \"\")\u00EF\u00BF\u00BDTrigger>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<email>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDIF mt.to == null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<recipientList></recipientList>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<recipientList>\u00EF\u00BF\u00BDmt.to\u00EF\u00BF\u00BD</recipientList>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDIF mt.mailConfig != null && mt.mailConfig.subject == null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<subject>$PROJECT_DEFAULT_SUBJECT</subject>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<subject>\u00EF\u00BF\u00BDmt.mailConfig.subject\u00EF\u00BF\u00BD</subject>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDIF mt.mailConfig != null && mt.mailConfig.content == null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<body>$PROJECT_DEFAULT_CONTENT</body>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<body>\u00EF\u00BF\u00BDmt.mailConfig.content\u00EF\u00BF\u00BD</body>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<sendToDevelopers>\u00EF\u00BF\u00BDmt.toCommiter\u00EF\u00BF\u00BD</sendToDevelopers>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<sendToRequester>\u00EF\u00BF\u00BDmt.toRequester\u00EF\u00BF\u00BD</sendToRequester>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<includeCulprits>\u00EF\u00BF\u00BDmt.toCulprits\u00EF\u00BF\u00BD</includeCulprits>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<sendToRecipientList>\u00EF\u00BF\u00BDmt.toList\u00EF\u00BF\u00BD</sendToRecipientList>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDIF mt.mailConfig != null\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<attachmentsPattern>\u00EF\u00BF\u00BDmt.mailConfig.attachments\u00EF\u00BF\u00BD</attachmentsPattern>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDmailConfig(mt.mailConfig)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</email>");
    _builder.newLine();
    _builder.append("</hudson.plugins.emailext.plugins.trigger.\u00EF\u00BF\u00BDmt.type.replace(\"-\", \"\")\u00EF\u00BF\u00BDTrigger>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence mailConfig(final MailConfig mc) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<attachBuildLog>\u00EF\u00BF\u00BDmc.attachBuildLog\u00EF\u00BF\u00BD</attachBuildLog>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final TestResult t) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\u00EF\u00BF\u00BDIF isNotEmpty(t.testresults)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("<hudson.tasks.junit.JUnitResultArchiver>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<testResults>\u00EF\u00BF\u00BDt.testresults.normalize\u00EF\u00BF\u00BD</testResults>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<keepLongStdio>\u00EF\u00BF\u00BDt.longIO\u00EF\u00BF\u00BD</keepLongStdio>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<testDataPublishers>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF t.claim\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<hudson.plugins.claim.ClaimTestDataPublisher/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</testDataPublishers>");
    _builder.newLine();
    _builder.append("</hudson.tasks.junit.JUnitResultArchiver>");
    _builder.newLine();
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final DownStream d) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.parameterizedtrigger.BuildTrigger>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<configs>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDFOR b:d.builds\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDdownStreamBuild(b)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</configs>");
    _builder.newLine();
    _builder.append("</hudson.plugins.parameterizedtrigger.BuildTrigger>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final Artifacts a) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.tasks.ArtifactArchiver>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<artifacts>\u00EF\u00BF\u00BDa.artifacts.normalize\u00EF\u00BF\u00BD</artifacts>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF false\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<latestOnly>true</latestOnly>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<latestOnly>false</latestOnly>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("</hudson.tasks.ArtifactArchiver>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final GitPublisher g) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.git.GitPublisher>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<configVersion>2</configVersion>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<pushMerge>false</pushMerge>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<pushOnlyIfSuccess>\u00EF\u00BF\u00BDg.onlyOnSuccess\u00EF\u00BF\u00BD</pushOnlyIfSuccess>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<branchesToPush>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<hudson.plugins.git.GitPublisher_-BranchToPush>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<targetRepoName>\u00EF\u00BF\u00BDg.origin\u00EF\u00BF\u00BD</targetRepoName>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<branchName>\u00EF\u00BF\u00BDg.branch\u00EF\u00BF\u00BD</branchName>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</hudson.plugins.git.GitPublisher_-BranchToPush>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</branchesToPush>");
    _builder.newLine();
    _builder.append("</hudson.plugins.git.GitPublisher>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final Gatling g) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<com.excilys.ebi.gatling.jenkins.GatlingPublisher plugin=\"gatling@1.0.0\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<simulation>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<name>\u00EF\u00BF\u00BDg.resultprefix.normalize\u00EF\u00BF\u00BD</name>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</simulation>");
    _builder.newLine();
    _builder.append("</com.excilys.ebi.gatling.jenkins.GatlingPublisher>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final Violations v) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.violations.ViolationsPublisher>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<config>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<suppressions class=\"tree-set\">");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<no-comparator/>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</suppressions>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<typeConfigs>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<no-comparator/>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("\u00EF\u00BF\u00BDFOR vc:v.violations\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("\u00EF\u00BF\u00BDviolationsConfig(vc)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</typeConfigs>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<limit>100</limit>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<sourcePathPattern></sourcePathPattern>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<fauxProjectPath></fauxProjectPath>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<encoding>default</encoding>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</config>");
    _builder.newLine();
    _builder.append("</hudson.plugins.violations.ViolationsPublisher>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence violationsConfig(final ViolationsConfig vc) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<entry>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<string>checkstyle</string>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<hudson.plugins.violations.TypeConfig>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<type>\u00EF\u00BF\u00BDvc.type\u00EF\u00BF\u00BD</type>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<min>\u00EF\u00BF\u00BDvc.min\u00EF\u00BF\u00BD</min>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<max>\u00EF\u00BF\u00BDvc.max\u00EF\u00BF\u00BD</max>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<unstable>\u00EF\u00BF\u00BDvc.unstable\u00EF\u00BF\u00BD</unstable>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<usePattern>false</usePattern>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<pattern>\u00EF\u00BF\u00BDvc.pattern\u00EF\u00BF\u00BD</pattern>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</hudson.plugins.violations.TypeConfig>");
    _builder.newLine();
    _builder.append("</entry>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final HTMLPublisher h) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<htmlpublisher.HtmlPublisher>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<reportTargets>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<htmlpublisher.HtmlPublisherTarget>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<reportName>\u00EF\u00BF\u00BDh.name\u00EF\u00BF\u00BD</reportName>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<reportDir>\u00EF\u00BF\u00BDh.dir\u00EF\u00BF\u00BD</reportDir>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<reportFiles>\u00EF\u00BF\u00BDh.files\u00EF\u00BF\u00BD</reportFiles>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<keepAll>\u00EF\u00BF\u00BDh.keepPast\u00EF\u00BF\u00BD</keepAll>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<wrapperName>htmlpublisher-wrapper.html</wrapperName>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</htmlpublisher.HtmlPublisherTarget>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</reportTargets>");
    _builder.newLine();
    _builder.append("</htmlpublisher.HtmlPublisher>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final Warnings w) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.warnings.WarningsPublisher>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<healthy></healthy>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<unHealthy></unHealthy>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<thresholdLimit>low</thresholdLimit>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<pluginName>[WARNINGS] </pluginName>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<defaultEncoding></defaultEncoding>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<canRunOnFailed>false</canRunOnFailed>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<useDeltaValues>false</useDeltaValues>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<thresholds>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDIF w.unstableTotalAll > 0\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<unstableTotalAll>\u00EF\u00BF\u00BDw.unstableTotalAll\u00EF\u00BF\u00BD</unstableTotalAll>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<unstableTotalAll></unstableTotalAll>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<unstableTotalHigh></unstableTotalHigh>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<unstableTotalNormal></unstableTotalNormal>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<unstableTotalLow></unstableTotalLow>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<unstableNewAll></unstableNewAll>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<unstableNewHigh></unstableNewHigh>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<unstableNewNormal></unstableNewNormal>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<unstableNewLow></unstableNewLow>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDIF w.failTotalAll > 0\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<failedTotalAll>\u00EF\u00BF\u00BDw.failTotalAll\u00EF\u00BF\u00BD</failedTotalAll>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<failedTotalAll></failedTotalAll>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<failedTotalHigh></failedTotalHigh>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<failedTotalNormal></failedTotalNormal>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<failedTotalLow></failedTotalLow>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<failedNewAll></failedNewAll>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<failedNewHigh></failedNewHigh>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<failedNewNormal></failedNewNormal>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<failedNewLow></failedNewLow>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</thresholds>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<shouldDetectModules>false</shouldDetectModules>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<dontComputeNew>true</dontComputeNew>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<parserConfigurations/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<consoleLogParsers>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<string>\u00EF\u00BF\u00BDw.parser.name\u00EF\u00BF\u00BD</string>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</consoleLogParsers>");
    _builder.newLine();
    _builder.append("</hudson.plugins.warnings.WarningsPublisher>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final Claim c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.claim.ClaimPublisher/>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final HipChat h) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<jenkins.plugins.hipchat.HipChatPublisher>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<room>\u00EF\u00BF\u00BDh.room\u00EF\u00BF\u00BD</room>");
    _builder.newLine();
    _builder.append("</jenkins.plugins.hipchat.HipChatPublisher>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final PlayAutoTestReport p) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<com.gmail.ikeike443.PlayTestResultPublisher/>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final JaCoCo j) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.jacoco.JacocoPublisher>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<execPattern>\u00EF\u00BF\u00BDj.execPattern\u00EF\u00BF\u00BD</execPattern>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<classPattern>\u00EF\u00BF\u00BDj.classPattern\u00EF\u00BF\u00BD</classPattern>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<sourcePattern>\u00EF\u00BF\u00BDj.sourcePattern\u00EF\u00BF\u00BD</sourcePattern>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<inclusionPattern>\u00EF\u00BF\u00BDj.inclusionPattern\u00EF\u00BF\u00BD</inclusionPattern>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<exclusionPattern>\u00EF\u00BF\u00BDj.exclusionPattern\u00EF\u00BF\u00BD</exclusionPattern>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<minimumInstructionCoverage>\u00EF\u00BF\u00BDj.minimumInstructionCoverage\u00EF\u00BF\u00BD</minimumInstructionCoverage>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<minimumBranchCoverage>\u00EF\u00BF\u00BDj.minimumBranchCoverage\u00EF\u00BF\u00BD</minimumBranchCoverage>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<minimumComplexityCoverage>\u00EF\u00BF\u00BDj.minimumComplexityCoverage\u00EF\u00BF\u00BD</minimumComplexityCoverage>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<minimumLineCoverage>\u00EF\u00BF\u00BDj.minimumLineCoverage\u00EF\u00BF\u00BD</minimumLineCoverage>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<minimumMethodCoverage>\u00EF\u00BF\u00BDj.minimumMethodCoverage\u00EF\u00BF\u00BD</minimumMethodCoverage>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<minimumClassCoverage>\u00EF\u00BF\u00BDj.minimumClassCoverage\u00EF\u00BF\u00BD</minimumClassCoverage>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<maximumInstructionCoverage>\u00EF\u00BF\u00BDj.maximumInstructionCoverage\u00EF\u00BF\u00BD</maximumInstructionCoverage>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<maximumBranchCoverage>\u00EF\u00BF\u00BDj.maximumBranchCoverage\u00EF\u00BF\u00BD</maximumBranchCoverage>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<maximumComplexityCoverage>\u00EF\u00BF\u00BDj.maximumComplexityCoverage\u00EF\u00BF\u00BD</maximumComplexityCoverage>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<maximumLineCoverage>\u00EF\u00BF\u00BDj.maximumLineCoverage\u00EF\u00BF\u00BD</maximumLineCoverage>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<maximumMethodCoverage>\u00EF\u00BF\u00BDj.maximumMethodCoverage\u00EF\u00BF\u00BD</maximumMethodCoverage>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<maximumClassCoverage>\u00EF\u00BF\u00BDj.maximumClassCoverage\u00EF\u00BF\u00BD</maximumClassCoverage>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<changeBuildStatus>\u00EF\u00BF\u00BDj.changeBuildStatus\u00EF\u00BF\u00BD</changeBuildStatus>");
    _builder.newLine();
    _builder.append("</hudson.plugins.jacoco.JacocoPublisher>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final Checkstyle c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.checkstyle.CheckStylePublisher>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<healthy>");
    int _healthy = c.getHealthy();
    _builder.append(_healthy, "  ");
    _builder.append("</healthy>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<unHealthy>");
    int _unHealthy = c.getUnHealthy();
    _builder.append(_unHealthy, "  ");
    _builder.append("</unHealthy>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<thresholdLimit>");
    String _thresholdLimit = c.getThresholdLimit();
    _builder.append(_thresholdLimit, "  ");
    _builder.append("</thresholdLimit>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<pluginName>[CHECKSTYLE] </pluginName>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<defaultEncoding>");
    String _defaultEncoding = c.getDefaultEncoding();
    _builder.append(_defaultEncoding, "  ");
    _builder.append("</defaultEncoding>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<canRunOnFailed>");
    boolean _isCanRunOnFailed = c.isCanRunOnFailed();
    _builder.append(_isCanRunOnFailed, "  ");
    _builder.append("</canRunOnFailed>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<useStableBuildAsReference>");
    boolean _isUseStableBuildAsReference = c.isUseStableBuildAsReference();
    _builder.append(_isUseStableBuildAsReference, "  ");
    _builder.append("</useStableBuildAsReference>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<useDeltaValues>");
    boolean _isUseDeltaValues = c.isUseDeltaValues();
    _builder.append(_isUseDeltaValues, "  ");
    _builder.append("</useDeltaValues>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<thresholds>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<unstableTotalAll>");
    Thresholds _thresholds = c.getThresholds();
    int _unstableTotalAll = _thresholds.getUnstableTotalAll();
    _builder.append(_unstableTotalAll, "    ");
    _builder.append("</unstableTotalAll>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalHigh>");
    Thresholds _thresholds_1 = c.getThresholds();
    int _unstableTotalHigh = _thresholds_1.getUnstableTotalHigh();
    _builder.append(_unstableTotalHigh, "    ");
    _builder.append("</unstableTotalHigh>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalNormal>");
    Thresholds _thresholds_2 = c.getThresholds();
    int _unstableTotalNormal = _thresholds_2.getUnstableTotalNormal();
    _builder.append(_unstableTotalNormal, "    ");
    _builder.append("</unstableTotalNormal>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalLow>");
    Thresholds _thresholds_3 = c.getThresholds();
    int _unstableTotalLow = _thresholds_3.getUnstableTotalLow();
    _builder.append(_unstableTotalLow, "    ");
    _builder.append("</unstableTotalLow>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalAll>");
    Thresholds _thresholds_4 = c.getThresholds();
    int _failedTotalAll = _thresholds_4.getFailedTotalAll();
    _builder.append(_failedTotalAll, "    ");
    _builder.append("</failedTotalAll>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalHigh>");
    Thresholds _thresholds_5 = c.getThresholds();
    int _failedTotalHigh = _thresholds_5.getFailedTotalHigh();
    _builder.append(_failedTotalHigh, "    ");
    _builder.append("</failedTotalHigh>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalNormal>");
    Thresholds _thresholds_6 = c.getThresholds();
    int _failedTotalNormal = _thresholds_6.getFailedTotalNormal();
    _builder.append(_failedTotalNormal, "    ");
    _builder.append("</failedTotalNormal>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalLow>");
    Thresholds _thresholds_7 = c.getThresholds();
    int _failedTotalLow = _thresholds_7.getFailedTotalLow();
    _builder.append(_failedTotalLow, "    ");
    _builder.append("</failedTotalLow>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("</thresholds>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<shouldDetectModules>");
    boolean _isShouldDetectModules = c.isShouldDetectModules();
    _builder.append(_isShouldDetectModules, "  ");
    _builder.append("</shouldDetectModules>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<dontComputeNew>");
    boolean _isDontComputeNew = c.isDontComputeNew();
    _builder.append(_isDontComputeNew, "  ");
    _builder.append("</dontComputeNew>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<doNotResolveRelativePaths>");
    boolean _isDoNotResolveRelativePaths = c.isDoNotResolveRelativePaths();
    _builder.append(_isDoNotResolveRelativePaths, "  ");
    _builder.append("</doNotResolveRelativePaths>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<pattern>");
    String _pattern = c.getPattern();
    _builder.append(_pattern, "  ");
    _builder.append("</pattern>");
    _builder.newLineIfNotEmpty();
    _builder.append("</hudson.plugins.checkstyle.CheckStylePublisher>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final PMD p) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.checkstyle.CheckStylePublisher>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<healthy>");
    int _healthy = p.getHealthy();
    _builder.append(_healthy, "  ");
    _builder.append("</healthy>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<unHealthy>");
    int _unHealthy = p.getUnHealthy();
    _builder.append(_unHealthy, "  ");
    _builder.append("</unHealthy>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<thresholdLimit>");
    String _thresholdLimit = p.getThresholdLimit();
    _builder.append(_thresholdLimit, "  ");
    _builder.append("</thresholdLimit>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<pluginName>[CHECKSTYLE] </pluginName>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<defaultEncoding>");
    String _defaultEncoding = p.getDefaultEncoding();
    _builder.append(_defaultEncoding, "  ");
    _builder.append("</defaultEncoding>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<canRunOnFailed>");
    boolean _isCanRunOnFailed = p.isCanRunOnFailed();
    _builder.append(_isCanRunOnFailed, "  ");
    _builder.append("</canRunOnFailed>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<useStableBuildAsReference>");
    boolean _isUseStableBuildAsReference = p.isUseStableBuildAsReference();
    _builder.append(_isUseStableBuildAsReference, "  ");
    _builder.append("</useStableBuildAsReference>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<useDeltaValues>");
    boolean _isUseDeltaValues = p.isUseDeltaValues();
    _builder.append(_isUseDeltaValues, "  ");
    _builder.append("</useDeltaValues>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<thresholds>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<unstableTotalAll>");
    Thresholds _thresholds = p.getThresholds();
    int _unstableTotalAll = _thresholds.getUnstableTotalAll();
    _builder.append(_unstableTotalAll, "    ");
    _builder.append("</unstableTotalAll>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalHigh>");
    Thresholds _thresholds_1 = p.getThresholds();
    int _unstableTotalHigh = _thresholds_1.getUnstableTotalHigh();
    _builder.append(_unstableTotalHigh, "    ");
    _builder.append("</unstableTotalHigh>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalNormal>");
    Thresholds _thresholds_2 = p.getThresholds();
    int _unstableTotalNormal = _thresholds_2.getUnstableTotalNormal();
    _builder.append(_unstableTotalNormal, "    ");
    _builder.append("</unstableTotalNormal>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalLow>");
    Thresholds _thresholds_3 = p.getThresholds();
    int _unstableTotalLow = _thresholds_3.getUnstableTotalLow();
    _builder.append(_unstableTotalLow, "    ");
    _builder.append("</unstableTotalLow>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalAll>");
    Thresholds _thresholds_4 = p.getThresholds();
    int _failedTotalAll = _thresholds_4.getFailedTotalAll();
    _builder.append(_failedTotalAll, "    ");
    _builder.append("</failedTotalAll>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalHigh>");
    Thresholds _thresholds_5 = p.getThresholds();
    int _failedTotalHigh = _thresholds_5.getFailedTotalHigh();
    _builder.append(_failedTotalHigh, "    ");
    _builder.append("</failedTotalHigh>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalNormal>");
    Thresholds _thresholds_6 = p.getThresholds();
    int _failedTotalNormal = _thresholds_6.getFailedTotalNormal();
    _builder.append(_failedTotalNormal, "    ");
    _builder.append("</failedTotalNormal>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalLow>");
    Thresholds _thresholds_7 = p.getThresholds();
    int _failedTotalLow = _thresholds_7.getFailedTotalLow();
    _builder.append(_failedTotalLow, "    ");
    _builder.append("</failedTotalLow>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("</thresholds>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<shouldDetectModules>");
    boolean _isShouldDetectModules = p.isShouldDetectModules();
    _builder.append(_isShouldDetectModules, "  ");
    _builder.append("</shouldDetectModules>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<dontComputeNew>");
    boolean _isDontComputeNew = p.isDontComputeNew();
    _builder.append(_isDontComputeNew, "  ");
    _builder.append("</dontComputeNew>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<doNotResolveRelativePaths>");
    boolean _isDoNotResolveRelativePaths = p.isDoNotResolveRelativePaths();
    _builder.append(_isDoNotResolveRelativePaths, "  ");
    _builder.append("</doNotResolveRelativePaths>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<pattern>");
    String _pattern = p.getPattern();
    _builder.append(_pattern, "  ");
    _builder.append("</pattern>");
    _builder.newLineIfNotEmpty();
    _builder.append("</hudson.plugins.checkstyle.CheckStylePublisher>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final FindBugs f) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.checkstyle.CheckStylePublisher>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<healthy>");
    int _healthy = f.getHealthy();
    _builder.append(_healthy, "  ");
    _builder.append("</healthy>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<unHealthy>");
    int _unHealthy = f.getUnHealthy();
    _builder.append(_unHealthy, "  ");
    _builder.append("</unHealthy>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<thresholdLimit>");
    String _thresholdLimit = f.getThresholdLimit();
    _builder.append(_thresholdLimit, "  ");
    _builder.append("</thresholdLimit>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<pluginName>[CHECKSTYLE] </pluginName>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<defaultEncoding>");
    String _defaultEncoding = f.getDefaultEncoding();
    _builder.append(_defaultEncoding, "  ");
    _builder.append("</defaultEncoding>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<canRunOnFailed>");
    boolean _isCanRunOnFailed = f.isCanRunOnFailed();
    _builder.append(_isCanRunOnFailed, "  ");
    _builder.append("</canRunOnFailed>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<useStableBuildAsReference>");
    boolean _isUseStableBuildAsReference = f.isUseStableBuildAsReference();
    _builder.append(_isUseStableBuildAsReference, "  ");
    _builder.append("</useStableBuildAsReference>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<useDeltaValues>");
    boolean _isUseDeltaValues = f.isUseDeltaValues();
    _builder.append(_isUseDeltaValues, "  ");
    _builder.append("</useDeltaValues>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<thresholds>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<unstableTotalAll>");
    Thresholds _thresholds = f.getThresholds();
    int _unstableTotalAll = _thresholds.getUnstableTotalAll();
    _builder.append(_unstableTotalAll, "    ");
    _builder.append("</unstableTotalAll>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalHigh>");
    Thresholds _thresholds_1 = f.getThresholds();
    int _unstableTotalHigh = _thresholds_1.getUnstableTotalHigh();
    _builder.append(_unstableTotalHigh, "    ");
    _builder.append("</unstableTotalHigh>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalNormal>");
    Thresholds _thresholds_2 = f.getThresholds();
    int _unstableTotalNormal = _thresholds_2.getUnstableTotalNormal();
    _builder.append(_unstableTotalNormal, "    ");
    _builder.append("</unstableTotalNormal>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalLow>");
    Thresholds _thresholds_3 = f.getThresholds();
    int _unstableTotalLow = _thresholds_3.getUnstableTotalLow();
    _builder.append(_unstableTotalLow, "    ");
    _builder.append("</unstableTotalLow>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalAll>");
    Thresholds _thresholds_4 = f.getThresholds();
    int _failedTotalAll = _thresholds_4.getFailedTotalAll();
    _builder.append(_failedTotalAll, "    ");
    _builder.append("</failedTotalAll>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalHigh>");
    Thresholds _thresholds_5 = f.getThresholds();
    int _failedTotalHigh = _thresholds_5.getFailedTotalHigh();
    _builder.append(_failedTotalHigh, "    ");
    _builder.append("</failedTotalHigh>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalNormal>");
    Thresholds _thresholds_6 = f.getThresholds();
    int _failedTotalNormal = _thresholds_6.getFailedTotalNormal();
    _builder.append(_failedTotalNormal, "    ");
    _builder.append("</failedTotalNormal>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalLow>");
    Thresholds _thresholds_7 = f.getThresholds();
    int _failedTotalLow = _thresholds_7.getFailedTotalLow();
    _builder.append(_failedTotalLow, "    ");
    _builder.append("</failedTotalLow>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("</thresholds>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<shouldDetectModules>");
    boolean _isShouldDetectModules = f.isShouldDetectModules();
    _builder.append(_isShouldDetectModules, "  ");
    _builder.append("</shouldDetectModules>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<dontComputeNew>");
    boolean _isDontComputeNew = f.isDontComputeNew();
    _builder.append(_isDontComputeNew, "  ");
    _builder.append("</dontComputeNew>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<doNotResolveRelativePaths>");
    boolean _isDoNotResolveRelativePaths = f.isDoNotResolveRelativePaths();
    _builder.append(_isDoNotResolveRelativePaths, "  ");
    _builder.append("</doNotResolveRelativePaths>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<pattern>");
    String _pattern = f.getPattern();
    _builder.append(_pattern, "  ");
    _builder.append("</pattern>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<isRankActivated>");
    boolean _isIsRankActivated = f.isIsRankActivated();
    _builder.append(_isIsRankActivated, "  ");
    _builder.append("</isRankActivated>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<excludePattern>");
    String _excludePattern = f.getExcludePattern();
    _builder.append(_excludePattern, "  ");
    _builder.append("</excludePattern>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<includePattern>");
    String _includePattern = f.getIncludePattern();
    _builder.append(_includePattern, "  ");
    _builder.append("</includePattern>");
    _builder.newLineIfNotEmpty();
    _builder.append("</hudson.plugins.checkstyle.CheckStylePublisher>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final Cobertura c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.cobertura.CoberturaPublisher>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<coberturaReportFile>\u00EF\u00BF\u00BDc.xmlreport\u00EF\u00BF\u00BD</coberturaReportFile>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<onlyStable>\u00EF\u00BF\u00BDc.onlyStable\u00EF\u00BF\u00BD</onlyStable>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<healthyTarget>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<targets class=\"enum-map\" enum-type=\"hudson.plugins.cobertura.targets.CoverageMetric\">");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<entry>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<hudson.plugins.cobertura.targets.CoverageMetric>LINE</hudson.plugins.cobertura.targets.CoverageMetric>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<int>80</int>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("</entry>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</targets>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</healthyTarget>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<unhealthyTarget>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<targets class=\"enum-map\" enum-type=\"hudson.plugins.cobertura.targets.CoverageMetric\">");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<entry>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<hudson.plugins.cobertura.targets.CoverageMetric>LINE</hudson.plugins.cobertura.targets.CoverageMetric>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<int>0</int>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("</entry>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</targets>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</unhealthyTarget>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<failingTarget>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<targets class=\"enum-map\" enum-type=\"hudson.plugins.cobertura.targets.CoverageMetric\">");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<entry>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<hudson.plugins.cobertura.targets.CoverageMetric>LINE</hudson.plugins.cobertura.targets.CoverageMetric>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<int>0</int>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("</entry>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</targets>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</failingTarget>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<sourceEncoding>UTF_8</sourceEncoding>");
    _builder.newLine();
    _builder.append("</hudson.plugins.cobertura.CoberturaPublisher>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final Rcov r) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.rubyMetrics.rcov.RcovPublisher>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<reportDir>\u00EF\u00BF\u00BDr.reportDir\u00EF\u00BF\u00BD</reportDir>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<targets>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<hudson.plugins.rubyMetrics.rcov.model.MetricTarget>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<metric>TOTAL_COVERAGE</metric>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<healthy>80</healthy>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<unhealthy>0</unhealthy>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<unstable>0</unstable>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</hudson.plugins.rubyMetrics.rcov.model.MetricTarget>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<hudson.plugins.rubyMetrics.rcov.model.MetricTarget>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<metric>CODE_COVERAGE</metric>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<healthy>80</healthy>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<unhealthy>0</unhealthy>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<unstable>0</unstable>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</hudson.plugins.rubyMetrics.rcov.model.MetricTarget>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</targets>");
    _builder.newLine();
    _builder.append("</hudson.plugins.rubyMetrics.rcov.RcovPublisher>");
    _builder.newLine();
    return _builder;
  }
  
  public String getListOfFqNames(final List<Config> builds) {
      String s = "";
      boolean first = true;
      for (final Config c : builds) {
        {
          if (first) {
            first = false;
          } else {
            String _operator_plus = StringExtensions.operator_plus(s, "");
            s = _operator_plus;
          }
          String _fqn = this.fqn(c);
          String _operator_plus_1 = StringExtensions.operator_plus(s, _fqn);
          s = _operator_plus_1;
        }
      }
      return s;
  }
  
  public String translateCondition(final String c) {
    String _xblockexpression = null;
    {
      boolean _equals = "Stable".equals(c);
      if (_equals) {
        return "SUCCESS";
      }
      boolean _equals_1 = "Unstable".equals(c);
      if (_equals_1) {
        return "UNSTABLE";
      }
      boolean _equals_2 = "Not-Failed".equals(c);
      if (_equals_2) {
        return "UNSTABLE_OR_BETTER";
      }
      boolean _equals_3 = "Failed".equals(c);
      if (_equals_3) {
        return "FAILED";
      }
      String _xifexpression = null;
      boolean _equals_4 = "Complete".equals(c);
      if (_equals_4) {
        return "ALWAYS";
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public CharSequence downStreamBuild(final DownStreamBuild b) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.parameterizedtrigger.BuildTriggerConfig>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDIF b.triggerParams.empty\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<configs class=\"java.util.Collections$EmptyList\"/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<triggerWithNoParameters>true</triggerWithNoParameters>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDELSE\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<configs>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDFOR p:b.triggerParams\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDtriggerParam(p)\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\u00EF\u00BF\u00BDENDFOR\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</configs>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("\u00EF\u00BF\u00BDENDIF\u00EF\u00BF\u00BD");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<projects>\u00EF\u00BF\u00BDb.builds.fqn\u00EF\u00BF\u00BD</projects>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<condition>\u00EF\u00BF\u00BDb.condition.translateCondition\u00EF\u00BF\u00BD</condition>");
    _builder.newLine();
    _builder.append("</hudson.plugins.parameterizedtrigger.BuildTriggerConfig>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _triggerParam(final CurrentTriggerParams p) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  protected CharSequence _triggerParam(final GitCommitParam p) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.git.GitRevisionBuildParameters/>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _triggerParam(final PropertyFileTriggerParams p) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.parameterizedtrigger.FileBuildParameters>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<propertiesFile>\u00EF\u00BF\u00BDp.propertyFile\u00EF\u00BF\u00BD</propertiesFile>");
    _builder.newLine();
    _builder.append("</hudson.plugins.parameterizedtrigger.FileBuildParameters>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _triggerParam(final PredefinedTriggerParams p) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.parameterizedtrigger.PredefinedBuildParameters>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<properties>\u00EF\u00BF\u00BDp.predefined.normalize\u00EF\u00BF\u00BD</properties>");
    _builder.newLine();
    _builder.append("</hudson.plugins.parameterizedtrigger.PredefinedBuildParameters>");
    _builder.newLine();
    return _builder;
  }
  
  public String fqn(final EObject c) {
    if (c instanceof Config) {
      return _fqn((Config)c);
    } else if (c instanceof Group) {
      return _fqn((Group)c);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(c).toString());
    }
  }
  
  public CharSequence param(final Parameter p, final ParameterType b) {
    if (b instanceof BooleanParam) {
      return _param(p, (BooleanParam)b);
    } else if (b instanceof ChoiceParam) {
      return _param(p, (ChoiceParam)b);
    } else if (b instanceof StringParam) {
      return _param(p, (StringParam)b);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(p, b).toString());
    }
  }
  
  public CharSequence scm(final Scm cvs) {
    if (cvs instanceof ScmCVS) {
      return _scm((ScmCVS)cvs);
    } else if (cvs instanceof ScmGit) {
      return _scm((ScmGit)cvs);
    } else if (cvs instanceof ScmSVN) {
      return _scm((ScmSVN)cvs);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(cvs).toString());
    }
  }
  
  public CharSequence trigger(final EObject t) {
    if (t instanceof FirstStartTrigger) {
      return _trigger((FirstStartTrigger)t);
    } else if (t instanceof GitHubPushTrigger) {
      return _trigger((GitHubPushTrigger)t);
    } else if (t instanceof PollScmTrigger) {
      return _trigger((PollScmTrigger)t);
    } else if (t instanceof TimerTrigger) {
      return _trigger((TimerTrigger)t);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(t).toString());
    }
  }
  
  public CharSequence wrapper(final EObject a) {
    if (a instanceof AnsiColor) {
      return _wrapper((AnsiColor)a);
    } else if (a instanceof ExclusiveExecution) {
      return _wrapper((ExclusiveExecution)a);
    } else if (a instanceof Lock) {
      return _wrapper((Lock)a);
    } else if (a instanceof MatrixTieParent) {
      return _wrapper((MatrixTieParent)a);
    } else if (a instanceof Release) {
      return _wrapper((Release)a);
    } else if (a instanceof Timeout) {
      return _wrapper((Timeout)a);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(a).toString());
    }
  }
  
  public CharSequence build(final EObject a) {
    if (a instanceof Ant) {
      return _build((Ant)a);
    } else if (a instanceof Batch) {
      return _build((Batch)a);
    } else if (a instanceof Maven) {
      return _build((Maven)a);
    } else if (a instanceof Shell) {
      return _build((Shell)a);
    } else if (a instanceof SystemGroovy) {
      return _build((SystemGroovy)a);
    } else if (a instanceof TriggerBuilderSection) {
      return _build((TriggerBuilderSection)a);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(a).toString());
    }
  }
  
  public CharSequence publisher(final EObject a) {
    if (a instanceof Artifacts) {
      return _publisher((Artifacts)a);
    } else if (a instanceof Checkstyle) {
      return _publisher((Checkstyle)a);
    } else if (a instanceof Claim) {
      return _publisher((Claim)a);
    } else if (a instanceof Cobertura) {
      return _publisher((Cobertura)a);
    } else if (a instanceof DownStream) {
      return _publisher((DownStream)a);
    } else if (a instanceof ExtMail) {
      return _publisher((ExtMail)a);
    } else if (a instanceof FindBugs) {
      return _publisher((FindBugs)a);
    } else if (a instanceof Gatling) {
      return _publisher((Gatling)a);
    } else if (a instanceof GitPublisher) {
      return _publisher((GitPublisher)a);
    } else if (a instanceof HTMLPublisher) {
      return _publisher((HTMLPublisher)a);
    } else if (a instanceof HipChat) {
      return _publisher((HipChat)a);
    } else if (a instanceof JaCoCo) {
      return _publisher((JaCoCo)a);
    } else if (a instanceof PMD) {
      return _publisher((PMD)a);
    } else if (a instanceof PlayAutoTestReport) {
      return _publisher((PlayAutoTestReport)a);
    } else if (a instanceof Rcov) {
      return _publisher((Rcov)a);
    } else if (a instanceof TestResult) {
      return _publisher((TestResult)a);
    } else if (a instanceof Violations) {
      return _publisher((Violations)a);
    } else if (a instanceof Warnings) {
      return _publisher((Warnings)a);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(a).toString());
    }
  }
  
  public CharSequence triggerParam(final EObject p) {
    if (p instanceof CurrentTriggerParams) {
      return _triggerParam((CurrentTriggerParams)p);
    } else if (p instanceof GitCommitParam) {
      return _triggerParam((GitCommitParam)p);
    } else if (p instanceof PredefinedTriggerParams) {
      return _triggerParam((PredefinedTriggerParams)p);
    } else if (p instanceof PropertyFileTriggerParams) {
      return _triggerParam((PropertyFileTriggerParams)p);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(p).toString());
    }
  }
}
