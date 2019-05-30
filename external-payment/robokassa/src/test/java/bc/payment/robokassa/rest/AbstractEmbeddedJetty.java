package bc.payment.robokassa.rest;

import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class AbstractEmbeddedJetty {
	
	protected static final int DEFAULT_PORT = 8085;
    private static final String CONTEXT_PATH = "/";
    private static final String MAPPING_URL = "/*";
    
    public AbstractEmbeddedJetty() {
	}
    
    @BeforeClass
    public static void init(){
    	try{
    		startJetty(DEFAULT_PORT);
    	}catch(Exception ex){
    		throw new RuntimeException("can't start Jetty embedded server:", ex);
    	}
    }
    private static Server server;
    
    @AfterClass
    public static void destroy(){
    	if(server!=null && server.isRunning()){
    		try {
				server.stop();
			} catch (Exception e) {
			}
    	}
    }

    private static void startJetty(int port) throws Exception {
        server = new Server(port);
        // org.eclipse.jetty.plus.jndi.EnvEntry entry=new org.eclipse.jetty.plus.jndi.EnvEntry("", "");
        server.setHandler(getServletContextHandler(getContext()));
        server.start();
        // server.setStopAtShutdown(stop);
        // server.join();
    }	
    
    
    private static ServletContextHandler getServletContextHandler(WebApplicationContext context) throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath(CONTEXT_PATH);
        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);
        contextHandler.addEventListener(new ContextLoaderListener(context));
        contextHandler.setResourceBase("src/main/webapp");
        return contextHandler;
    }

    private static final String CONFIG_LOCATION = "classpath:spring-context-test.xml";
    // private static final String DEFAULT_PROFILE = "dev";

    private static WebApplicationContext getContext() {
        // AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
    	ConfigurableWebApplicationContext context = new XmlWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);
        return context;
    }    
    
    protected String getServerRoot(){
    	return "http://localhost:"+DEFAULT_PORT;
    }
}
