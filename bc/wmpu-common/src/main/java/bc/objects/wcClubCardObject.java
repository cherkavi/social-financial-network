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

public class wcClubCardObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(wcClubCardObject.class);
	
	private String cardSerialNumber;
	private String idIssuer;
	private String idPaymentSystem;
	private String cdCard1;
	private boolean needCalcTasks;
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	public wcClubCardObject(String pCdCard1){
		this.cdCard1 = pCdCard1;
		this.needCalcTasks = false;
		getFeature();
	}
	
	public wcClubCardObject(String pCardSerialNumber, String pIdIssuer, String pIdPaymentSystem){
		this.cardSerialNumber = pCardSerialNumber;
		this.idIssuer = pIdIssuer;
		this.idPaymentSystem = pIdPaymentSystem;
		this.needCalcTasks = false;
		getFeature();
	}
	
	public wcClubCardObject(String pCardSerialNumber, String pIdIssuer, String pIdPaymentSystem, boolean pNeedCalcTasks){
		this.cardSerialNumber = pCardSerialNumber;
		this.idIssuer = pIdIssuer;
		this.idPaymentSystem = pIdPaymentSystem;
		this.needCalcTasks = pNeedCalcTasks;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = "";
		if (!isEmpty(this.cdCard1)) {
			featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vw$card_all WHERE cd_card1 = ?";
			
			bcFeautureParam[] array = new bcFeautureParam[1];
			array[0] = new bcFeautureParam("string", this.cdCard1);
			
			fieldHm = getFeatures2(featureSelect, array);
			
			this.cardSerialNumber = this.getValue("CARD_SERIAL_NUMBER");
			this.idIssuer = this.getValue("ID_ISSUER");
			this.idPaymentSystem = this.getValue("ID_PAYMENT_SYSTEM");
			
		} else {
			if (!(this.needCalcTasks)) {
				featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vw$card_all WHERE card_serial_number = ? AND id_issuer = ? AND id_payment_system = ?";
				
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
					" FROM " + getGeneralDBScheme() + ".vw$card_all a " +
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
    
    public String getWEBClientCardTransHTML(String pFind, String p_beg, String p_end) {
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
	            "           	  FROM " + getGeneralDBScheme() + ".vw$trans_all " +
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
    
    public String getWEBClientCardOperationHTML(String pFind, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	PreparedStatement st = null;
        Connection con = null;
        String mySQL = "";
        
        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        try{
        	mySQL = 
        		" SELECT rn, date_card_oper_online_frmt, name_card_oper_online_state," +
        		"        desc_card_oper_online, note_card_oper_online " +
	            " FROM (SELECT ROWNUM rn, cd_card1, " +
	            "              date_card_oper_online_frmt, " +
	            "              DECODE(cd_card_oper_online_state, " +
	    		"                       'EXECUTE',  '<font color=\"green\"><b>'||name_card_oper_online_state||'</b></font>', " +
	    		"                       'ERROR',  '<font color=\"red\"><b>'||name_card_oper_online_state||'</b></font>', " +
	    		"                       name_card_oper_online_state)  " +
	    		"              name_card_oper_online_state, " +
	   			"              desc_card_oper_online, " +
	   			"              note_card_oper_online " +
	            " 		  FROM (SELECT * " +
	            "           	  FROM " + getGeneralDBScheme() + ".vw$card_oper_online " +
	            "                WHERE card_serial_number = ? " +
	            "                  AND card_id_issuer = ? " + 
	            "                  AND card_id_payment_system = ? ";
        	pParam.add(new bcFeautureParam("string", this.cardSerialNumber));
        	pParam.add(new bcFeautureParam("int", this.idIssuer));
        	pParam.add(new bcFeautureParam("int", this.idPaymentSystem));
        	
            if (!isEmpty(pFind)) {
            	mySQL = mySQL + " AND (UPPER(cd_card1) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(date_card_oper_online_frmt) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(desc_card_oper_online) LIKE UPPER('%'||?||'%') OR " +
               		"UPPER(note_card_oper_online) LIKE UPPER('%'||?||'%')) ";
            	for (int i=0; i<3; i++) {
            	    pParam.add(new bcFeautureParam("string", pFind));
            	}
           	}
            pParam.add(new bcFeautureParam("int", p_end));
            pParam.add(new bcFeautureParam("int", p_beg));
            mySQL = mySQL +
	            "                ORDER BY date_card_oper_online DESC) " +
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
            	html.append(getBottomFrameTableTH(card_taskXML, mtd.getColumnName(i)));
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
    
}
