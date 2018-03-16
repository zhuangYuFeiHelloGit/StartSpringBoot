package com.zyf;

import org.springframework.stereotype.Service;

/**
 * 使用注解的被拦截类
 */
@Service
public class ShowAnnotationService {

	@Action(name = "注解式拦截的add操作")
	public void add(){}
}
