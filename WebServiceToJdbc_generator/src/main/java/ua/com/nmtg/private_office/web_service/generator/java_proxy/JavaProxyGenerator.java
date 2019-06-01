package ua.com.nmtg.private_office.web_service.generator.java_proxy;

import java.util.List;

import ua.com.nmtg.private_office.web_service.generator.ISourceGenerator;
import ua.com.nmtg.private_office.web_service.generator.change_package.IChangePackageStrategy;
import ua.com.nmtg.private_office.web_service.generator.change_package.NoChangePackageStrategy;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.MethodSignature;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.UnitDescription;
import ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation.IAnnotationVisitor;
import ua.com.nmtg.private_office.web_service.generator.java_proxy.strategy.EmptyProxyServiceStrategy;
import ua.com.nmtg.private_office.web_service.generator.java_proxy.strategy.IProxyServiceStrategy;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.EmptyNameDecorator;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.INameDecorator;

public class JavaProxyGenerator implements ISourceGenerator{
	private INameDecorator nameDecorator=new EmptyNameDecorator();
	private IChangePackageStrategy changePackageStrategy=new NoChangePackageStrategy();
	private IProxyServiceStrategy serviceStrategy=new EmptyProxyServiceStrategy();
	private List<IAnnotationVisitor> annotationVisitors=null;
	
	public JavaProxyGenerator(){
	}
	
	/**
	 * set Decorator for name of class ( wich will be generate )  
	 * @param nameDecorator
	 */
	public void setNameDecorator(INameDecorator nameDecorator){
		if(nameDecorator!=null){
			this.nameDecorator=nameDecorator;
		}
	}
	
	/**
	 * set Strategy for change the package in object wich will be create  
	 * @param packageStrategy
	 */
	public void setChangePackageStrategy(IChangePackageStrategy packageStrategy){
		if(packageStrategy!=null){
			this.changePackageStrategy=packageStrategy;
		}
	}
	
	public void setProxyServiceStrategy(IProxyServiceStrategy serviceStrategy){
		if(serviceStrategy!=null){
			this.serviceStrategy=serviceStrategy;
		}
	}
	
	@Override
	public String generateSourceCode(UnitDescription description) {
		// TODO - analise the annotation for generate the Java Proxy
		final String lineDelimiter="\n";
		final String space="    ";
		final String fieldDataSource="dataSource";
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("package "+this.changePackageStrategy.getPackageName(description.getPackageName())).append(";");
		returnValue.append(lineDelimiter);
		returnValue.append(lineDelimiter);
		returnValue.append(description.getImportList().toString());
		if(this.serviceStrategy.getAdditionalImport()!=null){
			returnValue.append(this.serviceStrategy.getAdditionalImport());
		}
		returnValue.append(lineDelimiter);
		returnValue.append(lineDelimiter);
		String className=this.nameDecorator.decorate(description.getClassHeader().getName());
		returnValue.append("public class "+className);
		returnValue.append(" implements "+description.getClassHeader().getName()).append("{");
		returnValue.append(lineDelimiter);
		returnValue.append(lineDelimiter);
		returnValue.append(this.serviceStrategy.getConstructorAndFields(className, fieldDataSource));
		returnValue.append(lineDelimiter);
		returnValue.append(lineDelimiter);
		
		List<MethodSignature> methodList=description.getListOfMethods();
		for(MethodSignature method:methodList){
			returnValue.append(space);
			returnValue.append(method.toString());
			returnValue.append("{");
			returnValue.append(lineDelimiter);
			returnValue.append(space);
			returnValue.append("}");
			returnValue.append(lineDelimiter);
			returnValue.append(lineDelimiter);
		}
		returnValue.append("}");
		return returnValue.toString();
	}

	@Override
	public String getSourceName(UnitDescription description) {
		return this.nameDecorator.decorate(description.getClassHeader().getName())+".java";
	}

	public void setAnnotationVisitors(List<IAnnotationVisitor> annotationVisitors){
		this.annotationVisitors=annotationVisitors;
	}
	
	@Override
	public List<IAnnotationVisitor> getAnnotationVisitors() {
		return annotationVisitors;
	}

}
