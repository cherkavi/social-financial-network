package bc.payment.robokassa.rest;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import bc.payment.PaymentResultOfExternalService;
import bc.payment.exception.GeneralPaymentException;
import bc.payment.exception.PaymentParameterException;
import bc.payment.robokassa.RobokassaPaymentService;
import bc.payment.robokassa.RobokassaRedirectAware;

@Controller
@RequestMapping("/pages")
public class RedirectPages {
	private final static Logger LOGGER=Logger.getLogger(RedirectPages.class);

	@Autowired(required=true)
	private DataSource dataSource;

	@Autowired(required=true)
	private RobokassaPaymentService paymentService;

	@Value("${robokassa.jdbc.schema}")
	private String  generalDbScheme;

	@Autowired
	private RobokassaRedirectAware redirectAware;


	@RequestMapping(value="/success", method = RequestMethod.GET)
	public ModelAndView successPage(
			@RequestParam(value="invId", required=true) Long invoiceId
			// ,@RequestParam(value="SignatureValue", required=true) String signature
			// ,@RequestParam(value="OutSum", required=true) String sum
			) throws SQLException, GeneralPaymentException {

		// if(!RobokassaUtils.isCrcValid(signatureValue, outSum, invId, RobokassaUrl.robokassaPassword)){}
		Connection connection=null;
		try{
			connection=this.dataSource.getConnection();

			//			PaymentExternalVO payment=paymentFinder.find(connection, invoiceId);
			//			if(PaymentType.PAID.equals(payment.getType())){
			//				return new ModelAndView("redirect:" + "http://google.com.ua");
			//			}

			if(this.paymentService.isPaymentSuccessful(invoiceId)){
				this.paymentService.setPaymentExternalSystemResult(connection, Long.toString(invoiceId), PaymentResultOfExternalService.ACKNOWLEDGED);
				return new ModelAndView("redirect:" + this.redirectAware.successRedirectUrl(invoiceId));
			}else{
				RedirectPages.LOGGER.fatal("!!! FRAUD !!!"+invoiceId);
				// TODO FRAUD !!!
				return new ModelAndView("redirect:" + this.redirectAware.failRedirectUrlDefault());
			}
		}finally{
			JdbcUtils.closeConnection(connection);
		}

	}

	@RequestMapping(value="/fail", method = RequestMethod.GET)
	public ModelAndView failPage(
			@RequestParam(value="invId", required=true) Long invoiceId
			// @RequestParam(value="SignatureValue", required=true) String signature,
			// @RequestParam(value="OutSum", required=true) String sum,
			) throws PaymentParameterException, SQLException {
		return new ModelAndView("redirect:" + this.redirectAware.failRedirectUrl(invoiceId));
	}

	@ExceptionHandler(PaymentParameterException.class)
	public ResponseEntity<String> handleCustomException(PaymentParameterException ex) {
		return new ResponseEntity<String>("Payment parameter exception:"+ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(GeneralPaymentException.class)
	public ResponseEntity<String> handleGeneralException(GeneralPaymentException ex) {
		RedirectPages.LOGGER.error("Payment exception:"+ex.getMessage());
		return new ResponseEntity<String>("Payment exception", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<String> handleException(SQLException ex) {
		RedirectPages.LOGGER.error("Internal DB Server Error:"+ex.getMessage());
		return new ResponseEntity<String>("Internal DB Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception ex) {
		RedirectPages.LOGGER.error("Internal Server Error:"+ex.getMessage());
		return new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
	}


}
