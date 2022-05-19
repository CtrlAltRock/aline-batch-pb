package com.smoothstack.BatchMicroservice.tasklet.generation;

import com.smoothstack.BatchMicroservice.generator.FileGenerator;
import com.smoothstack.BatchMicroservice.maps.MerchantMap;
import com.smoothstack.BatchMicroservice.model.generation.Merchant;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

public class XMLMerchantWriter implements Tasklet {
    private final MerchantMap merchantMap = MerchantMap.getInstance();

    private final String path;

    public XMLMerchantWriter(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path+"GeneratedMerchants.xml", "merchants");
        StringBuilder merchantSb = new StringBuilder();
        XStream merchantXStream = new XStream();
        merchantXStream.alias("merchant", Merchant.class);
        FileWriter merchantFs = new FileWriter(path+"GeneratedMerchants.xml", true);
        merchantMap.getGeneratedMerchants().forEach((k, v) -> {
            merchantSb.append(merchantXStream.toXML(v));
        });
        merchantFs.append(merchantSb);
        merchantFs.close();
        fg.xmlCloser(path+"GeneratedMerchants.xml", "merchants");
        return null;
    }
}
