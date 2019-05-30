package bc.payment.income.validation;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import bc.payment.income.exception.ValidationException;
import bc.payment.income.transport.CommonHttpDto;

public class ValidationOperationProcessors {

	public static Processor check(){
		return CHECK;
	}
	
	public static Processor pay(){
		return PAY;
	}
	
	public static Processor cancel(){
		return CANCEL;
	}

	private final static  Processor CHECK=new Processor(){
		@Override
		public void process(Exchange exchange) throws ValidationException {
			CommonHttpDto dto=getDto(exchange);
			existenceParameter(dto, "TransactionId");
			existenceParameter(dto, "Account");
		}
	};
	
	private final static  Processor PAY=new Processor(){
		@Override
		public void process(Exchange exchange) throws Exception {
			CommonHttpDto dto=getDto(exchange);
			existenceParameter(dto, "TransactionId");
			existenceParameter(dto, "TransactionDate");
			existenceParameter(dto, "Account");
			existenceParameter(dto, "Amount");
		}
	};
	
	private final static  Processor CANCEL=new Processor(){
		@Override
		public void process(Exchange exchange) throws Exception {
			CommonHttpDto dto=getDto(exchange);
			existenceParameter(dto, "TransactionId");
			existenceParameter(dto, "RevertId");
			existenceParameter(dto, "RevertDate");
			existenceParameter(dto, "Account");
			existenceParameter(dto, "Amount");
		}
	};
	
	private static void existenceParameter(CommonHttpDto dto, String parameterName) throws ValidationException{
		String[] value=dto.getParameters().get(parameterName);
		if(value==null || value.length==0 || value[0]==null){
			throw new ValidationException("mandatory parameter were not found: "+parameterName);
		}
	}
	
	private static CommonHttpDto getDto(Exchange exchange){
		Object body=exchange.getIn().getBody();
		if(body instanceof CommonHttpDto){
			return (CommonHttpDto)body;
		}
		throw new IllegalArgumentException("CommonHttpDto expected");
	}
	
}
