package bc.ws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "terminalMenuResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetTargetProgramsResult extends CommonResult{
    private Integer targetProgramId;
    private Integer targetProgramParentId;
    private String targetProgramName;
    private String targetProgramShortName;
    private String targetProgramDescription;
    private String targetProgramInitiator;
    private String targetProgramAdministrator;
    private String targetProgramPartnerName;
    private String targetProgramAddress;
    private String beginActionDate;
    private String endActionDate;
    private String paymentPeriodCode;
    private String paymentPeriodName;
    private String paymentAmount;
    private String minimalPaymentAmount;
    private String paymentCurrencyCode;
    private String paymentPeriodCount;
    private String needSubscribe;
    private String needAdministratorConfirmation;
    private String entranceFeeAmount;
    private String membershipFeeAmount;
    private String membershipPeriodCode;
    private String membershipPeriodValue;
    private String targetProgramImagePath;

    public Integer getTargetProgramId() {
        return targetProgramId;
    }

    public void setTargetProgramId(Integer targetProgramId) {
        this.targetProgramId = targetProgramId;
    }

    public Integer getTargetProgramParentId() {
        return targetProgramParentId;
    }

    public void setTargetProgramParentId(Integer targetProgramParentId) {
        this.targetProgramParentId = targetProgramParentId;
    }

    public String getTargetProgramName() {
        return targetProgramName;
    }

    public void setTargetProgramName(String targetProgramName) {
        this.targetProgramName = targetProgramName;
    }

    public String getTargetProgramShortName() {
        return targetProgramShortName;
    }

    public void setTargetProgramShortName(String targetProgramShortName) {
        this.targetProgramShortName = targetProgramShortName;
    }

    public String getTargetProgramDescription() {
        return targetProgramDescription;
    }

    public void setTargetProgramDescription(String targetProgramDescription) {
        this.targetProgramDescription = targetProgramDescription;
    }

    public String getTargetProgramInitiator() {
        return targetProgramInitiator;
    }

    public void setTargetProgramInitiator(String targetProgramInitiator) {
        this.targetProgramInitiator = targetProgramInitiator;
    }

    public String getTargetProgramAdministrator() {
        return targetProgramAdministrator;
    }

    public void setTargetProgramAdministrator(String targetProgramAdministrator) {
        this.targetProgramAdministrator = targetProgramAdministrator;
    }

    public String getTargetProgramPartnerName() {
        return targetProgramPartnerName;
    }

    public void setTargetProgramPartnerName(String targetProgramPartnerName) {
        this.targetProgramPartnerName = targetProgramPartnerName;
    }

    public String getTargetProgramAddress() {
        return targetProgramAddress;
    }

    public void setTargetProgramAddress(String targetProgramAddress) {
        this.targetProgramAddress = targetProgramAddress;
    }

    public String getBeginActionDate() {
        return beginActionDate;
    }

    public void setBeginActionDate(String beginActionDate) {
        this.beginActionDate = beginActionDate;
    }

    public String getEndActionDate() {
        return endActionDate;
    }

    public void setEndActionDate(String endActionDate) {
        this.endActionDate = endActionDate;
    }

    public String getPaymentPeriodCode() {
        return paymentPeriodCode;
    }

    public void setPaymentPeriodCode(String paymentPeriodCode) {
        this.paymentPeriodCode = paymentPeriodCode;
    }

    public String getPaymentPeriodName() {
        return paymentPeriodName;
    }

    public void setPaymentPeriodName(String paymentPeriodName) {
        this.paymentPeriodName = paymentPeriodName;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getMinimalPaymentAmount() {
        return minimalPaymentAmount;
    }

    public void setMinimalPaymentAmount(String minimalPaymentAmount) {
        this.minimalPaymentAmount = minimalPaymentAmount;
    }

    public String getPaymentCurrencyCode() {
        return paymentCurrencyCode;
    }

    public void setPaymentCurrencyCode(String paymentCurrencyCode) {
        this.paymentCurrencyCode = paymentCurrencyCode;
    }

    public String getPaymentPeriodCount() {
        return paymentPeriodCount;
    }

    public void setPaymentPeriodCount(String paymentPeriodCount) {
        this.paymentPeriodCount = paymentPeriodCount;
    }

    public String getNeedSubscribe() {
        return needSubscribe;
    }

    public void setNeedSubscribe(String needSubscribe) {
        this.needSubscribe = needSubscribe;
    }

    public String getNeedAdministratorConfirmation() {
        return needAdministratorConfirmation;
    }

    public void setNeedAdministratorConfirmation(String needAdministratorConfirmation) {
        this.needAdministratorConfirmation = needAdministratorConfirmation;
    }

    public String getEntranceFeeAmount() {
        return entranceFeeAmount;
    }

    public void setEntranceFeeAmount(String entranceFeeAmount) {
        this.entranceFeeAmount = entranceFeeAmount;
    }

    public String getMembershipFeeAmount() {
        return membershipFeeAmount;
    }

    public void setMembershipFeeAmount(String membershipFeeAmount) {
        this.membershipFeeAmount = membershipFeeAmount;
    }

    public String getMembershipPeriodCode() {
        return membershipPeriodCode;
    }

    public void setMembershipPeriodCode(String membershipPeriodCode) {
        this.membershipPeriodCode = membershipPeriodCode;
    }

    public String getMembershipPeriodValue() {
        return membershipPeriodValue;
    }

    public void setMembershipPeriodValue(String membershipPeriodValue) {
        this.membershipPeriodValue = membershipPeriodValue;
    }

    public String getTargetProgramImagePath() {
        return targetProgramImagePath;
    }

    public void setTargetProgramImagePath(String targetProgramImagePath) {
        this.targetProgramImagePath = targetProgramImagePath;
    }
}
