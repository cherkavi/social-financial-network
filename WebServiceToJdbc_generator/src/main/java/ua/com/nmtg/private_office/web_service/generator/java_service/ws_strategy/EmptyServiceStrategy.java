package ua.com.nmtg.private_office.web_service.generator.java_service.ws_strategy;

import ua.com.nmtg.private_office.web_service.generator.code_description.elements.ClassHeader;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.MethodSignature;
import ua.com.nmtg.private_office.web_service.generator.java_service.data_access_strategy.IDataSourceStrategy;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.INameDecorator;

public class EmptyServiceStrategy implements IServiceStrategy{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProxyMethodBody(MethodSignature method) {
		return "return null;";
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFieldsAndConstructor(ClassHeader classHeader,
			INameDecorator serviceObjectNameDecorator,
			INameDecorator proxyObjectNameDecorator,
			IDataSourceStrategy dataSourceStrategy) {
		return "    public "+serviceObjectNameDecorator.decorate(classHeader.getName())+"(){}";
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFullClassDescription(ClassHeader classHeader,
										  INameDecorator serviceObjectNameDecorator) {
		return "public class "+serviceObjectNameDecorator.decorate(classHeader.getName())+"";
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAdditionalImport(IDataSourceStrategy dataSourceStrategy) {
		return "";
	}

}
