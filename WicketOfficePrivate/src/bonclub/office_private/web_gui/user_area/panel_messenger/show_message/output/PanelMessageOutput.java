package bonclub.office_private.web_gui.user_area.panel_messenger.show_message.output;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/** панель, котора€ отображает вход€щие сообщени€ */
public class PanelMessageOutput extends Panel{
	final static private long serialVersionUID=1L;
	
	public PanelMessageOutput(String id,String recipient, String text_message){
		super(id);
		this.add(new Label("recipient",new Model(recipient)));
		this.add(new TextArea("text",new Model(text_message)));
	}
}
