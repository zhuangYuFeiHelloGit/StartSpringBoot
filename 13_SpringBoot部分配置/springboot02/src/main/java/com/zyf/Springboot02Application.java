package com.zyf;

import com.zyf.config.AuthorSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Springboot02Application {

	@Autowired
	private AuthorSettings authorSettings;

	@RequestMapping("/")
	public String index(){
		return "author name is " + authorSettings.getName() + "    author gender is " + authorSettings.getGender();
	}

	public static void main(String[] args) {
		SpringApplication.run(Springboot02Application.class, args);
	}
}
