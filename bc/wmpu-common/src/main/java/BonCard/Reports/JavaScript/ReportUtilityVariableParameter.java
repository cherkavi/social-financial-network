package BonCard.Reports.JavaScript;

import org.apache.log4j.Logger;
import java.sql.*;

public class ReportUtilityVariableParameter {
	private final static Logger LOGGER=Logger.getLogger(ReportUtilityVariableParameter.class);
	
	/** SQL_TEXT */
	private String field_sql_text;
	/** ID_REPORT */
	private String field_id_report;
	/** CD_PARAM */
	private String field_html_id;
	/** REQUIRED */
	private boolean field_required;
	/** DEFAULT_VALUE*/
	private String field_default_value;
	/** MYLTI_CHOISE*/
	private boolean field_multi_choice;
	
	public ReportUtilityVariableParameter(){
		clear();
	}
	
	public void setFromResultSet(ResultSet resultset){
		LOGGER.debug("set from ResultSet:BEGIN");
		try{
			this.setSQLText(resultset.getString("SQL_TEXT"));
			this.setIdReport(resultset.getString("ID_REPORT"));
			this.setHtmlId(resultset.getString("CD_PARAM"));
			this.setRequired(resultset.getString("REQUIRED"));
			this.setDefaultValue(resultset.getString("DEFAULT_VALUE"));
			this.setMultiChoice(resultset.getString("MYLTI_CHOISE"));
			LOGGER.debug("all variables is set");
		}catch(SQLException ex){
			LOGGER.debug("SQLException:"+ex.getMessage()); 
		}catch(Exception ex){
			LOGGER.debug("Exception:"+ex.getMessage());
		}
		LOGGER.debug("set from ResultSet:END");
	}

	public void clear(){
		this.field_sql_text="";
		this.field_id_report="";
		this.field_html_id="";
		this.field_required=false;
		this.field_default_value="";
		this.field_multi_choice=false;
		LOGGER.debug("all variables is cleared");
	}
	
	public void setMultiChoice(String value){
		String get_value=value.trim().toUpperCase();
		if(get_value.equals("Y")||get_value.equals("YES")){
			this.setMultiChoice(true);
		}else{
			this.setMultiChoice(false);
		}
	}
	public void setMultiChoice(boolean value){
		this.field_multi_choice=value;
	}
	public boolean isMultiChoice(){
		return this.field_multi_choice;
	}

	public void setRequired(String value){
		String get_value=value.trim().toUpperCase();
		if(get_value.equals("Y")||get_value.equals("YES")){
			this.setRequired(true);
		}else{
			this.setRequired(false);
		}
	}
	public void setRequired(boolean value){
		this.field_required=value;
	}
	/** является ли поле обязательных для передачи параметра, либо же параметр может быть опущен*/
	public boolean isRequired(){
		return this.field_required;
	}
	
	
	public void setDefaultValue(String value){
		this.field_default_value=value;
	}
	public String getDefaultValue(){
		return this.field_default_value;
	}
	
	public void setHtmlId(String value){
		this.field_html_id=value;
	}
	public String getHtmlId(){
		return this.field_html_id;
	}
	
	public void setIdReport(String value){
		this.field_id_report=value;
	}
	public String getIdReport(){
		return this.field_id_report;
	}
	
	public void setSQLText(String value){
		this.field_sql_text=value;
	}
	public String getSQLText(){
		return this.field_sql_text;
	}
}
