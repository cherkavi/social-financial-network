package bc.objects;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import BonCard.DataBase.UtilityConnector;
//import bc.AppConst;
import bc.bcBase;
import bc.connection.Connector;
import bc.service.bcFeautureParam;

public class bcObject { 
	//protected static Connection con = null;

	public bcXML accountXML = bcDictionary.getInstance("accounts"); 
    public bcXML bank_statementXML = bcDictionary.getInstance("bank_statements");
    public bcXML bk_accountXML = bcDictionary.getInstance("bk_accounts");
    public bcXML bk_schemeXML = bcDictionary.getInstance("bk_scheme");
    public bcXML buttonXML = bcDictionary.getInstance("buttons");
    public bcXML cardsettingXML = bcDictionary.getInstance("cardsettings");
    public bcXML card_taskXML = bcDictionary.getInstance("card_tasks");
    public bcXML calculatorXML = bcDictionary.getInstance("calculator");
    public bcXML call_centerXML = bcDictionary.getInstance("call_center");
    public bcXML clearingXML = bcDictionary.getInstance("clearing");
    public bcXML clubXML = bcDictionary.getInstance("club");
    public bcXML club_actionXML = bcDictionary.getInstance("club_actions");
    public bcXML clubcardXML = bcDictionary.getInstance("clubcards");
    public bcXML clubfundXML = bcDictionary.getInstance("clubfund");
    public bcXML comissionXML = bcDictionary.getInstance("comission");
    public bcXML commonXML = bcDictionary.getInstance("Common");
    public bcXML contactXML = bcDictionary.getInstance("contacts");
    public bcXML currencyXML = bcDictionary.getInstance("currency");
    public bcXML daily_rateXML = bcDictionary.getInstance("daily_rates");
    public bcXML dictionaryXML = bcDictionary.getInstance("dictionary");
    public bcXML documentXML = bcDictionary.getInstance("documents");
    public bcXML emailXML = bcDictionary.getInstance("emails");
    public bcXML eventXML = bcDictionary.getInstance("Events");
    public bcXML exp_fileXML = bcDictionary.getInstance("exp_files");
    public bcXML form_messageXML = bcDictionary.getInstance("form_messages");
    public bcXML issuerXML = bcDictionary.getInstance("issuers");
    public bcXML jurpersonXML = bcDictionary.getInstance("JurPersons");
    public bcXML kvitovkaXML = bcDictionary.getInstance("kvitovka");
    public bcXML logisticXML = bcDictionary.getInstance("logistics");
    public bcXML loylineXML = bcDictionary.getInstance("loylines");
    public bcXML loyXML = bcDictionary.getInstance("loy");
    public bcXML messageXML = bcDictionary.getInstance("messages");
    public bcXML natprsXML = bcDictionary.getInstance("NatPersons");
    public bcXML oper_schemeXML = bcDictionary.getInstance("oper_scheme");
    public bcXML postingXML = bcDictionary.getInstance("postings");
    public bcXML posting_schemeXML = bcDictionary.getInstance("posting_scheme");
    public bcXML private_officeXML = bcDictionary.getInstance("private_office");
    public bcXML questionnaireXML = bcDictionary.getInstance("questionnaire");
    public bcXML reglamentXML = bcDictionary.getInstance("Reglaments");
    public bcXML relationshipXML = bcDictionary.getInstance("relationship");
    public bcXML remittanceXML = bcDictionary.getInstance("remittances");
    public bcXML reportXML = bcDictionary.getInstance("reports");
    public bcXML roleXML = bcDictionary.getInstance("Roles");
    public bcXML samXML = bcDictionary.getInstance("sam");
    public bcXML security_monitorXML = bcDictionary.getInstance("security_monitor");
    public bcXML settingXML = bcDictionary.getInstance("Settings");
    public bcXML shedulelineXML = bcDictionary.getInstance("shedulelines");
    public bcXML sheduleXML = bcDictionary.getInstance("shedule");
    public bcXML smsXML = bcDictionary.getInstance("sms");
    public bcXML stornoXML = bcDictionary.getInstance("storno");
    public bcXML syslogXML = bcDictionary.getInstance("sys_log");
    public bcXML sysmonitorXML = bcDictionary.getInstance("sys_monitor");
    public bcXML taxXML = bcDictionary.getInstance("taxes");
    public bcXML telegramXML = bcDictionary.getInstance("telegrams");
    public bcXML terminalXML = bcDictionary.getInstance("terminals");
    public bcXML terminalCertificateXML = bcDictionary.getInstance("terminalCertificate");
    public bcXML term_manufacturerXML = bcDictionary.getInstance("term_manufacturers");
    public bcXML term_sesXML = bcDictionary.getInstance("term_ses");
    public bcXML trainingXML = bcDictionary.getInstance("training");
    public bcXML transactionXML = bcDictionary.getInstance("transactions");
    public bcXML userXML = bcDictionary.getInstance("Users");
    public bcXML warningXML = bcDictionary.getInstance("warnings");

	public bcXML webposXML = bc.objects.bcDictionary.getInstance("webpos");
    
    private final static Logger LOGGER=Logger.getLogger(bcObject.class);
    
    public String selectedBackGroundStyle = " style=\"background-color: #F6F6F6\"";
    public String reportSelectedBackGroundStyle = " style=\"background-color: #FEFEFE\"";
    public String noneBackGroundStyle = " style=\"background-color: #FFFFFF\"";

	public String getSysDateTime() {
		String dateFormat = "RRRR.MM.DD H:m";

		SimpleDateFormat sdf_short = new SimpleDateFormat(dateFormat
				.replaceAll("R", "y").replaceAll("Y", "y").replaceAll("m", "M")
				.replaceAll("D", "d"));

		return sdf_short.format(new Date());
	}
    
    public ArrayList<bcFeautureParam> initParamArray() {
    	ArrayList<bcFeautureParam> pParam = new ArrayList<bcFeautureParam>();
    	return pParam;
    }
    
    public PreparedStatement prepareParam (PreparedStatement st, List<bcFeautureParam> pParam) throws SQLException {
    	StringBuilder html = new StringBuilder();
    	try {
		    int fieldCounter = 0;

		    for( bcFeautureParam eachParam:pParam){
		    	String key = eachParam.getDataType();
		    	String value = eachParam.getValue();
		    	
		    	if ("string".equalsIgnoreCase(key)) {
		    		fieldCounter ++;
			    	st.setString(fieldCounter, value);
			    	continue;
			    }
		    	
			    if ("int".equalsIgnoreCase(key)) {
			    	int myId = Integer.parseInt(value);
			    	fieldCounter ++;
			    	st.setInt(fieldCounter, myId);
			    	continue;
			    }
    			
    		}

    	} catch (SQLException e) {
    		LOGGER.error(html.toString(), e);
    		throw new SQLException(e);
    	}
    	return st;
    }
	
	public boolean isEmpty(String pValue) {
		return StringUtils.isEmpty(pValue);
	}
    
	public String prepareSQLToLog(String pSQL) {
		String pOutputMessage = pSQL;

        return pOutputMessage;
	}
    
	public String prepareSQLToLog(String pSQL, bcFeautureParam[] pParam) {
		String pOutputMessage = pSQL;
		for(int counter=1; counter<pParam.length+1;counter++){
        	if ("string".equalsIgnoreCase(pParam[counter-1].getDataType())) {
        		pOutputMessage = pOutputMessage + ", " + counter + "={'" + pParam[counter-1].getValue() + "', " + pParam[counter-1].getDataType() + "}";
        	} else {
        		pOutputMessage = pOutputMessage + ", " + counter + "={" + pParam[counter-1].getValue() + ", " + pParam[counter-1].getDataType() + "}";
        	}
        }
        return pOutputMessage;
	}
    
	private String preparedSQL = "";
	public String prepareSQLToLog(String pSQL, List<bcFeautureParam> pParam) {
		preparedSQL = pSQL;

		int counter=0;
		for(bcFeautureParam eachParam: pParam){
			counter++;
        	if ("string".equalsIgnoreCase(eachParam.getDataType())) {
        		preparedSQL = preparedSQL + ", " + counter + "={'" + eachParam.getValue() + "', " + eachParam.getDataType() + "}";
        	} else {
        		preparedSQL = preparedSQL + ", " + counter + "={" + eachParam.getValue() + ", " + eachParam.getDataType() + "}";
        	}
		}
		return preparedSQL;
	}
	
	public String prepareSQLToLog(String pSQL, String[] pParam) {
		String pOutputMessage = pSQL;

		for(int counter=1; counter<pParam.length+1;counter++){
        	pOutputMessage = pOutputMessage + ", " + counter + "={'" + pParam[counter-1] + "'}";
        }
        return pOutputMessage;
	}
	
	public String prepareSQLToLog(String pSQL, String[] pParam, String[] pResults) {
		String pOutputMessage = pSQL;

		pOutputMessage = pOutputMessage + ", 0={'" + pResults[0] + "'}";
        for(int counter=1; counter<pParam.length+1;counter++){
        	pOutputMessage = pOutputMessage + ", " + counter + "={'" + pParam[counter-1] + "'}";
        }
        for(int counter=2; counter<pResults.length+1;counter++){
        	pOutputMessage = pOutputMessage + ", " + (pParam.length + counter-1) + "={'" + pResults[counter-1] + "'}";
        }
        return pOutputMessage;
	}

    
	//private String sessionId;
    
	private String prepareLUD(String LUD) {
		return LUD;
	}
	
	public void setSessionId(String pSessionId) {
		String currentSessionId = bcBase.SESSION_PARAMETERS.SESSION_ID.getValue();
		if (isEmpty(currentSessionId)) {
			LOGGER.debug("SET sessionId=" + pSessionId);
			bcBase.SESSION_PARAMETERS.LANG.setValue(pSessionId);
		} else if (!currentSessionId.equalsIgnoreCase(pSessionId)) {
			LOGGER.debug("NEW sessionId=" + pSessionId);
			bcBase.SESSION_PARAMETERS.LANG.setValue(pSessionId);
		}
		//LOGGER.debug("setSessionId("+pSessionId+")=" + bcBase.SESSION_PARAMETERS.SESSION_ID.getValue());
	}
    
	public String getSessionId() {
		return bcBase.SESSION_PARAMETERS.SESSION_ID.getValue();
	}
	
	//private String date_format = "";
	
	public void readDateFormat() {
		String currentDateFormat = bcBase.SESSION_PARAMETERS.DATE_FORMAT.getValue();
		if (currentDateFormat == null || "".equalsIgnoreCase(currentDateFormat)) {
    		//getCurntUserParamFeature();
    		currentDateFormat = getOneValueByNoneId("SELECT dateformat FROM v_user_param_ln");
    		bcBase.SESSION_PARAMETERS.DATE_FORMAT.setValue(currentDateFormat);
        	//setGeneralDBScheme(returnValue);
			//date_format = getOneValueByNoneId("SELECT dateformat FROM v_user_param_ln");
		}
	}
	
	public void setDateFormat(String pDateFormat) {
		if (isEmpty(pDateFormat)) {
			String returnValue = getFeautureResult(currentUserParamHm, "DATEFORMAT");
			//LOGGER.debug("getGeneralDBScheme(1)=" + returnValue);
	    	if (isEmpty(returnValue)) {
	    		readDateFormat();
	    	}
		} else {
    		bcBase.SESSION_PARAMETERS.DATE_FORMAT.setValue(pDateFormat);
		}
		//LOGGER.debug("date_format=" + bcBase.SESSION_PARAMETERS.DATE_FORMAT.getValue());
	}
	
	public String getDateFormat() {
		String returnValue = bcBase.SESSION_PARAMETERS.DATE_FORMAT.getValue();
		/*if (isEmpty()) {
			readDateFormat();
        	returnValue = bcBase.SESSION_PARAMETERS.DATE_FORMAT.getValue();
    	}*/
    	return returnValue;
	}
	
	//String dbScheme = "";
	
	public void setGeneralDBScheme(String pDBScheme) {
		if (pDBScheme == null || "".equalsIgnoreCase(pDBScheme)) {
			getCurntUserParamFeature();
        	String returnValue = getFeautureResult(currentUserParamHm, "GENERAL_DB_SCHEME");
			bcBase.SESSION_PARAMETERS.GENERAL_DB_SCHEME.setValue(returnValue);
		} else {
			bcBase.SESSION_PARAMETERS.GENERAL_DB_SCHEME.setValue(pDBScheme);
		}
		//LOGGER.debug("dbScheme=" + bcBase.SESSION_PARAMETERS.GENERAL_DB_SCHEME.getValue());
	}
	
	public String getGeneralDBScheme() {
    	String returnValue = bcBase.SESSION_PARAMETERS.GENERAL_DB_SCHEME.getValue(); //getFeautureResult(currentUserParamHm, "GENERAL_DB_SCHEME");
		//LOGGER.debug("getGeneralDBScheme(1)=" + returnValue);
    	if (isEmpty(returnValue)) {
    		returnValue = getFeautureResult(currentUserParamHm, "GENERAL_DB_SCHEME");
    		if (isEmpty(returnValue)) {
	    		getCurntUserParamFeature();
	        	returnValue = getFeautureResult(currentUserParamHm, "GENERAL_DB_SCHEME");
	        	bcBase.SESSION_PARAMETERS.GENERAL_DB_SCHEME.setValue(returnValue);
	    		LOGGER.debug("getGeneralDBScheme()=" + returnValue);
    		}
    		//LOGGER.debug("getGeneralDBScheme(2)=" + returnValue);
    	}
    	return returnValue;
	}
    
	//private String language;
    
	public void setLanguage(String pLanguage) {
		if (isEmpty(pLanguage)) {
			String returnValue = getFeautureResult(currentUserParamHm, "UIL");
			LOGGER.debug("setLanguage(1)("+pLanguage+")=" + returnValue);
	    	if (isEmpty(returnValue)) {
	    		getCurntUserParamFeature();
	        	returnValue = getFeautureResult(currentUserParamHm, "UIL");
	    		LOGGER.debug("setLanguage(2)("+pLanguage+")=" + returnValue);
	    	}
			bcBase.SESSION_PARAMETERS.LANG.setValue(returnValue);
		} else {
			bcBase.SESSION_PARAMETERS.LANG.setValue(pLanguage);
		}
		//LOGGER.debug("setLanguage("+pLanguage+")=" + bcBase.SESSION_PARAMETERS.LANG.getValue());
	}
    
	public String getLanguage() {
    	String returnValue = bcBase.SESSION_PARAMETERS.LANG.getValue(); //getFeautureResult(currentUserParamHm, "GENERAL_DB_SCHEME");
		//LOGGER.debug("getGeneralDBScheme(1)=" + returnValue);
    	if (isEmpty(returnValue)) {
    		returnValue = getFeautureResult(currentUserParamHm, "UIL");
    		if (isEmpty(returnValue)) {
	    		getCurntUserParamFeature();
	        	returnValue = getFeautureResult(currentUserParamHm, "UIL");
	    		//LOGGER.debug("getLanguage()=" + returnValue);
    		}
        	bcBase.SESSION_PARAMETERS.LANG.setValue(returnValue);
    	}
		//LOGGER.debug("getLanguage()=" + returnValue);
		return returnValue;
	}

	public bcObject(){
		//LOGGER.debug("bcObject() constructor");
		setSessionId(bcBase.SESSION_PARAMETERS.SESSION_ID.getValue() );
		setLanguage(bcBase.SESSION_PARAMETERS.LANG.getValue());
		setDateFormat(bcBase.SESSION_PARAMETERS.DATE_FORMAT.getValue());
		setGeneralDBScheme(bcBase.SESSION_PARAMETERS.GENERAL_DB_SCHEME.getValue());
	}
	
    /*public String getContextPath(){
        return AppConst.contextPath;
    }*/
	
    private Map<String,Comparable<String>> currentUserParamHm = new HashMap<String, Comparable<String>>();
	
	public void getCurntUserParamFeature() {
		String featureCurntUserSelect = " SELECT * FROM V_USER_PARAM_LN";
		bcFeautureParam[] array = new bcFeautureParam[1];
		array[0] = new bcFeautureParam("none", "");
		currentUserParamHm = getFeatures2(featureCurntUserSelect, array);
		LOGGER.debug("getCurntUserParamFeature()");
	}
	
    public String getCalendarInputField (String pFieldName, String pValue, String pSize) {
    	StringBuilder html = new StringBuilder();
    	
    	html.append("<input type=\"text\" name=\"" + pFieldName + "\" size=\""+ pSize + "\" value=\"" + pValue + "\"  class=\"inputfield\" title=\"" + /*this.getDateFormatTitle()  +*/ "\" id=\"id_" + pFieldName + "\">\n");
    	html.append("<img src=\"../images/calendar.gif\" id=\"btn_" + pFieldName + "\" class=\"ui-datepicker-calendar-img\" title=\"" + this.buttonXML.getfieldTransl("button_data_selector", false)  + "\" />\n");
    	
		return html.toString();
    }
        
    public String getCalendarScript (String pFieldName, String pJSPDateFormat, boolean showsTime) {
    	StringBuilder html = new StringBuilder();
    	
    	html.append("<script type=\"text/javascript\">\n");
    	html.append("Calendar.setup({\n");
    	html.append("	inputField  : \"id_" + pFieldName + "\",         // ID поля вводу дати\n");
    	html.append("	button      : \"btn_" + pFieldName + "\",       // ID кнопки для меню вибору дати\n");
    	
    	if (!showsTime) {
    		html.append("	ifFormat    : \"" + pJSPDateFormat + "\"    // формат дати (23.03.2008)\n");
    	} else {
    		html.append("	ifFormat    : \"" + pJSPDateFormat + "  %H:%M\",    // формат дати (23.03.2008)\n");
    		html.append("	showsTime   : true\n");
    	}
    	html.append("});\n");	
    	html.append("</script>\n");
		
		return html.toString();
    }

    /*public String getGeneralDBScheme() {
    	String returnValue = getFeautureResult(currentUserParamHm, "GENERAL_DB_SCHEME");
		//LOGGER.debug("getGeneralDBScheme(1)=" + returnValue);
    	if (isEmpty(returnValue)) {
    		getCurntUserParamFeature();
        	returnValue = getFeautureResult(currentUserParamHm, "GENERAL_DB_SCHEME");
    		//LOGGER.debug("getGeneralDBScheme(2)=" + returnValue);
    	}
		LOGGER.debug("getGeneralDBScheme()=" + returnValue);
    	return returnValue;
    }*/
    /*
	public Map<String,Comparable<String>> getFeatures (String pFeaturesSelect, String pId, boolean isFeatureIdString){
	   Map<String,Comparable<String>> featuresHm = new HashMap<String, Comparable<String>>();
	   Statement st = null;
	   Connection con = null;
	   StringBuilder sql = new StringBuilder();
	   sql.append(pFeaturesSelect);
		   
	   if (isFeatureIdString) sql.append(" UPPER('"+pId+"') "); else sql.append(pId); 
	   
	   this.readDateFormat();
	   String dateFormat = this.getDateFormat();
	   dateFormat = dateFormat.replaceAll("R", "y").replaceAll("Y", "y").replaceAll("m", "M").replaceAll("D", "d");
	   String hourFormat = " HH";
	   String hourMinuteFormat = " HH:mm";
	   String hourMinuteSecondFormat = " HH:mm:ss";
	   String LUDDateFormat = "yyyy.MM.dd HH:mm:ss";
	   String pColumnName = "";
	   String pColumnType = "";
       try{
		    featuresHm.clear();
		    myDebug.writeSQL(sql.toString());
	    	con = Connector.getConnection(this.sessionId);
	    	st = con.createStatement();
	    	ResultSet rs = st.executeQuery(sql.toString()); 
	    	ResultSetMetaData mtd = rs.getMetaData();
	    	while(rs.next()){
	    		for (int i=1; i <= mtd.getColumnCount(); i++) {
	    			pColumnName = mtd.getColumnName(i).toUpperCase();
	    			pColumnType = mtd.getColumnTypeName(i).toUpperCase();
	    			String result = rs.getString(i);
						if ("DATE".equalsIgnoreCase(pColumnType)) {
							
							if (result == null || "".equalsIgnoreCase(result)) {
								result = "";
								featuresHm.put(pColumnName, result);
								featuresHm.put(pColumnName + "_DF", result);
								featuresHm.put(pColumnName + "_DHF", result);
								featuresHm.put(pColumnName + "_DHMF", result);
								featuresHm.put(pColumnName + "_DHMSF", result);
								if("LAST_UPDATE_DATE".equalsIgnoreCase(pColumnName)) {
									featuresHm.put("LUD", "");
								}
							} else {
								featuresHm.put(pColumnName, result);
								//LOGGER.debug(pColumnName + " = " + result);
								
								SimpleDateFormat sdf_short0=new SimpleDateFormat(dateFormat);
								Date date0 = rs.getTimestamp(i);
								result = sdf_short0.format(date0);
								featuresHm.put(pColumnName + "_DF", result);
								//LOGGER.debug(pColumnName + "_DF = " + result);
									
								SimpleDateFormat sdf_short1=new SimpleDateFormat(dateFormat + hourFormat);
								Date date1 = rs.getTimestamp(i);
								result = sdf_short1.format(date1);
								featuresHm.put(pColumnName + "_DHF", result);
								//LOGGER.debug(pColumnName + "_DHF = " + result);
									
								SimpleDateFormat sdf_short2=new SimpleDateFormat(dateFormat + hourMinuteFormat);
								Date date2 = rs.getTimestamp(i);
								result = sdf_short2.format(date2);
								featuresHm.put(pColumnName + "_DHMF", result);
								//LOGGER.debug(pColumnName + "_DHMF = " + result);
									
								SimpleDateFormat sdf_short3=new SimpleDateFormat(dateFormat + hourMinuteSecondFormat);
								Date date3 = rs.getTimestamp(i);
								result = sdf_short3.format(date3);
								featuresHm.put(pColumnName + "_DHMSF", result);
								//LOGGER.debug(pColumnName + "_DHMSF = " + result);
								
								if("LAST_UPDATE_DATE".equalsIgnoreCase(pColumnName)) {
									SimpleDateFormat sdf_lud=new SimpleDateFormat(LUDDateFormat);
									Date date4 = rs.getTimestamp(i);
									result = this.prepareLUD(sdf_lud.format(date4).replace(" ", "").replace(".", "").replace(":", ""));
									featuresHm.put("LUD", result);
									//LOGGER.debug("LUD = " + result);
								}
							}
						} else {
							featuresHm.put(pColumnName, result);
						}
	    			
		        } // for columnCount
	    	}
	   }
	   catch (SQLException e) {LOGGER.error("SQLException: " + e.toString());}
	   catch (Exception el) {LOGGER.error("Exception: " + el.toString() + ", pColumnName=" + pColumnName + ", pColumnType=" + pColumnType);}
	   finally {
           try {
               if (st!=null) st.close();
           } catch (SQLException w) {w.toString();}
           try {
               if (con!=null) con.close();
           } catch (SQLException w) {w.toString();}
           Connector.closeConnection(con);
       } // finally
   	   LOGGER.debug("END");
	   return featuresHm;
    } //getFeatures()
	*/
    
    /*public PreparedStatement myPrepareSQL(PreparedStatement pInputSQL, bcFeautureParam[] pParam) {
    	PreparedStatement localSQL = pInputSQL;

	    int fieldCounter = 0;
	    for(int i = 0; i < pParam.length; i++) {
	    	String key = pParam[i].getDataType();
	    	String value = pParam[i].getValue();
	    	//LOGGER.debug("key="+key);
	    	//LOGGER.debug("value="+value);
	    	if ("string".equalsIgnoreCase(key)) {
	    		fieldCounter ++;
		    	localSQL.setString(fieldCounter, value);
		    } else if ("int".equalsIgnoreCase(key)) {
		    	int myId = Integer.parseInt(value);
		    	fieldCounter ++;
		    	localSQL.setInt(fieldCounter, myId);
		    }
	    }
    	return localSQL;
    }*/
    
    int resultSetRowCount = 0;
    
    public int getResultSetRowCount () {
    	//System.out.println("resultSetRowCount="+resultSetRowCount);
    	return resultSetRowCount;
    }
	
	public Map<String,Comparable<String>> getFeatures2 (String pFeaturesSelect, bcFeautureParam[] pId){
		Map<String,Comparable<String>> featuresHm = new HashMap<String, Comparable<String>>();
		
		PreparedStatement st = null;
		Connection con = null;
		
		this.readDateFormat();
		String dateFormat = this.getDateFormat();
		dateFormat = dateFormat.replaceAll("R", "y").replaceAll("Y", "y").replaceAll("m", "M").replaceAll("D", "d");
		String hourFormat = "HH";
		String hourMinuteFormat = "HH:mm";
		String hourMinuteSecondFormat = "HH:mm:ss";
		String LUDDateFormat = "yyyy.MM.dd HH:mm:ss";
		String pColumnName = "";
		String pColumnType = "";
		
		String debugString = "";
    	resultSetRowCount = 0;
	    try{
		
	    	featuresHm.clear();
	    	debugString = pFeaturesSelect;
			
		    con = Connector.getConnection(getSessionId());
		    st = con.prepareStatement(pFeaturesSelect);
		    int fieldCounter = 0;
		    for(int i = 0; i < pId.length; i++) {
		    	String key = pId[i].getDataType();
		    	String value = pId[i].getValue();		 
		    	//LOGGER.debug("value="+value);
		    	if ("string".equalsIgnoreCase(key)) {
		    		fieldCounter ++;
			    	st.setString(fieldCounter, value);
			    	debugString = debugString + ", " + fieldCounter + "={'" + value + "'," + key +"}";
			    } else if ("int".equalsIgnoreCase(key)) {
			    	int myId = Integer.parseInt(value);
			    	fieldCounter ++;
			    	st.setInt(fieldCounter, myId);
			    	debugString = debugString + ", " + fieldCounter + "={" + value + "," + key +"}";
			    }
		    }
		    
		    //LOGGER.debug(debugString);
			
		    	ResultSet rs = st.executeQuery(); 
		    	ResultSetMetaData mtd = rs.getMetaData();
		    	while(rs.next()){
		    		for (int i=1; i <= mtd.getColumnCount(); i++) {
		    			pColumnName = mtd.getColumnName(i).toUpperCase();
		    			pColumnType = mtd.getColumnTypeName(i).toUpperCase();   	
		    			String result = rs.getString(i);
						/*if ("CREATION_DATE".equalsIgnoreCase(pColumnName)||
								"LAST_UPDATE_DATE".equalsIgnoreCase(pColumnName)) {
						   try {
							   SimpleDateFormat sdf_short=new SimpleDateFormat(dateFormat + hourMinuteFormat);
							   Date date = rs.getTimestamp(i);
							   result = sdf_short.format(date);
							   featuresHm.put(pColumnName, result);
						   } 
						   catch (Exception el) {LOGGER.error("bcObject.getFeautureResult Exception: " + el.toString());}

						} else {
						*/
							if ("DATE".equalsIgnoreCase(pColumnType)) {
								
								if (result == null || "".equalsIgnoreCase(result)) {
									result = "";
									featuresHm.put(pColumnName, result);
									featuresHm.put(pColumnName + "_DF", result);
									featuresHm.put(pColumnName + "_DHF", result);
									featuresHm.put(pColumnName + "_DHMF", result);
									featuresHm.put(pColumnName + "_DHMSF", result);
									featuresHm.put(pColumnName + "_HF", result);
									featuresHm.put(pColumnName + "_HMF", result);
									featuresHm.put(pColumnName + "_HMSF", result);
									if("LAST_UPDATE_DATE".equalsIgnoreCase(pColumnName)) {
										featuresHm.put("LUD", "");
									}
								} else {
									featuresHm.put(pColumnName, result);
									//LOGGER.debug(pColumnName + " = " + result);
									
									SimpleDateFormat sdf_short0=new SimpleDateFormat(dateFormat);
									Date date0 = rs.getTimestamp(i);
									result = sdf_short0.format(date0);
									featuresHm.put(pColumnName + "_DF", result);
									//LOGGER.debug(pColumnName + "_DF = " + result);
										
									SimpleDateFormat sdf_short1=new SimpleDateFormat(dateFormat + " " + hourFormat);
									Date date1 = rs.getTimestamp(i);
									result = sdf_short1.format(date1);
									featuresHm.put(pColumnName + "_DHF", result);
									//LOGGER.debug(pColumnName + "_DHF = " + result);
										
									SimpleDateFormat sdf_short2=new SimpleDateFormat(dateFormat + " " + hourMinuteFormat);
									Date date2 = rs.getTimestamp(i);
									result = sdf_short2.format(date2);
									featuresHm.put(pColumnName + "_DHMF", result);
									//LOGGER.debug(pColumnName + "_DHMF = " + result);
										
									SimpleDateFormat sdf_short3=new SimpleDateFormat(dateFormat + " " + hourMinuteSecondFormat);
									Date date3 = rs.getTimestamp(i);
									result = sdf_short3.format(date3);
									featuresHm.put(pColumnName + "_DHMSF", result);
									//LOGGER.debug(pColumnName + "_DHMSF = " + result);
										
									SimpleDateFormat sdf_short4=new SimpleDateFormat(hourFormat);
									Date date4 = rs.getTimestamp(i);
									result = sdf_short4.format(date4);
									featuresHm.put(pColumnName + "_HF", result);
									//LOGGER.debug(pColumnName + "_DHF = " + result);
										
									SimpleDateFormat sdf_short5=new SimpleDateFormat(hourMinuteFormat);
									Date date5 = rs.getTimestamp(i);
									result = sdf_short5.format(date5);
									featuresHm.put(pColumnName + "_HMF", result);
									//LOGGER.debug(pColumnName + "_DHMF = " + result);
										
									SimpleDateFormat sdf_short6=new SimpleDateFormat(hourMinuteSecondFormat);
									Date date6 = rs.getTimestamp(i);
									result = sdf_short6.format(date6);
									featuresHm.put(pColumnName + "_HMSF", result);
									//LOGGER.debug(pColumnName + "_DHMSF = " + result);
									
									if("LAST_UPDATE_DATE".equalsIgnoreCase(pColumnName)) {
										SimpleDateFormat sdf_lud=new SimpleDateFormat(LUDDateFormat);
										Date date7 = rs.getTimestamp(i);
										result = this.prepareLUD(sdf_lud.format(date7).replace(" ", "").replace(".", "").replace(":", ""));
										featuresHm.put("LUD", result);
										//LOGGER.debug("LUD = " + result);
									}
								}
							} else {
								featuresHm.put(pColumnName, result);
							}
						/*}*/
		    			
			        } // for columnCount
		    		resultSetRowCount ++;
		    	}
			    
		    	if (resultSetRowCount!=1) {
		    		LOGGER.error(prepareSQLToLog("rowCount=" + resultSetRowCount + "," + pFeaturesSelect, pId));
		    	} else {
		    		LOGGER.debug(prepareSQLToLog("rowCount=" + resultSetRowCount + "," + pFeaturesSelect, pId));
		    	}
		   }
		   catch (SQLException e) {
			   LOGGER.debug(prepareSQLToLog(pFeaturesSelect, pId));
			   LOGGER.error("SQLException: " + e.toString());
		   }
		   catch (Exception el) {
			   LOGGER.debug(prepareSQLToLog(pFeaturesSelect, pId));
			   LOGGER.error("Exception: " + el.toString() + ", pColumnName=" + pColumnName + ", pColumnType=" + pColumnType + ",debugString=" + debugString);
		   }
		   finally {
			   UtilityConnector.closeQuietly(st);
			   UtilityConnector.closeQuietly(con);
	       } // finally
	       // LOGGER.debug("END");

	       //LOGGER.debug("featuresHm.count="+featuresHm.size());
		   return featuresHm;
	}
	
	public Map<String,Comparable<String>> getFeatures2 (String pFeaturesSelect, String pId, boolean isFeatureIdString){
		bcFeautureParam[] array = new bcFeautureParam[1];
		if (isEmpty(pId)) {
			array[0] = new bcFeautureParam("none", "");
		} else {
			if (!isFeatureIdString) {
				array[0] = new bcFeautureParam("int", pId);
			} else {
				array[0] = new bcFeautureParam("string", pId);
			}
		}
		return getFeatures2(pFeaturesSelect, array);
	}
	
	/*public Map<String,Comparable<String>> getFeatures (String pFeaturesSelect, String pId, boolean isFeatureIdString){
		bcFeautureParam[] array = new bcFeautureParam[2];
		if (isFeatureIdString) {
			array[0] = new bcFeautureParam("1", "int", pId);
		} else {
			array[0] = new bcFeautureParam("1", "string", pId);
		}
		return getFeatures2(pFeaturesSelect, array);
	}
	*/
	
	public String prepareToHTML (String pValue) {
		String result = "";
		result = pValue.replace("&", "&amp;")
					   .replace("\"", "&quot;")
					   .replace("<<", "&laquo;")
					   .replace(">>", "&raquo;")
					   .replace("<", "&lt;")
					   .replace(">", "&gt;");
		return result;
	}
	
	public String prepareFromHTML (String pValue) {
		String result = "";
		result = pValue.replace("&amp;", "&")
					   .replace("&quot;", "\"")
					   .replace("&laquo;", "<<")
					   .replace("&raquo;", ">>")
					   .replace("&lt;", "<")
					   .replace("&gt;", ">")
					   .replace("&nbsp;", " ");
		return result;
	}
	
	public String getFeautureResult (Map<String,Comparable<String>> pFeautureHm, String pColumnName) {
		String result = "";
		if (pFeautureHm.isEmpty()) {
			result = "";
			LOGGER.debug(pColumnName + "='" + result + "': RESULTSET IS EMPTY");
		} else {
			if (pFeautureHm.containsKey(pColumnName.toUpperCase())) {
				result = (String) pFeautureHm.get(pColumnName.toUpperCase());
				if (isEmpty(result)) {				
					result = "";
				}
				result = prepareToHTML(result);
			} else {
				result = "";
				LOGGER.error(pColumnName.toUpperCase() + " NOT FOUND");
			}
		}
		return result;
	}

	public String getValue2(String pValue){
    	String result;
    	if (pValue == null || "null".equalsIgnoreCase(pValue)) {
    		result = "&nbsp;";
    	} else {
    		result = pValue;    		
    	}
    	return result;
    } // end of getValue2   

	public String getValue3(String pValue){
    	String result;
    	if (pValue == null || "null".equalsIgnoreCase(pValue) || "".equalsIgnoreCase(pValue)) {
    		result = "&nbsp;";
    	} else {
    		result = pValue;    		
    	}
    	return result;
    } // end of getValue3

    public int isEditPermited(String pTabName){

        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	"SELECT id_privilege_type " +
        	"  FROM " + getGeneralDBScheme() + ".VC_USER_MENU_TABSHEET_ALL " +
        	" WHERE UPPER(tabname_menu_element)=UPPER(?)" +
        	"       AND is_enable = 'Y' " +
        	"       AND is_visible = 'Y' ";
        pParam.add(new bcFeautureParam("string", pTabName));
        int result = -1;
        try{
        	
        	//LOGGER.debug(prepareSQLToLog(mySQL, pParam));
            con = Connector.getConnection(getSessionId());;
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            
           while (rset.next())
            {
        	   if (rset.getString(1).equalsIgnoreCase("2")) {
        		   result = 1;
        	   } else {
        		   result = 0;
        	   }
            }
        } // try
        catch (SQLException e) {LOGGER.error("SQLException: " + e.toString()); html.append("bcObject.isEditPermited() SQLException: " + e.toString());}
        catch (Exception el) {LOGGER.error("Exception: " + el.toString()); html.append("bcObject.isEditPermited() Exception: " + el.toString());}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        //LOGGER.debug("bcObject.isEditMenuPermited('"+pTabName+"'): result=" + result);
        return result;
	}
    
    /**
     * @param id_tabname
     * @return
     * <ul>
     * 	<li>-1 - not found </li>
     * 	<li>0 - no access</li>
     * 	<li>1 - ACCESSED </li>
     * </ul>
     */
    public int isEditMenuPermited(String id_tabname){
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rset=null;
        List<bcFeautureParam> pParam = initParamArray();
        
        String mySQL = 
        	"SELECT id_privilege_type " +
        	" FROM " + getGeneralDBScheme() + ".VC_USER_MENU_ALL " +
        	" WHERE UPPER(tabname_menu_element) = UPPER(?)" +
        	"       AND is_enable = 'Y' " +
        	"       AND is_visible = 'Y' ";
        pParam.add(new bcFeautureParam("string", id_tabname));
        int result = -1;
        try{
        	//LOGGER.debug(prepareSQLToLog(mySQL, pParam));
            con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            prepareParam(st, pParam);
            rset = st.executeQuery();
            
           if (rset.next()) {
        	   if (rset.getString(1).equalsIgnoreCase("2")) {
        		   result = 1;
        	   } else {
        		   result = 0;
        	   }
            }
        } catch (SQLException e) {
        	LOGGER.error("SQLException: " + e.toString()); html.append("bcObject.isEditPermited('"+id_tabname+"') SQLException: " + e.toString());
        } catch (Exception el) {
        	LOGGER.error("Exception: " + el.toString()); html.append("bcObject.isEditPermited('"+id_tabname+"') Exception: " + el.toString());
        }finally {
        	UtilityConnector.closeQuietly(rset);
            UtilityConnector.closeQuietly(st);
            Connector.closeConnection(con);
        }
        //LOGGER.debug("bcObject.isEditMenuPermited('"+id_tabname+"'): result=" + result);
        return result;
	}
    
    private int maxSelectOption = 10;
    
    // TODO !!! INVESTIGATE IT !!!
    private int selectOprionCount = 0;

    public String getSelectBodyFromQuery(String query, String selected_value, boolean isNull){
		return getSelectBodyFromQuery(query, selected_value, null, isNull);
	} // getSelectBodyFromQuery()    

    public String getSelectBodyFromQuery(String query, String selected_value, String pSize, boolean isNull){
		StringBuilder return_value=new StringBuilder();
        Statement st = null;
        Connection con = null;
        String selectedTxt = "";
        LOGGER.debug(query);
        this.selectOprionCount = 0;
        try{
            con = Connector.getConnection(getSessionId());
            st = con.createStatement();
       
            ResultSet rset = st.executeQuery(query);

            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            //return_value.append("\n");
            if (isNull) {
            	return_value.append("<option value=\"\"></option>"); 
            }
            
			while(rset.next()){
				selectedTxt = "";
				if(selected_value!=null){
					for (int i=1; i <= colCount; i++) {
						if(selected_value.equalsIgnoreCase(rset.getString(i))) {
							selectedTxt = "SELECTED";
						}
					}
				}
				String pOptionValue = rset.getString(1);
				String pOptionText = "";
				if (isEmpty(pSize)) {
					pOptionText = rset.getString(2);
				} else {
					if (!isEmpty(rset.getString(2))) {
						int lTextSize = Integer.parseInt(pSize);
						if (rset.getString(2).length() > lTextSize) {
							pOptionText = rset.getString(2).substring(0, lTextSize) + "...";
						} else {
							pOptionText = rset.getString(2);
						}
					} else {
						pOptionText = "";
					}
				}
				return_value.append("<option value=\""+pOptionValue+"\" " + selectedTxt + ">"+pOptionText+"</option>");
				this.selectOprionCount = this.selectOprionCount + 1;
			}
		}
		catch (SQLException e) {
			LOGGER.error("Dbean.getSelectBodyFromQuery SQLException: " + e.toString());
			return_value.append("<option value=\"\">SQL Error</option>");
		}
        catch (Exception el) {
        	LOGGER.error("Dbean.getSelectBodyFromQuery Exception: " + el.toString());
        }
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        }
		return return_value.toString();
	} // getSelectBodyFromQuery()  

    public String getSelectBodyFromParamQuery(String query, ArrayList<bcFeautureParam> pParam, String selected_value, boolean isNull){
		StringBuilder return_value=new StringBuilder();
		PreparedStatement st = null;
        Connection con = null;
        String selectedTxt = "";
		try{
			con = Connector.getConnection(getSessionId());
       
            LOGGER.debug(prepareSQLToLog(query, pParam));
            st = con.prepareStatement(query);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();

            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            if (isNull) {
            	return_value.append("<option value=\"\"></option>"); 
            	//found = true;
            }
			while(rset.next()){
				selectedTxt = "";
				if(selected_value!=null){
					for (int i=1; i <= colCount; i++) {
						if(selected_value.equalsIgnoreCase(rset.getString(i))) {
							selectedTxt = "SELECTED";
						}
					}
				}
				return_value.append("<option value=\""+rset.getString(1)+"\" " + selectedTxt + ">"+rset.getString(2)+"</option>");
			}
		}
		catch (SQLException e) {
			LOGGER.error("SQLException: " + e.toString()); 
			return_value.append("<option value=\"\">bcObject.getSelectBodyFromQuery()() SQL Error</option>");
		}
        catch (Exception el) {LOGGER.error("Exception: " + el.toString());}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
		return return_value.toString();
	} // getSelectBodyFromQuery()       
    
    public String getOneValueByParamId(String pSQL, ArrayList<bcFeautureParam> pParam) {
	    String result = "";
	    PreparedStatement st = null;
	    Connection con = null;
	    
	    try{
	        con = Connector.getConnection(getSessionId());
	        st = con.prepareStatement(pSQL);
	        st = prepareParam(st, pParam);
	        ResultSet rset = st.executeQuery();
	
	        while (rset.next())
	        {
	            result= rset.getString(1);
	        }
	        result = getHTMLValue(result);
	    	LOGGER.debug("result="+result+","+prepareSQLToLog(pSQL, pParam));
	    } // try
	    catch (SQLException e) {
	    	e.toString(); 
	    	LOGGER.debug(prepareSQLToLog(pSQL, pParam));
	    	LOGGER.error("SQLException: "+e.toString());}
	    catch (Exception el) {
	    	el.toString(); 
	    	LOGGER.debug(prepareSQLToLog(pSQL, pParam));
	    	LOGGER.error("Exception: "+el.toString());}
	    finally {
	        try {
	            if (st!=null) st.close();
	        } catch (SQLException w) {w.toString();}
	        try {
	            if (con!=null) con.close();
	        } catch (SQLException w) {w.toString();}
	        Connector.closeConnection(con);
	    } // finally
	    return result;
    }

    public String getOneValueByNoneId(String pSQL) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	pParam.add(new bcFeautureParam("none", ""));
    	return getOneValueByParamId(pSQL, pParam);
    }

    public String getOneValueByStringId(String pSQL, String pId) {
    	if (isEmpty(pId)) {
     		return "";
     	}
    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	pParam.add(new bcFeautureParam("string", pId));
    	return getOneValueByParamId(pSQL, pParam);
    }

    public String getOneValueByIntId(String pSQL, String pId) {
    	if (isEmpty(pId)) {
     		return "";
     	}
    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	pParam.add(new bcFeautureParam("int", pId));
    	return getOneValueByParamId(pSQL, pParam);
    }
    
    /*
    public String getOneValueById2(String current_value, String mySQL){
     	if (isEmpty(current_value)) {
     		return "";
     	} else {
     		return getOneValueById(mySQL);
     	}
    }*/


    public String getHTMLValue(String pValue){ // TEMPORARY FOR DEMO!!
    	String result;
    	if (pValue == null || "null".equalsIgnoreCase(pValue)) {
    		result = "";
    	} else {
    		result = pValue;    		
    	}
    	result = result.replace("&", "&amp;").replace("\"", "&quot;").replace("<<", "&laquo;").replace(">>", "&raquo;");
    	result = result.replace("<", "&lt;").replace(">", "&gt;");
    	return result;
    } // end of getHTMLValue

	public String getNumbersOption(int pTo){
		return getNumbersOption(pTo, -9999);
	}
	public String getNumbersOption(int pTo, String  pSelectedNumber){
		int selectedNumber = -9999;
		try {
			if (isEmpty(pSelectedNumber)) {
				selectedNumber = 0;
			} else {
				selectedNumber = Integer.parseInt(pSelectedNumber);
			}
		} catch (NumberFormatException ne) {
			LOGGER.error("NumberFormatException: " + ne.toString());
		} catch (Exception e) {
			LOGGER.error("Exception: " + e.toString());
		}
		return getNumbersOption(pTo, selectedNumber);
	}
	public String getNumbersOption(int pTo, int pSelectedNumber){
		StringBuilder result = new StringBuilder ();
		for (int i=0; i <= pTo; i++){
			result.append("<option value=\"").append(i).append("\" ");
			if ( i == pSelectedNumber) {
				result.append("selected=\"selected\"");
			}
			result.append(" >");
			result.append(i);
			result.append("</option>");
		}
		return result.toString();
	}
	

	
	public String formatTDData(String pColSpan, String columnName, int columnType, String bgColor, String pAlign) {
		
		String return_value = "<td ";

		if (!(pColSpan == null || "".equalsIgnoreCase(pColSpan))) {
			return_value = return_value + " colspan=\"" + pColSpan + "\" ";
		}
		
		if (columnType == Types.DATE ||
				columnType == Types.TIMESTAMP ||
				columnType == Types.NUMERIC ||
				columnType == Types.INTEGER ||
				columnType == Types.DECIMAL) {
			return_value = return_value + " align=\"center\" ";
		/*
		} else if (columnType == Types.VARCHAR ||
				columnType == Types.LONGNVARCHAR ||
				columnType == Types.NCHAR ||
				columnType == Types.NVARCHAR) {
			if (columnName.contains("FRMT") || 
					columnName.contains("PERCENT") || 
					columnName.contains("TSL") || 
					columnName.contains("DATE") || 
					columnName.contains("COUNT") ||
					columnName.contains("QUANTITY") ||
					columnName.contains("NUMBER") || 
					columnName.contains("HEX")) {
					return_value = "<td align=\"center\" "+bgColor+">";
			} else {
				return_value = "<td "+bgColor+">";
			}
		*/
		} else {
			if (pAlign == null || "".equalsIgnoreCase(pAlign)) {
				if ("RN".equalsIgnoreCase(columnName) ||
					columnName.startsWith("ID_") ||
					columnName.endsWith("_FRMT") || 
					columnName.contains("PERCENT") || 
					columnName.endsWith("_TSL") || 
					(columnName.contains("COUNT") && !(columnName.contains("ACCOUNT"))) ||
					columnName.contains("DATE") || 
					columnName.contains("PHONE") || 
					columnName.endsWith("_COUNT") ||
					columnName.contains("QUANTITY") ||
					columnName.contains("_NUMBER") || 
					columnName.contains("NUMBER_") || 
					columnName.contains("HEX") || 
					columnName.contains("VERSION") || 
					columnName.contains("BONUS_TRANSFER") || 
					columnName.contains("BONUS_CALC") || 
					"ACTION".equalsIgnoreCase(columnName) || 
					"CLUB_ST".equalsIgnoreCase(columnName) || 
					columnName.contains("NT_ICC") || 
					"NT_EXT".equalsIgnoreCase(columnName) || 
					"OUTHER_SID".equalsIgnoreCase(columnName) || 
					"NT_MSG_B".equalsIgnoreCase(columnName) || 
					"TEL_VERSION".equalsIgnoreCase(columnName) || 
					"NT_SAM".equalsIgnoreCase(columnName) || 
					"NC_TERM".equalsIgnoreCase(columnName) || 
					"NC".equalsIgnoreCase(columnName) || 
					"NI".equalsIgnoreCase(columnName)) {
					return_value = return_value + " align=\"center\" ";
				} else if (columnName.endsWith("_AMNT")) {
					return_value = return_value + " align=\"right\" ";
				}
			} else {
				return_value = return_value + " align=\"" + pAlign + "\" ";
			}
		}
		if (columnName.contains("CD_CARD1")) {
			return_value = return_value + " class=\"cd_card1_nowrap\"";
		}
		return_value = return_value + " " + bgColor + ">";
		return return_value;
	}
	
	public String formatTDData(String columnName, int columnType, String bgColor) {
		return formatTDData("", columnName, columnType, bgColor, "");
	}
	
	public String formatTDData(String columnName, int columnType, String bgColor, String pAlign) {
		return formatTDData("", columnName, columnType, bgColor, pAlign);
	}

    public String getMeaningFromLookupNameOptions(String lookup_name, String lookup_code, boolean isNull) {
    	return getSelectBodyFromQuery(
    			" SELECT lookup_code, meaning " +
    			"   FROM " + getGeneralDBScheme() + ".vc_lookup_values " +
    			"  WHERE UPPER(name_lookup_type)=UPPER('"+lookup_name+"')", lookup_code, isNull);
     } // getMeaningFromLookupNameOptions()

    public String getReportId(String cd_report){
    	return getOneValueByStringId("SELECT id_report FROM " + getGeneralDBScheme()+".v_reports_all WHERE cd_report = ? ", cd_report);
    } // getCardStateName

    public String getClubFundPaymentKindName(String cd_kind){
    	return getOneValueByStringId("SELECT name_club_fund_payment_kind FROM " + getGeneralDBScheme()+".vc_club_fund_payment_kind_all WHERE cd_club_fund_payment_kind = ? ", cd_kind);
    } // getCardStateName

    public String getMeaningFoCodeValue(String lookupType, String lookupCode){

    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	pParam.add(new bcFeautureParam("string", lookupType));
    	pParam.add(new bcFeautureParam("string", lookupCode));
    	
    	return getOneValueByParamId("SELECT meaning FROM " + getGeneralDBScheme()+".vc_lookup_values WHERE UPPER(name_lookup_type) = ? AND lookup_code= ? ", pParam);
    } // getMeaningForNumValue
    
	public String getDeleteButtonStyleHTML(String pHyperLink, String pTDStyle){
		StringBuilder html = new StringBuilder ();
		html.append("<td " + pTDStyle + "><div class=\"div_button\" onclick=\"var msg='" + buttonXML.getfieldTransl("delete", false) + "?';var res=window.confirm(msg);if (res) {ajaxpage('"+ pHyperLink + "','div_main');}\">" + "<img vspace=\"0\" hspace=\"0\"" +
				" src=\"../images/oper/rows/delete.png\" align=\"top\" width=\"16\" height=\"16\" style=\"border: 0px;\"" + 
				" title=\"" + buttonXML.getfieldTransl("button_delete", false)+"\">" + getHyperLinkEnd()+"</td>\n");
		return html.toString();
	}
    
	public String getDeleteButtonHTML(String pHyperLink){
		return getDeleteButtonStyleHTML(pHyperLink, "");
	}
    
	public String getEditButtonStyleHTML(String pHyperLink, String pTDStyle){
		return getEditButtonStyleHTML(pHyperLink, pTDStyle, "div_main");
	}
    
	public String getEditButtonStyleHTML(String pHyperLink, String pTDStyle, String pDivName){
		StringBuilder html = new StringBuilder ();
		html.append("<td " + pTDStyle + " style=\"width:15px;text-align:center;\">\n");
		html.append(getHyperLinkFirst() + pHyperLink + "&action=edit&process=no', '" + pDivName + "')\">\n");
		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/edit.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ buttonXML.getfieldTransl("button_edit", false) +"\">\n");
		html.append(getHyperLinkEnd() + "</td>\n");
		return html.toString();
	}
    
	public String getCopyButtonStyleHTML(String pHyperLink, String pTDStyle, String pDivName){
		StringBuilder html = new StringBuilder ();
		html.append("<td " + pTDStyle + " style=\"width:15px;text-align:center;\">\n");
		html.append(getHyperLinkFirst() + pHyperLink + "&action=copy&process=no', '" + pDivName + "')\">\n");
		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/copy.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ buttonXML.getfieldTransl("button_copy", false) +"\">\n");
		html.append(getHyperLinkEnd() + "</td>\n");
		return html.toString();
	}
    
	public String getSelectButtonStyleHTML(String pHyperLink, String pTDStyle, String pDivName){
		StringBuilder html = new StringBuilder ();
		html.append("<td " + pTDStyle + " style=\"width:15px;text-align:center;\">\n");
		html.append(getHyperLinkFirst() + pHyperLink + "', '" + pDivName + "')\">\n");
		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/select.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ buttonXML.getfieldTransl("button_select", false) +"\">\n");
		html.append(getHyperLinkEnd() + "</td>\n");
		return html.toString();
	}
    
	public String getEditButtonHTML(String pHyperLink){
		return getEditButtonStyleHTML(pHyperLink, "", "div_main");
	}
    
	public String getEditButtonWitDivHTML(String pHyperLink, String pDivName){
		return getEditButtonStyleHTML(pHyperLink, "", pDivName);
	}
    
	public String getCheckButtonHTML(String pHyperLink){
		StringBuilder html = new StringBuilder ();
		html.append("<td align=\"center\">\n");
		html.append(getHyperLinkFirst() + pHyperLink + "&action=check&process=yes" + getHyperLinkMiddle() + "\n");
		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/check.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ buttonXML.getfieldTransl("button_check", false) +"\">\n");
		html.append(getHyperLinkEnd() + "</td>\n");
		return html.toString();
	}
    
	public String getChequeButtonHTML(String pHyperLink){
		StringBuilder html = new StringBuilder ();
		html.append("<td align=\"center\">\n");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('" + pHyperLink+"','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/cheque.gif\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\" \">\n");
		html.append("</a></td>\n");
		return html.toString();
	}
    
	public String getStornoButtonStyleHTML(String pHyperLink, String pDivName, String pTDStyle){
		StringBuilder html = new StringBuilder ();
		html.append("<td " + pTDStyle + "><div class=\"div_button\" onclick=\"ajaxpage('"+ pHyperLink + "','" + pDivName + "');\">" + "<img vspace=\"0\" hspace=\"0\"" +
				" src=\"../images/oper/storno.png\" align=\"top\" width=\"16\" height=\"16\" style=\"border: 0px;\"" + 
				" title=\"" + buttonXML.getfieldTransl("title_storno", false)+"\">" + getHyperLinkEnd()+"</td>\n");
		return html.toString();
	}
    
    public String getSystemUserName(String id_user){
    	return getOneValueByIntId("SELECT name_user FROM " + getGeneralDBScheme()+".v_user_names WHERE id_user = ? ", id_user);
    }
    
	public String getReportButtonHTML(String pHyperLink, String pTarget){
		StringBuilder html = new StringBuilder ();
		html.append("<td align=\"center\">\n");
		html.append("<a class=\"div_logo_hyperlink\" href=\"" + pHyperLink + "\" target=\"" + pTarget + "\" title=\"" + buttonXML.getfieldTransl("button_report", false) +"\">\n");
		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/report_report.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\">\n");
		html.append("</td>\n");
		return html.toString();
	}
    
	public String getReportPostingHTML(String pHyperLink, String pTarget){
		StringBuilder html = new StringBuilder ();
		html.append("<td align=\"center\">\n");
		html.append("<a class=\"div_logo_hyperlink\" href=\"" + pHyperLink + "\" target=\"" + pTarget + "\" title=\"" + buttonXML.getfieldTransl("button_posting_line", false) +"\">\n");
		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/report_posting.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\">\n");
		html.append("</td>\n");
		return html.toString();
	}
    
	public String getDeleteFileImageHTML(String pHyperLink, String pDivName, String pPrompt){
		StringBuilder html = new StringBuilder ();
		html.append("<a hrev=\"\" onclick=\"var msg='" + pPrompt + "?';var res=window.confirm(msg);if (res) {ajaxpage('"+ pHyperLink + "','" + pDivName + "');}\">" + "<img vspace=\"0\" hspace=\"0\"" +
				" src=\"../images/oper/rows/del.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
				" title=\"" + buttonXML.getfieldTransl("button_delete", false)+"\">" + getHyperLinkEnd()+"</a>\n");
		/*html.append("<td align=\"center\">\n");
		html.append(getHyperLinkFirst() + pHyperLink + "&action=remove&process=no" + getHyperLinkMiddle() + "\n");
		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/delete.png\" align=\"top\" style=\"border: 0px;\" title=\""+ buttonXML.getfieldTransl(pLanguage,"button_delete", false) +"\">\n");
		html.append(getHyperLinkEnd() + "</td>\n");
		*/
		return html.toString();
	}
    
	public String getDeleteButtonStyle2HTML(String pHyperLink, String pTDStyle, String pPrompt, String pValue){
		String lPrompt = isEmpty(pPrompt)?"":pPrompt;
		String lValue = isEmpty(pValue)?"":pValue;
		if (lPrompt.contains("%1")) {
			lPrompt = preparePrompt(lPrompt.replaceAll("%1", lValue));
		} else {
			lPrompt = preparePrompt(lPrompt) + " \\\'" + preparePrompt(lValue)+ "\\\'";
		}
		return getDeleteButtonStyle2HTML(pHyperLink, pTDStyle, lPrompt);
	}
    
	public String getDeleteButtonStyle2HTML(String pHyperLink, String pTDStyle, String pPrompt){
		StringBuilder html = new StringBuilder ();
		html.append("<td " + pTDStyle + "><div class=\"div_button\" onclick=\"var msg='" + pPrompt + "?';var res=window.confirm(msg);if (res) {ajaxpage('"+ pHyperLink + "','div_main');}\">" + "<img vspace=\"0\" hspace=\"0\"" +
				" src=\"../images/oper/rows/delete.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
				" title=\"" + buttonXML.getfieldTransl("button_delete", false)+"\">" + getHyperLinkEnd()+"</td>\n");
		/*html.append("<td align=\"center\">\n");
		html.append(getHyperLinkFirst() + pHyperLink + "&action=remove&process=no" + getHyperLinkMiddle() + "\n");
		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/delete.png\" align=\"top\" style=\"border: 0px;\" title=\""+ buttonXML.getfieldTransl(pLanguage,"button_delete", false) +"\">\n");
		html.append(getHyperLinkEnd() + "</td>\n");
		*/
		return html.toString();
	}
	
	public String preparePrompt(String pPrompt) {
		String lReturn = "";
		if (!isEmpty(pPrompt)) {
			lReturn = pPrompt.replace("'", "\\'").replace("\"", "\\'\\'");
		}
		return lReturn;
	}
    
	public String getDeleteButtonHTML(String pHyperLink, String pPrompt){
		return getDeleteButtonStyle2HTML(pHyperLink, "style=\"width:15px;text-align:center;\"", pPrompt);
	}
    
	public String getDeleteButtonHTML(String pHyperLink, String pPrompt, String pValue){
		return getDeleteButtonStyle2HTML(pHyperLink, "style=\"width:15px;text-align:center;\"", pPrompt, pValue);
	}
    
	public String getDeleteButtonHTML(String pHyperLink, String pAction, String pProcess, String pImage, String pPrompt){
		return getDeleteButtonBaseHTML(pHyperLink, pAction, pProcess, pImage, pPrompt, "div_main");
	}
    
	public String getDeleteButtonBaseHTML(String pHyperLink, String pAction, String pProcess, String pImage, String pPrompt, String pDivName){
		StringBuilder html = new StringBuilder ();
		String lPromptMessage = "";
		String lTitle = "";
		if (isEmpty(pPrompt)) {
			lPromptMessage = buttonXML.getfieldTransl("delete", false);
		} else {
			lPromptMessage = preparePrompt(pPrompt);
		}
		lTitle = lPromptMessage;
		lPromptMessage = lPromptMessage.replace("'", "\\'").replace("\"", "\\'\\'");
		lTitle = lTitle.replace("\"", "'");
		//System.out.println("lPromptMessage="+lPromptMessage);
		html.append("<td style=\"width:15px;text-align:center;\"><div class=\"div_button\" onclick=\"var msg='" + lPromptMessage + 
				"?';var res=window.confirm(msg);if (res) {ajaxpage('"+ 
				pHyperLink + "&action="+pAction+"&process="+pProcess+"','" + pDivName + "');}\">" + "<img vspace=\"0\" hspace=\"0\"" +
				" src=\"../images/oper/rows/"+pImage+"\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
				" title=\"" +lTitle+"\">" + getHyperLinkEnd()+"</td>\n");
		
		//html.append("<td align=\"center\">\n");
		//html.append(getHyperLinkFirst() + pHyperLink + "&action=" + pAction + "&process=" + pProcess + getHyperLinkMiddle() + "\n");
		//html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/" + pImage + "\" align=\"top\" style=\"border: 0px;\" title=\""+ pTitle +"\">\n");
		//html.append(getHyperLinkEnd() + "</td>\n");
		return html.toString();
	}
    
	public String getBottomFrameTable(){
		return "<table class=\"tablebottom\"><thead>";
	}
    
	public String getDocTemplateTable(){
		return "<table class=\"table_doc_template\">";
	}
    
	public String getTableSorterTable(){
		return "<table class=\"tablesorter\"><thead>";
	}
    
	public String getBottomFrameTableTH3(bcXML pXML, String pColumnName, String pTitle, String pAdditionalInfo){
		StringBuilder html = new StringBuilder ();
		String lTitle = "";
		String lColumnCaption = "";

		if (!isEmpty(pTitle)) {
			lTitle = "&nbsp;<img src=\"../images/menu/info.png\" title=\"" + pTitle + "\">";
		}
		
		if (pColumnName == null || "".equalsIgnoreCase(pColumnName)) {
			lColumnCaption = "&nbsp;";		
		} else { 
	    	if ("CREATION_DATE".equalsIgnoreCase(pColumnName) ||
	        		"CREATION_DATE_FRMT".equalsIgnoreCase(pColumnName) ||
	        		"LAST_UPDATE_DATE".equalsIgnoreCase(pColumnName) ||
	        		"LAST_UPDATE_DATE_FRMT".equalsIgnoreCase(pColumnName)) {
	    		lColumnCaption = commonXML.getfieldTransl(pColumnName, false);
	    	} else if ("ID_CLUB".equalsIgnoreCase(pColumnName) ||
	    			"NAME_CLUB".equalsIgnoreCase(pColumnName) ||
	    			"SNAME_CLUB".equalsIgnoreCase(pColumnName) ||
	    			"CLUB".equalsIgnoreCase(pColumnName)) {
	    		lColumnCaption = clubXML.getfieldTransl(pColumnName, false);
	    	} else if ("RN".equalsIgnoreCase(pColumnName)) {
	    		lColumnCaption = commonXML.getfieldTransl(pColumnName, false);
	    	} else if ("CLUB_MEMBER_STATUS".equalsIgnoreCase(pColumnName) ||
	    			"CD_CLUB_MEMBER_STATUS".equalsIgnoreCase(pColumnName) ||
	    			"NAME_CLUB_MEMBER_STATUS".equalsIgnoreCase(pColumnName)) {
	    		lColumnCaption = clubXML.getfieldTransl("CLUB_MEMBER_STATUS", false);
	    	} else {
	    		lColumnCaption = pXML.getfieldTransl(pColumnName, false);
	    	}
			if (!isEmpty(pAdditionalInfo)) {
				lColumnCaption = lColumnCaption + pAdditionalInfo;
			}
		}
		html.append("<th" + ("RN".equalsIgnoreCase(pColumnName)?" width=\"40\"":"")+ ">" + lColumnCaption + lTitle + "</th>\n");
		return html.toString();
	}
    
	public String getBottomFrameTableTH(bcXML pXML, String pColumnName){
		return getBottomFrameTableTH3(pXML, pColumnName, "", "");
	}
	
	public String getBottomFrameTableExtendedTH(bcXML pXML, int pColumnId, String pColumnName, String pOrderColumn, String pOrderType, String pOrderHyperLink){
		StringBuilder html = new StringBuilder ();
		String pFieldCaption = "";
		String lColumnId = "" + pColumnId;
    	if ("RN".equalsIgnoreCase(pColumnName) || 
        		"CREATION_DATE".equalsIgnoreCase(pColumnName) ||
        		"CREATION_DATE_FRMT".equalsIgnoreCase(pColumnName) ||
        		"LAST_UPDATE_DATE".equalsIgnoreCase(pColumnName) ||
        		"LAST_UPDATE_DATE_FRMT".equalsIgnoreCase(pColumnName)) {
    		pFieldCaption = commonXML.getfieldTransl(pColumnName, false);
    	} else if ("ID_CLUB".equalsIgnoreCase(pColumnName)) {
    		pFieldCaption = clubXML.getfieldTransl(pColumnName, false);
    	} else if ("ID_CLUB".equalsIgnoreCase(pColumnName)) {
    		html.append("<th>"+ clubXML.getfieldTransl("id_club", false)+"</th>\n");
    	} else if ("CLUB_NAME".equalsIgnoreCase(pColumnName)) {
    		html.append("<th>"+ clubXML.getfieldTransl("club", false)+"</th>\n");
    	} else if ("NAME_CLUB".equalsIgnoreCase(pColumnName)) {
    		html.append("<th>"+ clubXML.getfieldTransl("club", false)+"</th>\n");
    	} else if ("SNAME_CLUB".equalsIgnoreCase(pColumnName)) {
    		html.append("<th>"+ clubXML.getfieldTransl("club", false)+"</th>\n");
    	} else {
    		pFieldCaption = pXML.getfieldTransl(pColumnName, false);
    	}
    	
    	if (lColumnId.equalsIgnoreCase(pOrderColumn)) {
    		if ("asc".equalsIgnoreCase(pOrderType)) {
        		pFieldCaption = pFieldCaption + 
        			"<div class=\"div_button\">" + 
        			"<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/sort/asc2.png\"" + "\" /></div>"+ 
        			"<div class=\"div_button\" onclick=\"ajaxpage('"+ pOrderHyperLink + "&col="+pColumnId+"&order=desc','div_main');\">" + 
        			"<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/sort/desc.png\"" + "\" /></div>";
    		} else {
    			pFieldCaption = pFieldCaption + 
    				"<div class=\"div_button\" onclick=\"ajaxpage('"+ pOrderHyperLink + "&col="+pColumnId+"&order=asc','div_main');\">" + 
    				"<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/sort/asc.png\"" + "\" /></div>"+ 
    				"<div class=\"div_button\">" + 
    				"<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/sort/desc2.png\"" + "\" /></div>";
    		}
    	} else {
    		pFieldCaption = pFieldCaption + 
    			"<div class=\"div_button\" onclick=\"ajaxpage('"+ pOrderHyperLink + "&col="+pColumnId+"&order=asc','div_main');\">" + 
    			"<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/sort/asc.png\"" + "\" /></div>"+ 
    			"<div class=\"div_button\" onclick=\"ajaxpage('"+ pOrderHyperLink + "&col="+pColumnId+"&order=desc','div_main');\">" + 
    			"<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/sort/desc.png\"" + "\" /></div>";
    	}
    	
    	
    	html.append("<th>"+ pFieldCaption+"</th>\n");
    	
		return html.toString();
	}
    
	public String getBottomFrameTableTDBase(String pColSpan, String pColumnName, int pColumnType, String pValue, String pHyperLink, String pDivName, String pFont, String pBgColor, String pAlign){
		StringBuilder html = new StringBuilder ();
		String tdBegin = "<td>";
		if (!(pColSpan == null || "".equalsIgnoreCase(pColSpan))) {
			tdBegin = "<td colspan=\"" + pColSpan+"\">";
		}
		String tdEnd = "</td>";
		String myFontBegin = "";
		String myFontEnd = "";
		String myBGColor = "";
		String myHyperLinkBegin = "";
		String myHyperLinkEnd = "";

		if (!(pHyperLink == null || "".equalsIgnoreCase(pHyperLink))) {
			myHyperLinkBegin = getHyperLinkFirst() + pHyperLink + getHyperLinkMiddle(pDivName);
			myHyperLinkEnd = getHyperLinkEnd();
		}
		if (!(pFont == null || "".equalsIgnoreCase(pFont))) {
			myFontBegin = pFont;
			myFontEnd = "</font></b>";
		}
		if (!(pBgColor == null || "".equalsIgnoreCase(pBgColor))) {
			myBGColor = pBgColor;
		}
		
		tdBegin = formatTDData(pColSpan, pColumnName, pColumnType, myBGColor, pAlign);
		
		/*
		if (pColumnName.toUpperCase().contains("FRMT") || 
				pColumnName.toUpperCase().contains("PERCENT") || 
				pColumnName.toUpperCase().contains("TSL") || 
				pColumnName.toUpperCase().contains("DATE") || 
				pColumnName.toUpperCase().contains("NUMBER") || 
				pColumnName.toUpperCase().contains("_COUNT") || 
				pColumnName.toUpperCase().startsWith("COUNT") || 
				pColumnName.toUpperCase().contains("ID_") || 
				"RN".equalsIgnoreCase(pColumnName)) {
			tdBegin = "<td " + myBGColor + " align=\"center\">";
		} else {
			tdBegin = "<td " + myBGColor + ">";
		}
		*/
		html.append(tdBegin +  myHyperLinkBegin +myFontBegin + getValue3(pValue) + myFontEnd + myHyperLinkEnd + tdEnd + "\n");
		return html.toString();
	}
	
	public String getBottomFrameTableTDBase(String pColSpan, String pColumnName, int pColumnType, String pValue, String pHyperLink, String pFont, String pBgColor){
		return getBottomFrameTableTDBase(pColSpan, pColumnName, Types.OTHER, pValue, pHyperLink, "div_main", pFont, pBgColor, "");
	}
    
	public String getBottomFrameTableTD(String pColumnName, String pValue, String pHyperLink, String pFont, String pBgColor){
		return getBottomFrameTableTDBase("", pColumnName, Types.OTHER, pValue, pHyperLink, "div_main", pFont, pBgColor, "");
	}
    
	public String getBottomFrameTableTDWithDiv(String pColumnName, String pValue, String pHyperLink, String pFont, String pBgColor, String pDivName){
		return getBottomFrameTableTDBase("", pColumnName, Types.OTHER, pValue, pHyperLink, pDivName, pFont, pBgColor, "");
	}

    
	public String getBottomFrameTableTDAlign(String pColumnName, String pValue, String pHyperLink, String pFont, String pBgColor, String pAlign){
		return getBottomFrameTableTDBase("", pColumnName, Types.OTHER, pValue, pHyperLink, "div_main", pFont, pBgColor, pAlign);
	}

    
	public String getBottomFrameTableTD2(String pColSpan, String pColumnName, String pValue, String pHyperLink, String pFont, String pBgColor){
		return getBottomFrameTableTDBase(pColSpan, pColumnName, Types.OTHER, pValue, pHyperLink, "div_main", pFont, pBgColor, "");
	}

    public String getBottomFrameTableTDShort(String pColumnName, String pValue){
		return getBottomFrameTableTD(pColumnName, pValue, "", "", "");
	}

    public String getHyperLinkFirst(){
		return "<div class=\"div_table_element\" onclick=\"ajaxpage('";
	}

    public String getHyperLinkHeaderFirst(){
		return "<div class=\"div_table_element_header\" onclick=\"ajaxpage('";
	}

    public String getHyperLinkMiddle(){
		return "', 'div_main')\">";
	}

    public String getHyperLinkMiddle(String pDivName){
		return "', '" + pDivName + "')\">";
	}

    public String getHyperLinkEnd(){
		return "</div>";
	}

    public String getSubmitButton(String pHyperLink){
        StringBuilder html = new StringBuilder();
        String lHyperLink = pHyperLink.trim();
        String lEndChar = lHyperLink.substring(lHyperLink.length()-5);
        if (!("value".equalsIgnoreCase(lEndChar.trim()))) {
        	lHyperLink = lHyperLink + "' ";
        }
        html.append("<button type=\"button\" class=\"button\" onclick=\"ajaxpage('" + 
    			pHyperLink + ",'div_main')\">" + 
    			buttonXML.getfieldTransl("submit", false) + "</button>");
        return html.toString();
    }
    
    public String getSubmitButtonAjax(String pHyperLink){
        return getSubmitButtonAjax3(pHyperLink, "submit", "");
    }
    
    public String getSubmitButtonAjax2(String pHyperLink, String pButton){
        return getSubmitButtonAjax3(pHyperLink, pButton, "");
    }
    
    public String getSubmitButtonAjax3(String pHyperLink, String pButton, String pConfirmScript){
        StringBuilder html = new StringBuilder();
        String lHyperLink = pHyperLink.trim();
        String lEnd5Char = lHyperLink.substring(lHyperLink.length()-5);
        String lEndChar = lHyperLink.substring(lHyperLink.length()-1);
        if ("?".equalsIgnoreCase(lEndChar) || "&".equalsIgnoreCase(lEndChar)) {
    		lHyperLink = lHyperLink + "' + ";
    	} else {
            if ("value".equalsIgnoreCase(lEnd5Char.trim())) {
            	lHyperLink = lHyperLink + " + '&' + ";
            } else {
            	lHyperLink = lHyperLink + "?' + ";
            }
    	}
        if (!isEmpty(pConfirmScript)) {
        html.append("<button type=\"button\" class=\"button\" onclick=\" if ("+pConfirmScript+") {ajaxpage('" + 
        		lHyperLink + " mySubmitForm('updateForm'),'div_main')}\">" + 
        		buttonXML.getfieldTransl(pButton, false) + "</button>");
        } else {
            html.append("<button type=\"button\" class=\"button\" onclick=\"ajaxpage('" + 
           		lHyperLink + " mySubmitForm('updateForm'),'div_main')\">" + 
           		buttonXML.getfieldTransl(pButton, false) + "</button>");
        }
        return html.toString();
    }

    public String getGoBackButton(String pHyperLink){
    	StringBuilder html = new StringBuilder();
    	html.append("<button type=\"button\" class=\"button\" onclick=\"ajaxpage('" + 
    			pHyperLink + "', 'div_main')\">" + 
    			buttonXML.getfieldTransl("cancel", false) + "</button>");
    	return html.toString();
	}

    
    public String getJurPersonShortName (String jurPerson_id) {
    	return getOneValueByIntId("SELECT sname_jur_prs FROM " + getGeneralDBScheme()+".vc_jur_prs_short_all WHERE id_jur_prs = ? ", jurPerson_id);
    }//getJurPersonShortName
    
    public String getClubShortName (String id_club) {
    	return getOneValueByIntId("SELECT sname_club FROM " + getGeneralDBScheme()+".vc_club_names WHERE id_club = ?", id_club);
    }//getClubShortName
    
    public boolean isJurPersonDealer (String jurPerson_id) {
    	boolean isDealer = false;
    	String typeValue = "";
    	if (isEmpty(jurPerson_id)) {
    		isDealer = false;
    	} else {
    		typeValue = getOneValueByIntId("SELECT CASE WHEN NVL(is_dealer, 'N') = 'Y' THEN '1' ELSE '0' END type_value FROM " + getGeneralDBScheme()+".vc_jur_prs_short_club_all WHERE id_jur_prs = ?", jurPerson_id);
    		if ("1".equalsIgnoreCase(typeValue)) {
    			isDealer = true;
    		}
    	}
    	return isDealer;
    }//getJurPersonShortName


    public String getGoToHyperLink(String pMenu, String pId, String pHyperLink){

        StringBuilder html = new StringBuilder();
        
        boolean hasPermission = false;
        if (isEmpty(pMenu)) {
        	hasPermission = true;
        } else if (this.isEditMenuPermited(pMenu)>=0) {
        	hasPermission = true;
        }
        
		if (hasPermission) {
			if (!isEmpty(pId)) {
				html.append("<span class=\"div_goto\" onclick=\"ajaxpage('" + pHyperLink +"', 'div_main')\">&nbsp;(" + this.form_messageXML.getfieldTransl("go_to", false) + ")</span>");
			}
		}
        
        return html.toString();
	}

    public String getBKAccountName (String id_account) {
    	return getOneValueByIntId("SELECT cd_bk_account||' '||name_bk_account FROM " + getGeneralDBScheme()+".vc_bk_accounts_club_all WHERE id_bk_account = ? ", id_account);
    }//getBKAccountName

    public String getCurrencyOptions(String cd_currency, boolean isNull) {
    	return getSelectBodyFromQuery(
    			" SELECT cd_currency, name_currency " +
    			"   FROM " + getGeneralDBScheme()+".v_currency_all " +
    			"  WHERE is_used=1" +
    			"  ORDER BY name_currency", cd_currency, isNull);
    } //getCurrencyOptions()
    
    public String getCurrencyNameById(String currency_id) {
    	return getOneValueByIntId("select name_currency FROM " + getGeneralDBScheme()+".vc_currency_all WHERE cd_currency = ? ", currency_id);
    }

    public String getWindowFindBKAccount(String pField, String pValue, String pSize) {
    	StringBuilder html = new StringBuilder();
    	
    	html.append("<input type=\"hidden\" id=\""+pField+"_id_bk_account\" name=\""+pField+"_id_bk_account\" value=\""+pValue+"\">\n");
    	html.append("<input type=\"text\" id=\""+pField+"_name_bk_account\" name=\""+pField+"_name_bk_account\" size=\""+pSize+"\" value=\"" + getBKAccountName(pValue) + "\" readonly class=\"inputfield\">\n");
    	html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findbkaccounts.jsp?id_bk_account='+document.getElementById('"+pField+"_id_bk_account').value+'&field="+pField+"','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
    	html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\" title=\"" + buttonXML.getfieldTransl("button_edit", false) + "\"></a>\n");
    	html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('"+pField+"_id_bk_account').value = ''; document.getElementById('"+pField+"_name_bk_account').value = ''; return false;\">\n");
    	html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\" title=\"" + buttonXML.getfieldTransl("button_delete", false) + "\"></a>\n");

    	return html.toString();
    }

    public String getBKOperationSchemeLineAdditionalName(String idBkOperationSchemeLine){
    	return getOneValueByIntId("SELECT oper_number||' ('||debet_cd_bk_account_sh_line||' - '||credit_cd_bk_account_sh_line||')' name_scheme FROM " + getGeneralDBScheme()+".vc_oper_scheme_lines_club_all WHERE id_bk_operation_scheme_line = ? ", idBkOperationSchemeLine);
    } // getNatPrsGroupName

    public String getWindowFindBKOperationScheme(String pField, String pValue, String pSize) {
    	StringBuilder html = new StringBuilder();
    	
    	html.append("<input type=\"hidden\" id=\"id_"+pField+"\" name=\"id_"+pField+"\" value=\""+pValue+"\">\n");
    	html.append("<input type=\"text\" id=\"name_"+pField+"\" name=\"name_"+pField+"\" size=\""+pSize+"\" value=\"" + getBKOperationSchemeLineAdditionalName(pValue) + "\" readonly class=\"inputfield\">\n");
    	html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findoperscheme.jsp?id_bk_operation_scheme_line='+document.getElementById('id_"+pField+"').value+'&field="+pField+"','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
    	html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\" title=\"" + buttonXML.getfieldTransl("button_edit", false) + "\"></a>\n");
    	html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"+pField+"').value = ''; document.getElementById('name_"+pField+"').value = ''; return false;\">\n");
    	html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\" title=\"" + buttonXML.getfieldTransl("button_delete", false) + "\"></a>\n");

    	return html.toString();
    }

    public String getClubRelationshipName(String id_club_rel){
    	return getOneValueByIntId("SELECT full_name_club_rel FROM " + getGeneralDBScheme()+".vc_club_rel_club_all WHERE id_club_rel = ? ", id_club_rel);
    } // getBCUserName

    public String getWindowFindClurRelationship(String pField, String pValue, String pParticipant, String pSize) {
    	StringBuilder html = new StringBuilder();
    	
    	html.append("<input type=\"hidden\" id=\"id_"+pField+"\" name=\"id_"+pField+"\" value=\""+pValue+"\">\n");
    	html.append("<input type=\"text\" id=\"name_"+pField+"\" name=\"name_"+pField+"\" size=\""+pSize+"\" value=\"" + getClubRelationshipName(pValue) + "\" readonly class=\"inputfield\">\n");
    	html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findrelationship.jsp?id_participant="+pParticipant+"&id_club_rel='+document.getElementById('id_"+pField+"').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
    	html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\" title=\"" + buttonXML.getfieldTransl("button_edit", false) + "\"></a>\n");
    	html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"+pField+"').value = ''; document.getElementById('name_"+pField+"').value = ''; return false;\">\n");
    	html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\" title=\"" + buttonXML.getfieldTransl("button_delete", false) + "\"></a>\n");

    	return html.toString();
    }

    public String getBankAccountNumberAndName (String id_account) {
    	return getOneValueByIntId("SELECT number_bank_account||' - '||name_bank_account_type FROM " + getGeneralDBScheme()+".vc_bank_account_all WHERE id_bank_account = ? ", id_account);
    }//getBankAccountNumber

    public String getWindowFindBankAccount2(String pField, String pValue, String pSize) {
    	StringBuilder html = new StringBuilder();
    	
    	html.append("<input type=\"hidden\" id=\"id_"+pField+"\" name=\"id_"+pField+"\" value=\""+pValue+"\">\n");
    	html.append("<input type=\"text\" id=\"name_"+pField+"\" name=\"name_"+pField+"\" size=\""+pSize+"\" value=\"" + getBankAccountNumberAndName(pValue) + "\" readonly class=\"inputfield\">\n");
    	html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findbankaccounts2.jsp?id_bank_account='+document.getElementById('id_"+pField+"').value+'&field="+pField+"','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
    	html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\" title=\"" + buttonXML.getfieldTransl("button_edit", false) + "\"></a>\n");
    	html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"+pField+"').value = ''; document.getElementById('name_"+pField+"').value = ''; return false;\">\n");
    	html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\" title=\"" + buttonXML.getfieldTransl("button_delete", false) + "\"></a>\n");

    	return html.toString();
    }   
    
    public String getWindowFindClub(String pField, String pValue, String pShortNameClub, String pSize, String pOnChangeScriptName) {
    	StringBuilder html = new StringBuilder();
    	
    	String clubSelect = getSelectBodyFromQuery(
    			" SELECT id_club, sname_club " +
    			"   FROM " + getGeneralDBScheme()+".vc_club_names " +
    			"  ORDER BY sname_club", pValue, pSize, true);
    	
    	String lClubName = "";
    	if (isEmpty(pShortNameClub)) {
			lClubName = pShortNameClub;
		} else {
			lClubName = this.getClubShortName(pValue);
		}
    	
    	if (this.selectOprionCount > this.maxSelectOption) { 
    		html.append("<input type=\"hidden\" id=\"id_"+pField+"\" name=\"id_"+pField+"\" value=\""+pValue+"\">\n");
    		html.append("<input type=\"text\" id=\"name_"+pField+"\" name=\"name_"+pField+"\" size=\""+pSize+"\" value=\"" + lClubName + "\" readonly class=\"inputfield\" title=\"" + lClubName + "\">\n");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: " + pOnChangeScriptName + "var cWin=window.open('../crm/services/findclub.jsp?id_club='+document.getElementById('id_"+pField+"').value+'&field="+pField+"','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\" title=\"" + buttonXML.getfieldTransl("button_edit", false) + "\"></a>\n");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_"+pField+"').value = ''; document.getElementById('name_"+pField+"').value = ''; return false;\">\n");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\" title=\"" + buttonXML.getfieldTransl("button_delete", false) + "\"></a>\n");
    	} else {
    		html.append("<script type=\"text/javascript\">");
    		html.append("function changeClubNameScript() {");
    		html.append("   document.getElementById('id_" + pField + "').value = document.getElementById('name_" + pField + "').value;");
    		//html.append("   alert(document.getElementById('name_" + pField + "').value);");
    		html.append("}");
    		html.append("changeClubNameScript();");
    		html.append("</script>");
    		html.append("<input type=\"hidden\" id=\"id_"+pField+"\" name=\"id_"+pField+"\" value=\"" + pValue + "\">\n");
    		html.append("<select name=\"name_" + pField + "\" id=\"name_" + pField + "\" class=\"inputfield\" onchange=\"changeClubNameScript(); " + pOnChangeScriptName + "\">\n");
    		html.append(clubSelect);
    		html.append("</select>\n");
    	}

    	return html.toString();
    }

    /*
    В pQuesy 3-е поле должно содержать значения Y/N (выбрано/невыбрано)
    */
    public String getCheckBoxesFromQuery(String pName, String pQuery, ArrayList<bcFeautureParam> pParam, boolean readOnly){
		StringBuilder return_value=new StringBuilder();
		PreparedStatement st = null;
        Connection con = null;
        LOGGER.debug(prepareSQLToLog(pQuery, pParam));

        try{
            con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(pQuery);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            
            int counter = 0;

			while(rset.next()){
				
				counter ++;
				if (counter > 1) {
					return_value.append("<br>");
				}
				
				String lChecked = "";
				if ("Y".equalsIgnoreCase(rset.getString(3))) {
					lChecked = " CHECKED ";
				}
				if (!readOnly) {
					if ("Y".equalsIgnoreCase(rset.getString(3))) {
						return_value.append("<input type=\"hidden\" name=\"prv_"+pName+"_"+rset.getString(1)+"\" id=\"prv_"+pName+"_"+rset.getString(1)+"\" value=\""+rset.getString(1)+"\">");
					}
					return_value.append("<input type=\"checkbox\" name=\""+pName+"_"+rset.getString(1)+"\" id=\""+pName+"_"+rset.getString(1)+"\" " + lChecked + " value=\""+rset.getString(3)+"\">"); 
					return_value.append("<label class=\"checbox_label\" for=\""+pName+"_"+rset.getString(1)+"\">"+rset.getString(2)+"</label>");
				} else {
					return_value.append("<input type=\"checkbox\" name=\""+pName+"_"+rset.getString(1)+"\" id=\""+pName+"_"+rset.getString(1)+"\" disabled " + lChecked + " value=\""+rset.getString(3)+"\">"); 
					return_value.append(rset.getString(2));
				}
			}
		}
		catch (SQLException e) {
			LOGGER.error("Dbean.getCheckBoxesFromQuery SQLException: " + e.toString());
			return_value.append("<option value=\"\">SQL Error</option>");
		}
        catch (Exception el) {
        	LOGGER.error("Dbean.getCheckBoxesFromQuery Exception: " + el.toString());
        }
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        }
		return return_value.toString();
	}
    
    public String getCheckBoxesFromQuery(String pName, String pQuery, boolean readOnly){
    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	return getCheckBoxesFromQuery(pName, pQuery, pParam, readOnly);
    	
    }
    
    private String targetProgramCount = "0";
    
    public String getTargetProgramCount() {
    	return targetProgramCount;
    }
    
    protected String geTargetProgramImagesSelecOneHTML(String pIdNatPrs, String pDataType, String pSQL, ArrayList<bcFeautureParam> pParam, String pFormName, String pDivName) {
    	StringBuilder html = new StringBuilder();
    	PreparedStatement st = null;
        Connection con = null;
        
        try{
        	LOGGER.debug(prepareSQLToLog(pSQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(pSQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();

            html.append("<table id=\"target_program_table\">");
            html.append("<tbody>");
            
    		int colCount = 0;
    		
            String pFontBold = "";
            String pBackground = "";
            String titleTargetPrg = "";
    		
    		html.append("<tr>");
            while (rset.next())
            {
    			colCount ++;
    			targetProgramCount  = rset.getString("ROW_COUNT");
    			//String lIdTargetPrgParent = rset.getString("ID_TARGET_PRG_PARENT");
    			int lChildCount = Integer.parseInt(rset.getString("CHILD_COUNT"));
    			/*if (this.targetProgramCount == 1) {
    				html.append("<td colspan=\"3\" style=\"color:green;font-size:10px;\">\n");
    				html.append("Выберите одну или несколько программ и нажмите кнопку \"Далее\"");
    				html.append("</td></tr><tr>\n");
    			}*/
            	html.append("<td>\n");
            	String pLink = "";
            	if (lChildCount > 0) {
            		if ("ALL".equalsIgnoreCase(pDataType)) {
            			pLink = "ajaxpage('action/replenish.jsp?tab2=1&tab=5&data_type="+pDataType+"&action=select&id_prg_parent=" + rset.getString("ID_TARGET_PRG") + "&id_nat_prs=" + pIdNatPrs + "&' + mySubmitForm('" + pFormName + "'),'div_main');";
            		} else {
            			pLink = "ajaxpage('action/replenish.jsp?tab2=2&data_type="+pDataType+"&action=check_card&tab2=2&id_prg_parent=" + rset.getString("ID_TARGET_PRG") + "&id_nat_prs=" + pIdNatPrs + "&' + mySubmitForm('" + pFormName + "'),'" + pDivName + "');";
            		}
                } else {
            		if ("ALL".equalsIgnoreCase(pDataType)) {
            			pLink = "ajaxpage('action/replenish.jsp?tab2=1&data_type="+pDataType+"&action=check_card&tab2=2&id_target_prg=" + rset.getString("ID_TARGET_PRG") + "&id_nat_prs=" + pIdNatPrs + "&' + mySubmitForm('" + pFormName + "'),'" + pDivName + "');";
            		} else {
            			pLink = "ajaxpage('action/replenishupdate.jsp?tab2=2&type=card&process=no&data_type="+pDataType+"&action=select_program&id_target_prg=" + rset.getString("ID_TARGET_PRG") + "&id_nat_prs=" + pIdNatPrs + "&' + mySubmitForm('" + pFormName + "'),'div_action_big');";
            		}
                }
            	titleTargetPrg = isEmpty(rset.getString("SNAME_TARGET_PRG"))?"":" title=\"" + rset.getString("SNAME_TARGET_PRG") + "\"";
            	if (isEmpty(rset.getString("IMAGE_TARGET_PRG"))) {
            		html.append("<span class=\"target_program\" id=\"target_prg_" + rset.getString("ID_TARGET_PRG") + "\" " + titleTargetPrg + " onclick=\"" + pLink + "\"></span>\n");
            		html.append("<span class=\"target_program_caption\" onclick=\"" + pLink + "\">" + rset.getString("SNAME_TARGET_PRG") + "</span>\n");
            	} else {
            		html.append("<img src=\"../TargetProgramPicture?id_target_prg="+rset.getString("ID_TARGET_PRG")+"\" class=\"target_program\" id=\"target_prg_" + rset.getString("ID_TARGET_PRG") + "\" " + titleTargetPrg + " onclick=\"" + pLink + "\"></span>\n");
            		//html.append("<img src=\""+rset.getString("IMAGE_TARGET_PRG")+"\" class=\"target_program\" id=\"target_prg_" + rset.getString("ID_TARGET_PRG") + "\" title=\"" + rset.getString("NAME_TARGET_PRG") + "\" onclick=\"" + pLink + "\"></span>\n");
            	}
            	html.append("<a class=\"target_program_about\" href=\"#openModal" + rset.getString("ID_TARGET_PRG") + "\">О программе</a>\n");
            	html.append("<div id=\"openModal" + rset.getString("ID_TARGET_PRG") + "\" class=\"modalDialog\">\n");
            	html.append("<div>\n");
            	html.append("<a href=\"#close\" title=\"Закрыть\" class=\"close\">X</a>\n");
            	html.append(getBottomFrameTable());
                html.append("<tbody>");
        		html.append("<tr>\n");
        		html.append(getBottomFrameTableTDAlign("NAME_TARGET_PRG", clubfundXML.getfieldTransl("NAME_TARGET_PRG", false), "", "<font color=\"green\">", pBackground, "left"));
        		html.append(getBottomFrameTableTDAlign("NAME_TARGET_PRG", rset.getString("NAME_TARGET_PRG"), "", "<font style=\"font-weight: bold; color: green;\">", pBackground, "left"));
        		html.append("</tr>\n");
        		if (!isEmpty(rset.getString("DESC_TARGET_PRG"))) {
            		html.append("<tr>\n");
            		html.append(getBottomFrameTableTDAlign("DESC_TARGET_PRG", clubfundXML.getfieldTransl("DESC_TARGET_PRG", false), "", pFontBold, pBackground, "left"));
            		html.append(getBottomFrameTableTDAlign("DESC_TARGET_PRG", rset.getString("DESC_TARGET_PRG"), "", "", pBackground, "left"));
            		html.append("</tr>\n");
        		}
        		if (!isEmpty(rset.getString("MIN_PAY_AMOUNT"))) {
            		html.append("<tr>\n");
            		html.append(getBottomFrameTableTDAlign("MIN_PAY_AMOUNT", clubfundXML.getfieldTransl("MIN_PAY_AMOUNT", false), "", pFontBold, pBackground, "left"));
            		html.append(getBottomFrameTableTDAlign("MIN_PAY_AMOUNT", rset.getString("MIN_PAY_AMOUNT_FRMT"), "", "", pBackground, "left"));
            		html.append("</tr>\n");
        		}
                html.append("<tr>");
        		html.append(getBottomFrameTableTDAlign("INITIATOR_TARGET_PRG", clubfundXML.getfieldTransl("INITIATOR_TARGET_PRG", false), "", pFontBold, pBackground, "left"));
        		html.append(getBottomFrameTableTDAlign("INITIATOR_TARGET_PRG", rset.getString("NAME_NAT_PRS_INITIATOR"), "", "", pBackground, "left"));
        		html.append("</tr>\n");
        		html.append("<tr>");
        		html.append(getBottomFrameTableTDAlign("ADMINISTRATOR_TARGET_PRG", clubfundXML.getfieldTransl("ADMINISTRATOR_TARGET_PRG", false), "", pFontBold, pBackground, "left"));
        		html.append(getBottomFrameTableTDAlign("ADMINISTRATOR_TARGET_PRG", rset.getString("NAME_NAT_PRS_ADMINISTRATOR"), "", "", pBackground, "left"));
        		html.append("</tr>\n");
        		html.append("<tr>\n");
        		html.append(getBottomFrameTableTDAlign("ADR_TARGET_PRG", clubfundXML.getfieldTransl("ADR_TARGET_PRG", false), "", pFontBold, pBackground, "left"));
        		html.append(getBottomFrameTableTDAlign("ADR_TARGET_PRG", rset.getString("NAME_JUR_PRS")+ "<br>" + rset.getString("ADR_JUR_PRS"), "", "", pBackground, "left"));
        		html.append("</tr>\n");
        		html.append("<tr>\n");
        		html.append(getBottomFrameTableTDAlign("DATE_BEG", clubfundXML.getfieldTransl("DATE_BEG", false), "", pFontBold, pBackground, "left"));
        		html.append(getBottomFrameTableTDAlign("DATE_BEG", rset.getString("DATE_BEG_FRMT"), "", "", pBackground, "left"));
        		html.append("</tr>\n");
        		if (!isEmpty(rset.getString("DATE_END_FRMT"))) {
	        		html.append("<tr>\n");
	        		html.append(getBottomFrameTableTDAlign("DATE_END", clubfundXML.getfieldTransl("DATE_END", false), "", pFontBold, pBackground, "left"));
	        		html.append(getBottomFrameTableTDAlign("DATE_END", rset.getString("DATE_END_FRMT"), "", "", pBackground, "left"));
	        		html.append("</tr>\n");
        		}
            	html.append("</tbody></table>\n");
            	html.append("</div>\n");
            	html.append("</div>\n");
            	html.append("<input type=\"checkbox\" style=\"visibility:hidden\" id=\"chb_target_prg_" + rset.getString("ID_TARGET_PRG") + "\" name=\"chb_target_prg_" + rset.getString("ID_TARGET_PRG") + "\">");
       	 		html.append("</td>\n");
	       	 	if (colCount== 3) {
	       	 	html.append("</tr><tr>");
					colCount = 0;
				}
            }
            html.append("</tr>");
            html.append("</tbody></table>\n");
        } // try
        catch (SQLException e) {LOGGER.error(html, e);}
        catch (Exception el) {LOGGER.error(html, el);}
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
    } // getCardTransHTML
    
    public String showException(Exception e) {
    	StringBuilder html = new StringBuilder();
    	
    	StringWriter errors = new StringWriter();
    	e.printStackTrace(new PrintWriter(errors));
    	
    	html.append(errors.toString().replaceAll("(\r\n|\n)", "<br />"));
    	return html.toString();
    }
    
    public String showCRMException(Exception e) {
    	StringBuilder html = new StringBuilder();
    	if (!isEmpty(preparedSQL)) {
    		html.append(prepareToHTML(preparedSQL).replaceAll("(\r\n|\n)", "<br />"));
    		html.append("<br /><br />");
    	}
    	html.append(showException(e));
    	return html.toString();
    }
    
    /*public String showCRMException(Exception e) {
    	StringBuilder html = new StringBuilder();
    	
    	StringWriter errors = new StringWriter();
    	e.printStackTrace(new PrintWriter(errors));
    	
    	html.append("<form action=\""
				+ "../admin/warningupdate.jsp\" name=\"updateForm\" id=\"updateForm\" accept-charset=\"UTF-8\" method=\"POST\">\n");
		html.append("<input type=\"hidden\" id=\"operation_result\" value=\"OPERATION_COMPLETE_ERROR\">\n");
		//html.append("<input type=\"hidden\" value=\"" + currentMenu.getIdMenuElement() + "\" name=\"id_menu\">");
		//html.append("<input type=\"hidden\" value=\"" + currentMenu.getCurrentTabIdMenuElement() + "\" name=\"id_tab\">");
		//html.append("<input type=\"hidden\" value=\"" + loginUser.getValue("ID_USER") + "\" name=\"found_by\">");
		//html.append("<input type=\"hidden\" value=\"" + loginUser.getValue("ID_USER") + "\" name=\"id_menu_element\">");
		html.append("<input type=\"hidden\" value=\"yes\" name=\"process\">");
		html.append("<input type=\"hidden\" value=\"adderror\" name=\"action\">");
		html.append("<table class=\"tabledetail\"><tbody>\n");
		html.append("<tr>\n");
		html.append("<td valign=\"middle\" align=\"center\">\n");
		html.append("<textarea name=\"desc_warning\" cols=\"110\" rows=\"15\" class=\"inputfield\">");
		html.append(errors.toString());
		html.append("</textarea>");
		html.append("</td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td valign=\"middle\" align=\"center\">\n");
		html.append("<button class=\"button\" onclick=\"ajaxpage('../admin/warningsupdate.jsp?' + mySubmitForm('updateForm'), 'div_main')\" type=\"button\">"+buttonXML.getfieldTransl("button_save_warning", false)+"</button>");
		html.append("</td>\n");
		html.append("</tr>\n");
		html.append("</table></form>\n");
		
    	
    	//html.append(errors.toString().replaceAll("(\r\n|\n)", "<br />"));
    	return html.toString();
    }*/
}
