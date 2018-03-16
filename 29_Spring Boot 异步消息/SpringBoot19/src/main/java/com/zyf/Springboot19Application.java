package com.zyf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;

/**
 * 实现CommandLineRunner，重写的run方法
 * 会在程序启动后执行
 * 这样当程序启动后，就会执行run，向目的地发送消息
 */
@SpringBootApplication
public class Springboot19Application implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(Springboot19Application.class, args);
	}

	/**
	 * 注入Spring Boot为我们配置好的jmsTemplate
	 */
	@Autowired
	JmsTemplate jmsTemplate;

	@Override
	public void run(String... strings) throws Exception {
		//通过jmsTemplate的send方法向：my-destination 目的地发送一个Msg消息
		//此时就会在消息代理上定义了一个目的地叫 my-destination
		jmsTemplate.send("my-destination",new Msg());
	}
}
