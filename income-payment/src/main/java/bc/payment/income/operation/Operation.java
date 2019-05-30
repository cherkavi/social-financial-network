package bc.payment.income.operation;

import org.apache.camel.Exchange;

import bc.payment.income.exception.OperationException;

public abstract class Operation {
	
	public abstract OperationResponse execute(Exchange exchange) throws OperationException;
	
}
