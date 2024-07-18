package com.rayan.easy_ecommerce.infra.exceptions.custom;

import com.rayan.easy_ecommerce.infra.exceptions.handler.EasyEcommerceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ProductNotFoundException extends EasyEcommerceException {

          private final String detail;

          public ProductNotFoundException(String detail) {
                    this.detail = detail;
          }

          @Override
          public ProblemDetail toProblemDetail() {
                    var problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                    problemDetail.setTitle("Product not found");
                    problemDetail.setDetail(detail);
                    return problemDetail;
          }

}
