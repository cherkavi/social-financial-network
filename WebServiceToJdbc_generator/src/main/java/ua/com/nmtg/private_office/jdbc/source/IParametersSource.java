package ua.com.nmtg.private_office.jdbc.source;

import java.util.TreeSet;

/**
 * interface for get the parameters
 */
public interface IParametersSource {
	
	/** 
	 * @return name of owner ( of procedure/function )
	 * */
	public String getOwner();
	
	/** 
	 * @return name of owner ( of procedure/function )
	 * */
	public String getPackage();
	
	
	/**
	 * @return name of parameters
	 */
	public TreeSet<Parameter> getParameters();
	
}
