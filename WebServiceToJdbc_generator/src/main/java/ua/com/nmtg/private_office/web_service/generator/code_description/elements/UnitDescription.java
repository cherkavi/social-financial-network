package ua.com.nmtg.private_office.web_service.generator.code_description.elements;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation.IAnnotationVisitor;

/**
 * main object for collect elements, such as:
 * <ul>
 * 	<li> PackageName </li>
 * 	<li> ImportList </li>
 * 	<li> ClassHeader </li>
 * 	<li> MethodSignature (List)
 * 		<ul>
 * 			<li>MethodReturnValue</li>
 * 			<li>MethodParamter</li>
 * 		</ul> 
 * 	</li>
 * </ul>
 */
public class UnitDescription {
	private PackageName packageName;
	private ImportList importList;
	private ClassHeader classHeader;
	private List<MethodSignature> listOfMethods=new LinkedList<MethodSignature>();
	private List<IAnnotationVisitor> listOfAnnotationVisitors=null;
	
	public UnitDescription(){
		
	}
	
	public UnitDescription(PackageName packageName, 
						   ImportList importList,
						   ClassHeader classHeader, 
						   List<MethodSignature> listOfMethods,
						   List<IAnnotationVisitor> listOfAnnotationVisitors) {
		super();
		this.packageName = packageName;
		this.importList = importList;
		this.classHeader = classHeader;
		this.listOfMethods = listOfMethods;
		this.listOfAnnotationVisitors=listOfAnnotationVisitors;
	}

	protected UnitDescription setPackageName(PackageName packageName){
		this.packageName=packageName;
		return this;
	}
	
	protected UnitDescription setImportList(ImportList importList){
		this.importList=importList;
		return this;
	}
	
	protected UnitDescription setClassHeader(ClassHeader classHeader){
		this.classHeader=classHeader;
		return this;
	}
	
	protected UnitDescription addMethodSignature(MethodSignature method){
		this.listOfMethods.add(method);
		return this;
	}
	
	protected UnitDescription addMethodsSignature(Collection<MethodSignature> methods){
		this.listOfMethods.addAll(methods);
		return this;
	}
	
	public PackageName getPackageName() {
		return packageName;
	}

	public ImportList getImportList() {
		return importList;
	}

	public ClassHeader getClassHeader() {
		return classHeader;
	}
	
	public List<IAnnotationVisitor> getAnnotationVisitors(){
		return this.listOfAnnotationVisitors;
	}

	/**
	 * if need to add some method - just call this method and add it 
	 * @return
	 */
	public List<MethodSignature> getListOfMethods() {
		return listOfMethods ;
	}


	@Override
	public String toString() {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append(this.packageName.toString());
		returnValue.append("\n");
		returnValue.append(this.importList.toString());
		returnValue.append("\n");
		returnValue.append(this.classHeader.toString());
		returnValue.append("{\n");
		StringBuilder methods=new StringBuilder();
		for(MethodSignature eachMethod:this.listOfMethods){
			methods.append("    ");
			methods.append(eachMethod.toString());
			methods.append(";\n");
		}
		returnValue.append(methods);
		returnValue.append("}");
		return returnValue.toString();
	}
}
