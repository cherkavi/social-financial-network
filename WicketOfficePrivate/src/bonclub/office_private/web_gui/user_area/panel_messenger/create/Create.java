package bonclub.office_private.web_gui.user_area.panel_messenger.create;

import java.io.Serializable;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import wicket_utility.ActionExecutor;
import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.session.OfficePrivateSession;
import bonclub.office_private.session.UserList;
import bonclub.office_private.web_gui.user_area.UserArea;
import bonclub.office_private.web_gui.user_area.panel_messenger.MessengerUtility;

/** Панель, которая отображает интерфейс создания сообщения */
public class Create extends Panel{
	private final static long serialVersionUID=1L;
	private ActionExecutor executor;
	
	/** вывести отладочную информацию */
	private void out_debug(String information){
		System.out.print("Create ");
		System.out.print(" DEBUG: ");
		System.out.println(information);
	}
	/** вывести информацию об ошибке */
	private void out_error(String information){
		System.out.print("Create ");
		System.out.print(" ERROR: ");
		System.out.println(information);
	}
	
	/** модель для таблицы с данными */
	private FormModel field_form_model=new FormModel();
	/** максимальная длинна перечисленных в строке пользователей */
	private final int field_max_length=23;
	/** имя ключа, по которому в сессии лежит строка с текстом */
	private String field_key_message="MESSAGE_TEXT";
	
	public Create(String panelId, ActionExecutor executor){
		super(panelId);
		this.executor=executor;
		// получить строку из сессии
		try{
			String message=((OfficePrivateSession)this.getSession()).getString(this.field_key_message);
			if(message!=null){
				field_form_model.setMessage_text(message);
			}
		}catch(Exception ex){
		}
		Form<?> form_message=new Form<Object>("form_message",new CompoundPropertyModel<Object>(field_form_model)){
			private static final long serialVersionUID = 1L;
			public void onSubmit(){
				super.onSubmit();
				((OfficePrivateSession)this.getSession()).putString(field_key_message, field_form_model.getMessage_text());
			}
		};
		
		// users_string - список пользователей
		form_message.add(new Label("users_string",new PropertyModel<Object>(this,"UserList")));

		// button_user_edit
		form_message.add(new Button("button_user_edit",new Model<String>(this.getString("button_user_edit.caption"))){
			private static final long serialVersionUID = 1L;
			public void onSubmit(){
				on_button_edit();
			}
		});
		TextArea<String> text_area=new TextArea<String>("message_text");
		// данные не могут быть равны null
		text_area.setRequired(false);
		// message_text
		form_message.add(text_area);
		
		// button_send
		form_message.add(new Button("button_send",new Model<String>(this.getString("button_send.caption"))){
			private static final long serialVersionUID = 1L;
			public void onSubmit(){
				on_button_send();
			}
		});
		// button_cancel
		form_message.add(new Button("button_cancel",new Model<String>(this.getString("button_cancel.caption"))){
			private static final long serialVersionUID = 1L;
			public void onSubmit(){
				on_button_cancel();
			}
		});
		this.add(form_message);
	}
	
	/** reaction on striking button SEND */
	private void on_button_send(){
		if(getRecipientListAsString().length()==0){
			this.executor.action(UserArea.eventPostRecipientList, null);
		}else{
			// TODO проверка на отсылку пустого письма 
			if(this.send_message()==true){
				out_debug("send Ok");
				((OfficePrivateSession)this.getSession()).removeString(field_key_message);
				this.executor.action(UserArea.eventPostInformation, new String[]{this.getString("send.ok")});
			}else{
				// TODO реакция на ошибочную отправку сообщения
				out_debug("send error");
				((OfficePrivateSession)this.getSession()).removeString(field_key_message);
				this.executor.action(UserArea.eventPostInformation, new String[]{this.getString("send.error")});
			}
		}
	}
	
	/** посылка сообщения заявленным адресатам
	 * */
	private boolean send_message(){
		MessengerUtility utility=new MessengerUtility((OfficePrivateApplication)this.getApplication());
		try{
			return utility.sendMessage(this.field_form_model.getMessage_text(), 
									   ((OfficePrivateSession)this.getSession()).getCustomerId(),
									   ((OfficePrivateSession)this.getSession()).getUserList("RECIPIENT").getList());
		}catch(Exception ex){
			out_error("send_message: Exception:"+ex.getMessage());
			return false;
		}finally{
			utility.close();
		}
		
	}
	
	/** reaction on strking button CANCEL */
	private void on_button_cancel(){
		try{
			((OfficePrivateSession)this.getSession()).removeList("RECIPIENT");
		}catch(Exception ex){
			out_error("on_button_cancel clear list error:"+ex.getMessage());
		}
		this.executor.action(UserArea.eventPostInformation, null);
	}
	
	/** reaction on striking button EDIT */
	private void on_button_edit(){
		this.executor.action(UserArea.eventPostRecipientList, null);
	}
	
	/** получить Nick имена всех получателей для данного письма(разделенные запятыми), 
	 * которые будут отображены на форме
	 */
	public String getRecipientListAsString(){
		String return_value="";
		try{
			// получить из сессии объект со всеми добавленными пользователями
			OfficePrivateSession session=(OfficePrivateSession)this.getSession();
			UserList list=session.getUserList("RECIPIENT");
			return_value=list.getListDelimeterString(", ");
			// создать строку, разделенную запятыми
		}catch(Exception ex){
			out_error("Error in getting list of user's by name:RECIPIENT");
		}
		return return_value;
		
	}
	
	/** метод SET для модели UserList  */
	public void setUserList(){
	}
	
	/** метод GET для модели UserList */
	public String getUserList(){
		String return_value=getRecipientListAsString();
		/** проверка добавленных пользователей на длинну*/
		if(return_value.length()>field_max_length){
			return_value=return_value.substring(0,field_max_length-3)+"...";
		}
		return return_value;
	}
}

/** содержит данные о форме, на которой расположены текстовые данные */
class FormModel implements Serializable{
	/** */
	private static final long serialVersionUID = 1L;
	private String message_text="";

	public String getMessage_text() {
		System.out.println("FormModel: getMessage:");
		return message_text;
	}

	public void setMessage_text(String message_text) {
		System.out.println("FromModel: setMessage:");
		this.message_text = message_text;
	}
	public FormModel(){
		System.out.println("FormModel constructor");
	}
}