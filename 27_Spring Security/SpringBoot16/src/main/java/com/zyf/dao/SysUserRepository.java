package com.zyf.dao;

import com.zyf.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zyf on 2018/3/14.
 */
public interface SysUserRepository extends JpaRepository<SysUser,Long> {

	/**
	 * 根据用户名查找用户
	 * @param username
	 * @return
	 */
	SysUser findByUsername(String username);
}
