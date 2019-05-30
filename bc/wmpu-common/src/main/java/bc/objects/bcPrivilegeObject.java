package bc.objects;

import java.util.HashMap;
import java.util.Map;

import bc.service.bcFeautureParam;


public class bcPrivilegeObject extends bcObject {
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();
	
	private String idRole;
	private String idMenuElement;
	
	public bcPrivilegeObject(String pIdRole, String pIdMenuElement) {
		this.idRole = pIdRole;
		this.idMenuElement = pIdMenuElement;
		getFeature();
	}
	
	private void getFeature() {
		String mySQL = " SELECT * FROM " + getGeneralDBScheme() + ".VC_MENU_PRIVILEGE_ALL WHERE id_role = ? AND id_menu_element = ?";
		
		bcFeautureParam[] array = new bcFeautureParam[2];
		array[0] = new bcFeautureParam("int", this.idRole);
		array[1] = new bcFeautureParam("int", this.idMenuElement);

		fieldHm = getFeatures2(mySQL, array);
	}
	
	public String getValue(String pColumnName) {
		return getFeautureResult(fieldHm, pColumnName);
	}

}
