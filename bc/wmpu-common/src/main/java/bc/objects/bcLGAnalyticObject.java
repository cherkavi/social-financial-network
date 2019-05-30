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

public class bcLGAnalyticObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcLGAnalyticObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String analyticType;
	private String idEntry;
	
	public bcLGAnalyticObject(String pAnalyticType, String pIdEntry) {
		this.analyticType = pAnalyticType;
		this.idEntry = pIdEntry;
		getFeature();
	}
	
	private void getFeature() {

		if ("JUR_PRS".equalsIgnoreCase(this.analyticType)) {
			String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_LG_STAT_CLUB_ALL WHERE id_jur_prs = ?";
			fieldHm = getFeatures2(mySQL, this.idEntry, false);
        }
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getOperationListHTML(String pFindString, String pLGType, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        String mySQL = "";

        ArrayList<bcFeautureParam> pParam = initParamArray();

        if ("JUR_PRS".equalsIgnoreCase(this.analyticType)) {
	        mySQL = 
	        	" SELECT id_lg_record, action_date_frmt, sname_jur_prs_receiver, sname_jur_prs_sender, " +
	        	"        name_lg_type, object_count, id_jur_prs_receiver, id_jur_prs_sender, cd_lg_type "+
	            "   FROM (SELECT ROWNUM rn, id_lg_record, action_date_frmt, " +
	            "        		 DECODE(cd_lg_type, " +
	            "               		'QUESTIONNAIRE', '<font color=\"blue\"><b>'||name_lg_type||'</b></font>', " +
	            "               		'BON_CARD', '<font color=\"green\"><b>'||name_lg_type||'</b></font>', " +
	            "               		'OTHER', '<font color=\"red\"><b>'||name_lg_type||'</b></font>', " +
	            "               		'SAM', '<font color=\"brown\"><b>'||name_lg_type||'</b></font>', " +
	            "               		'GIFT', '<font color=\"gray\"><b>'||name_lg_type||'</b></font>', " +
	            "               		'TERMINAL', '<font color=\"brown\"><b>'||name_lg_type||'</b></font>', " +
	            "               		name_lg_type" +
	            "        		 ) name_lg_type, " +
	            "				 sname_jur_prs_receiver, " +
	        	"                sname_jur_prs_sender, object_count, id_jur_prs_receiver, id_jur_prs_sender, cd_lg_type "+
	            "           FROM (SELECT * " +
	            "                   FROM " + getGeneralDBScheme() + ".vc_lg_club_all "+
	            "                  WHERE (id_jur_prs_receiver = ? " +  
	            "                     OR id_jur_prs_sender = ?) ";
	        pParam.add(new bcFeautureParam("int", this.idEntry));
	        pParam.add(new bcFeautureParam("int", this.idEntry));
	        
	        if (!isEmpty(pFindString)) {
	        	mySQL = mySQL + 
	        		" AND (UPPER(action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
	        		"	   UPPER(sname_jur_prs_receiver) LIKE UPPER('%'||?||'%') OR " +
	        		"	   UPPER(sname_jur_prs_sender) LIKE UPPER('%'||?||'%') OR " +
	        		"	   UPPER(object_count) LIKE UPPER('%'||?||'%')) ";
	        	for (int i=0; i<4; i++) {
	        	    pParam.add(new bcFeautureParam("string", pFindString));
	        	}
	        }
	        if (!isEmpty(pLGType)) {
	        	mySQL = mySQL + " AND cd_lg_type = ? ";
	        	pParam.add(new bcFeautureParam("string", pLGType));
	        }
	        pParam.add(new bcFeautureParam("int", p_end));
	        pParam.add(new bcFeautureParam("int", p_beg));
	        mySQL = mySQL +
	            "                  ORDER BY action_date_frmt desc, sname_jur_prs_receiver, sname_jur_prs_sender, name_lg_type)  " +
	            "          WHERE ROWNUM < ? " +
	            " ) WHERE rn >= ?";
        }
        
        boolean hasLGBonCardPermission = false;
        boolean hasLGQuestionnairePermission = false;
        boolean hasLGTerminalPermission = false;
        boolean hasLGSAMPermission = false;
        boolean hasLGOtherPermission = false;
        boolean hasJurPrsPermission = false;
        
        boolean hasPermission = false;
        String lgHyperlink = "";
        try{
        	if (isEditMenuPermited("LOGISTIC_PARTNERS_CARDS")>=0) {
        		hasLGBonCardPermission = true;
        	}
        	if (isEditMenuPermited("LOGISTIC_PARTNERS_QUEST")>=0) {
        		hasLGQuestionnairePermission = true;
        	}
        	if (isEditMenuPermited("LOGISTIC_PARTNERS_TERMINALS")>=0) {
        		hasLGTerminalPermission = true;
        	}
        	if (isEditMenuPermited("LOGISTIC_PARTNERS_SAMS")>=0) {
        		hasLGSAMPermission = true;
        	}
        	if (isEditMenuPermited("LOGISTIC_PARTNERS_OTHERS")>=0) {
        		hasLGOtherPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasJurPrsPermission = true;
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
            for (int i=1; i <= colCount-3; i++) {
               html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	String lgType = rset.getString("CD_LG_TYPE");
            	if ("BON_CARD".equalsIgnoreCase(lgType)) {
            		hasPermission = hasLGBonCardPermission;
            		lgHyperlink = "../crm/logistic/partners/cardspecs.jsp?id=" + rset.getString("ID_LG_RECORD");
            	} else if ("QUESTIONNAIRE".equalsIgnoreCase(lgType)) {
            		hasPermission = hasLGQuestionnairePermission;
            		lgHyperlink = "../crm/logistic/partners/questspecs.jsp?id=" + rset.getString("ID_LG_RECORD");
                } else if ("TERMINAL".equalsIgnoreCase(lgType)) {
                	hasPermission = hasLGTerminalPermission;
                	lgHyperlink = "../crm/logistic/partners/terminalspecs.jsp?id=" + rset.getString("ID_LG_RECORD");
                } else if ("SAM".equalsIgnoreCase(lgType)) {
                	hasPermission = hasLGSAMPermission;
                	lgHyperlink = "../crm/logistic/partners/samspecs.jsp?id=" + rset.getString("ID_LG_RECORD");
                } else if ("OTHER".equalsIgnoreCase(lgType)) {
                	hasPermission = hasLGOtherPermission;
                	lgHyperlink = "../crm/logistic/partners/otherspecs.jsp?id=" + rset.getString("ID_LG_RECORD");
                } else {
                	hasPermission = false;
                	lgHyperlink = "";
                }
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-3; i++) {
          	  		if ("ID_LG_RECORD".equalsIgnoreCase(mtd.getColumnName(i)) && hasPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), lgHyperlink, "", ""));
	          	  	} else if ("SNAME_JUR_PRS_RECEIVER".equalsIgnoreCase(mtd.getColumnName(i)) && hasJurPrsPermission) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id=" + rset.getString("ID_JUR_PRS_RECEIVER"), "", ""));
	      	  		} else if ("SNAME_JUR_PRS_SENDER".equalsIgnoreCase(mtd.getColumnName(i)) && hasJurPrsPermission) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id=" + rset.getString("ID_JUR_PRS_SENDER"), "", ""));
	      	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
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
}
