package bc.ws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "paymentResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentResult extends CommonResult{
    @XmlElement(name="transactionId")
	private Integer idTelegr;
	private String sumShareFeeNeed;
	private String shareFeeNeedMargin;
	private String changeMargin;
	private String totalMargin;
	private String calcPointTotal;
	private String calcPointShareholder;
	private String sumPutToShareAccount;
	private String phoneMobile;
	private String sumGetFromShareAccount;
	private Boolean canSendPinInSms;

	
	public PaymentResult() {
		super();
	}

	public PaymentResult(String returnCode, String message) {
		super(returnCode, message);
	}

	public void setIdTelegr(Integer value) {
		this.idTelegr=value;
	}

	public void setSumShareFeeNeed(String value) {
		this.sumShareFeeNeed=value;
	}

	public void setShareFeeNeedMargin(String value) {
		this.shareFeeNeedMargin=value;
	}

	public void setChangeMargin(String value) {
		this.changeMargin=value;
	}

	public void setTotalMargin(String value) {
		this.totalMargin=value;
	}

	public void setCalcPointTotal(String value) {
		this.calcPointTotal=value;
	}

	public void setCalcPointShareholder(String value) {
		this.calcPointShareholder=value;
	}

	public void setSumPutToShareAccount(String value) {
		this.sumPutToShareAccount=value;
	}

	public void setSumGetFromShareAccount(String value) {
		this.sumGetFromShareAccount=value;
	}

	public void setCanSendPinInSms(Boolean value) {
		this.canSendPinInSms=value;
	}

	public Integer getIdTelegr() {
		return idTelegr;
	}

	public String getSumShareFeeNeed() {
		return sumShareFeeNeed;
	}

	public String getShareFeeNeedMargin() {
		return shareFeeNeedMargin;
	}

	public String getChangeMargin() {
		return changeMargin;
	}

	public String getTotalMargin() {
		return totalMargin;
	}

	public String getCalcPointTotal() {
		return calcPointTotal;
	}

	public String getCalcPointShareholder() {
		return calcPointShareholder;
	}

	public String getSumPutToShareAccount() {
		return sumPutToShareAccount;
	}

	public String getSumGetFromShareAccount() {
		return sumGetFromShareAccount;
	}

	public Boolean getCanSendPinInSms() {
		return canSendPinInSms;
	}

	public String getPhoneMobile() {
		return phoneMobile;
	}

	public void setPhoneMobile(String phoneMobile) {
		this.phoneMobile = phoneMobile;
	}

	@Override
	public String toString() {
		return "PaymentResult [idTelegr=" + idTelegr + ", sumShareFeeNeed=" + sumShareFeeNeed + ", shareFeeNeedMargin="
				+ shareFeeNeedMargin + ", changeMargin=" + changeMargin + ", totalMargin=" + totalMargin
				+ ", calcPointTotal=" + calcPointTotal + ", calcPointShareholder=" + calcPointShareholder
				+ ", sumPutToShareAccount=" + sumPutToShareAccount + ", sumGetFromShareAccount="
				+ sumGetFromShareAccount + ", canSendPinInSms=" + canSendPinInSms + "]";
	}
	
}
