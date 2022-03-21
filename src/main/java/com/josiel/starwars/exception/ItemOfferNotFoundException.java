package com.josiel.starwars.exception;

public class ItemOfferNotFoundException extends RuntimeException {
    public ItemOfferNotFoundException() {
        super("Item offer not found!");
    }
}
