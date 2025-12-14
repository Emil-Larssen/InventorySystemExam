package com.consolelogteam.inventorysystem.Exceptions;

public class MaxInventorySlotsReachedException extends RuntimeException {
    public MaxInventorySlotsReachedException(String message) {
        super(message);
    }
}
