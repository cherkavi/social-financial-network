package bc.payment.income;

import org.apache.camel.LoggingLevel;
import org.apache.camel.spring.SpringRouteBuilder;

import bc.payment.income.transport.CommonHttpDto;
import bc.payment.income.utility.HttpPredicates;
import bc.payment.income.validation.ValidationOperationProcessors;

public class CityPayPayment extends SpringRouteBuilder{

	@Override
	public void configure() throws Exception {
		// <!-- "timer://unique_name?fixedRate=true&period=1000"  -->
        from("jetty:http://localhost:2015/payment")
        // common HttpDto
        .bean(CommonHttpDto.class, "read")
        .choice()
    	.when(HttpPredicates.isGetRequest())
    		.choice()
    		.when(HttpPredicates.isHttpParameterEquals("QueryType", "check"))
	        	.log(LoggingLevel.INFO, "check")
	        	.process(ValidationOperationProcessors.check())
	        	.beanRef("operation.check", "execute")
	        	.to("stream:out")
    		.when(HttpPredicates.isHttpParameterEquals("QueryType", "pay"))
	        	.log(LoggingLevel.INFO, "pay")
	        	.process(ValidationOperationProcessors.pay())
	        	.beanRef("operation.pay", "execute")
	        	.to("stream:out")
    		.when(HttpPredicates.isHttpParameterEquals("QueryType", "cancel"))
	        	.log(LoggingLevel.INFO, "cancel")
	        	.process(ValidationOperationProcessors.cancel())
	        	.beanRef("operation.cancel", "execute")
	        	.to("stream:out")
	        // TODO PayDayReport.html
	        .otherwise()
	        	.log(LoggingLevel.ERROR, "not recognized");
	}

}



/*
 * SECOND ROUTE
 * 
 * 
.when(HttpPredicates.isPostRequest())
	.log("POST message")
	.to("stream:out")
   .bean(Body2String.class, "convertBody")
	.bean(ConverterXml.class, "fromString")
   .choice()
		.when(new Predicate() {
			@Override
			public boolean matches(Exchange exchange) {
				XPath xPath =  XPathFactory.newInstance().newXPath();
				Node node;
				try {
					node = (Node) xPath.evaluate("/Body/GetStatus", exchange.getIn().getBody(), XPathConstants.NODE);
					// node = (Node) xPath.compile("/Body/GetStatus").evaluate(exchange.getIn().getBody(), XPathConstants.NODE);
					return node!=null;
				} catch (XPathExpressionException e) {
					return false;
				}
			}
		})	.to("stream:out")
		.otherwise()
			.to("stream:err");
	*/
