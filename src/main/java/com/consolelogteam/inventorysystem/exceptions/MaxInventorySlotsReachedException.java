package com.consolelogteam.inventorysystem.exceptions;

public class MaxInventorySlotsReachedException extends RuntimeException {
    public MaxInventorySlotsReachedException(String message) {
        super(message);
    }
}
