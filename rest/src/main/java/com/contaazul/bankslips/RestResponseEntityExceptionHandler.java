package com.contaazul.bankslips;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Criado por leonardo.segala em 04/07/2018.
 */
@RestController
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
    @ExceptionHandler(BankSlipConstraintException.class)
    protected ResponseEntity<Object> bankSlipConstraintExceptionHandler(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getCause().getMessage();
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
                    .body(BankSlipListener.ERROR_MESSAGE);
        }
        if(httpServletRequest.getRequestURI().contains("/rest/bankslips")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bankslip not provided in the request body");
        }
        return super.handleHttpMessageNotReadable(ex, headers, status, request);
    }
}
