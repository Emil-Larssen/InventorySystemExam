package com.consolelogteam.inventorysystem;

public class MaxInventorySlotsReachedException extends RuntimeException {
    public MaxInventorySlotsReachedException(String message) {
        super(message);
    }
}
