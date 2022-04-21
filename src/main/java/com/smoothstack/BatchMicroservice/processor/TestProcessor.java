package com.smoothstack.BatchMicroservice.processor;

import com.smoothstack.BatchMicroservice.cache.UserCache;
import com.smoothstack.BatchMicroservice.generator.CardGenerator;
import com.smoothstack.BatchMicroservice.model.Card;
import com.smoothstack.BatchMicroservice.model.Transaction;
import com.smoothstack.BatchMicroservice.model.User;
import org.springframework.batch.item.ItemProcessor;


public class TestProcessor implements ItemProcessor<Transaction, User> {

    private static UserCache userCache = new UserCache();

    private final CardGenerator cardGenerator = CardGenerator.getInstance();

    @Override
    public User process(Transaction item) throws Exception {
        User user = userCache.findUserOrGenerate(item.getUser());
//        System.out.print(userOrGenerate.writeToXML());
        if(item.getCard()+1>=user.getCards().size()) {
            for(int i = user.getCards().size(); i<=item.getCard()+1; i++) {
                if (user.getCards().isEmpty() || item.getCard().intValue()+1 > user.getCards().size()){
                    synchronized (CardGenerator.class) {
                        if (user.getCards().isEmpty() || item.getCard().intValue()+1 > user.getCards().size()){
                            Card card = cardGenerator.makeCard(user);
                            user.setCard(card);
                        }
                    }
                }
            }
        }
        return user;
    }
}
