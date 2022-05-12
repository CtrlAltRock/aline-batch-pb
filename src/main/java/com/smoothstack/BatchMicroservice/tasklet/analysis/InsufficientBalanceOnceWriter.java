package com.smoothstack.BatchMicroservice.tasklet.analysis;

import com.smoothstack.BatchMicroservice.generator.FileGenerator;
import com.smoothstack.BatchMicroservice.maps.UserMap;
import com.smoothstack.BatchMicroservice.model.analysis.InsufficientBalance;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileOutputStream;

public class InsufficientBalanceOnceWriter implements Tasklet {
    private final UserMap userMap = UserMap.getInstance();
    private final String path;

    public InsufficientBalanceOnceWriter(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path+"User-PercentInsufficientBalanceOnce.xml", "InsufficientBalance");
        XStream xs = new XStream();
        xs.alias("InsufficientOnce", InsufficientBalance.class);
        FileOutputStream fw = new FileOutputStream(path+"User-PercentInsufficientBalanceOnce.xml", true);
        InsufficientBalance ib = new InsufficientBalance(userMap.getGeneratedUsers().size(), userMap.getInsufficientBalanceByUser().size());
        xs.toXML(ib, fw);
        fw.close();
        fg.xmlCloser(path+"User-PercentInsufficientBalanceOnce.xml", "InsufficientBalance");
        return null;
    }
}
