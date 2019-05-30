package bc.payment.income.exception;

public class InputParameterException extends GeneralPaymentException{

	private static final long serialVersionUID = 1L;

	public InputParameterException() {
		super();
	}

	public InputParameterException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InputParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public InputParameterException(String message) {
		super(message);
	}

	public InputParameterException(Throwable cause) {
		super(cause);
	}

	
}
