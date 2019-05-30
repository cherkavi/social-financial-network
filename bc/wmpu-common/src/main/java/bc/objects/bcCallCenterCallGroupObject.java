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

public class bcCallCenterCallGroupObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcCallCenterCallGroupObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idCallGroup;
	
	public bcCallCenterCallGroupObject(String pIdCallGroup) {
		this.idCallGroup = pIdCallGroup;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CC_CALL_GROUP_CLUB_ALL WHERE id_cc_call_group = ?";
		fieldHm = getFeatures2(featureSelect, this.idCallGroup, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

	public String getClientsHTML(String pFind, String pQuestionType, String pQuestionStatus, String p_beg, String p_end, String pOrderField, String pOrderType) {
		ArrayList<bcFeautureParam> pParam = initParamArray();
	    String mySQL = 
	    	" SELECT rn, id_cc_question, " +
	        "      fio_nat_prs, phone_mobile, cd_card1, bal_card_frmt card_bal_exist_frmt, " +
	        "      date_acc_frmt card_last_date_payment_frmt, card_payment_count, card_payment_sum_frmt, " +
	        "      due_date_frmt call_date_frmt, name_cc_question_status cc_question_status, " +
	        "      card_serial_number, card_id_issuer, card_id_payment_system, id_nat_prs " +
	        "	FROM (SELECT ROWNUM rn, id_cc_question, due_date_frmt, " +
	        "                cd_card1, fio_nat_prs, phone_mobile, bal_card_frmt, date_acc_frmt, " +
	        "                card_payment_count, card_payment_sum_frmt, name_cc_question_status," +
	        "                card_serial_number, card_id_issuer, card_id_payment_system, id_nat_prs " +
	        "	        FROM (SELECT id_cc_question, fio_nat_prs, phone_mobile, cd_card1, " + 
	        "                        bal_card_frmt, date_acc_frmt, card_payment_count, card_payment_sum_frmt, due_date_frmt, " +
	        "                        DECODE(cd_cc_question_status, " +
	        "                  		        'BEGUN', '<b><font color=\"black\">'||name_cc_question_status||'</font></b>', " +
	        "                  	        	'IN_PROCESS', '<b><font color=\"blue\">'||name_cc_question_status||'</font></b>', " +
	        "                  		        'FINISHED', '<b><font color=\"green\">'||name_cc_question_status||'</font></b>', " +
	        "                  		        'POSTPONED', '<b><font color=\"brown\">'||name_cc_question_status||'</font></b>', " +
	        "                  		        'CANCEL', '<b><font color=\"red\">'||name_cc_question_status||'</font></b>', " +
	        "                  		        name_cc_question_status" +
	        "                        ) name_cc_question_status," +
	        "                        card_serial_number, card_id_issuer, card_id_payment_system, id_nat_prs " +
	        "                   FROM " + getGeneralDBScheme()+".vc_cc_questions2_all " +
	        "                  WHERE id_cc_call_group = " + this.idCallGroup;
	    pParam.add(new bcFeautureParam("int", this.idCallGroup));
	    
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + " AND (UPPER(due_date_frmt) LIKE UPPER('%'||?||'%') " +
				"    OR UPPER(cd_card1) LIKE UPPER('%'||?||'%') " +
				"    OR UPPER(fio_nat_prs) LIKE UPPER('%'||?||'%') " +
				"    OR UPPER(title) LIKE UPPER('%'||?||'%')) ";
	    	for (int i=0; i<4; i++) {
	    	    pParam.add(new bcFeautureParam("string", pFind));
	    	}
	    }
	    if (!isEmpty(pQuestionType)) {
	    	mySQL = mySQL + " AND cd_cc_question_type = ? ";
	    	pParam.add(new bcFeautureParam("string", pQuestionType));
	    }
	    if (!isEmpty(pQuestionStatus)) {
	    	mySQL = mySQL + " AND cd_cc_question_status = ? ";
	    	pParam.add(new bcFeautureParam("string", pQuestionStatus));
	    }
		if (!isEmpty(pOrderField)) {
			if ("5".equalsIgnoreCase(pOrderField)) {
				mySQL = mySQL + " ORDER BY bal_card";
		    } else if ("8".equalsIgnoreCase(pOrderField)) {
		    	mySQL = mySQL + " ORDER BY card_payment_sum";
		    } else {
		    	mySQL = mySQL + " ORDER BY " + pOrderField;
		    }
			if (!isEmpty(pOrderType)) {
				mySQL = mySQL + " " + pOrderType;
			}
		}
		pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL +
	       	"                 ) " +
	       	"          WHERE ROWNUM < ? " + 
	       	"        ) " +
	       	"  WHERE rn >= ?";
	    
	    StringBuilder html = new StringBuilder();
	    PreparedStatement st = null;
	    Connection con = null;
	    //boolean hasEditPermission = false; 
	    boolean hasCardPermission = false;
	    boolean hasNatPrsPermission = false;
	    boolean hasQuestionPermission = false;
	     
	    try{
	    	  //if (isEditPermited("CALL_CENTER_CALL_GROUP_CLIENTS")>0) {
	    		//  hasEditPermission = true;
	    	  //}
	        	if (isEditMenuPermited("CARDS_CLUBCARDS")>=0) {
	        		hasCardPermission = true;
	        	}
	        	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
	        		hasNatPrsPermission = true;
	        	}
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

	          html.append(getBottomFrameTableTH(call_centerXML, "RN"));
	          for (int i=2; i <= colCount-4; i++) {
	        	  html.append(getBottomFrameTableExtendedTH(
	        			  call_centerXML,
	        			  i-1,
	        			  mtd.getColumnName(i), 
	        			  pOrderField,
	        			  pOrderType,
	        			  "../crm/call_center/call_groupspecs.jsp?id="+this.idCallGroup));
	        	  //html.append(getBottomFrameTableTH(call_centerXML, mtd.getColumnName(i)));
	          }  
              //if (hasEditPermission) {
	           	  html.append("<th>&nbsp;</th>\n");  
	          //}
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>\n");
			      for (int i=1; i <= colCount-4; i++) {
			    	  if (hasCardPermission && "CD_CARD1".equalsIgnoreCase(mtd.getColumnName(i)) &&
			    			  !("".equalsIgnoreCase(mtd.getColumnName(i)))) {
	          	  		  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardspecs.jsp?id="+rset.getString("CARD_SERIAL_NUMBER")+"&iss="+rset.getString("CARD_ID_ISSUER")+"&paysys="+rset.getString("CARD_ID_PAYMENT_SYSTEM"), "", ""));
	         	  	  } else if (hasNatPrsPermission && "FIO_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i)) &&
			    			  !("".equalsIgnoreCase(mtd.getColumnName(i)))) {
	         	  		  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
	         	  	  } else if (hasQuestionPermission && "ID_CC_QUESTION".equalsIgnoreCase(mtd.getColumnName(i)) &&
			    			  !("".equalsIgnoreCase(mtd.getColumnName(i)))) {
	         	  		  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/call_center/questionspecs.jsp?id="+rset.getString("ID_CC_QUESTION"), "", ""));
		         	  } else {
	          	  		  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	         	  	  }
			      }
	              //if (hasEditPermission) {
            		  String myHyperLink = "../crm/call_center/call_groupupdate.jsp?type=client&id="+this.idCallGroup+"&id_activity="+rset.getString("ID_CC_QUESTION");
            		  //String delHyperLink = getContextPath()+"../crm/call_center/call_groupupdate.jsp?type=client&id="+this.idCallGroup+"&id_activity="+rset.getString("ID_CC_QUESTION")+"&process=yes&action=remove";
            		  //html.append(getDeleteButtonHTML(delHyperLink, getLanguage(), call_centerXML.getfieldTransl(getLanguage(),"h_delete_call_group_client", false), rset.getString("ID_CC_QUESTION")));
            		  html.append(getEditButtonHTML(myHyperLink));
	              //}
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
