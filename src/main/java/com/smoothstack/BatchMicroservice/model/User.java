package com.smoothstack.BatchMicroservice.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//@Entity
public class User {
//    @Id
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

//    @OneToMany(mappedBy = "user")
    private List<Card> cards;

//    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;
}
