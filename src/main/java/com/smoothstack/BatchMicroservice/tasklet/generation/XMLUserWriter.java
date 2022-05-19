package com.smoothstack.BatchMicroservice.tasklet.generation;

import com.smoothstack.BatchMicroservice.generator.FileGenerator;
import com.smoothstack.BatchMicroservice.maps.UserMap;
import com.smoothstack.BatchMicroservice.model.generation.User;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

public class XMLUserWriter implements Tasklet {

    private final String path;

    private final UserMap userMap = UserMap.getInstance();

    public XMLUserWriter(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path+"GeneratedUsers.xml", "users");
        StringBuilder userSb = new StringBuilder();
        XStream userXStream = new XStream();
        userXStream.alias("user", User.class);
        FileWriter userFw = new FileWriter(path+"GeneratedUsers.xml", true);
        userMap.getGeneratedUsers().forEach((k, v) -> {
            userSb.append(userXStream.toXML(v));
        });
        userFw.append(userSb);
        userFw.close();
        fg.xmlCloser(path+"GeneratedUsers.xml", "users");
        return null;
    }
}
