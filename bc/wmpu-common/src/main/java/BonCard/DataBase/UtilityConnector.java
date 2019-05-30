package BonCard.DataBase;
import java.sql.*;

import org.apache.log4j.Logger;

/**
 * вспомогательный класс для получения данных из базы данных 
 * @author cherkashinv
  */
public class UtilityConnector {
	private static Logger LOGGER=Logger.getLogger(UtilityConnector.class);
	
	private UtilityConnector(){
	}
	
	/**
	 * @param query - текст запроса, в котором 1 столбец - value, 2 столбец - text
	 * @param selected_value - строка, которая должна быть выделена 
	 * @return
	 */
	public static String getSelectBodyFromQuery(Connection connection, String query,String selected_value){
		StringBuilder return_value=new StringBuilder();
		ResultSet resultset=null;
		try{
			resultset=connection.createStatement().executeQuery(query);
			while(resultset.next()){
				if(selected_value!=null){
					if(selected_value.equalsIgnoreCase(resultset.getString(1))){
						return_value.append("<option value=\""+resultset.getString(1)+"\" selected>"+resultset.getString(2)+"</option>");
					}else{
						return_value.append("<option value=\""+resultset.getString(1)+"\" >"+resultset.getString(2)+"</option>");
					}
				}else{
					return_value.append("<option value=\""+resultset.getString(1)+"\" >"+resultset.getString(2)+"</option>");
				}
			}
		}catch(SQLException ex){
			LOGGER.error("SQLException: "+ex.getMessage());
		}catch(Exception e){
			LOGGER.error("Exception: "+e.getMessage());
		}finally{
			UtilityConnector.closeQuietly(resultset);
		}
		return return_value.toString();
	}
	
	/**
	 * close connection without any exception ( null, sql will be omitted )
	 * @param connection
	 */
	public static void closeQuietly(Connection connection){
		if(connection!=null){
			try {
				connection.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * close statement quietly
	 * @param statement
	 */
	public static void closeQuietly(Statement statement) {
		if(statement!=null){
			try {
				statement.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * close statement quietly
	 * @param statement
	 */
	public static void closeQuietly(ResultSet rs) {
		if(rs!=null){
			try {
				rs.close();
				if(rs.getStatement()!=null){
					rs.getStatement().close();
				}
			} catch (SQLException e) {
			}
		}
	}
	
	
	/**
	 * execute "standard" function like: <br>
	 * FUNCTION fnc_close_user_sessions(p_username IN VARCHAR2, p_error_message OUT VARCHAR2) RETURN NUMBER <br>
	 * FUNCTION fnc_check_connection(p_username IN VARCHAR2,p_password IN VARCHAR2,p_sessionid IN VARCHAR2,p_error_message OUT VARCHAR2) RETURN NUMBER <br >
	 * @param functionName - name of function
	 * @param parameters - input parameters 
	 * @return
	 * @throws SQLException 
	 */
	public static FunctionExecutionResult executeFunctionByName(Connection connection, String functionName, Object[] parameters) throws SQLException{
		// create text of SQL
		int parametersSize=(parameters==null?0:parameters.length);
		CallableStatement statement=null;
		
		try{
			statement=connection.prepareCall(createStandardSqlText(functionName, parametersSize));
			statement.registerOutParameter(1, java.sql.Types.INTEGER); // register result
			statement.registerOutParameter(1+parametersSize+1, java.sql.Types.VARCHAR);// register error message
			
			// fill object
			for(int index=0;index<parametersSize; index++){
				// set parameter, after output result, start with 1 ( JDBC style )
				statement.setObject(1+index+1, parameters[index]);
			}
			
			// execute
			statement.execute();
			
			return new FunctionExecutionResult(statement.getInt(1), statement.getString(1+parametersSize+1));
		} catch (SQLException e) {
			LOGGER.error("execute function "+functionName+" Exception: "+e.toString());
			return FunctionExecutionResult.errorInstance(e.getMessage());
		}finally{
			UtilityConnector.closeQuietly(statement);
		}
	}
	
	/**
	 * result of execution, <br />
	 * when it's 
	 *
	 */
	public static class FunctionExecutionResult{
		private final static int CRITICAL_ERROR_CODE=-999;
		private int errorCode;
		private String errorMessage;
		
		public FunctionExecutionResult(int code, String message) {
			errorCode=code;
			errorMessage=message;
		}
		
		/**
		 * create object with critical error code
		 * @param message
		 * @return
		 */
		public static FunctionExecutionResult errorInstance(String message) {
			return new FunctionExecutionResult(CRITICAL_ERROR_CODE, message);
		}
		/**
		 * @return the errorCode
		 */
		public int getErrorCode() {
			return errorCode;
		}
		
		/**
		 * @return the errorMessage
		 */
		public String getErrorMessage() {
			return errorMessage;
		}
		
		public boolean isPositive(){
			return this.errorCode<=0;
		}
		
	}
	
	/**
	 * @param functionName
	 * @param countOfParameters
	 * @return
	 * "{? = call CREATE_A_PERSON (?)}"
	 */
	private static String createStandardSqlText(String functionName, int countOfParameters){
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("{? = call "+functionName+" (");
		for(int index=0;index<countOfParameters;index++){
			returnValue.append("?,");
		}
		returnValue.append("?)}");
		return returnValue.toString();
	}
	
}
