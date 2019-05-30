package bc.service;

public class tabSheets {
	
	private String idMenuElement;
	private String nameMenuElement;
	private String titleMenuElement;
	private String tabNameMenuElement;
	private String hasHelp;
	private int idPrivilegeType;
	private String isEnable;
	private String isVisible;
	private boolean existFlag = false;
	
	public tabSheets (
			String pIdMenuElement, 
			String pNameMenuElement, 
			String pTitleMenuElement, 
			String pTabNameMenuElement, 
			int pIdPrivilegeType, 
			String pHasHelp, 
			String pIsEnable, 
			String pIsVisible) {
		this.idMenuElement = pIdMenuElement;
		this.nameMenuElement = pNameMenuElement;
		this.titleMenuElement = pTitleMenuElement;
		this.tabNameMenuElement = pTabNameMenuElement;
		this.idPrivilegeType = pIdPrivilegeType;
		this.hasHelp = pHasHelp;
		this.isEnable = (pIsEnable==null || "".equalsIgnoreCase(pIsEnable))?"N":pIsEnable;
		this.isVisible = (pIsVisible==null || "".equalsIgnoreCase(pIsVisible))?"N":pIsVisible;
		if ("Y".equalsIgnoreCase(pIsEnable) && "Y".equalsIgnoreCase(pIsVisible)) {
			existFlag = true;
		}
	}
	
	public String getIdMenuElement() { if (this.idMenuElement==null) return ""; else return this.idMenuElement;}
	public String getNameMenuElement() { if (this.nameMenuElement==null) return ""; else return this.nameMenuElement;}
	public String getTitleMenuElement() { if (this.titleMenuElement==null) return ""; else return this.titleMenuElement;}
	public String getTabNameMenuElement() { if (this.tabNameMenuElement==null) return ""; else return this.tabNameMenuElement;}
	public String getTabHasHelpFlag() { if (this.hasHelp==null) return "N"; else return this.hasHelp;}
	public int getIdPrivilegeType() { return this.idPrivilegeType;}
	public boolean getExistFlag() {
		if (this.isEnable==null || "".equalsIgnoreCase(this.isEnable) || "N".equalsIgnoreCase(this.isEnable)) {
			this.existFlag = false;
		}
		if (this.isVisible==null || "".equalsIgnoreCase(this.isVisible) || "N".equalsIgnoreCase(this.isVisible)) {
			this.existFlag = false;
		}
		return this.existFlag;
	}
	public void setExistFlag(boolean pExistFlag) { this.existFlag = pExistFlag; }
	public String getIsEnable() { if (this.isEnable==null) return "N"; else return this.isEnable;}
	public String getIsVisible() { if (this.isVisible==null) return "N"; else return this.isVisible;}
	
}
