package bc.ajax;

public class address {
	public address(){}
	
	private String codeCountry;
	private String idOblast;
	private String idDistrict;
	private String idCity;
	private String idCityDistrict;
	private String[] countryListCode;
	private String[] countryListName;
	private String[] oblastListCode;
	private String[] oblastListName;
	private String[] districtListCode;
	private String[] districtListName;
	private String[] cityListCode;
	private String[] cityListName;
	private String[] cityDistrictListCode;
	private String[] cityDistrictListName;
	
	private String sessionId;
	
	public String getCodeCountry() {
		return this.codeCountry;
	}
	
	public void setCodeCountry(String pCodeCountry) {
		this.codeCountry = pCodeCountry;
	}
	
	public String[] getCountryListCode() {
		return this.countryListCode;
	}
	
	public void setCountryListCode(String[] pCountryListCode) {
		this.countryListCode = pCountryListCode;
	}
	
	public String[] getCountryListName() {
		return this.countryListName;
	}
	
	public void setCountryListName(String[] pCountryListName) {
		this.countryListName = pCountryListName;
	}
	
	public String getIdOblast() {
		return this.idOblast;
	}
	
	public void setIdOblast(String pIdOblast) {
		this.idOblast = pIdOblast;
	}
	
	public String getIdDistrict() {
		return this.idDistrict;
	}
	
	public void setIdDistrict(String pIdDistrict) {
		this.idDistrict = pIdDistrict;
	}
	
	public String getIdCity() {
		return this.idCity;
	}
	
	public void setIdCity(String pIdCity) {
		this.idCity = pIdCity;
	}
	
	public String getIdCityDistrict(){
		return this.idCityDistrict;
	}
	
	public void setIdCityDistrict(String pIdCityDistrict){
		this.idCityDistrict=pIdCityDistrict;
	}
	
	public String[] getOblastListCode() {
		return this.oblastListCode;
	}
	
	public void setOblastListCode(String[] pOblastListCode) {
		this.oblastListCode = pOblastListCode;
	}
	
	public String[] getOblastListName() {
		return this.oblastListName;
	}
	
	public void setOblastListName(String[] pOblastListName) {
		this.oblastListName = pOblastListName;
	}
	
	public String[] getDistrictListCode() {
		return this.districtListCode;
	}
	
	public void setDistrictListCode(String[] pDistrictListCode) {
		this.districtListCode = pDistrictListCode;
	}
	
	public String[] getDistrictListName() {
		return this.districtListName;
	}
	
	public void setDistrictListName(String[] pDistrictListName) {
		this.districtListName = pDistrictListName;
	}
	
	public String[] getCityListCode() {
		return this.cityListCode;
	}
	
	public void setCityListCode(String[] pCityListCode) {
		this.cityListCode = pCityListCode;
	}
	
	public String[] getCityListName() {
		return this.cityListName;
	}
	
	public void setCityListName(String[] pCityListName) {
		this.cityListName = pCityListName;
	}
	
	public String[] getCityDistrictListCode() {
		return this.cityDistrictListCode;
	}
	
	public void setCityDistrictListCode(String[] pCityDistrictListCode) {
		this.cityDistrictListCode = pCityDistrictListCode;
	}
	
	public String[] getCityDistrictListName() {
		return this.cityDistrictListName;
	}
	
	public void setCityDistrictListName(String[] pCityDistrictListName) {
		this.cityDistrictListName = pCityDistrictListName;
	}
	
	public String getSessionId(){
		return this.sessionId;
	}
	
	public void setSessionId(String value){
		this.sessionId=value;
	}
}
