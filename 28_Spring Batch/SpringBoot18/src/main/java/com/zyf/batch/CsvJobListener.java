package com.zyf.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * Created by zyf on 2018/3/15.
 */
public class CsvJobListener implements JobExecutionListener {

	long startTime;
	long endTime;


	@Override
	public void beforeJob(JobExecution jobExecution) {
		startTime = System.currentTimeMillis();
		System.out.println("任务开始");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		endTime = System.currentTimeMillis();
		System.out.println("任务结束");

		System.out.println("耗时："+(endTime - startTime) + "ms");
	}
}
