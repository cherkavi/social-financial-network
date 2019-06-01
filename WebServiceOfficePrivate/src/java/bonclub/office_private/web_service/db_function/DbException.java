package bonclub.office_private.web_service.db_function;

public class DbException extends Exception{

	private static final long serialVersionUID = 1L;
	private final static String CLEAR_CODE="0";
	private String code=CLEAR_CODE;
	
	public DbException() {
		super();
	}
	
	public DbException(String message){
		super(message);
	}

	public DbException(String code, String message){
		super(message);
		this.code=code;
	}
	public String getCode(){
		return this.code;
	}
	
}
