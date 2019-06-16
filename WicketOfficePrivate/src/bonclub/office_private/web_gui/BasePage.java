package bonclub.office_private.web_gui;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public abstract class BasePage extends WebPage{
	public BasePage(){
		WebMarkupContainer cssLink=new WebMarkupContainer("main_style");
		cssLink.add(new SimpleAttributeModifier("href","BasePage.css"));
		this.add(cssLink);
		Model<String> model=new Model<String>();
		try{
			model.setObject(this.getString("title_message"));
		}catch(Exception ex){
		}
		Label titleMessage=new Label("title_message",model);
		this.add(titleMessage);
	}
}
