package bc.payment.income.exception;

public class OperationException extends GeneralPaymentException{

	private static final long serialVersionUID = 1L;

	public OperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public OperationException(String message) {
		super(message);
	}

	public OperationException(Throwable cause) {
		super(cause);
	}

}
