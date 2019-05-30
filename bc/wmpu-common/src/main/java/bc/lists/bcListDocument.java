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


public class bcListDocument extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcListDocument.class);
	
	public bcListDocument() {
	}
	
	public String getDocumentsHTML(String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pFindString, String pDocumentType, String pRelationType, String pDocumentState, String pEditLink, String pCopyLink, String pDeleteLink, String p_beg, String p_end) {
		return getDocumentsHTMLBase(false, pWhereCause, pWhereValue, pFindString, pDocumentType, pRelationType, pDocumentState, pEditLink, pCopyLink, pDeleteLink, p_beg, p_end);
	}

	public String getDocumentsHTMLOnlySelect(String pFindString, String pDocumentType, String pRelationType, String pDocumentState, String pGoToLink, String p_beg, String p_end) {
		String pWhereCause = "";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	
    	return getDocumentsHTMLBase(true, pWhereCause, pWhereValue, pFindString, pDocumentType, pRelationType, pDocumentState, pGoToLink, "", "", p_beg, p_end);
	}

	public String getDocumentsHTMLBase(boolean pOnlySelect, String pWhereCause, ArrayList<bcFeautureParam> pWhereValue, String pFindString, String pDocumentType, String pRelationType, String pDocumentState, String pEditLink, String pCopyLink, String pDeleteLink, String p_beg, String p_end) {
		StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        ArrayList<bcFeautureParam> pParam = initParamArray();

        boolean hasDocPermission = false;
        boolean hasJurPrsPermission = false;
        
        String mySQL = 
        	" SELECT rn, number_doc, date_doc_frmt, name_doc, " +
        	"        parties_doc, name_doc_type, club_rel_type, name_doc_state, " +
        	"        id_jur_prs_party1, sname_jur_prs_party1, " +
        	"        id_jur_prs_party2, sname_jur_prs_party2, " +
        	"        id_jur_prs_party3, sname_jur_prs_party3, id_doc, full_doc "+
            "   FROM (SELECT ROWNUM rn, id_doc, name_doc_type, " +
            "                DECODE(cd_doc_state, " +
            "               		'SIGNED', '<font color=\"green\"><b>'||name_doc_state||'</b></font>', " +
            "               		'WORKING_OUT', '<font color=\"blue\"><b>'||name_doc_state||'</b></font>', " +
            "               		'FINISCHED', '<font color=\"red\">'||name_doc_state||'</font>', " +
            "               		name_doc_state" +
            "        		 ) name_doc_state, " +
            "				 number_doc, date_doc_frmt, name_doc, " +
            "                parties_doc," +
            "                name_club_rel_type club_rel_type," +
            "                id_jur_prs_party1, sname_jur_prs_party1, " +
        	"                id_jur_prs_party2, sname_jur_prs_party2, " +
        	"                id_jur_prs_party3, sname_jur_prs_party3, full_doc  "+
        	"			FROM (SELECT *" +
        	"                   FROM " + getGeneralDBScheme()+".vc_doc_priv_all " +
        	(isEmpty(pWhereCause)?" WHERE 1=1 ":pWhereCause);
        
	   	if (pWhereValue.size() > 0) {
	   		for(int counter=0; counter<=pWhereValue.size()-1;counter++){
	   			pParam.add(pWhereValue.get(counter));
	   		}
	   	}
    	if (!isEmpty(pDocumentType)) {
    		mySQL = mySQL + " AND cd_doc_type = ? ";
    		pParam.add(new bcFeautureParam("string", pDocumentType));
    	}
    	if (!isEmpty(pDocumentState)) {
    		mySQL = mySQL + " AND cd_doc_state = ? ";
    		pParam.add(new bcFeautureParam("string", pDocumentState));
    	}
    	if (!isEmpty(pRelationType)) {
    		mySQL = mySQL + " AND cd_club_rel_type = ?";
        	pParam.add(new bcFeautureParam("string", pRelationType));
    	}
    	if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(id_doc) like UPPER('%'||?||'%') OR " +
    			"      UPPER(full_doc) like UPPER('%'||?||'%') OR " +
    			"      UPPER(sname_jur_prs_party1) like UPPER('%'||?||'%') OR " +
    			"      UPPER(sname_jur_prs_party2) like UPPER('%'||?||'%') OR " +
    			"      UPPER(sname_jur_prs_party3) like UPPER('%'||?||'%') " +
    			") ";
    		for (int i=0; i<5; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}

        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
	        "                  ORDER BY number_doc )" + 
	    	"          WHERE ROWNUM < ?) " + 
	    	"  WHERE rn >= ?";
        try{
        	if (isEditMenuPermited("CLUB_DOCUMENTS")>=0) {
        		hasDocPermission = true;
        	}
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasJurPrsPermission = true;
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
            	html.append(getBottomFrameTableTH(documentXML, mtd.getColumnName(i)));
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
          	  	for (int i=1; i <= colCount-8; i++) {
          	  		if (("NUMBER_DOC".equalsIgnoreCase(mtd.getColumnName(i)) ||
          	  				"DATE_DOC_FRMT".equalsIgnoreCase(mtd.getColumnName(i)) ||
          	  				"NAME_DOC".equalsIgnoreCase(mtd.getColumnName(i))
          	  			) && hasDocPermission) {
          	  			if (!pOnlySelect) {
          	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/documentspecs.jsp?id="+rset.getString("ID_DOC"), "", ""));
          	  			} else {
          	  				html.append(getBottomFrameTableTDWithDiv(mtd.getColumnName(i), rset.getString(i), pEditLink+"&id_doc="+rset.getString("ID_DOC"), "", "", "div_data_detail"));
          	  			}
           	  		} else if ("PARTIES_DOC".equalsIgnoreCase(mtd.getColumnName(i)) && !pOnlySelect) {
           	  			if (hasJurPrsPermission) {
           	  				String partiesDoc = "";
           	  				if (!isEmpty(rset.getString("ID_JUR_PRS_PARTY1"))) {
           	  					partiesDoc = partiesDoc + "<div onclick=\"ajaxpage('../crm/clients/yurpersonspecs.jsp?id=" + rset.getString("ID_JUR_PRS_PARTY1") + "', 'div_main')\" class=\"div_table_element\">" + rset.getString("SNAME_JUR_PRS_PARTY1") + "</div>";
           	  				}
           	  				if (!isEmpty(rset.getString("ID_JUR_PRS_PARTY2"))) {
           	  					partiesDoc = partiesDoc + ", <div onclick=\"ajaxpage('../crm/clients/yurpersonspecs.jsp?id=" + rset.getString("ID_JUR_PRS_PARTY2") + "', 'div_main')\" class=\"div_table_element\">" + rset.getString("SNAME_JUR_PRS_PARTY2") + "</div>";
           	  				}
           	  				if (!isEmpty(rset.getString("ID_JUR_PRS_PARTY3"))) {
           	  					partiesDoc = partiesDoc + ", <div onclick=\"ajaxpage('../crm/clients/yurpersonspecs.jsp?id=" + rset.getString("ID_JUR_PRS_PARTY3") + "', 'div_main')\" class=\"div_table_element\">" + rset.getString("SNAME_JUR_PRS_PARTY3") + "</div>";
           	  				}
           	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), partiesDoc, "", "", ""));
           	  			} else {
           	  				html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
           	  			}
          	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
          	  		}
          	  	}
          	  	if (!isEmpty(pDeleteLink) && !pOnlySelect) {
            	   String myDeleteLink = pDeleteLink+"&id_doc="+rset.getString("ID_DOC");
                   html.append(getDeleteButtonHTML(myDeleteLink, documentXML.getfieldTransl("l_remove_doc", false), rset.getString("FULL_DOC")));
          	  	}
          	  	if (!isEmpty(pCopyLink) && !pOnlySelect) {
            	   String myCopyLink = pEditLink+"&id_doc="+rset.getString("ID_DOC");
            	   html.append(getCopyButtonStyleHTML(myCopyLink, "", "div_data_detail"));
                }
          	  	if (!isEmpty(pEditLink) && !pOnlySelect) {
             	   String myEditLink = pEditLink+"&id_doc="+rset.getString("ID_DOC");
             	   html.append(getEditButtonWitDivHTML(myEditLink, "div_data_detail"));
                }
          	  	if (pOnlySelect) {
              	   String mySelectLink = pEditLink+"&id_doc="+rset.getString("ID_DOC");
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
