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


public class bcQuestionnaireImportSettingObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcQuestionnaireImportSettingObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
    private String idQuestSetting;
    
    public bcQuestionnaireImportSettingObject(String pIdQuestSetting) {
    	this.idQuestSetting = pIdQuestSetting;
		this.getFeature();
    }
	
	public void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_QUEST_IMPORT_SET_CLUB_ALL WHERE id_import_setting = ?";
		fieldHm = getFeatures2(featureSelect, this.idQuestSetting, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getPatternsHTML(String pHasJoin, String pPatternType, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        
        Statement st_code = null;
        Statement st_state = null;
        ArrayList<String> array_value=new ArrayList<String>();
        ArrayList<String> array_code=new ArrayList<String>();
        String option_code = "";
        int rowCnt = 0;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_pattern, name_pattern, name_pattern_type, name_dispatch_type, " +
			"        name_pattern_status, begin_action_date, end_action_date, name_ds_cl_profile, " +
			"        has_join, new_state_record, new_state_record_tsl " +
        	"   FROM (SELECT ROWNUM rn, id_pattern, name_pattern, name_pattern_type, name_dispatch_type, " +
			"                name_pattern_status, begin_action_date, end_action_date, name_ds_cl_profile, " +
			"                has_join, new_state_record, new_state_record_tsl " +
        	"          FROM (SELECT p.id_cl_pattern id_pattern, p.name_cl_pattern name_pattern, p.name_pattern_type, p.name_dispatch_type, " +
			"                	    p.name_pattern_status, p.begin_action_date, p.end_action_date, p.name_ds_cl_profile, p.cd_pattern_type, " +
        	"                       DECODE(b.id_cl_pattern, NULL, 'N', 'Y') has_join," +
        	"                       b.new_state_record, b.new_state_record_tsl " + 
        	"                  FROM (SELECT a.id_cl_pattern, a.name_cl_pattern, a.name_pattern_type, a.name_dispatch_type," +
			"                 		  		DECODE(a.cd_pattern_status, " +
        	"                        				'ACTIVE', '<font color=\"black\">'||name_pattern_status||'</font>', " +
        	"                        				'SUSPENDED', '<font color=\"brown\"><b>'||name_pattern_status||'</b></font>', " +
        	"                        				'CANCEL', '<font color=\"red\"><b>'||name_pattern_status||'</b></font>', " +
        	"                        				'FINISH', '<font color=\"green\"><b>'||name_pattern_status||'</b></font>', " +
        	"                        		a.name_pattern_status) name_pattern_status, " +
	  		" 						 		a.begin_action_date_frmt begin_action_date," +
			"                        		a.end_action_date_frmt end_action_date, a.name_ds_cl_profile, a.cd_pattern_type " + 
        	"						   FROM " + getGeneralDBScheme() + ".vc_ds_cl_pattern_club_all a" + 
        	" 				        ) p " +
        	"                  LEFT JOIN " +
        	"                       (SELECT id_cl_pattern, new_state_record, new_state_record_tsl " +
        	"                          FROM " + getGeneralDBScheme() + ".vc_quest_import_set_pattern" +
        	"                         WHERE id_import_setting = ? " +  
        	"                       ) b " + 
        	"                   ON (p.id_cl_pattern = b.id_cl_pattern)" +
        	"                 ORDER BY p.name_cl_pattern) " +
        	"         WHERE 1=1 ";
        pParam.add(new bcFeautureParam("int", this.idQuestSetting));
        
       	if (!(pHasJoin== null || "".equalsIgnoreCase(pHasJoin))) {
           	mySQL = mySQL + " AND has_join = ? ";
           	pParam.add(new bcFeautureParam("string", pHasJoin));
        }
       	if (!(pPatternType== null || "".equalsIgnoreCase(pPatternType))) {
           	mySQL = mySQL + " AND cd_pattern_type = ? ";
           	pParam.add(new bcFeautureParam("string", pPatternType));
        }
       	pParam.add(new bcFeautureParam("int", p_end));
       	pParam.add(new bcFeautureParam("int", p_beg));
       	mySQL = mySQL +
         	"          AND ROWNUM < ? " + 
            " ) WHERE rn >= ?";

        
        boolean hasPatternPermission = false;
        boolean hasEditPermission = false;
        
        String myFont = "";
        String myBGColor = "";
        
        try{
   	 	 	if (isEditMenuPermited("DISPATCH_PATTERNS_CLIENT") >0) {
   	 	 		hasPatternPermission = true;
   	 	 	}

   	 	 	if (isEditPermited("CARDS_QUEST_SETTINGS_PATTERNS") >0) {
   	 	 		hasEditPermission = true;
   	 	 	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
            
        	st_code = con.createStatement();
        	ResultSet rset_code = st_code.executeQuery("SELECT NULL lookup_code FROM dual UNION ALL SELECT lookup_code FROM " + getGeneralDBScheme() + ".vc_lookup_values WHERE UPPER(name_lookup_type)=UPPER('SEND_MESSAGE_STATE')");
        	
        	while (rset_code.next()) {
        		String selectedCode = rset_code.getString("lookup_code");
        		if (selectedCode == null) {
        			selectedCode = "";
        		}
        		option_code = "";
        		st_state = con.createStatement();
                ResultSet rset_state = st_state.executeQuery("SELECT NULL lookup_code, NULL meaning FROM dual UNION ALL SELECT lookup_code, meaning FROM " + getGeneralDBScheme() + ".vc_lookup_values WHERE UPPER(name_lookup_type)=UPPER('SEND_MESSAGE_STATE')");
                while (rset_state.next())
                {
                	String looupCode = rset_state.getString("lookup_code");
                	looupCode = (looupCode==null)?"":looupCode;
                	String meaning = rset_state.getString("meaning");
                	meaning = (meaning==null)?"":meaning;
                	option_code = option_code + "<option value=\""+looupCode+"\"";
                    if (selectedCode.equalsIgnoreCase(looupCode)) {
                    	option_code = option_code + " SELECTED";
                    }
                    option_code = option_code + ">"+meaning+"</option>";
                }
                //LOGGER.debug(option_code);
                array_code.add(selectedCode);
                array_value.add(option_code);
                st_state.close();
        	}
        	st_code.close();
        	
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
       	catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
        finally {
            try {
                if (st_code!=null) st_code.close();
                if (st_state!=null) st_state.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        try{
   	 	 	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();

            int colCount = mtd.getColumnCount();

            if (hasEditPermission) {
           	 	html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/cards/quest_settingsupdate.jsp\">\n");
           	 	html.append("<input type=\"hidden\" name=\"type\" value=\"pattern\">\n");
        	 	html.append("<input type=\"hidden\" name=\"action\" value=\"set\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
           	 	html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idQuestSetting + "\">\n");
            }
            
            html.append(getBottomFrameTable());
            html.append("<tr>");

            for (int i=1; i <= colCount-3; i++) {
            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th align=\"center\"> "+ 
           			messageXML.getfieldTransl("has_join", false) +
           			"<br><input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this)\"></th>\n");
            	html.append("<th align=\"center\"> "+ 
               			messageXML.getfieldTransl("state_record", false) +
               			"</th>\n");
            }
            html.append("</tr></thead><tbody>");
            if (hasEditPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\"11\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/cards/quest_settingsupdate.jsp"));
             	html.append("</td>");
             	html.append("</tr>");
             }
            while (rset.next())
            {
                rowCnt = rowCnt + 1;
            	String idPattern=rset.getString("ID_PATTERN");
                
                
                if ("Y".equalsIgnoreCase(rset.getString("HAS_JOIN"))){
                	myFont = "<b>";
                	myBGColor = selectedBackGroundStyle;
                } else {
                	myFont = "";
                	myBGColor = "";
                }
                html.append("<tr>");
                for (int i=1; i<=colCount-3; i++) {
                	if ("ID_PATTERN".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasPatternPermission) {
                    		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/patterns/client_patternspecs.jsp?id=" + rset.getString(i), myFont, myBGColor));
                    	} else {
                    		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBGColor));
                    	}
                	} else {
                		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBGColor));
                	}
                }
                
                if (hasEditPermission) {
                	html.append("<td " + myBGColor + " align=\"center\">");
                	html.append("<input type=\"hidden\" name=\"id_pattern_"+rowCnt+"\" value=\""+idPattern+"\">");
                	if ("Y".equalsIgnoreCase(rset.getString("HAS_JOIN"))){
                		html.append("<INPUT type=hidden  value=\"Y\" name=\"prv_has_join_"+rowCnt+"\">");
                    	html.append("<INPUT  type=\"checkbox\" name=\"has_join_"+rowCnt+"\" id=\"has_join_"+rowCnt+"\" checked onclick=\"return CheckCB(this);\"></td>\n");
                	} else {
                		html.append("<INPUT type=hidden  value=\"N\" name=\"prv_has_join_"+rowCnt+"\">");
                    	html.append("<INPUT  type=\"checkbox\" name=\"has_join_"+rowCnt+"\" id=\"has_join_"+rowCnt+"\" onclick=\"return CheckCB(this);\"></td>\n");
                	}
                	html.append("</td>");
                }
                if (hasEditPermission) {
                	String newState = rset.getString("NEW_STATE_RECORD");
                	newState = (newState==null)?"":newState;
                    html.append("<td>");
                    html.append("<input type=\"hidden\" name=\"row_number"+rowCnt+"\" value=\""+rset.getString("ID_PATTERN")+"\">");
                    html.append("<select name=\"new_state_"+rowCnt+"\" class=\"inputfield\">");
                    for(int counter=0;counter<array_code.size();counter++){
                    	if(array_code.get(counter).equalsIgnoreCase(newState)){
                    		html.append(array_value.get(counter));
                    	}
                    }
                    html.append("</select>");
                    html.append("</td>\n");
                } else {
                    html.append("<td>"+ getValue2(rset.getString("NEW_STATE_RECORD_TSL")) +"</td>\n");
                }
                html.append("</tr>\n");
            }
            if (hasEditPermission) {
             	html.append("<tr>\n");
             	html.append("<td colspan=\"11\" align=\"center\">\n");
             	html.append("<input type=\"hidden\" name=\"row_count\" value=\""+rowCnt+"\">");
             	html.append(getSubmitButtonAjax("../crm/cards/quest_settingsupdate.jsp"));
             	html.append("</td>\n");
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

}
