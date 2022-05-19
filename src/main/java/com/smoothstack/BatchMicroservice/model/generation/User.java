package com.smoothstack.BatchMicroservice.model.generation;

import com.smoothstack.BatchMicroservice.model.Transaction;
import com.smoothstack.BatchMicroservice.model.generation.Card;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<Card> cards;
    private List<Transaction> transactions;
    public void setCard(Card card){
        cards.add(card);
    }
}
