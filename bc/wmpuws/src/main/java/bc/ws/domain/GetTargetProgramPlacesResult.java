package bc.ws.domain;

public class GetTargetProgramPlacesResult extends CommonResult{
    private Integer targetProgramPlaceId;
    private String targetProgramPlaceName;
    private String targetProgramPlaceShortName;
    private String targetProgramPlaceAddress;

    public GetTargetProgramPlacesResult() {
    }

    public GetTargetProgramPlacesResult(Integer returnCode, String message) {
        super(returnCode, message);
    }

    public GetTargetProgramPlacesResult(String returnCode, String message) {
        super(returnCode, message);
    }

    public Integer getTargetProgramPlaceId() {
        return targetProgramPlaceId;
    }

    public void setTargetProgramPlaceId(Integer targetProgramPlaceId) {
        this.targetProgramPlaceId = targetProgramPlaceId;
    }

    public String getTargetProgramPlaceName() {
        return targetProgramPlaceName;
    }

    public void setTargetProgramPlaceName(String targetProgramPlaceName) {
        this.targetProgramPlaceName = targetProgramPlaceName;
    }

    public String getTargetProgramPlaceShortName() {
        return targetProgramPlaceShortName;
    }

    public void setTargetProgramPlaceShortName(String targetProgramPlaceShortName) {
        this.targetProgramPlaceShortName = targetProgramPlaceShortName;
    }

    public String getTargetProgramPlaceAddress() {
        return targetProgramPlaceAddress;
    }

    public void setTargetProgramPlaceAddress(String targetProgramPlaceAddress) {
        this.targetProgramPlaceAddress = targetProgramPlaceAddress;
    }

    @Override
    public String toString() {
        return "GetTargetProgramPlacesResult{" +
                "targetProgramPlaceId='" + targetProgramPlaceId + '\'' +
                ", targetProgramPlaceName='" + targetProgramPlaceName + '\'' +
                ", targetProgramPlaceShortName='" + targetProgramPlaceShortName + '\'' +
                ", targetProgramPlaceAddress='" + targetProgramPlaceAddress + '\'' +
                '}';
    }
}
