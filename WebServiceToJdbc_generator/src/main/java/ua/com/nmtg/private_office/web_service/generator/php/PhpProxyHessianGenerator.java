package ua.com.nmtg.private_office.web_service.generator.php;

import java.util.List;

import ua.com.nmtg.private_office.web_service.generator.ISourceGenerator;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.MethodParameter;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.MethodSignature;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.UnitDescription;
import ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation.IAnnotationVisitor;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.EmptyNameDecorator;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.INameDecorator;

public class PhpProxyHessianGenerator implements ISourceGenerator{
	private INameDecorator decorator=new EmptyNameDecorator();
	private List<IAnnotationVisitor> annotationVisitors;
	
	public PhpProxyHessianGenerator(){
	}
	
	/** */
	public void setNameDecorator(INameDecorator decorator){
		if(decorator!=null){
			this.decorator=decorator;
		}
	}
	
	/**
	 * {@inheritDoc}} 
	 */
	@Override
	public String generateSourceCode(UnitDescription description) {
		// TODO - add analize the annotation 
		String space="    ";
		String lineDelimiter="\n";
		StringBuilder returnValue=new StringBuilder();
		
		returnValue.append("class ").append(this.decorator.decorate(description.getClassHeader().getName())).append("{");
		returnValue.append(lineDelimiter);
		returnValue.append(space).append("private $proxy;").append(lineDelimiter).append(lineDelimiter);
		returnValue.append(space)
			.append("public function __construct($pUrl){")
			.append(lineDelimiter)
			.append(space)
			.append(space)
			.append("$this->proxy = new HessianClient($pUrl);")
			.append(lineDelimiter)
			.append(space)
			.append("}")
			.append(lineDelimiter)
			.append(lineDelimiter);
		
		for(MethodSignature method:description.getListOfMethods()){
			returnValue
			.append(space)
			.append("public function ").append(method.getName()).append(getParameterList(method)).append("{")
			.append(lineDelimiter)
			.append(space)
			.append(space)
			.append("return $this->proxy->").append(method.getName()).append(getParameterList(method)).append(";")
			.append(lineDelimiter)
			.append(space)
			.append("}")
			.append(lineDelimiter)
			.append(lineDelimiter)
			;
		}
		returnValue.append("};");
		return returnValue.toString();
	}

	
	private String getParameterList(MethodSignature method){
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("(");
		List<MethodParameter> parameters=method.getParameters();
		int index=0;
		for(MethodParameter parameter: parameters){
			if(index>0){
				returnValue.append(", ");
			}
			if(parameter.getName()!=null){
				returnValue.append("$");
				returnValue.append(parameter.getName());
			}else{
				returnValue.append("$p"+ index );
			}
			index++;
		}
		returnValue.append(")");
		return returnValue.toString();
	}

	@Override
	public String getSourceName(UnitDescription description) {
		return this.decorator.decorate(description.getClassHeader().getName())+".php";
	}

	public void setAnnotationVisitors(List<IAnnotationVisitor> annotationVisitors){
		this.annotationVisitors=annotationVisitors;
	}

	@Override
	public List<IAnnotationVisitor> getAnnotationVisitors() {
		return this.annotationVisitors;
	}
}
