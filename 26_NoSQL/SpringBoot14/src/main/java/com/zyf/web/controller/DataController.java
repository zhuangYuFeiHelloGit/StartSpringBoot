package com.zyf.web.controller;

import com.zyf.dao.PersonRepository;
import com.zyf.domain.Location;
import com.zyf.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by zyf on 2018/3/14.
 */
@RestController
public class DataController {

	@Autowired
	PersonRepository personRepository;

	@RequestMapping("/save")
	public Person save(){
		Person person = new Person("zyf",99);
		Collection<Location> locations = new LinkedHashSet<Location>();

		Location l1 = new Location("北京","2000");
		Location l2 = new Location("上海","2001");
		Location l3 = new Location("广州","2002");
		Location l4 = new Location("深圳","2003");

		locations.add(l1);
		locations.add(l2);
		locations.add(l3);
		locations.add(l4);

		person.setLocations(locations);

		return personRepository.save(person);
	}

	@RequestMapping("/q1")//测试方法名查询
	public Person q1(String name){
		return personRepository.findByName(name);
	}


	@RequestMapping("/q2")
	public List<Person> q2(Integer age){
		return personRepository.withQueryFindByAge(age);
	}

}

