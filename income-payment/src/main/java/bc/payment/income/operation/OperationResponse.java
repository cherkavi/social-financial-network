package bc.payment.income.operation;

import java.io.StringWriter;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * answer for external system 
 */
@Root(name="Response")
public class OperationResponse {
	private final static Logger LOGGER=LoggerFactory.getLogger(OperationResponse.class);
	
	@Element(name="TransactionId")
	private String transactionId;
	@Element(name="RevertId", required=false)
	private String revertId;
	@Element(name="TransactionExt")
	private String transactionExt;
	@Element(name="Amount")
	private String amount;
	@Element(name="ResultCode")
	private String resultCode;
	@Element(name="Comment")
	private String comment;

	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getTransactionExt() {
		return transactionExt;
	}
	public void setTransactionExt(String transactionExt) {
		this.transactionExt = transactionExt;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
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
	
	private final static String XML_HEADER="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	@Override
	public String toString() {
		Serializer serializer = new Persister();
		StringWriter writer=new StringWriter();
		writer.write(XML_HEADER);
		try {
			serializer.write(this, writer);
			return writer.toString();
		} catch (Exception e) {
			LOGGER.error("can't convert object to String", e);
			return null;
		}
	}
}
