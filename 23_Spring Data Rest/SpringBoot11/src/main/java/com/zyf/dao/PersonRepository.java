package com.zyf.dao;

import com.zyf.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by zyf on 2018/3/13.
 */
@RepositoryRestResource(path = "people")
public interface PersonRepository extends JpaRepository<Person,Long> {

	/**
	 * 将该方法，也暴露为REST资源
	 * @param name
	 * @return
	 */
	@RestResource(path = "nameStartsWith",rel = "nameStartsWith")
	Person findByNameStartsWith(@Param("name") String name);
}
