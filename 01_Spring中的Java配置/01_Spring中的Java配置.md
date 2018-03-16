### Spring4.x与SpringBoot都推荐使用Java配置
* xml配置：将bean的信息配置在xml配置文件中
* 注解配置：在对应的bean上使用注解将该bean添加到ioc容器中
* Java配置：可以完全替代xml配置。（推荐）
    * @Configuration声明当前类是一个配置类，相当于Spring的一个配置文件。
    * @Bean注解在方法上，声明当前方法的返回值是一个Bean。

### Java配置示例
#### 0，Maven配置：pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.zyf</groupId>
    <artifactId>PreSpringBoot01</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--定义属性-->
    <properties>
        <spring-framework.version>4.3.8.RELEASE</spring-framework.version>
    </properties>

    <!--导入依赖库-->
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>${spring-framework.version}</version>
        </dependency>
    </dependencies>

    <!--设置编译级别-->
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <!--指定java版本-->
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```
#### 1，创建类FunctionService

```java
//@Service 这里没有使用注解
public class FunctionService {

	public String fun(String str){
		return "愉快：" + str;
	}
}
```

#### 2，创建类UserFunctionService

```java
public class UseFunctionService {

//	@Autowired 这里也没有使用装配注解
	private FunctionService functionService;

	public void setFunctionService(FunctionService functionService) {
		this.functionService = functionService;
	}

	public String fun(String str){
		return functionService.fun(str);
	}
}
```

#### 3，创建Java配置类：JavaConfig

```java
@Configuration//表示当前类是一个配置类
public class JavaConfig {

	@Bean//表示该方法会返回一个bean，bean的名称就是方法名
	public FunctionService functionService(){
		//自己创建一个FunctionService对象并返回
		return new FunctionService();
	}

	@Bean//表示该方法会返回一个bean
	public UseFunctionService useFunctionService(){
		//自己构建一个UserFunctionService对象并返回
		UseFunctionService useFunctionService = new UseFunctionService();
		//直接调用functionService注入functionService对象
		useFunctionService.setFunctionService(functionService());
		return useFunctionService;
	}

	/**
	 * spring容器提供的功能，只要容器中存在某个bean，就可以在另一个bean的方法声明中使用参数注入
	 * @param functionService
	 * @return
	 */
	@Bean//表示该方法会返回一个bean
	public UseFunctionService useFunctionService(FunctionService functionService){
		//自己构建一个UserFunctionService对象并返回
		UseFunctionService useFunctionService = new UseFunctionService();
		//直接调用将参数functionService对象注入
		useFunctionService.setFunctionService(functionService);
		return useFunctionService;
	}

}
```

#### 4，测试

```java
public class Main {
	public static void main(String[] args) {
		//原来创建一个ClassPath...是传入一个spring配置文件的路径
		//现在使用java配置，是传入一个java类对象
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(JavaConfig.class);
		UseFunctionService useFunctionService = context.getBean(UseFunctionService.class);
		System.out.println(useFunctionService.fun("哈哈哈哈哈哈"));

		//关闭容器
		context.close();
	}
}
```

#### 5，测试结果
![](https://ws2.sinaimg.cn/large/006tNc79gy1foyc3uh9w3j309p01caa2.jpg)
    


