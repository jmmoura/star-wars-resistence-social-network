package com.josiel.starwars.exception;

public class AmountItemsPointsDoesNotMatchException extends RuntimeException {
    public AmountItemsPointsDoesNotMatchException(){
        super("Amount items points does not match!");
    }
}
