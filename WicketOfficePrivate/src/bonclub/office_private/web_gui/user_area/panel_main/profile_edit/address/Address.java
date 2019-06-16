package bonclub.office_private.web_gui.user_area.panel_main.profile_edit.address;

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

/** Профиль. Профиль пользователя. Адрес*/
public class Address extends Panel{
	private final static long serialVersionUID=1L;
	private String[] values=new String[8];
	
	private TextField<String> editPostIndex;
	private TextField<String> editOblast;
	private TextField<String> editRegion;
	private TextField<String> editCity;
	private TextField<String> editHouse;
	private TextField<String> editStreet;
	private TextField<String> editHousing;
	private TextField<String> editFlat;
	private Form<Object> formMain;	
	
	/** Профиль. Профиль пользователя. Адрес
	 * @param id - уникальный идентификатор для панели 
	 * @param userId - уникальный идентификатор пользователя 
	 * */
	public Address(String id, UsersParent user){
		super(id);
		initData(user);
		initComponents();
	}

	/** инициализация данных */
	private void initData(UsersParent user){
		// INFO Профиль(редактирование). Профиль пользователя. адрес
		values[0]=user.getPostIndex();
		values[1]=user.getOblast();
		values[2]=user.getRegion();
		values[3]=user.getCity();
		values[4]=user.getStreet();
		values[5]=user.getHouse();
		values[6]=user.getFlatNumber();
		values[7]=user.getHousing();
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		System.out.println("InitComponents");
		// create component's
		editPostIndex=new TextField<String>("edit_post_index",new Model<String>(""));
		editOblast=new TextField<String>("edit_oblast",new Model<String>(""));
		editRegion=new TextField<String>("edit_region",new Model<String>(""));
		editCity=new TextField<String>("edit_city",new Model<String>(""));
		editHouse=new TextField<String>("edit_house",new Model<String>(""));
		editStreet=new TextField<String>("edit_street",new Model<String>(""));
		editHousing=new TextField<String>("edit_housing",new Model<String>(""));
		editFlat=new TextField<String>("edit_flat",new Model<String>(""));

		//editCity.setRequired(true);
		//editStreet.setRequired(true);
		//editHouse.setRequired(true);
		//editFlat.setRequired(true);
		
		// set data to components
		this.editPostIndex.setModelObject(values[0]);
		this.editOblast.setModelObject(values[1]);
		this.editRegion.setModelObject(values[2]);
		this.editCity.setModelObject(values[3]);
		this.editStreet.setModelObject(values[4]);
		this.editHouse.setModelObject(values[5]);
		this.editFlat.setModelObject(values[6]);
		this.editHousing.setModelObject(values[7]);
		
		//placing component's
		formMain=new Form<Object>("form_main_address");
		formMain.add(editPostIndex);
		formMain.add(editOblast);
		formMain.add(editRegion);
		formMain.add(editCity);
		formMain.add(editStreet);
		formMain.add(editHouse);
		formMain.add(editFlat);
		formMain.add(editHousing);

		formMain.add(new ComponentFeedbackPanel("edit_post_index_error",editPostIndex));
		formMain.add(new ComponentFeedbackPanel("edit_city_error",editCity));
		formMain.add(new ComponentFeedbackPanel("edit_street_error",editStreet));
		formMain.add(new ComponentFeedbackPanel("edit_house_error",editHouse));
		formMain.add(new ComponentFeedbackPanel("edit_flat_error",editFlat));
		formMain.add(new ComponentFeedbackPanel("form_main_error",formMain));

		Button buttonSave=new Button("button_save"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonSave();
			}
		};
		buttonSave.add(new SimpleAttributeModifier("value",this.getString("button_save.caption")));
		buttonSave.setDefaultFormProcessing(true);
		formMain.add(buttonSave);
		this.add(formMain);
	}
	
	/** нажата кнопка сохранения данных */
	private void onButtonSave() {
		// check data 
		//editPostIndex.error("");
		//editCity.error("");
		//editStreet.error("");
		//editHouse.error("");
		//editFlat.error("");
		// место обновления данных для Адреса
/*		
  		customer.setPostKod(this.editPostIndex.getModelObject());
		customer.setOblast(this.editOblast.getModelObject());
		customer.setRegion(this.editRegion.getModelObject());
		customer.setCity(this.editCity.getModelObject());
		customer.setStreet(this.editStreet.getModelObject());
		customer.setHomeNumber(this.editHouse.getModelObject());
		customer.setFlat(this.editFlat.getModelObject());
		customer.setCorpus(this.editHousing.getModelObject());
*/	
		// получить объект UsersParent
		Connection connection=null;
		try{
			connection=((OfficePrivateApplication)this.getApplication()).getDatabaseParentConnection();
			UsersParent user=getUsersParent(connection);
			// сохранить данный объект
	  		user.setPostIndex(this.editPostIndex.getModelObject());
			user.setOblast(this.editOblast.getModelObject(),connection);
			user.setRegion(this.editRegion.getModelObject());
			user.setCity(this.editCity.getModelObject());
			user.setStreet(this.editStreet.getModelObject());
			user.setHouse(this.editHouse.getModelObject());
			user.setFlatNumber(this.editFlat.getModelObject());
			user.setHousing(this.editHousing.getModelObject());
			String returnValue=user.updateObjectInDatabase(connection);
			if(returnValue!=null){
				System.out.println("profile_edit.Address Exception: "+returnValue);
				this.formMain.error(this.getString("save_data_error"));
			}
		}catch(Exception ex){
			System.out.println("profile_edit#onButtonSave Exception: "+ex.getMessage());
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
			System.err.println("profile_edit.Address#getBoncardNumberByCurrentUser Exception: "+ex.getMessage());
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
			System.err.println("profile_edit.Address error get object UsersParent: "+ex.getMessage());
		}
		return returnValue;
	}
	
}
