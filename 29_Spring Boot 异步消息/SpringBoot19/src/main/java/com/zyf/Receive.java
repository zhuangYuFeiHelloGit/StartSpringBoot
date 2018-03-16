package com.zyf;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by zyf on 2018/3/15.
 */
@Component
public class Receive {

	/**
	 * 注解@JmsListener是Spring 4.1提供的新特性，用来简化JMS开发
	 * 只需通过destination属性指定要监听的目的地，即可接收到该目的地的消息
	 * @param message
	 */
	@JmsListener(destination = "my-destination")
	public void receiveMessage(String message){
		System.out.println("接收到：<"+message+">");
	}
}
