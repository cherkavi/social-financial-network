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


public class bcListTargetProgram extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcListTargetProgram.class);
	
	public bcListTargetProgram() {
	}
	
	public String getTargetProgramsHTML(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pNotVisibleData, String pFindString, String pPayPeriod, String pEditLink, String pCopyLink, String pDeleteLink, String p_beg, String p_end) {
		return getTargetProgramsHTMLBase(false, pWhereCause, pWhereValue, pNotVisibleData, pFindString, pPayPeriod, pEditLink, pCopyLink, pDeleteLink, p_beg, p_end);
	}
	
	public String getTargetProgramsHTMLOnlySelect(String pFindString, String pPayPeriod, String pGoToLink, String p_beg, String p_end) {
		String pWhereCause = "";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	
    	return getTargetProgramsHTMLBase(true, pWhereCause, pWhereValue, "", pFindString, pPayPeriod, pGoToLink, "", "", p_beg, p_end);
	}

	public String getTargetProgramsHTMLBase(boolean pOnlySelect, String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pNotVisibleData, String pFindString, String pPayPeriod, String pEditLink, String pCopyLink, String pDeleteLink, String p_beg, String p_end) {
		StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        boolean hasPartnerPermission = false;
        boolean hasTargetProgramPermission = false;
        
        String mySQL = 
        	" SELECT rn, "+("PARTNER".equalsIgnoreCase(pNotVisibleData)?"":"sname_jur_prs, ")+"name_target_prg, " +
        	"        name_target_prg_pay_period target_prg_pay_period, " +
        	"        pay_amount_full_frmt pay_amount_short_frmt, desc_target_prg, " +
        	"        /*sname_club,*/ date_beg_frmt target_program_date_beg, " +
        	"        date_end_frmt target_program_date_end, " +
        	"        id_target_prg, id_jur_prs, name_target_prg_initial " +
        	"   FROM (SELECT ROWNUM rn, id_jur_prs, sname_jur_prs, id_target_prg, " +
        	"                '<span>'||LPAD(' ', ((level_target_prg-1)*4*6), '&nbsp;')||name_target_prg||'</span>' name_target_prg, desc_target_prg, " +
        	"                name_target_prg_pay_period, " +
        	"                DECODE(cd_target_prg_pay_period, NULL, '', pay_amount_full_frmt) pay_amount_full_frmt, sname_club, date_beg_frmt, date_end_frmt, " +
        	"                level_target_prg, name_target_prg_initial " +
        	"   		  FROM (SELECT order_rn, id_jur_prs, sname_jur_prs, id_target_prg, " +
        	"                          CASE WHEN id_target_prg_parent IS NULL " +
        	"                               THEN '<b><font color=\"green\">'||name_target_prg||'</font></b>'" +
        	"                               ELSE name_target_prg" +
        	"                          END name_target_prg, desc_target_prg, " +
        	"                          cd_target_prg_pay_period, " +
        	"                          CASE WHEN NVL(child_count,0) > 0 THEN '' ELSE name_target_prg_pay_period END name_target_prg_pay_period, pay_amount_full_frmt, " +
        	"                          sname_club, date_beg_frmt, date_end_frmt, " +
        	"                          level_target_prg, name_target_prg name_target_prg_initial" +
        	"                    FROM (SELECT ROWNUM order_rn, id_jur_prs, sname_jur_prs, id_target_prg, name_target_prg, desc_target_prg, " +
        	"                                 DECODE(cd_target_prg_pay_period, " +
            "               						 'IRREGULAR', '<font color=\"red\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 'HOUR', '<font color=\"green\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 'DAY', '<font color=\"green\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 'WEEK', '<font color=\"green\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 'MONTH', '<font color=\"green\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 'STUDY_COUNT', '<font color=\"blue\"><b>'||name_target_prg_pay_period||'</b></font>', " +
            "               						 name_target_prg_pay_period" +
            "        		 		 		  ) name_target_prg_pay_period, pay_amount_full_frmt, " +
        	"                                 cd_target_prg_pay_period, sname_club, date_beg_frmt, " +
        	"                                 date_end_frmt, id_club, id_target_prg_parent, " +
        	"                                 LEVEL level_target_prg, child_count " +
        	"                            FROM " + getGeneralDBScheme()+".vc_target_prg_club_all " +
        	(isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause) +
        	"                           START WITH id_target_prg_parent IS NULL " +
        	"                         CONNECT BY PRIOR id_target_prg = id_target_prg_parent " +
        	"                           ORDER siblings BY id_club, name_target_prg, id_target_prg) ";
        
	   	if (pWhereValue.size() > 0) {
	   		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
	   			pParam.add(pWhereValue.get(counter));
	   		}
	   	}
	        
	   	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
    			" AND (UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(name_target_prg) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(pay_amount_full_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(date_beg_frmt) LIKE UPPER('%'||?||'%') OR " +
    			"      UPPER(date_end_frmt) LIKE UPPER('%'||?||'%')) ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
	   	if (!isEmpty(pPayPeriod)) {
    		mySQL = mySQL + " AND cd_target_prg_pay_period = ? ";
    		pParam.add(new bcFeautureParam("string", pPayPeriod));
    	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "   ORDER BY sname_jur_prs, name_target_prg)  WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        try{
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
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
            for (int i=1; i <= colCount-3; i++) {
            	html.append(getBottomFrameTableTH(clubfundXML, mtd.getColumnName(i)));
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
          	  	for (int i=1; i <= colCount-3; i++) {
          	  		if ("NAME_TARGET_PRG".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			hasTargetProgramPermission) {
          	  			if (!pOnlySelect) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/target_programspecs.jsp?id="+rset.getString("ID_TARGET_PRG"), "", ""));
          	  			} else {
          	  				html.append(getBottomFrameTableTDWithDiv(mtd.getColumnName(i), rset.getString(i), pEditLink+"&id_target_prg="+rset.getString("ID_TARGET_PRG"), "", "", "div_data_detail"));
          	  			}
          	  		} else if ("SNAME_JUR_PRS".equalsIgnoreCase(mtd.getColumnName(i)) &&
          	  			hasPartnerPermission && !pOnlySelect) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS"), "", ""));
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
          	  	if (!isEmpty(pDeleteLink) && !pOnlySelect) {
            	   String myDeleteLink = pDeleteLink+"&id_target_prg="+rset.getString("ID_TARGET_PRG");
                   html.append(getDeleteButtonHTML(myDeleteLink, clubfundXML.getfieldTransl("h_delete_target_program", false), rset.getString("NAME_TARGET_PRG_INITIAL")));
          	  	}
          	  	if (!isEmpty(pCopyLink) && !pOnlySelect) {
              	   String myCopyLink = pEditLink+"&id_target_prg="+rset.getString("ID_TARGET_PRG");
              	   html.append(getCopyButtonStyleHTML(myCopyLink, "", "div_data_detail"));
                }
          	  	if (!isEmpty(pEditLink) && !pOnlySelect) {
            	   String myEditLink = pEditLink+"&id_target_prg="+rset.getString("ID_TARGET_PRG");
            	   html.append(getEditButtonWitDivHTML(myEditLink, "div_data_detail"));
                }
          	  	if (pOnlySelect) {
              	   String mySelectLink = pEditLink+"&id_target_prg="+rset.getString("ID_TARGET_PRG");
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
