### 25_数据缓存Cache
当我们需要重复的获取相同的数据时，我们一次又一次的请求数据库或者远程服务，导致大量的时间耗费在数据库查询或者远程方法调用上，导致程序性能的恶化，这便是数据缓存要解决问题。
Spring 定义了 `.cache.CacheManager` 和 `.cache.Cache` 接口用来统一不同的缓存技术

### Spring 缓存支持
`CacheManager` 是 Spring 提供的各种缓存技术抽象接口
`Cache` 接口包含缓存的各种操作（增加，删除，获得缓存）
#### 1，Spring 支持的 CacheManager

| CacheManager | 描述 |
| --- | --- |
| SimpleCacheManager | 使用简单的Connection来存储缓存，主要用与测试 |
| ConcurrentManager | 使用ConcurrentMap来存储缓存 |
| NoOpCacheManager | 仅测试用途，不会实际存储缓存 |
| EhCacheCacheManager | 使用EhCache作为缓存技术 |
| GuavaCacheManager | 使用Google Guava的GuavaCache缓存技术 |
| HazelcastCacheManager | 使用Hazelcast作为缓存技术 |
| JCacheCacheManager | 使用JCache标准的实现作为缓存技术 |
| RedisCacheManager | 使用Redis作为缓存技术 |

在我们使用任意一个实现的 `CacheManager` 的时候，需注册实现的 `CacheManager` 的Bean

```java
@Bean
public EhCacheCacheManager cacheManager(CacheManager ehCacheCacheManager){
    return new EhCacheCacheManager(ehCacheCacheManager);
}
```

#### 2，声明式缓存注解
Spring 提供了4个注解来声明缓存规则（声明式注解的例子--AOP）

| 注解 | 解释 |
| --- | --- |
| @Cacheable | 在方法执行前，Spring 先查看缓存中是否有数据，如果有数据，则直接返回缓存数据； 若没有数据，调用方法并将方法返回值放入缓存 |
| @CachePut | 无论怎样，都会将方法的返回值放入缓存 |
| @CacheEvict | 将一条或多条数据从缓存中删除 |
| @Caching | 可以通过@Caching 注解组合多个注解策略在一个方法上 |

`@Cacheable` `@CachePut` `@CacheEvict` 都有 `value` 属性，指定的是要使用缓存名称； `key` 属性指定的是数据在缓存中的存储的键。

#### 3，开启声明式缓存支持
在配置类上使用 `@EnableCaching`

### Spring Boot 的支持
Spring 中使用缓存技术的关键是配置 `CacheManager` ，而Spring Boot为我们自动配置了多个 `CacheManager` 的实现（上述表格中的缓存都自动配置了，默认使用的是 `SimpleCacheConfiguration` 及使用的是 `ConcurrentCacheManager` ）。
Spring Boot 的 `CacheManager` 的自动配置放在了 `org.springframework.boot.autoconfigure` 包中。

在 `application.properties` 中使用 `spring.cache` 前缀来配置缓存。

在 SpringBoot 的环境下，使用缓存技术只需要在项目中导入相关缓存技术的依赖包，并在配置类中使用 `@EnableCaching` 开启缓存支持即可。

#### 1，创建SpringBoot项目
依赖：Cache，JPA，WEB，Oracle驱动
配置：Oracle

#### 2，创建实体类Person
与前面的例子一致

#### 3，创建实体类的Repository
与前面的例子一致

#### 4，创建Service层

```java
package com.zyf.service;

import com.zyf.domain.Person;

/**
 * Created by zyf on 2018/3/13.
 */
public interface PersonService {

	Person save(Person person);

	void remove(Long id);

	Person findOne(Person person);
}

```

```java
package com.zyf.service.impl;

import com.zyf.PersonRepository;
import com.zyf.domain.Person;
import com.zyf.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by zyf on 2018/3/13.
 */
@Service
public class PersonServiceImpl implements PersonService{

	@Autowired
	PersonRepository personRepository;

	@Override
	@CachePut(value = "people",key = "#person.id")//缓存名称为people//#可以找到参数person并获取它的id
	public Person save(Person person) {
		Person p = personRepository.save(person);

		System.out.println("为id、key为："+p.getId()+"的数据做了缓存");
		return p;
	}

	@Override
	@CacheEvict(value = "people",key = "id")//找到参数id
//	@CacheEvict(value = "people")//如果没有指定key，则方法的参数会作为key使用
	public void remove(Long id) {
		System.out.println("删除了id、key为："+id+"的数据缓存");
		personRepository.delete(id);
	}

	@Override
	@Cacheable(value = "people",key = "#person.id")//将person加入缓存，key为person的id属性值
	public Person findOne(Person person) {
		Person p = personRepository.findOne(person.getId());
		System.out.println("为、key为："+person.getId()+"的数据做了缓存处理");
		return p;
	}
}

```

#### 5，创建控制器

```java
package com.zyf.web.controller;

import com.zyf.domain.Person;
import com.zyf.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zyf on 2018/3/13.
 */
@RestController
public class CacheController {

	@Autowired
	PersonService personService;

	@RequestMapping("/put")
	public Person put(Person person){
		return personService.save(person);
	}

	@RequestMapping("/able")
	public Person cacheable(Person person){
		return personService.findOne(person);
	}

	@RequestMapping("/evit")
	public String evit(Long id){
		personService.remove(id);
		return "ok";
	}

}

```

#### 6，添加缓存支持

```java
@SpringBootApplication
@EnableCaching
public class Springboot13Application {

	public static void main(String[] args) {
		SpringApplication.run(Springboot13Application.class, args);
	}
}

```

#### 7，运行访问测试
**访问 `/put` ，添加数据，会将数据加入到缓存中**
![](https://ws3.sinaimg.cn/large/006tKfTcgy1fpbca3aqx5j30fk05c74s.jpg)

**查看控制台输出**
![](https://ws1.sinaimg.cn/large/006tKfTcgy1fpbcd8nji8j30v2062tai.jpg)

**访问 `/able` ，查询数据，因为缓存中已经有key为69的数据了，不会查询数据库，会直接从缓存中获得**
![](https://ws4.sinaimg.cn/large/006tKfTcgy1fpbcek9un7j30ak058q3a.jpg)

**查看控制台输出，发现并没有查询的sql语句输出**
![](https://ws4.sinaimg.cn/large/006tKfTcgy1fpbcf6zr7jj30tk05ita6.jpg)

### 切换缓存技术
切换缓存技术除了移入相关依赖包或者配置以外，使用方式和上述例子一致。
#### 1，EhCache
**添加依赖**

```xml
        <dependency>
            <groupId>net.sf.ehcache</groupId>
            <artifactId>ehcache</artifactId>
        </dependency>
```

**在类路径下创建 `ehcache.xml` 配置文件，SpringBoot会自动扫描**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<ehcache>
    <cache name="people" maxElementsInMemory="1000">

    </cache>
</ehcache>
```

#### 2，Guava
**添加依赖**

```xml
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>18.0</version>
        </dependency>
```

#### 3，Redis

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-redis</artifactId>
        </dependency>
```

