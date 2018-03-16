### 一，SpringMVC的其他注解
* `@ControllerAdvice` 将控制器的全局配置放置在一个位置
* 注解了 `@Controller` 的类的方法可使用 `@ExceptionHandler` `@InitBinder` `@ModelAttribute` 注解到方法上，这对所有注解了 `@RequestMapping` 的控制器内的方法都有效
* `@ExceptionHandler` 用于全局处理控制器里的异常
* `@InitBinder` 用来设置 `WebDataBinder` ， `WebDataBinder` 用来自动前台请求参数到Model中
* `@ModelAttribute` 绑定键值对到Model中，可用来让全局的 `@RequestMapping` 都能获得在此处设置的键值对

#### 1，创建DemoObj类

```java
package com.zyf.domain;

/**
 * Created by zyf on 2018/3/5.
 */
public class DemoObj {
	private Long id;
	private String name;

	public DemoObj() {
	}

	public DemoObj(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "DemoObj{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}

```
#### 2，定制ControllerAdvice

```java
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


```

#### 3，创建AdviceController控制器

```java
package com.zyf.controller;

import com.zyf.domain.DemoObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zyf on 2018/3/5.
 */
@Controller
public class AdviceController {

	@RequestMapping("/advice")
	public String getSomething(@ModelAttribute("msg") String m, DemoObj obj){
		System.out.println(obj);
		throw new IllegalArgumentException("非常抱歉，参数有误/"+"来自@ModelAttribute："+m);
	}
}

```

#### 4，创建错误展示页面
![](https://ws3.sinaimg.cn/large/006tKfTcgy1fp2ahp84ayj307b04nt8r.jpg)


```jsp
<%@page language="java" contentType="text/html;UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitioinal//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <title>@ControllerAdvice Demo</title>
</head>
<body>
<h1>
    ${errorMessage}
</h1>

</body>
</html>

```

#### 5，部署运行访问
[](http://localhost:8080/advice?id=1&name=3)

##### 并没有接收到id，因为id被过滤了
![](https://ws3.sinaimg.cn/large/006tKfTcgy1fp2aekxpckj305r00p3yc.jpg)

##### 页面显示
![](https://ws2.sinaimg.cn/large/006tKfTcgy1fp2ajp9ah8j31bm07mwgd.jpg)


### 二，快捷的ViewController
* 对于无任何业务处理，只是简单的页面转向，下面代码就很麻烦。

```java
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

```

* 通过在配置中类重写 `addViewControllers` 可以简化配置过程
* 对应 `/index` 的请求方法就不需要些了

```java
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//路径为/index的请求，显示名称为index的视图
		registry.addViewController("/index").setViewName("/index");
	}
```


### 三，路径匹配参数配置
* 在SpringMVC中，路径参数如果带 `.` ，那么 `.` 后面的值将被忽略
* 在配置类中重写configurePathMatch方法则可不忽略后面的内容

#### 1，在HelloController中创建方法pathParam

```java
	/**
	 * @RequestMapping("/pathParam/{str}") 中的str就对应方法中的str参数
	 * @param str
	 * @param request
	 * @return
	 */
	@RequestMapping(value = ("/pathParam/{str}"),produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String pathParam(@PathVariable String str, HttpServletRequest request){
		return "url:"+request.getRequestURL() + "    参数值为："+str;
	}
	
```
	
#### 2，部署后访问演示
![](https://ws2.sinaimg.cn/large/006tKfTcgy1fp2axjvbuzj30pa04c0tf.jpg)

#### 3，重写配置类中的configurePathMatch方法

```java
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		//不做匹配，则不会忽略.后面的内容
		configurer.setUseSuffixPatternMatch(false);
	}
```

#### 4，部署后访问演示
![](https://ws4.sinaimg.cn/large/006tKfTcgy1fp2b35dltvj30ps06u0tt.jpg)



