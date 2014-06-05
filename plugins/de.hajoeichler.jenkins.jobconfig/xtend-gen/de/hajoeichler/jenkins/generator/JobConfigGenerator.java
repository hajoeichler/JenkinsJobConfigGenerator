package de.hajoeichler.jenkins.generator;

import de.hajoeichler.jenkins.jobConfig.AnsiColor;
import de.hajoeichler.jenkins.jobConfig.Ant;
import de.hajoeichler.jenkins.jobConfig.AntDecl;
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
import de.hajoeichler.jenkins.jobConfig.LockDecl;
import de.hajoeichler.jenkins.jobConfig.MailConfig;
import de.hajoeichler.jenkins.jobConfig.MailTrigger;
import de.hajoeichler.jenkins.jobConfig.Matrix;
import de.hajoeichler.jenkins.jobConfig.MatrixDecl;
import de.hajoeichler.jenkins.jobConfig.MatrixTieParent;
import de.hajoeichler.jenkins.jobConfig.Maven;
import de.hajoeichler.jenkins.jobConfig.MavenDecl;
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
import de.hajoeichler.jenkins.jobConfig.ScmRegions;
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
import de.hajoeichler.jenkins.jobConfig.WarningsDecl;
import de.hajoeichler.jenkins.jobConfig.WrapperSection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
import org.eclipse.xtext.xbase.lib.IntegerExtensions;
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
    {
      boolean _isMatrixJob = this.isMatrixJob(c);
      if (_isMatrixJob) {
        _builder.append("<matrix-project>");
        _builder.newLine();
      } else {
        _builder.append("<project>");
        _builder.newLine();
      }
    }
    _builder.append("  ");
    _builder.append("<actions/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<description>");
    String _description = c.getDescription();
    _builder.append(_description, "  ");
    _builder.append("</description>");
    _builder.newLineIfNotEmpty();
    {
      String _displayName = c.getDisplayName();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_displayName, null);
      if (_operator_notEquals) {
        _builder.append("  ");
        _builder.append("<displayName>");
        String _displayName_1 = c.getDisplayName();
        _builder.append(_displayName_1, "  ");
        _builder.append("</displayName>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      OldBuildHandling _anyOldBuildHandling = this.getAnyOldBuildHandling(c);
      boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_anyOldBuildHandling, null);
      if (_operator_notEquals_1) {
        _builder.append("  ");
        OldBuildHandling _anyOldBuildHandling_1 = this.getAnyOldBuildHandling(c);
        CharSequence _logRotator = this.logRotator(_anyOldBuildHandling_1);
        _builder.append(_logRotator, "  ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    _builder.append("<keepDependencies>false</keepDependencies>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<properties>");
    _builder.newLine();
    _builder.append("    ");
    CharSequence _gitHub = this.gitHub(c);
    _builder.append(_gitHub, "    ");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    CharSequence _parameters = this.parameters(c);
    _builder.append(_parameters, "    ");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("</properties>");
    _builder.newLine();
    {
      Scm _anyScm = this.getAnyScm(c);
      boolean _operator_equals = ObjectExtensions.operator_equals(_anyScm, null);
      if (_operator_equals) {
        _builder.append("  ");
        _builder.append("<scm class=\"hudson.scm.NullSCM\"/>");
        _builder.newLine();
      } else {
        _builder.append("  ");
        Scm _anyScm_1 = this.getAnyScm(c);
        CharSequence _scm = this.scm(_anyScm_1);
        _builder.append(_scm, "  ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    CharSequence _restrictTo = this.restrictTo(c);
    _builder.append(_restrictTo, "  ");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<disabled>");
    boolean _isDisabled = c.isDisabled();
    _builder.append(_isDisabled, "  ");
    _builder.append("</disabled>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>");
    _builder.newLine();
    _builder.append("  ");
    CharSequence _triggers = this.triggers(c);
    _builder.append(_triggers, "  ");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<concurrentBuild>");
    boolean _isConcurrentBuild = c.isConcurrentBuild();
    _builder.append(_isConcurrentBuild, "  ");
    _builder.append("</concurrentBuild>");
    _builder.newLineIfNotEmpty();
    {
      boolean _isMatrixJob_1 = this.isMatrixJob(c);
      if (_isMatrixJob_1) {
        _builder.append("  ");
        CharSequence _matrix = this.matrix(c);
        _builder.append(_matrix, "  ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    CharSequence _builders = this.builders(c);
    _builder.append(_builders, "  ");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    CharSequence _publishers = this.publishers(c);
    _builder.append(_publishers, "  ");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    CharSequence _wrappers = this.wrappers(c);
    _builder.append(_wrappers, "  ");
    _builder.newLineIfNotEmpty();
    {
      boolean _isMatrixJob_2 = this.isMatrixJob(c);
      if (_isMatrixJob_2) {
        _builder.append("</matrix-project>");
        _builder.newLine();
      } else {
        _builder.append("</project>");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public CharSequence logRotator(final OldBuildHandling obh) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<logRotator>");
    _builder.newLine();
    {
      int _daysToKeep = obh.getDaysToKeep();
      boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_daysToKeep, 0);
      if (_operator_greaterThan) {
        _builder.append("  ");
        _builder.append("<daysToKeep>");
        int _daysToKeep_1 = obh.getDaysToKeep();
        _builder.append(_daysToKeep_1, "  ");
        _builder.append("</daysToKeep>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      int _maxNumberOfBuilds = obh.getMaxNumberOfBuilds();
      boolean _operator_greaterThan_1 = IntegerExtensions.operator_greaterThan(_maxNumberOfBuilds, 0);
      if (_operator_greaterThan_1) {
        _builder.append("  ");
        _builder.append("<numToKeep>");
        int _maxNumberOfBuilds_1 = obh.getMaxNumberOfBuilds();
        _builder.append(_maxNumberOfBuilds_1, "  ");
        _builder.append("</numToKeep>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      int _daysToKeepArtifact = obh.getDaysToKeepArtifact();
      boolean _operator_greaterThan_2 = IntegerExtensions.operator_greaterThan(_daysToKeepArtifact, 0);
      if (_operator_greaterThan_2) {
        _builder.append("  ");
        _builder.append("<artifactDaysToKeep>");
        int _daysToKeepArtifact_1 = obh.getDaysToKeepArtifact();
        _builder.append(_daysToKeepArtifact_1, "  ");
        _builder.append("</artifactDaysToKeep>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      int _maxNumberOfBuildsWithArtifact = obh.getMaxNumberOfBuildsWithArtifact();
      boolean _operator_greaterThan_3 = IntegerExtensions.operator_greaterThan(_maxNumberOfBuildsWithArtifact, 0);
      if (_operator_greaterThan_3) {
        _builder.append("  ");
        _builder.append("<artifactNumToKeep>");
        int _maxNumberOfBuildsWithArtifact_1 = obh.getMaxNumberOfBuildsWithArtifact();
        _builder.append(_maxNumberOfBuildsWithArtifact_1, "  ");
        _builder.append("</artifactNumToKeep>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</logRotator>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence gitHub(final Config c) {
    StringConcatenation _builder = new StringConcatenation();
    String _gitUrl = this.getGitUrl(c);
    final String gitUrl = _gitUrl;
    _builder.newLineIfNotEmpty();
    {
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(gitUrl, null);
      if (_operator_notEquals) {
        _builder.append("<com.coravy.hudson.plugins.github.GithubProjectProperty>");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("<projectUrl>");
        String _normalize = this.normalize(gitUrl);
        _builder.append(_normalize, "  ");
        _builder.append("</projectUrl>");
        _builder.newLineIfNotEmpty();
        _builder.append("</com.coravy.hudson.plugins.github.GithubProjectProperty>");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public CharSequence restrictTo(final Config c) {
    StringConcatenation _builder = new StringConcatenation();
    String _restrictTo = this.getRestrictTo(c);
    final String r = _restrictTo;
    _builder.newLineIfNotEmpty();
    {
      boolean _operator_equals = ObjectExtensions.operator_equals(r, null);
      if (_operator_equals) {
        _builder.append("<canRoam>true</canRoam>");
        _builder.newLine();
      } else {
        _builder.append("<assignedNode>");
        _builder.append(r, "");
        _builder.append("</assignedNode>");
        _builder.newLineIfNotEmpty();
        _builder.append("<canRoam>false</canRoam>");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public CharSequence parameters(final Config c) {
    StringConcatenation _builder = new StringConcatenation();
    LinkedHashMap<String,Parameter> _linkedHashMap = new LinkedHashMap<String,Parameter>();
    final LinkedHashMap<String,Parameter> m = _linkedHashMap;
    _builder.newLineIfNotEmpty();
    Map<String,Parameter> _allParameters = this.getAllParameters(c, m);
    Collection<Parameter> _values = _allParameters.values();
    final Collection<Parameter> v = _values;
    _builder.newLineIfNotEmpty();
    {
      boolean _isEmpty = v.isEmpty();
      boolean _operator_equals = BooleanExtensions.operator_equals(_isEmpty, false);
      if (_operator_equals) {
        _builder.append("<hudson.model.ParametersDefinitionProperty>");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("<parameterDefinitions>");
        _builder.newLine();
        {
          for(final Parameter p : v) {
            _builder.append("    ");
            ParameterType _type = p.getType();
            CharSequence _param = this.param(p, _type);
            _builder.append(_param, "    ");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("  ");
        _builder.append("</parameterDefinitions>");
        _builder.newLine();
        _builder.append("</hudson.model.ParametersDefinitionProperty>");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  protected CharSequence _param(final Parameter p, final StringParam s) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.model.StringParameterDefinition>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<name>");
    String _name = p.getName();
    _builder.append(_name, "  ");
    _builder.append("</name>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<description>");
    String _description = p.getDescription();
    _builder.append(_description, "  ");
    _builder.append("</description>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<defaultValue>");
    String _value = s.getValue();
    String _normalize = this.normalize(_value);
    _builder.append(_normalize, "  ");
    _builder.append("</defaultValue>");
    _builder.newLineIfNotEmpty();
    _builder.append("</hudson.model.StringParameterDefinition>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _param(final Parameter p, final BooleanParam b) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.model.BooleanParameterDefinition>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<name>");
    String _name = p.getName();
    _builder.append(_name, "  ");
    _builder.append("</name>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<description>");
    String _description = p.getDescription();
    _builder.append(_description, "  ");
    _builder.append("</description>");
    _builder.newLineIfNotEmpty();
    {
      boolean _isChecked = b.isChecked();
      if (_isChecked) {
        _builder.append("  ");
        _builder.append("<defaultValue>true</defaultValue>");
        _builder.newLine();
      }
    }
    {
      boolean _isNotChecked = b.isNotChecked();
      if (_isNotChecked) {
        _builder.append("  ");
        _builder.append("<defaultValue>false</defaultValue>");
        _builder.newLine();
      }
    }
    _builder.append("</hudson.model.BooleanParameterDefinition>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _param(final Parameter p, final ChoiceParam c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.model.ChoiceParameterDefinition>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<name>");
    String _name = p.getName();
    _builder.append(_name, "  ");
    _builder.append("</name>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<description>");
    String _description = p.getDescription();
    _builder.append(_description, "  ");
    _builder.append("</description>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<choices class=\"java.util.Arrays$ArrayList\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<a class=\"string-array\">");
    _builder.newLine();
    {
      String _choices = c.getChoices();
      String[] _split = _choices.split("\n");
      for(final String s : _split) {
        _builder.append("    ");
        _builder.append("<string>");
        _builder.append(s, "    ");
        _builder.append("</string>");
        _builder.newLineIfNotEmpty();
      }
    }
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
    _builder.append("<url>");
    String _url = git.getUrl();
    String _normalize = this.normalize(_url);
    _builder.append(_normalize, "      ");
    _builder.append("</url>");
    _builder.newLineIfNotEmpty();
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
    {
      String _branch = git.getBranch();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_branch, null);
      if (_operator_notEquals) {
        _builder.append("      ");
        _builder.append("<name>");
        String _branch_1 = git.getBranch();
        _builder.append(_branch_1, "      ");
        _builder.append("</name>");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("      ");
        _builder.append("<name>origin/master</name>");
        _builder.newLine();
      }
    }
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
    {
      boolean _isWipeOutWorkspace = git.isWipeOutWorkspace();
      if (_isWipeOutWorkspace) {
        _builder.append("  ");
        _builder.append("<wipeOutWorkspace>true</wipeOutWorkspace>");
        _builder.newLine();
      } else {
        _builder.append("  ");
        _builder.append("<wipeOutWorkspace>false</wipeOutWorkspace>");
        _builder.newLine();
      }
    }
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
    {
      ScmRegions _regions = git.getRegions();
      boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_regions, null);
      if (_operator_notEquals_1) {
        _builder.append("  ");
        _builder.append("<excludedRegions>");
        ScmRegions _regions_1 = git.getRegions();
        String _excludedRegions = _regions_1.getExcludedRegions();
        String _normalize_1 = this.normalize(_excludedRegions);
        _builder.append(_normalize_1, "  ");
        _builder.append("</excludedRegions>");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("  ");
        _builder.append("<excludedRegions></excludedRegions>");
        _builder.newLine();
      }
    }
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
    {
      ScmRegions _regions_2 = git.getRegions();
      boolean _operator_notEquals_2 = ObjectExtensions.operator_notEquals(_regions_2, null);
      if (_operator_notEquals_2) {
        _builder.append("  ");
        _builder.append("<includedRegions>");
        ScmRegions _regions_3 = git.getRegions();
        String _includedRegions = _regions_3.getIncludedRegions();
        String _normalize_2 = this.normalize(_includedRegions);
        _builder.append(_normalize_2, "  ");
        _builder.append("</includedRegions>");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("  ");
        _builder.append("<includedRegions></includedRegions>");
        _builder.newLine();
      }
    }
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
    _builder.append("<remote>");
    String _url = svn.getUrl();
    String _normalize = this.normalize(_url);
    _builder.append(_normalize, "      ");
    _builder.append("</remote>");
    _builder.newLineIfNotEmpty();
    _builder.append("      ");
    _builder.append("<local>");
    String _localDir = svn.getLocalDir();
    String _normalize_1 = this.normalize(_localDir);
    _builder.append(_normalize_1, "      ");
    _builder.append("</local>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("</hudson.scm.SubversionSCM_-ModuleLocation>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("</locations>");
    _builder.newLine();
    {
      ScmRegions _regions = svn.getRegions();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_regions, null);
      if (_operator_notEquals) {
        _builder.append("  ");
        _builder.append("<excludedRegions>");
        ScmRegions _regions_1 = svn.getRegions();
        String _excludedRegions = _regions_1.getExcludedRegions();
        String _normalize_2 = this.normalize(_excludedRegions);
        _builder.append(_normalize_2, "  ");
        _builder.append("</excludedRegions>");
        _builder.newLineIfNotEmpty();
        _builder.append("  ");
        _builder.append("<includedRegions>");
        ScmRegions _regions_2 = svn.getRegions();
        String _includedRegions = _regions_2.getIncludedRegions();
        String _normalize_3 = this.normalize(_includedRegions);
        _builder.append(_normalize_3, "  ");
        _builder.append("</includedRegions>");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("  ");
        _builder.append("<excludedRegions></excludedRegions>");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("<includedRegions></includedRegions>");
        _builder.newLine();
      }
    }
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
    _builder.append("<cvsroot>");
    String _root = cvs.getRoot();
    _builder.append(_root, "  ");
    _builder.append("</cvsroot>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<module>");
    String _modules = cvs.getModules();
    _builder.append(_modules, "  ");
    _builder.append("</module>");
    _builder.newLineIfNotEmpty();
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
    LinkedHashMap<EClass,EObject> _linkedHashMap = new LinkedHashMap<EClass,EObject>();
    final LinkedHashMap<EClass,EObject> m = _linkedHashMap;
    _builder.newLineIfNotEmpty();
    {
      Map<EClass,EObject> _allTriggers = this.getAllTriggers(c, m);
      Collection<EObject> _values = _allTriggers.values();
      for(final EObject t : _values) {
        _builder.append("  ");
        CharSequence _trigger = this.trigger(t);
        _builder.append(_trigger, "  ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</triggers>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _trigger(final TimerTrigger t) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.triggers.TimerTrigger>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<spec>");
    String _timer = t.getTimer();
    _builder.append(_timer, "  ");
    _builder.append("</spec>");
    _builder.newLineIfNotEmpty();
    _builder.append("</hudson.triggers.TimerTrigger>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _trigger(final PollScmTrigger t) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.triggers.SCMTrigger>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<spec>");
    String _poll = t.getPoll();
    _builder.append(_poll, "  ");
    _builder.append("</spec>");
    _builder.newLineIfNotEmpty();
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
    LinkedHashMap<EClass,EObject> _linkedHashMap = new LinkedHashMap<EClass,EObject>();
    final LinkedHashMap<EClass,EObject> m = _linkedHashMap;
    _builder.newLineIfNotEmpty();
    {
      Map<EClass,EObject> _allWrappers = this.getAllWrappers(c, m);
      Collection<EObject> _values = _allWrappers.values();
      for(final EObject w : _values) {
        _builder.append("  ");
        CharSequence _wrapper = this.wrapper(w);
        _builder.append(_wrapper, "  ");
        _builder.newLineIfNotEmpty();
      }
    }
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
    _builder.append("<name>");
    LockDecl _lock = l.getLock();
    String _name = _lock.getName();
    _builder.append(_name, "      ");
    _builder.append("</name>");
    _builder.newLineIfNotEmpty();
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
    _builder.append("<timeoutMinutes>");
    int _t = t.getT();
    _builder.append(_t, "  ");
    _builder.append("</timeoutMinutes>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<failBuild>");
    boolean _isFailBuild = t.isFailBuild();
    _builder.append(_isFailBuild, "  ");
    _builder.append("</failBuild>");
    _builder.newLineIfNotEmpty();
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
    _builder.append("<labelName>");
    String _matrixParent = m.getMatrixParent();
    _builder.append(_matrixParent, "  ");
    _builder.append("</labelName>");
    _builder.newLineIfNotEmpty();
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
    _builder.append("<doNotKeepLog>");
    boolean _isNotKeepForever = r.isNotKeepForever();
    _builder.append(_isNotKeepForever, "  ");
    _builder.append("</doNotKeepLog>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<overrideBuildParameters>false</overrideBuildParameters>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<parameterDefinitions>");
    _builder.newLine();
    {
      ParameterSection _paramSection = r.getParamSection();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_paramSection, null);
      if (_operator_notEquals) {
        {
          ParameterSection _paramSection_1 = r.getParamSection();
          EList<Parameter> _parameters = _paramSection_1.getParameters();
          for(final Parameter p : _parameters) {
            _builder.append("    ");
            ParameterType _type = p.getType();
            CharSequence _param = this.param(p, _type);
            _builder.append(_param, "    ");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("  ");
    _builder.append("</parameterDefinitions>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<preBuildSteps>");
    _builder.newLine();
    {
      BuildSection _preBuildSection = r.getPreBuildSection();
      boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_preBuildSection, null);
      if (_operator_notEquals_1) {
        {
          BuildSection _preBuildSection_1 = r.getPreBuildSection();
          EList<EObject> _builds = _preBuildSection_1.getBuilds();
          for(final EObject b : _builds) {
            _builder.append("    ");
            CharSequence _build = this.build(b);
            _builder.append(_build, "    ");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("  ");
    _builder.append("</preBuildSteps>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<postBuildSteps>");
    _builder.newLine();
    {
      BuildSection _finalBuildSection = r.getFinalBuildSection();
      boolean _operator_notEquals_2 = ObjectExtensions.operator_notEquals(_finalBuildSection, null);
      if (_operator_notEquals_2) {
        {
          BuildSection _finalBuildSection_1 = r.getFinalBuildSection();
          EList<EObject> _builds_1 = _finalBuildSection_1.getBuilds();
          for(final EObject b_1 : _builds_1) {
            _builder.append("    ");
            CharSequence _build_1 = this.build(b_1);
            _builder.append(_build_1, "    ");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("  ");
    _builder.append("</postBuildSteps>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<postSuccessfulBuildSteps>");
    _builder.newLine();
    {
      BuildSection _successBuildSection = r.getSuccessBuildSection();
      boolean _operator_notEquals_3 = ObjectExtensions.operator_notEquals(_successBuildSection, null);
      if (_operator_notEquals_3) {
        {
          BuildSection _successBuildSection_1 = r.getSuccessBuildSection();
          EList<EObject> _builds_2 = _successBuildSection_1.getBuilds();
          for(final EObject b_2 : _builds_2) {
            _builder.append("    ");
            CharSequence _build_2 = this.build(b_2);
            _builder.append(_build_2, "    ");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("  ");
    _builder.append("</postSuccessfulBuildSteps>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<postFailedBuildSteps>");
    _builder.newLine();
    {
      BuildSection _failedBuildSection = r.getFailedBuildSection();
      boolean _operator_notEquals_4 = ObjectExtensions.operator_notEquals(_failedBuildSection, null);
      if (_operator_notEquals_4) {
        {
          BuildSection _failedBuildSection_1 = r.getFailedBuildSection();
          EList<EObject> _builds_3 = _failedBuildSection_1.getBuilds();
          for(final EObject b_3 : _builds_3) {
            _builder.append("    ");
            CharSequence _build_3 = this.build(b_3);
            _builder.append(_build_3, "    ");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
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
    LinkedHashMap<String,List<String>> _linkedHashMap = new LinkedHashMap<String,List<String>>();
    final LinkedHashMap<String,List<String>> r = _linkedHashMap;
    _builder.newLineIfNotEmpty();
    this.getMatrixes(c, r);
    _builder.newLineIfNotEmpty();
    {
      Set<Entry<String,List<String>>> _entrySet = r.entrySet();
      for(final Entry<String,List<String>> e : _entrySet) {
        _builder.append("<axes>");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("<hudson.matrix.LabelAxis>");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("<name>");
        String _key = e.getKey();
        _builder.append(_key, "    ");
        _builder.append("</name>");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("<values>");
        _builder.newLine();
        {
          List<String> _value = e.getValue();
          for(final String v : _value) {
            _builder.append("      ");
            _builder.append("<string>");
            _builder.append(v, "      ");
            _builder.append("</string>");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("    ");
        _builder.append("</values>");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("</hudson.matrix.LabelAxis>");
        _builder.newLine();
        _builder.append("</axes>");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public CharSequence builders(final Config c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<builders>");
    _builder.newLine();
    _builder.append("  ");
    ArrayList<EObject> _arrayList = new ArrayList<EObject>();
    final ArrayList<EObject> l = _arrayList;
    _builder.newLineIfNotEmpty();
    {
      List<EObject> _allBuilders = this.getAllBuilders(c, l);
      for(final EObject b : _allBuilders) {
        _builder.append("  ");
        CharSequence _build = this.build(b);
        _builder.append(_build, "  ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</builders>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _build(final Maven m) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.tasks.Maven>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<targets>");
    String _mavenGoals = m.getMavenGoals();
    _builder.append(_mavenGoals, "  ");
    _builder.append("</targets>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<mavenName>");
    MavenDecl _version = m.getVersion();
    String _name = _version.getName();
    _builder.append(_name, "  ");
    _builder.append("</mavenName>");
    _builder.newLineIfNotEmpty();
    {
      String _mavenPOM = m.getMavenPOM();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_mavenPOM, null);
      if (_operator_notEquals) {
        _builder.append("  ");
        _builder.append("<pom>");
        String _mavenPOM_1 = m.getMavenPOM();
        String _normalize = this.normalize(_mavenPOM_1);
        _builder.append(_normalize, "  ");
        _builder.append("</pom>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      String _mavenProperties = m.getMavenProperties();
      boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_mavenProperties, null);
      if (_operator_notEquals_1) {
        _builder.append("  ");
        _builder.append("<properties>");
        String _mavenProperties_1 = m.getMavenProperties();
        _builder.append(_mavenProperties_1, "  ");
        _builder.append("</properties>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    _builder.append("<usePrivateRepository>");
    boolean _isMavenPrivateRepo = m.isMavenPrivateRepo();
    _builder.append(_isMavenPrivateRepo, "  ");
    _builder.append("</usePrivateRepository>");
    _builder.newLineIfNotEmpty();
    _builder.append("</hudson.tasks.Maven>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _build(final Shell s) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.tasks.Shell>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<command>");
    String _shellScript = s.getShellScript();
    String _normalize = this.normalize(_shellScript);
    _builder.append(_normalize, "  ");
    _builder.append("</command>");
    _builder.newLineIfNotEmpty();
    _builder.append("</hudson.tasks.Shell>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _build(final Batch b) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.tasks.BatchFile>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<command>");
    String _batchScript = b.getBatchScript();
    String _normalize = this.normalize(_batchScript);
    _builder.append(_normalize, "  ");
    _builder.append("</command>");
    _builder.newLineIfNotEmpty();
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
    _builder.append("<antName>");
    AntDecl _version = a.getVersion();
    String _name = _version.getName();
    _builder.append(_name, "  ");
    _builder.append("</antName>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<buildFile>");
    String _buildFile = a.getBuildFile();
    _builder.append(_buildFile, "  ");
    _builder.append("</buildFile>");
    _builder.newLineIfNotEmpty();
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
    _builder.append("<command>");
    String _groovyScript = sg.getGroovyScript();
    String _normalize = this.normalize(_groovyScript);
    _builder.append(_normalize, "    ");
    _builder.append("</command>");
    _builder.newLineIfNotEmpty();
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
    {
      EList<TriggeredBuild> _triggeredBuilds = tbs.getTriggeredBuilds();
      for(final TriggeredBuild tb : _triggeredBuilds) {
        _builder.append("    ");
        CharSequence _triggeredBuild = this.triggeredBuild(tb);
        _builder.append(_triggeredBuild, "    ");
        _builder.newLineIfNotEmpty();
      }
    }
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
    {
      EList<EObject> _triggerParams = tb.getTriggerParams();
      for(final EObject p : _triggerParams) {
        _builder.append("    ");
        CharSequence _triggerParam = this.triggerParam(p);
        _builder.append(_triggerParam, "    ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    _builder.append("</configs>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<projects>");
    Config _builds = tb.getBuilds();
    String _fqn = this.fqn(_builds);
    _builder.append(_fqn, "  ");
    _builder.append("</projects>");
    _builder.newLineIfNotEmpty();
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
    LinkedHashMap<EClass,EObject> _linkedHashMap = new LinkedHashMap<EClass,EObject>();
    final LinkedHashMap<EClass,EObject> m = _linkedHashMap;
    _builder.newLineIfNotEmpty();
    {
      Map<EClass,EObject> _allPublishers = this.getAllPublishers(c, m);
      Collection<EObject> _values = _allPublishers.values();
      for(final EObject p : _values) {
        _builder.append("  ");
        CharSequence _publisher = this.publisher(p);
        _builder.append(_publisher, "  ");
        _builder.newLineIfNotEmpty();
      }
    }
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
    _builder.append("<recipientList>");
    String _to = this.getTo(em);
    _builder.append(_to, "  ");
    _builder.append("</recipientList>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<configuredTriggers>");
    _builder.newLine();
    _builder.append("    ");
    LinkedHashMap<String,MailTrigger> _linkedHashMap = new LinkedHashMap<String,MailTrigger>();
    final LinkedHashMap<String,MailTrigger> m = _linkedHashMap;
    _builder.newLineIfNotEmpty();
    {
      Map<String,MailTrigger> _allMailTriggers = this.getAllMailTriggers(em, m);
      Collection<MailTrigger> _values = _allMailTriggers.values();
      for(final MailTrigger mt : _values) {
        _builder.append("    ");
        CharSequence _mailTrigger = this.mailTrigger(mt);
        _builder.append(_mailTrigger, "    ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    _builder.append("</configuredTriggers>");
    _builder.newLine();
    {
      String _type = em.getType();
      boolean _operator_equals = ObjectExtensions.operator_equals(_type, null);
      if (_operator_equals) {
        _builder.append("  ");
        _builder.append("<contentType>default</contentType>");
        _builder.newLine();
      } else {
        _builder.append("  ");
        _builder.append("<contentType>");
        String _type_1 = em.getType();
        _builder.append(_type_1, "  ");
        _builder.append("</contentType>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    String _subject = this.getSubject(em);
    final String subject = _subject;
    _builder.newLineIfNotEmpty();
    {
      boolean _operator_equals_1 = ObjectExtensions.operator_equals(subject, null);
      if (_operator_equals_1) {
        _builder.append("  ");
        _builder.append("<defaultSubject>$DEFAULT_SUBJECT</defaultSubject>");
        _builder.newLine();
      } else {
        _builder.append("  ");
        _builder.append("<defaultSubject>");
        _builder.append(subject, "  ");
        _builder.append("</defaultSubject>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    String _content = this.getContent(em);
    final String content = _content;
    _builder.newLineIfNotEmpty();
    {
      boolean _operator_equals_2 = ObjectExtensions.operator_equals(content, null);
      if (_operator_equals_2) {
        _builder.append("  ");
        _builder.append("<defaultContent>$DEFAULT_CONTENT</defaultContent>");
        _builder.newLine();
      } else {
        _builder.append("  ");
        _builder.append("<defaultContent>");
        _builder.append(content, "  ");
        _builder.append("</defaultContent>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    _builder.append("<attachmentsPattern>");
    String _attachments = this.getAttachments(em);
    _builder.append(_attachments, "  ");
    _builder.append("</attachmentsPattern>");
    _builder.newLineIfNotEmpty();
    {
      MailConfig _mailConfig = em.getMailConfig();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_mailConfig, null);
      if (_operator_notEquals) {
        _builder.append("  ");
        MailConfig _mailConfig_1 = em.getMailConfig();
        CharSequence _mailConfig_2 = this.mailConfig(_mailConfig_1);
        _builder.append(_mailConfig_2, "  ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</hudson.plugins.emailext.ExtendedEmailPublisher>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence mailTrigger(final MailTrigger mt) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.emailext.plugins.trigger.");
    String _type = mt.getType();
    String _replace = _type.replace("-", "");
    _builder.append(_replace, "");
    _builder.append("Trigger>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<email>");
    _builder.newLine();
    {
      String _to = mt.getTo();
      boolean _operator_equals = ObjectExtensions.operator_equals(_to, null);
      if (_operator_equals) {
        _builder.append("    ");
        _builder.append("<recipientList></recipientList>");
        _builder.newLine();
      } else {
        _builder.append("    ");
        _builder.append("<recipientList>");
        String _to_1 = mt.getTo();
        _builder.append(_to_1, "    ");
        _builder.append("</recipientList>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _operator_and = false;
      MailConfig _mailConfig = mt.getMailConfig();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_mailConfig, null);
      if (!_operator_notEquals) {
        _operator_and = false;
      } else {
        MailConfig _mailConfig_1 = mt.getMailConfig();
        String _subject = _mailConfig_1.getSubject();
        boolean _operator_equals_1 = ObjectExtensions.operator_equals(_subject, null);
        _operator_and = BooleanExtensions.operator_and(_operator_notEquals, _operator_equals_1);
      }
      if (_operator_and) {
        _builder.append("    ");
        _builder.append("<subject>$PROJECT_DEFAULT_SUBJECT</subject>");
        _builder.newLine();
      } else {
        _builder.append("    ");
        _builder.append("<subject>");
        MailConfig _mailConfig_2 = mt.getMailConfig();
        String _subject_1 = _mailConfig_2.getSubject();
        _builder.append(_subject_1, "    ");
        _builder.append("</subject>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _operator_and_1 = false;
      MailConfig _mailConfig_3 = mt.getMailConfig();
      boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_mailConfig_3, null);
      if (!_operator_notEquals_1) {
        _operator_and_1 = false;
      } else {
        MailConfig _mailConfig_4 = mt.getMailConfig();
        String _content = _mailConfig_4.getContent();
        boolean _operator_equals_2 = ObjectExtensions.operator_equals(_content, null);
        _operator_and_1 = BooleanExtensions.operator_and(_operator_notEquals_1, _operator_equals_2);
      }
      if (_operator_and_1) {
        _builder.append("    ");
        _builder.append("<body>$PROJECT_DEFAULT_CONTENT</body>");
        _builder.newLine();
      } else {
        _builder.append("    ");
        _builder.append("<body>");
        MailConfig _mailConfig_5 = mt.getMailConfig();
        String _content_1 = _mailConfig_5.getContent();
        _builder.append(_content_1, "    ");
        _builder.append("</body>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("    ");
    _builder.append("<sendToDevelopers>");
    boolean _isToCommiter = mt.isToCommiter();
    _builder.append(_isToCommiter, "    ");
    _builder.append("</sendToDevelopers>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<sendToRequester>");
    boolean _isToRequester = mt.isToRequester();
    _builder.append(_isToRequester, "    ");
    _builder.append("</sendToRequester>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<includeCulprits>");
    boolean _isToCulprits = mt.isToCulprits();
    _builder.append(_isToCulprits, "    ");
    _builder.append("</includeCulprits>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<sendToRecipientList>");
    boolean _isToList = mt.isToList();
    _builder.append(_isToList, "    ");
    _builder.append("</sendToRecipientList>");
    _builder.newLineIfNotEmpty();
    {
      MailConfig _mailConfig_6 = mt.getMailConfig();
      boolean _operator_notEquals_2 = ObjectExtensions.operator_notEquals(_mailConfig_6, null);
      if (_operator_notEquals_2) {
        _builder.append("    ");
        _builder.append("<attachmentsPattern>");
        MailConfig _mailConfig_7 = mt.getMailConfig();
        String _attachments = _mailConfig_7.getAttachments();
        _builder.append(_attachments, "    ");
        _builder.append("</attachmentsPattern>");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        MailConfig _mailConfig_8 = mt.getMailConfig();
        CharSequence _mailConfig_9 = this.mailConfig(_mailConfig_8);
        _builder.append(_mailConfig_9, "    ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    _builder.append("</email>");
    _builder.newLine();
    _builder.append("</hudson.plugins.emailext.plugins.trigger.");
    String _type_1 = mt.getType();
    String _replace_1 = _type_1.replace("-", "");
    _builder.append(_replace_1, "");
    _builder.append("Trigger>");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence mailConfig(final MailConfig mc) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<attachBuildLog>");
    boolean _isAttachBuildLog = mc.isAttachBuildLog();
    _builder.append(_isAttachBuildLog, "");
    _builder.append("</attachBuildLog>");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _publisher(final TestResult t) {
    StringConcatenation _builder = new StringConcatenation();
    {
      String _testresults = t.getTestresults();
      boolean _isNotEmpty = this.isNotEmpty(_testresults);
      if (_isNotEmpty) {
        _builder.append("<hudson.tasks.junit.JUnitResultArchiver>");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("<testResults>");
        String _testresults_1 = t.getTestresults();
        String _normalize = this.normalize(_testresults_1);
        _builder.append(_normalize, "  ");
        _builder.append("</testResults>");
        _builder.newLineIfNotEmpty();
        _builder.append("  ");
        _builder.append("<keepLongStdio>");
        boolean _isLongIO = t.isLongIO();
        _builder.append(_isLongIO, "  ");
        _builder.append("</keepLongStdio>");
        _builder.newLineIfNotEmpty();
        _builder.append("  ");
        _builder.append("<testDataPublishers>");
        _builder.newLine();
        {
          boolean _isClaim = t.isClaim();
          if (_isClaim) {
            _builder.append("  ");
            _builder.append("<hudson.plugins.claim.ClaimTestDataPublisher/>");
            _builder.newLine();
          }
        }
        _builder.append("  ");
        _builder.append("</testDataPublishers>");
        _builder.newLine();
        _builder.append("</hudson.tasks.junit.JUnitResultArchiver>");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  protected CharSequence _publisher(final DownStream d) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.parameterizedtrigger.BuildTrigger>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<configs>");
    _builder.newLine();
    {
      EList<DownStreamBuild> _builds = d.getBuilds();
      for(final DownStreamBuild b : _builds) {
        _builder.append("    ");
        CharSequence _downStreamBuild = this.downStreamBuild(b);
        _builder.append(_downStreamBuild, "    ");
        _builder.newLineIfNotEmpty();
      }
    }
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
    _builder.append("<artifacts>");
    String _artifacts = a.getArtifacts();
    String _normalize = this.normalize(_artifacts);
    _builder.append(_normalize, "  ");
    _builder.append("</artifacts>");
    _builder.newLineIfNotEmpty();
    {
      if (false) {
        _builder.append("  ");
        _builder.append("<latestOnly>true</latestOnly>");
        _builder.newLine();
      } else {
        _builder.append("  ");
        _builder.append("<latestOnly>false</latestOnly>");
        _builder.newLine();
      }
    }
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
    _builder.append("<pushOnlyIfSuccess>");
    boolean _isOnlyOnSuccess = g.isOnlyOnSuccess();
    _builder.append(_isOnlyOnSuccess, "  ");
    _builder.append("</pushOnlyIfSuccess>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<branchesToPush>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<hudson.plugins.git.GitPublisher_-BranchToPush>");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<targetRepoName>");
    String _origin = g.getOrigin();
    _builder.append(_origin, "      ");
    _builder.append("</targetRepoName>");
    _builder.newLineIfNotEmpty();
    _builder.append("      ");
    _builder.append("<branchName>");
    String _branch = g.getBranch();
    _builder.append(_branch, "      ");
    _builder.append("</branchName>");
    _builder.newLineIfNotEmpty();
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
    _builder.append("<name>");
    String _resultprefix = g.getResultprefix();
    String _normalize = this.normalize(_resultprefix);
    _builder.append(_normalize, "    ");
    _builder.append("</name>");
    _builder.newLineIfNotEmpty();
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
    {
      EList<ViolationsConfig> _violations = v.getViolations();
      for(final ViolationsConfig vc : _violations) {
        _builder.append("      ");
        CharSequence _violationsConfig = this.violationsConfig(vc);
        _builder.append(_violationsConfig, "      ");
        _builder.newLineIfNotEmpty();
      }
    }
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
    _builder.append("<type>");
    String _type = vc.getType();
    _builder.append(_type, "    ");
    _builder.append("</type>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<min>");
    int _min = vc.getMin();
    _builder.append(_min, "    ");
    _builder.append("</min>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<max>");
    int _max = vc.getMax();
    _builder.append(_max, "    ");
    _builder.append("</max>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstable>");
    int _unstable = vc.getUnstable();
    _builder.append(_unstable, "    ");
    _builder.append("</unstable>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<usePattern>false</usePattern>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<pattern>");
    String _pattern = vc.getPattern();
    _builder.append(_pattern, "    ");
    _builder.append("</pattern>");
    _builder.newLineIfNotEmpty();
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
    _builder.append("<reportName>");
    String _name = h.getName();
    _builder.append(_name, "      ");
    _builder.append("</reportName>");
    _builder.newLineIfNotEmpty();
    _builder.append("      ");
    _builder.append("<reportDir>");
    String _dir = h.getDir();
    _builder.append(_dir, "      ");
    _builder.append("</reportDir>");
    _builder.newLineIfNotEmpty();
    _builder.append("      ");
    _builder.append("<reportFiles>");
    String _files = h.getFiles();
    _builder.append(_files, "      ");
    _builder.append("</reportFiles>");
    _builder.newLineIfNotEmpty();
    _builder.append("      ");
    _builder.append("<keepAll>");
    boolean _isKeepPast = h.isKeepPast();
    _builder.append(_isKeepPast, "      ");
    _builder.append("</keepAll>");
    _builder.newLineIfNotEmpty();
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
    {
      int _unstableTotalAll = w.getUnstableTotalAll();
      boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_unstableTotalAll, 0);
      if (_operator_greaterThan) {
        _builder.append("    ");
        _builder.append("<unstableTotalAll>");
        int _unstableTotalAll_1 = w.getUnstableTotalAll();
        _builder.append(_unstableTotalAll_1, "    ");
        _builder.append("</unstableTotalAll>");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("    ");
        _builder.append("<unstableTotalAll></unstableTotalAll>");
        _builder.newLine();
      }
    }
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
    {
      int _failTotalAll = w.getFailTotalAll();
      boolean _operator_greaterThan_1 = IntegerExtensions.operator_greaterThan(_failTotalAll, 0);
      if (_operator_greaterThan_1) {
        _builder.append("    ");
        _builder.append("<failedTotalAll>");
        int _failTotalAll_1 = w.getFailTotalAll();
        _builder.append(_failTotalAll_1, "    ");
        _builder.append("</failedTotalAll>");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("    ");
        _builder.append("<failedTotalAll></failedTotalAll>");
        _builder.newLine();
      }
    }
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
    _builder.append("<string>");
    WarningsDecl _parser = w.getParser();
    String _name = _parser.getName();
    _builder.append(_name, "    ");
    _builder.append("</string>");
    _builder.newLineIfNotEmpty();
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
    _builder.append("<room>");
    String _room = h.getRoom();
    _builder.append(_room, "  ");
    _builder.append("</room>");
    _builder.newLineIfNotEmpty();
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
    _builder.append("<execPattern>");
    String _execPattern = j.getExecPattern();
    _builder.append(_execPattern, "  ");
    _builder.append("</execPattern>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<classPattern>");
    String _classPattern = j.getClassPattern();
    _builder.append(_classPattern, "  ");
    _builder.append("</classPattern>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<sourcePattern>");
    String _sourcePattern = j.getSourcePattern();
    _builder.append(_sourcePattern, "  ");
    _builder.append("</sourcePattern>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<inclusionPattern>");
    String _inclusionPattern = j.getInclusionPattern();
    _builder.append(_inclusionPattern, "  ");
    _builder.append("</inclusionPattern>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<exclusionPattern>");
    String _exclusionPattern = j.getExclusionPattern();
    _builder.append(_exclusionPattern, "  ");
    _builder.append("</exclusionPattern>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<minimumInstructionCoverage>");
    int _minimumInstructionCoverage = j.getMinimumInstructionCoverage();
    _builder.append(_minimumInstructionCoverage, "  ");
    _builder.append("</minimumInstructionCoverage>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<minimumBranchCoverage>");
    int _minimumBranchCoverage = j.getMinimumBranchCoverage();
    _builder.append(_minimumBranchCoverage, "  ");
    _builder.append("</minimumBranchCoverage>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<minimumComplexityCoverage>");
    int _minimumComplexityCoverage = j.getMinimumComplexityCoverage();
    _builder.append(_minimumComplexityCoverage, "  ");
    _builder.append("</minimumComplexityCoverage>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<minimumLineCoverage>");
    int _minimumLineCoverage = j.getMinimumLineCoverage();
    _builder.append(_minimumLineCoverage, "  ");
    _builder.append("</minimumLineCoverage>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<minimumMethodCoverage>");
    int _minimumMethodCoverage = j.getMinimumMethodCoverage();
    _builder.append(_minimumMethodCoverage, "  ");
    _builder.append("</minimumMethodCoverage>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<minimumClassCoverage>");
    int _minimumClassCoverage = j.getMinimumClassCoverage();
    _builder.append(_minimumClassCoverage, "  ");
    _builder.append("</minimumClassCoverage>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<maximumInstructionCoverage>");
    int _maximumInstructionCoverage = j.getMaximumInstructionCoverage();
    _builder.append(_maximumInstructionCoverage, "  ");
    _builder.append("</maximumInstructionCoverage>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<maximumBranchCoverage>");
    int _maximumBranchCoverage = j.getMaximumBranchCoverage();
    _builder.append(_maximumBranchCoverage, "  ");
    _builder.append("</maximumBranchCoverage>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<maximumComplexityCoverage>");
    int _maximumComplexityCoverage = j.getMaximumComplexityCoverage();
    _builder.append(_maximumComplexityCoverage, "  ");
    _builder.append("</maximumComplexityCoverage>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<maximumLineCoverage>");
    int _maximumLineCoverage = j.getMaximumLineCoverage();
    _builder.append(_maximumLineCoverage, "  ");
    _builder.append("</maximumLineCoverage>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<maximumMethodCoverage>");
    int _maximumMethodCoverage = j.getMaximumMethodCoverage();
    _builder.append(_maximumMethodCoverage, "  ");
    _builder.append("</maximumMethodCoverage>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<maximumClassCoverage>");
    int _maximumClassCoverage = j.getMaximumClassCoverage();
    _builder.append(_maximumClassCoverage, "  ");
    _builder.append("</maximumClassCoverage>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<changeBuildStatus>");
    boolean _isChangeBuildStatus = j.isChangeBuildStatus();
    _builder.append(_isChangeBuildStatus, "  ");
    _builder.append("</changeBuildStatus>");
    _builder.newLineIfNotEmpty();
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
    String _healthy = c.getHealthy();
    _builder.append(_healthy, "  ");
    _builder.append("</healthy>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<unHealthy>");
    String _unHealthy = c.getUnHealthy();
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
    String _unstableTotalAll = _thresholds.getUnstableTotalAll();
    _builder.append(_unstableTotalAll, "    ");
    _builder.append("</unstableTotalAll>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalHigh>");
    Thresholds _thresholds_1 = c.getThresholds();
    String _unstableTotalHigh = _thresholds_1.getUnstableTotalHigh();
    _builder.append(_unstableTotalHigh, "    ");
    _builder.append("</unstableTotalHigh>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalNormal>");
    Thresholds _thresholds_2 = c.getThresholds();
    String _unstableTotalNormal = _thresholds_2.getUnstableTotalNormal();
    _builder.append(_unstableTotalNormal, "    ");
    _builder.append("</unstableTotalNormal>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalLow>");
    Thresholds _thresholds_3 = c.getThresholds();
    String _unstableTotalLow = _thresholds_3.getUnstableTotalLow();
    _builder.append(_unstableTotalLow, "    ");
    _builder.append("</unstableTotalLow>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalAll>");
    Thresholds _thresholds_4 = c.getThresholds();
    String _failedTotalAll = _thresholds_4.getFailedTotalAll();
    _builder.append(_failedTotalAll, "    ");
    _builder.append("</failedTotalAll>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalHigh>");
    Thresholds _thresholds_5 = c.getThresholds();
    String _failedTotalHigh = _thresholds_5.getFailedTotalHigh();
    _builder.append(_failedTotalHigh, "    ");
    _builder.append("</failedTotalHigh>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalNormal>");
    Thresholds _thresholds_6 = c.getThresholds();
    String _failedTotalNormal = _thresholds_6.getFailedTotalNormal();
    _builder.append(_failedTotalNormal, "    ");
    _builder.append("</failedTotalNormal>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalLow>");
    Thresholds _thresholds_7 = c.getThresholds();
    String _failedTotalLow = _thresholds_7.getFailedTotalLow();
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
    _builder.append("<hudson.plugins.pmd.PmdPublisher>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<healthy>");
    String _healthy = p.getHealthy();
    _builder.append(_healthy, "  ");
    _builder.append("</healthy>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<unHealthy>");
    String _unHealthy = p.getUnHealthy();
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
    _builder.append("<pluginName>[PMD] </pluginName>");
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
    String _unstableTotalAll = _thresholds.getUnstableTotalAll();
    _builder.append(_unstableTotalAll, "    ");
    _builder.append("</unstableTotalAll>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalHigh>");
    Thresholds _thresholds_1 = p.getThresholds();
    String _unstableTotalHigh = _thresholds_1.getUnstableTotalHigh();
    _builder.append(_unstableTotalHigh, "    ");
    _builder.append("</unstableTotalHigh>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalNormal>");
    Thresholds _thresholds_2 = p.getThresholds();
    String _unstableTotalNormal = _thresholds_2.getUnstableTotalNormal();
    _builder.append(_unstableTotalNormal, "    ");
    _builder.append("</unstableTotalNormal>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalLow>");
    Thresholds _thresholds_3 = p.getThresholds();
    String _unstableTotalLow = _thresholds_3.getUnstableTotalLow();
    _builder.append(_unstableTotalLow, "    ");
    _builder.append("</unstableTotalLow>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalAll>");
    Thresholds _thresholds_4 = p.getThresholds();
    String _failedTotalAll = _thresholds_4.getFailedTotalAll();
    _builder.append(_failedTotalAll, "    ");
    _builder.append("</failedTotalAll>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalHigh>");
    Thresholds _thresholds_5 = p.getThresholds();
    String _failedTotalHigh = _thresholds_5.getFailedTotalHigh();
    _builder.append(_failedTotalHigh, "    ");
    _builder.append("</failedTotalHigh>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalNormal>");
    Thresholds _thresholds_6 = p.getThresholds();
    String _failedTotalNormal = _thresholds_6.getFailedTotalNormal();
    _builder.append(_failedTotalNormal, "    ");
    _builder.append("</failedTotalNormal>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalLow>");
    Thresholds _thresholds_7 = p.getThresholds();
    String _failedTotalLow = _thresholds_7.getFailedTotalLow();
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
    _builder.append("</hudson.plugins.pmd.PmdPublisher>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final FindBugs f) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.findbugs.FindBugsPublisher>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<healthy>");
    String _healthy = f.getHealthy();
    _builder.append(_healthy, "  ");
    _builder.append("</healthy>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<unHealthy>");
    String _unHealthy = f.getUnHealthy();
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
    _builder.append("<pluginName>[FINDBUGS] </pluginName>");
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
    String _unstableTotalAll = _thresholds.getUnstableTotalAll();
    _builder.append(_unstableTotalAll, "    ");
    _builder.append("</unstableTotalAll>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalHigh>");
    Thresholds _thresholds_1 = f.getThresholds();
    String _unstableTotalHigh = _thresholds_1.getUnstableTotalHigh();
    _builder.append(_unstableTotalHigh, "    ");
    _builder.append("</unstableTotalHigh>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalNormal>");
    Thresholds _thresholds_2 = f.getThresholds();
    String _unstableTotalNormal = _thresholds_2.getUnstableTotalNormal();
    _builder.append(_unstableTotalNormal, "    ");
    _builder.append("</unstableTotalNormal>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<unstableTotalLow>");
    Thresholds _thresholds_3 = f.getThresholds();
    String _unstableTotalLow = _thresholds_3.getUnstableTotalLow();
    _builder.append(_unstableTotalLow, "    ");
    _builder.append("</unstableTotalLow>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalAll>");
    Thresholds _thresholds_4 = f.getThresholds();
    String _failedTotalAll = _thresholds_4.getFailedTotalAll();
    _builder.append(_failedTotalAll, "    ");
    _builder.append("</failedTotalAll>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalHigh>");
    Thresholds _thresholds_5 = f.getThresholds();
    String _failedTotalHigh = _thresholds_5.getFailedTotalHigh();
    _builder.append(_failedTotalHigh, "    ");
    _builder.append("</failedTotalHigh>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalNormal>");
    Thresholds _thresholds_6 = f.getThresholds();
    String _failedTotalNormal = _thresholds_6.getFailedTotalNormal();
    _builder.append(_failedTotalNormal, "    ");
    _builder.append("</failedTotalNormal>");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("<failedTotalLow>");
    Thresholds _thresholds_7 = f.getThresholds();
    String _failedTotalLow = _thresholds_7.getFailedTotalLow();
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
    _builder.append("</hudson.plugins.findbugs.FindBugsPublisher>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _publisher(final Cobertura c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.cobertura.CoberturaPublisher>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<coberturaReportFile>");
    String _xmlreport = c.getXmlreport();
    _builder.append(_xmlreport, "  ");
    _builder.append("</coberturaReportFile>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<onlyStable>");
    boolean _isOnlyStable = c.isOnlyStable();
    _builder.append(_isOnlyStable, "  ");
    _builder.append("</onlyStable>");
    _builder.newLineIfNotEmpty();
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
    _builder.append("<reportDir>");
    String _reportDir = r.getReportDir();
    _builder.append(_reportDir, "  ");
    _builder.append("</reportDir>");
    _builder.newLineIfNotEmpty();
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
    {
      EList<EObject> _triggerParams = b.getTriggerParams();
      boolean _isEmpty = _triggerParams.isEmpty();
      if (_isEmpty) {
        _builder.append("  ");
        _builder.append("<configs class=\"java.util.Collections$EmptyList\"/>");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("<triggerWithNoParameters>true</triggerWithNoParameters>");
        _builder.newLine();
      } else {
        _builder.append("  ");
        _builder.append("<configs>");
        _builder.newLine();
        {
          EList<EObject> _triggerParams_1 = b.getTriggerParams();
          for(final EObject p : _triggerParams_1) {
            _builder.append("  ");
            _builder.append("  ");
            CharSequence _triggerParam = this.triggerParam(p);
            _builder.append(_triggerParam, "    ");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("  ");
        _builder.append("</configs>");
        _builder.newLine();
      }
    }
    _builder.append("  ");
    _builder.append("<projects>");
    Config _builds = b.getBuilds();
    String _fqn = this.fqn(_builds);
    _builder.append(_fqn, "  ");
    _builder.append("</projects>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<condition>");
    String _condition = b.getCondition();
    String _translateCondition = this.translateCondition(_condition);
    _builder.append(_translateCondition, "  ");
    _builder.append("</condition>");
    _builder.newLineIfNotEmpty();
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
    _builder.append("<propertiesFile>");
    String _propertyFile = p.getPropertyFile();
    _builder.append(_propertyFile, "  ");
    _builder.append("</propertiesFile>");
    _builder.newLineIfNotEmpty();
    _builder.append("</hudson.plugins.parameterizedtrigger.FileBuildParameters>");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _triggerParam(final PredefinedTriggerParams p) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.parameterizedtrigger.PredefinedBuildParameters>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<properties>");
    String _predefined = p.getPredefined();
    String _normalize = this.normalize(_predefined);
    _builder.append(_normalize, "  ");
    _builder.append("</properties>");
    _builder.newLineIfNotEmpty();
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
