package com.zyf.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/**
 * Created by zyf on 2018/3/12.
 * 正向工程：通过实体类生成表结构
 * 逆向工程：通过表结构生成实体类
 * 注解@column用来映射属性名和字段名，不使用该注解时，Hibernate会自动根据属性名生成数据表的字段名
 */
@Entity//表示这是一个与数据库表映射的实体类
@NamedQuery(name = "Person.withNameAndAddressNamedQuery",query = "select p from Person p where p.name=?1 and address=?2")
public class Person {

	@Id//表示这个属性映射为数据库中表的的主键
	@GeneratedValue//自增
	private Long id;

	private String name;

	private Integer age;

	private String address;

	public Person() {
	}

	public Person(Long id, String name, Integer age, String address) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", name='" + name + '\'' +
				", age=" + age +
				", address='" + address + '\'' +
				'}';
	}
}
