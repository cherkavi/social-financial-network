package ua.com.nmtg.private_office.web_service.generator.code_description.parser;

import ua.com.nmtg.private_office.web_service.generator.change_package.SubPackage;
import ua.com.nmtg.private_office.web_service.generator.code_description.parser.exceptions.AnalizeParserException;
import ua.com.nmtg.private_office.web_service.generator.java_service.JavaServiceGenerator;
import ua.com.nmtg.private_office.web_service.generator.java_service.data_access_strategy.DataSourceJndiStrategy;
import ua.com.nmtg.private_office.web_service.generator.java_service.ws_strategy.HessianServiceStrategy;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.PrePostNameDecorator;

public class TestJavaServiceGenerator extends TestCodeParser{

	@Override
	public void testMain() throws AnalizeParserException {
		super.testMain();
		System.out.println("----- TestJavaServiceGenerator -----");
		JavaServiceGenerator serviceGenerator=new JavaServiceGenerator();
		
		serviceGenerator.setNameDecorator(new PrePostNameDecorator("", "_WS"));
		serviceGenerator.setChangePackageStrategy(new SubPackage("service"));
		serviceGenerator.setProxyObjectNameDecorator(new PrePostNameDecorator("","_Proxy"));
		serviceGenerator.setDataSourceStrategy(new DataSourceJndiStrategy("java:/comp/env/jdbc/data_source"));
		serviceGenerator.setServiceStrategy(new HessianServiceStrategy());
		
		System.out.println(serviceGenerator.generateSourceCode(getUnitDescription(null)));
		System.out.println("------------------------------------");
	}
}
