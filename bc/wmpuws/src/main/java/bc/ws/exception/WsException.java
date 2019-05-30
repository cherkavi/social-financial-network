package bc.ws.exception;

import javax.xml.ws.WebFault;

@WebFault
public class WsException extends Exception{
	
	public static class Message{
		private String text;
		private int code;
		
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
	}

	private static final long serialVersionUID = 1L;
	private Message messageBean;

	public WsException() {
		super();
	}

	public WsException(String message, Throwable cause) {
		super(message, cause);
	}

	public WsException(String message, Throwable cause, Message messageBean) {
		super(message, cause);
		this.messageBean=messageBean;
	}

	public WsException(String message) {
		super(message);
	}

	public WsException(String message, Message messageBean) {
		super(message);
		this.messageBean=messageBean;
	}

	public Message getBeanMessage() {
		return messageBean;
	}

}
