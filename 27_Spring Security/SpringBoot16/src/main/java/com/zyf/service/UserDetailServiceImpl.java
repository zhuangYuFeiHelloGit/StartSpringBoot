package com.zyf.service;


import com.zyf.dao.SysUserRepository;
import com.zyf.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by zyf on 2018/3/14.
 */
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	SysUserRepository sysUserRepository;


	/**
	 * 重写loadUserByUsername方法
	 * 根据用户名获得用户
	 * @param s 用户名
	 * @return 自定义的SysUser实现了UserDetails，所以可以直接返回
	 * @throws UsernameNotFoundException 异常都帮我们封装好了
	 */
	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		SysUser user = sysUserRepository.findByUsername(s);
		if (user == null){
			throw new UsernameNotFoundException("用户名不存在");
		}
		return user;
	}
}
