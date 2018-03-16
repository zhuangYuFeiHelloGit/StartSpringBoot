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
