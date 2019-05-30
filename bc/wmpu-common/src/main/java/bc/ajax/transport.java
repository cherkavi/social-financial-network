package bc.ajax;

public class transport {
	public transport(){}
	
	private String functionName;
	private String functionParam;
	private String functionParam2;
	private String functionParam3;
	private String functionParam4;
	private String idDestination;
	private String[] functionKeys;
	private String[] functionValues;
	
	private String functionHTMLReturnValue;
	private String language;
	private String pageNumber;
	
	private String sessionId;
	private String pageRowCount;
	
	public String getFunctionName() {
		return this.functionName;
	}
	
	public void setFunctionName(String pFunctionName) {
		this.functionName = pFunctionName;
	}
	
	public String getFunctionParam() {
		return this.functionParam;
	}
	
	public void setFunctionParam(String pFunctionParam) {
		this.functionParam = pFunctionParam;
	}
	
	public String getFunctionParam2() {
		return this.functionParam2;
	}
	
	public void setFunctionParam2(String pFunctionParam2) {
		this.functionParam2 = pFunctionParam2;
	}
	
	public String getFunctionParam3() {
		return this.functionParam3;
	}
	
	public void setFunctionParam3(String pFunctionParam3) {
		this.functionParam3 = pFunctionParam3;
	}
	
	public String getFunctionParam4() {
		return this.functionParam4;
	}
	
	public void setFunctionParam4(String pFunctionParam4) {
		this.functionParam4 = pFunctionParam4;
	}
	
	public String[] getFunctionKeys() {
		return this.functionKeys;
	}
	
	public void setFunctionKeys(String[] pFunctionKeys) {
		this.functionKeys = pFunctionKeys;
	}
	
	public String[] getFunctionValues() {
		return this.functionValues;
	}
	
	public void setFunctionValues(String[] pFunctionValues) {
		this.functionValues = pFunctionValues;
	}
	
	public String getIdDestination(){
		return this.idDestination;
	}
	
	public void setIdDestination(String value){
		this.idDestination=value;
	}
	
	public String getFunctionHTMLReturnValue(){
		return this.functionHTMLReturnValue;
	}
	
	public void setFunctionHTMLReturnValue(String value){
		this.functionHTMLReturnValue=value;
	}
	
	public String getLanguage(){
		return this.language;
	}
	
	public void setLanguage(String value){
		this.language=value;
	}
	
	public String getPageNumber(){
		return this.pageNumber;
	}
	
	public void setPageNumber(String value){
		this.pageNumber=value;
	}
	
	public String getSessionId(){
		return this.sessionId;
	}
	
	public void setSessionId(String value){
		this.sessionId=value;
	}
	
	public String getPageRowCount(){
		return this.pageRowCount;
	}
	
	public void setPageRowCount(String value){
		this.pageRowCount=value;
	}
}
