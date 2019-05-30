package bc.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.service.bcFeautureParam;

public class bcDictionaryObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcDictionaryObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String viewName;
	private String dictLanguage;
	
	private bcDictionaryColumnsObject columns = null;
	
	public bcDictionaryObject(String pViewName, String pDictLanguage) {
		this.viewName = pViewName;
		this.dictLanguage = pDictLanguage;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_DICTIONARY_ALL WHERE view_name = ?";
		fieldHm = getFeatures2(featureSelect, this.viewName, true);
	}
	
	public void readColumnsInfo(String pTableName, String pHasTranslate) {
		columns = new bcDictionaryColumnsObject(pTableName, pHasTranslate);
	}
	
	public int getColumnsCount(String pType) {
		return columns.getColumnCount(pType);
	}
	
	public String getColumnName(String pType, int pId) {
		return columns.getColumnName(pType, pId);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getDictionaryContentHTML(String pHasTranslate, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<String> array_value=new ArrayList<String>();
        ArrayList<String> array_code=new ArrayList<String>();
        Statement st_yes_no = null;
        
        boolean hasEditPermission = false;
        String mySQL = "";
        boolean isReadOnly = false;
        String myAlign = "";
        
        String readOnlyLabel = "";
        String existFlagLabel = "";
        String inputTelgrLabel = "";
        String outputTelgrLabel = "";
        String usedInLogisticLabel = "";
        try{
        	if (isEditPermited("SETUP_DICTIONARY_INFO") >0) {
        		hasEditPermission = true;
        	}
        	
        	con = Connector.getConnection(getSessionId());
            
        	st_yes_no = con.createStatement();
        	String lookupSQL = "SELECT lookup_code, meaning FROM " + getGeneralDBScheme() + ".vc_lookup_values WHERE UPPER(name_lookup_type)=UPPER('YES_NO')";
        	ResultSet rset_yes_no = st_yes_no.executeQuery(lookupSQL);
        	LOGGER.debug("(lookups)" + lookupSQL);
        	
        	while (rset_yes_no.next()) {
                array_code.add(rset_yes_no.getString("lookup_code"));
                array_value.add(rset_yes_no.getString("meaning"));
        	}
        	st_yes_no.close();
        	
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
       	catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
        finally {
            try {
                if (st_yes_no!=null) st_yes_no.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        

        ArrayList<bcFeautureParam> pParam = initParamArray();

        try {
        	
        	mySQL = "SELECT * FROM " + getGeneralDBScheme() + "." + this.viewName;
            if ("Y".equalsIgnoreCase(pHasTranslate)) {
            	mySQL = mySQL + " WHERE cd_language = ? ";
            	pParam.add(new bcFeautureParam("string", this.dictLanguage));
            }
            pParam.add(new bcFeautureParam("int", p_end));
            pParam.add(new bcFeautureParam("int", p_beg));
            mySQL = " SELECT * " +
            		"   FROM (SELECT ROWNUM rn, d.* " +
            		"           FROM ( " +
            		mySQL +
            		"         ) d WHERE ROWNUM < ?" + 
            		") WHERE rn >= ?";            	
            
            LOGGER.debug(prepareSQLToLog(mySQL, pParam));
            
            con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount; i++) {
                html.append(getBottomFrameTableTH(dictionaryXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	isReadOnly = false;
            	html.append("<tr>\n");
            	for (int i=1; i <= colCount; i++) {
            		if (mtd.getColumnName(i).startsWith("ID_") ||
            				mtd.getColumnName(i).equalsIgnoreCase("RN") ||
            				mtd.getColumnName(i).equalsIgnoreCase("CD_LANGUAGE") ||
            				mtd.getColumnName(i).equalsIgnoreCase("EXIST_FLAG") ||
            				mtd.getColumnName(i).equalsIgnoreCase("READ_ONLY") ||
            				mtd.getColumnName(i).equalsIgnoreCase("CHECK_PHASE") ||
            				mtd.getColumnName(i).equalsIgnoreCase("INPUT_TELGR_CHECK") ||
            				mtd.getColumnName(i).equalsIgnoreCase("OUTPUT_TELGR_CHECK") ||
            				mtd.getColumnName(i).equalsIgnoreCase("PRIORITY") ||
            				mtd.getColumnName(i).equalsIgnoreCase("USED_IN_LOGISTIC")) {
            			myAlign = " align = \"center\"";
            		} else {
            			myAlign = "";
            		}
            		
            		existFlagLabel = "";
            		if (mtd.getColumnName(i).equalsIgnoreCase("EXIST_FLAG")) {
	            		for(int counter=0;counter<array_code.size();counter++){
	                    	if(array_code.get(counter).equalsIgnoreCase(rset.getString("EXIST_FLAG"))){
	                    		existFlagLabel = array_value.get(counter);
	                    	}
	                    }
            		}
            		readOnlyLabel = "";
            		if (mtd.getColumnName(i).equalsIgnoreCase("READ_ONLY")) {
	            		for(int counter=0;counter<array_code.size();counter++){
	                    	if(array_code.get(counter).equalsIgnoreCase(rset.getString("READ_ONLY"))){
	                    		readOnlyLabel = array_value.get(counter);
	                    	}
	                    }
            		}
            		inputTelgrLabel = "";
            		if (mtd.getColumnName(i).equalsIgnoreCase("INPUT_TELGR_CHECK")) {
	            		for(int counter=0;counter<array_code.size();counter++){
	                    	if(array_code.get(counter).equalsIgnoreCase(rset.getString("INPUT_TELGR_CHECK"))){
	                    		inputTelgrLabel = array_value.get(counter);
	                    	}
	                    }
            		}
            		outputTelgrLabel = "";
            		if (mtd.getColumnName(i).equalsIgnoreCase("OUTPUT_TELGR_CHECK")) {
	            		for(int counter=0;counter<array_code.size();counter++){
	                    	if(array_code.get(counter).equalsIgnoreCase(rset.getString("OUTPUT_TELGR_CHECK"))){
	                    		outputTelgrLabel = array_value.get(counter);
	                    	}
	                    }
            		}
            		usedInLogisticLabel = "";
            		if (mtd.getColumnName(i).equalsIgnoreCase("USED_IN_LOGISTIC")) {
	            		for(int counter=0;counter<array_code.size();counter++){
	                    	if(array_code.get(counter).equalsIgnoreCase(rset.getString("USED_IN_LOGISTIC"))){
	                    		usedInLogisticLabel = array_value.get(counter);
	                    	}
	                    }
            		}
            		
            		if (mtd.getColumnName(i).equalsIgnoreCase("READ_ONLY")) {
            			if (rset.getString(i).equalsIgnoreCase("Y")) {
            				isReadOnly = true;
            				html.append("<td " + myAlign + "><b><font color=\"red\">"+getValue3(readOnlyLabel)+"</font><b></td>\n");
            			} else {
                			html.append("<td " + myAlign + ">"+getValue3(readOnlyLabel)+"</td>\n");
            			}
            		} else if (mtd.getColumnName(i).equalsIgnoreCase("EXIST_FLAG")) {
            			html.append("<td " + myAlign + ">"+getValue3(existFlagLabel)+"</td>\n");
            		} else if (mtd.getColumnName(i).equalsIgnoreCase("INPUT_TELGR_CHECK")) {
            			html.append("<td " + myAlign + ">"+getValue3(inputTelgrLabel)+"</td>\n");
            		} else if (mtd.getColumnName(i).equalsIgnoreCase("OUTPUT_TELGR_CHECK")) {
            			html.append("<td " + myAlign + ">"+getValue3(outputTelgrLabel)+"</td>\n");
            		} else if (mtd.getColumnName(i).equalsIgnoreCase("USED_IN_LOGISTIC")) {
            			html.append("<td " + myAlign + ">"+getValue3(usedInLogisticLabel)+"</td>\n");
            		} else {
            			html.append("<td " + myAlign + ">"+getValue3(rset.getString(i))+"</td>\n");
            		}
            	}
               
            	if (hasEditPermission) {
            		if (isReadOnly) {
            			html.append("<td align=center>&nbsp;</td><td align=center>&nbsp;</td>\n");
            		} else {
                      	String myHyperLink = "../crm/setup/dictionaryupdate.jsp?type="+this.viewName+"&id_record="+rset.getString(1);
                    	html.append(getEditButtonHTML(myHyperLink));
            		}
            	   
               	}
           		html.append("</tr>\n");
               
            }
            html.append("</tbody></table>\n");
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
       	catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        return html.toString();
    } // getDictionaryContentHTML
}
