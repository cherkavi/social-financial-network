package bc.ws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "admissionFeeResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdmissionFeeResult extends CommonResult{
    @XmlElement(name="transactionId")
	private Integer idTelgr;
    @XmlElement(name="cardStatusId")
	private Integer idCardStatus;
	private String sumShareFeeNeed;
	private String oprSum;
	private String pointFeeMargin;
	private String shareFeeMargin;
	private String membershipFeeMargin;
	private String mtfMargin;
	private String changeMargin;
	private String totalMargin;
	private String membershipLastDate;
	private String calcPointTotal;
	private String calcPointShareholder;
	private String sumPutToShareAccount;
	private String sumGetFromShareAccount;
	private String sumGetPoint;
	private String phoneMobile;
	private boolean canSendPinInSms;	
	
	public AdmissionFeeResult() {
		super();
	}

	public AdmissionFeeResult(String returnCode, String message) {
		super(returnCode, message);
	}

	public Integer getIdTelgr() {
		return idTelgr;
	}

	public void setIdTelgr(Integer idTelgr) {
		this.idTelgr = idTelgr;
	}

	public Integer getIdCardStatus() {
		return idCardStatus;
	}

	public void setIdCardStatus(Integer idCardStatus) {
		this.idCardStatus = idCardStatus;
	}

	public String getSumShareFeeNeed() {
		return sumShareFeeNeed;
	}

	public void setSumShareFeeNeed(String sumShareFeeNeed) {
		this.sumShareFeeNeed = sumShareFeeNeed;
	}

	public String getOprSum() {
		return oprSum;
	}

	public void setOprSum(String oprSum) {
		this.oprSum = oprSum;
	}

	public String getPointFeeMargin() {
		return pointFeeMargin;
	}

	public void setPointFeeMargin(String pointFeeMargin) {
		this.pointFeeMargin = pointFeeMargin;
	}

	public String getShareFeeMargin() {
		return shareFeeMargin;
	}

	public void setShareFeeMargin(String shareFeeMargin) {
		this.shareFeeMargin = shareFeeMargin;
	}

	public String getMembershipFeeMargin() {
		return membershipFeeMargin;
	}

	public void setMembershipFeeMargin(String membershipFeeMargin) {
		this.membershipFeeMargin = membershipFeeMargin;
	}

	public String getMtfMargin() {
		return mtfMargin;
	}

	public void setMtfMargin(String mtfMargin) {
		this.mtfMargin = mtfMargin;
	}

	public String getChangeMargin() {
		return changeMargin;
	}

	public void setChangeMargin(String changeMargin) {
		this.changeMargin = changeMargin;
	}

	public String getTotalMargin() {
		return totalMargin;
	}

	public void setTotalMargin(String totalMargin) {
		this.totalMargin = totalMargin;
	}

	public String getMembershipLastDate() {
		return membershipLastDate;
	}

	public void setMembershipLastDate(String membershipLastDate) {
		this.membershipLastDate = membershipLastDate;
	}

	public String getCalcPointTotal() {
		return calcPointTotal;
	}

	public void setCalcPointTotal(String calcPointTotal) {
		this.calcPointTotal = calcPointTotal;
	}

	public String getCalcPointShareholder() {
		return calcPointShareholder;
	}

	public void setCalcPointShareholder(String calcPointShareholder) {
		this.calcPointShareholder = calcPointShareholder;
	}

	public String getSumPutToShareAccount() {
		return sumPutToShareAccount;
	}

	public void setSumPutToShareAccount(String sumPutToShareAccount) {
		this.sumPutToShareAccount = sumPutToShareAccount;
	}

	public String getSumGetFromShareAccount() {
		return sumGetFromShareAccount;
	}

	public void setSumGetFromShareAccount(String sumGetFromShareAccount) {
		this.sumGetFromShareAccount = sumGetFromShareAccount;
	}

	public String getSumGetPoint() {
		return sumGetPoint;
	}

	public void setSumGetPoint(String sumGetPoint) {
		this.sumGetPoint = sumGetPoint;
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

	@Override
	public String toString() {
		return "AdmissionFeeResult [idTelgr=" + idTelgr + ", idCardStatus=" + idCardStatus + ", sumShareFeeNeed="
				+ sumShareFeeNeed + ", oprSum=" + oprSum + ", pointFeeMargin=" + pointFeeMargin + ", shareFeeMargin="
				+ shareFeeMargin + ", membershipFeeMargin=" + membershipFeeMargin + ", mtfMargin=" + mtfMargin
				+ ", changeMargin=" + changeMargin + ", totalMargin=" + totalMargin + ", membershipLastDate="
				+ membershipLastDate + ", calcPointTotal=" + calcPointTotal + ", calcPointShareholder="
				+ calcPointShareholder + ", sumPutToShareAccount=" + sumPutToShareAccount + ", sumGetFromShareAccount="
				+ sumGetFromShareAccount + ", sumGetPoint=" + sumGetPoint + ", phoneMobile=" + phoneMobile
				+ ", canSendPinInSms=" + canSendPinInSms + "]";
	}
	
}
