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

public class bcClubComissionTypeObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcClubComissionTypeObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idComissionType;
	
	public bcClubComissionTypeObject(String pIdComissionType) {
		this.idComissionType = pIdComissionType;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_COMISSION_TYPE_CLUB_ALL WHERE id_comission_type = ?";
		fieldHm = getFeatures2(featureSelect, this.idComissionType, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getJurPersonComissionHTML(String pFind, String p_beg, String p_end) {
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
        String mySQL = 
    		"SELECT rn, sname_jur_prs_payer jur_prs_payer, sname_jur_prs_receiver jur_prs_receiver, " + 
  			"       name_payment_system, begin_action_date_frmt, end_action_date_frmt, " +
  			"       fixed_value_frmt, percent_value_frmt, description, exist_flag_tsl, " +
  			"       id_comission, id_doc, cd_comission_type, exist_flag, " +
	  		"       payer_has_error, payer_has_error_type_tsl, receiver_has_error, receiver_has_error_type_tsl," +
	  		"       id_jur_prs_payer, name_comission_type, " +
	  		"       id_jur_prs_receiver, is_special_comission " +
  			"  FROM (SELECT ROWNUM rn, a.name_comission_type_full name_comission_type, " +
  			"               a.sname_jur_prs_payer, a.sname_jur_prs_receiver, " + 
  			"               a.name_payment_system, a.begin_action_date_frmt, a.end_action_date_frmt, " +
  			"               a.fixed_value_frmt, a.percent_value_frmt, a.description, a.exist_flag_tsl, " +
  			"               a.id_comission, a.id_doc, a.cd_comission_type, a.exist_flag, " +
	  		"               a.payer_has_error, a.payer_has_error_type_tsl, " +
	  		"               a.receiver_has_error, a.receiver_has_error_type_tsl," +
	  		"               a.id_jur_prs_payer, a.id_jur_prs_receiver," +
	  		"               a.is_special_comission " +
  			"  		   FROM (SELECT * " +
  			"                  FROM " + getGeneralDBScheme() + ".vc_jur_prs_comission_priv_all " +
  	  		"       	      WHERE id_comission_type = ? ";
        pParam.add(new bcFeautureParam("int", this.idComissionType));
        
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
   				" AND (UPPER(name_club_rel_type) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_comission_type_full) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs_payer) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs_receiver) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
      	  	"                 ORDER BY sname_jur_prs_payer, sname_jur_prs_receiver) a " +
      	  	"        WHERE ROWNUM < ? " + 
      	  	" ) WHERE rn >= ?";
        
        StringBuilder html = new StringBuilder();
        StringBuilder html_temp = new StringBuilder();
        Connection con = null;        
        PreparedStatement st = null;
        int myCnt = 0;
        String myFont = "";
        String myFontEnd = "";

        boolean hasEditPermission = false;
        boolean hasPartnerPermission = false;
        //boolean hasRelationshipPermission = false;
           
        String payerHyperLink = "";
        String receiverHyperLink = "";
  
        try{
        	if (isEditPermited("CLIENTS_YURPERSONS_COMISSION")>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
        	}
        	//if (isEditMenuPermited("CLUB_RELATIONSHIP")>=0) {
        	//	hasRelationshipPermission = true;
        	//}
        	  
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
                 
            int colCount = mtd.getColumnCount();
            
            if (hasEditPermission) { 
            	html.append("<form action=\"../crm/club/comistypeupdate.jsp\" name=\"updateForm\" id=\"updateForm\" accept-charset=\"UTF-8\" method=\"POST\">\n");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"comission\">\n");
            	html.append("<input type=\"hidden\" name=\"action\" value=\"editall\">\n");
            	html.append("<input type=\"hidden\" name=\"id\" value=\""+this.idComissionType+"\">\n");
            	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            }
            html.append(getBottomFrameTable());
            html.append("<tr>");
            html.append("<th>&nbsp;</th>\n");
            for (int i=2; i <= colCount-12; i++) {
            	html.append(getBottomFrameTableTH(comissionXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>\n");
         
         while (rset.next())
         {
        	 myCnt = myCnt + 1;
        	 if ("N".equalsIgnoreCase(rset.getString("EXIST_FLAG"))) {
        		 myFont = "<b><font color=\"red\">";
        		 myFontEnd = "</font></b>";
        	 } else if ("Y".equalsIgnoreCase(rset.getString("IS_SPECIAL_COMISSION"))) {
        		 myFont = "<b><font color=\"green\">";
        		 myFontEnd = "</font></b>";
        	 } else {
        		 myFont = "";
        		 myFontEnd = "";
        	 }
        	 
        	 if (!isEmpty(rset.getString("ID_JUR_PRS_PAYER")) && hasPartnerPermission) {
        		 payerHyperLink = getHyperLinkFirst()+"../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS_PAYER")+getHyperLinkMiddle();
        	 } else {
        		 payerHyperLink = "";
        	 }
        	 
        	 if (!isEmpty(rset.getString("ID_JUR_PRS_RECEIVER")) && hasPartnerPermission) {
        		 receiverHyperLink = getHyperLinkFirst()+"../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS_RECEIVER")+getHyperLinkMiddle();
        	 } else {
        		 receiverHyperLink = "";
        	 }
        	 
        	 html_temp.append("<tr><td>" + myFont + getValue2(rset.getString("RN")) +  myFontEnd + "</td>\n");
        	 //if (hasRelationshipPermission) {
        	 //	 html_temp.append("<td>" + myFont + getHyperLinkFirst()+"../crm/club/relationshipspecs.jsp?id="+rset.getString("ID_CLUB_REL") + getHyperLinkMiddle() + getValue2(rset.getString("FULL_NAME_CLUB_REL")) + getHyperLinkEnd() + myFontEnd + "</td>");
         	 //} else {
         	 //	 html_temp.append("<td>" + myFont + getValue2(rset.getString("FULL_NAME_CLUB_REL")) +  myFontEnd + "</td>");
         	 //}
         	 html_temp.append("<td>" + myFont + payerHyperLink + getValue2(rset.getString("JUR_PRS_PAYER")) + getHyperLinkEnd() + myFontEnd);
        	 if (!"N".equalsIgnoreCase(rset.getString("PAYER_HAS_ERROR"))) {
        		 html_temp.append("&nbsp;<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/warning.png\" align=\"top\" style=\"border: 0px;\" title=\""+rset.getString("PAYER_HAS_ERROR_TYPE_TSL")+"\">");
        	 }
        	 html_temp.append("</td><td>" + myFont + receiverHyperLink + getValue2(rset.getString("JUR_PRS_RECEIVER")) + getHyperLinkEnd() + myFontEnd);
        	 if (!"N".equalsIgnoreCase(rset.getString("RECEIVER_HAS_ERROR"))) {
        		 html_temp.append("&nbsp;<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/warning.png\" align=\"top\" style=\"border: 0px;\" title=\""+rset.getString("RECEIVER_HAS_ERROR_TYPE_TSL")+"\">");
        	 }
             html_temp.append("</td><td>" + myFont + getValue2(rset.getString("NAME_PAYMENT_SYSTEM")) +  myFontEnd + 
            		   "</td><td>" + myFont + getValue2(rset.getString("BEGIN_ACTION_DATE_FRMT")) +  myFontEnd + 
            		   "</td><td>" + myFont + getValue2(rset.getString("END_ACTION_DATE_FRMT")) +  myFontEnd + 
            		   "</td>\n");
             if (hasEditPermission) {
            	 html_temp.append("<input type=\"hidden\" name=\"nameparam"+myCnt+"\" value=\""+rset.getString("ID_COMISSION")+"\">");
            	 html_temp.append("<td><input type=\"text\" name=\"fixedvalue"+myCnt+"\" size=\"8\" value=\""+getHTMLValue(rset.getString("FIXED_VALUE_FRMT"))+"\" class=\"inputfield\"></td>\n");
            	 html_temp.append("<td><input type=\"text\" name=\"percentvalue"+myCnt+"\" size=\"8\" value=\""+getHTMLValue(rset.getString("PERCENT_VALUE_FRMT"))+"\" class=\"inputfield\"></td>\n");
             } else {
            	 html_temp.append("<td>" + myFont + getValue2(rset.getString("FIXED_VALUE_FRMT")) +  myFontEnd + "</td>\n");
            	 html_temp.append("<td>" + myFont + getValue2(rset.getString("PERCENT_VALUE_FRMT")) +  myFontEnd + "</td>\n");
             }
             html_temp.append("<td>" + myFont + getValue2(rset.getString("DESCRIPTION")) +  myFontEnd + 
          		   "</td><td>" + myFont + getValue2(rset.getString("EXIST_FLAG_TSL")) +  myFontEnd + 
          		   "</td>\n");
             if (hasEditPermission) {
            	 String myHyperLink = "../crm/club/comistypeupdate.jsp?type=comission&id="+this.idComissionType+"&id_comission="+rset.getString("ID_COMISSION");
            	 String myDeleteLink = "../crm/club/comistypeupdate.jsp?type=comission&id="+this.idComissionType+"&id_comission="+rset.getString("ID_COMISSION")+"&action=remove&process=yes";
	             html_temp.append(getDeleteButtonHTML(myDeleteLink, comissionXML.getfieldTransl("h_delete_bank_comission", false), rset.getString("NAME_COMISSION_TYPE")));
	             html_temp.append(getEditButtonHTML(myHyperLink));
             }
             html_temp.append("</tr>\n");
         }
         html_temp.append("<input type=\"hidden\" name=\"paramcount\" value=\""+myCnt+"\">");
         if (hasEditPermission && (myCnt>0)) {
        	 if (myCnt>0) {
        		 html.append("<tr>");
        		 html.append("<td colspan=\"14\" align=\"center\">");
        		 html.append(getSubmitButtonAjax("../crm/club/comistypeupdate.jsp"));
        		 html.append("</td></tr>\n");
        	 }
         }
    	 html.append(html_temp.toString());
    	 if (hasEditPermission && (myCnt>0)) {
        	 if (myCnt>0) {
        		 html.append("<tr>");
        		 html.append("<td colspan=\"14\" align=\"center\">");
        		 html.append(getSubmitButtonAjax("../crm/club/comistypeupdate.jsp"));
        		 html.append("</td></tr>\n");
        	 }
         }
    	 html.append("</form></table>\n");
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
     Connector.closeConnection(con);
     return html.toString();
  } // class getJurPersonComissionHTML

    public String getOperSchemesHTML(String pFind, String pIdOperScheme, String p_beg, String p_end) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, desc_bk_operation_scheme fn_posting_scheme, id_bk_operation_scheme_line, name_club_rel_type, name_bk_operation_type, " +
    		"        DECODE(cd_bk_phase, " +
    		"               'AFTER_MONEY_TRANSFER', '<font color=\"green\">'||name_bk_phase||'</font>', " +
    		"               name_bk_phase) name_bk_phase, " +
    		"        oper_number, " +
	      	"        debet_cd_bk_account_sh_line debet_cd_bk_account, " +
	      	"        credit_cd_bk_account_sh_line credit_cd_bk_account, " +
	      	"        oper_content, exist_flag_tsl, exist_flag, id_bk_operation_scheme," +
	      	"        debet_id_bk_account_sh_line, credit_id_bk_account_sh_line " +
	      	"   FROM (SELECT ROWNUM rn, id_bk_operation_scheme_line, name_club_rel_type, name_bk_operation_type, " +
	      	"                cd_bk_phase, name_bk_phase, oper_number, debet_cd_bk_account_sh_line, " +
	      	"                credit_cd_bk_account_sh_line, oper_content, exist_flag_tsl, exist_flag," +
	      	"                id_bk_operation_scheme, desc_bk_operation_scheme," +
	      	"                debet_id_bk_account_sh_line, credit_id_bk_account_sh_line " +
	      	" 			FROM (SELECT * " +
	      	"                   FROM " + getGeneralDBScheme() + ".vc_oper_scheme_lines_club_all " +
  	  		"       	      WHERE (UPPER(amount) LIKE '%'||?||'.'||?||'.%' " + 
  	  		"                    OR UPPER(assignment_posting) LIKE '%'||?||'.'||?||'.%' " + 
  	  		"                    OR UPPER(payment_function) LIKE '%'||?||'.'||?||'.%') ";

    	pParam.add(new bcFeautureParam("string", this.getValue("CD_CLUB_REL_TYPE")));
    	pParam.add(new bcFeautureParam("string", this.getValue("CD_COMISSION_TYPE")));
    	pParam.add(new bcFeautureParam("string", this.getValue("CD_CLUB_REL_TYPE")));
    	pParam.add(new bcFeautureParam("string", this.getValue("CD_COMISSION_TYPE")));
    	pParam.add(new bcFeautureParam("string", this.getValue("CD_CLUB_REL_TYPE")));
    	pParam.add(new bcFeautureParam("string", this.getValue("CD_COMISSION_TYPE")));
    	
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_bk_operation_scheme_line) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_club_rel_type) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(oper_number) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(debet_cd_bk_account_sh_line) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(credit_cd_bk_account_sh_line) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(oper_content) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        if (!isEmpty(pIdOperScheme)) {
        	mySQL = mySQL + " AND id_bk_operation_scheme = ? ";
        	pParam.add(new bcFeautureParam("int", pIdOperScheme));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
      	  	"                 ORDER BY desc_bk_operation_scheme, oper_number, name_bk_phase)  " +
      	  	"        WHERE ROWNUM < ? " + 
      	  	" ) WHERE rn >= ?";
           	StringBuilder html = new StringBuilder();
           	Connection con = null;        
           	PreparedStatement st = null;
           	String myFont = "";

           	boolean hasOperSchemePermission = false;
           	boolean hasBKAccountPermission = false;
           	
           
           	try{
           		if (isEditMenuPermited("FINANCE_POSTING_SCHEME")>=0) {
           			hasOperSchemePermission = true;
           		}
           		if (isEditMenuPermited("FINANCE_BK_SCHEME")>=0) {
           			hasBKAccountPermission = true;
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
	              for (int i=1; i <= colCount-4; i++) {
	              	  html.append(getBottomFrameTableTH(posting_schemeXML, mtd.getColumnName(i)));
	              }
	              html.append("</tr></thead><tbody>\n");
         
	              while (rset.next())  {
	            	  if ("N".equalsIgnoreCase(rset.getString("EXIST_FLAG"))) {
	            		  myFont = "<font color=\"red\">";
	            	  } else {
	            		  myFont = "";
	            	  }
	            	  html.append("<tr>");
	            	  for (int i=1; i <= colCount-4; i++) {
	            		  if (hasOperSchemePermission && "ID_BK_OPERATION_SCHEME_LINE".equalsIgnoreCase(mtd.getColumnName(i))) {
	            			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/posting_scheme_linespecs.jsp?id=" + rset.getString(i), myFont, ""));
	            		  } else if (hasBKAccountPermission && "DEBET_CD_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
	            			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/bk_scheme_linespecs.jsp?id=" + rset.getString("DEBET_ID_BK_ACCOUNT_SH_LINE"), myFont, ""));
	            		  } else if (hasBKAccountPermission && "CREDIT_CD_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
	            			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/bk_scheme_linespecs.jsp?id=" + rset.getString("CREDIT_ID_BK_ACCOUNT_SH_LINE"), myFont, ""));
	            		  } else if (hasOperSchemePermission && "FN_POSTING_SCHEME".equalsIgnoreCase(mtd.getColumnName(i))) {
	            			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/posting_schemespecs.jsp?id=" + rset.getString("ID_BK_OPERATION_SCHEME"), myFont, ""));
	            		  } else {
	            			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, ""));
	            		  }
		              }
	            	  html.append("</tr>");
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
	     Connector.closeConnection(con);
	     return html.toString();
    } // class getJurPersonComissionHTML

    public String getClubRelationshipsHTML(String pFind, String p_beg, String p_end) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, id_club_rel, date_club_rel, name_club_rel_type,  " +
	  	  	"        sname_party1_full, sname_party2_full, payment_function, exist_flag, id_club_rel_oper_scheme " +
	  	  	"   FROM (SELECT ROWNUM rn, id_club_rel, name_club_rel_type, date_club_rel_frmt date_club_rel, " +
	  	  	"                desc_club_rel, sname_party1 sname_party1_full, " +
	  	  	"                sname_party2 sname_party2_full, payment_function, exist_flag, id_club_rel_oper_scheme " +
	       	"           FROM (SELECT * " +
	       	"                   FROM " + getGeneralDBScheme() + ".vc_club_rel_bk_oper_s_club_all "+
  	  		"       	      WHERE UPPER(payment_function) LIKE '%'||?||'.'||?||'.%' ";
    	pParam.add(new bcFeautureParam("string", this.getValue("CD_CLUB_REL_TYPE")));
    	pParam.add(new bcFeautureParam("string", this.getValue("CD_COMISSION_TYPE")));
    	
        if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_club_rel) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_club_rel_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_club_rel_type) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_party1) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_party2) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(payment_function) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
      	  	"                 ORDER BY id_club_rel)  " +
      	  	"        WHERE ROWNUM < ? " + 
      	  	" ) WHERE rn >= ?";
           	StringBuilder html = new StringBuilder();
           	Connection con = null;        
           	PreparedStatement st = null;
           	String myFont = "";

           	boolean hasClubRelPermission = false;
           
           	try{
           		if (isEditMenuPermited("CLUB_RELATIONSHIP")>=0) {
           			hasClubRelPermission = true;
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
	              for (int i=1; i <= colCount-1; i++) {
	              	  html.append(getBottomFrameTableTH(relationshipXML, mtd.getColumnName(i)));
	              }
	              html.append("</tr></thead><tbody>\n");
         
	              while (rset.next())  {
	            	  if ("N".equalsIgnoreCase(rset.getString("EXIST_FLAG"))) {
	            		  myFont = "<font color=\"red\">";
	            	  } else {
	            		  myFont = "";
	            	  }
	            	  html.append("<tr>");
	            	  for (int i=1; i <= colCount-1; i++) {
	            		  if (hasClubRelPermission && "ID_CLUB_REL".equalsIgnoreCase(mtd.getColumnName(i))) {
	            			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/relationshipspecs.jsp?id=" + rset.getString(i), myFont, ""));
	            		  } else {
	            			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, ""));
	            		  }
		              }
	            	  html.append("</tr>");
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
	     Connector.closeConnection(con);
	     return html.toString();
    } // class getJurPersonComissionHTML

}
