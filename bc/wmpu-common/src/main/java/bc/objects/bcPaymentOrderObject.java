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

public class bcPaymentOrderObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcPaymentOrderObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idPaymentOrder;
	
	public bcPaymentOrderObject() {	}
	
	public bcPaymentOrderObject(String pIdPaymentOrder) {
		this.idPaymentOrder = pIdPaymentOrder;
		this.getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_PAYMENT_ORDER_CLUB_ALL WHERE id_payment_order = ?";
		fieldHm = getFeatures2(featureSelect, this.idPaymentOrder, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getClearingLinesHTML(String pIdOwner, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        
        boolean hasAccountPermission = false;
        boolean hasPartnerPermission = false;
        boolean hasClearingPermission = false;
        boolean hasClientPermission = false;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT rn, number_doc_clearing, receiver_mfo_bank, receiver_name_bank_alt, receiver_number_bank_account, " +
        	"		 receiver_sname_owner_ba, payer_mfo_bank, " +
        	" 		 payer_name_bank_alt, payer_number_bank_account, payer_sname_owner_ba, " +
        	"        name_currency, entered_amount, payment_function, need_clearing_line_export, state_clearing_line_export_tsl, " +
        	"		 reconcile_state, id_clearing_line, receiver_id_bank_account, receiver_id_bank, " +
        	"        payer_id_bank_account, payer_id_bank, receiver_type_owner_ba, payer_type_owner_ba, " +
        	"        receiver_id_owner_ba, payer_id_owner_ba " +
        	"   FROM (SELECT ROWNUM rn, number_doc_clearing, " +
        	"        		 receiver_sname_owner_ba, receiver_number_bank_account, " +
        	"		 		 receiver_sname_bank receiver_name_bank_alt, receiver_mfo_bank, payer_sname_owner_ba, " +
        	" 		 		 payer_number_bank_account, payer_sname_bank payer_name_bank_alt, " +
        	"        		 payer_mfo_bank, name_currency, entered_amount_frmt entered_amount, payment_function,  " +
        	"		 		 need_clearing_line_export, state_clearing_line_export_tsl, reconcile_state_tsl reconcile_state, " +
        	"		 		 id_clearing_line, receiver_id_bank_account, receiver_id_bank, " +
        	"        		 payer_id_bank_account, payer_id_bank, " +
        	"        		 receiver_type_owner_ba, payer_type_owner_ba, " +
        	"        		 receiver_id_owner_ba, payer_id_owner_ba " +
        	"   		FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".VC_CLEARING_LINES_CLUB_ALL " +
        	"                  WHERE id_payment_order = ? ";
        pParam.add(new bcFeautureParam("int", this.idPaymentOrder));
        
        if (!isEmpty(pIdOwner)) {
        	mySQL = mySQL + " AND (receiver_id_owner_ba = ? OR payer_id_owner_ba = ?) ";
            pParam.add(new bcFeautureParam("int", pIdOwner));
            pParam.add(new bcFeautureParam("int", pIdOwner));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL =	mySQL + "  ORDER BY a.number_line) WHERE ROWNUM < ?) WHERE rn >= ?";
        
        try {
        	if (isEditMenuPermited("CLIENTS_BANK_ACCOUNTS")>=0) {
        		hasAccountPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
        		hasClientPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_CLEARING")>=0) {
        		hasClearingPermission = true;
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
            html.append("<th rowspan=\"2\">"+ commonXML.getfieldTransl("RN", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("DATE_CLEARING", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("NUMBER_DOC_CLEARING", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ clearingXML.getfieldTransl("RECEIVER_PARTICIPANT", false)+"</th>\n");
            html.append("<th colspan=\"4\">"+ clearingXML.getfieldTransl("PAYER_PARTICIPANT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("NAME_CURRENCY", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("ENTERED_AMOUNT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("PAYMENT_FUNCTION", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("STATE_CLEARING_LINE_EXPORT", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clearingXML.getfieldTransl("RECONCILE_STATE", false)+"</th>\n");
            html.append("</tr>");
            html.append("<tr>");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_NAME_BANK_ALT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_NUMBER_BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("RECEIVER_SNAME_OWNER_BA", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_MFO_BANK", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_NAME_BANK_ALT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_NUMBER_BANK_ACCOUNT", false)+"</th>\n");
            html.append("<th>"+ clearingXML.getfieldTransl("PAYER_SNAME_OWNER_BA", false)+"</th>\n");
            html.append("</tr></thead><tbody>");
            html.append("<tr>");
            html.append("<td colspan=\"16\" align=\"center\">");
            html.append(getSubmitButtonAjax("../crm/finance/clearingupdate.jsp"));
            html.append("</td></tr>\n");
            while (rset.next())
            {
                html.append("<tr>");
                for (int i=1; i <= colCount-9; i++) {
                	if (i==1) {
               			html.append("<td align=\"center\">"+ getValue2(rset.getString(i)) +"</td>\n");
                	} else if ("NUMBER_DOC_CLEARING".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasClearingPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/clearing_linespecs.jsp?id="+ rset.getString("ID_CLEARING_LINE"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("RECEIVER_NAME_BANK_ALT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasPartnerPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("RECEIVER_ID_BANK"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("RECEIVER_NUMBER_BANK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasAccountPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/accountspecs.jsp?id="+rset.getString("RECEIVER_ID_BANK_ACCOUNT"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("RECEIVER_SNAME_OWNER_BA".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (!isEmpty(rset.getString("RECEIVER_TYPE_OWNER_BA"))) {
                			if ("JUR_PRS".equalsIgnoreCase(rset.getString("RECEIVER_TYPE_OWNER_BA")) && hasPartnerPermission) {
                    			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("RECEIVER_ID_OWNER_BA"), "", ""));
                			} else if ("NAT_PRS".equalsIgnoreCase(rset.getString("RECEIVER_TYPE_OWNER_BA")) && hasClientPermission) {
                    			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("RECEIVER_ID_OWNER_BA"), "", ""));
                			}
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("PAYER_NAME_BANK_ALT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasPartnerPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("PAYER_ID_BANK"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("PAYER_NUMBER_BANK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasAccountPermission) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/accountspecs.jsp?id="+rset.getString("PAYER_ID_BANK_ACCOUNT"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("PAYER_SNAME_OWNER_BA".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (!isEmpty(rset.getString("PAYER_TYPE_OWNER_BA"))) {
                			if ("JUR_PRS".equalsIgnoreCase(rset.getString("PAYER_TYPE_OWNER_BA")) && hasPartnerPermission) {
                    			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("PAYER_ID_OWNER_BA"), "", ""));
                			} else if ("NAT_PRS".equalsIgnoreCase(rset.getString("PAYER_TYPE_OWNER_BA")) && hasClientPermission) {
                    			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("PAYER_ID_OWNER_BA"), "", ""));
                			}
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
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
    } //getClearingAllHTML

}
