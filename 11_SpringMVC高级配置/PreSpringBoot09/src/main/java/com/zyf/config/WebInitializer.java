package com.zyf.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by zyf on 2018/3/5.
 */
public class WebInitializer implements WebApplicationInitializer {
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext context =
				new AnnotationConfigWebApplicationContext();
		context.register(MyMvcConfig.class);
		//将Spring的配置与ServletContext关联
		context.setServletContext(servletContext);

		context.refresh();

		//将Spring的DispatcherServlet与AnnotationConfigWebApplicationContext关联
		ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(context));

		//上述两个注释是一个双向的过程

		//匹配所有路径
		servlet.addMapping("/");

		//最先加载
		servlet.setLoadOnStartup(1);
	}

}
