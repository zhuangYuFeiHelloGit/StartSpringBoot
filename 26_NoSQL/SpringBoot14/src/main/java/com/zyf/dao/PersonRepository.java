package com.zyf.dao;

import com.zyf.domain.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by zyf on 2018/3/14.
 */
public interface PersonRepository extends MongoRepository<Person,String>{

	//支持方法名查询
	Person findByName(String name);

	@Query("{'age':?0}")//支持@Query查询，查询参数：一个JSON字符串
	List<Person> withQueryFindByAge(Integer age);
}
