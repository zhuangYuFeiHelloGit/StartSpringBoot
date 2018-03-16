package com.zyf.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zyf on 2018/3/14.
 * 实现UserDetails接口后，Spring Security就可以管理SysUser了
 */
@Entity
public class SysUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	private String username;
	private String password;

	/**
	 * cascade = {CascadeType.REFRESH}：级联刷新，获取User时也获取Role
	 * fetch = FetchType.EAGER：关闭懒加载
	 */
	@ManyToMany(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)//配置用户和角色的多对多关系
	private List<SysRole> roles;


	/**
	 * 可以在该方法中，根据自定义逻辑，将用户权限（角色）添加到auths中
	 * @return
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<>();
		List<SysRole> roles = this.getRoles();
		for (SysRole role : roles) {

			auths.add(new SimpleGrantedAuthority(role.getName()));
		}
		return auths;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * 账号是否不过期，false则验证不通过
	 * @return
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 账号是否不锁定，false则验证不通过
	 * @return
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 凭证是否不过期，false则验证不通过
	 * @return
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 该账号是否启用，false为验证不通过
	 * @return
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	public List<SysRole> getRoles() {
		return roles;
	}

	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}
}
