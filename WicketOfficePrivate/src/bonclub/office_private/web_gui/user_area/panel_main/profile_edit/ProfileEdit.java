package bonclub.office_private.web_gui.user_area.panel_main.profile_edit;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;
import bonclub.office_private.database.wrap.Users;
import bonclub.office_private.database.wrap.UsersParent;
import bonclub.office_private.session.OfficePrivateSession;
import bonclub.office_private.web_gui.user_area.UserArea;
import bonclub.office_private.web_gui.user_area.panel_main.profile_edit.professional_group.ProfessionalGroup;
import bonclub.office_private.web_gui.user_area.panel_main.profile_edit.address.Address;
import bonclub.office_private.web_gui.user_area.panel_main.profile_edit.contact_information.ContactInformation;
import bonclub.office_private.web_gui.user_area.panel_main.profile_edit.main.Main;

import wicket_utility.ActionExecutor;

/** панель, которая отображает профиль пользователя */
public class ProfileEdit extends Panel {
	private final static long serialVersionUID=1L;
	/** ActionExecutor */
	private ActionExecutor executor;
	
	/** панель, которая отображает профиль пользователя */
	public ProfileEdit(String id, ActionExecutor executor){
		super(id);
		this.executor=executor;
		this.initComponents();
	}

	private void initComponents() {
		/** имена панелей, которые нужно отобразить */
		ArrayList<String> panelsName=new ArrayList<String>();
		// Место добавление имен дополнительных панелей 
		panelsName.add("main.Main");
		panelsName.add("address.Address");
		panelsName.add("contact_information.ContactInformation");
		panelsName.add("professional_group.ProfessionalGroup");
/*		
		// имя пакета данного класса		
		String tempPackagePreamble=this.getClass().getPackage().toString();
		int spacePosition=tempPackagePreamble.indexOf(" ");
        final String packagePreamble=tempPackagePreamble.substring(spacePosition+1);
		//список из панелей, которые нужно добавить на форму
		ListView<String> listOfPanel=new ListView<String>("list_of_panels",panelsName){
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(getPanelByName(packagePreamble, item.getModelObject(), "panel_with_data"));
			}
			
			// создать объект под-панели, на основании конструктора 
			private Panel getPanelByName(String packageName, String className, String id){
				// получить объект на основании имени класса и конструктора String, Integer
				Panel returnValue=new EmptyPanel(id);
				try{
					//System.out.println("Получить класс:"+packageName+"."+className);
					Class<?> classOfPanel=Class.forName(packageName+"."+className);
					//System.out.println("Получить объект класса ");
					returnValue=(Panel)classOfPanel.getConstructor(String.class, Integer.class).newInstance(id,ProfileEdit.this.userId);
				}catch(Exception ex){
					//System.err.println("PrivateData#getPanelByName Exception: \n"+ex.getMessage());
				}
				return (returnValue==null)?(new EmptyPanel(id)):returnValue;
			}
			
		};
		this.add(listOfPanel);
*/
		UsersParent usersParent=this.getUsersParent();
		this.add(new Main("panel_main",usersParent));
		this.add(new Address("panel_address",usersParent));
		this.add(new ContactInformation("panel_contact", usersParent));
		this.add(new ProfessionalGroup("panel_professional", usersParent));
		
		Form<Object> formMain=new Form<Object>("form_main");
		Button buttonEdit=new Button("button_close"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				ProfileEdit.this.onEditEnd();
			};
		};
		buttonEdit.add(new SimpleAttributeModifier("value",this.getString("form_main.button_close.Value")));
		formMain.add(buttonEdit);
		this.add(formMain);
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
			System.err.println("UserData#getBoncardNumberByCurrentUser Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	/** получить данные пользователя на основании номера карты */
	private UsersParent getUsersParent(){
		UsersParent returnValue=new UsersParent();
		Connection connection=null;
		try{
			connection=((OfficePrivateApplication)this.getApplication()).getDatabaseParentConnection();
			returnValue.fillData(connection, getBoncardNumberByCurrentUser());
		}catch(Exception ex){
			System.err.println("PrivateData error get object UsersParent: "+ex.getMessage());
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	/** редактирование данныех*/
	private void onEditEnd(){
		this.executor.action(UserArea.eventProfileEditEnd, null);
	}
}

