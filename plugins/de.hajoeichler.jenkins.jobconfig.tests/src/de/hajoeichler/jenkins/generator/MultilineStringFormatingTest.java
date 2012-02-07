package de.hajoeichler.jenkins.generator;

import org.junit.Test;

public class MultilineStringFormatingTest extends AbstractGeneratorTest {

	@Test
	public void testEmptyJobConfig() throws Exception {
		assertSingleConfig("emptyJob.jobConfig", "foo");
	}

	@Test
	public void testMultiLineShell() throws Exception {
		assertSingleConfig("multiLineShell.jobConfig", "multiLineShell");
	}

}
