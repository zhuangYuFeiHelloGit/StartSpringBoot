### 28_Spring Batch
**什么是Spring Batch**
Spring Batch 是用来处理大量数据操作的一个框架，主要用来读取大量数据，然后进行一定处理后输出指定的形式。

**Spring Batch 主要组成**

| 名称 | 用途 |
| --- | --- |
| JobRepository | 用来注册Job的容器 |
| JobLauncher | 用来启动Job的接口 |
| Job | 我们要实际执行的任务，包含一个或多个Step |
| Step | Step-步骤，包含ItemReader，ItemProcessor，ItemWriter |
| ItemReader | 用来读取数据的接口 |
| ItemProcessor | 用来处理数据的接口 |
| ItemWriter | 用来输出数据的接口 |

#### 1，Spring Boot 的支持
`Spring Boot` 对 `Spring Batch` 提供了自动配置，为我们自动初始化了 `Spring Batch` 存储批处理记录的数据库，且当我们程序启动时，会自动执行我们定义的 `Job` 的Bean

**相关配置**

```prop
#启动时要执行的job，默认会执行全部job
spring.batch.job.names=job1,job2

#是否自动执行job，默认为是
spring.batch.job.enabled=true

#是否初始化Spring Batch 的数据库，默认为是
spring.batch.initializer.enabled=true

#设置数据库
#spring.batch.schema=

#设置 Spring Batch 数据库表的前缀
#spring.batch.table-prefix=
```

#### 2，新建Spring Boot项目
依赖：JDBC，Batch，Web，Oracle驱动，hibernate-validator（数据校验）
**注：**Spring Batch 会自动加载 `hsqldb` 驱动，要去除

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-batch</artifactId>
  <exclusions>
      <exclusion>
          <groupId>org.hsqldb</groupId>
          <artifactId>hsqldb</artifactId>
      </exclusion>
  </exclusions>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
  <exclusions>
      <exclusion>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-tomcat</artifactId>
      </exclusion>
  </exclusions>
</dependency>

<dependency>
  <groupId>com.oracle</groupId>
  <artifactId>ojdbc6</artifactId>
  <version>11.2.0.2.0</version>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>

<dependency>
  <groupId>org.hibernate</groupId>
  <artifactId>hibernate-validator</artifactId>
</dependency>
```

#### 3，准备用来测试的csv数据位于 `src/main/resources/people.csv`
![](https://ws4.sinaimg.cn/large/006tNc79gy1fpdig7od60j30io07tdga.jpg)
![](https://ws3.sinaimg.cn/large/006tNc79gy1fpdijp3hhyj30t209nwgz.jpg)

#### 4，数据表定义，位于 `src/main/resources/schema.sql`
![](https://ws1.sinaimg.cn/large/006tNc79gy1fpdcppm0n6j31540hmjv8.jpg)

#### 5，数据源配置

```prop
spring.datasource.username=root
spring.datasource.password=123
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jackson.serialization.indent-output=true
```
#### 6，Person类

```java
package com.zyf.domain;

import javax.validation.constraints.Size;

/**
 * Created by zyf on 2018/3/15.
 */
public class Person {

	/**
	 * 使用JSR-303校验数据
	 */
	@Size(max = 4,min = 2)
	private String name;
	private int age;
	private String nation;
	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}

```

#### 7，数据处理及校验

**处理**

```jav
package com.zyf.batch;

import com.zyf.domain.Person;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

/**
 * Created by zyf on 2018/3/15.
 */
public class CsvItemProcessor extends ValidatingItemProcessor<Person> {

	@Override
	public Person process(Person item) throws ValidationException {
		//调用自定义的校验器
		super.process(item);

		//处理数据
		if(item.getNation().equals("蜀国")){
			item.setNation("01");
		}else {
			item.setNation("02");
		}

		return item;
	}
}

```

**校验**

```java
package com.zyf.batch;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.InitializingBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;


/**
 * Created by zyf on 2018/3/15.
 */
public class CsvBeanValidator<T> implements Validator<T>,InitializingBean {

	private javax.validation.Validator validator;


	@Override
	public void validate(T t) throws ValidationException {
		//校验数据，得到验证不通过的约束
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
		if(constraintViolations.size() > 0){
			StringBuilder message = new StringBuilder();
			for (ConstraintViolation<T> constraintViolation : constraintViolations) {
				message.append(constraintViolation.getMessage()+"\n");
			}
			throw new ValidationException(message.toString());
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//使用JSR-303 的 Validator 来校验我们的数据
		//此处初始化JSR-303 的 Validator
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

		validator = validatorFactory.usingContext().getValidator();
	}

}

```

#### 8，Job监听

```java
package com.zyf.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * Created by zyf on 2018/3/15.
 */
public class CsvJobListener implements JobExecutionListener {

	long startTime;
	long endTime;


	@Override
	public void beforeJob(JobExecution jobExecution) {
		startTime = System.currentTimeMillis();
		System.out.println("任务开始");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		endTime = System.currentTimeMillis();
		System.out.println("任务结束");

		System.out.println("耗时："+(endTime - startTime) + "ms");
	}
}

```

#### 9，配置

```java
package com.zyf.batch;

import com.zyf.domain.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by zyf on 2018/3/15.
 */
@Configuration
@EnableBatchProcessing//开启批处理的支持
public class CsvBatchConfig {
	@Bean
	public ItemReader<Person> reader(){
		//使用FlatFileItemReader读取文件
		FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
		//设置目标文件的路径，通过ClassPathResource找到类路径
		reader.setResource(new ClassPathResource("p.csv"));
		//将文件中的数据与Person类中的属性一一映射
		reader.setLineMapper(new DefaultLineMapper<Person>(){
			//代码块
			{
				setLineTokenizer(new DelimitedLineTokenizer(){
					//代码块
					{
						setNames(new String[]{"name","age","nation","address"});
					}
				});

				setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>(){
					{
						setTargetType(Person.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public ItemProcessor<Person,Person> processor(){

		//使用我们自定义的CsvItemProcessor
		CsvItemProcessor processor = new CsvItemProcessor();
		//为处理器指定校验器
		processor.setValidator(csvBeanValidator());
		return processor;

	}

	/**
	 *
	 * @param dataSource spring boot 已经为我们定义了 DataSource，Spring能让容器中已有的Bean，以参数的形式注入
	 * @return
	 */
	@Bean
	public ItemWriter<Person> writer(DataSource dataSource){
		//使用jdbc批处理的JdbcBatchItemWriter将数据写入到数据库
		JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		String sql = "insert into PERSON (id,name,age,nation,address) values(PERSON_BATCH.nextval, :name, :age, :nation, :address)";
		//设置要执行批处理的SQL语句
		writer.setSql(sql);
		//配置数据源
		writer.setDataSource(dataSource);
		return writer;
	}

	/**
	 *
	 * @param dataSource 自动注入
	 * @param transactionManager 自动注入
	 * @return
	 * @throws Exception
	 */
	@Bean
	public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
		JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
		jobRepositoryFactoryBean.setDataSource(dataSource);
		jobRepositoryFactoryBean.setTransactionManager(transactionManager);
		jobRepositoryFactoryBean.setDatabaseType("oracle");
		return jobRepositoryFactoryBean.getObject();

	}

	@Bean
	public SimpleJobLauncher jobLauncher(DataSource dataSource,PlatformTransactionManager transactionManager) throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository(dataSource,transactionManager));
		return jobLauncher;
	}

	@Bean
	public Job importJob(JobBuilderFactory jobs,Step s1){
		return jobs.get("importJob")
				.incrementer(new RunIdIncrementer())
				.flow(s1)//为job指定step（要执行什么）
				.end()
				.listener(csvJobListener())//绑定监听器
				.build();
	}

	/**
	 *
	 * @param stepBuilderFactory
	 * @param reader
	 * @param writer
	 * @param processor
	 * @return
	 */
	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory,ItemReader<Person> reader,ItemWriter<Person> writer,ItemProcessor<Person,Person> processor){
		return stepBuilderFactory.get("step1")
				.<Person,Person>chunk(65000)//批处理每次提交65000条数据
				.reader(reader)//给step绑定reader
				.processor(processor)//给step绑定processor
				.writer(writer)//给step绑定writer
				.build();

	}

	@Bean
	public CsvJobListener csvJobListener(){
		return new CsvJobListener();
	}

	@Bean
	public Validator<Person> csvBeanValidator(){
		return new CsvBeanValidator<Person>();
	}
}

```

#### 10，运行测试
![](https://ws2.sinaimg.cn/large/006tNc79gy1fpdifs12q2j30m804gq3n.jpg)
![](https://ws2.sinaimg.cn/large/006tNc79gy1fpdig2hgwqj311i0bcq4x.jpg)
#### 11，通过访问某路径，执行job
**新的配置**
**别忘了吧CsvBatchConfig类的@Configuration注释掉**

```java
package com.zyf.batch;

import com.zyf.domain.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by zyf on 2018/3/15.
 */
@Configuration
@EnableBatchProcessing
public class TriggerBatchConfig {
	@Bean
	@StepScope
	public FlatFileItemReader<Person> reader(@Value("#{jobParameters['input.file.name']}") String pathToFile){
		//使用FlatFileItemReader读取文件
		FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
		//设置目标文件的路径，通过ClassPathResource找到类路径
		reader.setResource(new ClassPathResource(pathToFile));
		//将文件中的数据与Person类中的属性一一映射
		reader.setLineMapper(new DefaultLineMapper<Person>(){
			//代码块
			{
				setLineTokenizer(new DelimitedLineTokenizer(){
					//代码块
					{
						setNames(new String[]{"name","age","nation","address"});
					}
				});

				setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>(){
					{
						setTargetType(Person.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public ItemProcessor<Person,Person> processor(){

		//使用我们自定义的CsvItemProcessor
		CsvItemProcessor processor = new CsvItemProcessor();
		//为处理器指定校验器
		processor.setValidator(csvBeanValidator());
		return processor;

	}

	/**
	 *
	 * @param dataSource spring boot 已经为我们定义了 DataSource，Spring能让容器中已有的Bean，以参数的形式注入
	 * @return
	 */
	@Bean
	public ItemWriter<Person> writer(DataSource dataSource){
		//使用jdbc批处理的JdbcBatchItemWriter将数据写入到数据库
		JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		String sql = "insert into PERSON (id,name,age,nation,address) values(PERSON_BATCH.nextval, :name, :age, :nation, :address)";
		//设置要执行批处理的SQL语句
		writer.setSql(sql);
		//配置数据源
		writer.setDataSource(dataSource);
		return writer;
	}

	/**
	 *
	 * @param dataSource 自动注入
	 * @param transactionManager 自动注入
	 * @return
	 * @throws Exception
	 */
	@Bean
	public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
		JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
		jobRepositoryFactoryBean.setDataSource(dataSource);
		jobRepositoryFactoryBean.setTransactionManager(transactionManager);
		jobRepositoryFactoryBean.setDatabaseType("oracle");
		return jobRepositoryFactoryBean.getObject();

	}

	@Bean
	public SimpleJobLauncher jobLauncher(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository(dataSource,transactionManager));
		return jobLauncher;
	}

	@Bean
	public Job importJob(JobBuilderFactory jobs, Step s1){
		return jobs.get("importJob")
				.incrementer(new RunIdIncrementer())
				.flow(s1)//为job指定step（要执行什么）
				.end()
				.listener(csvJobListener())//绑定监听器
				.build();
	}

	/**
	 *
	 * @param stepBuilderFactory
	 * @param reader
	 * @param writer
	 * @param processor
	 * @return
	 */
	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Person> reader, ItemWriter<Person> writer, ItemProcessor<Person,Person> processor){
		return stepBuilderFactory.get("step1")
				.<Person,Person>chunk(65000)//批处理每次提交65000条数据
				.reader(reader)//给step绑定reader
				.processor(processor)//给step绑定processor
				.writer(writer)//给step绑定writer
				.build();

	}

	@Bean
	public CsvJobListener csvJobListener(){
		return new CsvJobListener();
	}

	@Bean
	public Validator<Person> csvBeanValidator(){
		return new CsvBeanValidator<Person>();
	}

}

```

#### 12，创建Controller

```java
package com.zyf.web.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zyf on 2018/3/15.
 */
@RestController
public class DemoController {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job importJob;

	JobParameters jobParameters;

	@RequestMapping("/imp")
	public String imp(String fileName) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
		String path = fileName +".csv";
		jobParameters = new JobParametersBuilder().addLong("time",System.currentTimeMillis())
				.addString("input.file.name",path)
				.toJobParameters();
		jobLauncher.run(importJob,jobParameters);
		return "ok";
	}
}

```

#### 13，更改自动执行job的配置

![](https://ws3.sinaimg.cn/large/006tNc79gy1fpdjcpi3qtj3188064ta8.jpg)

#### 14，访问 `/imp?fileName=p`

![](https://ws1.sinaimg.cn/large/006tNc79gy1fpdjdlzq0vj30ae03adg2.jpg)


