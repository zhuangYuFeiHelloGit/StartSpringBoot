### 15_SpringBoot 自定义starter pom
我们在 `pom.xml` 中的那些springboot依赖，就是starter

目的：当某个类存在的时候，自动配置这个类的Bean，并可将Bean的书写在 `application.properties` 中配置

* 其实就相当于在学习java的时候，将一个项目打成jar包，然后在另一个项目中使用它。

#### 1，创建一个Maven项目

![](https://ws4.sinaimg.cn/large/006tNc79gy1fp44cpxkdqj31ka0i27ao.jpg)

![](https://ws1.sinaimg.cn/large/006tNc79gy1fp48l5vc0bj31js07e0tq.jpg)

#### 2，修改pom.xml文件

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.zyf</groupId>
    <artifactId>spring-boot-starter-demo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>spring-boot-starter-demo</name>
    <url>http://maven.apache.org</url>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>

        <!--添加自动配置的依赖-->
        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-autoconfigure -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
            <version>1.5.9.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>3.8.1</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>

```

#### 3，添加属性配置（类型安全的属性配置实际上就是一个类）

```java
package com.zyf.spring_boot_stater_demo;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by zyf on 2018/3/7.
 */
//在配置文件中通过：demo.msg=xxx来配置msg
//若不设置，则默认为：我是DEMO
@ConfigurationProperties(prefix = "demo")
public class DemoProperties {
	public static final String MSG = "我是DEMO";

	private String msg = MSG;

	public static String getMSG() {
		return MSG;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

```

#### 4，创建一个类DemoService，作为判断依据类
根据这个类是否存在，来决定是否创建这个类的Bean

```java
package com.zyf.spring_boot_stater_demo;

import org.springframework.stereotype.Service;

/**
 * Created by zyf on 2018/3/7.
 */
//当进行自动配置时，判断DemoService是否存在，若存在，则创建这个类的Bean
public class DemoService {
	private String msg;

	public String say(){
		return "hello：" + msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

```

#### 5，若想我们自己定义的自动配置可以生效，需要将该类注册到 `spring.factories` 文件中

![](https://ws3.sinaimg.cn/large/006tNc79gy1fp48q5e9tmj30l00fc76c.jpg)

**注册文件中的内容：**

```prop
org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.zyf.DemoServiceAutoConfiguration
```

#### 6，将该starter，安装到本地maven库
![](https://ws4.sinaimg.cn/large/006tNc79gy1fp48sege04j30ne0m677k.jpg)

#### 7，安装成功
![](https://ws2.sinaimg.cn/large/006tNc79gy1fp48ss0lwbj313e0dwgot.jpg)

#### 8，创建一个SpringBoot项目，将自定义的starter导入pom

```xml
<dependencies>
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-web</artifactId>
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

   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-test</artifactId>
       <scope>test</scope>
   </dependency>

   <!--自定义的maven依赖-->
   <dependency>
       <groupId>com.zyf</groupId>
       <artifactId>spring-boot-starter-demo</artifactId>
       <version>1.0-SNAPSHOT</version>
   </dependency>
</dependencies>
```

#### 9，在右侧Maven Projects查看

![](https://ws2.sinaimg.cn/large/006tNc79gy1fp48vc9hxnj30q20b0ta7.jpg)

#### 10，一个非常简单的运行类

```java
package com.zyf;

import com.zyf.spring_boot_stater_demo.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Springboot05Application {


	@Autowired
	DemoService demoService;

	@RequestMapping("/")
	public String index(){
		return demoService.say();
	}

	public static void main(String[] args) {
		SpringApplication.run(Springboot05Application.class, args);
	}
}

```

#### 11，演示
可以看到是默认的
![](https://ws4.sinaimg.cn/large/006tNc79gy1fp48wkkgq6j30d607ot9a.jpg)

#### 12，在配置文件中设置debug=true可以查看到自动配置相关信息
可以看到自定义的 `DemoServiceAutoConfiguration` 已经自动配置了
![](https://ws2.sinaimg.cn/large/006tNc79gy1fp48zqyrvnj31d807cwfo.jpg)

#### 13，在配置文件中添加demo.msg配置

```prop
debug=true
demo.msg="everybody is good , that's real good!"
```

#### 14，运行查看
![](https://ws4.sinaimg.cn/large/006tNc79gy1fp4987si68j30ho04swey.jpg)

