package bc.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.service.bcFeautureParam;


public class bcSecurityMonitorObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcSecurityMonitorObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idMonitor;
	
	public bcSecurityMonitorObject(String pIdMonitor) {
		this.idMonitor = pIdMonitor;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_SYS_SEC_MONITOR_ALL WHERE id_sec_monitor = ?";
		fieldHm = getFeatures2(featureSelect, this.idMonitor, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getBankAccountNumberAndName (String id_account) {
    	return getOneValueByIntId("SELECT number_bank_account||' - '||name_bank_account_type FROM " + getGeneralDBScheme()+".vc_bank_account_all WHERE id_bank_account = ? ", id_account);
    }//getBankAccountNumber
    
    public String getReglamentsHTML(String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_job, name_job, desc_job, next_date, interval, broken_tsl, " +
	 		 " 		 what, interval2, num_job"  +
	 		 "  FROM (SELECT ROWNUM rn, id_job, name_job, desc_job, next_date, interval, broken_tsl, " +
	 		 " 	             what, interval interval2, num_job"  +
	 		 "          FROM (SELECT * " +
	 		 "                  FROM " + getGeneralDBScheme()+".vc_sys_jobs_all " +
	 		 "                 WHERE id_sec_monitor = ? " + 
	 		 "                 ORDER BY name_job )" +
	 		 "         WHERE ROWNUM < ? " + 
	 		 ") WHERE rn >= ?";
        pParam.add(new bcFeautureParam("int", this.idMonitor));
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        
        boolean hasReglamentPermission = false;
        
        try{
        	if (isEditMenuPermited("SECURITY_REGLAMENTS")>=0) {
        		hasReglamentPermission = true;
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-2; i++) {
               html.append(getBottomFrameTableTH(reglamentXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
                html.append("<tr>");
                for (int i=1; i <= colCount-2; i++) {
                	if (hasReglamentPermission && "ID_JOB".equalsIgnoreCase(mtd.getColumnName(i))) {
                		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/security/reglamentspecs.jsp?id="+rset.getString(i), "", ""));
                	} else {
                		html.append(getBottomFrameTableTDShort(mtd.getColumnName(i), rset.getString(i)));
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
    }
    
    public String getResultsHTML(String p_beg, String p_end, String pIdSysReport, String pReportFormat) {
    	StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_sec_monitor_exec, execution_date, name_sec_monitor_disp_type, " +
        	"        name_sec_monitor_result, id_report "+
            "   FROM (SELECT ROWNUM rn, id_sec_monitor_exec, execution_date_frmt execution_date, " +
            "                name_sec_monitor_disp_type, name_sec_monitor_result, id_report "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_sys_sec_monitor_exec_all "+
            "                  WHERE id_sec_monitor = ? " + 
            "                  ORDER BY id_sec_monitor_exec DESC) " + 
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        pParam.add(new bcFeautureParam("int", this.idMonitor));
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        
        try{
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-1; i++) {
               html.append(getBottomFrameTableTH(security_monitorXML, mtd.getColumnName(i)));
            }
            html.append("<th>&nbsp;</th>");
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	html.append("<tr>");
                for (int i=1; i <= colCount-1; i++) {
                	html.append(getBottomFrameTableTDShort(mtd.getColumnName(i), rset.getString(i)));
                }
                
                if (isEmpty(rset.getString("ID_REPORT"))) {
                    html.append("<td>&nbsp;</td>\n");
                } else {
                	if (isEmpty(pIdSysReport)) {
                        html.append("<td>&nbsp;</td>\n");
                    } else {
             	   		String myHyperLink = "../reports/Reporter?REPORT_ID=" + pIdSysReport + "&REPORT_FORMAT="+pReportFormat+"&ID_REPORT="+getValue2(rset.getString("ID_REPORT"));
             	   		html.append(getReportButtonHTML(myHyperLink, "_blank"));
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
    }
	
    public String getParametersHTML() {
    	StringBuilder html = new StringBuilder();
        StringBuilder html_preliminary = new StringBuilder();
        StringBuilder html_established = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        int colCounter = 0;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT id_task_param, cd_param, name_param, value_param, data_type, " +
        	"        sql_to_select_values, sql_get_one_value, cd_param_type "+
            "   FROM " + getGeneralDBScheme() + ".vc_sys_sec_monitor_param "+
            "  WHERE id_sec_monitor = ? " + 
            "  ORDER BY id_task_param";
        pParam.add(new bcFeautureParam("int", this.idMonitor));
        
        boolean hasEditPermission = false;
        ArrayList<String> arrayIdBefore=new ArrayList<String>();
        ArrayList<String> arrayCdBefore=new ArrayList<String>();
        ArrayList<String> arrayNameBefore=new ArrayList<String>();
        ArrayList<String> arrayValueBefore=new ArrayList<String>();
        ArrayList<String> arrayDataTypeBefore=new ArrayList<String>();
        ArrayList<String> arraySQLSelectBefore=new ArrayList<String>();
        ArrayList<String> arraySQLOneBefore=new ArrayList<String>();
        

        ArrayList<String> arrayIdAfter=new ArrayList<String>();
        ArrayList<String> arrayCdAfter=new ArrayList<String>();
        ArrayList<String> arrayNameAfter=new ArrayList<String>();
        ArrayList<String> arrayValueAfter=new ArrayList<String>();
        ArrayList<String> arrayDataTypeAfter=new ArrayList<String>();
        ArrayList<String> arraySQLSelectAfter=new ArrayList<String>();
        ArrayList<String> arraySQLOneAfter=new ArrayList<String>();
        
        try{
        	if (isEditPermited("SECURITY_MONITOR_INFO")>0) {
        		hasEditPermission = true;
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            while (rset.next())
            {
            	if ("PRELIMINARY_CONDITIONS".equalsIgnoreCase(rset.getString("CD_PARAM_TYPE"))) {
                    arrayIdBefore.add(rset.getString("ID_TASK_PARAM"));
                    arrayCdBefore.add(rset.getString("CD_PARAM"));
                    arrayNameBefore.add(rset.getString("NAME_PARAM"));
                    arrayValueBefore.add(rset.getString("VALUE_PARAM"));
                    arrayDataTypeBefore.add(rset.getString("DATA_TYPE"));
                    arraySQLSelectBefore.add(rset.getString("SQL_TO_SELECT_VALUES"));
                    arraySQLOneBefore.add(rset.getString("SQL_GET_ONE_VALUE"));
            	} else if ("ESTABLISHED_VALUES".equalsIgnoreCase(rset.getString("CD_PARAM_TYPE"))) {
                    arrayIdAfter.add(rset.getString("ID_TASK_PARAM"));
                    arrayCdAfter.add(rset.getString("CD_PARAM"));
                    arrayNameAfter.add(rset.getString("NAME_PARAM"));
                    arrayValueAfter.add(rset.getString("VALUE_PARAM"));
                    arrayDataTypeAfter.add(rset.getString("DATA_TYPE"));
                    arraySQLSelectAfter.add(rset.getString("SQL_TO_SELECT_VALUES"));
                    arraySQLOneAfter.add(rset.getString("SQL_GET_ONE_VALUE"));
            	}
            }
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

        if (arrayIdBefore.size() > 0 ) {
        	html_preliminary.append("<tr><td colspan=\"6\"><b><u>" +
        		security_monitorXML.getfieldTransl( "h_preliminary_conditions", false) +
        		"</u></b></td></tr>");
        }
        if (arrayIdAfter.size() > 0) {
        	html_established.append("<tr><td colspan=\"6\"><b><u>" +
        		security_monitorXML.getfieldTransl( "h_established_values", false) +
        		"</u></b></td></tr>");
        }
        
        for(int counter=0;counter<arrayIdBefore.size();counter++){
            colCounter = colCounter + 1;
            if (colCounter > 3) {
            	colCounter = 1;
            }
            
            if (colCounter == 1) {
            	html_preliminary.append("<tr>");
            }
        	if (hasEditPermission) {
        		html_preliminary.append("<td>");
        		html_preliminary.append(arrayNameBefore.get(counter));
        		html_preliminary.append("</td>");
        		html_preliminary.append("<td>");
        		if(!isEmpty(arraySQLSelectBefore.get(counter))){
        			html_preliminary.append("<select name=\"mpr_"+arrayIdBefore.get(counter)+"\" class=\"inputfield\">");
        			html_preliminary.append(getSelectBodyFromQuery(arraySQLSelectBefore.get(counter), arrayValueBefore.get(counter), true));
        			html_preliminary.append("</select>");
        		} else {
        			if ("ID_BANK_ACCOUNT".equalsIgnoreCase(arrayCdBefore.get(counter))) {
        				html_preliminary.append("<td>");
        				html_preliminary.append("<input type=\"hidden\" id=\"id_bank_account_"+arrayIdBefore.get(counter)+"\" name=\"id_bank_account_"+arrayIdBefore.get(counter)+"\" value=\"" + getHTMLValue(arrayValueBefore.get(counter))+ "\" readonly class=\"inputfield\">");
        				html_preliminary.append("<input type=\"text\" id=\"name_bank_account_"+arrayIdBefore.get(counter)+"\" name=\"name_bank_account_"+arrayIdBefore.get(counter)+"\" size=\"50\" value=\"" + this.getBankAccountNumberAndName(arrayValueBefore.get(counter)) + "\" readonly class=\"inputfield\">");
        				html_preliminary.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findbankaccounts2.jsp?id_bank_account=" + arrayValueBefore.get(counter) + "&field=bank_account_"+arrayIdBefore.get(counter)+"','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\"  title=\"" + this.buttonXML.getfieldTransl("button_edit", false) + "\">");
        				html_preliminary.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\"></a>");
        				html_preliminary.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_bank_account_"+arrayIdBefore.get(counter)+"').value = ''; document.getElementById('name_bank_account_"+arrayIdBefore.get(counter)+"').value = ''; return false;\" title=\"" + this.buttonXML.getfieldTransl("button_delete", false) + "\">");
        				html_preliminary.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\"></a>");
        				html_preliminary.append("</td>");
        				
        			} else {
        				html_preliminary.append("<input type=\"text\" name=\"mpr_"+arrayIdBefore.get(counter)+"\" value=\""+getHTMLValue(arrayValueBefore.get(counter))+"\" class=\"inputfield\">");
        			}
        		}
        		html_preliminary.append("</td>");
        	} else {
        		
        	}
        	if (colCounter >= 3) {
        		html_preliminary.append("</tr>");
        	}
        }

        for(int counter=0;counter<arrayIdAfter.size();counter++){
            colCounter = colCounter + 1;
            if (colCounter > 3) {
            	colCounter = 1;
            }
            
            if (colCounter == 1) {
            	html_established.append("<tr>");
            }
        	if (hasEditPermission) {
        		html_established.append("<td>");
        		html_established.append(arrayNameAfter.get(counter));
        		html_established.append("</td>");
        		html_established.append("<td>");
        		if(!isEmpty(arraySQLSelectAfter.get(counter))){
        			html_established.append("<select name=\"mpr_"+arrayIdAfter.get(counter)+"\" class=\"inputfield\">");
        			html_established.append(getSelectBodyFromQuery(arraySQLSelectAfter.get(counter), arrayValueAfter.get(counter), true));
        			html_established.append("</select>");
        		} else {
        			if ("ID_BANK_ACCOUNT".equalsIgnoreCase(arrayCdAfter.get(counter))) {
        				html_preliminary.append("<td>");
        				html_preliminary.append("<input type=\"hidden\" id=\"id_bank_account_"+arrayIdAfter.get(counter)+"\" name=\"id_bank_account_"+arrayIdAfter.get(counter)+"\" value=\"" + getHTMLValue(arrayValueAfter.get(counter))+ "\" readonly class=\"inputfield\">");
        				html_preliminary.append("<input type=\"text\" id=\"name_bank_account_"+arrayIdAfter.get(counter)+"\" name=\"name_bank_account_"+arrayIdAfter.get(counter)+"\" size=\"50\" value=\"" + this.getBankAccountNumberAndName(arrayValueAfter.get(counter)) + "\" readonly class=\"inputfield\">");
        				html_preliminary.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findbankaccounts2.jsp?id_bank_account=" + arrayValueAfter.get(counter) + "&field=bank_account_"+arrayIdAfter.get(counter)+"','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\"  title=\"" + this.buttonXML.getfieldTransl("button_edit", false) + "\">");
        				html_preliminary.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\"></a>");
        				html_preliminary.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_bank_account_"+arrayIdAfter.get(counter)+"').value = ''; document.getElementById('name_bank_account_"+arrayIdAfter.get(counter)+"').value = ''; return false;\" title=\"" + this.buttonXML.getfieldTransl("button_delete", false) + "\">");
        				html_preliminary.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\"></a>");
        				html_preliminary.append("</td>");
        				
        			} else {
        				html_established.append("<input type=\"text\" name=\"mpr_"+arrayIdAfter.get(counter)+"\" value=\""+getHTMLValue(arrayValueAfter.get(counter))+"\" class=\"inputfield\">");
        		
        			}
        		}
        		html_established.append("</td>");
        	} else {
        		
        	}
        	if (colCounter >= 3) {
        		html_established.append("</tr>");
        	}
        }
        html.append(html_preliminary.toString() + html_established.toString());

        return html.toString();
    }

}
