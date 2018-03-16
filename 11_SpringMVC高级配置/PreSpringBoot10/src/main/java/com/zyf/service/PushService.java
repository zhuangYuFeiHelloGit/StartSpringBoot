package com.zyf.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by zyf on 2018/3/6.
 */
@Service
public class PushService {

	/**
	 * 在PushService生成DeferredResult给控制器使用
	 */
	private DeferredResult<String> deferredResult;

	public DeferredResult<String> getAsyncUpdate() {
		deferredResult = new DeferredResult<>();
		return deferredResult;
	}

	/**
	 * 通过该注解定时更新DeferredResult
	 */
	@Scheduled(fixedDelay = 3000)
	public void refresh(){
		if(deferredResult != null){
			deferredResult.setResult(Long.toString(System.currentTimeMillis()));
		}

	}
}
