package com.rayan.easy_ecommerce.infra.exceptions.custom;

import com.rayan.easy_ecommerce.infra.exceptions.handler.EasyEcommerceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UserAlreadyExistsException extends EasyEcommerceException {

          private final String detail;

          public UserAlreadyExistsException(String detail) {
                    this.detail = detail;
          }

          @Override
          public ProblemDetail toProblemDetail() {
                    var problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
                    problemDetail.setTitle("User already exists");
                    problemDetail.setDetail(detail);
                    return problemDetail;
          }

}
