package com.zyf;

import com.zyf.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zyf on 2018/3/13.
 */
public interface PersonRepository extends JpaRepository<Person,Long> {

}
