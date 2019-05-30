package bc.ws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "confirmationResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfirmationResult extends CommonResult{

	private String canSendPinInSms;
	private String phoneMobileConfirm;

	
	public ConfirmationResult() {
		super();
	}

	public ConfirmationResult(String returnCode, String message) {
		super(returnCode, message);
	}

	public void setPhoneMobileConfirm(String value) {
		this.phoneMobileConfirm=value;
	}

	public void setCanSendPinInSms(String value) {
		this.canSendPinInSms=value;
	}

	public String getCanSendPinInSms() {
		return canSendPinInSms;
	}

	public String getPhoneMobileConfirm() {
		return phoneMobileConfirm;
	}

	@Override
	public String toString() {
		return "ConfirmationResult [canSendPinInSms=" + canSendPinInSms + ", phoneMobileConfirm=" + phoneMobileConfirm
				+ "]";
	}
}
