package ua.com.nmtg.private_office.web_service.generator.code_description.parser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.UnitDescription;
import ua.com.nmtg.private_office.web_service.generator.code_description.parser.exceptions.AnalizeParserException;
import ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation.IAnnotationVisitor;
import ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation.implementation.DataBaseTypeAnnotation;

public class TestCodeParser extends TestCase {
	
	protected String getPathToInterface(){
		return "d:\\eclipse_workspace\\WebServiceHessian\\src\\com\\test\\ITest.java";	
	}
	
	protected UnitDescription getUnitDescription(List<IAnnotationVisitor>  visitors) throws AnalizeParserException{
		return CodeParser.parseSourceFile(this.getPathToInterface(), 
										  visitors);
	}
	
	
	public void testMain() throws AnalizeParserException {
		System.out.println("---- Parsed Value ----");
		IAnnotationVisitor annotationVisitor=new DataBaseTypeAnnotation();
		List<IAnnotationVisitor> listOfAnnotation=new LinkedList<IAnnotationVisitor>();
		listOfAnnotation.add(annotationVisitor);
		System.out.println(getUnitDescription(listOfAnnotation).toString());
		printList(annotationVisitor.getPayload());
		System.out.println("----------------------");
	}

	private void printList(List<Map<String, ?>> payload) {
		for(Map<String, ?> each:payload){
			System.out.println(">>> ");
			printMap("    ", each);
			System.out.println("<<< ");
		}
	}

	@SuppressWarnings("unchecked")
	private void printMap(String preambula, Map<String, ?> map) {
		map.entrySet().iterator();
		Iterator<?> iterator=map.entrySet().iterator();
		while(iterator.hasNext()){
			System.out.print(preambula);
			Entry<String, ?> entry=(Entry<String, ?>)iterator.next();
			System.out.println("Key: "+entry.getKey()+"     Value:"+entry.getValue());
		}
	}
}
