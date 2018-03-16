package com.zyf;

import java.lang.annotation.*;

/**
 * 拦截规则的注解
 */
@Target(ElementType.METHOD)//该注解使用在方法声明上
@Retention(RetentionPolicy.RUNTIME)//该注解在运行时使用
//注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在
//也就是说内存中的字节码中也包含该注解
@Documented//使用javadoc等工具生成文档时，会将该注解也加入进文档
public @interface Action {

	String name();

}
