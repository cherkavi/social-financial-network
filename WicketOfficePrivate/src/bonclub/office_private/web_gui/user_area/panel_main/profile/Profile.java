package bonclub.office_private.web_gui.user_area.panel_main.profile;

import java.sql.Connection;
import java.util.ArrayList;


import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;

import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;
import bonclub.office_private.database.wrap.Users;
import bonclub.office_private.database.wrap.UsersParent;
import bonclub.office_private.session.OfficePrivateSession;
import bonclub.office_private.web_gui.user_area.UserArea;

import wicket_utility.ActionExecutor;

/** панель, котора€ отображает профиль пользовател€ */
public class Profile extends Panel {
	private final static long serialVersionUID=1L;
	/** ActionExecutor */
	private ActionExecutor executor;
	
	/** панель, котора€ отображает профиль пользовател€ 
	 * @param id - уникальный идентификатор панели 
	 * @param executor - испольнитель, на который идет ссылка дл€ реализации действий
	 * (уникальный идентификатор пользовател€ получаем из сессии )
	 */
	public Profile(String id, ActionExecutor executor){
		super(id);
		this.executor=executor;
		this.initComponents();
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
	
	/** получить данные пользовател€ на основании номера карты */
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
	
	
	private void initComponents() {
		/** имена панелей, которые нужно отобразить */
		ArrayList<String> panelsName=new ArrayList<String>();
		// ћесто добавление имен дополнительных панелей 
		panelsName.add("main.Main");
		panelsName.add("address.Address");
		panelsName.add("contact_information.ContactInformation");
		panelsName.add("professional_group.ProfessionalGroup");
		String tempPackagePreamble=this.getClass().getPackage().toString();
		int spacePosition=tempPackagePreamble.indexOf(" ");
		final String packagePreamble=tempPackagePreamble.substring(spacePosition+1);
		/** объект, который */
		final UsersParent usersParent=this.getUsersParent();
		/** список из панелей, которые нужно добавить на форму */
		ListView<String> listOfPanel=new ListView<String>("list_of_panels",panelsName){
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(getPanelByName(packagePreamble, item.getModelObject(), "panel_with_data",usersParent));
			}
			/** создать объект под-панели, на основании конструктора */
			private Panel getPanelByName(String packageName, String className, String id, UsersParent user){
				// получить объект на основании имени класса и конструктора String, Integer
				Panel returnValue=new EmptyPanel(id);
				try{
					//System.out.println("ѕолучить класс:"+packageName+"."+className);
					Class<?> classOfPanel=Class.forName(packageName+"."+className);
					//System.out.println("ѕолучить объект класса ");
					returnValue=(Panel)classOfPanel.getConstructor(String.class, UsersParent.class).newInstance(id,user);
				}catch(Exception ex){
					System.err.println("PrivateData#getPanelByName Exception: \n"+ex.getMessage());
				}
				return (returnValue==null)?(new EmptyPanel(id)):returnValue;
			}
			
		};
		this.add(listOfPanel);
		Form<Object> formMain=new Form<Object>("form_main");
		Button buttonEdit=new Button("button_edit"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				Profile.this.onEdit();
			};
		};
		buttonEdit.add(new SimpleAttributeModifier("value",this.getString("form_main.button_edit.Value")));
		formMain.add(buttonEdit);
		this.add(formMain);
	}
	
	
	/** редактирование данныех*/
	private void onEdit(){
		this.executor.action(UserArea.eventProfileRequestEdit, null);
	}
}

