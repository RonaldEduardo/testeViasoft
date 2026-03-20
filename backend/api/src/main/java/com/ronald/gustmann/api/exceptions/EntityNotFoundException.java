package com.ronald.gustmann.api.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> entityClass, Object id) {
        super(entityClass.getSimpleName() + " com ID [" + id + "] não foi encontrado");
    }

    public EntityNotFoundException(Class<?> entityClass) {
        super(entityClass.getSimpleName() + " não foi encontrado");
    }
}
