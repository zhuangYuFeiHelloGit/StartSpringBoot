### 1，SpringBoot创建
### 2，基本配置
* `Spring Boot` 通常有一个名为*Application的入口类，类中有一个 `main` 方法，这个方法就是一个标准的Java应用的入口方法。
* 在 `main` 方法中使用 `SpringApplication.run(Springboot01Application.class,args)`， 启动SpringBoot项目。
* [修改banner的网址：](http://patorjk.com/software/taag/)

### 3，SpringBoot的配置文件
* SpringBoot使用一个全局的配置文件 `application.properies` 或 `application.yml` 放置在 `src/main/resources` 目录或者类路径的 `/config` 下
* SpringBoot的全局配置文件的作用是对一些默认配置的配置值进行修改。

#### 示例
* 将Tomcat的端口号改为 `9090` ，并将默认的访问路径 `/` 改为 `/helloboot`
* 在 `application.properties` 中添加：

```java
server.port=9090
server.context-path=/springboot01
```

### 4，starter pom
* SpringBoot为我们提供了简化企业级开发绝大多数场景的 `starter pom` ，只要使用了应用场景所需要的 `starter pom` ，相关的技术配置将会消除，就可以得到 SpringBoot 为我们提供的自动配置的 `Bean`

#### 1，官方 starter pom（简单写俩）
| spring-boot-starter | SpringBoot核心starter 包含自动配置，日志，yaml配置文件的支持 |
| --- | --- |
| spring-boot-starter-aop  | 使用spring-aop和AspectJ支持面向切面编程 |


#### 2，第三方的 starter pom
### 5，使用xml配置
* Spring Boot 提倡零xml配置，但是有时候可能会遇到特殊要求，必须使用xml配置，这是可以通过下面语句加载xml配置。

![](https://ws2.sinaimg.cn/large/006tKfTcgy1fp35nlmwj3j31im056ta7.jpg)

#### SpringBoot还支持命令行参数作为外部配置

### 6，常规属性配置
* 在 `SpringBoot` 中，注入 `properties` 文件中的值，只需要在 `application.properties` 中定义属性，然后使用 `@Value` 注入即可

#### 1，`application.properties` 中添加属性

```java
project.author=zyf
project.name=springboot01
```

#### 2，修改入口类

```java
package com.zyf.springboot01;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//核心注解：是一个组合注解，功能：配置，自动配置，扫描
//自动配置：根据类路径中的jar包为当前项目自动配置
// 如果添加了spring-boot-starter-web依赖：（自动配置Tomcat和SpringMVC）
//SpringBoot会自动扫描 @SpringBootApplication所在类的同级包以及下级包里的Bean
//下面是关闭：DataSource的自动配置
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})

//@ImportResource({"classpath:some-context.xml","classpath:another-context.xml",})

@RestController
@SpringBootApplication
@ComponentScan("com.zyf")
public class Springboot01Application {


	@Value("${project.author}")
	private String projectAuthor;

	@Value("${project.name}")
	private String projectName;

	@RequestMapping("/")
	public String index(){

		return "projectAuthor：---"+projectAuthor+"     projectName：---"+projectName;
	}

	public static void main(String[] args) {
		//启动SpringBoot项目
		SpringApplication.run(Springboot01Application.class, args);
//		关闭banner
//		SpringApplication app = new SpringApplication(Springboot01Application.class);
//		app.setBannerMode(Banner.Mode.OFF);
//		app.run(args);
	}
}
```



