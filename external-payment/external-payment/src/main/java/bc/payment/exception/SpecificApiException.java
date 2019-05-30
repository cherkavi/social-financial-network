package bc.payment.exception;

public class SpecificApiException extends GeneralException{

	private static final long serialVersionUID = 1L;
	private int code;
	private String message;

	public SpecificApiException(int code, String message) {
		super(message);
		this.code=code;
		this.message=message;
	}

	public int getCode(){
		return this.code;
	}
	
	public String getMessage(){
		return this.message;
	}
	
}
