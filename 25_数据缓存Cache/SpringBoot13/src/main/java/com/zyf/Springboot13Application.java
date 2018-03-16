package com.zyf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Springboot13Application {

	public static void main(String[] args) {
		SpringApplication.run(Springboot13Application.class, args);
	}
}
