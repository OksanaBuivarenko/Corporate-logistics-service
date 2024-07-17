package com.micro.inventory_service.exception;

public class NotEnoughProductException extends RuntimeException {

    public NotEnoughProductException(String message) {
        super("Not enough products! " + message);
    }

}
