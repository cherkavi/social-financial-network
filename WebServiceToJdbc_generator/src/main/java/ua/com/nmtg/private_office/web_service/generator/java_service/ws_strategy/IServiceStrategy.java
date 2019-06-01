package ua.com.nmtg.private_office.web_service.generator.java_service.ws_strategy;

import ua.com.nmtg.private_office.web_service.generator.code_description.elements.ClassHeader;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.MethodSignature;
import ua.com.nmtg.private_office.web_service.generator.java_service.data_access_strategy.IDataSourceStrategy;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.INameDecorator;

/**
 * strategy for different type of WebService elements  
 */
public interface IServiceStrategy {
	/** @nullable */
	public String getAdditionalImport(IDataSourceStrategy dataSourceStrategy);
	
	/** @notNull need to have at least the Description such as "class ClassName "*/
	public String getFullClassDescription(ClassHeader classHeader,
										  INameDecorator serviceObjectNameDecorator
										  );
	
	/** @notNull need to have at least the Default Constructor  
	 * @param classHeader - class header decorator 
	 * @param serviceObjectNameDecorator - ClassName decorator for service 
	 * @param proxyObjectNameDecorator - ClassName decorator for proxy object 
	 * @param dataSourceStrategy - strategy for access to Data {@link IDataSourceStrategy} 
	 * @return
	 */
	public String getFieldsAndConstructor(ClassHeader classHeader,
			  							  INameDecorator serviceObjectNameDecorator, 
			  							  INameDecorator proxyObjectNameDecorator,
			  							  IDataSourceStrategy dataSourceStrategy);
	
	/** @notNull need to have at least the "return null;" */
	public String getProxyMethodBody(MethodSignature method);
	
	
}
