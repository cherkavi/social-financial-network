package bonclub.office_private.web_gui.user_area.panel_messenger.sended;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import wicket_utility.ActionExecutor;

import bonclub.office_private.database.wrap.Users;
import bonclub.office_private.session.OfficePrivateSession;
import bonclub.office_private.web_gui.user_area.UserArea;

import java.util.*;

public class Sended extends Panel{
	private final static long serialVersionUID=1L;
	/** объект, который принимает запросы на принятие решений */
	private ActionExecutor executor;
	
	/** вывести информацию об ошибке */
	private void out_error(String information){
		System.out.print("Sended ");
		System.out.print(" ERROR: ");
		System.out.println(information);
	}
	
	
	/** получить из сессии всех получателей */
	private List<Users> getRecipient(){
		List<Users> return_value=null;
		try{
			return_value=((OfficePrivateSession)this.getSession()).getUserList("RECIPIENT").getList();
		}catch(Exception ex){
			out_error("getRecipient: Impossible algorithm: Exception "+ex.getMessage());
			return_value=new ArrayList<Users>();
		}
		return return_value;
	}
	
	/** отображение страницы успешной отправки письма*/
	public Sended(String panelId, ActionExecutor executor){
		super(panelId);
		this.executor=executor;
		List<Users> list=getRecipient();
		ListView<Users> user_list=new ListView<Users>("user_list",list){
			private static final long serialVersionUID=1L;
			@Override
			protected void populateItem(ListItem<Users> item) {
				Users current_user=(Users)item.getModelObject();
				item.add(new Label("nick",current_user.getNick()));
				item.add(new Label("full_name",current_user.getFullName()));
			}
		};
		/*
		 *<tr wicket:id="user_list">
			<td wicket:id="nick"></td>
			<td wicket:id="full_name"></td>
		 */
		// create component's
		Form<Object> form_buttons=new Form<Object>("form_buttons");
		Button button_more=new Button("button_more"){
			/** */
			private static final long serialVersionUID = 1L;

			public void onSubmit(){
				on_button_more();
			}
		};
		button_more.add(new SimpleAttributeModifier("value",this.getString("button_more.caption")));

		Button button_new=new Button("button_new"){
			private static final long serialVersionUID = 1L;
			public void onSubmit(){
				on_button_new();
			}
		};
		button_more.add(new SimpleAttributeModifier("value",this.getString("button_new.caption")));
		// placing component's
		this.add(user_list);
		form_buttons.add(button_more);
		form_buttons.add(button_new);
		this.add(form_buttons);
	}
	
	/** clear RECIPIENT list */
	private void clearRecipientList(){
		try{
			((OfficePrivateSession)this.getSession()).removeList("RECIPIENT");
		}catch(Exception ex){
			out_error("clear RecipientList Exception:"+ex.getMessage());
		}
	}
	
	/** reaction on striking button more */
	private void on_button_more(){
		executor.action(UserArea.eventPostCreate,null);
	}
	
	/** reaction on striking button new */
	private void on_button_new(){
		clearRecipientList();
		executor.action(UserArea.eventPostCreate,null);
	}
	
}
