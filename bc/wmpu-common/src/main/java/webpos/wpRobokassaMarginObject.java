package webpos;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import bc.service.bcFeautureParam;

public class wpRobokassaMarginObject extends wpObject {
    
	private Map<String,Comparable<String>> fieldHm = new HashMap<String, Comparable<String>>();

	//private String idClub;
	//private String idDealer;
	
	public wpRobokassaMarginObject(/*String pIdClub, String pIdDealer*/) {
		//this.idClub = pIdClub;
		//this.idDealer = pIdDealer;
		getFeature();
	}
	
	private void getFeature() {
		bcFeautureParam[] array = new bcFeautureParam[1];
		array[0] = new bcFeautureParam("none", "");
		
		String featureSelect = 
			" SELECT * FROM " + getGeneralDBScheme() + ".vc_robokassa_margin_all ";
		fieldHm = getFeatures2(featureSelect, array);
	}
	
	public String getMarginPercent() {
		return getFeautureResult(fieldHm, "MARGIN_PERCENT");
	}
	
	public String getMarginValue(String pOperSum) {
		double marginPercent = new Double(getMarginPercent().replace(",","."));
		String marginValue = "";
		if (!(pOperSum == null || "".equalsIgnoreCase(pOperSum))) {
			double oprSum = new Double(pOperSum.replace(",","."));
			marginValue = (new DecimalFormat("#0.00").format(oprSum * marginPercent / 100)+"").replace(".",",");
			System.out.println("marginValue="+marginValue);
		}
		return marginValue;
	}
	
	public String getTotalOprSum(String pOperSum) {
		double marginPercent = new Double(getMarginPercent().replace(",","."));
		String totalSum = "";
		if (!(pOperSum == null || "".equalsIgnoreCase(pOperSum))) {
			double oprSum = new Double(pOperSum.replace(",","."));
			totalSum = (new DecimalFormat("#0.00").format(oprSum + (oprSum * marginPercent / 100))+"").replace(".",",");
			System.out.println("totalSum="+totalSum);
		}
		return totalSum;
	}
	
	public String getMerginHint () {
		String marginHint = "";
		
		if (!isEmpty(getMarginPercent())) {
			marginHint = "При <u>оплате через Робокассу</u> взымается <b>комиссия</b> в размере <b> " + getMarginPercent() + " %</b> от суммы операции";
		}
		return marginHint;
	}

}
