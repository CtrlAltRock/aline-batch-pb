package com.smoothstack.BatchMicroservice.tasklet;

import com.smoothstack.BatchMicroservice.generator.FileGenerator;
import com.smoothstack.BatchMicroservice.maps.LocationMap;
import com.smoothstack.BatchMicroservice.model.Location;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileOutputStream;

public class XMLLocationWriter implements Tasklet {
    private final LocationMap locationMap = LocationMap.getInstance();
    private final String path;

    public XMLLocationWriter(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path+"GeneratedLocations.xml", "locations");
        XStream locationXStream = new XStream();
        locationXStream.alias("location", Location.class);
        FileOutputStream locationFs = new FileOutputStream(path+"GeneratedLocations.xml", true);
        locationMap.getGeneratedLocations().forEach((k, v) -> {
            locationXStream.toXML(v, locationFs);
        });
        fg.xmlCloser(path+"GeneratedLocations.xml", "locations");
        return null;
    }
}
