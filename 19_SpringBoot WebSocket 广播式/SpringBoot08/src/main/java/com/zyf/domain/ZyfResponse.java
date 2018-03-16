package com.zyf.domain;

/**
 * Created by zyf on 2018/3/8.
 * 服务端向浏览器发消息时，通过此类作为载体传输
 */
public class ZyfResponse {

	private String responseMessage;

	public ZyfResponse(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getResponseMessage() {
		return responseMessage;
	}
}
