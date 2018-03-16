### Spring 测试
* 测试是开发工作中不可或缺的部分。
* 单元测试只针对当前开发的类和方法进行测试，可以简单通过模拟依赖来实现，对运行环境没有依赖。
* 但是单元测试只能验证当前类或方法是否正常工作，而我们想要知道系统的各个部分组合在一起是否能正常工作，这就是集成测试存在的意义。
* **集成测试为我们提供了一种无须部署或运行程序来完成验证系统各部分是否正常协同工作的能力。**

### Spring TestContext FrameWork
`Spring` 通过 `TestContext FrameWork` 对集成测试提供顶级支持。它不依赖于特定的测试框架。支持Junit也支持TestNG

### 简单示例
#### 1，在pom.xml中添加Spring测试的依赖包

```xml
<!--07_新的依赖：使用Spring提供的集成测试-->
<dependency>
  <groupId>${spring-groupId}</groupId>
  <artifactId>spring-test</artifactId>
  <version>${spring-framework.version}</version>
</dependency>
```

#### 2，创建目标Bean

```java
package com.zyf;

import org.springframework.stereotype.Service;

/**
 * Created by zyf on 2018/3/5.
 */
@Service
public class TargetService {
	private String content;

	public TargetService(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}

```

#### 3，创建配置类

```java
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

```

#### 4，在test/java/com.zyf下创建测试类

```java
package com.zyf;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zyf on 2018/3/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//在junit环境下提供Spring TestContext Framework的功能
@ContextConfiguration(classes = {TestConfig.class})
//加载配置ApplicationContext,classes参数用来加载配置类
@ActiveProfiles("prod")//声明当前正在活动的profile（可以理解为环境）
public class Test {

	@Autowired//注入
	private TargetService targetService;

	@org.junit.Test
	public void prodBeanShouldInject(){
		String expected = "for production profile";

		String actual = targetService.getContent();

		//判断二者是否相等
		Assert.assertEquals(expected,actual);

	}
}

```

#### 5，测试结果
![](https://ws2.sinaimg.cn/large/006tNc79gy1fp20w0r5olj30ow0iojvo.jpg)

#### 6，将ActionProfile由prod更改为dev后的测试结果
![](https://ws2.sinaimg.cn/large/006tNc79gy1fp20wf0io1j30xa0lqago.jpg)



