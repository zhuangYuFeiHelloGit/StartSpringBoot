package com.zyf.web.controller;

import com.zyf.domain.Person;
import com.zyf.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zyf on 2018/3/13.
 */
@RestController
public class CacheController {

	@Autowired
	PersonService personService;

	@RequestMapping("/put")
	public Person put(Person person){
		return personService.save(person);
	}

	@RequestMapping("/able")
	public Person cacheable(Person person){
		return personService.findOne(person);
	}

	@RequestMapping("/evit")
	public String evit(Long id){
		personService.remove(id);
		return "ok";
	}

}
