package ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation.implementation;

import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NormalAnnotationExpr;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation.ITypeAnnotationVisitor;

public class DataBaseTypeAnnotation implements ITypeAnnotationVisitor{
	private final static String nameOfRootAnnotation="DataBase";
	private final static String rootArgumentName="listOfElements";
	private final static String nameOfElementAnnotation="DataBaseElement";
	
	private AnnotationExpr filterAnnotationExprByName(List<AnnotationExpr> list, String name){
		for(AnnotationExpr expr: list){
			if(expr.getName().toString().equals(name)){
				return expr;
			}
		}
		return null;
	}
	
	private MemberValuePair filterMemberValuePairByName(List<MemberValuePair> list, String name){
		for(MemberValuePair pair: list){
			if(pair.getName().toString().equals(name)){
				return pair;
			}
		}
		return null;
	}
	
	private List<NormalAnnotationExpr> filterAndTransformListByName(List<Expression> list, String name){
		List<NormalAnnotationExpr> returnValue=new LinkedList<NormalAnnotationExpr>();
		for(Expression expression:list){
			if(expression instanceof NormalAnnotationExpr){
				NormalAnnotationExpr normalAnnotation=(NormalAnnotationExpr)expression;
				if( normalAnnotation.getName().getName().equals(name)){
					returnValue.add(normalAnnotation);
				}
			}
		}
		return returnValue;
	}
	
	private List<Map<String, ?>> payload=new LinkedList<Map<String,?>>();
	
	@Override
	public List<Map<String, ?>> getPayload(){
		return this.payload;
	}
	
	private void convertToDestination(List<NormalAnnotationExpr> listOfDataBaseElements){
		payload.clear();
		if(listOfDataBaseElements.size()>0){
			for(NormalAnnotationExpr expression:listOfDataBaseElements){
				payload.add(getMapFromExpression(expression));
			}
		}else{
			// array of elements is empty 
			
		}
	}
	
	private Map<String, String> getMapFromExpression(NormalAnnotationExpr expression) {
		Map<String, String> returnValue=new HashMap<String, String>();
		for(MemberValuePair pair :  expression.getPairs()){
			returnValue.put(pair.getName(), pair.getValue().toString());
		}
		return returnValue;
	}

	@Override
	public void checkAnnotationExpr(List<AnnotationExpr> listOfExpr) {
		System.out.println("---- AnnotationExpr List ----");
		if((listOfExpr!=null)&&(listOfExpr.size()>0)){
			NormalAnnotationExpr normalExpression=(NormalAnnotationExpr)this.filterAnnotationExprByName(listOfExpr, nameOfRootAnnotation);
			if(normalExpression!=null){
				MemberValuePair elementsList=filterMemberValuePairByName(normalExpression.getPairs(), rootArgumentName);
				if((elementsList!=null)&&( elementsList.getValue() instanceof ArrayInitializerExpr )){
					ArrayInitializerExpr rootArgumentExpression=(ArrayInitializerExpr)elementsList.getValue();
					convertToDestination(filterAndTransformListByName(rootArgumentExpression.getValues(), 
																      nameOfElementAnnotation));
				}else{
					// the mandatory parameter of the root element was not found 
				}
			}else{
				// root element was not found 
			}
		}
	
	}

}


/*


for(AnnotationExpr expr:listOfExpr){
if(expr instanceof NormalAnnotationExpr){
	NormalAnnotationExpr normalExpression=(NormalAnnotationExpr)expr;
	List<MemberValuePair> pairs=normalExpression.getPairs();
	System.out.println("---- Pairs: ---- ");
	for(MemberValuePair pair:pairs){
		String name=pair.getName();
		Expression expression=pair.getValue();
		if(expression instanceof ArrayInitializerExpr){
			ArrayInitializerExpr arrayExpression=(ArrayInitializerExpr)expression;
			List<Expression> arrayOfExpression=arrayExpression.getValues();
			System.out.println("		Name:"+name+"  >>>>");
			for(Expression arrayElement:arrayOfExpression){
				if(arrayElement instanceof NormalAnnotationExpr){
					NormalAnnotationExpr normalArrayElement=(NormalAnnotationExpr)arrayElement;
					for(MemberValuePair arrayPair: normalArrayElement.getPairs()){
						System.out.println("			Name:"+arrayPair.getName()+"   Pair:"+arrayPair.getValue());
					}
				}else{
					System.out.println("			ArrayElement:"+arrayElement);
				}
			}
			System.out.println("        >>>>");
		}else{
			System.out.println("		Name:"+name+"    Value:"+expression);
		}
	}
	System.out.println("---------------");
}else{
	System.out.println("	Name: "+expr.getName()+"    Data: "+expr.getData());
}
}
System.out.println("---- ------------------ ----");

*/