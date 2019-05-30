package bc.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.connection.Connector;


public class bcFNPostingSchemeLineObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcFNPostingSchemeLineObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idOperSchemeLine;
	
	public bcFNPostingSchemeLineObject(String pIdOperSchemeLine) {
		this.idOperSchemeLine = pIdOperSchemeLine;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_OPER_SCHEME_LINES_CLUB_ALL WHERE id_bk_operation_scheme_line = ?";
		fieldHm = getFeatures2(featureSelect, this.idOperSchemeLine, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
    public String getClubRelationshipsHTML(String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
    	
        String mySQL = 
	        " SELECT rn, id_club_rel, date_club_rel, name_club_rel_type,  " +
	  	  	"        sname_party1_full, sname_party2_full, payment_function, exist_flag, id_club_rel_oper_scheme " +
	  	  	"   FROM (SELECT ROWNUM rn, id_club_rel, name_club_rel_type, date_club_rel_frmt date_club_rel, " +
	  	  	"                desc_club_rel, sname_party1 sname_party1_full, " +
	  	  	"                sname_party2 sname_party2_full, payment_function, exist_flag, id_club_rel_oper_scheme " +
	       	"           FROM (SELECT * " +
	       	"                   FROM " + getGeneralDBScheme() + ".vc_club_rel_bk_oper_s_club_all "+
	       	"                  WHERE id_bk_operation_scheme_line = ?" + 
	      	"         ) WHERE ROWNUM < ? " +
	      	" ) WHERE rn >= ?";
        
        String myFont = "";
        String myBGColor = noneBackGroundStyle;
        
        boolean hasClubRelationshipPermission = false;
        boolean hasEditPermission = false;
        
        try{
        	if (isEditMenuPermited("CLUB_RELATIONSHIP")>=0) {
        		hasClubRelationshipPermission = true;
        	}
       	 	if (isEditPermited("FINANCE_POSTING_SCHEME_RELATIONSHIPS")>0) {
       	 		hasEditPermission = true;
       	 	}
        	
        	LOGGER.debug(mySQL + 
            		", 1={" + this.idOperSchemeLine + ",int}" + 
            		", 2={" + p_end + ",int}" + 
            		", 3={" + p_beg + ",int}");
        	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
        	st.setInt(1, Integer.parseInt(this.idOperSchemeLine));
        	st.setInt(2, Integer.parseInt(p_end));
        	st.setInt(3, Integer.parseInt(p_beg));
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();
            
            if (hasEditPermission) {
           	 	html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/finance/posting_scheme_lineupdate.jsp\">\n");
           	 	html.append("<input type=\"hidden\" name=\"type\" value=\"general\">\n");
        	 	html.append("<input type=\"hidden\" name=\"action\" value=\"set_club_rel\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
           	 	html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idOperSchemeLine + "\">\n");
            }

            html.append(getBottomFrameTable());
            html.append("<tr>");

            for (int i=1; i <= colCount-1; i++) {
               String colName = mtd.getColumnName(i);
               if ("EXIST_FLAG".equalsIgnoreCase(colName)) {
                   if (hasEditPermission) {
                   	html.append("<th> "+ relationshipXML.getfieldTransl(colName, false)+
                  			"<br><input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this)\"></th>\n");
                   } else {
                	   html.append(getBottomFrameTableTH(relationshipXML, mtd.getColumnName(i)));
                   }
               } else {
            	   html.append(getBottomFrameTableTH(relationshipXML, mtd.getColumnName(i)));
               }
            }
            if (hasEditPermission) {
             	html.append("<th>&nbsp;</th>");
             }
            html.append("</tr></thead><tbody>\n");
            if (hasEditPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\""+colCount+"\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/finance/posting_scheme_lineupdate.jsp"));
             	html.append("</td>");
             	html.append("</tr>");
             }
            
            while (rset.next()) {
            	
                String id = "id_" + rset.getString("ID_CLUB_REL") + "_" + this.idOperSchemeLine;
                String tprvCheck="prv_"+id;
                String tCheck="chb_"+id;
                
                if ("Y".equalsIgnoreCase(rset.getString("EXIST_FLAG"))){
                	myFont = "<font style=\"font :bold;\" color=\"black\">";
                	myBGColor = selectedBackGroundStyle;
                } else {
                	myFont = "";
                	myBGColor = noneBackGroundStyle;
                }

                html.append("<tr>");
                for (int i=1; i<=colCount-1; i++) {
                	if ("ID_CLUB_REL".equalsIgnoreCase(mtd.getColumnName(i))) {
                        if (hasClubRelationshipPermission) {
                        	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/relationshipspecs.jsp?id="+rset.getString(i), myFont, myBGColor));
                        } else {
                        	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBGColor));
                        }
                	} else {
                		if ("EXIST_FLAG".equalsIgnoreCase(mtd.getColumnName(i))) {
                			if (hasEditPermission) {
                				if ("Y".equalsIgnoreCase(rset.getString("EXIST_FLAG"))) {
                                  	html.append("<td align=\"center\" " + myBGColor + "><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked onclick=\"return CheckCB(this);\">\n");
                                  	html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+"></td>");
                				} else {
                                  	html.append("<td align=\"center\" " + myBGColor + "><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\">\n");
                				}
                			} else {
                				if ("Y".equalsIgnoreCase(rset.getString("EXIST_FLAG"))) {
                					html.append("<td align=\"center\" " + myBGColor + "><INPUT  type=\"checkbox\" value = \""+rset.getString("EXIST_FLAG")+"\" name="+tCheck+" id="+tCheck+" checked readonly onclick=\"return CheckCB(this);\"></td>\n");
                				} else {
                					html.append("<td align=\"center\" " + myBGColor + "><INPUT  type=\"checkbox\" value = \""+rset.getString("EXIST_FLAG")+"\" name="+tCheck+" id="+tCheck+"  readonly onclick=\"return CheckCB(this);\"></td>\n");
                				}
                			}
                		} else {
                        	html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBGColor));
                		}
                	}
                }
                if (hasEditPermission) {
               	   String myHyperLink = "../crm/finance/posting_scheme_lineupdate.jsp?id=" + this.idOperSchemeLine + "&id_club_rel_oper_scheme=" + rset.getString("ID_CLUB_REL_OPER_SCHEME") + "&type=rel_bk_oper";
               	   html.append(getEditButtonStyleHTML(myHyperLink, getLanguage(), myBGColor));
     	        }
                html.append("</tr>\n");
            }
            if (hasEditPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\""+colCount+"\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/finance/posting_scheme_lineupdate.jsp"));
             	html.append("</td>");
             	html.append("</tr>");
             }
            html.append("</tbody></table>\n");
            if (hasEditPermission) {
             	html.append("</form>");
             }
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
    
    public String getRelationShipsNeededCount(String pCdClubRelType) {
    	return getOneValueByStringId(
    			" SELECT COUNT(*) " +
    			"   FROM " + getGeneralDBScheme()+".vc_club_rel_need_club_all " +
    			"  WHERE cd_club_rel_type = ? ", pCdClubRelType);
    } // getCardStateName
	
    public String getRelationShipsNeededHTML(String pCdClubRelType, String p_beg, String p_end) {
    	StringBuilder html = new StringBuilder();
    	Connection con = null;
    	PreparedStatement st = null;
	    boolean hasAccessPermission = false;
	    String mySQL =  
		    	" SELECT rn, name_club_rel_type, sname_party1 sname_party1_full, sname_party2 sname_party2_full, id_club_rel " +
	      	  	"   FROM (SELECT ROWNUM rn, sname_party1, sname_party2, name_club_rel_type, id_club_rel " +
	          	"   		  FROM (SELECT * " +
	          	"                   FROM " + getGeneralDBScheme()+".vc_club_rel_need_club_all " +
	           	"                  WHERE cd_club_rel_type = ? " + 
	          	"         ) WHERE ROWNUM < ? " + 
	          	" ) WHERE rn >= ?";
	    try{
	    	if (isEditMenuPermited("CLUB_RELATIONSHIP")>=0) {
	    		hasAccessPermission = true;
	    	}
	    	  
	    	LOGGER.debug(mySQL + 
            		", 1={'" + pCdClubRelType + "',string}" + 
            		", 2={" + p_end + ",int}" + 
            		", 3={" + p_beg + ",int}");
	    	con = Connector.getConnection(getSessionId());
	        st = con.prepareStatement(mySQL);
        	st.setString(1, pCdClubRelType);
        	st.setInt(1, Integer.parseInt(p_end));
        	st.setInt(1, Integer.parseInt(p_beg));
	        ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>");
	        for (int i=1; i <= colCount-1; i++) {
	        	html.append(getBottomFrameTableTH(relationshipXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
	        
	        while (rset.next()) {
	        	html.append("<tr>\n");
		        for (int i=1; i <= colCount-1; i++) {
	        		if (hasAccessPermission) {
	        			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/finance/posting_scheme_lineupdate.jsp?type=relationship&action=addneeded&process=no&id="+this.idOperSchemeLine+"&id_club_rel="+rset.getString("id_club_rel"), "", ""));
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
	  } // getRelationShipsHTML

}
