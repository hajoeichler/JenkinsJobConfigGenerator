package de.hajoeichler.jenkins.validation;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.validation.Check;

import de.hajoeichler.jenkins.generator.JobConfigGenerator;
import de.hajoeichler.jenkins.jobConfig.Config;
import de.hajoeichler.jenkins.jobConfig.JobConfigPackage;
import de.hajoeichler.jenkins.jobConfig.Shell;
import de.hajoeichler.jenkins.jobConfig.StringParam;

public class JobConfigJavaValidator extends AbstractJobConfigJavaValidator {

	@Check
	public void checkShell(Shell shell) {
		String string = shell.getShellScript();
		checkForJobUrl(string,
				JobConfigPackage.eINSTANCE.getShell_ShellScript(), shell);
	}

	@Check
	public void checkStringParam(StringParam stringParam) {
		checkForJobUrl(stringParam.getValue(),
				JobConfigPackage.eINSTANCE.getStringParam_Value(), stringParam);
	}

	private void checkForJobUrl(String string, EStructuralFeature feature,
			EObject container) {
		if (!string.contains("job/")) {
			return;
		}
		int index = string.indexOf("job/", 0);
		int nextSlash = string.indexOf("/", index + 4);
		if (nextSlash == -1) {
			return;
		}
		String jobName = string.substring(index + 4, nextSlash);
		EObject rootContainer = EcoreUtil.getRootContainer(container);
		List<Config> allJobs = EcoreUtil2.getAllContentsOfType(rootContainer,
				Config.class);
		JobConfigGenerator jobConfigGenerator = new JobConfigGenerator();
		for (Config config : allJobs) {
			String fqn = jobConfigGenerator.fqn(config);
			if (fqn.equals(jobName)) {
				return;
			}
		}
		//warning("The URL does not point to a valid job name.", feature);
	}
}
