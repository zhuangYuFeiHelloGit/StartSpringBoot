package com.zyf;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by zyf on 2018/3/5.
 */
public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
				//未指定要加载哪个配置类
				new AnnotationConfigApplicationContext();
		//后置指定
		context.register(AwareConfig.class);
		//刷新容器，一定要刷新!
		context.refresh();
		AwareService awareService = context.getBean(AwareService.class);
		awareService.outputResource();
	}
}
