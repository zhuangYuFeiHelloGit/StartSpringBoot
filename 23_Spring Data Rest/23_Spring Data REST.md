### 23_Spring Data REST

* [什么是RestFul](http://blog.csdn.net/chenxiaochan/article/details/73716617)
* [通俗的解释Rest和RestFul](https://www.zhihu.com/question/28557115?sort=created&page=2)
* Spring Data JPA 是基于 Spring Data 的 repository 之上，可以将 repository 自动输出为 REST 资源。 目前 Spring Data REST 支持将 Spring Data JPA，Spring Data MongoDB，Spring Data Neo4j，Spring Data GemFire 以及 Spring Data Cassandra 的 repository 自动转换成 REST 服务
* 在 SpringMVC中使用Spring Data REST的两种方式
    * 自定义配置类继承 `RepositoryRestMvcConfiguration`
    * 通过@Import在配置类中导入 `RepositoryRestMvcConfiguration`
* 在 SpringMVC 和 在 SpringBoot 中使用 Spring Data REST 的方式是一致的

#### 1，Spring Boot 对 Spring Data REST 的支持
在 SpringBootRepositoryRestMvcConfiguration 类中，SpringBoot 已经为我们自动配置了 `RepositoryRestConfiguration` ，所以在Spring Boot 中使用 Spring Data REST 不需要我们再自行配置，只需要导入 `spring-boot-starter-data-rest` 的依赖即可。


#### 2，新建项目
* 导入jpa依赖
* 导入rest依赖
* 导入oracle jdbc 驱动依赖
* 在application.properties文件中配置相关属性

```prop
spring.jpa.database=oracle
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=root
spring.datasource.password=123

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jackson.serialization.indent-output=true
```

#### 3，创建实体类Person

```java
	//省略了get/set

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private Integer age;

	private String address;

	public Person() {
	}
```

#### 4，创建 PersonRepository 接口

```java
package com.zyf.dao;

import com.zyf.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by zyf on 2018/3/13.
 */
public interface PersonRepository extends JpaRepository<Person,Long> {

	/**
	 * 将该方法，也暴露为REST资源
	 * @param name
	 * @return
	 */
	@RestResource(path = "nameStartsWith",rel = "nameStartsWith")
	Person findByNameStartsWith(@Param("name") String name);
}

```

#### 5，安装 Postman

#### 6，运行项目后，使用 Postman 访问
![](https://ws1.sinaimg.cn/large/006tKfTcgy1fpazmmzb2cj30nv0g9myz.jpg)

#### 7，访问以下路径查看现象

* **GET请求：**
    * 查看列表数据：`/persons`
    * 获取单一对象：`/persons/{id}`
    * 使用 `PersonRepository` 中暴露的方法：`/persons/search/nameStartsWith?name=盖`
    * 分页：`/persons/?page=1&size=2` 查询第二页（第一页page=0）的数据，每页两条数据
    * 排序：`/persons/?sort=age,desc` 按照age属性倒序查询
* **POST请求：**
    * 保存：`/persons` 将要保存的数据以JSON形式放置在请求体中
    * ![](https://ws1.sinaimg.cn/large/006tKfTcgy1fpazur1hxij30p009hmyk.jpg)
* **PUT请求：**
    * 更新：`/persons/{id}` 与保存中提交数据的方式一致
* **DELETE请求：**
    * 删除：`/persons/{id}`

    
#### 8
出现404 Not Found 说明要访问的 REST 资源不存在
如果响应体中没有内容，可能是要获得id为3的数据，但是实际上压根就没有id为3的数据，那么不会报错，但是响应体中无内容

#### 9，将根路径由 `/` 修改为 `/api`
在 `application.properties` 文件中添加如下配置：

```prop
spring.data.rest.base-path=/api
```

再次访问时：`/api/persons`

#### 10，定制节点路径
* Spring Data Rest 的默认规则：在实体类名后加 `s` 形成路径
* 可以通过在 `Repository` 接口上添加注解 `@RepositoryRestResoure` 修改节点路径
    * `@RepositoryRestResource(path = "people")` 
    * ![](https://ws2.sinaimg.cn/large/006tKfTcgy1fpb0p6k6rdj30fr0bqmxx.jpg)


