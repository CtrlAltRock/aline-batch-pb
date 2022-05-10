package com.smoothstack.BatchMicroservice.tasklet;

import com.smoothstack.BatchMicroservice.maps.MerchantMap;
import com.smoothstack.BatchMicroservice.maps.TransactionMap;
import com.smoothstack.BatchMicroservice.maps.UserMap;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XmlWriterTasklet implements Tasklet {

    private final UserMap userMap = UserMap.getInstance();
    private final MerchantMap merchantMap = MerchantMap.getInstance();
    private final TransactionMap tMap = TransactionMap.getInstance();

    private final String path;

    public XmlWriterTasklet(String path) {
        this.path = path;
    }

    private Integer overOneInsufficient = 0;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println(path);

//      ANALYSIS WRITER

        merchantMap.getRecurringTransaction().forEach((k,v)->{
            List<Map.Entry<Float, Integer>> collect = v.entrySet()
                    .stream()
                    .filter(n-> n.getValue() > 1)
                    .filter(i -> i.getKey() > 0)
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(5)
                    .collect(Collectors.toList());
            if(!collect.isEmpty()) System.out.println("MerchantId => " + k);
            collect.forEach(z -> {
                System.out.println("transaction amount => " + z.getKey());
                System.out.println("occurrence => " + z.getValue());
            });
        });

        userMap.getInsufficientBalanceByUser().forEach((k,v) -> {
            if(v>1) {
                overOneInsufficient += 1;
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        float oncePercent = ((float)userMap.getInsufficientBalanceByUser().size() / (float)userMap.getGeneratedUsers().size());
        float overOncePercent = ((float)overOneInsufficient / (float)userMap.getGeneratedUsers().size());
        System.out.println("Total Unique Merchants => " + merchantMap.getTotalUniqueMerchants());
        System.out.println("% of user with insufficient balance => " + decimalFormat.format(oncePercent * 100f));
        System.out.println("% of user with over once insufficient balance => " + decimalFormat.format(overOncePercent * 100f));

        Map<Integer, Integer> syncFraudByYear = tMap.getSyncFraudByYear();
        tMap.getSyncTransactionByYear().forEach((k,v) -> {
            System.out.print("year => " + k);
            if(!syncFraudByYear.containsKey(k)){
                System.out.print(": % fraud => 0%");
            } else {
                System.out.print(": % fraud => " + decimalFormat.format((float)syncFraudByYear.get(k) / (float)v * 100f) + "%");
            }
            System.out.println();
        });


        return null;
    }

}
