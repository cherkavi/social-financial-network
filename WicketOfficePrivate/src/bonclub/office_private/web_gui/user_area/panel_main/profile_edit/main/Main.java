package bonclub.office_private.web_gui.user_area.panel_main.profile_edit.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;
import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;
import bonclub.office_private.database.Connector;
import bonclub.office_private.database.wrap.Users;
import bonclub.office_private.database.wrap.UsersParent;
import bonclub.office_private.session.OfficePrivateSession;

/** Профиль. Профиль пользователя. */
public class Main extends Panel{
	private final static long serialVersionUID=1L;
	private Object[] values=new Object[6];
	
	private final Model<String> modelName=new Model<String>();
	private final TextField<String> editName=new TextField<String>("value_1");

	private final Model<String> modelSurname=new Model<String>();
	private final TextField<String> editSurname=new TextField<String>("value_2");

	private final Model<String> modelFatherName=new Model<String>();
	private final TextField<String> editFatherName=new TextField<String>("value_3");

	private final Model<String> modelNick=new Model<String>();
	private final TextField<String> editNick=new TextField<String>("value_4");

	private final Model<Date> modelBirthDay=new Model<Date>();
	private final DateTextField editBirthDay=new DateTextField("value_5","dd.MM.yyyy");
	
	private final ArrayList<String> listSexVariables=new ArrayList<String>();
	private final Model<String> modelSex=new Model<String>();
	private DropDownChoice<String> dropDownSex;
	private Form<Object> formMain;
	
	/** Профиль. Профиль пользователя.  
	 * @param id - уникальный идентификатор для панели 
	 * @param userId - уникальный идентификатор пользователя 
	 * */
	public Main(String id, UsersParent usersParent){
		super(id);
		initData(usersParent);
		initComponents();
	}

	private String getNick(){
		String returnValue=null;
		Integer userId=((OfficePrivateSession)this.getSession()).getCustomerId();
		ConnectUtility connector=this.getConnectUtility();
		try{
			Users user=(Users)connector.getSession().get(Users.class, userId);
			returnValue=user.getNick();
		}catch(Exception ex){
			System.out.println("profile_edit.Main#getNick Exception: "+ex.getMessage());
		}
		connector.close();
		return returnValue;
	}
	
	/** заполнить объект {@see this#listSexVariables } */
	private void fillListSexVariables(){
		Connection connection=null;
		try{
			connection=((OfficePrivateApplication)this.getApplication()).getDatabaseParentConnection();
			ResultSet rs=connection.createStatement().executeQuery("SELECT lookup_code, meaning  FROM  bc_admin.vc_lookup_values  WHERE name_lookup_type = 'MALE_FEMALE' ORDER BY number_value");
			while(rs.next()){
				this.listSexVariables.add(rs.getString("meaning"));
			}
		}catch(Exception ex){
			System.out.println("profile_edit.Main#fillListSexVariables Exception: "+ex.getMessage());
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
	}
	
	/** инициализация данных */
	private void initData(UsersParent user){
		// INFO Профиль(редактирование). Профиль пользователя.
		values[0]=user.getName();
		values[1]=user.getSurname();
		values[2]=user.getFatherName();
		values[3]=user.getBirthDate();
		String nick=this.getNick();
		values[4]=(nick==null)?"":nick;
		values[5]=user.getSex();
		
		modelName.setObject((String)values[0]);
		modelSurname.setObject((String)values[1]);
		modelFatherName.setObject((String)values[2]);
		try{
			modelBirthDay.setObject((java.util.Date)values[3]);
		}catch(Exception ex){
		}
		modelNick.setObject((String)values[4]);
		this.fillListSexVariables();
		for(int counter=0;counter<this.listSexVariables.size();counter++){
			if(this.listSexVariables.get(counter).equalsIgnoreCase(user.getSex())){
				this.modelSex.setObject(this.listSexVariables.get(counter));
				break;
			}
		}
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		formMain=new Form<Object>("form_main_main");
		editName.setModel(modelName);
		editName.setRequired(false);
		editName.setEnabled(false);
		formMain.add(editName);
		formMain.add(new ComponentFeedbackPanel("value_1_error",editName));
		
		editSurname.setModel(modelSurname);
		editSurname.setRequired(false);
		editSurname.setEnabled(false);
		formMain.add(editSurname);
		formMain.add(new ComponentFeedbackPanel("value_2_error",editSurname));
		
		editFatherName.setModel(modelFatherName);
		editFatherName.setEnabled(false);
		formMain.add(editFatherName);
		
		editNick.setModel(modelNick);
		editNick.setRequired(true);
		formMain.add(editNick);
		formMain.add(new ComponentFeedbackPanel("value_4_error",editNick));
		
		editBirthDay.setModel(modelBirthDay);
		//editBirthDay.add(new DatePicker());
		editBirthDay.setEnabled(false);
		formMain.add(editBirthDay);
		formMain.add(new ComponentFeedbackPanel("value_5_error",editBirthDay));
		
		this.dropDownSex=new DropDownChoice<String>("value_6",this.modelSex, this.listSexVariables);
		this.dropDownSex.setEnabled(false);
		formMain.add(dropDownSex);

		Button buttonSave=new Button("button_save"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonSave();
			};
		};
		buttonSave.add(new SimpleAttributeModifier("value",this.getString("button_save.caption")));
		formMain.add(buttonSave);
		
		formMain.add(new ComponentFeedbackPanel("form_main_error",formMain));
		this.add(formMain);
	}

	protected void onButtonSave() {
		// проверить на введенные значения:
		//editName.error("");
		//editSurname.error("");
		//editBirthDay.error("");
		// сохранять можно только Nick:
		ConnectUtility connector=this.getConnectUtility();
		Integer customerId=this.getCurrentCustomerId();
		try{
			Session session=connector.getSession();
			Users customer=(Users)session.get(Users.class, customerId);
			//customer.setName(this.editName.getModelObject());
			//customer.setSurname(this.editSurname.getModelObject());
			//customer.setBirthday(this.editBirthDay.getModelObject());
			//customer.setFatherName(this.editFatherName.getModelObject());
			//customer.setSex( this.modelSex.getObject().equals(this.getString("sex.male"))?1:0);
			customer.setNick(this.modelNick.getObject());
			session.beginTransaction();
			session.update(customer);
			session.getTransaction().commit();
		}catch(Exception ex){
			this.formMain.error(this.getString("save_error"));
			System.err.println("Save Exception");
		}
		System.out.println("on button save");
	}
	

	/** получить уникальный номер текущего Пользователя из сессии */
	private Integer getCurrentCustomerId(){
		return ((OfficePrivateSession)this.getSession()).getCustomerId();
	}
	
	/** получить уникальный номер текущего пользователя из сессии */
	private ConnectUtility getConnectUtility(){
		Connector connector=((OfficePrivateApplication)this.getApplication()).getHibernateConnection();
		return new ConnectUtility(connector);
	}
	
}
