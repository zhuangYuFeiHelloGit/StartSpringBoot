package com.zyf;

/**
 * Created by zyf on 2018/3/2.
 * 使用@Bean形式的Bean
 */
public class BeanService {

	public void init(){
		System.out.println("@Bean-init-method");
	}

	public BeanService() {
		System.out.println("初始化构造方法-BeanService");
	}

	public void destroy(){
		System.out.println("@Bean-destroy-method");
	}

}
