package bc.payment.sberbank.report.parser;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

@Root(name="pay")
public class IncomePayment{
	// agent_date=”2011-05-12 11:22:33”
	@Attribute(name="agent_date")
	private String agentDateString;
	private Date agentDate;

	@Attribute(name="pay_id")
	private String payId;

	// pay_date=”2011-05-12 11:00:12”
	@Attribute(name="pay_date")
	private String payDateString;
	private Date payDate;

	@Attribute(name="account")
	private String account;

	@Attribute(name="pay_amount")
	private BigDecimal payAmount;

	@Attribute(name="reg_id")
	private String reqId;

	@Attribute(name="err_code")
	private Integer errorCode;

	@Attribute(name="note")
	private String note;

	@Commit
	private void postProcessing() {
		SimpleDateFormat sdf=new SimpleDateFormat(DateTimeConverter.DATETIME_FORMAT);
		try {
			this.agentDate=sdf.parse(this.agentDateString);
		} catch (ParseException e) {
			throw new IllegalArgumentException("agent_date expected: "+DateTimeConverter.DATETIME_FORMAT+"  but found: "+this.agentDateString);
		}
		try {
			this.payDate=sdf.parse(this.payDateString);
		} catch (ParseException e) {
			throw new IllegalArgumentException("pay_date expected: "+DateTimeConverter.DATETIME_FORMAT+"  but found: "+this.payDateString);
		}
		// TODO ??? this.payAmount/100;
	}

	public Date getAgentDate() {
		return this.agentDate;
	}

	public String getPayId() {
		return this.payId;
	}

	public Date getPayDate() {
		return this.payDate;
	}

	public String getAccount() {
		return this.account;
	}

	public BigDecimal getPayAmount() {
		return this.payAmount;
	}

	public String getReqId() {
		return this.reqId;
	}

	public Integer getErrorCode() {
		return this.errorCode;
	}

	public String getNote() {
		return this.note;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.payId == null) ? 0 : this.payId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		IncomePayment other = (IncomePayment) obj;
		if (this.payId == null) {
			if (other.payId != null) {
				return false;
			}
		} else if (!this.payId.equals(other.payId)) {
			return false;
		}
		return true;
	}


}
