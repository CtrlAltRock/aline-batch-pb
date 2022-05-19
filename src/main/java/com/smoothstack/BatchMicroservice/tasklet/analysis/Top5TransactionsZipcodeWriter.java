package com.smoothstack.BatchMicroservice.tasklet.analysis;

import com.smoothstack.BatchMicroservice.generator.FileGenerator;
import com.smoothstack.BatchMicroservice.maps.TransactionMap;
import com.smoothstack.BatchMicroservice.model.analysis.TransactionsByZip;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Top5TransactionsZipcodeWriter implements Tasklet {

    TransactionMap tMap = TransactionMap.getInstance();

    private final String path;

    public Top5TransactionsZipcodeWriter(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path+"Top5TransactionsByZipCode.xml", "Top5TransactionsByZipCode");
        FileWriter fw = new FileWriter(path+"Top5TransactionsByZipCode.xml", true);
        XStream xs = new XStream();
        xs.alias("TransactionsByZipCode", TransactionsByZip.class);
        StringBuilder sb = new StringBuilder();
        List<TransactionsByZip> collect = tMap.getSyncZipCodeTransaction().entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(n -> new TransactionsByZip(n.getKey(), n.getValue()))
                .collect(Collectors.toList());
        collect.forEach(z -> {
            sb.append(xs.toXML(z));
        });
        fw.append(sb);
        fw.close();
        fg.xmlCloser(path+"Top5TransactionsByZipCode.xml", "Top5TransactionsByZipCode");
        return null;
    }
}
