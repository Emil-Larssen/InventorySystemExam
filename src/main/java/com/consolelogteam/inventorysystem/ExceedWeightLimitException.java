package com.consolelogteam.inventorysystem;

public class ExceedWeightLimitException extends RuntimeException {
    public ExceedWeightLimitException(String message) {
        super(message);
    }
}
