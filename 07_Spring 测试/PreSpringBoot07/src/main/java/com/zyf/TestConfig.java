package com.zyf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by zyf on 2018/3/5.
 */
@Configuration
public class TestConfig {

	@Bean
	@Profile("dev")
	public TargetService devTargetService(){
		//开发环境
		return new TargetService("for development profile");
	}

	@Bean
	@Profile("prod")
	public TargetService prodTargetService(){
		//生产环境
		return new TargetService("for production profile");
	}
}
