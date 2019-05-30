package bc.payment.income.utility;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

import bc.payment.income.transport.CommonHttpDto;

public class HttpPredicates {
	
	public static Predicate isGetRequest(){
		return new Predicate() {
			@Override
			public boolean matches(Exchange exchange) {
				if(!(exchange.getIn().getBody() instanceof CommonHttpDto)){
					return false;
				}
				CommonHttpDto message=(CommonHttpDto)exchange.getIn().getBody();
				return message.isGetMethod();
			}
		};
	}
	
	public static Predicate isPostRequest(){
		return new Predicate() {
			@Override
			public boolean matches(Exchange exchange) {
				if(!(exchange.getIn().getBody() instanceof CommonHttpDto)){
					return false;
				}
				CommonHttpDto message=(CommonHttpDto)exchange.getIn().getBody();
				return message.isPostMethod();
			}
		};
	}
	
	public static Predicate isHttpParameterEquals(final String fieldName, final String controlValue){
		return new Predicate(){
			@Override
			public boolean matches(Exchange exchange) {
				Object message=exchange.getIn().getBody();
				if(!(message instanceof CommonHttpDto)){
					return false;
				}
				
				
				String[] fieldValue=((CommonHttpDto)message).getParameters().get(fieldName);
				if(fieldValue==null && controlValue==null){
					return true;
				}
				if(fieldValue==null || controlValue==null){
					return false;
				}
				for(String eachElement:fieldValue){
					if(eachElement.equals(controlValue)){
						return true;
					}
				}
				return false;
			}
			
		};
	}
	
}
