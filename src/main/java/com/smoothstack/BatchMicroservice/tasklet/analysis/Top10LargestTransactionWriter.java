package com.smoothstack.BatchMicroservice.tasklet.analysis;

import com.smoothstack.BatchMicroservice.generator.FileGenerator;
import com.smoothstack.BatchMicroservice.maps.TransactionMap;
import com.smoothstack.BatchMicroservice.model.Transaction;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

public class Top10LargestTransactionWriter implements Tasklet {

    private final TransactionMap tMap = TransactionMap.getInstance();
    private final String path;

    public Top10LargestTransactionWriter(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path+"Top10LargestTransactions.xml", "Top10LargestTransactions");
        FileWriter fw = new FileWriter(path+"Top10LargestTransactions.xml", true);
        XStream xs = new XStream();
        xs.alias("Transaction", Transaction.class);
        StringBuilder sb = new StringBuilder();
        tMap.getSyncTop10LargestTransaction().forEach(t -> {
            sb.append(xs.toXML(t));
        });
        fw.append(sb);
        fw.close();
        fg.xmlCloser(path+"Top10LargestTransactions.xml", "Top10LargestTransactions");
        return null;
    }
}
