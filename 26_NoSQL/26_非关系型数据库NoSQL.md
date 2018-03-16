### 26_非关系型数据库NoSQL
* NoSQL 是对于不使用关系作为数据管理的数据库的统称。NoSQL的主要特点是不使用 SQL 语言作为查询语言，数据存储也不是固定的表，字段。
* NoSQL 数据库主要有文档存储型（MongoDB），图形关系存储型（Neo4j）和键值对存储型（Redis）。

### 1，MongoDB
* MongoDB 是一个基于文档（Document）的存储型数据库，使用面向对象的思想，每一条数据记录都是文档的对象。
* MongoDb 是一个基于分布式文件存储的数据库，由 C++ 语言编写，旨在为 WEB 应用提供可扩展的高性能数据存储解决方案。
* MongoDB 是一个介于关系型数据库和非关系型数据库之间的产品，是非关系型数据库当中功能最丰富，最像关系型数据库的。
* [NoSQL简介](http://www.runoob.com/mongodb/nosql.html)


#### 1，Spring 的支持
* Spring 对 `MongoDB` 的支持主要是通过 `Spring Data MongoDB` 来实现的。

**Object/Document 映射注解支持**
JPA 提供了一套 Object/Document 映射的注解（@Entity，@Id），而 `Spring Data MongoDB` 也提供了以下注解


| 注解 | 描述 |
| --- | --- |
| @Document | 映射领域对象与MongoDB的一个文档 |
| @Id | 映射当前属性是ID |
| @DbRef | 当前属性将参考其他的文档  |
| @Field | 为文档的属性定义名称 |
| @Version | 将当前属性作为版本 |

**MongoTemplate**
像JdbcTemplate一样，`Spring Data MongoDB` 也为我们提供了一个 `MongoTemplate` ， `MongoTemplate` 为我们提供了数据访问的方法，我们还需要 `MongoClient` 和 `MongoDbFactory` 来配置数据库连接属性

```java

```

**Repository的支持**
类似于 `Spring Data JPA` ，`Spring Data MongoDB` 也提供了 `Repository` 的支持，使用方式和 `Spring Data JPA` 一致。

**开启MongoDB的Repository支持**
在配置类上注解 `@EnableMongoRepositories`


#### 2，SpringBoot的支持
* 可以在 `application.properties` 文件中，使用前缀为 `spring.data.mongodb` 的属性，配置MongoDB的相关信息。
* SpringBoot 为我们提供了一些默认属性（MongoDB默认端口号为27017，默认服务器为localhost，默认数据库为test）
* SpringBoot 已经自动配置了 `@EnableMongoRepositories`
* 所以在SpringBoot下使用MongoDB只需引入依赖包即可，无需其他配置。

#### 3，准备工作

在docker中将MongoDB镜像运行为容器
`docker run -d -p 27017:27017 mongo`

将虚拟机端口27017映射到本机端口27017
![](https://ws3.sinaimg.cn/large/006tNc79gy1fpc4fh1gi1j30nw0b6jt1.jpg)

下载MongoDB数据库管理软件：[点击下载](https://www.robomongo.org/download)

![](https://ws3.sinaimg.cn/large/006tNc79gy1fpc55cvg2cj30kk0astct.jpg)

#### 4，新建项目
依赖：Web，MongoDB
SpringBoot默认的数据库连接满足当前的测试需求（默认操作test数据库），所以不需要在配置文件（ `application.properties` 中配置）

#### 5，创建实体类（称为领域模型）

```java
package com.zyf.domain;

/**
 * Created by zyf on 2018/3/14.
 */
public class Location {

	private String place;

	private String year;

	public Location(String place, String year) {
		this.place = place;
		this.year = year;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}

```


```java
package com.zyf.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Created by zyf on 2018/3/14.
 */
@Document//映射领域模型与MongoDB的文档
public class Person {

	@Id//表示这个属性为文档的id
	private String id;

	private String name;

	private Integer age;

	@Field("locs")//表示locations属性在文档中的名称为locs，locations将以数组形式存在当前数据记录中
	private Collection<Location> locations = new LinkedHashSet<>();

	public Person(String name, Integer age) {
		this.name = name;
		this.age = age;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Collection<Location> getLocations() {
		return locations;
	}

	public void setLocations(Collection<Location> locations) {
		this.locations = locations;
	}
}

```


#### 6，创建dao

```java
package com.zyf.dao;

import com.zyf.domain.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by zyf on 2018/3/14.
 */
public interface PersonRepository extends MongoRepository<Person,String>{

	//支持方法名查询
	Person findByName(String name);

	@Query("{'age':?0}")//支持@Query查询，查询参数：一个JSON字符串
	List<Person> withQueryFindByAge(Integer age);
}

```


#### 7，创建控制器

```java
package com.zyf.web.controller;

import com.zyf.dao.PersonRepository;
import com.zyf.domain.Location;
import com.zyf.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by zyf on 2018/3/14.
 */
@RestController
public class DataController {

	@Autowired
	PersonRepository personRepository;

	@RequestMapping("/save")
	public Person save(){
		Person person = new Person("zyf",99);
		Collection<Location> locations = new LinkedHashSet<Location>();

		Location l1 = new Location("北京","2000");
		Location l2 = new Location("上海","2001");
		Location l3 = new Location("广州","2002");
		Location l4 = new Location("深圳","2003");

		locations.add(l1);
		locations.add(l2);
		locations.add(l3);
		locations.add(l4);

		person.setLocations(locations);

		return personRepository.save(person);
	}

	@RequestMapping("/q1")//测试方法名查询
	public Person q1(String name){
		return personRepository.findByName(name);
	}

	@RequestMapping("/q2")
	public List<Person> q2(Integer age){
		return personRepository.withQueryFindByAge(age);
	}

}


```

#### 8，访问save后使用robo查看

![](https://ws1.sinaimg.cn/large/006tNc79gy1fpcbdss71xj30ef02cwez.jpg)
![](https://ws1.sinaimg.cn/large/006tNc79gy1fpcbejvpplj30d5029wew.jpg)
![](https://ws2.sinaimg.cn/large/006tNc79gy1fpcbffwg0mj30pt05rq42.jpg)
![](https://ws3.sinaimg.cn/large/006tNc79gy1fpcbaatqalj30ie0lsgnr.jpg)

### 2，Redis
Redis 是一个基于键值对的开源内存数据存储，Redis也可以做数据缓存
#### 1，Spring 的支持

* Spring 对 Redis 的支持也是通过 `Spring Data Redis` 来实现的， `Spring Data JPA` 为我们提供了连接相关 `ConnectionFactory` 和数据操作相关的 `RedisTemplate`。
* 根据Redis 不同的 Java 客户端， `Spring Data Redis` 提供了不同的 `ConnectionFactory`
    * `JedisConnectionFactory` ：使用 Jedis 作为 Redis 客户端
    * `JredisConnectionFactory` ：使用 Jredis 作为 Redis 客户端
    * `LettuceConnectionFactory` ：使用Lettuce 作为 Redis 客户端
    * `SrpConnectionFactory` ： 使用 `Spullara/redis-protocol` 作为 Redis 客户端
    
**配置方式**

```java
@Bean
public RedisConnectionFactory redisConnectionFactory(){
    return new JedisConnectionFactory();
}
```

**RedisTemplate配置方式**

```java
@Bean
public RedisTemplate<Object,Object> redisTemplate() throw UnknownHostException{
    RedisTemplate<Object,Object> template = new RedisTemplate<Obect,Object>();
    template.setConnectionFactory(redisConnectionFactory());
    return template;s
}
```

**使用**
`Spring Data Redis` 为我们提供了 `RedisTemplate` 和 `StringRedisTemplate` 两个模板啦进行数据操作，其中，`StringRedisTemplate` 只针对键值都是字符型的数据进行操作。


| 方法 | 说明 |
| --- | --- |
| opsForValue（） | 操作只有简单属性的数据 |
| opsForList（） | 操作含有list的数据 |
| opsForSet（） | 操作含有set的数据 |
| opsForZSet（） | 操作含有ZSet（有序的set）的数据 |
| opsForHash（） | 操作含有hash的数据 |

**定义Serializer**
当我们的数据存储到Redis的时候，我们的键（key）和值（value）都是通过 Spring 提供的 `Serializer` 序列化到数据库的。 `RedisTemplate` 默认使用的是 `JdkSerializationRedisSerializer` ，`StringRedisTemplate` 默认使用的是 `StringRedisSerializer`


#### 2，SpringBoot的支持
* `RedisAutoConfiguration` 为我们默认配置了 `JedisConnectionFactory` 、 `RedisTemplate` 以及 `StringRedisTemplate` ，这样我们就可以直接使用 `Redis` 作为数据存储
* 可以使用 `spring.redis` 为前缀的属性在 `application.properties` 文件中配置redis

```prop
#数据库名称，默认为0
#spring.redis.database=0
#服务器地址，默认为localhost
#spring.redis.host=localhost
#数据库密码
#spring.redis.password=
#连接端口号，默认为6379
#spring.redis.port=
#连接池设置
#spring.redis.pool.xxxxxx
```

#### 3，创建SpringBoot项目
* 依赖：Web，Redis
* docker运行redis映射为容器 `docker run -d -p 6379:6379 redis:2.8.21`
* 虚拟机端口映射
    * ![](https://ws3.sinaimg.cn/large/006tNc79gy1fpcdb0kecdj310e0ccju0.jpg)
* [下载Mac版RedisClient](https://github.com/caoxinyu/RedisClient/blob/OSX/release/redisclient-OSX.jar?raw=true)
* [下载Windows64版RedisClient](https://github.com/caoxinyu/RedisClient/blob/windows/release/redisclient-win32.x86_64.2.0.jar?raw=true)
* [下载Windows32版RedisClient](https://github.com/caoxinyu/RedisClient/blob/windows/release/redisclient-win32.x86.2.0.jar?raw=true)
* 将下载好的jar包放置在任意文件夹中，在终端进入该文件夹，执行 `java -XstartOnFirstThread -jar 下载的jar包名.jar` 命令即可打开

#### 4，默认连接即可满足测试需求，直接创建Person类

```java
package com.zyf.domain;

import java.io.Serializable;

/**
 * Created by zyf on 2018/3/14.
 */
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private Integer age;

	public Person() {
	}

	public Person(String id, String name, Integer age) {
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
#### 5，创建Dao

```java
package com.zyf.dao;

import com.zyf.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by zyf on 2018/3/14.
 */
@Repository
public class PersonDao {

	/**
	 * SpringBoot帮我们配置了StringRedisTemplate
	 * 所以可以直接注入
	 */
	@Autowired
	StringRedisTemplate stringRedisTemplate;

	/**
	 * 同上
	 */
	@Autowired
	RedisTemplate<Object,Object> redisTemplate;

	/**
	 * 通过@Resource注解指定stringRedisTemplate，
	 * 可注入基于字符串的简单属性操作方法
	 */
	@Resource(name = "stringRedisTemplate")//会有警告，无视就行
	ValueOperations<String,String> valOpsStr;

	/**
	 * 通过@Resource注解指定redisTemplate，
	 * 可注入基于对象的简单属性操作方法
	 */
	@Resource(name = "redisTemplate")
	ValueOperations<Object,Object> valOps;

	/**
	 * 通过set方法存储字符串类型
	 */
	public void stringRedisTemplateDemo(){
		valOpsStr.set("xx","XX");
	}

	/**
	 * 通过set方法存储对象类型
	 * @param person
	 */
	public void save(Person person){
		valOps.set(person.getId(),person);
	}

	/**
	 * 通过get方法，获得字符串
	 * @return
	 */
	public String getString(){
		return valOpsStr.get("xx");
	}

	/**
	 * 通过get方法，获得对象
	 * @return
	 */
	public Object getPerson(){
		return valOps.get("1");
	}
}

```

#### 6，修改入口类
`JdkSerializationiRedisSerializer` 使用二进制形式存储数据，不方便我们通过 `RedisClient` 查看，这里我们自己配置 `RedisTemplate` 并定义 `Serializer`


```java
package com.zyf;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SpringBootApplication
public class Springboot15Application {

	public static void main(String[] args) {
		SpringApplication.run(Springboot15Application.class, args);
	}

	@Bean
	@SuppressWarnings({"rawtypes","unchecked"})
	public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
		RedisTemplate<Object,Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();

		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

		jackson2JsonRedisSerializer.setObjectMapper(om);

		//设置value的序列化采用jackson2JsonRedisSerializer
		template.setValueSerializer(jackson2JsonRedisSerializer);

		//设置key的序列化采用StringRedisSerializer
		template.setKeySerializer(new StringRedisSerializer());

		template.afterPropertiesSet();
		return template;
	}
}

```

#### 7，运行访问演示后，查看Redis Client
点击server，添加一个连接，密码不填即可。
![](https://ws2.sinaimg.cn/large/006tNc79gy1fpcepdkd02j30lm0d0gnk.jpg)

![](https://ws2.sinaimg.cn/large/006tNc79gy1fpcepqrf6sj30m60dwwgi.jpg)

