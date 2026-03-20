package com.ronald.gustmann.api.infra;

import com.ronald.gustmann.api.dto.exceptions.RestExceptionMessage;
import com.ronald.gustmann.api.exceptions.DefensiveNotContainRecipeException;
import com.ronald.gustmann.api.exceptions.EntityNotFoundException;
import com.ronald.gustmann.api.exceptions.InsufficientCreditLimitException;
import com.ronald.gustmann.api.exceptions.InsufficientProductStockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExcptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<RestExceptionMessage> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new RestExceptionMessage(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler({
            InsufficientCreditLimitException.class,
            InsufficientProductStockException.class,
            DefensiveNotContainRecipeException.class
    })
    protected ResponseEntity<RestExceptionMessage> handleBusinessRules(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new RestExceptionMessage(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage()));
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    protected ResponseEntity<RestExceptionMessage> handleConflict(ObjectOptimisticLockingFailureException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new RestExceptionMessage(HttpStatus.CONFLICT, "Conflito de concorrência: o registro foi alterado por outro usuário."));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<RestExceptionMessage> handleGeneric(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new RestExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado no servidor."));
    }
}
