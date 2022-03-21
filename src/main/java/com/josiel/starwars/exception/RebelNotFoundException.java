package com.josiel.starwars.exception;

public class RebelNotFoundException extends RuntimeException {
    public RebelNotFoundException() {
        super("Rebel not found!");
    }
}
