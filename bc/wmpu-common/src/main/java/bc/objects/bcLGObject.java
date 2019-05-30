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

public class bcLGObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcLGObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	private String bonCardFeatureTable = "VC_LG_CARD_PRIV_ALL";
	private String questionnaireFeatureTable = "VC_LG_QUESTIONNAIRE_PRIV_ALL";
	private String terminalFeatureTable = "VC_LG_TERMINAL_PRIV_ALL";
	private String samFeatureTable = "VC_LG_SAM_PRIV_ALL";
	private String giftFeatureTable = "VC_LG_GIFTS_PRIV_ALL";
	private String otherFeatureTable = "VC_LG_OTHER_PRIV_ALL";
	
	private String generalFeatureTable = "VC_LG_CLUB_ALL";
	
	private String featureTable = "";
	
	private String typeLG;
	private String idRecord;
	
	public bcLGObject(String pTypeLG, String pIdRecord) {
		this.typeLG = pTypeLG;
		this.idRecord = pIdRecord;
		getFeature();
	}

	public bcLGObject(String pIdRecord) {
		this.typeLG = "GENERAL";
		this.idRecord = pIdRecord;

		featureTable = generalFeatureTable;
		getFeature();
	}
	
	private void getFeature() {

		if ("BON_CARD".equalsIgnoreCase(this.typeLG)) {
			featureTable = bonCardFeatureTable;
        } else if ("QUESTIONNAIRE".equalsIgnoreCase(this.typeLG)) {
        	featureTable = questionnaireFeatureTable;
        } else if ("TERMINAL".equalsIgnoreCase(this.typeLG)) {
        	featureTable = terminalFeatureTable;
        } else if ("SAM".equalsIgnoreCase(this.typeLG)) {
        	featureTable = samFeatureTable;
        } else if ("OTHER".equalsIgnoreCase(this.typeLG)) {
        	featureTable = otherFeatureTable;
        } else if ("GIFT".equalsIgnoreCase(this.typeLG)) {
        	featureTable = giftFeatureTable;
        } else if ("GENERAL".equalsIgnoreCase(this.typeLG)) {
        	featureTable = generalFeatureTable;
        }
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + "." + featureTable + " WHERE id_lg_record = ?";
		fieldHm = getFeatures2(mySQL, this.idRecord, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getBonCardsRangesHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT begin_cd_card2, end_cd_card2, sname_issuer, " +
        	"        name_payment_system, name_card_status, card_must_found, card_found, " +
        	"        id_issuer, id_lg_card_range "+
            "   FROM (SELECT ROWNUM rn, a.begin_cd_card2, a.end_cd_card2, a.sname_issuer, " +
        	"                a.name_payment_system, a.name_card_status, " +
        	"                CASE WHEN NVL(a.card_found,0) <> NVL(a.card_must_found, 0) " +
        	"                     THEN '<font color=\"red\"><b>'||TO_CHAR(a.card_must_found)||'</b></font>'" +
        	"                     ELSE TO_CHAR(a.card_must_found) " +
        	"                END card_must_found, " +
        	"                CASE WHEN NVL(a.card_found,0) <> NVL(a.card_must_found, 0) " +
        	"                     THEN '<font color=\"red\"><b>'||TO_CHAR(a.card_found)||'</b></font>'" +
        	"                     ELSE TO_CHAR(a.card_found) " +
        	"                END card_found, " +
        	"                a.id_lg_card_range, a.id_issuer "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_card_rng_priv_all "+
            "                  WHERE id_lg_record = ? ";
        pParam.add(new bcFeautureParam("int", this.idRecord));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(sname_issuer) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_payment_system) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_card_status) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
            "                  ORDER BY begin_cd_card2) a " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasEditPermission = false;
        boolean hasJurPrsPermission = false;
        
        try{
        	if (isEditPermited("LOGISTIC_PARTNERS_CARDS_RANGE")>0) {
        		hasEditPermission = true;
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
            for (int i=1; i <= colCount-2; i++) {
               html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>");
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
	          	  	if ("SNAME_ISSUER".equalsIgnoreCase(mtd.getColumnName(i)) && hasJurPrsPermission) {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id=" + rset.getString("ID_ISSUER"), "", ""));
	      	  		} else {
	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	      	  		}
          	  	}
                if (hasEditPermission) {
	            	 String myHyperLink = "../crm/logistic/partners/cardupdate.jsp?type=range&id=" + this.idRecord + "&line="+rset.getString("ID_LG_CARD_RANGE");
              	     String myDeleteLink = "../crm/logistic/partners/cardupdate.jsp?type=range&id=" + this.idRecord + "&line=" + rset.getString("ID_LG_CARD_RANGE")+"&action=remove&process=yes";
		             html.append(getDeleteButtonHTML(myDeleteLink, logisticXML.getfieldTransl("h_range_delete", false), rset.getString("BEGIN_CD_CARD2") + " - " + rset.getString("END_CD_CARD2")));
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
    }
	
    public String getBonCardsListHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, cd_card1, cd_card2, date_open_frmt, " +
        	"        date_close_frmt, expiry_date_frmt, " +
        	"        name_card_status, name_card_state," +
        	"        name_card_type, name_bon_category, name_disc_category," +
        	"        card_serial_number, id_issuer, id_payment_system "+
            "   FROM (SELECT ROWNUM rn, a.cd_card1, a.cd_card2, a.date_open_frmt, " +
        	"                a.date_close_frmt, a.expiry_date_frmt, " +
        	"                a.name_card_status, a.name_card_state," +
        	"                a.name_card_type, a.name_bon_category, a.name_disc_category," +
        	"                a.card_serial_number, a.id_issuer, a.id_payment_system "+
            "           FROM (SELECT v.* " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_card_rng_card_all v "+
            "                  WHERE v.id_lg_record = ? ";
        pParam.add(new bcFeautureParam("int", this.idRecord));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + " AND UPPER(cd_card1) LIKE UPPER('%'||?||'%'))";
    		pParam.add(new bcFeautureParam("string", pFindString));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
            "                  ORDER BY v.cd_card1) a " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasPermission = false;
        
        try{
        	if (isEditMenuPermited("CARDS_CLUBCARDS")>=0) {
        		hasPermission = true;
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
               html.append(getBottomFrameTableTH(clubcardXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {

          	  	html.append("<tr>");
          	  	for (int i=1; i <= colCount-3; i++) {
          	  		if (hasPermission && "CD_CARD1".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardspecs.jsp?id="+rset.getString("CARD_SERIAL_NUMBER")+"&iss="+rset.getString("ID_ISSUER")+"&paysys="+rset.getString("ID_PAYMENT_SYSTEM"), "", ""));
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
	
    public String getDocumentsListHTML(String pTabSheetName, String pUpdatePath, String pFindString, String pDocType, String pDocState, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        String src_doc = "";

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT id_doc, name_doc_type, name_doc_state, full_doc, sname_jur_prs_party1, sname_jur_prs_party2, " +
        	"        sname_jur_prs_party3, src_doc, file_doc "+
            "   FROM (SELECT ROWNUM rn, id_doc, name_doc_type, " +
            "                DECODE(cd_doc_state, " +
            "               		'SIGNED', '<font color=\"green\"><b>'||name_doc_state||'</b></font>', " +
            "               		'FINISCHED', '<font color=\"red\">'||name_doc_state||'</font>', " +
            "               		name_doc_state" +
            "        		 ) name_doc_state, " +
            "                full_doc, sname_jur_prs_party1, " +
            "                sname_jur_prs_party2, sname_jur_prs_party3, src_doc, file_doc "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_doc_priv_all " + 
            "                  WHERE id_lg_record = ? ";
        pParam.add(new bcFeautureParam("int", this.idRecord));
        
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
            " ) WHERE rn >= ?";
        boolean hasEditPermission = false;
        boolean hasDocPermission = false;
        try{
        	if (isEditPermited(pTabSheetName)>0) {
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
            	   String myHyperLink = pUpdatePath + "?lg_type=" + this.typeLG + "&type=doc&id=" + this.idRecord + "&id_doc=" + rset.getString("ID_DOC");
            	   String myDeleteLink = pUpdatePath + "?lg_type=" + this.typeLG + "&type=doc&id=" + this.idRecord + "&id_doc=" + rset.getString("ID_DOC")+"&action=remove&process=yes";
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
	
    public String getSAMsListHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_sam, sam_serial_number, name_sam_status, id_term, name_term_type, " +
        	"        date_beg_frmt, date_end_frmt, id_lg_sam, sam_exist "+
            "   FROM (SELECT ROWNUM rn, id_sam, sam_serial_number, name_sam_status, id_term, name_term_type, " +
            "                date_beg_frmt, date_end_frmt, id_lg_sam, sam_exist "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_sam_all " + 
            "                  WHERE id_lg_record = ? ";
        pParam.add(new bcFeautureParam("int", this.idRecord));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_sam) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sam_serial_number) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<2; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
            "                  ORDER BY id_sam " +
            "         ) WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        boolean hasEditPermission = false;
        boolean hasSAMPermission = false;
        boolean hasTerminalPermission = false;
        try{
        	if (isEditPermited("LOGISTIC_PARTNERS_SAMS_SAMS")>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
        		hasTerminalPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_SAM")>=0) {
        		hasSAMPermission = true;
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
               html.append(getBottomFrameTableTH(samXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
            	if ("Y".equalsIgnoreCase(rset.getString("SAM_EXIST"))) {
          	  		for (int i=1; i <= colCount-2; i++) {
          	  		  	if ("ID_SAM".equalsIgnoreCase(mtd.getColumnName(i)) && hasSAMPermission) {
              	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/samspecs.jsp?id="+rset.getString("ID_SAM"), "", ""));
    	          	  	} else if ("ID_TERM".equalsIgnoreCase(mtd.getColumnName(i)) && hasTerminalPermission) {
              	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString("ID_TERM"), "", ""));
    	          	  	} else {
    	      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
    	      	  		}
          	  		}
            	} else {
            		html.append(getBottomFrameTableTD("RN", rset.getString("RN"), "", "", ""));
      	  			html.append(getBottomFrameTableTD("ID_SAM", rset.getString("ID_SAM"), "", "", ""));
      	  			html.append("<td colspan=\"5\">&nbsp;</td>\n");
      	  		}
          	  	if (hasEditPermission) {
          	  		String myDeleteLink = "../crm/logistic/partners/samupdate.jsp?type=sam&id=" + this.idRecord + "&line=" + rset.getString("ID_LG_SAM")+"&action=remove&process=yes";
          	  		html.append(getDeleteButtonHTML(myDeleteLink, logisticXML.getfieldTransl("h_delete_sam", false), rset.getString("ID_SAM")));
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

    public String getSAMEditListHTML(String p_beg, String p_end) {
        String mySQL = 
        	" SELECT rn, id_sam, id_term, name_sam_type, " +
       	   	"		 name_sam_status, date_beg_frmt, date_end_frmt, sam_selected, id_lg_sam " + 
       	   	"   FROM (SELECT ROWNUM rn, id_sam, id_term, name_sam_type, " +
       	   	"				 name_sam_status, date_beg_frmt, date_end_frmt, sam_selected, id_lg_sam " + 
       	   	"           FROM (SELECT id_sam, id_term, name_sam_type, " + 
       	   	"  					     name_sam_status, date_beg_frmt, date_end_frmt, id_lg_sam, " + 
       	   	"						 DECODE(id_sam_logistic, NULL, 'N', 'Y') sam_selected " + 
       	   	"					FROM (SELECT a.id_sam, a.id_term, a.name_sam_type, " + 
            "								 a.name_sam_status, a.date_beg_frmt, " +
            "                                a.date_end_frmt, l.id_lg_sam, l.id_sam id_sam_logistic " + 
            "						    FROM " + getGeneralDBScheme() + ".vc_sam_club_all a " +
            "                           left JOIN (SELECT id_sam, id_lg_sam " +
            "                                        FROM " + getGeneralDBScheme() + ".vc_lg_sam_all " +
            "                                       WHERE id_lg_record = " + this.idRecord + ") l " + 
            "							  ON (a.id_sam = l.id_sam))" +
            "                   ORDER BY DECODE(id_sam_logistic, NULL, 2, 1), id_sam) " +
       	   	"          WHERE ROWNUM < " + p_end + 
       	   	" ) WHERE rn >= " + p_beg;
          
          StringBuilder html = new StringBuilder();
          Connection con = null;
          PreparedStatement st = null;
          String myFont = "";
          String myFontEnd = "";
          String myBGColor = "";
          
          boolean hasPermission = false;

         try{
        	 if (isEditPermited("LOGISTIC_PARTNERS_SAMS_SAMS")>0) {
        		 hasPermission = true;
        	 }
        	 
        	 LOGGER.debug(mySQL + 
            		", 1={" + this.idRecord + ",int}" + 
            		", 2={" + p_end + ",int}" + 
            		", 3={" + p_beg + ",int}");
        	 con = Connector.getConnection(getSessionId());
             st = con.prepareStatement(mySQL);
             st.setInt(1, Integer.parseInt(this.idRecord));
             st.setInt(2, Integer.parseInt(p_end));
             st.setInt(3, Integer.parseInt(p_beg));
             ResultSet rset = st.executeQuery();
             ResultSetMetaData mtd = rset.getMetaData();
                
             int colCount = mtd.getColumnCount();
             
             if (hasPermission) {
            	 html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/logistic/partners/samupdate.jsp\">\n");
            	 html.append("<input type=\"hidden\" name=\"action\" value=\"add_list\">\n");
            	 html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            	 html.append("<input type=\"hidden\" name=\"type\" value=\"sam\">\n");
            	 html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idRecord + "\">\n");
             }
             html.append(getBottomFrameTable());
             html.append("<tr>");
             for (int i=1; i <= colCount-1; i++) {
            	 String colName = mtd.getColumnName(i);                            
                 if ("SAM_SELECTED".equalsIgnoreCase(mtd.getColumnName(i))) {
                     if (hasPermission) {
                       	 html.append("<th>"+ logisticXML.getfieldTransl("h_object_selected", false)+
                       		"<input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb_sam')\"></th>\n");
                        } 
                 } else {
                	 html.append(getBottomFrameTableTH(samXML, colName));
                 }
             }
             if (hasPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\"8\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/logistic/partners/samupdate.jsp"));
             	html.append(getGoBackButton("../crm/logistic/partners/samspecs.jsp?id=" + this.idRecord));
             	html.append("</td>");
             	html.append("</tr>");
             }

             html.append("</tr></thead><tbody>\n");
             int rowCount = 0;
             while (rset.next()) {
             	rowCount++;
                String jurPrsID="id_sam_"+rset.getString("ID_SAM");
                String tprvCheck="prv_sam_"+rset.getString("ID_LG_SAM");
                String tCheck="chb_sam_"+rset.getString("ID_SAM");
                
                if(rset.getString("SAM_SELECTED").equalsIgnoreCase("Y")){
                	myFont = "<b>";
                	myFontEnd = "</b>";
                	myBGColor = selectedBackGroundStyle;
                } else {
                	myFont = "";
                	myFontEnd = "";
                	myBGColor = "";
                }
                    
                html.append("<tr>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(1)) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(2)) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(3)) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(4)) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(5)) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(6)) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(7)) + myFontEnd + "</td>\n");
                if (rset.getString("SAM_SELECTED").equalsIgnoreCase("Y") && hasPermission){
                  	html.append("<td " + myBGColor + "><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked onclick=\"return CheckCB(this);\"></td>\n");
                  	html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
                } else { 
                    html.append("<td><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\"></td>\n");
                }
                        
                html.append("<INPUT type=hidden  value="+rset.getString("ID_SAM")+" name="+jurPrsID+">");
                html.append("</tr> ");        
            }

            if (hasPermission) {
            	html.append("<tr>");
            	html.append("<td colspan=\"8\" align=\"center\">");
            	html.append(getSubmitButtonAjax("../crm/logistic/partners/samupdate.jsp"));
             	html.append(getGoBackButton("../crm/logistic/partners/samspecs.jsp?id=" + this.idRecord));
            	html.append("</td>");
            	html.append("</tr>");
            }
            html.append("<input type=hidden value="+rowCount+" name=rowCount>");
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
    } //getUserJurPrsPrivilegesHTML
	
    public String getTerminalsListHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_term, name_device_type, name_term_status, sname_term_owner, " +
        	"        sname_finance_acquirer, id_lg_term, term_exist, id_device_type "+
            "   FROM (SELECT ROWNUM rn, id_term, name_device_type, " +
            "				 DECODE(cd_term_status, " +
			"                  	    'ACTIVE', '<b><font color=\"green\">'||name_term_status||'</font></b>', " +
			"                       'SETTING', '<font color=\"red\">'||name_term_status||'</font>', " +
			"                       'EXCLUDED', '<b><font color=\"red\">'||name_term_status||'</font></b>', " +
			"                       'BLOCKED', '<b><font color=\"red\">'||name_term_status||'</font></b>', " +
			"                       name_term_status" +
			"                ) name_term_status, " +
            "				 sname_term_owner, sname_finance_acquirer, id_lg_term, term_exist, id_device_type "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_term_all " + 
            "                  WHERE id_lg_record = ? ";
        pParam.add(new bcFeautureParam("int", this.idRecord));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_term_owner) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_finance_acquirer) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
            "                  ORDER BY id_term " +
            "         ) WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        boolean hasEditPermission = false;
        boolean hasTerminalPermission = false;
        boolean hasDeviceTypePermission = false;
        
        try{
        	if (isEditPermited("LOGISTIC_PARTNERS_TERMINALS_TERMINALS")>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
        		hasTerminalPermission = true;
        	}
        	if (isEditMenuPermited("CLUB_TERM_DEVICE_TYPE")>=0) {
        		hasDeviceTypePermission = true;
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
               html.append(getBottomFrameTableTH(terminalXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
            	if ("Y".equalsIgnoreCase(rset.getString("TERM_EXIST"))) {
	          	  	for (int i=1; i <= colCount-3; i++) {
		          	  	if ("ID_TERM".equalsIgnoreCase(mtd.getColumnName(i)) && hasTerminalPermission) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString("ID_TERM"), "", ""));
		          	  } else if (hasDeviceTypePermission && "NAME_DEVICE_TYPE".equalsIgnoreCase(mtd.getColumnName(i)) &&
	         	  				!isEmpty(rset.getString("ID_DEVICE_TYPE"))) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/term_device_typespecs.jsp?id="+rset.getString("ID_DEVICE_TYPE"), "", ""));
	         	  		} else {
		      	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
		      	  		}
	          	  	}
            	} else {
            		html.append(getBottomFrameTableTD("RN", rset.getString("RN"), "", "", ""));
      	  			html.append(getBottomFrameTableTD("ID_TERM", rset.getString("ID_TERM"), "", "", ""));
      	  			html.append("<td colspan=\"4\">&nbsp;</td>\n");
      	  		}
                if (hasEditPermission) {
            	   String myDeleteLink = "../crm/logistic/partners/terminalupdate.jsp?type=term&id=" + this.idRecord + "&line=" + rset.getString("ID_LG_TERM")+"&action=remove&process=yes";
                   html.append(getDeleteButtonHTML(myDeleteLink, logisticXML.getfieldTransl("h_delete_term", false), rset.getString("ID_TERM")));
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

    public String getTerminalsEditListHTML(String p_beg, String p_end) {
        final String mySQL = 
        	" SELECT rn, id_term, name_device_type, name_term_type, " +
       	   	"		 name_term_status, sname_term_owner, sname_finance_acquirer, term_selected, id_lg_term " + 
       	   	"   FROM (SELECT ROWNUM rn, id_term, name_device_type, name_term_type, " +
       	   	"				 name_term_status, sname_term_owner, sname_finance_acquirer, term_selected, id_lg_term " + 
       	   	"           FROM (SELECT id_term, name_device_type, name_term_type, " + 
       	   	"  					     name_term_status, sname_term_owner, sname_finance_acquirer, id_lg_term, " + 
       	   	"						 DECODE(id_term_logistic, NULL, 'N', 'Y') term_selected " + 
       	   	"					FROM (SELECT a.id_term, a.name_device_type, " +
       	   	"								 DECODE(a.cd_term_status, " +
		   	"                       				'ACTIVE', '<b><font color=\"green\">'||a.name_term_status||'</font></b>', " +
			"                       				'SETTING', '<font color=\"red\">'||a.name_term_status||'</font>', " +
			"                       				'EXCLUDED', '<b><font color=\"red\">'||a.name_term_status||'</font></b>', " +
			"                       				'BLOCKED', '<b><font color=\"red\">'||a.name_term_status||'</font></b>', " +
		   	"                               		a.name_term_status" +
		   	"                        		 ) name_term_status, " +
       	   	"								 a.name_term_type, " + 
            "								 a.sname_term_owner, a.sname_finance_acquirer, " +
            "                                l.id_lg_term, l.id_term id_term_logistic " + 
            "						    FROM " + getGeneralDBScheme() + ".vc_term_club_all a " +
            "                           left JOIN (SELECT id_term, id_lg_term " +
            "                                        FROM " + getGeneralDBScheme() + ".vc_lg_term_all " +
            "                                       WHERE id_lg_record = ?) l " + 
            "							  ON (a.id_term = l.id_term))" +
            "                   ORDER BY DECODE(id_term_logistic, NULL, 2, 1), id_term) " +
       	   	"          WHERE ROWNUM < ? " + 
       	   	" ) WHERE rn >= ?";
          
          StringBuilder html = new StringBuilder();
          Connection con = null;
          PreparedStatement st = null;
          String myFont = "";
          String myFontEnd = "";
          String myBGColor = "";
          
          boolean hasPermission = false;

         try{
        	 if (isEditPermited("LOGISTIC_PARTNERS_TERMINALS_TERMINALS")>0) {
        		 hasPermission = true;
        	 }
        	 
        	 LOGGER.debug(mySQL + 
            		", 1={" + this.idRecord + ",int}" + 
            		", 2={" + p_end + ",int}" + 
            		", 3={" + p_beg + ",int}");
        	 con = Connector.getConnection(getSessionId());
             st = con.prepareStatement(mySQL);
             st.setInt(1, Integer.parseInt(this.idRecord));
             st.setInt(2, Integer.parseInt(p_end));
             st.setInt(3, Integer.parseInt(p_beg));
             ResultSet rset = st.executeQuery();
             ResultSetMetaData mtd = rset.getMetaData();
                
             int colCount = mtd.getColumnCount();
             
             if (hasPermission) {
            	 html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/logistic/partners/terminalupdate.jsp\">\n");
            	 html.append("<input type=\"hidden\" name=\"action\" value=\"add_list\">\n");
            	 html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            	 html.append("<input type=\"hidden\" name=\"type\" value=\"term\">\n");
            	 html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idRecord + "\">\n");
             }
             html.append(getBottomFrameTable());
             html.append("<tr>");
             for (int i=1; i <= colCount-1; i++) {
            	 String colName = mtd.getColumnName(i);                            
                 if ("TERM_SELECTED".equalsIgnoreCase(mtd.getColumnName(i))) {
                     if (hasPermission) {
                       	 html.append("<th>"+ logisticXML.getfieldTransl("h_object_selected", false)+
                       		"<input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb_term')\"></th>\n");
                        } 
                 } else {
                	 html.append(getBottomFrameTableTH(terminalXML, colName));
                 }
             }
             if (hasPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\"8\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/logistic/partners/terminalupdate.jsp"));
             	html.append(getGoBackButton("../crm/logistic/partners/terminalspecs.jsp?id=" + this.idRecord));
             	html.append("</td>");
             	html.append("</tr>");
             }

             html.append("</tr></thead><tbody>\n");
             int rowCount = 0;
             while (rset.next()) {
             	rowCount++;
                String jurPrsID="id_term_"+rset.getString("ID_TERM");
                String tprvCheck="prv_term_"+rset.getString("ID_LG_TERM");
                String tCheck="chb_term_"+rset.getString("ID_TERM");
                
                if(rset.getString("TERM_SELECTED").equalsIgnoreCase("Y")){
                	myFont = "<b>";
                	myFontEnd = "</b>";
                	myBGColor = selectedBackGroundStyle;
                } else {
                	myFont = "";
                	myFontEnd = "";
                	myBGColor = "";
                }
                    
                html.append("<tr>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(1)) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(2)) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(3)) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(4)) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(5)) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(6)) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(7)) + myFontEnd + "</td>\n");
                if (rset.getString("TERM_SELECTED").equalsIgnoreCase("Y") && hasPermission){
                  	html.append("<td " + myBGColor + "><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked onclick=\"return CheckCB(this);\"></td>\n");
                  	html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
                } else { 
                    html.append("<td><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\"></td>\n");
                }
                        
                html.append("<INPUT type=hidden  value="+rset.getString("ID_TERM")+" name="+jurPrsID+">");
                html.append("</tr> ");        
            }

            if (hasPermission) {
            	html.append("<tr>");
            	html.append("<td colspan=\"8\" align=\"center\">");
            	html.append(getSubmitButtonAjax("../crm/logistic/partners/terminalupdate.jsp"));
             	html.append(getGoBackButton("../crm/logistic/partners/terminalspecs.jsp?id=" + this.idRecord));
            	html.append("</td>");
            	html.append("</tr>");
            }
            html.append("<input type=hidden value="+rowCount+" name=rowCount>");
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
    } //getUserJurPrsPrivilegesHTML
   	
    public String getOthersListHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_lg_production, cd_lg_production, name_lg_production, " +
        	"        name_currency, cost_lg_production_frmt, count_lg_production, cost_all_production_frmt, id_lg_other "+
            "   FROM (SELECT ROWNUM rn, id_lg_production, cd_lg_production, name_lg_production, " +
        	"                 name_currency, cost_lg_production_frmt, count_lg_production, cost_all_production_frmt, id_lg_other "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_others_all " + 
            "                  WHERE id_lg_record = ? ";
        pParam.add(new bcFeautureParam("int", this.idRecord));
                
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_lg_production) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(cd_lg_production) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_lg_production) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
            "                  ORDER BY name_lg_production " +
            "         ) WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasEditPermission = false;
        boolean hasProductionPermission = false;
        try{
        	if (isEditPermited("LOGISTIC_PARTNERS_OTHERS_PRODUCTION")>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("LOGISTIC_PARTNERS_CATALOG")>=0) {
        		hasProductionPermission = true;
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
               html.append(getBottomFrameTableTH(logisticXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
            	for (int i=1; i <= colCount-1; i++) {
          	  	  	if ("ID_LG_PRODUCTION".equalsIgnoreCase(mtd.getColumnName(i)) && hasProductionPermission) {
              			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/clients/catalogspecs.jsp?id="+rset.getString("ID_LG_PRODUCTION"), "", ""));
    	          	} else {
    	      			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
    	      		}
          	  	}
          	  	if (hasEditPermission) {
          	  		String myDeleteLink = "../crm/logistic/partners/otherupdate.jsp?type=production&id=" + this.idRecord + "&line=" + rset.getString("ID_LG_OTHER")+"&action=remove&process=yes";
      	  			String myUpdateLink = "../crm/logistic/partners/otherupdate.jsp?type=production&id=" + this.idRecord + "&line=" + rset.getString("ID_LG_OTHER");
          	  		html.append(getDeleteButtonHTML(myDeleteLink, logisticXML.getfieldTransl("h_other_production_delete", false), rset.getString("NAME_LG_PRODUCTION")));
          	  		html.append(getEditButtonHTML(myUpdateLink));
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
	
    public String getGiftsListHTML(String pFindString, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, id_lg_gift, cd_gift, name_gift, desc_gift, " +
        	"        cost_one_gift_frmt, count_gift_all_frmt, " +
        	"        count_gift_given_frmt, count_gift_remain_frmt, " +
        	"        id_gift "+
            "   FROM (SELECT ROWNUM rn, cd_gift, name_gift, desc_gift, " +
            " 		         DECODE(NVL(count_gift_all,0)," +
            "                       0, '<font color=\"red\"><b>'||TO_CHAR(count_gift_all)||'</b></font>'," +
            "                       TO_CHAR(count_gift_all)" +
            "                ) count_gift_all_frmt, " +
            " 		         DECODE(NVL(count_gift_given,0)," +
            "                       0, TO_CHAR(count_gift_given)," +
            "                       '<font color=\"blue\"><b>'||TO_CHAR(count_gift_given)||'</b></font>'" +
            "                ) count_gift_given_frmt, " +
      		" 		         DECODE(NVL(count_gift_remain,0)," +
            "                       0, '<font color=\"red\"><b>'||TO_CHAR(count_gift_remain)||'</b></font>'," +
            "                       TO_CHAR(count_gift_remain)" +
            "                ) count_gift_remain_frmt, " +
            "                cost_one_gift_frmt||' '||sname_currency cost_one_gift_frmt, id_gift, id_lg_gift "+
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_gifts_club_all " + 
            "                  WHERE id_lg_record = ? ";
        pParam.add(new bcFeautureParam("int", this.idRecord));
        
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(cd_gift) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_gift) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(desc_gift) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
            "                  ORDER BY name_gift " +
            "         ) WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        boolean hasEditPermission = false;
        boolean hasGiftPermission = false;
        
        try{
        	if (isEditPermited("CLUB_EVENT_WAREHOUSE_GIFTS")>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLUB_EVENT_GIFTS")>=0) {
        		hasGiftPermission = true;
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
               html.append(getBottomFrameTableTH(club_actionXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
            	for (int i=1; i <= colCount-2; i++) {
		          	if ("ID_LG_GIFT".equalsIgnoreCase(mtd.getColumnName(i))) {
	          			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/warehousegiftspecs.jsp?id="+rset.getString("ID_LG_GIFT"), "", ""));
		          	} else if ("NAME_GIFT".equalsIgnoreCase(mtd.getColumnName(i)) && hasGiftPermission) {
	          			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/giftspecs.jsp?id="+rset.getString("ID_GIFT"), "", ""));
		          	} else {
		      			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
		      		}
	          	}
                if (hasEditPermission) {
                	String myDeleteLink = "../crm/club_event/warehouseupdate.jsp?type=gift&id=" + this.idRecord + "&line=" + rset.getString("ID_LG_GIFT")+"&action=remove&process=yes";
    	  			html.append(getDeleteButtonHTML(myDeleteLink, club_actionXML.getfieldTransl("h_delete_warehouse", false), rset.getString("ID_GIFT")));
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

    public String getGiftsEditListHTML(String p_beg, String p_end) {
        final String mySQL = 
        	" SELECT rn, id_gift, cd_gift, name_gift, gift_selected, id_lg_gift " + 
       	   	"   FROM (SELECT ROWNUM rn, id_gift, cd_gift, name_gift, gift_selected, id_lg_gift " + 
       	   	"           FROM (SELECT id_gift, cd_gift, name_gift, id_lg_gift, " + 
       	   	"						 DECODE(id_gift_logistic, NULL, 'N', 'Y') gift_selected " + 
       	   	"					FROM (SELECT a.id_gift, a.cd_gift, a.name_gift, " +
            "                                l.id_lg_gift, l.id_gift id_gift_logistic " + 
            "						    FROM " + getGeneralDBScheme() + ".vc_gifts_club_all a " +
            "                           left JOIN (SELECT id_gift, id_lg_gift " +
            "                                        FROM " + getGeneralDBScheme() + ".vc_lg_gifts_club_all " +
            "                                       WHERE id_lg_record = ?) l " + 
            "							  ON (a.id_gift = l.id_gift))" +
            "                   ORDER BY DECODE(id_gift_logistic, NULL, 2, 1), name_gift) " +
       	   	"          WHERE ROWNUM < ? " + 
       	   	" ) WHERE rn >= ?";
        
          StringBuilder html = new StringBuilder();
          Connection con = null;
          PreparedStatement st = null;
          String myFont = "";
          String myFontEnd = "";
          String myBGColor = "";
          
          boolean hasPermission = false;

         try{
        	 if (isEditPermited("CLUB_EVENT_WAREHOUSE_GIFTS")>0) {
        		 hasPermission = true;
        	 }
        	 
        	 LOGGER.debug(mySQL + 
            		", 1={" + this.idRecord + ",int}" + 
            		", 2={" + p_end + ",int}" + 
            		", 3={" + p_beg + ",int}");
        	 con = Connector.getConnection(getSessionId());
             st = con.prepareStatement(mySQL);
             st.setInt(1, Integer.parseInt(this.idRecord));
             st.setInt(2, Integer.parseInt(p_end));
             st.setInt(3, Integer.parseInt(p_beg));
             ResultSet rset = st.executeQuery();
             ResultSetMetaData mtd = rset.getMetaData();
                
             int colCount = mtd.getColumnCount();
             
             if (hasPermission) {
            	 html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/club_event/warehouseupdate.jsp\">\n");
            	 html.append("<input type=\"hidden\" name=\"action\" value=\"add_list\">\n");
            	 html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            	 html.append("<input type=\"hidden\" name=\"type\" value=\"gift\">\n");
            	 html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idRecord + "\">\n");
             }
             html.append(getBottomFrameTable());
             html.append("<tr>");
             for (int i=1; i <= colCount-1; i++) {
            	 String colName = mtd.getColumnName(i);                            
                 if ("GIFT_SELECTED".equalsIgnoreCase(mtd.getColumnName(i))) {
                     if (hasPermission) {
                       	 html.append("<th>"+ logisticXML.getfieldTransl("h_object_selected", false)+
                       		"<input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb_term')\"></th>\n");
                        } 
                 } else {
                	 html.append(getBottomFrameTableTH(club_actionXML, colName));
                 }
             }
             if (hasPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\"6\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/club_event/warehouseupdate.jsp"));
             	html.append(getGoBackButton("../crm/club_event/warehousespecs.jsp?id=" + this.idRecord));
             	html.append("</td>");
             	html.append("</tr>");
             }

             html.append("</tr></thead><tbody>\n");
             int rowCount = 0;
             while (rset.next()) {
             	rowCount++;
                String jurPrsID="id_gift_"+rset.getString("ID_GIFT");
                String tprvCheck="prv_gift_"+rset.getString("ID_LG_GIFT");
                String tCheck="chb_gift_"+rset.getString("ID_GIFT");
                
                if(rset.getString("GIFT_SELECTED").equalsIgnoreCase("Y")){
                	myFont = "<b>";
                	myFontEnd = "</b>";
                	myBGColor = selectedBackGroundStyle;
                } else {
                	myFont = "";
                	myFontEnd = "";
                	myBGColor = "";
                }
                    
                html.append("<tr>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(1)) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(2)) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(3)) + myFontEnd + "</td>\n" +
                	"<td " + myBGColor + ">" + myFont + getValue2(rset.getString(4)) + myFontEnd + "</td>\n");
                if (rset.getString("GIFT_SELECTED").equalsIgnoreCase("Y") && hasPermission){
                  	html.append("<td " + myBGColor + "><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked onclick=\"return CheckCB(this);\"></td>\n");
                  	html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
                } else { 
                    html.append("<td><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\"></td>\n");
                }
                        
                html.append("<INPUT type=hidden  value="+rset.getString("ID_GIFT")+" name="+jurPrsID+">");
                html.append("</tr> ");        
            }

            if (hasPermission) {
            	html.append("<tr>");
             	html.append("<td colspan=\"6\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/club_event/warehouseupdate.jsp"));
             	html.append(getGoBackButton("../crm/club_event/warehousespecs.jsp?id=" + this.idRecord));
             	html.append("</td>");
             	html.append("</tr>");
            }
            html.append("<input type=hidden value="+rowCount+" name=rowCount>");
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
    } //getUserJurPrsPrivilegesHTML

    public String getGiftsEditListHTML2(String pFind, String p_beg, String p_end) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT cd_gift, name_gift, id_gift " + 
       	   	"   FROM (SELECT ROWNUM rn, id_gift, cd_gift, name_gift " + 
       	   	"           FROM (SELECT id_gift, cd_gift, name_gift " + 
       	   	"					FROM " + getGeneralDBScheme() + ".vc_gifts_club_all a ";
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + 
        		" WHERE (UPPER(cd_gift) LIKE UPPER('%'||?||'%') OR " +
				"        UPPER(name_gift) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
            "                  ORDER BY cd_gift) " +
       	   	"          WHERE ROWNUM < ? " + 
       	   	" ) WHERE rn >= ?";
        
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        
        boolean hasPermission = false;
        boolean hasGiftPermission = false;

        String selectCurrency = getCurrencyOptions("", true);
	    	
        try{
        	 if (isEditPermited("CLUB_EVENT_WAREHOUSE_GIFTS")>0) {
        		 hasPermission = true;
        	 }
	    	 if (isEditMenuPermited("CLUB_EVENT_GIFTS")>=0) {
	    		 hasGiftPermission = true;
	    	 }
        	 
        	 LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	 con = Connector.getConnection(getSessionId());
             st = con.prepareStatement(mySQL);
             st = prepareParam(st, pParam);
             ResultSet rset = st.executeQuery();
             ResultSetMetaData mtd = rset.getMetaData();
                
             int colCount = mtd.getColumnCount();
             
             if (hasPermission) {
            	 html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/club_event/warehouseupdate.jsp\">\n");
            	 html.append("<input type=\"hidden\" name=\"action\" value=\"add_list\">\n");
            	 html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            	 html.append("<input type=\"hidden\" name=\"type\" value=\"gift\">\n");
            	 html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idRecord + "\">\n");
             }
             html.append(getBottomFrameTable());
             html.append("<tr>");
             if (hasPermission) {
               	 html.append("<th>"+ logisticXML.getfieldTransl("h_object_selected", false)+
               		"<input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this,'chb_')\"></th>\n");
             } 
             for (int i=1; i <= colCount-1; i++) {
            	 String colName = mtd.getColumnName(i);                            
               	 html.append(getBottomFrameTableTH(club_actionXML, colName));
             }
	         html.append(getBottomFrameTableTH(club_actionXML, "desc_gift"));
	         html.append(getBottomFrameTableTH(club_actionXML, "cd_currency"));
	         html.append(getBottomFrameTableTH(club_actionXML, "cost_one_gift"));
	         html.append(getBottomFrameTableTH(club_actionXML, "count_gifts"));
           	 html.append("</tr></thead><tbody>");
           	 
             if (hasPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\"7\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/club_event/warehouseupdate.jsp"));
             	html.append(getGoBackButton("../crm/club_event/warehousespecs.jsp?id=" + this.idRecord));
             	html.append("</td>");
             	html.append("</tr>");
             }

             html.append("</tr>\n");
             int rowCount = 0;
             while (rset.next()) {
             	rowCount++;

             	String tCheck			= "chb_" + rowCount;

	        	String tId 			= "id_" + rowCount;
	        	String tName 		= "name_" + rowCount;
	        	String tCurrency 	= "currency_" + rowCount;
	        	String tCost 		= "cost_" + rowCount;
	        	String tCount 		= "count_" + rowCount;
                
                html.append("<tr>\n");
                if (hasPermission){
                    html.append("<td>" +
                    		"<INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\">" + 
                    		"<input type=\"hidden\" name="+tId+" id="+tId+" value=\""+ rset.getString("ID_GIFT") +"\">"+
  	        	  		  	"</td>\n");
                }
	          	for (int i=1; i <= colCount-1; i++) {
	          		  if ("NAME_GIFT".equalsIgnoreCase(mtd.getColumnName(i)) && hasGiftPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/giftspecs.jsp?id="+rset.getString("ID_GIFT"), "", ""));
	          		  } else {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          		  }
	          	}
	        	html.append("<td><INPUT type=\"text\" name="+tName+" id="+tName+" size=\"30\" value=\""+rset.getString("NAME_GIFT")+"\" class=\"inputfield\"></td>\n");
	        	html.append("<td><select name="+tCurrency+" id="+tCurrency+" class=\"inputfield\">" + selectCurrency + "</select></td>");
	        	html.append("<td><INPUT type=\"text\" name="+tCost+" id="+tCost+" size=\"8\" value=\"\" class=\"inputfield\"></td>\n");
	        	html.append("<td><INPUT type=\"text\" name="+tCount+" id="+tCount+" size=\"8\" value=\"\" class=\"inputfield\"></td>\n");
	            html.append("</tr>\n");
            }

            if (hasPermission) {
            	html.append("<tr>");
             	html.append("<td colspan=\"7\" align=\"center\">");
                html.append("<input type=hidden value="+rowCount+" name=rowcount>");
             	html.append(getSubmitButtonAjax("../crm/club_event/warehouseupdate.jsp"));
             	html.append(getGoBackButton("../crm/club_event/warehousespecs.jsp?id=" + this.idRecord));
             	html.append("</td>");
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
        return html.toString();
    } //getUserJurPrsPrivilegesHTML

	  public String getGiftsWinnersHTML(String pFind, String pGiftState, String p_beg, String p_end) {
		  ArrayList<bcFeautureParam> pParam = initParamArray();

		  String mySQL = 
			  " SELECT rn, id_nat_prs_gift, name_nat_prs, name_nat_prs_gift_state, " +
		      "        name_gift, cost_gift_frmt, " +
		      "        date_reserve_frmt, date_given_frmt, id_nat_prs " +
		      "   FROM (SELECT ROWNUM rn, id_nat_prs_gift, name_nat_prs, name_nat_prs_gift_state, " +
		      "                name_gift, cost_gift_frmt, date_reserve_frmt, " +
		      "                date_given_frmt, id_nat_prs " +
		      "           FROM (SELECT id_nat_prs_gift, full_name name_nat_prs, " +
		      "                        DECODE(cd_nat_prs_gift_state, " +
		      "                               'RESERVED', '<font color=\"green\"><b>'||name_nat_prs_gift_state||'<b></font>', " +
		      "                               'GIVEN', '<font color=\"blue\"><b>'||name_nat_prs_gift_state||'<b></font>', " +
		      "                               '<font color=\"red\"><b>'||name_nat_prs_gift_state||'<b></font>' " +
		      "                        ) name_nat_prs_gift_state, " +
		      "                        name_gift, " +
		      "                        cost_gift_frmt||' '||sname_currency cost_gift_frmt, " +
		      "                        date_reserve_frmt, date_given_frmt, " +
		      "                        id_nat_prs " +
	          "                  FROM " + getGeneralDBScheme() + ".vc_nat_prs_gifts_club_all" +
	          "  		         WHERE id_lg_record = ? ";
		  pParam.add(new bcFeautureParam("int", this.idRecord));
		  
		  if (!isEmpty(pGiftState)) {
		   	  mySQL = mySQL + " AND cd_nat_prs_gift_state = ? ";
		   	pParam.add(new bcFeautureParam("string", pGiftState));
		  }
		  if (!isEmpty(pFind)) {
		   	  mySQL = mySQL + 
				   	" AND (TO_CHAR(id_nat_prs_gift) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(name_gift) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(full_name) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(date_reserve_frmt) LIKE UPPER('%'||?||'%') OR " +
					"	   UPPER(date_given_frmt) LIKE UPPER('%'||?||'%')) ";
		   	for (int i=0; i<5; i++) {
		   	    pParam.add(new bcFeautureParam("string", pFind));
		   	}
		  }
		  mySQL = mySQL + 
		      "                  ORDER BY date_reserve DESC) " +
	          "          WHERE ROWNUM < " + p_end +
	          " ) WHERE rn >= " + p_beg;
	      StringBuilder html = new StringBuilder();
	      Connection con = null;
	      PreparedStatement st = null; 
	      boolean hasNatPrsGiftPermission = false;
	      boolean hasNatPrsPermission = false;
	      try{
	    	  if (isEditMenuPermited("CLUB_EVENT_DELIVERY")>=0) {
	    		  hasNatPrsGiftPermission = true;
	    	  }
	    	  if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
	    		  hasNatPrsPermission = true;
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
	              String colName = mtd.getColumnName(i);
	              html.append(getBottomFrameTableTH(club_actionXML, colName));
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	        	  html.append("<tr>");
	          	  for (int i=1; i <= colCount-1; i++) {
	          		  if ("ID_NAT_PRS_GIFT".equalsIgnoreCase(mtd.getColumnName(i)) && hasNatPrsGiftPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club_event/deliveryspecs.jsp?id="+rset.getString("ID_NAT_PRS_GIFT"), "", ""));
	          		  } else if ("NAME_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i)) && hasNatPrsPermission) {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
	          		  } else {
	          			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          	  	  }
	          	  }
	              html.append("</td>\n");
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
	  } // getClubActionWinnersHTML
}
