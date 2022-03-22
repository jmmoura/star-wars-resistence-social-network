package com.josiel.starwars.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class ItemSet {
    @Id @GeneratedValue
    private Integer id;
    private Item item;
    private int amount;
}
