package bonclub.office_private.web_gui.user_area.panel_main.profile_edit.professional_group;

import java.sql.Connection;

import java.sql.ResultSet;
import java.util.ArrayList;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
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

/** Профиль. Профиль пользователя. Адрес*/
public class ProfessionalGroup extends Panel{
	private final static long serialVersionUID=1L;
	private final ArrayList<String> listProf=new ArrayList<String>();
	private final ArrayList<String> listIdProf=new ArrayList<String>();
	private final Model<String> modelListProf=new Model<String>();
	private DropDownChoice<String> comboboxProf;
	
	private final Model<String> modelProf=new Model<String>();
	private TextField<String> editProf;
	private Form<Object> formMain;
	
	/** Профиль. Профиль пользователя. Адрес
	 * @param id - уникальный идентификатор для панели 
	 * @param userId - уникальный идентификатор пользователя 
	 * */
	public ProfessionalGroup(String id,UsersParent users){
		super(id);
		initData(users);
		initComponents();
	}

	/** инициализация данных */
	private void initData(UsersParent users){
		ResultSet rs=null;
		Connection connection=null;
		try{
			connection=((OfficePrivateApplication)this.getApplication()).getDatabaseParentConnection();
			// получить все возможные варианты групп, и загрузить их в ListProf
			rs=connection.createStatement().executeQuery("select * from bc_admin.vc_nat_prs_group_all");
			while(rs.next()){
				this.listIdProf.add(rs.getString("CD_NAT_PRS_GROUP"));
				this.listProf.add(rs.getString("NAME_NAT_PRS_GROUP"));
			}
			// получить имя группы, которое выбрано для пользователя - назначить модели объектов  
			UsersParent user=this.getUsersParent(connection);
			if((user.getProf()!=null)&&(!user.getProf().equals(""))){
				if(user.isProfOther()){
					// вывести другую профессию
					int index=this.listIdProf.indexOf(UsersParent.keyProfOther);
					this.modelListProf.setObject(this.listProf.get(index));
					this.modelProf.setObject(user.getProfOther());
				}else{
					int index=this.listIdProf.indexOf(user.getProfId());
					this.modelListProf.setObject(this.listProf.get( index));
					this.modelProf.setObject("");
				}
			}else{
				// назначить значения по умолчанию 
				this.modelListProf.setObject(this.listIdProf.get(0));
				this.modelProf.setObject("");
			}
			//user.getProf();
			// проверить на принадлежность к стандарту
			
		}catch(Exception ex){
			System.err.println("profile_edit.ProfessionalGroup#initData:  Error in load initData Exception: "+ex.getMessage());
		}finally{
			try{
				rs.close();
			}catch(Exception ex){};
			try{
				connection.close();
			}catch(Exception ex){};
		}

	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		formMain=new Form<Object>("form_main_prof");
		comboboxProf=new DropDownChoice<String>("combobox_prof",modelListProf, listProf); 
		formMain.add(comboboxProf);

		editProf=new TextField<String>("edit_prof",modelProf);
		formMain.add(editProf);

		Button buttonSend=new Button("button_send"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonSend();
			}
		};
		buttonSend.add(new SimpleAttributeModifier("value",this.getString("form_main_prof.button_send.Caption")));
		formMain.add(buttonSend);

		formMain.add(new ComponentFeedbackPanel("form_main_error",formMain));
		this.add(formMain);
	}

	/** сохранить полученные данны */
	private void onButtonSend() {
		// получить объект по данному пользователю
		Connection connection=null;
		try{
			connection=((OfficePrivateApplication)this.getApplication()).getDatabaseParentConnection();
			UsersParent user=this.getUsersParent(connection);
			// проверить на "Другой вариант"
			int indexOf=this.listProf.indexOf(this.modelListProf.getObject());
			if(this.listIdProf.get(indexOf).equals(UsersParent.keyProfOther)){
				// другой вариант
				user.setProf(this.modelProf.getObject(), connection);
			}else{
				user.setProf(this.modelListProf.getObject(),connection);
			}
			String returnValue=user.updateObjectInDatabase(connection);
			if(returnValue!=null){
				// ошибка сохранения данных 
				this.formMain.error(this.getString("save_data_error"));
			}else{
				// данные успешно сохранены 
			}
		}catch(Exception ex){
			System.out.println("profile_edit#onButtonSend Exception: "+ex.getMessage());
			this.formMain.error(this.getString("save_data_error"));
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
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
			System.err.println("ProfessionalGroup#getBoncardNumberByCurrentUser Exception: "+ex.getMessage());
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
			System.err.println("ProfessionalGroup error get object UsersParent: "+ex.getMessage());
		}
		return returnValue;
	}
	
}
