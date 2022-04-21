package com.smoothstack.BatchMicroservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Entity
public class Card {

//    @Id
    private Long id;

    private String number;

//    @ManyToOne
    private User user;

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", userId=" + user.getId() +
                '}';
    }
}
