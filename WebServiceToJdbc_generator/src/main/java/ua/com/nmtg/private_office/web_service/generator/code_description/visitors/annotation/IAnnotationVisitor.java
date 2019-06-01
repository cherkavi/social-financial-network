package ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation;

import japa.parser.ast.expr.AnnotationExpr;

import java.util.List;
import java.util.Map;


/**
 * interface for describe the annotation of source file  
 */
public interface IAnnotationVisitor {
	/** 
	 * parse the list of annotation 
	 * @param list
	 */
	void checkAnnotationExpr(List<AnnotationExpr> list);
	
	/**
	 * need to call the {@link #checkAnnotationExpr(List)} before use this method 
	 * @return - list of readed parameters 
	 */
	List<Map<String, ?>> getPayload();
}
