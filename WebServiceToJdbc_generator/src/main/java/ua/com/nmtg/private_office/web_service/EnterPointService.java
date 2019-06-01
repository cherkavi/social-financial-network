package ua.com.nmtg.private_office.web_service;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import ua.com.nmtg.private_office.web_service.generator.code_description.parser.exceptions.AnalizeParserException;
import ua.com.nmtg.private_office.web_service.writer.IWriter;


public class EnterPointService {
	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		BasicConfigurator.configure();
		String springConfiguration=null;
		if(args.length>0){
			springConfiguration=args[0];
		}else{
			// springConfiguration="D:\\eclipse_workspace\\WebServiceToJdbc_generator\\office_private_transaction-settings.xml";
			springConfiguration="C:\\projects\\eclipse_workspace\\WebServiceToJdbc_generator\\cxf-spring-settings.xml";
		}
		ApplicationContext context=new FileSystemXmlApplicationContext(springConfiguration);
		Map<String, IWriter> needToGenerate=context.getBeansOfType(IWriter.class);
		Iterator<String> iterator=needToGenerate.keySet().iterator();
		while(iterator.hasNext()){
			String beanName=iterator.next();
			System.out.println("Bean Name:"+beanName);
			try{
				((IWriter)context.getBean(beanName)).write();
			}catch(AnalizeParserException ex){
				System.out.println("Write Exception:"+ex.getMessage());
			}
		}
		System.out.println("done.");
	}
}
