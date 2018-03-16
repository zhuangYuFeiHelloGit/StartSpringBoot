package com.zyf.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.filter.OrderedCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * Created by zyf on 2018/3/7.
 */
@Configuration//是一个配置类
@EnableConfigurationProperties(HttpEncodingProperties.class)
//通过该注解开启属性注入，使用@Autowired注入
@ConditionalOnClass(CharacterEncodingFilter.class)
//条件注解：当CharacterEncodingFilter在类路径的条件下
@ConditionalOnProperty(prefix = "spring.http.encoding",value = "enable",matchIfMissing = true)
//条件注解：当spring.http.encoding=enable情况下条件符合
//如果没有设置(matchIfMissing)，则默认为true，条件也符合

public class HttpEncodingAutoConfiguration {

	@Autowired
	private HttpEncodingProperties httpEncodingProperties;

	@Bean//使用java配置的方式，在当前这个配置类中，创建一个bean
	@ConditionalOnMissingBean(CharacterEncodingFilter.class)
	//当容器中没有这个bean的时候，新建一个
	public CharacterEncodingFilter characterEncodingFilter(){
		CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
		filter.setEncoding(this.httpEncodingProperties.getCharset().name());
		filter.setForceEncoding(this.httpEncodingProperties.isForce());

		return filter;

	}
}
