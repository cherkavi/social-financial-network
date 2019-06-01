package ua.com.nmtg.private_office.web_service.generator.java_proxy.strategy;

public class DataSourceProxyServiceStrategy implements IProxyServiceStrategy{

	@Override
	public String getConstructorAndFields(String className, String fieldDataSourceName) {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("    private DataSource "+fieldDataSourceName+"; \n");
		returnValue.append("    public "+className+"(DataSource dataSource){ this."+fieldDataSourceName+"=dataSource; }");
		return returnValue.toString();
	}

	@Override
	public String getAdditionalImport() {
		return "import javax.sql.DataSource;";
	}

}
