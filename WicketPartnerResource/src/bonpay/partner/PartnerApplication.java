package bonpay.partner;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.spring.SpringWebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import bonpay.partner.database.Connector;
import bonpay.partner.session.PartnerSession;
import bonpay.partner.web_gui.PartnerEnterPoint;

/** ����� ����� � ���������� Wicket Apache */
public class PartnerApplication extends SpringWebApplication implements IWebApplicationFactory{

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
		
	@SpringBean(name="hibernateConnection")
	private Connector hibernateConnection=null;
	
	
	public Connector getHibernateConnection() {
		return hibernateConnection;
	}


	public void setHibernateConnection(Connector hibernateConnection) {
		this.hibernateConnection = hibernateConnection;
	}


	@Override
	protected void init(){
		System.out.println("PartnerApplication init  begin:");
		addComponentInstantiationListener(new SpringComponentInjector(this));
		try{
			//hibernateConnection=new Connector();
			hibernateConnection = (Connector)createSpringBeanProxy(Connector.class,
																   "hibernateConnection");
		}catch(Exception ex){};
		//
		System.out.println("PartnerApplication init  end:");
	}

	
	@Override
	public Class<?> getHomePage() {
		return PartnerEnterPoint.class;
	}


	@Override
	public WebApplication createApplication(WicketFilter arg0) {
		return this;
	}
	
	@Override
	public Session newSession(Request request, Response response){
		return new PartnerSession(request);
	}
}
