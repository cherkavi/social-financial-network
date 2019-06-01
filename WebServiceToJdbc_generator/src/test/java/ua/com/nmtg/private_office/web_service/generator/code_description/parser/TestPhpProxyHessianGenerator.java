package ua.com.nmtg.private_office.web_service.generator.code_description.parser;

import ua.com.nmtg.private_office.web_service.generator.code_description.parser.exceptions.AnalizeParserException;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.PrePostNameDecorator;
import ua.com.nmtg.private_office.web_service.generator.php.PhpProxyHessianGenerator;

public class TestPhpProxyHessianGenerator extends TestCodeParser{
	
	public void testMain() throws AnalizeParserException{
		super.testMain();
		System.out.println("---- TestPhpProxyHessianGenerator ---- ");
		// create Proxy 
		PhpProxyHessianGenerator sourceGenerator=new PhpProxyHessianGenerator();
		sourceGenerator.setNameDecorator(new PrePostNameDecorator("Proxy_",null));
		// output value 
		System.out.println(sourceGenerator.generateSourceCode(this.getUnitDescription(null)));
		System.out.println("---- ---------------------------- ---- ");
	}
}
