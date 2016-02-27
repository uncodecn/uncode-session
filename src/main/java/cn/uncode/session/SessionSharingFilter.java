package cn.uncode.session;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class SessionSharingFilter implements Filter {
	
	private static final int MAX_ACTIVE_TIME = 60*30;//秒
	private static final String MAX_ACTIVE_TIME_KEY = "maxActiveTime";
	
	private ServletContext servletContext;
	private int maxActiveTime = MAX_ACTIVE_TIME;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		servletContext = filterConfig.getServletContext();
		String matString = filterConfig.getInitParameter(MAX_ACTIVE_TIME_KEY);
		if(StringUtils.isNotEmpty(matString)){
			maxActiveTime = Integer.valueOf(matString);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		SessionHttpServletRequestWrapper sessionHttpServletRequestWrapper = new SessionHttpServletRequestWrapper(httpServletRequest, maxActiveTime, servletContext);
        chain.doFilter(sessionHttpServletRequestWrapper, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}
