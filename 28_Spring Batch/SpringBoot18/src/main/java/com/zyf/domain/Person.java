package com.zyf.domain;

import javax.validation.constraints.Size;

/**
 * Created by zyf on 2018/3/15.
 */
public class Person {

	/**
	 * 使用JSR-303校验数据
	 */
	@Size(max = 4,min = 2)
	private String name;
	private int age;
	private String nation;
	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
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
				"name='" + name + '\'' +
				", age=" + age +
				", nation='" + nation + '\'' +
				", address='" + address + '\'' +
				'}';
	}
}
