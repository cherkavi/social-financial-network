package bc.payment.robokassa.rest;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bc.payment.PaymentExternalVO;
import bc.payment.PaymentType;
import bc.payment.exception.GeneralPaymentException;
import bc.payment.exception.PaymentParameterException;
import bc.payment.robokassa.RobokassaFinder;
import bc.payment.robokassa.RobokassaPaymentService;
import bc.payment.robokassa.RobokassaRedirectAware;
import bc.payment.robokassa.RobokassaUrl;

@Controller
@RequestMapping("/payment")
public class RobokassaResource {
	private final static Logger LOGGER=Logger.getLogger(RobokassaResource.class);

	@Autowired
	private RobokassaFinder finder;

	@Autowired
	private RobokassaUrl robokassaUrl;
	
	@Autowired
	private RobokassaPaymentService service;
	
	@Autowired
	RobokassaRedirectAware redirectService;
	
	@Autowired
	private DataSource dataSource;

	@Value("${robokassa.jdbc.schema}")
	private String  generalDbScheme;

	private final static String LANG="RU";

	@RequestMapping(value="/generateUrl", method = RequestMethod.GET)
	public @ResponseBody String generateUrl( 
			@RequestParam(value="id", required=true) Long id, 
			@RequestParam(value="amount", required=true) BigDecimal amount,
			@RequestParam(value="shipItem", required=false) String shipItem
			) 
					throws PaymentParameterException {
		return robokassaUrl.makeExternalLink(id, amount, shipItem);
	}

	@RequestMapping(value="/checkPayment", method = RequestMethod.GET)
	public @ResponseBody String isPaymentSuccessful( 
			@RequestParam(value="paymentId", required=true) Long paymentId) 
					throws PaymentParameterException {
		return Boolean.toString(service.isPaymentSuccessful(paymentId));
	}

	@RequestMapping(value="/calculateComission", method = RequestMethod.GET)
	public @ResponseBody Object calculateComission( @RequestParam(value="amount", required=true) BigDecimal amount) 
					throws PaymentParameterException {
		return service.calculateComission(amount);
	}

	@RequestMapping(value="/getCurrencies", method = RequestMethod.GET)
	public @ResponseBody Object getCurrencies( @RequestParam(value="amount", required=true) BigDecimal amount) 
					throws PaymentParameterException {
		return service.getCurrencies();
	}

	/**
	 * find payment list 
	 * @param userId
	 * @param dateBeginInclude
	 * @param dateEndExclude
	 * @param paymentType
	 * @param maxRows
	 * @return
	 * @throws GeneralPaymentException
	 * @throws SQLException
	 */
	@RequestMapping(value="/findPayment", method = RequestMethod.GET)
	public @ResponseBody List<PaymentExternalVO> findPayment( 
				@RequestParam(value="userId", required=true) Long userId,
				@RequestParam(value="dateBeginInclude", required=true) @DateTimeFormat(pattern="yyyy-MM-dd_HH:mm:ss")  Date dateBeginInclude,
				@RequestParam(value="dateEndExclude", required=true) @DateTimeFormat(pattern="yyyy-MM-dd_HH:mm:ss")  Date dateEndExclude,
				@RequestParam(value="paymentType", required=true)  PaymentType paymentType,
				@RequestParam(value="maxRows", required=true) int maxRows) 
						throws GeneralPaymentException, SQLException {
		Connection connection=null;
		try{
			connection=dataSource.getConnection();
			return finder.find(userId, connection,dateBeginInclude, dateEndExclude, paymentType, maxRows, LANG, generalDbScheme);
		}finally{
			JdbcUtils.closeConnection(connection);
		}
	}

	/**
	 * find one certain payment 
	 * @param userId
	 * @param paymentId
	 * @return
	 * @throws GeneralPaymentException
	 * @throws SQLException
	 */
	@RequestMapping(value="/findCertainPayment", method = RequestMethod.GET)
	public @ResponseBody PaymentExternalVO findPayment( 
				@RequestParam(value="userId", required=true) Long userId,
				@RequestParam(value="paymentId", required=true) Long paymentId) 
						throws GeneralPaymentException, SQLException {
		Connection connection=null;
		try{
			connection=dataSource.getConnection();
			return finder.find(userId, connection,paymentId, LANG, generalDbScheme);
		}finally{
			JdbcUtils.closeConnection(connection);
		}
	}
	
	/**
	 * 
	 * @param paymentId
	 * @return
	 * @throws PaymentParameterException
	 */
	@RequestMapping(value="/writeRedirect", method = RequestMethod.GET)
	public @ResponseBody String isPaymentSuccessful( 
			@RequestParam(value="paymentId", required=true) Long paymentId, @RequestParam(value="urlSuccessful", required=true) String urlSuccessful, @RequestParam(value="urlFail", required=true) String urlFail) 
					throws PaymentParameterException {
		return Boolean.toString(redirectService.writeRedirectUrl(paymentId, urlSuccessful, urlFail));
	}
	
	
	@ExceptionHandler(PaymentParameterException.class)
	public ResponseEntity<String> handleCustomException(PaymentParameterException ex) {
		LOGGER.error("Payment parameter exception:"+ex.getMessage());
		return new ResponseEntity<String>("Payment parameter exception:"+ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(GeneralPaymentException.class)
	public ResponseEntity<String> handleGeneralException(GeneralPaymentException ex) {
		LOGGER.error("Payment exception:"+ex.getMessage());
		return new ResponseEntity<String>("Payment exception", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception ex) {
		LOGGER.error("Internal Server Error:"+ex.getMessage());
		return new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
