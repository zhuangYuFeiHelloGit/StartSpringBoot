package com.zyf;

import com.zyf.domain.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@SpringBootApplication
public class Springboot06Application {

	@RequestMapping("/")
	public String index(Model model){
		Person single = new Person("abc",123);

		List<Person> people = new ArrayList<>();
		Person p1 = new Person("x",11);
		Person p2 = new Person("y",22);
		Person p3 = new Person("z",33);

		people.add(p1);
		people.add(p2);
		people.add(p3);

		model.addAttribute("singlePerson",single);
		model.addAttribute("people",people);

		return "index";
	}

	public static void main(String[] args) {
		SpringApplication.run(Springboot06Application.class, args);
	}
}
