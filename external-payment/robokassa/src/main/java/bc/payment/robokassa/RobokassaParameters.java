package bc.payment.robokassa;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RobokassaParameters {
	private static Logger LOGGER=Logger.getLogger(RobokassaParameters.class);
	
	static final String CONTEXT_VAR_URL="robokassa/payment/url";
	static final String CONTEXT_VAR_LOGIN="robokassa/payment/login";
	static final String CONTEXT_VAR_PASSWORD="robokassa/payment/password";
	static final String CONTEXT_VAR_PASSWORD2="robokassa/payment/password2";
	static final String CONTEXT_VAR_TEST="robokassa/test";
	
	@Value("${robokassa/payment/url}")
	private String robokassaUrl;
	
	/** client identification */
	@Value("${robokassa/payment/login}")
	private String robokassaLogin;
	/** password #1 */
	@Value("${robokassa/payment/password}")
	private String robokassaPassword;
	/** password #2 */
	@Value("${robokassa/payment/password2}")
	private String robokassaPassword2;
	/** need to use Test Web Services ? */
	@Value("${robokassa/test}")
	private Boolean testEndpoint;
	
	@PostConstruct
	private void init() throws NamingException{
		Context environmentContext=null;
		try {
			environmentContext = (Context) new InitialContext().lookup("java:/comp/env");
		} catch (NamingException e) {
			LOGGER.warn("can't initialize ROBOKASSA environment variables", e);
			return;
		}
		if(testEndpoint==null){
			testEndpoint=(Boolean)environmentContext.lookup(CONTEXT_VAR_TEST);
		}
		if(robokassaUrl==null){
			robokassaUrl=(String)environmentContext.lookup(CONTEXT_VAR_URL);
		}
		if(robokassaLogin==null){
			robokassaLogin=(String)environmentContext.lookup(CONTEXT_VAR_LOGIN);
		}
		if(robokassaPassword==null){
			robokassaPassword=(String)environmentContext.lookup(CONTEXT_VAR_PASSWORD);
		}
		if(robokassaPassword2==null){
			robokassaPassword2=(String)environmentContext.lookup(CONTEXT_VAR_PASSWORD2);
		}
	}

	public String getRobokassaUrl() {
		return robokassaUrl;
	}

	public String getRobokassaLogin() {
		return robokassaLogin;
	}

	public String getRobokassaPassword() {
		return robokassaPassword;
	}

	public String getRobokassaPassword2() {
		return robokassaPassword2;
	}

	public Boolean getTestEndpoint() {
		return testEndpoint;
	}

	
}
