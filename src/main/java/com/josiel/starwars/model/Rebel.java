package com.josiel.starwars.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class Rebel {
    @Id @GeneratedValue
    private Integer id;
    private String name;
    private int age;
    private char genre;
    @OneToOne
    private Position position;
    @OneToMany
    private List<Item> inventory;
    private int betrayerReportsCount;
}