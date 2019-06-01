package ua.com.nmtg.private_office.web_service.generator.java_service;

import java.util.List;

import ua.com.nmtg.private_office.web_service.generator.ISourceGenerator;
import ua.com.nmtg.private_office.web_service.generator.change_package.IChangePackageStrategy;
import ua.com.nmtg.private_office.web_service.generator.change_package.NoChangePackageStrategy;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.MethodSignature;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.UnitDescription;
import ua.com.nmtg.private_office.web_service.generator.code_description.visitors.annotation.IAnnotationVisitor;
import ua.com.nmtg.private_office.web_service.generator.java_service.data_access_strategy.EmptyDataSourceStrategy;
import ua.com.nmtg.private_office.web_service.generator.java_service.data_access_strategy.IDataSourceStrategy;
import ua.com.nmtg.private_office.web_service.generator.java_service.ws_strategy.EmptyServiceStrategy;
import ua.com.nmtg.private_office.web_service.generator.java_service.ws_strategy.IServiceStrategy;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.EmptyNameDecorator;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.INameDecorator;

/** 
 * service Generator 
 */
public class JavaServiceGenerator implements ISourceGenerator{
	private INameDecorator nameDecorator=new EmptyNameDecorator();
	private IChangePackageStrategy changePackageStrategy=new NoChangePackageStrategy();
	private IServiceStrategy serviceStrategy=new EmptyServiceStrategy();
	private INameDecorator proxyObjectNameDecorator=new EmptyNameDecorator();
	private IChangePackageStrategy proxyChangePackageStrategy=new NoChangePackageStrategy();
	private IDataSourceStrategy dataSourceStrategy=new EmptyDataSourceStrategy();
	private List<IAnnotationVisitor> annotationVisitors;
	
	/**
	 * service Generator 
	 */
	public JavaServiceGenerator(){
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
	
	/**
	 * set change package strategy for Proxy object 
	 * @param packageStrategy
	 */
	public void setProxyChangePackageStrategy(IChangePackageStrategy packageStrategy){
		if(packageStrategy!=null){
			this.proxyChangePackageStrategy=packageStrategy;
		}
	}
	

	/** 
	 * set Strategy for generate the WebService ( it depends on use framework ) 
	 * @param serviceStrategy
	 */
	public void setServiceStrategy(IServiceStrategy serviceStrategy){
		if(serviceStrategy!=null){
			this.serviceStrategy=serviceStrategy;
		}
	}

	
	/**
	 * set name strategy for Proxy object 
	 * @param nameDecorator
	 */
	public void setProxyObjectNameDecorator(INameDecorator nameDecorator){
		if(nameDecorator!=null){
			this.proxyObjectNameDecorator=nameDecorator;
		}
	}

	
	/**
	 * set strategy for retrive DataSource for Object 
	 * @param dataSourceStrategy
	 */
	public void setDataSourceStrategy(IDataSourceStrategy dataSourceStrategy){
		if(dataSourceStrategy!=null){
			this.dataSourceStrategy=dataSourceStrategy;
		}
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public String generateSourceCode(UnitDescription description) {
		// TODO - add analize the annotation 
		final String lineDelimiter="\n";
		final String space="    ";
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("package "+this.changePackageStrategy.getPackageName(description.getPackageName())).append(";");
		returnValue.append(lineDelimiter);
		returnValue.append(lineDelimiter);
		returnValue.append(description.getImportList().toString());
		returnValue.append(lineDelimiter);
		returnValue.append("import "+description.getPackageName().getPackageName()+((description.getPackageName().getPackageName().length()!=0)?".":"")+description.getClassHeader().getName()+";");
		returnValue.append(lineDelimiter);
		returnValue.append("import "
						   +proxyChangePackageStrategy.getPackageName(description.getPackageName()) 
				 		   +((proxyChangePackageStrategy.getPackageName(description.getPackageName()).length()!=0)?".":"")
				 		   +this.proxyObjectNameDecorator.decorate(description.getClassHeader().getName())
				 		   +";");
		returnValue.append(lineDelimiter);
		if(this.serviceStrategy.getAdditionalImport(this.dataSourceStrategy)!=null){
			returnValue.append(this.serviceStrategy.getAdditionalImport(this.dataSourceStrategy));
		}
		returnValue.append(lineDelimiter).append(lineDelimiter);
		
		returnValue.append(this.serviceStrategy.getFullClassDescription(description.getClassHeader(), this.nameDecorator)).append("{");
		returnValue.append(lineDelimiter);
		returnValue.append(this.serviceStrategy.getFieldsAndConstructor(description.getClassHeader(), this.nameDecorator, this.proxyObjectNameDecorator, this.dataSourceStrategy));
		returnValue.append(lineDelimiter);
		
		List<MethodSignature> methodList=description.getListOfMethods();
		for(MethodSignature method:methodList){
			returnValue.append(space);
			returnValue.append(method.toString());
			returnValue.append("{");
			returnValue.append(lineDelimiter);
			returnValue.append(space);
			returnValue.append(this.serviceStrategy.getProxyMethodBody(method));
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
		return this.annotationVisitors;
	}

}
