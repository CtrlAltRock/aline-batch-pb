package com.smoothstack.BatchMicroservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    private Long id;

    private String number;

    private Long userId;

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", userId=" + userId +
                '}';
    }
}
