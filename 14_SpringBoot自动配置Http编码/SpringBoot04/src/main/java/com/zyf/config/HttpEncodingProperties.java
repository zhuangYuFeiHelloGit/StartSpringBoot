package com.zyf.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;

/**
 * Created by zyf on 2018/3/7.
 */
@ConfigurationProperties(prefix = "spring.http.encoding")
public class HttpEncodingProperties {

	//定义默认编码为UTF-8
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	private Charset charset = DEFAULT_CHARSET;

	//设置forceEncoding默认为true
	private boolean force = true;

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public boolean isForce() {
		return force;
	}

	public void setForce(boolean force) {
		this.force = force;
	}
}
