package bonclub.office_private.web_gui.user_area.panel_messenger.output;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.behavior.SimpleAttributeModifier;
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
import bonclub.office_private.database.wrap.Message_Send;
import bonclub.office_private.session.OfficePrivateSession;
import bonclub.office_private.web_gui.user_area.UserArea;
import bonclub.office_private.web_gui.user_area.panel_messenger.MessengerUtility;
import bonclub.office_private.web_gui.user_area.panel_messenger.input.PanelLink;

public class Output extends Panel{
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
	private List<Message_Send> field_empty_list=new ArrayList<Message_Send>();
	/** список, в котором хранятся все сообщения */
	private List<Message_Send> field_list=null;
	private static SimpleDateFormat field_date_format=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	private ActionExecutor executor;
	
	@SuppressWarnings("unchecked")
	public Output(String panelId, ActionExecutor executor){
		super(panelId);
		this.executor=executor;
		// create element's
		Form<?> message_form=new Form<Object>("message_form");
		Button button_delete=new Button("button_delete"){
			private static final long serialVersionUID=1L;
			public void onSubmit(){
				on_button_delete();
			}
		};
		button_delete.add(new SimpleAttributeModifier("value",this.getString("button_delete.caption")));
		
		Button button_input=new Button("button_input"){
			private static final long serialVersionUID=1L;
			public void onSubmit(){
				on_button_input();
			}
		};
		button_input.add(new SimpleAttributeModifier("value",this.getString("button_input.caption")));

		
		field_list=this.getOutputMessage();
		
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
						Message_Send message_send=(Message_Send)row_model.getObject();
						cellItem.add(new PanelSelect(id,new PropertyModel(message_send,"selected")));
					}
				},
				//new PropertyColumn(new Model("Recipient(s)"),"recipient"),
				new AbstractColumn(new Model(this.getString("table_caption.recipients"))){
					private final static long serialVersionUID=1L;
					@Override
					public void populateItem(Item cellItem, String wicket_id, IModel model) {
						Message_Send message_send=(Message_Send)model.getObject();
						cellItem.add(new PanelLink(Output.this.executor,
								   Output.this,	
								   wicket_id,
								   message_send.getRecipient((OfficePrivateApplication)Output.this.getApplication()), 
								   message_send.getId(),
								   true)
					 );
						
					}
				},
				new AbstractColumn(new Model(this.getString("table_caption.date"))){
					private static final long serialVersionUID = 1L;
					@Override
					public void populateItem(Item cellItem, String wicket_id, IModel rowModel) {
						Message_Send message_send=(Message_Send)rowModel.getObject();
						cellItem.add(new Label(wicket_id,field_date_format.format(message_send.getDate_create())));
					}
				}
				//new PropertyColumn(new Model("Date"),"date_create")
		};
		SortableDataProvider data_provider=new SortableDataProvider(){
			/** */
			private static final long serialVersionUID = 1L;
			@Override
			public Iterator<Message_Send> iterator(int first, int count) {
				return Output.this.field_list.subList(first, first+count).iterator();
			}
			@Override
			public IModel model(Object row) {
				return new Model(((Message_Send)row));
			}
			@Override
			public int size() {
				return Output.this.field_list.size();
			}
		};
		DefaultDataTable data_table=new DefaultDataTable("message_table",columns,data_provider, 5);
		message_form.add(data_table);
		// placing element's		
		message_form.add(button_delete);
		message_form.add(button_input);
		this.add(message_form);
	}
	
	/** get all output message */
	private List<Message_Send> getOutputMessage(){
		List<Message_Send> return_value=null;
		MessengerUtility utility=new MessengerUtility((OfficePrivateApplication)this.getApplication());
		try{
			out_debug("User_id:"+((OfficePrivateSession)this.getSession()).getCustomerId());
			return_value=utility.getOutputMessage(((OfficePrivateSession)this.getSession()).getCustomerId());
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
				if(utility.removeMessageSend(field_list.get(counter).getId())){
					// message is deleted
				}else{
					// message is not deleted
					out_error("on_button_delete: Message is not deleted"+field_list.get(counter).getId());
				}
			}
		}
		utility.close();
		this.field_list=getOutputMessage();
	}

	/** reaction on striking button goto Output Message*/
	private void on_button_input(){
		this.executor.action(UserArea.eventPostInput, null);
	}
}
