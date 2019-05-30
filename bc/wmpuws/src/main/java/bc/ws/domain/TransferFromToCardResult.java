package bc.ws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "transferFromToCardResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransferFromToCardResult extends CommonResult{
    @XmlElement(name="transactionId")
	Integer idTelgr;
	String phoneMobile;
	boolean canSendPinInSms;
	String cardTypeError;
	
	public TransferFromToCardResult() {
		super();
	}

	public TransferFromToCardResult(String returnCode, String message) {
		super(returnCode, message);
	}

	public Integer getIdTelgr() {
		return idTelgr;
	}

	public void setIdTelgr(Integer idTelgr) {
		this.idTelgr = idTelgr;
	}

	public String getPhoneMobile() {
		return phoneMobile;
	}

	public void setPhoneMobile(String phoneMobile) {
		this.phoneMobile = phoneMobile;
	}

	public boolean isCanSendPinInSms() {
		return canSendPinInSms;
	}

	public void setCanSendPinInSms(boolean canSendPinInSms) {
		this.canSendPinInSms = canSendPinInSms;
	}

	public String getCardTypeError() {
		return cardTypeError;
	}

	public void setCardTypeError(String cardTypeError) {
		this.cardTypeError = cardTypeError;
	}

	@Override
	public String toString() {
		return "TransferFromToCardResult [idTelgr=" + idTelgr + ", phoneMobile=" + phoneMobile + ", canSendPinInSms="
				+ canSendPinInSms + ", cardTypeError=" + cardTypeError + "]";
	}
	
}
