package com.zyf.service.impl;

import com.zyf.PersonRepository;
import com.zyf.domain.Person;
import com.zyf.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by zyf on 2018/3/13.
 */
@Service
public class PersonServiceImpl implements PersonService{

	@Autowired
	PersonRepository personRepository;

	@Override
	@CachePut(value = "people",key = "#person.id")//缓存名称为people//#可以找到参数person并获取它的id
	public Person save(Person person) {
		Person p = personRepository.save(person);

		System.out.println("为id、key为："+p.getId()+"的数据做了缓存");
		return p;
	}

	@Override
	@CacheEvict(value = "people",key = "id")//找到参数id
//	@CacheEvict(value = "people")//如果没有指定key，则方法的参数会作为key使用
	public void remove(Long id) {
		System.out.println("删除了id、key为："+id+"的数据缓存");
		personRepository.delete(id);
	}

	@Override
	@Cacheable(value = "people",key = "#person.id")//将person加入缓存，key为person的id属性值
	public Person findOne(Person person) {
		Person p = personRepository.findOne(person.getId());
		System.out.println("为、key为："+person.getId()+"的数据做了缓存处理");
		return p;
	}
}
