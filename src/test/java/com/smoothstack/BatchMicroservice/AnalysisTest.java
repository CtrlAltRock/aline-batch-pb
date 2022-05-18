package com.smoothstack.BatchMicroservice;

import com.smoothstack.BatchMicroservice.maps.MerchantMap;
import com.smoothstack.BatchMicroservice.maps.TransactionMap;
import com.smoothstack.BatchMicroservice.maps.UserMap;
import com.smoothstack.BatchMicroservice.model.Transaction;
import com.smoothstack.BatchMicroservice.model.analysis.Top5RecurringTransaction;
import com.smoothstack.BatchMicroservice.processor.AnalysisProcessor;
import com.smoothstack.BatchMicroservice.processor.MerchantProcessor;
import com.smoothstack.BatchMicroservice.processor.UserProcessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(
        properties = {
                "input.path = C:/Projects/Smoothstack/Assignments/Sprints/AlineFinancial/aline-batch-microservice/src/test/resources/TestData/test2.csv",
                "output.path.generation = C:/Projects/Smoothstack/Assignments/Sprints/AlineFinancial/aline-batch-microservice/src/test/ProcessedOutTestFiles/Generation/",
                "output.path.analysis = C:/Projects/Smoothstack/Assignments/Sprints/AlineFinancial/aline-batch-microservice/src/test/ProcessedOutTestFiles/Analysis/"
        })
public class AnalysisTest {
        //Maps
        private final TransactionMap tMap = TransactionMap.getInstance();
        private final UserMap userMap = UserMap.getInstance();
        private final MerchantMap merchantMap = MerchantMap.getInstance();

        //Processors
        private final AnalysisProcessor analysisProcessor = new AnalysisProcessor();
        private final UserProcessor userProcessor = new UserProcessor();
        private final MerchantProcessor merchantProcessor = new MerchantProcessor();

        public List<Transaction> setUp(){
                List<Transaction> ts = new ArrayList<>();
                Transaction t1 = new Transaction();
                Transaction t2 = new Transaction();
                Transaction t3 = new Transaction();
                Transaction t4 = new Transaction();

                t1.setYear(2021);
                t1.setFraud("No");
                t1.setErrors("");
                t1.setUser(0L);
                t1.setMerchant_name("1234567898");
                t1.setCard(0L);
                t1.setAmount("$24.64");
                t1.setMethod("Swipe");

                t2.setYear(2022);
                t2.setFraud("No");
                t2.setErrors("Insufficient Balance");
                t2.setUser(1L);
                t2.setMerchant_name("1234567899");
                t2.setCard(2L);
                t2.setAmount("$543.34");
                t2.setMethod("Chip");

                t3.setYear(2022);
                t3.setFraud("Yes");
                t3.setErrors("Insufficient Balance");
                t3.setUser(2L);
                t3.setMerchant_name("1234567890");
                t3.setCard(1L);
                t3.setAmount("$25.25");
                t3.setMethod("Online");

                t4.setYear(2022);
                t4.setFraud("No");
                t4.setErrors("Insufficient Balance");
                t4.setUser(2L);
                t4.setMerchant_name("1234567890");
                t4.setCard(1L);
                t4.setAmount("$25.25");
                t4.setMethod("Online");

                ts.add(t1);
                ts.add(t2);
                ts.add(t3);
                ts.add(t4);

                return ts;
        }

        @AfterEach
        public void cleanup(){
                tMap.clearAll();
                userMap.clearAll();
        }


        @Test
        public void correctYearAndType() throws Exception {
                List<Transaction> transactions = setUp();
                transactions.forEach(t -> {
                        try {
                                analysisProcessor.process(t);
                        } catch (Exception e) {
                                throw new RuntimeException(e);
                        }
                });
                // fraud
                assertEquals(2, tMap.getSyncTransactionByYear().size());
                assertEquals(1, tMap.getSyncTransactionByYear().get(2021));
                assertEquals(1, tMap.getSyncFraudByYear().size());
                assertEquals(1, tMap.getSyncFraudByYear().get(2022));

                // type
                assertEquals(3, tMap.getTransactionType().size());
        }

        @Test
        public void insufficientBalance(){
                List<Transaction> transactions = setUp();
                transactions.forEach(t -> {
                        try {
                                userProcessor.process(t);
                        } catch (Exception e) {
                                throw new RuntimeException(e);
                        }
                });
                assertEquals(2, userMap.getInsufficientBalanceByUser().size());
                assertEquals(1, userMap.getInsufficientBalanceByUser().get(1L));
                assertEquals(2, userMap.getInsufficientBalanceByUser().get(2L));
        }

        @Test
        public void uniqueMerchantAndRecurringTransaction(){
                List<Transaction> transactions = setUp();
                transactions.forEach(t -> {
                        try {
                                merchantProcessor.process(t);
                        } catch (Exception e) {
                                throw new RuntimeException(e);
                        }
                });
                // unique
                assertEquals(3, merchantMap.getGeneratedMerchants().size());
                // recurring
                assertEquals(3 , merchantMap.getRecurringTransaction().size());
                assertEquals(2, merchantMap.getRecurringTransaction().get("1234567890").get(getTop5().toString()));
        }

        public Top5RecurringTransaction getTop5(){
                Transaction t4 = new Transaction();
                t4.setYear(2022);
                t4.setFraud("No");
                t4.setErrors("Insufficient Balance");
                t4.setUser(2L);
                t4.setMerchant_name("1234567890");
                t4.setCard(1L);
                t4.setAmount("$25.25");
                return new Top5RecurringTransaction(t4);
        }

}
