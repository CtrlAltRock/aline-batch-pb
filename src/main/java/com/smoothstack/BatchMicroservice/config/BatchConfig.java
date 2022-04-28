package com.smoothstack.BatchMicroservice.config;

import com.smoothstack.BatchMicroservice.model.Transaction;
import com.smoothstack.BatchMicroservice.processor.CardProcessor;
import com.smoothstack.BatchMicroservice.processor.MerchantProcessor;
import com.smoothstack.BatchMicroservice.processor.TransactionSkipPolicy;
import com.smoothstack.BatchMicroservice.processor.UserProcessor;
import com.smoothstack.BatchMicroservice.tasklet.XmlWriterTasklet;
import com.smoothstack.BatchMicroservice.writer.XMLItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
                .start(threadedStep())
                .next(xmlWriterStep())
                .build();
    }

    @Bean
    public Step threadedStep() throws Exception {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(6);
        threadPoolTaskExecutor.setMaxPoolSize(250);
        threadPoolTaskExecutor.afterPropertiesSet();

        return stepsFactory.get("transaction step")
                .<Transaction, Object>chunk(1000)
                .reader(csvReader())
                .faultTolerant()
                .retryLimit(1)
                .retry(Exception.class)
                .skipPolicy(new TransactionSkipPolicy())
                .processor(compositeItemProcessor())
                .writer(new XMLItemWriter())
                .taskExecutor(threadPoolTaskExecutor)
                .build();
    }

    @Bean
    public FlatFileItemReader<Transaction> csvReader() {
        System.out.println(inputPath);
        return new FlatFileItemReaderBuilder<Transaction>()
                .name("csvflatfileitemreader")
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
                new UserProcessor(), new CardProcessor(), new MerchantProcessor());

        compositeProcessor.setDelegates(processors);
        compositeProcessor.afterPropertiesSet();

        return compositeProcessor;
    }


    @Bean
    public Step xmlWriterStep(){
        return  stepsFactory.get("xmlWriterStep")
                .tasklet(new XmlWriterTasklet(outputPath))
                .build();
    }
}
