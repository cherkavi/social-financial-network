package ua.com.nmtg.private_office.web_service.generator.code_description.parser;

import ua.com.nmtg.private_office.web_service.generator.change_package.SubPackage;
import ua.com.nmtg.private_office.web_service.generator.code_description.parser.exceptions.AnalizeParserException;
import ua.com.nmtg.private_office.web_service.generator.java_proxy.JavaProxyGenerator;
import ua.com.nmtg.private_office.web_service.generator.java_proxy.strategy.DataSourceProxyServiceStrategy;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.PrePostNameDecorator;

public class TestJavaProxyGenerator extends TestCodeParser{
	@Override
	public void testMain() throws AnalizeParserException {
		super.testMain();
		
		System.out.println("---- TestJavaProxyGenerator ----");
		JavaProxyGenerator sourceGenerator=new JavaProxyGenerator();
		sourceGenerator.setChangePackageStrategy(new SubPackage("service"));
		sourceGenerator.setNameDecorator(new PrePostNameDecorator("", "_Proxy"));
		sourceGenerator.setProxyServiceStrategy(new DataSourceProxyServiceStrategy());
		System.out.println(sourceGenerator.generateSourceCode(getUnitDescription(null)));
		System.out.println("--------------------------------");
	}
}
