package bc.payment.income;

import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

public class StartRouteTest extends AbstractJUnit4SpringContextTests {

	private static final String SPRING_CONTEXT_LOCATION="classpath*:META-INF/spring/*context.xml";

	public static void main(String[] args) throws Exception{
		// spring context
		ApplicationContext springContext=new ClassPathXmlApplicationContext(SPRING_CONTEXT_LOCATION);
		// camel routes
		CamelContext camelContext=createContext(springContext);
		// define all routes
		addAllRoutes(springContext, camelContext);
		// start 
		camelContext.start();
		waitInfinitly();
	}

	private static void waitInfinitly() throws InterruptedException {
		while(true){
			TimeUnit.SECONDS.sleep(100000);
		}
	}

	private static CamelContext createContext(ApplicationContext springContext) throws Exception {
		return SpringCamelContext.springCamelContext(springContext);
	}


	private static void addAllRoutes(ApplicationContext springContext, CamelContext camelContext) throws Exception {
		camelContext.addRoutes(new CityPayPayment());
	}
	
}
