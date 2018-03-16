package com.zyf;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by zyf on 2018/3/16.
 */
@Component
public class Receiver {

	/**
	 * 注解@RabbitListener用来声明要监听哪个队列（目的地）
	 * @param message
	 */
	@RabbitListener(queues = "zyf-queue")
	public void receiveMessage(String message){
		System.out.println("Received <" + message + ">");
	}

}
