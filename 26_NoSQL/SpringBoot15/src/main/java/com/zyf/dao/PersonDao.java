package com.zyf.dao;

import com.zyf.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by zyf on 2018/3/14.
 */
@Repository
public class PersonDao {

	/**
	 * SpringBoot帮我们配置了StringRedisTemplate
	 * 所以可以直接注入
	 */
	@Autowired
	StringRedisTemplate stringRedisTemplate;

	/**
	 * 同上
	 */
	@Autowired
	RedisTemplate<Object,Object> redisTemplate;

	/**
	 * 通过@Resource注解指定stringRedisTemplate，
	 * 可注入基于字符串的简单属性操作方法
	 */
	@Resource(name = "stringRedisTemplate")
	ValueOperations<String,String> valOpsStr;

	/**
	 * 通过@Resource注解指定redisTemplate，
	 * 可注入基于对象的简单属性操作方法
	 */
	@Resource(name = "redisTemplate")
	ValueOperations<Object,Object> valOps;

	/**
	 * 通过set方法存储字符串类型
	 */
	public void stringRedisTemplateDemo(){
		valOpsStr.set("xx","XX");
	}

	/**
	 * 通过set方法存储对象类型
	 * @param person
	 */
	public void save(Person person){
		valOps.set(person.getId(),person);
	}

	/**
	 * 通过get方法，获得字符串
	 * @return
	 */
	public String getString(){
		return valOpsStr.get("xx");
	}

	/**
	 * 通过get方法，获得对象
	 * @return
	 */
	public Object getPerson(){
		return valOps.get("1");
	}
}
