package bonclub.office_private.web_gui.user_area.panel_messenger.recipient_list;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import wicket_utility.ActionExecutor;

import bonclub.office_private.database.wrap.Users;
import bonclub.office_private.session.OfficePrivateSession;
import bonclub.office_private.session.UserList;
import bonclub.office_private.web_gui.user_area.UserArea;

import java.util.ArrayList;
import java.util.List;

/** список пользователей, которые могут стать получателя сообщения */
public class RecipientList extends Panel{
	private final static long serialVersionUID=1L;
	
	private void out_debug(String information){
		System.out.print("RecipientList ");
		System.out.print(" DEBUG: ");
		System.out.println(information);
	}
	private void out_error(String information){
		System.out.print("RecipientList ");
		System.out.print(" ERROR: ");
		System.out.println(information);
	}
	
	/** список пользователей, которые попадают в список рассылки */
	private List<Users> field_list;
	private ActionExecutor executor;
	
	
	public RecipientList(String panelId, ActionExecutor executor ){
		super(panelId);
		this.executor=executor;
		// create element
		Form<Object> form_main=new Form<Object>("form_main"){
			private static final long serialVersionUID=1L;
			public void onSubmit(){
				// form onSubmit reaction
			}
		};
		
		this.field_list=this.getRecipient();
		ListView<Users> list=new ListView<Users>("recipient_list",this.getRecipient()){
			private static final long serialVersionUID=1L;
			@SuppressWarnings("unchecked")
			@Override
			protected void populateItem(ListItem<Users> item) {
				Users user=(Users)item.getModelObject();
				item.add(new CheckBox("user_checkbox",new PropertyModel(user,"selected")));
				item.add(new Label("recipient_nick",user.getNick()));
				item.add(new Label("recipient_full_name",user.getFullName()));
			}
		};
		Button button_done=new Button("button_done"){
			private static final long serialVersionUID = 1L;
			public void onSubmit(){
				on_button_done();
			}
		};
		button_done.add(new SimpleAttributeModifier("value",this.getString("button_done.caption")));

		Button button_add=new Button("button_add"){
			private static final long serialVersionUID = 1L;
			public void onSubmit(){
				on_button_add();
			}
		};
		button_add.add(new SimpleAttributeModifier("value",this.getString("button_add.caption")));

		Button button_remove=new Button("button_remove"){
			private static final long serialVersionUID = 1L;
			public void onSubmit(){
				on_button_remove();
			}
		};
		button_remove.add(new SimpleAttributeModifier("value",this.getString("button_remove.caption")));

		form_main.add(button_done);
		form_main.add(button_add);
		form_main.add(button_remove);
		form_main.add(list);
		// placing element
		this.add(form_main);
	}
	
	/** reaction on striking button DONE */
	private void on_button_done(){
		//setResponsePage(Create.class);
		this.executor.action(UserArea.eventPostCreate, null);
	}
	
	/** reaction on striking button ADD*/
	private void on_button_add(){
		this.executor.action(UserArea.eventPostFindUser, null);
	}

	/** reaction on striking button REMOVE*/
	private void on_button_remove(){
		try{
			OfficePrivateSession session=(OfficePrivateSession)this.getSession();
			UserList list=session.getUserList("RECIPIENT");
			
			for(int counter=this.field_list.size()-1;counter>=0;counter--){
				if(this.field_list.get(counter).getSelected()==true){
					System.out.println("remove:"+this.field_list.get(counter).getNick());
					list.removeUser(this.field_list.get(counter));
					this.field_list.remove(counter);
					
				}
			}
			out_debug("refresh");
			//setResponsePage(RecipientList.class);
			this.executor.action(UserArea.eventPostRecipientList, null);
		}catch(Exception ex){
			out_error("Error in remove user from RECIPIENT");
		}
	}
	
	/** получить список всех User, которым может быть послано сообщение*/
	private List<Users> getRecipient(){
		List<Users> return_value=null;
		try{
			OfficePrivateSession ms=((OfficePrivateSession)this.getSession());
			return_value=ms.getUserList("RECIPIENT").setSelected(false).getList();
		}catch(Exception ex){
			out_debug("list is empty");
			return_value=new ArrayList<Users>();
		}
		return return_value;
	}
}
