package com.zyf;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by zyf on 2018/3/5.
 */
public class AnnotationMain {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(MyConfig.class);
		MyService myService = context.getBean(MyService.class);

		myService.output();

		context.close();
	}
}
