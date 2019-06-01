package ua.com.nmtg.private_office.jdbc.destination;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ua.com.nmtg.private_office.jdbc.destination.exception.DestinationException;
import ua.com.nmtg.private_office.jdbc.source.Parameter;

public class JavaBeanDestinationPoint implements IDestinationPoint{
	private static Logger logger=Logger.getLogger(JavaBeanDestinationPoint.class);
	private final String packageName;
	private final String pathToFile;
	private final String className;
	
	public JavaBeanDestinationPoint(String pathToFile, String className) {
		this(pathToFile, className, null);
	}
	public JavaBeanDestinationPoint(String pathToFile, String className, String packageName) {
		this.pathToFile=pathToFile;
		this.packageName=packageName;
		this.className=className;
	}

	private final static String newLine="\n";
	private final static String space="    ";
	
	@Override
	public void write(Collection<Parameter> parameters) throws DestinationException {
		StringBuilder returnValue=new StringBuilder();
		if(this.packageName!=null){
			returnValue.append("package ").append(this.packageName).append(";").append(newLine).append(newLine);
		}
		returnValue.append("import java.sql.Date;").append(newLine);
		returnValue.append("import java.io.Serializable;").append(newLine);
		returnValue.append(newLine);
		returnValue.append("public class ").append(this.className).append(" implements Serializable {").append(newLine);
		
		for(Parameter param: parameters){
			returnValue.append(createField(param));
			returnValue.append(newLine);
		}

		for(Parameter param: parameters){
			returnValue.append(createGetter(param));
			returnValue.append(newLine);
			returnValue.append(createSetter(param));
			returnValue.append(newLine);
		}
		
		returnValue.append("}").append(newLine);
		try{
			FileUtils.writeStringToFile(new File(this.pathToFile), returnValue.toString());
		}catch(IOException ioEx){
			logger.error("Can't write data to file: ");
			throw new DestinationException(ioEx.getMessage());
		}
	}
	
	private StringBuilder createSetter(Parameter param) {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append(space).append("public void ");
		returnValue.append("set"+upperForFirstLetter( param.getName().toLowerCase()) )
		.append("(")
		.append(Parameter.getJavaType(param))
		.append(" value ){");
		returnValue.append(newLine);
		returnValue.append(space).append(space).append("this.").append(param.getName().toLowerCase()).append("=").append("value;").append(newLine);
		returnValue.append(space).append("}").append(newLine);
		return returnValue;
	}
	
	private StringBuilder createGetter(Parameter param) {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append(space).append("public ");
		returnValue.append(Parameter.getJavaType(param)).append(" ");
		returnValue.append("get"+upperForFirstLetter( param.getName().toLowerCase()) ).append("(){");
		returnValue.append(newLine);
		returnValue.append(space).append(space);
		returnValue.append("return "+param.getName().toLowerCase()).append(";").append(newLine);
		
		returnValue.append(space);
		returnValue.append("}").append(newLine);
		return returnValue;
	}
	
	private String upperForFirstLetter(String lowerCase) {
		return (""+lowerCase.charAt(0)).toUpperCase()+StringUtils.substring(lowerCase, 1);
	}

	private StringBuilder createField(Parameter param) {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append(space).append("private ");
		returnValue.append(Parameter.getJavaType(param)).append(" ");
		returnValue.append(param.getName().toLowerCase()).append(";");
		returnValue.append(newLine);
		return returnValue;
	}
	
}
