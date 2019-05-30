package bc.ajax;

public class relations {
	public relations(){}
	
	private String idClubRel;
	private String idDoc;
	private String idComission;
	private String cdComissionType;
	private String[] documentListId;
	private String[] documentListName;
	private String[] comissionListId;
	private String[] comissionListName;
	private String[] comissionTypeListCode;
	private String[] comissionTypeListName;
	
	private String sessionId;
	
	public String getIdClubRel() {
		return this.idClubRel;
	}
	
	public void setIdClubRel(String pIdClubRel) {
		this.idClubRel = pIdClubRel;
	}
	
	public String getIdDoc() {
		return this.idDoc;
	}
	
	public void setIdDoc(String pIdDoc) {
		this.idDoc = pIdDoc;
	}
	
	public String getIdComission() {
		return this.idComission;
	}
	
	public void setIdComission(String pIdComission) {
		this.idComission = pIdComission;
	}
	
	public String getCdComissionType() {
		return this.cdComissionType;
	}
	
	public void setCdComissionType(String pCdComissionType) {
		this.cdComissionType = pCdComissionType;
	}
	
	public String[] getDocumentListId() {
		return this.documentListId;
	}
	
	public void setDocumentListId(String[] pDocumentListId) {
		this.documentListId = pDocumentListId;
	}
	
	public String[] getDocumentListName() {
		return this.documentListName;
	}
	
	public void setDocumentListName(String[] pDocumentListName) {
		this.documentListName = pDocumentListName;
	}
	
	public String[] getComissionListId() {
		return this.comissionListId;
	}
	
	public void setComissionListId(String[] pComissionListId) {
		this.comissionListId = pComissionListId;
	}
	
	public String[] getComissionListName() {
		return this.comissionListName;
	}
	
	public void setComissionListName(String[] pComissionListName) {
		this.comissionListName = pComissionListName;
	}
	
	public String[] getComissionTypeListCode() {
		return this.comissionTypeListCode;
	}
	
	public void setComissionTypeListCode(String[] pComissionTypeListCode) {
		this.comissionTypeListCode = pComissionTypeListCode;
	}
	
	public String[] getComissionTypeListName() {
		return this.comissionTypeListName;
	}
	
	public void setComissionTypeListName(String[] pComissionTypeListName) {
		this.comissionTypeListName = pComissionTypeListName;
	}
	
	public String getSessionId(){
		return this.sessionId;
	}
	
	public void setSessionId(String value){
		this.sessionId=value;
	}
}
