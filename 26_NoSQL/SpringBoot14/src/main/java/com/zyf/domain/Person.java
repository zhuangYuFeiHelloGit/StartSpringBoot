package com.zyf.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Created by zyf on 2018/3/14.
 */
@Document//映射领域模型与MongoDB的文档
public class Person {

	@Id//表示这个属性为文档的id
	private String id;

	private String name;

	private Integer age;

	@Field("locs")//表示locations属性在文档中的名称为locs，locations将以数组形式存在当前数据记录中
	private Collection<Location> locations = new LinkedHashSet<>();

	public Person(String name, Integer age) {
		this.name = name;
		this.age = age;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public Collection<Location> getLocations() {
		return locations;
	}

	public void setLocations(Collection<Location> locations) {
		this.locations = locations;
	}
}
