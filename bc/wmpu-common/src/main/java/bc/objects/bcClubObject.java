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
import bc.lists.bcListFund;
import bc.lists.bcListTargetProgram;
import bc.service.bcFeautureParam;

public class bcClubObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcClubObject.class);
	
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idClub;
	
	public bcClubObject(String pIdClub) {
		this.idClub = pIdClub;
		getFeature();
	}
	
	private void getFeature() {
		if (!isEmpty(this.idClub)) {
			String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CLUB_INFO WHERE id_club = ?";
			fieldHm = getFeatures2(featureSelect, this.idClub, false);
		} else {
			LOGGER.error("idClub IS EMPTY");
		}
	}
	
	public void getFeature(String pIdClub) {
		this.idClub = pIdClub;
		getFeature();
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
	public String getClubParticipantHTML(String pFindString, String pClubMemberType, String pSelected, String p_beg, String p_end, String pJSPDateFormat) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        Connection con = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();
        
        boolean hasPartnerPermission = false;
        boolean hasEditPermission = false;
        
        try{
        	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
        		hasPartnerPermission = true;
            }
        	if (isEditPermited("CLUB_CLUB_PARTICIPANT")>0) {
       	 		hasEditPermission = true;
       	 	}
        } 
        catch (Exception e) {LOGGER.error(html, e); html.append(showCRMException(e));}
        

        String mySQL = 
        	" SELECT rn, '<span>'||LPAD(' ', ((level_jur_prs-1)*4*6), '&nbsp;')||sname_jur_prs||'</span>' sname_jur_prs, " +
        	"        jur_adr_full, name_club_member_status, date_beg club_date_beg, id_club, " +
        	"        id_jur_prs, cd_club_member_status, cd_jur_prs_status " +
            "   FROM (SELECT ROWNUM rn, id_jur_prs, sname_jur_prs, " +
        	"				 jur_adr_full, date_beg, id_club, " +
        	"                cd_club_member_status, " +
        	"                DECODE(cd_club_member_status, " +
        	"                     	'CLOSED', '<font color=\"red\"><b>'||name_club_member_status||'</b></font>', " +
       		"                     	'OUT_OF_CLUB', '<font color=\"gray\">'||name_club_member_status||'</font>', " +
       		"                     	'IN_PROCESS', '<font color=\"blue\">'||name_club_member_status||'</font>', " +
       		"                     	'PARTICIPATE', '<font color=\"green\">'||name_club_member_status||'</font>', " +
       		"                       name_club_member_status" +
       		"                ) name_club_member_status, cd_jur_prs_status, level_jur_prs " +
            "           FROM (SELECT m.id_jur_prs, m.sname_jur_prs, " +
        	"			   	         m.jur_adr_full, m.date_beg, m.id_club, " +
        	"                        m.is_issuer, m.is_finance_acquirer, m.is_technical_acquirer, m.is_dealer," +
        	"                        m.is_bank, m.is_operator, m.is_partner, m.is_terminal_manufacturer, " +
        	"                        m.is_agent, m.is_shareholder, m.is_registrator, m.is_coordinator, m.is_curator," +
        	"                        m.cd_club_member_status, s.name_club_member_status, m.cd_jur_prs_status, m.level_jur_prs " +
        	"			        FROM (SELECT a.id_jur_prs, a.sname_jur_prs, " +
        	"				 		         a.jur_adr_full, a.date_beg_frmt date_beg, a.id_club, " +
        	"                                a.is_issuer, a.is_finance_acquirer, a.is_technical_acquirer, a.is_dealer," +
        	"                                a.is_bank, a.is_operator, a.is_partner, a.is_terminal_manufacturer, " +
        	"                                a.is_agent, a.is_shareholder, a.is_registrator, " +
        	"								 a.is_coordinator, a.is_curator, " +
        	"                                a.sname_jur_prs sname_jur_prs_order, a.cd_club_member_status," +
        	"                                DECODE(a.date_beg, NULL, 'N', 'Y') is_in_club, a.cd_jur_prs_status," +
        	"                                level level_jur_prs " +
        	"				            FROM " + getGeneralDBScheme() + ".vc_jur_prs_club_reg_all a " +
        	"                          WHERE a.id_club = ? " +
        	((!hasEditPermission)?" AND date_beg IS NOT NULL ":"") + 
        	"                            /*AND a.cd_jur_prs_status = 'PARTNER'*/" +
        	"                           START WITH id_jur_prs_parent IS NULL " +
    		"                         CONNECT BY PRIOR id_jur_prs = id_jur_prs_parent " +
    		"                           ORDER siblings BY sname_jur_prs) m," +
            "                       " + getGeneralDBScheme() + ".vc_club_member_status_all s" +
        	"                 WHERE m.cd_club_member_status = s.cd_club_member_status (+) ";
        pParam.add(new bcFeautureParam("int", this.idClub));
        
        if (!isEmpty(pClubMemberType)) {
        	if ("DEALER".equalsIgnoreCase(pClubMemberType)) {
            	mySQL = mySQL + " AND NVL(is_dealer, 'N') = 'Y' ";
        	} else if ("BANK".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_bank, 'N') = 'Y' ";
        	} else if ("OPERATOR".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_operator, 'N') = 'Y' ";
        	} else if ("PARTNER".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_partner, 'N') = 'Y' ";
        	} else if ("TERMINAL_MANUFACTURER".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_terminal_manufacturer, 'N') = 'Y' ";
        	} else if ("ISSUER".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_issuer, 'N') = 'Y' ";
        	} else if ("FINANCE_ACQUIRER".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_finance_acquirer, 'N') = 'Y' ";
        	} else if ("TECHNICAL_ACQUIRER".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_technical_acquirer, 'N') = 'Y' ";
        	} else if ("AGENT".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_agent, 'N') = 'Y' ";
        	} else if ("SHAREHOLDER".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_shareholder, 'N') = 'Y' ";
        	} else if ("REGISTRATOR".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_registrator, 'N') = 'Y' ";
        	} else if ("COORDINATOR".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_coordinator, 'N') = 'Y' ";
        	} else if ("CURATOR".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND NVL(is_curator, 'N') = 'Y' ";
        	} else if ("SERVICE_PLACE".equalsIgnoreCase(pClubMemberType)) {
        		mySQL = mySQL + " AND cd_jur_prs_status = 'SERVICE_PLACE' ";
        	}
        }

        if (!isEmpty(pFindString)) {
    		mySQL = mySQL + 
   				" AND (TO_CHAR(id_jur_prs) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%') OR " +
   				"      UPPER(name_club_member_status) LIKE UPPER('%'||?||'%') OR " +
   				"      TO_CHAR(date_beg) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFindString));
    		}
    	}
    	if (!isEmpty(pSelected)) {
    		mySQL = mySQL + " AND is_in_club = ? ";
    		pParam.add(new bcFeautureParam("string", pSelected));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + " ) " +
        	"WHERE ROWNUM < ? " + 
            " ) WHERE rn >= ?";
        
        String myFont = "";
        String myFontEnd = "";
        String myBGColor = "";
        
        String calendarScript = "";
        
        Statement st_member = null;
		ArrayList<String> cd_member=new ArrayList<String>();
		ArrayList<String> name_member=new ArrayList<String>();
		ArrayList<String> style_member=new ArrayList<String>();
		
        String mySQLMemberStatus = 
        	" SELECT cd_club_member_status, name_club_member_status " +
			"   FROM " + getGeneralDBScheme()+".vc_club_member_status_all " +
			"  ORDER BY name_club_member_status";
        
        try{
        	LOGGER.debug(mySQLMemberStatus);
       	 	con = Connector.getConnection(getSessionId());
       	 	st_member = con.createStatement();
        	ResultSet rset_member = st_member.executeQuery(mySQLMemberStatus);
        	while (rset_member.next()) {
        		cd_member.add(rset_member.getString("CD_CLUB_MEMBER_STATUS"));
        		name_member.add(rset_member.getString("NAME_CLUB_MEMBER_STATUS"));
        		if ("CLOSED".equalsIgnoreCase(rset_member.getString("CD_CLUB_MEMBER_STATUS"))) {
        			style_member.add(" style = \"color: red; font-weight:bold;\"");
        		} else if ("OUT_OF_CLUB".equalsIgnoreCase(rset_member.getString("CD_CLUB_MEMBER_STATUS"))) {
        			style_member.add(" style = \"color: gray; font-weight:bold;\"");
        		} else if ("IN_PROCESS".equalsIgnoreCase(rset_member.getString("CD_CLUB_MEMBER_STATUS"))) {
        			style_member.add(" style = \"color: blue; font-weight:bold;\"");
        		} else if ("REGISTRATOR".equalsIgnoreCase(rset_member.getString("CD_CLUB_MEMBER_STATUS"))) {
        			style_member.add(" style = \"color: green; font-weight:bold;\"");
        		} else if ("COORDINATOR".equalsIgnoreCase(rset_member.getString("CD_CLUB_MEMBER_STATUS"))) {
        			style_member.add(" style = \"color: green; \"");
        		} else if ("CURATOR".equalsIgnoreCase(rset_member.getString("CD_CLUB_MEMBER_STATUS"))) {
        			style_member.add(" style = \"color: green;\"");
        		} else {
        			style_member.add(" ");
        		}
        	}
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
        catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
        finally {
            try {
                if (st_member!=null) st_member.close();
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
            //int colCount = mtd.getColumnCount();

            if (hasEditPermission) {
           	 	html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/club/clubupdate.jsp\">\n");
           	 	html.append("<input type=\"hidden\" name=\"type\" value=\"set_jur_prs\">\n");
        	 	html.append("<input type=\"hidden\" name=\"action\" value=\"set\">\n");
           	 	html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
           	 	html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idClub + "\">\n");
            }
            
            html.append(getBottomFrameTable());
            html.append("<tr>");
            for (int i=1; i <= colCount-4; i++) {
               html.append(getBottomFrameTableTH(jurpersonXML, mtd.getColumnName(i)));
            }
            if (hasEditPermission) {
            	html.append("<th> "+ clubXML.getfieldTransl("is_club_member", false) + "<br>" +
           			"<input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" onclick=\"CheckAll(this)\"></th>\n");
            }
            html.append("</tr></thead><tbody>\n");
            if (hasEditPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\"8\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/club/clubupdate.jsp"));
             	html.append("</td>");
             	html.append("</tr>");
             }
            while (rset.next())
            {
                String id = "id_" + rset.getString("ID_JUR_PRS");
                String tprvCheck="prv_"+id;
                String tCheck="chb_"+id;
                String dateReg="reg_"+id;
                String tStatus="sts_"+id;
                
                if (!isEmpty(rset.getString("CLUB_DATE_BEG"))){
                	myFont = "<b>";
                	myFontEnd = "</b>";
                	myBGColor = selectedBackGroundStyle;
                } else {
                	myFont = "";
                	myFontEnd = "";
                	myBGColor = "";
                }
                html.append(getBottomFrameTableTD("RN", rset.getString("RN"), "", myFont, myBGColor));
                if (hasPartnerPermission && !isEmpty(rset.getString("SNAME_JUR_PRS"))) {
	        		html.append(getBottomFrameTableTD("SNAME_JUR_PRS", rset.getString("SNAME_JUR_PRS"), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS"), myFont, myBGColor));
	            } else {
	               	html.append(getBottomFrameTableTD("SNAME_JUR_PRS", rset.getString("SNAME_JUR_PRS"), "", myFont, myBGColor));
	            } 

                html.append(getBottomFrameTableTD("JUR_ADR_FULL", rset.getString("JUR_ADR_FULL"), "", myFont, myBGColor));
                String selectOption = "<option value=\"\"></option>";
                String selectStyle = "";
                String memberSelect = "";
                if ("PARTNER".equalsIgnoreCase(rset.getString("CD_JUR_PRS_STATUS"))) {
	                if (hasEditPermission) {
	                	if (cd_member.size()>0) {
	                		for(int counter=0;counter<cd_member.size();counter++){
	                			if (cd_member.get(counter).equalsIgnoreCase(rset.getString("CD_CLUB_MEMBER_STATUS"))) {
	                				selectOption = selectOption + "<option value=\"" + cd_member.get(counter) + "\" SELECTED " + style_member.get(counter) + ">" + name_member.get(counter) + "</option>";
	                				selectStyle = style_member.get(counter);
	                			} else {
	                				selectOption = selectOption + "<option value=\"" + cd_member.get(counter) + "\" " + style_member.get(counter) + ">" + name_member.get(counter) + "</option>";
	                			}
	                		}
	                	}
	                	memberSelect = memberSelect + "<select name=\"" + tStatus + "\" id=\"" + tStatus + "\" class=\"inputfield\" " + selectStyle + ">";
	                	memberSelect = memberSelect + selectOption;
	                    memberSelect = memberSelect + "</select>";
	                	html.append(getBottomFrameTableTD("NAME_CLUB_MEMBER_STATUS", memberSelect, "", myFont, myBGColor));
	                } else {
	                	html.append(getBottomFrameTableTD("NAME_CLUB_MEMBER_STATUS", rset.getString("NAME_CLUB_MEMBER_STATUS"), "", myFont, myBGColor));
	                }
	                if (hasEditPermission) {
	                	if (!isEmpty(rset.getString("CLUB_DATE_BEG"))){
	                		html.append("<td " + myBGColor + ">");
	                		html.append(getCalendarInputField(dateReg, getValue2(rset.getString("CLUB_DATE_BEG")), "10"));
	                		html.append("</td>\n");
	                      	html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked onclick=\"return CheckCB(this);\"></td>\n");
	                      	html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
	                      	calendarScript = calendarScript +
	                      		"<script type=\"text/javascript\">\n" + 
	                      		"Calendar.setup({\n" +
	                			"inputField  : \"id_" + dateReg + "\",         // ID поля вводу дати\n" +
	                      		"ifFormat    : \"" + pJSPDateFormat + "\",    // формат дати (23.03.2008)\n" +
	                      		"button      : \"btn_" + dateReg + "\"       // ID кнопки для меню вибору дати\n" +
	                    		"});\n" +
	                    		"</script>";
	                    } else { 
	                    	html.append("<td " + myBGColor + ">");
	                		html.append(getCalendarInputField(dateReg, "", "10"));
	                		html.append("</td>\n");
	                      	html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" onclick=\"return CheckCB(this);\"></td>\n");
	                      	calendarScript = calendarScript +
		                  		"<script type=\"text/javascript\">\n" + 
		                  		"Calendar.setup({\n" +
		            			"inputField  : \"id_" + dateReg + "\",         // ID поля вводу дати\n" +
		                  		"ifFormat    : \"" + pJSPDateFormat + "\",    // формат дати (23.03.2008)\n" +
		                  		"button      : \"btn_" + dateReg + "\"       // ID кнопки для меню вибору дати\n" +
		                		"});\n" +
		                		"</script>";
	                    }
	                } else {
	                	html.append("<td align=\"center\"" + myBGColor + ">" + myFont + getValue2(rset.getString("CLUB_DATE_BEG")) + myFontEnd + "</td>\n");
	                }
                } else {
            		html.append("<td colspan=\"2\" align=\"center\"" + myBGColor + ">&nbsp;</td>\n");
                	if (hasEditPermission) {
                		if (!isEmpty(rset.getString("CLUB_DATE_BEG"))){
                			html.append("<td " + myBGColor + " align=\"center\"><INPUT type=hidden  value=\"Y\" name="+tprvCheck+"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked></td>\n");
                		} else {
                			html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+"></td>\n");
                		}
                	}
                }
                html.append("</tr>\n");
            }
            if (hasEditPermission) {
             	html.append("<tr>\n");
             	html.append("<td colspan=\"8\" align=\"center\">\n");
             	html.append(getSubmitButtonAjax("../crm/club/clubupdate.jsp"));
             	html.append("</td>\n");
             	html.append("</tr>\n");
             }
            html.append("</tbody></table></form>\n");
            html.append(calendarScript);
        } // try
        catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
        //catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
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
    } // getClubParticipantHTML

    public String getUserPrivilegesHTML(String pFind, String pStatus, String pSelected, String p_beg, String p_end) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
        	" SELECT rn, name_user, sname_jur_prs jur_prs, fio_nat_prs fio_user, phone_mobile, " +
        	"        name_user_status, has_permission club_data_access, " +
        	"        has_permission_tsl club_data_access_name, " +
        	"        id_user, id_nat_prs, id_jur_prs " +
        	"   FROM (SELECT ROWNUM rn, sname_jur_prs, name_user, fio_nat_prs, phone_mobile, " +
        	"                name_user_status, has_permission, has_permission_tsl, " +
        	"                id_user, id_nat_prs, id_jur_prs " +
        	"           FROM (SELECT sname_jur_prs, name_user, fio_nat_prs, phone_mobile, " +
        	"                        DECODE(cd_user_status, " +
            "          						'OPENED', '<font color=\"green\">'||name_user_status||'</font>', " +
            "          						'<font color=\"red\">'||name_user_status||'</font>'" +
            "  		 		 		 ) name_user_status, " +
        	"                        has_permission, has_permission_tsl, " +
        	"                        id_user, id_nat_prs, id_jur_prs " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_user_club_priv_all a " +
        	"                  WHERE a.id_club = ? ";
    	pParam.add(new bcFeautureParam("int", this.idClub));
    	
    	if (!isEmpty(pSelected)) {
    		if ("Y".equalsIgnoreCase(pSelected)) {
        		mySQL = mySQL + " AND has_permission = 'Y' ";
    		} else if ("N".equalsIgnoreCase(pSelected)) {
    			mySQL = mySQL + " AND has_permission = 'N' ";
    		}
    	}
    	if (!isEmpty(pFind)) {
    		mySQL = mySQL + 
    			" AND (TO_CHAR(id_user) LIKE UPPER('%'||?||'%') OR" +
    			"      UPPER(name_user) LIKE UPPER('%'||?||'%') OR" +
    			"      UPPER(fio_nat_prs) LIKE UPPER('%'||?||'%') OR" +
    			"      UPPER(phone_mobile) LIKE UPPER('%'||?||'%'))";
    		for (int i=0; i<4; i++) {
    		    pParam.add(new bcFeautureParam("string", pFind));
    		}
    	}
    	if (!isEmpty(pStatus)) {
    		mySQL = mySQL + " AND cd_user_status = ? ";
    		pParam.add(new bcFeautureParam("string", pStatus));
    	}
    	pParam.add(new bcFeautureParam("int", p_end));
    	pParam.add(new bcFeautureParam("int", p_beg));
    	mySQL = mySQL +
        	"                  ORDER BY DECODE(a.has_permission, 'Y',1,2), a.name_user)" + 
        	"          WHERE ROWNUM < ? " + 
        	" ) WHERE rn >= ?";
          
    	StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        String myFont = "";
        String myFontEnd = "";
        String myBGColor = "";
          
        boolean hasPermission = false;
        boolean hasUserPermission = false;
        boolean hasNatPrsPermission = false;
        boolean hasJurPrsPermission = false;

        try{
        	 if (isEditPermited("CLUB_CLUB_ACCESS")>0) {
        		 hasPermission = true;
        	 }
        	 if (isEditMenuPermited("SECURITY_USERS")>=0) {
        		 hasUserPermission = true;
        	 }
        	 if (isEditMenuPermited("CLIENTS_NATPERSONS")>=0) {
        		 hasNatPrsPermission = true;
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
             if (hasPermission) {
            	 html.append("<form method=\"post\" name=\"updateForm\" id=\"updateForm\" action=\"../crm/club/clubupdate.jsp\">\n");
            	 html.append("<input type=\"hidden\" name=\"type\" value=\"access\">\n");
            	 html.append("<input type=\"hidden\" name=\"action\" value=\"set\">\n");
            	 html.append("<input type=\"hidden\" name=\"process\" value=\"yes\">\n");
            	 html.append("<input type=\"hidden\" name=\"id\" value=\"" + this.idClub + "\">\n");
             }
             html.append(getBottomFrameTable());
             html.append("<tr>");
             for (int i=1; i <= colCount-4; i++) {
            	 String colName = mtd.getColumnName(i);                            
                 if ("CLUB_DATA_ACCESS".endsWith(colName)) {
                     if (hasPermission) {
                       	 html.append("<th>"+ userXML.getfieldTransl("club_data_access", false)+
                       		"<br><input id=\"mainCheck\" name=\"mainCheck\" type=\"checkbox\" style=\"height:inherit;padding:0;\" onclick=\"CheckAll(this,'chb_id')\"></th>\n");
                     } else {
                    	 html.append(getBottomFrameTableTH(userXML, colName));
                     }
                 } else {
                	 html.append(getBottomFrameTableTH(userXML, colName));
                 }
             }
             html.append("</tr></thead><tbody>\n");
             if (hasPermission) {
             	html.append("<tr>");
             	html.append("<td colspan=\"7\" align=\"center\">");
             	html.append(getSubmitButtonAjax("../crm/club/clubupdate.jsp"));
             	html.append("</td>");
             	html.append("</tr>");
             }

             
             int rowCount = 0;
             while (rset.next()) {
             	rowCount++;
                String jurPrsID="id_"+rset.getString("ID_USER");
                String tprvCheck="prv_"+jurPrsID;
                String tCheck="chb_"+jurPrsID;
                
                if(rset.getString("CLUB_DATA_ACCESS").equalsIgnoreCase("Y")){
                	myFont = "<b>";
                	myFontEnd = "</b>";
                	myBGColor = selectedBackGroundStyle;
                } else {
                	myFont = "";
                	myFontEnd = "";
                	myBGColor = "";
                }
                    
                for (int i=1; i <= colCount-5; i++) {
          	  		if (hasUserPermission && "NAME_USER".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/security/userspecs.jsp?id="+rset.getString("ID_USER"), myFont, myBGColor));
         	  		} else if (hasNatPrsPermission && "FIO_USER".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/natpersonspecs.jsp?id="+rset.getString("ID_NAT_PRS"), myFont, myBGColor));
         	  		} else if (hasJurPrsPermission && "JUR_PRS".equalsIgnoreCase(mtd.getColumnName(i))) {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id="+rset.getString("ID_JUR_PRS"), myFont, myBGColor));
         	  		} else {
          	  			html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBGColor));
          	  		}
                }
                /*html.append("<tr>\n" +
                	"<td " + myBGColor + " align=\"center\">" + myFont + rset.getString("RN") + myFontEnd + "</td>\n"
                	);
                if (hasUserPermission) {
                	html.append(getBottomFrameTableTD("NAME_USER", rset.getString(3), "../crm/security/userspecs.jsp?id="+rset.getString("ID_USER"), myFont, myBGColor));
                } else {
                	html.append("<td " + myBGColor + ">" + myFont + rset.getString("NAME_USER") + myFontEnd + "</td>\n");
                }
                html.append("<td " + myBGColor + ">" + myFont + rset.getString("NAME_USER_STATUS") + myFontEnd + "</td>\n");*/
                if (hasPermission) {
	                if(rset.getString("CLUB_DATA_ACCESS").equalsIgnoreCase("Y")){
	                  	html.append("<td " + myBGColor + " align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" checked style=\"height:inherit;padding:0;\" onclick=\"return CheckCB(this);\"></td>\n");
	                  	html.append("<INPUT type=hidden  value=\"Y\" name="+tprvCheck+">");
	                } else { 
	                    html.append("<td align=\"center\"><INPUT  type=\"checkbox\" value = \"Y\" name="+tCheck+" id="+tCheck+" style=\"height:inherit;padding:0;\" onclick=\"return CheckCB(this);\"></td>\n");
	                }
                } else {
                	html.append("<td " + myBGColor + " align=\"center\">" + myFont + rset.getString("CLUB_DATA_ACCESS_NAME") + myFontEnd + "</td>\n");
                }
                        
                html.append("<INPUT type=hidden  value="+rset.getString(2)+" name="+jurPrsID+">");
                html.append("</tr> ");        
            }

            if (hasPermission) {
            	html.append("<tr>");
            	html.append("<td colspan=\"7\" align=\"center\">");
            	html.append(getSubmitButtonAjax("../crm/club/clubupdate.jsp"));
            	html.append("</td>");
            	html.append("</tr>");
            }
            html.append("<input type=hidden value="+rowCount+" name=rowCount>");
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
    } //getUserJurPrsPrivilegesHTML

    public String getFundsHTML(String pFind, String p_beg, String p_end) {

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 
    		" SELECT rn, sname_jur_prs, name_club_fund, sname_club_fund, desc_club_fund, " +
        	"        date_beg_frmt fund_date_beg, date_end_frmt fund_date_end, id_club_fund, id_jur_prs  " +
        	"   FROM (SELECT ROWNUM rn, sname_jur_prs, name_club_fund, sname_club_fund, desc_club_fund, " +
        	"                date_beg_frmt, date_end_frmt, id_club_fund, id_jur_prs " +
        	"   		  FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme()+".vc_club_fund_club_all a " +
        	"                  WHERE a.id_club = ? ";
    	pParam.add(new bcFeautureParam("int", this.idClub));
    	
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + " AND (TO_CHAR(id_club_fund) LIKE UPPER('%'||?||'%') OR " +
				"UPPER(name_club_fund) LIKE UPPER('%'||?||'%') OR " +
				"UPPER(sname_club_fund) LIKE UPPER('%'||?||'%') OR " + 
				"UPPER(desc_club_fund) LIKE UPPER('%'||?||'%') OR " + 
				"UPPER(sname_jur_prs) LIKE UPPER('%'||?||'%')) ";
			for (int i=0; i<5; i++) {
			    pParam.add(new bcFeautureParam("string", pFind));
			}
    	}
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
      	  	"                 ORDER BY sname_jur_prs, name_club_fund)  " +
      	  	"        WHERE ROWNUM < ? " + 
      	  	" ) WHERE rn >= ?";
           	StringBuilder html = new StringBuilder();
           	Connection con = null;        
           	PreparedStatement st = null;

           	boolean hasFundPermission = false;
           	boolean hasPartnerPermission = false;
  	        //boolean hasEditPermission = false;
           
           	try{
           		if (isEditMenuPermited("club_fund")>=0) {
           			hasFundPermission = true;
           		}
            	if (isEditMenuPermited("CLIENTS_YURPERSONS")>=0) {
            		hasPartnerPermission = true;
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
	              	  html.append(getBottomFrameTableTH(clubfundXML, mtd.getColumnName(i)));
	              }
		          /*if (hasEditPermission) {
			            html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
			          }*/
	              html.append("</tr></thead><tbody>\n");
         
	              while (rset.next())  {
	            	  html.append("<tr>");
	            	  for (int i=1; i <= colCount-2; i++) {
	            		  if (hasFundPermission && "NAME_CLUB_FUND".equalsIgnoreCase(mtd.getColumnName(i))) {
	            			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/club/fundspecs.jsp?id=" + rset.getString("ID_CLUB_FUND"), "", ""));
	            		  } else if (hasPartnerPermission && "SNAME_JUR_PRS".equalsIgnoreCase(mtd.getColumnName(i))) {
	            			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "../crm/clients/yurpersonspecs.jsp?id=" + rset.getString("ID_JUR_PRS"), "", ""));
	            		  } else {
	            			  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", "", ""));
	            		  }
		              }
	  	            /*if (hasEditPermission) {
		            	String myHyperLink = getContextPath()+"../crm/club/clubupdate.jsp?id="+this.idClub+"&id_fund="+rset.getString("ID_CLUB_FUND")+"&type=fund";
		            	String myDeleteLink = getContextPath()+"../crm/club/clubupdate.jsp?id="+this.idClub+"&id_fund="+rset.getString("ID_CLUB_FUND")+"&type=fund&action=remove&process=yes";
		                html.append(getDeleteButtonHTML(myDeleteLink, getLanguage(), buttonXML.getfieldTransl(getLanguage(),"DELETE", false), rset.getString("NAME_CLUB_FUND")));
		            	html.append(getEditButtonHTML(myHyperLink, getLanguage()));
		            }*/
	            	  html.append("</tr>");
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
    } // class getJurPersonComissionHTML

    public String getClubFundsHTML(String pFindString, String p_beg, String p_end) {
    	bcListFund list = new bcListFund();
    	
    	String pWhereCause = " WHERE id_club = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idClub));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	if (isEditPermited("CLUB_CLUB_FUNDS")>0) {
    		myDeleteLink = "../crm/club/fundupdate.jsp?back_type=CLUB&type=general&id="+this.idClub+"&action=remove&process=yes";
    	    myEditLink = "../crm/club/fundupdate.jsp?back_type=CLUB&type=general&id="+this.idClub;
    	}
    	
    	return list.getFundsHTML(pWhereCause, pWhereValue, "", pFindString, myEditLink, myDeleteLink, p_beg, p_end);
    	
    } 

    public String getTargetProgramsHTML(String pFindString, String pPayPeriod, String p_beg, String p_end) {
    	bcListTargetProgram list = new bcListTargetProgram();
    	
    	String pWhereCause = " WHERE id_club = ? ";
    	
    	ArrayList<bcFeautureParam> pWhereValue = new ArrayList<bcFeautureParam>();
    	pWhereValue.add(new bcFeautureParam("int", this.idClub));
    	String myDeleteLink = "";
    	String myEditLink = "";
    	String myCopyLink = "";
    	if (isEditPermited("CLUB_CLUB_TARGET_PROGRAM")>0) {
    		myDeleteLink = "../crm/club/target_programupdate.jsp?back_type=CLUB&type=general&id="+this.idClub+"&action=remove&process=yes";
    	    myEditLink = "../crm/club/target_programupdate.jsp?back_type=CLUB&type=general&id="+this.idClub;
    	    myCopyLink = "../crm/club/target_programupdate.jsp?back_type=CLUB&type=general&id="+this.idClub;
    	}
    	
    	return list.getTargetProgramsHTML(pWhereCause, pWhereValue, "", pFindString, pPayPeriod, myEditLink, myCopyLink, myDeleteLink, p_beg, p_end);
    	
    } 

}
