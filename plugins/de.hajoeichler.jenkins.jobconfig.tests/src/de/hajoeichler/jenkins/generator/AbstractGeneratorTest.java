package de.hajoeichler.jenkins.generator;

import static org.junit.Assert.assertTrue;

import java.io.File;

import junitx.framework.FileAssert;

public class AbstractGeneratorTest {

	protected void assertSingleConfig(String configFile, String jobName) {
		Main.main(new String[] { "./testdata/config/" + configFile });
		File configsDir = new File("./target/configs/" + jobName);
		assertTrue("We expect a folder with the name of job.",
				configsDir.isDirectory() && configsDir.exists());
		File configXml = new File(configsDir, "config.xml");
		assertTrue("We expect a config.xml.",
				configXml.isFile() && configXml.exists());

		FileAssert.assertEquals(
				new File("./testdata/config/"
						+ configFile.replace(".jobConfig", ".xml")), configXml);
	}
}
