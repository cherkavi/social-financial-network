package ua.com.nmtg.private_office.web_service.generator.web_xml;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import ua.com.nmtg.private_office.web_service.generator.ISourceGenerator;
import ua.com.nmtg.private_office.web_service.generator.change_package.IChangePackageStrategy;
import ua.com.nmtg.private_office.web_service.generator.change_package.NoChangePackageStrategy;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.UnitDescription;
import ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation.IAnnotationVisitor;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.EmptyNameDecorator;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.INameDecorator;

public class WebXmlHessianGenerator implements ISourceGenerator{
	final String space="    ";
	final String lineDelimiter="\n";

	private IChangePackageStrategy changePackageStrategy=new NoChangePackageStrategy();
	private INameDecorator serviceNameDecorator=new EmptyNameDecorator();
	private String displayName="PrivateOffice";
	
	private String additionalTags="";
	private List<IAnnotationVisitor> annotationVisitors;
	
	public WebXmlHessianGenerator(){
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
	
	/** 
	 * @param changePackageStrategy - change package for Service (WebService )
	 */
	public void setChangePackageStrategy(IChangePackageStrategy changePackageStrategy){
		if(changePackageStrategy!=null){
			this.changePackageStrategy=changePackageStrategy;
		}
	}
	
	/**
	 * @param nameDecorator - name decorator for Service ( WebService )
	 */
	public void setServiceNameDecorator(INameDecorator nameDecorator){
		if(nameDecorator!=null){
			this.serviceNameDecorator=nameDecorator;
		}
	}
	
	private StringBuilder getHeader(){
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(lineDelimiter);
		returnValue.append("<web-app xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  ").append(lineDelimiter); 
	    returnValue.append("	xmlns=\"http://java.sun.com/xml/ns/javaee\"   ").append(lineDelimiter);
	    returnValue.append("	xmlns:web=\"http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\"  ").append(lineDelimiter);
	    returnValue.append("	xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\"  ").append(lineDelimiter); 
	    returnValue.append("	id=\"WebApp_ID\"  ").append(lineDelimiter); 
		returnValue.append("	version=\"2.5\"> ").append(lineDelimiter);
		
		returnValue.append(this.lineDelimiter);
		returnValue.append(space);
		returnValue.append("<display-name>").append(this.displayName).append("</display-name>");
		returnValue.append(lineDelimiter);
		return returnValue;
	}
	
	private String getFooter(){
		StringBuilder returnValue=new StringBuilder();
		returnValue.append(this.lineDelimiter);
		returnValue.append(this.additionalTags);
		returnValue.append(this.lineDelimiter);
		returnValue.append("</web-app>");
		return returnValue.toString();
	}
	
	
	@Override
	public String generateSourceCode(UnitDescription description) {
		// INFO need analize the annotation 
		StringBuilder returnValue=new StringBuilder();
		returnValue.append(this.getHeader());
		
		String interfaceName=description.getClassHeader().getName();
		String webServiceClassPackage=this.changePackageStrategy.getPackageName(description.getPackageName());
		if(webServiceClassPackage.length()>0){
			webServiceClassPackage+=".";
		}
		String webServiceClass=this.serviceNameDecorator.decorate(description.getClassHeader().getName());
		
		returnValue
			.append(space)
			.append("<servlet>").append(lineDelimiter)
			.append(space).append(space)
			.append("<description></description>").append(lineDelimiter)
			.append(space).append(space)
			.append("<display-name>"+interfaceName+"</display-name>").append(lineDelimiter)
			.append(space).append(space)
			.append("<servlet-name>"+interfaceName+"</servlet-name>").append(lineDelimiter)
			.append(space).append(space)
			.append("<servlet-class>"+webServiceClassPackage+webServiceClass+"</servlet-class>").append(lineDelimiter)
			.append(space)
			.append("</servlet>").append(lineDelimiter)
			.append(space).append(space)
			.append("<servlet-mapping>").append(lineDelimiter)
			.append(space).append(space)
			.append("<servlet-name>"+interfaceName+"</servlet-name>").append(lineDelimiter)
			.append(space).append(space)
			.append("<url-pattern>/"+interfaceName+"</url-pattern>").append(lineDelimiter)
			.append(space)
			.append("</servlet-mapping>").append(lineDelimiter);
		returnValue.append(this.getFooter());
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
