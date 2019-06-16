package bonclub.office_private.web_gui.user_area.panel_messenger.input;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import wicket_utility.ActionExecutor;
import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.wrap.Message_Recieve;
import bonclub.office_private.session.OfficePrivateSession;
import bonclub.office_private.web_gui.user_area.UserArea;
import bonclub.office_private.web_gui.user_area.panel_messenger.MessengerUtility;


public class Input extends Panel{
	private final static long serialVersionUID=1L;
	
	/** вывести отладочную информацию */
	private void out_debug(String information){
		System.out.print("Input ");
		System.out.print(" DEBUG: ");
		System.out.println(information);
	}
	/** вывести информацию об ошибке */
	private void out_error(String information){
		System.out.print("Input ");
		System.out.print(" ERROR: ");
		System.out.println(information);
	}
	
	/** пустой список для хранения пустого списка событий */
	private List<Message_Recieve> field_empty_list=new ArrayList<Message_Recieve>();
	/** список, в котором хранятся все сообщения */
	private List<Message_Recieve> field_list=null;
	private static SimpleDateFormat field_date_format=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	private ActionExecutor executor;
	
	/** панель, которая отображает входящие сообщения */
	@SuppressWarnings("unchecked")
	public Input(String panelId, ActionExecutor executor){
		super(panelId);
		this.executor=executor;
		// create element's
		Form<Object> message_form=new Form<Object>("message_form");
		Button button_delete=new Button("button_delete",new Model<String>(this.getString("button_delete.caption"))){
			private static final long serialVersionUID=1L;
			public void onSubmit(){
				on_button_delete();
			}
		};
		Button button_output=new Button("button_output",new Model<String>(this.getString("button_output.caption"))){
			private static final long serialVersionUID=1L;
			public void onSubmit(){
				on_button_output();
			}
		};
		field_list=this.getInputMessage();
		
/*		ListView message_list=new ListView("message_list",field_list){
			private static final long serialVersionUID=1L;
			@Override
			protected void populateItem(ListItem item) {
				Message_Send current_message=(Message_Send)item.getModelObject();
				item.add(new CheckBox("message_selected",new PropertyModel(current_message,"selected")));
				item.add(new Label("message_recipient","recipient"));
				try{
					item.add(new Label("message_date",field_date_format.format(current_message.getDate_create())));
				}catch(Exception ex){
					item.add(new Label("message_date",""));
				}
			}
		};
		message_form.add(message_list);*/
		
		IColumn[] columns=new IColumn[]{
				new AbstractColumn(new Model(this.getString("table_caption.select"))){
					private static final long serialVersionUID=1L;
					@Override
					public void populateItem(Item cellItem, String id, IModel row_model) {
						Message_Recieve message_recieve=(Message_Recieve)row_model.getObject();
						cellItem.add(new PanelSelect(id,new PropertyModel(message_recieve,"selected")));
					}
				},
				//new PropertyColumn(new Model("Recipient(s)"),"recipient"),
				new AbstractColumn(new Model(this.getString("table_caption.sender"))){
					private final static long serialVersionUID=1L;
					@Override
					public void populateItem(Item cellItem, String wicket_id, IModel model) {
						Message_Recieve message_recieve=(Message_Recieve)model.getObject();
						cellItem.add(new PanelLink(Input.this.executor,
												   Input.this,	
												   wicket_id,
												   message_recieve.getSender((OfficePrivateApplication)Input.this.getApplication()),
												   message_recieve.getId(),
												   false)
									 );
					}
				},
				new AbstractColumn(new Model(this.getString("table_caption.date"))){
					private static final long serialVersionUID = 1L;
					@Override
					public void populateItem(Item cellItem, String wicket_id, IModel rowModel) {
						Message_Recieve message_recieve=(Message_Recieve)rowModel.getObject();
						cellItem.add(new Label(wicket_id,field_date_format.format(message_recieve.getDate_recieve())));
					}
				}
				//new PropertyColumn(new Model("Date"),"date_create")
		};
		SortableDataProvider<Object> data_provider=new SortableDataProvider<Object>(){
			/** */
			private static final long serialVersionUID = 1L;
			@Override
			public Iterator<Message_Recieve> iterator(int first, int count) {
				return Input.this.field_list.subList(first, first+count).iterator();
			}
			@Override
			public IModel<Object> model(Object row) {
				return new Model(((Message_Recieve)row));
			}
			@Override
			public int size() {
				return Input.this.field_list.size();
			}
		};
		DefaultDataTable<Object> data_table=new DefaultDataTable<Object>("message_table",
																	     columns,
																	     data_provider, 
																	     5);
		message_form.add(data_table);
		// placing element's		
		message_form.add(button_delete);
		message_form.add(button_output);
		this.add(message_form);
	}
	
	/** get all output message */
	private List<Message_Recieve> getInputMessage(){
		List<Message_Recieve> return_value=null;
		MessengerUtility utility=null;
		try{
			out_debug("User_id:"+((OfficePrivateSession)this.getSession()).getCustomerId());
			utility=new MessengerUtility((OfficePrivateApplication)this.getApplication());
			return_value=utility.getInputMessage(((OfficePrivateSession)this.getSession()).getCustomerId());
		}catch(Exception ex){
		}finally{
			utility.close();
		}
		return (return_value==null)?this.field_empty_list:return_value;
	}
	
	/** reaction on striking button delete*/
	private void on_button_delete(){
		MessengerUtility utility=new MessengerUtility((OfficePrivateApplication)this.getApplication());
		for(int counter=0;counter<field_list.size();counter++){
			out_debug(field_list.get(counter).getId()+" : "+field_list.get(counter).isSelected());
			if(field_list.get(counter).isSelected()){
				if(utility.removeMessageRecieve((field_list.get(counter).getId()))){
					// message is deleted
				}else{
					// message is not deleted
					out_error("on_button_delete: Message is not deleted"+field_list.get(counter).getId());
				}
			}
		}
		utility.close();
		this.field_list=getInputMessage();
	}

	/** reaction on striking button goto Output Message*/
	private void on_button_output(){
		executor.action(UserArea.eventPostOutput, null);
	}
}
