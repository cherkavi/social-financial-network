package bc.ws.domain;

public class GetComissionDescriptionResult extends CommonResult{
    private String comissionPercentValue;
    private String comissionFixedValue;
    private String comissionCurrencyCode;
    private String comissionDescriptioin;

    public GetComissionDescriptionResult() {
    }

    public GetComissionDescriptionResult(Integer returnCode, String message) {
        super(returnCode, message);
    }

    public GetComissionDescriptionResult(String returnCode, String message) {
        super(returnCode, message);
    }

    public String getComissionPercentValue() {
        return comissionPercentValue;
    }

    public void setComissionPercentValue(String comissionPercentValue) {
        this.comissionPercentValue = comissionPercentValue;
    }

    public String getComissionFixedValue() {
        return comissionFixedValue;
    }

    public void setComissionFixedValue(String comissionFixedValue) {
        this.comissionFixedValue = comissionFixedValue;
    }

    public String getComissionCurrencyCode() {
        return comissionCurrencyCode;
    }

    public void setComissionCurrencyCode(String comissionCurrencyCode) {
        this.comissionCurrencyCode = comissionCurrencyCode;
    }

    public String getComissionDescriptioin() {
        return comissionDescriptioin;
    }

    public void setComissionDescriptioin(String comissionDescriptioin) {
        this.comissionDescriptioin = comissionDescriptioin;
    }

    @Override
    public String toString() {
        return "GetComissionDescriptionResult{" +
                "comissionPercentValue='" + comissionPercentValue + '\'' +
                ", comissionFixedValue='" + comissionFixedValue + '\'' +
                ", comissionCurrencyCode='" + comissionCurrencyCode + '\'' +
                ", comissionDescriptioin='" + comissionDescriptioin + '\'' +
                '}';
    }
}
