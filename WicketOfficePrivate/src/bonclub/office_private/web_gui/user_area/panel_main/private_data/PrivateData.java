package bonclub.office_private.web_gui.user_area.panel_main.private_data;

import java.sql.Connection;
import java.util.ArrayList;


import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;

import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;
import bonclub.office_private.database.wrap.Users;
import bonclub.office_private.database.wrap.UsersParent;
import bonclub.office_private.session.OfficePrivateSession;
import wicket_utility.ActionExecutor;

/** панель, котора€ отображает личную информацию вошедшего пользовател€ */
public class PrivateData extends Panel {
	private final static long serialVersionUID=1L;
	/** ActionExecutor */
	@SuppressWarnings("unused")
	private ActionExecutor executor;
	
	/** панель, котора€ отображает личную информацию вошедшего пользовател€ 
	 * @param id - уникальный идентификатор панели 
	 * @param executor - исполнитель дл€ действий 
	 * (уникальный номер текущего пользовател€ получаем из сессионного компонента )
	 */
	public PrivateData(String id, ActionExecutor executor){
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
			System.err.println("PrivateData#getBoncardNumberByCurrentUser Exception: "+ex.getMessage());
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
			System.err.println("PrivateData#getUsersParent error get object UsersParent: "+ex.getMessage());
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
		panelsName.add("user_data.UserData");
		panelsName.add("bonus_tools.BonusTools");
		panelsName.add("add_info.AddInfo");
		//panelsName.add("sells_information.SellsInformation");
		//panelsName.add("sells_history.SellsHistory");
		String tempPackagePreamble=this.getClass().getPackage().toString();
		int spacePosition=tempPackagePreamble.indexOf(" ");
		final String packagePreamble=tempPackagePreamble.substring(spacePosition+1);
		final UsersParent usersParent=this.getUsersParent();
		
		ListView<String> listOfPanel=new ListView<String>("list_of_panels",panelsName){
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(getPanelByName(packagePreamble, item.getModelObject(), "panel_with_data", usersParent));
			}
			
			/** создать объект под-панели, на основании конструктора */
			private Panel getPanelByName(String packageName, String className, String id, UsersParent user){
				// получить объект на основании имени класса и конструктора String, Integer
				Panel returnValue=new EmptyPanel(id);
				try{
					System.out.println("ѕолучить класс:"+packageName+"."+className);
					Class<?> classOfPanel=Class.forName(packageName+"."+className);
					System.out.println("ѕолучить объект класса ");
					returnValue=(Panel)classOfPanel.getConstructor(String.class, UsersParent.class).newInstance(id,usersParent);
				}catch(Exception ex){
					System.err.println("PrivateData#getPanelByName Exception: \n"+ex.getMessage());
				}
				return (returnValue==null)?(new EmptyPanel(id)):returnValue;
			}
			
		};
		this.add(listOfPanel);
	}
	
	
}
