package com.josiel.starwars.exception;

public class ItemOfferAmountOutOfBoundsException extends RuntimeException {
    public ItemOfferAmountOutOfBoundsException() {
        super("The rebel does not have enough amount of this item!");
    }
}
