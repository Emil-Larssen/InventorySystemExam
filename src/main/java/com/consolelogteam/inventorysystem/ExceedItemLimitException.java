package com.consolelogteam.inventorysystem;

public class ExceedItemLimitException extends RuntimeException {
    public ExceedItemLimitException(String message) {
        super(message);
    }
}
