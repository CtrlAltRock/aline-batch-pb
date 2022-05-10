package com.smoothstack.BatchMicroservice.tasklet;

import com.smoothstack.BatchMicroservice.generator.FileGenerator;
import com.smoothstack.BatchMicroservice.maps.StateMap;
import com.smoothstack.BatchMicroservice.model.State;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileOutputStream;

public class XMLStateWriter implements Tasklet {
    private final StateMap stateMap = StateMap.getInstance();
    private final String path;

    public XMLStateWriter(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path+"GeneratedStates.xml", "states");
        XStream stateXStream = new XStream();
        stateXStream.alias("state", State.class);
        FileOutputStream stateFs = new FileOutputStream(path+"GeneratedStates.xml", true);
        stateMap.getGeneratedStates().forEach((k, v) -> {
            stateXStream.toXML(v, stateFs);
        });
        fg.xmlCloser(path+"GeneratedStates.xml", "states");
        System.out.println("Completed :)");
        return null;
    }
}
