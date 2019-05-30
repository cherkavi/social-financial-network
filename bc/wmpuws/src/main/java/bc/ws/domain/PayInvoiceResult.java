package bc.ws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "payInvoiceResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class PayInvoiceResult extends CommonResult{
    @XmlElement(name="transactionId")
	private Integer idTelegr;
	private String sumShareFeeNeed;
	private String sumShareFeeNeedMargin;
	private String changeMargin;
	private String totalMargin;
	private String sumPutToShareAccount;
	private String sumGetFromShareAccount;
	private String phoneMobile;
	private boolean canSendPinInSms;

	
	public PayInvoiceResult() {
		super();
	}

	public PayInvoiceResult(String returnCode, String message) {
		super(returnCode, message);
	}

	public void setIdTelegr(Integer value) {
		this.idTelegr=value;
	}

	public void setSumShareFeeNeed(String value) {
		this.sumShareFeeNeed=value;
	}

	public void setSumShareFeeNeedMargin(String value) {
		this.sumShareFeeNeedMargin=value;
	}

	public void setChangeMargin(String value) {
		this.changeMargin=value;
	}

	public void setTotalMargin(String value) {
		this.totalMargin=value;
	}

	public void setSumPutToShareAccount(String value) {
		this.sumPutToShareAccount=value;
	}

	public void setSumGetFromShareAccount(String value) {
		this.sumGetFromShareAccount=value;
	}

	public void setPhoneMobile(String value) {
		this.phoneMobile=value;
	}

	public void setCanSendPinInSms(boolean value) {
		this.canSendPinInSms=value;
	}

	public Integer getIdTelegr() {
		return idTelegr;
	}

	public String getSumShareFeeNeed() {
		return sumShareFeeNeed;
	}

	public String getSumShareFeeNeedMargin() {
		return sumShareFeeNeedMargin;
	}

	public String getChangeMargin() {
		return changeMargin;
	}

	public String getTotalMargin() {
		return totalMargin;
	}

	public String getSumPutToShareAccount() {
		return sumPutToShareAccount;
	}

	public String getSumGetFromShareAccount() {
		return sumGetFromShareAccount;
	}

	public String getPhoneMobile() {
		return phoneMobile;
	}

	public boolean isCanSendPinInSms() {
		return canSendPinInSms;
	}


}
