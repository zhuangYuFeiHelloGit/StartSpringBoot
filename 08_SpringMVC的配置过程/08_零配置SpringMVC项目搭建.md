### 零配置SpringMVC项目搭建
#### 1，Maven配置文件pom.xml内容

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.zyf</groupId>
    <artifactId>PreSpringBoot08</artifactId>
    <version>1.0-SNAPSHOT</version>

    <packaging>war</packaging>

    <properties>
        <java.version>1.8</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

        <!--web-->
        <jsp.version>2.2</jsp.version>
        <jstl.version>1.2</jstl.version>
        <servlet.version>3.1.0</servlet.version>

        <!--spring-->
        <spring-framework.version>4.3.8.RELEASE</spring-framework.version>
        <spring.groupId>org.springframework</spring.groupId>

        <logback.versioin>1.0.13</logback.versioin>

        <slf4j.version>1.7.5</slf4j.version>

    </properties>

    <dependencies>
        <dependency>
            <groupId>javax</groupId>
            <artifactId>javaee-web-api</artifactId>
            <version>7.0</version>
            <scope>provided</scope>
        </dependency>

        <!--Spring MVC-->
        <dependency>
            <groupId>${spring.groupId}</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>${spring-framework.version}</version>
        </dependency>

        <!--其他web依赖-->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
            <version>${jstl.version}</version>
        </dependency>

        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>${servlet.version}</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>jsp-api</artifactId>
            <version>${jsp.version}</version>
        </dependency>

        <!--spring and Transaction-->
        <dependency>
            <groupId>${spring.groupId}</groupId>
            <artifactId>spring-tx</artifactId>
            <version>${spring-framework.version}</version>
        </dependency>

        <!--使用SLF4J和LogBack作为日志-->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j.version}</version>
        </dependency>

        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>jcl-over-slf4j</artifactId>
            <version>${slf4j.version}</version>
        </dependency>

        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>${logback.versioin}</version>
        </dependency>

        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-core</artifactId>
            <version>${logback.versioin}</version>
        </dependency>

        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-access</artifactId>
            <version>${logback.versioin}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.1.0</version>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

#### 2，日志配置
* 在 `src/main/resource` 目录下，新建 `logback.xml` 用来配置日志

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration scan="true" scanPeriod="1 seconds">

    <contextListener class="ch.qos.logback.classic.jul.LevelChangePropagator">

        <restJUL>true</restJUL>

    </contextListener>

    <jmxConfigurator/>

    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>logback: %d{HH:mm:ss.SSS} %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!--将该包下的日志信息设置为debug级别-->
    <!--能看到更详细是SpringMVC错误-->
    <logger name="org.springframework.web" level="DEBUG"/>

    <root level="info">
        <appender-ref ref="console"/>
    </root>
</configuration>
```

#### 3，创建视图jsp
* 在 `src/main/resources` 下建立 `views` 目录，并在此目录下新建 `index.jsp`

![](https://ws1.sinaimg.cn/large/006tNc79gy1fp28e47fy1j307q079t8w.jpg)

```jsp
<%@page language="java" contentType="text/html;UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitioinal//EN"
        "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <title>Insert title here</title>
</head>

<body>
    <pre>
        Welcome to Spring MVC world
    </pre>
</body>
</html>

```

#### 4，配置Spring MVC

```java
package com.zyf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Created by zyf on 2018/3/5.
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.zyf")
public class MyMvcConfig {

	/**
	 *
	 * @returnInternalResourceViewResolver 是SpringMVC视图渲染的核心机制
	 */
	@Bean
	public InternalResourceViewResolver viewResolver(){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		//jsp页面前缀
		viewResolver.setPrefix("/WEB-INF/classes/views/");
		//后缀
		viewResolver.setSuffix(".jsp");
		viewResolver.setViewClass(JstlView.class);
		return viewResolver;
	}
}

```

#### 5，web配置

```java
package com.zyf.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by zyf on 2018/3/5.
 * 是Spring提供用来配置Servlet3.0+配置的接口
 * 实现该接口后，就可以将WebInitializer用来替代web.xml配置文件
 * 会自动被SpringServletContainerInitializer获取到
 */
public class WebInitializer implements WebApplicationInitializer {


	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(MyMvcConfig.class);
		//将配置类与当前的ServletContext关联起来
		context.setServletContext(servletContext);
		context.refresh();


		//注册springMVC的dispatcherServlet
		ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(context));


		servlet.addMapping("/");

		//最先加载
		servlet.setLoadOnStartup(1);

	}
}

```

#### 6，创建HelloController类

```java
package com.zyf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zyf on 2018/3/5.
 */
@Controller
public class HelloController {

	@RequestMapping("/index")
	public String hello(){
		return "index";
	}
}

```

#### 7，配置tomcat部署项目后，访问index
![](https://ws4.sinaimg.cn/large/006tNc79gy1fp28guc0opj30lm09gaau.jpg)


