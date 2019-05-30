package bc.ws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "certificateResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class CertificateResult extends CommonResult{
    @XmlElement(name="transactionId")
	private Integer idTelegr;
	
	
	public CertificateResult() {
		super();
	}

	public CertificateResult(String returnCode, String message) {
		super(returnCode, message);
	}

	public Integer getIdTelegr() {
		return idTelegr;
	}

	public void setIdTelegr(Integer idTelegr) {
		this.idTelegr = idTelegr;
	}

}
