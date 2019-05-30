package bc.ws.exception;

public class PersistentException extends GeneralException{

	private static final long serialVersionUID = 1L;

	public PersistentException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistentException(String message) {
		super(message);
	}

	public PersistentException(Throwable cause) {
		super(cause);
	}

}
