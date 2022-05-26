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

public class Over100AndAfter8PMByZipCode implements Tasklet {

    TransactionMap tMap = TransactionMap.getInstance();

    private final String path;

    public Over100AndAfter8PMByZipCode(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path+"Over100AndAfter8PMByZipCode.xml", "Over100AndAfter8PMByZipCode");
        FileWriter fw = new FileWriter(path+"Over100AndAfter8PMByZipCode.xml", true);
        XStream xs = new XStream();
        xs.alias("TransactionsByZipCode", TransactionsByZip.class);
        StringBuilder sb = new StringBuilder();
        List<TransactionsByZip> collect = tMap.getSyncOver100AndAfter8pm().entrySet().stream()
                .map(n -> new TransactionsByZip(n.getKey(), n.getValue()))
                .collect(Collectors.toList());
        collect.forEach(z -> {
            sb.append(xs.toXML(z));
        });
        fw.append(sb);
        fw.close();
        fg.xmlCloser(path+"Over100AndAfter8PMByZipCode.xml", "Over100AndAfter8PMByZipCode");
        return null;
    }
}
