package com.zyf.service;

import com.zyf.domain.Person;

/**
 * Created by zyf on 2018/3/13.
 */
public interface PersonService {

	Person save(Person person);

	void remove(Long id);

	Person findOne(Person person);
}
