package com.zyf.controller;

import com.zyf.domain.Person;
import com.zyf.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zyf on 2018/3/13.
 */
@Controller
public class PersonController {

	@Autowired
	PersonService personService;

	@RequestMapping("/rollback")
	public Person rollback(Person person){
		return personService.savePersonWithRollBack(person);
	}

	@RequestMapping("/norollback")
	public Person noRollback(Person person){
		return personService.savePersonWithoutRollBack(person);
	}
}
