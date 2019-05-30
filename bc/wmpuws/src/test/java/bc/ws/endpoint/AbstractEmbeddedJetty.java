package bc.ws.endpoint;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
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
    private static final String PATH_WEB_XML="src/main/webapp/WEB-INF/web.xml";
    
    public AbstractEmbeddedJetty() {
	}
    
    @BeforeClass
    public static void init(){
		// System.setProperty("java.naming.factory.url.pkgs", "org.eclipse.jetty.jndi");
		// System.setProperty("java.naming.factory.initial", "org.eclipse.jetty.jndi.InitialContextFactory");
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
        configure(server);
        server.start();
        // server.setStopAtShutdown(stop);
        // server.join();
    }	
    
    /**
     * additional configuration for server
     * @param server
     * @throws Exception
     */
    private static void configure(Server server) throws Exception {
        String[] configFiles = {"src/test/resources/jetty.xml"};
        for(String configFile : configFiles) {
        	XmlConfiguration configuration = new XmlConfiguration(new File(configFile).toURI().toURL());
        	configuration.configure(server);
        }
	}

	private static ServletContextHandler getServletContextHandler(WebApplicationContext context) throws IOException {
    	WebAppContext contextHandler=new WebAppContext();
		contextHandler.setErrorHandler(null);
		contextHandler.setContextPath(CONTEXT_PATH);
		contextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);
		contextHandler.addEventListener(new ContextLoaderListener(context));
		contextHandler.setResourceBase("src/main/webapp");
		contextHandler.setDefaultsDescriptor(PATH_WEB_XML);
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
