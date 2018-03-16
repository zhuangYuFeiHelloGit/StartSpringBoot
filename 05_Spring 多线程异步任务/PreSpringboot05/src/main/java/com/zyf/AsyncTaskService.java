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
