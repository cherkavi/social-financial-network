package bc.payment.citypay.domain;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import bc.payment.citypay.service.MarshallerCityPay;

/**
 */
@Root(name="Response")
public class CheckingResponse {
	@ElementList(inline=true, empty=true, required=false)
	List<CheckingPayment> responsePayments;

	public CheckingResponse() {
		this(new ArrayList<CheckingPayment>(0));
	}
	
	public CheckingResponse(List<CheckingPayment> responseLine) {
		this.responsePayments = responseLine;
	}

	public List<CheckingPayment> getResponseLine() {
		return responsePayments;
	}

	@Override
	public String toString() {
		return MarshallerCityPay.toXmlString(this);
	}
	
}
