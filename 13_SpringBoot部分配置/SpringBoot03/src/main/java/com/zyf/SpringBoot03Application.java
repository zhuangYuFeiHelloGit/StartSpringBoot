package com.zyf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringBoot03Application {

	@RequestMapping("/")
	public String index(){
		return "仔细看端口号";
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot03Application.class, args);
	}
}
