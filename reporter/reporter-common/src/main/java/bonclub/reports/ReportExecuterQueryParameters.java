package bonclub.reports;

import java.io.Serializable;
import java.sql.ResultSet;

/** 
 * класс-обертка для параметров запроса (хранит текст запроса и хранит путь к файлу, для мультиотчетов)
 */
public class ReportExecuterQueryParameters implements Serializable{
	private final static long serialVersionUID=1L;
	private String field_query;
	private String field_path_to_file;
	transient private ResultSet field_resultset;

	public ReportExecuterQueryParameters(String query,String path_to_file){
		//this.field_query=query;
		//this.field_path_to_file=path_to_file;
		this.setQuery(query);
		this.setPathToFile(path_to_file);
	}

	/** получить значение для ResultSet */
	public ResultSet getResultSet(){
		return this.field_resultset;
	}

	/** установить значение для ResultSet */
	public void setResultSet(ResultSet value){
		this.field_resultset=value;
	}

	/** установить текст запроса */
	public void setQuery(String value){
		this.field_query=value;
	}
	
	/** получить текст запроса */
	public String getQuery(){
		return this.field_query;
	}
	
	/** устновить путь к файлу-отчета */
	public void setPathToFile(String value){
		this.field_path_to_file=value;
	}
	
	/** получить путь к файлу-отчета */
	public String getPathToFile(){
		return this.field_path_to_file;
	}
}
