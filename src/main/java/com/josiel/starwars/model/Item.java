package com.josiel.starwars.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class Item {
    @Id @GeneratedValue
    private Integer id;
    private ItemDescription description;
    private int amount;
}
