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
import bc.lists.bcListCardPackage;
import bc.service.bcFeautureParam;


public class bcCardSettingObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcCardSettingObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idCardStatus;
	private String idClub;
	
	public bcCardSettingObject(String pIdCardStatus, String pIdClub) {
		this.idCardStatus = pIdCardStatus;
		this.idClub = pIdClub;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = 
			" SELECT * FROM " + getGeneralDBScheme() + ".VC_CARD_STATUS_CLUB_ALL WHERE id_card_status = ? AND id_club = ?";
		
		bcFeautureParam[] array = new bcFeautureParam[2];
		array[0] = new bcFeautureParam("int", this.idCardStatus);
		array[1] = new bcFeautureParam("int", this.idClub);

		fieldHm = getFeatures2(mySQL, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

    public String getCategoriesHTML(String pFind, String p_beg, String p_end) {
	    StringBuilder html = new StringBuilder();
	    Connection con = null;
	    PreparedStatement st = null;
	    boolean hasEditPermission = false;
	    
	    ArrayList<bcFeautureParam> pParam = initParamArray();
		
	    String mySQL = 
	    	" SELECT rn, name_category, club_bon_frmt, max_bon_st_frmt, bonus_transfer_term, " +
			"        club_disc_frmt, max_disc_st_frmt, bonus_calc_term, id_club_src, id_club, id_category " +
			"   FROM (SELECT ROWNUM rn, '<font color=\"blue\"><b>'||TO_CHAR(id_club)||'<b></font>' id_club, " +
			"				 '<b><font color=\"green\">'||name_category||'</font></b>' name_category, " +
			"                CASE WHEN club_bon > 0 " +
			"                     THEN '<b><font color=\"blue\">'||club_bon_frmt||'</font></b>'" +
			"                     ELSE '<b><font color=\"red\">'||club_bon_frmt||'</font></b>' " +
			"                END club_bon_frmt, " +
			"                max_bon_st_frmt, bonus_transfer_term, " +
			"                CASE WHEN club_disc > 0 " +
			"                     THEN '<b><font color=\"blue\">'||club_disc_frmt||'</font></b>'" +
			"                     ELSE '<b><font color=\"red\">'||club_disc_frmt||'</font></b>' " +
			"                END club_disc_frmt, " +
			"                max_disc_st_frmt, bonus_calc_term, id_category, id_club id_club_src " +
			"           FROM (SELECT * " +
			"                   FROM " + getGeneralDBScheme()+".vc_card_category_club_all a " +
			"                  WHERE id_card_status = ?" +  
			"                    AND id_club = ?";
	    
	    pParam.add(new bcFeautureParam("int", this.idCardStatus));
	    pParam.add(new bcFeautureParam("int", this.idClub));
	    
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    				" AND (TO_CHAR(id_club) LIKE UPPER('%'||?||'%') OR " +
    				"  TO_CHAR(id_category) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(name_category) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(club_bon_frmt) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(max_bon_st_frmt) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(bonus_transfer_term) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(club_disc_frmt) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(max_disc_st_frmt) LIKE UPPER('%'||?||'%') OR " +
    				"  UPPER(bonus_calc_term) LIKE UPPER('%'||?||'%'))";
    		for(int i=0; i<9; i++) {
    			pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
	    mySQL = mySQL + 
	    	" 				   ORDER BY a.id_club, a.name_category) " +
	    	"          WHERE ROWNUM < ?" + 
	    	"		 ) " +
	    	"  WHERE rn >= ?";
	    pParam.add(new bcFeautureParam("int", p_end));
	    pParam.add(new bcFeautureParam("int", p_beg));
	    try{
	    	if (isEditPermited("CARDS_CARDSETTINGS_CATEGORIES")>0) {
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
	        for (int i=1; i <= colCount-3; i++) {
	        	html.append(getBottomFrameTableTH(cardsettingXML, mtd.getColumnName(i)));
	        }
	        if (hasEditPermission) {
	        	html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
	        }
	        html.append("</tr></thead><tbody>\n");
	        while (rset.next()) {
	        	html.append("<tr>");
	          	for (int i=1; i <= colCount-3; i++) {
	          		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          	}
	            if (hasEditPermission) {
	            	String myHyperLink = "../crm/cards/cardsettingupdate.jsp?id="+this.idCardStatus+"&id_category="+rset.getString("ID_CATEGORY")+"&id_club="+rset.getString("ID_CLUB_SRC")+"&type=category";
	            	String myDeleteLink = "../crm/cards/cardsettingupdate.jsp?id="+this.idCardStatus+"&id_category="+rset.getString("ID_CATEGORY")+"&id_club="+rset.getString("ID_CLUB_SRC")+"&type=category&action=remove&process=yes";
	                html.append(getDeleteButtonHTML(myDeleteLink, cardsettingXML.getfieldTransl("h_delete_category", false), rset.getString("ID_CATEGORY")));
	            	html.append(getEditButtonHTML(myHyperLink));
	            }
	            html.append("</tr>\n");
	        }
	        html.append("</tbody></table>\n");
	    } // try
	    catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
	    // catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
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
	} // getJurPersonServicePlacesHTML

    public String getCardPackagesHTML(String pFindString, String p_beg, String p_end) {
    	bcListCardPackage list = new bcListCardPackage();
    	
    	String pWhereCause = " WHERE id_card_status = ? AND id_club = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idCardStatus));
    	pWhereValue.add(new bcFeautureParam("int", this.idClub));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	String myCopyLink = "";
    	if (isEditPermited("CARDS_CARDSETTINGS_CARD_PACKAGES")>0) {
    		myDeleteLink = "../crm/club/card_packageupdate.jsp?back_type=CARD_SETTING&type=general&id="+this.idCardStatus+"&id_club="+this.idClub+"&action=remove&process=yes";
    	    myEditLink = "../crm/club/card_packageupdate.jsp?back_type=CARD_SETTING&type=general&id="+this.idCardStatus+"&id_club="+this.idClub;
    	    myCopyLink = "../crm/club/card_packageupdate.jsp?back_type=CARD_SETTING&type=general&id="+this.idCardStatus+"&id_club="+this.idClub;
    	}
    	
    	return list.getCardPackagesHTML(pWhereCause, pWhereValue, "", pFindString, "", myEditLink, myCopyLink, myDeleteLink, p_beg, p_end);
    	
    }
	
}
