package bc.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import bc.connection.Connector;
import org.apache.log4j.Logger;
import bc.objects.bcObject;
import bc.service.bcFeautureParam;

public class bcReports extends bcObject { 
	protected static Connection con = null;
	
	private final static Logger LOGGER=Logger.getLogger(bcReports.class);
	
	String reportFormat = "";
	
	public bcReports(String pReportFormat){
		this.reportFormat = pReportFormat;
	}
   
    public String getReportHeaderHTML(String typeReport, String id_entry, String pFind, String id_current_report, String detailHyperLink, String logHyperLink, String p_beg, String p_end, String pPrint) {
        StringBuilder html = new StringBuilder();

        String myBgColor = noneBackGroundStyle;
        String myFont = "<font color=\"black\">";
    	String myFontEnd = "</font>";
    	
    	PreparedStatement st = null;

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	String mySQL = 	
        	" SELECT rn, id_report, type_report_tsl, date_report_frmt, state_report_tsl, state_report " +
			"   FROM (SELECT ROWNUM rn, type_report_tsl, id_report, date_report_frmt, state_report_tsl, state_report " +
        	"           FROM (SELECT id_report, type_report_tsl, date_report_frmt, state_report_tsl, state_report " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_rep_header " +
        	"                  WHERE type_report IN (" + typeReport + ") ";
        if (!isEmpty(id_entry)) {
        	mySQL = mySQL + " AND id_entry = ? ";
        	pParam.add(new bcFeautureParam("int", id_entry));
        }
        mySQL = mySQL +
        	"                  ORDER BY id_report DESC" +
        	"         ) WHERE ";
        if (!isEmpty(id_current_report)) {
        	mySQL = mySQL + " id_report = ? AND ";
        	pParam.add(new bcFeautureParam("int", id_current_report));
        	p_beg = "1";
        	p_end = "10";
        }
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + 
        		" (TO_CHAR(id_report) LIKE UPPER('%'||?||'%') OR " +
        		"  date_report_frmt LIKE UPPER('%'||?||'%')) AND ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + " ROWNUM < ?) WHERE rn >= ?";
        try{
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();

            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>\n");
            if ("N".equalsIgnoreCase(pPrint)) {
            	if (!isEmpty(detailHyperLink)) {
            		html.append("<th>&nbsp;</th>\n");
            	}
            	if (!isEmpty(logHyperLink)) {
            		html.append("<th>&nbsp;</th>\n");
            	}
            }
            for (int i=1; i <= colCount-1; i++) {
            	html.append(getBottomFrameTableTH(postingXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
               	if ("E".equalsIgnoreCase(rset.getString("STATE_REPORT"))) {
               		myBgColor = reportSelectedBackGroundStyle;
               		myFont = "<font color=\"red\">";
                   	myFontEnd = "</font>";
               	} else {
               		myBgColor = noneBackGroundStyle;
               		myFont = "<font color=\"black\">";
                   	myFontEnd = "</font>";
               	}
               	if ("N".equalsIgnoreCase(pPrint)) {
               		if (!isEmpty(detailHyperLink)) {
		               	if (isEmpty(id_current_report)) {
			               	html.append("<td align=\"center\" "+myBgColor+">");
							html.append(getHyperLinkFirst()+detailHyperLink + "&id=" + id_entry + "&id_report=" + rset.getString("ID_REPORT") + "&page_report_det=1&page_report_log=1" + getHyperLinkMiddle());
							html.append(myFont + "<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/report_detail.png\" width=\"20\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ reportXML.getfieldTransl("t_content", false) +"\">" + myFontEnd);
							html.append(getHyperLinkEnd() + "</td>\n");
		               	} else {
			               	html.append("<td align=\"center\" "+myBgColor+">");
							html.append(getHyperLinkFirst()+detailHyperLink + "&id=" + id_entry + "&page_report_det=1&page_report_log=1" + getHyperLinkMiddle());
							html.append(myFont + "<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/report_detail.png\" width=\"20\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ reportXML.getfieldTransl("t_content", false) +"\">" + myFontEnd);
							html.append(getHyperLinkEnd() + "</td>\n");
		               	}
               		}
               		if (!isEmpty(logHyperLink)) {
		               	if (isEmpty(id_current_report)) {
			               	html.append("<td align=\"center\" "+myBgColor+">");
							html.append(getHyperLinkFirst()+logHyperLink + "&id=" + id_entry + "&id_report="+rset.getString("ID_REPORT") + "&page_report_det=1&page_report_log=1" + getHyperLinkMiddle());
							html.append(myFont + "<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/report_log.png\" width=\"20\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ reportXML.getfieldTransl("t_log", false) +"\">" + myFontEnd);
							html.append(getHyperLinkEnd() + "</td>\n");
		               	} else {
			               	html.append("<td align=\"center\" "+myBgColor+">");
							html.append(getHyperLinkFirst() + logHyperLink + "&id=" + id_entry + "&page_report_det=1&page_report_log=1" + getHyperLinkMiddle());
							html.append(myFont + "<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/report_log.png\" width=\"20\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ reportXML.getfieldTransl("t_log", false) +"\">" + myFontEnd);
							html.append(getHyperLinkEnd() + "</td>\n");
		               	}
               		}
               	}
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("RN")) + myFontEnd + "</td>\n");
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("ID_REPORT")) + myFontEnd + "</td>\n");
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("TYPE_REPORT_TSL")) + myFontEnd + "</td>\n");
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("DATE_REPORT_FRMT")) + myFontEnd + "</td>\n");
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("STATE_REPORT_TSL")) + myFontEnd + "</td>\n");

                html.append("</tr>\n");
            }
            html.append("</tbody></table>\n");
        } // try
        catch (SQLException e) {LOGGER.error(html,e);}
        catch (Exception el) {LOGGER.error(html,el);}
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
    } //getReportHeaderHTML

    
    public String getQuestionnairesReportHTML(String id_report, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        
        String mySQL = "";
        PreparedStatement st = null;
        String myBgColor = noneBackGroundStyle;
        String myFont = "";
    	String myFontEnd = "";

    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	try{
        	mySQL = 
        		" SELECT id_quest_int, line_number, date_report, cd_card, surname, name, " +
        		"        patronymic, date_of_birth, text_report " +
				"   FROM (SELECT ROWNUM rn, id_quest_int, line_number, date_report, cd_card, surname, name, " +
        		"                patronymic, date_of_birth, text_report " +
				"   		FROM (SELECT id_quest_int, line_number, date_report, cd_card, surname, name, " +
        		"        				  patronymic, date_of_birth, text_report " +
				"   				FROM " + getGeneralDBScheme() + ".V_REP_NAT_PRS_IMP_ALL " +
				"  				   WHERE id_report = ? ";

        	pParam.add(new bcFeautureParam("int", id_report));
        	pParam.add(new bcFeautureParam("int", p_end));
        	pParam.add(new bcFeautureParam("int", p_beg));
        	mySQL = mySQL + ") WHERE ROWNUM < ?) WHERE rn >= ?";
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
            con = Connector.getConnection(getSessionId());
            
           	st = con.prepareStatement(mySQL); 
           	st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>\n");
            for (int i=1; i <= colCount; i++) {
            	html.append(getBottomFrameTableTH(questionnaireXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr "+myBgColor+"><td align=\"center\">" + myFont + getValue2(rset.getString(1)) + myFontEnd + "</td>\n" +
                        "<td "+myBgColor+">" + myFont + getValue2(rset.getString(2)) + myFontEnd + "</td>\n" +
                        "<td "+myBgColor+">" + myFont + getValue2(rset.getString(3)) + myFontEnd + "</td>\n" +
                        "<td "+myBgColor+">" + myFont + getValue2(rset.getString(4)) + myFontEnd + "</td>\n" +
                        "<td "+myBgColor+">" + myFont + getValue2(rset.getString(5)) + myFontEnd + "</td>\n" +
                        "<td "+myBgColor+">" + myFont + getValue2(rset.getString(6)) + myFontEnd + "</td>\n" +
                        "<td "+myBgColor+">" + myFont + getValue2(rset.getString(7)) + myFontEnd + "</td>\n" +
                        "<td "+myBgColor+">" + myFont + getValue2(rset.getString(8)) + myFontEnd + "</td>\n" +
                        "<td "+myBgColor+">" + myFont + getValue2(rset.getString(9)) + myFontEnd + "</td></tr>\n");
            }
            html.append("</tbody></table>\n");
        } // try
        catch (SQLException e) {LOGGER.error(html,e);}
        catch (Exception el) {LOGGER.error(html,el);}
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
    } // getQuestionnairesReportHTML

    public String getOperSchemeReportHTML(String cd_event_filtr, String cd_event, String cd_type, String pBeg, String pEnd) {
        StringBuilder html = new StringBuilder();
        
        String mySQLGeneral =  "";
        String mySQL = "";
        PreparedStatement st = null;
        PreparedStatement stGeneral = null;

        ArrayList<bcFeautureParam> pParam = initParamArray();
        ArrayList<bcFeautureParam> pParam2 = new ArrayList<bcFeautureParam>();

        try{
        	
            mySQLGeneral =  
            	" SELECT name_bk_event cd_bk_source, name_bk_operation_type, cd_bk_event, cd_bk_operation_type "+
				"   FROM " + getGeneralDBScheme() + ".VC_BK_OPERATION_TYPE ";
            if (!isEmpty(cd_event)) {
            	mySQLGeneral = mySQLGeneral + 
            		"  WHERE cd_bk_event = ? " +
					"    AND cd_bk_operation_type = ? ";
            	pParam.add(new bcFeautureParam("string", cd_event));
            	pParam.add(new bcFeautureParam("string", cd_type));
            } else if (!isEmpty(cd_event_filtr)) {
            	mySQLGeneral = mySQLGeneral + "  WHERE cd_bk_event = ? ";
            	pParam.add(new bcFeautureParam("string", cd_event_filtr));
            }
			mySQLGeneral = mySQLGeneral +"   ORDER BY name_bk_event, name_bk_operation_type";
			
        	
			LOGGER.debug(prepareSQLToLog(mySQL, pParam));
            con = Connector.getConnection(getSessionId());
            
            stGeneral = con.prepareStatement(mySQLGeneral);
            st = prepareParam(st, pParam);
            ResultSet rsetGeneral = stGeneral.executeQuery();

            while (rsetGeneral.next())
            {
            	html.append("<br>"+getValue2(rsetGeneral.getString(1)));
            	html.append("<br>"+getValue2(rsetGeneral.getString(2)));
            	html.append("<br>");
            	try{
            		mySQL = " SELECT id_bk_operation_scheme_line, name_bk_phase, oper_code, oper_number, " +
                    "        debet_cd_bk_account_sh_line debet_cd_bk_account, " +
                    "        credit_cd_bk_account_sh_line credit_cd_bk_account, oper_content, " +
                    "        amount, assignment_posting, name_frequence cd_frequence, nt_icc, " +
                    "        exist_flag_tsl exist_flag, using_in_clearing_tsl using_in_clearing " +
                    "   FROM " + getGeneralDBScheme() + ".vc_oper_scheme_lines_club_all "+
                   	"  WHERE cd_bk_event = ? " +
                   	"    AND cd_bk_operation_type = ? " +
                   	"  ORDER BY oper_code, name_bk_phase, oper_number";
            		pParam2.add(new bcFeautureParam("string", rsetGeneral.getString("CD_BK_EVENT")));
            		pParam2.add(new bcFeautureParam("string", rsetGeneral.getString("CD_BK_OPERATION_TYPE")));
                    
                    st = con.prepareStatement(mySQL); 

        		    int fieldCounter2 = 0;
        		    for(int i = 0; i < pParam2.size(); i++) {
        		    	String key = pParam2.get(i).getDataType();
        		    	String value = pParam2.get(i).getValue();
        		    	if ("string".equalsIgnoreCase(key)) {
        		    		fieldCounter2 ++;
        			    	st.setString(fieldCounter2, value);
        			    } else if ("int".equalsIgnoreCase(key)) {
        			    	int myId = Integer.parseInt(value);
        			    	fieldCounter2 ++;
        			    	st.setInt(fieldCounter2, myId);
        			    }
        		    }
                    ResultSet rset = st.executeQuery();

                    ResultSetMetaData mtd = rset.getMetaData();
                    int colCount = mtd.getColumnCount();
                    
                    html.append(getBottomFrameTable());
                    html.append("<tr>\n");
                    for (int i=1; i <= colCount; i++) {
                    	html.append(getBottomFrameTableTH(posting_schemeXML, mtd.getColumnName(i)));
                    }
                    html.append("</tr></thead><tbody>\n");
                    while (rset.next())
                    {
                    	html.append("<tr>\n");
                        for (int i=1; i<=colCount; i++) {
                        	html.append("<td>"+getValue2(rset.getString(i))+"</td>\n");
                        }
                        html.append("</tr>\n");
                    }
                    html.append("</tbody></table>\n");
            	} 
            	catch (SQLException e) {LOGGER.error(html,e);}
                catch (Exception el) {LOGGER.error(html,el);}
                finally {
                    try {
                        if (st!=null) st.close();
                    } catch (SQLException w) {LOGGER.error(html, w);}

                }
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
    } // getOperSchemeReportHTML

    public String getPostingsGroupHTML(String id_group, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        String mySQL = "";
        
        boolean hasPostingPermission = false;
        
        try{
        	if (isEditMenuPermited("FINANCE_POSTINGS")>=0) {
        		hasPostingPermission = true;
        	}
        	
        	mySQL = 
        		" SELECT id_trans, id_posting, id_posting_group, operation_date, name_currency," +
				"        debet_id_bk_account, credit_id_bk_account, " +
				"		 entered_amount, assignment_posting, " +
				"        state_posting_tsl, id_clearing " +
				"   FROM (SELECT ROWNUM rn, id_trans, id_posting, id_posting_group, " +
				"				 operation_date_frmt operation_date, name_currency," +
				"                debet_cd_bk_account_frmt||' '||debet_name_bk_account debet_id_bk_account,  " +
				"        		 credit_cd_bk_account_frmt||' '||credit_name_bk_account credit_id_bk_account, " +
				"		 		 entered_amount, assignment_posting, " +
				"        		 state_posting_tsl, id_clearing " +
				"   		FROM (SELECT * " +
				"           		FROM " + getGeneralDBScheme() + ".VC_ACC_POSTING_DETAIL_CLUB_ALL " +
				"          		   WHERE id_posting_group = ?)" +
				"  WHERE ROWNUM < ?) WHERE rn >= ? ORDER BY id_trans";

        	LOGGER.debug(mySQL + 
        			", 1={" + id_group + ",int}" + 
        			", 2={" + p_end + ",int}" + 
        			", 3={" + p_beg + ",int}");
            con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st.setInt(1, Integer.parseInt(id_group));
            st.setInt(2, Integer.parseInt(p_end));
            st.setInt(3, Integer.parseInt(p_beg));

            ResultSet rset = st.executeQuery();

            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>\n");
            for (int i=1; i <= colCount; i++) {
            	html.append(getBottomFrameTableTH(postingXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i <= colCount; i++) {
                	if (i==1) {
                		if (hasPostingPermission) {
                			html.append( "<td>"+getHyperLinkFirst()+"../crm/cards/transactionspecs.jsp?id="+rset.getString(i)+getHyperLinkMiddle()+ getValue2(rset.getString(i)) + getHyperLinkEnd() + "</td>\n");
                		} else {
                			html.append( "<td>"+ getValue2(rset.getString(i)) +"</td>\n");
                		}
                	} else if (i==2) {
                		if (hasPostingPermission) {
                			html.append( "<td>"+getHyperLinkFirst()+"../crm/finance/postingspecs.jsp?id="+rset.getString(i)+getHyperLinkMiddle()+ getValue2(rset.getString(i)) + getHyperLinkEnd() + "</td>\n");
                		} else {
                			html.append( "<td>"+ getValue2(rset.getString(i)) +"</td>\n");
                		}
                	} else {
                		html.append( "<td>"+ getValue2(rset.getString(i)) +"</td>\n");
                	}
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
    } //getPostingsGroupHTML

    public String getTransactionRejectionReportHTML(String rep_id, String pBeg, String pEnd) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null;
        String mySQL = "";
        try{
        	mySQL = 
        		" SELECT id_posting_detail, operation_date_frmt operation_date, name_currency," +
				"        debet_cd_bk_account_frmt||' '||debet_name_bk_account debet_id_bk_account,  " +
				"        credit_cd_bk_account_frmt||' '||credit_name_bk_account credit_id_bk_account, " +
				"		 entered_amount_frmt entered_amount, assignment_posting, " +
				"        name_bk_operation_type id_bk_operation_scheme_line " +
				"   FROM " + getGeneralDBScheme() + ".vc_acc_posting_temp_all" +
				" WHERE id_report = ?";
        	
        	LOGGER.debug(mySQL + 
        			", 1={" + rep_id + ",int}");
            con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st.setInt(1, Integer.parseInt(rep_id));
            ResultSet rset = st.executeQuery();

            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>\n");
            for (int i=1; i <= colCount; i++) {
            	html.append(getBottomFrameTableTH(postingXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
                for (int i=1; i <= colCount; i++) {
                	html.append( "<td>"+ getValue2(rset.getString(i)) +"</td>\n");
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
    } //getTransactionRejectionReportHTML

    public String getPostingsReportHTML(String id_report, String id_entry, String pFind, String p_beg, String p_end, String pPrint) {
        StringBuilder html = new StringBuilder();

        String myBgColor = noneBackGroundStyle;
        String myFont = "<font color=\"black\">";
    	String myFontEnd = "</font>";
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();
    	
    	PreparedStatement st = null;
        String mySQL = 	
        	" SELECT rn, id_report, type_report_tsl, date_report_frmt, state_report_tsl, state_report " +
			"   FROM (SELECT ROWNUM rn, type_report_tsl, id_report, date_report_frmt, state_report_tsl, state_report " +
        	"           FROM (SELECT id_report, type_report_tsl, date_report_frmt, state_report_tsl, state_report " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_rep_header " +
        	"                  WHERE type_report in ('POSTING', 'POSTING_ALL', 'POSTING_CARD_OPER', " +
        	"						'POSTING_CARD_PURCHASE', 'POSTING_REMITTANCE', 'POSTING_TRANS')";
        if (!isEmpty(id_report)) {
        	mySQL = mySQL + " AND id_report = ? ";
        	pParam.add(new bcFeautureParam("string", id_report));
        	p_beg = "1";
        	p_end = "10";
        }
        if (!isEmpty(id_entry)) {
        	mySQL = mySQL + " AND id_entry = ? ";
        	pParam.add(new bcFeautureParam("string", id_entry));
        	p_beg = "1";
        	p_end = "10";
        }
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + 
        		" AND (TO_CHAR(id_report) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(date_report_frmt) LIKE UPPER('%'||?||'%')) ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
    		"                  ORDER BY id_report DESC" +
    		"         ) WHERE ROWNUM < ?) " +
        	" WHERE rn >= ?";
        
        try{
        	//myReportId = object.getReportId("AP01");
        	//myReportPostingId = object.getReportId("AP04");
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();

            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>\n");
            if ("N".equalsIgnoreCase(pPrint)) {
	            html.append("<th>&nbsp;</th>\n");
	            //html.append("<th>&nbsp;</th>\n");
	            //html.append("<th>&nbsp;</th>\n");
            }
            for (int i=1; i <= colCount-1; i++) {
            	html.append(getBottomFrameTableTH(postingXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
               	if ("E".equalsIgnoreCase(rset.getString("STATE_REPORT"))) {
               		myBgColor = reportSelectedBackGroundStyle;
               		myFont = "<font color=\"red\">";
                   	myFontEnd = "</font>";
               	} else {
               		myBgColor = noneBackGroundStyle;
               		myFont = "<font color=\"black\">";
                   	myFontEnd = "</font>";
               	}
               	
               	if ("N".equalsIgnoreCase(pPrint)) {
					html.append("<td align=\"center\" "+myBgColor+">");
					if (isEmpty(id_report)) {
						html.append(getHyperLinkFirst()+"../crm/finance/postings.jsp?id_report="+rset.getString("ID_REPORT")+getHyperLinkMiddle());
					} else {
						html.append(getHyperLinkFirst()+"../crm/finance/postings.jsp?"+getHyperLinkMiddle());
					}
					html.append(myFont + "<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/postings.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ reportXML.getfieldTransl("t_content", false) +"\">" + myFontEnd);
					html.append(getHyperLinkEnd() + "</td>\n");
					
					/*
					html.append("<td align=\"center\" "+myBgColor+">");
					if (isEmpty(id_report_posting)) {
						html.append(getHyperLinkFirst()+object.getContextPath()+"/finance/postings.jsp?id_report_posting="+rset.getString("ID_REPORT")+getHyperLinkMiddle());
					} else {
						html.append(getHyperLinkFirst()+object.getContextPath()+"/finance/postings.jsp?"+getHyperLinkMiddle());
					}
					html.append(myFont + "<img vspace=\"0\" hspace=\"0\" src=\"" + object.getContextPath() + "../images/oper/rows/report_posting.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ reportXML.getfieldTransl("t_content", false) +"\">" + myFontEnd);
					html.append(getHyperLinkEnd() + "</td>\n");
					*/
					/*
	               	if (!isEmpty(myReportId)) {
	               		String myHyperLink = object.getContextPath()+"/reports/Reporter?REPORT_ID=" + myReportId + "&REPORT_FORMAT="+pReportFormat+"&ID_REPORT="+getValue2(rset.getString("ID_REPORT"));
	               		html.append(object.getReportButtonHTML(myHyperLink, "_blank"));
	               	} else {
	               		html.append("<td>&nbsp;</td>\n");
	               	}if (!isEmpty(myReportPostingId)) {
	               		String myHyperLink = object.getContextPath()+"/reports/Reporter?REPORT_ID=" + myReportPostingId + "&REPORT_FORMAT="+pReportFormat+"&ID_REPORT="+getValue2(rset.getString("ID_REPORT"));
	               		html.append(object.getReportPostingHTML(myHyperLink, "_blank"));
	               	} else {
	               		html.append("<td>&nbsp;</td>\n");
	               	}
               	*/
               	}
				
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("RN")) + myFontEnd + "</td>\n");
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("ID_REPORT")) + myFontEnd + "</td>\n");
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("TYPE_REPORT_TSL")) + myFontEnd + "</td>\n");
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("DATE_REPORT_FRMT")) + myFontEnd + "</td>\n");
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("STATE_REPORT_TSL")) + myFontEnd + "</td>\n");

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
    } //getPostingsReportHTML

    public String getCheckFinModelHTML(String typeReport, String idEntry, String id_current_report, String pFindString, String p_beg, String p_end, String pPrint) {
    	
    	String lTypeReport = "'ALL_FINANCE_MODEL_CHECK', 'CLUB_REL_CHECK', 'BK_OPERATION_CHECK'";
    	if (!isEmpty(typeReport)) {
    		lTypeReport = "'" + typeReport + "'";
    	}
    	
    	return getReportHeaderHTML(
    			lTypeReport, 
    			idEntry,
    			pFindString, 
    			id_current_report, 
    			"/finance/check_model.jsp?detail_type=REPORT", 
    			"", 
    			p_beg, 
    			p_end, 
    			pPrint
    	);
    }

    public String getCheckModelReportDetailHTML(String id_report, String pFind, String pState, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();

        String myBgColor = noneBackGroundStyle;
        String myFont = "<font color=\"black\">";
    	String myFontEnd = "</font>";
    	
    	boolean hasClubRelPermission = false;
    	boolean hasBKOperSchemePermission = false;
    	
    	ArrayList<bcFeautureParam> pParam = initParamArray();

    	PreparedStatement st = null;
        String mySQL = 	
        	" SELECT id_line, name_club_rel,  " +
			"        name_bk_operation_scheme_line, state_line_tsl, error_msg, id_club_rel, " +
			"        id_bk_operation_scheme_line, state_line " +
			"   FROM (SELECT ROWNUM rn, id_line, name_club_rel,  " +
			"                name_bk_operation_scheme_line, state_line_tsl, error_msg, id_club_rel, " +
			"                id_bk_operation_scheme_line, state_line " +
        	"           FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_rep_fin_model_check " +
        	"                  WHERE id_report = ? ";
        pParam.add(new bcFeautureParam("int", id_report));
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + 
        		" AND (UPPER(name_club_rel) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(name_bk_operation_scheme_line) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(state_line_tsl) LIKE UPPER('%'||?||'%') OR " +
        		"      UPPER(error_msg) LIKE UPPER('%'||?||'%'))";
        	for (int i=0; i<4; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
        }
        if (!isEmpty(pState)) {
        	mySQL = mySQL + " AND state_line = ? ";
        	pParam.add(new bcFeautureParam("string", pState));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + 
        	"                  ORDER BY id_line" +
        	"         ) WHERE ROWNUM < ?) " +
        	" WHERE rn >= ?";
        try{
        	if (isEditMenuPermited("FINANCE_POSTING_SCHEME")>=0) {
        		hasBKOperSchemePermission = true;
        	}
        	if (isEditMenuPermited("CLUB_RELATIONSHIP")>=0) {
        		hasClubRelPermission = true;
        	}
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();

            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>\n");
            for (int i=1; i <= colCount-3; i++) {
            	html.append(getBottomFrameTableTH(reportXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
               	if ("E".equalsIgnoreCase(rset.getString("STATE_LINE"))) {
               		myBgColor = reportSelectedBackGroundStyle;
               		myFont = "<font color=\"red\">";
                   	myFontEnd = "</font>";
               	} else if ("W".equalsIgnoreCase(rset.getString("STATE_LINE"))) {
               		myBgColor = reportSelectedBackGroundStyle;
               		myFont = "<font color=\"darkblue\">";
                   	myFontEnd = "</font>";
               	} else {
               		myBgColor = noneBackGroundStyle;
               		myFont = "<font color=\"black\">";
                   	myFontEnd = "</font>";
               	}
               	for (int i=1; i <= colCount-3; i++) {
               		if ("ID_LINE".equalsIgnoreCase(mtd.getColumnName(i))) {
               			html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString(i)) + myFontEnd + "</td>\n");
               		} else if ("NAME_BK_OPERATION_SCHEME_LINE".equalsIgnoreCase(mtd.getColumnName(i)) && 
               				!isEmpty(rset.getString(i)) &&
               				hasBKOperSchemePermission) {
               			html.append("<td "+myBgColor+" align=\"left\">"+getHyperLinkFirst()+"../crm/finance/posting_scheme_linespecs.jsp?id="+rset.getString("ID_BK_OPERATION_SCHEME_LINE")+getHyperLinkMiddle() + getValue2(rset.getString(i)) + getHyperLinkEnd() + "</td>\n");
               		} else if ("NAME_CLUB_REL".equalsIgnoreCase(mtd.getColumnName(i)) && 
               				!isEmpty(rset.getString(i)) &&
               				hasClubRelPermission) {
               			html.append("<td "+myBgColor+" align=\"left\">"+getHyperLinkFirst()+"../crm/club/relationshipspecs.jsp?id="+rset.getString("ID_CLUB_REL")+getHyperLinkMiddle() + getValue2(rset.getString(i)) + getHyperLinkEnd() + "</td>\n");
               		} else {
               			if ("STATE_LINE_TSL".equalsIgnoreCase(mtd.getColumnName(i))) {
               				html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString(i)) + myFontEnd + "</td>\n");
               			} else {
               				html.append("<td "+myBgColor+" align=\"left\">" + myFont + getValue2(rset.getString(i)) + myFontEnd + "</td>\n");
               			}
               		}
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
    } //getCheckModelReportDetailHTML
    
    public String getClearingReportHTML(String id_report, String pFind, String p_beg, String p_end, String pPrint) {
        StringBuilder html = new StringBuilder();

        String myBgColor = noneBackGroundStyle;
        String myFont = "<font color=\"black\">";
    	String myFontEnd = "</font>";
    	
    	PreparedStatement st = null;

    	ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 	
        	" SELECT rn, id_report, type_report_tsl, date_report_frmt, state_report_tsl, state_report " +
			"   FROM (SELECT ROWNUM rn, type_report_tsl, id_report, date_report_frmt, state_report_tsl, state_report " +
        	"           FROM (SELECT id_report, type_report_tsl, date_report_frmt, state_report_tsl, state_report " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_rep_header " +
        	"                  WHERE type_report = 'CLEARING'" +
        	"                  ORDER BY id_report DESC" +
        	"         ) WHERE ";
        if (!isEmpty(id_report)) {
        	mySQL = mySQL + " id_report = ? AND ";
        	pParam.add(new bcFeautureParam("int", id_report));
        	p_beg = "1";
        	p_end = "10";
        }
        if (!isEmpty(pFind)) {
        	mySQL = mySQL + 
        		" (TO_CHAR(id_report) LIKE UPPER('%'||?||'%') OR " +
        		"  date_report_frmt LIKE UPPER('%'||?||'%')) AND ";
        	for (int i=0; i<2; i++) {
        	    pParam.add(new bcFeautureParam("string", pFind));
        	}
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + " ROWNUM < ?) " +
        	" WHERE rn >= ?";
        try{
        	//myReportId = object.getReportId("CLEARING_REP2");
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);

            ResultSet rset = st.executeQuery();
            st = prepareParam(st, pParam);
            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>\n");
            if ("N".equalsIgnoreCase(pPrint)) {
            	html.append("<th>&nbsp;</th>\n");
            }
            for (int i=1; i <= colCount-1; i++) {
            	html.append(getBottomFrameTableTH(postingXML, mtd.getColumnName(i)));
            }
            /*
            if ("N".equalsIgnoreCase(pPrint)) {
            	html.append("<th>&nbsp;</th>\n");
            }
            */
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
               	if ("E".equalsIgnoreCase(rset.getString("STATE_REPORT"))) {
               		myBgColor = reportSelectedBackGroundStyle;
               		myFont = "<font color=\"red\">";
                   	myFontEnd = "</font>";
               	} else {
               		myBgColor = noneBackGroundStyle;
               		myFont = "<font color=\"black\">";
                   	myFontEnd = "</font>";
               	}
               	if ("N".equalsIgnoreCase(pPrint)) {
	               	if (isEmpty(id_report)) {
		               	html.append("<td align=\"center\" "+myBgColor+">");
						html.append(getHyperLinkFirst()+"../crm/finance/clearing.jsp?id_report="+rset.getString("ID_REPORT")+getHyperLinkMiddle());
						html.append(myFont + "<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/postings.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ reportXML.getfieldTransl("t_content", false) +"\">" + myFontEnd);
						html.append(getHyperLinkEnd() + "</td>\n");
	               	} else {
		               	html.append("<td align=\"center\" "+myBgColor+">");
						html.append(getHyperLinkFirst()+"../crm/finance/clearing.jsp"+getHyperLinkMiddle());
						html.append(myFont + "<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/postings.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ reportXML.getfieldTransl("t_content", false) +"\">" + myFontEnd);
						html.append(getHyperLinkEnd() + "</td>\n");
	               	}
               	}
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("RN")) + myFontEnd + "</td>\n");
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("ID_REPORT")) + myFontEnd + "</td>\n");
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("TYPE_REPORT_TSL")) + myFontEnd + "</td>\n");
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("DATE_REPORT_FRMT")) + myFontEnd + "</td>\n");
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("STATE_REPORT_TSL")) + myFontEnd + "</td>\n");

               	/*
               	if ("N".equalsIgnoreCase(pPrint)) {
	               	if (!isEmpty(myReportId)) {
	               		String myHyperLink = object.getContextPath()+"/reports/Reporter?REPORT_ID=" + myReportId + "&REPORT_FORMAT="+pReportFormat+"&ID_REPORT="+getValue2(rset.getString("ID_REPORT"));
	               		html.append(object.getReportButtonHTML(myHyperLink, "_blank"));
	               	} else {
	               		html.append("<td>&nbsp;</td>\n");
	               	}
               	}
               	*/
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
    } //getPostingsReportHTML

    public String getClearingReportDetailHTML(String id_report, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();

        String myBgColor = noneBackGroundStyle;
        String myFont = "<font color=\"black\">";
    	String myFontEnd = "</font>";
    	
    	boolean hasPostingPermission = false;
    	boolean hasBankAccountPermission = false;
    	
    	PreparedStatement st = null;
        String mySQL = 	
        	" SELECT line_number, number_clearing,  " +
			"        id_posting_detail, state_line_tsl, error_msg, number_bank_account, " +
			"        id_bank_account, state_line, id_clearing " +
			"   FROM (SELECT ROWNUM rn, line_number, id_clearing, number_clearing, " +
			"                id_posting_detail, state_line_tsl, error_msg, " +
			"                number_bank_account||', '||name_bank_account_type number_bank_account, id_bank_account, state_line " +
        	"           FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".vc_rep_clearing_lines " +
        	"                  WHERE id_report = ? " + 
        	"                  ORDER BY line_number" +
        	"         ) WHERE ROWNUM < ?) " +
        	" WHERE rn >= ?";
        
        try{
        	if (isEditMenuPermited("CLIENTS_BANK_ACCOUNTS")>=0) {
        		hasBankAccountPermission = true;
        	}
        	if (isEditMenuPermited("FINANCE_POSTINGS")>=0) {
        		hasPostingPermission = true;
        	}
        	
        	LOGGER.debug(mySQL + 
        			", 1={" + id_report + ",int}" + 
        			", 2={" + p_end + ",int}" + 
        			", 3={" + p_beg + ",int}");
        	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st.setInt(1, Integer.parseInt(id_report));
            st.setInt(2, Integer.parseInt(p_end));
            st.setInt(3, Integer.parseInt(p_beg));

            ResultSet rset = st.executeQuery();

            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>\n");
            for (int i=1; i <= colCount-3; i++) {
            	html.append(getBottomFrameTableTH(reportXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
               	if ("E".equalsIgnoreCase(rset.getString("STATE_LINE"))) {
               		myBgColor = reportSelectedBackGroundStyle;
               		myFont = "<font color=\"red\">";
                   	myFontEnd = "</font>";
               	} else {
               		myBgColor = noneBackGroundStyle;
               		myFont = "<font color=\"black\">";
                   	myFontEnd = "</font>";
               	}
               	for (int i=1; i <= colCount-3; i++) {
               		if ("NUMBER_BANK_ACCOUNT".equalsIgnoreCase(mtd.getColumnName(i)) && 
               				!isEmpty(rset.getString(i)) &&
               				hasBankAccountPermission) {
               			html.append("<td align=\"center\">"+getHyperLinkFirst()+"../crm/clients/accountspecs.jsp?id="+rset.getString("ID_BANK_ACCOUNT")+getHyperLinkMiddle() + getValue2(rset.getString(i)) + getHyperLinkEnd() + "</td>\n");
               		} else if ("ID_POSTING_DETAIL".equalsIgnoreCase(mtd.getColumnName(i)) && 
               				!isEmpty(rset.getString(i)) &&
               				hasPostingPermission) {
               			html.append("<td align=\"center\">"+getHyperLinkFirst()+"../crm/finance/postingspecs.jsp?id="+rset.getString("ID_POSTING_DETAIL")+getHyperLinkMiddle() + getValue2(rset.getString(i)) + getHyperLinkEnd() + "</td>\n");
               		} else if ("number_clearing".equalsIgnoreCase(mtd.getColumnName(i)) && 
               				!isEmpty(rset.getString(i))) {
               			html.append("<td align=\"center\">"+getHyperLinkFirst()+"../crm/finance/clearingspecs.jsp?id="+rset.getString("ID_CLEARING")+getHyperLinkMiddle() + getValue2(rset.getString(i)) + getHyperLinkEnd() + "</td>\n");
               		} else {
               			if ("STATE_LINE_TSL".equalsIgnoreCase(mtd.getColumnName(i))) {
               				html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString(i)) + myFontEnd + "</td>\n");
               			} else {
               				html.append("<td align=\"center\">" + getValue2(rset.getString(i)) + "</td>\n");
               			}
               		}
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
    } //getPostingsReportHTML

    public String getQuestionnaireImportReportHTML(String repType, String idEntry, String pIdReport, String pFind, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();

        String myBgColor = noneBackGroundStyle;
        String myFont = "<font color=\"black\">";
    	String myFontEnd = "</font>";
    	
    	PreparedStatement st = null;
        String myReportId = "";

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 	
        	" SELECT rn, id_report, type_report_tsl, date_report_frmt, state_report_tsl, state_report " +
			"   FROM (SELECT ROWNUM rn, type_report_tsl, id_report, date_report_frmt, state_report_tsl, state_report " +
        	"           FROM (SELECT id_report, type_report_tsl, date_report_frmt, state_report_tsl, state_report " +
        	"                   FROM " + getGeneralDBScheme();
        if ("PACK".equalsIgnoreCase(repType)) {
        	mySQL = mySQL  + ".vc_rep_header " +
        			" WHERE type_report IN ('IMPORT_QUEST_PACK') " +
        			"   AND id_entry = " + idEntry;
        	pParam.add(new bcFeautureParam("int", idEntry));
        } else {
        	mySQL = mySQL + ".vc_rep_header " +
        			" WHERE type_report IN ('IMPORT_QUEST', 'IMPORT_QUEST_PACK') " +
        			"   AND id_entry = " + idEntry;
        	pParam.add(new bcFeautureParam("int", idEntry));
        }
        if (!isEmpty(pFind)) {
            mySQL = mySQL + 
            	" AND (TO_CHAR(id_report) LIKE UPPER('%'||?||'%') OR " +
            	"  date_report_frmt LIKE UPPER('%'||?||'%')) ";
            for (int i=0; i<2; i++) {
                pParam.add(new bcFeautureParam("string", pFind));
            }
        }
        if (!isEmpty(pIdReport)) {
        	mySQL = mySQL + " AND id_report=" + pIdReport;
        	pParam.add(new bcFeautureParam("int", pIdReport));
        	p_beg = "1";
        	p_end = "10";
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL +
            "                  ORDER BY id_report DESC" +
        	"         ) WHERE ROWNUM < ?) " +
        	" WHERE rn >= ?";
        
        String lDetailFormName = "";
        if ("PACK".equalsIgnoreCase(repType)) {
        	lDetailFormName = "questionnaire_packspecs.jsp";
        } else {
        	lDetailFormName = "questionnaire_importspecs.jsp";
        }
        try{
        	myReportId = getReportId("QI01");
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();

            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>\n");
            html.append("<th>&nbsp;</th>\n");
            for (int i=1; i <= colCount-1; i++) {
            	html.append(getBottomFrameTableTH(postingXML, mtd.getColumnName(i)));
            }
            html.append("<th>&nbsp;</th>\n");
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
               	if ("E".equalsIgnoreCase(rset.getString("STATE_REPORT"))) {
               		myBgColor = reportSelectedBackGroundStyle;
               		myFont = "<font color=\"red\">";
                   	myFontEnd = "</font>";
               	} else {
               		myBgColor = noneBackGroundStyle;
               		myFont = "<font color=\"black\">";
                   	myFontEnd = "</font>";
               	}
               	html.append("<td align=\"center\" "+myBgColor+">");
               	if (!isEmpty(pIdReport)) {
               		html.append(getHyperLinkFirst()+"../crm/cards/"+lDetailFormName+"?id="+idEntry+getHyperLinkMiddle());
               	} else {
               		html.append(getHyperLinkFirst()+"../crm/cards/"+lDetailFormName+"?id="+idEntry+"&id_report="+rset.getString("ID_REPORT")+getHyperLinkMiddle());
               	}
				html.append(myFont + "<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/postings.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ reportXML.getfieldTransl("t_content", false) +"\">" + myFontEnd);
				html.append(getHyperLinkEnd() + "</td>\n");
				
				html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("RN")) + myFontEnd + "</td>\n");
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("ID_REPORT")) + myFontEnd + "</td>\n");
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("TYPE_REPORT_TSL")) + myFontEnd + "</td>\n");
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("DATE_REPORT_FRMT")) + myFontEnd + "</td>\n");
               	html.append("<td "+myBgColor+" align=\"center\">" + myFont + getValue2(rset.getString("STATE_REPORT_TSL")) + myFontEnd + "</td>\n");

               	if (!isEmpty(myReportId)) {
               		String myHyperLink = "../reports/Reporter?REPORT_ID=" + myReportId + "&REPORT_FORMAT="+this.reportFormat+"&ID_REPORT="+getValue2(rset.getString("ID_REPORT"));
               		html.append(getReportButtonHTML(myHyperLink, "_blank"));
               	} else {
               		html.append("<td>&nbsp;</td>\n");
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
    } //getPostingsReportHTML

    public String getQuestionnaireImportReportDetailHTML(String id_report, String p_beg, String p_end) {
        StringBuilder html = new StringBuilder();

        String myBgColor = noneBackGroundStyle;
        String myFont = "<font color=\"black\">";
    	String myFontEnd = "</font>";
    	
    	PreparedStatement st = null;
        String mySQL = 	
        	" SELECT line_number, id_quest_int, cd_card, surname, name, patronymic, date_of_birth, " +
			"        text_report, state_line_tsl, state_line " +
			"   FROM (SELECT ROWNUM rn, line_number, id_quest_int, cd_card, surname, name, patronymic, date_of_birth, " +
			"                text_report, state_report_line_tsl state_line_tsl, state_report_line state_line " +
        	"           FROM (SELECT * " +
        	"                   FROM " + getGeneralDBScheme() + ".v_rep_nat_prs_imp_all " +
        	"                  WHERE id_report = ?" + 
        	"                  ORDER BY line_number" +
        	"         ) WHERE ROWNUM < ?) " +
        	" WHERE rn >= ?";
        try{
        	
        	LOGGER.debug(mySQL + 
        			", 1={" + id_report + ",int}" + 
        			", 2={" + p_end + ",int}" + 
        			", 3={" + p_beg + ",int}");
        	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st.setInt(1, Integer.parseInt(id_report));
            st.setInt(2, Integer.parseInt(p_end));
            st.setInt(3, Integer.parseInt(p_beg));

            ResultSet rset = st.executeQuery();

            ResultSetMetaData mtd = rset.getMetaData();
            int colCount = mtd.getColumnCount();

            html.append(getBottomFrameTable());
            html.append("<tr>\n");
            for (int i=1; i <= colCount-1; i++) {
            	html.append(getBottomFrameTableTH(questionnaireXML, mtd.getColumnName(i)));
            }
            html.append("</tr></thead><tbody>\n");
            while (rset.next())
            {
                html.append("<tr>\n");
               	if ("E".equalsIgnoreCase(rset.getString("STATE_LINE"))) {
               		myBgColor = reportSelectedBackGroundStyle;
               		myFont = "<font color=\"red\">";
                   	myFontEnd = "</font>";
               	} else {
               		myBgColor = noneBackGroundStyle;
               		myFont = "<font color=\"black\">";
                   	myFontEnd = "</font>";
               	}
               	for (int i=1; i <= colCount-1; i++) {
               		if ("ID_QUEST_INT".equalsIgnoreCase(mtd.getColumnName(i)) && 
               				!isEmpty(rset.getString(i))) {
               			html.append("<td align=\"center\">"+getHyperLinkFirst()+"../crm/cards/questionnaire_importspecs.jsp?type=QUEST&id_profile=NEW&id="+rset.getString("ID_QUEST_INT")+getHyperLinkMiddle() + getValue2(rset.getString(i)) + getHyperLinkEnd() + "</td>\n");
               		} else {
               			String myAlign = "";
               			if ("LINE_NUMBER".equalsIgnoreCase(mtd.getColumnName(i)) ||
               					"CD_CARD".equalsIgnoreCase(mtd.getColumnName(i)) ||
               					"DATE_OF_BIRTH".equalsIgnoreCase(mtd.getColumnName(i))) {
               				myAlign = " align=\"center\"";
               			}
               			html.append("<td " + myBgColor + myAlign + ">" + myFont + getValue2(rset.getString(i)) + myFontEnd + "</td>\n");
               		}
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
    } //getPostingsReportHTML

    public String getBKOperSchemeCheckReportHTML(String idEntry, String id_current_report, String pFindString, String p_beg, String p_end, String pPrint) {
    	
    	return getReportHeaderHTML(
    			"'BK_OPERATION_CHECK'", 
    			idEntry,
    			pFindString, 
    			id_current_report, 
    			"/finance/posting_scheme_linespecs.jsp?detail_type=REPORT", 
    			"", 
    			p_beg, 
    			p_end, 
    			pPrint
    	);

    } //getPostingsReportHTML

    


    public String getBankStatementReportDetailHTML(String id_report, String pFind, String pRowType, String p_beg, String p_end, String pPrint) {
        StringBuilder html = new StringBuilder();
        PreparedStatement st = null; 
        
        String myFont = "";
        String myFontEnd = "";

        String myBgColor = noneBackGroundStyle;
        
        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL =
        	"SELECT line_report id_line, id_bank_statement_line, check_result_tsl state_line_tsl, " +
        	"       text_result error_msg, check_result" +
            "  FROM (SELECT ROWNUM rn, line_report, id_bank_statement_line, check_result_tsl, text_result, check_result" +
            "          FROM (SELECT line_report, id_bank_statement_line, check_result_tsl, " +
            "						text_result, check_result " +
            "                  FROM " + getGeneralDBScheme() + ".vc_rep_bank_statement_import " +
            "                 WHERE id_report = " + id_report;
	    if (!isEmpty(pFind)) {
	    	mySQL = mySQL + " AND (UPPER(text_result) LIKE UPPER('%'||?||'%')) ";
	    	pParam.add(new bcFeautureParam("string", pFind));
	    }
        if (!isEmpty(pRowType)) {
        	mySQL = mySQL + " and NVL(check_result,'N') = ? ";
	    	pParam.add(new bcFeautureParam("string", pRowType));
        }
        pParam.add(new bcFeautureParam("int", p_end));
        pParam.add(new bcFeautureParam("int", p_beg));
        mySQL = mySQL + " ORDER BY line_report) a WHERE ROWNUM < ?) WHERE rn >= ?";
       
        try{
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(mySQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            ResultSetMetaData mtd = rset.getMetaData();
            
            int colCount = mtd.getColumnCount();
            html.append(getBottomFrameTable());
            html.append("<tr>\n");
            for (int i=1; i <= colCount-1; i++) {
               html.append(getBottomFrameTableTH(reportXML, mtd.getColumnName(i)));
            }
            html.append("<tr></thead><tbody>\n");
            
            while (rset.next())
                {
                    html.append("<tr>");
                    if (rset.getString("CHECK_RESULT").equalsIgnoreCase("E")) {
                    	myFont = "<b><font color=\"red\">";
                    	myFontEnd = "</font></b>";
                    	myBgColor = reportSelectedBackGroundStyle;
                    } else if (rset.getString("CHECK_RESULT").equalsIgnoreCase("W")) {
                    	myFont = "<b><font color=\"brown\">";
                    	myFontEnd = "</font></b>";
                    	myBgColor = reportSelectedBackGroundStyle;
                    } else {
                    	myFont = "";
                    	myFontEnd = "";
                    	myBgColor = noneBackGroundStyle;
                    }
                    
                    html.append("<td "+myBgColor+">" + myFont + getValue2(rset.getString(1)) + myFontEnd + "</td>\n" +
                    		"<td "+myBgColor+">" + myFont + getValue2(rset.getString(2)) + myFontEnd + "</td>\n" +
                    		"<td "+myBgColor+">" + myFont + getValue2(rset.getString(3)) + myFontEnd + "</td>\n" +
                    		"<td "+myBgColor+">" + myFont + getValue2(rset.getString(4)) + myFontEnd + "</td>\n"
                    		);
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
       }
}
