package ua.com.nmtg.private_office.web_service.generator.web_xml;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import ua.com.nmtg.private_office.web_service.generator.ISourceGenerator;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.UnitDescription;
import ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation.IAnnotationVisitor;

public class WebXmlCxfGenerator implements ISourceGenerator{
	final String space="    ";
	final String lineDelimiter="\n";

	// private IChangePackageStrategy changePackageStrategy=new NoChangePackageStrategy();
	// private INameDecorator serviceNameDecorator=new EmptyNameDecorator();
	private String displayName="PrivateOffice";
	
	private String additionalTags="";
	private List<IAnnotationVisitor> annotationVisitors;
	
	public WebXmlCxfGenerator(){
	}
	
	/** 
	 * @param name - for web-app/display-name value 
	 */
	public void setDisplayName(String name){
		if(StringUtils.trimToNull(name)!=null){
			this.displayName=name;
		}
	}
	
	/**
	 * set additional Tag's for Web.xml 
	 * @param value
	 */
	public void setAdditionalTags(String value){
		if(value!=null){
			this.additionalTags=value;
		}
	}
	
	@Override
	public String generateSourceCode(UnitDescription description) {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	").append(lineDelimiter);
		returnValue.append("<web-app xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://java.sun.com/xml/ns/javaee\" xmlns:web=\"http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\" id=\"WebApp_ID\" version=\"2.5\">	").append(lineDelimiter);
		if(this.displayName!=null){
			returnValue.append("	  <display-name>").append(this.displayName).append("</display-name>").append(lineDelimiter);
		}
		returnValue.append("	  <context-param>	").append(lineDelimiter);
		returnValue.append("	    <param-name>contextConfigLocation</param-name>	").append(lineDelimiter);
		returnValue.append("	    <param-value>classpath:cxf.xml</param-value>	").append(lineDelimiter);
		returnValue.append("	  </context-param>	").append(lineDelimiter);
		returnValue.append("	  <listener>	").append(lineDelimiter);
		returnValue.append("	    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>	").append(lineDelimiter);
		returnValue.append("	  </listener>	").append(lineDelimiter);
		returnValue.append("	  <servlet>	").append(lineDelimiter);
		returnValue.append("	    <servlet-name>CXFServlet</servlet-name>	").append(lineDelimiter);
		returnValue.append("	    <servlet-class>org.apache.cxf.transport.servlet.CXFServlet</servlet-class>	").append(lineDelimiter);
		returnValue.append("	    <load-on-startup>1</load-on-startup>	").append(lineDelimiter);
		returnValue.append("	  </servlet>	").append(lineDelimiter);
		returnValue.append("	  <servlet-mapping>	").append(lineDelimiter);
		returnValue.append("	    <servlet-name>CXFServlet</servlet-name>	").append(lineDelimiter);
		returnValue.append("	    <url-pattern>/*</url-pattern>	").append(lineDelimiter);
		returnValue.append("	  </servlet-mapping>	").append(lineDelimiter);
		if((additionalTags!=null)&&(additionalTags.length()>0)){
			returnValue.append(additionalTags);
		}
		returnValue.append("\n");
		returnValue.append("</web-app>	").append(lineDelimiter);
		return returnValue.toString();
	}

	@Override
	public String getSourceName(UnitDescription description) {
		return "web.xml";
	}

	public void setAnnotationVisitors(List<IAnnotationVisitor> annotationVisitors){
		this.annotationVisitors=annotationVisitors;
	}
	

	@Override
	public List<IAnnotationVisitor> getAnnotationVisitors() {
		return this.annotationVisitors;
	}
}
