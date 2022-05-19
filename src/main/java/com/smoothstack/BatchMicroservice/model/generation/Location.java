package com.smoothstack.BatchMicroservice.model.generation;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Location {
    private String zipCode;
    private String city;
    private Long stateId;
    private Long uniqueKey;
}
