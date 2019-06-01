package bonclub.office_private.web_service;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import bonclub.office_private.web_service.db_function.DbFunction;

public class AbstractWebServiceFunction {
	
	private final static String JNDI_DATASOURCE="java:/comp/env/jdbc/data_source";
	private DbFunction dbFunction=new DbFunction();
	
	/**
	 * get utility object for retrieve data from Database 
	 * @return
	 */
	protected DbFunction getDbFunction(){
		return this.dbFunction;
	}
	
	/**
	 * decode Http Url string to Java String
	 * @param value
	 * @return
	 */
	@SuppressWarnings("deprecation")
	protected String decode(String value){
		return URLDecoder.decode(value);
	}

	
	private DataSource dataSource;
	
	/**
	 * @return - DataSource to Database 
	 * @throws NamingException
	 */
	protected DataSource getDataSource() throws NamingException{
		if(dataSource==null){
			Context initContext = new InitialContext();
			dataSource=(DataSource)initContext.lookup(JNDI_DATASOURCE);
		}
		return dataSource;
	}
	
	protected Connection getConnection() throws NamingException, SQLException{
		try{
			return this.getDataSource().getConnection();
		}catch(NullPointerException npe){
			throw new NamingException("getDataSource - NPE ");
		}
		
	}
	
	
	protected void safeCloseConnection(Connection connection){
		try{
			if(connection!=null){
				connection.close();
			}
		}catch(SQLException ex){
		}
	}
	
	

}
