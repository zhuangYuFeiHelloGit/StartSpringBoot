package com.zyf;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Springboot20Application implements CommandLineRunner{

	/**
	 * Spring Boot 已经为我们配置好rabbitTemplate
	 */
	@Autowired
	RabbitTemplate rabbitTemplate;

	public static void main(String[] args) {
		SpringApplication.run(Springboot20Application.class, args);
	}

	/**
	 * 定义目的地（队列），队列名称为：zyf-queue
	 * @return
	 */
	@Bean
	public Queue zyfQueue(){
		return new Queue("zyf-queue");
	}

	@Override
	public void run(String... strings) throws Exception {
		//向对列zyf-queue发送消息
		rabbitTemplate.convertAndSend("zyf-queue","来自RabbitMQ的消息：我是AMQP的实现");
	}
}


