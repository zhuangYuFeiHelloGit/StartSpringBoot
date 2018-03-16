package com.zyf.controller;

import com.zyf.domain.ZyfMessage;
import com.zyf.domain.ZyfResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by zyf on 2018/3/8.
 */
@Controller
public class ZyfController {

	@MessageMapping("/welcome")//当浏览器向服务端发送请求时，通过@MessageMapping映射/welcome这个地址
	//类似于 @RequestMapping
	@SendTo("/topic/getResponse")//当服务端有消息时，会对订阅了 @SendTo 中的路径的浏览器发送消息
	public ZyfResponse say(ZyfMessage zyfMessage) throws InterruptedException {
		Thread.sleep(3000);
		return new ZyfResponse("Welcome, "+zyfMessage.getName()+"!");

	}
}
