package ua.com.nmtg.private_office.jdbc.destination;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ua.com.nmtg.private_office.jdbc.destination.decorator.IConditionDecorator;
import ua.com.nmtg.private_office.jdbc.destination.exception.DestinationException;
import ua.com.nmtg.private_office.jdbc.source.Parameter;

public class PhpBeanDestinationPoint implements IDestinationPoint{
	private static Logger logger=Logger.getLogger(PhpBeanDestinationPoint.class);
	private final String pathToFile;
	private final String className;
	private IConditionDecorator getterDecorator;
	private IConditionDecorator setterDecorator;

	public PhpBeanDestinationPoint(String pathToFile, 
								   String className,
								   IConditionDecorator getterDecorator,
								   IConditionDecorator setterDecorator
								   ) {
		this.pathToFile=pathToFile;
		this.className=className;
		this.getterDecorator=getterDecorator;
		this.setterDecorator=setterDecorator;
	}

	private final static String newLine="\n";
	private final static String space="    ";
	
	@Override
	public void write(Collection<Parameter> parameters) throws DestinationException {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("<?php class ").append(this.className).append(" {").append(newLine);
		
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
		returnValue.append("?>");
		try{
			FileUtils.writeStringToFile(new File(this.pathToFile), returnValue.toString());
		}catch(IOException ioEx){
			logger.error("Can't write data to file: ");
			throw new DestinationException(ioEx.getMessage());
		}
	}
	
	private StringBuilder createSetter(Parameter param) {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append(space).append("public function ");
		returnValue.append("set"+upperForFirstLetter( param.getName().toLowerCase()) )
		.append("( $value ){");
		returnValue.append(newLine);
		if(( this.setterDecorator!=null) && (this.setterDecorator.isNeedToExecute(param))){
			returnValue.append(space).append(space).append("$this->").append(param.getName().toLowerCase()).append("=").append(this.setterDecorator.decorate("$value")).append(";").append(newLine);
		}else{
			returnValue.append(space).append(space).append("$this->").append(param.getName().toLowerCase()).append("=").append("$value;").append(newLine);
		}
		returnValue.append(space).append("}").append(newLine);
		return returnValue;
	}
	
	private StringBuilder createGetter(Parameter param) {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append(space).append("public function ");
		returnValue.append("get"+upperForFirstLetter( param.getName().toLowerCase()) ).append("(){");
		returnValue.append(newLine);
		returnValue.append(space).append(space);
		if((this.getterDecorator!=null)&&(this.getterDecorator.isNeedToExecute(param))){
			returnValue.append("return ").append(this.getterDecorator.decorate("$this->"+param.getName().toLowerCase())).append(";").append(newLine);
		}else{
			returnValue.append("return ").append("$this->"+param.getName().toLowerCase()).append(";").append(newLine);
		}
		
		returnValue.append(space);
		returnValue.append("}").append(newLine);
		return returnValue;
	}
	
	private String upperForFirstLetter(String lowerCase) {
		return (""+lowerCase.charAt(0)).toUpperCase()+StringUtils.substring(lowerCase, 1);
	}

	private StringBuilder createField(Parameter param) {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append(space).append("public ");
		returnValue.append("$");
		returnValue.append(param.getName().toLowerCase()).append(";");
		returnValue.append(newLine);
		return returnValue;
	}
	
}
