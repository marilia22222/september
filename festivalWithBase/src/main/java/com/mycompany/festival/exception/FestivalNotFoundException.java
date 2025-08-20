package com.mycompany.festival.exception;

// Απλά κάν' την να επεκτείνει την RuntimeException. Αυτό είναι όλο!
public class FestivalNotFoundException extends RuntimeException {
    public FestivalNotFoundException(String message) {
        super(message);
    }
}