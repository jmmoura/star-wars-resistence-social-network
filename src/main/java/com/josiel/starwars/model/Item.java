package com.josiel.starwars.model;

public enum Item {
    ARMA(4),
    MUNICAO(3),
    AGUA(2),
    COMIDA(1);

    private int points;

    Item(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
