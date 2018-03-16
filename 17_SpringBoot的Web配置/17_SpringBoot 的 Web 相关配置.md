### 17，SpringBoot 的 Web 相关配置
#### 1，SpringBoot 提供的自动配置 （了解）
* 自动配置 `ViewResolver`
    * `ContentNegotiatingViewResolver` 是 SpringMVC 提供的一个特殊的 `ViewResolver` 类处理不同的 `View` ，它不自己处理 `View` ，而是代理给不同的 `ViewResolver` 来处理不同的 `View` （类似于 `DispatcherServlet` ），所以它具有最高的优先级。
    
    * `BeanNameViewResolver` ：控制器（ `@Controller` ）中的一个方法的返回值（例如：视图的名称为 `index` ）会根据 `BeanNameViewResolver` 去查找名称为 `index` 的 Bean，找到对应的 `View`
    
    * `InternalResourceResolver` ：设置前缀，后缀
    
* 自动配置的静态资源，在自动配置类的 `addResourceHandlers` 方法中定义了以下静态资源的自动配置
    * 类路径文件：把类路径下的 `/static` `/public` `/resources` 和 `/META-INF/resources` 文件夹下的静态文件直接映射为 `/**` ，这样就可以直接通过 `http://localhost:8080/**` 访问
    * webjar：将我们常用的脚本框架封装在 jar 包中的 jar 包。把 webjar 的 `/META-INF/resources/webjars/` 下的静态文件映射为 `/webjar/**` ，这样就可以直接通过 `http://localhost:8080/webjar/**` 访问

* 自动配置的 `Formatter` 和 `Converter`
    * 只要我们定义了 `Converter` 、 `GenericConverter` 、 `Formatter` 接口的实现类的 Bean ，这些Bean就会自动注册到 SpringMVC 中

* 自动配置的 `HttpMessageConverters`
    
* 静态首页的支持
    * 如果把静态 `index.html` 文件放置在如下目录
    * `classpath:/META-INF/resources/index.html`
    * `classpath:/resources/index.html`
    * `classpath:/static/index.html`
    * `classpath:/public/index.html`
    * 当我们访问应用根目录 `http://localhost:8080/` 时会直接映射

#### 2，接管 SpringBoot 的 Web 配置
* 通常情况下，SpringBoot的自动配置，是符合我们大多数需求的。
* 如果SpringBoot提供的SpringMVC默认配置不符合我们的需求，则可以通过一个配置类（ `@Configuration` 的类），加上 `@EnableWebMvc` 注解来实现完全自己控制的MVC配置。
* 如果在使用自己的MVC配置时，还要保留SpringBoot的默认配置，可以定义一个配置类，继承 `WebMvcConfigureAdapter` ，无需使用 `@EnableWebMvc` 注解（SpringMVC文档中有）


```java
package com.zyf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by zyf on 2018/3/7.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
	   //并不会覆盖WebMvcAutoConfiguration中的addViewController方法
	   //WebMvcAutoConfiguration中会把/index指向index.html
		registry.addViewController("/index").setViewName("/index");
	}
}

```

#### 3，注册Servlet、Filter、Listener
* 当使用嵌入式Servlet容器 （ Tomcat 或 Jetty 等）时，通过将 Servlet、Filter 和 Listener 声明为 Spring Bean 而达到注册的效果；
* 或者注册 ServletRegistrationBean，FilterRegistrationBean 和 ServletListenerRegistrationBean 的 Bean

##### 直接注册 Bean 示例

```java
public class DemoFilter implements Filter{
}

import java.util.EventListener;
public class DemoListener implements EventListener{
}

public class DemoServlet extends HttpServlet {
}

```

```java
	@Bean
	public DemoServlet demoServlet(){
		return new DemoServlet();
	}
	
	@Bean
	public DemoFilter demoFilter(){
		return new DemoFilter();
	}
	
	@Bean
	public DemoListener demoListener(){
		return new DemoListener();
	}
```

##### 通过 `RegistrationBean` 示例

```java
	@Bean
	public ServletRegistrationBean servletRegistrationBean(){
															//路径
		return new ServletRegistrationBean(new DemoServlet(),"/demo/*");
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new DemoFilter());
		//spring boot 会按照order值的大小，从小到大的顺序来依次过滤
		registrationBean.setOrder(2);
		return registrationBean;

	}

	@Bean
	public ServletListenerRegistrationBean<DemoListener> demoListenerServletListenerRegistrationBean(){
		return new ServletListenerRegistrationBean<>(new DemoListener());
	}
```

#### 3，对 Servlet 容器的配置
* 只需要在 `application.properties` 中进行配置即可
* 通用的 Servlet 容器的配置都以 `server` 为前缀
* Tomcat 特有的配置都以 `server.tomcat` 为前缀

##### 配置 Servlet 容器

```prop

#配置程序端口号，默认为8080
server.port=9090

#用户会话session的过期时间，以秒为单位
server.session.timeout=60

#配置主页访问路径，默认为/
server.context-path=/springboot06

```

##### 配置 Tomcat

```prop
#配置Tomcat编码，默认为 UTF-8
server.tomcat.uri-encoding=UTF-8

```

#### 4，代码配置 Tomcat
* 如果要通过代码的方式配置 Servlet 容器，则可以注册一个实现了 `EmbeddedServletContainerCustomizer` 接口的 Bean；
* 若想直接配置 Tomcat 、 Jetty、Undertow 则可以直接定义 `TomcatEmbeddedServletContainerFactory` 、 `JettyEmbeddedServletContainerFactory` 、 `UndertowEmbeddedServletContainerFactory`

##### 1，通用配置
**新建类的配置**

```java
package com.zyf.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by zyf on 2018/3/7.
 */
@Component
public class CustomServletContainer implements EmbeddedServletContainerCustomizer {
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		//配置端口号
		container.setPort(8888);

		//如果出现404错误，则显示404.html页面
		container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/404.html"));

		//设置session过期时间为10分钟
		container.setSessionTimeout(10, TimeUnit.MINUTES);
	}
}

```

**在配置文件中将定义静态内部配置类**

```java
	@Component
	public static class CustomServletContainer implements EmbeddedServletContainerCustomizer{

		@Override
		public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
			
		}
	}

```

##### 2，特定的配置
* 以 Tomcat 为例，只需在配置类 `Springboot06Application` 中添加以下代码

```java
	@Bean
	public EmbeddedServletContainerFactory servletContainer(){
		TomcatEmbeddedServletContainerFactory factory = 
				new TomcatEmbeddedServletContainerFactory();
		factory.setPort(8888);
		factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/404.html"));
		factory.setSessionTimeout(10, TimeUnit.MINUTES);
		return factory;
	}
```

#### 5，替换 Tomcat
* SpringBoot 默认使用 `Tomcat` 作为内嵌的 Servlet 容器
* 如果要使用 `Jetty` 或者 `Undertow` 作为 Servlet 容器，只需修改 `starter-web` 依赖即可

##### 替换为 `Jetty`

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-thymeleaf</artifactId>
  <exclusions>
      <exclusion>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-tomcat</artifactId>
      </exclusion>
  </exclusions>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

##### 替换为 `Undertow`

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-thymeleaf</artifactId>
  <exclusions>
      <exclusion>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-tomcat</artifactId>
      </exclusion>
  </exclusions>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-undertow</artifactId>
</dependency>
```



