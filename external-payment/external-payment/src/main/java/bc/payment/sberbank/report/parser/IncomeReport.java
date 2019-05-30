package bc.payment.sberbank.report.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

/**
 * Mapping for incoming report from Sberbank
 * <code> should be considered: encoding="windows-1251" </code>
 */
@Root(name="registry")
public class IncomeReport {
	@Attribute(name="format")
	private String format;

	@Attribute(name="form_date") // form_date=”2011-05-13 12:00:00”>
	private String formDateString;
	private Date formDate;

	@Element(name="reg_date")
	private String regDateString;
	private Date regDate;

	@Element(name="agent_name")
	private String agentName;

	@Element(name="prov_name")
	private String provName;


	@ElementList(name="pays")
	private List<IncomePayment> payments;


	// Validate
	@Commit
	private void postProcessing(){
		SimpleDateFormat sdfDateTime=new SimpleDateFormat(DateTimeConverter.DATETIME_FORMAT);
		try {
			this.formDate=sdfDateTime.parse(this.formDateString);
		} catch (ParseException e) {
			throw new IllegalArgumentException("form_date expected: "+DateTimeConverter.DATETIME_FORMAT+"  but found: "+this.formDateString);
		}
		SimpleDateFormat sdfDate=new SimpleDateFormat(DateConverter.DATE_FORMAT);
		try {
			this.regDate=sdfDate.parse(this.regDateString);
		} catch (ParseException e) {
			throw new IllegalArgumentException("reg_date expected: "+DateConverter.DATE_FORMAT+"  but found: "+this.regDateString);
		}
	}


	public String getFormat() {
		return this.format;
	}

	public Date getFormDate() {
		return this.formDate;
	}


	public Date getRegDate() {
		return this.regDate;
	}


	public String getAgentName() {
		return this.agentName;
	}


	public String getProvName() {
		return this.provName;
	}


	public List<IncomePayment> getPayments() {
		return this.payments;
	}

}


/*
<pay
	agent_date=”2011-05-12 11:22:33”
	pay_id=”2345”
	pay_date=”2011-05-12 11:00:12”
	account=”54321”

	pay_amount=”10000”
	reg_id=”98765”
	err_code=”0”
	note=””

	/>

<pay
	agent_date=”2011-05-12 11:22:35”
	pay_id=”2346”
	pay_date=”2011-05-12 11:00:17”
	account=”65432”

	pay_amount=”20000”
	reg_id=”98767”
	err_code=”99”
	note=”Ошибка подключения к серверу оператора” />
 */