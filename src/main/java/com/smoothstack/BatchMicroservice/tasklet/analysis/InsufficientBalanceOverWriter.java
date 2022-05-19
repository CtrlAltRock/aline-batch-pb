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

public class InsufficientBalanceOverWriter implements Tasklet {
    private final UserMap userMap = UserMap.getInstance();
    private final String path;

    public InsufficientBalanceOverWriter(String path) {
        this.path = path;
    }

    private Integer overOneInsufficient = 0;
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        userMap.getInsufficientBalanceByUser().forEach((k,v) -> {
            if(v>1) overOneInsufficient += 1;
        });
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path+"User-PercentInsufficientBalanceOverOnce.xml", "InsufficientBalance");
        XStream xs = new XStream();
        xs.alias("InsufficientOverOnce", InsufficientBalance.class);
        FileOutputStream fw = new FileOutputStream(path+"User-PercentInsufficientBalanceOverOnce.xml", true);
        InsufficientBalance ib = new InsufficientBalance(userMap.getGeneratedUsers().size(), overOneInsufficient);
        xs.toXML(ib, fw);
        fg.xmlCloser(path+"User-PercentInsufficientBalanceOverOnce.xml", "InsufficientBalance");
        return null;
    }
}
