
package de.hajoeichler.jenkins;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class JobConfigStandaloneSetup extends JobConfigStandaloneSetupGenerated{

	public static void doSetup() {
		new JobConfigStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

