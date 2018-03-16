package com.zyf.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

/**
 * Created by zyf on 2018/3/6.
 */
@Controller
public class SseController {

	/**
	 * 若使用SSE，则需要向服务器端输出的类型为：text/event-stream
	 *
	 * @return
	 */
	@RequestMapping(value = "/push",produces = {"text/event-stream;charset=UTF-8"})
	@ResponseBody
	public String push(){
		Random r = new Random();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return "data:SSE 1,2,3   " + r.nextInt() + "\n\n";
	}



}
