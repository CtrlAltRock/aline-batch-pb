package com.smoothstack.BatchMicroservice.writer;

import com.smoothstack.BatchMicroservice.cache.CardCache;
import com.smoothstack.BatchMicroservice.cache.UserCache;
import com.smoothstack.BatchMicroservice.model.Card;
import com.smoothstack.BatchMicroservice.model.User;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class XMLItemWriter extends AbstractItemStreamItemWriter {

    private final UserCache userCache = UserCache.getInstance();
    private final CardCache cardCache = CardCache.getInstance();

    @Override
    public void write(List items) throws FileNotFoundException {
        XStream userXStream = new XStream();
        userXStream.alias("user", User.class);
        FileOutputStream userFs = new FileOutputStream("C:\\Projects\\Smoothstack\\Assignments\\Sprints\\AlineFinancial\\aline-batch-microservice\\src\\main\\ProcessedOutFiles\\GeneratedUsers.xml", true);

        userCache.getGeneratedUsers().forEach((k, v) -> {
            if (!userCache.getSeenUsers().contains(k)) {
                synchronized (UserCache.class) {
                    if (!userCache.getSeenUsers().contains(k)) {
                        System.out.println(v);
                        userXStream.toXML(v, userFs);
                        userCache.setSeenUser(k);
                    }
                }
            }
        });


        XStream cardXStream = new XStream();
        userXStream.alias("card", Card.class);
        FileOutputStream cardFs = new FileOutputStream("C:\\Projects\\Smoothstack\\Assignments\\Sprints\\AlineFinancial\\aline-batch-microservice\\src\\main\\ProcessedOutFiles\\GeneratedCards.xml", true);

        cardCache.getGeneratedCards().forEach((k, v) -> {
            for (Card card : v) {
                if (!cardCache.getSeenCards().contains(card.getId())) {
                    synchronized (CardCache.class) {
                        if (!cardCache.getSeenCards().contains(card.getId())) {
                            cardXStream.toXML(card, cardFs);
                            cardCache.setSeenCards(card.getId());
                        }
                    }
                }
            }
        });
    }

}
