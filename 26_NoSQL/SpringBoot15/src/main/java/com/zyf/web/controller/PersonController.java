package com.zyf.web.controller;

import com.zyf.dao.PersonDao;
import com.zyf.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zyf on 2018/3/14.
 */
@RestController
public class PersonController {

	@Autowired
	PersonDao personDao;

	/**
	 * 设置字符及对象
	 */
	@RequestMapping("/set")
	public void set(){
		Person person = new Person("1","zyf",32);
		personDao.save(person);
		personDao.stringRedisTemplateDemo();
	}

	/**
	 * 获得字符串
	 * @return
	 */
	@RequestMapping("/getStr")
	public String getStr(){
		return personDao.getString();
	}

	/**
	 * 获得对象
	 * @return
	 */
	@RequestMapping("/getPerson")
	public Object getPerson(){
		return personDao.getPerson();
	}
}
