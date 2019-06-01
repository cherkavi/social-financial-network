package ua.com.nmtg.private_office.web_service.generator.change_package;

import ua.com.nmtg.private_office.web_service.generator.code_description.elements.PackageName;

public class SubPackage implements IChangePackageStrategy{
	private final String subPackage;
	
	public SubPackage(String packageName){
		this.subPackage=packageName;
	}
	
	@Override
	public String getPackageName(PackageName packageName) {
		return packageName.getSubPackage(subPackage);
	}

}
