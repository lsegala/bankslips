package com.contaazul.bankslips;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * Criado por leonardo.segala em 04/07/2018.
 */
@RestController
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

    public static final String ERROR_MESSAGE = "Invalid bankslip provided.The possible reasons are:\n* A field of the provided bankslip was null or with invalid values";

    @ExceptionHandler(TransactionSystemException.class)
    protected ResponseEntity<Object> bankSlipConstraintExceptionHandler(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse;
        if(ex.getCause() instanceof RollbackException && ex.getCause().getCause() instanceof ConstraintViolationException){
            bodyOfResponse = ERROR_MESSAGE;
        }else{
            bodyOfResponse = ex.getMessage();
        }
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(BankSlipNotFoundException.class)
    protected ResponseEntity<Object> bankSlipNotFoundexceptionHandler(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) ((ServletWebRequest) request).getNativeRequest();
        if(ex.getMessage().contains("Cannot deserialize value of type `com.contaazul.bankslips.BankSlipStatus` from String")){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ERROR_MESSAGE);
        }
        if(httpServletRequest.getRequestURI().contains("/rest/bankslips")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bankslip not provided in the request body");
        }
        return super.handleHttpMessageNotReadable(ex, headers, status, request);
    }
}
