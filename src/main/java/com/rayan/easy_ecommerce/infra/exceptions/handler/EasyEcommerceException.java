package com.rayan.easy_ecommerce.infra.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class EasyEcommerceException extends RuntimeException {

    public ProblemDetail toProblemDetail() {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Easy-Ecommerce internal server error.");
        return problemDetail;
    }
}
