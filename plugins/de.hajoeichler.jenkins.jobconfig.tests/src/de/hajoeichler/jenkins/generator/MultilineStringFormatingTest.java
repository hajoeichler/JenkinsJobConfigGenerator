package de.hajoeichler.jenkins.generator;

import static org.junit.Assert.*;

import org.junit.Test;

public class MultilineStringFormatingTest extends AbstractGeneratorTest {

	@Test
	public void testEmptyJobConfig() throws Exception {
		assertSingleConfig("emptyJob", "foo");
	}

	@Test
	public void testMultiLineShell() throws Exception {
		assertSingleConfig("multiLineShell", "multiLineShell");
	}

	@Test
	public void overridingOfWrapper() throws Exception {
		assertSingleConfig("overriddenTimeout", "test-overriddenTimeout");
	}

	@Test
	public void hipChat() throws Exception {
		assertSingleConfig("hipchat", "JobWithHipChat");
	}
}
