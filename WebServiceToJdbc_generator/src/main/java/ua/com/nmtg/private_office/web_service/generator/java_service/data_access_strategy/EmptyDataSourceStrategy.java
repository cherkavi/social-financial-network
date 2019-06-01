package ua.com.nmtg.private_office.web_service.generator.java_service.data_access_strategy;

public class EmptyDataSourceStrategy implements IDataSourceStrategy{
	protected final static String EMPTY=""; 
	@Override
	public String getAdditionalConstructorBody(String dataSourceFieldName) {
		return EMPTY;
	}

	@Override
	public String getAdditionalFields() {
		return EMPTY;
	}

	@Override
	public String getAdditionalImport() {
		return EMPTY;
	}

}
