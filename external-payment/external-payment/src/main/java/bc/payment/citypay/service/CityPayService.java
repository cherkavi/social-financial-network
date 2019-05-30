package bc.payment.citypay.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bc.payment.citypay.domain.InputValue;
import bc.payment.citypay.domain.OperationResponse;
import bc.payment.citypay.resource.QueryType;
import bc.payment.common.service.CommonService;
import bc.payment.exception.GeneralPaymentException;
import bc.payment.exception.InputParameterException;
import bc.payment.exception.ProcessPaymentException;
import bc.payment.exception.SpecificApiException;
import bc.utils.ConvertUtils;

@Service
public class CityPayService {
	private static final Logger LOGGER=Logger.getLogger(CityPayService.class);
	
	// private final static String VENDOR_NAME="CITYPAY";
	private final static String VENDOR_NAME="PAYBERRY"; // ukrainian name of the CityPay

		
	@Autowired
	CommonService commonService;

	enum Operation{
		/** check account <br /> 
		 * <b>city-pay-spec.pdf</b> page #4 <br />
		 */
		CHECK(QueryType.check){
			@Override
			void checkMandatoryParameters(InputValue input) throws InputParameterException {
				/** внутренний номер запроса проверки (валидационной транзакции) */
				Operation.checkNotNull("transactionId", input.getTransactionId());
				/** идентификатор абонента в информационной системе провайдера */
				Operation.checkNotNull("account", input.getAccount());
			}

			@Override
			void process(InputValue input, CommonService service) throws ProcessPaymentException, SpecificApiException {
				try {
					service.checkAccountForRecharge(VENDOR_NAME, 
													input.getAccount(), 
													ConvertUtils.getCents(input.getAmount()), 
													input.getPayElementId(),
													input.getProviderId(),
													input.getTerminalId(),
													input.getTerminalTransactionId(),
													extractOrderedFields(FIELD_NAME, input.getExtraFields())
													);
				} catch (GeneralPaymentException e) {
					throw new ProcessPaymentException("operation check payment threw exception ", e);
				}
			}
		},

		/** refill account - confirmation of a payment */
		PAYMENT(QueryType.pay){

			@Override
			void checkMandatoryParameters(InputValue input) throws InputParameterException {
				/** внутренний номер запроса проверки (валидационной транзакции) */
				Operation.checkNotNull("transactionId", input.getTransactionId());
				/** дата учёта платежа в системе CITY-PAY */
				Operation.checkNotNull("transactionDate", input.getTransactionDate());
				/** идентификатор абонента в информационной системе провайдера */
				Operation.checkNotNull("account", input.getAccount());
				/** сумма к зачислению на лицевой счёт абонента */
				Operation.checkNotNull("amount", input.getAmount());
			}

			@Override
			void process(InputValue input, CommonService commonService) throws ProcessPaymentException, SpecificApiException {
				try {
					commonService.payment(VENDOR_NAME, 
										input.getTransactionId(), 
										input.getAccount(), 
										ConvertUtils.getCents(input.getAmount()), 
										input.getTransactionDate(), 
										input.getPayElementId(), 
										input.getProviderId(),
										input.getTerminalId(),
										input.getTerminalTransactionId(),
										extractOrderedFields(FIELD_NAME,input.getExtraFields())
										);
				} catch (GeneralPaymentException e) {
					throw new ProcessPaymentException("operation payment threw exception ", e);
				}
			}

		},

		/** cancel payment */
		CANCEL(QueryType.cancel){

			@Override
			void checkMandatoryParameters(InputValue input) throws InputParameterException {
				/** внутренний номер отменяющего платежа в системе CITY-PAY. */
				Operation.checkNotNull("TransactionId", input.getTransactionId());
				/** внутренний номер отменяемого платежа в системе CITY-PAY */
				Operation.checkNotNull("RevertId", input.getRevertId());

				/** дата учёта отменяемого платежа в системе CITY-PAY */
				Operation.checkNotNull("RevertDate", input.getRevertDate());

				/** идентификатор абонента в информационной системе провайдера */
				Operation.checkNotNull("Account", input.getAccount());

				/** сумма, указанная в отменяемой транзакции
				 * <b>Платёж, не завершённый успешно, не может быть отменён.</b> */
				Operation.checkNotNull("Amount", input.getAmount());
			}

			@Override
			void process(InputValue input, CommonService commonService) throws ProcessPaymentException, SpecificApiException {
				try {
					commonService.cancelPayment(input.getTransactionId(), input.getRevertId(), input.getRevertDate(), input.getAccount(), ConvertUtils.getCents(input.getAmount()));
				} catch (GeneralPaymentException e) {
					throw new ProcessPaymentException("error was happend during cancel operation ", e);
				}
			}

		};
		/**
		 * prefix for the name of fields like "field1", "field2", "field3"... 
		 */
		private final static String FIELD_NAME="field";
		
		private QueryType type;

		Operation(QueryType queryType){
			this.type=queryType;
		}

		/**
		 * select operation according input value
		 * @param input
		 * @return @nullable
		 */
		static Operation select(InputValue input) {
			if(input==null){
				return null;
			}
			QueryType queryType=input.getQueryType();
			if(queryType==null){
				return null;
			}
			for(Operation eachOperation:Operation.values()){
				if(eachOperation.type==queryType){
					return eachOperation;
				}
			}
			return null;
		}

		abstract void checkMandatoryParameters(InputValue input) throws InputParameterException;

		abstract void process(InputValue input, CommonService service) throws ProcessPaymentException, SpecificApiException;

		static void checkNotNull(String parameterName, String value) throws InputParameterException{
			if(StringUtils.trimToNull(value)==null){
				throw new InputParameterException("mandatory parameter is empty or null: "+parameterName);
			}
		}

		static void checkNotNull(String parameterName, Number value) throws InputParameterException{
			if(value==null){
				throw new InputParameterException("mandatory parameter is empty or null: "+parameterName);
			}
			Operation.checkNotNull(parameterName, value.toString());
		}

		static void checkNotNull(String parameterName, Date value) throws InputParameterException{
			if(value==null){
				throw new InputParameterException("mandatory parameter is empty or null: "+parameterName);
			}
		}

		final static int MAX_EXTRA_FIELD_INDEX=20;
		/**
		 * retrieve field from map and set them into index place, example:
		 * field1 - 0, field2 - 1, field3 - 2 
		 * @param prefix - which should be removed from start of the string
		 * @param extraFields
		 * @return
		 */
		static List<Pair<String, String>> extractOrderedFields(String prefix, Map<String, String> extraFields) {
			Set<String> keys=new HashSet<String>(extraFields.keySet());
			// remove all keys which is not a "field"+number
			TreeMap<Integer, String> returnValue=new TreeMap<Integer, String>();
			Iterator<String> keysIterator=keys.iterator();
			while(keysIterator.hasNext()){
				String nextKey=keysIterator.next();
				if(!nextKey.startsWith(prefix)){
					keysIterator.remove();
					continue;
				}
				String value=nextKey.substring(prefix.length());
				Integer keyIndex=null;
				try{
					keyIndex=Integer.parseInt(value);
				}catch(NumberFormatException ex){
					keysIterator.remove();
					continue;
				}
				if(keyIndex>MAX_EXTRA_FIELD_INDEX || keyIndex<=0){
					continue;
				}
				returnValue.put(keyIndex, extraFields.get(nextKey));
			}
			
			List<Pair<String, String>> valuesList=new ArrayList<Pair<String, String>>(returnValue.size());
			int index=1;
			for(Map.Entry<Integer, String> eachValue : returnValue.entrySet()){
				if(index!=eachValue.getKey()){
					// rewind the value
					while(index<eachValue.getKey()){
						valuesList.add(new ImmutablePair<String, String>(prefix+index, null));
						index++;
					}
				}
				valuesList.add(new ImmutablePair<String, String>(prefix+index, eachValue.getValue()));
				index++;
				if(index>MAX_EXTRA_FIELD_INDEX){
					break;
				}
			}
			return valuesList;
		}


	}

	/**
	 * facade-point for understanding which operation need to do:
	 * <ul>
	 * 	<li>{@link QueryType#check} - check payment </li>
	 * 	<li>{@link QueryType#pay} - payment </li>
	 * 	<li>{@link QueryType#cancel} - cancel payment </li>
	 * </ul>
	 * see city-pay-spec.pdf <br />
	 * <li>
	 * 		<ul>check account - page 4</ul>
	 * 		<ul>refill account - </ul>
	 * 		<ul>cancel payment - </ul>
	 * 	<ul></ul>
	 * </li>
	 * @param input - common value
	 * @return
	 */
	public OperationResponse execute(InputValue input) throws InputParameterException{
		CityPayService.LOGGER.debug("<<<InputValue for execute: "+input);
		Operation operation=Operation.select(input);
		if(operation==null){
			throw new InputParameterException("Operation did not recognize");
		}
		operation.checkMandatoryParameters(input);
		CommonService.THREAD_TRANSACTION_ID.set(null);
		CommonService.ADDITIONAL_FIELDS.set(null);
		try{
			operation.process(input, this.commonService);
			return OperationResponse.build()
					.setResultCode(0)
					.setTransactionId(input.getTransactionId().toString())
					.setTransactionExt(CommonService.THREAD_TRANSACTION_ID.get())
					.setAdditionalFields(CommonService.ADDITIONAL_FIELDS.get())
					.setComment(StringUtils.EMPTY)
					.setRevertId(input.getRevertId()==null?null:input.getRevertId().toString())
					.setAmount(input.getAmount()==null?null:input.getAmount().toString())
					.create();
		}catch(SpecificApiException specificException){
			return OperationResponse.build()
					.setResultCode(specificException.getCode()) 
					.setTransactionId(input.getTransactionId().toString())
					.setComment(specificException.getMessage())
					.create();
		}catch(ProcessPaymentException ex){
			LOGGER.error("execute exception", ex);
			return OperationResponse.build()
					.setResultCode(2) // Внутренняя ошибка системы
					.setTransactionId(input.getTransactionId().toString())
					.setComment("ask maintenance service").create();
		}
	}

}


