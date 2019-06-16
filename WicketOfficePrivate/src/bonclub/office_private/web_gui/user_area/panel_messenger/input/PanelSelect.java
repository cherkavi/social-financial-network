package bonclub.office_private.web_gui.user_area.panel_messenger.input;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class PanelSelect extends Panel{
	/** */
	private static final long serialVersionUID = 1L;
	public PanelSelect(String id, IModel model){
		super(id);
		add(new CheckBox("select_element",model));
	}
}
