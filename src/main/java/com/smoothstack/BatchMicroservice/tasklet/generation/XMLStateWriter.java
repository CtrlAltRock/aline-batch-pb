package com.smoothstack.BatchMicroservice.tasklet.generation;

import com.smoothstack.BatchMicroservice.generator.FileGenerator;
import com.smoothstack.BatchMicroservice.maps.StateMap;
import com.smoothstack.BatchMicroservice.model.generation.State;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

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
        StringBuilder stringBuilder = new StringBuilder();
        XStream stateXStream = new XStream();
        stateXStream.alias("state", State.class);
        FileWriter stateFw = new FileWriter(path+"GeneratedStates.xml", true);
        stateMap.getGeneratedStates().forEach((k, v) -> {
            stringBuilder.append(stateXStream.toXML(v));
        });
        stateFw.append(stringBuilder);
        stateFw.close();
        fg.xmlCloser(path+"GeneratedStates.xml", "states");
        return null;
    }
}
