package com.josiel.starwars.exception;

public class BetrayerInventoryException extends RuntimeException {
    public BetrayerInventoryException() {
        super("The rebel is a betrayer and cannot manipulate his/her inventory!");
    }
}
