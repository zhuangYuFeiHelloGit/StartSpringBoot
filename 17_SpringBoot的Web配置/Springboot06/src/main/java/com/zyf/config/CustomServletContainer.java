package com.zyf.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by zyf on 2018/3/7.
 */
@Component
public class CustomServletContainer implements EmbeddedServletContainerCustomizer {
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		//配置端口号
		container.setPort(8888);

		//如果出现404错误，则显示404.html页面
		container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/404.html"));

		//设置session过期时间为10分钟
		container.setSessionTimeout(10, TimeUnit.MINUTES);
	}
}
