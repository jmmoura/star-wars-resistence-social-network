package com.josiel.starwars.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rebel {
    @Id @GeneratedValue
    private Integer id;
    private String name;
    private int age;
    private char genre;
    @OneToOne(cascade=CascadeType.ALL)
    private Position position;
    @OneToMany(cascade=CascadeType.ALL)
    private List<ItemSet> inventory;
    private int betrayerReportsCount;
}