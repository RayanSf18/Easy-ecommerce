package com.rayan.easy_ecommerce.infra.exceptions.custom;

import com.rayan.easy_ecommerce.infra.exceptions.handler.EasyEcommerceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ProductQuantityInvalidException extends EasyEcommerceException {

          private final String detail;

          public ProductQuantityInvalidException(String detail) {
                    this.detail = detail;
          }

          @Override
          public ProblemDetail toProblemDetail() {
                    var problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
                    problemDetail.setTitle("Quantity not valid");
                    problemDetail.setDetail(detail);
                    return problemDetail;
          }

}
