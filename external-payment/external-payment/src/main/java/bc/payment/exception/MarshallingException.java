package bc.payment.exception;

public class MarshallingException extends GeneralException {

	private static final long serialVersionUID = 1L;

	public MarshallingException(String message, Throwable cause) {
		super(message, cause);
	}

	public MarshallingException(String message) {
		super(message);
	}

	
}
