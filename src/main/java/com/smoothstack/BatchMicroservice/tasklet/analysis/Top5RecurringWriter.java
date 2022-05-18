package com.smoothstack.BatchMicroservice.tasklet.analysis;

import com.smoothstack.BatchMicroservice.generator.FileGenerator;
import com.smoothstack.BatchMicroservice.maps.MerchantMap;
import com.smoothstack.BatchMicroservice.model.Transaction;
import com.smoothstack.BatchMicroservice.model.analysis.Top5RecurringTransaction;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Top5RecurringWriter implements Tasklet {
    private final String path;

    public Top5RecurringWriter(String path) {
        this.path = path;
    }

    private final MerchantMap merchantMap = MerchantMap.getInstance();


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path+"Merchant-Top5RecurringTransactions.xml", "Top5RecurringTransaction");
        XStream xs = new XStream();
        StringBuilder sb = new StringBuilder();
        xs.alias("Merchant", Top5RecurringTransaction.class);
        xs.alias("Transaction", Transaction.class);
        xs.omitField(Top5RecurringTransaction.class, "amount");
        xs.omitField(Top5RecurringTransaction.class, "userId");
        xs.omitField(Top5RecurringTransaction.class, "cardIndex");
        FileWriter fw = new FileWriter(path+"Merchant-Top5RecurringTransactions.xml", true);
        merchantMap.getRecurringTransaction().forEach((k,v)->{
            List<Map.Entry<String, Integer>> collect = v.entrySet()
                    .stream()
                    .filter(n -> n.getValue() > 1)
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(5)
                    .collect(Collectors.toList());
            if(!collect.isEmpty()){
                Top5RecurringTransaction t5 = new Top5RecurringTransaction();
                HashMap<Transaction, Integer> t5re =  new HashMap<>();
                collect.forEach(z -> {
                    String[] s = z.getKey().split(" ");
                    Transaction t = new Transaction();
                    t.setAmount(s[1]);
                    t.setUser(Long.valueOf(s[3]));
                    t.setCard(Long.valueOf(s[5]));
                    t5re.put(t, z.getValue());
                });
                t5.setMerchantId(k);
                t5.setOccurrences(t5re);
                sb.append(xs.toXML(t5));
            }
        });
        fw.append(sb);
        fw.close();
        fg.xmlCloser(path+"Merchant-Top5RecurringTransactions.xml", "Top5RecurringTransaction");
        return null;
    }
}
