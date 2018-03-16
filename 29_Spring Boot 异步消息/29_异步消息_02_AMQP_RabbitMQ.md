### 29_异步消息_02_AMQP_RabbitMQ
#### 1，docker下运行 `rabbitmq` 镜像为容器

* 5672 ：消息代理的端口
* 15672 ：管理界面端口

开启虚拟机对上述两个端口的端口映射后，重启虚拟机（VMware Fusion Mac版本）

![](https://ws2.sinaimg.cn/large/006tNc79gy1fpdmm9bw72j30t60gwjtu.jpg)

`docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:3-management`

![](https://ws3.sinaimg.cn/large/006tKfTcgy1fpee9ymnwvj30jx01vdft.jpg)

浏览器访问：`http://localhost:15672/`
![](https://ws4.sinaimg.cn/large/006tKfTcgy1fpeebd2m0lj31i80e476l.jpg)

#### 2，新建 Spring Boot 项目
* 依赖：AMQP（spring-boot-starter-amqp)
* `Spring Boot` 默认我们的 `Rabbit` 主机为 `localhost`、端口号为 `5672` ，所以我们无需为 `Spring Boot` 的 `application.properties` 配置 `RabbitMQ` 的连接信息

#### 3，定义要发送的信息及目的地（队列）

```java
package com.zyf;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Springboot20Application implements CommandLineRunner{

	/**
	 * Spring Boot 已经为我们配置好rabbitTemplate
	 */
	@Autowired
	RabbitTemplate rabbitTemplate;

	public static void main(String[] args) {
		SpringApplication.run(Springboot20Application.class, args);
	}

	/**
	 * 定义目的地（队列），队列名称为：zyf-queue
	 * @return
	 */
	@Bean
	public Queue zyfQueue(){
		return new Queue("zyf-queue");
	}

	@Override
	public void run(String... strings) throws Exception {
		//向对列zyf-queue发送消息
		rabbitTemplate.convertAndSend("zyf-queue","来自RabbitMQ的消息：我是AMQP的实现");
	}
}
```

#### 4，消息监听

```java
package com.zyf;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by zyf on 2018/3/16.
 */
@Component
public class Receiver {

	/**
	 * 注解@RabbitListener用来声明要监听哪个队列（目的地）
	 * @param message
	 */
	@RabbitListener(queues = "zyf-queue")
	public void receiveMessage(String message){
		System.out.println("Received <" + message + ">");
	}

}

```

#### 5，运行
![](https://ws2.sinaimg.cn/large/006tKfTcgy1fpef5g69czj30km02wt95.jpg)

![](https://ws2.sinaimg.cn/large/006tKfTcgy1fpef6f5fwbj31kw0pcjx8.jpg)


