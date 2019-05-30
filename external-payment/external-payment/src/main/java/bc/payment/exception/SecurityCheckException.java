package bc.payment.exception;

public class SecurityCheckException extends ServiceException{

	private static final long serialVersionUID = 1L;

	public SecurityCheckException(String message, Throwable cause) {
		super(message, cause);
	}

	public SecurityCheckException(String message) {
		super(message);
	}

	
}
