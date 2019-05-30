package bc.ws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "giveCardResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class GiveCardResult extends CommonResult{

	private Boolean canSendPinInSms;
	private String phoneMobile;
	private String sumPutShareAccountChange;
	private String commonMargin;
    @XmlElement(name="transactionId")
	private Integer idTelegr;

	public GiveCardResult() {
		super();
	}

	public GiveCardResult(String returnCode, String message) {
		super(returnCode, message);
	}

	public void setIdTelegr(Integer value) {
		this.idTelegr=value;
	}

	public void setCommonMargin(String value) {
		this.commonMargin=value;
	}

	public void setSumPutShareAccountChange(String value) {
		this.sumPutShareAccountChange=value;
	}

	public void setPhoneMobile(String value) {
		this.phoneMobile=value;
	}

	public void setCanSendPinInSms(Boolean value) {
		this.canSendPinInSms=value;
	}

	public Boolean getCanSendPinInSms() {
		return canSendPinInSms;
	}

	public String getPhoneMobile() {
		return phoneMobile;
	}

	public String getSumPutShareAccountChange() {
		return sumPutShareAccountChange;
	}

	public String getCommonMargin() {
		return commonMargin;
	}

	public Integer getIdTelegr() {
		return idTelegr;
	}

}
