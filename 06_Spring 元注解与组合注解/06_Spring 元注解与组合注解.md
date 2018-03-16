### 其他注解
* `@profile` 不同环境下使用不同的配置（开发环境，测试环境，生产环境（上线））
* `@EnableScheduling` 计划任务，实际上就相当于定时执行
* `@Condition` 根据满足某一个特定条件创建一个特定的Bean

### 组合注解与元注解
* 从Spring 2 开始，为了响应JDK1.5推出的注解功能，Spring开始大量加入注解来替代xml配置
* Spring注解主要用来配置和注入Bean，以及AOP相关配置（ `@Transaction` ）
* 但是代码越来越多，就会有好多相同的注解，被标注在不同的类或方法中，这就是重复性的代码，要消除他们。

**元注解就是可以注解到别的注解上的注解，被注解的注解称之为组合注解，组合注解具备元注解的功能**    

`我们使用@Configuration标注配置类，实际上该配置类也有了@Component的含义，表名这个类也是一个Bean`

### 示例
#### 1，创建组合注解

```java
package com.zyf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.*;

/**
 * Created by zyf on 2018/3/5.
 */
@Target(ElementType.TYPE)//标注在类的声明上
@Retention(RetentionPolicy.RUNTIME)//保存在生成的字节码文件中
@Documented//将注解的信息包含在javadoc生成的文档中
@Configuration
@ComponentScan
public @interface MyConfiguraton {

	/**
	 * 传入的value参数，会覆盖元注解@ComponentScan注解的value上
	 * @return
	 */
	String[] value() default {};

}

```

#### 2，创建配置类

```java
package com.zyf;

/**
 * Created by zyf on 2018/3/5.
 */
@MyConfiguraton()
public class MyConfig {
}

```

#### 3，创建MyService类

```java
package com.zyf;

/**
 * Created by zyf on 2018/3/5.
 */
@MyConfiguraton
public class MyService {
	public void output(){
		System.out.println("组合注解@MyConfiguration好用了");
	}
}

```

#### 4，测试

```java
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

```

#### 5，测试结果
![](https://ws3.sinaimg.cn/large/006tNc79gy1fp1z7dd5qsj30iu09aabs.jpg)

