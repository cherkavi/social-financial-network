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
import bc.lists.bcListCardOperation;
import bc.service.bcFeautureParam;

public class bcReferralSchemeObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcReferralSchemeObject.class);
	
	private String idReferralScheme;
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	public bcReferralSchemeObject(String pIdReferralScheme){
		this.idReferralScheme = pIdReferralScheme;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vc_referral_scheme_club_all WHERE id_referral_scheme = ?";
				
		bcFeautureParam[] array = new bcFeautureParam[1];
		array[0] = new bcFeautureParam("int", this.idReferralScheme);
		
		fieldHm = getFeatures2(featureSelect, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getLinesHTML(String pCdLineType, String pFind, String p_beg, String p_end) {
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		"SELECT /*rn, */accounting_level accounting_level_frmt, accounting_percent, " +
    		"       name_referral_scheme_calc_type, name_referral_scheme_rec_type, " +
    		"       fio_nat_prs, cd_card1, id_referral_scheme_line, id_nat_prs," +
	  		"       card_serial_number, card_id_issuer, card_id_payment_system " +
	  		"  FROM (SELECT ROWNUM rn, " +
	  		"               DECODE(cd_referral_scheme_rec_type," +
	  		"                      'CASHIER', '<font color=\"blue\"><b>'||name_referral_scheme_rec_type||'</b></font>', " +
	  		"                      'SHAREHOLDER', '<font color=\"black\"><b>'||name_referral_scheme_rec_type||'</b></font>', " +
	  		"                      'SOCIETY', '<font color=\"red\"><b>'||name_referral_scheme_rec_type||'</b></font>', " +
	  		"                      '<font color=\"green\"><b>'||name_referral_scheme_rec_type||'</b></font>'" +
	  		"               ) name_referral_scheme_rec_type, " +
	  		"               DECODE(cd_referral_scheme_calc_type," +
	  		"                      'INPUT_SUM', '<font color=\"blue\"><b>'||name_referral_scheme_calc_type||'</b></font>', " +
	  		"                      'POINT_SUM', '<font color=\"green\"><b>'||name_referral_scheme_calc_type||'</b></font>', " +
	  		"                      name_referral_scheme_calc_type" +
	  		"               ) name_referral_scheme_calc_type, " +
	  		"               accounting_level, accounting_percent, fio_nat_prs, cd_card1, " +
	  		"               id_referral_scheme_line, id_nat_prs," +
	  		"               card_serial_number, card_id_issuer, card_id_payment_system " +
	  		"          FROM (SELECT cd_referral_scheme_rec_type, name_referral_scheme_rec_type, " +
	  		"                       cd_referral_scheme_calc_type, name_referral_scheme_calc_type, " +
	  		"                       accounting_level, accounting_percent_frmt accounting_percent, fio_nat_prs, cd_card1, " +
	  		"                       id_referral_scheme_line, id_nat_prs," +
	  		"                       card_serial_number, card_id_issuer, card_id_payment_system " +
            "				   FROM " + getGeneralDBScheme() + ".vc_referral_scheme_line_all " +
	  		"   WHERE id_referral_scheme = ? ";
    	pParam.add(new bcFeautureParam("int", this.idReferralScheme));
    	
    	if (!isEmpty(pCdLineType)) {
    		mySQL = mySQL + 
   				" AND cd_referral_scheme_line_type = ?";
    		pParam.add(new bcFeautureParam("string", pCdLineType));
    	}
    	
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
   				" AND (UPPER(accounting_level) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(accounting_percent) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(fio_nat_prs) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(cd_card1) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
  		mySQL = mySQL + 
  			"                 ORDER BY accounting_level, fio_nat_prs"+
  			"                ) " +
  			"         WHERE ROWNUM < ?" + 
  			") WHERE rn >= ?";
  		
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;
        boolean hasEditPermission = false;
        boolean hasNatPrsPermission = false;
        boolean hasCardPermission = false;
        try{
        	if (isEditPermited("CRM_CLUB_REFERRAL_SCHEME_LINES")>0) {
        		hasEditPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
        		hasNatPrsPermission = true;
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
            	html.append(getBottomFrameTableTH(clubXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n"); 
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                for (int i=1; i<=colCount-5; i++) {
          	  		if (hasCardPermission && "CD_CARD1".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  				!isEmpty(rset.getString("CD_CARD1"))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/cards/clubcardspecs.jsp?id="+rset.getString("CARD_SERIAL_NUMBER")+"&iss="+rset.getString("CARD_ID_ISSUER")+"&paysys="+rset.getString("CARD_ID_PAYMENT_SYSTEM"), "", ""));
         	  		} else if (hasNatPrsPermission && "FIO_NAT_PRS".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  				!isEmpty(rset.getString("ID_NAT_PRS"))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersons.jsp?id="+rset.getString("ID_NAT_PRS"), "", ""));
         	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
                }
	            if (hasEditPermission) {
	            	String myHyperLink = "../crm/club/referral_schemeupdate.jsp?id_referral_scheme="+this.idReferralScheme+"&id_referral_scheme_line="+rset.getString("ID_REFERRAL_SCHEME_LINE")+"&type=line";
	            	html.append(getDeleteButtonHTML(myHyperLink, "remove", "yes", "delete.png", clubXML.getfieldTransl("h_delete_referral_scheme_line", false) + " " + rset.getString("ACCOUNTING_LEVEL_FRMT")));
	            	html.append(getEditButtonWitDivHTML(myHyperLink, "div_data_detail"));
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
    } // getClubCardsTasksHTML
    
    public String getClubCardsTasksHTML(String pFindString, String pOperType, String pOperState, String p_beg, String p_end) {
    	bcListCardOperation list = new bcListCardOperation();
    	
    	String pWhereCause = "   WHERE id_referral_scheme = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idReferralScheme));

    	String lDeleteLink = "";
    	String lEditLink = "";
    	
    	return list.getCardOperBasedOnActionsHTML(pWhereCause, pWhereValue, pFindString, pOperType, pOperState, lEditLink, lDeleteLink, "div_data_detail", p_beg, p_end);
    	
    }
}
