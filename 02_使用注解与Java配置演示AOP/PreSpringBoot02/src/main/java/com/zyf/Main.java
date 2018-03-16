package com.zyf;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by zyf on 2018/3/2.
 */
public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(AopConfig.class);

		ShowAnnotationService annotationService = context.getBean(ShowAnnotationService.class);
		ShowMethodService methodService = context.getBean(ShowMethodService.class);


		annotationService.add();
		methodService.add();

		context.close();
	}
}
