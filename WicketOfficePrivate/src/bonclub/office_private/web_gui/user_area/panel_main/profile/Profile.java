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

/** ������, ������� ���������� ������� ������������ */
public class Profile extends Panel {
	private final static long serialVersionUID=1L;
	/** ActionExecutor */
	private ActionExecutor executor;
	
	/** ������, ������� ���������� ������� ������������ 
	 * @param id - ���������� ������������� ������ 
	 * @param executor - ������������, �� ������� ���� ������ ��� ���������� ��������
	 * (���������� ������������� ������������ �������� �� ������ )
	 */
	public Profile(String id, ActionExecutor executor){
		super(id);
		this.executor=executor;
		this.initComponents();
	}

	/** �� �������� ������������ �������� ����� BonCard*/
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
	
	/** �������� ������ ������������ �� ��������� ������ ����� */
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
		/** ����� �������, ������� ����� ���������� */
		ArrayList<String> panelsName=new ArrayList<String>();
		// ����� ���������� ���� �������������� ������� 
		panelsName.add("main.Main");
		panelsName.add("address.Address");
		panelsName.add("contact_information.ContactInformation");
		panelsName.add("professional_group.ProfessionalGroup");
		String tempPackagePreamble=this.getClass().getPackage().toString();
		int spacePosition=tempPackagePreamble.indexOf(" ");
		final String packagePreamble=tempPackagePreamble.substring(spacePosition+1);
		/** ������, ������� */
		final UsersParent usersParent=this.getUsersParent();
		/** ������ �� �������, ������� ����� �������� �� ����� */
		ListView<String> listOfPanel=new ListView<String>("list_of_panels",panelsName){
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(getPanelByName(packagePreamble, item.getModelObject(), "panel_with_data",usersParent));
			}
			/** ������� ������ ���-������, �� ��������� ������������ */
			private Panel getPanelByName(String packageName, String className, String id, UsersParent user){
				// �������� ������ �� ��������� ����� ������ � ������������ String, Integer
				Panel returnValue=new EmptyPanel(id);
				try{
					//System.out.println("�������� �����:"+packageName+"."+className);
					Class<?> classOfPanel=Class.forName(packageName+"."+className);
					//System.out.println("�������� ������ ������ ");
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
	
	
	/** �������������� �������*/
	private void onEdit(){
		this.executor.action(UserArea.eventProfileRequestEdit, null);
	}
}

