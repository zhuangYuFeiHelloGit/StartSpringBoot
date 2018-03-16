### 22_Spring Data JPA
* 曾经Hibernate是数据访问解决技术的绝对霸主，使用 O/R 映射 （Object-Relational Mapping）对象关系映射技术实现数据访问，O/R映射将领域模型类和数据库的表进行映射，通过程序操作对象而实现表数据操作的能力，让数据访问操作无需关注数据库的相关技术。

* 随着Hibernate盛行，Hibernate主导了 EJB 3.0 的 JPA规范。JPA即 Java Persistence API（java持久化接口）。是一个基于 O/R映射的标准规范，只定义标准与规则，不提供实现（只给你注解或接口），软件提供商可以按照标准规范来实现，使用者只需要按照规范中定义的方式来使用。

* JPA的主要实现由Hibernate、EclipseLink、OpenJPA等。只要使用JPA来开发（规范），无论哪一种底层实现的开发方式都是一样的（使用规范提供的抽象即可）

* Spring Data JPA 是 Spring Data的一个子项目，通过提供基于 JPA 的Repository 极大的减少了JPA作为数据访问方案的代码量。

#### 1，使用Docker安装Oracle XE 数据库
#### 2，下载Oracle的jdbc驱动jar包
[下载ojdbc6.jar](mvn install:install-file -DgroupId=com.oracle "-DartifactId=ojdbc6" "-Dversion=11.2.0.2.0" "-Dpackaging=jar" "-Dfile=/Users/zyf/Library/Maven/LocalWarehouse/com/oracle/ojdbc6/11.2.0.2.0/ojdbc6.jar")

#### 3，将下载好的jar包保存到maven本地库中
我的本地库路径：`/Users/zyf/Library/Maven/LocalWarehouse/com/oracle/ojdbc6/11.2.0.2.0/ojdbc6.jar`

#### 4，将jar包添加到maven仓库
在控制台执行以下命令
`mvn install:install-file -DgroupId=com.oracle "-DartifactId=ojdbc6" "-Dversion=11.2.0.2.0" "-Dpackaging=jar" "-Dfile=/Users/zyf/Library/Maven/LocalWarehouse/com/oracle/ojdbc6/11.2.0.2.0/ojdbc6.jar"`

* DgroupId=com.oracle ：指定当前包的groupId为com.oracle
* DartifactId=ojdbc6 ： 指定当前包的artifactId为ojdbc6
* Dversion=11.2.0.2.0 ：指定当前包的版本
* -Dfile=/Users/zyf/Library/Maven/LocalWarehouse/com/oracle/ojdbc6/11.2.0.2.0/ojdbc6.jar ： 指定要打包的jar的文件位置
* 执行后，该jar文件被打包到本地库

![](https://ws3.sinaimg.cn/large/006tNbRwgy1fp9uj1w15tj30dc05qdgb.jpg)

#### 5，新建SpringBoot项目
* 依赖JPA
* 依赖Web
* 在pom.xml中导入ojdbc

```xml
		<dependency>
			<groupId>com.oracle</groupId>
			<artifactId>ojdbc6</artifactId>
			<version>11.2.0.2.0</version>
		</dependency>
```

* 导入google guava依赖，该依赖包含大量的工具类

```xml
		<dependency>
			<groupId>com.google.guava</groupId>
			<artifactId>guava</artifactId>
			<version>18.0</version>
		</dependency>
```

#### 6，新建一个data.sql 文件防止在 `src/main/resources` 下，内容为向表格增加一些数据，数据插入完成后请删除或对此文件重命名
SpringBoot提供的功能：放在类路径下的 `schema.sql` 文件会自动用来初始化表结构，放在类路径下的 `data.sql` 文件会自动用来填充表数据。

```sql
INSERT INTO person(id,name,age,address) VALUES (hibernate_sequence.nextval,'盖伦',18,'北京');
INSERT INTO person(id,name,age,address) VALUES (hibernate_sequence.nextval,'卢姥爷',17,'上海');
INSERT INTO person(id,name,age,address) VALUES (hibernate_sequence.nextval,'蛇哥',19,'广州');
INSERT INTO person(id,name,age,address) VALUES (hibernate_sequence.nextval,'PG1',16,'深圳');
INSERT INTO person(id,name,age,address) VALUES (hibernate_sequence.nextval,'阿怡',15,'杭州');
INSERT INTO person(id,name,age,address) VALUES (hibernate_sequence.nextval,'天佑',20,'杭州');
COMMIT ;

```

#### 7，在application.properties中配置数据源和jpa属性

```prop
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=root
spring.datasource.password=123

#启动时会根据实体类生成表
spring.jpa.hibernate.ddl-auto=update
#显示sql语句
spring.jpa.show-sql=true
#让控制器格式化的输出json对象
spring.jackson.serialization.indent-output=true
```

#### 8，定义映射实体类

```java
package com.zyf.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/**
 * Created by zyf on 2018/3/12.
 * 正向工程：通过实体类生成表结构
 * 逆向工程：通过表结构生成实体类
 * 注解@column用来映射属性名和字段名，不使用该注解时，Hibernate会自动根据属性名生成数据表的字段名
 */
@Entity//表示这是一个与数据库表映射的实体类
@NamedQuery(name = "Person.withNameAndAddressNamedQuery",query = "select p from Person p where p.name=?1 and address=?2")
public class Person {

	@Id//表示这个属性映射为数据库中表的的主键
	@GeneratedValue//自增
	private Long id;

	private String name;

	private Integer age;

	private String address;

	public Person() {
	}

	public Person(Long id, String name, Integer age, String address) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", name='" + name + '\'' +
				", age=" + age +
				", address='" + address + '\'' +
				'}';
	}
}

```

#### 9，定义数据访问接口

```java
package com.zyf.dao;

import com.zyf.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by zyf on 2018/3/12.
 * 定义数据访问接口
 */
public interface PersonRepository extends JpaRepository<Person,Long> {
	//方法名查询，接收一个参数，返回值为list
	List<Person> findByAddress(String name);

	//方法名查询，接收name和address，返回单个对象
	Person findByNameAndAddress(String name,String address);

	//@Query查询，参数按照名称绑定
	@Query("select p from Person p where p.name= :n and p.address= :a")
	Person withNameAndAddressQuery(@Param("n")String name,@Param("a")String address);

	//@NamedQuery查询，在实体类Person中定义过了
	Person withNameAndAddressNamedQuery(String name,String address);
}

```

#### 10，定义控制器

```java
package com.zyf.web.controller;

import com.zyf.dao.PersonRepository;
import com.zyf.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zyf on 2018/3/12.
 */
@RestController
public class DatabaseController {

	/**
	 * Spring Data JPA 会自动为我们注册bean，所以可以自动注入
	 */
	@Autowired
	PersonRepository personRepository;

	/**
	 * 增删改都一样
	 * 保存支持批量保存
	 * 删除支持id删除，批量删除，删除全部
	 * @param name
	 * @param address
	 * @param age
	 * @return
	 */
	@RequestMapping("/save")
	public Person save(String name,String address,Integer age){
		Person p = personRepository.save(new Person(null,name,age,address));
		return p;
	}

	@RequestMapping("/query1")
	public List<Person> query1(String address){
		List<Person> people = personRepository.findByAddress(address);
		return people;
	}

	@RequestMapping("/query2")
	public Person query2(String name,String address){
		Person person = personRepository.findByNameAndAddress(name,address);
		return person;
	}

	@RequestMapping("/query3")
	public Person query3(String name,String address){
		Person p = personRepository.withNameAndAddressQuery(name,address);
		return p;
	}

	@RequestMapping("/query4")
	public Person query4(String name,String address){
		Person p = personRepository.withNameAndAddressNamedQuery(name,address);
		return p;
	}

	/**
	 * 排序
	 * @return
	 */
	@RequestMapping("/sort")
	public List<Person> sort(){
		//根据年龄排序
		List<Person> people = personRepository.findAll(new Sort(Sort.Direction.ASC,"age"));

		return people;
	}

	/**
	 * 分页
	 * @return
	 */
	@RequestMapping("/page")
	public Page<Person> page(){

		Page<Person> personPage = personRepository.findAll(new PageRequest(1,2));

		return personPage;
	}
}

```


#### 11，在docker中运行Oracle XE 容器
**运行并将Oracle XE 管理界面的8080端口映射为本机的9090端口，将1521端口映射为本机的1521端口**
`docker run -d -p 9090:8080 -p1521:1521 wnameless/oracle-xe-11g`
**容器暴露的端口，只是在虚拟机上可以访问到，本机要访问，需要将虚拟机的端口映射到当前开发机器上**

* VMware Fusion设置端口转发，需要修改配置文件：`/Library/Preferences/VMware Fusion/vmnet8` ，做如下修改后，重启虚拟机，注意这个ip地址是Linux虚拟机的ip地址
![](https://ws1.sinaimg.cn/large/006tNc79gy1fpa15ffk43j31e20f642j.jpg)

**在Linux虚拟机中登陆Oracle XE 数据库**
![](https://ws4.sinaimg.cn/large/006tNc79gy1fpa1w3ojstj318205imy3.jpg)

![](https://ws1.sinaimg.cn/large/006tNc79gy1fpa1wmea52j311e0aidgu.jpg)

**创建用户**

* `create user root identified by 123` root用户名  123密码
* `grant dba to root` 给root用户授权

#### 12，运行测试
![](https://ws2.sinaimg.cn/large/006tNc79gy1fpa5jtngt2j30hu0zw423.jpg)
![](https://ws2.sinaimg.cn/large/006tNc79gy1fpa5mma18cj30n00g8dhp.jpg)

### 使用Spring Data JPA的细节网上找找文档
[文档1](https://www.cnblogs.com/ityouknow/p/5891443.html)
[文档2](https://www.jianshu.com/p/9d5bf0e4943f)


