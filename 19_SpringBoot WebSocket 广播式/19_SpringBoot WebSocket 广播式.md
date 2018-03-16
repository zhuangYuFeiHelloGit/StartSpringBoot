### 19_SpringBoot WebSocket 广播式
* WebSocket 是为浏览器和服务端提供双工异步通信的功能
* 浏览器可以向服务端发送消息，服务端也可以向浏览器发送消息
* WebSocket 需要浏览器的支持（现在的浏览器基本上都支持）
* WebSocket 通过一个 socket 来实现双工异步通信能力
* 直接使用 WebSocket 开发比较繁琐，可以直接使用它的子协议 STOMP 它是一个更高级别的协议
* STOMP 使用一个基于桢的格式来定义消息，与 HTTP 的 request response 类似

#### 1，SpringBoot 通过的自动配置
* 创建项目，选择 `WebSocket` `Thymeleaf` 两个依赖
* 什么是广播式：当服务端有消息时，会将消息发送给所有连接了当前 `endpoint` 的浏览器
* 配置WebSocket，需要在配置类上使用 `@EnableWebSocketMessageBroker`
* 或者通过继承 `AbsstractEnableWebSocketMessageBrokerConfigurer` 类，重写方法来配置 `WebSocket`

##### 创建配置类
```java
package com.zyf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Created by zyf on 2018/3/8.
 */
@Configuration
@EnableWebSocketMessageBroker//开启使用STOMP，用来传输基于代理的消息，此时控制器支持使用
//@messageMapping 就像使用 @RequestMapping 一样
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {

		//注册STOMP 协议的节点（endpoint） 并映射到指定的URL
		//注册一个 STOMP 的 endpoint ，并指定使用 SockJS协议 
		stompEndpointRegistry.addEndpoint("/endpointZhuang").withSockJS();

	}

	/**
	 * 配置消息代理
	 * @param registry
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		
		//广播式应配置一个 /topic 的消息代理
		registry.enableSimpleBroker("/topic");
	}
}

```

##### 创建实体数据类bean

```java
package com.zyf.domain;

/**
 * Created by zyf on 2018/3/8.
 * 浏览器向服务端发送的消息，用此类接收
 */
public class ZyfMessage {
	private String name;

	public String getName() {
		return name;
	}
}

```

```java
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

```

##### 创建控制器

```java
package com.zyf.controller;

import com.zyf.domain.ZyfMessage;
import com.zyf.domain.ZyfResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by zyf on 2018/3/8.
 */
@Controller
public class ZyfController {

	@MessageMapping("/welcome")//当浏览器向服务端发送请求时，通过@MessageMapping映射/welcome这个地址
	//类似于 @RequestMapping
	@SendTo("/topic/getResponse")//当服务端有消息时，会对订阅了 @SendTo 中的路径的浏览器发送消息
	public ZyfResponse say(ZyfMessage zyfMessage) throws InterruptedException {
		Thread.sleep(3000);
		return new ZyfResponse("Welcome, "+zyfMessage.getName()+"!");

	}

}

```

##### 添加脚本
* `stomp.min.js` STOMP协议的客户端脚本
* `sockjs.min.js` SockJS的客户端脚本
* `jquery.js`

![](https://ws4.sinaimg.cn/large/006tNc79gy1fp5efx271jj30gk0kqmz5.jpg)

##### 创建演示页面

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8" />
    <title>Spring Boot+WebSocket+广播式</title>

</head>
<body onload="disconnect()">
<noscript><h2 style="color: #ff0000">貌似你的浏览器不支持websocket</h2></noscript>
<div>
    <div>
        <button id="connect" onclick="connect();">连接</button>
        <button id="disconnect" disabled="disabled" onclick="disconnect();">断开连接</button>
    </div>
    <div id="conversationDiv">
        <label>输入你的名字</label><input type="text" id="name" />
        <button id="sendName" onclick="sendName();">发送</button>
        <p id="response"></p>
    </div>
</div>
<!--会自动导入 src/main/resources/static 下的静态资源-->
<script th:src="@{sockjs.min.js}"></script>
<script th:src="@{stomp.min.js}"></script>
<script th:src="@{jquery.js}"></script>
<script type="text/javascript">
    var stompClient = null;

    //设置是否连接
    function setConnected(connected) {
        document.getElementById('connect').disabled = connected;
        document.getElementById('disconnect').disabled = !connected;
        document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
        $('#response').html();
    }

    //自定义一个方法，在里面实现连接逻辑
    function connect() {
        //连接SockJS的endpoint
        //连哪个节点？传入参数确定（是在WebSocketConfig中定义的节点）
        var socket = new SockJS('/endpointZhuang');

        //使用STOMP子协议的WebSocket客户端
        stompClient = Stomp.over(socket);

        //连接WebSocket服务端
        stompClient.connect({}, function(frame) {
            setConnected(true);
            console.log('Connected: ' + frame);

            //通过stompClient.subscribe 订阅 /topic/getResponse (目标) 发送的消息
            //这个是在控制器的 @SendTo 中定义的
            stompClient.subscribe('/topic/getResponse', function(respnose){
                showResponse(JSON.parse(respnose.body).responseMessage);
            });
        });
    }
	
	
    function disconnect() {
        if (stompClient != null) {
            stompClient.disconnect();
        }
        setConnected(false);
        console.log("Disconnected");
    }

    function sendName() {
        var name = $('#name').val();
        //通过stompClient.send 向 /welcome （目标） 发送消息，
        // 这个是在控制器的 @MessageMapping中定义的
        stompClient.send("/welcome", {}, JSON.stringify({ 'name': name }));
    }

    function showResponse(message) {
          var response = $("#response");
          response.html(message);
    }
</script>
</body>
</html>
```

##### 运行演示
* 效果：开启三个浏览器窗口，访问 `http://localhost:8080/zyf` 后，分别连接服务器
* 在任意一个窗口中发送消息到服务端，三秒后，三个窗口都可以接收到从服务端发送来的消息

![](https://ws3.sinaimg.cn/large/006tNc79gy1fp5g57ospoj317p0agn1i.jpg)

![](https://ws4.sinaimg.cn/large/006tNc79gy1fp5g5zmdsij317j0bhahg.jpg)

![](https://ws2.sinaimg.cn/large/006tNc79gy1fp5g76663dj317q0auwkn.jpg)

##### 在谷歌浏览器中，观察STOMP协议的桢

![](https://ws2.sinaimg.cn/large/006tNc79gy1fp5gbsp8h0j30pv0cxjtf.jpg)

![](https://ws1.sinaimg.cn/large/006tNc79gy1fp5gew1ijsj30oe030dgf.jpg)

![](https://ws4.sinaimg.cn/large/006tNc79gy1fp5ggb8esoj30xs02w753.jpg)

