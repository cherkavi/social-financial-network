package bc.ws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "contributionsCheckProgramResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContributionsCheckProgramResult extends CommonResult{

	private String needPaySum;
	private String tpMembershipLastDate;
	private String tpMembershipFeeSum;
	private boolean needTpMembershipFee;
	private String tpEntranceFee;
	private boolean needTpEntranceFee;
	private boolean needTpAdminConfirm;
	private boolean needTpSubscribe;
    @XmlElement(name="membershipMaxPayMonth")
	private String membershipMaxpayMonth;
	private String membershipNeedPaySum;
	private String membershipNopayMonth;
	private String membershipLastDate;
	private String membershipMonthSum;
	private boolean needMembershipFee;
    @XmlElement(name="cardStatusId")
	private Integer idCardStatus;

	public ContributionsCheckProgramResult() {
		super();
	}

	public ContributionsCheckProgramResult(String returnCode, String message) {
		super(returnCode, message);
	}

	public void setIdCardStatus(Integer value) {
		idCardStatus=value;		
	}

	public void setNeedMembershipFee(boolean value) {
		needMembershipFee=value;		
	}

	public void setMembershipMonthSum(String value) {
		membershipMonthSum=value;
	}

	public void setMembershipLastDate(String value) {
		membershipLastDate=value;
	}

	public void setMembershipNopayMonth(String value) {
		membershipNopayMonth=value;
	}

	public void setMembershipNeedPaySum(String value) {
		membershipNeedPaySum=value;		
	}

	public void setMembershipMaxpayMonth(String value) {
		membershipMaxpayMonth=value;
	}

	public void setNeedTpSubscribe(boolean value) {
		needTpSubscribe=value;
	}

	public void setNeedTpAdminConfirm(boolean value) {
		needTpAdminConfirm=value;
	}

	public void setNeedTpEntranceFee(boolean value) {
		needTpEntranceFee=value;
	}

	public void setTpEntranceFee(String value) {
		tpEntranceFee=value;
	}

	public void setNeedTpMembershipFee(boolean value) {
		needTpMembershipFee=value;
	}

	public void setTpMembershipFeeSum(String value) {
		tpMembershipFeeSum=value;
	}

	public void setTpMembershipLastDate(String value) {
		tpMembershipLastDate=value;
	}

	public void setNeedPaySum(String value) {
		needPaySum=value;
	}

	public String getNeedPaySum() {
		return needPaySum;
	}

	public String getTpMembershipLastDate() {
		return tpMembershipLastDate;
	}

	public String getTpMembershipFeeSum() {
		return tpMembershipFeeSum;
	}

	public boolean isNeedTpMembershipFee() {
		return needTpMembershipFee;
	}

	public String getTpEntranceFee() {
		return tpEntranceFee;
	}

	public boolean isNeedTpEntranceFee() {
		return needTpEntranceFee;
	}

	public boolean isNeedTpAdminConfirm() {
		return needTpAdminConfirm;
	}

	public boolean isNeedTpSubscribe() {
		return needTpSubscribe;
	}

	public String getMembershipMaxpayMonth() {
		return membershipMaxpayMonth;
	}

	public String getMembershipNeedPaySum() {
		return membershipNeedPaySum;
	}

	public String getMembershipNopayMonth() {
		return membershipNopayMonth;
	}

	public String getMembershipLastDate() {
		return membershipLastDate;
	}

	public String getMembershipMonthSum() {
		return membershipMonthSum;
	}

	public boolean isNeedMembershipFee() {
		return needMembershipFee;
	}

	public Integer getIdCardStatus() {
		return idCardStatus;
	}

	@Override
	public String toString() {
		return "ContributionsCheckProgramResult [needPaySum=" + needPaySum + ", tpMembershipLastDate="
				+ tpMembershipLastDate + ", tpMembershipFeeSum=" + tpMembershipFeeSum + ", needTpMembershipFee="
				+ needTpMembershipFee + ", tpEntranceFee=" + tpEntranceFee + ", needTpEntranceFee=" + needTpEntranceFee
				+ ", needTpAdminConfirm=" + needTpAdminConfirm + ", needTpSubscribe=" + needTpSubscribe
				+ ", membershipMaxpayMonth=" + membershipMaxpayMonth + ", membershipNeedPaySum=" + membershipNeedPaySum
				+ ", membershipNopayMonth=" + membershipNopayMonth + ", membershipLastDate=" + membershipLastDate
				+ ", membershipMonthSum=" + membershipMonthSum + ", needMembershipFee=" + needMembershipFee
				+ ", idCardStatus=" + idCardStatus + "]";
	}
	
}
