package com.zyf;

import org.springframework.stereotype.Service;

/**
 * 使用方法规则的被拦截类
 * 在add上没有声明@Action注解
 */
@Service
public class ShowMethodService {

	public void add(){}

}
