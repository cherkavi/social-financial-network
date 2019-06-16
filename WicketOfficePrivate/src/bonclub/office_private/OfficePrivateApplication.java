package bonclub.office_private;

import java.sql.Connection;
import java.util.Properties;


import org.apache.log4j.PropertyConfigurator;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.WebApplication;
//import org.apache.wicket.spring.injection.annot.SpringBean;
import bonclub.office_private.database.ConnectUtility;
import bonclub.office_private.database.Connector;
import bonclub.office_private.session.OfficePrivateSession;
import bonclub.office_private.test.CreateCustomer;
import bonclub.office_private.web_gui.login.Login;

public class OfficePrivateApplication extends WebApplication{ 
//SpringWebApplication implements IWebApplicationFactory{
	static{
		Properties properties=new Properties();
		properties.put("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
		properties.put("log4j.appender.stdout.Target", "System.out");
		properties.put("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
		properties.put("log4j.appender.stdout.layout.ConversionPattern", "%d{ABSOLUTE} %5p %c{1}:%L - %m%n");
		properties.put("log4j.rootLogger","info, stdout");
		//properties.put("log4j.logger.org.springframework.web.context.ContextLoader","debug,stdout");
		properties.put("log4j.logger.org.hibernate","warn");
		properties.put("log4j.logger.org.hibernate","warn, stdout"); 
		properties.put("log4j.logger.org.hibernate.SQL","warn, stdout");
		properties.put("log4j.logger.org.hibernate.type","warn, stdout");
		properties.put("log4j.logger.org.hibernate.engine.QueryParameters","warn"); 
		PropertyConfigurator.configure(properties);
	}
		
	//@SpringBean(name="hibernateConnection")
	private Connector hibernateConnection=null;
	
	
	public Connector getHibernateConnection() {
		return hibernateConnection;
	}


	public void setHibernateConnection(Connector hibernateConnection) {
		this.hibernateConnection = hibernateConnection;
	}

	public ConnectUtility getConnectUtility(){
		return new ConnectUtility(this.getHibernateConnection());
	}

	/** получить соединение с родительской базой данных Oracle */
	public Connection getDatabaseParentConnection(){
		return this.hibernateConnection.getParentDatabaseConnection();
	}

	@Override
	protected void init(){
		getMarkupSettings().setStripWicketTags(true);
		mountBookmarkablePage("create", CreateCustomer.class);
		try {
			this.hibernateConnection=new Connector();
		} catch (Exception e) {
			System.err.println("OfficePrivateApplication init() Connector: "+e.getMessage());
		}
		//addComponentInstantiationListener(new SpringComponentInjector(this));
		
		/*try{
			//hibernateConnection=new Connector();
			hibernateConnection = (Connector)createSpringBeanProxy(Connector.class,
																   "hibernateConnection");
		}catch(Exception ex){};
		*/
/*		getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy(){
			@Override
			public boolean isActionAuthorized(Component arg0, Action arg1) {
				return true;
			}
			@Override
			public boolean isInstantiationAuthorized(Class arg0) {
				return true;
			}
		});*/
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return Login.class;
		//return UserArea.class;
	}
	
	@Override
	public org.apache.wicket.Session newSession(Request request, Response response){
		return new OfficePrivateSession(request);
	}
	

/*	@Override
	public WebApplication createApplication(WicketFilter arg0) {
		return this;
	}
*/
}
