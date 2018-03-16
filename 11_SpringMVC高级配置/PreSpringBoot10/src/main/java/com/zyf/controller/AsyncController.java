package com.zyf.controller;

import com.zyf.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by zyf on 2018/3/6.
 */
@Controller
public class AsyncController {

	/**
	 * 自动装配
	 */
	@Autowired
	PushService pushService;

	@RequestMapping("/defer")
	@ResponseBody
	public DeferredResult<String> deferredCall(){
		//返回给客户端
		return pushService.getAsyncUpdate();
	}
}
