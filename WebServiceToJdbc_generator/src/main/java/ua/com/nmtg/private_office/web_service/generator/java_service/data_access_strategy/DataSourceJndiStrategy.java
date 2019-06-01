package ua.com.nmtg.private_office.web_service.generator.java_service.data_access_strategy;


public class DataSourceJndiStrategy implements IDataSourceStrategy{
	private final String jndiName;
	
	public DataSourceJndiStrategy(String jndiName){
		this.jndiName=jndiName;
	}
	
	@Override
	public String getAdditionalImport() {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append(lineDelimiter);
		returnValue.append("import javax.naming.Context;");
		returnValue.append(lineDelimiter);
		returnValue.append("import javax.naming.InitialContext;");
		returnValue.append(lineDelimiter);
		returnValue.append("import javax.sql.DataSource;");
		returnValue.append(lineDelimiter);
		return returnValue.toString();
	}

	
	@Override
	public String getAdditionalFields() {
		return "";
	}

	private final static String lineDelimiter="\n";
	
	@Override
	public String getAdditionalConstructorBody(String dataSourceFieldName) {
		final String space="    ";
		StringBuilder returnValue=new StringBuilder();
		returnValue.append(space);
		returnValue.append(space);
		returnValue.append("DataSource "+dataSourceFieldName+" =null;").append(lineDelimiter);
		returnValue.append(space);
		returnValue.append(space);
		returnValue.append("try{").append(lineDelimiter);
		returnValue.append(space);
		returnValue.append(space);
		returnValue.append(space);
		returnValue.append("Context initContext = new InitialContext();").append(lineDelimiter);
		returnValue.append(space);
		returnValue.append(space);
		returnValue.append(space);
		returnValue.append(dataSourceFieldName+" = (DataSource)initContext.lookup(\""+this.jndiName+"\");").append(lineDelimiter);
		returnValue.append(space);
		returnValue.append(space);
		returnValue.append("}catch(Exception ex){").append(lineDelimiter);
		returnValue.append(space);
		returnValue.append(space);
		returnValue.append(space);
		returnValue.append("System.err.println(\"create DataSource Exception:\"+ex.getMessage());");
		returnValue.append(lineDelimiter);
		returnValue.append(space);
		returnValue.append(space);
		returnValue.append("}");
		return returnValue.toString();
	}


}
