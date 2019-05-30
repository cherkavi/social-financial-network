package bc.payment.citypay.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import bc.payment.citypay.resource.QueryType;

/**
 * input value for different type of operatoin: payment/check/cancel
 */
public class InputValue {
	private QueryType queryType;
	private Long transactionId;
	private Date transactionDate;
	private Long revertId;
	private Date revertDate;
	private String account;
	private BigDecimal amount;
	private Integer payElementId;
	private Integer providerId;
	private Integer terminalId;
	private Integer terminalTransactionId;
	private BigDecimal amountSum;
	private Map<String, String> extraFields;
	
	public InputValue(
			QueryType queryType
			,Long transactionId
			,Date transactionDate
			,Long revertId
			,Date revertDate
			,String account
			,BigDecimal amount
			,Integer payElementId
			,Integer providerId
			,Integer terminalId
			,Integer terminalTransactionId
			,BigDecimal amountSum
			,Map<String, String> extraFields
			) {
		super();
		this.queryType = queryType;
		this.transactionId = transactionId;
		this.transactionDate=transactionDate;
		this.revertId=revertId;
		this.revertDate=revertDate;
		this.account = account;
		this.amount=amount;
		this.payElementId = payElementId;
		this.providerId = providerId;
		this.terminalId = terminalId;
		this.terminalTransactionId=terminalTransactionId;
		this.amountSum=amountSum;
		this.extraFields=extraFields;
	}

	public QueryType getQueryType() {
		return queryType;
	}


	public Long getTransactionId() {
		return transactionId;
	}


	public String getAccount() {
		return account;
	}


	public Integer getPayElementId() {
		return payElementId;
	}


	public Integer getProviderId() {
		return providerId;
	}


	public Integer getTerminalId() {
		return terminalId;
	}


	public Map<String, String> getExtraFields() {
		return extraFields;
	}
	
	public Date getTransactionDate() {
		return transactionDate;
	}

	public Long getRevertId() {
		return revertId;
	}

	public Date getRevertDate() {
		return revertDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Integer getTerminalTransactionId() {
		return terminalTransactionId;
	}

	public BigDecimal getAmountSum() {
		return amountSum;
	}

	public static String getParamField() {
		return PARAM_FIELD;
	}

	private final static String PARAM_FIELD="field";
	
	public static Map<String, String> readFields(Map<String, String[]> parameterMap) {
		Map<String, String> returnValue=new HashMap<String, String>();
		for(Entry<String, String[]> eachValue:parameterMap.entrySet()){
			if(eachValue.getKey().startsWith(PARAM_FIELD) && eachValue.getValue()!=null && eachValue.getValue().length>0 ){
				returnValue.put(eachValue.getKey(), eachValue.getValue()[0]);
			}
		}
		return returnValue;
	}

	
	
	@Override
	public String toString() {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("\n QueryType:"+queryType);
		returnValue.append("\n TransactionId:"+transactionId);
		returnValue.append("\n Account:"+account);
		returnValue.append("\n PayElementId:"+payElementId);
		returnValue.append("\n ProviderId:"+providerId);
		returnValue.append("\n TerminalId:"+terminalId);
		
		if(extraFields.size()>0){
			for(Entry<String, String> eachEntry:extraFields.entrySet()){
				returnValue.append("\n"+eachEntry.getKey()+" = "+eachEntry.getValue());
			}
		}
		return returnValue.toString();
	}
	
}
