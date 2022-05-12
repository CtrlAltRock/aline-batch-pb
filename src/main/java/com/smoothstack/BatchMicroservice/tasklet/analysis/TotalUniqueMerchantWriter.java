package com.smoothstack.BatchMicroservice.tasklet.analysis;

import com.smoothstack.BatchMicroservice.generator.FileGenerator;
import com.smoothstack.BatchMicroservice.maps.MerchantMap;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

public class TotalUniqueMerchantWriter implements Tasklet {
    private final MerchantMap merchantMap = MerchantMap.getInstance();
    private final String path;

    public TotalUniqueMerchantWriter(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path+"Merchant-TotalUnique.xml", "TotalUniqueMerchants");
        XStream xs = new XStream();
        StringBuilder sb = new StringBuilder();
        FileWriter fw = new FileWriter(path+"Merchant-TotalUnique.xml", true);
        sb.append(xs.toXML(merchantMap.getTotalUniqueMerchants()));
        fw.append(sb);
        fw.close();
        fg.xmlCloser(path+"Merchant-TotalUnique.xml", "TotalUniqueMerchants");
        return null;
    }
}
