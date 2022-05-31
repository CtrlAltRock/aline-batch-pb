package com.smoothstack.BatchMicroservice;

import com.smoothstack.BatchMicroservice.maps.MerchantMap;
import com.smoothstack.BatchMicroservice.maps.TransactionMap;
import com.smoothstack.BatchMicroservice.maps.UserMap;
import com.smoothstack.BatchMicroservice.model.Transaction;
import com.smoothstack.BatchMicroservice.model.analysis.Top5RecurringTransaction;
import com.smoothstack.BatchMicroservice.processor.AnalysisProcessor;
import com.smoothstack.BatchMicroservice.processor.CardProcessor;
import com.smoothstack.BatchMicroservice.processor.MerchantProcessor;
import com.smoothstack.BatchMicroservice.processor.UserProcessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        private final CardProcessor cardProcessor = new CardProcessor();

        @BeforeEach
        public void setUp(){
                tMap.clearAll();
                userMap.clearAll();
                merchantMap.clearAll();

                List<Transaction> ts = new ArrayList<>();
                Transaction t1 = new Transaction();
                Transaction t2 = new Transaction();
                Transaction t3 = new Transaction();
                Transaction t4 = new Transaction();

                t1.setUser(0L);
                t1.setCard(0L);
                t1.setYear(2021);
                t1.setAmount("$24.64");
                t1.setTime("21:00");
                t1.setMethod("Swipe");
                t1.setMerchant_name("1234567898");
                t1.setMerchant_city("Los Angeles");
                t1.setMerchant_state("CA");
                t1.setMerchant_zip("12345.0");
                t1.setErrors("");
                t1.setFraud("No");

                t2.setUser(1L);
                t2.setCard(2L);
                t2.setYear(2022);
                t2.setAmount("$543.34");
                t2.setTime("21:00");
                t2.setMethod("Chip");
                t2.setMerchant_name("1234567899");
                t2.setMerchant_city("Houston");
                t2.setMerchant_state("TX");
                t2.setMerchant_zip("12345.0");
                t2.setErrors("Insufficient Balance");
                t2.setFraud("No");

                t3.setUser(2L);
                t3.setCard(1L);
                t3.setYear(2022);
                t3.setAmount("$25.25");
                t3.setTime("21:00");
                t3.setMethod("Online");
                t3.setMerchant_name("1234567890");
                t3.setMerchant_city("ONLINE");
                t3.setMerchant_state("");
                t3.setMerchant_zip("12346.0");
                t3.setErrors("Insufficient Balance");
                t3.setFraud("Yes");

                t4.setUser(2L);
                t4.setCard(1L);
                t4.setYear(2022);
                t4.setAmount("$25.25");
                t4.setTime("21:00");
                t4.setMethod("Online");
                t4.setMerchant_name("1234567890");
                t4.setMerchant_city("ONLINE");
                t4.setMerchant_state("");
                t4.setMerchant_zip("12346.0");
                t4.setErrors("Insufficient Balance");
                t4.setFraud("No");

                ts.add(t1);
                ts.add(t2);
                ts.add(t3);
                ts.add(t4);

                ts.forEach(t -> {
                        userProcessor.process(t);
                        cardProcessor.process(t);
                        merchantProcessor.process(t);
                        analysisProcessor.process(t);
                });
        }

        @AfterEach
        public void cleanup(){
                tMap.clearAll();
                userMap.clearAll();
                merchantMap.clearAll();
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

        @Test
        public void correctYearFraud(){
                // fraud
                assertEquals(2, tMap.getSyncTransactionByYear().size());
                assertEquals(1, tMap.getSyncTransactionByYear().get(2021));
                assertEquals(1, tMap.getSyncFraudByYear().size());
                assertEquals(1, tMap.getSyncFraudByYear().get(2022));
        }

        @Test
        public void transactionType(){
                // type
                assertEquals(3, tMap.getTransactionType().size());
        }

        @Test
        public void top10Largest(){
                // top 10 largest
                assertEquals(4, tMap.getSyncTop10LargestTransaction().size());
        }

        @Test
        public void top5ByZip(){
                // top 5 by zip
                assertEquals(2, tMap.getSyncZipCodeTransaction().size());
                assertEquals(2, tMap.getSyncZipCodeTransaction().get("12346.0"));
                assertEquals(2, tMap.getSyncZipCodeTransaction().get("12345.0"));
        }

        @Test
        public void insufficientBalance(){
                // insufficient balance
                assertEquals(2, userMap.getInsufficientBalanceByUser().size());
                assertEquals(1, userMap.getInsufficientBalanceByUser().get(1L));
                assertEquals(2, userMap.getInsufficientBalanceByUser().get(2L));
        }

        @Test
        public void uniqueMerchant(){
                // unique
                assertEquals(3, merchantMap.getGeneratedMerchants().size());

        }

        @Test
        public void recurringTransaction(){
                // recurring
                assertEquals(3 , merchantMap.getRecurringTransaction().size());
                assertEquals(2, merchantMap.getRecurringTransaction().get("1234567890").get(getTop5().toString()));
        }

        @Test
        public void top5ByCity(){
                // top 5 by city
                assertEquals(1, tMap.getSyncCityTransaction().get("Houston"));
                assertEquals(1, tMap.getSyncCityTransaction().get("Los Angeles"));
                assertFalse(tMap.getSyncCityTransaction().containsKey("ONLINE"));
        }

        @Test
        public void transactionsByStateNoFraud(){
                // transactions by state no fraud
                assertEquals(2, tMap.getSyncTransactionByStateNoFraud().size());
                assertEquals(1, tMap.getSyncTransactionByStateNoFraud().get("CA"));
                assertEquals(1, tMap.getSyncTransactionByStateNoFraud().get("TX"));
        }

        @Test
        public void over100AndAfter8PMByZipCode(){
                // over $100 and after 8PM by zipCode or online
                assertEquals(1, tMap.getSyncOver100AndAfter8pm().size());
                assertEquals(1, tMap.getSyncOver100AndAfter8pm().get("12345.0"));
        }

        @Test
        public void deposits(){
                // setup
                Transaction t = new Transaction();
                t.setUser(4L);
                t.setAmount("$-21.50");
                t.setErrors("");
                userProcessor.process(t);

                // deposits
                assertEquals(1, userMap.getSyncDeposits().size());
                assertTrue(userMap.getSyncDeposits().containsKey(4L));
        }
}
