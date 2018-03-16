package com.zyf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by zyf on 2018/3/2.
 */
@Service
public class DemoService {

	@Value("注入普通的字符串")
	private String another;

	public String getAnother() {
		return another;
	}

	public void setAnother(String another) {
		this.another = another;
	}
}
