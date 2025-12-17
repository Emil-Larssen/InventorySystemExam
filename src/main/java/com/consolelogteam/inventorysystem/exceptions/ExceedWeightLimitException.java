package com.consolelogteam.inventorysystem.exceptions;

public class ExceedWeightLimitException extends RuntimeException {
    public ExceedWeightLimitException(String message) {
        super(message);
    }
}
