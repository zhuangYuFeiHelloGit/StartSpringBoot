package com.zyf.config;

import com.zyf.filter.DemoFilter;
import com.zyf.listener.DemoListener;
import com.zyf.servlet.DemoServlet;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by zyf on 2018/3/7.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Bean
	public DemoServlet demoServlet(){
		return new DemoServlet();
	}

	@Bean
	public DemoFilter demoFilter(){
		return new DemoFilter();
	}

	@Bean
	public DemoListener demoListener(){
		return new DemoListener();
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean(){
															//路径
		return new ServletRegistrationBean(new DemoServlet(),"/demo/*");
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new DemoFilter());
		//spring boot 会按照order值的大小，从小到大的顺序来依次过滤
		registrationBean.setOrder(2);
		return registrationBean;

	}

	@Bean
	public ServletListenerRegistrationBean<DemoListener> demoListenerServletListenerRegistrationBean(){
		return new ServletListenerRegistrationBean<>(new DemoListener());
	}


	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/index").setViewName("/index");
	}
}
