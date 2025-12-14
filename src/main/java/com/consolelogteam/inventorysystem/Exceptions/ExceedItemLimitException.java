package com.consolelogteam.inventorysystem.Exceptions;

public class ExceedItemLimitException extends RuntimeException {
    public ExceedItemLimitException(String message) {
        super(message);
    }
}
