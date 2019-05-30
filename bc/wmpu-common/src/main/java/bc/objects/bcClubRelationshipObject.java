package bc.objects;

import java.net.URLEncoder;
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

public class bcClubRelationshipObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcClubRelationshipObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idClubRel;
	private String type;

	public bcClubRelationshipObject(String pType, String pIdClubRel) {
		this.type = pType;
		this.idClubRel = pIdClubRel;
		getFeature();
	}
	
	private void getFeature() {
		if ("CREATED".equalsIgnoreCase(type)) {
			String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CLUB_REL_CLUB_ALL WHERE id_club_rel = ?";
			fieldHm = getFeatures2(featureSelect, this.idClubRel, false);
		} else if ("NEEDED".equalsIgnoreCase(type)) {
			String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CLUB_REL_NEED_CLUB_ALL WHERE id_club_rel = ?";
			fieldHm = getFeatures2(featureSelect, this.idClubRel, false);
		}
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}  
	
    public String getDocumentsListHTML(String pFindString, String pDocType, String pDocState, String p_beg, String p_end, String pTabName, String pEditHiperLink) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        String src_doc = "";

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT id_doc, name_doc_type, name_doc_state, full_doc, sname_jur_prs_party1, sname_jur_prs_party2, " +
        	"        sname_jur_prs_party3, src_doc, file_doc "+
            "   FROM (SELECT ROWNUM rn, id_doc, name_doc_type, " +
            "				 DECODE(cd_doc_state, " +
            "               		'SIGNED', '<font color=\"green\"><b>'||name_doc_state||'</b></font>', " +
            "               		'FINISCHED', '<font color=\"red\">'||name_doc_state||'</font>', " +
            "               		name_doc_state" +
            "        		 ) name_doc_state, " +
            "				 full_doc, sname_jur_prs_party1, " +
            "                sname_jur_prs_party2, sname_jur_prs_party3, src_doc, file_doc "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_doc_priv_all" +
            "                  WHERE id_club_rel = ? ";
        pParam.add(new bcFeautureParam("int", this.idClubRel));
        
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_doc) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(full_doc) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs_party1) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs_party2) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs_party3) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	if (!isEmpty(pDocType)) {
    		mySQL = mySQL + " AND cd_doc_type = ? ";
    		pParam.add(new bcFeautureParam("string", pDocType));
    	}
    	if (!isEmpty(pDocState)) {
    		mySQL = mySQL + " AND cd_doc_state = ? ";
    		pParam.add(new bcFeautureParam("string", pDocState));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "         ) WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ? ";
        boolean hasEditPermission = false;
        boolean hasDocPermission = false;
        try{
        	if (isEditPermited(pTabName)>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLUB_DOCUMENTS")>=0) {
        		hasDocPermission = true;
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
                html.append(getBottomFrameTableTH(documentXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	if (!isEmpty(rset.getString("FILE_DOC"))) {
            		src_doc = "<a href=\"../FileSender?FILENAME=" + URLEncoder.encode(rset.getString("FILE_DOC"),"UTF-8") + "\" title=\"" + buttonXML.getfieldTransl("open", false) + "\" target=_blank> " +
            				  "<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/small/open.gif\" align=\"top\" style=\"border: 0px;\">" +
            				  "</a>";
            	} else {
            		src_doc = "";
            	}
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if ("ID_DOC".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			if (hasDocPermission) {
          	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), "<span class=\"div_table_element\" onclick=\"ajaxpage('../crm/club/documentspecs.jsp?id="+rset.getString(i) + "', 'div_main')\">"+rset.getString(i)+"</span>"+src_doc, "", "", ""));
          	  			} else {
          	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i) + src_doc, "", "", ""));
          	  			}
         	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
                if (hasEditPermission) {
                 	String myHyperLink = pEditHiperLink + "&id_doc=" + rset.getString("ID_DOC") + "&type=doc";
                 	String myDeleteLink = pEditHiperLink + "&id_doc=" + rset.getString("ID_DOC") + "&type=doc&action=remove&process=yes";
                	html.append(getDeleteButtonHTML(myDeleteLink, documentXML.getfieldTransl("l_remove_doc", false), rset.getString("ID_DOC")));
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
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        return html.toString();
    } // getDocumentsListHTML 
	
    public String getDocumentsNeededListHTML(String pFindString, String pDocType, String p_beg, String p_end, String pTabName, String pEditHiperLink) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, name_doc_type, sname_party1_full, sname_party2_full, id_doc, id_party1, id_party2 "+
            "   FROM (SELECT ROWNUM rn, sname_party1 sname_party1_full, sname_party2 sname_party2_full, name_doc_type, id_doc, id_party1, id_party2 "+
        	"			FROM (SELECT *" +
        	"                   FROM " + getGeneralDBScheme()+".vc_doc_club_rel_need_club_all " +
            "                  WHERE id_club_rel = ? ";
        pParam.add(new bcFeautureParam("int", this.idClubRel));
        
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(sname_jur_prs_party1) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs_party2) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	if (!isEmpty(pDocType)) {
    		mySQL = mySQL + " AND cd_doc_type = ? ";
    		pParam.add(new bcFeautureParam("string", pDocType));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
            "         ) WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ? ";
        boolean hasEditPermission = false;
        boolean hasPartnerPermission = false;
        try{
        	if (isEditPermited(pTabName)>0) {
        		hasEditPermission = true;
        	}
      	  	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
      	  		hasPartnerPermission = true;
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
                html.append(getBottomFrameTableTH(relationshipXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-3; i++) {
          	  		if ("NAME_DOC_TYPE".equalsIgnoreCase(mtd.getColumnName(i)) && hasEditPermission) {
                     	String myHyperLink = pEditHiperLink + "&id_doc=" + rset.getString("ID_DOC") + "&type=doc&action=addneeded&process=no";
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), myHyperLink, "", ""));
          	  		} else if ("SNAME_PARTY1_FULL".equalsIgnoreCase(mtd.getColumnName(i)) && hasPartnerPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_PARTY1"), "", ""));
          	  		} else if ("SNAME_PARTY2_FULL".equalsIgnoreCase(mtd.getColumnName(i)) && hasPartnerPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_PARTY2"), "", ""));
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
    } // getDocumentsListHTML

    public String getComissionListHTML(String pFindString, String p_beg, String p_end) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        		"SELECT rn, name_comission_type, " +
        		"       sname_jur_prs_payer jur_prs_payer, sname_jur_prs_receiver jur_prs_receiver, " + 
	  			"       name_payment_system, begin_action_date_frmt, end_action_date_frmt, " +
	  			"       fixed_value_frmt, percent_value_frmt, description, exist_flag_tsl, " +
	  			"       id_comission, id_doc, id_comission_type, exist_flag, " +
	  			"       payer_has_error, payer_has_error_type_tsl, " +
	  			"       receiver_has_error, receiver_has_error_type_tsl," +
	  			"       id_jur_prs_payer, cd_jur_prs_prim_type_payer, " +
	  			"       id_jur_prs_receiver, cd_jur_prs_prim_type_receiver, is_special_comission " +
	  			"  FROM (SELECT ROWNUM rn, a.name_comission_type_full name_comission_type, a.sname_jur_prs_payer, a.sname_jur_prs_receiver, " + 
	  			"               a.name_payment_system, a.begin_action_date_frmt, a.end_action_date_frmt, " +
	  			"               a.fixed_value_frmt, a.percent_value_frmt, a.description, a.exist_flag_tsl, " +
	  			"               a.id_comission, a.id_doc, a.id_comission_type, a.exist_flag, " +
	  			"               a.payer_has_error, a.payer_has_error_type_tsl, " +
	  			"               a.receiver_has_error, a.receiver_has_error_type_tsl," +
	  			"               a.id_jur_prs_payer, a.cd_jur_prs_prim_type_payer, " +
	  			"               a.id_jur_prs_receiver, a.cd_jur_prs_prim_type_receiver," +
	  			"               a.is_special_comission " +
	  			"  		   FROM (SELECT * " +
	  			"                  FROM " + getGeneralDBScheme() + ".vc_jur_prs_comission_club_all " +
	  			" 	  	          WHERE id_club_rel = ? ";
    	pParam.add(new bcFeautureParam("int", this.idClubRel));
    	
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(name_comission_type_full) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs_payer) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs_receiver) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(begin_action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(end_action_date_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
	  			"                 ORDER BY sname_jur_prs_payer, sname_jur_prs_receiver, name_comission_type_full" +
	  			"               ) a " +
	  			"         WHERE ROWNUM < ? " + 
	  			" ) WHERE rn >= ?";
        
        StringBuilder html = new StringBuilder();
        StringBuilder html_one= new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        int myCnt = 0;
        boolean hasEditPermission = false;
        boolean hasComisTypePermission = false;
        boolean hasPartnerPermission = false;
        String myFont = "";
        String myFontEnd = "";
           
        String payerHyperLink = "";
        String receiverHyperLink = "";
        try{
        	if (isEditPermited("CLUB_RELATIONSHIP_COMISSIONS")>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLUB_COMISTYPE")>=0) {
        		hasComisTypePermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
        	}
        	  
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
                 
            int colCount = mtd.getColumnCount();
            
            if (hasEditPermission) { 
            	html.append("<form action=\"../crm/club/relationshipcomissionupdate.jsp\" name=\"updateForm\" id=\"updateForm\" accept-charset=\"UTF-8\" method=\"POST\">");
            	html.append("<input type=\"hidden\" name=\"type\" value=\"comission\">");
            	html.append("<input type=\"hidden\" name=\"action\" value=\"editall\">");
            	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">");
            	html.append("<input type=\"hidden\" name=\"id\" value=\""+this.idClubRel+"\">");
            }
            html.append(getBottomFrameTable());
            html.append("<tr>");
            html.append("<th>&nbsp;</th>\n");
            for (int i=2; i <= colCount-13; i++) {
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
        	 
        	 if (!isEmpty(rset.getString("CD_JUR_PRS_PRIM_TYPE_PAYER")) && hasPartnerPermission) {
        		 payerHyperLink = getHyperLinkFirst()+"../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS_PAYER")+getHyperLinkMiddle();
        	 } else {
        		 payerHyperLink = "";
        	 }
        	 
        	 if (!isEmpty(rset.getString("CD_JUR_PRS_PRIM_TYPE_RECEIVER")) && hasPartnerPermission) {
        		 receiverHyperLink = getHyperLinkFirst()+"../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS_RECEIVER")+getHyperLinkMiddle();
        	 } else {
        		 receiverHyperLink = "";
        	 }
        	 html_one.append("<tr><td>" + myFont + getValue2(rset.getString("RN")) +  myFontEnd + "</td>\n");
        	 if (hasComisTypePermission) {
        		 html_one.append("<td>" + myFont + getHyperLinkFirst()+"../crm/club/comistypespecs.jsp?id="+rset.getString("ID_COMISSION_TYPE") + getHyperLinkMiddle() + getValue2(rset.getString("NAME_COMISSION_TYPE")) + getHyperLinkEnd() + myFontEnd);
    		 } else {
    			 html_one.append("<td>" + myFont + getValue2(rset.getString("NAME_COMISSION_TYPE")) +  myFontEnd);
    		 }

        	 html_one.append("<td>" + myFont + payerHyperLink + getValue2(rset.getString("JUR_PRS_PAYER")) + getHyperLinkEnd() + myFontEnd);
        	 if (!"N".equalsIgnoreCase(rset.getString("PAYER_HAS_ERROR"))) {
        		 html_one.append("&nbsp;<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/warning.png\" align=\"top\" style=\"border: 0px;\" title=\""+rset.getString("PAYER_HAS_ERROR_TYPE_TSL")+"\">");
        	 }
        	 html_one.append("</td><td>" + myFont + receiverHyperLink + getValue2(rset.getString("JUR_PRS_RECEIVER")) + getHyperLinkEnd() + myFontEnd);
        	 if (!"N".equalsIgnoreCase(rset.getString("RECEIVER_HAS_ERROR"))) {
        		 html_one.append("&nbsp;<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/warning.png\" align=\"top\" style=\"border: 0px;\" title=\""+rset.getString("RECEIVER_HAS_ERROR_TYPE_TSL")+"\">");
        	 }
             html_one.append("</td>\n<td>" + myFont + getValue2(rset.getString("NAME_PAYMENT_SYSTEM")) +  myFontEnd + 
            		   "</td>\n<td>" + myFont + getValue2(rset.getString("BEGIN_ACTION_DATE_FRMT")) +  myFontEnd + 
            		   "</td>\n<td>" + myFont + getValue2(rset.getString("END_ACTION_DATE_FRMT")) +  myFontEnd + 
            		   "</td>\n");
             if (hasEditPermission) {
            	 html_one.append("<input type=\"hidden\" name=\"nameparam"+myCnt+"\" value=\""+rset.getString("ID_COMISSION")+"\">");
            	 html_one.append("<td><input type=\"text\" name=\"fixedvalue"+myCnt+"\" size=\"8\" value=\""+getHTMLValue(rset.getString("FIXED_VALUE_FRMT"))+"\" class=\"inputfield\"></td>\n");
            	 html_one.append("<td><input type=\"text\" name=\"percentvalue"+myCnt+"\" size=\"8\" value=\""+getHTMLValue(rset.getString("PERCENT_VALUE_FRMT"))+"\" class=\"inputfield\"></td>\n");
             } else {
            	 html_one.append("<td>" + myFont + getValue2(rset.getString("FIXED_VALUE_FRMT")) +  myFontEnd + "</td>\n");
            	 html_one.append("<td>" + myFont + getValue2(rset.getString("PERCENT_VALUE_FRMT")) +  myFontEnd + "</td>\n");
             }
             html_one.append("<td>" + myFont + getValue2(rset.getString("DESCRIPTION")) +  myFontEnd + 
          		   "</td>\n<td>" + myFont + getValue2(rset.getString("EXIST_FLAG_TSL")) +  myFontEnd + 
          		   "</td>\n");
             if (hasEditPermission) {
              	String myHyperLink = "../crm/club/relationshipcomissionupdate.jsp?type=comission&id=" + this.idClubRel + "&code="+rset.getString("ID_COMISSION");
              	String myDeleteLink = "../crm/club/relationshipcomissionupdate.jsp?type=comission&id=" + this.idClubRel + "&code="+rset.getString("ID_COMISSION") + "&type=comission&action=remove&process=yes";
            	html_one.append(getDeleteButtonHTML(myDeleteLink, clubXML.getfieldTransl("h_delete_comission", false), rset.getString("NAME_COMISSION_TYPE")));
            	html_one.append(getEditButtonHTML(myHyperLink));
             }
             html_one.append("</tr>\n");
         }
         html.append("<input type=\"hidden\" name=\"paramcount\" value=\""+myCnt+"\">");
         if (hasEditPermission && (myCnt>0)) {
        	 html.append("<tr>");
        	 html.append("<td colspan=\"13\" align=\"center\">");
        	 html.append(getSubmitButtonAjax("../crm/club/relationshipcomissionupdate.jsp"));
        	 html.append("</td></tr>\n");
         }
         html.append(html_one.toString());
         if (hasEditPermission && (myCnt>0)) {
        	 html.append("<tr>");
        	 html.append("<td colspan=\"13\" align=\"center\">");
        	 html.append(getSubmitButtonAjax("../crm/club/relationshipcomissionupdate.jsp"));
        	 html.append("</td></tr>\n");
         }
    	 html.append("</tbody></table>\n");
    	 html.append("</form>\n");
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
    
    public String getComissionNeedCount() {
    	return getOneValueByIntId(" SELECT " + getGeneralDBScheme()+".PACK_UI_COMISSION.get_need_club_rel_comis_count(?) FROM dual", this.idClubRel);
    } // getCardStateName
  
	public String getOperationSchemesHTML(String pFindString, String pIdOperScheme, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, desc_bk_operation_scheme fn_posting_scheme, " +
        	"		 id_bk_operation_scheme_line, name_bk_operation_type, " +
	      	"        name_bk_phase, oper_number, " +
	      	"        debet_cd_bk_account_sh_line debet_cd_bk_account, " +
	      	"        credit_cd_bk_account_sh_line credit_cd_bk_account, " +
	      	"        oper_content, exist_flag exist_flag_rel, " +
	      	"        id_club_rel_oper_scheme, id_bk_operation_scheme " +
	      	"   FROM (SELECT ROWNUM rn, id_bk_operation_scheme_line, name_club_rel_type, name_bk_operation_type, " +
	      	"                name_bk_phase, oper_number, " +
	      	"                debet_cd_bk_account_sh_line, credit_cd_bk_account_sh_line, oper_content, " +
	      	"                exist_flag, id_club_rel_oper_scheme, id_bk_operation_scheme, desc_bk_operation_scheme " +
	      	" 			FROM (SELECT *" +
	      	"                   FROM " + getGeneralDBScheme() + ".vc_club_rel_bk_oper_s_club_all " +
            "                  WHERE id_club_rel = ? ";
        pParam.add(new bcFeautureParam("int", this.idClubRel));
        
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_bk_operation_scheme_line) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_club_rel_type) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_bk_operation_type) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(oper_number) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(debet_cd_bk_account_sh_line) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(credit_cd_bk_account_sh_line) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(oper_content) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<7; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(pIdOperScheme)) {
           	mySQL = mySQL + " AND id_bk_operation_scheme = ? ";
           	pParam.add(new bcFeautureParam("int", pIdOperScheme));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL+ 
            "                  ORDER BY desc_bk_operation_scheme, oper_number " +
            "         ) WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        String myFont = "";
        String myFontEnd = "";
        String myBGColor = "";
        
        boolean hasOperSchemePermission = false;
        boolean hasEditPermission = false;
        
        try{
        	if (isEditMenuPermited("FINANCE_POSTING_SCHEME")>=0) {
        		hasOperSchemePermission = true;
        	}
       	 	if (isEditPermited("CLUB_RELATIONSHIP_POSTING_SCHEME")>0) {
       	 		hasEditPermission = true;
       	 	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            if (hasEditPermission) {
           	 	html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/club/relationshipupdate.jsp\">\n");
        	 	html.append("<input type=\"hidden\" name=\"type\" value=\"general\">\n");
        	 	html.append("<input type=\"hidden\" name=\"action\" value=\"set_club_rel\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
           	 	html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idClubRel + "\">\n");
            }

            html.append(getBottomFrameTable());
            html.append("<tr>");

            for (int i=1; i <= colCount-2; i++) {
               String colName = mtd.getColumnName(i);
               if ("EXIST_FLAG_REL".equalsIgnoreCase(colName)) {
                   if (hasEditPermission) {
                   	html.append("<th> "+ posting_schemeXML.getfieldTransl(colName, false)+
                  			"<input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this)\"></th>\n");
                   } else {
                	   html.append(getBottomFrameTableTH(posting_schemeXML, mtd.getColumnName(i)));
                   }
               } else {
            	   html.append(getBottomFrameTableTH(posting_schemeXML, mtd.getColumnName(i)));
               }
            }
            html.append("</tr></thead><tbody>\n");
            if (hasEditPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\""+colCount+"\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/club/relationshipupdate.jsp"));
             	html.append("</td>");
             	html.append("</tr>");
             }
            
            while (rset.next()) {
            	
                String id = "id_" + this.idClubRel + "_" + rset.getString("ID_BK_OPERATION_SCHEME_LINE");
                String tprvCheck="prv_"+id;
                String tCheck="chb_"+id;
                
                if ("Y".equalsIgnoreCase(rset.getString("EXIST_FLAG_REL"))){
                	myFont = "<b>";
                	myFontEnd = "</b>";
                	myBGColor = selectedBackGroundStyle;
                } else {
                	myFont = "";
                	myFontEnd = "";
                	myBGColor = "";
                }
                
                html.append("<tr>");
                for (int i=1; i<=colCount-2; i++) {
               		if ("EXIST_FLAG_REL".equalsIgnoreCase(mtd.getColumnName(i))) {
               			if (hasEditPermission) {
               				if ("Y".equalsIgnoreCase(rset.getString("EXIST_FLAG_REL"))) {
                               	html.append("<td align=\"center\" " + myBGColor + "><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked onclick=\"return CheckCB(this);\">\n");
                               	html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+"></td>");
               				} else {
                               	html.append("<td align=\"center\" " + myBGColor + "><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\">\n");
               				}
               			} else {
               				if ("Y".equalsIgnoreCase(rset.getString("EXIST_FLAG_REL"))) {
               					html.append("<td align=\"center\" " + myBGColor + "><INPUT  type=\"checkbox\" value = \""+rset.getString("EXIST_FLAG_REL")+"\" name="+tCheck+" id="+tCheck+" checked readonly onclick=\"return CheckCB(this);\"></td>\n");
               				} else {
               					html.append("<td align=\"center\" " + myBGColor + "><INPUT  type=\"checkbox\" value = \""+rset.getString("EXIST_FLAG_REL")+"\" name="+tCheck+" id="+tCheck+"  readonly onclick=\"return CheckCB(this);\"></td>\n");
               				}
               			}
               		} else if (hasOperSchemePermission && "ID_BK_OPERATION_SCHEME_LINE".equalsIgnoreCase(mtd.getColumnName(i))) {
               			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/posting_scheme_linespecs.jsp?id=" + rset.getString(i), myFont, myBGColor));
               		} else if (hasOperSchemePermission && "FN_POSTING_SCHEME".equalsIgnoreCase(mtd.getColumnName(i))) {
               			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/posting_schemespecs.jsp?id=" + rset.getString("ID_BK_OPERATION_SCHEME"), myFont, myBGColor));
               		} else {
               			html.append("<td " + myBGColor + ">" + myFont + getValue2(rset.getString(i)) + myFontEnd + "</td>");
               		}
                }
                html.append("</tr>\n");
            }
            if (hasEditPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\""+colCount+"\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/club/relationshipupdate.jsp"));
             	html.append("</td>");
             	html.append("</tr>");
             }
            html.append("</tbody></table>\n");
            if (hasEditPermission) {
             	html.append("</form>");
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
        return html.toString();
    } // getDocumentsListHTML

    private String getClubRelNameParty1(String relType) {
    	String nameParty = "";
    	if ("DEALER-TECHNICAL_ACQUIRER".equalsIgnoreCase(relType) || 
    			"DEALER-FINANCE_ACQUIRER".equalsIgnoreCase(relType)) {
    		nameParty = "sname_dealer_full";
    	} else {
    		nameParty = "sname_club_full";
    	}
    	return nameParty;
    }
    
    private String getClubRelNameParty2(String relType) {
    	String nameParty = "";
    	if ("SOCIETY-SHAREHOLDER".equalsIgnoreCase(relType) || "SOCIETY-OTHER".equalsIgnoreCase(relType)) {
    		nameParty = "";
    	} else if ("SOCIETY-CARD_SELLER".equalsIgnoreCase(relType)) {
    		nameParty = "sname_card_seller_full";
    	} else if ("SOCIETY-DEALER".equalsIgnoreCase(relType)) {
    		nameParty = "sname_dealer_full";
    	} else if ("SOCIETY-FINANCE_ACQUIRER".equalsIgnoreCase(relType)) {
    		nameParty = "sname_fin_acquirer_full";
    	} else if ("SOCIETY-ISSUER".equalsIgnoreCase(relType)) {
    		nameParty = "sname_issuer_full";
    	} else if ("SOCIETY-PARTNER".equalsIgnoreCase(relType)) {
    		nameParty = "sname_partner_full";
    	} else if ("SOCIETY-TECHNICAL_ACQUIRER".equalsIgnoreCase(relType)) {
    		nameParty = "sname_tech_acquirer_full";
    	} else if ("SOCIETY-TERMINAL_MANUFACTURER".equalsIgnoreCase(relType)) {
    		nameParty = "sname_term_manufact_full";
    	} else if ("DEALER-FINANCE_ACQUIRER".equalsIgnoreCase(relType)) {
    		nameParty = "sname_fin_acquirer_full";
    	} else if ("DEALER-TECHNICAL_ACQUIRER".equalsIgnoreCase(relType)) {
    		nameParty = "sname_tech_acquirer_full";
    	}
    	return nameParty;
    }
    
    private String getClubRelTypeParty1(String relType) {
    	String typeParty = "CLUB";
    	if ("DEALER-TECHNICAL_ACQUIRER".equalsIgnoreCase(relType) || 
    			"DEALER-FINANCE_ACQUIRER".equalsIgnoreCase(relType)) {
    		typeParty = "DEALER";
    	}
    	return typeParty;
    }
    
    private String getClubRelTypeParty2(String relType) {
    	String typeParty = "ALL";
    	if ("SOCIETY-SHAREHOLDER".equalsIgnoreCase(relType) || 
    			"SOCIETY-OTHER".equalsIgnoreCase(relType)) {
    		typeParty = "ALL";
    	} else if ("SOCIETY-CARD_SELLER".equalsIgnoreCase(relType)) {
    		typeParty = "ALL";
    	} else if ("SOCIETY-DEALER".equalsIgnoreCase(relType)) {
    		typeParty = "DEALER";
    	} else if ("SOCIETY-FINANCE_ACQUIRER".equalsIgnoreCase(relType)) {
    		typeParty = "FIN_ACQUIRER";
    	} else if ("SOCIETY-ISSUER".equalsIgnoreCase(relType)) {
    		typeParty = "ISSUER";
    	} else if ("SOCIETY-PARTNER".equalsIgnoreCase(relType)) {
    		typeParty = "PARTNER";
    	} else if ("SOCIETY-TECHNICAL_ACQUIRER".equalsIgnoreCase(relType)) {
    		typeParty = "TECH_ACQUIRER";
    	} else if ("SOCIETY-TERMINAL_MANUFACTURER".equalsIgnoreCase(relType)) {
    		typeParty = "TERMINAL_MANUFACTURER";
    	} else if ("DEALER-FINANCE_ACQUIRER".equalsIgnoreCase(relType)) {
    		typeParty = "FIN_ACQUIRER";
    	} else if ("DEALER-TECHNICAL_ACQUIRER".equalsIgnoreCase(relType)) {
    		typeParty = "TECH_ACQUIRER";
    	}
    	return typeParty;
    }
    
    public String getClubRelCheckScript(String relType) {
    	StringBuilder html = new StringBuilder();
		html.append("var formData = new Array (");
		html.append("	new Array ('name_club', 'varchar2', 1),\n");
    	if ("SOCIETY-SHAREHOLDER".equalsIgnoreCase(relType) || "SOCIETY-OTHER".equalsIgnoreCase(relType)) {
			html.append("	new Array ('date_club_rel', 'varchar2', 1),\n");
			html.append("	new Array ('name_party1_settlem_accnt', 'varchar2', 1)\n");
    	} else if ("SOCIETY-ISSUER".equalsIgnoreCase(relType)) {
    		html.append("	new Array ('date_club_rel', 'varchar2', 1),\n");
    		html.append("	new Array ('name_party1_settlem_accnt', 'varchar2', 1),\n");
    		html.append("	new Array ('name_party1_club_bon_accnt', 'varchar2', 1),\n");
    		html.append("	new Array ('name_party2', 'varchar2', 1),\n");
    		html.append("	new Array ('name_party2_settlem_accnt', 'varchar2', 1)\n");
    	} else if ("SOCIETY-DEALER".equalsIgnoreCase(relType)) {
    		html.append("	new Array ('date_club_rel', 'varchar2', 1),\n");
    		html.append("	new Array ('name_party1_settlem_accnt', 'varchar2', 1),\n");
    		html.append("	new Array ('name_party1_club_distrib_accnt', 'varchar2', 1),\n");
    		html.append("	new Array ('name_party2', 'varchar2', 1),\n");
    		html.append("	new Array ('name_party2_settlem_accnt', 'varchar2', 1)\n");
		} else  {
			html.append("	new Array ('date_club_rel', 'varchar2', 1),\n");
			html.append("	new Array ('name_party1', 'varchar2', 1),\n");
			html.append("	new Array ('name_party1_settlem_accnt', 'varchar2', 1),\n");
			html.append("	new Array ('name_party2', 'varchar2', 1),\n");
			html.append("	new Array ('name_party2_settlem_accnt', 'varchar2', 1)\n");
		}
		html.append(");\n\n");
    	html.append("function myValidateForm() {\n");
    	html.append("return validateForm(formData);\n");
    	html.append("}\n");
    	return html.toString();
    }

    private String getClubRelTypeName(String cd_club_rel_type) {
    	return getOneValueByStringId("SELECT name_club_rel_type FROM " + getGeneralDBScheme()+".vc_club_rel_type_all WHERE cd_club_rel_type = ? ", cd_club_rel_type);
    } //getGiftsOptions()

    public String getClubRelAddHTML(String pIdClub, String rel_type, String operation_type, String jur_prs, String pSysDate, String dateFormatTitle) {
    	StringBuilder html = new StringBuilder();
    	
    	String name_party1 = getClubRelNameParty1(rel_type);
		String name_party2 = getClubRelNameParty2(rel_type);
		String type_party1 = getClubRelTypeParty1(rel_type);
		String type_party2 = getClubRelTypeParty2(rel_type);
		
		String id_party1 		= "";
		String sname_party1 	= "";
		String id_party2 		= "";
		String sname_party2 	= "";
		String id_operator      = "";
		String sname_operator   = "";
		String id_club	        = "";
		String sname_club       = "";
		if ("addneeded".equalsIgnoreCase(operation_type)) {
			id_party1	 	= this.getValue("ID_PARTY1");
			sname_party1 	= this.getValue("SNAME_PARTY1");
			id_party2 		= this.getValue("ID_PARTY2");
			sname_party2 	= this.getValue("SNAME_PARTY2");
			id_club 		= this.getValue("ID_CLUB");
			sname_club 		= getClubShortName(this.getValue("ID_CLUB"));
		} else {
			bcClubShortObject club = new bcClubShortObject(pIdClub);
			id_club 			= club.getValue("ID_CLUB");
			sname_club 			= club.getValue("SNAME_CLUB");
			id_operator 		= club.getValue("ID_OPERATOR");
			sname_operator 		= club.getValue("SNAME_OPERATOR");
			if (isEmpty(jur_prs)) {
				if ("DEALER-TECHNICAL_ACQUIRER".equalsIgnoreCase(rel_type) || "DEALER-FINANCE_ACQUIRER".equalsIgnoreCase(rel_type)) {
					id_party1 = "";
					sname_party1 = "";
				} else {
					id_party1 = id_operator;
					sname_party1 = sname_operator;
				}
				id_party2 = "";
				sname_party2 = "";
			} else {
				String sname_jur_prs = getJurPersonShortName(jur_prs);
				if ("DEALER-TECHNICAL_ACQUIRER".equalsIgnoreCase(rel_type) || "DEALER-FINANCE_ACQUIRER".equalsIgnoreCase(rel_type)) {
					boolean isDealer = isJurPersonDealer(jur_prs);
					if (isDealer) {
						id_party1 = jur_prs;
		    			sname_party1 = sname_jur_prs;
					} else {
	    	    		id_party2 = jur_prs;
		    			sname_party2 = sname_jur_prs;
					}
    	    	} else {
    	    		id_party1 = id_operator;
					sname_party1 = sname_operator;
    	    		id_party2 = jur_prs;
	    			sname_party2 = sname_jur_prs;
    	    	}
			}
		}
		
    	html.append("<tr>");
    	html.append("<td>" + relationshipXML.getfieldTransl("cd_club_rel_type", false) + "</td><td><input type=\"text\" name=\"name_club_rel_type\" size=\"45\" value=\"" + getClubRelTypeName(rel_type) + "\" readonly class=\"inputfield-ro\"></td>");
    	html.append("<td>" + clubXML.getfieldTransl("club", false));
		html.append("<td>");
		html.append("<input type=\"hidden\" name=\"id_club\" id=\"id_club\" value=\"" + id_club + "\">");
		html.append("<input type=\"text\" name=\"name_club\" id=\"name_club\" size=\"45\" value=\"" + sname_club + "\" readonly class=\"inputfield-ro\">");
		html.append("</td>");
    	/*
    	html.append("<td>");
		html.append("<input type=\"hidden\" id=\"id_club\" name=\"id_club\" value=\"" + id_club + "\" readonly class=\"inputfield\">");
		html.append("<input type=\"text\" id=\"name_club\" name=\"name_club\" size=\"37\" value=\"" + sname_club + "\" readonly class=\"inputfield\">");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('" + getContextPath() + "../crm/services/findclub.jsp?id_club='+document.getElementById('id_club').value+'&field=club','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\"  title=\"" + buttonXML.getfieldTransl("button_edit", false) + "\">");
		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\"></a>");
		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_club').value = ''; document.getElementById('name_club').value = ''; return false;\" title=\"" + buttonXML.getfieldTransl("button_delete", false) + "\">");
		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\"></a>");
		html.append("</td>");
		*/
    	html.append("</tr>");
    	html.append("<tr>");
    	html.append("<td>" + relationshipXML.getfieldTransl("date_club_rel", true) + "</td> <td>");
    	html.append(getCalendarInputField("date_club_rel", pSysDate, "10"));
    	html.append("</td>");
    	html.append("<td valign=\"top\">" + relationshipXML.getfieldTransl("desc_club_rel", false) + "</td><td valign=\"top\"><textarea name=\"desc_club_rel\" cols=\"40\" rows=\"3\" class=\"inputfield\"></textarea></td>");
    	html.append("</tr>");

    	html.append("<tr><td colspan=\"4\" class=\"top_line\"><b>" + relationshipXML.getfieldTransl("h_participants", false) + "</b></td></tr>");

    	html.append("<tr>");
    	
    	if (!("DEALER-TECHNICAL_ACQUIRER".equalsIgnoreCase(rel_type) || "DEALER-FINANCE_ACQUIRER".equalsIgnoreCase(rel_type))) {
    		html.append("<td>" + relationshipXML.getfieldTransl(name_party1, false) + "</td>"); 
	    	html.append("<td>");
	    	html.append("<input type=\"hidden\" name=\"id_party1\" id=\"id_party1\" value=\"" + id_party1 + "\">");
	    	html.append("<input type=\"text\" name=\"name_party1\" id=\"name_party1\" size=\"45\" value=\"" + sname_party1 + "\" readonly class=\"inputfield-ro\">");
	    	html.append("</td>");
    	} else {
    		html.append("<td>" + relationshipXML.getfieldTransl(name_party1, true) + "</td>"); 
    		html.append("<td>");
    		html.append("<input type=\"hidden\" id=\"id_party1\" name=\"id_party1\" value=\"" + id_party1 + "\" readonly class=\"inputfield\">");
    		html.append("<input type=\"text\" id=\"name_party1\" name=\"name_party1\" size=\"37\" value=\"" + sname_party1 + "\" readonly class=\"inputfield\">");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findjurprs.jsp?id_jur_prs=" + id_party1 + "&field=party1&type=" + type_party1  + "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\"  title=\"" + buttonXML.getfieldTransl("button_edit", false) + "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_party1').value = ''; document.getElementById('name_party1').value = ''; return false;\" title=\"" + buttonXML.getfieldTransl("button_delete", false) + "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("</td>");
    	}
    	
    	if (!("SOCIETY-SHAREHOLDER".equalsIgnoreCase(rel_type) || "SOCIETY-OTHER".equalsIgnoreCase(rel_type))) {
    		html.append("<td>" + relationshipXML.getfieldTransl(name_party2, true) + "</td>");
    		html.append("<td>");
    		html.append("<input type=\"hidden\" id=\"id_party2\" name=\"id_party2\" value=\"" + id_party2 + "\" readonly class=\"inputfield\">");
    		html.append("<input type=\"text\" id=\"name_party2\" name=\"name_party2\" size=\"37\" value=\"" + sname_party2 + "\" readonly class=\"inputfield\">");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findjurprs.jsp?id_jur_prs=" + id_party2 + "&field=party2&type=" + type_party2 + "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\"  title=\"" + buttonXML.getfieldTransl("button_edit", false) + "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_party2').value = ''; document.getElementById('name_party2').value = ''; return false;\" title=\"" + buttonXML.getfieldTransl("button_delete", false) + "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("</td>");
    	}
    	html.append("</tr>");
    	html.append("<tr>");
    	html.append("<td>" + relationshipXML.getfieldTransl("id_club_settlem_accnt", true) + "</td>");
    	html.append("<td>");
    	html.append("<input type=\"hidden\" id=\"id_party1_settlem_accnt\" name=\"id_party1_settlem_accnt\" value=\"\" readonly class=\"inputfield\">");
    	html.append("<input type=\"text\" id=\"name_party1_settlem_accnt\" name=\"name_party1_settlem_accnt\" size=\"37\" value=\"\" readonly class=\"inputfield\">");
    	html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findbankaccounts2.jsp?id_bank_account=&field=party1_settlem_accnt&id_jur_prs='+getElementById('id_party1').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\"  title=\"" + buttonXML.getfieldTransl("button_edit", false)+ "\">");
    	html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\"></a>");
    	html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_party1_settlem_accnt').value = ''; document.getElementById('name_party1_settlem_accnt').value = ''; return false;\" title=\"" + buttonXML.getfieldTransl("button_delete", false)+ "\">");
    	html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\"></a>");
    	html.append("</td>");
    	if (!("SOCIETY-SHAREHOLDER".equalsIgnoreCase(rel_type) || "SOCIETY-OTHER".equalsIgnoreCase(rel_type))) {
    		html.append("<td>" + relationshipXML.getfieldTransl("id_partner_settlem_accnt", true) + "</td>");
    		html.append("<td>");
    		html.append("<input type=\"hidden\" id=\"id_party2_settlem_accnt\" name=\"id_party2_settlem_accnt\" value=\"\" readonly class=\"inputfield\">");
    		html.append("<input type=\"text\" id=\"name_party2_settlem_accnt\" name=\"name_party2_settlem_accnt\" size=\"37\" value=\"\" readonly class=\"inputfield\">");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findbankaccounts2.jsp?id_bank_account=&field=party2_settlem_accnt&id_jur_prs='+getElementById('id_party2').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\"  title=\"" + buttonXML.getfieldTransl("button_edit", false)+ "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_party2_settlem_accnt').value = ''; document.getElementById('name_party2_settlem_accnt').value = ''; return false;\" title=\"" + buttonXML.getfieldTransl("button_delete", false) + "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("</td>");
    	}
    	html.append("</tr>");
    	
    	if ("SOCIETY-ISSUER".equalsIgnoreCase(rel_type)) {
    		html.append("<tr>");
    		html.append("<td valign=\"top\">&nbsp;</td><td valign=\"top\">&nbsp;</td>");
    		html.append("<td valign=\"top\">" + relationshipXML.getfieldTransl("id_issuer_club_bon_accnt", true) + "</td>");
    		html.append("<td valign=\"top\">");
    		html.append("<input type=\"hidden\" id=\"id_party1_club_bon_accnt\" name=\"id_party1_club_bon_accnt\" value=\"\" readonly class=\"inputfield\">");
    		html.append("<input type=\"text\" id=\"name_party1_club_bon_accnt\" name=\"name_party1_club_bon_accnt\" size=\"37\" value=\"\" readonly class=\"inputfield\">");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findbankaccounts2.jsp?id_bank_account=&field=party1_club_bon_accnt&id_jur_prs='+getElementById('id_party1').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\"  title=\"" + buttonXML.getfieldTransl("button_edit", false) + "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_party1_club_bon_accnt').value = ''; document.getElementById('name_party1_club_bon_accnt').value = ''; return false;\" title=\"" + buttonXML.getfieldTransl("button_delete", false)+ "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("</td>");
    		html.append("</tr>");
    	}
	
    	if ("SOCIETY-DEALER".equalsIgnoreCase(rel_type)) {
    		html.append("<tr>");
    		html.append("<td valign=\"top\">" + relationshipXML.getfieldTransl("id_club_distrib_accnt", true) + "</td>");
    		html.append("<td valign=\"top\">");
    		html.append("<input type=\"hidden\" id=\"id_party1_club_distrib_accnt\" name=\"id_party1_club_distrib_accnt\" value=\"\" readonly class=\"inputfield\">");
    		html.append("<input type=\"text\" id=\"name_party1_club_distrib_accnt\" name=\"name_party1_club_distrib_accnt\" size=\"37\" value=\"\" readonly class=\"inputfield\">");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findbankaccounts2.jsp?id_bank_account=&field=party1_club_distrib_accnt&id_jur_prs='+getElementById('id_party1').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\"  title=\"" + buttonXML.getfieldTransl("button_edit", false) + "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_party1_club_distrib_accnt').value = ''; document.getElementById('name_party1_club_distrib_accnt').value = ''; return false;\" title=\"" + buttonXML.getfieldTransl("button_delete", false)+ "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("</td>");
    		html.append("<td valign=\"top\">&nbsp;</td><td valign=\"top\">&nbsp;</td>");
    		html.append("</tr>");
    	}

    	return html.toString();
    }
    
    public String getClubRelEditHTML(String dateFormatTitle) {
    	StringBuilder html = new StringBuilder();
    	
    	String rel_type = this.getValue("CD_CLUB_REL_TYPE");
    	String name_party1 = getClubRelNameParty1(rel_type);
		String name_party2 = getClubRelNameParty2(rel_type);
		String type_party1 = getClubRelTypeParty1(rel_type);
		String type_party2 = getClubRelTypeParty2(rel_type);
		
		
    	html.append("<tr>");
    	html.append("<td>" + relationshipXML.getfieldTransl("id_club_rel", false) + "</td><td><input type=\"text\" name=\"id_club_rel\" size=\"20\" value=\"" + this.getValue("ID_CLUB_REL") + "\" readonly class=\"inputfield-ro\"></td>");
    	html.append("<td>" + clubXML.getfieldTransl("club", false));
		html.append(getGoToHyperLink(
				"CLUB_CLUB",
				this.getValue("ID_CLUB"),
				"../crm/club/clubspecs.jsp?id=" + this.getValue("ID_CLUB")
			));
    	html.append("</td>");
    	html.append("<td>");
		html.append("<input type=\"text\" name=\"name_club\" id=\"name_club\" size=\"44\" value=\"" + getClubShortName(this.getValue("ID_CLUB")) + "\" readonly class=\"inputfield-ro\">");
		html.append("</td>");
		html.append("</tr>");
    	html.append("<tr>");
    	html.append("<td>" + relationshipXML.getfieldTransl("cd_club_rel_type", false) + "</td><td><input type=\"text\" name=\"name_club_rel_type\" size=\"45\" value=\"" + this.getValue("NAME_CLUB_REL_TYPE") + "\" readonly class=\"inputfield-ro\"></td>");
    	html.append("<td rowspan=\"2\" valign=\"top\">" + relationshipXML.getfieldTransl("desc_club_rel", false) + "</td><td rowspan=\"2\" valign=\"top\"><textarea name=\"desc_club_rel\" cols=\"40\" rows=\"2\" class=\"inputfield\">" + this.getValue("DESC_CLUB_REL")+ "</textarea></td>");
    	html.append("</tr>");
    	html.append("<tr>");
    	html.append("<td>" + relationshipXML.getfieldTransl("date_club_rel", true) + "</td> <td>");
    	html.append(getCalendarInputField("date_club_rel", this.getValue("DATE_CLUB_REL_FRMT"), "10"));
    	html.append("</td>");
    	html.append("</tr>");

    	html.append("<tr><td colspan=\"4\" class=\"top_line\"><b>" + relationshipXML.getfieldTransl("h_participants", false) + "</b></td></tr>");

    	html.append("<tr>");
    	
    	if (!("DEALER-TECHNICAL_ACQUIRER".equalsIgnoreCase(rel_type) || "DEALER-FINANCE_ACQUIRER".equalsIgnoreCase(rel_type))) {
    		html.append("<td>" + relationshipXML.getfieldTransl(name_party1, false));
    		html.append(getGoToHyperLink(
    				"CLIENTS_YURPERSONS",
    				this.getValue("ID_PARTY1"),
    				"../crm/clients/yurpersonspecs.jsp?id=" + this.getValue("ID_PARTY1")
    			));
    		html.append("</td>"); 
    		html.append("<td>");
    		html.append("<input type=\"hidden\"  name=\"id_party1\" id=\"id_party1\" value=\"" + this.getValue("ID_PARTY1") + "\">");
    		html.append("<input type=\"text\" name=\"name_party1\" id=\"name_party1\" size=\"45\" value=\"" + this.getValue("SNAME_PARTY1") + "\" readonly class=\"inputfield-ro\">");
    		html.append("</td>");
    	} else {
    		html.append("<td>" + relationshipXML.getfieldTransl(name_party1, true));
    		html.append(getGoToHyperLink(
    				"CLIENTS_YURPERSONS",
    				this.getValue("ID_PARTY1"),
    				"../crm/clients/yurpersonspecs.jsp?id=" + this.getValue("ID_PARTY1")
    			));
    		html.append("</td>"); 
    		html.append("<td>");
    		html.append("<input type=\"hidden\" id=\"id_party1\" name=\"id_party1\" value=\"" + this.getValue("ID_PARTY1") + "\" readonly class=\"inputfield\">");
    		html.append("<input type=\"text\" id=\"name_party1\" name=\"name_party1\" size=\"37\" value=\"" + this.getValue("SNAME_PARTY1") + "\" readonly class=\"inputfield\">");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findjurprs.jsp?id_jur_prs=" + this.getValue("ID_PARTY1") + "&field=party1&type=" + type_party1  + "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\"  title=\"" + buttonXML.getfieldTransl("button_edit", false) + "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_party1').value = ''; document.getElementById('name_party1').value = ''; return false;\" title=\"" + buttonXML.getfieldTransl("button_delete", false) + "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("</td>");
    	}
    	
    	if (!("SOCIETY-SHAREHOLDER".equalsIgnoreCase(rel_type) || "SOCIETY-OTHER".equalsIgnoreCase(rel_type))) {
    		html.append("<td>" + relationshipXML.getfieldTransl(name_party2, true));
    		html.append(getGoToHyperLink(
    				"CLIENTS_YURPERSONS",
    				this.getValue("ID_PARTY2"),
    				"../crm/clients/yurpersonspecs.jsp?id=" + this.getValue("ID_PARTY2")
    			));
    		html.append("</td>");
    		html.append("<td >");
    		html.append("<input type=\"hidden\" id=\"id_party2\" name=\"id_party2\" value=\"" + this.getValue("ID_PARTY2") + "\" readonly class=\"inputfield\">");
    		html.append("<input type=\"text\" id=\"name_party2\" name=\"name_party2\" size=\"37\" value=\"" + this.getValue("SNAME_PARTY2") + "\" readonly class=\"inputfield\">");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findjurprs.jsp?id_jur_prs=" + this.getValue("ID_PARTY2") + "&field=party2&type=" + type_party2 + "','blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\"  title=\"" + buttonXML.getfieldTransl("button_edit", false) + "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_party2').value = ''; document.getElementById('name_party2').value = ''; return false;\" title=\"" + buttonXML.getfieldTransl("button_delete", false) + "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("</td>");
    	}
    	html.append("</tr>");
    	html.append("<tr>");
    	html.append("<td>" + relationshipXML.getfieldTransl("id_club_settlem_accnt", true));
    	html.append(getGoToHyperLink(
    			"CLIENTS_BANK_ACCOUNTS",
    			this.getValue("ID_PARTY1_SETTLEM_ACCNT"),
    			"../crm/clients/accountspecs.jsp?id=" + this.getValue("ID_PARTY1_SETTLEM_ACCNT")
    		));
    	html.append("</td>");
    	html.append("<td>");
    	html.append("<input type=\"hidden\" id=\"id_party1_settlem_accnt\" name=\"id_party1_settlem_accnt\" value=\"" + this.getValue("ID_PARTY1_SETTLEM_ACCNT")+ "\" readonly class=\"inputfield\">");
    	html.append("<input type=\"text\" id=\"name_party1_settlem_accnt\" name=\"name_party1_settlem_accnt\" size=\"37\" value=\"" + getBankAccountNumberAndName(this.getValue("ID_PARTY1_SETTLEM_ACCNT"))+ "\" readonly class=\"inputfield\">");
    	html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findbankaccounts2.jsp?id_bank_account=" + this.getValue("ID_PARTY1_SETTLEM_ACCNT") + "&field=party1_settlem_accnt&id_jur_prs='+getElementById('id_party1').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\"  title=\"" + buttonXML.getfieldTransl("button_edit", false)+ "\">");
    	html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\"></a>");
    	html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_party1_settlem_accnt').value = ''; document.getElementById('name_party1_settlem_accnt').value = ''; return false;\" title=\"" + buttonXML.getfieldTransl("button_delete", false)+ "\">");
    	html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\"></a>");
    	html.append("</td>");
    	if (!("SOCIETY-SHAREHOLDER".equalsIgnoreCase(rel_type) || "SOCIETY-OTHER".equalsIgnoreCase(rel_type))) {
    		html.append("<td>" + relationshipXML.getfieldTransl("id_partner_settlem_accnt", true));
        	html.append(getGoToHyperLink(
        			"CLIENTS_BANK_ACCOUNTS",
        			this.getValue("ID_PARTY2_SETTLEM_ACCNT"),
        			"../crm/clients/accountspecs.jsp?id=" + this.getValue("ID_PARTY2_SETTLEM_ACCNT")
        		));
        	html.append("</td>");
    		html.append("<td>");
    		html.append("<input type=\"hidden\" id=\"id_party2_settlem_accnt\" name=\"id_party2_settlem_accnt\" value=\"" + this.getValue("ID_PARTY2_SETTLEM_ACCNT")+ "\" readonly class=\"inputfield\">");
    		html.append("<input type=\"text\" id=\"name_party2_settlem_accnt\" name=\"name_party2_settlem_accnt\" size=\"37\" value=\"" + getBankAccountNumberAndName(this.getValue("ID_PARTY2_SETTLEM_ACCNT"))+ "\" readonly class=\"inputfield\">");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findbankaccounts2.jsp?id_bank_account=" + this.getValue("ID_PARTY2_SETTLEM_ACCNT")+ "&field=party2_settlem_accnt&id_jur_prs='+getElementById('id_party2').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\"  title=\"" + buttonXML.getfieldTransl("button_edit", false)+ "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_party2_settlem_accnt').value = ''; document.getElementById('name_party2_settlem_accnt').value = ''; return false;\" title=\"" + buttonXML.getfieldTransl("button_delete", false) + "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("</td>");
    	}
    	html.append("</tr>");

    	if ("SOCIETY-ISSUER".equalsIgnoreCase(rel_type)) {
    		html.append("<tr>");
    		html.append("<td valign=\"top\">&nbsp;</td><td valign=\"top\">&nbsp;</td>");
    		html.append("<td valign=\"top\">" + relationshipXML.getfieldTransl("id_issuer_club_bon_accnt", true));
        	html.append(getGoToHyperLink(
        			"CLIENTS_BANK_ACCOUNTS",
        			this.getValue("ID_PARTY1_CLUB_BON_ACCNT"),
        			"../crm/clients/accountspecs.jsp?id=" + this.getValue("ID_PARTY1_CLUB_BON_ACCNT")
        		));
        	html.append("</td>");
    		html.append("<td valign=\"top\">");
    		html.append("<input type=\"hidden\" id=\"id_party1_club_bon_accnt\" name=\"id_party1_club_bon_accnt\" value=\"" + this.getValue("ID_PARTY1_CLUB_BON_ACCNT")+ "\" readonly class=\"inputfield\">");
    		html.append("<input type=\"text\" id=\"name_party1_club_bon_accnt\" name=\"name_party1_club_bon_accnt\" size=\"37\" value=\"" + getBankAccountNumberAndName(this.getValue("ID_PARTY1_CLUB_BON_ACCNT"))+ "\" readonly class=\"inputfield\">");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findbankaccounts2.jsp?id_bank_account=" + this.getValue("ID_PARTY1_CLUB_BON_ACCNT")+ "&field=party1_club_bon_accnt&id_jur_prs='+getElementById('id_party1').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\"  title=\"" + buttonXML.getfieldTransl("button_edit", false) + "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_party1_club_bon_accnt').value = ''; document.getElementById('name_party1_club_bon_accnt').value = ''; return false;\" title=\"" + buttonXML.getfieldTransl("button_delete", false)+ "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("</td>");
    		html.append("</tr>");
    	}
	
    	if ("SOCIETY-DEALER".equalsIgnoreCase(rel_type)) {
    		html.append("<tr>");
    		html.append("<td valign=\"top\">" + relationshipXML.getfieldTransl("id_club_distrib_accnt", true));
        	html.append(getGoToHyperLink(
        			"CLIENTS_BANK_ACCOUNTS",
        			this.getValue("ID_PARTY1_CLUB_DISTRIB_ACCNT"),
        			"../crm/clients/accountspecs.jsp?id=" + this.getValue("ID_PARTY1_CLUB_DISTRIB_ACCNT")
        		));
        	html.append("</td>");
    		html.append("<td valign=\"top\">");
    		html.append("<input type=\"hidden\" id=\"id_party1_club_distrib_accnt\" name=\"id_party1_club_distrib_accnt\" value=\"" + this.getValue("ID_PARTY1_CLUB_DISTRIB_ACCNT")+ "\" readonly class=\"inputfield\">");
    		html.append("<input type=\"text\" id=\"name_party1_club_distrib_accnt\" name=\"name_party1_club_distrib_accnt\" size=\"37\" value=\"" + getBankAccountNumberAndName(this.getValue("ID_PARTY1_CLUB_DISTRIB_ACCNT"))+ "\" readonly class=\"inputfield\">");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('../crm/services/findbankaccounts2.jsp?id_bank_account=" + this.getValue("ID_PARTY1_CLUB_DISTRIB_ACCNT")+ "&field=party1_club_distrib_accnt&id_jur_prs='+getElementById('id_party1').value,'blank','height=600,width=900,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\"  title=\"" + buttonXML.getfieldTransl("button_edit", false) + "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/change.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("<A HREF=\"#\" onClick=\"JavaScript: document.getElementById('id_party1_club_distrib_accnt').value = ''; document.getElementById('name_party1_club_distrib_accnt').value = ''; return false;\" title=\"" + buttonXML.getfieldTransl("button_delete", false)+ "\">");
    		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/window_open/delete.png\" align=\"top\" style=\"border: 0px;\"></a>");
    		html.append("</td>");
    		html.append("<td valign=\"top\">&nbsp;</td><td valign=\"top\">&nbsp;</td>");
    		html.append("</tr>");
    	}
		html.append("<tr>");
		html.append("<td colspan=4 class=\"top_line\"><b>" + commonXML.getfieldTransl("h_record_param", false) + "</b></td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td>" + commonXML.getfieldTransl("creation_date", false) + "</td> <td><input type=\"text\" name=\"creation_date\" size=\"20\" value=\"" + this.getValue("CREATION_DATE_DHMSF") + "\" readonly class=\"inputfield-ro\"> </td>");
		html.append("<td>" + commonXML.getfieldTransl("last_update_date",false) + "</td> <td><input type=\"text\" name=\"last_update_date\" size=\"20\" value=\"" + this.getValue("LAST_UPDATE_DATE_DHMSF") + "\" readonly class=\"inputfield-ro\"> </td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td>" + commonXML.getfieldTransl("created_by", false) + "</td> <td><input type=\"text\" name=\"created_by\" size=\"20\" value=\"" + getSystemUserName(this.getValue("CREATED_BY")) + "\" readonly class=\"inputfield-ro\"> </td>");
		html.append("<td>" + commonXML.getfieldTransl("last_update_by", false) + "</td> <td><input type=\"text\" name=\"last_update_by\" size=\"20\" value=\"" + getSystemUserName(this.getValue("LAST_UPDATE_BY")) + "\" readonly class=\"inputfield-ro\"> </td>");
		html.append("</tr>");

    	return html.toString();
    }
    
    public String getClubRelPreviewHTML(String dateFormatTitle) {
    	StringBuilder html = new StringBuilder();
    	
    	String rel_type = this.getValue("CD_CLUB_REL_TYPE");
    	String name_party1 = getClubRelNameParty1(rel_type);
		String name_party2 = getClubRelNameParty2(rel_type);
		
		
    	html.append("<tr>");
    	html.append("<td>" + relationshipXML.getfieldTransl("id_club_rel", false) + "</td><td><input type=\"text\" name=\"id_club_rel\" size=\"20\" value=\"" + this.getValue("ID_CLUB_REL") + "\" readonly class=\"inputfield-ro\"></td>");
    	html.append("<td>" + clubXML.getfieldTransl("club", false));
		html.append(getGoToHyperLink(
				"CLUB_CLUB",
				this.getValue("ID_CLUB"),
				"../crm/club/clubspecs.jsp?id=" + this.getValue("ID_CLUB")
			));
    	html.append("</td>");
    	html.append("<td>");
		html.append("<input type=\"text\" name=\"name_club\" id=\"name_club\" size=\"44\" value=\"" + getClubShortName(this.getValue("ID_CLUB")) + "\" readonly class=\"inputfield-ro\">");
		html.append("</td>");
    	html.append("</tr>");
    	html.append("<tr>");
    	html.append("<td>" + relationshipXML.getfieldTransl("cd_club_rel_type", false) + "</td><td><input type=\"text\" name=\"name_club_rel_type\" size=\"45\" value=\"" + this.getValue("NAME_CLUB_REL_TYPE") + "\" readonly class=\"inputfield-ro\"></td>");
    	html.append("<td rowspan=\"2\" valign=\"top\">" + relationshipXML.getfieldTransl("desc_club_rel", false) + "</td><td rowspan=\"2\" valign=\"top\"><textarea name=\"desc_club_rel\" cols=\"40\" rows=\"2\" readonly class=\"inputfield-ro\">" + this.getValue("DESC_CLUB_REL")+ "</textarea></td>");
    	html.append("</tr>");
    	html.append("<tr>");
    	html.append("<td>" + relationshipXML.getfieldTransl("date_club_rel", false) + "</td><td><input type=\"text\" name=\"date_club_rel\" size=\"20\" value=\"" + this.getValue("DATE_CLUB_REL_FRMT") + "\" readonly class=\"inputfield-ro\"></td>");
    	html.append("</tr>");

    	html.append("<tr><td colspan=\"4\" class=\"top_line\"><b>" + relationshipXML.getfieldTransl("h_participants", false) + "</b></td></tr>");
    	
    	html.append("<tr>");
    	
    	if (!("DEALER-TECHNICAL_ACQUIRER".equalsIgnoreCase(rel_type) || "DEALER-FINANCE_ACQUIRER".equalsIgnoreCase(rel_type))) {
    		html.append("<td>" + relationshipXML.getfieldTransl(name_party1, false));
    		html.append(getGoToHyperLink(
    				"CLIENTS_YURPERSONS",
    				this.getValue("ID_PARTY1"),
    				"../crm/clients/yurpersonspecs.jsp?id=" + this.getValue("ID_PARTY1")
    			));
    		
    		html.append("</td>"); 
    		html.append("<td>");
    		html.append("<input type=\"text\" name=\"name_party1\" id=\"name_party1\" size=\"45\" value=\"" + this.getValue("SNAME_PARTY1") + "\" readonly class=\"inputfield-ro\">");
    		html.append("</td>");
    	} else {
    		html.append("<td>" + relationshipXML.getfieldTransl(name_party1, false));
    		html.append(getGoToHyperLink(
    				"CLIENTS_YURPERSONS",
    				this.getValue("ID_PARTY1"),
    				"../crm/clients/yurpersonspecs.jsp?id=" + this.getValue("ID_PARTY1")
    			));
    		
    		html.append("</td>"); 
    		html.append("<td>");
    		html.append("<input type=\"text\" id=\"name_party1\" name=\"name_party1\" size=\"45\" value=\"" + this.getValue("SNAME_PARTY1") + "\" readonly class=\"inputfield-ro\">");
    		html.append("</td>");
    	}
    	
    	if (!("SOCIETY-SHAREHOLDER".equalsIgnoreCase(rel_type) || "SOCIETY-OTHER".equalsIgnoreCase(rel_type))) {
    		html.append("<td>" + relationshipXML.getfieldTransl(name_party2, false));
    		html.append(getGoToHyperLink(
    				"CLIENTS_YURPERSONS",
    				this.getValue("ID_PARTY2"),
    				"../crm/clients/yurpersonspecs.jsp?id=" + this.getValue("ID_PARTY2")
    			));
    		
    		html.append("</td>");
    		html.append("<td>");
    		html.append("<input type=\"text\" id=\"name_party2\" name=\"name_party2\" size=\"45\" value=\"" + this.getValue("SNAME_PARTY2") + "\" readonly class=\"inputfield-ro\">");
    		html.append("</td>");
    	}
    	html.append("</tr>");
    	html.append("<tr>");
    	html.append("<td>" + relationshipXML.getfieldTransl("id_club_settlem_accnt", false));
    	html.append(getGoToHyperLink(
    			"CLIENTS_BANK_ACCOUNTS",
    			this.getValue("ID_PARTY1_SETTLEM_ACCNT"),
    			"../crm/clients/accountspecs.jsp?id=" + this.getValue("ID_PARTY1_SETTLEM_ACCNT")
    		));
    	html.append("</td>");
    	html.append("<td>");
    	html.append("<input type=\"text\" id=\"name_party1_settlem_accnt\" name=\"name_party1_settlem_accnt\" size=\"45\" value=\"" + getBankAccountNumberAndName(this.getValue("ID_PARTY1_SETTLEM_ACCNT"))+ "\" readonly class=\"inputfield-ro\">");
    	html.append("</td>");
    	if (!("SOCIETY-SHAREHOLDER".equalsIgnoreCase(rel_type) || "SOCIETY-OTHER".equalsIgnoreCase(rel_type))) {
    		html.append("<td>" + relationshipXML.getfieldTransl("id_partner_settlem_accnt", false));
        	html.append(getGoToHyperLink(
        			"CLIENTS_BANK_ACCOUNTS",
        			this.getValue("ID_PARTY2_SETTLEM_ACCNT"),
        			"../crm/clients/accountspecs.jsp?id=" + this.getValue("ID_PARTY2_SETTLEM_ACCNT")
        		));
        	html.append("</td>");
    		html.append("<td>");
    		html.append("<input type=\"text\" id=\"name_party2_settlem_accnt\" name=\"name_party2_settlem_accnt\" size=\"45\" value=\"" + getBankAccountNumberAndName(this.getValue("ID_PARTY2_SETTLEM_ACCNT"))+ "\" readonly class=\"inputfield-ro\">");
    		html.append("</td>");
    	}
    	html.append("</tr>");

    	if ("SOCIETY-ISSUER".equalsIgnoreCase(rel_type)) {
    		html.append("<tr>");
    		html.append("<td valign=\"top\">&nbsp;</td><td valign=\"top\">&nbsp;</td>");
    		html.append("<td valign=\"top\">" + relationshipXML.getfieldTransl("id_issuer_club_bon_accnt", false));
        	html.append(getGoToHyperLink(
        			"CLIENTS_BANK_ACCOUNTS",
        			this.getValue("ID_PARTY1_CLUB_BON_ACCNT"),
        			"../crm/clients/accountspecs.jsp?id=" + this.getValue("ID_PARTY1_CLUB_BON_ACCNT")
        		));
        	html.append("</td>");
    		html.append("<td valign=\"top\">");
    		html.append("<input type=\"text\" id=\"name_party1_club_bon_accnt\" name=\"name_party1_club_bon_accnt\" size=\"45\" value=\"" + getBankAccountNumberAndName(this.getValue("ID_PARTY1_CLUB_BON_ACCNT"))+ "\" readonly class=\"inputfield-ro\">");
    		html.append("</td>");
    		html.append("</tr>");
    	}
	
    	if ("SOCIETY-DEALER".equalsIgnoreCase(rel_type)) {
    		html.append("<tr>");
    		html.append("<td valign=\"top\">" + relationshipXML.getfieldTransl("id_club_distrib_accnt", false));
        	html.append(getGoToHyperLink(
        			"CLIENTS_BANK_ACCOUNTS",
        			this.getValue("ID_PARTY1_CLUB_DISTRIB_ACCNT"),
        			"../crm/clients/accountspecs.jsp?id=" + this.getValue("ID_PARTY1_CLUB_DISTRIB_ACCNT")
        		));
        	html.append("</td>");
    		html.append("<td valign=\"top\">");
    		html.append("<input type=\"text\" id=\"name_party1_club_distrib_accnt\" name=\"name_party1_club_distrib_accnt\" size=\"45\" value=\"" + getBankAccountNumberAndName(this.getValue("ID_PARTY1_CLUB_DISTRIB_ACCNT"))+ "\" readonly class=\"inputfield-ro\">");
    		html.append("</td>");
    		html.append("<td valign=\"top\">&nbsp;</td><td valign=\"top\">&nbsp;</td>");
    		html.append("</tr>");
    	}
		html.append("<tr>");
		html.append("<td colspan=4 class=\"top_line\"><b>" + commonXML.getfieldTransl("h_record_param", false) + "</b></td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td>" + commonXML.getfieldTransl("creation_date", false) + "</td> <td><input type=\"text\" name=\"creation_date\" size=\"20\" value=\"" + this.getValue("CREATION_DATE_DHMSF") + "\" readonly class=\"inputfield-ro\"> </td>");
		html.append("<td>" + commonXML.getfieldTransl("last_update_date",false) + "</td> <td><input type=\"text\" name=\"last_update_date\" size=\"20\" value=\"" + this.getValue("LAST_UPDATE_DATE_DHMSF") + "\" readonly class=\"inputfield-ro\"> </td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td>" + commonXML.getfieldTransl("created_by", false) + "</td> <td><input type=\"text\" name=\"created_by\" size=\"20\" value=\"" + getSystemUserName(this.getValue("CREATED_BY")) + "\" readonly class=\"inputfield-ro\"> </td>");
		html.append("<td>" + commonXML.getfieldTransl("last_update_by", false) + "</td> <td><input type=\"text\" name=\"last_update_by\" size=\"20\" value=\"" + getSystemUserName(this.getValue("LAST_UPDATE_BY")) + "\" readonly class=\"inputfield-ro\"> </td>");
		html.append("</tr>");

    	return html.toString();
    }
}
