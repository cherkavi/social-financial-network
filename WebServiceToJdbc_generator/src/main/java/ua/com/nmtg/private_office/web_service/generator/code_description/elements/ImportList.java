package ua.com.nmtg.private_office.web_service.generator.code_description.elements;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * import block of the class description
 */
public class ImportList {
	private Set<String> importList=new HashSet<String>();
	
	
	/**
	 * import block of the class description
	 */
	public ImportList(){
	}
	
	/**
	 * list of classes (without word "import" without ";")
	 * @param importClass
	 */
	public ImportList(String ... importClass){
		for(String eachValue:importClass){
			this.addImport(eachValue);
		}
	}
	
	private String clearDirtyValue(String value){
		String returnValue=StringUtils.substringBefore(value, ";");
		if(returnValue.indexOf(' ')>=0){
			returnValue=StringUtils.substringAfter(returnValue, " ");
		}
		return returnValue;
	}
	
	public void addImport(String value){
		importList.add(clearDirtyValue(value));	
	}

	
	@Override
	public String toString() {
		StringBuilder returnValue=new StringBuilder();
		for(String eachString:this.importList){
			returnValue.append("import ").append(eachString).append(";\n");
		}
		return returnValue.toString();
	}
	
}
