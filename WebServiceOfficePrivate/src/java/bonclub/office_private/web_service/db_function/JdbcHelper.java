package bonclub.office_private.web_service.db_function;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;


public class JdbcHelper {
	private static final Logger logger=Logger.getLogger(JdbcHelper.class);
	/**
	 * get ResultSet by PreparedStatement query and parameters 
	 * @param connection
	 * @param preparedQuery
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet getResultSet(Connection connection, String preparedQuery, Object ... parameters) throws SQLException {
		PreparedStatement statement=connection.prepareStatement(preparedQuery);
		if(parameters!=null){
			for(int index=0;index<parameters.length;index++){
				statement.setObject(index+1, parameters[index]);
			}
		}
		return statement.executeQuery();
	}

	public static Object getPojoFromResultSet(ResultSet rs, Class<?> clazz) throws InstantiationException, IllegalAccessException, SQLException{
		Object objectBean=clazz.newInstance();
		ResultSetMetaData metaData=rs.getMetaData();
		int columnCount=metaData.getColumnCount();
		for(int index=0;index<columnCount;index++){
			String columnName=metaData.getColumnName(index+1);
			try{
				PropertyUtils.setProperty(objectBean, columnName.toLowerCase(), rs.getObject(index+1));
			}catch(InvocationTargetException ite){
				logger.error("can't set property ("+columnName+") to object ("+clazz.getName()+")", ite);
			}catch(NoSuchMethodException nsme){
				logger.error("can't execute the method  ("+columnName+") to object ("+clazz.getName()+")", nsme);
			}catch(IllegalArgumentException iae){
				logger.error("need to change type of parameter  "+clazz.getName()+"#"+columnName+"  Details: "+iae.getMessage(), iae);
			}
		}
		return objectBean;
	}
}
