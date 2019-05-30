package webpos;

public class wpObject extends bc.objects.bcObject  { 
	//protected static Connection con = null;
    
	public String getWEBPosTable(){
		return "<table class=\"tablebottom\"><thead>";
	}
    
	public String getWebPosDeleteFileImageHTML(String pHyperLink, String pDivName, String pPrompt){
		StringBuilder html = new StringBuilder ();
		html.append("<a hrev=\"\" onclick=\"var msg='" + pPrompt + "?';var res=window.confirm(msg);if (res) {ajaxpage('"+ pHyperLink + "','" + pDivName + "');}\">" + "<img vspace=\"0\" hspace=\"0\"" +
				" src=\"images/oper/del.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
				" title=\"" + buttonXML.getfieldTransl("button_delete", false)+"\">" + getHyperLinkEnd()+"</a>\n");
		/*html.append("<td align=\"center\">\n");
		html.append(getHyperLinkFirst() + pHyperLink + "&action=remove&process=no" + getHyperLinkMiddle() + "\n");
		html.append("<img vspace=\"0\" hspace=\"0\" src=\"../images/oper/rows/delete.png\" align=\"top\" style=\"border: 0px;\" title=\""+ buttonXML.getfieldTransl(pLanguage,"button_delete", false) +"\">\n");
		html.append(getHyperLinkEnd() + "</td>\n");
		*/
		return html.toString();
	}
	
	private String preparePrompt(String pPrompt, String pValue) {
		String lPrompt = isEmpty(pPrompt)?"":pPrompt;
		String lValue = isEmpty(pValue)?"":pValue;
		if (lPrompt.contains("%1")) {
			lPrompt = preparePrompt(lPrompt.replaceAll("%1", lValue));
		} else {
			lPrompt = preparePrompt(lPrompt) + " \\\'" + preparePrompt(lValue)+ "\\\'";
		}
		return lPrompt;
	}
    
    public String getWebposDeleteButtonHTML(String pHyperLink, String pPrompt, String pValue, String pDivName){
		StringBuilder html = new StringBuilder ();
		String lPrompt = preparePrompt(pPrompt, pValue);
		html.append("<td style=\"width:15px;text-align:center;\"><div class=\"div_button\" onclick=\"var msg='" + lPrompt + "?';var res=window.confirm(msg);if (res) {ajaxpage('"+ pHyperLink + "','" + pDivName + "');}\">" + "<img vspace=\"0\" hspace=\"0\"" +
				" src=\"images/oper/delete.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\"" + 
				" title=\"" + buttonXML.getfieldTransl("button_delete", false)+"\">" + getHyperLinkEnd()+"</td>\n");
		return html.toString();
	}
    
	public String getWebposEditButtonHTML(String pHyperLink, String pDivName){
		StringBuilder html = new StringBuilder ();
		html.append("<td style=\"width:15px;text-align:center;\">\n");
		html.append(getHyperLinkFirst() + pHyperLink + "&action=edit&process=no', '" + pDivName + "')\">\n");
		html.append("<img vspace=\"0\" hspace=\"0\" src=\"images/oper/edit.png\" width=\"16\" height=\"16\" align=\"top\" style=\"border: 0px;\" title=\""+ buttonXML.getfieldTransl("button_edit", false) +"\">\n");
		html.append(getHyperLinkEnd() + "</td>\n");
		return html.toString();
	}
    
}
