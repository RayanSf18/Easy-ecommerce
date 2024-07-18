package com.rayan.easy_ecommerce.infra.exceptions;

import com.rayan.easy_ecommerce.infra.exceptions.handler.EasyEcommerceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(EasyEcommerceException.class)
    public ProblemDetail handleSimpleEcommerceException(EasyEcommerceException exception) {
        return exception.toProblemDetail();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        var fieldErrors = exception.getFieldErrors()
            .stream()
            .map(fieldError -> new InvalidParam(fieldError.getField(), fieldError.getDefaultMessage()))
            .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Your request parameters didn't validate.");
        problemDetail.setProperty("invalid-params", fieldErrors);

        return problemDetail;
    }

    private record InvalidParam(String name, String reason){}
}
