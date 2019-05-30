package bc.payment.robokassa;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcUtils;

import com.roboxchange.merchant.webservice.CurrenciesList;
import com.roboxchange.merchant.webservice.OperationState;
import com.roboxchange.merchant.webservice.OperationStateResponse;
import com.roboxchange.merchant.webservice.RatesList;
import com.roboxchange.merchant.webservice.Result;
import com.roboxchange.merchant.webservice.ServiceSoap;
import com.roboxchange.merchant.webservice.Service;

import bc.util.ConnectorUtils;
import bc.util.CurrencyUtils;
import bc.payment.PaymentExternalService;
import bc.payment.PaymentResultOfExternalService;
import bc.payment.exception.GeneralPaymentException;

@org.springframework.stereotype.Service
public class RobokassaPaymentService implements PaymentExternalService {
	private static final Logger LOGGER = Logger.getLogger(RobokassaPaymentService.class);
	
	@Autowired
	private RobokassaParameters parameters;

	public boolean isPaymentSuccessful(Long paymentId) {
		Service ss = new Service(Service.WSDL_LOCATION, Service.SERVICE);
		ServiceSoap port = ss.getServiceSoap12();
		// https://www.robokassa.ru/ru/Doc/Ru/Interface.aspx
		// Интерфейс получения состояния оплаты счета
		String crcSignature = RobokassaUtils.crc(parameters.getRobokassaLogin(), Long.toString(paymentId),
				parameters.getRobokassaPassword2());
		OperationStateResponse result = port.opState(parameters.getRobokassaLogin(), paymentId.intValue(), crcSignature);
		return parseResult(result.getResult(), result.getState());
	}

	private final static String DEFAULT_LANG = "ru";

	
	public RatesList calculateComission(BigDecimal amount) {
		// if (RobokassaUrl.testEndpoint) {
		Service ss = new Service(Service.WSDL_LOCATION, Service.SERVICE);
		ServiceSoap port = ss.getServiceSoap12();
		// https://www.robokassa.ru/ru/Doc/Ru/Interface.aspx
		return port.getRates(parameters.getRobokassaLogin(), (String) null, CurrencyUtils.amountToString(amount),
				DEFAULT_LANG);
	}

	public CurrenciesList getCurrencies() {
		// if (RobokassaUrl.testEndpoint) {
		Service ss = new Service(Service.WSDL_LOCATION, Service.SERVICE);
		ServiceSoap port = ss.getServiceSoap12();
		// https://www.robokassa.ru/ru/Doc/Ru/Interface.aspx
		return port.getCurrencies(parameters.getRobokassaLogin(), DEFAULT_LANG);
	}

	
	private boolean parseResult(Result result, OperationState state) {
		if (result == null) {
			LOGGER.error("payment notification, Result is null");
			return false;
		}
		int resultCode = result.getCode();
		if (resultCode == 1) {
			// 1 - неверная цифровая подпись запроса
			// TODO !!! ALERT "внутрення ошибка"
			LOGGER.info("payment notification, ResultCode==1: неверная цифровая подпись запроса");
			return false;
		}
		if (resultCode == 3) {
			// 3 - информация об операции с таким InvoiceID не найдена
			// TODO !!! ALERT "обратитесь в call center"
			LOGGER.info("payment notification, ResultCode==3: информация об операции с таким InvoiceID не найдена");
			return false;
		}
		if (resultCode > 0) {
			LOGGER.info("payment notification, ResultCode: " + resultCode);
			return false;
		}

		if (state == null) {
			LOGGER.error("payment notification, OperationState is null");
			return false;
		}
		int stateCode = state.getCode();
		// отличный от 100 - "в процессе"
		return stateCode == 100;
	}

	@Override
	public void setPaymentExternalSystemResult(Connection connection, String paymentId,
			PaymentResultOfExternalService paymentResult) throws GeneralPaymentException {
		CallableStatement callableStatement = null;

		String mySQL = "{? = call PACK$EXTERNAL_PAYMENT.set_telgr_external_state(?,?,?,?)}";
		try {
			callableStatement = connection.prepareCall(mySQL);
			callableStatement.registerOutParameter(1, Types.VARCHAR); // function
			callableStatement.setString(2, "ROBOKASSA"); // ,p_id_telgr IN NUMBER
			callableStatement.setString(3, paymentId); // ,p_id_telgr IN NUMBER
			callableStatement.setString(4, paymentResult.toString()); // ,p_cd_trans_external_state IN VARCHAR2
			callableStatement.registerOutParameter(5, Types.VARCHAR); // error
																		// message
			callableStatement.execute();

			connection.commit();

			if (!(ConnectorUtils.PROCEDURE_RESULT_OK.equalsIgnoreCase(callableStatement.getString(1)))) {
				throw new GeneralPaymentException("Record was not updated successfully:" + paymentId,
						new GeneralPaymentException(callableStatement.getString(4)));
			}
		} catch (SQLException e) {
			throw new GeneralPaymentException("can't update record:" + paymentId, e);
		} finally {
			JdbcUtils.closeStatement(callableStatement);
		}
	}
}
