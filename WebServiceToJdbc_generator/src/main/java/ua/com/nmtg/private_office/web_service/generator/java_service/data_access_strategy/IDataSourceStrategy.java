package ua.com.nmtg.private_office.web_service.generator.java_service.data_access_strategy;

/**
 * strategy for access to Data ( to Database via Connection, via DataSource ) 
 * @author Administrator
 *
 */
public interface IDataSourceStrategy {
	/** add fields or members (local fields declaration, anonymous methods, private methods ) after class declaration and before constructor declaration  */
	public String getAdditionalFields();
	/** add additional pieces of code to constructor  
	 * @param dataSourceFieldName - name of dataSource
	 * @return
	 */
	public String getAdditionalConstructorBody(String dataSourceFieldName);
	
	/** add additional import to Code */
	public String getAdditionalImport();

}
