package com.smoothstack.BatchMicroservice;

import com.smoothstack.BatchMicroservice.config.BatchConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {BatchConfig.class})
@TestPropertySource(
		properties = {
				"input.path = C:/Projects/Smoothstack/Assignments/Sprints/AlineFinancial/aline-batch-microservice/src/test/resources/TestData/test2.csv",
				"output.path.generation = C:/Projects/Smoothstack/Assignments/Sprints/AlineFinancial/aline-batch-microservice/src/test/ProcessedOutTestFiles/Generation/",
				"output.path.analysis = C:/Projects/Smoothstack/Assignments/Sprints/AlineFinancial/aline-batch-microservice/src/test/ProcessedOutTestFiles/Analysis/"
})
class BatchTests {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;

	@AfterEach
	public void cleanUp() {
		jobRepositoryTestUtils.removeJobExecutions();
	}

	@Test
	public void givenReferenceOutput_whenJobExecuted_thenSuccess() throws Exception {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		JobInstance actualJobInstance = jobExecution.getJobInstance();
		ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualJobInstance.getJobName(), is("transactionJob"));
		assertThat(actualJobExitStatus.getExitCode(), is("COMPLETED"));
	}

	@Test
	public void givenReferenceOutput_whenStep1Executed_thenSuccess() throws Exception {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("transaction step");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualJobExitStatus.getExitCode(), is("COMPLETED"));
	}

	@Test
	public void whenStep2Executed_thenSuccess() {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("userWriter");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
	}

	@Test
	public void whenStep3Executed_thenSuccess() {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("cardWriter");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
	}

	@Test
	public void whenStep4Executed_thenSuccess() {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("merchantWriter");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
	}

	@Test
	public void whenStep5Executed_thenSuccess() {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("locationWriter");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
	}

	@Test
	public void whenStep6Executed_thenSuccess() {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("stateWriter");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
	}

	@Test
	public void whenStep7Executed_thenSuccess() {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("fraudByYearStep");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
	}

	@Test
	public void whenStep8Executed_thenSuccess() {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("top5RecurringStep");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
	}

	@Test
	public void whenStep9Executed_thenSuccess() {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("totalUniqueStep");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
	}

	@Test
	public void whenStep10Executed_thenSuccess() {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("userBalanceOnceStep");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
	}

	@Test
	public void whenStep11Executed_thenSuccess() {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("userBalanceOverStep");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
	}

	@Test
	public void whenStep12Executed_thenSuccess() {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("transactionTypeStep");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
	}

	@Test
	public void whenStep13Executed_thenSuccess() {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("top5TransactionByZipCodeStep");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
	}

	@Test
	public void whenStep14Executed_thenSuccess() {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("top5TransactionByCityStep");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
	}

	@Test
	public void whenStep15Executed_thenSuccess() {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("transactionsByStateNoFraudStep");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
	}

	@Test
	public void whenStep16Executed_thenSuccess() {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("over100AndAfter8PMByZipCodeStep");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
	}


	@Test
	public void whenStepCleanupExecuted_thenSuccess() {
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("cleanUpStep");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
	}
}
