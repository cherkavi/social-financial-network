package ua.com.nmtg.private_office.web_service.generator;

import java.util.List;

import ua.com.nmtg.private_office.web_service.generator.code_description.elements.UnitDescription;
import ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation.IAnnotationVisitor;

public interface ISourceGenerator {

	/**
	 * @return - list of annotation visitors
	 * ( represent the different annotations ) 
	 */
	public List<IAnnotationVisitor> getAnnotationVisitors();
	
	/**
	 * @param description - description of source code
	 * @param visitors - additional information about source code - annotation for another part of the code  
	 * @return
	 */
	public String generateSourceCode(UnitDescription description);
	
	/**
	 * get name of Source code 
	 * @return fileName of source code 
	 */
	public String getSourceName(UnitDescription description);
}
