package com.zyf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zyf on 2018/3/7.
 */
@Configuration
//开启配置属性注入
@EnableConfigurationProperties(DemoProperties.class)
@ConditionalOnClass(DemoService.class)
//判断类路径中是否有DemoService这个类
@ConditionalOnProperty(prefix = "demo",value = "enabled",matchIfMissing = true)
//默认为true
public class DemoServiceAutoConfiguration {

	/**
	 * DemoProperties会加载配置文件中的内容
	 */
	@Autowired
	private DemoProperties demoProperties;

	@Bean
	public DemoService demoService(){
		DemoService demoService = new DemoService();
		//getMsg实际上就是返回了配置文件中的内容
		//也就将配置文件中的信息，传给了demoService
		demoService.setMsg(demoProperties.getMsg());
		return demoService;
	}
}
