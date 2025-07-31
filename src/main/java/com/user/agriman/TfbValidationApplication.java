package com.user.agriman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"Controller", "Service","Configurations"})
public class TfbValidationApplication {

	public static void main(String[] args) {
		SpringApplication.run(TfbValidationApplication.class, args);
	}
}
