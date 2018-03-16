package com.zyf.domain;

/**
 * Created by zyf on 2018/3/5.
 */
public class DemoObj {
	private Long id;
	private String name;

	public DemoObj() {
	}

	public DemoObj(Long id, String name) {
		this.id = id;
		this.name = name;
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

	@Override
	public String toString() {
		return "DemoObj{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
