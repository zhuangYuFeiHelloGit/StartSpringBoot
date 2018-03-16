#### 24_Spring Boot 的事务支持
* 所有的数据访问技术都有事务处理机制，这些技术提供了API用来开启事务，提交事务来完成数据操作，或者在发生错误的时候回滚数据。
* Spring 的事务机制是用统一的机制来处理不同的数据访问技术的事务处理。
* Spring 的事务机制提供了一个 `PlatformTransactionManager` 接口，不同的数据访问技术的事务使用不同的接口实现。


| 数据访问技术 | 实现类 |
| --- | --- |
| JDBC | DataSourceTransactionManager |
| JPA | JpaTransactionManager |
| Hibernate | HibernateTransactionManager |
| JDO | JdoTransactionManager |
| 分布式事务 | JtaTransactionManager |

**定义事务管理器的代码如下**

```java
@Bean
public PlatformTransactionManager transactionManager(){
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setDataSource(dataSource());
    return transactionManager;
}
```

#### 1，声明式事务
Spring 支持声明式事务，即使用注解来选择需要使用事务的方法（切入点），它使用 `@Transactional` 注解在方法上，声明该方法需要事务支持，这是一个基于 AOP 的实现操作。

`@Transactional` 注解是 `org.springframework.transaction.annotation` 包下的

Spring 提供了一个 `@EnableTransactionManager` 注解，在配置类上来开启声明式事务的支持。会自动扫注解 `@Transactional` 的方法和类（表示该类中所有public的方法都需要开启事务的）。

#### 2，Spring Data JPA 的事务支持
Spring Data JPA 对所有的默认方法都开启了事务支持，且查询类事务默认启用 `readOnly=true` 属性。
可以查看 `SimpleJpaRepository` 类的源码

![](https://ws4.sinaimg.cn/large/006tKfTcgy1fpb1rcl4hlj30w807i40a.jpg)
![](https://ws2.sinaimg.cn/large/006tKfTcgy1fpb1sjqy87j30ze06w761.jpg)

#### 3，Spring Boot 的事务支持：自动配置的事务管理器
* 在使用JDBC作为数据访问技术的时候，Spring Boot 为我们定义了 `PlatformTransactionManager` 的实现 `DataSourceTransactionManager` 的Bean；
* 在使用JPA作为数据访问技术的时候，Spring Boot 为我们定义了 `PlatformTransactionManager` 的实现 `JpaTransactionManager` 的Bean；

#### 4，Spring Boot 的事务支持：自动开启注解事务的支持
* Spring Boot 专门用于配置事务的类为 ：`TransactionAutoConfiguration`，此配置类依赖于 `JpaBaseConfiguration` 和 `DataSourceTransactionManagerAutoConfiguration`
* 在 `DataSourceTransactionManagerAutoConfiguration` 里开启了对声明式事务的支持
* 所以在 Spring Boot中，无需显示的开启使用 `@EnableTransactionManager` 注解

#### 5，新建Spring Boot 项目：演示使用异常导致事务回滚
* 导入 `web` 与 `jpa` 依赖
* 添加 `ojdbc6.jar` 依赖，在 `application.properties` 文件中添加配置

#### 6，创建实体类Person
* 属性：id（主键、自增），name，address，age
* 空参数构造方法，set/get方法
* 别忘了在类上注解 ：`@Entity`

#### 7，创建实体类的Repository接口（DAO）

```java
public interface PersonRepository extends JpaRepository<Person,Long>{
	
}
```

#### 8，创建对应的Service 层

```java
package com.zyf.service;

import com.zyf.domain.Person;

/**
 * Created by zyf on 2018/3/13.
 */
public interface PersonService {
	/**
	 * 演示回滚
	 */
	Person savePersonWithRollBack(Person person);

	/**
	 * 演示不会滚
	 */
	Person savePersonWithoutRollBack(Person person);
}

```


```java
package com.zyf.service.impl;

import com.zyf.dao.PersonRepository;
import com.zyf.domain.Person;
import com.zyf.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zyf on 2018/3/13.
 */
@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	PersonRepository personRepository;

	//通过rollbackfor属性指定：出现该异常则数据回滚
	@Transactional(rollbackFor = {IllegalArgumentException.class})
	@Override
	public Person savePersonWithRollBack(Person person) {
		Person p = personRepository.save(person);
		if(person.getName().equals("zyf")){
			throw new IllegalArgumentException("不好意思不能存，回滚啦");
		}
		return p;
	}

	//通过noRollbackFor属性指定：出现该异常则数据不回滚
	@Transactional(noRollbackFor = {IllegalArgumentException.class})
	@Override
	public Person savePersonWithoutRollBack(Person person) {
		Person p = personRepository.save(person);
		if(person.getName().equals("zyf")){
			throw new IllegalArgumentException("不好意思不能存，回滚啦");
		}
		return p;
	}
}

```

#### 9，控制器

```java
package com.zyf.controller;

import com.zyf.domain.Person;
import com.zyf.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zyf on 2018/3/13.
 */
@Controller
public class PersonController {
	
	@Autowired
	PersonService personService;
	
	@RequestMapping("/rollback")
	public Person rollback(Person person){
		return personService.savePersonWithRollBack(person);
	}
	
	@RequestMapping("/norollback")
	public Person noRollback(Person person){
		return personService.savePersonWithoutRollBack(person);
	}
}

```

#### 10，测试
**访问不回滚**
![](https://ws3.sinaimg.cn/large/006tKfTcgy1fpb7qbk2jtj30vh06cmye.jpg)
**依然存进去了**
![](https://ws3.sinaimg.cn/large/006tKfTcgy1fpb7q71w1vj30sw02iwen.jpg)

**访问回滚的话数据库中是不会插入新数据的**


