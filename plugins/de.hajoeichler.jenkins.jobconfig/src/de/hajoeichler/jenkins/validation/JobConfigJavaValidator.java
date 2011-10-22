package de.hajoeichler.jenkins.validation;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.validation.Check;

import de.hajoeichler.jenkins.jobConfig.Config;
import de.hajoeichler.jenkins.jobConfig.JobConfigPackage;
import de.hajoeichler.jenkins.jobConfig.Shell;

public class JobConfigJavaValidator extends AbstractJobConfigJavaValidator {

	@Check
	public void checkUrlInShellScript(Shell shell) {
		String shellScript = shell.getShellScript();
		if (!shellScript.contains("/job/")) {
			return;
		}
		int index = shellScript.indexOf("/job/", 0);
		int nextSlash = shellScript.indexOf("/", index + 5);
		if (nextSlash == -1) {
			return;
		}
		String jobName = shellScript.substring(index + 5, nextSlash);
		EObject rootContainer = EcoreUtil.getRootContainer(shell);
		List<Config> allJobs = EcoreUtil2.getAllContentsOfType(rootContainer, Config.class);
		for (Config config : allJobs) {
			if (config.getName().equals(jobName)) {
				return;
			}
		}
		warning("The URL does not point to a valid job name.", JobConfigPackage.eINSTANCE.getShell_ShellScript());
	}
}
