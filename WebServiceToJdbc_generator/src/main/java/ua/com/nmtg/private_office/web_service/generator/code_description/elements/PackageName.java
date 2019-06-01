package ua.com.nmtg.private_office.web_service.generator.code_description.elements;

import org.apache.commons.lang.StringUtils;

public class PackageName {
	private final String packageName;
	
	public PackageName(String value){
		this.packageName=value;
	}

	public String getParentPackage(){
		String[] values=StringUtils.split(packageName, '.');
		StringBuilder returnValue=new StringBuilder();
		for(int index=0;index<(values.length-1);index++){
			if(returnValue.length()>0){
				returnValue.append('.');
			}
			returnValue.append(values[index]);
		}
		return returnValue.toString();
	}
	
	public String getSubPackage(String subPackageName){
		String[] values=StringUtils.split(packageName, '.');
		StringBuilder returnValue=new StringBuilder();
		for(int index=0;index<(values.length);index++){
			if(returnValue.length()>0){
				returnValue.append('.');
			}
			returnValue.append(values[index]);
		}
		if(returnValue.length()>0){
			returnValue.append('.');
		}
		returnValue.append(subPackageName);
		return returnValue.toString();
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((packageName == null) ? 0 : packageName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PackageName other = (PackageName) obj;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		} else if (!packageName.equals(other.packageName))
			return false;
		return true;
	}
	
	public String getPackageName(){
		return this.packageName;
	}
	
	@Override
	public String toString() {
		if(StringUtils.trimToNull(this.packageName)!=null){
			return "package "+this.packageName+";";
		}else{
			return "";
		}
		
	}
	
}
