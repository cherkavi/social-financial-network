package bc.payment.exception;

public class GeneralException extends Exception{

	private static final long serialVersionUID = 1L;

	public GeneralException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneralException(String message) {
		super(message);
	}
	
}
