package bc.ws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "activationResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActivationResult extends CommonResult{

    @XmlElement(name="transactionId")
	private Integer idTelegr;

	public ActivationResult(){
		super();
	}
	
	public ActivationResult(String returnCode, String message) {
		super(returnCode, message);
	}

	public void setIdTelegr(Integer value) {
		this.idTelegr=value;
	}

	public Integer getIdTelegr() {
		return idTelegr;
	}
	
}
