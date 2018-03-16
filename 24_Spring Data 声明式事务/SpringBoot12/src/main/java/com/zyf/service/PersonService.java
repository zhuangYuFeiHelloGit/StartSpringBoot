package com.zyf.service;

import com.zyf.domain.Person;

/**
 * Created by zyf on 2018/3/13.
 */
public interface PersonService {
	/**
	 * 演示回滚
	 */
	Person savePersonWithRollBack(Person person);

	/**
	 * 演示不会滚
	 */
	Person savePersonWithoutRollBack(Person person);
}
