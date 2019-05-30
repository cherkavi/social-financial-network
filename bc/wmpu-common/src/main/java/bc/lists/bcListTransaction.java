package bc.lists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.service.bcFeautureParam;

public class bcListTransaction extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcListTransaction.class);
	
	public bcListTransaction() {
		
	}
	
	public String getAllTransactionList(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pFindString, String pTypeTrans, String pPayType, String pStateTrans, String p_beg, String p_end){
		StringBuilder html = new StringBuilder();
    	PreparedStatement st = null;
        Connection con = null;
        String mySQL = "";
        
        boolean hasTerminalPermission = false;
        boolean hasDealerPermission = false;
        boolean hasTransPermission = false;
        //boolean hasTelegramPermission = false;
        
        String myBGround = "";
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        try{
        	if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
        		hasTerminalPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasDealerPermission = true;
        	}
        	if (isEditMenuPermited("CARDS_TRANSACTIONS")>=0) {
        		hasTransPermission = true;
        	}
        	//if (isEditMenuPermited("CLIENTS_TERMSES")>=0) {
        	//	hasTelegramPermission = true;
        	//}
        	mySQL = 
        		" SELECT rn, id_trans, " +
    			"        name_service_place, id_term, cashier_name, " +
    			"        type_trans_txt, sys_date_frmt date_trans_frmt, " +
    			"        cd_card1, nc_term, /*nt_ext, action,*/  " +
    			"		 CASE WHEN NVL(opr_sum,0) > 0 " +
    			"             THEN '<font color=\"blue\">'||opr_sum_frmt||'</font>'||DECODE(error_type,'N',' '||sname_currency,'') " +
    			"             ELSE '' " +
    			"        END opr_sum_frmt, /*sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt,*/ " +
    			"        CASE WHEN NVL(sum_bon,0) > 0 " +
    			"             THEN '<font color=\"blue\">'||sum_bon_frmt||'</font>' " +
    			"             ELSE '' " +
    			"        END sum_bon_frmt, " +
    			"        /*CASE WHEN NVL(sum_disc,0) > 0 " +
    			"             THEN '<font color=\"blue\">'||sum_disc_frmt||'</font>'||' '||sname_currency " +
    			"             ELSE sum_disc_frmt " +
    			"        END sum_disc_frmt,*/ state_trans_tsl, " +
    			"        id_telgr, id_service_place, state_trans, card_serial_number, id_issuer, id_payment_system, error_type " +
    			"   FROM (SELECT ROWNUM rn, id_trans, " +
    			"			   DECODE(fcd_trans_type, " +
    	        "                       'REC_PAYMENT',  '<font style = \"color: black\"><b>'||type_trans_txt||'</b></font>', " +
    	        "                       'REC_MOV_BON',  '<font style = \"color: blue\"><b>'||type_trans_txt||'</b></font>', " +
    	        "                       'REC_CHK_CARD',  '<font style = \"color: green\"><b>'||type_trans_txt||'</b></font>', " +
    	        "                       'REC_INVAL_CARD',  '<font style = \"color: red\"><b>'||type_trans_txt||'</b></font>', " +
    	        "                       'REC_STORNO_BON',  '<font style = \"color: red\"><b>'||type_trans_txt||'</b></font>', " +
    	        "                       'REC_PAYMENT_IM',  '<font style = \"color: black\"><b>'||type_trans_txt||'</b></font>', " +
    	        "                       'REC_PAYMENT_EXT',  '<font style = \"color: black\"><b>'||type_trans_txt||'</b></font>', " +
    			"                       'REC_ACTIVATION',  '<font style = \"color: blue\"><b>'||type_trans_txt||'</b></font>', " +
    			"                       'REC_QUESTIONING',  '<font style = \"color: blue\"><b>'||type_trans_txt||'</b></font>', " +
    			"                       'REC_PUT_CARD',  '<font style = \"color: #191970\"><b>'||type_trans_txt||'</b></font>', " +
    	        "                       'REC_COUPON',  '<font style = \"color: #8B8989\"><b>'||type_trans_txt||'</b></font>', " +
    			"                       'REC_MEMBERSHIP_FEE',  '<font style = \"color: #8B3A3A\"><b>'||type_trans_txt||'</b></font>', " +
    			"                       'REC_POINT_FEE',  '<font style = \"color: #C8860B\"><b>'||type_trans_txt||'</b></font>', " +
        		"                       'REC_MTF',  '<font style = \"color: #B8860B\"><b>'||type_trans_txt||'</b></font>', " +
    			"                       'REC_SHARE_FEE',  '<font style = \"color: green\"><b>'||type_trans_txt||'</b></font>', " +
    			"                       'REC_TRANSFER_GET_POINT',  '<font style = \"color: #BA55D3\"><b>'||type_trans_txt||'</b></font>', " +
    			"                       'REC_TRANSFER_PUT_POINT',  '<font style = \"color: #BA55D3\"><b>'||type_trans_txt||'</b></font>', " +
    			//"                       'REC_CANCEL',  '<font style = \"color: red\"><b>'||type_trans_txt||'</b></font>', " +
    			//"                       'REC_RETURN',  '<font style = \"color: red\"><b>'||type_trans_txt||'</b></font>', " +
    			"                       type_trans_txt" +
    			"                ) type_trans_txt, " +
    	        "                sys_date_frmt, " +
    			"                name_service_place, id_term, cd_card1, " +
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
    			"                id_telgr, id_service_place, state_trans, card_serial_number, id_issuer, id_payment_system, error_type " +
	            " 		  FROM (SELECT * " +
	            "           	  FROM " + getGeneralDBScheme() + ".vc_trans_ok_error_all " +
	            (isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause) ;
        	if (pWhereValue.size() > 0) {
        		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
        			pParam.add(pWhereValue.get(counter));
        		}
        	}
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
            pParam.add(new bcFeautureParam("int", p_end));
            pParam.add(new bcFeautureParam("int", p_beg));
            mySQL = mySQL +
	            "                ORDER BY nt_icc DESC, NVL(nt_icc_part2,0) DESC, id_trans DESC) " +
	            "        WHERE ROWNUM < ?) " +
	            " WHERE rn >= ?";
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-7; i++) {
            	html.append(getBottomFrameTableTH(transactionXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");

            while (rset.next())
            {
            	if ("9".equalsIgnoreCase(rset.getString("STATE_TRANS"))) {
            		myBGround = selectedBackGroundStyle;
            	} else if ("5".equalsIgnoreCase(rset.getString("STATE_TRANS"))) {
            		myBGround = selectedBackGroundStyle;
            	} else if ("1".equalsIgnoreCase(rset.getString("STATE_TRANS"))) {
            		myBGround = selectedBackGroundStyle;
            	} else if ("0".equalsIgnoreCase(rset.getString("STATE_TRANS"))) {
            		myBGround = noneBackGroundStyle;
            	} else if ("-1".equalsIgnoreCase(rset.getString("STATE_TRANS"))) {
            		myBGround = noneBackGroundStyle;
            	} else if ("-2".equalsIgnoreCase(rset.getString("STATE_TRANS"))) {
            		myBGround = noneBackGroundStyle;
            	} else if ("-4".equalsIgnoreCase(rset.getString("STATE_TRANS"))) {
            		myBGround = selectedBackGroundStyle;
            	} else if ("-6".equalsIgnoreCase(rset.getString("STATE_TRANS"))) {
            		myBGround = noneBackGroundStyle;
            	} else if ("-7".equalsIgnoreCase(rset.getString("STATE_TRANS"))) {
            		myBGround = noneBackGroundStyle;
            	} else if ("-9".equalsIgnoreCase(rset.getString("STATE_TRANS"))) {
            		myBGround = selectedBackGroundStyle;
            	} else {
            		myBGround = noneBackGroundStyle;
            	}

            	html.append("<tr>\n");
                for (int i=1; i<=colCount-7; i++) {
                	String myData = "";
                	String myHyperLink = "";
                	if ("ID_TERM".equalsIgnoreCase(mtd.getColumnName(i)) && 
                    		hasTerminalPermission && 
                			(!isEmpty(rset.getString(i)))) {
                    	myData = rset.getString(i);
                    	myHyperLink = "../crm/clients/terminalspecs.jsp?id="+rset.getString(i);
                    } else if ("NAME_SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i)) && 
                    		hasDealerPermission && 
                			(!isEmpty(rset.getString("ID_SERVICE_PLACE")))) {
                    	myData = rset.getString(i);
                    	myHyperLink = "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_SERVICE_PLACE");
                    //} else if ("ID_TELGR".equalsIgnoreCase(mtd.getColumnName(i)) && 
                    //		hasTelegramPermission && 
                	//		(!isEmpty(rset.getString("ID_TELGR")))) {
                    //	myData = rset.getString(i);
                    //	myHyperLink = "../crm/clients/telegramspecs.jsp?id="+rset.getString("ID_TELGR");
                    } else if ("ID_TRANS".equalsIgnoreCase(mtd.getColumnName(i))) {
                    	if ("N".equalsIgnoreCase(rset.getString("ERROR_TYPE"))) {
                    		if (hasTransPermission && 
                        			(!isEmpty(rset.getString(i)))) {
                    			myData = rset.getString(i);
                    			myHyperLink = "../crm/cards/transactionspecs.jsp?id="+rset.getString("ID_TRANS");
                    		} else {
                    			myData = rset.getString(i);
                    			myHyperLink = "";
                    		}
                    	} else {
                    		if (hasTransPermission && 
                        			(!isEmpty(rset.getString(i)))) {
                    			myData = rset.getString(i);
                    			myHyperLink = "../crm/cards/trans_correctionspecs.jsp?id="+rset.getString("ID_TRANS");
                    		} else {
                    			myData = rset.getString(i);
                    			myHyperLink = "";
                    		}
                    	}
                    } else {
                    	myData = rset.getString(i);
                    	myHyperLink = "";
                	}
        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), myData, myHyperLink, "", myBGround));
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
	
	public String getTransactionList(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pFindString, String pTypeTrans, String pPayType, String pStateTrans, String p_beg, String p_end){
		StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        boolean hasTransPermission = false;
        boolean hasTerminalPermission = false;
        boolean hasCardPermission = false;
        boolean hasDealerPermission = false;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = " SELECT rn, id_trans, " +
			"        name_service_place, id_term, cashier_name, nc_term, " +
			"        type_trans_txt, sys_date_frmt date_trans_frmt, " +
			"        cd_card1, /*nt_ext, action,*/  " +
			"		 CASE WHEN NVL(opr_sum,0) > 0 " +
			"             THEN '<font color=\"blue\">'||opr_sum_frmt||'</font>'||' '||sname_currency " +
			"             ELSE '' " +
			"        END opr_sum_frmt, /*sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt,*/ " +
			"        CASE WHEN NVL(sum_bon,0) > 0 " +
			"             THEN '<font color=\"blue\">'||sum_bon_frmt||'</font>' " +
			"             ELSE '' " +
			"        END sum_bon_frmt, " +
			"        /*CASE WHEN NVL(sum_disc,0) > 0 " +
			"             THEN '<font color=\"blue\">'||sum_disc_frmt||'</font>'||' '||sname_currency " +
			"             ELSE sum_disc_frmt " +
			"        END sum_disc_frmt,*/ state_trans_tsl, " +
			"        id_telgr, id_service_place, card_serial_number, id_issuer, id_payment_system " +
			"   FROM (SELECT ROWNUM rn, id_trans, " +
			"			   DECODE(fcd_trans_type, " +
	        "                       'REC_PAYMENT',  '<font style = \"color: black\"><b>'||name_trans_type_full||'</b></font>', " +
	        "                       'REC_MOV_BON',  '<font style = \"color: blue\"><b>'||name_trans_type_full||'</b></font>', " +
	        "                       'REC_CHK_CARD',  '<font style = \"color: green\"><b>'||name_trans_type_full||'</b></font>', " +
	        "                       'REC_INVAL_CARD',  '<font style = \"color: red\"><b>'||name_trans_type_full||'</b></font>', " +
	        "                       'REC_STORNO_BON',  '<font style = \"color: red\"><b>'||name_trans_type_full||'</b></font>', " +
	        "                       'REC_PAYMENT_IM',  '<font style = \"color: black\"><b>'||name_trans_type_full||'</b></font>', " +
	        "                       'REC_PAYMENT_EXT',  '<font style = \"color: black\"><b>'||name_trans_type_full||'</b></font>', " +
			"                       'REC_ACTIVATION',  '<font style = \"color: blue\"><b>'||name_trans_type_full||'</b></font>', " +
			"                       'REC_QUESTIONING',  '<font style = \"color: blue\"><b>'||name_trans_type_full||'</b></font>', " +
			"                       'REC_PUT_CARD',  '<font style = \"color: #191970\"><b>'||name_trans_type_full||'</b></font>', " +
	        "                       'REC_COUPON',  '<font style = \"color: #8B8989\"><b>'||name_trans_type_full||'</b></font>', " +
			"                       'REC_MEMBERSHIP_FEE',  '<font style = \"color: #8B3A3A\"><b>'||name_trans_type_full||'</b></font>', " +
			"                       'REC_POINT_FEE',  '<font style = \"color: #C8860B\"><b>'||name_trans_type_full||'</b></font>', " +
    		"                       'REC_MTF',  '<font style = \"color: #B8860B\"><b>'||name_trans_type_full||'</b></font>', " +
			"                       'REC_SHARE_FEE',  '<font style = \"color: green\"><b>'||name_trans_type_full||'</b></font>', " +
			"                       'REC_TRANSFER_GET_POINT',  '<font style = \"color: #BA55D3\"><b>'||name_trans_type_full||'</b></font>', " +
			"                       'REC_TRANSFER_PUT_POINT',  '<font style = \"color: #BA55D3\"><b>'||name_trans_type_full||'</b></font>', " +
			//"                       'REC_CANCEL',  '<font style = \"color: red\"><b>'||name_trans_type_full||'</b></font>', " +
			//"                       'REC_RETURN',  '<font style = \"color: red\"><b>'||name_trans_type_full||'</b></font>', " +
			"                       name_trans_type_full" +
			"                ) type_trans_txt, " +
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
			"           FROM (SELECT a.* " +
			"                   FROM " + getGeneralDBScheme()+".vc_trans_club_all a " +
	            (isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause) ;
        	if (pWhereValue.size() > 0) {
        		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
        			pParam.add(pWhereValue.get(counter));
        		}
        	}
        	if (!isEmpty(pTypeTrans)) {
            	mySQL = mySQL + " AND a.type_trans = ? ";
            	pParam.add(new bcFeautureParam("int", pTypeTrans));
            }
            if (!isEmpty(pPayType)) {
            	mySQL = mySQL + " AND a.pay_type = ? ";
            	pParam.add(new bcFeautureParam("string", pPayType));
            }
            if (!isEmpty(pStateTrans)) {
            	mySQL = mySQL + " AND a.state_trans = ? ";
            	pParam.add(new bcFeautureParam("int", pStateTrans));
            }
            if (!isEmpty(pFindString)) {
            	mySQL = mySQL + 
            		" AND (UPPER(a.id_trans) LIKE UPPER('%'||?||'%') OR " +
            		"      UPPER(a.name_service_place) LIKE UPPER('%'||?||'%') OR " +
       				"      UPPER(a.TO_CHAR(id_term)) LIKE UPPER('%'||?||'%') OR " +
       				"      UPPER(a.cashier_name) LIKE UPPER('%'||?||'%') OR " +
           			"      UPPER(a.sys_date_frmt) LIKE UPPER('%'||?||'%') OR " +
           			"      UPPER(a.cd_card1) LIKE UPPER('%'||?||'%') OR " +
           			"      UPPER(a.nc_term) LIKE UPPER('%'||?||'%') OR " +
           			"      UPPER(a.opr_sum_frmt) LIKE UPPER('%'||?||'%')) ";
               	for (int i=0; i<8; i++) {
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
            	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
            		hasDealerPermission = true;
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
                for (int i=1; i <= colCount-5; i++) {
                    html.append(getBottomFrameTableTH(transactionXML, mtd.getColumnName(i)));
                }
                html.append("</tr></thead><tbody>\n");
                while (rset.next())
                {
                	html.append("<tr>");
              	  	for (int i=1; i <= colCount-5; i++) {
              	  		if (hasTransPermission && "ID_TRANS".equalsIgnoreCase(mtd.getColumnName(i))) {
              	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/transactionspecs.jsp?id="+rset.getString("ID_TRANS"), "", ""));
             	  		} else if (hasDealerPermission && "NAME_SERVICE_PLACE".equalsIgnoreCase(mtd.getColumnName(i))) {
             	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_SERVICE_PLACE"), "", ""));
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
	}
	
    
}
