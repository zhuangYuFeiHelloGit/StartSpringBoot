package com.zyf.converter;

import com.zyf.domain.DemoObj;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by zyf on 2018/3/5.
 * 自定义HttpMessageConverter需要继承AbstractHttpMessageConverter
 */
public class MyMessageConverter extends AbstractHttpMessageConverter<DemoObj> {


	public MyMessageConverter() {
		//x-zyf 是自定义的媒体类型
		super(new MediaType("application", "x-zyf", Charset.forName("Utf-8")));
	}

	@Override
	protected boolean supports(Class<?> aClass) {
		//表示只支持DemoObj这个类
		//return DemoObj.class.isAssignableFrom(aClass);
		//返回false则不会支持任何类，要想使用，就需要返回true
		return true;
	}

	/**
	 * 重写readInternal方法
	 * 处理请求中的数据
	 *
	 * @param aClass
	 * @param httpInputMessage
	 * @return
	 * @throws IOException
	 * @throws HttpMessageNotReadableException
	 */
	@Override
	protected DemoObj readInternal(Class<? extends DemoObj> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
		//获得请求中的数据，得到字符串形式
		String temp = StreamUtils.copyToString(httpInputMessage.getBody(), Charset.forName("UTF-8"));

		//前端请求的格式是我们自己约定的
		String[] tempArr = temp.split("-");

		return new DemoObj(new Long(tempArr[0]), tempArr[1]);
	}

	/**
	 * 重写writeInternal方法
	 * 处理任何输出数据到response
	 *
	 * @param obj  要输出到response的对象
	 * @param httpOutputMessage
	 * @throws IOException
	 * @throws HttpMessageNotWritableException
	 */
	@Override
	protected void writeInternal(DemoObj obj, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
		String out = "hello：" + obj.getId() + "-" + obj.getName();
		httpOutputMessage.getBody().write(out.getBytes());
	}
}
