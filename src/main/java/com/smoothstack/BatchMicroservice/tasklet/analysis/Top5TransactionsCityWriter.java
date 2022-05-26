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

public class Top5TransactionsCityWriter implements Tasklet {

    TransactionMap tMap = TransactionMap.getInstance();

    private final String path;

    public Top5TransactionsCityWriter(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path+"Top5TransactionsByCity.xml", "Top5TransactionsByCity");
        FileWriter fw = new FileWriter(path+"Top5TransactionsByCity.xml", true);
        XStream xs = new XStream();
        xs.alias("TransactionsByCity", TransactionsByZip.class);
        xs.aliasField("city", TransactionsByZip.class, "zipcode");
        StringBuilder sb = new StringBuilder();
        List<TransactionsByZip> collect = tMap.getSyncCityTransaction().entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(n -> new TransactionsByZip(n.getKey(), n.getValue()))
                .collect(Collectors.toList());
        collect.forEach(z -> sb.append(xs.toXML(z)));
        fw.append(sb);
        fw.close();
        fg.xmlCloser(path+"Top5TransactionsByCity.xml", "Top5TransactionsByCity");
        return null;
    }
}
