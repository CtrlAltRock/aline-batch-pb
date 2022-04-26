package com.smoothstack.BatchMicroservice;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBatchTest
@EnableAutoConfiguration
@DirtiesContext
class BatchMicroserviceApplicationTests {

	private static final String TEST_OUTPUT = "src/test/resources/output/actual-output.json";

	private static final String EXPECTED_OUTPUT = "src/test/resources/output/expected-output.json";

	private static final String TEST_INPUT = "src/test/resources/input/test-input.csv";

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;

	@After
	public void cleanUp() {
		jobRepositoryTestUtils.removeJobExecutions();
	}

	private JobParameters defaultJobParameters() {
		JobParametersBuilder paramsBuilder = new JobParametersBuilder();
		paramsBuilder.addString("file.input", TEST_INPUT);
		paramsBuilder.addString("file.output", TEST_OUTPUT);
		return paramsBuilder.toJobParameters();
	}

	@Test
	public void givenReferenceOutput_whenJobExecuted_thenSuccess() throws Exception {
		// given
		FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT);
		FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);

		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());
		JobInstance actualJobInstance = jobExecution.getJobInstance();
		ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualJobInstance.getJobName(), is("transformBooksRecords"));
		assertThat(actualJobExitStatus.getExitCode(), is("COMPLETED"));
		AssertFile.assertFileEquals(expectedResult, actualResult);
	}

	@Test
	public void givenReferenceOutput_whenStep1Executed_thenSuccess() throws Exception {

		// given
		FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT);
		FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);

		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("step1", defaultJobParameters());
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualJobExitStatus.getExitCode(), is("COMPLETED"));
		AssertFile.assertFileEquals(expectedResult, actualResult);
	}

	@Test
	public void whenStep2Executed_thenSuccess() {

		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("step2", defaultJobParameters());
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
		actualStepExecutions.forEach(stepExecution -> {assertThat(stepExecution.getWriteCount(), is(8));});
	}

}
