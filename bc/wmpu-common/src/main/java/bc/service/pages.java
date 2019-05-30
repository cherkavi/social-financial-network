package bc.service;

public class pages {
	
    // Количество строк на экранной странице
    private Integer pageRowCount = 50; 
    public Integer getPageRowCount()
    {
        return this.pageRowCount;
    }

    public void setPageRowCount(Integer rowCount)
    {
        this.pageRowCount = rowCount;
    }

	private String pageCurrent;
	private String pageNext;
	private String firstRowNumber;
	private String lastRowNumber;
	
	public pages (String pPageRowCount) {
		if (!(pPageRowCount==null || "".equalsIgnoreCase(pPageRowCount))) {
			this.pageRowCount = Integer.parseInt(pPageRowCount);
		}
		this.pageCurrent = "1";
		this.pageNext = "2";
		this.firstRowNumber = "1";
		this.lastRowNumber = "" + (Integer.parseInt(this.firstRowNumber) + this.pageRowCount);
	}
	
	public pages (String pPageNumber, String pPageRowCount) {
		if (!(pPageRowCount==null || "".equalsIgnoreCase(pPageRowCount))) {
			this.pageRowCount = Integer.parseInt(pPageRowCount);
		}
		if (pPageNumber==null || "".equalsIgnoreCase(pPageNumber)) {
			pPageNumber = "1";
			this.firstRowNumber = "1";
			this.lastRowNumber = "" + (Integer.parseInt(this.firstRowNumber) + this.pageRowCount);
		}
		this.pageCurrent = pPageNumber;
		this.pageNext = "" +(Integer.parseInt(this.pageCurrent) + 1);
		if (this.firstRowNumber==null || "".equalsIgnoreCase(this.firstRowNumber)) {
			this.firstRowNumber = "1";
		}
		this.firstRowNumber = "" + (Integer.parseInt(this.firstRowNumber) + 
				((Integer.parseInt(this.pageCurrent)-1)*this.pageRowCount));
		this.lastRowNumber = "" + (Integer.parseInt(this.firstRowNumber) + this.pageRowCount);
	}
	
	public String getCurrentPage() { return this.pageCurrent;}
	public String getNexPage() { return this.pageNext;}
	public String getFirstRowNumber() { return this.firstRowNumber;}
	public String getLastRowNumber() { return this.lastRowNumber;}
	
}
