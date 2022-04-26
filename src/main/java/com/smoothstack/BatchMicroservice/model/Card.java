package com.smoothstack.BatchMicroservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Card {
    private Long id;
    private String number;
    private Long userId;

}
