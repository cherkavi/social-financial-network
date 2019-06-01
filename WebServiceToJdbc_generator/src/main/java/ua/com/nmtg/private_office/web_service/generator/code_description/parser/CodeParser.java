package ua.com.nmtg.private_office.web_service.generator.code_description.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.ClassHeader;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.ImportList;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.MethodParameter;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.MethodReturnValue;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.MethodSignature;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.PackageName;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.UnitDescription;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.MethodSignature.Visibility;
import ua.com.nmtg.private_office.web_service.generator.code_description.parser.exceptions.AnalizeParserException;
import ua.com.nmtg.private_office.web_service.generator.code_description.parser.exceptions.FileNotFoundParserException;
import ua.com.nmtg.private_office.web_service.generator.code_description.parser.exceptions.FileIsNotInterfaceParserException;
import ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation.IAnnotationVisitor;
import ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation.IMethodAnnotationVisitor;
import ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation.ITypeAnnotationVisitor;

public class CodeParser {

	/**
	 * parse the source code of The ExternalFile 
	 * @param pathToFile
	 * @param annotationVisitors 
	 * @return
	 * @throws AnalizeParserException
	 */
	public static UnitDescription parseSourceFile(String pathToFile , 
												  List<IAnnotationVisitor> visitors) throws AnalizeParserException{
		CodeParser parser=new CodeParser();
		CompilationUnit compilationUnit=null;
		try{
			compilationUnit=parser.getCompilationUnit(pathToFile);
		}catch(FileNotFoundException ex){
			throw new FileNotFoundParserException("Try to detect the File :"+pathToFile);
		}catch(ParseException ex){
			throw new AnalizeParserException("Parse the source Exception: "+ex.getMessage());
		}
		return parseSourceFile(parser, 
							   compilationUnit,
							   visitors);
	}

	/**
	 * parse the Source as InputStream 
	 * @param is
	 * @return
	 * @throws AnalizeParserException
	 */
	public static UnitDescription parseSource( InputStream is, List<IAnnotationVisitor> visitors) throws AnalizeParserException{
		CodeParser parser=new CodeParser();
		CompilationUnit compilationUnit=null;
		try{
			compilationUnit=parser.getCompilationUnit(is);
		}catch(ParseException ex){
			throw new AnalizeParserException("Parse the source Exception: "+ex.getMessage());
		}
		return parseSourceFile(parser, compilationUnit, visitors);
	}
	
	/**
	 * 
	 * @param visitors - array of IAnnotationVisitor of different types 
	 * @param classes - type of the visitor, which will be retrive from array 
	 * @param list - annotation for analise
	 */
	private static void walkAnnotationVisitorsByType(List<IAnnotationVisitor> visitors, 
													 Class<? extends IAnnotationVisitor> filterClass, 
													 List<AnnotationExpr> list){
		if((visitors!=null)&&(visitors.size()>0)){
			for(IAnnotationVisitor visitor: visitors){
				if(filterClass.isAssignableFrom(visitor.getClass())){
					visitor.checkAnnotationExpr(list);
				}
			}
		}
	}
	

	public static UnitDescription parseSourceFile(final CodeParser parser, 
												  final CompilationUnit compilationUnit, 
												  final List<IAnnotationVisitor> visitors ) throws AnalizeParserException{
		// parse the {@link PackageName}
		new VoidVisitorAdapter<Object>() {
			public void visit(japa.parser.ast.PackageDeclaration packageDeclaration, Object arg) {
				parser.setPackageName(new PackageName(packageDeclaration.getName().toString()));
			};
		}.visit(compilationUnit, null);
		
		// parse the {@link ImportList}
		new VoidVisitorAdapter<Object>() {
			public void visit(japa.parser.ast.ImportDeclaration importDeclaration, Object arg) {
				parser.addImportDeclaration(importDeclaration.getName().toString());
			};
		}.visit(compilationUnit, null);
		
		// parse the {@link ClassHeader}  
		new VoidVisitorAdapter<Object>() {
			public void visit(ClassOrInterfaceDeclaration declaration, 
							  Object arg) {
				walkAnnotationVisitorsByType(visitors, 
											 ITypeAnnotationVisitor.class, 
											 declaration.getAnnotations()
											 );
				parser.setClassHeader(new ClassHeader(declaration.getName(), 
									  declaration.isInterface()));
			};
			
		}.visit(compilationUnit, null);
		
		// parse the {@link MethodSignature}
		new VoidVisitorAdapter<Object>() {
			public void visit(japa.parser.ast.body.MethodDeclaration methodDeclaration, 
							  Object arg) {
				Visibility visibility=Visibility.v_public;
				MethodReturnValue returnValue=new MethodReturnValue(methodDeclaration.getType().toString());
				String name=methodDeclaration.getName();
				List<MethodParameter> parameters=new LinkedList<MethodParameter>();

				List<Parameter> list=methodDeclaration.getParameters();
				if(list!=null && !list.isEmpty())
					for(Parameter each:list){
						parameters.add(new MethodParameter(each.getType().toString(), each.getId().getName()));
					}
				walkAnnotationVisitorsByType(visitors, 
											 IMethodAnnotationVisitor.class, 
											 methodDeclaration.getAnnotations()
											 );
				parser.addMethodSignature(new MethodSignature(visibility, returnValue, name, parameters));
				// System.out.println("Method name:"+methodDeclaration.getName()+"  arguments: "+arg);
				
			}

			/*public void visit(japa.parser.ast.body.ClassOrInterfaceDeclaration declaration, Object arg) {
				System.out.println("ClassOrInterface declaration:"+declaration.getName()+"   is Interface:"+declaration.isInterface());
			};*/
		}.visit(compilationUnit, null);
		/*
		new VoidVisitorAdapter<Object>(){
			public void visit(japa.parser.ast.body.AnnotationDeclaration annotation, Object arg) {
				System.out.println("Annotation Name: "+annotation.getName());
				printAnnotationExprList(annotation.getAnnotations());
				List<japa.parser.ast.body.BodyDeclaration> listOfDeclaration=annotation.getMembers();
				System.out.println("---- Members ----");
				for(BodyDeclaration body:listOfDeclaration){
					System.out.println("	Data: "+body.getData());
				}
				System.out.println("-----------------");
			};
		}.visit(compilationUnit, null);
		*/
		/*
		new VoidVisitorAdapter<Object>(){
			public void visit(japa.parser.ast.body.AnnotationMemberDeclaration annotation, Object arg) {
				System.out.println("Name: "+annotation.getName()+"   Type: "+annotation.getType().toString()+"   DefaultValue:"+annotation.getDefaultValue().getData());
				printAnnotationExprList(annotation.getAnnotations());
			};
		}.visit(compilationUnit, null);
		*/
		return parser.build();
	}
	
	private UnitDescription build() throws AnalizeParserException{
		if(this.classHeader.isInteface()==false){
			throw new FileIsNotInterfaceParserException(this.classHeader.getName());
		}
		
		return new UnitDescription(){
			{
				this.setPackageName(packageName);
				this.setImportList(importList);
				this.setClassHeader(classHeader);
				this.addMethodsSignature(listOfMethods);
			}
		};
	}

	private CodeParser(){}
	private ClassHeader classHeader;
	private PackageName packageName;
	private ImportList importList=new ImportList();
	private List<MethodSignature> listOfMethods=new LinkedList<MethodSignature>();
	
	private void setClassHeader(ClassHeader classHeader){
		this.classHeader=classHeader;
	}

	private void setPackageName(PackageName packageName) {
		this.packageName=packageName;
	}
	
	private void addImportDeclaration(String importName){
		this.importList.addImport(importName);
	}

	private void addMethodSignature(MethodSignature methodSignature) {
		this.listOfMethods.add(methodSignature);
	}

	
	
	/**
	 * utility method 
	 * @param pathToFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws ParseException
	 */
	private CompilationUnit getCompilationUnit(String pathToFile) throws FileNotFoundException, ParseException{
		InputStream in = null;
        try{
        	in=new FileInputStream(new File(pathToFile));
        	return getCompilationUnit(in);
        }
        finally{
       	 try{
       		 in.close();
       	 }catch(Exception ex){};
        }
	}

	private CompilationUnit getCompilationUnit(InputStream inputStream ) throws ParseException{
        return JavaParser.parse(inputStream);
	}
	
}
