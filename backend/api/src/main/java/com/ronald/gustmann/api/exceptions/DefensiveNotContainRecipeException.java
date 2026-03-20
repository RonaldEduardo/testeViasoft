package com.ronald.gustmann.api.exceptions;

public class DefensiveNotContainRecipeException extends RuntimeException {
    public DefensiveNotContainRecipeException() {
        super("O defensivo precisa conter uma receita");
    }

    public DefensiveNotContainRecipeException(String message) {
        super(message);
    }
}
