package com.zyf;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by zyf on 2018/3/2.
 */
public class InitDesMain {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(InitDesConfig.class);

		JSR250Service jsr250Service = context.getBean(JSR250Service.class);
		BeanService beanService = context.getBean(BeanService.class);


	}
}
