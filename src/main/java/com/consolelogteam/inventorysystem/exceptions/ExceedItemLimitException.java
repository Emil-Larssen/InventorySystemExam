package com.consolelogteam.inventorysystem.exceptions;

public class ExceedItemLimitException extends RuntimeException {
    public ExceedItemLimitException(String message) {
        super(message);
    }
}
