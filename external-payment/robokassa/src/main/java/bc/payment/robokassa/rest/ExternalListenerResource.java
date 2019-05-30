package bc.payment.robokassa.rest;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bc.payment.PaymentResultOfExternalService;
import bc.payment.exception.GeneralPaymentException;
import bc.payment.exception.PaymentParameterException;
import bc.payment.robokassa.RobokassaParameters;
import bc.payment.robokassa.RobokassaPaymentService;
import bc.payment.robokassa.RobokassaUtils;
import bc.util.ConnectorUtils;

@Controller
@RequestMapping("/listener")
public class ExternalListenerResource {

	@Autowired
	private RobokassaPaymentService service;
	
	@Autowired
	private RobokassaParameters parameters;

	@Autowired
	private DataSource dataSource;

	private static String RESPONSE_PREAMBULA="OK";

	@RequestMapping(value="/confirmPayment", method = RequestMethod.GET)
	public @ResponseBody String confirmPayment(
			@RequestParam(value="InvId", required=true) Long paymentId,
			@RequestParam(value="OutSum", required=true) String amount,
			@RequestParam(value="SignatureValue", required=true) String signature)
					throws GeneralPaymentException, SQLException {
		if(!RobokassaUtils.isCrcResultFromRobokassaValid(Long.toString(paymentId), amount, signature, parameters.getRobokassaPassword2())){
			throw new GeneralPaymentException("response is not recognized");
		}
		Connection connection=null;
		try{
			connection=this.dataSource.getConnection();
			if(this.service.isPaymentSuccessful(paymentId)){ // check external system
				this.service.setPaymentExternalSystemResult(connection, Long.toString(paymentId), PaymentResultOfExternalService.ACKNOWLEDGED);
			}else{
				// TODO !!! FRAUD !!!
				throw new GeneralPaymentException("FAIL confirmation");
			}
			return ExternalListenerResource.RESPONSE_PREAMBULA+paymentId;
		}finally{
			ConnectorUtils.closeQuietly(connection);
		}
	}


	@ExceptionHandler(PaymentParameterException.class)
	public ResponseEntity<String> handleCustomException(PaymentParameterException ex) {
		return new ResponseEntity<String>("Payment parameter exception:"+ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(GeneralPaymentException.class)
	public ResponseEntity<String> handleGeneralException(GeneralPaymentException ex) {
		return new ResponseEntity<String>("Payment exception:"+ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception ex) {
		return new ResponseEntity<String>("Internal Server Error:"+ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
