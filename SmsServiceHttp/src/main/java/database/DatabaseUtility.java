package database;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DatabaseUtility {
	
	public static String SCHEMA_DEFAULT="bc_admin";

	/**
	 * marker interface for parameter of function  
	 */
	public static interface FunctionParameter{
	}
	
	/**
	 * name of SQL function  
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface DatabaseFunction{
		String name();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public static @interface DatabaseFunctionParameter{
		/** position of parameter inside, MUST be unique */
		int order();
		/** direction of parameter OUT ( default, true ) or IN ( false ) */
		boolean directionOut() default  true;
		SQL_TYPE sqlType() default SQL_TYPE.VARCHAR2;
	}
	
	public static enum SQL_TYPE{
		VARCHAR2(Types.VARCHAR),
		NUMBER(Types.INTEGER);
		
		private Integer sqlType;
		private SQL_TYPE(Integer sqlType) {
			// TODO Auto-generated constructor stub
			this.sqlType=sqlType;
		}
		public Integer getType(){
			return this.sqlType;
		}
	}
	
	
	private final static String SQL_PACKAGE_DELIMITER=".";
	private final static String SQL_PARAMETER_DELIMITER=",";
	private final static String SQL_PARAMETER="?";
	
	
	public static String getSqlFunctionDeclaration(FunctionParameter parameter){
		return getSqlFunctionDeclaration(null, parameter);
	}

	/**
	 * 
	 * @param packageName
	 * @param functionParameter
	 * @return
	 * {? = call bc_sms.PACK_BC_SMS_SEND.get_profile_param(?, ?, ?, ?, ?, ?, ?, ?, ?)}
	 */
	static String getSqlFunctionDeclaration(String packageName, FunctionParameter functionParameter){
		String functionName=getFunctionName(functionParameter);
		List<DatabaseFunctionParameter> parameters=getFunctionParameters(functionParameter);
		
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("{? = call ");
		if(packageName!=null){
			returnValue.append(packageName);
			returnValue.append(SQL_PACKAGE_DELIMITER);
		}
		returnValue.append(functionName);
		returnValue.append("(");
		for(int index=0;index<parameters.size(); index++){
			// eachParameter.directionOut()
			if(index>0){
				returnValue.append(SQL_PARAMETER_DELIMITER);
			}
			returnValue.append(SQL_PARAMETER);
		}
		returnValue.append(")}");
		return returnValue.toString();
	}

	private final static String EXECUTE_PHASE_CREATE="create";
	private final static String EXECUTE_PHASE_FILL="fill parameter";
	private final static String EXECUTE_PHASE_EXECUTE="execute";
	private final static String EXECUTE_PHASE_READ="read parameters";
	private final static String EXECUTE_POSITIVE_RESULT="0";
	private final static String DEFAULT_UNKNOWN_EXCEPTION="";
	
	/**
	 * execute function and fill parameter
	 * @param connection - connection to Database
	 * @param prefix - name of package or name of preambula  ( preambula, can contains and user too ) 
	 * @param functionParameter - which will fill after execution 
	 * @throws DatabaseException
	 */
	public static void executeFunctionFillResults(Connection connection, String prefix, FunctionParameter functionParameter) throws DatabaseException{
		CallableStatement statement=null;
		String executePhase=null;
		try {
			executePhase=EXECUTE_PHASE_CREATE;
			String sqlFunction=getSqlFunctionDeclaration(prefix, functionParameter);
			statement = connection.prepareCall(sqlFunction);
 
			executePhase=EXECUTE_PHASE_FILL;
			statement.registerOutParameter(1, Types.VARCHAR);
			int parameterIndex=1;
			for(DatabaseFunctionParameter eachParameter:getFunctionParameters(functionParameter)){
				parameterIndex++;
				if(eachParameter.directionOut()){
					statement.registerOutParameter(parameterIndex, eachParameter.sqlType().getType());
				}else{
					statement.setObject(parameterIndex, getParameterValue(functionParameter, eachParameter));
				}
			}
			
			executePhase=EXECUTE_PHASE_EXECUTE;
			statement.execute();
			
			executePhase=EXECUTE_PHASE_READ;
			// check for return "0"
			if(EXECUTE_POSITIVE_RESULT.equals((String)statement.getObject(1))){
				parameterIndex=1;
				for(DatabaseFunctionParameter eachParameter:getFunctionParameters(functionParameter)){
					parameterIndex++;
					if(eachParameter.directionOut()){
						setParameterValue( functionParameter, eachParameter, statement.getObject(parameterIndex));
					}
				}
			}else{
				String possibleException=DEFAULT_UNKNOWN_EXCEPTION;
				try{
					Object possibleExceptionObject=statement.getObject(parameterIndex);
					if(possibleExceptionObject!=null){
						possibleException=possibleExceptionObject.toString();
					}
				}catch(Exception ex){
					// can't get exception 
					possibleException=DEFAULT_UNKNOWN_EXCEPTION;
				}
				throw new DatabaseException(MessageFormat.format("exception when execute function {0} return result is {1} ( possible Exception is: {2} ", functionParameter, (String)statement.getObject(1), possibleException));
			}
		} catch (SQLException e) {
			throw new DatabaseException(MessageFormat.format("exception when [{0}] function by parameter: {1},   Exception: {2}", executePhase, functionParameter, e.getMessage()));
		}finally{
			if(statement!=null){
				try{
					statement.close();
				}catch(SQLException ex){};
			}
		}
	}
	
	// ------------------------------- UTILITY CLASSES ----------------------

	static void setParameterValue(FunctionParameter functionParameter, DatabaseFunctionParameter parameter, Object objectForSet) {
		for(Field eachField:functionParameter.getClass().getDeclaredFields()){
			eachField.setAccessible(true);
			DatabaseFunctionParameter annotation=eachField.getAnnotation(DatabaseFunctionParameter.class);
			if(parameter.equals(annotation)){
				try {
					eachField.set(functionParameter, objectForSet);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(MessageFormat.format("can't set value: {0}  to parameter: {1}", objectForSet, parameter));
				} catch (IllegalAccessException e) {
					throw new RuntimeException("can't access to  parameter: "+parameter);
				}
			}
		}
	}

	/**
	 * retrieve value of parameter from argument by annotation of field 
	 * @param functionParameter - container with many parameters 
	 * @param eachParameter - annotation of one of them 
	 * @return
	 */
	static Object getParameterValue(FunctionParameter functionParameter,
										DatabaseFunctionParameter eachParameter) {
		for(Field eachField:functionParameter.getClass().getDeclaredFields()){
			eachField.setAccessible(true);
			DatabaseFunctionParameter annotation=eachField.getAnnotation(DatabaseFunctionParameter.class);
			if(eachParameter.equals(annotation)){
				try {
					return eachField.get(functionParameter);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("can't read parameter: "+eachParameter);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("can't access to  parameter: "+eachParameter);
				}
			}
		}
		return null;
	}

	/**
	 * @param functionParameter - parameter of function 
	 * @return name of SQL function 
	 */
	private static String getFunctionName(FunctionParameter functionParameter){
		DatabaseFunction functionName=functionParameter.getClass().getAnnotation(DatabaseFunction.class);
		return functionName.name();
	}
	
	/**
	 * @param functionParameter
	 * @return list of function parameters
	 */
	static List<DatabaseFunctionParameter> getFunctionParameters(FunctionParameter functionParameter){
		List<DatabaseFunctionParameter> returnValue=new ArrayList<DatabaseFunctionParameter>();
		for(Field eachField:functionParameter.getClass().getDeclaredFields()){
			eachField.setAccessible(true);
			DatabaseFunctionParameter annotation=eachField.getAnnotation(DatabaseFunctionParameter.class);
			if(annotation!=null){
				returnValue.add(annotation);
			}
		}
		Collections.sort(returnValue, new Comparator<DatabaseFunctionParameter>(){

			@Override
			public int compare(DatabaseFunctionParameter o1,
					DatabaseFunctionParameter o2) {
				return o1.order()-o2.order();
			}
			
		});
		return returnValue;
	}
	
}
