package com.zyf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by zyf on 2018/3/2.
 */
@Configuration//这是一个配置类
@ComponentScan("com.zyf")//扫描哪些包中的注解
@EnableAspectJAutoProxy//开启Spring对AspectJ的支持
public class AopConfig {

	//因为我们已经在需要使用的类上，通过注解的方式声明成bean了
	//就无需再java配置类中，自定义方法返回对象了

}
