package com.zyf;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by zyf on 2018/3/5.
 * 实现两个接口，获得Bean名次和资源加载的服务
 */
@Service
public class AwareService implements BeanNameAware,ResourceLoaderAware {

	/**
	 * bean的名字
	 */
	private String beanName;
	/**
	 * 资源加载器
	 */
	private ResourceLoader loader;


	@Override
	public void setBeanName(String s) {
		this.beanName = s;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.loader = resourceLoader;
	}

	public void outputResource(){
		System.out.println("Bean的名字："+beanName);
		Resource resource = loader.getResource("classpath:com/zyf/aware示例.txt");
		try {
			System.out.println("ResourceLoader加载的文件内容为："+ IOUtils.toString(resource.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
