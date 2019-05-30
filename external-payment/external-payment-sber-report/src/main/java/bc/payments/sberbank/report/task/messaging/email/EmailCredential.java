package bc.payments.sberbank.report.task.messaging.email;

public class EmailCredential {
	private String emailUrl;
	private int portNumber;
	private String emailLogin;
	private String emailPassword;
	
	public EmailCredential(String emailUrl, int emailPortNumber, String emailLogin, String emailPassword) {
		super();
		this.emailUrl = emailUrl;
		this.portNumber = emailPortNumber;
		this.emailLogin = emailLogin;
		this.emailPassword = emailPassword;
	}

	public String getEmailHost() {
		return emailUrl;
	}

	public int getEmailPort() {
		return portNumber;
	}

	public String getEmailLogin() {
		return emailLogin;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	@Override
	public String toString() {
		return "EmailCredential [emailUrl=" + emailUrl + ", portNumber=" + portNumber + ", emailLogin=" + emailLogin
				+ ", emailPassword=" + emailPassword + "]";
	}
	
}
