package ua.com.nmtg.private_office.web_service.generator.change_package;

import ua.com.nmtg.private_office.web_service.generator.code_description.elements.PackageName;

/**
 * The Package name without change  
 */
public class NoChangePackageStrategy implements IChangePackageStrategy{

	@Override
	public String getPackageName(PackageName packageName) {
		return packageName.getPackageName();
	}

}
