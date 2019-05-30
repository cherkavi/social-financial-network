package bc.payment.common.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import bc.payment.exception.GeneralPaymentException;
import bc.payment.exception.SpecificApiException;
import bc.util.ConnectorUtils;
import bc.util.jdbc.CommonFunction;

@Component
public class CommonService {
	private final static Logger LOGGER=Logger.getLogger(CommonService.class);
	public static final ThreadLocal<Long> THREAD_TRANSACTION_ID=new ThreadLocal<Long>();
	public static final ThreadLocal<Map<String, String>> ADDITIONAL_FIELDS=new ThreadLocal<Map<String, String>>();
	private static final int PARAM_COUNT = 5;

	@Autowired
	private DataSource dataSource;

	private final int CURRENCY_DEFAULT_NUMBER=643;

	private static final String SQL_PAYMENT="{? = call  pack$external_payment.add_external_oper(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?,  ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?)}";
	/**
	 * 
	 * @param vendor - name of the vendor
	 * @param paymentId - payment id
	 * @param account - account number
	 * @param amountInCents - sum in cents
	 * @param agentDate - date from agent
	 * @param payElementId - number from remote system
	 * @param terminalId - id of terminal from remote system
	 * @param transactionId - id of transaction from remote system
	 * @throws GeneralPaymentException  when SQL throw it
	 * @throws SpecificApiException exception of API with additional information contains {@link SpecificApiException#getCode()} and {@link SpecificApiException}
	 */
	public void payment(String vendor, final long paymentId, final String account, final int amountInCents, final Date agentDate, Integer payElementId, Integer providerId, Integer terminalId, Integer transactionId) throws GeneralPaymentException, SpecificApiException {
		payment(vendor, paymentId, account, amountInCents, agentDate, payElementId, providerId, terminalId, transactionId, null);
	}

		
	
	/**
	 * payment 
	 * @param vendor - name of vendor ( CITYPAY, SBERBANK )
	 * @param paymentId - id of external payment 
	 * @param account - number of account
	 * @param amountInCents - amount in cents
	 * @param agentDate - date of external system
	 * @param payElementId - nullable, service identifier of Vendor   
	 * @param terminalId - id of terminal
	 * @param transactionId - id of transaction
	 * @param additionalFields - additional fields ( field1..field5, and field6... )
	 * @return <ul>
	 * 	<li> null - successfully executed </li>
	 * 	<li> string - message error </li>
	 * </ul>
	 * @throws GeneralPaymentException - when SQL throw it
	 * @throws SpecificApiException - exception of API with additional information contains {@link SpecificApiException#getCode()} and {@link SpecificApiException}
	 */
	public void payment(String vendor, 
						final long paymentId, 
						final String account, 
						final int amountInCents, 
						final Date agentDate, 
						Integer payElementId, 
						Integer providerId, 
						Integer terminalId, 
						Integer transactionId,
						List<Pair<String, String>> additionalFields) throws GeneralPaymentException, SpecificApiException {
		LOGGER.debug("-payment-");
		THREAD_TRANSACTION_ID.set(null);
		Connection connection=null;
		CallableStatement callableStatement=null;
		try{
			connection=this.dataSource.getConnection();
			callableStatement = connection.prepareCall(SQL_PAYMENT);
			LOGGER.debug(SQL_PAYMENT);
			// возвращаемое значение: 
			// 0 - операция успешная, 
			// не 0 - ошибка (в этом случае в возвращаемом значении p_result_message будет указана сама ошибка)
			callableStatement.registerOutParameter(1, Types.VARCHAR); // function result
//			p_cd_trans_pay_type IN VARCHAR2, = 'SBERBANK' 
			callableStatement.setString(2, vendor);
//			p_id_external_point_trans IN NUMBER, -- pay_id – уникальный номер платежа 'ИД транзакции внешнего терминала/точки'
			callableStatement.setLong(3, paymentId);
//			p_cheque_number IN VARCHAR2,          -- ???? 'Номер чека'
			callableStatement.setNull(4, java.sql.Types.VARCHAR);
//			p_payer IN VARCHAR2,     = param1=payer_name ФИО плательщика 'Необязательный (если необходимо) название плательщика'
			callableStatement.setNull(5, java.sql.Types.VARCHAR);
//			p_receiver IN VARCHAR2,  = account – ключевой параметр. 'Номер карты, которую пополняют'
			callableStatement.setString(6, account);
//			p_date_external_oper IN DATE,  = pay_date – дата платежа. .
			if(agentDate!=null){
				callableStatement.setTimestamp(7, new java.sql.Timestamp(agentDate.getTime()));
			}else{
				callableStatement.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis()));
			}
//			p_amount_external_oper IN NUMBER, = pay_amount – сумма платежа в копейках. 'Сумма пополнения в копейках'
			callableStatement.setInt(8, amountInCents);
//			p_cd_currency IN NUMBER,           =  -- Код валюты (Рубли 643 - значение поля CD_CURRENCY представления VC_CURRENCY_ALL)'
			callableStatement.setInt(9, CURRENCY_DEFAULT_NUMBER);
//			p_desc_external_oper IN VARCHAR2,  = param2. Необязательный. Описание операции/назначение платежа'
			// Сюда писать поля, начиная с field6. Формат: field6=<значение>;field7=<значение>; (и т.д., пары "название поля=значение", в конце точка с запятой)
			callableStatement.setString(10, getParametersWithDelimiter(additionalFields, PARAM_COUNT, ";", "="));
//			p_state_external_oper IN VARCHAR2, =  Состояние операции, пока возможные значение 'SUCCESS' и 'ERROR', при необходимости дополнить значения. В протоколе есть состояния и описаны ошибки - добавить
			callableStatement.setString(11, "SUCCESS");
//			p_id_external_oper_file IN NUMBER
			callableStatement.setNull(12, java.sql.Types.NUMERIC);
			// p_pay_element_id IN VARCHAR2
			if(payElementId==null){
				callableStatement.setNull(13, Types.VARCHAR);
			}else{
				callableStatement.setString(13, payElementId.toString());
			}
			
			// p_provider_id IN VARCHAR2 -- ProviderId
			if(providerId==null){
				callableStatement.setNull(14, Types.VARCHAR);
			}else{
				callableStatement.setString(14, providerId.toString());
			}
			
			// p_terminal_id IN VARCHAR2 -- TerminalId
			if(terminalId==null){
				callableStatement.setNull(15, Types.VARCHAR);
			}else{
				callableStatement.setString(15, terminalId.toString());
			}
			
			// p_terminal_transaction_id IN VARCHAR2  -- TerminalTransactionId
			if(transactionId==null){
				callableStatement.setNull(16, Types.VARCHAR);
			}else{
				callableStatement.setString(16, transactionId.toString());
			}
			// parameters
			for(int index=0; index<PARAM_COUNT;index++){
				setStringParameterFromListKey(callableStatement, 17+(index*2), additionalFields, index);
				setStringParameterFromListValue(callableStatement, 18+(index*2), additionalFields, index);
				// callableStatement.setNull(17+(index*2), Types.VARCHAR);
				// callableStatement.setNull(18+(index*2), Types.VARCHAR);
			}
			
//			p_id_external_oper OUT NUMBER, = reg_id – идентификатор платежа у Оператора. Возвращаемый ИД платежа в системе
			callableStatement.registerOutParameter(27, Types.NUMERIC);
//			p_red_date_external_oper OUT DATE --  дата регистрации платежа у оператора
			callableStatement.registerOutParameter(28, Types.TIMESTAMP); // дата регистрации платежа у оператора

			for(int index=0; index<PARAM_COUNT;index++){
				//p_output_field1_name OUT VARCHAR2
				callableStatement.registerOutParameter(29+(index*2), Types.VARCHAR); // error message
				//p_output_field1_value OUT VARCHAR2
				callableStatement.registerOutParameter(30+(index*2), Types.VARCHAR); // error message
			}
			//	p_result_message OUT VARCHAR2    -- результат вставки
			callableStatement.registerOutParameter(39, Types.VARCHAR); // error message
			
			callableStatement.execute();
			connection.commit();
			
			String errorCode=callableStatement.getString(1);
			
			if (ConnectorUtils.PROCEDURE_RESULT_OK.equalsIgnoreCase(errorCode)) {
				THREAD_TRANSACTION_ID.set(callableStatement.getLong(27));
				return;
			}
			String errorMessage=callableStatement.getString(39);
			LOGGER.warn("error:"+errorMessage);
			throw new SpecificApiException(Integer.parseInt(errorCode), errorMessage);
		}catch(SQLException ex){
			throw new GeneralPaymentException("can't execute function for recharging account", ex);
		}finally{
			JdbcUtils.closeStatement(callableStatement);
			JdbcUtils.closeConnection(connection);
		}
		
	}
	
	private String getParametersWithDelimiter(List<Pair<String, String>> values, int startIndex, String delimiterBetweenElements, String delimiterBetweenKeyValue){
		StringBuilder returnValue=new StringBuilder();
		if( values==null || startIndex>=values.size() ){
			return returnValue.toString();
		}
		for(int index=startIndex;index<values.size();index++){
			if(returnValue.length()>0){
				returnValue.append(delimiterBetweenElements);
			}
			Pair<String, String> nextValue=values.get(index);
			returnValue.append(nextValue.getKey()).append(delimiterBetweenKeyValue).append(nextValue.getValue());
		}
		return returnValue.toString();
	}
/*
	private void setStringParameterFromMap(CallableStatement callableStatement, int index,
			List<String> additionalFields, int fieldIndex) throws SQLException {
		if(additionalFields==null || additionalFields.isEmpty()){
			callableStatement.setNull(index, Types.VARCHAR);
			return;
		}
		String value=additionalFields.size()>fieldIndex?additionalFields.get(fieldIndex):null;
		if(value==null){
			callableStatement.setNull(index, Types.VARCHAR);
		}else{
			callableStatement.setString(index, value);
		}
	}
*/
	private void setStringParameterFromListKey(CallableStatement callableStatement, int index,
										   List<Pair<String, String>> additionalFields, int fieldIndex) throws SQLException {
		if(additionalFields==null || additionalFields.isEmpty()){
			callableStatement.setNull(index, Types.VARCHAR);
			return;
		}
		String value=additionalFields.size()>fieldIndex?additionalFields.get(fieldIndex).getKey():null;
		if(value==null){
			callableStatement.setNull(index, Types.VARCHAR);
		}else{
			callableStatement.setString(index, value);
		}
	}

	private void setStringParameterFromListValue(CallableStatement callableStatement, int index,
											   List<Pair<String, String>> additionalFields, int fieldIndex) throws SQLException {
		if(additionalFields==null || additionalFields.isEmpty()){
			callableStatement.setNull(index, Types.VARCHAR);
			return;
		}
		String value=additionalFields.size()>fieldIndex?additionalFields.get(fieldIndex).getValue():null;
		if(value==null){
			callableStatement.setNull(index, Types.VARCHAR);
		}else{
			callableStatement.setString(index, value);
		}
	}

	private final static String SQL_CHECK_CARD="{? = call PACK$EXTERNAL_PAYMENT.check_card(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,? )}";
	

	/**
	 * @param vendor - name of vendor ( CITYPAY, SBERBANK )
	 * @param account - number of account
	 * @param amountInCents - amount in cents
	 * @param payElementId - nullable, service identifier of Vendor
	 * @param providerId - id of provider
	 * @param terminalId - id of terminal
	 * @param transactionId - id of transaction
	 * @return
	 * <ul>
	 * 	<li>null - procedure result OK</li>
	 *  <li>string - text of error </li>
	 * </ul>
	 * @throws GeneralPaymentException - when function will return error code ( not equals 0 )
	 * @throws SpecificApiException - exception of API with additional information contains {@link SpecificApiException#getCode()} and {@link SpecificApiException#getMessage()}
	 */
	public void checkAccountForRecharge(String vendor, String account, int amountInCents, Integer payElementId, Integer providerId, Integer terminalId, Integer transactionId) throws GeneralPaymentException, SpecificApiException{
		checkAccountForRecharge(vendor, account, amountInCents, payElementId, providerId, terminalId, transactionId, null);
	}
	
	/**
	 * @param vendor - name of vendor ( CITYPAY, SBERBANK )
	 * @param account - number of account
	 * @param amountInCents - amount in cents
	 * @param payElementId - nullable, service identifier of Vendor
	 * @param providerId - id of provider
	 * @param terminalId - id of terminal
	 * @param transactionId - id of transaction
	 * @param additionalFields - field1..field5 ( field6... - will be accumulated in special field )
	 * @return <ul>
	 * 	<li>null - procedure result OK</li>
	 *  <li>string - text of error </li>
	 * </ul>
	 * @throws GeneralPaymentException - when SQL throw it
	 * @throws SpecificApiException - exception of API with additional information contains {@link SpecificApiException#getCode()} and {@link SpecificApiException#getMessage()}
	 */
	public void checkAccountForRecharge(String vendor, 
										String account, 
										int amountInCents, 
										Integer payElementId,
										Integer providerId,
										Integer terminalId,
										Integer transactionId,
										List<Pair<String, String>> additionalFields) throws GeneralPaymentException, SpecificApiException{
		ADDITIONAL_FIELDS.set(null);
		Connection connection=null;
		CallableStatement callableStatement = null;
		LOGGER.debug("check account");
		try {
			connection=this.dataSource.getConnection();
			callableStatement = connection.prepareCall(SQL_CHECK_CARD);
			LOGGER.debug(SQL_CHECK_CARD);
			// возвращаемое значение: 
			// 0 - операция успешная, 
			// не 0 - ошибка (в этом случае в возвращаемом значении p_result_message будет указана сама ошибка)
			callableStatement.registerOutParameter(1, Types.VARCHAR); // function result 
			// p_cd_trans_pay_type IN VARCHAR2 -- = 'SBERBANK' для файлов от Сбербанка
			callableStatement.setString(2, vendor);
			LOGGER.debug(vendor);
			//  Номер карты
			callableStatement.setString(3, account);
			LOGGER.debug(account);
			// Сумма операции в копейках
			callableStatement.setInt(4, amountInCents);
			// p_pay_element_id IN VARCHAR2
			if(payElementId==null){
				callableStatement.setNull(5, Types.VARCHAR);
			}else{
				callableStatement.setString(5, payElementId.toString());
			}
			
			// 6 ,p_provider_id IN VARCHAR2 -- ProviderId
			if(providerId==null){
				callableStatement.setNull(6, Types.VARCHAR);
			}else{
				callableStatement.setString(6, providerId.toString());
			}
			
			// 7 ,p_terminal_id IN VARCHAR2 -- TerminalId
			if(terminalId==null){
				callableStatement.setNull(7, Types.VARCHAR);
			}else{
				callableStatement.setString(7, terminalId.toString());
			}
			
			// 8 ,p_terminal_transaction_id IN VARCHAR2  -- TerminalTransactionId
			if(transactionId==null){
				callableStatement.setNull(8, Types.VARCHAR);
			}else{
				callableStatement.setString(8, transactionId.toString());
			}

			for(int index=0; index<PARAM_COUNT;index++){
				setStringParameterFromListKey(callableStatement, 9+(index*2), additionalFields, index);
				setStringParameterFromListValue(callableStatement, 10+(index*2), additionalFields, index);
			}
			// ,p_receiver_type OUT VARCHAR2 -- возвращаемые значения (что передалось в p_receiver) : ‘INVOICE’ - счет, ‘CARD’ - карта, ‘UNKNOWN’ - неизвестно
			callableStatement.registerOutParameter(19, Types.VARCHAR);

			for(int index=0; index<PARAM_COUNT;index++){
				//,p_output_field1_name OUT VARCHAR2
				callableStatement.registerOutParameter(20+(index*2), Types.VARCHAR);
				//,p_output_field1_value OUT VARCHAR2
				callableStatement.registerOutParameter(21+(index*2), Types.VARCHAR);
			}

			// error message
			callableStatement.registerOutParameter(30, Types.VARCHAR);
			callableStatement.execute();
			connection.commit();
			
			String errorCode=callableStatement.getString(1);
			ADDITIONAL_FIELDS.set(collectToMap(getValuesAsArray(callableStatement, 20, 30) ));
			if(ConnectorUtils.PROCEDURE_RESULT_OK.equalsIgnoreCase(errorCode)){
				return;
			}
			String errorMessage=callableStatement.getString(30);
			LOGGER.warn("error:"+errorMessage);
			throw new SpecificApiException(Integer.parseInt(errorCode), errorMessage);
		} catch (SQLException e) {
			LOGGER.error("can't execute the procedure: "+SQL_CHECK_CARD, e);
			throw new GeneralPaymentException("can't execute function for checking recharge ", e);
		} finally {
			JdbcUtils.closeStatement(callableStatement);
			JdbcUtils.closeConnection(connection);
		}		   	
	}
	
	private String[] getValuesAsArray(CallableStatement callableStatement, int indexBegin, int indexEndExclude) throws SQLException {
		String[] returnValue=new String[indexEndExclude-indexBegin];
		for(int index=indexBegin; index<indexEndExclude; index++){
			returnValue[index-indexBegin]=callableStatement.getString(index);
		}
		return returnValue;
	}


	private Map<String, String> collectToMap(String ... values ) {
		Map<String, String> returnValue=new HashMap<String, String>();
		int index=0;
		while(index<values.length){
			String key=values[index];
			index++;
			if(index>=values.length){
				break;
			}
			if(key==null){
				index++;
				continue;
			}
			returnValue.put(key, values[index]);
			index++;
		}
		return returnValue;
	}

	private final static String SQL_REVERT="{? = call PACK$EXTERNAL_PAYMENT.revert_external_oper(?,?,?,?,?, ?,?,?,?)}";
	
	/**
	 * cancel payment
	 * @param transactionId id of transaction
	 * @param revertId id of revert
	 * @param revertDate date of revert
	 * @param account account
	 * @param cents amount in cents
	 * @return
	 * <ul>
	 * 	<li>null - procedure result OK</li>
	 *  <li>string - text of error </li>
	 * </ul>
	 * @throws GeneralPaymentException when SQL throw exception
	 * @throws SpecificApiException - exception of API with additional information contains {@link SpecificApiException#getCode()} and {@link SpecificApiException#getMessage()}
	 */
	public void cancelPayment(Long transactionId, Long revertId, Date revertDate, String account, int cents) throws GeneralPaymentException, SpecificApiException{
		Connection connection=null;
		CallableStatement callableStatement = null;
		THREAD_TRANSACTION_ID.set(null);
		LOGGER.debug("revert");
		try {
			connection=this.dataSource.getConnection();
			callableStatement = connection.prepareCall(SQL_REVERT);
			LOGGER.debug(SQL_REVERT);
			// возвращаемое значение: 
			// 0 - операция успешная, 
			// не 0 - ошибка (в этом случае в возвращаемом значении p_result_message будет указана сама ошибка)
			callableStatement.registerOutParameter(1, Types.VARCHAR); // function result 
			// p_id_external_point_trans IN NUMBER
			callableStatement.setLong(2, transactionId);
			LOGGER.debug(transactionId);
			//  p_id_revert_trans IN NUMBER
			callableStatement.setLong(3, revertId);
			// ,p_revert_date IN DATE
			callableStatement.setTimestamp(4, new java.sql.Timestamp(revertDate.getTime()));
			// ,p_payer IN VARCHAR2
			callableStatement.setString(5, account);
			// ,p_amount_external_oper IN NUMBER
			callableStatement.setInt(6, cents);
			// ,p_cd_currency IN NUMBER
			callableStatement.setInt(7, CURRENCY_DEFAULT_NUMBER);
			// ,p_id_external_oper OUT NUMBER -- !!!!!!!!!!! ДОБАВЛЕНО НОВОЕ ПОЛЕ - ИД СОЗДАННОЙ ОПЕРАЦИИ
			callableStatement.registerOutParameter(8, Types.NUMERIC);
			// ,p_red_revert_date OUT DATE
			callableStatement.registerOutParameter(9, Types.TIMESTAMP);
			// ,p_result_message OUT VARCHAR2
			callableStatement.registerOutParameter(10, Types.VARCHAR);
			
			// error message
			callableStatement.execute();
			connection.commit();
			
			String errorCode=callableStatement.getString(1);
			if(ConnectorUtils.PROCEDURE_RESULT_OK.equalsIgnoreCase(errorCode)){
				THREAD_TRANSACTION_ID.set(callableStatement.getLong(8));
				return;
			}
			String errorMessage=callableStatement.getString(10);
			LOGGER.warn("error:"+errorMessage);
			throw new SpecificApiException(Integer.parseInt(errorCode), errorMessage);
		} catch (SQLException e) {
			throw new GeneralPaymentException("can't execute function for checking recharge ", e);
		} finally {
			JdbcUtils.closeStatement(callableStatement);
			JdbcUtils.closeConnection(connection);
		}		   	
	}

}


class RedirectFunction extends CommonFunction {
	
	public RedirectFunction(DataSource dataSource) {
		super(dataSource, "pack$external_payment.set_robokassa_redirect", 
				new SqlParameter("p_id_telgr", Types.NUMERIC),
				new SqlParameter("p_redirect_success", Types.VARCHAR),
				new SqlParameter("p_redirect_fail", Types.VARCHAR),
				new SqlOutParameter("p_result_message", Types.VARCHAR)
				);
	}

	public boolean execute(Long idTelgr, String urlSuccess, String urlFail) {
		Map<String, Object> in = new HashMap<String, Object>();
		in.put("p_id_telgr", idTelgr);
		in.put("p_redirect_success", urlSuccess);
		in.put("p_redirect_fail", urlFail);
		
		return executeAndParseResult(in); 
	}
	
}