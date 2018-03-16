package com.zyf;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by zyf on 2018/3/2.
 */
public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(ELConfig.class);
		ELConfig bean = context.getBean(ELConfig.class);
		bean.outputResource();

		context.close();
	}
}
