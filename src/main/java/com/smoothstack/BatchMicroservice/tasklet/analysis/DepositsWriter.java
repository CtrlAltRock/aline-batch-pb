package com.smoothstack.BatchMicroservice.tasklet.analysis;

import com.smoothstack.BatchMicroservice.generator.FileGenerator;
import com.smoothstack.BatchMicroservice.maps.UserMap;
import com.smoothstack.BatchMicroservice.model.Transaction;
import com.smoothstack.BatchMicroservice.model.analysis.DepositByUser;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;
import java.util.List;
import java.util.stream.Collectors;

public class DepositsWriter implements Tasklet {
    private final UserMap userMap = UserMap.getInstance();
    private final String path;

    public DepositsWriter(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path + "User-Deposits.xml", "DepositsByUser");
        XStream xs = new XStream();
        xs.alias("deposit", Transaction.class);
        xs.alias("user-deposits", DepositByUser.class);
        StringBuilder sb = new StringBuilder();
        FileWriter fw = new FileWriter(path + "User-Deposits.xml",true);
        List<String> collect = userMap.getSyncDeposits().entrySet().stream()
                .map(k -> xs.toXML(new DepositByUser(k.getKey(), k.getValue())))
                .collect(Collectors.toList());
        collect.forEach(sb::append);
        fw.append(sb);
        fw.close();
        fg.xmlCloser(path + "User-Deposits.xml", "DepositsByUser");
        return null;
    }

}
