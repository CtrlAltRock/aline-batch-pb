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

public class Bottom5OnlineTransactionsByMonth implements Tasklet {

    TransactionMap tMap = TransactionMap.getInstance();

    private final String path;

    public Bottom5OnlineTransactionsByMonth(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path + "Bottom5OnlineTransactionsByMonth.xml", "Bottom5OnlineTransactionsByMonth");
        FileWriter fw = new FileWriter(path + "Bottom5OnlineTransactionsByMonth.xml", true);
        XStream xs = new XStream();
        xs.alias("OnlineTransactionByMonth", TransactionsByZip.class);
        xs.aliasField("month", TransactionsByZip.class, "zipcode");
        StringBuilder sb = new StringBuilder();
        List<TransactionsByZip> collect = tMap.getSyncOnlineByMonth().entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                .limit(5)
                .map(n -> new TransactionsByZip(String.valueOf(n.getKey()), n.getValue()))
                .collect(Collectors.toList());
        collect.forEach(z -> sb.append(xs.toXML(z)));
        fw.append(sb);
        fw.close();
        fg.xmlCloser(path + "Bottom5OnlineTransactionsByMonth.xml", "Bottom5OnlineTransactionsByMonth");
        return null;
    }
}
