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


public class bcListLoyalityScheme extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcListLoyalityScheme.class);
	
	public bcListLoyalityScheme() {
	}
	
	public String getLoyalitySchemesHTML(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pNotVisibleData, String pFindString, String pCdKindLoyality, String pEditLink, String pCopyLink, String pDeleteLink, String p_beg, String p_end) {
		return getLoyalitySchemesHTMLBase(false, pWhereCause, pWhereValue, pNotVisibleData, pFindString, pCdKindLoyality, pEditLink, pCopyLink, pDeleteLink, p_beg, p_end);
	}
	
	public String getLoyalitySchemesHTMLOnlySelect(String pFindString, String pCdKindLoyality, String pGoToLink, String p_beg, String p_end) {
		String pWhereCause = "";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	
		return getLoyalitySchemesHTMLBase(true, pWhereCause, pWhereValue, "", pFindString, pCdKindLoyality, pGoToLink, "", "", p_beg, p_end);
	}

	public String getLoyalitySchemesHTMLBase(boolean pOnlySelect, String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pNotVisibleData, String pFindString, String pCdKindLoyality, String pEditLink, String pCopyLink, String pDeleteLink, String p_beg, String p_end) {
		StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        boolean hasPartnerPermission = false;
        boolean hasLoyalitySchemePermission = false;
        
        String mySQL = 
        	" SELECT rn, "+("PARTNER".equalsIgnoreCase(pNotVisibleData)?"":"sname_jur_prs name_partner, ")+" name_loyality_scheme title_loyality_scheme, " +
            "		 /*desc_loyality_scheme,*/ cd_kind_loyality||' - '||name_kind_loyality name_kind_loyality, " +
            "		 date_beg_frmt, date_end_frmt, id_loyality_scheme, id_jur_prs " +
            "   FROM (SELECT ROWNUM rn, id_loyality_scheme, name_loyality_scheme, " +
            "				 desc_loyality_scheme, cd_kind_loyality, name_kind_loyality, sname_jur_prs, " +
            "				 date_beg_frmt, date_end_frmt, id_jur_prs " +
            "           FROM (SELECT * " +
            "                   FROM " + getGeneralDBScheme()+".vc_loyality_scheme_club_all " +
        	(isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause);
        
	   	if (pWhereValue.size() > 0) {
	   		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
	   			pParam.add(pWhereValue.get(counter));
	   		}
	   	}
        
	   	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
    			" AND (UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(name_loyality_scheme) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(desc_loyality_scheme) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%') OR " + 
    			"      UPPER(date_end_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
	   	if (!isEmpty(pCdKindLoyality)) {
        	mySQL = mySQL + " AND cd_kind_loyality = ? ";
        	pParam.add(new bcFeautureParam("string", pCdKindLoyality));
        }

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "   ORDER BY sname_jur_prs, name_loyality_scheme)  WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        try{
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
            }
        	if (isEditMenuPermited("CLIENTS_LOY")>=0) {
        		hasLoyalitySchemePermission = true;
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
            	html.append(getBottomFrameTableTH(loyXML, mtd.getColumnName(i)));
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
          	  	for (int i=1; i <= colCount-2; i++) {
          	  		if ("TITLE_LOYALITY_SCHEME".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			hasLoyalitySchemePermission) {
          	  			if (!pOnlySelect) {
          	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/loyspecs.jsp?id="+rset.getString("ID_LOYALITY_SCHEME"), "", ""));
          	  			} else {
          	  				html.append(getBottomFrameTableTDWithDiv(mtd.getColumnName(i), rset.getString(i), pEditLink+"&id_loyality_scheme="+rset.getString("ID_LOYALITY_SCHEME"), "", "", "div_data_detail"));
          	  			}
          	  		} else if ("NAME_PARTNER".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			hasPartnerPermission && !pOnlySelect) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
          	  	if (!isEmpty(pDeleteLink) && !pOnlySelect) {
            	   String myDeleteLink = pDeleteLink+"&id_loyality_scheme="+rset.getString("ID_LOYALITY_SCHEME");
                   html.append(getDeleteButtonHTML(myDeleteLink, loyXML.getfieldTransl("H_LOYALITY_DELETE", false), rset.getString("TITLE_LOYALITY_SCHEME")));
          	  	}
          	  	if (!isEmpty(pCopyLink) && !pOnlySelect) {
            	   String myCopyLink = pEditLink+"&id_loyality_scheme="+rset.getString("ID_LOYALITY_SCHEME");
            	   html.append(getCopyButtonStyleHTML(myCopyLink, "", "div_data_detail"));
                }
          	  	if (!isEmpty(pEditLink) && !pOnlySelect) {
             	   String myEditLink = pEditLink+"&id_loyality_scheme="+rset.getString("ID_LOYALITY_SCHEME");
             	   html.append(getEditButtonWitDivHTML(myEditLink, "div_data_detail"));
                }
          	  	if (pOnlySelect) {
              	   String mySelectLink = pEditLink+"&id_loyality_scheme="+rset.getString("ID_LOYALITY_SCHEME");
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
