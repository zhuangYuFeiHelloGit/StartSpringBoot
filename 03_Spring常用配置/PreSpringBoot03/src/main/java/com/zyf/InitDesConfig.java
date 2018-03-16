package com.zyf;

import org.springframework.context.annotation.Bean;

/**
 * Created by zyf on 2018/3/2.
 */
public class InitDesConfig {

	//需要手动指定哪个方法是init
	@Bean(initMethod = "init",destroyMethod = "destroy")
	public BeanService beanService(){
		return new BeanService();
	}


	//已经在JSR250类内部通过注解指定了
	@Bean
	public JSR250Service jsr250Service(){
		return new JSR250Service();
	}

}
