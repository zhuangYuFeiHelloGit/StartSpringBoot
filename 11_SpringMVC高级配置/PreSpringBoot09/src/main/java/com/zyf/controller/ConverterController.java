package com.zyf.controller;

import com.zyf.domain.DemoObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zyf on 2018/3/5.
 */
@Controller
public class ConverterController {

	/**
	 * produces：指定返回类型为自定义的媒体类型
	 * @RequestBody 将前端传过来的json数据封装到DemoObj对象上
	 * @param demoObj
	 * @return
	 */
	@RequestMapping(value = "/convert",produces = "application/x-zyf")
	@ResponseBody
	public DemoObj convert(@RequestBody DemoObj demoObj){
		System.out.println(demoObj);
		return demoObj;
	}

}
