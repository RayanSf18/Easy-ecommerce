package com.rayan.easy_ecommerce.infra.exceptions.custom;

import com.rayan.easy_ecommerce.infra.exceptions.handler.EasyEcommerceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UserNotFoundException extends EasyEcommerceException {

          private final String detail;

          public UserNotFoundException(String detail) {
                    this.detail = detail;
          }

          @Override
          public ProblemDetail toProblemDetail() {
                    var problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                    problemDetail.setTitle("User not found");
                    problemDetail.setDetail(detail);
                    return problemDetail;
          }

}
