package bc.ajax;

public class cardCategories {
	public cardCategories(){}
	
	private String categoryPrefix;
	private String idCardStatus;
	private String idBonCategory;
	private String idDiscCategory;
	private String[] bonCategoriesId;
	private String[] bonCategoriesName;
	private String[] discCategoriesId;
	private String[] discCategoriesName;
	
	private String sessionId;
	
	public String getCategoryPrefix() {
		return this.categoryPrefix;
	}
	
	public void setCategoryPrefix(String pCategoryPrefix) {
		this.categoryPrefix = pCategoryPrefix;
	}
	
	public String getIdCardStatus() {
		return this.idCardStatus;
	}
	
	public void setIdCardStatus(String pIdCardStatus) {
		this.idCardStatus = pIdCardStatus;
	}
	
	public String getIdBonCategory() {
		return this.idBonCategory;
	}
	
	public void setIdBonCategory(String pIdBonCategory) {
		this.idBonCategory = pIdBonCategory;
	}
	
	public String getIdDiscCategory() {
		return this.idDiscCategory;
	}
	
	public void setIdDiscCategory(String pIdDiscCategory) {
		this.idDiscCategory = pIdDiscCategory;
	}
	
	public String[] getBonCategoriesId() {
		return this.bonCategoriesId;
	}
	
	public void setBonCategoriesId(String[] pBonCategoriesId) {
		this.bonCategoriesId = pBonCategoriesId;
	}
	
	public String[] getBonCategoriesName() {
		return this.bonCategoriesName;
	}
	
	public void setBonCategoriesName(String[] pBonCategoriesName) {
		this.bonCategoriesName = pBonCategoriesName;
	}
	
	public String[] getDiscCategoriesId() {
		return this.discCategoriesId;
	}
	
	public void setDiscCategoriesId(String[] pDiscCategoriesId) {
		this.discCategoriesId = pDiscCategoriesId;
	}
	
	public String[] getDiscCategoriesName() {
		return this.discCategoriesName;
	}
	
	public void setDiscCategoriesName(String[] pDiscCategoriesName) {
		this.discCategoriesName = pDiscCategoriesName;
	}
	
	public String getSessionId(){
		return this.sessionId;
	}
	
	public void setSessionId(String value){
		this.sessionId=value;
	}
}
