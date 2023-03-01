package com.tma.coffeehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class CoffeehouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeehouseApplication.class, args);
	}

}
