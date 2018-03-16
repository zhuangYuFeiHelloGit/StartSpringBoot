### 多线程
* Spring通过任务执行器(TaskExecutor)来实现多线程和并发编程。
* 使用ThreadPoolTaskExecutor可实现一个基于线程池的TaskExecutor
* 一般情况下，面对的都是异步任务，要在配置类中使用 `@EnableAsync` 开启对异步任务的支持
* 并通过在实际执行的Bean的方法中，使用 `@Async` 注解来声明其是一个异步任务

### 示例
#### 1，配置类

```java
package com.zyf;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by zyf on 2018/3/5.
 */
@Configuration
@ComponentScan(value = "com.zyf")
@EnableAsync//开启对异步任务的支持
public class TaskExecutorConfig implements AsyncConfigurer {
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		//核心线程数
		taskExecutor.setCorePoolSize(5);
		//设置池最大容量
		taskExecutor.setMaxPoolSize(10);
		//设置队列容量
		//如果池中线程已全部被使用，则将后续的任务添加到队列中等待执行
		//如果队列也满了，则新建线程
		//如果线程数已经达到池容量最大值
		//则抛出异常
		taskExecutor.setQueueCapacity(25);

		//初始化
		taskExecutor.initialize();

		return taskExecutor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {

		return null;
	}
}

```

#### 2，待执行的Bean

```java
package com.zyf;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by zyf on 2018/3/5.
 */
@Service
public class AsyncTaskService {

	/**
	 * 通过@Async注解，表示该方法是一个异步方法
	 * 如果将@Async注解标识在类的声明上，则表明这个类中所有的方法都是异步的
	 * 异步方法，会被自动注入在TaskExecutorConfig类中返回的
	 * 那个ThreadPoolTaskExecutor作为TaskExecutor
	 * @param n
	 */
	@Async
	public void executeAsyncTask(Integer n){
		System.out.println("异步任务执行："+n);
	}


	@Async
	public void executeAsyncTaskPlus(Integer n){
		System.out.println("异步任务执行+1："+(n+1));
	}
}

```

#### 3，测试类

```java
package com.zyf;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by zyf on 2018/3/5.
 */
public class AsyncMain {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext();
		context.register(TaskExecutorConfig.class);
		context.refresh();
		AsyncTaskService taskService = context.getBean(AsyncTaskService.class);
		for (int i = 0; i < 10; i++) {
			taskService.executeAsyncTask(i);
			taskService.executeAsyncTaskPlus(i);
		}

		context.close();
	}
}

```

#### 4，测试结果
![](https://ws2.sinaimg.cn/large/006tNc79gy1fp1yhduq64j30ec0hyjuu.jpg)

