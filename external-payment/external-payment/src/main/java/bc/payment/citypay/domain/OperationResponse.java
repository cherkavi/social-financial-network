package bc.payment.citypay.domain;

import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import bc.payment.citypay.service.MarshallerCityPay;

/**
 * answer for external system 
 */
@Root(name="Response")
public class OperationResponse {
	
	@Element(name="TransactionId")
	private String transactionId;
	@Element(name="RevertId", required=false)
	private String revertId;
	@Element(name="TransactionExt", required=false)
	private Long transactionExt;
	@Element(name="Amount", required=false)
	private String amount;
	@Element(name="ResultCode")
	private int resultCode;
	@Element(name="Comment", required=false)
	private String comment;
	@ElementMap(name="Fields", entry="field", key="name", attribute=true, inline=false, required=false, empty=false)
	private HashMap<String, String> additionalFields; // need to use HashMap instead Map for avoiding @class attribute inside mappring

	public static class OperationResponseBuilder{
		private final OperationResponse value;
		
		private OperationResponseBuilder(){
			this.value=new OperationResponse();
		}
		
		public OperationResponseBuilder setTransactionId(String transactionId) {
			value.setTransactionId(transactionId);
			return this;
		}

		public OperationResponseBuilder setTransactionExt(Long transactionExt) {
			value.setTransactionExt(transactionExt);
			return this;
		}

		public OperationResponseBuilder setAmount(String amount) {
			value.setAmount(amount);
			return this;
		}

		public OperationResponseBuilder setResultCode(int resultCode) {
			value.setResultCode(resultCode);
			return this;
		}

		public OperationResponseBuilder setComment(String comment) {
			value.setComment(comment);
			return this;
		}

		public OperationResponseBuilder setRevertId(String revertId) {
			value.setRevertId(revertId);
			return this;
		}

		public OperationResponse create(){
			return value;
		}

		public OperationResponseBuilder setAdditionalFields(Map<String, String> map) {
			value.additionalFields=(map==null)?null:new HashMap<String, String>(map);
			return this;
		}
		
	}
	
	public static OperationResponseBuilder build(){
		return new OperationResponseBuilder();
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public Long getTransactionExt() {
		return transactionExt;
	}
	public void setTransactionExt(Long transactionExt) {
		this.transactionExt = transactionExt;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getRevertId() {
		return revertId;
	}
	public void setRevertId(String revertId) {
		this.revertId = revertId;
	}
	
	@Override
	public String toString() {
		return MarshallerCityPay.toXmlString(this);
	}
}
