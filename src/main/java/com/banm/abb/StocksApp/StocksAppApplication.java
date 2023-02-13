package com.banm.abb.StocksApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class StocksAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(StocksAppApplication.class, args);
	}
}
