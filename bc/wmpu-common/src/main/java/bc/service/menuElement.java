package bc.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import bc.bcBase;
import bc.connection.Connector;

public class menuElement extends bc.objects.bcObject {
	private final static Logger LOGGER=Logger.getLogger(menuElement.class);
    private Map<String,bc.service.tabSheets> tabSheetObjHm = new HashMap<String,bc.service.tabSheets>(); // форма, значение
    private int tabSheetCount = 0;
    
	private String idMenuElement;
	private String nameMenuElement;
	private String titleMenuElement;
	private String tabNameMenuElement;
	private String idPrivilegeType;
	private String currentPageName;
	private String currentTab;
	private String hasHelp;
	private String isEnable;
	private String isVisible;
	
	public menuElement () {
		setSessionId(bcBase.SESSION_PARAMETERS.SESSION_ID.getValue() );
		setLanguage(bcBase.SESSION_PARAMETERS.LANG.getValue());
		setDateFormat(bcBase.SESSION_PARAMETERS.DATE_FORMAT.getValue());
		setGeneralDBScheme(bcBase.SESSION_PARAMETERS.GENERAL_DB_SCHEME.getValue());
		clearMenuElement();
		LOGGER.setLevel(Level.ERROR);
	}
	
	public void setMenuElement(String pIdMenuElement, String pTabNameMenuElement) {
		PreparedStatement st = null;
		PreparedStatement st2 = null;
		Connection con = null;
        boolean hasPermission = false;
        ArrayList<bcFeautureParam> pParam = initParamArray();

		clearMenuElement();
        
        String menuSQL = "";
        String tabSheetSQL = "";
        int counter = 0;
        if (pTabNameMenuElement==null || "".equalsIgnoreCase(pTabNameMenuElement)) {
        	menuSQL =
        		" SELECT * " +
        		"   FROM " + getGeneralDBScheme() + ".vc_user_menu_all " +
        		"  WHERE id_menu_element = ? " +
        		"  ORDER BY id_privilege_type"; 
        	tabSheetSQL = 
            	" SELECT * " +
            	"   FROM " + getGeneralDBScheme() + ".vc_user_menu_tabsheet_all " +
            	"  WHERE id_menu_element_parent = ? " +
            	"  ORDER BY order_number";
        	pParam.add(new bcFeautureParam("int", pIdMenuElement));
        } else {
        	menuSQL =
        		" SELECT * " +
        		"   FROM " + getGeneralDBScheme() + ".vc_user_menu_all " +
        		"  WHERE UPPER(tabname_menu_element) = UPPER(?)" +
        		"  ORDER BY id_privilege_type";
        	tabSheetSQL = 
            	" SELECT * " +
            	"   FROM " + getGeneralDBScheme() + ".vc_user_menu_tabsheet_all " +
            	"  WHERE id_menu_element_parent IN (" +
            	"		SELECT id_menu_element " +
            	"         FROM " + getGeneralDBScheme() + ".vc_user_menu_all " +
            	"        WHERE UPPER(tabname_menu_element) = UPPER(?))" +
            	"  ORDER BY order_number";
        	pParam.add(new bcFeautureParam("string", pTabNameMenuElement));
        }
        
        try{
            
            LOGGER.debug("menuElement.setMenuElement('"+pIdMenuElement+"','"+pTabNameMenuElement+"')");
        	
            LOGGER.debug(prepareSQLToLog(menuSQL, pParam));
            
            con = Connector.getConnection(getSessionId());
            st = con.prepareStatement(menuSQL);
            st = prepareParam(st, pParam);
            ResultSet rset = st.executeQuery();
            
            //LOGGER.debug("setMenuElement, menuSQL = " + menuSQL);
            while (rset.next())
            {
            	counter ++;
            	hasPermission = true;
        		this.idMenuElement = rset.getString("ID_MENU_ELEMENT");
        		this.nameMenuElement = rset.getString("NAME_MENU_ELEMENT");
        		this.titleMenuElement = rset.getString("TITLE_MENU_ELEMENT");
        		this.tabNameMenuElement = rset.getString("TABNAME_MENU_ELEMENT");
        		this.idPrivilegeType = rset.getString("ID_PRIVILEGE_TYPE");
            	this.currentPageName = rset.getString("RELATIVE_PATH")+rset.getString("EXEC_FILE");
        		this.hasHelp = rset.getString("HAS_HELP");
        		this.isEnable = rset.getString("IS_ENABLE");
        		this.isVisible = rset.getString("IS_VISIBLE");
            }

        } // try
        catch (SQLException e) {LOGGER.error("menuElement.setMenuElement() SQLException: " + e.toString());}
        catch (Exception el) {LOGGER.error("menuElement.setMenuElement() Exception: " + el.toString());}
        finally {
            try {
                if (st!=null) st.close();
            } catch (SQLException w) {w.toString();}
            try {
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}
            Connector.closeConnection(con);
        } // finally
        LOGGER.debug("menuElement.setMenuElement = " + counter);

        if (!hasPermission)  {
           	//LOGGER.debug("setMenuElement, hasPermission = false");
           	clearMenuElement();
           	this.currentPageName = "0";
        } else {
        	try {
        		//LOGGER.debug("setMenuElement, tabSheetSQL = " + tabSheetSQL);
        		LOGGER.debug(prepareSQLToLog(tabSheetSQL, pParam));
                
                con = Connector.getConnection(getSessionId());
                st2 = con.prepareStatement(tabSheetSQL);
                st2 = prepareParam(st2, pParam);
        		ResultSet rsetTabSheet = st2.executeQuery();
        		this.tabSheetCount = 0;
        		while (rsetTabSheet.next())
        		{
        			this.tabSheetCount = this.tabSheetCount + 1;
        			bc.service.tabSheets oneTabSheet = new bc.service.tabSheets(
               			rsetTabSheet.getString("ID_MENU_ELEMENT"),
               			rsetTabSheet.getString("NAME_MENU_ELEMENT"),
               			rsetTabSheet.getString("TITLE_MENU_ELEMENT"),
               			rsetTabSheet.getString("TABNAME_MENU_ELEMENT"),
               			rsetTabSheet.getInt("ID_PRIVILEGE_TYPE"),
               			rsetTabSheet.getString("HAS_HELP"),
               			rsetTabSheet.getString("IS_ENABLE"),
               			rsetTabSheet.getString("IS_VISIBLE")
        			);
        			tabSheetObjHm.put("" + this.tabSheetCount, oneTabSheet);
        		}
            	LOGGER.debug("pTabNameMenuElement="+pTabNameMenuElement+", tabs count=" + tabSheetCount);
            } // try
            catch (SQLException e) {LOGGER.error("menuElement.setMenuElement() SQLException(2): " + e.toString());}
            catch (Exception el) {LOGGER.error("menuElement.setMenuElement() Exception(2): " + el.toString());}
            finally {
                try {
                    if (st2!=null) st2.close();
                } catch (SQLException w) {w.toString();}
                try {
                    if (con!=null) con.close();
                } catch (SQLException w) {w.toString();}
                Connector.closeConnection(con);
            } // finally

        }
        //LOGGER.debug("tabSheetCount = " + tabSheetCount);
        
        LOGGER.debug("menuElement.setMenuElement() END");
	}
	
	private void clearMenuElement() {
		this.idMenuElement = "";
		this.nameMenuElement = "";
		this.titleMenuElement = "";
		this.tabNameMenuElement = "";
		this.idPrivilegeType = "";
		this.tabSheetObjHm.clear();
		this.tabSheetCount = 0;
		this.currentPageName = "0";
		this.currentTab = "0";
		this.hasHelp = "N";
	}
	
	public String getIdMenuElement() { if (this.idMenuElement==null) return ""; else return this.idMenuElement;}
	public String getNameMenuElement() { if (this.nameMenuElement==null) return ""; else return this.nameMenuElement;}
	public String getTitleMenuElement() { if (this.titleMenuElement==null) return ""; else return this.titleMenuElement;}
	public String getTabNameMenuElement() { if (this.tabNameMenuElement==null) return ""; else return this.tabNameMenuElement;}
	public String getIdPrivilegeType() { if (this.idPrivilegeType==null) return ""; else return this.idPrivilegeType;}
	public String getCurrentPage() { if (this.currentPageName==null) return ""; else return this.currentPageName;}
	public String getCurrentTab() { if (this.currentTab==null) return ""; else return this.currentTab;}
	public String getHasHelpFlag() { if (this.hasHelp==null) return "N"; else return this.hasHelp;}
	public String getIsEnable() { if (this.isEnable==null) return "N"; else return this.isEnable;}
	public String getIsVisible() { if (this.isVisible==null) return "N"; else return this.isVisible;}
	

	public String getCurrentTabIdMenuElement() {
		String return_value = "";
		if (this.tabSheetObjHm.containsKey(this.currentTab)) {
			return_value = this.tabSheetObjHm.get(this.currentTab).getIdMenuElement();
		}
        LOGGER.debug("menuElement.getCurrentTabIdMenuElement(), return_value=" + return_value);
		return return_value;
	}
	
	public String getCurrentTaHasHelpFlag() {
		String return_value = "N";
		if (this.tabSheetObjHm.containsKey(this.currentTab)) {
			return_value = this.tabSheetObjHm.get(this.currentTab).getTabHasHelpFlag();
		}
        LOGGER.debug("menuElement.getCurrentTaHasHelpFlag(), return_value=" + return_value);
		return return_value;
	}

	public void setFirstCurrentTab() {
		this.currentTab = "1";
	}

	public void setCurrentTab(String pTabSheetTabNameMenuElement) {
		String curTab = "";
        
        for (int i=1; i <= this.tabSheetCount; i++) {
        	curTab = "" + i;
        	if (pTabSheetTabNameMenuElement.equalsIgnoreCase(this.tabSheetObjHm.get(curTab).getTabNameMenuElement())) {
        		//LOGGER.debug("curTab = " + curTab);
        		this.currentTab = curTab;
                LOGGER.debug("menuElement.setCurrentTab("+pTabSheetTabNameMenuElement+"), currentTab=" + this.currentTab);
        	}
        }
	}
	
	public void setCurrentTabForId(String pTabId) {
		this.currentTab = pTabId;
        LOGGER.debug("menuElement.setCurrentTabForId(), currentTab=" + this.currentTab);
	}

	public boolean isCurrentTab(String pTabSheetTabNameMenuElement) {
		String curTab = "";
        boolean result = false;
        for (int i=1; i <= this.tabSheetCount; i++) {
        	curTab = "" + i;
        	if (pTabSheetTabNameMenuElement.equalsIgnoreCase(this.tabSheetObjHm.get(curTab).getTabNameMenuElement())) {
        		//LOGGER.debug("currentTabl = " + this.currentTab);
        		//LOGGER.debug("curTab = " + curTab);
        		if (curTab.equalsIgnoreCase(this.currentTab)) {
        			result = true;
        		}
        	}
        }
        LOGGER.debug("menuElement.isCurrentTab(), tabSheet=" + pTabSheetTabNameMenuElement + ", result=" + result);
        return result;
	}
	
	public boolean isTabSheetCurrentTab(String pTabSheetTabNameMenuElement) {
		boolean result = false;
		
		String curTab = "";
        
        for (int i=1; i <= this.tabSheetCount; i++) {
        	curTab = "" + i;
        	if (pTabSheetTabNameMenuElement.equalsIgnoreCase(this.tabSheetObjHm.get(curTab).getTabNameMenuElement())) {
        		if (curTab.equalsIgnoreCase(this.currentTab) &&
        			(this.tabSheetObjHm.get(curTab).getExistFlag())) {
        			result = true;
        		}
        	}
        }
        LOGGER.debug("menuElement.isTabSheetCurrentTab(), tabSheet=" + pTabSheetTabNameMenuElement + ", result=" + result);
		return result;
	}

	public boolean isTabSheetPermitted(String pTabSheetTabNameMenuElement) {
		boolean result = false;
		
		String curTab = "";
        
        for (int i=1; i <= this.tabSheetCount; i++) {
        	curTab = "" + i;
        	if (pTabSheetTabNameMenuElement.equalsIgnoreCase(this.tabSheetObjHm.get(curTab).getTabNameMenuElement())) {
        		if ((this.tabSheetObjHm.get(curTab).getIdPrivilegeType() > 0) &&
        			(this.tabSheetObjHm.get(curTab).getExistFlag())) {
      			  result = true;
      			}
        	}
        }
        LOGGER.debug("menuElement.isTabSheetPermitted(), tabSheet=" + pTabSheetTabNameMenuElement + ", result=" + result);
		return result;
	}

	public boolean isTabSheetEditPermitted(String pTabSheetTabNameMenuElement) {
		boolean result = false;
		
		String curTab = "";
        
        for (int i=1; i <= this.tabSheetCount; i++) {
        	curTab = "" + i;
        	if (pTabSheetTabNameMenuElement.equalsIgnoreCase(this.tabSheetObjHm.get(curTab).getTabNameMenuElement())) {
        		if ((this.tabSheetObjHm.get(curTab).getIdPrivilegeType() ==2) &&
        			(this.tabSheetObjHm.get(curTab).getExistFlag())) {
      			  result = true;
      			}
        	}
        }
        LOGGER.debug("menuElement.isTabSheetEditPermitted(), tabSheet=" + pTabSheetTabNameMenuElement + ", result=" + result);
		return result;
	}
	
	public boolean isCurrentTabAndEditPermitted(String pTabSheetTabNameMenuElement) {
		boolean result = false;
		
		String curTab = "";
        
        for (int i=1; i <= this.tabSheetCount; i++) {
        	curTab = "" + i;
        	if (pTabSheetTabNameMenuElement.equalsIgnoreCase(this.tabSheetObjHm.get(curTab).getTabNameMenuElement())) {
        		if (curTab.equalsIgnoreCase(this.currentTab)) {
        			if ((this.tabSheetObjHm.get(curTab).getIdPrivilegeType() ==2) &&
        				(this.tabSheetObjHm.get(curTab).getExistFlag())) {
        			  result = true;
        			}
        		}
        	}
        }
        LOGGER.debug("menuElement.isCurrentTabAndEditPermitted(), tabSheet=" + pTabSheetTabNameMenuElement + ", result=" + result);
		return result;
	}
	
	public boolean isCurrentTabAndPreviewPermitted(String pTabSheetTabNameMenuElement) {
		boolean result = false;
		
		String curTab = "";
        
        for (int i=1; i <= this.tabSheetCount; i++) {
        	curTab = "" + i;
        	if (pTabSheetTabNameMenuElement.equalsIgnoreCase(this.tabSheetObjHm.get(curTab).getTabNameMenuElement())) {
        		if (curTab.equalsIgnoreCase(this.currentTab)) {
        			if ((this.tabSheetObjHm.get(curTab).getIdPrivilegeType() == 1) &&
        				(this.tabSheetObjHm.get(curTab).getExistFlag())) {
        			  result = true;
        			}
        		}
        	}
        }
        LOGGER.debug("menuElement.isCurrentTabAndPreviewPermitted(), tabSheet=" + pTabSheetTabNameMenuElement + ", result=" + result);
		return result;
	}
	
	public boolean isCurrentTabAndAccessPermitted(String pTabSheetTabNameMenuElement) {
		boolean result = false;
		
		String curTab = "";
        
        for (int i=1; i <= this.tabSheetCount; i++) {
        	curTab = "" + i;
        	if (pTabSheetTabNameMenuElement.equalsIgnoreCase(this.tabSheetObjHm.get(curTab).getTabNameMenuElement())) {
        		if (curTab.equalsIgnoreCase(this.currentTab)) {
        			if ((this.tabSheetObjHm.get(curTab).getIdPrivilegeType() > 0) &&
        				(this.tabSheetObjHm.get(curTab).getExistFlag())) {
        			  result = true;
        			}
        		}
        	}
        }
        //LOGGER.debug("menuElement.isCurrentTabAndAccessPermitted(), tabSheet=" + pTabSheetTabNameMenuElement + ", result=" + result);
		return result;
	}

	protected int getTabSheetIdPrivilegeType(String pTabNumber) {
		int privilege = -1;
		if (this.tabSheetObjHm.containsKey(pTabNumber)) {
			privilege = this.tabSheetObjHm.get(pTabNumber).getIdPrivilegeType();
		}
        LOGGER.debug("menuElement.getTabSheetIdPrivilegeType(), tabNumber=" + pTabNumber + ", privilege=" + privilege);
		return privilege;
	}
	
	public String getTabSheetName(String pTabNumber) {
		String nameElement = "";
		if (this.tabSheetObjHm.containsKey(pTabNumber)) {
			nameElement = this.tabSheetObjHm.get(pTabNumber).getTabNameMenuElement();
		}
        LOGGER.debug("menuElement.getTabSheetName(), tabNumber=" + pTabNumber + ", nameElement=" + nameElement);
		return nameElement;
	}
	
	private String getTabSheetNameMenuElement(String pTabNumber) {
		String nameElement = "";
		if (this.tabSheetObjHm.containsKey(pTabNumber)) {
			nameElement = this.tabSheetObjHm.get(pTabNumber).getNameMenuElement();
		}
        LOGGER.debug("menuElement.getTabSheetNameMenuElement(), tabNumber=" + pTabNumber + ", nameElement=" + nameElement);
		return nameElement;
	}
	
	public String getTabSheetName2(String pTabSheet) {
		String curTab = "";
		String name = "";
        
		for (int i=1; i <= this.tabSheetCount; i++) {
			curTab = "" + i;
			if (pTabSheet.equalsIgnoreCase(this.tabSheetObjHm.get(curTab).getTabNameMenuElement())) {
				name = this.tabSheetObjHm.get(curTab).getNameMenuElement();
			}
		}
        LOGGER.debug("menuElement.getTabSheetName2(), tabSheet=" + pTabSheet + ", name=" + name);
		return name;
	}
	
	public void setExistFlag(String pTabSheetTabNameMenuElement, boolean pExistFlag) {
        String curTab = "";
        
		for (int i=1; i <= this.tabSheetCount; i++) {
			curTab = "" + i;
			if (pTabSheetTabNameMenuElement.equalsIgnoreCase(this.tabSheetObjHm.get(curTab).getTabNameMenuElement())) {
				this.tabSheetObjHm.get(curTab).setExistFlag(pExistFlag);
			}
		}
	}
	
	public String getTabID(String pTabSheetTabNameMenuElement) {
        String curTab = "";
        
		for (int i=1; i <= this.tabSheetCount; i++) {
			curTab = "" + i;
			if (pTabSheetTabNameMenuElement.equalsIgnoreCase(this.tabSheetObjHm.get(curTab).getTabNameMenuElement())) {
				return curTab;
			}
		}
		return curTab;
	}
	
	public String getFormTabScheeds(String pCurrentTab, String pHyperLink) {
		StringBuilder html = new StringBuilder();
		
		this.currentTab = pCurrentTab;
		boolean existFlag;
		
        html.append("<div id=\"tabs\">\n");
        html.append("<ul>\n");
        
        String curTab = "";
        String tabName = "";
        String tabTitle = "";
        
        for (int i=1; i <= this.tabSheetCount; i++) {
        	curTab = "" + i;
        	
        	tabName = this.tabSheetObjHm.get(curTab).getNameMenuElement();
        	tabTitle = this.tabSheetObjHm.get(curTab).getTitleMenuElement();
        	
        	existFlag = this.tabSheetObjHm.get(curTab).getExistFlag();
        	/*System.out.println("id="+this.tabSheetObjHm.get(curTab).getIdMenuElement()+
        			", isVisible="+this.tabSheetObjHm.get(curTab).getIsVisible()+
        			", isEnable="+this.tabSheetObjHm.get(curTab).getIsEnable()+
        			", existFlag="+this.tabSheetObjHm.get(curTab).getExistFlag());*/
        	if (existFlag) {
            	html.append("<li");
            	if (curTab.equals(pCurrentTab)) { html.append(" class=\"s\" ");} 
            	html.append(">\n");
        	
            	html.append("<div class=\"div_tabs\" onclick=\"ajaxpage('" + pHyperLink + "&tab=" + curTab + "', 'div_main')\"");
            	if (curTab.equals(pCurrentTab)) { html.append(" class=\"s\" ");}
            	html.append(">");
            	html.append("<span");
            	if (curTab.equals(pCurrentTab)) { html.append(" class=\"s\" ");}
            	if (!tabName.equalsIgnoreCase(tabTitle)) {
            		html.append(" title=\""+tabTitle+"\"");
            	}
            	html.append(">");
            	html.append(getTabSheetNameMenuElement(curTab));
				html.append("</span>\n");
				html.append("</div>\n");
				html.append("</li>\n");
        	}
        }
		html.append("</ul>\n");
		html.append("</div>\n");
        LOGGER.debug("menuElement.getFormTabScheeds(), currentTab=" + this.currentTab);
		return html.toString();
	}

	
}
