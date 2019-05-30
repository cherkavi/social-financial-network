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
import bc.lists.bcListTransaction;
import bc.service.bcFeautureParam;

public class bcCardPackageObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcCardPackageObject.class);
	
	private String idCardPackage;
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	public bcCardPackageObject(String pIdCardPackage){
		this.idCardPackage = pIdCardPackage;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vc_club_jp_card_pack_club_all WHERE id_jur_prs_card_pack = ?";
				
		bcFeautureParam[] array = new bcFeautureParam[1];
		array[0] = new bcFeautureParam("int", this.idCardPackage);
		
		fieldHm = getFeatures2(featureSelect, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getGivenCardsHTML(String pFindString, String pMemberStatus, String pRoleState, String p_beg, String p_end) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, fio_nat_prs_initial fio_nat_prs, cd_card1 cd_card, phone_mobile, date_card_sale_frmt, total_amount_card_sale_frmt,  " +
	  	  	"        name_club_member_status, name_nat_prs_role_state, id_nat_prs_role, id_nat_prs," +
	  	  	"        card_serial_number, card_id_issuer, card_id_payment_system, cd_nat_prs_role_state " +
	  	  	"   FROM (SELECT ROWNUM rn, cd_card1, fio_nat_prs_initial, phone_mobile, date_card_sale_frmt, total_amount_card_sale_frmt,  " +
	  	  	"                DECODE(cd_club_member_status, " +
       		"                       'CLOSED', '<font color=\"red\"><b>'||name_club_member_status||'</b></font>', " +
       		"                       'OUT_OF_CLUB', '<font color=\"gray\">'||name_club_member_status||'</font>', " +
       		"                       'IN_PROCESS', '<font color=\"blue\">'||name_club_member_status||'</font>', " +
       		"                       'PARTICIPATE', '<font color=\"green\">'||name_club_member_status||'</font>', " +
       		"                       name_club_member_status" +
       		"                ) name_club_member_status," +
       		"				 DECODE(cd_nat_prs_role_state, " +
  		  	"                   	'GIVEN', '<b><font color=\"green\">'||name_nat_prs_role_state||'</font></b>', " +
			"                      	'QUESTIONED', '<b><font color=\"blue\">'||name_nat_prs_role_state||'</font></b>', " +
			"                      	'ACTIVATED', '<b><font color=\"black\">'||name_nat_prs_role_state||'</font></b>', " +
			"                      	'WITHOUT_CARD', '<b><font color=\"gray\">'||name_nat_prs_role_state||'</font></b>', " +
			"                      	name_nat_prs_role_state" +
			"                ) name_nat_prs_role_state, id_nat_prs_role, id_nat_prs," +
	  	  	"                card_serial_number, card_id_issuer, card_id_payment_system, cd_nat_prs_role_state " +
	       	"           FROM (SELECT a.cd_card1, a.cd_club_member_status, a.name_club_member_status, a.fio_nat_prs_initial, a.phone_mobile, a.date_card_sale_frmt, " +
	       	"					     a.total_amount_card_sale_frmt||' '||c.sname_currency total_amount_card_sale_frmt,  " +
	  	  	"        			     a.name_nat_prs_role_state, a.id_nat_prs_role, a.id_nat_prs," +
	  	  	"                        a.card_serial_number, a.card_id_issuer, a.card_id_payment_system, a.cd_nat_prs_role_state " +
	       	"                   FROM " + getGeneralDBScheme() + ".vc_nat_prs_role_all a, " +
	       	"                		 " + getGeneralDBScheme() + ".vc_currency_names c "+
  	  		"       	       WHERE a.id_jur_prs_card_pack = ? " +
  	  		"                    AND a.cd_currency_card_sale = c.cd_currency ";
    	pParam.add(new bcFeautureParam("int", this.idCardPackage));
    	
        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (UPPER(cd_card1) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(fio_nat_prs_initial) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(phone_mobile) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(date_card_sale_frmt) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(total_amount_card_sale_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	if (!isEmpty(pMemberStatus)) {
    		mySQL = mySQL + " AND cd_club_member_status = ? ";
    		pParam.add(new bcFeautureParam("string", pMemberStatus));
    	}
    	if (!isEmpty(pRoleState)) {
    		mySQL = mySQL + " AND cd_nat_prs_role_state = ? ";
    		pParam.add(new bcFeautureParam("string", pRoleState));
    	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
      	  	"                 ORDER BY date_card_sale DESC)  " +
      	  	"        WHERE ROWNUM < ? " + 
      	  	" ) WHERE rn >= ?";
           	StringBuilder html = new StringBuilder();
           	Connection con = null;        
           	PreparedStatement st = null;
           	String myFont = "";

            boolean hasCardPermission = false;
           	boolean hasNatPrsPermission = false;
           
           	try{
           		if (isEditMenuPermited("CARDS_CLUBCARDS")>=0) {
           			hasCardPermission = true;
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
	              for (int i=1; i <= colCount-6; i++) {
	              	  html.append(getBottomFrameTableTH(natprsXML, mtd.getColumnName(i)));
	              }
	              html.append("</tr></thead><tbody>\n");
         
	              while (rset.next())  {
	            	  if ("EXCEPTED".equalsIgnoreCase(rset.getString("CD_NAT_PRS_ROLE_STATE"))) {
	            		  myFont = "<font color=\"red\">";
	            	  } else {
	            		  myFont = "";
	            	  }
	            	  html.append("<tr>");
	            	  for (int i=1; i <= colCount-6; i++) {
	            		  if (hasNatPrsPermission && "FIO_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i))) {
	            			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id=" + rset.getString("ID_NAT_PRS"), myFont, ""));
	            		  } else if (hasCardPermission && "CD_CARD".equalsIgnoreCase(mtd.getColumnName(i))) {
	            	  		  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardspecs.jsp?id="+rset.getString("CARD_SERIAL_NUMBER")+"&iss="+rset.getString("CARD_ID_ISSUER")+"&paysys="+rset.getString("CARD_ID_PAYMENT_SYSTEM"), "", ""));
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
    
    public String getTransactionsHTML(String pFindString, String pTypeTrans, String pPayType, String pStateTrans, String p_beg, String p_end) {
    	bcListTransaction list = new bcListTransaction();
    	
    	String pWhereCause = " WHERE id_jur_prs_card_pack = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idCardPackage));
    	
    	return list.getTransactionList(pWhereCause, pWhereValue, pFindString, pTypeTrans, pPayType, pStateTrans, p_beg, p_end);
    }

    /*public String getTransactionsOldHTML(String pFindString, String pTypeTrans, String pPayType, String pStateTrans, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasTransPermission = false;
        boolean hasTerminalPermission = false;
        boolean hasCardPermission = false;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = " SELECT rn, sys_date_frmt date_trans_frmt, " +
			"        name_service_place, id_term, cashier_name, " +
			"        nc_term, cd_card1, cheque_type,   " +
			"		 CASE WHEN NVL(opr_sum,0) > 0 " +
			"             THEN '<font color=\"blue\">'||opr_sum_frmt||'</font>'||' '||sname_currency " +
			"             ELSE '' " +
			"        END opr_sum_frmt, / " +
			"        id_trans, id_telgr, id_service_place, card_serial_number, id_issuer, id_payment_system " +
			"   FROM (SELECT ROWNUM rn, id_trans, " +
			"			   DECODE(cd_trans_pay_type, " +
	        "                       'CASH',  '<font style = \"color: green\"><b>'||name_trans_pay_type||'</b></font>', " +
	        "                       'BANK_CARD',  '<font style = \"color: blue\"><b>'||name_trans_pay_type||'</b></font>', " +
	        "                       'SMPU_CARD',  '<font style = \"color: black;\"><b>'||name_trans_pay_type||'</b></font>', " +
	        "                       'INVOICE',  '<font style = \"color: brown;\"><b>'||name_trans_pay_type||'</b></font>', " +
	        "                       'UNKNOWN',  '<font style = \"color: red;\"><b>'||name_trans_pay_type||'</b></font>', " +
	        "                       '<font style = \"color: red;\"><b>'||name_trans_pay_type||'</b></font>'" +
			"                ) cheque_type, " +
	        "                sys_date_frmt, " +
			"                name_dealer, name_service_place, id_term, cd_card1, " +
			"                DECODE(fcd_trans_type, " +
	        "                       'REC_PAYMENT', sname_currency, " +
	        "                       'REC_MOV_BON', '', " +
	        "                       'REC_CHK_CARD', '', " +
	        "                       'REC_INVAL_CARD', '', " +
	        "                       'REC_STORNO_BON', sname_currency, " +
	        "                       'REC_PAYMENT_IM', sname_currency, " +
	        "                       'REC_PAYMENT_EXT', sname_currency, " +
	    	"                       'REC_ACTIVATION', '', " +
	    	"                       'REC_QUESTIONING', '', " +
	    	"                       'REC_PUT_CARD', sname_currency, " +
	        "                       'REC_COUPON', '', " +
	    	"                       'REC_MEMBERSHIP_FEE', sname_currency, " +
	    	"                       'REC_MTF', sname_currency, " +
	        "                       'REC_POINT_FEE', '', " +
	    	"                       'REC_SHARE_FEE', sname_currency, " +
    		"                       'REC_TRANSFER_GET_POINT', '', " +
    		"                       'REC_TRANSFER_PUT_POINT', '', " +
    		"                       'REC_PAYMENT_INVOICE', sname_currency, " +
    		"                       'REC_SHARE_FEE_CHANGE', sname_currency, " +
    		"                       'REC_TRANSFORM_FROM_SHARE', sname_currency, " +
	    	//"                       'REC_CANCEL', sname_currency, " +
	    	//"                       'REC_RETURN', sname_currency, " +
	    	"                       sname_currency" +
	    	"                ) sname_currency, nc_term, nt_ext, action,  " +
			"		         opr_sum, opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
			"                sum_bon, sum_bon_frmt, sum_disc, sum_disc_frmt, " +
			"                DECODE(state_trans, " +
			"                       -99,  '<font color=\"gray\">'||state_trans_tsl||'</font>', " +
			"                       -55,  '<font color=\"red\">'||state_trans_tsl||'</font>', " +
			"                       -54,  '<font color=\"blue\">'||state_trans_tsl||'</font>', " +
			"                       -13,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
			"                       -12,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
			"                       -11,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
			"                       -9,  '<font color=\"gray\">'||state_trans_tsl||'</font>', " +
			"                       -7,  '<font color=\"blue\">'||state_trans_tsl||'</font>', " +
			"                       -6,  '<font color=\"blue\">'||state_trans_tsl||'</font>', " +
			"                       -4,  '<font color=\"green\">'||state_trans_tsl||'</font>', " +
			"                       -2,  '<font color=\"ligthred\">'||state_trans_tsl||'</font>', " +
			"                       -1,  '<font color=\"darkbrown\">'||state_trans_tsl||'</font>', " +
			"                       0,  '<font color=\"black\"><b>'||state_trans_tsl||'</b></font>', " +
			"                       1,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
			"                       5,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
			"                       9,  '<font color=\"red\"><b>'||state_trans_tsl||'</b></font>', " +
			"                       state_trans_tsl) state_trans_tsl, cashier_name, " +
			"                id_telgr, id_service_place, card_serial_number, id_issuer, id_payment_system " +
			"           FROM (SELECT * " +
			"                   FROM " + getGeneralDBScheme()+".vc_trans_club_all " +
			"                  WHERE id_jur_prs_card_pack = ? ";
        pParam.add(new bcFeautureParam("int", this.idCardPackage));
        if (!isEmpty(pTypeTrans)) {
        	mySQL = mySQL + " AND type_trans = ? ";
        	pParam.add(new bcFeautureParam("int", pTypeTrans));
        }
        if (!isEmpty(pPayType)) {
        	mySQL = mySQL + " AND pay_type = ? ";
        	pParam.add(new bcFeautureParam("string", pPayType));
        }
        if (!isEmpty(pStateTrans)) {
        	mySQL = mySQL + " AND state_trans = ? ";
        	pParam.add(new bcFeautureParam("int", pStateTrans));
        }
        if (!isEmpty(pFindString)) {
        	mySQL = mySQL + " AND (UPPER(id_trans) LIKE UPPER('%'||?||'%') OR " +
           		"UPPER(TO_CHAR(id_term)) LIKE UPPER('%'||?||'%') OR " +
           		"UPPER(sys_date_full_frmt) LIKE UPPER('%'||?||'%') OR " +
       			"UPPER(card_serial_number) LIKE UPPER('%'||?||'%')) ";
           	for (int i=0; i<4; i++) {
           	    pParam.add(new bcFeautureParam("string", pFindString));
           	}
       	}
        mySQL = mySQL + 
			"				   ORDER BY sys_date desc, card_serial_number, nt_icc) WHERE ROWNUM < ? " + 
			"        ) WHERE rn >= ?";
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        try{
        	if (isEditMenuPermited("CARDS_TRANSACTIONS")>=0) {
            	hasTransPermission = true;
            }
        	if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
            	hasTerminalPermission = true;
            }
        	if (isEditMenuPermited("CARDS_CLUBCARDS")>=0) {
        		hasCardPermission = true;
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
                html.append(getBottomFrameTableTH(transactionXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-6; i++) {
          	  		if (hasTransPermission && "NC_TERM".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/transactionspecs.jsp?id="+rset.getString("ID_TRANS"), "", ""));
         	  		} else if ("NAME_SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i))) {
         	  			if (this.getValue("ID_JUR_PRS").equalsIgnoreCase(rset.getString("ID_SERVICE_PLACE"))) {
         	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
         	  			} else {
              	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_SERVICE_PLACE"), "", ""));
         	  			}
         	  		} else if (hasTerminalPermission && "ID_TERM".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString("ID_TERM"), "", ""));
         	  		} else if (hasCardPermission && "CD_CARD1".equalsIgnoreCase(mtd.getColumnName(i))) {
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
    }*/
}
