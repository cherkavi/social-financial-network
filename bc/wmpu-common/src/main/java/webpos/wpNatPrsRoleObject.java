package webpos;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import bc.connection.Connector;
import bc.service.bcFeautureParam;

public class wpNatPrsRoleObject extends wpObject {
	private final static Logger LOGGER=Logger.getLogger(wpNatPrsRoleObject.class);
    
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idNatPrsRole;
	
	public wpNatPrsRoleObject(String pIdNatPrsRole) {
		this.idNatPrsRole = pIdNatPrsRole;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vp$nat_prs_role_all WHERE id_nat_prs_role = ?";
		fieldHm = getFeatures2(featureSelect, this.idNatPrsRole, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
	private int fileCount = 0;
	
	public int getFileCount() {
		return fileCount;
	}
	
    public String getDocumentsListHTML() {
        StringBuilder html = new StringBuilder();
        Connection con = null;
        PreparedStatement st = null;
        String src_doc = "";

        ArrayList<bcFeautureParam> pParam = initParamArray();

        String mySQL = 
        	" SELECT id_nat_prs_role_file, file_name, stored_file_name " +
            "   FROM " + getGeneralDBScheme() + ".v_nat_prs_role_file_all " + 
            "  WHERE id_nat_prs_role = ? ";
        pParam.add(new bcFeautureParam("int", this.idNatPrsRole));
        
        this.fileCount = 0;
        String lFileName = "";
        try{
        	
        	LOGGER.debug(prepareSQLToLog(mySQL, pParam));
        	con = Connector.getConnection(getSessionId());
        	st = con.prepareStatement(mySQL);
        	st = prepareParam(st, pParam);
        	ResultSet rset = st.executeQuery();
        	
        	int lMaxLength = 25;

            while (rset.next())
            {
            	this.fileCount ++;
            	lFileName = rset.getString("FILE_NAME");
            	System.out.println("1. lFileName = "+lFileName);
            	if (lFileName.length() > lMaxLength) {
            		int lastPointIndex = lFileName.lastIndexOf(".");
                	System.out.println("2. lastPointIndex = "+lastPointIndex);
            		if (lastPointIndex > 0) {
            			String lExtension = lFileName.substring(lastPointIndex);
            			String lName = lFileName.substring(0, lastPointIndex-1-1);
                    	System.out.println("3. lName = "+lName+", lExtension = "+lExtension);
            			if (lExtension.length() > 5) {
            				lExtension = lExtension.substring(0, 3)+ "...";
                        	System.out.println("4. lExtension = "+lExtension);
            				if (lName.length() > lMaxLength - lExtension.length()) {
            					lName = lName.substring(0, lMaxLength - lExtension.length() - 3) + "...";
                            	System.out.println("5. lName = "+lName);
            				}
            				lFileName = lName + lExtension;
                        	System.out.println("6. lFileName = "+lFileName);
            			} else {
            				if (lName.length() > lMaxLength - lExtension.length()) {
            					lName = lName.substring(0, lMaxLength - lExtension.length() - 3) + "...";
                            	System.out.println("7. lName = "+lName);
            				}
            				lFileName = lName + lExtension;      
                        	System.out.println("8. lFileName = "+lFileName);      				
            			}
            		} else {
            			lFileName = lFileName.substring(0, lMaxLength-3) + "...";
                    	System.out.println("9. lFileName = "+lFileName);     
            		}
            	}
            	if (this.fileCount > 1) {
            		html.append("<br>");            		
            	}
            	if (!isEmpty(rset.getString("FILE_NAME"))) {
            		src_doc = "<a href=\"../FileSender?FILENAME=" + URLEncoder.encode(rset.getString("STORED_FILE_NAME"),"UTF-8") + "\" title=\"" + buttonXML.getfieldTransl("open", false) + "\" target=_blank>" +
            				  "<img vspace=\"0\" hspace=\"0\" src=\"images/oper/open.gif\" align=\"top\" style=\"border: 0px;\">" +
            				  "</a>";
            	} else {
            		src_doc = "";
            	}
            	html.append("<spant title=\""+rset.getString("FILE_NAME")+"\">" + lFileName + "</span>&nbsp;" + src_doc);
         	   	String myDeleteLink = "action/new_client_questionnaire.jsp?id_role=" + this.idNatPrsRole + "&id_doc=" + rset.getString("ID_NAT_PRS_ROLE_FILE")+"&action=del_doc";
         	   	html.append(getWebPosDeleteFileImageHTML(myDeleteLink, "div_action_big", documentXML.getfieldTransl("l_remove_doc", false) + " \\\'" + rset.getString("FILE_NAME") + "\\\'"));
         	   
            }
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
    } // getDocumentsListHTML

}
