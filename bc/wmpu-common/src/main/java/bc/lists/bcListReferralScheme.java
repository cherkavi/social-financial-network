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


public class bcListReferralScheme extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcListReferralScheme.class);
	
	public bcListReferralScheme() {
	}
	
	public String getReferralSchemesHTML(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pFindString, String pSchemeType, String pCalcType, String pEditLink, String pCopyLink, String pDeleteLink, String p_beg, String p_end) {
		return getReferralSchemesHTMLBase(false, pWhereCause, pWhereValue, "", pFindString, pSchemeType, pCalcType, pEditLink, pCopyLink, pDeleteLink, p_beg, p_end);
	}

	public String getReferralSchemesHTMLOnlySelect(String pFindString, String pSchemeType, String pCalcType, String pGoToLink, String p_beg, String p_end) {
		String pWhereCause = "";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	
    	return getReferralSchemesHTMLBase(true, pWhereCause, pWhereValue, "", pFindString, pSchemeType, pCalcType, pGoToLink, "", "", p_beg, p_end);
	}

	public String getReferralSchemesHTMLBase(boolean pOnlySelect, String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pNotVisibleData, String pFindString, String pSchemeType, String pCalcType, String pEditLink, String pCopyLink, String pDeleteLink, String p_beg, String p_end) {
		StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        boolean hasPartnerPermission = false;
        boolean hasTargetProgramPermission = false;
        boolean hasReferralSchemePermission = false;
        
        String mySQL = 
        	" SELECT rn, " +
        	"        DECODE(cd_referral_scheme_type," +
        	"               'TARGET_PROGRAM', '<font color=\"blue\"><b>'||name_referral_scheme_type||'<b></font>', " +
            "               'PAYMENT', '<font color=\"green\"><b>'||name_referral_scheme_type||'<b></font>', " +
            "               name_referral_scheme_type" +
        	"        ) name_referral_scheme_type, " +
        	"        DECODE(cd_referral_scheme_type," +
        	"               'TARGET_PROGRAM', name_target_prg, " +
            "               'PAYMENT', sname_jur_prs, " +
            "               ''" +
        	"        ) jur_prs_and_target_prg, name_referral_scheme, desc_referral_scheme, " +
        	"        DECODE(cd_referral_scheme_calc_type," +
        	"               'POINT_SUM', '<font color=\"blue\"><b>'||name_referral_scheme_calc_type||'<b></font>', " +
            "               'CLUB_SUM', '<font color=\"green\"><b>'||name_referral_scheme_calc_type||'<b></font>', " +
            "               name_referral_scheme_calc_type" +
        	"        ) name_referral_scheme_calc_type, " +
        	"		 accounting_level_count, accounting_percent_all_frmt, " +
        	"        id_referral_scheme, id_jur_prs, id_target_prg, cd_referral_scheme_type " +
        	"   FROM (SELECT ROWNUM rn, sname_jur_prs, name_target_prg, name_referral_scheme, desc_referral_scheme, cd_referral_scheme_type, name_referral_scheme_type, " +
        	"		         cd_referral_scheme_calc_type, name_referral_scheme_calc_type, " +
        	"                accounting_level_count, accounting_percent_all_frmt, " +
        	"                id_referral_scheme, id_jur_prs, id_target_prg " +
        	"   	    FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".vc_referral_scheme_club_all " +
        	(isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause);
        
	   	if (pWhereValue.size() > 0) {
	   		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
	   			pParam.add(pWhereValue.get(counter));
	   		}
	   	}
	        
	   	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
    			" AND (UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(name_referral_scheme) LIKE UPPER('%'||?||'%') OR " +
    			"	   UPPER(desc_referral_scheme) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<3; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
        if (!isEmpty(pSchemeType)) {
    		mySQL = mySQL + " AND cd_referral_scheme_type = ? ";
    		pParam.add(new bcFeautureParam("string", pSchemeType));
    	}
        if (!isEmpty(pCalcType)) {
    		mySQL = mySQL + " AND cd_referral_scheme_calc_type = ? ";
    		pParam.add(new bcFeautureParam("string", pCalcType));
    	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "   ORDER BY DECODE(cd_referral_scheme_type," +
        	"               'TARGET_PROGRAM', name_target_prg, " +
            "               'PAYMENT', sname_jur_prs, " +
            "               ''" +
        	"        ), name_referral_scheme)  WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        try{
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
            }
        	if (isEditMenuPermited("CRM_CLUB_REFERRAL_SCHEME")>=0) {
        		hasReferralSchemePermission = true;
            }
        	if (isEditMenuPermited("CLUB_TARGET_PROGRAM")>=0) {
        		hasTargetProgramPermission = true;
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
            for (int i=1; i <= colCount-4; i++) {
            	html.append(getBottomFrameTableTH(clubXML, mtd.getColumnName(i)));
            }
            if (!isEmpty(pDeleteLink) && !pOnlySelect) {
                html.append("<th>&nbsp;</th>\n");
            }
            if (!isEmpty(pCopyLink) && !pOnlySelect) {
                html.append("<th>&nbsp;</th>\n");
            }
            if (!isEmpty(pEditLink) && !pOnlySelect) {
               html.append("<th>&nbsp;</th>\n");
            }
            if (pOnlySelect) {
                html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-4; i++) {
          	  		if ("NAME_REFERRAL_SCHEME".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			hasReferralSchemePermission) {
          	  			if (!pOnlySelect) {
          	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/referral_schemespecs.jsp?id="+rset.getString("ID_REFERRAL_SCHEME"), "", ""));
          	  			} else {
          	  				html.append(getBottomFrameTableTDWithDiv(mtd.getColumnName(i), rset.getString(i), pEditLink+"&id_referral_scheme="+rset.getString("ID_REFERRAL_SCHEME"), "", "", "div_data_detail"));
          	  			}
           	  		} else if ("JUR_PRS_AND_TARGET_PRG".equalsIgnoreCase(mtd.getColumnName(i)) && !pOnlySelect) {
	          	  		if ("TARGET_PROGRAM".equalsIgnoreCase(rset.getString("CD_REFERRAL_SCHEME_TYPE")) &&
	          	  			hasTargetProgramPermission &&
	          	  				!isEmpty(rset.getString("ID_TARGET_PRG"))) {
	          	  			html.append(getBottomFrameTableTD("JUR_PRS_AND_TARGET_PRG", rset.getString("JUR_PRS_AND_TARGET_PRG"), "../crm/club/target_programspecs.jsp?id="+rset.getString("ID_TARGET_PRG"), "", ""));
	          	  		} else if ("PAYMENT".equalsIgnoreCase(rset.getString("CD_REFERRAL_SCHEME_TYPE")) &&
		          	  			hasPartnerPermission &&
		          	  			!isEmpty(rset.getString("ID_JUR_PRS"))) {
		          	  		html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS"), "", ""));
	          	  		} else {
	          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	          	  		}
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
          	  	if (!isEmpty(pDeleteLink) && !pOnlySelect) {
            	   String myDeleteLink = pDeleteLink+"&id_referral_scheme="+rset.getString("ID_REFERRAL_SCHEME");
                   html.append(getDeleteButtonHTML(myDeleteLink, clubXML.getfieldTransl("h_delete_referral_scheme", false), rset.getString("NAME_REFERRAL_SCHEME")));
          	  	}
          	  	if (!isEmpty(pCopyLink) && !pOnlySelect) {
            	   String myCopyLink = pEditLink+"&id_referral_scheme="+rset.getString("ID_REFERRAL_SCHEME");
            	   html.append(getCopyButtonStyleHTML(myCopyLink, "", "div_data_detail"));
                }
          	  	if (!isEmpty(pEditLink) && !pOnlySelect) {
             	   String myEditLink = pEditLink+"&id_referral_scheme="+rset.getString("ID_REFERRAL_SCHEME");
             	   html.append(getEditButtonWitDivHTML(myEditLink, "div_data_detail"));
                }
          	  	if (pOnlySelect) {
              	   String mySelectLink = pEditLink+"&id_referral_scheme="+rset.getString("ID_REFERRAL_SCHEME");
              	   html.append(getSelectButtonStyleHTML(mySelectLink, "", "div_data_detail"));
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
