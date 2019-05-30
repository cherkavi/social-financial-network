package bc.payments.sberbank.report.task.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="pay")
public class ReportPayRecord {
	
	//	<agent_date>2009-04-15T11:22:33</agent_date>
	@Attribute(name="agent_date")
	private String agentDate;
	
	//	<pay_id>2345</pay_id>
	@Attribute(name="pay_id")
	private Integer payId;
	
	//	<pay_date>2009-04-15T11:00:12</pay_date>
	@Attribute(name="pay_date")
	private String payDate;
	
	//	<account>54321</account>
	@Attribute(name="account")
	private String account;

	//	<pay_amount>10000</pay_amount>
	@Attribute(name="pay_amount")
	private Integer payAmount;
	
	//	reg_id=”98767”
	@Attribute(name="reg_id")
	private Integer regId;
	
	//	err_code=”99”
	@Attribute(name="err_code", required=false)
	private Integer errCode;

	@Attribute(name="err_text", required=false)
	private String errText;
	 
	@Attribute(name="note",required=false)
	private String note;

	
	public String getAgentDate() {
		return agentDate;
	}

	public Integer getPayId() {
		return payId;
	}

	public String getPayDate() {
		return payDate;
	}

	public String getAccount() {
		return account;
	}

	public Integer getPayAmount() {
		return payAmount;
	}

	public Integer getRegId() {
		return regId;
	}

	public Integer getErrCode() {
		return errCode;
	}

	public String getErrText() {
		return errText;
	}

	public String getNote() {
		return note;
	}

}
