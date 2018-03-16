### 自定义HttpMessageConverter
* `HttpMessageConverter` 是用来处理 `request` 和 `response` 里的数据的。
* Spring为我们内置了大量的 `HttpMessageConverter`

#### 0，创建DemoObj类，与之前的一样

#### 1，自定义HttpMessageConverter

```java
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

```

#### 2，在配置类中添加自定义的转换器

```java
	@Bean
	public MyMessageConverter messageConverter(){
		return new MyMessageConverter();
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(messageConverter());
	}
```

#### 3，在配置类中添加演示页面

```java
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/toUpload").setViewName("/upload");
		registry.addViewController("/converter").setViewName("/converter");
	}
```

#### 4，创建converter.jsp文件
![](https://ws3.sinaimg.cn/large/006tKfTcgy1fp2eh3qx1rj30am092aar.jpg)

```jsp
<%--
  Created by IntelliJ IDEA.
  User: zyf
  Date: 2018/3/6
  Time: 上午12:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>converter 演示</title>
    <script type="text/javascript" src="../assets/jquery-3.2.1.min.js"></script>
</head>
<body>
    <div id="resp">
        <button onclick="req()">点我</button>
    </div>
</body>
<script type="text/javascript">
    function req() {
        $.ajax({
            url:"convert",
            data:"1-hello",//后台会根据—分割数据
            type:"POST",
            contentType:"application/x-zyf",//这里设置的就是自定义媒体类型
            success:function(data){
                $("#resp").html(data);
            }

        });
    }
</script>
</html>

```

#### 5，部署演示
![](https://ws3.sinaimg.cn/large/006tKfTcgy1fp2elbyk1ug30go06g4qq.gif)

#### 6，浏览器调试查看
![](https://ws2.sinaimg.cn/large/006tKfTcgy1fp2emzpizoj30pe0gqdjh.jpg)


