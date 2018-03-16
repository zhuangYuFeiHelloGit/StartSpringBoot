package com.zyf.web.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zyf on 2018/3/15.
 */
@RestController
public class DemoController {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job importJob;

	JobParameters jobParameters;

	@RequestMapping("/imp")
	public String imp(String fileName) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
		String path = fileName +".csv";
		jobParameters = new JobParametersBuilder().addLong("time",System.currentTimeMillis())
				.addString("input.file.name",path)
				.toJobParameters();
		jobLauncher.run(importJob,jobParameters);
		return "ok";
	}
}
