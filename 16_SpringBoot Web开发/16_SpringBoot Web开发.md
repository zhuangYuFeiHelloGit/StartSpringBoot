### 16_SpringBoot Web开发
* Web开发的核心内容是内嵌Servlet容器和SpringMVC
* SpringBoot中是内嵌tomcat的
* jetty也是Servlet容器

#### 1，SpringBoot的Web开发支持
* SpringBoot提供了 `spring-boot-starter-web` 为 Web 开发予以支持，其中为我们提供了内嵌的 `Tomcat` 和 `SpringMVC` 的依赖
* Web的相关配置被放在 `spring-boot-autoconfigure.jar` 的 `org.springframework.boot.autoconfigure.web` 下

#### 2，Thymeleaf 模板引擎
* Thymeleaf 是一个Java类库，是一个 `xml/xhtml/html5` 的模板引擎，可以作为 MVC 的 Web 应用的 View 层
* Thymeleaf 提供了额外的模块与 SpringMVC 集成，使用我们可以使用 Thymeleaf 完全替代 JSP


#### 3，与 SpringMVC 集成
* 在SpringMVC中，若要集成一个模板引擎的话，需要定义 `ViewResolver` ，而 `ViewResolver` 需要定义一个 `View` ，我们为JSP定义的 `ViewResolver` 如下所示：

```java
	@Bean
	public InternalResourceViewResolver viewResolver(){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

		viewResolver.setPrefix("/WEB-INF/classes/views/");
		viewResolver.setSuffix(".jsp");
        
     //使用JstlView定义了一个InternalResourceViewResolver
		viewResolver.setViewClass(JstlView.class);

		return viewResolver;
	}
```
* `Thymeleaf` 为我们定义好了 `ThymeleafView` 和 `ThymeleafViewResolver` （默认使用 `ThymeleafView`）

* 而使用SpringBoot，会为我们对 `Thymeleaf` 进行自动配置

#### 4，示例
* 选择 `Thymeleaf` 依赖，其中会包含 `web` 依赖    

![](https://ws4.sinaimg.cn/large/006tNc79gy1fp4ckj68ebj3110094ab9.jpg)


##### 1，创建Person类

```java
package com.zyf.domain;

/**
 * Created by zyf on 2018/3/7.
 * 用来在模板页面展示数据用
 */
public class Person {
	private String name;
	private Integer age;

	public Person() {
	}

	public Person(String name, Integer age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}

```

##### 2，引入 `Bootstrap` 和 `jQuery` 
![](https://ws2.sinaimg.cn/large/006tNc79gy1fp4dvomdg2j30k00mmacr.jpg)

* `static` 目录中存放静态资源（脚本样式，图片等）
* `templates` 目录中存放页面

##### 3，在 `templates` 下创建 `index.html` 页面
* 通过 `xmlns:th="http://www.thymeleaf.org"` 命名空间，将静态页面转换为动态页面
* 要进行动态处理的元素，需要使用 `th:` 为前缀（也就是要使用 `thymeleaf` 的语法）
* 通过 `@{}` 引用 Web 静态资源，这个路径是相对于 `resources/static/` 的
* 通过 `@{}` 访问 `model` 中的数据 （与 JSP 类似）
* 注意：需要动态处理的内容，需要通过命名空间 `th:` 使用 `thymeleaf` 语法

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="x-ua-compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link th:href="@{bootstrap/css/bootstrap.min.css}" rel="stylesheet"/>
    <link th:href="@{bootstrap/css/bootstrap-theme.min.css}" rel="stylesheet"/>
    <title>Title</title>
</head>
<body>
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">访问model</h3>
        </div>

        <div class="panel-body">
            <span th:text="${singlePerson.name}"></span>
        </div>
    </div>

    <div th:if="${not #lists.isEmpty(people)}">
        <div class="panel panel-primary">

            <div class="panel-heading">
                <h3 class="panel-title">列表</h3>
            </div>

            <div class="panel-body">
                <ul class="list-group">
                    <!--people是model中的属性，是一个集合-->
                    <!--person是每次集合时，对应的元素（增强for循环）-->
                    <li class="list-group-item" th:each="person:${people}">
                        <span th:text="${person.name}"></span>
                        <span th:text="${person.age}"></span>
                        <button class="btn" th:onclick="'showName(\''+${person.name}+'\')'">获得名字</button>
                    </li>
                </ul>
            </div>
        </div>

    </div>

    <script th:src="@{jquery-3.2.1.min.js}" type="text/javascript"></script>
    <script th:src="@{bootstrap/js/bootstrap.min.js}"></script>

    <script th:inline="javascript">
        var single = [[${singlePerson}]];
        console.log(single.name + "/" + single.age);

        function showName(name) {
            console.log(name);
        }
    </script>
</body>
</html>
```

##### 4，数据准备

```java
package com.zyf;

import com.zyf.domain.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@SpringBootApplication
public class Springboot06Application {

	@RequestMapping("/")
	public String index(Model model){
		Person single = new Person("abc",123);

		List<Person> people = new ArrayList<>();
		Person p1 = new Person("x",11);
		Person p2 = new Person("y",22);
		Person p3 = new Person("z",33);

		people.add(p1);
		people.add(p2);
		people.add(p3);

		model.addAttribute("singlePerson",single);
		model.addAttribute("people",people);

		return "index";
	}

	public static void main(String[] args) {
		SpringApplication.run(Springboot06Application.class, args);
	}
}

```

##### 5，运行演示
![](https://ws3.sinaimg.cn/large/006tNc79gy1fp4e0dggbqj318i0pstap.jpg)

##### 6，控制台输出
![](https://ws3.sinaimg.cn/large/006tNc79gy1fp4e1ojkjuj30dc0aa74v.jpg)


