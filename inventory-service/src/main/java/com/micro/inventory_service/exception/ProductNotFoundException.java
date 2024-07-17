package com.micro.inventory_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Could not find product '" + id + "'.");
    }

    public ProductNotFoundException(String name) {
        super("Could not find product '" + name + "'.");
    }
}
