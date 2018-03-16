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
