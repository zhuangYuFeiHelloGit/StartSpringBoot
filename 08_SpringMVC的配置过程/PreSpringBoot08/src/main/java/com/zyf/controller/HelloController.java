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
