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
