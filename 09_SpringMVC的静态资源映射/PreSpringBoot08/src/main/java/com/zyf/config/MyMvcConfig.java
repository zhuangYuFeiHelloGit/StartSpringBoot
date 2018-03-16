package com.zyf.config;

import com.zyf.interceptor.ShowInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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


	@Bean
	public ShowInterceptor showInterceptor(){
		return new ShowInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(showInterceptor());
	}
}
