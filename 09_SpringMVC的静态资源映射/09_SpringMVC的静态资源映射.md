### SpringMVC的静态资源映射
程序的静态文件（js,css,图片）等需要直接访问的话，如下配置。

#### 1，创建目录resources/assets/js
![](https://ws3.sinaimg.cn/large/006tNc79gy1fp28zoo0aij306i048wei.jpg)

#### 2，修改MyMvcConfig配置文件

```java
package com.zyf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Created by zyf on 2018/3/5.
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.zyf")
public class MyMvcConfig extends WebMvcConfigurerAdapter{

	/**
	 *
	 * @returnInternalResourceViewResolver 是SpringMVC视图渲染的核心机制
	 */
	@Bean
	public InternalResourceViewResolver viewResolver(){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		//jsp页面前缀
		viewResolver.setPrefix("/WEB-INF/classes/views/");
		//后缀
		viewResolver.setSuffix(".jsp");
		viewResolver.setViewClass(JstlView.class);
		return viewResolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//addResourceHandler指的是对外暴露的访问路径
		//addResourceLocations指的是文件放置的目录
		registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/assets/");
	}
}

```

### 2，SpringMVC的拦截器配置
* 拦截器实现对每一个请求处理前后进行相关的业务处理。
* 可让普通的Bean实现 `HanlderInterceptor` 接口或者继承 `HandlerInterceptorAdapter` 类来实现自定义拦截器

#### 1，创建ShowInterceptor类

```java
package com.zyf.interceptor;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zyf on 2018/3/5.
 */
public class ShowInterceptor extends HandlerInterceptorAdapter {


	public ShowInterceptor() {
		System.out.println("创建了一个------");
	}

	/**
	 * 请求前执行
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		System.out.println("请求之前");
		return true;//放行
	}

	/**
	 * 请求后执行
	 * @param request
	 * @param response
	 * @param handler
	 * @param modelAndView
	 * @throws Exception
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		System.out.println("请求之后");
	}
}

```

#### 2，将该类，作为Bean配置在在MyMvcConfig配置类中

```java
	@Bean
	public ShowInterceptor showInterceptor(){
		return new ShowInterceptor();
	}
```

#### 3，在MyMvcConfig配置类中复写方法注册该拦截器

```java	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(showInterceptor());
	}

```

#### 4，部署项目访问index

![](https://ws2.sinaimg.cn/large/006tNc79gy1fp29hyhkmej306902vwej.jpg)

