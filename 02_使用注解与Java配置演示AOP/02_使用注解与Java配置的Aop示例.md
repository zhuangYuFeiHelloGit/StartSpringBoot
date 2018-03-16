### 使用注解与Java配置的Aop示例
#### 0，添加所需依赖

```xml
<!--02_新的依赖-->
<!--导入spring的aop支持-->
<dependency>
  <groupId>${spring-groupId}</groupId>
  <artifactId>spring-aop</artifactId>
  <version>${spring-framework.version}</version>
</dependency>

<!--导入AspectJ依赖库（该库提供注解式切面编程）-->
<dependency>
  <groupId>org.aspectj</groupId>
  <artifactId>aspectjrt</artifactId>
  <version>1.8.13</version>
</dependency>

<dependency>
  <groupId>org.aspectj</groupId>
  <artifactId>aspectjweaver</artifactId>
  <version>1.8.13</version>
</dependency>
```
#### 1，自定义注解类：@Action

```java
/**
 * 拦截规则的注解
 */
@Target(ElementType.METHOD)//该注解使用在方法声明上
@Retention(RetentionPolicy.RUNTIME)//该注解在运行时使用
//注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在
//也就是说内存中的字节码中也包含该注解
@Documented//使用javadoc等工具生成文档时，会将该注解也加入进文档
public @interface Action {

	String name();

}
```

#### 2，用于演示注解拦截的类：ShowAnnotationService

```java
/**
 * 使用注解的被拦截类
 */
@Service
public class ShowAnnotationService {

	@Action(name = "注解式拦截的add操作")
	public void add(){}
}
```

#### 3，用于演示被方法规则拦截的类：ShowMethodService

```java
/**
 * 使用方法规则的被拦截类
 * 在add上没有声明@Action注解
 */
@Service
public class ShowMethodService {

	public void add(){}
	
}
```

#### 4，切面类，定义拦截后的操作

```java
/**
 * 切面
 */
@Aspect//声明此类是一个切面
@Component//将此机切面，放入Spring容器中，成为一个bean
public class LogAspect {

	//声明切入点
	@Pointcut("@annotation(com.zyf.Action)")
	public void annotationPointCut(){

	}

	//声明通知（建言），使用上面定义的那个切点
	@After("annotationPointCut()")
	public void after(JoinPoint joinPoint){
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		Action action = method.getAnnotation(Action.class);
		//通过反射获得注解上的属性,@action(name="我就是属性")
		System.out.println("注解式拦截："+action.name());
	}

	//声明通知（建言），直接使用拦截规则作为参数
	@Before("execution(* com.zyf.ShowMethodService.*(..))")
	public void before(JoinPoint joinPoint){
		
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		System.out.println("方法规则拦截："+method.getName());
		
	}
}
```

#### 5，Java配置的配置类

```java
@Configuration//这是一个配置类
@ComponentScan("com.zyf")//扫描哪些包中的注解
@EnableAspectJAutoProxy//开启Spring对AspectJ的支持
public class AopConfig {

	//因为我们已经在需要使用的类上，通过注解的方式声明成bean了
	//就无需再java配置类中，自定义方法返回对象了

}
```

#### 6，测试

```java
public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(AopConfig.class);
		
		ShowAnnotationService annotationService = context.getBean(ShowAnnotationService.class);
		ShowMethodService methodService = context.getBean(ShowMethodService.class);


		annotationService.add();
		methodService.add();

		context.close();
	}
}
```

#### 7，测试结果
![](https://ws4.sinaimg.cn/large/006tNc79gy1foydcx3e9vj30fy02et96.jpg)

