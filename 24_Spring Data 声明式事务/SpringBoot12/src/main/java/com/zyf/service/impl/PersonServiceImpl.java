package com.zyf.service.impl;

import com.zyf.dao.PersonRepository;
import com.zyf.domain.Person;
import com.zyf.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zyf on 2018/3/13.
 */
@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	PersonRepository personRepository;

	//通过rollbackfor属性指定：出现该异常则数据回滚
	@Transactional(rollbackFor = {IllegalArgumentException.class})
	@Override
	public Person savePersonWithRollBack(Person person) {
		Person p = personRepository.save(person);
		if(person.getName().equals("zyf")){
			throw new IllegalArgumentException("不好意思不能存，回滚啦");
		}
		return p;
	}

	//通过noRollbackFor属性指定：出现该异常则数据不回滚
	@Transactional(noRollbackFor = {IllegalArgumentException.class})
	@Override
	public Person savePersonWithoutRollBack(Person person) {
		Person p = personRepository.save(person);
		if(person.getName().equals("zyf")){
			throw new IllegalArgumentException("不好意思不能存，回滚啦");
		}
		return p;
	}
}
