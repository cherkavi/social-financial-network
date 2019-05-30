package bc.payment.exception;

/**
 * 
 * Something wrong with parameter of the payment 
 *
 */
public class PaymentParameterException extends GeneralPaymentException{
	private static final long serialVersionUID = 1L;

	public PaymentParameterException() {
		super();
	}
	
	public PaymentParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public PaymentParameterException(String message) {
		super(message);
	}

}
