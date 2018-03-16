### 27_Spring Security 安全控制
* 什么是 `Spring Security`
    * `Spring Security` 是专门针对基于 `Spring` 的项目的安全框架，充分利用了依赖注入和 AOP 来实现安全的功能。
    * 安全框架有两个重要的概念，认证（Authentication）和授权（Authorization）
    * 认证：确认用户可以访问当前系统
    * 授权：确定用户在当前系统下所拥有的功能权限

* Spring Boot 的支持
    * Spring Boot 针对 `Spring Security` 的自动配置在 `org.xxx.boot.autoconfigure.security` 包中
    * 主要通过 `SecurityAutoConfiguration` 和 `SecurityProperties` 来完成配置。
    * `SecurityAutoConfiguration` 导入了 `SpringBootWebSecurityConfiguration` 中的配置，在 `SpringBootWebSecurityConfiguration` 配置中，会帮我们做如下自动配置
        * 自动配置了一个内存中的用户，账户为 `user` ，密码在程序启动时出现
        * 忽略 `/css/**` 、`/js/**` 、`/images/**` 和 `/**/favicon.ico` 等静态文件的拦截
        * 自动配置的 `securityFilterChainRegistration` 的Bean

* 在 `application.properties` 文件中通过 `security` 为前缀的属性来配置 `Spring Security`

* 如果要扩展配置，只需配置类继承 `WebSecurityConfigurerAdapter` 即可，无需 `@EnableWebSecurity` 注解

#### 1，创建SpringBoot项目
依赖：JPA，Thymeleaf，Security，Oracle驱动
添加Thymeleaf对Security的支持

```xml
        <dependency>
            <groupId>org.thymleaf.extras</groupId>
            <artifactId>thymeleaf-extras-springsecurity4</artifactId>
        </dependency>
```

#### 2，配置 `application.properties`
```xml
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=root
spring.datasource.password=123

logging.level.org.springframework.security = INFO
spring.thymeleaf.cache=false
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

#### 3，将 bootstrap.min.css 放在 `src/main/resources/static/css` 下，此路径默认不拦截
#### 4，用户和角色
使用JPA来定义用户和角色

**用户**

```java
package com.zyf.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zyf on 2018/3/14.
 * 实现UserDetails接口后，Spring Security就可以管理SysUser了
 */
@Entity
public class SysUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	private String username;
	private String password;

	/**
	 * cascade = {CascadeType.REFRESH}：级联刷新，获取User时也获取Role
	 * fetch = FetchType.EAGER：关闭懒加载
	 */
	@ManyToMany(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)//配置用户和角色的多对多关系
	private List<SysRole> roles;


	/**
	 * 可以在该方法中，根据自定义逻辑，将用户权限（角色）添加到auths中
	 * @return
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<>();
		List<SysRole> roles = this.getRoles();
		for (SysRole role : roles) {

			auths.add(new SimpleGrantedAuthority(role.getName()));
		}
		return auths;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * 账号是否不过期，false则验证不通过
	 * @return
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 账号是否不锁定，false则验证不通过
	 * @return
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 凭证是否不过期，false则验证不通过
	 * @return
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 该账号是否启用，false为验证不通过
	 * @return
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	public List<SysRole> getRoles() {
		return roles;
	}

	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}
}

```

**角色**

```java
package com.zyf.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by zyf on 2018/3/14.
 * 角色
 */
@Entity
public class SysRole {

	@GeneratedValue
	@Id
	private Long id;

	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

```

#### 5，数据结构及初始化
* 当我们配置用户和角色的多对多关系后，通过设置 `spring.jpa.hibernate.ddl-auto=update` 为我们自动生成用户表：SYS_USER，角色表：SYS_ROLE，关联表：SYS_USER_ROLE
* 针对上面的表结构，我们初始化一些数据来方便我们演示。
* 在 `src/main/resources` 下，新建 `data.sql` 如下


```sql
INSERT INTO SYS_USER (id,username,password) VALUES (1,'zyf','123');
INSERT INTO SYS_USER (id,username,password) VALUES (2,'change','456');

INSERT INTO SYS_ROLE (id,name) VALUES (1,'ROLE_ADMIN');
INSERT INTO SYS_ROLE (id,name) VALUES (2,'ROLE_USER');

-- zyf是admin
INSERT INTO SYS_USER_ROLES(SYS_USER_ID,ROLES_ID) VALUES (1,1);
-- change是user
INSERT INTO SYS_USER_ROLES(SYS_USER_ID,ROLES_ID) VALUES (2,2);
```

#### 6，传值对象
用来测试不同角色用户的数据展示（Admin与User看到的数据不同）

```java
package com.zyf.domain;

/**
 * Created by zyf on 2018/3/14.
 */
public class Msg {
	private String title;
	private String content;
	private String etraInfo;

	public Msg(String title, String content, String etraInfo) {
		this.title = title;
		this.content = content;
		this.etraInfo = etraInfo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEtraInfo() {
		return etraInfo;
	}

	public void setEtraInfo(String etraInfo) {
		this.etraInfo = etraInfo;
	}
}

```

#### 7，数据访问Repository

```java
package com.zyf.dao;

import com.zyf.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zyf on 2018/3/14.
 */
public interface SysUserRepository extends JpaRepository<SysUser,Long> {

	/**
	 * 根据用户名查找用户
	 * @param username
	 * @return
	 */
	SysUser findByUsername(String username);
}

```

#### 8，Service层

```java
package com.zyf.service;


import com.zyf.dao.SysUserRepository;
import com.zyf.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by zyf on 2018/3/14.
 */
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private SysUserRepository sysUserRepository;
	
	/**
	 * 重写loadUserByUsername方法
	 * 根据用户名获得用户
	 * @param s 用户名
	 * @return 自定义的SysUser实现了UserDetails，所以可以直接返回
	 * @throws UsernameNotFoundException 异常都帮我们封装好了
	 */
	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		SysUser user = sysUserRepository.findByUsername(s);
		if (user == null){
			throw new UsernameNotFoundException("用户名不存在");
		}
		return user;
	}
}

```

#### 9，Spring MVC 配置

```java
package com.zyf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by zyf on 2018/3/14.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//访问/login转向到login.html页面
		registry.addViewController("/login").setViewName("login");
	}
}

```

#### 10，Spring Security 配置

```java
package com.zyf;

import com.zyf.service.UserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by zyf on 2018/3/14.
 * 扩展Spring Security 需配置 WebSecurityConfigurerAdapter
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public UserDetailsService userDetailServiceImpl(){
		return new UserDetailServiceImpl();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		//添加我们自定义的 user detail service认证
		auth.userDetailsService(userDetailServiceImpl());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.anyRequest().authenticated()//所有请求都需要认证（登陆后才能访问）
				.and()
				.formLogin()
				.loginPage("/login")//定制登录行为，登录页面可以任意访问
				.failureUrl("/login?error")
				.permitAll()//失败页面可以任意访问
				.and()
				.logout().permitAll();//定制注销行为，注销请求可以任意访问
		//permitAll：赋予任意全新啊
		//failureUrl：指定失败页面
	}
}

```

#### 11，login.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta content="text/html;charset=UTF-8"/>
<title>登录页面</title>
<link rel="stylesheet" th:href="@{css/bootstrap.min.css}"/>
<style type="text/css">
	body {
  padding-top: 50px;
}
.starter-template {
  padding: 40px 15px;
  text-align: center;
}
</style>
</head>
<body>
	
	 <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <a class="navbar-brand" href="#">Spring Security演示</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
           <li><a th:href="@{/}"> 首页 </a></li>
           
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>
     <div class="container">

      <div class="starter-template">
       <p th:if="${param.logout}" class="bg-warning">已成功注销</p><!-- 注销成功后显示 -->
			<p th:if="${param.error}" class="bg-danger">有错误，请重试</p> <!-- 登录失败时显示 -->
			<h2>使用账号密码登录</h2>
            <!-- 自Spring Security 4.x开始默认的登录路径为/login -->
			<form name="form" th:action="@{/login}" action="/login" method="POST"> 
				<div class="form-group">
					<label for="username">账号</label>
					<input type="text" class="form-control" name="username" value="" placeholder="账号" />
				</div>
				<div class="form-group">
					<label for="password">密码</label>
					<input type="password" class="form-control" name="password" placeholder="密码" />
				</div>
				<input type="submit" id="login" value="Login" class="btn btn-primary" />
			</form>
      </div>

    </div>
		
</body>
</html>
```

#### 12，home.html

```java
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" 
	  xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity4"><!-- thymeleaf提供的 Spring Security的标签支持 -->
<head>
<meta content="text/html;charset=UTF-8"/>
<title sec:authentication="name"></title> <!-- 通过sec:authentication="name"获得当前登录用户的用户名 -->
<link rel="stylesheet" th:href="@{css/bootstrap.min.css}" />
<style type="text/css">
body {
  padding-top: 50px;
}
.starter-template {
  padding: 40px 15px;
  text-align: center;
}
</style>
</head>
<body>
	 <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <a class="navbar-brand" href="#">Spring Security演示</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
           <li><a th:href="@{/}"> 首页 </a></li>
           
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>
    
     <div class="container">

      <div class="starter-template">
      	<h1 th:text="${msg.title}"></h1>
		
		<p class="bg-primary" th:text="${msg.content}"></p>
          <!-- sec:authorize="hasRole('ROLE_ADMIN')" -->
          <!-- 表示只有当前用户角色为 ROLE_ADMIN 时，才可现实标签内容-->
		<div sec:authorize="hasRole('ROLE_ADMIN')">
		 	<p class="bg-info" th:text="${msg.etraInfo}"></p>
		</div>

          <!-- sec:authorize="hasRole('ROLE_USER')" -->
          <!-- 表示只有当前用户角色为 ROLE_USER 时，才可现实标签内容-->
		<div sec:authorize="hasRole('ROLE_USER')"> <!-- 4-->
		 	<p class="bg-info">无更多信息显示</p>
		</div>	

          <!--注销默认的路径为 /logout 需通过 POST 请求提交-->
        <form th:action="@{/logout}" method="post">
            <input type="submit" class="btn btn-primary" value="注销"/>
        </form>
      </div>

    </div>
    
</body>
</html>
```

#### 13，控制器


```java
package com.zyf.web.controller;

import com.zyf.domain.Msg;
import com.zyf.domain.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zyf on 2018/3/14.
 */
@Controller
public class HomeController {

	@RequestMapping("/")
	public String index(Model model){
		//在controller中获得当前登录的用信息
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth =
				context.getAuthentication();
		SysUser user = (SysUser) auth.getPrincipal();
		System.out.println(user.getRoles().get(0).getName());
		Msg msg = new Msg("我是标题","我是内容","额外信息，只有管理员可看到");
		model.addAttribute("msg",msg);
		return "home";
	}
}

```

#### 14，运行测试查看控制台输出

#### 15，页面有错误，设计到Thymeleaf语法，未做修改

