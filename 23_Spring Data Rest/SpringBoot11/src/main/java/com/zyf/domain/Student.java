package com.zyf.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zyf on 2018/3/13.
 */
@Entity
public class Student {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String gender;

	public Student() {
	}

	public Student(String name, String gender) {
		this.name = name;
		this.gender = gender;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
}
