package bc.ws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "stornoCancellationResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class StornoCancellationResult extends CommonResult{
    @XmlElement(name="transactionId")
	private Integer calcelTelgrId;

	public StornoCancellationResult() {
		super();
	}
	
	public StornoCancellationResult(String returnCode, String message) {
		super(returnCode, message);
	}

	public Integer getCalcelTelgrId() {
		return calcelTelgrId;
	}

	public void setCalcelTelgrId(Integer calcelTelgrId) {
		this.calcelTelgrId = calcelTelgrId;
	}


}
