package bc.payments.sberbank.report.exception;

public class GeneralReportException extends Exception{

	private static final long serialVersionUID = 1L;

	public GeneralReportException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneralReportException(String message) {
		super(message);
	}

}
