package com.smoothstack.BatchMicroservice.tasklet;

import com.smoothstack.BatchMicroservice.generator.FileGenerator;
import com.smoothstack.BatchMicroservice.maps.TransactionMap;
import com.smoothstack.BatchMicroservice.model.analysis.FraudByYear;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Map;

public class XmlWriterTasklet implements Tasklet {



    private final TransactionMap tMap = TransactionMap.getInstance();

    private final String path;

    public XmlWriterTasklet(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println(path);
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path + "Year-FraudPercent.xml", "FraudPercentByYear");
        FileWriter fw = new FileWriter(path + "Year-FraudPercent.xml", true);
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        XStream xs = new XStream();
        xs.alias("entry", FraudByYear.class);
        StringBuilder sb = new StringBuilder();
        Map<Integer, Integer> syncFraudByYear = tMap.getSyncFraudByYear();
        tMap.getSyncTransactionByYear().forEach((k,v) -> {
            if(!syncFraudByYear.containsKey(k)){
                FraudByYear fbyZero = new FraudByYear(k, 0f);
                sb.append(xs.toXML(fbyZero));
            } else {
                FraudByYear fby = new FraudByYear();
                fby.setYear(k);
                fby.setPercent(Float.valueOf(decimalFormat.format((float)syncFraudByYear.get(k) / (float)v * 100f)));
                sb.append(xs.toXML(fby));;
            }
        });
        fw.append(sb);
        fw.close();
        fg.xmlCloser(path + "Year-FraudPercent.xml", "FraudPercentByYear");
        return null;
    }

}
