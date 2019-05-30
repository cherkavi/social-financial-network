package bc.payment.exception;

/**
 * General exception for payment
 */
public class GeneralPaymentException extends Exception{

	private static final long serialVersionUID = 1L;

	public GeneralPaymentException() {
		super();
	}

	public GeneralPaymentException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneralPaymentException(String message) {
		super(message);
	}

}
