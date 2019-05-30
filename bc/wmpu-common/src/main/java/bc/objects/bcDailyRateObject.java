package bc.objects;

import java.util.HashMap;
import java.util.Map;


public class bcDailyRateObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idDailyRate;
	
	public bcDailyRateObject(String pIdDailyRate) {
		this.idDailyRate = pIdDailyRate;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = "SELECT * FROM " + getGeneralDBScheme() + ".VC_DAILY_RATES_ALL WHERE id_daily_rate = ?";
		fieldHm = getFeatures2(mySQL, this.idDailyRate, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
