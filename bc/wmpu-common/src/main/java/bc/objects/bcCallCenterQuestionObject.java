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

public class bcCallCenterQuestionObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcCallCenterQuestionObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idQuestion;
	
	public bcCallCenterQuestionObject(String pIdQuestion) {
		this.idQuestion = pIdQuestion;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CC_QUESTIONS_CLUB_ALL WHERE id_cc_question = ?";
		fieldHm = getFeatures2(featureSelect, this.idQuestion, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

	public String getActivitiesHTML(String pFind, String p_beg, String p_end) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
	    String mySQL = 
	    	  	" SELECT id_cc_activity, name_user, begin_date, end_date, description " +
	            "   FROM (SELECT ROWNUM rn, id_cc_activity, name_user, begin_date, end_date, description " +
	            "           FROM (SELECT id_cc_activity, name_user, begin_date_frmt begin_date, " +
	            "                        end_date_frmt end_date, description " +
	            "                   FROM " + getGeneralDBScheme() + ".vc_cc_question_activities_all " +
	            "                  WHERE id_cc_question = ? ";
	    pParam.add(new bcFeautureParam("int", this.idQuestion));
	    
		if (!isEmpty(pFind)) {
		   	mySQL = mySQL + 
				" AND (UPPER(id_cc_activity) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(name_user) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(begin_date_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(end_date_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(description) LIKE UPPER('%'||?||'%'))";
		   	for (int i=0; i<5; i++) {
		   	    pParam.add(new bcFeautureParam("string", pFind));
		   	}
		}

		pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
		mySQL = mySQL +
	         	"                  ORDER BY begin_date)" +
	            "          WHERE ROWNUM < ? " + 
	        	" ) WHERE rn >= ?";
	    StringBuilder html = new StringBuilder();
	    PreparedStatement st = null;
	    Connection con = null;
	    boolean hasEditPermission = false; 
	    try{
	    	  if (isEditPermited("CALL_CENTER_QUESTIONS_ACTIVITIES")>0) {
	    		  hasEditPermission = true;
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
              if (hasEditPermission) {
	           	  html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");  
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>\n");
			      for (int i=1; i <= colCount; i++) {
			    	  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
			      }
	              if (hasEditPermission) {
            		  String myHyperLink = "../crm/call_center/questionupdate.jsp?type=activity&id="+this.idQuestion+"&id_activity="+rset.getString(1);
            		  String delHyperLink = "../crm/call_center/questionupdate.jsp?type=activity&id="+this.idQuestion+"&id_activity="+rset.getString(1)+"&process=yes&action=remove";
            		  html.append(getDeleteButtonHTML(delHyperLink, call_centerXML.getfieldTransl("h_delete_activity", false), rset.getString("ID_CC_ACTIVITY")));
            		  html.append(getEditButtonHTML(myHyperLink));
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

	public String getHistoryHTML(String pFind, String p_beg, String p_end) {
	
		ArrayList<bcFeautureParam> pParam = initParamArray();
		
		String mySQL = 
	      	" SELECT id_history_record, name_user_who_changed, change_date," +
	        "        change_description, old_value, new_value " +
	        "   FROM (SELECT ROWNUM rn, id_history_record, name_user_who_changed, change_date," +
	        "                change_description, old_value, new_value " +
	        "           FROM (SELECT id_history_record, name_user_who_changed, change_date_frmt change_date, " +
	        "                        change_description, old_value, new_value " +
	        "                   FROM " + getGeneralDBScheme() + ".vc_cc_questions_h_all " +
	        "                  WHERE id_cc_question = ? ";
		pParam.add(new bcFeautureParam("int", this.idQuestion));
		
		if (!isEmpty(pFind)) {
		   	mySQL = mySQL + 
				" AND (UPPER(id_history_record) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(name_user_who_changed) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(change_date_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(change_description) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(old_value) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(new_value) LIKE UPPER('%'||?||'%'))";
		   	for (int i=0; i<6; i++) {
		   	    pParam.add(new bcFeautureParam("string", pFind));
		   	}
		}
		pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
		mySQL = mySQL +
	          	"                  ORDER BY change_date)" +
	            "          WHERE ROWNUM < ? " + 
	        	" ) WHERE rn >= ?";
	    StringBuilder html = new StringBuilder();
	    PreparedStatement st = null;
	    Connection con = null;
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
	          for (int i=1; i <= colCount; i++) {
	        	  html.append(getBottomFrameTableTH(call_centerXML, mtd.getColumnName(i)));
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	              for (int i=1; i <= colCount; i++) {
	            	  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
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
	  }

    public String getMessagesHTML(String pFind, String pTypeMessage, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
    	String mySQL = 
	    	"SELECT rn, id_message, type_message, recepient, title_message, " + 
	    	"		sendings_quantity, error_sendings_quantity, event_date_frmt, " +
	    	"       state_record_tsl, is_archive_tsl, type_message2, " + 
	    	"		id_nat_prs " +
	    	"  FROM (SELECT ROWNUM rn, id_message, type_message, recepient, title_message, " + 
	    	"				sendings_quantity, error_sendings_quantity, event_date_frmt, " +
	    	"               state_record_tsl, is_archive_tsl, type_message2, " + 
	    	"				id_nat_prs " +
	    	"          FROM (SELECT id_message, type_message_tsl type_message, recepient, " +
	    	"                       title_message, " + 
	    	"						sendings_quantity, error_sendings_quantity, event_date_frmt, " +
	    	"               		DECODE(state_record, " +
	  		"                      			'PREPARED', '<b><font color=\"black\">'||state_record_tsl||'</font></b>', " +
			"                      			'NEW', '<font color=\"black\">'||state_record_tsl||'</font>', " +
			"                      			'EXECUTED', '<font color=\"green\">'||state_record_tsl||'</font>', " +
			"                      			'EXECUTED_PARTIALLY', '<font color=\"darkgreen\">'||state_record_tsl||'</font>', " +
			"                      			'ERROR', '<b><font color=\"red\">'||state_record_tsl||'</font></b>', " +
			"                      			'CARRIED_OUT_PARTIALLY', '<b><font color=\"darkgray\">'||state_record_tsl||'</font></b>', " +
			"                      			'CARRIED_OUT', '<font color=\"gray\">'||state_record_tsl||'</font>', " +
			"                      			'CANCELED', '<font color=\"blue\">'||state_record_tsl||'</font>', " +
			"                      			state_record_tsl) state_record_tsl, " +
			"  				       	DECODE(is_archive, " +
			"                             	'Y', '<b><font color=\"red\">'||is_archive_tsl||'</font></b>', " +
			"                             	is_archive_tsl) is_archive_tsl, " +
	  		"                       type_message type_message2, id_nat_prs " +
	    	"                  FROM " + getGeneralDBScheme() + ".vc_ds_pattern_mess_club_all " +
	    	"                 WHERE id_cc_question = ? ";
	    pParam.add(new bcFeautureParam("int", this.idQuestion));
	    	
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + 
	   			" AND (TO_CHAR(id_message) LIKE UPPER('%'||?||'%') OR " +
	   			"      UPPER(recepient) LIKE UPPER('%'||?||'%') OR " +
	   			"      UPPER(title_message) LIKE UPPER('%'||?||'%') OR " +
	   			"      UPPER(event_date_frmt) LIKE UPPER('%'||?||'%'))";
	    	for (int i=0; i<4; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
	    if (!isEmpty(pTypeMessage)) {
	    	mySQL = mySQL + " AND type_message = ? ";
	    	pParam.add(new bcFeautureParam("string", pTypeMessage));
	    }
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL + 
	    	"                 ORDER BY event_date desc, id_message) "+
	    	"         WHERE ROWNUM < ? " + 
	    	" ) WHERE rn >= ?";
	       
	    StringBuilder html = new StringBuilder();
	    Connection con = null;
	    PreparedStatement st = null; 
	       
	    boolean hasSMSPermission = false;
	    boolean hasEmailPermission = false;
	    boolean hasOfficePermission = false;
	    //
	    try{
	        	if (isEditMenuPermited("DISPATCH_MESSAGES_SMS")>=0) {
	        		hasSMSPermission = true;
	        	}
	        	if (isEditMenuPermited("DISPATCH_MESSAGES_EMAIL")>=0) {
	        		hasEmailPermission = true;
	        	}
	        	if (isEditMenuPermited("DISPATCH_MESSAGES_OFFICE")>=0) {
	        		hasOfficePermission = true;
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
	            	html.append(getBottomFrameTableTH(messageXML, mtd.getColumnName(i)));
	            }
	            html.append("</tr></thead><tbody>");
	            while (rset.next())
	            {
	       		 	html.append("<tr>\n");
	            	for (int i=1; i<=colCount-2; i++) {
	            		if ("ID_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i))) {
	            			if (hasSMSPermission && "SMS".equalsIgnoreCase(rset.getString("TYPE_MESSAGE2"))) {
	            				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/smsspecs.jsp?id="+rset.getString(i), "", ""));
	                		} else if (hasEmailPermission && "EMAIL".equalsIgnoreCase(rset.getString("TYPE_MESSAGE2"))) {
	                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/email_messagesspecs.jsp?id="+rset.getString(i), "", ""));
	                		} else if (hasOfficePermission && "OFFICE".equalsIgnoreCase(rset.getString("TYPE_MESSAGE2"))) {
	                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/dispatch/messages/office_messagesspecs.jsp?id="+rset.getString(i), "", ""));
	                		} else {
	                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	                		}
	            		} else if ("TEXT_MESSAGE".equalsIgnoreCase(mtd.getColumnName(i))) {
	            			html.append(getBottomFrameTableTD(mtd.getColumnName(i), getHTMLValue(rset.getString(i)), "", "", ""));
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
	    }//getNatPersonMessagesHTML

	  public String getInquirerLineHTML(boolean pHasEditPermission, String pAnswerType, String p_beg, String p_end) {
	      
		  ArrayList<bcFeautureParam> pParam = initParamArray();
		  String mySQL = 
	    	  	" SELECT id_cc_question_inquirer_line, id_cc_question, id_cc_inquirer_line," +
	            "        question_cc_inquirer_line, desc_cc_inquirer_line, ";
	      if (pHasEditPermission) {
	    	  mySQL = mySQL + 
	            "        " + getGeneralDBScheme() + ".GET_CC_INQUIRER_LINE_OPTIONS(?,'inquirer_line',id_cc_inquirer_line,values_cc_inquirer_line,'Y') values_cc_inquirer_line_html ";
	      } else {
	    	  mySQL = mySQL + 
	            "        " + getGeneralDBScheme() + ".GET_CC_INQUIRER_LINE_OPTIONS(?,'inquirer_line',id_cc_inquirer_line,values_cc_inquirer_line,'N') values_cc_inquirer_line_html ";
	    	  //mySQL = mySQL + " '<input type=\"text\" name=\"inquirer_line__'||TO_CHAR(id_cc_inquirer_line)||'\" size=\"20\" value=\"'||values_cc_inquirer_line||'\" readonly class=\"inputfield-ro\">' values_cc_inquirer_line_html";
	      }
	      pParam.add(new bcFeautureParam("string", pAnswerType));
	      
	      mySQL = mySQL + 
	            "   FROM (SELECT ROWNUM rn, id_cc_question_inquirer_line, id_cc_question, id_cc_inquirer_line," +
	            "                question_cc_inquirer_line, desc_cc_inquirer_line, values_cc_inquirer_line " +
	            "           FROM (SELECT DECODE(a.id_cc_question_inquirer_line, NULL, 0, a.id_cc_question_inquirer_line) id_cc_question_inquirer_line,  " +
	            "                        decode(a.id_cc_question, NULL, " + this.idQuestion + ", a.id_cc_question) id_cc_question," +
	            "                        decode(a.id_cc_inquirer_line, NULL, i.id_cc_inquirer_line, a.id_cc_inquirer_line) id_cc_inquirer_line,  " +
	            "                        decode(a.question_cc_inquirer_line, NULL, i.question_cc_inquirer_line, a.question_cc_inquirer_line) question_cc_inquirer_line, " +
	            "                        decode(a.desc_cc_inquirer_line, NULL, i.desc_cc_inquirer_line, a.desc_cc_inquirer_line) desc_cc_inquirer_line,  " +
	            "                        a.values_cc_inquirer_line " +
	            "                   FROM (SELECT id_cc_inquirer_line, question_cc_inquirer_line, desc_cc_inquirer_line," +
	            "                                values_cc_inquirer_line, order_number" +
	            "                           FROM " + getGeneralDBScheme() + ".vc_cc_inquirer_line_all" +
	            "                  		   WHERE id_cc_inquirer = ?) i " +
	            "                        LEFT JOIN " +
	            "                        (SELECT id_cc_question_inquirer_line, id_cc_question, " +
	            "                                id_cc_inquirer_line, question_cc_inquirer_line, " +
	            "                                desc_cc_inquirer_line, values_cc_inquirer_line " +
	            "                           FROM " + getGeneralDBScheme() + ".vc_cc_question_inq_line_all" +
	            "                  		   WHERE id_cc_question = ?) a " +
	            "                  ON (i.id_cc_inquirer_line = a.id_cc_inquirer_line) " + 
	          	"                  ORDER BY i.order_number)" +
	            "          WHERE ROWNUM < " + p_end +
	        	" ) WHERE rn >= " + p_beg;

	      pParam.add(new bcFeautureParam("int", this.getValue("ID_CC_INQUIRER")));
	      pParam.add(new bcFeautureParam("int", this.idQuestion));
	      pParam.add(new bcFeautureParam("int", p_end));
	      pParam.add(new bcFeautureParam("int", p_beg));
	      
	      StringBuilder html = new StringBuilder();
	      PreparedStatement st = null;
	      Connection con = null;
	      int myCnt = 0;
	      try{
	    	   LOGGER.debug(prepareSQLToLog(mySQL, pParam));
	    	  con = Connector.getConnection(getSessionId());
	          st = con.prepareStatement(mySQL);
	          st = prepareParam(st, pParam);

	          ResultSet rset = st.executeQuery();
	          ResultSetMetaData mtd = rset.getMetaData();
	          
	          int colCount = mtd.getColumnCount();
	          
	          //html.append(getBottomFrameTable());
	          html.append("<thead>");
	          html.append("<tr>");
	          for (int i=4; i <= colCount; i++) {
	        	  html.append(getBottomFrameTableTH(call_centerXML, mtd.getColumnName(i)));
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	              myCnt = myCnt + 1;
	        	  html.append("<tr>");
	              for (int i=4; i <= colCount; i++) {
	            	  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	              }
	              html.append("</tr>\n");
	          }
	          html.append("<input type=\"hidden\" name=\"paramcount\" value=\""+myCnt+"\">");
	          //html.append("</tbody></table>\n");

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
	  }
}
