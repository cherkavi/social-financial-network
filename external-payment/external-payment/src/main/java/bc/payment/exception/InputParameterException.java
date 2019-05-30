package bc.payment.exception;

public class InputParameterException extends GeneralException{

	private static final long serialVersionUID = 1L;

	public InputParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public InputParameterException(String message) {
		super(message);
	}

}
