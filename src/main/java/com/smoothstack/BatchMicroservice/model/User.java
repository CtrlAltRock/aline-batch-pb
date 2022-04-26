package com.smoothstack.BatchMicroservice.model;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@XmlRootElement(name="users")
@XmlAccessorType(XmlAccessType.FIELD)
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
