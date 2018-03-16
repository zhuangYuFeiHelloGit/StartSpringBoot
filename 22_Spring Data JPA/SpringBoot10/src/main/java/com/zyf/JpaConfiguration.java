package com.zyf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManagerFactory;

/**
 * Created by zyf on 2018/3/12.
 */
@Configuration
@EnableJpaRepositories("com.zyf.dao")//开启对Spring Data JPA的支持
public class JpaConfiguration {

//	@Bean
//	public EntityManagerFactory entityManagerFactory(){
		//...
//	}

	//还需配置DataSource，PlatformTransactionManager等Bean
}
