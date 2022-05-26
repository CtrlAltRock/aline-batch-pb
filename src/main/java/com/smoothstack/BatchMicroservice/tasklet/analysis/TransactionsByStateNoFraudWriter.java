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
import java.util.List;
import java.util.stream.Collectors;

public class TransactionsByStateNoFraudWriter implements Tasklet {
    private final TransactionMap tMap = TransactionMap.getInstance();

    private final String path;

    public TransactionsByStateNoFraudWriter(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path + "TransactionsByStateNoFraud.xml", "TransactionsByStateNoFraud");
        FileWriter fw = new FileWriter(path + "TransactionsByStateNoFraud.xml", true);
        XStream xs = new XStream();
        xs.alias("TransactionsByState", TransactionsByZip.class);
        xs.aliasField("state", TransactionsByZip.class, "zipcode");
        StringBuilder sb = new StringBuilder();
        List<TransactionsByZip> collect = tMap.getSyncTransactionByStateNoFraud().entrySet()
                .stream()
                .map(n -> new TransactionsByZip(n.getKey(), n.getValue()))
                .collect(Collectors.toList());
        collect.forEach(z -> {
            sb.append(xs.toXML(z));
        });
        fw.append(sb);
        fw.close();
        fg.xmlCloser(path + "TransactionsByStateNoFraud.xml", "TransactionsByStateNoFraud");
        return null;
    }
}
