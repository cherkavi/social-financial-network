package bc.service;

public class DBTableColumn {
	
	private String table_name;
	private String column_name;
	private String data_type;
	private String is_key_field;
	
	public DBTableColumn (String pTableName, String pColumnName, String pDataType, String pIsKeyField) {
		this.table_name = pTableName;
		this.column_name = pColumnName;
		this.data_type = pDataType;
		this.is_key_field = pIsKeyField;
	}
	
	public String getTableName() { return this.table_name;}
	public String getColumnName() { return this.column_name;}
	public String getDataType() { return this.data_type;}
	public String getIsKeyField() { return this.is_key_field;}
	
}
