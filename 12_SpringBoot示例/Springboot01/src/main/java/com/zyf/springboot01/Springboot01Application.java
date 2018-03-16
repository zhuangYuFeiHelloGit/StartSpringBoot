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
@ComponentScan()
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




