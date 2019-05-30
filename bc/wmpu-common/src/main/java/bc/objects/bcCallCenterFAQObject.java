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

public class bcCallCenterFAQObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcCallCenterFAQObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idFAQ;
	
	public bcCallCenterFAQObject(String pIdFAQ) {
		this.idFAQ = pIdFAQ;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CC_FAQ_CLUB_ALL WHERE id_cc_faq = ?";
		fieldHm = getFeatures2(featureSelect, this.idFAQ, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
    
	public String getCallCenterQuestionsHTML(String pFind, String pContactType, String pQuestionType, String pQuestionStatus, String p_beg, String p_end) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
		String mySQL = 
    		" SELECT rn, id_cc_question, cc_contact_type, due_date, title, cc_question_type, " +
        	"        cc_question_status, cc_question_important, cc_question_urgent," +
        	"        name_user " +
        	"	FROM (SELECT ROWNUM rn, id_cc_question, " +
        	"                DECODE(cd_cc_contact_type, " +
       		"                       'SYSTEM', '<font color=\"red\"><b>'||name_cc_contact_type||'</b></font>', " +
       		"                       'PHONE', '<font color=\"black\"><b>'||name_cc_contact_type||'</b></font>', " +
       		"                       'EMAIL', '<font color=\"green\"><b>'||name_cc_contact_type||'</b></font>', " +
       		"                       'SITE', '<font color=\"blue\"><b>'||name_cc_contact_type||'</b></font>', " +
       		"                       'PERSONAL_CABINET', '<font color=\"gray\"><b>'||name_cc_contact_type||'</b></font>', " +
       		"                       name_cc_contact_type" +
       		"                ) cc_contact_type, " +
        	"                due_date_frmt due_date, " +
        	"                title, " +
        	"                DECODE(cd_cc_question_type, " +
       		"                       'OTHER', '<font color=\"red\"><b>'||name_cc_question_type||'</b></font>', " +
       		"                       name_cc_question_type" +
       		"                ) cc_question_type, " +
       		"                DECODE(cd_cc_question_status, " +
       		"                       'POSTPONED', '<font color=\"red\"><b>'||name_cc_question_status||'</b></font>', " +
       		"                       'FINISHED', '<font color=\"green\"><b>'||name_cc_question_status||'</b></font>', " +
       		"                       'BEGUN', '<font color=\"black\"><b>'||name_cc_question_status||'</b></font>', " +
       		"                       'IN_PROCESS', '<font color=\"blue\"><b>'||name_cc_question_status||'</b></font>', " +
       		"                       name_cc_question_status" +
       		"                ) cc_question_status, " +
       		"                DECODE(cd_cc_question_important, " +
       		"                       'LOW', '<font color=\"green\"><b>'||name_cc_question_important||'</b></font>', " +
       		"                       'AVERAGE', '<font color=\"blue\"><b>'||name_cc_question_important||'</b></font>', " +
       		"                       'HIGH', '<font color=\"red\"><b>'||name_cc_question_important||'</b></font>', " +
       		"                       'VERY_HIGH', '<font color=\"red\"><b>'||name_cc_question_important||'</b></font>', " +
       		"                       name_cc_question_important" +
       		"                ) cc_question_important, " +
       		"                DECODE(cd_cc_question_urgent, " +
       		"                       'LOW', '<font color=\"green\"><b>'||name_cc_question_urgent||'</b></font>', " +
       		"                       'AVERAGE', '<font color=\"blue\"><b>'||name_cc_question_urgent||'</b></font>', " +
       		"                       'HIGH', '<font color=\"red\"><b>'||name_cc_question_urgent||'</b></font>', " +
       		"                       'VERY_HIGH', '<font color=\"red\"><b>'||name_cc_question_urgent||'</b></font>', " +
       		"                       name_cc_question_urgent" +
       		"                ) cc_question_urgent," +
        	"                name_user " +
        	"	        FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".vc_cc_questions_club_all " +
        	"                  WHERE id_cc_faq = ?";
		pParam.add(new bcFeautureParam("int", this.idFAQ));
		
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (UPPER(title) LIKE UPPER('%'||?||'%') " +
    			"   OR UPPER(due_date_frmt) LIKE UPPER('%'||?||'%') " +
				"   OR UPPER(description) LIKE UPPER('%'||?||'%') " +
				"   OR UPPER(note) LIKE UPPER('%'||?||'%') " +
				"   OR UPPER(resolution) LIKE UPPER('%'||?||'%') " +
				"   OR UPPER(solution) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	if (!isEmpty(pContactType)) {
    		mySQL = mySQL + " AND cd_cc_contact_type = ? ";
    		pParam.add(new bcFeautureParam("string", pContactType));
    	}
    	if (!isEmpty(pQuestionType)) {
    		mySQL = mySQL + " AND cd_cc_question_type = ? ";
    		pParam.add(new bcFeautureParam("string", pQuestionType));
    	}
    	if (!isEmpty(pQuestionStatus)) {
    		mySQL = mySQL + " AND cd_cc_question_status = ? ";
    		pParam.add(new bcFeautureParam("string", pQuestionStatus));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                 ORDER BY due_date DESC) " +
        	"          WHERE ROWNUM < ?" + 
        	"        ) " +
        	"  WHERE rn >= ?";
    	  StringBuilder html = new StringBuilder();
	      PreparedStatement st = null;
	      Connection con = null;
	      boolean hasQuestionPermission = false; 
	      try{
	    	  if (isEditMenuPermited("CALL_CENTER_QUESTIONS")>=0) {
	    		  hasQuestionPermission = true;
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

	          for (int i=1; i <= colCount; i++) {
	        	  html.append(getBottomFrameTableTH(call_centerXML, mtd.getColumnName(i)));
	          }  
              html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>\n");
			      for (int i=1; i <= colCount; i++) {
			    	  if ("ID_CC_QUESTION".equalsIgnoreCase(mtd.getColumnName(i)) && hasQuestionPermission) {
			    		  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/call_center/questionspecs.jsp?id=" + rset.getString(i), "", ""));
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
	                if (st!=null) {
						st.close();
					}
	            } catch (SQLException w) {w.toString();}
	            try {
	                if (con!=null) {
						con.close();
					}
	            } catch (SQLException w) {w.toString();}
	            Connector.closeConnection(con);
	        } // finally
	      return html.toString();
	  } // getClubActionGiftsHTML
}
