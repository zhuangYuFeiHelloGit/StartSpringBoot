### Spring常用配置
#### 1，Bean的Scope

```java
//默认是Singleton，相当于@Scope("singleton")，单例
@Service
public class DefaultScopeService{
}

@Service
@Scope("prototype")//声明为prototype，每次获得都会创建一个新的
public class DefaultScopeService{
}

```
#### 2，Spring EL-Spring及资源调用
##### 01，创建show.properties文件
![](https://ws1.sinaimg.cn/large/006tNc79gy1foygmz0e23j30d40a8wf3.jpg)    

![](https://ws4.sinaimg.cn/large/006tNc79gy1foygmlw0mlj30ki04w3yz.jpg)

```
    author=zyf
    name=BaseSpringConfig
```

##### 02，创建show.txt文件
![](https://ws1.sinaimg.cn/large/006tNc79gy1foygnc1gfmj30lw04edgf.jpg)

```text
    此去经年，应是良辰好景虚设。
```
##### 03，增添pom.xml中的依赖于配置

```xml
<!--03_新的依赖：简化文件相关操作-->
<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.2</version>
</dependency>
```



```xml
<!--在build标签中增添如下标签，将对应文件后缀名的文件，加入到编译后的类（Class字节码文件）路径中-->

<resources>
  <resource>
      <directory>src/main/java</directory>
      <includes>
          <include>**/*.properties</include>
          <include>**/*.txt</include>
      </includes>
      <!--不过滤上述后缀名的文件-->
      <filtering>false</filtering>
  </resource>
</resources>
```


##### 1，创建ELConfig配置类文件

```java
package com.zyf;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * Created by zyf on 2018/3/2.
 */
@Configuration
@ComponentScan("com.zyf")
@PropertySource("classpath:com/zyf/show.properties")//指定资源文件的路径
public class ELConfig {

	//1，注入普通字符串
	@Value("便纵用万种风情，更与何人说")
	private String normal;

	//2，注入操作系统属性
	@Value("#{systemProperties['os.name']}")
	private String osName;

	//3，注入表达式的结果，调用Math类静态方法 * 100.0得到的结果
	@Value("#{T(java.lang.Math).random() * 100.0}")
	private double randomNumber;

	//4，注入其它bean的属性
	@Value("#{demoService.another}")
	private String fromAnother;

	//5，注入文件资源
	@Value("classpath:com/zyf/show.txt")
	private Resource showFile;

	//6，注入网址资源
	@Value("http://www.baidu.com")
	private Resource baiduUrl;

	//7，注入资源文件（.properties）中的内容
	//已经通过PropertySource指定了资源文件的路径
	@Value("${name}")
	private String name;

	//7，若要使用value注入，则需要搭配如下静态方法
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer(){
		return new PropertySourcesPlaceholderConfigurer();
	}

	//7，注入资源文件后，也可以在environment对象中获得资源文件中的内容
	@Autowired
	private Environment environment;

	public void outputResource(){
		try {
			System.out.println("normal：---"+normal);
			System.out.println("osName：---"+osName);
			System.out.println("randomNumber：---"+randomNumber);
			System.out.println("fromAnother：---"+fromAnother);

			System.out.println(IOUtils.toString(showFile.getInputStream()));
			System.out.println(IOUtils.toString(baiduUrl.getInputStream()));
			System.out.println("name：---"+name);
			System.out.println("environment.getProperty('author')：---"+environment.getProperty("author"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
```

##### 2，测试

```java
public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(ELConfig.class);
		ELConfig bean = context.getBean(ELConfig.class);
		bean.outputResource();

		context.close();
	}
}
```

##### 3，测试结果
![](https://ws4.sinaimg.cn/large/006tNc79gy1foygpu6hmoj314q0dctcv.jpg)

#### 3，Bean的初始化和销毁
有时需要在Bean使用之前或之后做些必要的操作，Spring对Bean的生命周期的操作提供了支持。    

* Java配置方式：使用 `@Bean` 的 `initMethod` 和 `destroyMethod`。
* 注解方式：利用JSR250的 `@PostConstruct` 和 `@PreDestroy`

##### 0，在pom.xml中增加JSR250的依赖库

```xml
<!--03_新的依赖：提供bean初始化与销毁注解的支持-->
<dependency>
  <groupId>javax.annotation</groupId>
  <artifactId>jsr250-api</artifactId>
  <version>1.0</version>
</dependency>
```
##### 1，创建BeanService类

```java
package com.zyf;

/**
 * Created by zyf on 2018/3/2.
 * 使用@Bean形式的Bean
 */
public class BeanService {

	public void init(){
		System.out.println("@Bean-init-method");
	}

	public BeanService() {
		System.out.println("初始化构造方法-BeanService");
	}

	public void destroy(){
		System.out.println("@Bean-destroy-method");
	}

}

```

##### 2，创建JSR250Service类

```java
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

```

##### 3，创建InitDesConfig配置类

```java
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

```

##### 4，测试

```java
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
```

##### 5，测试结果
![](https://ws2.sinaimg.cn/large/006tNc79gy1foyhy6j9lyj30m608mtag.jpg)

