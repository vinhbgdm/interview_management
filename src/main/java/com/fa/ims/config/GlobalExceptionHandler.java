package com.fa.ims.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, EntityNotFoundException.class})
    public String notFound(Exception e) {
        e.printStackTrace();
        return "/error/404";
    }
    @ExceptionHandler({Exception.class})
    public String defaultError(Exception e) {
        e.printStackTrace();
        return "/error/500";
    }
}
