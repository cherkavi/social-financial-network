package bc.payment.exception;

public class ProcessPaymentException extends GeneralException{

	private static final long serialVersionUID = 1L;

	public ProcessPaymentException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcessPaymentException(String message) {
		super(message);
	}


}
