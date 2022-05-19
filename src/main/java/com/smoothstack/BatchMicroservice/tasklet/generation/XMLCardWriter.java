package com.smoothstack.BatchMicroservice.tasklet.generation;

import com.smoothstack.BatchMicroservice.generator.FileGenerator;
import com.smoothstack.BatchMicroservice.maps.CardMap;
import com.smoothstack.BatchMicroservice.model.generation.Card;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileWriter;

public class XMLCardWriter implements Tasklet {
    private final CardMap cardMap = CardMap.getInstance();
    private final String path;

    public XMLCardWriter(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path+"GeneratedCards.xml", "cards");
        StringBuilder cardSb = new StringBuilder();
        XStream cardXStream = new XStream();
        cardXStream.alias("card", Card.class);
        FileWriter cardFs = new FileWriter(path+"GeneratedCards.xml", true);
        cardMap.getGeneratedCards().forEach((k, v) -> {
            for (Card card : v) {
                cardSb.append(cardXStream.toXML(card));
            }
        });
        cardFs.append(cardSb);
        cardFs.close();
        fg.xmlCloser(path+"GeneratedCards.xml", "cards");
        return null;
    }
}
