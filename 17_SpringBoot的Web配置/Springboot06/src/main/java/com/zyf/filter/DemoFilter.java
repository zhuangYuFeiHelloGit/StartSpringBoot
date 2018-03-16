package com.zyf.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by zyf on 2018/3/7.
 */
public class DemoFilter implements Filter{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

	}

	@Override
	public void destroy() {

	}
}
