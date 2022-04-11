package com.josiel.starwars.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Position {
    @Id @GeneratedValue
    private Integer id;
    private String name;
    private double latitude;
    private double longitude;
    private boolean isUpdatedByAdmin;
}
