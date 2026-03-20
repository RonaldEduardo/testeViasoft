package com.ronald.gustmann.api.exceptions;

public class InsufficientCreditLimitException extends RuntimeException {
    public InsufficientCreditLimitException() {
        super("Limite de credito insuficiente");
    }

    public InsufficientCreditLimitException(String message) {
        super(message);
    }
}
