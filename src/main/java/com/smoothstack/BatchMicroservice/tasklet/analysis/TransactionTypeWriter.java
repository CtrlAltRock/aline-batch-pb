package com.smoothstack.BatchMicroservice.tasklet.analysis;

import com.smoothstack.BatchMicroservice.generator.FileGenerator;
import com.smoothstack.BatchMicroservice.maps.TransactionMap;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

public class TransactionTypeWriter implements Tasklet {

    private final TransactionMap tMap = TransactionMap.getInstance();
    private final String path;

    public TransactionTypeWriter(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path+ "TransactionType.xml", "TransactionTypes");
        FileWriter fw = new FileWriter(path+ "TransactionType.xml", true);
        XStream xs = new XStream();
        xs.alias("type", String.class);
        StringBuilder sb = new StringBuilder();
        tMap.getTransactionType().forEach(s -> {
            sb.append(xs.toXML(s));
        });
        fw.append(sb);
        fw.close();
        fg.xmlCloser(path+ "TransactionType.xml", "TransactionTypes");
        return null;
    }
}
