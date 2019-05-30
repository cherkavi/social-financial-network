package bc.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.connection.Connector;

public class bcDictionaryColumnsObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcDictionaryColumnsObject.class);
	
	private Map <String, bc.service.DBTableColumn> columnList = new HashMap<String, bc.service.DBTableColumn>();
	private Map <String, bc.service.DBTableColumn> translateColumnList = new HashMap<String, bc.service.DBTableColumn>();
	
	private String tableName;
	private String hasTranslate;
	
	public bcDictionaryColumnsObject(String pTableName, String pHasTranslate) {
		this.tableName = pTableName;
		this.hasTranslate = pHasTranslate;
		initAllColumns();		
	}
	
	private void initAllColumns() {
		
		String mySQL = " SELECT table_name, column_name, data_type, is_key_field " +
				" FROM " + getGeneralDBScheme() + ".vc_dictionary_column_all";
		String columnSQL = mySQL + " WHERE UPPER(table_name) = UPPER(?) ";
		String translateSQL = mySQL + " WHERE UPPER(table_name) = UPPER(?||'_TSL') ";
		Connection con = null;
		PreparedStatement st = null;
		
		int counter = 0;
		
		try{
			columnList.clear();
		    LOGGER.debug("columnList[]" + columnSQL + 
	    			", 1={'" + this.tableName + "',string}");
	    	con = Connector.getConnection(getSessionId());
	    	st = con.prepareStatement(columnSQL);
	    	st.setString(1, this.tableName);
	    	ResultSet rs = st.executeQuery();
	    	counter = 0;
	    	while(rs.next()){
	    		counter = counter + 1;
	    		bc.service.DBTableColumn oneColumn = 
	    			new bc.service.DBTableColumn(
	    					rs.getString("table_name"), 
	    					rs.getString("column_name"), 
	    					rs.getString("data_type"), 
	    					rs.getString("is_key_field"));
	    		columnList.put("" + counter, oneColumn);
	    	}
		}
		catch (SQLException e) {LOGGER.error("columnList[] SQLException: " + e.toString());}
		catch (Exception el) {LOGGER.error("columnList[] Exception: " + el.toString());}
		finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
		
   	   	LOGGER.debug("columnList[] END, columnList.size=" + columnList.size());
		
   	   	if ("Y".equalsIgnoreCase(hasTranslate)) {
			try{
				columnList.clear();
			    LOGGER.debug("translateColumnList[]" + translateSQL + 
		    			", 1={'" + this.tableName + "',string}");
		    	con = Connector.getConnection(getSessionId());
		    	st = con.prepareStatement(translateSQL);
		    	st.setString(1, this.tableName);
		    	ResultSet rs = st.executeQuery();
		    	counter = 0;
		    	while(rs.next()){
		    		counter = counter + 1;
		    		bc.service.DBTableColumn oneColumn = 
		    			new bc.service.DBTableColumn(
		    					rs.getString("table_name"), 
		    					rs.getString("column_name"), 
		    					rs.getString("data_type"), 
		    					rs.getString("is_key_field"));
		    		translateColumnList.put("" + counter, oneColumn);
		    	}
			}
			catch (SQLException e) {LOGGER.error("translateColumnList[] SQLException: " + e.toString());}
			catch (Exception el) {LOGGER.error("translateColumnList[] Exception: " + el.toString());}
			finally {
	            try {
	                if (st!=null) st.close();
	            } catch (SQLException w) {w.toString();}
	            try {
	                if (con!=null) con.close();
	            } catch (SQLException w) {w.toString();}
	            Connector.closeConnection(con);
	        } // finally
			
	  	   	LOGGER.debug("translateColumnList[] END, translateColumnList.size=" + translateColumnList.size());
   	   	}
	}
	
	public int getColumnCount(String pType) {
		if ("C".equalsIgnoreCase(pType)) {
			return columnList.size();
		} else if ("T".equalsIgnoreCase(pType)) {
			return translateColumnList.size();
		} else {
			return 0;
		}
	}
	
	public String getColumnName(String pType, int pId) {
		if ("C".equalsIgnoreCase(pType)) {
			if (!columnList.containsKey("" + pId)) {
				return columnList.get("" + pId).getColumnName();
			} else {
				return "";
			}
		} else if ("T".equalsIgnoreCase(pType)) {
			if (!translateColumnList.containsKey("" + pId)) {
				return translateColumnList.get("" + pId).getColumnName();
			} else {
				return "";
			}
		
		} else {
			return "";
		}
	}
}
