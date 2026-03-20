package com.ronald.gustmann.api.exceptions;

import com.ronald.gustmann.api.model.Product;

public class InsufficientProductStockException extends RuntimeException {
    public InsufficientProductStockException(Product product) {
        super("Estoque insuficiente para o produto " + product.getId());
    }

    public InsufficientProductStockException(String message) {
        super(message);
    }
}
