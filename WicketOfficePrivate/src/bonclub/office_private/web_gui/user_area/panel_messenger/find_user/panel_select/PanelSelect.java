package bonclub.office_private.web_gui.user_area.panel_messenger.find_user.panel_select;

import org.apache.wicket.markup.html.form.Button;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class PanelSelect extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PanelSelect(String id,final IModel model){
		super(id);
		Button button=new Button("button"){
			private static final long serialVersionUID = 1L;
			public void onSubmit(){
				model.setObject(new Boolean(true));
			}
		};
		this.add(button);
		//this.add(new CheckBox("checkbox",model));
		
	}
}
