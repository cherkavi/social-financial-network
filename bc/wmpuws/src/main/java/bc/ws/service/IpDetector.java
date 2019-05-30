package bc.ws.service;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * write ip address of incoming request into {@link ThreadLocal} variable
 * @author technik
 *
 */
public class IpDetector implements Filter{
	public static final ThreadLocal<String> PARAMETER=new ThreadLocal<String>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		PARAMETER.set(request.getRemoteAddr());
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
