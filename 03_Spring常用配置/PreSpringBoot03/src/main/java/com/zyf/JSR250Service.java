package com.zyf;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by zyf on 2018/3/2.
 */
public class JSR250Service {

	@PostConstruct
	public void init(){
		System.out.println("jsr250-init-method");

	}

	public JSR250Service() {
		System.out.println("构造方法：JSR250Service");

	}

	@PreDestroy
	public void destroy(){
		System.out.println("jsr250-destroy-method");
	}

}
