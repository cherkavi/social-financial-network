package bonclub.office_private.web_service.result;

import java.sql.SQLException;

import javax.naming.NamingException;

import bonclub.office_private.web_service.db_function.DbException;

/**
 * return value from WebService
 * @param <T>
 */
public class WsReturnValue<T> {
	public static enum InternalError{
		/** error during execute Query, such as "Table not found", "Procedure not found" ... */
		SQL,
		/** service error, such as "get DataSource Exception", "convert values from SQL response exception"  */
		Service;
	}
	
	private final static String CLEAR_ERROR_MESSAGE="";
	private final static String CLEAR_ERROR_CODE="0";
	/** execute procedure Error message  */
	private String errorMessage;
	/** execute procedure/function error code  */
	private String errorCode;
	/** return value from procedure/function  */
	private T value;
	
	private InternalError internalErrorKind;
	private String internalErrorMessage;
	
	/**
	 * create Valid 
	 * @param value
	 */
	public WsReturnValue(T value){
		this.value=value;
		errorMessage=CLEAR_ERROR_MESSAGE;
		errorCode=CLEAR_ERROR_CODE;
	}

	/**
	 * create execute SQL error ( DB level )
	 * @param errorCode
	 * @param errorMessage
	 */
	public WsReturnValue(DbException dbException){
		this.errorCode=dbException.getCode();
		this.errorMessage=dbException.getMessage();
	}
	
	/**
	 * create internal server error ( Service level ) 
	 * @param internalError
	 * @param message
	 */
	public WsReturnValue(InternalError internalError, String message){
		this.internalErrorKind=internalError;
		this.internalErrorMessage=message;
	}
	
	/**
	 * create internal server error, which based on SQLException 
	 * @param ex
	 */
	public WsReturnValue(SQLException ex) {
		this(InternalError.SQL, ex.getMessage());
	}

	/**
	 * create internal server error, which based on NamingException ( no such DataSource in JNDI )
	 * @param ex
	 */
	public WsReturnValue(NamingException ex) {
		this(InternalError.Service, ex.getMessage());
	}

	public WsReturnValue(IllegalAccessException ex) {
		this(InternalError.Service, ex.getMessage());
	}

	public WsReturnValue(InstantiationException ex) {
		this(InternalError.Service, ex.getMessage());
	}

	/**
	 * @return - sql error message  
	 * <br />
	 * <small>DB level</small>
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
	/**
	 * @return - sql error code (DB level )
	 * <br />
	 * <small>DB level</small>
	 */
	public String getErrorCode() {
		return errorCode;
	}
	/**
	 * @return - value from procedure/function 
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @return - internal kind of error ( SQL execute, or process SQL response error )
	 * <br />
	 * <small>service level</small>
	 */
	public InternalError getInternalErrorKind() {
		return internalErrorKind;
	}
	
	/**
	 * @return - internal service error message 
	 * <br />
	 * <small>service level</small>
	 */
	public String getInternalErrorMessage() {
		return internalErrorMessage;
	}

	
	
}
