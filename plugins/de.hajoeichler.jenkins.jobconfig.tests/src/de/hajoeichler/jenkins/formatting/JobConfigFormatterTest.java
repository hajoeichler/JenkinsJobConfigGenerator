package de.hajoeichler.jenkins.formatting;

import org.junit.Test;

public class JobConfigFormatterTest {

	@Test(expected=NullPointerException.class)
	public void checkFor2SpaceIntend() throws Exception {
		JobConfigFormatter formatter = new JobConfigFormatter();
		formatter.configureFormatting(null);
	}
}
