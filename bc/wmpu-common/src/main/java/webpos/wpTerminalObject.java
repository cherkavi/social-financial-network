package webpos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.bcBase;
import bc.connection.Connector;
import bc.service.bcDictionaryRecord;
import bc.service.bcFeautureParam;


public class wpTerminalObject extends wpObject {
	private final static Logger LOGGER=Logger.getLogger(wpTerminalObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	private Map<String,Comparable<String>> loyHM = new HashMap<String, Comparable<String>>();
	//private Map<String,Comparable<String>> baseSPHM = new HashMap<String, Comparable<String>>();
	private Map<String,Comparable<String>> SAMHm = new HashMap<String, Comparable<String>>();

	public ArrayList<bcDictionaryRecord> transTypeArray = new ArrayList<bcDictionaryRecord>();
	
	private void readTransTypes() {
		
		PreparedStatement st = null;
		Connection con = null;
		ArrayList<bcFeautureParam> pParam = initParamArray();
		pParam.add(new bcFeautureParam("none", ""));
		String query = 
			" SELECT cd_trans_type, name_trans_type, sname_trans_type FROM " + getGeneralDBScheme() + ".vc_trans_type_all";
		try {
			LOGGER.debug(prepareSQLToLog(query, pParam));
			con = Connector.getConnection(getSessionId());

			st = con.prepareStatement(query);
			st = prepareParam(st, pParam);
			ResultSet rset = st.executeQuery();

			while (rset.next()) {
				String code = rset.getString("CD_TRANS_TYPE");
				String value = rset.getString("NAME_TRANS_TYPE");
				String shortValue = rset.getString("SNAME_TRANS_TYPE");
				transTypeArray.add(new bcDictionaryRecord(code, value, shortValue, "", ""));
			}

		} catch (SQLException e) {
			LOGGER.debug("bcBase.getSelectBodyFromQuery SQLException: "
					+ e.toString());
		} catch (Exception el) {
			LOGGER.debug("bcBase.getSelectBodyFromQuery Exception: "
					+ el.toString());
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException w) {
				w.toString();
			}
			Connector.closeConnection(con);
		}
	}
	
	/*private String getTransTypeName(String cd_type) {
		String returnValue = "";
		if (!isEmpty(cd_type)) {
			for (int i = 0; i < transTypeArray.size(); i++) {
				String code = transTypeArray.get(i).getCode();
				String value = transTypeArray.get(i).getValue();
				if (code.equalsIgnoreCase(cd_type)) {
					returnValue = value;
				}
			}
		}
		return returnValue;
	}*/
	
	private String getTransTypeShortName(String cd_type) {
		String returnValue = "";
		if (!isEmpty(cd_type)) {
			for (int i = 0; i < transTypeArray.size(); i++) {
				String code = transTypeArray.get(i).getCode();
				String value = transTypeArray.get(i).getShortValue();
				if (code.equalsIgnoreCase(cd_type)) {
					returnValue = value;
				}
			}
		}
		return returnValue;
	}
	
	private String idTerminal;

	ArrayList<bcFeautureParam> pRelationshipFilterParam = initParamArray();

	public wpTerminalObject() {
		setLanguage(bcBase.SESSION_PARAMETERS.LANG.getValue());
		setSessionId(bcBase.SESSION_PARAMETERS.SESSION_ID.getValue() );
		setDateFormat(bcBase.SESSION_PARAMETERS.DATE_FORMAT.getValue());
		setGeneralDBScheme(bcBase.SESSION_PARAMETERS.GENERAL_DB_SCHEME.getValue());
	}
	
	public wpTerminalObject(String pIdTerm) {
		this.idTerminal = pIdTerm;
		setLanguage(bcBase.SESSION_PARAMETERS.LANG.getValue());
		setSessionId(bcBase.SESSION_PARAMETERS.SESSION_ID.getValue() );
		setDateFormat(bcBase.SESSION_PARAMETERS.DATE_FORMAT.getValue());
		setGeneralDBScheme(bcBase.SESSION_PARAMETERS.GENERAL_DB_SCHEME.getValue());
	}
	
	public void setIdTerm(String pIdTerm) {
		this.idTerminal = pIdTerm;
		setLanguage(bcBase.SESSION_PARAMETERS.LANG.getValue());
		setSessionId(bcBase.SESSION_PARAMETERS.SESSION_ID.getValue() );
		setDateFormat(bcBase.SESSION_PARAMETERS.DATE_FORMAT.getValue());
	}
	
	public void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vp$term_all WHERE id_term = ?";
		fieldHm = getFeatures2(featureSelect, this.idTerminal, false);
		
		featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vp$term_loy WHERE id_term = ?";
		loyHM = getFeatures2(featureSelect, this.idTerminal, false);
		
		//featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vp$term_service_places_all WHERE id_term = ? and is_base_service_place = 'Y' ";
		//baseSPHM = getFeatures2(featureSelect, this.idTerminal, false);
		
		//featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vp$term_sam_all WHERE id_term = ? and TRUNC(SYSDATE) BETWEEN TRUNC(assign_term_date_beg) AND TRUNC(NVL(assign_term_date_end,SYSDATE)) AND rownum < 2";
		//SAMHm = getFeatures2(featureSelect, this.idTerminal, false);
	}
	
	public void getTermFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vp$term_all WHERE id_term = ?";
		fieldHm = getFeatures2(featureSelect, this.idTerminal, false);
	}
	
	public void getTermAdditionFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vp$term_all WHERE id_term = ?";
		fieldHm = getFeatures2(featureSelect, this.idTerminal, false);

		//String featureSelect2 = " SELECT * FROM " + getGeneralDBScheme() + ".vp$term_service_places_all WHERE id_term = ? and is_base_service_place = 'Y' ";
		//baseSPHM = getFeatures2(featureSelect2, this.idTerminal, false);
	}
	
	public String getValue(String pColumnName) {
		String returnString = getFeautureResult(fieldHm, pColumnName);
		if (returnString == null || "".equalsIgnoreCase(returnString)) {
			returnString = getFeautureResult(loyHM, pColumnName);
		}
		return returnString;
	}
	
	public String getValue(String pColumnName, String pSection) {
		String returnString = "";
		if ("TERM".equalsIgnoreCase(pSection)) {
			returnString = getFeautureResult(fieldHm, pColumnName);
		} else if ("LOYALITY".equalsIgnoreCase(pSection)) {
			returnString = getFeautureResult(loyHM, pColumnName);
		//} else if ("SERVICE_PLACE".equalsIgnoreCase(pSection)) {
		//	returnString = getFeautureResult(baseSPHM, pColumnName);
		} else if ("SAM".equalsIgnoreCase(pSection)) {
			returnString = getFeautureResult(SAMHm, pColumnName);
		} else {
			LOGGER.error("UNKNOWN pSection=" + pSection);
		}
		return returnString;
	}
	
	public String getNextDocumentNumber() {
		if (SAMHm.size() == 0) {
		   String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vp$term_sam_all WHERE id_term = ? and TRUNC(SYSDATE) BETWEEN TRUNC(assign_term_date_beg) AND TRUNC(NVL(assign_term_date_end,SYSDATE)) AND rownum < 2";
		   SAMHm = getFeatures2(featureSelect, this.idTerminal, false);
		}
		return this.getValue("NC_NEXT", "SAM");
	}

	public String getNoteOperationState(String pColumnNumber) {

		StringBuilder html = new StringBuilder();
		html.append("&nbsp;&nbsp;&nbsp;<font style=\"color: black; font-weight: bold;\">" + pColumnNumber + " - " + transactionXML.getfieldTransl("name_trans_state", false).toLowerCase() + ":</font><br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/success.png\"> - " + buttonXML.getfieldTransl("success", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/in_process.png\"> - " + buttonXML.getfieldTransl("in_process", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/cancel.png\"> - " + buttonXML.getfieldTransl("canceled", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/return.png\"> - " + buttonXML.getfieldTransl("returned", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/return_partially.png\"> - " + buttonXML.getfieldTransl("returned_partially", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/error.png\"> - " + buttonXML.getfieldTransl("error", false).toLowerCase() + "<br>");
		html.append("<span style=\"border-top: 1px gray dashed;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/sent_pay.png\"> - " + buttonXML.getfieldTransl("invoice_sent_for_payment", false).toLowerCase() + "</span><br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/waiting.gif\"> - " + buttonXML.getfieldTransl("need_confirm", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/paid.png\"> - " + buttonXML.getfieldTransl("invoice_paid", false).toLowerCase() + "<br>");
		//html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/warning.png\"> - " + buttonXML.getfieldTransl("warning", false).toLowerCase() + "<br>");
		return html.toString();
	}

	public String getNoteInvoiceState(String pColumnNumber) {

		StringBuilder html = new StringBuilder();
		html.append("&nbsp;&nbsp;&nbsp;<font style=\"color: black; font-weight: bold;\">" + pColumnNumber + " - " + webposXML.getfieldTransl("title_invoice_state", false).toLowerCase() + ":</font><br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/isnt_paid.png\"> - " + webposXML.getfieldTransl("invoice_state_isnt_paid", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/paid_partially.png\"> - " + webposXML.getfieldTransl("invoice_state_paid_partially", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/paid.png\"> - " + webposXML.getfieldTransl("invoice_state_paid", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/overpayment.png\"> - " + webposXML.getfieldTransl("invoice_state_overpayment", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/cancel.png\"> - " + webposXML.getfieldTransl("invoice_state_cancelled", false).toLowerCase() + "<br>");
		return html.toString();
	}

	public String getNoteInvoicePriority(String pColumnNumber) {

		StringBuilder html = new StringBuilder();
		html.append("&nbsp;&nbsp;&nbsp;<font style=\"color: black; font-weight: bold;\">" + pColumnNumber + " - " + webposXML.getfieldTransl("title_invoice_priority", false).toLowerCase() + ":</font><br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/priority/critical.png\"> - " + webposXML.getfieldTransl("invoice_priority_critical", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/priority/high.png\"> - " + webposXML.getfieldTransl("invoice_priority_high", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/priority/medium.png\"> - " + webposXML.getfieldTransl("invoice_priority_medium", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/priority/low.png\"> - " + webposXML.getfieldTransl("invoice_priority_low", false).toLowerCase() + "<br>");
		return html.toString();
	}

	public String getNoteOperationAll(String pColumnNumber) {

		StringBuilder html = new StringBuilder();
		html.append("&nbsp;&nbsp;&nbsp;<font style=\"color: black; font-weight: bold;\">" + pColumnNumber + " - " + commonXML.getfieldTransl("title_actions", false).toLowerCase() + ":</font><br>");
		//html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/activate.png\"> - " + webposXML.getfieldTransl("title_activate", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/storno.png\"> - " + webposXML.getfieldTransl("title_storno_action", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/pay.png\"> - " + buttonXML.getfieldTransl("pay", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/row_edit_v.GIF\"> - " + buttonXML.getfieldTransl("confirm", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/cheque.gif\"> - " + webposXML.getfieldTransl("window_cheque", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/invoice.png\"> - " + webposXML.getfieldTransl("window_invoice", false).toLowerCase() + "<br>");
		return html.toString();
	}

	public String getNoteQuestOperationAll(String pColumnNumber) {

		StringBuilder html = new StringBuilder();
		html.append("&nbsp;&nbsp;&nbsp;<font style=\"color: black; font-weight: bold;\">" + pColumnNumber + " - " + commonXML.getfieldTransl("title_actions", false).toLowerCase() + ":</font><br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/activate.png\"> - " + webposXML.getfieldTransl("title_activate", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/storno.png\"> - " + webposXML.getfieldTransl("title_storno_action", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/row_edit_v.GIF\"> - " + buttonXML.getfieldTransl("button_edit", false).toLowerCase() + "<br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/cheque.gif\"> - " + webposXML.getfieldTransl("window_cheque", false).toLowerCase() + "<br>");
		return html.toString();
	}

/*	public String getNoteOperationStorno(String pColumnNumber) {

		StringBuilder html = new StringBuilder();
		html.append("&nbsp;&nbsp;&nbsp;<font style=\"color: black; font-weight: bold;\">" + pColumnNumber + " - " + webposXML.getfieldTransl("title_storno", false).toLowerCase() + ":</font><br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/storno.png\"> - " + webposXML.getfieldTransl("title_storno_action", false).toLowerCase() + "<br>");
		return html.toString();
	}

	public String getNoteOperationCheque(String pColumnNumber) {

		StringBuilder html = new StringBuilder();
		html.append("&nbsp;&nbsp;&nbsp;<font style=\"color: black; font-weight: bold;\">" + pColumnNumber + " - " + commonXML.getfieldTransl("title_actions", false).toLowerCase() + ":</font><br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/cheque.gif\"> - " + webposXML.getfieldTransl("window_cheque", false).toLowerCase() + "<br>");
		return html.toString();
	}*/

	public String getNoteOperationActions(String pColumnNumber) {

		StringBuilder html = new StringBuilder();
		html.append("&nbsp;&nbsp;&nbsp;<font style=\"color: black; font-weight: bold;\">" + pColumnNumber + " - " + commonXML.getfieldTransl("title_actions", false).toLowerCase() + ":</font><br>");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/row_edit_v.GIF\"> - " + buttonXML.getfieldTransl("button_edit", false).toLowerCase() + "<br>");
		return html.toString();
	}
	
    public String getTransactionTypeShortNamesHTML() {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
			" SELECT name_trans_type, sname_trans_type " +
    		"   FROM " + getGeneralDBScheme() + ".vc_trans_type_all " +
    		"  WHERE NVL(is_online_type, 'N') = 'Y' " +
    		" ORDER BY sname_trans_type, name_trans_type ";
    	pParam.add(new bcFeautureParam("none", null));
    	
		StringBuilder html = new StringBuilder();
		Connection con = null;
		PreparedStatement st = null;
        try{ 
      	  	LOGGER.debug(prepareSQLToLog(mySQL,pParam));
      	  	con = Connector.getConnection(getSessionId());
      	  	st = con.prepareStatement(mySQL);
      	  	st = prepareParam(st, pParam);
      	  	ResultSet rset = st.executeQuery();
            
      	    html.append("<font style=\"color: black; font-weight: bold;\">" + webposXML.getfieldTransl("operation_type", false) + ":</font><br>");
      	    while (rset.next())
            {
            	html.append("&nbsp;&nbsp;&nbsp;<b>" + rset.getString("SNAME_TRANS_TYPE") + "</b> - " + rset.getString("NAME_TRANS_TYPE") + "<br>");
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e);}
        catch (Exception el) {LOGGER.error(html, el);}
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
    } // getTermServCardHTML
	
    public String getTermLoyalityLinesHTML(String pFindString, String pIdLoyalityScheme, String pIdCardStatus, String pIdCategory, String pCdKindLoyality, String pIdLoyalityLineCurrent, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
			" SELECT name_card_status, name_category, cd_kind_loyality, " +
			"        opr_sum_frmt, " +
			"		 bon_shareholder_value_frmt bon_perc_and_fixed_value_frmt, " +
			"        max_bon_st_frmt, bonus_transfer_term_point, bonus_transfer_term_money, " +
			"        disc_shareholder_value_frmt disc_perc_and_fixed_value_frmt, " +
			"        max_disc_st_frmt, bonus_calc_term, nullperiod_flag_tsl, opr_sum, /*nullperiod_flag_tsl, active_tsl,*/ id_loyality_history_line " +
			"   FROM (SELECT ROWNUM rn, id_loyality_history_line, name_card_status, name_category, cd_kind_loyality, " +
    		"                opr_sum_frmt, bon_shareholder_value_frmt, " +
    		"                max_bon_st_frmt, bonus_transfer_term_point, bonus_transfer_term_money, " +
    		"                disc_shareholder_value_frmt, max_disc_st_frmt, bonus_calc_term, " +
    		"        		 /*DECODE(nullperiod_flag, 0, '<b><font color=\"red\">'||nullperiod_flag_tsl||'</font><b>', nullperiod_flag_tsl) nullperiod_flag_tsl,*/ " +
        	"        		 DECODE(active, 0, '<b><font color=\"red\">'||active_tsl||'</font><b>', active_tsl) active_tsl, opr_sum, nullperiod_flag_tsl " +
    		"           FROM (SELECT * " +
    		"                   FROM " + getGeneralDBScheme() + ".vp$term_loy_line " +
    		"                  WHERE id_term = ? " +
    		"                    AND id_loyality_history = ? " +
    		"                    AND active = 1 ";
    	pParam.add(new bcFeautureParam("int", this.idTerminal));
    	pParam.add(new bcFeautureParam("int", this.getValue("ID_LOYALITY_HISTORY")));
    	
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_line) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_end_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
       	if (!isEmpty(pIdCategory)) {
       		mySQL = mySQL + " AND id_category = ? ";
       		pParam.add(new bcFeautureParam("int", pIdCategory));
       	}
       	if (!isEmpty(pIdCardStatus)) {
       		mySQL = mySQL + " AND id_card_status = ? ";
       		pParam.add(new bcFeautureParam("int", pIdCardStatus));
       	}
       	if (!isEmpty(pCdKindLoyality)) {
       		mySQL = mySQL + " AND cd_kind_loyality = ? ";
       		pParam.add(new bcFeautureParam("string", pCdKindLoyality));
       	}

   		if (!isEmpty(pIdLoyalityScheme)) {
       		mySQL = mySQL + " AND id_loyality_history = ? ";
   			pParam.add(new bcFeautureParam("int", pIdLoyalityScheme));
   		}
   		pParam.add(new bcFeautureParam("int", p_end));
   		pParam.add(new bcFeautureParam("int", p_beg));
       	mySQL = mySQL +
			"                  ORDER BY id_loyality_scheme, cd_kind_loyality, " +
			"                           name_card_status, name_category, opr_sum_frmt )" + 
    		"           WHERE ROWNUM < ? " + 
    		" ) WHERE rn >= ?";
    	
		StringBuilder html = new StringBuilder();
		StringBuilder detail = new StringBuilder();
		Connection con = null;
		PreparedStatement st = null;
		
		String lFontAdditional = "<font style=\"color:red; font-weight: bold;\">";
        try{ 
      	  	LOGGER.debug(prepareSQLToLog(mySQL,pParam));
      	  	con = Connector.getConnection(getSessionId());
      	  	st = con.prepareStatement(mySQL);
      	  	st = prepareParam(st, pParam);
      	  	ResultSet rset = st.executeQuery();
            //ResultSetMetaData mtd = rset.getMetaData();
            //int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            //for (int i=1; i <= colCount-1; i++) {
            	//html.append(getBottomFrameTableTH(loylineXML, mtd.getColumnName(i)));
            	//html.append("<th>" + i + "</th>");
            //}
            html.append(getBottomFrameTableTH(loylineXML, "NAME_CARD_STATUS"));
            html.append(getBottomFrameTableTH(loylineXML, "NAME_CATEGORY"));
            //html.append(getBottomFrameTableTH(loyXML, "CD_KIND_LOYALITY"));
            html.append(getBottomFrameTableTH(loylineXML, "OPR_SUM_FRMT"));
            html.append(getBottomFrameTableTH(loylineXML, "BON_PERC_AND_FIXED_VALUE_FRMT"));
            //html.append(getBottomFrameTableTH(loylineXML, "BONUS_TRANSFER_TERM"));
            html.append("</tr></thead><tbody>");
            String oprSumValue = "";
            while (rset.next())
            {
                
                oprSumValue = rset.getString("OPR_SUM");
                if (oprSumValue == null || "".equalsIgnoreCase(oprSumValue) || "0".equalsIgnoreCase(oprSumValue)) {
                	oprSumValue = "любая";
                } else {
                	oprSumValue = rset.getString("OPR_SUM_FRMT");
                }
                
            	html.append("<tr>");
            	html.append(getBottomFrameTableTD("NAME_CARD_STATUS", rset.getString("NAME_CARD_STATUS"), "", "<font style=\"color:green;\">", ""));
            	html.append(getBottomFrameTableTD("NAME_CATEGORY", rset.getString("NAME_CATEGORY"), "admin/setting.jsp?id_loy_line="+rset.getString("ID_LOYALITY_HISTORY_LINE"), "<font style=\"color:blue;\">", ""));
            	//html.append(getBottomFrameTableTD("CD_KIND_LOYALITY", rset.getString("CD_KIND_LOYALITY"), "", "", ""));
            	html.append(getBottomFrameTableTD("OPR_SUM_FRMT", oprSumValue, "", "", ""));
            	html.append(getBottomFrameTableTD("BON_PERC_AND_FIXED_VALUE_FRMT", rset.getString("BON_PERC_AND_FIXED_VALUE_FRMT"), "", "", ""));
            	//html.append(getBottomFrameTableTDAlign("BONUS_TRANSFER_TERM", rset.getString("BONUS_TRANSFER_TERM_FRMT"), "", "", "", "center"));
          	  	/*for (int i=1; i <= colCount-1; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  	}*/
                html.append("</tr>\n");
                
                if (!(pIdLoyalityLineCurrent == null || "".equalsIgnoreCase(pIdLoyalityLineCurrent)) &&
                		rset.getString("ID_LOYALITY_HISTORY_LINE").equalsIgnoreCase(pIdLoyalityLineCurrent)) {
                	detail.append("<br><br>");
                    detail.append(getBottomFrameTable());
                	detail.append("</thead><tbody>");
                    
                    detail.append("<tr>");
                    detail.append(getBottomFrameTableTDAlign("PARAMETER_NAME", webposXML.getfieldTransl("TITLE_LOYALITY_PARAM_NAME", false), "", "<font style=\"font-weight:bold;\">", "", "left"));
                    detail.append(getBottomFrameTableTDAlign("PARAMETER_VALUE", webposXML.getfieldTransl("TITLE_LOYALITY_PARAM_VALUE", false), "", "<font style=\"font-weight:bold;\">", "", "right"));
                    detail.append("</tr>\n");
                    detail.append("<tr>");
                    detail.append(getBottomFrameTableTDAlign("NAME_CARD_STATUS", loylineXML.getfieldTransl("NAME_CARD_STATUS", false), "", "", "", "left"));
                    detail.append(getBottomFrameTableTDAlign("NAME_CARD_STATUS", rset.getString("NAME_CARD_STATUS"), "", "<font style=\"color:green;\">", "", "right"));
                    detail.append("</tr>\n");
                    detail.append("<tr>");
                    detail.append(getBottomFrameTableTDAlign("NAME_CATEGORY", loylineXML.getfieldTransl("NAME_CATEGORY", false), "", "", "", "left"));
                    detail.append(getBottomFrameTableTDAlign("NAME_CATEGORY", rset.getString("NAME_CATEGORY"), "", "<font style=\"color:blue;\">", "", "right"));
                    detail.append("</tr>\n");
                    //detail.append("<tr>");
                    //detail.append(getBottomFrameTableTDAlign("CD_KIND_LOYALITY", loylineXML.getfieldTransl("CD_KIND_LOYALITY", false), "", "", "", "left"));
                    //detail.append(getBottomFrameTableTDAlign("CD_KIND_LOYALITY", rset.getString("CD_KIND_LOYALITY"), "", "", "", "right"));
                    //detail.append("</tr>\n");
                    
                    if ("0001".equalsIgnoreCase(rset.getString("CD_KIND_LOYALITY"))) {
                    	detail.append("<tr>");
	                    detail.append(getBottomFrameTableTDAlign("OPR_SUM_FRMT", loylineXML.getfieldTransl("OPR_SUM_FRMT", false), "", "", "", "left"));
	                    detail.append(getBottomFrameTableTDAlign("OPR_SUM_FRMT", oprSumValue, "", "", "", "right"));
	                    detail.append("</tr>\n");
	                    detail.append("<tr>");
	                    detail.append(getBottomFrameTableTDAlign("BON_PERCENT_VALUE", loylineXML.getfieldTransl("BON_PERCENT_VALUE", false), "", "", "", "left"));
	                    detail.append(getBottomFrameTableTDAlign("BON_PERCENT_VALUE", rset.getString("BON_PERC_AND_FIXED_VALUE_FRMT"), "", lFontAdditional, "", "right"));
	                    detail.append("</tr>\n");
	                    detail.append("<tr>");
	                    detail.append(getBottomFrameTableTDAlign("MAX_BON_ST_FRMT", loylineXML.getfieldTransl("MAX_BON_ST_FRMT", false), "", "", "", "left"));
	                    detail.append(getBottomFrameTableTDAlign("MAX_BON_ST_FRMT", rset.getString("MAX_BON_ST_FRMT"), "", "", "", "right"));
	                    detail.append("</tr>\n");
                    } else if ("0002".equalsIgnoreCase(rset.getString("CD_KIND_LOYALITY"))) {
                    	detail.append("<tr>");
	                    detail.append(getBottomFrameTableTDAlign("BON_PERCENT_VALUE", loylineXML.getfieldTransl("BON_PERCENT_VALUE", false), "", "", "", "left"));
	                    detail.append(getBottomFrameTableTDAlign("BON_PERCENT_VALUE", rset.getString("BON_PERC_AND_FIXED_VALUE_FRMT"), "", lFontAdditional, "", "right"));
	                    detail.append("</tr>\n");
                    } else if ("0003".equalsIgnoreCase(rset.getString("CD_KIND_LOYALITY"))) {
                    	detail.append("<tr>");
	                    detail.append(getBottomFrameTableTDAlign("OPR_SUM_FRMT", loylineXML.getfieldTransl("OPR_SUM_FRMT", false), "", "", "", "left"));
	                    detail.append(getBottomFrameTableTDAlign("OPR_SUM_FRMT", oprSumValue, "", "", "", "right"));
	                    detail.append("</tr>\n");
	                    detail.append("<tr>");
	                    detail.append(getBottomFrameTableTDAlign("BON_FIXED_VALUE", loylineXML.getfieldTransl("BON_FIXED_VALUE", false), "", "", "", "left"));
	                    detail.append(getBottomFrameTableTDAlign("BON_FIXED_VALUE", rset.getString("BON_PERC_AND_FIXED_VALUE_FRMT"), "", lFontAdditional, "", "right"));
	                    detail.append("</tr>\n");
	                    detail.append("<tr>");
	                    detail.append(getBottomFrameTableTDAlign("MAX_BON_ST_FRMT", loylineXML.getfieldTransl("MAX_BON_ST_FRMT", false), "", "", "", "left"));
	                    detail.append(getBottomFrameTableTDAlign("MAX_BON_ST_FRMT", rset.getString("MAX_BON_ST_FRMT"), "", "", "", "right"));
	                    detail.append("</tr>\n");
                    }
                    detail.append("<tr>");
                    detail.append(getBottomFrameTableTDAlign("BONUS_TRANSFER_TERM_MONEY", loylineXML.getfieldTransl("BONUS_TRANSFER_TERM_MONEY", false), "", "", "", "left"));
                    detail.append(getBottomFrameTableTDAlign("BONUS_TRANSFER_TERM_MONEY", rset.getString("BONUS_TRANSFER_TERM_MONEY"), "", lFontAdditional, "", "right"));
                    detail.append("</tr>\n");
                    detail.append("<tr>");
                    detail.append(getBottomFrameTableTDAlign("BONUS_TRANSFER_TERM_POINT", loylineXML.getfieldTransl("BONUS_TRANSFER_TERM_POINT", false), "", "", "", "left"));
                    detail.append(getBottomFrameTableTDAlign("BONUS_TRANSFER_TERM_POINT", rset.getString("BONUS_TRANSFER_TERM_POINT"), "", lFontAdditional, "", "right"));
                    detail.append("</tr>\n");
                    /*detail.append("<tr>");
                    detail.append(getBottomFrameTableTDAlign("BONUS_CALC_TERM", loylineXML.getfieldTransl("BONUS_CALC_TERM", false), "", "", "", "left"));
                    detail.append(getBottomFrameTableTDAlign("BONUS_CALC_TERM", rset.getString("BONUS_CALC_TERM"), "", "", "", "right"));
                    detail.append("</tr>\n");*/
                    

                    if ("0001".equalsIgnoreCase(rset.getString("CD_KIND_LOYALITY"))) {
                    	detail.append("<tr>");
                        detail.append(getBottomFrameTableTDAlign("DISC_PERCENT_VALUE", loylineXML.getfieldTransl("DISC_PERCENT_VALUE", false), "", "", "", "left"));
                        detail.append(getBottomFrameTableTDAlign("DISC_PERCENT_VALUE", rset.getString("DISC_PERC_AND_FIXED_VALUE_FRMT"), "", "", "", "right"));
                        detail.append("</tr>\n");
                        detail.append("<tr>");
                        detail.append(getBottomFrameTableTDAlign("MAX_DISC_ST_FRMT", loylineXML.getfieldTransl("MAX_DISC_ST_FRMT", false), "", "", "", "left"));
                        detail.append(getBottomFrameTableTDAlign("MAX_DISC_ST_FRMT", rset.getString("MAX_DISC_ST_FRMT"), "", "", "", "right"));
                        detail.append("</tr>\n");                        
                    } else if ("0002".equalsIgnoreCase(rset.getString("CD_KIND_LOYALITY"))) {
                    	detail.append("<tr>");
                        detail.append(getBottomFrameTableTDAlign("DISC_PERCENT_VALUE", loylineXML.getfieldTransl("DISC_PERCENT_VALUE", false), "", "", "", "left"));
                        detail.append(getBottomFrameTableTDAlign("DISC_PERCENT_VALUE", rset.getString("DISC_PERC_AND_FIXED_VALUE_FRMT"), "", "", "", "right"));
	                    detail.append("</tr>\n");
                    } else if ("0003".equalsIgnoreCase(rset.getString("CD_KIND_LOYALITY"))) {
                    	detail.append("<tr>");
                        detail.append(getBottomFrameTableTDAlign("DISC_FIXED_VALUE", loylineXML.getfieldTransl("DISC_FIXED_VALUE", false), "", "", "", "left"));
                        detail.append(getBottomFrameTableTDAlign("DISC_FIXED_VALUE", rset.getString("DISC_PERC_AND_FIXED_VALUE_FRMT"), "", "", "", "right"));
                        detail.append("</tr>\n");
                        detail.append("<tr>");
                        detail.append(getBottomFrameTableTDAlign("MAX_DISC_ST_FRMT", loylineXML.getfieldTransl("MAX_DISC_ST_FRMT", false), "", "", "", "left"));
                        detail.append(getBottomFrameTableTDAlign("MAX_DISC_ST_FRMT", rset.getString("MAX_DISC_ST_FRMT"), "", "", "", "right"));
                        detail.append("</tr>\n");                        
                    }
                    
                    //detail.append("<tr>");
                    //detail.append(getBottomFrameTableTDAlign("NULLPERIOD_FLAG_TSL", loylineXML.getfieldTransl("NULLPERIOD_FLAG_TSL", false), "", "", "", "left"));
                    //detail.append(getBottomFrameTableTDAlign("NULLPERIOD_FLAG_TSL", rset.getString("NULLPERIOD_FLAG_TSL"), "", "", "", "right"));
                    //detail.append("</tr>\n");
                    detail.append("</tbody></table>\n");
                }

            }
            html.append("</tbody></table>\n");
            
            html.append(detail.toString());
            
        } // try
        catch (SQLException e) {LOGGER.error(html, e);}
        catch (Exception el) {LOGGER.error(html, el);}
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
    } // getTermServCardHTML
	
    public String getWEBPOSTermLoyalityLinesHTML(String pFindString, String pIdLoyalityScheme, String pIdCardStatus, String pIdCategory, String pCdKindLoyality, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
			" SELECT name_card_status, name_category, cd_kind_loyality, " +
			"        opr_sum_frmt, " +
			"		 CASE WHEN cd_kind_loyality = '0003' THEN bon_fixed_value_frmt ELSE bon_percent_value_percent END bon_perc_and_fixed_value_frmt, " +
			"        max_bon_st_frmt, bonus_transfer_term, " +
			"        CASE WHEN cd_kind_loyality = '0003' THEN disc_fixed_value_frmt ELSE disc_percent_value_percent END disc_perc_and_fixed_value_frmt, " +
			"        max_disc_st_frmt, bonus_calc_term, /*nullperiod_flag_tsl, active_tsl,*/ id_loyality_scheme " +
			"   FROM (SELECT ROWNUM rn, id_loyality_scheme, name_card_status, name_category, cd_kind_loyality, " +
    		"                opr_sum_frmt, bon_percent_value_percent, bon_fixed_value_frmt, " +
    		"                max_bon_st_frmt, bonus_transfer_term, disc_percent_value_percent, disc_fixed_value_frmt, " +
    		"                max_disc_st_frmt, bonus_calc_term, " +
    		"        		 DECODE(nullperiod_flag, 0, '<b><font color=\"red\">'||nullperiod_flag_tsl||'</font><b>', nullperiod_flag_tsl) nullperiod_flag_tsl, " +
        	"        		 DECODE(active, 0, '<b><font color=\"red\">'||active_tsl||'</font><b>', active_tsl) active_tsl, opr_sum " +
    		"           FROM (SELECT * " +
    		"                   FROM " + getGeneralDBScheme() + ".vp$term_loy_line " +
    		"                  WHERE id_term = ? ";
    	pParam.add(new bcFeautureParam("int", this.idTerminal));
    	
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_line) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_end_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
       	if (!isEmpty(pIdCategory)) {
       		mySQL = mySQL + " AND id_category = ? ";
       		pParam.add(new bcFeautureParam("int", pIdCategory));
       	}
       	if (!isEmpty(pIdCardStatus)) {
       		mySQL = mySQL + " AND id_card_status = ? ";
       		pParam.add(new bcFeautureParam("int", pIdCardStatus));
       	}
       	if (!isEmpty(pCdKindLoyality)) {
       		mySQL = mySQL + " AND cd_kind_loyality = ? ";
       		pParam.add(new bcFeautureParam("string", pCdKindLoyality));
       	}

   		if (!isEmpty(pIdLoyalityScheme)) {
       		mySQL = mySQL + " AND id_loyality_history = ? ";
   			pParam.add(new bcFeautureParam("int", pIdLoyalityScheme));
   		}
   		pParam.add(new bcFeautureParam("int", p_end));
   		pParam.add(new bcFeautureParam("int", p_beg));
       	mySQL = mySQL +
			"                  ORDER BY id_loyality_scheme, cd_kind_loyality, " +
			"                           name_card_status, name_category, opr_sum_frmt )" + 
    		"           WHERE ROWNUM < ? " + 
    		" ) WHERE rn >= ?";
    	
		StringBuilder html = new StringBuilder();
		Connection con = null;
		PreparedStatement st = null;
        //boolean hasEditPermission = false;
        try{ 
      	  	//if (isEditPermited("CLIENTS_TERMINALS_LOYBON")>0) {
      	  		// Заглушка - ИЗМЕНЕНИЕ СТРОК ЛОЯЛЬНОСТИ ТЕРМИНАЛА ЗАПРЕЩЕНО
      	  		//hasEditPermission = false;
      	  		//hasEditPermission = true;
      	  	//}
      	  	
      	  	LOGGER.debug(prepareSQLToLog(mySQL,pParam));
      	  	con = Connector.getConnection(getSessionId());
      	  st = con.prepareStatement(mySQL);
      	st = prepareParam(st, pParam);
      	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
            //html.append("<tr>");
            //for (int i=1; i <= colCount-1; i++) {
            //	html.append(getBottomFrameTableTH(loylineXML, mtd.getColumnName(i)));
            //}
            html.append("</tr>");
            html.append("</thead><tbody>");
            
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  	}
                html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
        } // try
        catch (SQLException e) {LOGGER.error(html, e);}
        catch (Exception el) {LOGGER.error(html, el);}
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
    } // getTermServCardHTML
    
    public String getPaymentsHTML(String pFind, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	PreparedStatement st = null;
        Connection con = null;
        String mySQL = "";
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        try{
        	mySQL = 
        		" SELECT rn, /*id_trans, type_trans_txt,*/ sys_date_frmt, " +
	   			"        cd_card1_hide, cd_currency, nt_icc, /*action,*/  " +
		    	"		 opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
		    	"        sum_bon_frmt, sum_disc_frmt, cd_card1, card_serial_number, id_issuer, id_payment_system " +
	            " FROM (SELECT ROWNUM rn, id_trans, " +
	            "              DECODE(type_trans, " +
	    		"                       1,  '<font color=\"black\"><b>'||type_trans_txt||'</b></font>', " +
	    		"                       2,  '<font color=\"blue\">'||type_trans_txt||'</font>', " +
	    		"                       3,  '<font color=\"green\">'||type_trans_txt||'</font>', " +
	    		"                       4,  '<font color=\"red\"><b>'||type_trans_txt||'</b></font>', " +
	    		"                       5,  '<font color=\"red\">'||type_trans_txt||'</font>', " +
	    		"                       6,  '<font color=\"black\"><b>'||type_trans_txt||'</b></font>', " +
	    		"                       7,  '<font color=\"black\"><b>'||type_trans_txt||'</b></font>', " +
	    		"                       type_trans_txt) type_trans_txt, " +
	    		"              sys_date_frmt, " +
	   			"          	   id_term, cd_card1_hide, cd_card1, cd_currency, nt_icc, action,  " +
		    	"		       opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
		    	"              sum_bon_frmt, sum_disc_frmt, card_serial_number, id_issuer, id_payment_system " +
	            " 		  FROM (SELECT * " +
	            "           	  FROM " + getGeneralDBScheme() + ".vp$payment_trans_all " +
	            "                WHERE id_term = ? ";
        	pParam.add(new bcFeautureParam("int", this.idTerminal));
        	
            if (!isEmpty(pFind)) {
            	mySQL = mySQL + " AND (UPPER(id_trans) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(TO_CHAR(id_term)) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(sys_date) LIKE UPPER('%'||?||'%') OR " +
           			"UPPER(card_serial_number) LIKE UPPER('%'||?||'%')) ";
            	for (int i=0; i<4; i++) {
            	    pParam.add(new bcFeautureParam("string", pFind));
            	}
           	}
            pParam.add(new bcFeautureParam("int", p_end));
            pParam.add(new bcFeautureParam("int", p_beg));
            mySQL = mySQL +
	            "                ORDER BY nt_icc, id_trans DESC) " +
	            "        WHERE ROWNUM < ?) " +
	            " WHERE rn >= ?";
        	
        	LOGGER.debug(prepareSQLToLog(mySQL,pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-3; i++) {
            	html.append(getBottomFrameTableTH(transactionXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");

            while (rset.next())
            {
            	html.append("<tr>\n");
                for (int i=1; i<=colCount-3; i++) {
        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                }
                html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
        } // try
        catch (SQLException e) {LOGGER.error(html, e);}
        catch (Exception el) {LOGGER.error(html, el);}
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
    } // getCardTransHTML
    
    private String operationsCount = "";
    
    public String getOperationsCount() {
    	return operationsCount;
    }
    
    public String getOnlineOperationHTML_old(String pIdUser, String pFind, String pOperationType, String pOperationState, String pPayType, String pRRN, String pCdCard1, String pDateBeg, String pDateEnd, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	PreparedStatement st = null;
        Connection con = null;
        String mySQL = "";
        String myBGColor = "";
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        boolean hasStornoPermission = false;
        String contextParam = "";
        
        try{
        	hasStornoPermission = true;
            
        	mySQL = 
        		" SELECT /*rn,*/ nc_term cheque_number, name_trans_type operation_type, sys_date_frmt operation_date, cd_card1_hide, opr_sum_frmt operation_sum, /*desc_trans,*/ " +
	   			"        fcd_trans_state, name_trans_state, fcd_trans_type, rrn, id_trans, id_trans_group, cd_card1, cd_telgr_external_state, pay_type, " +
	   			"		 context_access_type, context_id_user, context_id_dealer, row_count, level_trans " +
	            " FROM (SELECT ROWNUM rn, sys_date_frmt, desc_trans, nc_term, " +
	            "			   DECODE(fcd_trans_type, " +
	            "                       'REC_PAYMENT',  '<font style = \"color: black\"><b>'||cancel_style_beg||name_trans_type||pay_type_tsl1||cancel_style_end||'</b></font>', " +
	    		"                       'REC_ACTIVATION',  '<font style = \"color: blue\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		"                       'REC_PUT_CARD',  '<font style = \"color: #191970\"><b>'||cancel_style_beg||name_trans_type||pay_type_tsl1||cancel_style_end||'</b></font>', " +
	            "                       'REC_COUPON',  '<font style = \"color: #8B8989\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		"                       'REC_MEMBERSHIP_FEE',  '<font style = \"color: #8B3A3A\"><b>'||cancel_style_beg||name_trans_type||pay_type_tsl1||cancel_style_end||'</b></font>', " +
	    		"                       'REC_MTF',  '<font style = \"color: #B8860B\"><b>'||cancel_style_beg||name_trans_type||pay_type_tsl1||cancel_style_end||'</b></font>', " +
	    		"                       'REC_POINT_FEE',  '<font style = \"color: #C8860B\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		"                       'REC_SHARE_FEE',  '<font style = \"color: green\"><b>'||cancel_style_beg||name_trans_type||pay_type_tsl1||cancel_style_end||'</b></font>', " +
	    		"                       'REC_TRANSFER_GET_POINT',  '<font style = \"color: #BA55D3\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		"                       'REC_TRANSFER_PUT_POINT',  '<font style = \"color: #BA55D3\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		//"                       'REC_CANCEL',  '<font style = \"color: red\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		//"                       'REC_RETURN',  '<font style = \"color: red\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		"                       name_trans_type" +
	    		"              ) name_trans_type, " +
	            "              fcd_trans_state, name_trans_state, fcd_trans_type, " +
	    		"              opr_sum_frmt, cd_card1_hide, cd_card1, id_trans, id_trans_group, rrn, cd_telgr_external_state, pay_type, " +
	    		"			   context_access_type, context_id_user, context_id_dealer, row_count, level_trans " +
	            " 		  FROM (SELECT sys_date_frmt, desc_trans, nc_term, " +
	            "                      CASE pay_type " +
	            "							WHEN 'CASH' THEN ' (<font color=\"green\">'||sname_trans_pay_type||'</font>)' " +
	            "							WHEN 'BANK_CARD' THEN ' (<font color=\"blue\">'||sname_trans_pay_type||'</font>)' " +
	            "							WHEN 'SMPU_CARD' THEN ' (<font color=\"black\">'||sname_trans_pay_type||'</font>)' " +
	            "							WHEN 'INVOICE' THEN ' (<font color=\"brown\">'||sname_trans_pay_type||'</font>)' " +
	            "							ELSE ' (<font color=\"red\">'||sname_trans_pay_type||'</font>)' " +
	            "					   END pay_type_tsl1," +
	            "                      CASE pay_type " +
	            "							WHEN 'CASH' THEN ' (<font color=\"green\">'||sname_trans_pay_type||'</font>)' " +
	            "							WHEN 'BANK_CARD' THEN ' (<font color=\"blue\">'||sname_trans_pay_type||'</font>)' " +
	            "							WHEN 'SMPU_CARD' THEN ' (<font color=\"black\">'||sname_trans_pay_type||'</font>)' " +
	            "							WHEN 'INVOICE' THEN ' (<font color=\"brown\">'||sname_trans_pay_type||'</font>)' " +
	            "							ELSE '' " +
	            "					   END pay_type_tsl2," +
	            "			           CASE WHEN fcd_trans_state IN ('C_CANCELLED_TRANS','C_RETURNED_TRANS','C_RETURNED_PART_TRANS') " +
	            "                           THEN '<font style=\"text-decoration: line-through;\" title=\"" + webposXML.getfieldTransl("cheque_operation_cancel", false) + "\">'" +
	            "                           ELSE ''" +
	            "                      END cancel_style_beg," +
	            "			           CASE WHEN fcd_trans_state IN ('C_CANCELLED_TRANS','C_RETURNED_TRANS','C_RETURNED_PART_TRANS') " +
	            "                           THEN '</font>'" +
	            "                           ELSE ''" +
	            "                      END cancel_style_end, " +
	            "                      type_trans_txt name_trans_type, cd_trans_state, fcd_trans_state, name_trans_state, cd_trans_type, fcd_trans_type, " +
	    		"                      CASE WHEN NVL(opr_sum,0) = 0 " +
	    		"                           THEN '' " +
	    		"                           ELSE DECODE(cd_trans_action, 'CANCEL', '<span style=\"color:red\">- '||opr_sum_frmt||'</span>', 'RETURN', '<span style=\"color:red\">- '||opr_sum_frmt||'</span>', opr_sum_frmt)||' '||(CASE WHEN cd_currency = 10001 THEN '<font color=\"green\">'||sname_currency||'</font>' ELSE sname_currency END)  " +
	    		"                      END opr_sum_frmt, " +
	    		"                      cd_currency, sname_currency, cd_card1_hide, cd_card1, id_trans, id_trans_group, rrn, cd_telgr_external_state, " +
	    		"					   context_access_type, context_id_user, context_id_dealer, " +
	    		"                      count(*) over () as row_count, level_trans " +
	            "           	  FROM (SELECT sys_date_frmt, desc_trans, nc_term, type_trans_txt, cd_trans_state, " +
                "                              fcd_trans_state, name_trans_state, cd_trans_type, fcd_trans_type, opr_sum, opr_sum_frmt, " +
                "                              cd_currency, sname_currency, cd_card1_hide, cd_card1, id_trans, id_trans_group, "+
                "                              rrn, cd_telgr_external_state, pay_type, sname_trans_pay_type, cd_trans_action, " +
                "                 			   context_access_type, context_id_user, context_id_dealer, LEVEL level_trans" +
                "                         FROM " + getGeneralDBScheme() + ".vp$trans_all " +
	            "                        WHERE fcd_trans_type <> 'REC_QUESTIONING' ";
        	
        	if (!isEmpty(pRRN)) {
            	mySQL = mySQL +
	            	"        AND UPPER(rrn) LIKE UPPER('%'||?||'%') ";
            	pParam.add(new bcFeautureParam("string", pRRN.replaceAll(" ", "")));
           	}
            if (!isEmpty(pCdCard1)) {
            	mySQL = mySQL +
	            	"        AND UPPER(cd_card1) LIKE UPPER('%'||?||'%') ";
            	pParam.add(new bcFeautureParam("string", pCdCard1.replaceAll(" ", "")));
           	}
        	if (!isEmpty(pDateBeg)) {
        		mySQL = mySQL + 
	            	"                  AND TRUNC(sys_date) >= TO_DATE(?,'" + getDateFormat() + "')";
            	pParam.add(new bcFeautureParam("string", pDateBeg));
        	}
        	if (!isEmpty(pDateEnd)) {
	        	mySQL = mySQL + 
	        		"                  AND TRUNC(sys_date) <= TO_DATE(?,'" + getDateFormat() + "')";
	        	pParam.add(new bcFeautureParam("string", pDateEnd));
        	}
        	
            if (!isEmpty(pFind)) {
            	mySQL = mySQL + " AND (UPPER(sys_date_frmt) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(TO_CHAR(nc_term)) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(cd_card1) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(opr_sum_frmt) LIKE UPPER('%'||?||'%')) ";
            	pParam.add(new bcFeautureParam("string", pFind));
            	pParam.add(new bcFeautureParam("string", pFind));
            	pParam.add(new bcFeautureParam("string", pFind.replaceAll(" ", "")));
            	pParam.add(new bcFeautureParam("string", pFind));            	
           	}
            
            if (!isEmpty(pOperationType)) {
            	mySQL = mySQL +
	            	"        AND cd_trans_type = ? ";
            	pParam.add(new bcFeautureParam("string", pOperationType));
           	}
            
            if (!isEmpty(pPayType)) {
            	mySQL = mySQL +
	            	"        AND pay_type = ? ";
            	pParam.add(new bcFeautureParam("string", pPayType));
           	}
            
            if (!isEmpty(pOperationState)) {
            	mySQL = mySQL +
	            	"        AND cd_trans_state = ? ";
            	pParam.add(new bcFeautureParam("string", pOperationState));
           	} else {
           		mySQL = mySQL +
            	"        AND fcd_trans_state <> 'C_IN_PROCESS_TRANS' ";
           	}
            
            mySQL = mySQL +
	            "        START WITH id_trans_group is null" +
	            "        CONNECT BY PRIOR id_trans = id_trans_group " +
            	"        ORDER SIBLINGS BY sys_date DESC, nc_term DESC, id_trans DESC)) " +
	            "        WHERE ROWNUM < ?) " +
	            " WHERE rn >= ?";
            
            pParam.add(new bcFeautureParam("int", p_end));
            pParam.add(new bcFeautureParam("int", p_beg));
        	
        	LOGGER.debug(prepareSQLToLog(mySQL,pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);	
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-13; i++) {
            	if ("FCD_TRANS_STATE".equalsIgnoreCase(mtd.getColumnName(i))) {
            		html.append("<th>1</th>\n");
            	//} else if ("CHEQUE_NUMBER".equalsIgnoreCase(mtd.getColumnName(i))) {
            	//	html.append(getBottomFrameTableTH(webposXML, mtd.getColumnName(i)));
            	} else {
            		html.append(getBottomFrameTableTH(webposXML, mtd.getColumnName(i)));
            	}
            }
            html.append("<th colspan=\"3\">2</th>");
            html.append("</tr></thead><tbody>");
            
            String lStateImageSrc = "";
            String lStateImageTitle = "";
            int rowCount = 0;

            while (rset.next())
            {
            	rowCount ++;
            	operationsCount = rset.getString("ROW_COUNT");
            	html.append("<tr>\n");
            	contextParam = "context parameters: access type="+rset.getString("CONTEXT_ACCESS_TYPE")+
            				   ", id_user="+rset.getString("CONTEXT_ID_USER")+
            				   ", id_dealer="+rset.getString("CONTEXT_ID_DEALER");

				lStateImageSrc = "";
				lStateImageTitle = rset.getString("NAME_TRANS_STATE");
				if ("C_OK_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
					lStateImageSrc = "images/oper/success.png";
				} else if ("C_IN_PROCESS_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
					lStateImageSrc = "images/oper/in_process.png";
				} else if ("C_CANCELLED_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
					lStateImageSrc = "images/oper/cancel.png";
				} else if ("C_RETURNED_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
					lStateImageSrc = "images/oper/return.png";
				} else if ("C_RETURNED_PART_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
					lStateImageSrc = "images/oper/return_partially.png";
				} else if ("C_ERROR_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
					lStateImageSrc = "images/oper/error.png";
				} else if ("C_SENT_FOR_PAYMENT".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
					lStateImageSrc = "images/oper/sent_pay.png";
				} else if ("C_NEED_CONFIRM".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
					lStateImageSrc = "images/oper/waiting.gif";
				} else {
					lStateImageSrc = "";
				}
				if ("2".equalsIgnoreCase(rset.getString("LEVEL_TRANS") )) {
					html.append(getBottomFrameTableTD("CHEQUE_NUMBER", rset.getString("CHEQUE_NUMBER"), "", "", ""));
					html.append(getBottomFrameTableTDBase("3", "OPERATION_TYPE", Types.OTHER, "&nbsp;&nbsp;&nbsp;-&nbsp;"+rset.getString("OPERATION_TYPE"), "", "", ""));
					html.append(getBottomFrameTableTDAlign("OPERATION_SUM", rset.getString("OPERATION_SUM"), "", "", "", "right"));

                	html.append("<td colspan=\"4\">&nbsp;</td>");
				} else { 
                	if ("INVOICE".equalsIgnoreCase(rset.getString("PAY_TYPE"))) {
                		myBGColor = "style=\"background-color:#dddddd;\"";
                	}
	                for (int i=1; i<=colCount-13; i++) {
	        			if ("FCD_TRANS_STATE".equalsIgnoreCase(mtd.getColumnName(i))) {
	        				html.append("<td>");
	        				if (!isEmpty(lStateImageSrc)) {
	        					String imgName = "img"+rset.getString("ID_TRANS"); //+"_"+rset.getString("CONTEXT_ACCESS_TYPE")+"_"+rset.getString("CONTEXT_ID_USER")+"_"+rset.getString("CONTEXT_ID_DEALER");
	        					html.append("<img name=\""+imgName+"\" width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"" + lStateImageSrc + "\" title=\"" + lStateImageTitle + "\">");
	        				} else {
	        					html.append("");
	        				}
	        				html.append("</td>");
	        			} else {
	        				if ("OPERATION_SUM".equalsIgnoreCase(mtd.getColumnName(i))) {
	        					html.append(getBottomFrameTableTDAlign(mtd.getColumnName(i), rset.getString(i), "", "", myBGColor, "right"));
	        				} else {
	        					html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", myBGColor));
	        				}
	        			}
	                }
	                if ("C_OK_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE")) || 
	                		"C_SENT_FOR_PAYMENT".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE")) || 
	                		"C_RETURNED_PART_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
		            	if (hasStornoPermission && 
		            			("REC_PAYMENT".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE")) ||
		            			 "REC_SHARE_FEE".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE")) ||
		            			 "REC_MEMBERSHIP_FEE".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE")) ||
		            			 "REC_POINT_FEE".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE")) ||
		            			 "REC_MTF".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE")) ||
		            			 "REC_TRANSFER_GET_POINT".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE")) ||
		            			 "REC_TRANSFER_PUT_POINT".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE")) ||
		            			 "REC_PUT_CARD".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE")) ||
		            			 "REC_COMPOUND_OPER".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE"))
		            			)
		            		) {
		            	
		            		String myStornoLink = "action/storno.jsp?id_term=" + this.idTerminal + "&id_telgr="+rset.getString("ID_TRANS") + "&cd_card1="+rset.getString("CD_CARD1")+"&storno_rrn="+rset.getString("RRN")+"&type=storno&process=no&action=check&back_type=operations";
		            		html.append("<td " + myBGColor + "><div class=\"div_button\" onclick=\"ajaxpage('"+ myStornoLink + "','div_main');\">" + "<img vspace=\"0\" hspace=\"0\"" +
		            				" src=\"images/oper/storno.png\" align=\"top\" width=\"16\" height=\"16\" style=\"border: 0px;\"" + 
		            				" title=\"" + webposXML.getfieldTransl("title_storno_action", false)+"\">" + getHyperLinkEnd()+"</td>\n");
		            	} else {
		            		html.append("<td " + myBGColor + ">&nbsp;</td>");
		            	}
	                } else if ("C_CANCELLED_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
	                	html.append("<td " + myBGColor + ">&nbsp;</td>");
	                } else {
	                	html.append("<td " + myBGColor + ">&nbsp;</td>");
	                }
	                if ("C_NEED_CONFIRM".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
	            		String myConfirmLink = "report/operationupdate.jsp?id="+rset.getString("ID_TRANS")+ "&type=trans&process=no&action=confirm";
	            		html.append("<td " + myBGColor + "><div class=\"div_button\" onclick=\"ajaxpage('"+ myConfirmLink + "','div_action_big');\">" + "<img vspace=\"0\" hspace=\"0\"" +
	            				" src=\"images/row_edit_v.GIF\" align=\"top\" width=\"16\" height=\"16\" style=\"border: 0px;\"" + 
	            				" title=\"" + buttonXML.getfieldTransl("confirm", false)+"\">" + getHyperLinkEnd()+"</td>\n");
	                } else if ("C_SENT_FOR_PAYMENT".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
	            		String myConfirmLink = "report/operationupdate.jsp?id="+rset.getString("ID_TRANS")+ "&type=trans&process=no&action=pay";
	            		html.append("<td " + myBGColor + "><div class=\"div_button\" onclick=\"ajaxpage('"+ myConfirmLink + "','div_action_big');\">" + "<img vspace=\"0\" hspace=\"0\"" +
	            				" src=\"images/oper/pay.png\" align=\"top\" width=\"16\" height=\"16\" style=\"border: 0px;\"" + 
	            				" title=\"" + buttonXML.getfieldTransl("pay", false)+"\">" + getHyperLinkEnd()+"</td>\n");
	                } else {
	                	html.append("<td " + myBGColor + ">&nbsp;</td>");
	                }
	                String myChequeLink = "report/cheque.jsp?id="+rset.getString("ID_TRANS")+"&id_group="+rset.getString("ID_TRANS_GROUP");
	                html.append("<td " + myBGColor + "><A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('" + myChequeLink+"','blank','height=600,width=480,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
	        		html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/oper/cheque.gif\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"   title=\"" + webposXML.getfieldTransl("title_print_cheque", false) + "\">\n");
	        		html.append("</a></td>");
	                html.append("</tr>\n");
                }
            }
            html.append("</tbody></table>\n");
            if (rowCount > 0) {
	            html.append("<br><span>\n");
	            html.append("<font style=\"color:green; font-weight: bold;\">" + commonXML.getfieldTransl("title_reference_designation", false) + ":</font><br>\n");
	            html.append(getNoteOperationState("1"));
	            html.append(getNoteOperationAll("2"));
	            html.append("</span>\n");
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(e.toString());}
        catch (Exception el) {LOGGER.error(html, el); html.append(el.toString());}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        //System.out.println(html.toString());
        LOGGER.debug(contextParam);
        return html.toString();
    } // getCardTransHTML
    
    private String formatTransList(
    		int ni, 
    		int n_payment, 
    		int n_mov_bon, 
    		int n_chk_card, 
    		int n_inval_card, 
    		int n_storno_bon, 
    		int n_payment_im, 
    		int n_payment_ext, 
    		int n_actvation, 
    		//int n_cancel, 
    		int n_coupon, 
    		int n_membership_fee, 
    		int n_mtf, 
    		int n_point_fee, 
    		int n_put_card, 
    		int n_questioning, 
    		//int n_return, 
    		int n_share_fee, 
    		int n_transfer_get_point, 
    		int n_payment_invoice, 
    		int n_share_fee_change, 
    		int n_transform_from_share, 
    		int n_transfer_put_point) {

    	String returnValue = "";
        String C_REC_PAYMENT 				= getTransTypeShortName("1") + ", ";
        String C_REC_MOV_BON        		= getTransTypeShortName("2") + ", ";
        String C_REC_CHK_CARD       		= getTransTypeShortName("3") + ", ";
        String C_REC_INVAL_CARD     		= getTransTypeShortName("4") + ", ";
        String C_REC_STORNO_BON     		= getTransTypeShortName("5") + ", ";
        String C_REC_PAYMENT_IM     		= getTransTypeShortName("6") + " ";
        String C_REC_PAYMENT_EXT    		= getTransTypeShortName("7") + ", ";
        String C_REC_ACTIVATION     		= getTransTypeShortName("8") + ", ";
        //String C_REC_CANCEL         		= getTransTypeShortName("9") + ", ";
        String C_REC_COUPON         		= getTransTypeShortName("10") + ", ";
        String C_REC_MEMBERSHIP_FEE 		= getTransTypeShortName("11") + ", ";
        String C_REC_MTF            		= getTransTypeShortName("12") + ", ";
        String C_REC_POINT_FEE      		= getTransTypeShortName("13") + ", ";
        String C_REC_PUT_CARD       		= getTransTypeShortName("14") + ", ";
        String C_REC_QUESTIONING    		= getTransTypeShortName("15") + ", ";
        //String C_REC_RETURN         		= getTransTypeShortName("16") + ", ";
        String C_REC_SHARE_FEE      		= getTransTypeShortName("17") + ", ";
        String C_REC_TRANSFER_GET_POINT 	= getTransTypeShortName("18") + ", ";
        String С_REC_PAYMENT_INVOICE 		= getTransTypeShortName("19") + ", ";
        String C_REC_SHARE_FEE_CHANGE 		= getTransTypeShortName("20") + ", ";
        String C_REC_TRANSFORM_FROM_SHARE 	= getTransTypeShortName("21") + ", ";
        String C_REC_TRANSFER_PUT_POINT 	= getTransTypeShortName("22") + ", ";
        
        if (n_payment > 0) { returnValue= returnValue + C_REC_PAYMENT; }
        if (n_mov_bon > 0) { returnValue= returnValue + C_REC_MOV_BON; }
        if (n_chk_card > 0) { returnValue= returnValue + C_REC_CHK_CARD; }
        if (n_inval_card > 0) { returnValue= returnValue + C_REC_INVAL_CARD; }
        if (n_storno_bon > 0) { returnValue= returnValue + C_REC_STORNO_BON; }
        if (n_payment_im > 0) { returnValue= returnValue + C_REC_PAYMENT_IM; }
        if (n_payment_ext > 0) { returnValue= returnValue + C_REC_PAYMENT_EXT; }
        if (n_actvation > 0) { returnValue= returnValue + C_REC_ACTIVATION; }
        //if (n_cancel > 0) { returnValue= returnValue + C_REC_CANCEL; }
        if (n_coupon > 0) { returnValue= returnValue + C_REC_COUPON; }
        if (n_membership_fee > 0) { returnValue= returnValue + C_REC_MEMBERSHIP_FEE; }
        if (n_mtf > 0) { returnValue= returnValue + C_REC_MTF; }
        if (n_point_fee > 0) { returnValue= returnValue + C_REC_POINT_FEE; }
        if (n_put_card > 0) { returnValue= returnValue + C_REC_PUT_CARD; }
        if (n_questioning > 0) { returnValue= returnValue + C_REC_QUESTIONING; }
        //if (n_return > 0) { returnValue= returnValue + C_REC_RETURN; }
        if (n_share_fee > 0) { returnValue= returnValue + C_REC_SHARE_FEE; }
        if (n_transfer_get_point > 0) { returnValue= returnValue + C_REC_TRANSFER_GET_POINT; }
        if (n_payment_invoice > 0) { returnValue= returnValue + С_REC_PAYMENT_INVOICE; }
        if (n_share_fee_change > 0) { returnValue= returnValue + C_REC_SHARE_FEE_CHANGE; }
        if (n_transform_from_share > 0) { returnValue= returnValue + C_REC_TRANSFORM_FROM_SHARE; }
        if (n_transfer_put_point > 0) { returnValue= returnValue + C_REC_TRANSFER_PUT_POINT; }
        if (returnValue.length() > 0) {
        	returnValue = returnValue.substring(0, returnValue.length()-2);	
        	if (ni > 1) {
            	/*returnValue = "<div class=\"div_button\" onclick=\"ajaxpage('action/storno.jsp?id_term=11111111&id_telgr=191048&cd_card1=9900990010014&type=storno&process=no&action=check&back_type=operations','div_main');\">" + returnValue;
        		returnValue = returnValue + "&nbsp;<span class=\"telgr_detail\">&nbsp;</span>";
            	returnValue = returnValue + "</div>";*/
        	}
        }
    	return returnValue;
    }
    
    public String getOnlineOperationHTML(String pIdUser, String pFind, String pOperationType, String pOperationState, String pPayType, String pRRN, String pCdCard1, String pDateBeg, String pDateEnd, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	PreparedStatement st = null;
        Connection con = null; 
        String mySQL = "";
        String myBGColor = "";
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        boolean hasStornoPermission = false;
        //String contextParam = "";
        
        readTransTypes();
        
        try{
	       	 if (isEditMenuPermited("WEBPOS_SERVICE_STORNO")>0) {
	       		hasStornoPermission = true;
	    	 }
            
        	mySQL = 
        		" SELECT /*rn,*/ nc cheque_number, date_telgr_frmt operation_date, cd_card1, opr_sum_frmt operation_sum, " +
        		"        sname_trans_pay_type cheque_type, null type_trans, " +
	   			"        cd_telgr_state, name_telgr_state, ni, n_payment, n_mov_bon, n_chk_card, n_inval_card, " +
	            "		 n_storno_bon, n_payment_im, n_payment_ext, n_actvation, " +
	            "		 /*n_cancel,*/ n_coupon, n_membership_fee, n_mtf, " +
	            "		 n_point_fee, n_put_card, n_questioning, /*n_return,*/ " +
	            "		 n_share_fee, n_transfer_get_point, n_transfer_put_point, n_payment_invoice," +
	            "        n_share_fee_change, n_transform_from_share, id_telgr, cd_telgr_external_state, cd_trans_action, " +
	    		"        pay_type, rrn, row_count " +
	            " FROM (SELECT ROWNUM rn, nc, sname_trans_pay_type," +
	            "              date_telgr_frmt, cd_card1, " +
	            "			   opr_sum_frmt, cd_telgr_state, name_telgr_state, " +
	            "			   ni, n_payment, n_mov_bon, n_chk_card, n_inval_card, " +
	            "			   n_storno_bon, n_payment_im, n_payment_ext, n_actvation, " +
	            "			   /*n_cancel,*/ n_coupon, n_membership_fee, n_mtf, " +
	            "			   n_point_fee, n_put_card, n_questioning, /*n_return,*/ " +
	            "			   n_share_fee, n_transfer_get_point, n_transfer_put_point, n_payment_invoice," +
	            "              n_share_fee_change, n_transform_from_share, id_telgr, cd_telgr_external_state, cd_trans_action, " +
	    		"              pay_type, rrn, row_count " +
	            " 		  FROM (SELECT nc, pay_type, " +
	            "                      CASE pay_type " +
	            "							WHEN 'CASH' THEN '<font color=\"green\">'||sname_trans_pay_type||'</font>' " +
	            "							WHEN 'BANK_CARD' THEN '<font color=\"blue\">'||sname_trans_pay_type||'</font>' " +
	            "							WHEN 'SMPU_CARD' THEN '<font color=\"red\">'||sname_trans_pay_type||'</font>' " +
	            "							WHEN 'INVOICE' THEN '<font color=\"black\"><b>'||sname_trans_pay_type||'</b></font>' " +
	            "							ELSE '<font color=\"red\">'||sname_trans_pay_type||'</font>' " +
	            "					   END sname_trans_pay_type," +
	            "                      date_telgr_frmt, cd_card1, " +
	            "				       opr_sum_frmt, cd_telgr_state, name_telgr_state, " +
	            "					   ni, n_payment, n_mov_bon, n_chk_card, n_inval_card, " +
	            "					   n_storno_bon, n_payment_im, n_payment_ext, n_actvation, " +
	            "					   /*n_cancel,*/ n_coupon, n_membership_fee, n_mtf, " +
	            "					   n_point_fee, n_put_card, n_questioning, /*n_return,*/ " +
	            "					   n_share_fee, n_transfer_get_point, n_transfer_put_point, n_payment_invoice," +
	            "                      n_share_fee_change, n_transform_from_share, id_telgr, " +
	            "                      cd_telgr_external_state, cd_trans_action, rrn, " +
	    		"                      count(*) over () as row_count " +
	            "           	  FROM (SELECT nc, pay_type, sname_trans_pay_type, date_telgr_frmt, cd_card1_hide cd_card1, " +
	            "						       opr_sum_frmt||' '||sname_currency opr_sum_frmt, cd_telgr_state, name_telgr_state, " +
	            "							   ni, n_payment, n_mov_bon, n_chk_card, n_inval_card, " +
	            "							   n_storno_bon, n_payment_im, n_payment_ext, n_actvation, " +
	            "							   /*n_cancel,*/ n_coupon, n_membership_fee, n_mtf, " +
	            "							   n_point_fee, n_put_card, n_questioning, /*n_return,*/ " +
	            "						       n_share_fee, n_transfer_get_point, n_transfer_put_point, n_payment_invoice," +
	            "                              n_share_fee_change, n_transform_from_share, id_telgr, " +
	            "                              NULL cd_telgr_external_state, cd_trans_action, rrn " +
                "                         FROM " + getGeneralDBScheme() + ".vp$telgr_all " +
	            "                        WHERE NVL(ni,0) <> NVL(n_questioning,0) /*только анкета*/ ";
        	
        	if (!isEmpty(pRRN)) {
            	mySQL = mySQL +
	            	"        AND UPPER(rrn) LIKE UPPER('%'||?||'%') ";
            	pParam.add(new bcFeautureParam("string", pRRN.replaceAll(" ", "")));
           	}
            if (!isEmpty(pCdCard1)) {
            	mySQL = mySQL +
	            	"        AND UPPER(cd_card1) LIKE UPPER('%'||?||'%') ";
            	pParam.add(new bcFeautureParam("string", pCdCard1.replaceAll(" ", "")));
           	}
        	if (!isEmpty(pDateBeg)) {
        		mySQL = mySQL + 
	            	"                  AND TRUNC(date_telgr) >= TO_DATE(?,'" + getDateFormat() + "')";
            	pParam.add(new bcFeautureParam("string", pDateBeg));
        	}
        	if (!isEmpty(pDateEnd)) {
	        	mySQL = mySQL + 
	        		"                  AND TRUNC(date_telgr) <= TO_DATE(?,'" + getDateFormat() + "')";
	        	pParam.add(new bcFeautureParam("string", pDateEnd));
        	}
        	
            if (!isEmpty(pFind)) {
            	mySQL = mySQL + " AND (UPPER(date_telgr_frmt) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(TO_CHAR(nc)) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(cd_card1) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(opr_sum_frmt) LIKE UPPER('%'||?||'%')) ";
            	pParam.add(new bcFeautureParam("string", pFind));
            	pParam.add(new bcFeautureParam("string", pFind));
            	pParam.add(new bcFeautureParam("string", pFind.replaceAll(" ", "")));
            	pParam.add(new bcFeautureParam("string", pFind));            	
           	}
            
            if (!isEmpty(pOperationType)) {
            	if ("1".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_payment, 0) > 0 ";
            	} else if ("2".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_mov_bon, 0) > 0 ";
            	} else if ("3".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_chk_card, 0) > 0 ";
            	} else if ("4".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_inval_card, 0) > 0 ";
            	} else if ("5".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_storno_bon, 0) > 0 ";
            	} else if ("6".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_payment_im, 0) > 0 ";
            	} else if ("7".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_payment_ext, 0) > 0 ";
            	} else if ("8".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_actvation, 0) > 0 ";
            	//} else if ("9".equalsIgnoreCase(pOperationType)) {
            	//	mySQL = mySQL +	" AND NVL(n_cancel, 0) > 0 ";
            	} else if ("10".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_coupon, 0) > 0 ";
            	} else if ("11".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_membership_fee, 0) > 0 ";
            	} else if ("12".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_mtf, 0) > 0 ";
            	} else if ("13".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_point_fee, 0) > 0 ";
            	} else if ("14".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_put_card, 0) > 0 ";
            	} else if ("15".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_questioning, 0) > 0 ";
            	//} else if ("16".equalsIgnoreCase(pOperationType)) {
            	//	mySQL = mySQL +	" AND NVL(n_return, 0) > 0 ";
            	} else if ("17".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_share_fee, 0) > 0 ";
            	} else if ("18".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_transfer_get_point, 0) > 0 ";
            	} else if ("19".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_payment_invoice, 0) > 0 ";
            	} else if ("20".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_share_fee_change, 0) > 0 ";
            	} else if ("21".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_transform_from_share, 0) > 0 ";
            	} else if ("22".equalsIgnoreCase(pOperationType)) {
            		mySQL = mySQL +	" AND NVL(n_transfer_put_point, 0) > 0 ";
            	}
           	}
            
            if (!isEmpty(pPayType)) {
            	mySQL = mySQL +
	            	"        AND pay_type = ? ";
            	pParam.add(new bcFeautureParam("string", pPayType));
           	}
            
            if (!isEmpty(pOperationState)) {
            	mySQL = mySQL +
	            	"        AND cd_telgr_state = ? ";
            	pParam.add(new bcFeautureParam("string", pOperationState));
           	} else {
           		mySQL = mySQL +
            	"        AND cd_telgr_state <> 'IN_PROCESS' ";
           	}
            
            mySQL = mySQL +
	            "        ORDER BY date_telgr DESC, nc_term DESC, id_telgr DESC)) " +
	            "        WHERE ROWNUM < ?) " +
	            " WHERE rn >= ?";
            
            pParam.add(new bcFeautureParam("int", p_end));
            pParam.add(new bcFeautureParam("int", p_beg));
        	
        	LOGGER.debug(prepareSQLToLog(mySQL,pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);	
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();

            html.append(getBottomFrameTable());
            html.append("<tr>");
            html.append(getBottomFrameTableTH(webposXML, "CHEQUE_NUMBER"));
            html.append(getBottomFrameTableTH(webposXML, "OPERATION_DATE"));
            html.append(getBottomFrameTableTH(webposXML, "CD_CARD1"));
            html.append(getBottomFrameTableTH(webposXML, "OPERATION_SUM"));
            html.append(getBottomFrameTableTH(webposXML, "CHEQUE_TYPE"));
            html.append(getBottomFrameTableTH(webposXML, "OPERATION_TYPE"));
            html.append("<th>1</th>\n");
            html.append("<th colspan=\"3\">2</th>");
            html.append("</tr></thead><tbody>");
            
            String lStateImageSrc = "";
            String lStateImageTitle = "";
            int rowCount = 0;
	    	String cancelStyle = "";
	    	String operName = "";

            while (rset.next())
            {
            	rowCount ++;
            	operationsCount = rset.getString("ROW_COUNT");
            	html.append("<tr>\n");
            	//contextParam = "context parameters: access type="+rset.getString("CONTEXT_ACCESS_TYPE")+
            	//			   ", id_user="+rset.getString("CONTEXT_ID_USER")+
            	//			   ", id_dealer="+rset.getString("CONTEXT_ID_DEALER");

				lStateImageSrc = "";
				lStateImageTitle = rset.getString("NAME_TELGR_STATE");
				if ("EXECUTED".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
					lStateImageSrc = "images/oper/success.png";
				} else if ("IN_PROCESS".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
					lStateImageSrc = "images/oper/in_process.png";
				} else if ("CANCELLED".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
					lStateImageSrc = "images/oper/cancel.png";
				} else if ("RETURNED".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
					lStateImageSrc = "images/oper/return.png";
				} else if ("RETURNED_PARTIALLY".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
					lStateImageSrc = "images/oper/return_partially.png";
				} else if ("ERROR".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
					lStateImageSrc = "images/oper/error.png";
				} else if ("UNKNOWN".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
					lStateImageSrc = "images/oper/error.png";
				} else if ("FATAL_ERROR".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
					lStateImageSrc = "images/oper/error.png";
				} else if ("WARNING".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
					lStateImageSrc = "images/oper/error.png";
				} else if ("SENT_FOR_PAYMENT".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
					lStateImageSrc = "images/oper/sent_pay.png";
				} else if ("NEED_CONFIRM".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
					lStateImageSrc = "images/oper/waiting.gif";
				} else if ("INVOICE_PAID".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
					lStateImageSrc = "images/oper/paid.gif";
				} else {
					lStateImageSrc = "";
				}

            	if ("INVOICE".equalsIgnoreCase(rset.getString("PAY_TYPE"))) {
            		myBGColor = "style=\"background-color:#eeeeee;\"";
            	} else {
            		myBGColor = "";
            	}
				    for (int i=1; i<=6; i++) {
				    	cancelStyle = "";
				    	if ("CANCELLED".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
				    		cancelStyle = "<font style=\"color:red;text-decoration: line-through;font-weight:bold;\" title=\"" + webposXML.getfieldTransl("cheque_operation_cancelled", false) + "\">";
				    	} else if ("RETURNED".equalsIgnoreCase(rset.getString("CD_TELGR_STATE")) ||
				    			"RETURNED_PARTIALLY".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
				    		cancelStyle = "<font style=\"color:red;text-decoration: line-through;font-weight:bold;\" title=\"" + webposXML.getfieldTransl("cheque_operation_returned", false) + "\">";
				    	}
	        			if ("OPERATION_SUM".equalsIgnoreCase(mtd.getColumnName(i))) {
	        				html.append(getBottomFrameTableTDAlign(mtd.getColumnName(i), rset.getString(i), "", cancelStyle, myBGColor, "right"));
	        			} else if ("TYPE_TRANS".equalsIgnoreCase(mtd.getColumnName(i))) {
	        				String transList = formatTransList(
	        						rset.getInt("ni"), 
	        						rset.getInt("n_payment"), 
	        						rset.getInt("n_mov_bon"), 
	        						rset.getInt("n_chk_card"), 
	        						rset.getInt("n_inval_card"), 
	        						rset.getInt("n_storno_bon"), 
	        						rset.getInt("n_payment_im"), 
	        						rset.getInt("n_payment_ext"), 
	        						rset.getInt("n_actvation"), 
	        						//rset.getInt("n_cancel"), 
	        						rset.getInt("n_coupon"), 
	        						rset.getInt("n_membership_fee"), 
	        						rset.getInt("n_mtf"), 
	        						rset.getInt("n_point_fee"), 
	        						rset.getInt("n_put_card"), 
	        						rset.getInt("n_questioning"), 
	        						//rset.getInt("n_return"), 
	        						rset.getInt("n_share_fee"), 
	        						rset.getInt("n_transfer_get_point"), 
	        						rset.getInt("n_payment_invoice"), 
	        						rset.getInt("n_share_fee_change"), 
	        						rset.getInt("n_transform_from_share"), 
	        						rset.getInt("n_transfer_put_point"));
	        				html.append(getBottomFrameTableTD("TYPE_TRANS", transList, "", "", myBGColor));
	        			} else if ("CHEQUE_TYPE".equalsIgnoreCase(mtd.getColumnName(i))) {
	        				operName = rset.getString(i);
	        			    if ("CANCEL".equalsIgnoreCase(rset.getString("CD_TRANS_ACTION"))) {
	        			    	operName = operName + " (<font color=\"red\">" + this.webposXML.getfieldTransl("title_trans_action_cancel", false).toLowerCase() + "</font>)";    	
	        			    } else if ("RETURN".equalsIgnoreCase(rset.getString("CD_TRANS_ACTION"))) {
	        			    	operName = operName + " (<font color=\"red\">" + this.webposXML.getfieldTransl("title_trans_action_return", false).toLowerCase() + "</font>)";
	        			    }
	        			    
	        			    html.append(getBottomFrameTableTD(mtd.getColumnName(i), operName, "", "", myBGColor));
	        			} else {
	        				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", myBGColor));
	        			}
	                }

    				html.append("<td " + myBGColor + ">");
    				if (!isEmpty(lStateImageSrc)) {
    					String imgName = "img"+rset.getString("ID_TELGR"); //+"_"+rset.getString("CONTEXT_ACCESS_TYPE")+"_"+rset.getString("CONTEXT_ID_USER")+"_"+rset.getString("CONTEXT_ID_DEALER");
    					html.append("<img name=\""+imgName+"\" width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"" + lStateImageSrc + "\" title=\"" + lStateImageTitle + "\">");
    				} else {
    					html.append("");
    				}
    				html.append("</td>");
    				
	                if ("EXECUTED".equalsIgnoreCase(rset.getString("CD_TELGR_STATE")) || 
	                		"SENT_FOR_PAYMENT".equalsIgnoreCase(rset.getString("CD_TELGR_STATE")) || 
	                		"RETURNED_PARTIALLY".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
		            	if (hasStornoPermission && 
		            			!("CANCEL".equalsIgnoreCase(rset.getString("CD_TRANS_ACTION")) ||
		            					"RETURN".equalsIgnoreCase(rset.getString("CD_TRANS_ACTION"))
		            			)
		            		) {
		            	
		            		String myStornoLink = "action/stornoupdate.jsp?id_term=" + this.idTerminal + "&id_telgr="+rset.getString("ID_TELGR") + "&cd_card1="+rset.getString("CD_CARD1") + "&storno_rrn="+rset.getString("RRN")+"&type=storno&process=no&action=check&back_type=operations";
		            		html.append("<td " + myBGColor + "><div class=\"div_button\" onclick=\"ajaxpage('"+ myStornoLink + "','div_action_big');\">" + "<img vspace=\"0\" hspace=\"0\"" +
		            				" src=\"images/oper/storno.png\" align=\"top\" width=\"16\" height=\"16\" style=\"border: 0px;\"" + 
		            				" title=\"" + webposXML.getfieldTransl("title_storno_action", false)+"\">" + getHyperLinkEnd()+"</td>\n");
		            	} else {
		            		html.append("<td " + myBGColor + ">&nbsp;</td>");
		            	}
	                } else {
	                	html.append("<td " + myBGColor + ">&nbsp;</td>");
	                }
	                if ("NEED_CONFIRM".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
	            		String myConfirmLink = "report/operationupdate.jsp?id_telgr="+rset.getString("ID_TELGR")+ "&type=trans&process=no&action=confirm";
	            		html.append("<td " + myBGColor + "><div class=\"div_button\" onclick=\"ajaxpage('"+ myConfirmLink + "','div_action_big');\">" + "<img vspace=\"0\" hspace=\"0\"" +
	            				" src=\"images/row_edit_v.GIF\" align=\"top\" width=\"16\" height=\"16\" style=\"border: 0px;\"" + 
	            				" title=\"" + buttonXML.getfieldTransl("confirm", false)+"\">" + getHyperLinkEnd()+"</td>\n");
	                } else if ("SENT_FOR_PAYMENT".equalsIgnoreCase(rset.getString("CD_TELGR_STATE"))) {
	            		String myConfirmLink = "report/operationupdate.jsp?id_telgr="+rset.getString("ID_TELGR")+ "&type=trans&process=no&action=pay";
	            		html.append("<td " + myBGColor + "><div class=\"div_button\" onclick=\"ajaxpage('"+ myConfirmLink + "','div_action_big');\">" + "<img vspace=\"0\" hspace=\"0\"" +
	            				" src=\"images/oper/pay.png\" align=\"top\" width=\"16\" height=\"16\" style=\"border: 0px;\"" + 
	            				" title=\"" + buttonXML.getfieldTransl("pay", false)+"\">" + getHyperLinkEnd()+"</td>\n");
	                } else {
	                	html.append("<td " + myBGColor + ">&nbsp;</td>");
	                }
	                if ("INVOICE".equalsIgnoreCase(rset.getString("PAY_TYPE"))) {
	                	String myInvoiceLink = "report/invoice.jsp?id_telgr="+rset.getString("ID_TELGR");
		                html.append("<td " + myBGColor + "><A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('" + myInvoiceLink+"','blank','height=600,width=480,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		        		html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/oper/invoice.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"   title=\"" + webposXML.getfieldTransl("title_print_invoice", false) + "\">\n");
		        		html.append("</a></td>");
		                html.append("</tr>\n");
	                } else {
	                	String myChequeLink = "report/cheque.jsp?id_telgr="+rset.getString("ID_TELGR");
		                html.append("<td " + myBGColor + "><A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('" + myChequeLink+"','blank','height=600,width=480,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
		        		html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/oper/cheque.gif\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"   title=\"" + webposXML.getfieldTransl("title_print_cheque", false) + "\">\n");
		        		html.append("</a></td>");
		                html.append("</tr>\n");
	                }
            }
            html.append("</tbody></table>\n");
            if (rowCount > 0) {
	            html.append("<br><span>\n");
	            html.append("<table class=\"transparent_table\"><tr><td colspan=\"2\">\n");
	            html.append("<font style=\"color:green; font-weight: bold;\">" + commonXML.getfieldTransl("title_reference_designation", false) + ":</font><br>\n");
	            html.append("</td></tr>\n");
	            html.append("<tr><td width=\"50%\">\n");
	            html.append(getTransactionTypeShortNamesHTML());
	            html.append("</td><td>\n");
	            html.append(getNoteOperationState("1"));
	            html.append(getNoteOperationAll("2"));
	            html.append("</td></tr></table>\n");
	            html.append("</span>\n");
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(e.toString());}
        catch (Exception el) {LOGGER.error(html, el); html.append(el.toString());}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        //System.out.println(html.toString());
        //LOGGER.debug(contextParam);
        return html.toString();
    } // getCardTransHTML
    
    public String getOnlineOperationHTML_old2(String pIdUser, String pFind, String pOperationType, String pOperationState, String pPayType, String pRRN, String pCdCard1, String pDateBeg, String pDateEnd, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	PreparedStatement st = null;
        Connection con = null;
        String mySQL = "";
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        boolean hasStornoPermission = false;
        String contextParam = "";
        
        try{
        	hasStornoPermission = true;
            
        	mySQL = 
        		" SELECT /*rn,*/ nc_term cheque_number, name_trans_type operation_type, sname_trans_pay_type goods_pay_way, sys_date_frmt operation_date, cd_card1_hide, opr_sum_frmt operation_sum, /*desc_trans,*/ " +
	   			"        fcd_trans_state, name_trans_state, fcd_trans_type, rrn, id_trans, id_trans_group, cd_card1, cd_telgr_external_state, " +
	   			"		 context_access_type, context_id_user, context_id_dealer, row_count " +
	            " FROM (SELECT ROWNUM rn, sys_date_frmt, desc_trans, nc_term, sname_trans_pay_type, " +
	            "			   DECODE(fcd_trans_type, " +
	            "                       'REC_PAYMENT',  '<font style = \"color: black\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		"                       'REC_ACTIVATION',  '<font style = \"color: blue\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		"                       'REC_PUT_CARD',  '<font style = \"color: #191970\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	            "                       'REC_COUPON',  '<font style = \"color: #8B8989\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		"                       'REC_MEMBERSHIP_FEE',  '<font style = \"color: #8B3A3A\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		"                       'REC_MTF',  '<font style = \"color: #B8860B\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		"                       'REC_POINT_FEE',  '<font style = \"color: #C8860B\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		"                       'REC_SHARE_FEE',  '<font style = \"color: green\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		"                       'REC_TRANSFER_GET_POINT',  '<font style = \"color: #BA55D3\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		"                       'REC_TRANSFER_PUT_POINT',  '<font style = \"color: #BA55D3\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		//"                       'REC_CANCEL',  '<font style = \"color: red\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		//"                       'REC_RETURN',  '<font style = \"color: red\"><b>'||cancel_style_beg||name_trans_type||cancel_style_end||'</b></font>', " +
	    		"                       name_trans_type" +
	    		"              ) name_trans_type, " +
	            "              fcd_trans_state, name_trans_state, fcd_trans_type, " +
	    		"              opr_sum_frmt, cd_card1_hide, cd_card1, id_trans, id_trans_group, rrn, cd_telgr_external_state, " +
	    		"			   context_access_type, context_id_user, context_id_dealer, row_count " +
	            " 		  FROM (SELECT sys_date_frmt, desc_trans, nc_term, " +
	            "                      CASE pay_type " +
	            "							WHEN 'CASH' THEN '<font color=\"green\">'||sname_trans_pay_type||'</font>' " +
	            "							WHEN 'BANK_CARD' THEN '<font color=\"blue\">'||sname_trans_pay_type||'</font>' " +
	            "							WHEN 'SMPU_CARD' THEN '<font color=\"black\">'||sname_trans_pay_type||'</font>' " +
	            "							WHEN 'INVOICE' THEN '<font color=\"brown\">'||sname_trans_pay_type||'</font>' " +
	            "							ELSE '' " +
	            "					   END sname_trans_pay_type," +
	            "                      CASE WHEN fcd_trans_state IN ('C_CANCELLED_TRANS','C_RETURNED_TRANS','C_RETURNED_PART_TRANS') " +
	            "                           THEN '<font style=\"text-decoration: line-through;\" title=\"" + webposXML.getfieldTransl("cheque_operation_cancel", false) + "\">'" +
	            "                           ELSE ''" +
	            "                      END cancel_style_beg," +
	            "			           CASE WHEN fcd_trans_state IN ('C_CANCELLED_TRANS','C_RETURNED_TRANS','C_RETURNED_PART_TRANS') " +
	            "                           THEN '</font>'" +
	            "                           ELSE ''" +
	            "                      END cancel_style_end, " +
	            "                      type_trans_txt name_trans_type, cd_trans_state, fcd_trans_state, name_trans_state, cd_trans_type, fcd_trans_type, " +
	    		"                      CASE WHEN NVL(opr_sum,0) = 0 " +
	    		"                           THEN '' " +
	    		"                           ELSE DECODE(cd_trans_action, 'CANCEL', '<span style=\"color:red\">- '||opr_sum_frmt||'</span>', 'RETURN', '<span style=\"color:red\">- '||opr_sum_frmt||'</span>', opr_sum_frmt)||' '||(CASE WHEN cd_currency = 10001 THEN '<font color=\"green\">'||sname_currency||'</font>' ELSE sname_currency END)  " +
	    		"                      END opr_sum_frmt, " +
	    		"                      cd_currency, sname_currency, cd_card1_hide, cd_card1, id_trans, id_trans_group, rrn, cd_telgr_external_state, " +
	    		"					   context_access_type, context_id_user, context_id_dealer, " +
	    		"                      count(*) over () as row_count " +
	            "           	  FROM (SELECT sys_date_frmt, desc_trans, nc_term, type_trans_txt, cd_trans_state, " +
                "                              fcd_trans_state, name_trans_state, cd_trans_type, fcd_trans_type, opr_sum, opr_sum_frmt, " +
                "                              cd_currency, sname_currency, cd_card1_hide, cd_card1, id_trans, id_trans_group, "+
                "                              rrn, cd_telgr_external_state, pay_type, sname_trans_pay_type, cd_trans_action " +
                "                 			   context_access_type, context_id_user, context_id_dealer " +
                "                         FROM " + getGeneralDBScheme() + ".vp$trans_all " +
	            "                        WHERE fcd_trans_type <> 'REC_QUESTIONING' ";
        	
        	if (!isEmpty(pRRN)) {
            	mySQL = mySQL +
	            	"        AND UPPER(rrn) LIKE UPPER('%'||?||'%') ";
            	pParam.add(new bcFeautureParam("string", pRRN.replaceAll(" ", "")));
           	}
            if (!isEmpty(pCdCard1)) {
            	mySQL = mySQL +
	            	"        AND UPPER(cd_card1) LIKE UPPER('%'||?||'%') ";
            	pParam.add(new bcFeautureParam("string", pCdCard1.replaceAll(" ", "")));
           	}
        	if (!isEmpty(pDateBeg)) {
        		mySQL = mySQL + 
	            	"                  AND TRUNC(sys_date) >= TO_DATE(?,'" + getDateFormat() + "')";
            	pParam.add(new bcFeautureParam("string", pDateBeg));
        	}
        	if (!isEmpty(pDateEnd)) {
	        	mySQL = mySQL + 
	        		"                  AND TRUNC(sys_date) <= TO_DATE(?,'" + getDateFormat() + "')";
	        	pParam.add(new bcFeautureParam("string", pDateEnd));
        	}
        	
            if (!isEmpty(pFind)) {
            	mySQL = mySQL + " AND (UPPER(sys_date_frmt) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(TO_CHAR(nc_term)) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(cd_card1) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(opr_sum_frmt) LIKE UPPER('%'||?||'%')) ";
            	pParam.add(new bcFeautureParam("string", pFind));
            	pParam.add(new bcFeautureParam("string", pFind));
            	pParam.add(new bcFeautureParam("string", pFind.replaceAll(" ", "")));
            	pParam.add(new bcFeautureParam("string", pFind));            	
           	}
            
            if (!isEmpty(pOperationType)) {
            	mySQL = mySQL +
	            	"        AND cd_trans_type = ? ";
            	pParam.add(new bcFeautureParam("string", pOperationType));
           	}
            
            if (!isEmpty(pPayType)) {
            	mySQL = mySQL +
	            	"        AND pay_type = ? ";
            	pParam.add(new bcFeautureParam("string", pPayType));
           	}
            
            if (!isEmpty(pOperationState)) {
            	mySQL = mySQL +
	            	"        AND cd_trans_state = ? ";
            	pParam.add(new bcFeautureParam("string", pOperationState));
           	} else {
           		mySQL = mySQL +
            	"        AND fcd_trans_state <> 'C_IN_PROCESS_TRANS' ";
           	}
            
            mySQL = mySQL +
	            "        ORDER BY sys_date DESC, nc_term DESC, id_trans DESC)) " +
	            "        WHERE ROWNUM < ?) " +
	            " WHERE rn >= ?";
            
            pParam.add(new bcFeautureParam("int", p_end));
            pParam.add(new bcFeautureParam("int", p_beg));
        	
        	LOGGER.debug(prepareSQLToLog(mySQL,pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);	
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-11; i++) {
            	if ("FCD_TRANS_STATE".equalsIgnoreCase(mtd.getColumnName(i))) {
            		html.append("<th>1</th>\n");
            	//} else if ("CHEQUE_NUMBER".equalsIgnoreCase(mtd.getColumnName(i))) {
            	//	html.append(getBottomFrameTableTH(webposXML, mtd.getColumnName(i)));
            	} else {
            		html.append(getBottomFrameTableTH(webposXML, mtd.getColumnName(i)));
            	}
            }
            html.append("<th colspan=\"3\">2</th>");
            html.append("</tr></thead><tbody>");
            
            String lStateImageSrc = "";
            String lStateImageTitle = "";
            int rowCount = 0;

            while (rset.next())
            {
            	rowCount ++;
            	operationsCount = rset.getString("ROW_COUNT");
            	html.append("<tr>\n");
            	contextParam = "context parameters: access type="+rset.getString("CONTEXT_ACCESS_TYPE")+
            				   ", id_user="+rset.getString("CONTEXT_ID_USER")+
            				   ", id_dealer="+rset.getString("CONTEXT_ID_DEALER");

				lStateImageSrc = "";
				lStateImageTitle = rset.getString("NAME_TRANS_STATE");
				if ("C_OK_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
					lStateImageSrc = "images/oper/success.png";
				} else if ("C_IN_PROCESS_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
					lStateImageSrc = "images/oper/in_process.png";
				} else if ("C_CANCELLED_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
					lStateImageSrc = "images/oper/cancel.png";
				} else if ("C_RETURNED_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
					lStateImageSrc = "images/oper/return.png";
				} else if ("C_RETURNED_PART_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
					lStateImageSrc = "images/oper/return_partially.png";
				} else if ("C_ERROR_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
					lStateImageSrc = "images/oper/error.png";
				} else if ("C_SENT_FOR_PAYMENT".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
					lStateImageSrc = "images/oper/sent_pay.png";
				} else if ("C_NEED_CONFIRM".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
					lStateImageSrc = "images/oper/waiting.gif";
				} else {
					lStateImageSrc = "";
				}
				    for (int i=1; i<=colCount-11; i++) {
	        			if ("FCD_TRANS_STATE".equalsIgnoreCase(mtd.getColumnName(i))) {
	        				html.append("<td>");
	        				if (!isEmpty(lStateImageSrc)) {
	        					String imgName = "img"+rset.getString("ID_TRANS"); //+"_"+rset.getString("CONTEXT_ACCESS_TYPE")+"_"+rset.getString("CONTEXT_ID_USER")+"_"+rset.getString("CONTEXT_ID_DEALER");
	        					html.append("<img name=\""+imgName+"\" width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"" + lStateImageSrc + "\" title=\"" + lStateImageTitle + "\">");
	        				} else {
	        					html.append("");
	        				}
	        				html.append("</td>");
	        			} else {
	        				if ("OPERATION_SUM".equalsIgnoreCase(mtd.getColumnName(i))) {
	        					html.append(getBottomFrameTableTDAlign(mtd.getColumnName(i), rset.getString(i), "", "", "", "right"));
	        				} else {
	        					html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	        				}
	        			}
	                }
	                if ("C_OK_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE")) || 
	                		"C_SENT_FOR_PAYMENT".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE")) || 
	                		"C_RETURNED_PART_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
		            	if (hasStornoPermission && 
		            			("REC_PAYMENT".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE")) ||
		            			 "REC_SHARE_FEE".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE")) ||
		            			 "REC_MEMBERSHIP_FEE".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE")) ||
		            			 "REC_POINT_FEE".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE")) ||
		            			 "REC_MTF".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE")) ||
		            			 "REC_TRANSFER_GET_POINT".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE")) ||
		            			 "REC_TRANSFER_PUT_POINT".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE")) ||
		            			 "REC_PUT_CARD".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE")) ||
		            			 "REC_COMPOUND_OPER".equalsIgnoreCase(rset.getString("FCD_TRANS_TYPE"))
		            			)
		            		) {
		            	
		            		String myStornoLink = "action/storno.jsp?id_term=" + this.idTerminal + "&id_telgr="+rset.getString("ID_TRANS") + "&cd_card1="+rset.getString("CD_CARD1")+"&storno_rrn="+rset.getString("RRN")+"&type=storno&process=no&action=check&back_type=operations";
		            		html.append("<td><div class=\"div_button\" onclick=\"ajaxpage('"+ myStornoLink + "','div_main');\">" + "<img vspace=\"0\" hspace=\"0\"" +
		            				" src=\"images/oper/storno.png\" align=\"top\" width=\"16\" height=\"16\" style=\"border: 0px;\"" + 
		            				" title=\"" + webposXML.getfieldTransl("title_storno_action", false)+"\">" + getHyperLinkEnd()+"</td>\n");
		            	} else {
		            		html.append("<td>&nbsp;</td>");
		            	}
	                } else if ("C_CANCELLED_TRANS".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
	                	html.append("<td>&nbsp;</td>");
	                } else {
	                	html.append("<td>&nbsp;</td>");
	                }
	                if ("C_NEED_CONFIRM".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
	            		String myConfirmLink = "report/operationupdate.jsp?id="+rset.getString("ID_TRANS")+ "&type=trans&process=no&action=confirm";
	            		html.append("<td><div class=\"div_button\" onclick=\"ajaxpage('"+ myConfirmLink + "','div_action_big');\">" + "<img vspace=\"0\" hspace=\"0\"" +
	            				" src=\"images/row_edit_v.GIF\" align=\"top\" width=\"16\" height=\"16\" style=\"border: 0px;\"" + 
	            				" title=\"" + buttonXML.getfieldTransl("confirm", false)+"\">" + getHyperLinkEnd()+"</td>\n");
	                } else if ("C_SENT_FOR_PAYMENT".equalsIgnoreCase(rset.getString("FCD_TRANS_STATE"))) {
	            		String myConfirmLink = "report/operationupdate.jsp?id="+rset.getString("ID_TRANS")+ "&type=trans&process=no&action=pay";
	            		html.append("<td><div class=\"div_button\" onclick=\"ajaxpage('"+ myConfirmLink + "','div_action_big');\">" + "<img vspace=\"0\" hspace=\"0\"" +
	            				" src=\"images/oper/pay.png\" align=\"top\" width=\"16\" height=\"16\" style=\"border: 0px;\"" + 
	            				" title=\"" + buttonXML.getfieldTransl("pay", false)+"\">" + getHyperLinkEnd()+"</td>\n");
	                } else {
	                	html.append("<td>&nbsp;</td>");
	                }
	                String myChequeLink = "report/cheque.jsp?id="+rset.getString("ID_TRANS")+"&id_group="+rset.getString("ID_TRANS_GROUP");
	                html.append("<td><A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('" + myChequeLink+"','blank','height=600,width=480,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
	        		html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/oper/cheque.gif\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"   title=\"" + webposXML.getfieldTransl("title_print_cheque", false) + "\">\n");
	        		html.append("</a></td>");
	                html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
            if (rowCount > 0) {
	            html.append("<br><span>\n");
	            html.append("<font style=\"color:green; font-weight: bold;\">" + commonXML.getfieldTransl("title_reference_designation", false) + ":</font><br>\n");
	            html.append(getNoteOperationState("1"));
	            html.append(getNoteOperationAll("2"));
	            html.append("</span>\n");
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(e.toString());}
        catch (Exception el) {LOGGER.error(html, el); html.append(el.toString());}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        //System.out.println(html.toString());
        LOGGER.debug(contextParam);
        return html.toString();
    } // getCardTransHTML
    
    private String cardSalesCount = "0";
    
    public String getCardSalesCount() {
    	return cardSalesCount;
    }
    
    public String getCardSaleListHTML(String pIdUser, String pFind, String pCdCard1, String pRoleState, String pDateBeg, String pDateEnd, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	PreparedStatement st = null;
        Connection con = null;
        String mySQL = "";
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        boolean hasQuestionnairePermission = false;
        boolean hasActivationPermission = false;
        boolean hasReturnPermission = false;
        
        
        try{
        	mySQL = 
        		" SELECT rn, date_card_sale_frmt, cd_card1_hide, total_amount_card_sale_frmt, nat_prs_role_state, id_nat_prs_role, " +
        		"        cd_nat_prs_role_state, id_trans_card_given, id_trans_card_activation, cd_card1, row_count " +
	            "   FROM (SELECT ROWNUM rn, date_card_sale_frmt, cd_card1_hide, cd_card1, total_amount_card_sale_frmt, nat_prs_role_state, id_nat_prs_role, " +
	            "                cd_nat_prs_role_state, id_trans_card_given, id_trans_card_activation, row_count " +
	            " 	  	    FROM (SELECT TO_CHAR(date_card_sale, '"+getDateFormat()+"') date_card_sale_frmt, cd_card1_hide, cd_card1, " +
	            "                        total_amount_card_sale_frmt||' '||sname_currency_card_sale total_amount_card_sale_frmt," +
	            "                        DECODE(cd_nat_prs_role_state, " +
	            "                       		'GIVEN', '<font style = \"color: blue\"><b>" + webposXML.getfieldTransl("nat_prs_role_state_given", false) + "</b></font>', " +
	    		"                       		'ACTIVATED', '<font style = \"color: green\"><b>" + webposXML.getfieldTransl("nat_prs_role_state_activated", false) + "</b></font>', " +
	    		"                       		'QUESTIONED', " +
	    		"									CASE WHEN NVL(is_questionnaire_checked, 'N') = 'Y' " +
	    		"                                        THEN '<font style = \"color: black\"><b>" + webposXML.getfieldTransl("nat_prs_role_state_questioned_checked", false) + "</b></font>' " +
	    		"                                		 ELSE '<font style = \"color: brown\"><b>" + webposXML.getfieldTransl("nat_prs_role_state_questioned_not_checked", false) + "</b></font>' " +
	    		"                           		END, " +
	    		"                       		'CANCEL', '<font style = \"color: red\"><b>" + webposXML.getfieldTransl("nat_prs_role_state_cancel", false) + "</b></font>', " +
	    		"                       		'ERROR', '<font style = \"color: red\"><b>" + webposXML.getfieldTransl("nat_prs_role_state_error", false) + "</b></font>', " +
	            "                       		'UNKNOWN'" +
	    		"              			 ) nat_prs_role_state," +
	    		"                        id_nat_prs_role, cd_nat_prs_role_state, id_trans_card_given, id_trans_card_activation," +
	    		"                        count(*) over () as row_count" +
	            "           	    FROM " + getGeneralDBScheme() + ".vp$nat_prs_role_short_all " +
	            "                  WHERE 1 = 1 ";
	        //    "                  WHERE id_user_who_card_sold = ? ";
        	//pParam.add(new bcFeautureParam("int", pIdUser));
            if (!isEmpty(pCdCard1)) {
            	mySQL = mySQL +
	            	"        AND UPPER(cd_card1) LIKE UPPER('%'||?||'%') ";
            	pParam.add(new bcFeautureParam("string", pCdCard1.replaceAll(" ", "")));
           	}
        	if (!isEmpty(pRoleState)) {
        		if ("QUESTIONED_CHECKED".equalsIgnoreCase(pRoleState)) {
            		mySQL = mySQL + " AND cd_nat_prs_role_state = 'QUESTIONED' AND NVL(is_questionnaire_checked, 'N') = 'Y' ";
        		} else if ("QUESTIONED_NOT_CHECKED".equalsIgnoreCase(pRoleState)) {
            		mySQL = mySQL + " AND cd_nat_prs_role_state = 'QUESTIONED' AND NVL(is_questionnaire_checked, 'N') = 'N' ";
        		} else {
            		mySQL = mySQL + " AND cd_nat_prs_role_state = ? ";
                	pParam.add(new bcFeautureParam("string", pRoleState));
        		}
        	}
        	if (!isEmpty(pDateBeg)) {
        		mySQL = mySQL + 
	            	"                  AND TRUNC(date_card_sale) >= TO_DATE(?,'" + getDateFormat() + "')";
            	pParam.add(new bcFeautureParam("string", pDateBeg));
        	}
        	if (!isEmpty(pDateEnd)) {
	        	mySQL = mySQL + 
	        		"                  AND TRUNC(date_card_sale) <= TO_DATE(?,'" + getDateFormat() + "')";
	        	pParam.add(new bcFeautureParam("string", pDateEnd));
        	}
        	
            if (!isEmpty(pFind)) {
            	mySQL = mySQL + " AND (UPPER(cd_card1) LIKE UPPER('%'||?||'%') OR " +
           			"UPPER(fio_nat_prs) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(TO_CHAR(date_card_sale, '"+getDateFormat()+"')) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(total_amount_card_sale_frmt) LIKE UPPER('%'||?||'%')) ";
            	pParam.add(new bcFeautureParam("string", pFind.replaceAll(" ", "")));
            	pParam.add(new bcFeautureParam("string", pFind));
            	pParam.add(new bcFeautureParam("string", pFind));
            	pParam.add(new bcFeautureParam("string", pFind));
           	}
            
            mySQL = mySQL +
	            "        ORDER BY date_card_sale DESC, cd_card1 DESC) " +
	            "        WHERE ROWNUM < ?) " +
	            " WHERE rn >= ?";
            
            pParam.add(new bcFeautureParam("int", p_end));
            pParam.add(new bcFeautureParam("int", p_beg));
            
        	if (isEditPermited("WEBPOS_SERVICE_CARD_ACTIVATION_ACTIVATION")>0) {
        		hasActivationPermission = true;
       	 	}
        	if (isEditPermited("WEBPOS_SERVICE_CARD_ACTIVATION_QUESTIONNAIRE")>0) {
        		hasQuestionnairePermission = true;
       	 	}
        	if (isEditMenuPermited("WEBPOS_SERVICE_STORNO")>0) {
        		hasReturnPermission = true;
       	 	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL,pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);	
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-6; i++) {
            	html.append(getBottomFrameTableTH(webposXML, mtd.getColumnName(i)));
            }
            html.append("<th colspan=\"4\">1</th>");
            html.append("</tr></thead><tbody>");
            
            int rowCount = 0;

            String idChequeTrans = "";
            while (rset.next())
            {
            	cardSalesCount = rset.getString("ROW_COUNT");
            	idChequeTrans = "";
            	rowCount ++;
            	html.append("<tr>\n");
                for (int i=1; i<=colCount-6; i++) {
        			if ("TOTAL_AMOUNT_CARD_SALE_FRMT".equalsIgnoreCase(mtd.getColumnName(i))) {
        				html.append(getBottomFrameTableTDAlign(mtd.getColumnName(i), rset.getString(i), "", "", "", "right"));
        			} else {
        				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
        			}
                }
                idChequeTrans = rset.getString("ID_TRANS_CARD_GIVEN");
                if (!isEmpty(idChequeTrans)) {
	                String myChequeLink = "report/cheque.jsp?id="+idChequeTrans;
	                html.append("<td><A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('" + myChequeLink+"','blank','height=600,width=480,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
	        		html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/oper/cheque.gif\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\"" + webposXML.getfieldTransl("title_print_cheque_registration", false) + "\">\n");
	        		html.append("</a></td>");
                } else {
                	html.append("<td>&nbsp;</td>");
                }
                if ("GIVEN".equalsIgnoreCase(rset.getString("CD_NAT_PRS_ROLE_STATE")) && 
                		hasReturnPermission) {
	        		String myStornoLink = "action/stornoupdate.jsp?id_term=" + this.idTerminal + "&id="+rset.getString("ID_TRANS_CARD_GIVEN")+"&type=storno&process=no&action=check&back_type=operations";
	        		html.append("<td><div class=\"div_button\" onclick=\"ajaxpage('"+ myStornoLink + "','div_action_big');\">" + "<img vspace=\"0\" hspace=\"0\"" +
        				" src=\"images/oper/storno.png\" align=\"top\" width=\"16\" height=\"16\" style=\"border: 0px;\"" + 
        				" title=\"" + webposXML.getfieldTransl("title_storno_action", false)+"\">" + getHyperLinkEnd()+"</td>\n");
                } else {
                	html.append("<td>&nbsp;</td>");
                }
                if (hasQuestionnairePermission) {
                	String myHyperLink = "action/new_client_questionnaire.jsp?id_role="+rset.getString("ID_NAT_PRS_ROLE")+ "&type=questionnaire&action=edit&process=no";
	        		html.append("<td><div class=\"div_button\" onclick=\"ajaxpage('"+ myHyperLink + "','div_action_big');\">" + "<img vspace=\"0\" hspace=\"0\"" +
	        				" src=\"images/oper/row_edit_v.GIF\" align=\"top\" width=\"16\" height=\"16\" style=\"border: 0px;\"" + 
	        				" title=\"" + webposXML.getfieldTransl("title_questioning", false)+"\">" + getHyperLinkEnd()+"</td>\n");
                } else {
                	html.append("<td>&nbsp;</td>");
                }
                
                if ("QUESTIONED".equalsIgnoreCase(rset.getString("CD_NAT_PRS_ROLE_STATE"))) {
                	if (hasActivationPermission) {
		        		String myActivateLink = "action/new_client_activation.jsp?id_term=" + this.idTerminal + "&id_trans_given="+rset.getString("ID_TRANS_CARD_GIVEN") + "&cd_card1="+rset.getString("CD_CARD1")+"&type=client&process=yes&action=check_card&back_type=operations";
		        		html.append("<td><div class=\"div_button\" onclick=\"ajaxpage('"+ myActivateLink + "','div_action_big');\">" + "<img vspace=\"0\" hspace=\"0\"" +
	        				" src=\"images/oper/activate.png\" align=\"top\" width=\"16\" height=\"16\" style=\"border: 0px;\"" + 
	        				" title=\"" + webposXML.getfieldTransl("title_activate", false)+"\">" + getHyperLinkEnd()+"</td>\n");
                    } else {
                    	html.append("<td>&nbsp;</td>");
                    }
                } else if ("ACTIVATED".equalsIgnoreCase(rset.getString("CD_NAT_PRS_ROLE_STATE"))) {
                	idChequeTrans = rset.getString("ID_TRANS_CARD_ACTIVATION");
                    if (!isEmpty(idChequeTrans)) {
    	                String myChequeLink = "report/cheque.jsp?id="+idChequeTrans;
    	                html.append("<td><A HREF=\"#\" onClick=\"JavaScript: var cWin=window.open('" + myChequeLink+"','blank','height=600,width=480,top=50,left=150,toolbar=no,menubar=no,location=no,scrollbars=yes,status=yes'); cWin.focus();\">\n");
    	        		html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/oper/cheque.gif\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"  title=\"" + webposXML.getfieldTransl("title_print_cheque_activation", false) + "\">\n");
    	        		html.append("</a></td>");
                    } else {
                    	html.append("<td>&nbsp;</td>");
                    }
                } else {
                	html.append("<td>&nbsp;</td>");
                }
                html.append("</tr>");
            }
            html.append("</tbody></table>\n");
            if (rowCount > 0) {
	            html.append("<br><span>\n");
	            html.append("<font style=\"color:green; font-weight: bold;\">" + commonXML.getfieldTransl("title_reference_designation", false) + ":</font><br>\n");
	            html.append(getNoteQuestOperationAll("1"));
	            html.append("</span>\n");
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(e.toString());}
        catch (Exception el) {LOGGER.error(html, el); html.append(el.toString());}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        //System.out.println(html.toString());
        return html.toString();
    } // getCardTransHTML
    
    private String idTargetPrvParent = "";
    
    public String getIdTargetPrgParent () {
    	return idTargetPrvParent;
    }
    
    public String getAllTargetProgramImagesHTML(String pSection, String pIdTargetPrgParent, String pFind, String p_beg, String p_end) {
        String mySQL = "";        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
    	mySQL = 
    		" SELECT rn, id_target_prg, name_target_prg, sname_target_prg, desc_target_prg, id_club, " +
            "		 sname_club, name_nat_prs_initiator, min_pay_amount, min_pay_amount_frmt, " +
            "		 name_nat_prs_administrator, name_jur_prs, " +
            "		 adr_jur_prs, date_beg_frmt, date_end_frmt, id_target_prg_parent, child_count, image_target_prg, row_count " +
            " FROM (SELECT ROWNUM rn, id_target_prg, name_target_prg, sname_target_prg, desc_target_prg, id_club, " +
            "			   sname_club, name_nat_prs_initiator, min_pay_amount, min_pay_amount_frmt, " +
            "			   name_nat_prs_administrator, name_jur_prs, " +
            "			   adr_jur_prs, date_beg_frmt, date_end_frmt, id_target_prg_parent, child_count, image_target_prg, row_count " +
            " 		  FROM (SELECT id_target_prg, name_target_prg, sname_target_prg, desc_target_prg, id_club, " +
            "					   sname_club, name_nat_prs_initiator, min_pay_amount, min_pay_amount_frmt||' '||sname_currency min_pay_amount_frmt, " +
            "					   name_nat_prs_administrator, name_jur_prs, " +
            "					   adr_jur_prs, date_beg_frmt, date_end_frmt, id_target_prg_parent, child_count, image_target_prg," +
            "                      count(*) over () as row_count " +
            "           	  FROM " + getGeneralDBScheme() + ".vc_target_prg_club_all " +
            "                WHERE 1=1 " ;
    	
    	if (!isEmpty(pIdTargetPrgParent)) {
        	mySQL = mySQL + " AND id_target_prg_parent = ? ";
        	pParam.add(new bcFeautureParam("int", pIdTargetPrgParent));
       	} else {
       		mySQL = mySQL + " AND id_target_prg_parent IS NULL ";
       	}
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + " AND (UPPER(name_target_prg) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<1; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
       	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                ORDER BY name_target_prg) " +
            "        WHERE ROWNUM < ?) " +
            " WHERE rn >= ?";
        
        idTargetPrvParent = pIdTargetPrgParent;
        
        return geTargetProgramImagesSelecOneHTML("", pSection, mySQL, pParam, "updateForm5", "div_main");
    } // getCardTransHTML
	
    public String getCardPackLinesHTML(String pFindString, String pIdCardPack, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
			" SELECT id_jur_prs_card_pack, name_jur_prs_card_pack, " +
			"        desc_jur_prs_card_pack, sname_currency, name_card_status, entrance_fee_frmt, " +
			"		 share_fee_frmt, membership_fee_frmt, membership_fee_month_count, " +
			"        dealer_margin_frmt, agent_margin_frmt, total_amount_jp_card_pack_frmt " +
			"   FROM (SELECT ROWNUM rn, id_jur_prs_card_pack, name_jur_prs_card_pack, " +
    		"                desc_jur_prs_card_pack, sname_currency, name_card_status, entrance_fee_frmt, " +
    		"                share_fee_frmt, membership_fee_frmt, membership_fee_month_count, " +
    		"                dealer_margin_frmt, agent_margin_frmt, total_amount_jp_card_pack_frmt " +
    		"           FROM (SELECT * " +
    		"                   FROM " + getGeneralDBScheme() + ".vc_club_jp_card_pack_all " +
    		"                  WHERE id_jur_prs = ? " +
    		"                    AND TRUNC(SYSDATE) BETWEEN NVL(TRUNC(action_date_beg),TRUNC(SYSDATE)) AND NVL(TRUNC(action_date_end),TRUNC(SYSDATE)) ";
    	pParam.add(new bcFeautureParam("int", this.getValue("ID_DEALER")));
    	
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(name_jur_prs_card_pack) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(desc_jur_prs_card_pack) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_card_status) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
   		pParam.add(new bcFeautureParam("int", p_end));
   		pParam.add(new bcFeautureParam("int", p_beg));
       	mySQL = mySQL +
			"                  ORDER BY name_card_status, name_jur_prs_card_pack)" + 
    		"           WHERE ROWNUM < ? " + 
    		" ) WHERE rn >= ?";
    	
		StringBuilder html = new StringBuilder();
		StringBuilder detail = new StringBuilder();
		Connection con = null;
		PreparedStatement st = null;
		
		String lFontAdditional = "<font style=\"color:red; font-weight: bold;\">";
		String currency = "";
        //boolean hasEditPermission = false;
        try{ 
      	  	//if (isEditPermited("CLIENTS_TERMINALS_LOYBON")>0) {
      	  		// Заглушка - ИЗМЕНЕНИЕ СТРОК ЛОЯЛЬНОСТИ ТЕРМИНАЛА ЗАПРЕЩЕНО
      	  		//hasEditPermission = false;
      	  		//hasEditPermission = true;
      	  	//}
      	  	
      	  	LOGGER.debug(prepareSQLToLog(mySQL,pParam));
      	  	con = Connector.getConnection(getSessionId());
      	  	st = con.prepareStatement(mySQL);
      	  	st = prepareParam(st, pParam);
      	  	ResultSet rset = st.executeQuery();
            //ResultSetMetaData mtd = rset.getMetaData();
            //int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            //for (int i=1; i <= colCount-1; i++) {
            	//html.append(getBottomFrameTableTH(loylineXML, mtd.getColumnName(i)));
            	//html.append("<th>" + i + "</th>");
            //}
            html.append(getBottomFrameTableTH(webposXML, "CARD_STATUS"));
            html.append(getBottomFrameTableTH(webposXML, "NAME_JUR_PRS_CARD_PACK"));
            html.append(getBottomFrameTableTH(webposXML, "TOTAL_AMOUNT_CARD_SALE"));
            //html.append(getBottomFrameTableTH(loylineXML, "BONUS_TRANSFER_TERM"));
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	currency = rset.getString("SNAME_CURRENCY");
            	
            	html.append("<tr>");
            	html.append(getBottomFrameTableTD("CARD_STATUS", rset.getString("NAME_CARD_STATUS"), "", "<font style=\"color:green;\">", ""));
            	html.append(getBottomFrameTableTD("NAME_JUR_PRS_CARD_PACK", rset.getString("NAME_JUR_PRS_CARD_PACK"), "admin/setting.jsp?id_card_pack="+rset.getString("ID_JUR_PRS_CARD_PACK"), "<font style=\"color:blue;\">", ""));
            	html.append(getBottomFrameTableTD("TOTAL_AMOUNT_JP_CARD_PACK", rset.getString("TOTAL_AMOUNT_JP_CARD_PACK_FRMT") + " " + currency, "", "", ""));
            	html.append("</tr>\n");
                
                if (!(pIdCardPack == null || "".equalsIgnoreCase(pIdCardPack)) &&
                		rset.getString("ID_JUR_PRS_CARD_PACK").equalsIgnoreCase(pIdCardPack)) {
                	
                	detail.append("<br>");
                    detail.append(getBottomFrameTable());
                	detail.append("</thead><tbody>");
                    
                    detail.append("<tr>");
                    detail.append(getBottomFrameTableTDAlign("PARAMETER_NAME", webposXML.getfieldTransl("TITLE_CARD_PACK_PARAM_NAME", false), "", "<font style=\"font-weight:bold;\">", "", "left"));
                    detail.append(getBottomFrameTableTDAlign("PARAMETER_VALUE", webposXML.getfieldTransl("TITLE_CARD_PACK_PARAM_VALUE", false), "", "<font style=\"font-weight:bold;\">", "", "right"));
                    detail.append("</tr>\n");
                    detail.append("<tr>");
                    detail.append(getBottomFrameTableTDAlign("NAME_CARD_STATUS", webposXML.getfieldTransl("CARD_STATUS", false), "", "", "", "left"));
                    detail.append(getBottomFrameTableTDAlign("NAME_CARD_STATUS", rset.getString("NAME_CARD_STATUS"), "", "<font style=\"color:green;\">", "", "right"));
                    detail.append("</tr>\n");
                    detail.append("<tr>");
                    detail.append(getBottomFrameTableTDAlign("NAME_JUR_PRS_CARD_PACK", webposXML.getfieldTransl("NAME_JUR_PRS_CARD_PACK", false), "", "", "", "left"));
                    detail.append(getBottomFrameTableTDAlign("NAME_JUR_PRS_CARD_PACK", rset.getString("NAME_JUR_PRS_CARD_PACK"), "", "<font style=\"color:blue;\">", "", "right"));
                    detail.append("</tr>\n");
                    if (!isEmpty(rset.getString("DESC_JUR_PRS_CARD_PACK"))) {
	                    detail.append("<tr>");
	                    detail.append(getBottomFrameTableTDAlign("DESC_JUR_PRS_CARD_PACK", webposXML.getfieldTransl("DESC_JUR_PRS_CARD_PACK", false), "", "", "", "left"));
	                    detail.append(getBottomFrameTableTDAlign("DESC_JUR_PRS_CARD_PACK", rset.getString("DESC_JUR_PRS_CARD_PACK"), "", "", "", "right"));
	                    detail.append("</tr>\n");
                    }
                    detail.append("<tr>");
                    detail.append(getBottomFrameTableTDAlign("TOTAL_AMOUNT_JP_CARD_PACK", webposXML.getfieldTransl("TOTAL_AMOUNT_CARD_SALE", false), "", "", "", "left"));
                    detail.append(getBottomFrameTableTDAlign("TOTAL_AMOUNT_JP_CARD_PACK", rset.getString("TOTAL_AMOUNT_JP_CARD_PACK_FRMT") + " " + currency, "", lFontAdditional, "", "right"));
                    detail.append("</tr>\n");
                    detail.append("<tr>");
                    detail.append(getBottomFrameTableTDAlign("ENTRANCE_FEE", webposXML.getfieldTransl("CHEQUE_ENTRANCE_FEE", false), "", "", "", "left"));
                    detail.append(getBottomFrameTableTDAlign("ENTRANCE_FEE", rset.getString("ENTRANCE_FEE_FRMT") + " " + currency, "", "", "", "right"));
                    detail.append("</tr>\n");
                    detail.append("<tr>");
                    detail.append(getBottomFrameTableTDAlign("SHARE_FEE", webposXML.getfieldTransl("CHEQUE_SHARE_FEE", false), "", "", "", "left"));
                    detail.append(getBottomFrameTableTDAlign("SHARE_FEE", rset.getString("SHARE_FEE_FRMT") + " " + currency, "", "", "", "right"));
                    detail.append("</tr>\n");
                    detail.append("<tr>");
                    detail.append(getBottomFrameTableTDAlign("MEMBERSHIP_FEE", webposXML.getfieldTransl("CHEQUE_MEMBERSHIP_FEE", false), "", "", "", "left"));
                    detail.append(getBottomFrameTableTDAlign("MEMBERSHIP_FEE", rset.getString("MEMBERSHIP_FEE_FRMT") + " " + currency + " (" + rset.getString("MEMBERSHIP_FEE_MONTH_COUNT") + " " + webposXML.getfieldTransl("CHEQUE_TARGET_PRG_PORIOD_MONTH", false) + ")", "", "", "", "right"));
                    detail.append("</tr>\n");
                    detail.append("<tr>");
                    detail.append(getBottomFrameTableTDAlign("DEALER_MARGIN", webposXML.getfieldTransl("CHEQUE_DEALER_MARGIN", false), "", "", "", "left"));
                    detail.append(getBottomFrameTableTDAlign("DEALER_MARGIN", rset.getString("DEALER_MARGIN_FRMT") + " " + currency, "", "", "", "right"));
                    detail.append("</tr>\n");
                    detail.append("<tr>");
                    detail.append(getBottomFrameTableTDAlign("AGENT_MARGIN", webposXML.getfieldTransl("CHEQUE_AGENT_MARGIN", false), "", "", "", "left"));
                    detail.append(getBottomFrameTableTDAlign("AGENT_MARGIN", rset.getString("AGENT_MARGIN_FRMT") + " " + currency, "", "", "", "right"));
                    detail.append("</tr>\n");
                    detail.append("</tbody></table>\n");
                }

            }
            html.append("</tbody></table>\n");
            
            html.append(detail.toString());
            
        } // try
        catch (SQLException e) {LOGGER.error(html, e);}
        catch (Exception el) {LOGGER.error(html, el);}
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
    } // getTermServCardHTML
    
    private String invoicesCount = "";
    
    public String getInvoicesCount() {
    	return invoicesCount;
    }
    
    public String getInvoicesHTML(String pFind, String pDateBeg, String pDateEnd, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	PreparedStatement st = null;
        Connection con = null; 
        String mySQL = "";
        String myBGColor = "";
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        try{
	       	 mySQL = 
        		" SELECT number_invoice, date_invoice_frmt, sname_jur_prs_receiver, " +
	   			"        sname_jur_prs_payer, total_sum_frmt, " +
	            "		 id_invoice, cd_fn_priority, cd_fn_invoice_state, row_count " +
	            " FROM (SELECT ROWNUM rn, number_invoice, date_invoice_frmt, sname_jur_prs_payer, " +
	            "			   sname_jur_prs_receiver, total_sum_frmt, " +
	            "			   id_invoice, cd_fn_priority, cd_fn_invoice_state, row_count " +
	            " 		  FROM (SELECT number_invoice, date_invoice_frmt, sname_jur_prs_payer, " +
	            "				       sname_jur_prs_receiver, total_sum_frmt||' '||sname_currency total_sum_frmt," +
	            "					   id_invoice, cd_fn_priority, cd_fn_invoice_state, " +
	    		"                      count(*) over () as row_count " +
	            "           	  FROM (SELECT number_invoice, date_invoice_frmt, sname_jur_prs_payer, " +
	            "						       sname_jur_prs_receiver, cd_fn_priority, cd_fn_invoice_state, " +
	            "							   sname_currency, total_sum_frmt, id_invoice " +
                "                         FROM " + getGeneralDBScheme() + ".vp$fn_invoice_all " +
	            "                        WHERE 1=1 ";
        	
        	if (!isEmpty(pDateBeg)) {
        		mySQL = mySQL + 
	            	"                  AND TRUNC(date_invoice) >= TO_DATE(?,'" + getDateFormat() + "')";
            	pParam.add(new bcFeautureParam("string", pDateBeg));
        	}
        	if (!isEmpty(pDateEnd)) {
	        	mySQL = mySQL + 
	        		"                  AND TRUNC(date_invoice) <= TO_DATE(?,'" + getDateFormat() + "')";
	        	pParam.add(new bcFeautureParam("string", pDateEnd));
        	}
        	
            if (!isEmpty(pFind)) {
            	mySQL = mySQL + " AND (UPPER(number_invoice) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(date_invoice_frmt) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(sname_jur_prs_payer) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(sname_jur_prs_receiver) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(total_sum_frmt) LIKE UPPER('%'||?||'%')) ";
            	pParam.add(new bcFeautureParam("string", pFind));
            	pParam.add(new bcFeautureParam("string", pFind));
            	pParam.add(new bcFeautureParam("string", pFind));
            	pParam.add(new bcFeautureParam("string", pFind));
            	pParam.add(new bcFeautureParam("string", pFind));
           	}
            
            mySQL = mySQL +
	            "        ORDER BY date_invoice DESC, number_invoice, id_invoice DESC)) " +
	            "        WHERE ROWNUM < ?) " +
	            " WHERE rn >= ?";
            
            pParam.add(new bcFeautureParam("int", p_end));
            pParam.add(new bcFeautureParam("int", p_beg));
        	
        	LOGGER.debug(prepareSQLToLog(mySQL,pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);	
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();

            html.append(getBottomFrameTable());
            html.append("<tr>");
            html.append(getBottomFrameTableTH(webposXML, "NUMBER_INVOICE"));
            html.append(getBottomFrameTableTH(webposXML, "DATE_INVOICE"));
            html.append(getBottomFrameTableTH(webposXML, "SNAME_JUR_PRS_RECEIVER"));
            html.append(getBottomFrameTableTH(webposXML, "SNAME_JUR_PRS_PAYER"));
            html.append(getBottomFrameTableTH(webposXML, "TOTAL_SUM"));
            html.append("<th>1</th>\n");
            html.append("<th>2</th>");
            html.append("<th>3</th>");
            html.append("</tr></thead><tbody>");
            
            String lStateImage = "";
            String lStateTitle = "";
            String lPriorityImage = "";
            String lPriorityTitle = "";
            int rowCount = 0;

            while (rset.next())
            {
            	rowCount ++;
            	invoicesCount = rset.getString("ROW_COUNT");
            	html.append("<tr>\n");
            	//contextParam = "context parameters: access type="+rset.getString("CONTEXT_ACCESS_TYPE")+
            	//			   ", id_user="+rset.getString("CONTEXT_ID_USER")+
            	//			   ", id_dealer="+rset.getString("CONTEXT_ID_DEALER");

            	lPriorityImage = "";
            	lPriorityTitle = "";
				if ("CRITICAL".equalsIgnoreCase(rset.getString("CD_FN_PRIORITY"))) {
					lPriorityImage = "images/oper/priority/critical.png";
					lPriorityTitle = webposXML.getfieldTransl("invoice_priority_critical", false);
				} else if ("HIGH".equalsIgnoreCase(rset.getString("CD_FN_PRIORITY"))) {
					lPriorityImage = "images/oper/priority/high.png";
					lPriorityTitle = webposXML.getfieldTransl("invoice_priority_high", false);
				} else if ("MEDIUM".equalsIgnoreCase(rset.getString("CD_FN_PRIORITY"))) {
					lPriorityImage = "images/oper/priority/medium.png";
					lPriorityTitle = webposXML.getfieldTransl("invoice_priority_medium", false);
				} else if ("LOW".equalsIgnoreCase(rset.getString("CD_FN_PRIORITY"))) {
					lPriorityImage = "images/oper/priority/low.png";
					lPriorityTitle = webposXML.getfieldTransl("invoice_priority_low", false);
				}
				
				lStateImage = "";
            	lStateTitle = "";
            	if ("ISNT_PAID".equalsIgnoreCase(rset.getString("CD_FN_INVOICE_STATE"))) {
            		lStateImage = "images/oper/isnt_paid.png";
            		lStateTitle = webposXML.getfieldTransl("invoice_state_isnt_paid", false);
				} else if ("PAID".equalsIgnoreCase(rset.getString("CD_FN_INVOICE_STATE"))) {
					lStateImage = "images/oper/paid.png";
					lStateTitle = webposXML.getfieldTransl("invoice_state_paid", false);
				} else if ("CANCELLED".equalsIgnoreCase(rset.getString("CD_FN_INVOICE_STATE"))) {
					lStateImage = "images/oper/cancel.png";
					lStateTitle = webposXML.getfieldTransl("invoice_state_cancelled", false);
				} else if ("PAID_PARTIALLY".equalsIgnoreCase(rset.getString("CD_FN_INVOICE_STATE"))) {
					lStateImage = "images/oper/paid_partially.png";
					lStateTitle = webposXML.getfieldTransl("invoice_state_paid_partially", false);
				} else if ("OVERPAYMENT".equalsIgnoreCase(rset.getString("CD_FN_INVOICE_STATE"))) {
					lStateImage = "images/oper/overpayment.png";
					lStateTitle = webposXML.getfieldTransl("invoice_state_overpayment", false);
				}

            	if ("CANCELLED".equalsIgnoreCase(rset.getString("CD_FN_INVOICE_STATE"))) {
            		myBGColor = "style=\"background-color:#eeeeee;\"";
            	} else {
            		myBGColor = "";
            	}
			   	String myInvoiceLink = "report/operation.jsp?tab=3&id_term=" + this.idTerminal + "&id_invoice="+rset.getString("ID_INVOICE") + "&action=invdetail";
			   	//String myInvoiceLink = "";
				for (int i=1; i<=5; i++) {
				   	html.append(getBottomFrameTableTDAlign(mtd.getColumnName(i), rset.getString(i), myInvoiceLink, "", myBGColor, "right"));
	            }

   				if (!isEmpty(lPriorityImage)) {
   					String imgName = "priority"+rset.getString("ID_INVOICE");
   					String imgLine = "<img name=\""+imgName+"\" width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"" + lPriorityImage + "\" title=\"" + webposXML.getfieldTransl("title_invoice_priority", false) + ": " + lPriorityTitle.toLowerCase() + "\">";
				   	html.append(getBottomFrameTableTDAlign("", imgLine, myInvoiceLink, "", myBGColor, "center"));
   				} else {
				   	html.append(getBottomFrameTableTDAlign("", "&nbsp;", myInvoiceLink, "", myBGColor, "center"));
   				}

   				if (!isEmpty(lStateImage)) {
   					String imgName = "state"+rset.getString("ID_INVOICE");
   					String imgLine = "<img name=\""+imgName+"\" width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"" + lStateImage + "\" title=\"" + webposXML.getfieldTransl("title_invoice_state", false) + ": " + lStateTitle.toLowerCase() + "\">";
				   	html.append(getBottomFrameTableTDAlign("", imgLine, myInvoiceLink, "", myBGColor, "center"));
   				} else {
				   	html.append(getBottomFrameTableTDAlign("", "&nbsp;", myInvoiceLink, "", myBGColor, "center"));
   				}
                //String myChequeLink = "../reports/Reporter?&REPORT_ID=131&REPORT_FORMAT=HTML&NEED_DATE_IN_REPORT=Y&ID_INVOICE="+rset.getString("ID_INVOICE");
                html.append("<td align=\"center\"><A HREF=\"../reports/Reporter?&REPORT_ID=131&REPORT_FORMAT=HTML&NEED_DATE_IN_REPORT=Y&ID_INVOICE="+rset.getString("ID_INVOICE") + "\" target=\"_invoice"+rset.getString("ID_INVOICE")+"\">\n");
        		html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/oper/report.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"   title=\"" + webposXML.getfieldTransl("title_print_cheque", false) + "\">\n");
        		html.append("</a></td>");
   				html.append("</tr>");
            }
            html.append("</tbody></table>\n");
            if (rowCount > 0) {
	            html.append("<br><span>\n");
	            html.append("<table class=\"transparent_table\"><tr><td>\n");
	            html.append("<font style=\"color:green; font-weight: bold;\">" + commonXML.getfieldTransl("title_reference_designation", false) + ":</font><br>\n");
	            html.append("</td></tr>\n");
	            html.append("<tr><td>\n");
	            html.append(getNoteInvoicePriority("1"));
	            html.append("</td><td>\n");
	            html.append(getNoteInvoiceState("2"));
	            html.append("</td></tr></table>\n");
	            html.append("</span>\n");
            }
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(e.toString());}
        catch (Exception el) {LOGGER.error(html, el); html.append(el.toString());}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        //System.out.println(html.toString());
        //LOGGER.debug(contextParam);
        return html.toString();
    } // getCardTransHTML
    
    public String getExternalEntranceFeesHTML(String pIdTermUser, String pPhoneMobile, String pFind, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	PreparedStatement st = null;
        Connection con = null; 
        String mySQL = "";
        String goToLink = "";
		String imgLine = "<img name=\"img_go_\" width=\"16\" vspace=\"0\" hspace=\"0\" height=\"16\" align=\"top\" style=\"border: 0px;\" src=\"images/oper/go1.png\" title=\"" + commonXML.getfieldTransl("choice", false) + "\">";
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        //String contextParam = "";
        
        readTransTypes();
        
        try{
	       	mySQL = 
        		" SELECT rn, cheque_entrance_fee_date, phone_mobile, pay_type, cheque_fee, id_telgr, row_count " +
	            "   FROM (SELECT ROWNUM rn, phone_mobile, pay_type, cheque_fee, cheque_entrance_fee_date, id_telgr, row_count " +
	            " 		    FROM (SELECT phone_mobile, pay_type, cheque_fee, cheque_entrance_fee_date, id_telgr, " +
	    		"                        count(*) over () as row_count " +
	            "             	    FROM (SELECT a.id_telgr, a.phone_mobile_nat_prs phone_mobile, " +
	            "                                a.name_trans_pay_type pay_type, " +
	            "                                a.opr_sum_frmt||' '||a.sname_currency cheque_fee, " +
	            "                                a.date_telgr_frmt cheque_entrance_fee_date" +
                "                           FROM " + getGeneralDBScheme() + ".vc_telgr_club_all a " +
	            "                          WHERE a.cd_telgr_type = 'ENTRANCE_FEE' " +
	            "                            AND a.cd_telgr_state = 'NEED_CONFIRM' ";
        	
        	if (!isEmpty(pPhoneMobile)) {
            	mySQL = mySQL +
	            	"        AND phone_mobile_nat_prs = PREPARE_PHONE_MOBILE(?) ";
            	pParam.add(new bcFeautureParam("string", pPhoneMobile));
           	}
            if (!isEmpty(pFind)) {
            	mySQL = mySQL + " AND (UPPER(name_trans_pay_type) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(opr_sum_frmt) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(date_telgr_frmt) LIKE UPPER('%'||?||'%')) ";
            	pParam.add(new bcFeautureParam("string", pFind));
            	pParam.add(new bcFeautureParam("string", pFind));
            	pParam.add(new bcFeautureParam("string", pFind));            	
           	}
            
            mySQL = mySQL +
	            "        ORDER BY date_telgr_frmt DESC)) " +
	            "        WHERE ROWNUM < ?) " +
	            " WHERE rn >= ?";
            
            pParam.add(new bcFeautureParam("int", p_end));
            pParam.add(new bcFeautureParam("int", p_beg));
        	
        	LOGGER.debug(prepareSQLToLog(mySQL,pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);	
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-2; i++) {
            	html.append(getBottomFrameTableTH(webposXML, mtd.getColumnName(i)));
            }
            html.append("<th>1</th>\n");
            html.append("</tr></thead><tbody>");

            while (rset.next())
            {
            	operationsCount = rset.getString("ROW_COUNT");
            	html.append("<tr>\n");
            	goToLink = "action/new_client_registration.jsp?tab=1&type=client&action=put_card_phone&process=yes&data_type=PAID&id_entrance_fee_telgr="+rset.getString("ID_TELGR")+"&id_user="+pIdTermUser;
				for (int i=1; i<=colCount-2; i++) {
					html.append(getBottomFrameTableTDWithDiv(mtd.getColumnName(i), rset.getString(i), goToLink, "", "", "div_action_big"));
				}
			   	html.append(getBottomFrameTableTDWithDiv("", imgLine, goToLink, "", "", "div_action_big"));
            	html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(e.toString());}
        catch (Exception el) {LOGGER.error(html, el); html.append(el.toString());}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        //System.out.println(html.toString());
        //LOGGER.debug(contextParam);
        return html.toString();
    } // getCardTransHTML
    

}
