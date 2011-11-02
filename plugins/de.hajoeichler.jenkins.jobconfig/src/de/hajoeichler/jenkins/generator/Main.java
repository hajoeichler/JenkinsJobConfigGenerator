package de.hajoeichler.jenkins.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Aborting: no path to EMF resource provided!");
			return;
		}
		Injector injector = new de.hajoeichler.jenkins.JobConfigStandaloneSetupGenerated()
				.createInjectorAndDoEMFRegistration();
		Main main = injector.getInstance(Main.class);
		main.runGenerator(args[0]);
	}

	@Inject
	private Provider<ResourceSet> resourceSetProvider;

	@Inject
	private IResourceValidator validator;

	@Inject
	private IGenerator generator;

	@Inject
	private JavaIoFileSystemAccess fileAccess;

	protected void runGenerator(String directory) {
		// get all the jobConfig
		File dir = new File(directory);
		File[] jobsFiles = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".jobConfig");
			}
		});
		// load the resources
		ResourceSet set = resourceSetProvider.get();
		for (File file : jobsFiles) {
			set.getResource(URI.createURI(file.getAbsolutePath()), true);
		}

		for (Resource resource : set.getResources()) {
			// validate the resource
			List<Issue> list = validator.validate(resource, CheckMode.ALL,
					CancelIndicator.NullImpl);
			if (!list.isEmpty()) {
				for (Issue issue : list) {
					System.err.println(issue);
				}
				return;
			}
			// configure and start the generator
			fileAccess.setOutputPath("target/configs/");
			generator.doGenerate(resource, fileAccess);
		}

		try {
			cutLeadingSpacesInMultiLineStrings();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Code generation finished.");
	}

	private void cutLeadingSpacesInMultiLineStrings()
			throws FileNotFoundException, IOException {
		File target = new File("target/configs/");
		File[] configDirs = target.listFiles(new FileFilter() {
			public boolean accept(File arg0) {
				if (!arg0.isDirectory()) {
					return false;
				}
				File[] files = arg0.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return "config.xml".equals(name);
					}
				});
				return files.length == 1;
			}
		});
		for (File configDir : configDirs) {
			File configFile = new File(configDir, "config.xml");
			BufferedReader reader = new BufferedReader(new FileReader(
					configFile));
			StringBuilder fileContent = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.matches("^\\p{Blank}*<.*")) {
					fileContent.append(line.replaceAll("^\\p{Blank}*", ""));
				} else {
					fileContent.append(line);
				}
				fileContent.append("\n");
			}
			reader.close();
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					configFile));
			writer.write(fileContent.toString());
			writer.close();
		}
	}
}