package bonclub.office_private.web_gui.user_area.panel_main.profile_edit.contact_information;

import java.sql.Connection;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;
import bonclub.office_private.database.wrap.Users;
import bonclub.office_private.database.wrap.UsersParent;
import bonclub.office_private.session.OfficePrivateSession;

import wicket_utility.PatternValidator;

/** Профиль. Профиль пользователя. Адрес*/
public class ContactInformation extends Panel{
	private final static long serialVersionUID=1L;
	private String[] values=new String[3];
	private Model<String> modelPhoneMobile=new Model<String>();
	private TextField<String> editPhoneMobile=new TextField<String>("edit_phone_mobile");
	private Model<String> modelPhoneHome=new Model<String>();
	private TextField<String> editPhoneHome=new TextField<String>("edit_phone_home");
	private Model<String> modelEmail=new Model<String>();
	private TextField<String> editEmail=new TextField<String>("edit_email");
	private Form<Object> formMain;	
	/** Профиль. Профиль пользователя. Адрес
	 * @param id - уникальный идентификатор для панели 
	 * @param userId - уникальный идентификатор пользователя 
	 * */
	public ContactInformation(String id, UsersParent user){
		super(id);
		initData(user);
		initComponents();
	}

	/** инициализация данных */
	private void initData(UsersParent user){
		// INFO Профиль(редактирование). Контактная информация 
		values[0]=user.getPhoneMobile();
		values[1]=user.getPhoneHome();
		values[2]=user.getEmail();
		
		this.modelPhoneMobile.setObject(values[0]);
		this.modelPhoneHome.setObject(values[1]);
		this.modelEmail.setObject(values[2]);
		
		this.editPhoneMobile.setModel(this.modelPhoneMobile);
		this.editPhoneHome.setModel(this.modelPhoneHome);
		this.editEmail.setModel(this.modelEmail);
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		this.editPhoneMobile.setRequired(true);
		this.editPhoneMobile.add(new PatternValidator("[0-9]{11,12}","form_main.edit_phone_mobile.error"){
			private final static long serialVersionUID=1L;
			@Override
			public String prepareValue(String value) {
				return value.replaceAll("[()+ \\[\\]\\.]", "");
			}
		});
		this.editPhoneHome.add(new PatternValidator("[0-9](11,12)","form_main.edit_phone_mobile.error"){
			private final static long serialVersionUID=1L;
			@Override
			public String prepareValue(String value) {
				return value.replaceAll("[()+ [].]", "");
			}
		});
		this.editEmail.add(new PatternValidator("[A-Za-z]+[A-Za-z0-9]*@[A-Za-z0-9]*.[A-Za-z]{1,3}","form_main.edit_email.error"));
		
		formMain=new Form<Object>("form_main_contact");
		formMain.add(this.editPhoneMobile);
		formMain.add(new ComponentFeedbackPanel("edit_phone_mobile_error",editPhoneMobile));
		formMain.add(this.editPhoneHome);
		formMain.add(new ComponentFeedbackPanel("edit_phone_home_error",editPhoneHome));
		formMain.add(this.editEmail);
		formMain.add(new ComponentFeedbackPanel("edit_email_error",editEmail));
		
		Button buttonSave=new Button("button_save"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonSave();
			}
		};
		buttonSave.add(new SimpleAttributeModifier("value",this.getString("button_save.caption")));
		formMain.add(buttonSave);

		this.add(formMain);
	}

	public static void main(String[] args){
		String value="111[222]333";
		System.out.println(value.replaceAll("[()+ \\[\\]\\.]", ""));
	}
	
	private void onButtonSave() {
		Connection connection=null;
		try{
			connection=((OfficePrivateApplication)this.getApplication()).getDatabaseParentConnection();
			UsersParent userParent=this.getUsersParent(connection);
			userParent.setPhoneMobile(this.editPhoneMobile.getModelObject());
			userParent.setPhoneHome(this.editPhoneHome.getModelObject());
			userParent.setEmail(this.editEmail.getModelObject());
			String returnValue=userParent.updateObjectInDatabase(connection);
			if(returnValue!=null){
				this.formMain.error(this.getString("save_data_error"));
			}
			
		}catch(Exception ex){
			System.out.println("ContactInformation#onButtonSave Exception: "+ex.getMessage());
			this.formMain.error(this.getString("save_data_error"));
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
		System.out.println("ContactInformation#onButtonSave: ");
	}
	
	/** по текущему пользователю получить номер BonCard*/
	private String getBoncardNumberByCurrentUser(){
		String returnValue=null;
		ConnectUtility connector=((OfficePrivateApplication)this.getApplication()).getConnectUtility();
		Integer userId=((OfficePrivateSession)this.getSession()).getCustomerId();
		try{
			Users currentUser=(Users)connector.getSession().get(Users.class, userId);
			returnValue=currentUser.getBoncardNumber();
		}catch(Exception ex){
			System.err.println("ContactInformation#getBoncardNumberByCurrentUser Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	/** получить данные пользователя на основании номера карты */
	private UsersParent getUsersParent(Connection connection){
		UsersParent returnValue=new UsersParent();
		try{
			returnValue.fillData(connection, getBoncardNumberByCurrentUser());
		}catch(Exception ex){
			System.err.println("ContactInformation#getUsersParent error get object UsersParent: "+ex.getMessage());
		}
		return returnValue;
	}

}
