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


public class bcListFund extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcListFund.class);
	
	public bcListFund() {
	}

	public String getFundsHTML(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pNotVisibleData, String pFindString, String pEditLink, String pDeleteLink, String p_beg, String p_end) {
		StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        boolean hasPartnerPermission = false;
        boolean hasFundPermission = false;
        
        String mySQL = 
        	" SELECT rn, "+("PARTNER".equalsIgnoreCase(pNotVisibleData)?"":"sname_jur_prs, ")+"name_club_fund, sname_club_fund, desc_club_fund, " +
        	"        date_beg_frmt fund_date_beg, date_end_frmt fund_date_end, id_club_fund, id_jur_prs  " +
        	"   FROM (SELECT ROWNUM rn, sname_jur_prs, name_club_fund, sname_club_fund, desc_club_fund, " +
        	"                date_beg_frmt, date_end_frmt, id_club_fund, id_jur_prs " +
        	"   		  FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".vc_club_fund_club_all a " +
        	(isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause);
        
	   	if (pWhereValue.size() > 0) {
	   		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
	   			pParam.add(pWhereValue.get(counter));
	   		}
	   	}
	        
	   	if (!isEmpty(pFindString)) {
        	mySQL = mySQL + " AND (TO_CHAR(id_club_fund) LIKE UPPER('%'||?||'%') OR " +
				"UPPER(name_club_fund) LIKE UPPER('%'||?||'%') OR " +
				"UPPER(sname_club_fund) LIKE UPPER('%'||?||'%') OR " + 
				"UPPER(desc_club_fund) LIKE UPPER('%'||?||'%') OR " + 
				"UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<5; i++) {
			    pParam.add(new bcFeautureParam("string", pFindString));
			}
    	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "   ORDER BY sname_jur_prs, name_club_fund)  WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        try{
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
            }
        	if (isEditMenuPermited("CLUB_FUND")>=0) {
        		hasFundPermission = true;
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
            	html.append(getBottomFrameTableTH(clubfundXML, mtd.getColumnName(i)));
            }
            if (!isEmpty(pDeleteLink)) {
                html.append("<th>&nbsp;</th>\n");
             }
            if (!isEmpty(pEditLink)) {
               html.append("<th>&nbsp;</th>\n");
            }
            html.append("</tr></thead><tbody>");
            
            while (rset.next())
            {
            	html.append("<tr>");
          	  	for (int i=1; i <= colCount-3; i++) {
          	  		if ("NAME_CLUB_FUND".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			hasFundPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/fundspecs.jsp?id="+rset.getString("ID_CLUB_FUND"), "", ""));
          	  		} else if ("SNAME_JUR_PRS".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			hasPartnerPermission) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
          	  	if (!isEmpty(pDeleteLink)) {
            	   String myDeleteLink = pDeleteLink+"&id_club_fund="+rset.getString("ID_CLUB_FUND");
                   html.append(getDeleteButtonHTML(myDeleteLink, clubfundXML.getfieldTransl("h_delete_club_fund", false), rset.getString("NAME_CLUB_FUND")));
          	  	}
          	  	if (!isEmpty(pEditLink)) {
            	   String myEditLink = pEditLink+"&id_club_fund="+rset.getString("ID_CLUB_FUND");
            	   html.append(getEditButtonWitDivHTML(myEditLink, "div_data_detail"));
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
