package com.smoothstack.BatchMicroservice.config;

import com.smoothstack.BatchMicroservice.model.Transaction;
import com.smoothstack.BatchMicroservice.processor.*;
import com.smoothstack.BatchMicroservice.tasklet.CleanUpTasklet;
import com.smoothstack.BatchMicroservice.tasklet.analysis.*;
import com.smoothstack.BatchMicroservice.tasklet.generation.*;
import com.smoothstack.BatchMicroservice.writer.XMLItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    StepBuilderFactory stepsFactory;

    @Autowired
    JobBuilderFactory jobsFactory;

    @Value("${input.path}")
    private String inputPath;

    @Value("${output.path.generation}")
    private String outputPathGeneration;

    @Value("${output.path.analysis}")
    private String outputPathAnalysis;

    @Bean
    public Job transactionJob() throws Exception {
        return jobsFactory.get("transactionJob")
                .start(mainFlow())
                .end()
                .build();
    }

    @Bean
    public Flow mainFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("mainFlow")
                .start(threadedStep())
                .next(xmlWriterFlow())
                .next(cleanUpStep())
                .build();
    }

    @Bean
    public FlatFileItemReader<Transaction> csvReader() {
        System.out.println(inputPath);
        return new FlatFileItemReaderBuilder<Transaction>()
                .name("csvFlatFileitemreader")
                .resource(new FileSystemResource(inputPath))
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names("user", "card", "year", "month", "day", "time", "amount", "method",
                        "merchant_name", "merchant_city", "merchant_state", "merchant_zip",
                        "mcc", "errors", "fraud")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Transaction.class);
                }})
                .build();
    }

    @Bean
    public CompositeItemProcessor<Transaction, Object> compositeItemProcessor() throws Exception {
        CompositeItemProcessor<Transaction, Object> compositeProcessor
                = new CompositeItemProcessor<>();

        List<ItemProcessor<Transaction, Object>> processors = Arrays.asList(
                new UserProcessor(), new CardProcessor(), new MerchantProcessor(), new AnalysisProcessor());

        compositeProcessor.setDelegates(processors);
        compositeProcessor.afterPropertiesSet();

        return compositeProcessor;
    }

    @Bean
    public TaskExecutor getTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(12);
        threadPoolTaskExecutor.setMaxPoolSize(12);
        threadPoolTaskExecutor.afterPropertiesSet();

        return threadPoolTaskExecutor;
    }
    @Bean
    public Step threadedStep() throws Exception {
        return stepsFactory.get("transaction step")
                .<Transaction, Object>chunk(1000)
                .reader(csvReader())
                .faultTolerant()
                .retry(FlatFileParseException.class)
                .retryLimit(1)
                .skipPolicy(new TransactionSkipPolicy())
                .processor(compositeItemProcessor())
                .writer(new XMLItemWriter())
                .taskExecutor(getTaskExecutor())
                .build();
    }

    @Bean
    public Flow xmlWriterFlow(){
        return new FlowBuilder<SimpleFlow>("xmlWriterFlow")
                .split(getTaskExecutor())
                .add(
                        bottom5OnlineTransactionsByMonthFlow(),
                        depositsFlow(),
                        over100AndAfter8PMByZipCodeFlow(),
                        transactionsByStateNoFraudFlow(),
                        top5TransactionByCityFlow(),
                        top5TransactionByZipCodeFlow(),
                        top10LargestTransactionFlow(),
                        transactionTypeFlow(),
                        fraudByYearFlow(),
                        userBalanceOnce(),
                        userBalanceOver(),
                        top5RecurringFlow(),
                        totalUniqueFlow(),
                        userWriterFlow(),
                        cardWriterFlow(),
                        merchantWriterFlow(),
                        locationWriterFlow(),
                        stateWriterFlow()
                )
                .build();
    }

    private Step cleanUpStep() {
        return stepsFactory.get("cleanUpStep")
                .tasklet(new CleanUpTasklet())
                .build();
    }

    // flows and steps
    @Bean
    public Flow userWriterFlow(){
        return new FlowBuilder<SimpleFlow>("userWriterFlow")
                .start(userWriter())
                .build();
    }

    @Bean
    public Step userWriter(){
        return stepsFactory.get("userWriter")
                .tasklet(new XMLUserWriter(outputPathGeneration))
                .build();
    }

    @Bean
    public Flow cardWriterFlow(){
        return new FlowBuilder<SimpleFlow>("cardWriterFlow")
                .start(cardWriter())
                .build();
    }

    @Bean
    public Step cardWriter(){
        return stepsFactory.get("cardWriter")
                .tasklet(new XMLCardWriter(outputPathGeneration))
                .build();
    }

    @Bean
    public Flow merchantWriterFlow(){
        return new FlowBuilder<SimpleFlow>("merchantWriterFlow")
                .start(merchantWriter())
                .build();
    }

    @Bean
    public Step merchantWriter(){
        return stepsFactory.get("merchantWriter")
                .tasklet(new XMLMerchantWriter(outputPathGeneration))
                .build();
    }
    @Bean
    public Flow locationWriterFlow(){
        return new FlowBuilder<SimpleFlow>("locationWriterFlow")
                .start(locationWriter())
                .build();
    }

    @Bean
    public Step locationWriter(){
        return stepsFactory.get("locationWriter")
                .tasklet(new XMLLocationWriter(outputPathGeneration))
                .build();
    }

    @Bean
    public Flow stateWriterFlow(){
        return new FlowBuilder<SimpleFlow>("stateWriterFlow")
                .start(stateWriter())
                .build();
    }

    @Bean
    public Step stateWriter(){
        return stepsFactory.get("stateWriter")
                .tasklet(new XMLStateWriter(outputPathGeneration))
                .build();
    }

    @Bean
    public Flow fraudByYearFlow(){
        return new FlowBuilder<SimpleFlow>("fraudByYearFlow")
                .start(fraudByYearStep())
                .build();
    }
    @Bean
    public Step fraudByYearStep(){
        return stepsFactory.get("fraudByYearStep")
                .tasklet(new FraudByYearWriter(outputPathAnalysis))
                .build();
    }

    @Bean
    public Flow top5RecurringFlow(){
        return new FlowBuilder<SimpleFlow>("top5recurring")
                .start(top5RecurringStep())
                .build();
    }

    @Bean Step top5RecurringStep(){
        return stepsFactory.get("top5RecurringStep")
                .tasklet(new Top5RecurringWriter(outputPathAnalysis))
                .build();
    }

    @Bean
    public Flow totalUniqueFlow(){
        return new FlowBuilder<SimpleFlow>("totalUnique")
                .start(totalUniqueStep())
                .build();
    }

    @Bean Step totalUniqueStep(){
        return stepsFactory.get("totalUniqueStep")
                .tasklet(new TotalUniqueMerchantWriter(outputPathAnalysis))
                .build();
    }

    @Bean
    public Flow userBalanceOnce(){
        return new FlowBuilder<SimpleFlow>("userBalanceOnce")
                .start(userBalanceOnceStep())
                .build();
    }

    @Bean Step userBalanceOnceStep(){
        return stepsFactory.get("userBalanceOnceStep")
                .tasklet(new InsufficientBalanceOnceWriter(outputPathAnalysis))
                .build();
    }

    @Bean
    public Flow userBalanceOver(){
        return new FlowBuilder<SimpleFlow>("userBalanceOver")
                .start(userBalanceOverStep())
                .build();
    }

    @Bean Step userBalanceOverStep(){
        return stepsFactory.get("userBalanceOverStep")
                .tasklet(new InsufficientBalanceOverWriter(outputPathAnalysis))
                .build();
    }

    @Bean
    public Flow transactionTypeFlow(){
        return new FlowBuilder<SimpleFlow>("transactionTypeFlow")
                .start(transactionTypeStep())
                .build();
    }

    @Bean Step transactionTypeStep(){
        return stepsFactory.get("transactionTypeStep")
                .tasklet(new TransactionTypeWriter(outputPathAnalysis))
                .build();
    }

    @Bean
    public Flow top10LargestTransactionFlow(){
        return new FlowBuilder<SimpleFlow>("top10LargestTransactionFlow")
                .start(top10LargestTransactionStep())
                .build();
    }

    @Bean Step top10LargestTransactionStep(){
        return stepsFactory.get("top10LargestTransactionStep")
                .tasklet(new Top10LargestTransactionWriter(outputPathAnalysis))
                .build();
    }

    @Bean
    public Flow top5TransactionByZipCodeFlow(){
        return new FlowBuilder<SimpleFlow>("top5TransactionByZipCodeFlow")
                .start(top5TransactionByZipCodeStep())
                .build();
    }

    @Bean Step top5TransactionByZipCodeStep(){
        return stepsFactory.get("top5TransactionByZipCodeStep")
                .tasklet(new Top5TransactionsZipcodeWriter(outputPathAnalysis))
                .build();
    }

    @Bean
    public Flow top5TransactionByCityFlow(){
        return new FlowBuilder<SimpleFlow>("top5TransactionByCityFlow")
                .start(top5TransactionByCityStep())
                .build();
    }

    @Bean Step top5TransactionByCityStep(){
        return stepsFactory.get("top5TransactionByCityStep")
                .tasklet(new Top5TransactionsCityWriter(outputPathAnalysis))
                .build();
    }

    @Bean
    public Flow transactionsByStateNoFraudFlow(){
        return new FlowBuilder<SimpleFlow>("transactionsByStateNoFraudFlow")
                .start(transactionsByStateNoFraudStep())
                .build();
    }

    @Bean Step transactionsByStateNoFraudStep(){
        return stepsFactory.get("transactionsByStateNoFraudStep")
                .tasklet(new TransactionsByStateNoFraudWriter(outputPathAnalysis))
                .build();
    }

    @Bean
    public Flow over100AndAfter8PMByZipCodeFlow(){
        return new FlowBuilder<SimpleFlow>("over100AndAfter8PMByZipCodeFlow")
                .start(over100AndAfter8PMByZipCodeStep())
                .build();
    }

    @Bean Step over100AndAfter8PMByZipCodeStep(){
        return stepsFactory.get("over100AndAfter8PMByZipCodeStep")
                .tasklet(new Over100AndAfter8PMByZipCode(outputPathAnalysis))
                .build();
    }

    @Bean
    public Flow depositsFlow(){
        return new FlowBuilder<SimpleFlow>("depositsFlow")
                .start(depositsStep())
                .build();
    }

    @Bean Step depositsStep(){
        return stepsFactory.get("depositsStep")
                .tasklet(new DepositsWriter(outputPathAnalysis))
                .build();
    }

    @Bean
    public Flow bottom5OnlineTransactionsByMonthFlow(){
        return new FlowBuilder<SimpleFlow>("bottom5OnlineTransactionsByMonthFlow")
                .start(bottom5OnlineTransactionsByMonthStep())
                .build();
    }

    @Bean Step bottom5OnlineTransactionsByMonthStep(){
        return stepsFactory.get("bottom5OnlineTransactionsByMonthStep")
                .tasklet(new Bottom5OnlineTransactionsByMonth(outputPathAnalysis))
                .build();
    }
}
