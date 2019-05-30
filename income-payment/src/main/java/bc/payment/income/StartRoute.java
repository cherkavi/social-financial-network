package bc.payment.income;

import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartRoute {
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

	/**
	 * read all routes
	 * @param springContext
	 * @param camelContext
	 * @throws Exception
	 */
	private static void addAllRoutes(ApplicationContext springContext, CamelContext camelContext) throws Exception {
//		Resource[] fileRoutes=getAllFileRoutes(springContext);
//		for(Resource eachRoute:fileRoutes){
//			RoutesDefinition routes = camelContext.loadRoutesDefinition(eachRoute.getInputStream());
//			camelContext.addRouteDefinitions(routes.getRoutes());
//		}
		camelContext.addRoutes(new CityPayPayment());
	}

// 	private static final String RESOURCE_LOCATOR = "classpath*:META-INF/route/*.xml";
	/**
	 * read all files with routes
	 * @param springContext
	 * @return
	 * @throws IOException
	@SuppressWarnings("unused")
	private static Resource[] getAllFileRoutes(ApplicationContext springContext) throws IOException {
		return springContext.getResources(RESOURCE_LOCATOR);
	}
	 */

}
