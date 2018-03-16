package com.zyf.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by zyf on 2018/3/7.
 */
@Component
@ConfigurationProperties(prefix = "author")
//该注解可以加载properties中的配置
//通过prefix指定配置文件中的前缀
public class AuthorSettings {
	private String name;
	private String gender;

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
