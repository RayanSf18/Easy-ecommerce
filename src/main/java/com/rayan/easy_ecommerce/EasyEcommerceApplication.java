package com.rayan.easy_ecommerce;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Easy-ecommerce", version = "1.0.0", description = "Backend API for eCommerce management with users, products, categories, orders and payments."))
public class EasyEcommerceApplication {
	public static void main(String[] args) {
		SpringApplication.run(EasyEcommerceApplication.class, args);
	}
}
