package com.zyf;

import org.springframework.stereotype.Service;

/**
 * Created by zyf on 2018/3/5.
 */
@Service
public class TargetService {
	private String content;

	public TargetService(String content) {
		this.content = content;
		System.out.println(content);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
