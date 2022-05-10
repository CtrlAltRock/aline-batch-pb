package com.smoothstack.BatchMicroservice.tasklet;

import com.smoothstack.BatchMicroservice.generator.FileGenerator;
import com.smoothstack.BatchMicroservice.maps.UserMap;
import com.smoothstack.BatchMicroservice.model.User;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileOutputStream;

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
        XStream userXStream = new XStream();
        userXStream.alias("user", User.class);
        FileOutputStream userFs = new FileOutputStream(path+"GeneratedUsers.xml", true);
        userMap.getGeneratedUsers().forEach((k, v) -> {
//            TODO - optimize write to only write once
            userXStream.toXML(v, userFs);
        });
        fg.xmlCloser(path+"GeneratedUsers.xml", "users");
        return null;
    }
}
