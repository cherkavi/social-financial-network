package ua.com.nmtg.private_office.web_service.generator.code_description.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodSignature {
	public static enum Visibility{
		v_private("private"), 
		v_package(""), 
		v_protected("protected"), 
		v_public("public");
		
		private String displayName;
		
		private Visibility(String displayName) {
			this.displayName=displayName;
		}
		
		public String getDisplayName(){
			return this.displayName;
		}
	}

	private final Visibility visibility;
	private final MethodReturnValue returnValue;
	private final String name;
	private final List<MethodParameter> parameters=new ArrayList<MethodParameter>();
	
	public MethodSignature(Visibility visibility, 
						   MethodReturnValue returnValue, 
						   String name, 
						   MethodParameter ... parameters ){
		this(visibility, returnValue, name, Arrays.asList(parameters));
	}
	
	public MethodSignature(Visibility visibility, 
						   MethodReturnValue returnValue, 
						   String name, 
						   List<MethodParameter> parameters ){
		this.visibility=visibility;
		this.returnValue=returnValue;
		this.name=name;
		for(MethodParameter param:parameters){
			this.parameters.add(param);
		}
	}
	public Visibility getVisibility() {
		return visibility;
	}

	public MethodReturnValue getReturnValue() {
		return returnValue;
	}

	public String getName() {
		return name;
	}

	public List<MethodParameter> getParameters() {
		return parameters;
	}
	

	@Override
	public String toString() {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append(this.visibility.getDisplayName());
		returnValue.append(" ");
		returnValue.append(this.getReturnValue());
		returnValue.append(" ");
		returnValue.append(this.name);
		returnValue.append("(");
		StringBuilder parameterString=new StringBuilder();
		int counter=0;
		for(MethodParameter parameter:this.parameters){
			if(parameterString.length()>0){
				parameterString.append(", ");
			}
			parameterString.append(parameter.getType());
			parameterString.append(" ");
			counter++;
			if(parameter.getName()==null){
				parameterString.append("p");
				parameterString.append(counter);
			}else{
				parameterString.append(parameter.getName());
			}
		}
		returnValue.append(parameterString);
		returnValue.append(")");
		return returnValue.toString();
	}
}
