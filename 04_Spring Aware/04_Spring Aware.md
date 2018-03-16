### Spring Aware
* Spring的依赖注入，使得所有的Bean感觉不到Spring容器的存在，也就是说是可以随意更换了IOC容器的，因为Bean之间的耦合度很低。

* 但有时我们需要在bean中使用spring容器给我们提供的资源（Request等等），这时我们的Bean就必须要意识到Spring容器的存在，才能调用Spring所提供的资源，这就是 **Spring Aware**

* 若使用了**Spring Aware** 则Bean将会与Spring框架耦合。

|   |   |
| --- | --- |
| BeanNameAware | 获得容器中Bean的名称 |
| BeanFactoryAware | 获得当前bean factory，这样可以调用容器的服务 |
| ApplicationContextAware* | 当前的application Context 这样可以调用容器的服务 |
| MessageSourceAware | 获得message source 这样可以获得文本信息 |
| ApplicationEventPublisherAware | 应用事件发布器，可以发布事件 |
| ResourceLoaderAware | 获得资源加载器，可以获得外部资源文件 |

### 示例
#### 1，在com.zyf包下创建aware示例.txt
![](https://ws1.sinaimg.cn/large/006tNc79gy1fp1qmir5a9j30o60a2t9q.jpg)

#### 2，在com.zyf包下创建AwareService类（bean类）

```java
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

```

#### 3，在com.zyf包下创建AwareConfig类（配置类）

```java
package com.zyf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zyf on 2018/3/5.
 */
@Configuration
@ComponentScan(value = "com.zyf")
public class AwareConfig {
}

```

#### 3，在com.zyf包下创建Main类（测试）

```java
package com.zyf;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by zyf on 2018/3/5.
 */
public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
				//未指定要加载哪个配置类
				new AnnotationConfigApplicationContext();
		//后置指定
		context.register(AwareConfig.class);
		//刷新容器，一定要刷新!
		context.refresh();
		AwareService awareService = context.getBean(AwareService.class);
		awareService.outputResource();
	}
}

```

#### 4，测试结果
![](https://ws3.sinaimg.cn/large/006tNc79gy1fp1qz4k85hj30k6068gmy.jpg)


