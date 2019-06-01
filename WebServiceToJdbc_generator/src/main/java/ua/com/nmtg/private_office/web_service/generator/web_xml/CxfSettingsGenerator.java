package ua.com.nmtg.private_office.web_service.generator.web_xml;

import java.util.Collections;
import java.util.List;

import ua.com.nmtg.private_office.web_service.generator.ISourceGenerator;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.UnitDescription;
import ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation.IAnnotationVisitor;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.EmptyNameDecorator;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.INameDecorator;

public class CxfSettingsGenerator implements ISourceGenerator{

	@Override
	public String generateSourceCode(UnitDescription description) {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
		returnValue.append("<beans xmlns=\"http://www.springframework.org/schema/beans\"	\n");
		returnValue.append("	      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	\n");
		returnValue.append("	      xmlns:jaxws=\"http://cxf.apache.org/jaxws\"	\n");
		returnValue.append("	      xsi:schemaLocation=\"http://www.springframework.org/schema/beans	\n");
		returnValue.append("			http://www.springframework.org/schema/beans/spring-beans.xsd	\n");
		returnValue.append("			http://cxf.apache.org/jaxws	\n");
		returnValue.append("			http://cxf.apache.org/schemas/jaxws.xsd\">	\n");
		returnValue.append("		\n");
		returnValue.append("	  <import resource=\"classpath:META-INF/cxf/cxf.xml\" />	\n");
		returnValue.append("	  <import resource=\"classpath:META-INF/cxf/cxf-extension-soap.xml\"/>	\n");
		returnValue.append("	  <import resource=\"classpath:META-INF/cxf/cxf-servlet.xml\" />	\n");
		returnValue.append("		\n");

		String className=description.getClassHeader().getName().toLowerCase();
		returnValue.append("	  <jaxws:endpoint id=\""+className+"\"	\n");
		returnValue.append("	                  implementor=\""+description.getPackageName().getPackageName()+"."+this.nameDecorator.decorate(description.getClassHeader().getName())+"\"	\n");
		returnValue.append("	                  address=\"/"+className+"\"/>	\n");
		returnValue.append("		\n");
		returnValue.append("</beans>	\n");
		return returnValue.toString();
	}

	@Override
	public List<IAnnotationVisitor> getAnnotationVisitors() {
		return Collections.emptyList();
	}

	@Override
	public String getSourceName(UnitDescription description) {
		return "cxf.xml";
	}

	private INameDecorator nameDecorator=new EmptyNameDecorator();
	/** 
	 * decorator for the name of service 
	 * @param nameDecorator
	 */
	public void setNameDecorator(INameDecorator nameDecorator){
		if(nameDecorator!=null){
			this.nameDecorator=nameDecorator;
		}
	}

}
