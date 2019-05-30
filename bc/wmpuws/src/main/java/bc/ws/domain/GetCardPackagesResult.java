package bc.ws.domain;

public class GetCardPackagesResult extends CommonResult{
    public GetCardPackagesResult() {
    }

    public GetCardPackagesResult(Integer returnCode, String message) {
        super(returnCode, message);
    }

    public GetCardPackagesResult(String returnCode, String message) {
        super(returnCode, message);
    }

    private Integer cardPackageId;
    private String cardPackageName;
    private String cardPackageDescription;
    private Integer cardStatusId;
    private String cardStatusName;
    private String currencyCode;
    private String currencyName;
    private String currencyShortName;
    private String entranceFeeAmount;
    private String membershipFeeAmount;
    private String membershipFeeMonthCount;
    private String minimalShareFeeAmount;
    private String dealerMarginAmount;
    private String agentMarginAmount;
    private String totalCardPackageAmount;
    private String beginActionDate;
    private String endActionDate;

    public Integer getCardPackageId() {
        return cardPackageId;
    }

    public void setCardPackageId(Integer cardPackageId) {
        this.cardPackageId = cardPackageId;
    }

    public String getCardPackageName() {
        return cardPackageName;
    }

    public void setCardPackageName(String cardPackageName) {
        this.cardPackageName = cardPackageName;
    }

    public String getCardPackageDescription() {
        return cardPackageDescription;
    }

    public void setCardPackageDescription(String cardPackageDescription) {
        this.cardPackageDescription = cardPackageDescription;
    }

    public Integer getCardStatusId() {
        return cardStatusId;
    }

    public void setCardStatusId(Integer cardStatusId) {
        this.cardStatusId = cardStatusId;
    }

    public String getCardStatusName() {
        return cardStatusName;
    }

    public void setCardStatusName(String cardStatusName) {
        this.cardStatusName = cardStatusName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyShortName() {
        return currencyShortName;
    }

    public void setCurrencyShortName(String currencyShortName) {
        this.currencyShortName = currencyShortName;
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

    public String getMembershipFeeMonthCount() {
        return membershipFeeMonthCount;
    }

    public void setMembershipFeeMonthCount(String membershipFeeMonthCount) {
        this.membershipFeeMonthCount = membershipFeeMonthCount;
    }

    public String getMinimalShareFeeAmount() {
        return minimalShareFeeAmount;
    }

    public void setMinimalShareFeeAmount(String minimalShareFeeAmount) {
        this.minimalShareFeeAmount = minimalShareFeeAmount;
    }

    public String getDealerMarginAmount() {
        return dealerMarginAmount;
    }

    public void setDealerMarginAmount(String dealerMarginAmount) {
        this.dealerMarginAmount = dealerMarginAmount;
    }

    public String getAgentMarginAmount() {
        return agentMarginAmount;
    }

    public void setAgentMarginAmount(String agentMarginAmount) {
        this.agentMarginAmount = agentMarginAmount;
    }

    public String getTotalCardPackageAmount() {
        return totalCardPackageAmount;
    }

    public void setTotalCardPackageAmount(String totalCardPackageAmount) {
        this.totalCardPackageAmount = totalCardPackageAmount;
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

    @Override
    public String toString() {
        return "GetCardPackagesResult{" +
                "cardPackageId='" + cardPackageId + '\'' +
                ", cardPackageName='" + cardPackageName + '\'' +
                ", cardPackageDescription='" + cardPackageDescription + '\'' +
                ", cardStatusId='" + cardStatusId + '\'' +
                ", cardStatusName='" + cardStatusName + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", currencyName='" + currencyName + '\'' +
                ", currencyShortName='" + currencyShortName + '\'' +
                ", entranceFeeAmount='" + entranceFeeAmount + '\'' +
                ", membershipFeeAmount='" + membershipFeeAmount + '\'' +
                ", membershipFeeMonthCount='" + membershipFeeMonthCount + '\'' +
                ", minimalShareFeeAmount='" + minimalShareFeeAmount + '\'' +
                ", dealerMarginAmount='" + dealerMarginAmount + '\'' +
                ", agentMarginAmount='" + agentMarginAmount + '\'' +
                ", totalCardPackageAmount='" + totalCardPackageAmount + '\'' +
                ", beginActionDate='" + beginActionDate + '\'' +
                ", endActionDate='" + endActionDate + '\'' +
                '}';
    }
}
