package com.teqniated.erp.Inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration//(exclude = {ErrorMvcAutoConfiguration.class})
public class InventoryApplication {

	public static void main(String[] args) {

		SpringApplication.run(InventoryApplication.class, args);
	}

}
