package bc.ws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "contributionsCheckFeeResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContributionsCheckFeeResult extends CommonResult{

	private String oprSum;
	private String mtfMargin;
	private String membershipFeeMargin;
	private String shareFeeMargin;
    @XmlElement(name="cardStatusId")
	private Integer idCardStatus;

	public ContributionsCheckFeeResult() {
		super();
	}

	public ContributionsCheckFeeResult(String returnCode, String message) {
		super(returnCode, message);
	}

	public void setIdCardStatus(Integer value) {
		this.idCardStatus=value;
	}

	public void setShareFeeMargin(String value) {
		this.shareFeeMargin=value;
	}

	public void setMembershipFeeMargin(String value) {
		this.membershipFeeMargin=value;
	}

	public void setMtfMargin(String value) {
		this.mtfMargin=value;
	}

	public void setOprSum(String value) {
		this.oprSum=value;
	}

	public String getOprSum() {
		return oprSum;
	}

	public String getMtfMargin() {
		return mtfMargin;
	}

	public String getMembershipFeeMargin() {
		return membershipFeeMargin;
	}

	public String getShareFeeMargin() {
		return shareFeeMargin;
	}

	public Integer getIdCardStatus() {
		return idCardStatus;
	}

	@Override
	public String toString() {
		return "ContributionsCheckFeeResult [oprSum=" + oprSum + ", mtfMargin=" + mtfMargin + ", membershipFeeMargin="
				+ membershipFeeMargin + ", shareFeeMargin=" + shareFeeMargin + ", idCardStatus=" + idCardStatus + "]";
	}
	
}
