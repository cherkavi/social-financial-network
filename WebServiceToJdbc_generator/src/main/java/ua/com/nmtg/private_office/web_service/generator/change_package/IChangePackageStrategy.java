package ua.com.nmtg.private_office.web_service.generator.change_package;

import ua.com.nmtg.private_office.web_service.generator.code_description.elements.PackageName;

/**
 * interface for change the package declaration 
 */
public interface IChangePackageStrategy {
	public String getPackageName(PackageName packageName);
}
