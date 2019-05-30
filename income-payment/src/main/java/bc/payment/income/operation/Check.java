package bc.payment.income.operation;

import org.apache.camel.Exchange;

public class Check extends Operation{

	@Override
	public OperationResponse execute(Exchange exchange) {
		// TODO remove stub
		OperationResponse response=new OperationResponse();
		response.setTransactionId("11111");
		response.setTransactionExt("2222");
		response.setResultCode("0");
		response.setComment("simple comment");
		response.setAmount("12.25");
		return response;
	}

}
