package ua.com.nmtg.private_office.web_service.generator.java_service;

import java.util.Collections;
import java.util.List;

import ua.com.nmtg.private_office.web_service.generator.ISourceGenerator;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.MethodParameter;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.MethodSignature;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.UnitDescription;
import ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation.IAnnotationVisitor;

public class CxfServiceGenerator implements ISourceGenerator{

	public String getAdditionalImport() {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("import javax.jws.WebMethod; \n");
		returnValue.append("import javax.jws.WebParam; \n");
		returnValue.append("import javax.jws.WebService; \n");
		return returnValue.toString();
	}
	private static final String newLine="\n";
	private static final String space="    ";
	private static final String doubleSpace="        ";
	
	@Override
	public String generateSourceCode(UnitDescription description) {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("package ").append(description.getPackageName().getPackageName()).append(";").append(newLine);
		returnValue.append(newLine);
		returnValue.append(description.getImportList().toString());
		returnValue.append(newLine);
		returnValue.append("import javax.jws.WebMethod; \n");
		returnValue.append("import javax.jws.WebParam; \n");
		returnValue.append("import javax.jws.WebService; \n");
		returnValue.append(newLine);
		returnValue.append("@WebService").append(newLine);
		returnValue.append("public interface "+description.getClassHeader().getName());
		returnValue.append("{").append(newLine);
		for(MethodSignature method:description.getListOfMethods()){
			returnValue.append(space).append("@WebMethod").append(newLine);
			returnValue.append(space)
					   .append(method.getVisibility().getDisplayName()).append(" ")
					   .append(method.getReturnValue().toString()).append(" ")
					   .append(method.getName()).append("(").append(newLine);
			boolean isFirstParameter=true;
			for(MethodParameter parameter:method.getParameters()){
				if(isFirstParameter==false){
					returnValue.append(", ").append(newLine);
				}
				returnValue.append(doubleSpace).append("@WebParam(name=\"")
				.append(parameter.getName()).append("\") ")
				.append(parameter.getType()).append(" ")
				.append(parameter.getName());
				isFirstParameter=false;
			}
			returnValue.append(");");
			returnValue.append(newLine);
		}
		returnValue.append(newLine).append("}");
		return returnValue.toString();
	}

	@Override
	public List<IAnnotationVisitor> getAnnotationVisitors() {
		return Collections.emptyList();
	}

	@Override
	public String getSourceName(UnitDescription description) {
		return description.getClassHeader().getName()+".java";
	}

}
