package com.ronald.gustmann.api.dto.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class RestExceptionMessage {
    private HttpStatus httpStatus;
    private String message;
}
