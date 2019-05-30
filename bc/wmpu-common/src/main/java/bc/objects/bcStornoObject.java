package bc.objects;

import java.sql.CallableStatement;
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
import bc.service.bcFeautureParam;

public class bcStornoObject extends bcObject {
	private final static Logger LOGGER=Logger.getLogger(bcStornoObject.class);
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idRecord;
	private String storno_result = "0";
	private String storno_id_operation = "0";
	private String storno_errorString = "";

	public String getStornoResult() {
		return this.storno_result;
	}

	public void setStornoResult(String pStornoResult) {
		this.storno_result = pStornoResult;
	}

	public String getStornoIdOperation() {
		return this.storno_id_operation;
	}

	public String getStornoErrorString() {
		return this.storno_errorString;
	}
	
	public bcStornoObject() {
	}
	
	public void getShortFeature() {
		String mySQL = 
			" SELECT TO_CHAR(a.opr_sum_after,'fm9999999999990d0099') opr_sum_end, " +
	          " TO_CHAR(a.sum_pay_cash_after,'fm9999999999990d0099') sum_pay_cash_end," +
	          " TO_CHAR(a.sum_pay_card_after,'fm9999999999990d0099') sum_pay_card_end, " +
	          " TO_CHAR(a.sum_pay_bon_after,'fm9999999999990d0099') sum_pay_bon_end, " +
	          " TO_CHAR(a.sum_disc_after,'fm9999999999990d0099') sum_disc_end, " +
	          " TO_CHAR(a.sum_bon_after,'fm9999999999990d0099') sum_bon_end, " +
	          " TO_CHAR(a.sum_pay_cash_after+a.sum_pay_card_after,'fm9999999999990d0099') sum_back_user " +
	          " FROM " + getGeneralDBScheme() + ".V_REP_MANUAL_STORNO_DIF a " +
	          " WHERE id_operation = ? AND state_line<>0 AND type_trans=5";
		fieldHm = getFeatures2(mySQL, this.idRecord, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
	  public int fncBeginStornoPurchase(String id_purchase, String opr_sum, String sum_pay_bon, String ver_telgr,
	          String ver_key, String trc_level, String debug)
	    {
	       int execResult = 0;
	       StringBuilder error = new StringBuilder();
	       Connection con = null;
	       CallableStatement cs = null;
	       this.storno_result = "";
	       this.storno_id_operation = "";
	       this.storno_errorString = "";
	       /*
	        String mySQL = "{? = call FNC_MESSAGE(PACK_BC_MANUAL_OPERATIONS.storno_purchase("+
	           id_purchase+","+
	           opr_sum+","+
	           sum_pay_bon+",'"+
	           ver_telgr+"','"+
	           ver_key+"',"+
	           trc_level+",'"+
	           debug+"',?))}";
	       */ 
	       String fncMessage = "{? = call FNC_MESSAGE(PACK_BC_MANUAL_OPERATIONS.storno_purchase(?, ?, ?, ?, ?, ?, ?, ?))}";
	       try
	       {
	    	   con = Connector.getConnection(getSessionId());
	           cs = con.prepareCall(fncMessage);           
	           cs.registerOutParameter(1, java.sql.Types.VARCHAR);
	           cs.registerOutParameter(9, java.sql.Types.NUMERIC);
	           cs.setInt(2, Integer.parseInt(id_purchase));
	           //cs.setInt(2, 1310);
	           cs.setInt(3, Integer.parseInt(opr_sum));
	           //cs.setInt(3, 5400);
	           cs.setInt(4, Integer.parseInt(sum_pay_bon));
	           //cs.setInt(4, 2244);
	           cs.setString(5, ver_telgr);
	           cs.setString(6, ver_key);
	           //cs.setInt(7, Integer.parseInt(trc_level));
	           cs.setInt(7, 2);
	           cs.setString(8, debug);
	           cs.execute();
	           String result1 = cs.getString(1);
	           this.storno_result = result1;
	           int result9 = cs.getInt(9);
	           this.storno_id_operation = new Integer(result9).toString();
	           cs.close();
	           execResult = 0;
	       } catch (SQLException e) {error.append("SQL:"+e.toString()); execResult=-1;}
	         catch (Exception el) {error.append(el.toString()); execResult=-2;}
	         finally {
	             try {
	                 if (cs!=null) cs.close();
	             } catch (SQLException w) {w.toString();}
	             try {
	                 if (con!=null) con.close();
	             } catch (SQLException w) {w.toString();}
	             Connector.closeConnection(con);
	         } // finally
	       return execResult;
	    } // fncBeginStornoPurchase()
	  
	  public int fncApplyStorno(String id_purchase) {
	      int execResult = 0;
	      StringBuilder error = new StringBuilder();
	      Connection con = null;
	      CallableStatement cs = null;
	      this.storno_result = "";
	      this.storno_id_operation = "";
	      this.storno_errorString = "";
	      String mySQL = "{? = call FNC_MESSAGE(PACK_BC_MANUAL_OPERATIONS.apply_storno_purchase("+
	          id_purchase+"))}";
	      try
	      {
	    	  con = Connector.getConnection(getSessionId());
	         cs = con.prepareCall(mySQL);
	         cs.registerOutParameter(1, java.sql.Types.VARCHAR);
	         cs.execute();
	         this.storno_result = cs.getString(1);
	         this.storno_id_operation = cs.getString(2);
	         
	         cs.close();
	         execResult = 0;
	      } catch (SQLException e) {error.append("SQL:"+e.toString()); execResult=-1;}
	        catch (Exception el) {error.append(el.toString()); execResult=-2;}
	        finally {
	            try {
	                if (cs!=null) cs.close();
	            } catch (SQLException w) {w.toString();}
	            try {
	                if (con!=null) con.close();
	            } catch (SQLException w) {w.toString();}
	            Connector.closeConnection(con);
	        } // finally
	      return execResult;
	  } // fncApplyStorno()
	  
	  public int fncDeleteStorno(String id_purchase){
	      int execResult = 0;
	      StringBuilder error = new StringBuilder();
	      Connection con = null;
	      CallableStatement cs = null;
	      this.storno_result = "";
	      this.storno_id_operation = "";
	      this.storno_errorString = "";
	      String mySQL = "{? = call FNC_MESSAGE(PACK_BC_MANUAL_OPERATIONS.cancel_storno_purchase("+
	          id_purchase+"))}";
	      try
	      {
	    	  con = Connector.getConnection(getSessionId());
	          cs = con.prepareCall(mySQL);
	          cs.registerOutParameter(1, java.sql.Types.VARCHAR);
	          cs.execute();
	          this.storno_result = cs.getString(1);
	          this.storno_id_operation = cs.getString(2);
	          
	          cs.close();
	          execResult = 0;
	      } catch (SQLException e) {error.append("SQL:"+e.toString()); execResult=-1;}
	        catch (Exception el) {error.append(el.toString()); execResult=-2;}
	        finally {
	            try {
	                if (cs!=null) cs.close();
	            } catch (SQLException w) {w.toString();}
	            try {
	                if (con!=null) con.close();
	            } catch (SQLException w) {w.toString();}
	            Connector.closeConnection(con);
	        } // finally
	      return execResult;
	  } // fncDeleteStorno()

	  public String getStornoResultShortHTML(String id_res) {
	      StringBuilder html = new StringBuilder();
	      Connection con = null;
	      PreparedStatement st = null;
	      ArrayList<bcFeautureParam> pParam = initParamArray();

	      String mySQL=" SELECT TO_CHAR(a.opr_sum_after,'fm9999999999990d0099') opr_sum_end, " +
	          " TO_CHAR(a.sum_pay_cash_after,'fm9999999999990d0099') sum_pay_cash_end," +
	          " TO_CHAR(a.sum_pay_card_after,'fm9999999999990d0099') sum_pay_card_end, " +
	          " TO_CHAR(a.sum_pay_bon_after,'fm9999999999990d0099') sum_pay_bon_end, " +
	          " TO_CHAR(a.sum_disc_after,'fm9999999999990d0099') sum_disc_end, " +
	          " TO_CHAR(a.sum_bon_after,'fm9999999999990d0099') sum_bon_end, " +
	          " TO_CHAR(a.sum_pay_cash_after+a.sum_pay_card_after,'fm9999999999990d0099') sum_back_user " +
	          " FROM " + getGeneralDBScheme() + ".v_rep_manual_storno_dif a " +
	          " WHERE id_operation = ? AND state_line<>0 AND type_trans=5";
	      pParam.add(new bcFeautureParam("int", id_res));
	      try{
	    	  
	    	  LOGGER.debug(prepareSQLToLog(mySQL, pParam));
	    	  con = Connector.getConnection(getSessionId());
	    	  st = con.prepareStatement(mySQL);
	    	  st = prepareParam(st, pParam);
	    	  ResultSet rset = st.executeQuery();

	          ResultSetMetaData mtd = rset.getMetaData();
	          
	          int colCount = mtd.getColumnCount();
	          html.append("<table width=100% class=\"tablesorter\" id=\"id_table\"\n>");
	          html.append("<thead><tr>\n");

	          for (int i=1; i <= colCount; i++) {
	        	  html.append(getBottomFrameTableTH(stornoXML, mtd.getColumnName(i)));
	          }
	          html.append("</tr></thead><tbody>\n");
	          
	          while (rset.next())
	          {
	              html.append("<tr>" +
	                      "<td><a href=\"../crm/cards/stornospecs.jsp?id="+rset.getString(1)+"\" target=\"frm_main\">"+rset.getString(1)+"</a></td>\n" +
	                      "<td><a href=\"../crm/cards/stornospecs.jsp?id="+rset.getString(1)+"\" target=\"frm_main\">"+rset.getString(2)+"</a></td>\n" +
	                      "<td><a href=\"../crm/cards/stornospecs.jsp?id="+rset.getString(1)+"\" target=\"frm_main\">"+rset.getString(3)+"</a></td>\n" +
	                      "<td><a href=\"../crm/cards/stornospecs.jsp?id="+rset.getString(1)+"\" target=\"frm_main\">"+rset.getString(4)+"</a></td>\n" +
	                      "<td><a href=\"../crm/cards/stornospecs.jsp?id="+rset.getString(1)+"\" target=\"frm_main\">"+rset.getString(5)+"</a></td>\n" +
	                      "<td><a href=\"../crm/cards/stornospecs.jsp?id="+rset.getString(1)+"\" target=\"frm_main\">"+rset.getString(6)+"</a></td>\n" +
	                      "<td><a href=\"../crm/cards/stornospecs.jsp?id="+rset.getString(1)+"\" target=\"frm_main\">"+rset.getString(7)+"</a></td></tr>\n");
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
	  } // getStornoResultShortHTML


}
