package bc.service;

import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import bc.objects.bcDictionary;
import bc.objects.bcXML;

public class myLinks {
	private final static Logger LOGGER=Logger.getLogger(myLinks.class);
	
	private ArrayList<String> backLinks = new ArrayList<String>();
	//private ArrayList<String> forwardLinks = new ArrayList<String>();
	
	private int currentPage = -1;
	
    public bcXML buttonXML = bcDictionary.getInstance("buttons");
	
	public void clearAll() {
		backLinks.clear();
		LOGGER.setLevel(Level.ERROR);
		//forwardLinks.clear();
	}
	
	public void addLink(String pLink) {
		//LOGGER.debug("myLinks.addLink: backLinks.size()=" + backLinks.size());
		//LOGGER.debug("myLinks.addLink: currentPage=" + currentPage);
		if (pLink.contains("goback=yes")) {
			this.goBack();
		} else if (pLink.contains("goforvard=yes")) {
			currentPage = currentPage + 1;
		} else {
			if (!pLink.contains("process=yes")) {
				if (backLinks.size() > 0) {
					if (!backLinks.get(currentPage).equalsIgnoreCase(pLink)) {
						for (int i=backLinks.size()-1; i >=currentPage+1 ; i--) {
							//LOGGER.debug("myLinks.addLink(2): backLinks.remove("+i+")=" + backLinks.get(i));
							backLinks.remove(i);
						}
						//LOGGER.debug("myLinks.addLink(2): backLinks.size()=" + backLinks.size());
						backLinks.add(pLink);
						currentPage = backLinks.size()-1;
					}
				} else {
					backLinks.add(pLink);
					currentPage = backLinks.size()-1;
				}
			}
		}
	}
	
	public void goBack() {
		if ((backLinks.size()) > 0 && (currentPage > 0)) {
			currentPage = currentPage - 1;
		}
	}
	
	public String formatButtons() {
		//LOGGER.debug("myLinks.formatButtons: backLinks.size()=" + backLinks.size());
		//LOGGER.debug("myLinks.formatButtons: currentPage=" + currentPage);
		String return_value = "<td width=\"1\" style=\"border:0px; padding:0px\">";
		if (currentPage>0) {
			return_value = return_value + "<div class=\"div_button\" onclick=\"ajaxpage3('"+ backLinks.get(currentPage-1) + "&goback=yes', 'div_main')\"><img src=\"../images/go/back_active.PNG\" border=\"0\" title=\""+buttonXML.getfieldTransl("button_back", false)+"\"></a>";
		} else {
			return_value = return_value + "<img src=\"../images/go/back_inactive.PNG\" border=\"0\" title=\""+buttonXML.getfieldTransl("button_back", false)+"\">";			
		}
		
		//return_value = return_value + "</td><td width=\"1\" style=\"border:0px\"><img src=\"" + pContext + "/images/go/delimiter.PNG\" border=\"0\"></td><td width=\"1\" style=\"border:0px\">";
		return_value = return_value + "</td><td width=\"1\" style=\"border:0px; padding:0px\">";
		if ((backLinks.size()-1)>currentPage) {
			return_value = return_value + "<div class=\"div_button\" onclick=\"ajaxpage('"+ backLinks.get(currentPage+1) + "&goforvard=yes', 'div_main')\"><img src=\"../images/go/forward_active.PNG\" border=\"0\" title=\""+buttonXML.getfieldTransl("button_forward", false)+"\"></a>";
		} else {
			return_value = return_value + "<img src=\"../images/go/forward_inactive.PNG\" border=\"0\" title=\""+buttonXML.getfieldTransl("button_forward", false)+"\">";			
		}
		return_value = return_value + "</td>";
		return return_value;
	}
	
	public int getSize() {
		return backLinks.size();
	}
	
	public String getLastLink() {
		String returnValue = "";
		if (backLinks.size() > 0) {
			returnValue = backLinks.get(backLinks.size()-1);
		}
		return returnValue;
	}
}
