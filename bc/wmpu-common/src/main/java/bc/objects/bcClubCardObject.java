package bc.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.lists.bcListCardOperation;
import bc.lists.bcListTransaction;
import bc.service.bcFeautureParam;

public class bcClubCardObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcClubCardObject.class);
	private String cardSerialNumber;
	private String idIssuer;
	private String idPaymentSystem;
	private String cdCard1;
	private boolean needCalcTasks;
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	public bcClubCardObject(String pCdCard1){
		this.cdCard1 = pCdCard1;
		this.needCalcTasks = false;
		getFeature();
	}
	
	public bcClubCardObject(String pCardSerialNumber, String pIdIssuer, String pIdPaymentSystem){
		this.cardSerialNumber = pCardSerialNumber;
		this.idIssuer = pIdIssuer;
		this.idPaymentSystem = pIdPaymentSystem;
		this.needCalcTasks = false;
		getFeature();
	}
	
	public bcClubCardObject(String pCardSerialNumber, String pIdIssuer, String pIdPaymentSystem, boolean pNeedCalcTasks){
		this.cardSerialNumber = pCardSerialNumber;
		this.idIssuer = pIdIssuer;
		this.idPaymentSystem = pIdPaymentSystem;
		this.needCalcTasks = pNeedCalcTasks;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = "";
		if (!isEmpty(this.cdCard1)) {
			featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CARD_CLUB_ALL WHERE cd_card1 = ?";
			
			bcFeautureParam[] array = new bcFeautureParam[1];
			array[0] = new bcFeautureParam("string", this.cdCard1);
			
			fieldHm = getFeatures2(featureSelect, array);
			
			this.cardSerialNumber = this.getValue("CARD_SERIAL_NUMBER");
			this.idIssuer = this.getValue("ID_ISSUER");
			this.idPaymentSystem = this.getValue("ID_PAYMENT_SYSTEM");
			
		} else {
			if (!(this.needCalcTasks)) {
				featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CARD_CLUB_ALL WHERE card_serial_number = ? AND id_issuer = ? AND id_payment_system = ?";
				
				bcFeautureParam[] array = new bcFeautureParam[3];
				array[0] = new bcFeautureParam("string", this.cardSerialNumber);
				array[1] = new bcFeautureParam("int", this.idIssuer);
				array[2] = new bcFeautureParam("int", this.idPaymentSystem);
				
				fieldHm = getFeatures2(featureSelect, array);
			} else {
				featureSelect = 
					"SELECT c.*, TO_CHAR(c.bal_feauture/100,'fm9999999999990d099') bal_feauture_frmt, " +
					" c.bal_acc + c.bal_cur - c.bal_feauture bal_calc, " +
					" TO_CHAR((c.bal_acc + c.bal_cur - c.bal_feauture)/100,'fm9999999999990d099') bal_calc_frmt " +
					" FROM (" + 
					" SELECT a.*, " + getGeneralDBScheme() + ".fnc_calc_bon_card_task_rests(a.card_serial_number, a.id_issuer, a.id_payment_system) bal_feauture " + 
					" FROM " + getGeneralDBScheme() + ".VC_CARD_CLUB_ALL a " +
					" WHERE a.card_serial_number = ?" +
			        "   AND a.id_issuer = ?" +
			        "   AND a.id_payment_system = ?) c";
				
				bcFeautureParam[] array = new bcFeautureParam[3];
				array[0] = new bcFeautureParam("string", this.cardSerialNumber);
				array[1] = new bcFeautureParam("int", this.idIssuer);
				array[2] = new bcFeautureParam("int", this.idPaymentSystem);
				
				fieldHm = getFeatures2(featureSelect, array);
			}
		}
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getCardTrans2HTML(String pFindString, String pTypeTrans, String pPayType, String pStateTrans, String p_beg, String p_end) {
    	bcListTransaction list = new bcListTransaction();
    	
    	String pWhereCause = 
    		"                WHERE card_serial_number = ? " +
    		"                  AND id_issuer = ? " + 
    		"                  AND id_payment_system = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("string", this.cardSerialNumber));
    	pWhereValue.add(new bcFeautureParam("int", this.idIssuer));
    	pWhereValue.add(new bcFeautureParam("int", this.idPaymentSystem));
    	
    	return list.getTransactionList(pWhereCause, pWhereValue, pFindString, pTypeTrans, pPayType, pStateTrans, p_beg, p_end);
    }
	
    public String getCardTransHTML(String pFindString, String pTypeTrans, String pPayType, String pStateTrans, String p_beg, String p_end) {
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
	            "                WHERE card_serial_number = ? " +
	            "                  AND id_issuer = ? " + 
	            "                  AND id_payment_system = ? " ;
        	pParam.add(new bcFeautureParam("string", this.cardSerialNumber));
        	pParam.add(new bcFeautureParam("int", this.idIssuer));
        	pParam.add(new bcFeautureParam("int", this.idPaymentSystem));
        	
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
    } // getCardTransHTML

    public String getCardRequestsHTML(String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        
        Statement st_action = null;
        ArrayList<String> array_value=new ArrayList<String>();
        ArrayList<String> array_code=new ArrayList<String>();

        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String mySQL = 
        	" SELECT rn, id_term, id_term_ses, id_telgr, cd_telgr_type, date_telgr,  "+
        	"        name_telgr_state, way_telgr_tsl, action, club_st, nt_icc, "+
        	"        club_disc_frmt, club_bon_frmt, bal_acc_frmt, bal_cur_frmt, bal_bon_per_frmt, "+
        	"        bal_disc_per_frmt, date_acc_frmt, date_mov_frmt, date_calc_frmt, date_onl_frmt " +
        	"   FROM (SELECT ROWNUM rn, id_term_ses, id_term, id_telgr, cd_telgr_type, date_telgr_frmt date_telgr,  "+
        	"                DECODE(cd_telgr_state, " +
        	"                       'EXECUTED', '<font color=\"green\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       'IN_PROCESS', '<font color=\"blue\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       'ERROR', '<font color=\"red\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       'UNKNOWN', '<font color=\"red\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       'WARNING', '<font color=\"red\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       'FATAL_ERROR', '<font color=\"red\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       'RETURNED', '<font color=\"brown\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       'RETURNED_PARTIALLY', '<font color=\"brown\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       'CANCEL', '<font color=\"brown\"><b>'||name_telgr_state||'</b></font>', " +
    		"                       '<font color=\"red\"><b>'||name_telgr_state||'</b></font>' " +
    		"                ) name_telgr_state, " +
        	"                DECODE(way_telgr, " +
    		"                       'RECEIVED', '<font color=\"blue\"><b>" + telegramXML.getfieldTransl("way_telgr_received", false)+ "</b></font>', " +
    		"                       'SENT', '<font color=\"green\"><b>" + telegramXML.getfieldTransl("way_telgr_sent", false)+ "</b></font>', " +
    		"                       ''" +
    		"                ) way_telgr_tsl, " +
        	"				 action, club_st, c_nt_icc nt_icc, "+
        	"     		     club_disc_percent club_disc_frmt, club_bon_percent club_bon_frmt, " +
        	"   		     bal_acc_frmt, bal_cur_frmt, bal_bon_per_frmt, "+
        	"   		     bal_disc_per_frmt, date_acc_frmt, " +
        	"   		     date_mov_frmt, date_calc_frmt, " +
        	"   		     date_onl_frmt " +
        	" 		    FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_card_requests_all "+
        	" 		           WHERE card_serial_number = ? "+
        	"   	             AND id_issuer = ? " +
        	"                    AND id_payment_system = ? ";
    	pParam.add(new bcFeautureParam("string", this.cardSerialNumber));
    	pParam.add(new bcFeautureParam("int", this.idIssuer));
    	pParam.add(new bcFeautureParam("int", this.idPaymentSystem));
    	
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    				" AND (TO_CHAR(id_term) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(id_term_ses) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(id_telgr) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(cd_telgr_type) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(date_telgr) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        	"                  ORDER BY id_term_ses DESC, id_telgr) " +
        	"          WHERE ROWNUM < ?) " +
        	"  WHERE rn >= ?";
        
        boolean hasTermPermission = false;
        boolean hasTermSessionPermission = false;
        try{
        	if (isEditMenuPermited("CLIENTS_TERMINALS")>=0) {
        		hasTermPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_TERMSES")>=0) {
        		hasTermSessionPermission = true;
        	}
        	con = Connector.getConnection(getSessionId());
            
        	st_action = con.createStatement();
        	ResultSet rset_action = st_action.executeQuery("SELECT number_value, meaning FROM " + getGeneralDBScheme() + ".vc_lookup_values WHERE UPPER(name_lookup_type)=UPPER('APP_CARD_RESP_ACTION') ORDER BY number_value");
        	
        	while (rset_action.next()) {
        		array_code.add(rset_action.getString("NUMBER_VALUE"));
                array_value.add(rset_action.getString("MEANING"));
        	}
        	st_action.close();
        	
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
	     catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
        finally {
            try {
                if (st_action!=null) st_action.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        
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
                if (i<=8) {
                	html.append(getBottomFrameTableTH(telegramXML, mtd.getColumnName(i)));
                } else {
                	html.append(getBottomFrameTableTH(transactionXML, mtd.getColumnName(i)));
                }
            }
            html.append("</tr></thead><tbody>");

            String myBGColor = "";
            String myFont = "";
            while (rset.next())
            {
                html.append("<tr>\n");
                myBGColor = selectedBackGroundStyle;
                if ("6".equalsIgnoreCase(rset.getString("ACTION")) ||
           			"7".equalsIgnoreCase(rset.getString("ACTION")) ||
           			"15".equalsIgnoreCase(rset.getString("ACTION"))) {
                	myBGColor = selectedBackGroundStyle;
                	myFont = "<b><font color=red>";
           		} else if ("5".equalsIgnoreCase(rset.getString("ACTION"))) {
           			myBGColor = selectedBackGroundStyle;
           			myFont = "";
           		} else {
           			myBGColor = noneBackGroundStyle;
           			myFont = "";
           		}

                for (int i=1; i<=colCount; i++) {
                	if ("ID_TERM".equalsIgnoreCase(mtd.getColumnName(i)) && hasTermPermission) {
                  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/terminalspecs.jsp?id="+rset.getString(i), myFont, myBGColor));
                    } else if ("ID_TERM_SES".equalsIgnoreCase(mtd.getColumnName(i)) && hasTermSessionPermission) {
                  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/termsespecs.jsp?id="+rset.getString(i), myFont, myBGColor));
                    } else if ("ID_TELGR".equalsIgnoreCase(mtd.getColumnName(i)) && hasTermSessionPermission) {
                  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/telegramspecs.jsp?id="+rset.getString(i), myFont, myBGColor));
                    } else if ("ACTION".equalsIgnoreCase(mtd.getColumnName(i))) {
                    	String lAction = rset.getString(i);
                    	for(int counter=0;counter<array_code.size();counter++){
                        	if(array_code.get(counter).equalsIgnoreCase(rset.getString("ACTION"))){
                        		lAction = lAction + "&nbsp;[<span title=\""+ array_value.get(counter) + "\" style=\"cursor: pointer; text-align: center; color:blue; font-weight:bold;\">?</span>]";
                        	}
                        }
                  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), getValue3(lAction), "", myFont, myBGColor));
                    } else {
                  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBGColor));
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
    } // getCardRequestsHTML

    public String getClubCardsHistoryHTML(String pFind, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT date_beg_frmt history_date_beg, date_end_frmt history_date_end, " +
        	"        name_currency, id_club, " +
        	"        id_nat_prs, date_open, date_close, expiry_date, ver_key, name_card_status, " +
        	"        name_card_state, name_card_type, name_bon_category, name_disc_category, " +
        	"        club_bon, club_disc " +
        	"   FROM (SELECT ROWNUM rn, date_beg_frmt, date_beg, date_end_frmt, date_end, " + 
        	"                name_currency, id_club, id_nat_prs, " +
        	"                date_open_frmt date_open, date_close_frmt date_close, " +
        	"                expiry_date_frmt expiry_date, vk_sys_key_card ver_key, name_card_status, name_card_state, " +
        	"                name_card_type, name_bon_category, name_disc_category, " +
        	"                club_bon_percent club_bon, club_disc_percent club_disc " +
        	"           FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_card_h_club_all " +
        	"                  WHERE card_serial_number = ? "+
        	"                    AND id_issuer = ? " + 
        	"                    AND id_payment_system = ? ";
    	pParam.add(new bcFeautureParam("string", this.cardSerialNumber));
    	pParam.add(new bcFeautureParam("int", this.idIssuer));
    	pParam.add(new bcFeautureParam("int", this.idPaymentSystem));
    	
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    				" AND (UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(date_end_frmt) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(name_currency) LIKE UPPER('%'||?||'%') OR " +
    				"  TO_CHAR(id_club) LIKE UPPER('%'||?||'%') OR " +
    				"  TO_CHAR(id_nat_prs) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL + 
        	"                  ORDER BY date_beg, date_end) " +
        	"          WHERE ROWNUM < ?)" +
        	"  WHERE rn >= ?";
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        
        boolean hasNatPrsPermission = false;
        
        try{
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
            for (int i=1; i <= colCount; i++) {
            	html.append(getBottomFrameTableTH(clubcardXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i<=colCount; i++) {
                	if ("ID_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i)) && 
                			hasNatPrsPermission && 
                			(!isEmpty(rset.getString(i)))) {
                		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString(i), "", ""));
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

    public String getClubCardsDiscountHTML(String pFind, String id_status, String id_bon_category, String id_disc_category, String id_serv_place, String id_scheme, String cd_kind, String p_beg, String p_end) {
        String mySQL = "";
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        PreparedStatement st1 = null;
        Connection con = null;
        String my_hyperlink = "";
        String sql2 = "";
        String lSerPlace = "";
        ArrayList<bcFeautureParam> pParam = initParamArray();
        ArrayList<bcFeautureParam> pParam2 = initParamArray();

        try{
        	mySQL = 
            	" SELECT name_jur_prs, name_service_place, adr_service_place," +
            	"        name_kind_loyality, id_jur_prs, id_service_place, " +
            	"        name_loyality_scheme, cd_kind_loyality, id_loyality_scheme " +
            	"  FROM (SELECT ROWNUM rn, name_jur_prs, name_service_place, adr_service_place," +
            	"               name_kind_loyality, id_jur_prs, id_service_place, " +
            	"               name_loyality_scheme, cd_kind_loyality, id_loyality_scheme" +
        		"         FROM (SELECT name_jur_prs, name_service_place, adr_service_place," +
            	"                      name_kind_loyality, id_jur_prs, id_service_place, " +
            	"                      name_loyality_scheme, cd_kind_loyality, id_loyality_scheme" +
        		"                 FROM (SELECT name_jur_prs, name_service_place, adr_service_place, " +
            	"                              name_kind_loyality, id_jur_prs, id_service_place, " +
            	"                              name_loyality_scheme, cd_kind_loyality, id_loyality_scheme " +
            	"                         FROM " + getGeneralDBScheme() + ".vc_term_jur_prs_loy_lines_all a " +
            	"                        WHERE id_card_status = ? " + 
            	"                          AND id_category = ? " +
            	"                        UNION " +
            	"                       SELECT name_jur_prs, name_service_place, adr_service_place adr_full, " + 
            	"                              name_kind_loyality, id_jur_prs, id_service_place, " +
            	"                              name_loyality_scheme, cd_kind_loyality, id_loyality_scheme " +
            	"                         FROM " + getGeneralDBScheme() + ".vc_term_jur_prs_loy_lines_all a " +
            	"                        WHERE id_card_status = ? " + 
            	"                          AND id_category = ? " + 
            	"                       )";
        	pParam.add(new bcFeautureParam("int", id_status));
        	pParam.add(new bcFeautureParam("int", id_bon_category));
        	pParam.add(new bcFeautureParam("int", id_status));
        	pParam.add(new bcFeautureParam("int", id_disc_category));
        	
        	if (!isEmpty(pFind)) {
        		mySQL = mySQL + 
       				" WHERE (UPPER(name_jur_prs) LIKE UPPER('%'||?||'%') OR " +
       				"        UPPER(name_service_place) LIKE UPPER('%'||?||'%') OR " +
       				"        UPPER(adr_service_place) LIKE UPPER('%'||?||'%'))";
        		for (int i=0; i<3; i++) {
        		    pParam.add(new bcFeautureParam("string", pFind));
        		}
        	}
        	pParam.add(new bcFeautureParam("int", p_end));
        	pParam.add(new bcFeautureParam("int", p_beg));
        	mySQL = mySQL + 
            	"  ORDER BY id_jur_prs, id_service_place, name_loyality_scheme, cd_kind_loyality) " +
            	"  WHERE ROWNUM < ?)" +
            	"  WHERE rn >= ? ";
            
            LOGGER.debug(prepareSQLToLog(mySQL, pParam));
            con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            html.append("<td>&nbsp;</td>\n");
            
            html.append("<th>"+ jurpersonXML.getfieldTransl("title_partner", false)+"</th>\n");
            html.append("<th>"+ jurpersonXML.getfieldTransl("title_service_place", false)+"</th>\n");
            html.append("<th>"+ jurpersonXML.getfieldTransl("adr_full", false)+"</th>\n");
            html.append("<th>"+ loylineXML.getfieldTransl("name_kind_loyality", false)+"</th>\n");
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
            	lSerPlace = rset.getString(6);
        		if (isEmpty(lSerPlace)) {
        			lSerPlace = "";
        		}

            	if (isEmpty(id_serv_place)) {
            		
            		my_hyperlink = getHyperLinkFirst()+"../crm/cards/clubcardspecs.jsp?id="+
            			this.cardSerialNumber + "&iss=" + 
            			this.idIssuer + "&paysys=" + 
            			this.idPaymentSystem + "&id_service_place=" + 
    					lSerPlace + "&id_scheme=" + 
    					rset.getString(9) + "&cd_kind=" + 
    					rset.getString(8) + getHyperLinkMiddle();
    		
            		html.append("<tr>\n");
                    html.append("<td>" + my_hyperlink + "<img style=\"border: 0px;\" src=\"../images/oper/rows/close.png\">"+getHyperLinkEnd()+"</td>\n");
                    for (int i=1; i<=colCount-5; i++) {
                    	html.append("<td>"+getValue3(rset.getString(i))+"</td>\n");
                    }
                    html.append("</tr>\n");
                } else {
                	if (
                			(isEmpty(id_serv_place) &&
                			 isEmpty(rset.getString(6))) ||
                					(id_serv_place.equalsIgnoreCase(rset.getString(6))) &&
                			id_scheme.equalsIgnoreCase(rset.getString(9)) &&
                			cd_kind.equalsIgnoreCase(rset.getString(8))
                    		) {
                		my_hyperlink = getHyperLinkFirst()+"../crm/cards/clubcardspecs.jsp?id="+
                			this.cardSerialNumber + "&iss=" + 
                			this.idIssuer + "&paysys=" + 
                			this.idPaymentSystem + getHyperLinkMiddle();
            		
                		html.append("<tr>\n");
                        html.append("<td>" + my_hyperlink + "<img style=\"border: 0px;\" src=\"../images/oper/rows/open.png\">"+getHyperLinkEnd()+"</td>\n");
                        for (int i=1; i<=colCount-5; i++) {
                        	html.append("<td>"+getValue3(rset.getString(i))+"</td>\n");
                        }
                        html.append("</tr>\n");
                        html.append("<tr><td colspan=5>");
                        sql2 = "SELECT opr_sum, disc_percent_value, disc_fixed_value, club_disc, " +
                        	"		   max_disc_st, bonus_calc_term " +
                        	" FROM (" +
                        	" SELECT DISTINCT a.opr_sum, a.disc_percent_value, a.disc_fixed_value, " +
                			"        a.club_disc, a.max_disc_st, a.bonus_calc_term, a.opr_sum_number " +
                			"   FROM " + getGeneralDBScheme() + ".vc_term_loyality_lines_h_all a, " +
                			"        " + getGeneralDBScheme() + ".vc_term_service_places_sh_all t " +
                			"  WHERE a.id_term = t.id_term " +
                			"    AND a.id_loyality_history = t.id_loyality_history " +
                			"    AND t.id_service_place = TO_NUMBER(DECODE(?,NULL,t.id_service_place,?))" + 
                			"    AND a.id_loyality_scheme = ?" + 
                			"    AND a.id_card_status = ? " +
                			"    AND a.id_category = ? " + 
                			"    AND (a.active = 1 OR a.cd_kind_loyality = '0002'))" +
                			"  ORDER BY opr_sum_number, disc_percent_value";
                        pParam2.add(new bcFeautureParam("int", id_serv_place));
                        pParam2.add(new bcFeautureParam("int", id_serv_place));
                        pParam2.add(new bcFeautureParam("int", id_scheme));
                        pParam2.add(new bcFeautureParam("int", id_status));
                        pParam2.add(new bcFeautureParam("int", id_disc_category));
                        
                        LOGGER.debug(sql2 + 
                    			", 1={" + id_serv_place + ",int}"+ 
                    			", 2={" + id_serv_place + ",int}"+ 
                    			", 3={" + id_scheme + ",int}"+ 
                    			", 4={" + id_scheme + ",int}"+ 
                    			", 5={" + id_disc_category + ",int}");
                        
                        st1 = con.prepareStatement(sql2);
                        st1 = prepareParam(st1, pParam2);
                        ResultSet rsetbon = st1.executeQuery(sql2);
                        ResultSetMetaData mtd1 = rsetbon.getMetaData();
                        int colCount1 = mtd1.getColumnCount();
                        html.append(getBottomFrameTable());
                        html.append("<tr>");
                        for (int i=1; i <= colCount1; i++) {
                        	html.append(getBottomFrameTableTH(loylineXML, mtd1.getColumnName(i)));
                        }
                        html.append("</tr></thead><tbody>");
                        while (rsetbon.next())
                        {
                        	html.append("<tr>\n");
                        	for (int i=1; i<=colCount1; i++) {
                        		html.append("<td>"+getValue3(rsetbon.getString(i))+"</td>\n");
                        	}
                        	html.append("</tr>\n");
                        }
                        html.append("</tbody></table>\n");
                        html.append("</td></tr>\n");
                        st1.close();
                        //Connector.closeConnection(con1);
                        
                        html.append("<tr><td colspan=5>");
                        sql2 = "SELECT opr_sum, bon_percent_value, bon_fixed_value, club_bon, " +
                    		"		   max_bon_st, bonus_transfer_term " +
                    		" FROM (" +
                    		" SELECT DISTINCT a.opr_sum, a.bon_percent_value, a.bon_fixed_value, " +
                			"        a.club_bon, a.max_bon_st, a.bonus_transfer_term, a.opr_sum_number " +
                			"   FROM " + getGeneralDBScheme() + ".vc_term_loyality_lines_h_all a, " +
                			"        " + getGeneralDBScheme() + ".vc_term_service_places_sh_all t " +
                			"  WHERE a.id_term = t.id_term " +
                			"    AND a.id_loyality_history = t.id_loyality_history " +
                			"    AND t.id_service_place = TO_NUMBER(DECODE(?,NULL,t.id_service_place,?))" + 
                			"    AND a.id_loyality_scheme = "+ id_scheme + 
                			"    AND a.id_card_status= " + id_status + 
                			"    AND a.id_category = " + id_bon_category + 
                			"    AND (a.active = 1 OR a.cd_kind_loyality = '0002'))" +
                			"  ORDER BY opr_sum_number, bon_percent_value";
                        pParam2.add(new bcFeautureParam("int", id_serv_place));
                        pParam2.add(new bcFeautureParam("int", id_serv_place));
                        pParam2.add(new bcFeautureParam("int", id_scheme));
                        pParam2.add(new bcFeautureParam("int", id_status));
                        pParam2.add(new bcFeautureParam("int", id_bon_category));
                        
                        LOGGER.debug(sql2+ 
                    			", 1={" + id_serv_place + ",int}"+ 
                    			", 2={" + id_serv_place + ",int}"+ 
                    			", 3={" + id_scheme + ",int}"+ 
                    			", 4={" + id_scheme + ",int}"+ 
                    			", 5={" + id_bon_category + ",int}");
                        st1 = con.prepareStatement(sql2);
                        st1 = prepareParam(st1, pParam2);
                        ResultSet rsetdisc = st1.executeQuery(sql2);
                        ResultSetMetaData mtd2 = rsetdisc.getMetaData();
                        int colCount2 = mtd2.getColumnCount();
                        html.append(getBottomFrameTable());
                        html.append("<tr>");
                        for (int i=1; i <= colCount2; i++) {	
                        	html.append(getBottomFrameTableTH(loylineXML, mtd2.getColumnName(i)));
                        }
                        html.append("</tr></thead><tbody>");
                        while (rsetdisc.next())
                        {
                        	html.append("<tr>\n");
                        	for (int i=1; i<=colCount2; i++) {
                        		html.append("<td>"+getValue3(rsetdisc.getString(i))+"</td>\n");
                        	}
                        	html.append("</tr>\n");
                        }
                        html.append("</tbody></table>\n");
                        html.append("</td></tr>\n");
                        st1.close();
                        //Connector.closeConnection(con1);
                	} else {
                		my_hyperlink = getHyperLinkFirst()+"../crm/cards/clubcardspecs.jsp?id="+
                			this.cardSerialNumber + "&iss=" + 
                			this.idIssuer + "&paysys=" + 
                			this.idPaymentSystem + "&id_jur_prs=" +
                			rset.getString(5) + "&id_service_place=" + 
                			lSerPlace + "&id_scheme=" + 
        					rset.getString(9) + "&cd_kind=" + 
        					rset.getString(8) + getHyperLinkMiddle();
        		
                		html.append("<tr>\n");
                        html.append("<td>" + my_hyperlink + "<img style=\"border: 0px;\" src=\"../images/oper/rows/close.png\">"+getHyperLinkEnd()+"</td>\n");
                		for (int i=1; i<=colCount-5; i++) {
                        	html.append("<td>"+getValue3(rset.getString(i))+"</td>\n");
                        }
                        html.append("</tr>\n");
                	}
                }
                
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
    }//getClubCardsDiscountHTML   
    
    public String getClubCardsTasksHTML(String pFindString, String pOperType, String pOperState, String p_beg, String p_end) {
    	bcListCardOperation list = new bcListCardOperation();
    	
    	String pWhereCause = 
    		"   WHERE card_serial_number = ? " + 
    		"     AND card_id_issuer = ? " + 
    		"     AND card_id_payment_system = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("string", this.cardSerialNumber));
    	pWhereValue.add(new bcFeautureParam("int", this.idIssuer));
    	pWhereValue.add(new bcFeautureParam("int", this.idPaymentSystem));

    	String lDeleteLink = "";
    	String lEditLink = "";
 	 	if (isEditPermited("CARDS_CLUBCARDS_TASKS") >0) {
 	 		lDeleteLink = "../crm/cards/card_tasksupdate.jsp?back_type=CARD&card_serial_number="+this.cardSerialNumber+"&id_issuer="+this.idIssuer+"&id_payment_system="+this.idPaymentSystem+"&type=general";
 	 		lEditLink = "../crm/cards/card_tasksupdate.jsp?back_type=CARD&card_serial_number="+this.cardSerialNumber+"&id_issuer="+this.idIssuer+"&id_payment_system="+this.idPaymentSystem+"&type=general";
 	 	}
    	
    	return list.getCardOperationsHTML(pWhereCause, pWhereValue, pFindString, pOperType, pOperState, lEditLink, lDeleteLink, "div_data_detail", p_beg, p_end);
    	
    }

    public String getCardPostingsHTML(String pFind, String idBkOperationType, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 	
        	" SELECT rn, id_posting_detail, id_posting_line, operation_date, name_currency," +
        	"        oper_number id_bk_operation_scheme_line, debet_id_bk_account, credit_id_bk_account, " +
        	"		 entered_amount, assignment_posting, id_clearing_line, " +
        	"        debet_id_bk_account2, debet_cd_bk_account, debet_name_bk_account, " +
        	"        credit_id_bk_account2, credit_cd_bk_account, credit_name_bk_account, " +
        	"        id_bk_operation_scheme_line id_bk_operation_scheme_line2 " +
        	"   FROM (SELECT ROWNUM rn, id_posting_detail, id_posting_line, operation_date_frmt operation_date, " +
        	"                name_currency, oper_number, id_bk_operation_scheme_line, " +
        	"                debet_cd_bk_account||' '||debet_name_bk_account debet_id_bk_account,  " +
        	"        		 credit_cd_bk_account||' '||credit_name_bk_account credit_id_bk_account, " +
        	"		 		 entered_amount_frmt entered_amount, assignment_posting, " +
        	"        		 id_clearing_line, " +
        	"        		 debet_id_bk_account debet_id_bk_account2, " +
        	"                debet_cd_bk_account_frmt debet_cd_bk_account, debet_name_bk_account, " +
        	"        		 credit_id_bk_account credit_id_bk_account2, " +
        	"                credit_cd_bk_account_frmt credit_cd_bk_account, credit_name_bk_account " +
        	"   		FROM (SELECT * " +
        	"                   FROM "+getGeneralDBScheme() + ".vc_acc_posting_detail_club_all " +
        	"                  WHERE (debet_cd_bk_account LIKE '%'||?||'_'||?||'_'||?||'%'" +
        	"						OR credit_cd_bk_account LIKE '%'||?||'_'||?||'_'||?||'%') ";
    	pParam.add(new bcFeautureParam("string", this.cardSerialNumber));
    	pParam.add(new bcFeautureParam("int", this.idIssuer));
    	pParam.add(new bcFeautureParam("int", this.idPaymentSystem));
    	pParam.add(new bcFeautureParam("string", this.cardSerialNumber));
    	pParam.add(new bcFeautureParam("int", this.idIssuer));
    	pParam.add(new bcFeautureParam("int", this.idPaymentSystem));
    	
        if (!isEmpty(idBkOperationType)) {
        	mySQL = mySQL + " AND id_bk_operation_scheme_line IN (" +
        			" SELECT id_bk_operation_scheme_line " +
        			"   FROM "+getGeneralDBScheme() + ".vc_oper_scheme_lines_club_all " +
        			"  WHERE cd_bk_operation_type = ?) ";
        	pParam.add(new bcFeautureParam("string", pFind));
        }
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    				" AND (TO_CHAR(id_posting_detail) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(debet_cd_bk_account) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(credit_cd_bk_account) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(oper_number) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(assignment_posting) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(operation_date_frmt) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(entered_amount_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<7; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        	"         ) WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
        boolean hasPostingPermission = false;
        boolean hasAccDocPermission = false;
        boolean hasBKAccountPermission = false;
        boolean hasSchemePermission = false;
        
        //boolean hasEditPermission = false;
        try{
        	//if (isEditPermited("CARDS_TRANSACTIONS_POSTINS")>=0) {
        	//	hasEditPermission = true;
        	//}
        	if (isEditMenuPermited("FINANCE_POSTINGS")>=0) {
        		hasPostingPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_ACCOUNTING_DOC")>=0) {
        		hasAccDocPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_BK_ACCOUNTS")>=0) {
        		hasBKAccountPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_POSTING_SCHEME")>=0) {
        		hasSchemePermission = true;
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
            for (int i=1; i <= colCount-8; i++) {
                String colName = mtd.getColumnName(i);
                html.append(getBottomFrameTableTH(postingXML, colName));
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i <= colCount-8; i++) {
                	if ("ID_POSTING_DETAIL".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasPostingPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/postingspecs.jsp?id="+rset.getString(i), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("ID_POSTING_LINE".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasAccDocPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/accounting_docspecs.jsp?id="+rset.getString(i), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("ID_BK_OPERATION_SCHEME_LINE".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasSchemePermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/posting_scheme_linespecs.jsp?id="+rset.getString("ID_BK_OPERATION_SCHEME_LINE2"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("DEBET_ID_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasBKAccountPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString("DEBET_CD_BK_ACCOUNT"), "../crm/finance/postingspecs.jsp?id="+rset.getString("DEBET_ID_BK_ACCOUNT2"), "", ""));
                		} else {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
                		}
                	} else if ("CREDIT_ID_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i))) {
                		if (hasBKAccountPermission && (!(rset.getString(i) == null || "".equalsIgnoreCase(rset.getString(i))))) {
                			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString("CREDIT_CD_BK_ACCOUNT"), "../crm/finance/postingspecs.jsp?id="+rset.getString("CREDIT_ID_BK_ACCOUNT2"), "", ""));
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
    } //getCardPostingsHTML
	
    public String getLogisticHTML(String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	"SELECT rn, id_lg_record, action_date_frmt, operation_desc, bon_cards_count, " +
        	"		sname_jur_prs_receiver,  sname_jur_prs_sender," +
        	"       id_jur_prs_receiver, id_jur_prs_sender " +
        	"  FROM (SELECT ROWNUM rn, id_lg_record, operation_desc, bon_cards_count, " +
        	"               sname_jur_prs_receiver, sname_jur_prs_sender, action_date_frmt, " +
        	"               id_jur_prs_receiver, id_jur_prs_sender " +
        	"		   FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme() + ".vc_lg_card_priv_all "+
            "                  WHERE id_lg_record IN " +
            "                       (SELECT id_lg_record " +
            "                          FROM " + getGeneralDBScheme() + ".vc_lg_card_rng_priv_all " +
            "                         WHERE ? BETWEEN begin_cd_card2 AND end_cd_card2 " +
            "                           AND id_issuer = ? " + 
            "                           AND id_payment_system = ? " + 
            "                  ) ";
    	pParam.add(new bcFeautureParam("string", this.getValue("CD_CARD2")));
    	pParam.add(new bcFeautureParam("int", this.getValue("ID_ISSUER")));
    	pParam.add(new bcFeautureParam("int", this.getValue("ID_PAYMENT_SYSTEM")));
    	
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(id_lg_record) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(action_date_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(operation_desc) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(bon_cards_count) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(sname_jur_prs_receiver) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(sname_jur_prs_sender) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<6; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
            "				  ORDER BY action_date) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasLogisticPermission = false;
        boolean hadJurPrsPermission = false;
        
        try{
        	if (isEditMenuPermited("LOGISTIC_PARTNERS_CARDS")>=0) {
        		hasLogisticPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hadJurPrsPermission = true;
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
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if (hasLogisticPermission && "ID_LG_RECORD".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/logistic/partners/cardspecs.jsp?id="+rset.getString(i), "", ""));
         	  		} else if (hadJurPrsPermission && "SNAME_JUR_PRS_RECEIVER".equalsIgnoreCase(mtd.getColumnName(i)) &&
         	  				!isEmpty(rset.getString("ID_JUR_PRS_RECEIVER"))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS_RECEIVER"), "", ""));
         	  		} else if (hadJurPrsPermission && "SNAME_JUR_PRS_SENDER".equalsIgnoreCase(mtd.getColumnName(i)) &&
         	  				!isEmpty(rset.getString("ID_JUR_PRS_SENDER"))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS_SENDER"), "", ""));
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
    } //getAssignTermHistoryHTML
	
    public String getPursesHTML(String pFind, String pPurseType, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	"SELECT rn, number_card_purse, name_card_purse_type, value_card_purse_frmt, name_currency, id_card_purse " +
        	"  FROM (SELECT ROWNUM rn, number_card_purse, name_card_purse_type, name_currency, value_card_purse_frmt, id_card_purse " +
        	"		   FROM (SELECT number_card_purse, name_card_purse_type, name_currency, value_card_purse_frmt, id_card_purse " +
            "                   FROM " + getGeneralDBScheme() + ".vc_card_purse_all "+
            "                  WHERE card_serial_number = ? " +
	        "                   AND id_issuer = ? " + 
	        "                   AND id_payment_system = ? ";
    	pParam.add(new bcFeautureParam("string", this.cardSerialNumber));
    	pParam.add(new bcFeautureParam("int", this.idIssuer));
    	pParam.add(new bcFeautureParam("int", this.idPaymentSystem));
    	
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(id_card_purse) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(TO_CHAR(number_card_purse)) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(name_currency) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(value_card_purse_frmt) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	if (!isEmpty(pPurseType)) {
    		mySQL = mySQL + " AND cd_card_purse_type = ? ";
    		pParam.add(new bcFeautureParam("string", pPurseType));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
	        "                 ORDER BY number_card_purse) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        boolean hasEditPermission = false;
        
        try{
        	if (isEditPermited("CARDS_CLUBCARDS_PURSES")>0) {
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
            for (int i=1; i <= colCount-1; i++) {
                html.append(getBottomFrameTableTH(clubcardXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-1; i++) {
          	  		if ("NUMBER_CARD_PURSE".equalsIgnoreCase(mtd.getColumnName(i)) || 
          	  			"NAME_CARD_PURSE_TYPE".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardpursespecs.jsp?id="+rset.getString(i), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
	            if (hasEditPermission) {
	            	String myHyperLink = "../crm/cards/clubcardpursespecs.jsp?id="+rset.getString("ID_CARD_PURSE");
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
    } //getAssignTermHistoryHTML
	
    public String getFinanceOperationsHTML(String pState, String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	"SELECT rn, id_fn_card_oper, id_card_purse, date_fn_card_oper_frmt, payment_order_fn_card_oper, " +
        	"       name_fn_oper_type, name_fn_card_oper_state, value_fn_card_oper_frmt " +
        	"  FROM (SELECT ROWNUM rn, id_fn_card_oper, id_card_purse, date_fn_card_oper_frmt, payment_order_fn_card_oper, " +
        	"               name_fn_oper_type, name_fn_card_oper_state, value_fn_card_oper_frmt " +
        	"		   FROM (SELECT id_fn_card_oper, id_card_purse, date_fn_card_oper_frmt, payment_order_fn_card_oper, " +
        	"                       name_fn_oper_type, " +
        	"                       DECODE(cd_fn_card_oper_state, " +
    		"                              'ACCEPT', '<font color=\"blue\"><b>'||name_fn_card_oper_state||'</b></font>', " +
    		"                              'CONFIRM', '<font color=\"green\">'||name_fn_card_oper_state||'</font>', " +
    		"                              'REJECT', '<font color=\"red\">'||name_fn_card_oper_state||'</font>', " +
    		"                              'ERROR', '<font color=\"red\"><b>'||name_fn_card_oper_state||'</b></font>', " +
    		"                              name_fn_card_oper_state" +
    		"                       ) name_fn_card_oper_state, " +
        	"                       value_fn_card_oper_frmt " +
            "                   FROM " + getGeneralDBScheme() + ".vc_fn_card_oper_club_all "+
            "                  WHERE card_serial_number = ? " +
	        "                   AND id_issuer = ? " + 
	        "                   AND id_payment_system = ? ";
    	pParam.add(new bcFeautureParam("string", this.cardSerialNumber));
    	pParam.add(new bcFeautureParam("int", this.idIssuer));
    	pParam.add(new bcFeautureParam("int", this.idPaymentSystem));
    	
        if (!isEmpty(pState)) {
        	mySQL = mySQL + " AND cd_fn_card_oper_state = ? ";
        	pParam.add(new bcFeautureParam("string", pState));
        }
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + 
        		" AND (TO_CHAR(id_fn_card_oper) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(date_fn_card_oper_frmt) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(payment_order_fn_card_oper) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(name_fn_oper_type) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(value_fn_card_oper_frmt) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<5; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL + 
	        "                 ORDER BY date_fn_card_oper DESC, id_fn_card_oper) " +
            "          WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        //boolean hasEditPermission = false;
        
        boolean hasPursePermission = false;
        
        try{
        	if (isEditPermited("CARDS_CLUBCARDS_PURSES")>=0) {
        		hasPursePermission = true;
        	}
        	
        	/*if (isEditPermited("CARDS_CLUBCARDS_PURSES")>0) {
        		hasEditPermission = true;
        	}*/
        	
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
                html.append(getBottomFrameTableTH(clubcardXML, mtd.getColumnName(i)));
            }
            /*if (hasEditPermission) {
            	html.append("<th>&nbsp;</th>\n");
            }*/
            html.append("</tr></thead><tbody>");
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount; i++) {
          	  		if ("ID_CARD_PURSE".equalsIgnoreCase(mtd.getColumnName(i)) && hasPursePermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardpursespecs.jsp?id="+rset.getString(i), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
	            /*if (hasEditPermission) {
	            	String myHyperLink = getContextPath()+"../crm/cards/clubcardpursespecs.jsp?id="+rset.getString("ID_CARD_PURSE");
	            	html.append(getEditButtonHTML(myHyperLink, getLanguage()));
	            } else {
	            	html.append("<td align=center>&nbsp;</td>\n");
	            }*/
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
    } //getAssignTermHistoryHTML


    public String getRestsHTML(String pBeginPeriod, String pEndPeriod, String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String mySQL = 
	        	"SELECT rn, date_from_frmt, sname_currency, " +
	            "       /*bon_acc_balance_beg_frmt,*/ bon_acc_turnover_frmt, bon_acc_balance_end_frmt, " +
	            "       /*bon_cur_balance_beg_frmt,*/ bon_cur_turnover_frmt, bon_cur_balance_end_frmt, " +
	            "       /*bon_full_balance_beg_frmt,*/ bon_full_turnover_frmt, bon_full_balance_end_frmt " +
	            "  FROM (SELECT ROWNUM rn, date_from_frmt, sname_currency, " +
	            "               bon_acc_balance_beg_frmt, bon_acc_turnover_frmt, bon_acc_balance_end_frmt, " +
	            "               bon_cur_balance_beg_frmt, bon_cur_turnover_frmt, bon_cur_balance_end_frmt, " +
	            "               bon_full_balance_beg_frmt, bon_full_turnover_frmt, bon_full_balance_end_frmt" +
	            "          FROM (SELECT date_from_frmt, sname_currency, " +
	            "                       bon_acc_balance_beg_frmt, " +
	            "                       CASE WHEN bon_acc_turnover > 0 THEN '<font color=\"green\"><b>'||bon_acc_turnover_frmt||'</b></font>' " +
	            "                            WHEN bon_acc_turnover < 0 THEN '<font color=\"blue\"><b>'||bon_acc_turnover_frmt||'</b></font>' " +
	    		"                            ELSE bon_acc_turnover_frmt " +
	    		"                       END bon_acc_turnover_frmt, " +
	            "						bon_acc_balance_end_frmt, " +
	            "                       bon_cur_balance_beg_frmt, " +
	            "                       CASE WHEN bon_cur_turnover > 0 THEN '<font color=\"green\"><b>'||bon_cur_turnover_frmt||'</b></font>' " +
	            "                            WHEN bon_cur_turnover < 0 THEN '<font color=\"blue\"><b>'||bon_cur_turnover_frmt||'</b></font>' " +
	    		"                            ELSE bon_cur_turnover_frmt " +
	    		"                       END bon_cur_turnover_frmt, " +
	            "						bon_cur_balance_end_frmt, " +
	            "                       bon_full_balance_beg_frmt, " +
	            "                       CASE WHEN bon_full_turnover > 0 THEN '<font color=\"green\"><b>'||bon_full_turnover_frmt||'</b></font>' " +
	            "                            WHEN bon_full_turnover < 0 THEN '<font color=\"blue\"><b>'||bon_full_turnover_frmt||'</b></font>' " +
	    		"                            ELSE bon_full_turnover_frmt " +
	    		"                       END bon_full_turnover_frmt, " +
	            "						bon_full_balance_end_frmt " +
	            "                  FROM " + getGeneralDBScheme() + ".vc_card_bon_rests_all " +
	            "                 WHERE card_serial_number = ? " +
		        "                   AND id_issuer = ? " + 
		        "                   AND id_payment_system = ? ";
    	pParam.add(new bcFeautureParam("string", this.cardSerialNumber));
    	pParam.add(new bcFeautureParam("int", this.idIssuer));
    	pParam.add(new bcFeautureParam("int", this.idPaymentSystem));
    	
        if (!isEmpty(pBeginPeriod)) {
        	mySQL = mySQL + " and date_from >= TO_DATE(?,?) ";
        	pParam.add(new bcFeautureParam("string", pBeginPeriod));
        	pParam.add(new bcFeautureParam("string", this.getDateFormat()));
        }
        if (!isEmpty(pEndPeriod)) {
        	mySQL = mySQL + " and date_from <= TO_DATE(?,?) ";
        	pParam.add(new bcFeautureParam("string", pEndPeriod));
        	pParam.add(new bcFeautureParam("string", this.getDateFormat()));
        }
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + 
				" AND (UPPER(date_from_frmt) LIKE UPPER('%'||?||'%') OR " +
				//"      UPPER(bon_acc_balance_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(bon_acc_turnover_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(bon_acc_balance_end_frmt) LIKE UPPER('%'||?||'%') OR " +
				//"      UPPER(bon_cur_balance_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(bon_cur_turnover_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(bon_cur_balance_end_frmt) LIKE UPPER('%'||?||'%') OR " +
				//"      UPPER(bon_full_balance_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(bon_full_turnover_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(bon_full_balance_end_frmt) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<7; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
		}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + " ORDER BY TRUNC(date_from) DESC " +
        	"    ) WHERE ROWNUM < ?" + 
        	" ) WHERE rn >= ?";
       
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
            html.append(getBottomFrameTable());
            html.append("<tr>");
            html.append("<th rowspan=\"2\">"+ commonXML.getfieldTransl("RN", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clubcardXML.getfieldTransl("DATE_TURNOVER", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clubcardXML.getfieldTransl("SNAME_CURRENCY", false)+"</th>\n");
            html.append("<th colspan=\"2\">"+ clubcardXML.getfieldTransl("BON_ACC_BALANCE", false)+"</th>\n");
            html.append("<th colspan=\"2\">"+ clubcardXML.getfieldTransl("BON_CUR_BALANCE", false)+"</th>\n");
            html.append("<th colspan=\"2\">"+ clubcardXML.getfieldTransl("BON_FULL_BALANCE", false)+"</th>\n");
            html.append("</tr>");
            html.append("<tr>");
            //html.append("<th>"+ clubcardXML.getfieldTransl("BEGIN_BALANCE", false)+"</th>\n");
            html.append("<th>"+ clubcardXML.getfieldTransl("BON_TURNOVER", false)+"</th>\n");
            html.append("<th>"+ clubcardXML.getfieldTransl("END_BALANCE", false)+"</th>\n");
            //html.append("<th>"+ clubcardXML.getfieldTransl("BEGIN_BALANCE", false)+"</th>\n");
            html.append("<th>"+ clubcardXML.getfieldTransl("BON_TURNOVER", false)+"</th>\n");
            html.append("<th>"+ clubcardXML.getfieldTransl("END_BALANCE", false)+"</th>\n");
            //html.append("<th>"+ clubcardXML.getfieldTransl("BEGIN_BALANCE", false)+"</th>\n");
            html.append("<th>"+ clubcardXML.getfieldTransl("BON_TURNOVER", false)+"</th>\n");
            html.append("<th>"+ clubcardXML.getfieldTransl("END_BALANCE", false)+"</th>\n");
            html.append("</tr></thead><tbody>");
            html.append("<tr>");
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
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        return html.toString();
       }


    public String getRestsFullHTML(String pBeginPeriod, String pEndPeriod, String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null; 

        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        String mySQL = 
	        	"SELECT rn, date_from_frmt, sname_currency, " +
	            "       bon_acc_balance_beg_frmt, bon_acc_turnover_frmt, bon_acc_balance_end_frmt, " +
	            "       bon_cur_balance_beg_frmt, bon_cur_turnover_frmt, bon_cur_balance_end_frmt, " +
	            "       bon_full_balance_beg_frmt, bon_full_turnover_frmt, bon_full_balance_end_frmt " +
	            "  FROM (SELECT ROWNUM rn, date_from_frmt, sname_currency, " +
	            "               bon_acc_balance_beg_frmt, bon_acc_turnover_frmt, bon_acc_balance_end_frmt, " +
	            "               bon_cur_balance_beg_frmt, bon_cur_turnover_frmt, bon_cur_balance_end_frmt, " +
	            "               bon_full_balance_beg_frmt, bon_full_turnover_frmt, bon_full_balance_end_frmt" +
	            "          FROM (SELECT date_from_frmt, sname_currency, " +
	            "                       CASE WHEN bon_acc_balance_beg > 0 THEN '<font color=\"green\"><b>'||bon_acc_balance_beg_frmt||'</b></font>' " +
	            "                            WHEN bon_acc_balance_beg < 0 THEN '<font color=\"blue\"><b>'||bon_acc_balance_beg_frmt||'</b></font>' " +
	    		"                            ELSE bon_acc_balance_beg_frmt " +
	    		"                       END bon_acc_balance_beg_frmt, " +
	            "                       CASE WHEN bon_acc_turnover > 0 THEN '<font color=\"green\"><b>'||bon_acc_turnover_frmt||'</b></font>' " +
	            "                            WHEN bon_acc_turnover < 0 THEN '<font color=\"blue\"><b>'||bon_acc_turnover_frmt||'</b></font>' " +
	    		"                            ELSE bon_acc_turnover_frmt " +
	    		"                       END bon_acc_turnover_frmt, " +
	    		"                       CASE WHEN bon_acc_balance_end > 0 THEN '<font color=\"green\"><b>'||bon_acc_balance_end_frmt||'</b></font>' " +
	            "                            WHEN bon_acc_balance_end < 0 THEN '<font color=\"blue\"><b>'||bon_acc_balance_end_frmt||'</b></font>' " +
	    		"                            ELSE bon_acc_balance_end_frmt " +
	    		"                       END bon_acc_balance_end_frmt, " +
	    		"                       CASE WHEN bon_cur_balance_beg > 0 THEN '<font color=\"green\"><b>'||bon_cur_balance_beg_frmt||'</b></font>' " +
	            "                            WHEN bon_cur_balance_beg < 0 THEN '<font color=\"blue\"><b>'||bon_cur_balance_beg_frmt||'</b></font>' " +
	    		"                            ELSE bon_cur_balance_beg_frmt " +
	    		"                       END bon_cur_balance_beg_frmt, " +
	            "                       CASE WHEN bon_cur_turnover > 0 THEN '<font color=\"green\"><b>'||bon_cur_turnover_frmt||'</b></font>' " +
	            "                            WHEN bon_cur_turnover < 0 THEN '<font color=\"blue\"><b>'||bon_cur_turnover_frmt||'</b></font>' " +
	    		"                            ELSE bon_cur_turnover_frmt " +
	    		"                       END bon_cur_turnover_frmt, " +
	    		"                       CASE WHEN bon_cur_balance_end > 0 THEN '<font color=\"green\"><b>'||bon_cur_balance_end_frmt||'</b></font>' " +
	            "                            WHEN bon_cur_balance_end < 0 THEN '<font color=\"blue\"><b>'||bon_cur_balance_end_frmt||'</b></font>' " +
	    		"                            ELSE bon_cur_balance_end_frmt " +
	    		"                       END bon_cur_balance_end_frmt, " +
	    		"                       CASE WHEN bon_full_balance_beg > 0 THEN '<font color=\"green\"><b>'||bon_full_balance_beg_frmt||'</b></font>' " +
	            "                            WHEN bon_full_balance_beg < 0 THEN '<font color=\"blue\"><b>'||bon_full_balance_beg_frmt||'</b></font>' " +
	    		"                            ELSE bon_full_balance_beg_frmt " +
	    		"                       END bon_full_balance_beg_frmt, " +
	            "                       CASE WHEN bon_full_turnover > 0 THEN '<font color=\"green\"><b>'||bon_full_turnover_frmt||'</b></font>' " +
	            "                            WHEN bon_full_turnover < 0 THEN '<font color=\"blue\"><b>'||bon_full_turnover_frmt||'</b></font>' " +
	    		"                            ELSE bon_full_turnover_frmt " +
	    		"                       END bon_full_turnover_frmt, " +
	    		"                       CASE WHEN bon_full_balance_end > 0 THEN '<font color=\"green\"><b>'||bon_full_balance_end_frmt||'</b></font>' " +
	            "                            WHEN bon_full_balance_end < 0 THEN '<font color=\"blue\"><b>'||bon_full_balance_end_frmt||'</b></font>' " +
	    		"                            ELSE bon_full_balance_end_frmt " +
	    		"                       END bon_full_balance_end_frmt " +
	            "                  FROM " + getGeneralDBScheme() + ".vc_card_bon_rests_all " +
	            "                 WHERE card_serial_number = ? " +
		        "                   AND id_issuer = ? " + 
		        "                   AND id_payment_system = ? " ;
    	pParam.add(new bcFeautureParam("string", this.cardSerialNumber));
    	pParam.add(new bcFeautureParam("int", this.idIssuer));
    	pParam.add(new bcFeautureParam("int", this.idPaymentSystem));
    	
        if (!isEmpty(pBeginPeriod)) {
        	mySQL = mySQL + " and date_from >= TO_DATE(?,?) ";
        	pParam.add(new bcFeautureParam("string", pBeginPeriod));
        	pParam.add(new bcFeautureParam("string", this.getDateFormat()));
        }
        if (!isEmpty(pEndPeriod)) {
        	mySQL = mySQL + " and date_from <= TO_DATE(?,?) ";
        	pParam.add(new bcFeautureParam("string", pEndPeriod));
        	pParam.add(new bcFeautureParam("string", this.getDateFormat()));
        }
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + 
				" AND (UPPER(date_from_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(bon_acc_balance_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(bon_acc_turnover_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(bon_acc_balance_end_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(bon_cur_balance_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(bon_cur_turnover_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(bon_cur_balance_end_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(bon_full_balance_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(bon_full_turnover_frmt) LIKE UPPER('%'||?||'%') OR " +
				"      UPPER(bon_full_balance_end_frmt) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<10; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
		}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + " ORDER BY TRUNC(date_from) DESC " +
        	"    ) WHERE ROWNUM < ?" + 
        	" ) WHERE rn >= ?";
       
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
            html.append(getBottomFrameTable());
            html.append("<tr>");
            html.append("<th rowspan=\"2\">"+ commonXML.getfieldTransl("RN", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clubcardXML.getfieldTransl("DATE_TURNOVER", false)+"</th>\n");
            html.append("<th rowspan=\"2\">"+ clubcardXML.getfieldTransl("SNAME_CURRENCY", false)+"</th>\n");
            html.append("<th colspan=\"3\">"+ clubcardXML.getfieldTransl("BON_ACC_BALANCE", false)+"</th>\n");
            html.append("<th colspan=\"3\">"+ clubcardXML.getfieldTransl("BON_CUR_BALANCE", false)+"</th>\n");
            html.append("<th colspan=\"3\">"+ clubcardXML.getfieldTransl("BON_FULL_BALANCE", false)+"</th>\n");
            html.append("</tr>");
            html.append("<tr>");
            html.append("<th>"+ clubcardXML.getfieldTransl("BEGIN_BALANCE", false)+"</th>\n");
            html.append("<th>"+ clubcardXML.getfieldTransl("BON_TURNOVER", false)+"</th>\n");
            html.append("<th>"+ clubcardXML.getfieldTransl("END_BALANCE", false)+"</th>\n");
            html.append("<th>"+ clubcardXML.getfieldTransl("BEGIN_BALANCE", false)+"</th>\n");
            html.append("<th>"+ clubcardXML.getfieldTransl("BON_TURNOVER", false)+"</th>\n");
            html.append("<th>"+ clubcardXML.getfieldTransl("END_BALANCE", false)+"</th>\n");
            html.append("<th>"+ clubcardXML.getfieldTransl("BEGIN_BALANCE", false)+"</th>\n");
            html.append("<th>"+ clubcardXML.getfieldTransl("BON_TURNOVER", false)+"</th>\n");
            html.append("<th>"+ clubcardXML.getfieldTransl("END_BALANCE", false)+"</th>\n");
            html.append("</tr></thead><tbody>");
            html.append("<tr>");
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
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        return html.toString();
       }

	public String getBKAccountsHTML(String pFind, String pExistFlag, String p_beg, String p_end) {
	        StringBuilder html = new StringBuilder();
	        Connection con = null;
	        PreparedStatement st = null;
	        ArrayList<bcFeautureParam> pParam = initParamArray();
	        String mySQL = 
	        	" SELECT id_bk_account, cd_bk_account, name_bk_account, " +
	            "        internal_name_bk_account, " +
	            "        DECODE(exist_flag, " +
				"          		'Y', '<b><font color=\"green\">'||exist_flag_tsl||'</font></b>', " +
				"              	'<b><font color=\"red\">'||exist_flag_tsl||'</font></b>' " +
				"        ) exist_flag_tsl, " +
	            "        exist_flag " +
	            "   FROM (SELECT ROWNUM rn, id_bk_account, cd_bk_account, name_bk_account, " +
	            "                internal_name_bk_account, exist_flag, exist_flag_tsl " +
	            "           FROM (SELECT *" +
	            "                   FROM " + getGeneralDBScheme() + ".vc_bk_accounts_member_priv_all "+
	            "                  WHERE UPPER(cd_participant) = 'CLIENT'" +
	            "                    AND id_member =  ?||'_'||?||'_'||?";
	    	pParam.add(new bcFeautureParam("string", this.cardSerialNumber));
	    	pParam.add(new bcFeautureParam("string", this.idIssuer));
	    	pParam.add(new bcFeautureParam("string", this.idPaymentSystem));
	    	
	        if (!isEmpty(pFind)) {
	        	mySQL = mySQL + 
					" AND (TO_CHAR(id_bk_account) LIKE UPPER('%'||?||'%') OR " +
					"      UPPER(cd_bk_account) LIKE UPPER('%'||?||'%') OR " +
					"      UPPER(name_bk_account) LIKE UPPER('%'||?||'%') OR " +
					"      UPPER(internal_name_bk_account) LIKE UPPER('%'||?||'%')) ";
	        	for (int i=0; i<4; i++) {
	        	    pParam.add(new bcFeautureParam("string", pFind));
	        	}
			}
	        if (!isEmpty(pExistFlag)) {
	        	mySQL = mySQL + " AND exist_flag = ? ";
	        	pParam.add(new bcFeautureParam("string", pExistFlag));
			}
	        pParam.add(new bcFeautureParam("int", p_end));
	        pParam.add(new bcFeautureParam("int", p_beg));
	        mySQL = mySQL + 
	            "                  ORDER BY cd_bk_account) " +
	            "          WHERE ROWNUM < ?)" +
	            " WHERE rn >= ?";
        	
        	
	        boolean hasBKAccountPermission = false; 
	        
	        String myFont = "";

	        String myBgColor = noneBackGroundStyle;
	        try{
	        	if (isEditMenuPermited("FINANCE_BK_ACCOUNTS")>=0) {
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
	            for (int i=1; i <= colCount-1; i++) {
	                html.append(getBottomFrameTableTH(bk_accountXML, mtd.getColumnName(i)));
	            }
	            html.append("</tr></thead><tbody>\n");
	            while (rset.next())
	            {
	          	  	if ("Y".equalsIgnoreCase(rset.getString("EXIST_FLAG"))) {
	                		myFont = "";
	                		myBgColor = noneBackGroundStyle;
	                } else {
	                		myFont = "<font color=\"red\">";
	                		myBgColor = selectedBackGroundStyle;
	                }

	          	  	html.append("<tr>");
	          	  	for (int i=1; i <= colCount-1; i++) {
	          	  		if (hasBKAccountPermission && 
	          	  				("ID_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i)) ||
	          	  				 "CD_BK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i)))) {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/bk_accountspecs.jsp?id="+rset.getString(i), myFont, myBgColor));
	         	  		} else {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBgColor));
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
	} // getJurPersonBKAccountsHTML
	
    /*public String getOfficePrivateActionsHTML(String pFind, String pActionType, String pActionState, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();
        String mySQL = 
	        "SELECT rn, id_op_action, sname_office_private, date_op_action_frmt, name_op_action_type, " + 
			"		name_report, name_op_action_state, error_op_action, id_office_private, id_report " +
			"  FROM (SELECT ROWNUM rn, id_op_action, sname_office_private, date_op_action_frmt, name_op_action_type, " + 
			"				name_report, name_op_action_state, error_op_action, id_office_private, id_report " +
			"          FROM (SELECT id_op_action, sname_office_private, date_op_action_frmt, " +
			"        		 		DECODE(cd_op_action_type, " +
	        "               			   'LOGIN', '<font color=\"blue\"><b>'||name_op_action_type||'</b></font>', " +
	        "               			   'REPORT_EXECUTION', '<font color=\"green\"><b>'||name_op_action_type||'</b></font>', " +
	        "               			   'UPDATE_CLIENT_PARAM', '<font color=\"red\"><b>'||name_op_action_type||'</b></font>', " +
	        "               			   'UPDATE_CONTACT_PRS_PARAM', '<font color=\"red\"><b>'||name_op_action_type||'</b></font>', " +
	        "               			   name_op_action_type" +
	        "        		 		) name_op_action_type, " +
			"						cd_report||'. '||name_report name_report, " +
			"        		 		DECODE(cd_op_action_state, " +
	        "               			   'NEW', '<font color=\"blue\"><b>'||name_op_action_state||'</b></font>', " +
	        "               			   'IN_PROCESS', '<font color=\"gray\"><b>'||name_op_action_state||'</b></font>', " +
	        "               			   'CREATED', '<font color=\"green\"><b>'||name_op_action_state||'</b></font>', " +
	        "               			   'ERROR', '<font color=\"red\"><b>'||name_op_action_state||'</b></font>', " +
	        "               			   name_op_action_type" +
	        "        		 		) name_op_action_state, " +
			"						error_op_action, id_office_private, id_report " +
			"                  FROM " + getGeneralDBScheme() + ".vc_op_action_client_club_all " +
			"                 WHERE card_serial_number = ? " +
	        "                   AND card_id_issuer = ? " + 
	        "                   AND card_id_payment_system = ? ";
    	pParam.add(new bcFeautureParam("string", this.cardSerialNumber));
    	pParam.add(new bcFeautureParam("int", this.idIssuer));
    	pParam.add(new bcFeautureParam("int", this.idPaymentSystem));
    	
		if (!isEmpty(pFind)) {
			mySQL = mySQL + 
				" AND (UPPER(TO_CHAR(id_op_action)) LIKE UPPER('%'||?||'%') OR " +
				"	     UPPER(sname_office_private) LIKE UPPER('%'||?||'%') OR " +
				"	     UPPER(date_op_action_frmt) LIKE UPPER('%'||?||'%') OR " +
				"	     UPPER(cd_report||'. '||name_report) LIKE UPPER('%'||?||'%') OR " +
				"	     UPPER(error_op_action) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<5; i++) {
			    pParam.add(new bcFeautureParam("string", pFind));
			}
		}
		if (!isEmpty(pActionState)) {
			mySQL = mySQL + " AND cd_op_action_state = ? ";
			pParam.add(new bcFeautureParam("string", pFind));
		}
		if (!isEmpty(pActionType)) {
			mySQL = mySQL + " AND cd_op_action_type = ? ";
			pParam.add(new bcFeautureParam("string", pFind));
		}
		pParam.add(new bcFeautureParam("int", p_end));
		pParam.add(new bcFeautureParam("int", p_beg));
	    mySQL = mySQL + 
			"                 ORDER BY date_op_action DESC) "+
			"         WHERE ROWNUM < ? " + 
			" ) WHERE rn >= ?";
        

        boolean hasOfficePrivatePermission = false;
        boolean hasOfficePrivateClientActionPermission = false;
        try{
        	if (isEditMenuPermited("PRIVATE_OFFICE_PLACES")>=0) {
        		hasOfficePrivatePermission = true;
        	}
        	if (isEditMenuPermited("PRIVATE_OFFICE_CLIENTS_ACTIONS")>=0) {
        		hasOfficePrivateClientActionPermission = true;
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());;
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-2; i++) {
            	html.append(getBottomFrameTableTH(contactXML, mtd.getColumnName(i)));
            }
            html.append("<th>&nbsp;</th>\n");
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i<=colCount-2; i++) {
                	if ("ID_OP_ACTION".equalsIgnoreCase(mtd.getColumnName(i)) && hasOfficePrivateClientActionPermission) {
                		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/private_office/clients/actionspecs.jsp?id="+rset.getString("ID_OP_ACTION"), "", ""));
           			} else if ("SNAME_OFFICE_PRIVATE".equalsIgnoreCase(mtd.getColumnName(i)) && hasOfficePrivatePermission) {
                		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/private_office/placespecs.jsp?id="+rset.getString("ID_OFFICE_PRIVATE"), "", ""));
           			} else {
           				html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), mtd.getColumnType(i), rset.getString(i), "", "", ""));
           			}
                }
                String myHyperLink = getContextPath()+"../crm/cards/clubcardupdate.jsp?card_serial_number="+this.cardSerialNumber+"&id_issuer="+this.idIssuer+"&id_payment_system="+this.idPaymentSystem+"&id_op_action="+rset.getString("ID_OP_ACTION")+"&type=office";
	           	html.append(getEditButtonHTML(myHyperLink, getLanguage()));
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
    } //getAssignTermHistoryHTML*/
    
    public String getCardTransWEBClientHTML(String pFind, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	PreparedStatement st = null;
        Connection con = null;
        String mySQL = "";
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        try{
        	mySQL = 
        		" SELECT rn, sys_date_frmt, " +
	   			"        id_term, cd_currency, nt_icc,  " +
		    	"		 opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
		    	"        sum_bon_frmt, sum_disc_frmt " +
	            " FROM (SELECT ROWNUM rn, id_trans, id_telgr, " +
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
	   			"          	   id_term, cd_currency, nt_icc, action,  " +
		    	"		       opr_sum_frmt, sum_pay_cash_frmt, sum_pay_card_frmt, sum_pay_bon_frmt, " +
		    	"              sum_bon_frmt, sum_disc_frmt " +
	            " 		  FROM (SELECT * " +
	            "           	  FROM " + getGeneralDBScheme() + ".vc_trans_all " +
	            "                WHERE card_serial_number = ? " +
	            "                  AND id_issuer = ? " + 
	            "                  AND id_payment_system = ? " +
	            "                  AND type_trans in (1, 6, 7) ";
        	pParam.add(new bcFeautureParam("string", this.cardSerialNumber));
        	pParam.add(new bcFeautureParam("int", this.idIssuer));
        	pParam.add(new bcFeautureParam("int", this.idPaymentSystem));
        	
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
	            "                ORDER BY nt_icc DESC, id_trans DESC) " +
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
            for (int i=1; i <= colCount; i++) {
            	html.append(getBottomFrameTableTH(transactionXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>");

            while (rset.next())
            {
            	html.append("<tr>\n");
                for (int i=1; i<=colCount; i++) {
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
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        return html.toString();
    } // getCardTransHTML
    
}
