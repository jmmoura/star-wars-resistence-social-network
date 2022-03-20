package com.josiel.starwars.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class ItemOffer {
    @Id @GeneratedValue
    private Integer id;
    @ManyToOne
    private Rebel proposer;
    @ManyToOne
    private Rebel receiver;
    private ItemDescription itemDescription;
    private int amount;
}
