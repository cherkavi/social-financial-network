package bc.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import bc.bcBase;

public class SessionParametersEmitter implements Filter{
	private final static String ATTRIBUTE_NAME_BEAN="Bean";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// retrieve values
		if(request instanceof HttpServletRequest){
			bc.bcBase bean=retrieveBeanFromSession((HttpServletRequest)request);
			if(bean!=null){
				if(bean.getSessionId().equals( ((HttpServletRequest)request).getSession().getId()) ){
					bcBase.SESSION_PARAMETERS.SESSION_ID.setValue(bean.getSessionId());
					bcBase.SESSION_PARAMETERS.LANG.setValue(bean.getLanguage());
					bcBase.SESSION_PARAMETERS.DATE_FORMAT.setValue(bean.getDateFormat());
				} else {
					// previous session 
					bcBase.SESSION_PARAMETERS.SESSION_ID.setValue(null);
					bcBase.SESSION_PARAMETERS.LANG.setValue(null);
					bcBase.SESSION_PARAMETERS.DATE_FORMAT.setValue(null);
				}
			}else{
				// first session  
				bcBase.SESSION_PARAMETERS.SESSION_ID.setValue(null);
				bcBase.SESSION_PARAMETERS.LANG.setValue(null);
				bcBase.SESSION_PARAMETERS.DATE_FORMAT.setValue(null);
			}
		}
		chain.doFilter(request, response);
	}

	private bc.bcBase retrieveBeanFromSession(HttpServletRequest request) {
		Object value=request.getSession().getAttribute(ATTRIBUTE_NAME_BEAN);
		if(value instanceof bc.bcBase){
			return (bc.bcBase)value;
		}else {
			return null;
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void destroy() {
	}


}
