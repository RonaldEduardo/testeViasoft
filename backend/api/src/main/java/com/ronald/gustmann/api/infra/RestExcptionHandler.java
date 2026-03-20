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
    protected ResponseEntity<RestExceptionMessage> entityNotFoundHandler(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestExceptionMessage(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(InsufficientCreditLimitException.class)
    protected ResponseEntity<RestExceptionMessage> insufficientCreditLimitHandler(InsufficientCreditLimitException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestExceptionMessage(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(InsufficientProductStockException.class)
    protected ResponseEntity<RestExceptionMessage> insufficientProductStockException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestExceptionMessage(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(DefensiveNotContainRecipeException.class)
    protected ResponseEntity<RestExceptionMessage> defensiveNotContainRecipeException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestExceptionMessage(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    protected ResponseEntity<RestExceptionMessage> optimisticLockExceptionHandler(ObjectOptimisticLockingFailureException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new RestExceptionMessage(HttpStatus.CONFLICT, "Conflito de concorrencia: recurso foi atualizado por outra transacao."));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<RestExceptionMessage> exceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestExceptionMessage(HttpStatus.BAD_REQUEST, e.getMessage()));
    }
}
