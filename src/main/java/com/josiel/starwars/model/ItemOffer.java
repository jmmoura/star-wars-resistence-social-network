package com.josiel.starwars.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class ItemOffer {
    @Id @GeneratedValue
    private Integer id;
    private Integer proposerId;
    private Integer receiverId;
    private ItemDescription itemDescription;
    private int amount;
}
