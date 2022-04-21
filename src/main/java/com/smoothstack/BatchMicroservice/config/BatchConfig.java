package com.smoothstack.BatchMicroservice.config;

import com.smoothstack.BatchMicroservice.model.Transaction;
import com.smoothstack.BatchMicroservice.model.User;
import com.smoothstack.BatchMicroservice.processor.TestProcessor;
import com.smoothstack.BatchMicroservice.writer.ConsoleItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    StepBuilderFactory stepsFactory;

    @Autowired
    JobBuilderFactory jobsFactory;

    @Autowired
    JobLauncher jobLauncher;

    @Bean
    public Job transactionJob() {
        return jobsFactory.get("transactionJob")
                .start(fakeFlow())
                .end()
                .build();
    }

    @Bean
    public Flow fakeFlow() {
        return new FlowBuilder<SimpleFlow>("fakeFlow")
                .start(threadedStep())
                .build();
    }

    @Bean
    public Step threadedStep() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(12);
        threadPoolTaskExecutor.setMaxPoolSize(500);
        threadPoolTaskExecutor.afterPropertiesSet();

        return stepsFactory.get("Card Process Step")
                .<Transaction, User>chunk(100000)
                .reader(csvReader())
                .processor(new TestProcessor())
//                .writer(userXmlWriter())
                .writer(new ConsoleItemWriter())
                .taskExecutor(threadPoolTaskExecutor)
                .build();
    }

    @Bean
    public FlatFileItemReader<Transaction> csvReader() {
        return new FlatFileItemReaderBuilder<Transaction>()
                .name("csvflatfileitemreader")
                .resource(new FileSystemResource("C:\\Users\\Patrick\\Downloads\\credit_card\\transactions\\transactions\\card_transaction.v1.csv"))
                .linesToSkip(1)
                .delimited()
                .names("user", "card", "year", "month", "day", "time", "amount", "method", "merchant_name", "merchant_city", "merchant_state", "merchant_zip", "mcc", "errors", "fraud")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Transaction>() {{
                    setTargetType(Transaction.class);
                }})
                .build();
    }

    public XStreamMarshaller userXmlMarshaller() {


        HashMap<String, Class> aliases = new HashMap<>();
        aliases.put("user",User.class);


        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(aliases);

        return marshaller;
    }

    public ItemWriter<User> userXmlWriter() {
        FileSystemResource path = new FileSystemResource("src/main/ProcessedOutFiles/GeneratedUsers.xml");
//        XStreamMarshaller transactionV2Marshaller = new XStreamMarshaller();
//        transactionV2Marshaller.setAliases(Collections.singletonMap(
//                "Users",
//                User.class
//        ));
        return new StaxEventItemWriterBuilder<User>()
                .name("userXmlWriter")
                .resource(path)
                .marshaller(userXmlMarshaller())
                .rootTagName("GeneratedUsers")
                .build();
    }

}
