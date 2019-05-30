package bc.ws.domain;

public class GetOperationsResult extends CommonResult{

    private Integer transactionId;
    private Integer operationId;
    private String documentNumber;
    private String operationTypeCode;
    private String operationTypeName;
    private String operationDate;
    private String operationAmount;
    private String operationCurrencyCode;
    private String RRN;

    public GetOperationsResult() {
    }

    public GetOperationsResult(Integer returnCode, String message) {
        super(returnCode, message);
    }

    public GetOperationsResult(String returnCode, String message) {
        super(returnCode, message);
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getOperationTypeCode() {
        return operationTypeCode;
    }

    public void setOperationTypeCode(String operationTypeCode) {
        this.operationTypeCode = operationTypeCode;
    }

    public String getOperationTypeName() {
        return operationTypeName;
    }

    public void setOperationTypeName(String operationTypeName) {
        this.operationTypeName = operationTypeName;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public String getOperationAmount() {
        return operationAmount;
    }

    public void setOperationAmount(String operationAmount) {
        this.operationAmount = operationAmount;
    }

    public String getOperationCurrencyCode() {
        return operationCurrencyCode;
    }

    public void setOperationCurrencyCode(String operationCurrencyCode) {
        this.operationCurrencyCode = operationCurrencyCode;
    }

    public String getRRN() {
        return RRN;
    }

    public void setRRN(String RRN) {
        this.RRN = RRN;
    }

    @Override
    public String toString() {
        return "GetOperationsResult{" +
                "transactionId='" + transactionId + '\'' +
                ", operationId='" + operationId + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", operationTypeCode='" + operationTypeCode + '\'' +
                ", operationTypeName='" + operationTypeName + '\'' +
                ", operationDate='" + operationDate + '\'' +
                ", operationAmount='" + operationAmount + '\'' +
                ", operationCurrencyCode='" + operationCurrencyCode + '\'' +
                ", RRN='" + RRN + '\'' +
                '}';
    }
}
