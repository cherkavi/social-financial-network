package bonclub.office_private.web_gui.user_area.panel_messenger.show_message.input;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/** панель, котора€ отображает вход€щие сообщени€ */
public class PanelMessageInput extends Panel{
	final static private long serialVersionUID=1L;
	
	public PanelMessageInput(String id,String sender, String text_message){
		super(id);
		this.add(new Label("sender",new Model(sender)));
		this.add(new TextArea("text",new Model(text_message)));
	}
}
