package com.smoothstack.BatchMicroservice.config;

import com.smoothstack.BatchMicroservice.model.Transaction;
import com.smoothstack.BatchMicroservice.processor.*;
import com.smoothstack.BatchMicroservice.tasklet.*;
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

    @Value("${output.path}")
    private String outputPath;

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
                .build();
    }

    @Bean
    public Flow xmlWriterFlow(){
        return new FlowBuilder<SimpleFlow>("xmlWriterFlow")
                .split(getTaskExecutor())
                .add(userWriterFlow(), cardWriterFlow(),
                        merchantWriterFlow(), locationWriterFlow(),
                        stateWriterFlow(), analysisFlow())
                .build();
    }

    @Bean
    public Flow userWriterFlow(){
        return new FlowBuilder<SimpleFlow>("userWriterFlow")
                .start(userWriter())
                .build();
    }

    @Bean
    public Step userWriter(){
        return stepsFactory.get("userWriter")
                .tasklet(new XMLUserWriter(outputPath))
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
                .tasklet(new XMLCardWriter(outputPath))
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
                .tasklet(new XMLMerchantWriter(outputPath))
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
                .tasklet(new XMLLocationWriter(outputPath))
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
                .tasklet(new XMLStateWriter(outputPath))
                .build();
    }


    @Bean
    public TaskExecutor getTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(6);
        threadPoolTaskExecutor.setMaxPoolSize(250);
        threadPoolTaskExecutor.afterPropertiesSet();

        return threadPoolTaskExecutor;
    }

    @Bean
    public Step threadedStep() throws Exception {
        return stepsFactory.get("transaction step")
                .<Transaction, Object>chunk(1000)
                .reader(csvReader())
                .faultTolerant()
                .retryLimit(1)
                .retry(Exception.class)
                .skipPolicy(new TransactionSkipPolicy())
                .processor(compositeItemProcessor())
                .writer(new XMLItemWriter())
                .taskExecutor(getTaskExecutor())
                .build();
    }

    @Bean
    public Flow analysisFlow(){
        return new FlowBuilder<SimpleFlow>("analysisFlow")
                .start(xmlWriterStep())
                .build();
    }
    @Bean
    public Step xmlWriterStep(){
        return  stepsFactory.get("xmlWriterStep")
                .tasklet(new XmlWriterTasklet(outputPath))
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


}
