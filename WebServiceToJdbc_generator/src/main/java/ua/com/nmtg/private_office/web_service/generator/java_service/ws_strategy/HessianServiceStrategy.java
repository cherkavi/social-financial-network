package ua.com.nmtg.private_office.web_service.generator.java_service.ws_strategy;


import ua.com.nmtg.private_office.web_service.generator.code_description.elements.ClassHeader;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.MethodParameter;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.MethodSignature;
import ua.com.nmtg.private_office.web_service.generator.java_service.data_access_strategy.IDataSourceStrategy;
import ua.com.nmtg.private_office.web_service.generator.name_decorator.INameDecorator;

public class HessianServiceStrategy implements IServiceStrategy{

	@Override
	public String getAdditionalImport(IDataSourceStrategy dataSourceStrategy) {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("import com.caucho.hessian.io.AbstractSerializerFactory; \n");
		returnValue.append("import com.caucho.hessian.io.Deserializer; \n");
		returnValue.append("import com.caucho.hessian.io.HessianProtocolException; \n");
		returnValue.append("import com.caucho.hessian.io.JavaDeserializer; \n");
		returnValue.append("import com.caucho.hessian.io.JavaSerializer; \n");
		returnValue.append("import com.caucho.hessian.io.Serializer; \n");
		returnValue.append("import com.caucho.hessian.server.HessianServlet; \n");
		return returnValue.toString()+dataSourceStrategy.getAdditionalImport();
	}

	@Override
	public String getProxyMethodBody(MethodSignature method) {
		StringBuilder returnValue=new StringBuilder();
		//                      return this.              proxy               .            getBeanById(       id)
		returnValue.append("    return this.").append(proxyNameField).append(".").append(method.getName())
				.append("(");
		StringBuilder parameterValue=new StringBuilder();
		for(MethodParameter parameter:method.getParameters()){
			if(parameterValue.length()>0){
				parameterValue.append(", ");
			}
			parameterValue.append(parameter.getName());
		}
		returnValue.append(parameterValue);
		returnValue.append(");");
		return returnValue.toString();
	}

	private final String proxyNameField="proxy";
	private final String dataSourceNameField="dataSource";
	
	@Override
	public String getFieldsAndConstructor(ClassHeader classHeader,
										  INameDecorator serviceObjectNameDecorator,
										  INameDecorator proxyObjectNameDecorator,
										  IDataSourceStrategy dataSourceStrategy) {
		final String space="    ";
		final String lineDelimiter="\n";
		StringBuilder returnValue=new StringBuilder();
		returnValue.append(space);
		returnValue.append("private final "+proxyObjectNameDecorator.decorate(classHeader.getName())+" "+proxyNameField+";");
		returnValue.append(lineDelimiter);
		returnValue.append(dataSourceStrategy.getAdditionalFields());
		returnValue.append(space);
		returnValue.append("public "+ serviceObjectNameDecorator.decorate(classHeader.getName()) +"()").append("{");
		returnValue.append(lineDelimiter);
		returnValue.append(dataSourceStrategy.getAdditionalConstructorBody(dataSourceNameField));
		returnValue.append(lineDelimiter);
		returnValue.append(space);
		returnValue.append(space);
		returnValue.append("this.getSerializerFactory().addFactory(\n");
		returnValue.append("		new AbstractSerializerFactory(){\n");
		returnValue.append("			@Override\n");
		returnValue.append("			public Deserializer getDeserializer(final Class cl) throws HessianProtocolException {return new JavaDeserializer(cl);}\n");
		returnValue.append("			@Override\n");
		returnValue.append("			public Serializer getSerializer(final Class cl) throws HessianProtocolException {return new JavaSerializer(cl);}\n");
		returnValue.append("		}); \n");
		returnValue.append("this."+proxyNameField+"=new "+proxyObjectNameDecorator.decorate(classHeader.getName())+"("+dataSourceNameField+");");
		returnValue.append(lineDelimiter);
		returnValue.append(space);
		returnValue.append("}");
		returnValue.append(lineDelimiter);
		return returnValue.toString();
	}



	@Override
	public String getFullClassDescription(ClassHeader classHeader,
										  INameDecorator serviceObjectNameDecorator) {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("public class "+serviceObjectNameDecorator.decorate(classHeader.getName())+" extends HessianServlet implements "+classHeader.getName() );
		return returnValue.toString();
	}

}
