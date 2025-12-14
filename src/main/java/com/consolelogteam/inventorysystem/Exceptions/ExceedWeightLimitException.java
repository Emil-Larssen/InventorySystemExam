package com.consolelogteam.inventorysystem.Exceptions;

public class ExceedWeightLimitException extends RuntimeException {
    public ExceedWeightLimitException(String message) {
        super(message);
    }
}
