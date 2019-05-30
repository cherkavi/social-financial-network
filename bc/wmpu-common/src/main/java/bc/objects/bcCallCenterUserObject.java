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

public class bcCallCenterUserObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcCallCenterUserObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idUser;
	
	public bcCallCenterUserObject(String pIdUser) {
		this.idUser = pIdUser;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CC_USERS_ALL WHERE id_user = ?";
		fieldHm = getFeatures2(featureSelect, this.idUser, false);
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
        	"                  WHERE id_assigned_user = ? ";
		pParam.add(new bcFeautureParam("int", this.idUser));
		
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
        	"          WHERE ROWNUM < ? " + 
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

    public String getMessagesHTML(String pFind, String pMessageType, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, id_ds_message, name_ds_message_type, name_ds_message_state, " +
        	"		 event_date_frmt, name_ds_message_owner, name_ds_recepient, " +
        	"        basis_for_operation, title_ds_message, begin_action_date_frmt, " +
        	"		 end_action_date_frmt, is_archive_tsl, id_user, " +
        	"		 id_contact_prs, id_nat_prs, cd_ds_message_type, cd_ds_message_owner_type " +
        	"	FROM (SELECT ROWNUM rn, id_ds_message, name_ds_message_type, name_ds_message_state, " +
        	"				 event_date_frmt, name_ds_message_owner, name_ds_recepient, " +
        	"                basis_for_operation, title_ds_message, begin_action_date_frmt, " +
        	"				 end_action_date_frmt, is_archive_tsl, cd_ds_message_type, cd_ds_message_owner_type, " +
        	"                id_user, id_contact_prs, id_nat_prs " +
        	"	        FROM (SELECT id_ds_message, " +
        	"                        name_ds_message_owner, name_ds_recepient, " +
			"               		 DECODE(cd_ds_message_type," +
        	"                      			'SMS', '<b><font color=\"green\">'||name_ds_message_type||'</font></b>'," +
        	"                      			'EMAIL', '<b><font color=\"darkgray\">'||name_ds_message_type||'</font></b>'," +
        	"                      			'CARDS', '<b><font color=\"blue\">'||name_ds_message_type||'</font></b>'," +
        	"                      			'CONTACT_PRS', '<b><font color=\"brown\">'||name_ds_message_type||'</font></b>'," +
        	"                      			'OFFICE', '<b><font color=\"brown\">'||name_ds_message_type||'</font></b>'," +
        	"                      			name_ds_message_type" +
        	"               		 ) name_ds_message_type, " +
        	"               		 DECODE(cd_ds_message_state, " +
  		  	"                      			'PREPARED', '<b><font color=\"black\">'||name_ds_message_state||'</font></b>', " +
			"                      			'NEW', '<font color=\"black\">'||name_ds_message_state||'</font>', " +
			"                      			'EXECUTED', '<font color=\"green\">'||name_ds_message_state||'</font>', " +
			"                      			'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||name_ds_message_state||'</font>', " +
			"                      			'ERROR', '<b><font color=\"red\">'||name_ds_message_state||'</font></b>', " +
			"                      			'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||name_ds_message_state||'</font></b>', " +
			"                      			'CARRIED_OUT', '<font color=\"gray\">'||name_ds_message_state||'</font>', " +
			"                      			'CANCELED', '<font color=\"blue\">'||name_ds_message_state||'</font>', " +
			"                      			name_ds_message_state" +
			"						 ) name_ds_message_state, " +
        	"						 event_date_frmt, title_ds_message, basis_for_operation, " +
        	"						 begin_action_date_frmt, end_action_date_frmt, " +
        	"  			     		 DECODE(is_archive, " +
			"                  				'Y', '<b><font color=\"green\">'||is_archive_tsl||'</font></b>', " +
			"                   			'<b><font color=\"red\">'||is_archive_tsl||'</font></b>' " +
			"                		 ) is_archive_tsl, " +
        	"						 cd_ds_message_type, cd_ds_message_owner_type, " +
        	"                        id_user, id_contact_prs, id_nat_prs " +
        	"                   FROM " + getGeneralDBScheme()+".vc_ds_messages_club_all " +
        	"                  WHERE id_user = ? ";
    	pParam.add(new bcFeautureParam("int", this.idUser));
    	
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(id_ds_message) LIKE UPPER('%'||?||'%') " +
    			"   OR UPPER(name_ds_message_owner) LIKE UPPER('%'||?||'%') " +
    			"   OR UPPER(name_ds_recepient) LIKE UPPER('%'||?||'%') " +
				"   OR UPPER(event_date_frmt) LIKE UPPER('%'||?||'%') " +
				"   OR UPPER(title_ds_message) LIKE UPPER('%'||?||'%') " +
				"   OR UPPER(basis_for_operation) LIKE UPPER('%'||?||'%') " +
				"   OR UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') " +
				"   OR UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<8; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	if (!isEmpty(pMessageType)) {
    		mySQL = mySQL + " AND cd_ds_message_type = ? ";
    		pParam.add(new bcFeautureParam("string", pMessageType));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                 ORDER BY begin_action_date DESC) " +
        	"          WHERE ROWNUM < ? " + 
        	"        ) " +
        	"  WHERE rn >= ?";
    	  StringBuilder html = new StringBuilder();
	      PreparedStatement st = null;
	      Connection con = null;
	      boolean hasOfficeMessagePermission = false;
	      boolean hasEmailMessagePermission = false;
	      boolean hasNatPrsPermission = false;
	      boolean hasContactPrsPermission = false;
	      boolean hasUserPermission = false;
	      try{
	    	  if (isEditMenuPermited("DISPATCH_MESSAGES_OFFICE")>=0) {
	    		  hasOfficeMessagePermission = true;
	    	  }
	    	  if (isEditMenuPermited("DISPATCH_MESSAGES_EMAIL")>=0) {
	    		  hasEmailMessagePermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
	    		  hasNatPrsPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLIENTS_CONTACT_PRS")>=0) {
	    		  hasContactPrsPermission = true;
	    	  }
	    	  if (isEditMenuPermited("SECURITY_USERS")>=0) {
	    		  hasUserPermission = true;
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

	          for (int i=1; i <= colCount-6; i++) {
	        	  html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
	          }  
              html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>\n");
			      for (int i=1; i <= colCount-6; i++) {
			    	  if ("ID_DS_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i)) && 
			    			  "OFFICE".equalsIgnoreCase(rset.getString("CD_DS_MESSAGE_TYPE")) &&
					    	  hasOfficeMessagePermission) {
			    		  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/office_messagesspecs.jsp?id=" + rset.getString(i), "", ""));
			    	  } else if ("ID_DS_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i)) &&  
			    			  "EMAIL".equalsIgnoreCase(rset.getString("CD_DS_MESSAGE_TYPE")) &&
						      hasEmailMessagePermission) {
			    		  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + rset.getString(i), "", ""));
			    	  } else if ("NAME_DS_MESSAGE_OWNER".equalsIgnoreCase(mtd.getColumnName(i)) &&  
			    			  "CLIENT".equalsIgnoreCase(rset.getString("CD_DS_MESSAGE_OWNER_TYPE")) &&
						      hasNatPrsPermission) {
			    		  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id=" + rset.getString("ID_NAT_PRS"), "", ""));
			    	  } else if ("NAME_DS_MESSAGE_OWNER".equalsIgnoreCase(mtd.getColumnName(i)) &&   
			    			  "PARTNER".equalsIgnoreCase(rset.getString("CD_DS_MESSAGE_OWNER_TYPE")) &&
						      hasContactPrsPermission) {
			    		  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/contact_prsspecs.jsp?id=" + rset.getString("ID_CONTACT_PRS"), "", ""));
			    	  } else if ("NAME_DS_MESSAGE_OWNER".equalsIgnoreCase(mtd.getColumnName(i)) &&    
			    			  "SYSTEM".equalsIgnoreCase(rset.getString("CD_DS_MESSAGE_OWNER_TYPE")) &&
						      hasUserPermission) {
			    		  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/security/userspecs.jsp?id=" + rset.getString("ID_USER"), "", ""));
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
