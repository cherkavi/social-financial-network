package bc.ws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "stornoReturnResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class StornoReturnResult extends CommonResult{
    @XmlElement(name="transactionId")
	private Integer returnTelegrId;

	public StornoReturnResult() {
		super();
	}

	public StornoReturnResult(String returnCode, String message) {
		super(returnCode, message);
	}

	public Integer getCalcelTelgrId() {
		return returnTelegrId;
	}

	public void setReturnTelegrId(Integer cancelTelegrId) {
		this.returnTelegrId = cancelTelegrId;
	}

    @Override
    public String toString() {
        return "StornoReturnResult{" +
                "returnTelegrId=" + returnTelegrId +  '}';
    }
}
