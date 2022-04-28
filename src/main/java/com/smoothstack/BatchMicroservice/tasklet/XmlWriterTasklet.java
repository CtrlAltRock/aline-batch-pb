package com.smoothstack.BatchMicroservice.tasklet;

import com.smoothstack.BatchMicroservice.cache.*;
import com.smoothstack.BatchMicroservice.generator.FileGenerator;
import com.smoothstack.BatchMicroservice.model.*;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.FileOutputStream;

public class XmlWriterTasklet implements Tasklet {

    private final UserCache userCache = UserCache.getInstance();
    private final CardCache cardCache = CardCache.getInstance();
    private final MerchantCache merchantCache = MerchantCache.getInstance();
    private final LocationCache locationCache = LocationCache.getInstance();
    private final StateCache stateCache = StateCache.getInstance();

    private final String path;

    public XmlWriterTasklet(String path) {
        this.path = path;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println(path);

        //      USER WRITER
        FileGenerator fg = new FileGenerator();
        fg.xmlHeader(path+"GeneratedUsers.xml", "users");
        XStream userXStream = new XStream();
        userXStream.alias("user", User.class);
        FileOutputStream userFs = new FileOutputStream(path+"GeneratedUsers.xml", true);
        userCache.getGeneratedUsers().forEach((k, v) -> {
            userXStream.toXML(v, userFs);
        });
        fg.xmlCloser(path+"GeneratedUsers.xml", "users");


//      CARD WRITER
        fg.xmlHeader(path+"GeneratedCards.xml", "cards");
        XStream cardXStream = new XStream();
        cardXStream.alias("card", Card.class);
        FileOutputStream cardFs = new FileOutputStream(path+"GeneratedCards.xml", true);
        cardCache.getGeneratedCards().forEach((k, v) -> {
            for (Card card : v) {
                cardXStream.toXML(card, cardFs);
            }
        });
        fg.xmlCloser(path+"GeneratedCards.xml", "cards");

//      MERCHANT WRITER
        fg.xmlHeader(path+"GeneratedMerchants.xml", "merchants");
        XStream merchantXStream = new XStream();
        merchantXStream.alias("merchant", Merchant.class);
        FileOutputStream merchantFs = new FileOutputStream(path+"GeneratedMerchants.xml", true);
        merchantCache.getGeneratedMerchants().forEach((k, v) -> {
            merchantXStream.toXML(v, merchantFs);
        });
        fg.xmlCloser(path+"GeneratedMerchants.xml", "merchants");

//      LOCATION WRITER
        fg.xmlHeader(path+"GeneratedLocations.xml", "locations");
        XStream locationXStream = new XStream();
        locationXStream.alias("location", Location.class);
        FileOutputStream locationFs = new FileOutputStream(path+"GeneratedLocations.xml", true);
        locationCache.getGeneratedLocations().forEach((k, v) -> {
            locationXStream.toXML(v, locationFs);
        });
        fg.xmlCloser(path+"GeneratedLocations.xml", "locations");

//      STATE WRITER
        fg.xmlHeader(path+"GeneratedStates.xml", "states");
        XStream stateXStream = new XStream();
        stateXStream.alias("state", State.class);
        FileOutputStream stateFs = new FileOutputStream(path+"GeneratedStates.xml", true);
        stateCache.getGeneratedStates().forEach((k, v) -> {
            stateXStream.toXML(v, stateFs);
        });
        fg.xmlCloser(path+"GeneratedStates.xml", "states");
        System.out.println("Completed :)");
        return null;
    }
}
