package com.zyf.dao;

import com.zyf.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by zyf on 2018/3/13.
 */

public interface StudentRepository extends JpaRepository<Student,Long> {

}
