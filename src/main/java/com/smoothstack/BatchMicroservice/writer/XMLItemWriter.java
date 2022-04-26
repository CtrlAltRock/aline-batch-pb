package com.smoothstack.BatchMicroservice.writer;

import com.smoothstack.BatchMicroservice.cache.*;
import com.smoothstack.BatchMicroservice.model.*;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import java.io.FileOutputStream;
import java.util.List;

public class XMLItemWriter extends AbstractItemStreamItemWriter {
    private final UserCache userCache = UserCache.getInstance();
    private final CardCache cardCache = CardCache.getInstance();
    private final MerchantCache merchantCache = MerchantCache.getInstance();
    private final LocationCache locationCache = LocationCache.getInstance();
    private final StateCache stateCache = StateCache.getInstance();

    @Override
    public void write(List items) throws Exception {
//      USER WRITER
        XStream userXStream = new XStream();
        userXStream.alias("user", User.class);
        FileOutputStream userFs = new FileOutputStream("C:\\Projects\\Smoothstack\\Assignments\\Sprints\\AlineFinancial\\aline-batch-microservice\\src\\main\\ProcessedOutFiles\\GeneratedUsers.xml", true);
        userCache.getGeneratedUsers().forEach((k, v) -> {
            if (!userCache.getSeenUsers().contains(k)) {
                synchronized (UserCache.class) {
                    if (!userCache.getSeenUsers().contains(k)) {
                        userXStream.toXML(v, userFs);
                        userCache.setSeenUser(k);
                        System.out.println(v);
                    }
                }
            }
        });
//      CARD WRITER
        XStream cardXStream = new XStream();
        cardXStream.alias("card", Card.class);
        FileOutputStream cardFs = new FileOutputStream("C:\\Projects\\Smoothstack\\Assignments\\Sprints\\AlineFinancial\\aline-batch-microservice\\src\\main\\ProcessedOutFiles\\GeneratedCards.xml", true);
        cardCache.getGeneratedCards().forEach((k, v) -> {
            for (Card card : v) {
                if (!cardCache.getSeenCards().contains(card.getId())) {
                    synchronized (CardCache.class) {
                        if (!cardCache.getSeenCards().contains(card.getId())) {
                            cardXStream.toXML(card, cardFs);
                            cardCache.setSeenCards(card.getId());
                            System.out.println(card);
                        }
                    }
                }
            }
        });
//      MERCHANT WRITER
        XStream merchantXStream = new XStream();
        merchantXStream.alias("merchant", Merchant.class);
        FileOutputStream merchantFs = new FileOutputStream("C:\\Projects\\Smoothstack\\Assignments\\Sprints\\AlineFinancial\\aline-batch-microservice\\src\\main\\ProcessedOutFiles\\GeneratedMerchants.xml", true);
        merchantCache.getGeneratedMerchants().forEach((k, v) -> {
            if (!merchantCache.getSeenMerchants().contains(k)) {
                synchronized (MerchantCache.class) {
                    if (!merchantCache.getSeenMerchants().contains(k)) {
                        merchantXStream.toXML(v, merchantFs);
                        merchantCache.setSeenMerchant(k);
                        System.out.println(v);
                    }
                }
            }
        });
//      LOCATION WRITER
        XStream locationXStream = new XStream();
        locationXStream.alias("location", Location.class);
        FileOutputStream locationFs = new FileOutputStream("C:\\Projects\\Smoothstack\\Assignments\\Sprints\\AlineFinancial\\aline-batch-microservice\\src\\main\\ProcessedOutFiles\\GeneratedLocations.xml", true);
        locationCache.getGeneratedLocations().forEach((k, v) ->{
            if(!locationCache.getSeenLocations().contains(k)){
                synchronized (LocationCache.class) {
                    if(!locationCache.getSeenLocations().contains(k)){
                        locationXStream.toXML(v, locationFs);
                        locationCache.setSeenLocations(k);
                        System.out.println(v);
                    }
                }
            }
        });
//      STATE WRITER
        XStream stateXStream = new XStream();
        stateXStream.alias("state", State.class);
        FileOutputStream stateFs = new FileOutputStream("C:\\Projects\\Smoothstack\\Assignments\\Sprints\\AlineFinancial\\aline-batch-microservice\\src\\main\\ProcessedOutFiles\\GeneratedStates.xml", true);
        stateCache.getGeneratedStates().forEach((k,v) -> {
            if(!stateCache.getSeenStates().contains(k)){
                synchronized (StateCache.class){
                    if(!stateCache.getSeenStates().contains(k)){
                        stateXStream.toXML(v, stateFs);
                        stateCache.setSeenState(k);
                        System.out.println(v);
                    }
                }
            }
        });
    }
}
