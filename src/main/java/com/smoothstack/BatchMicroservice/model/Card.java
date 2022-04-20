package com.smoothstack.BatchMicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
