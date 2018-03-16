package com.zyf.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zyf on 2018/3/5.
 */
@ControllerAdvice//声明一个控制器建言（通知），会自动注册成Spring的Bean
public class ExceptionHandlerAdvice {

	/**
	 * @ExceptionHandler 在此处定义为全局处理，value过滤拦截的条件，这里就是拦截所有的Exception
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	public ModelAndView exception(Exception exception, WebRequest request){
		ModelAndView modelAndView = new ModelAndView("error");
		modelAndView.addObject("errorMessage",exception.getMessage());

		return modelAndView;
	}

	@ModelAttribute//将键值对添加到全局，这样任何一个注解了@RequestMapping的方法都可以获得此键值对
	public void addAttributes(Model model){
		model.addAttribute("msg","建言中的内容");
	}

	@InitBinder//定制WebInitBinder
	public void initBinder(WebDataBinder webDataBinder){
		//忽略request中的参数id
		webDataBinder.setDisallowedFields("id");
	}


}
