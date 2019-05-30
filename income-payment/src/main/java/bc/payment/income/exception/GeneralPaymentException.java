package bc.payment.income.exception;

public class GeneralPaymentException extends Exception{

	private static final long serialVersionUID = 1L;

	public GeneralPaymentException() {
		super();
	}

	public GeneralPaymentException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GeneralPaymentException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneralPaymentException(String message) {
		super(message);
	}

	public GeneralPaymentException(Throwable cause) {
		super(cause);
	}

	
}
