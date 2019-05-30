package bc.service;

public class bcWebPosMenu extends bc.objects.bcObject {
	//private final static Logger LOGGER=Logger.getLogger(bcWebPosMenu.class);
    //public Map<String,bc.service.tabSheets> tabsheetObjHm = new HashMap<String,bc.service.tabSheets>();
    //public Map<String,bc.service.tabSheets> functionObjHm = new HashMap<String,bc.service.tabSheets>();
    //public int tabsheetCount = 0;
    //public int functionCount = 0;
    
	private String idMenuElement;
	private String nameMenuElement;
	private String typeMenuElement;
	private String titleMenuElement;
	private String tabNameMenuElement;
	private String idMenuElementParent;
	private String idPrivilegeType;
	private String relativePath;
	private String execFile;
	private String imgSrc;
	private String hasHelp;
	private String isEnable;
	private String isVisible;
	
	public bcWebPosMenu () {
	}
	
	public void addMenu(String pIdMenuElement,
			String pNameMenuElement,
			String pTitleMenuElement,
			String pTabNameMenuElement,
			String pTypeMenuElement,
			String pIdMenuElementParent,
			String pRelativePath,
			String pExecFile,
			String pIdPrivilegeType,
			String pImgSrc,
			String pHasHelp,
			String pIsEnable,
			String pIsVisible) {

        this.idMenuElement = pIdMenuElement;
        this.nameMenuElement = pNameMenuElement;
        this.titleMenuElement = pTitleMenuElement;
        this.tabNameMenuElement = pTabNameMenuElement;
        this.typeMenuElement = pTypeMenuElement;
        this.idMenuElementParent = pIdMenuElementParent;
        this.relativePath = pRelativePath;
        this.execFile = pExecFile;
        this.idPrivilegeType = pIdPrivilegeType;
        this.imgSrc = pImgSrc;
        this.hasHelp = pHasHelp;
        this.isEnable = pIsEnable;
        this.isVisible = pIsVisible;
	}
	
	/*public void addTabSheet(String pIdMenuElement,
			String pNameMenuElement,
			String pTitleMenuElement,
			String pTabNameMenuElement,
			String pIdPrivilegeType,
			String pImgSrc,
			String pHasHelp,
			String pIsEnable,
			String pIsVisible) {
		this.tabsheetCount = this.tabsheetCount + 1;
		bc.service.tabSheets oneTabSheet = new bc.service.tabSheets(
				pIdMenuElement,
				pNameMenuElement,
				pTitleMenuElement,
				pTabNameMenuElement,
				Integer.parseInt(pIdPrivilegeType),
				pHasHelp,
				pIsEnable,
				pIsVisible
		);
		tabsheetObjHm.put("" + this.tabsheetCount, oneTabSheet);
	}
	
	public void addFunction(String pIdMenuElement,
			String pNameMenuElement,
			String pTitleMenuElement,
			String pTabNameMenuElement,
			String pIdPrivilegeType,
			String pImgSrc,
			String pHasHelp,
			String pIsEnable,
			String pIsVisible) {
		this.functionCount = this.functionCount + 1;
		bc.service.tabSheets oneTabSheet = new bc.service.tabSheets(
				pIdMenuElement,
				pNameMenuElement,
				pTitleMenuElement,
				pTabNameMenuElement,
				Integer.parseInt(pIdPrivilegeType),
				pHasHelp,
				pIsEnable,
				pIsVisible
		);
		functionObjHm.put("" + this.functionCount, oneTabSheet);
	}*/
	
	public String getIdMenuElement() { if (this.idMenuElement==null) return ""; else return this.idMenuElement;}
	public String getNameMenuElement() { if (this.nameMenuElement==null) return ""; else return this.nameMenuElement;}
	public String getTitleMenuElement() { if (this.titleMenuElement==null) return ""; else return this.titleMenuElement;}
	public String getTabNameMenuElement() { if (this.tabNameMenuElement==null) return ""; else return this.tabNameMenuElement;}
	public String getTypeMenuElement() { if (this.typeMenuElement==null) return ""; else return this.typeMenuElement;}
	public String getIdMenuElementParent() { if (this.idMenuElementParent==null) return ""; else return this.idMenuElementParent;}
	public String getRelativePath() { if (this.relativePath==null) return ""; else return this.relativePath;}
	public String getExecFile() { if (this.execFile==null) return ""; else return this.execFile;}
	public String getIdPrivilegeType() { if (this.idPrivilegeType==null) return "0"; else return this.idPrivilegeType;}
	public String getImgSrc() { if (this.imgSrc==null) return ""; else return this.imgSrc;}
	public String getHasHelpFlag() { if (this.hasHelp==null) return "N"; else return this.hasHelp;}
	public String getIsEnable() { if (this.isEnable==null) return "N"; else return this.isEnable;}
	public String getIsVisible() { if (this.isVisible==null) return "N"; else return this.isVisible;}
	
	/*public boolean hasTabSheetPermission(String pTabSheetName) {
		boolean result = false;
		
		String curTab = "";
        
        for (int i=1; i <= this.tabsheetCount; i++) {
        	curTab = "" + i;
        	//System.out.println("tabsheet="+this.tabsheetObjHm.get(curTab).getTabNameMenuElement()+", priv="+this.tabsheetObjHm.get(curTab).getIdPrivilegeType());
        	if (pTabSheetName.equalsIgnoreCase(this.tabsheetObjHm.get(curTab).getTabNameMenuElement())) {
        		//System.out.println("tabsheet="+this.tabsheetObjHm.get(curTab).getTabNameMenuElement()+", priv="+this.tabsheetObjHm.get(curTab).getIdPrivilegeType());
            	String idPrivilegeType = "" + this.tabsheetObjHm.get(curTab).getIdPrivilegeType();
        		if (idPrivilegeType==null || "".equalsIgnoreCase(idPrivilegeType)) {
        			idPrivilegeType = "0";
        		}
        		if (!("0".equalsIgnoreCase(idPrivilegeType))) {
        			result = true;
        		}
        		
        		if ("N".equalsIgnoreCase(this.tabsheetObjHm.get(curTab).getIsVisible())) {
        			result = false;
        		}
        		if ("N".equalsIgnoreCase(this.tabsheetObjHm.get(curTab).getIsEnable())) {
        			result = false;
        		}
        	}
        }
		return result;
	}
	public boolean isTabSheetEnable(String pTabSheetName) {
		boolean result = false;
		
		String curTab = "";
        
        for (int i=1; i <= this.tabsheetCount; i++) {
        	curTab = "" + i;
        	if (pTabSheetName.equalsIgnoreCase(this.tabsheetObjHm.get(curTab).getTabNameMenuElement())) {
        		if ("Y".equalsIgnoreCase(this.tabsheetObjHm.get(curTab).getIsEnable())) {
					result = true;
				}
        	}
        }
		return result;
	}
	public boolean isTabSheetVisible(String pTabSheetName) {
		boolean result = false;
		
		String curTab = "";
        
        for (int i=1; i <= this.tabsheetCount; i++) {
        	curTab = "" + i;
        	if (pTabSheetName.equalsIgnoreCase(this.tabsheetObjHm.get(curTab).getTabNameMenuElement())) {
        		if ("Y".equalsIgnoreCase(this.tabsheetObjHm.get(curTab).getIsVisible())) {
					result = true;
				}
        	}
        }
		return result;
	}
	
	public boolean hasFunctionPermission(String pFunctionName) {
		boolean result = false;
		
		String curTab = "";
        
        for (int i=1; i <= this.functionCount; i++) {
        	curTab = "" + i;
        	//System.out.println("function="+this.functionObjHm.get(curTab).getTabNameMenuElement()+", priv="+this.functionObjHm.get(curTab).getIdPrivilegeType());
        	if (pFunctionName.equalsIgnoreCase(this.functionObjHm.get(curTab).getTabNameMenuElement())) {
        		//System.out.println("function="+this.functionObjHm.get(curTab).getTabNameMenuElement()+", priv="+this.functionObjHm.get(curTab).getIdPrivilegeType());
            	String idPrivilegeType = "" + this.functionObjHm.get(curTab).getIdPrivilegeType();
        		if (idPrivilegeType==null || "".equalsIgnoreCase(idPrivilegeType)) {
        			idPrivilegeType = "0";
        		}
        		if (!("0".equalsIgnoreCase(idPrivilegeType))) {
        			result = true;
        		}
        		
        		if ("N".equalsIgnoreCase(this.functionObjHm.get(curTab).getIsVisible())) {
        			result = false;
        		}
        		if ("N".equalsIgnoreCase(this.functionObjHm.get(curTab).getIsEnable())) {
        			result = false;
        		}
        	}
        }
		return result;
	}
	public boolean isFunctionEnable(String pFunctionName) {
		boolean result = false;
		
		String curTab = "";
        
        for (int i=1; i <= this.functionCount; i++) {
        	curTab = "" + i;
        	if (pFunctionName.equalsIgnoreCase(this.functionObjHm.get(curTab).getTabNameMenuElement())) {
        		if ("Y".equalsIgnoreCase(this.functionObjHm.get(curTab).getIsEnable())) {
					result = true;
				}
        	}
        }
		return result;
	}
	public boolean isFunctionVisible(String pFunctionName) {
		boolean result = false;
		
		String curTab = "";
        
        for (int i=1; i <= this.functionCount; i++) {
        	curTab = "" + i;
        	if (pFunctionName.equalsIgnoreCase(this.functionObjHm.get(curTab).getTabNameMenuElement())) {
        		if ("Y".equalsIgnoreCase(this.functionObjHm.get(curTab).getIsVisible())) {
					result = true;
				}
        	}
        }
		return result;
	}*/

}
