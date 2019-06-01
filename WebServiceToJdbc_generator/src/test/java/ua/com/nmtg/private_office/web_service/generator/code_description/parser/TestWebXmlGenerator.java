package ua.com.nmtg.private_office.web_service.generator.code_description.parser;

import ua.com.nmtg.private_office.web_service.generator.change_package.SubPackage;
import ua.com.nmtg.private_office.web_service.generator.code_description.parser.exceptions.AnalizeParserException;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.PrePostNameDecorator;
import ua.com.nmtg.private_office.web_service.generator.web_xml.WebXmlHessianGenerator;

public class TestWebXmlGenerator extends TestCodeParser{
	@Override
	public void testMain() throws AnalizeParserException {
		super.testMain();
		WebXmlHessianGenerator sourceGenerator=new WebXmlHessianGenerator();
		sourceGenerator.setDisplayName("TestWebService");
		sourceGenerator.setAdditionalTags("<resource-ref>  \n" +
										  "	   <description>postgreSQL Datasource example</description>   \n" +
										  "    <res-ref-name>jdbc/data_source</res-ref-name>   \n" +
										  "    <res-type>javax.sql.DataSource</res-type>   \n" +
										  "    <res-auth>Container</res-auth>   \n" +
										  "</resource-ref> \n\n");
		sourceGenerator.setServiceNameDecorator(new PrePostNameDecorator("", "_WS"));
		sourceGenerator.setChangePackageStrategy(new SubPackage("service"));
		System.out.println("----- TestWebXmlGenerator -----");
		System.out.println(sourceGenerator.generateSourceCode(getUnitDescription(null)));
		System.out.println("-------------------------------");
	}
}
