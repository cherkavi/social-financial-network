package bc.objects;

import java.util.HashMap;
import java.util.Map;



public class bcCardSettingCategoryObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idCategory;
	
	public bcCardSettingCategoryObject(String pIdCategory) {
		this.idCategory = pIdCategory;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_CARD_CATEGORY_CLUB_ALL WHERE id_category = ?";
		fieldHm = getFeatures2(mySQL, this.idCategory, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}
	
}
