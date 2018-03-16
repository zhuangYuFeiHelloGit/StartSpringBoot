### 14_Spring Boot 自动配置 Http编码
* 在不使用Spring Boot时，我们是这样配置编码的


```xml
    <!--字符编码配置-->
    <filter>
        <filter-name>characterEncoding</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter
        </filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceEncoding</param-name>
            <!--设置request的编码-->
            <!--设置response的返回编码-->
            <param-value>true</param-value>
        </init-param>
    </filter>
```

* 所以自动配置需要满足两个条件：
    * 能配置 `CharacterEncodingFilter` 这个 Bean
    * 能配置 `encoding` 和 `forceEncoding` 这两个参数

#### 1，通过类型安全的配置方式，创建 `HttpEncodingProperties` 类

```java
package com.zyf.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;

/**
 * Created by zyf on 2018/3/7.
 */
@ConfigurationProperties(prefix = "spring.http.encoding")
public class HttpEncodingProperties {
	
	//定义默认编码为UTF-8
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	
	private Charset charset = DEFAULT_CHARSET;
	
	//设置forceEncoding默认为true
	private boolean force = true;

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public boolean isForce() {
		return force;
	}

	public void setForce(boolean force) {
		this.force = force;
	}
}

```

#### 2，创建配置类 `HttpEncodingAutoConfiguration`

```java
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

```

#### 3，在 `application.properties` 配置文件中配置属性

```prop
# 查看自动配置报告
debug=true
spring.http.encoding.charset=UTF-8
spring.http.encoding.force=true
```

#### 4，在 `src/main/resources` 下创建文件注册我们的配置类

![](https://ws2.sinaimg.cn/large/006tNc79gy1fp441t2k9uj30fe0g03zz.jpg)


**spring.factories文件中的内容**

```java
org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.zyf.config.HttpEncodingAutoConfiguration

```

#### 5，运行后查看控制台

![](https://ws3.sinaimg.cn/large/006tNc79gy1fp443fx8wkj31eo0hk79c.jpg)

