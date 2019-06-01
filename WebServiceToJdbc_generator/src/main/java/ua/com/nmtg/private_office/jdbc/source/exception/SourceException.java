package ua.com.nmtg.private_office.jdbc.source.exception;

public class SourceException extends Exception{
	private final static long serialVersionUID=1L;
	
	public SourceException(){
		super();
	}
	
	public SourceException(String message){
		super(message);
	}
}
