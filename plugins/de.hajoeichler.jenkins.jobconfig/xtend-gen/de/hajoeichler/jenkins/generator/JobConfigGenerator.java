package de.hajoeichler.jenkins.generator;

import de.hajoeichler.jenkins.jobConfig.Artifacts;
import de.hajoeichler.jenkins.jobConfig.BooleanParam;
import de.hajoeichler.jenkins.jobConfig.BuildSection;
import de.hajoeichler.jenkins.jobConfig.ChoiceParam;
import de.hajoeichler.jenkins.jobConfig.Config;
import de.hajoeichler.jenkins.jobConfig.CurrentTriggerParams;
import de.hajoeichler.jenkins.jobConfig.DownStream;
import de.hajoeichler.jenkins.jobConfig.DownStreamBuild;
import de.hajoeichler.jenkins.jobConfig.ExtMail;
import de.hajoeichler.jenkins.jobConfig.FirstStartTrigger;
import de.hajoeichler.jenkins.jobConfig.GitCommitParam;
import de.hajoeichler.jenkins.jobConfig.Group;
import de.hajoeichler.jenkins.jobConfig.Lock;
import de.hajoeichler.jenkins.jobConfig.LockDecl;
import de.hajoeichler.jenkins.jobConfig.MailTrigger;
import de.hajoeichler.jenkins.jobConfig.Maven;
import de.hajoeichler.jenkins.jobConfig.MavenDecl;
import de.hajoeichler.jenkins.jobConfig.OldBuildHandling;
import de.hajoeichler.jenkins.jobConfig.Parameter;
import de.hajoeichler.jenkins.jobConfig.ParameterSection;
import de.hajoeichler.jenkins.jobConfig.ParameterType;
import de.hajoeichler.jenkins.jobConfig.PollScmTrigger;
import de.hajoeichler.jenkins.jobConfig.PredefinedTriggerParams;
import de.hajoeichler.jenkins.jobConfig.PropertyFileTriggerParams;
import de.hajoeichler.jenkins.jobConfig.PublisherSection;
import de.hajoeichler.jenkins.jobConfig.Scm;
import de.hajoeichler.jenkins.jobConfig.ScmCVS;
import de.hajoeichler.jenkins.jobConfig.ScmGit;
import de.hajoeichler.jenkins.jobConfig.Shell;
import de.hajoeichler.jenkins.jobConfig.StringParam;
import de.hajoeichler.jenkins.jobConfig.TestResult;
import de.hajoeichler.jenkins.jobConfig.Timeout;
import de.hajoeichler.jenkins.jobConfig.TimerTrigger;
import de.hajoeichler.jenkins.jobConfig.TriggerSection;
import de.hajoeichler.jenkins.jobConfig.WrapperSection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.BooleanExtensions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.eclipse.xtext.xtend2.lib.ResourceExtensions;
import org.eclipse.xtext.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class JobConfigGenerator implements IGenerator {
  
  private Config currentConfig;
  
  public void doGenerate(final Resource resource, final IFileSystemAccess fsa) {
    Iterable<EObject> _allContentsIterable = ResourceExtensions.allContentsIterable(resource);
    Iterable<Config> _filter = IterableExtensions.<Config>filter(_allContentsIterable, de.hajoeichler.jenkins.jobConfig.Config.class);
    for (final Config config : _filter) {
      boolean _isAbstract = config.isAbstract();
      boolean _operator_not = BooleanExtensions.operator_not(_isAbstract);
      if (_operator_not) {
        {
          this.currentConfig = config;
          String _fileName = this.fileName(config);
          StringConcatenation _content = this.content(config);
          fsa.generateFile(_fileName, _content);
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
    if ((_eContainer instanceof de.hajoeichler.jenkins.jobConfig.Group)) {
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
      if ((any instanceof de.hajoeichler.jenkins.jobConfig.Config)) {
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
  
  public List<ParameterSection> getAllParameterSections(final Config c) {
    {
      ArrayList<ParameterSection> _arrayList = new ArrayList<ParameterSection>();
      final ArrayList<ParameterSection> l = _arrayList;
      Config _parentConfig = c.getParentConfig();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_parentConfig, null);
      if (_operator_notEquals) {
        Config _parentConfig_1 = c.getParentConfig();
        List<ParameterSection> _allParameterSections = this.getAllParameterSections(_parentConfig_1);
        l.addAll(_allParameterSections);
      }
      ParameterSection _paramSection = c.getParamSection();
      boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_paramSection, null);
      if (_operator_notEquals_1) {
        ParameterSection _paramSection_1 = c.getParamSection();
        l.add(_paramSection_1);
      }
      return l;
    }
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
    {
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
  }
  
  public Map<EClass,EObject> getAllWrappers(final Config c, final Map<EClass,EObject> m) {
    {
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
  }
  
  public List<EObject> getAllBuilders(final Config c, final List<EObject> l) {
    {
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
  }
  
  public Map<EClass,EObject> getAllPublishers(final Config c, final Map<EClass,EObject> m) {
    {
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
  }
  
  public StringConcatenation content(final Config c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\'1.0\' encoding=\'UTF-8\'?>");
    _builder.newLine();
    {
      boolean _isIsMatixJob = c.isIsMatixJob();
      if (_isIsMatixJob) {
        _builder.append("<matrix-project>");
        _builder.newLine();} else {
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
      OldBuildHandling _anyOldBuildHandling = this.getAnyOldBuildHandling(c);
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_anyOldBuildHandling, null);
      if (_operator_notEquals) {
        _builder.append("  ");
        OldBuildHandling _anyOldBuildHandling_1 = this.getAnyOldBuildHandling(c);
        StringConcatenation _logRotator = this.logRotator(_anyOldBuildHandling_1);
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
    StringConcatenation _gitHub = this.gitHub(c);
    _builder.append(_gitHub, "    ");
    _builder.newLineIfNotEmpty();
    {
      List<ParameterSection> _allParameterSections = this.getAllParameterSections(c);
      for(final ParameterSection ps : _allParameterSections) {
        _builder.append("    ");
        StringConcatenation _parameterSection = this.parameterSection(ps);
        _builder.append(_parameterSection, "    ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    _builder.append("</properties>");
    _builder.newLine();
    {
      Scm _anyScm = this.getAnyScm(c);
      boolean _operator_equals = ObjectExtensions.operator_equals(_anyScm, null);
      if (_operator_equals) {
        _builder.append("  ");
        _builder.append("<scm class=\"hudson.scm.NullSCM\"/>");
        _builder.newLine();} else {
        _builder.append("  ");
        Scm _anyScm_1 = this.getAnyScm(c);
        StringConcatenation _scm = this.scm(_anyScm_1);
        _builder.append(_scm, "  ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    StringConcatenation _restrictTo = this.restrictTo(c);
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
    StringConcatenation _triggers = this.triggers(c);
    _builder.append(_triggers, "  ");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<concurrentBuild>");
    boolean _isConcurrentBuild = c.isConcurrentBuild();
    _builder.append(_isConcurrentBuild, "  ");
    _builder.append("</concurrentBuild>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    StringConcatenation _builders = this.builders(c);
    _builder.append(_builders, "  ");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    StringConcatenation _publishers = this.publishers(c);
    _builder.append(_publishers, "  ");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    StringConcatenation _wrappers = this.wrappers(c);
    _builder.append(_wrappers, "  ");
    _builder.newLineIfNotEmpty();
    {
      boolean _isIsMatixJob_1 = c.isIsMatixJob();
      if (_isIsMatixJob_1) {
        _builder.append("</matrix-project>");
        _builder.newLine();} else {
        _builder.append("</project>");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public StringConcatenation logRotator(final OldBuildHandling obh) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<logRotator>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<daysToKeep>");
    int _daysToKeep = obh.getDaysToKeep();
    _builder.append(_daysToKeep, "  ");
    _builder.append("</daysToKeep>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<numToKeep>");
    int _maxNumberOfBuilds = obh.getMaxNumberOfBuilds();
    _builder.append(_maxNumberOfBuilds, "  ");
    _builder.append("</numToKeep>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<artifactDaysToKeep>");
    int _daysToKeepArtifact = obh.getDaysToKeepArtifact();
    _builder.append(_daysToKeepArtifact, "  ");
    _builder.append("</artifactDaysToKeep>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<artifactNumToKeep>");
    int _maxNumberOfBuildsWithArtifact = obh.getMaxNumberOfBuildsWithArtifact();
    _builder.append(_maxNumberOfBuildsWithArtifact, "  ");
    _builder.append("</artifactNumToKeep>");
    _builder.newLineIfNotEmpty();
    _builder.append("</logRotator>");
    _builder.newLine();
    return _builder;
  }
  
  public StringConcatenation gitHub(final Config c) {
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
        _builder.append(gitUrl, "  ");
        _builder.append("</projectUrl>");
        _builder.newLineIfNotEmpty();
        _builder.append("</com.coravy.hudson.plugins.github.GithubProjectProperty>");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public StringConcatenation restrictTo(final Config c) {
    StringConcatenation _builder = new StringConcatenation();
    String _restrictTo = this.getRestrictTo(c);
    final String r = _restrictTo;
    _builder.newLineIfNotEmpty();
    {
      boolean _operator_equals = ObjectExtensions.operator_equals(r, null);
      if (_operator_equals) {
        _builder.append("<canRoam>true</canRoam>");
        _builder.newLine();} else {
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
  
  public StringConcatenation parameterSection(final ParameterSection ps) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.model.ParametersDefinitionProperty>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<parameterDefinitions>");
    _builder.newLine();
    {
      EList<Parameter> _parameters = ps.getParameters();
      for(final Parameter p : _parameters) {
        _builder.append("  ");
        ParameterType _type = p.getType();
        StringConcatenation _param = this.param(p, _type);
        _builder.append(_param, "  ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</parameterDefinitions>");
    _builder.newLine();
    _builder.append("</hudson.model.ParametersDefinitionProperty>");
    _builder.newLine();
    return _builder;
  }
  
  protected StringConcatenation _param(final Parameter p, final StringParam s) {
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
    _builder.append(_value, "  ");
    _builder.append("</defaultValue>");
    _builder.newLineIfNotEmpty();
    _builder.append("</hudson.model.StringParameterDefinition>");
    _builder.newLine();
    return _builder;
  }
  
  protected StringConcatenation _param(final Parameter p, final BooleanParam b) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  protected StringConcatenation _param(final Parameter p, final ChoiceParam c) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  protected StringConcatenation _scm(final ScmGit git) {
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
    _builder.append(_url, "      ");
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
        _builder.newLineIfNotEmpty();} else {
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
        _builder.newLine();} else {
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
    _builder.append("<excludedRegions>");
    String _excludedRegions = git.getExcludedRegions();
    String _normalize = this.normalize(_excludedRegions);
    _builder.append(_normalize, "  ");
    _builder.append("</excludedRegions>");
    _builder.newLineIfNotEmpty();
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
    _builder.append("<scmName></scmName>");
    _builder.newLine();
    _builder.append("</scm>");
    _builder.newLine();
    return _builder;
  }
  
  protected StringConcatenation _scm(final ScmCVS cvs) {
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
    _builder.append("<canUseUpdate>true</canUseUpdate>");
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
  
  public StringConcatenation triggers(final Config c) {
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
        StringConcatenation _trigger = this.trigger(t);
        _builder.append(_trigger, "  ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</triggers>");
    _builder.newLine();
    return _builder;
  }
  
  protected StringConcatenation _trigger(final TimerTrigger t) {
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
  
  protected StringConcatenation _trigger(final PollScmTrigger t) {
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
  
  protected StringConcatenation _trigger(final FirstStartTrigger t) {
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
  
  public StringConcatenation wrappers(final Config c) {
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
        StringConcatenation _wrapper = this.wrapper(w);
        _builder.append(_wrapper, "  ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</buildWrappers>");
    _builder.newLine();
    return _builder;
  }
  
  protected StringConcatenation _wrapper(final Lock l) {
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
  
  protected StringConcatenation _wrapper(final Timeout t) {
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
    _builder.append("<failBuild>true</failBuild>");
    _builder.newLine();
    _builder.append("</hudson.plugins.build__timeout.BuildTimeoutWrapper>");
    _builder.newLine();
    return _builder;
  }
  
  public StringConcatenation builders(final Config c) {
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
        StringConcatenation _build = this.build(b);
        _builder.append(_build, "  ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</builders>");
    _builder.newLine();
    return _builder;
  }
  
  protected StringConcatenation _build(final Maven m) {
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
        _builder.append(_mavenPOM_1, "  ");
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
  
  protected StringConcatenation _build(final Shell s) {
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
  
  public StringConcatenation publishers(final Config c) {
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
        StringConcatenation _publisher = this.publisher(p);
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
      boolean _operator_equals = ObjectExtensions.operator_equals(((Boolean)_isMergeWithSuperConfig), ((Boolean)true));
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
  
  public ExtMail getParentExtMail(final ExtMail em) {
    ExtMail _xblockexpression = null;
    {
      Config _myConfig = this.getMyConfig(em);
      final Config c = _myConfig;
      ExtMail _xifexpression = null;
      Config _parentConfig = c.getParentConfig();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_parentConfig, null);
      if (_operator_notEquals) {
        ExtMail _xifexpression_1 = null;
        Config _parentConfig_1 = c.getParentConfig();
        PublisherSection _publisherSection = _parentConfig_1.getPublisherSection();
        boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_publisherSection, null);
        if (_operator_notEquals_1) {
          Config _parentConfig_2 = c.getParentConfig();
          PublisherSection _publisherSection_1 = _parentConfig_2.getPublisherSection();
          EList<EObject> _publishers = _publisherSection_1.getPublishers();
          for (final EObject p : _publishers) {
            if ((p instanceof de.hajoeichler.jenkins.jobConfig.ExtMail)) {
              return ((ExtMail) p);
            }
          }
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public Map<String,MailTrigger> getAllMailTriggers(final ExtMail em, final Map<String,MailTrigger> m) {
    {
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
      boolean _operator_equals = ObjectExtensions.operator_equals(((Boolean)_isMergeWithSuperConfig), ((Boolean)true));
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
  }
  
  protected StringConcatenation _publisher(final ExtMail em) {
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
        StringConcatenation _mailTrigger = this.mailTrigger(mt);
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
        _builder.newLine();} else {
        _builder.append("  ");
        _builder.append("<contentType>");
        String _type_1 = em.getType();
        _builder.append(_type_1, "  ");
        _builder.append("</contentType>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      String _subject = em.getSubject();
      boolean _operator_equals_1 = ObjectExtensions.operator_equals(_subject, null);
      if (_operator_equals_1) {
        _builder.append("  ");
        _builder.append("<defaultSubject>${DEFAULT_SUBJECT}</defaultSubject>");
        _builder.newLine();} else {
        _builder.append("  ");
        _builder.append("<defaultSubject>");
        String _subject_1 = em.getSubject();
        _builder.append(_subject_1, "  ");
        _builder.append("</defaultSubject>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      String _content = em.getContent();
      boolean _operator_equals_2 = ObjectExtensions.operator_equals(_content, null);
      if (_operator_equals_2) {
        _builder.append("  ");
        _builder.append("<defaultContent>${DEFAULT_CONTENT}</defaultContent>");
        _builder.newLine();} else {
        _builder.append("  ");
        _builder.append("<defaultContent>");
        String _content_1 = em.getContent();
        _builder.append(_content_1, "  ");
        _builder.append("</defaultContent>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    _builder.append("<attachmentsPattern></attachmentsPattern>");
    _builder.newLine();
    _builder.append("</hudson.plugins.emailext.ExtendedEmailPublisher>");
    _builder.newLine();
    return _builder;
  }
  
  public StringConcatenation mailTrigger(final MailTrigger mt) {
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
        _builder.append("<recipientList>$PROJECT_DEFAULT_RECIPIENTS</recipientList>");
        _builder.newLine();} else {
        _builder.append("    ");
        _builder.append("<recipientList>");
        String _to_1 = mt.getTo();
        _builder.append(_to_1, "    ");
        _builder.append("</recipientList>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      String _subject = mt.getSubject();
      boolean _operator_equals_1 = ObjectExtensions.operator_equals(_subject, null);
      if (_operator_equals_1) {
        _builder.append("    ");
        _builder.append("<subject>$PROJECT_DEFAULT_SUBJECT</subject>");
        _builder.newLine();} else {
        _builder.append("    ");
        _builder.append("<subject>");
        String _subject_1 = mt.getSubject();
        _builder.append(_subject_1, "    ");
        _builder.append("</subject>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      String _content = mt.getContent();
      boolean _operator_equals_2 = ObjectExtensions.operator_equals(_content, null);
      if (_operator_equals_2) {
        _builder.append("    ");
        _builder.append("<body>$PROJECT_DEFAULT_CONTENT</body>");
        _builder.newLine();} else {
        _builder.append("    ");
        _builder.append("<body>");
        String _content_1 = mt.getContent();
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
  
  protected StringConcatenation _publisher(final TestResult t) {
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
        _builder.append(_testresults_1, "  ");
        _builder.append("</testResults>");
        _builder.newLineIfNotEmpty();
        _builder.append("  ");
        _builder.append("<keepLongStdio>");
        boolean _isLongIO = t.isLongIO();
        _builder.append(_isLongIO, "  ");
        _builder.append("</keepLongStdio>");
        _builder.newLineIfNotEmpty();
        _builder.append("  ");
        _builder.append("<testDataPublishers/>");
        _builder.newLine();
        _builder.append("</hudson.tasks.junit.JUnitResultArchiver>");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  protected StringConcatenation _publisher(final DownStream d) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.parameterizedtrigger.BuildTrigger>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<configs>");
    _builder.newLine();
    {
      EList<DownStreamBuild> _builds = d.getBuilds();
      for(final DownStreamBuild b : _builds) {
        _builder.append("  ");
        StringConcatenation _downStreamBuild = this.downStreamBuild(b);
        _builder.append(_downStreamBuild, "  ");
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
  
  protected StringConcatenation _publisher(final Artifacts a) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.tasks.ArtifactArchiver>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<artifacts>");
    String _artifacts = a.getArtifacts();
    _builder.append(_artifacts, "  ");
    _builder.append("</artifacts>");
    _builder.newLineIfNotEmpty();
    {
      if (false) {
        _builder.append("  ");
        _builder.append("<latestOnly>true</latestOnly>");
        _builder.newLine();} else {
        _builder.append("  ");
        _builder.append("<latestOnly>false</latestOnly>");
        _builder.newLine();
      }
    }
    _builder.append("</hudson.tasks.ArtifactArchiver>");
    _builder.newLine();
    return _builder;
  }
  
  public String getListOfFqNames(final List<Config> builds) {
    {
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
  }
  
  public StringConcatenation downStreamBuild(final DownStreamBuild b) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.parameterizedtrigger.BuildTriggerConfig>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<configs>");
    _builder.newLine();
    {
      EList<EObject> _triggerParams = b.getTriggerParams();
      for(final EObject p : _triggerParams) {
        _builder.append("  ");
        StringConcatenation _triggerParam = this.triggerParam(p);
        _builder.append(_triggerParam, "  ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    _builder.append("</configs>");
    _builder.newLine();
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
    _builder.append(_condition, "  ");
    _builder.append("</condition>");
    _builder.newLineIfNotEmpty();
    {
      EList<EObject> _triggerParams_1 = b.getTriggerParams();
      boolean _isEmpty = _triggerParams_1.isEmpty();
      if (_isEmpty) {
        _builder.append("  ");
        _builder.append("<triggerWithNoParameters>true</triggerWithNoParameters>");
        _builder.newLine();
      }
    }
    _builder.append("</hudson.plugins.parameterizedtrigger.BuildTriggerConfig>");
    _builder.newLine();
    return _builder;
  }
  
  protected StringConcatenation _triggerParam(final CurrentTriggerParams p) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  protected StringConcatenation _triggerParam(final GitCommitParam p) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.git.GitRevisionBuildParameters/>");
    _builder.newLine();
    return _builder;
  }
  
  protected StringConcatenation _triggerParam(final PropertyFileTriggerParams p) {
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
  
  protected StringConcatenation _triggerParam(final PredefinedTriggerParams p) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<hudson.plugins.parameterizedtrigger.PredefinedBuildParameters>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<properties>");
    String _predefined = p.getPredefined();
    _builder.append(_predefined, "  ");
    _builder.append("</properties>");
    _builder.newLineIfNotEmpty();
    _builder.append("</hudson.plugins.parameterizedtrigger.PredefinedBuildParameters>");
    _builder.newLine();
    return _builder;
  }
  
  public String fqn(final EObject c) {
    if ((c instanceof Config)) {
      return _fqn((Config)c);
    } else if ((c instanceof Group)) {
      return _fqn((Group)c);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        java.util.Arrays.<Object>asList(c).toString());
    }
  }
  
  public StringConcatenation param(final Parameter p, final ParameterType b) {
    if ((p instanceof Parameter)
         && (b instanceof BooleanParam)) {
      return _param((Parameter)p, (BooleanParam)b);
    } else if ((p instanceof Parameter)
         && (b instanceof ChoiceParam)) {
      return _param((Parameter)p, (ChoiceParam)b);
    } else if ((p instanceof Parameter)
         && (b instanceof StringParam)) {
      return _param((Parameter)p, (StringParam)b);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        java.util.Arrays.<Object>asList(p, b).toString());
    }
  }
  
  public StringConcatenation scm(final Scm cvs) {
    if ((cvs instanceof ScmCVS)) {
      return _scm((ScmCVS)cvs);
    } else if ((cvs instanceof ScmGit)) {
      return _scm((ScmGit)cvs);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        java.util.Arrays.<Object>asList(cvs).toString());
    }
  }
  
  public StringConcatenation trigger(final EObject t) {
    if ((t instanceof FirstStartTrigger)) {
      return _trigger((FirstStartTrigger)t);
    } else if ((t instanceof PollScmTrigger)) {
      return _trigger((PollScmTrigger)t);
    } else if ((t instanceof TimerTrigger)) {
      return _trigger((TimerTrigger)t);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        java.util.Arrays.<Object>asList(t).toString());
    }
  }
  
  public StringConcatenation wrapper(final EObject l) {
    if ((l instanceof Lock)) {
      return _wrapper((Lock)l);
    } else if ((l instanceof Timeout)) {
      return _wrapper((Timeout)l);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        java.util.Arrays.<Object>asList(l).toString());
    }
  }
  
  public StringConcatenation build(final EObject m) {
    if ((m instanceof Maven)) {
      return _build((Maven)m);
    } else if ((m instanceof Shell)) {
      return _build((Shell)m);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        java.util.Arrays.<Object>asList(m).toString());
    }
  }
  
  public StringConcatenation publisher(final EObject a) {
    if ((a instanceof Artifacts)) {
      return _publisher((Artifacts)a);
    } else if ((a instanceof DownStream)) {
      return _publisher((DownStream)a);
    } else if ((a instanceof ExtMail)) {
      return _publisher((ExtMail)a);
    } else if ((a instanceof TestResult)) {
      return _publisher((TestResult)a);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        java.util.Arrays.<Object>asList(a).toString());
    }
  }
  
  public StringConcatenation triggerParam(final EObject p) {
    if ((p instanceof CurrentTriggerParams)) {
      return _triggerParam((CurrentTriggerParams)p);
    } else if ((p instanceof GitCommitParam)) {
      return _triggerParam((GitCommitParam)p);
    } else if ((p instanceof PredefinedTriggerParams)) {
      return _triggerParam((PredefinedTriggerParams)p);
    } else if ((p instanceof PropertyFileTriggerParams)) {
      return _triggerParam((PropertyFileTriggerParams)p);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        java.util.Arrays.<Object>asList(p).toString());
    }
  }
}