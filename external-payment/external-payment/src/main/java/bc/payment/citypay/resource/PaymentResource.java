
package bc.payment.citypay.resource;


import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bc.payment.citypay.domain.CheckingPayment;
import bc.payment.citypay.domain.CheckingResponse;
import bc.payment.citypay.domain.InputValue;
import bc.payment.citypay.domain.OperationResponse;
import bc.payment.citypay.service.CityPayFinder;
import bc.payment.citypay.service.CityPayService;
import bc.payment.exception.GeneralException;
import bc.payment.exception.InputParameterException;

/**
 * specification: city-pay-spec.pdf <br />
 *
 */
@Controller
@RequestMapping(value = "/citypay" ) // TODO produces = "text/plain;charset=UTF-8"
public class PaymentResource {
	private static final Logger LOGGER=Logger.getLogger(PaymentResource.class);

	@Autowired
	private CityPayService paymentService;

	@Autowired
	private CityPayFinder paymentFinder;

	/**
	 * Пример запроса
	 * <ul>
	 * 	<li>на проверку состояния счета ({@link QueryType#check}) абонента</li>
	 * 	<li>на проверку пополнение счета ({@link QueryType#pay}) абонента</li>
	 * 	<li>на проверку состояния счета ({@link QueryType#check}) абонента</li>
	 * </ul>
	 * @param queryType - запрос на проверку состояния абонента
	 * @param transactionId - внутренний номер запроса проверки (валидационной транзакции) в CITY-PAY
	 * @param account - идентификатор абонента в информационной системе провайдера Возможна передача необязательных переменных в строке запроса:
	 * @param payElementId – идентификатор услуги, предоставляемой провайдером, целое число длиной до 5 знаков (используется, если провайдер предоставляет более 1 услуги)
	 * @param providerId – внутренний идентификатор провайдера в системе CITY-PAY, целое число, длиной до 4 знаков
	 * @param terminalId – Id терминала, целое число длиной до 20 знаков (используется для
	 * field1, field2,..., fieldN – дополнительные поля, передаваемые в строке запроса – могут содержать буквы, цифры и спецсимволы системе CITY-PAY
	 * @return
	 * @throws InputParameterException
	 */
	@RequestMapping(value="payment_app.cgi", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String executeCheckPayCancel(
			@RequestParam(value="QueryType", required=true) QueryType queryType
			,@RequestParam(value="TransactionId", required=true) Long transactionId
			,@RequestParam(value="TransactionDate", required=false) @DateTimeFormat(pattern="yyyyMMddHHmmss") Date transactionDate
			,@RequestParam(value="RevertId", required=false) Long revertId
			,@RequestParam(value="RevertDate", required=false) @DateTimeFormat(pattern="yyyyMMddHHmmss") Date revertDate
			,@RequestParam(value="Account", required=false) String account
			,@RequestParam(value="Amount", required=false) BigDecimal amount
			,@RequestParam(value="PayElementId", required=false) Integer payElementId
			,@RequestParam(value="ProviderId", required=false) Integer providerId
			,@RequestParam(value="TerminalId", required=false) Integer terminalId
			,@RequestParam(value="TerminalTransactionId", required=false) Integer terminalTransactionId
			,@RequestParam(value="AmountSum", required=false) BigDecimal amountSum
			// field1, field2,..., fieldN
			, HttpServletRequest request
			) throws InputParameterException{
		InputValue input=new InputValue(
				queryType
				,transactionId
				,transactionDate
				,revertId
				,revertDate
				,account
				,amount
				,payElementId
				,providerId
				,terminalId
				,terminalTransactionId
				,amountSum
				,InputValue.readFields(request.getParameterMap()));
		return this.paymentService.execute(input).toString();
	}

	/**
	 * Every day automation checking <br />
	 * city-pay-spec.pdf#page_8  <br />
	 *
	 * @param checkDateBegin - date begin
	 * @param checkDateEnd - date end
	 * @return {@link CheckingResponse} it holds list of {@link CheckingPayment}
	 */
	@RequestMapping(value="PayDayReport.html", method = RequestMethod.GET) // , produces = "text/plain;charset=UTF-8"
	@ResponseBody
	public String perDayReport(
			@RequestParam(value="CheckDateBegin", required=true) @DateTimeFormat(pattern="yyyyMMddHHmmss") Date checkDateBegin,
			@RequestParam(value="CheckDateEnd", required=true) @DateTimeFormat(pattern="yyyyMMddHHmmss") Date checkDateEnd,
			@RequestParam(value="PayElementId", required=false) String payElementId
			){
		return this.paymentFinder.findPayments(checkDateBegin, checkDateEnd, payElementId).toString();
	}


	@ExceptionHandler(InputParameterException.class)
	@ResponseBody
	public ResponseEntity<String> handleCustomException(InputParameterException ex) {
		OperationResponse response=new OperationResponse();
		response.setResultCode(23); // Code message: Приём платежа запрещён по техническим причинам (ошибка на стороне провайдера)
		response.setComment("request parameters exception: "+ex.getMessage());
		PaymentResource.LOGGER.error(""+ex.getMessage());
		return new ResponseEntity<String>("Check input parameters of the request: "+ex.getMessage(), HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(GeneralException.class)
	@ResponseBody
	public ResponseEntity<String> handleCustomException(GeneralException ex) {
		OperationResponse response=new OperationResponse();
		response.setResultCode(23); // Code message: Приём платежа запрещён по техническим причинам (ошибка на стороне провайдера)
		response.setComment("general payment exception: "+ex.getMessage());
		PaymentResource.LOGGER.error(""+ex.getMessage());
		return new ResponseEntity<String>("Common Exception, ask provider for clarification", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public ResponseEntity<String> handleCustomException(IllegalArgumentException ex) {
		OperationResponse response=new OperationResponse();
		response.setResultCode(23); // Code message: Приём платежа запрещён по техническим причинам (ошибка на стороне провайдера)
		response.setComment(ex.getMessage());
		return new ResponseEntity<String>("Payment parameter exception:"+ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

}
