package bc.servlet;

import java.io.File;

import javax.servlet.http.HttpServlet;

import org.apache.commons.lang3.StringUtils;

import bc.util.JndiUtils;

/**
 * wrapper of servlet, with ability to read Init Parameter from web.xml, <br />
 * and parse it from JNDI context
 */
public class JndiParamHttpServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	public JndiParamHttpServlet(){
		super();
	}
	
	/**
	 * will parse ${value} as jndi path otherwise will return real value 
	 */
	@Override
	public String getInitParameter(String name) {
		String value=StringUtils.trimToNull(super.getInitParameter(name));
		if(value==null){
			return StringUtils.EMPTY;
		}
		if(!JndiUtils.isJndiValue(value)){
			return value;
		}
		return JndiUtils.readJndi(value);
	}
	
	
	/**
	 * trim path, add to the end of the directory File.separator, if it was not found
	 * @param path
	 * @return
	 */
	protected String correctFolderPath(String path) {
		String returnValue=StringUtils.trimToNull(path);
		if(!StringUtils.endsWith(path, File.separator)){
			returnValue=returnValue+File.separator;
		}
		return returnValue;
	}


}
