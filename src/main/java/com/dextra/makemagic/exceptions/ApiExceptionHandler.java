package com.dextra.makemagic.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(AllException.class)
    public ResponseEntity<Object> handleException(AllException ex, WebRequest request) {

        Problem problem = new Problem();
        problem.setStatus(ex.getStatus().value());
        problem.setTitle(ex.getMessage());
        problem.setDateTime(LocalDateTime.now());

        return handleExceptionInternal(ex, problem, new HttpHeaders(), ex.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<Fields> fields = new ArrayList<>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            String name = ((FieldError) error).getField();
            fields.add(new Fields(name, message));
        }

        Problem problem = new Problem();
        problem.setStatus(status.value());
        problem.setTitle("Verifique todos os campos obrigatórios!!");
        problem.setDateTime(LocalDateTime.now());
        problem.setFields(fields);

        return super.handleExceptionInternal(ex, problem, headers, status, request);
    }
}
