package bc.objects;

import java.util.HashMap;
import java.util.Map;

public class bcUserLoadedImageObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idLoadedImage;
	
	public bcUserLoadedImageObject(String pIdLoadedImage) {
		this.idLoadedImage = pIdLoadedImage;
		getFeature();
	}
	
	private void getFeature() {
		String featureSelect = " SELECT * FROM " + getGeneralDBScheme() + ".vc_user_loaded_files WHERE id_loaded_file = ?";
		fieldHm = getFeatures2(featureSelect, this.idLoadedImage, false);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
