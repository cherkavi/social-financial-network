package bc.objects;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.connection.Connector;


public class bcBankStatementHeaderIntObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcBankStatementHeaderIntObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idBankStatement;
	
	public bcBankStatementHeaderIntObject(String pIdBankStatement) {
		this.idBankStatement = pIdBankStatement;
	}
	
	public void getFeature() {
		String featureSelect = 
			" SELECT * FROM " + getGeneralDBScheme() + ".VC_BS_HEADERS_INT_CLUB_ALL WHERE id_bank_statement = ?";
		fieldHm = getFeatures2(featureSelect, this.idBankStatement, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

	  public String getBankStatementLinesIntHTML(String p_beg, String p_end) {
		  StringBuilder html = new StringBuilder();
	      PreparedStatement st = null;
	      Connection con = null;
	      String mySQL = 
	    	  " SELECT line_number, mfo_bank_correspondent,  "+
	          "        name_bank_branch_correspondent, num_bank_account_correspondent, " +
	          "        inn_number_jur_prs_corr, name_jur_prs_correspondent, cd_operation, date_operation, "+ 
	          "        debet_amount, credit_amount, number_document, date_document, " +
	          "        payment_assignment, record_status_flag_tsl, id_bank_statement_line, record_status_flag " +
	          "   FROM (SELECT ROWNUM rn, line_number, corr_name name_jur_prs_correspondent, " +
	          "        		   corr_inn_number inn_number_jur_prs_corr,  "+
	          "        		   corr_bank_account_number num_bank_account_correspondent, " +
	          "        		   corr_bank_name name_bank_branch_correspondent, " +
	          "        		   corr_bank_mfo mfo_bank_correspondent, " +
	          "        		   operation_code cd_operation, operation_date_frmt date_operation, "+ 
	          "        		   debet_amount_frmt debet_amount, credit_amount_frmt credit_amount, " +
	          "        		   doc_number number_document, doc_date_frmt date_document, " +
	          "        		   assignment payment_assignment, record_status_flag_tsl, " +
	          "                id_bank_statement_line, record_status_flag " +
	          "   		  FROM (SELECT * " +
	          "                   FROM " + getGeneralDBScheme() + ".vc_bs_lines_int_club_all " +
	          "  		         WHERE id_bank_statement = ? " +
	          "                  ORDER BY line_number" +
	          "                ) WHERE ROWNUM < ?" +  
	          " ) WHERE rn >= ?";

	      boolean hasLineEditPerm = false;
	      
	      String myFont = "";
          String myBgColor = noneBackGroundStyle;
	      
	      try{
	    	  if (isEditPermited("FINANCE_BSIMPORT_LINES")>0) {
	    		  hasLineEditPerm = true;
	    	  }
	    	  
	    	  LOGGER.debug(mySQL + 
		    			", 1={" + this.idBankStatement + ",int}" + 
		    			", 2={" + p_end + ",int}" + 
		    			", 3={" + p_beg + ",int}");
	    	  con = Connector.getConnection(getSessionId());
	    	  st = con.prepareStatement(mySQL);
	    	  st.setInt(1, Integer.parseInt(this.idBankStatement));
	    	  st.setInt(2, Integer.parseInt(p_end));
	    	  st.setInt(3, Integer.parseInt(p_beg));
	          ResultSet rset = st.executeQuery();

	          ResultSetMetaData mtd = rset.getMetaData();
	          int colCount = mtd.getColumnCount();

	          html.append(getBottomFrameTable());
	          html.append("<tr>");
	          for (int i=1; i <= colCount-2; i++) {
	        	  html.append(getBottomFrameTableTH(bank_statementXML, mtd.getColumnName(i)));
	          }
	          if (hasLineEditPerm) {
	        	  html.append("<th>&nbsp;</th><th>&nbsp;</th>\n");
	          }
	          html.append("</tr></thead><tbody>");
	          while (rset.next())
	          {
	              html.append("<tr>");
            	  if ("E".equalsIgnoreCase(rset.getString("RECORD_STATUS_FLAG"))) {
            		  myFont = "<font color=\"red\">";
              		  myBgColor = selectedBackGroundStyle;
            	  } else {
            		  myFont = "";
              		  myBgColor = noneBackGroundStyle;
            	  }
	              for (int i=1; i <= colCount-2; i++) {
	                  html.append(getBottomFrameTableTD(mtd.getColumnName(i), rset.getString(i), "", myFont, myBgColor));
	              }
            	  if (hasLineEditPerm) {
            		  String myHyperLink = "../crm/finance/bsimportupdate.jsp?id="+this.idBankStatement+"&line="+rset.getString("ID_BANK_STATEMENT_LINE")+"&type=line";
            		  String myDeleteLink = "../crm/finance/bsimportupdate.jsp?id="+this.idBankStatement+"&line="+rset.getString("ID_BANK_STATEMENT_LINE")+"&type=line&action=remove&process=yes";
            		  html.append(getDeleteButtonStyle2HTML(myDeleteLink, myBgColor, bank_statementXML.getfieldTransl("h_delete_line", false), rset.getString("LINE_NUMBER")));
            		  html.append(getEditButtonStyleHTML(myHyperLink, getLanguage(), myBgColor));
            	  }
	              html.append("</tr>\n");
	          }
	          html.append("</tbody></table>\n");

	      } // try
	      catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
	        catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
	      finally {
	            try {
	                if (st!=null) {
						st.close();
					}
	            } catch (SQLException w) {w.toString();}
	            try {
	                if (con!=null) {
						con.close();
					}
	            } catch (SQLException w) {w.toString();}
	            Connector.closeConnection(con);
	        } // finally
	      return html.toString();
	  } //getBankStatementLinesIntHTML

	  public String runBankStatementImport (String bs_number, String imp_type) {
		  StringBuilder html = new StringBuilder();
	      int result = -1;
	      CallableStatement cs = null;
	      Connection con = null;
	      String reportId = "";
	      try
	      {
	    	  con = Connector.getConnection(getSessionId());

	    	  if (imp_type=="2") {
	    		  cs = con.prepareCall("{? = call PACK_UI_BANK_STATEMENT_IMP.run_bank_statement_import(?,?,NULL,'N',?,?)}");
	    		  cs.registerOutParameter(1, Types.NUMERIC);
	    		  cs.setString(2,bs_number);
	    		  cs.setString(3,imp_type);
	    		  cs.registerOutParameter(4, Types.NUMERIC);
	    		  cs.registerOutParameter(5, Types.VARCHAR);
	    	  } else {
	    		  cs = con.prepareCall("{? = call PACK_UI_BANK_STATEMENT_IMP.run_bank_statement_import(NULL,?,NULL,'N',?,?)}");
	    		  cs.registerOutParameter(1, Types.NUMERIC);
	    		  cs.setString(2,imp_type);
	    		  cs.registerOutParameter(3, Types.NUMERIC);
	    		  cs.registerOutParameter(4, Types.VARCHAR);
	    	  }
	    	  cs.execute();
	    	  result = cs.getInt(1);
	    	  reportId = new Integer(result).toString();
	    	  cs.close();
	      }
	      catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
	        catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
		  finally {
	            try {
	                if (cs!=null) {
						cs.close();
					}
	            } catch (SQLException w) {w.toString();}
	            try {
	                if (con!=null) {
						con.close();
					}
	            } catch (SQLException w) {w.toString();}
	            Connector.closeConnection(con);
	        } // finally
	      return reportId;
	  } // runBankStatementImport

	  public String getBankStatementFileLinesHTML(String p_beg, String p_end) {
		  StringBuilder html = new StringBuilder();
	      PreparedStatement st = null;
	      Connection con = null;
	      String mySQL = 
	    	  " SELECT line_number, line_text " +
	          "   FROM (SELECT ROWNUM rn, line_number, line_text " +
	          "   		  FROM (SELECT line_number, line_text " +
	          "                   FROM " + getGeneralDBScheme() + ".vc_bs_file_lines " +
	          "  		         WHERE id_bank_statement_file = ? " + 
	          "                  ORDER BY line_number" +
	          "                ) WHERE ROWNUM < ? " + 
	          " ) WHERE rn >= ?";
	      try{
	    	  
	    	  LOGGER.debug(mySQL + 
		    			", 1={" + this.getValue("ID_BANK_STATEMENT_FILE") + ",int}" + 
		    			", 2={" + p_end + ",int}" + 
		    			", 3={" + p_beg + ",int}");
	    	  con = Connector.getConnection(getSessionId());
	    	  st = con.prepareStatement(mySQL);
	    	  st.setInt(1, Integer.parseInt(this.getValue("ID_BANK_STATEMENT_FILE")));
	    	  st.setInt(2, Integer.parseInt(p_end));
	    	  st.setInt(3, Integer.parseInt(p_beg));
	          ResultSet rset = st.executeQuery();

	          ResultSetMetaData mtd = rset.getMetaData();
	          int colCount = mtd.getColumnCount();

	          html.append(getBottomFrameTable());
	          html.append("<tr>");
	          for (int i=1; i <= colCount; i++) {
	        	  html.append(getBottomFrameTableTH(bank_statementXML, mtd.getColumnName(i)));
	          }
	          html.append("</tr></thead><tbody>");
	          while (rset.next())
	          {
	              html.append("<tr>");
	              for (int i=1; i <= colCount; i++) {
	            	  html.append(getBottomFrameTableTDBase("", mtd.getColumnName(i), mtd.getColumnType(i), rset.getString(i), "", "", ""));
	              }
	              html.append("</tr>\n");
	          }
	          html.append("</tbody></table>\n");

	      } // try
	      catch (SQLException e) {LOGGER.error(html, e); html.append(showCRMException(e));}
	        catch (Exception el) {LOGGER.error(html, el); html.append(showCRMException(el));}
	      finally {
	            try {
	                if (st!=null) {
						st.close();
					}
	            } catch (SQLException w) {w.toString();}
	            try {
	                if (con!=null) {
						con.close();
					}
	            } catch (SQLException w) {w.toString();}
	            Connector.closeConnection(con);
	        } // finally
	      return html.toString();
	  } //getBankStatementLinesIntHTML

}
