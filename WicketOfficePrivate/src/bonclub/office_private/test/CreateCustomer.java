package bonclub.office_private.test;

import java.sql.Connection;

import java.util.Date;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.Connector;
import bonclub.office_private.database.wrap.Users;
import bonclub.office_private.web_gui.login.Login;

import wicket_utility.PatternValidator;

public class CreateCustomer extends WebPage{
	private TextField<String> editLogin;
	private TextField<String> editPassword;
	private TextField<String> editBoncard;
	private Form<Object> formMain;
	
	public CreateCustomer(){
		initComponents();
	}
	
	private void initComponents(){
		Model<String> model=new Model<String>();
		try{
			model.setObject(this.getString("title_message"));
		}catch(Exception ex){
		}
		Label titleMessage=new Label("title_message",model);
		this.add(titleMessage);
		
		formMain=new Form<Object>("form_main");

		this.editLogin=new TextField<String>("edit_login",new Model<String>(""));
		this.editLogin.setRequired(true);
		formMain.add(this.editLogin);
		formMain.add(new ComponentFeedbackPanel("edit_login_error",this.editLogin));
		
		this.editPassword=new TextField<String>("edit_password",new Model<String>(""));
		this.editPassword.setRequired(true);
		formMain.add(this.editPassword);
		formMain.add(new ComponentFeedbackPanel("edit_password_error",this.editPassword));
		
		this.editBoncard=new TextField<String>("edit_boncard_number",new Model<String>(""));
		this.editBoncard.setRequired(true);
		this.editBoncard.add(new PatternValidator("[0-9]{5}","error_boncard_format"){
			private final static long serialVersionUID=1L;
			@Override
			public String prepareValue(String value) {
				return value.replaceAll("( ) - + [ ] . ", "");
			}
		});
		formMain.add(this.editBoncard);
		formMain.add(new ComponentFeedbackPanel("edit_boncard_error",this.editBoncard));

		formMain.add(new ComponentFeedbackPanel("form_error",this.formMain));
		Button buttonAdd=new Button("button_add"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonAdd();
			}
		};
		buttonAdd.add(new SimpleAttributeModifier("value",this.getString("button_add.caption")));
		formMain.add(buttonAdd);
		this.add(formMain);
	}

	
	private void onButtonAdd(){
		Connection connection=null;
		Session session=null;
		try{
			Connector connector=((OfficePrivateApplication)this.getApplication()).getHibernateConnection();
			connection=connector.getConnection();
			session=connector.openSession(connection);
			while(true){
				if(isLoginRepeat(this.editLogin.getModelObject(),session)){
					this.editLogin.error(this.getString("error_login_exists"));
					break;
				}
				if(isBoncardNumberRepeat(this.editBoncard.getModelObject(),session)){
					this.editBoncard.error(this.getString("error_boncard_exists"));
					break;
				}
				if(saveValue(this.editLogin.getModelObject(), this.editPassword.getModelObject(), this.editBoncard.getModelObject(),session)==true){
					// данные успешно сохранены
					this.setResponsePage(Login.class);
				}else{
					// ошибка сохранения данных 
					this.formMain.error(this.getString("error_save"));
				}
				
				break;
			}
			
		}catch(Exception ex){
			System.err.println("CreateCustomer#onButtonAdd Exception: "+ex.getMessage());
		}finally{
			try{
				session.close();
			}catch(Exception ex){};
			try{
				connection.close();
			}catch(Exception ex){};
		}
		
		
	}
	
	/** сохранить полученные данные */
	private boolean saveValue(String login, 
							  String password,
							  String boncard,
							  Session session) {
		boolean returnValue=false;
		try{
			Users customers=new Users();
			customers.setLogin(login);
			customers.setPassword(password);
			customers.setBoncardNumber(boncard);
			customers.setCreateTime(new Date());
			session.beginTransaction();
			session.save(customers);
			session.getTransaction().commit();
			returnValue=true;
		}catch(Exception ex){
			System.err.println("CreateCustomer#saveValue: Exception:"+ex.getMessage());
			returnValue=false;
		}
		return returnValue;
	}

	private boolean isLoginRepeat(String login,Session session){
		boolean returnValue=false;
		try{
			if(session.createCriteria(Users.class).add(Restrictions.eq("login",login)).list().size()>0){
				returnValue=true;
			}else{
				returnValue=false;
			}
		}catch(Exception ex){
			System.err.println("CreateCustomer#isLoginRepeat Exception: "+ex.getMessage());
		}
		return returnValue;
	}
	private boolean isBoncardNumberRepeat(String boncardNumber, Session session){
		boolean returnValue=false;
		try{
			String value=boncardNumber.replaceAll("( ) - + [ ] . ", "");
			if(session.createCriteria(Users.class).add(Restrictions.eq("boncardNumber",value)).list().size()>0){
				returnValue=true;
			}else{
				returnValue=false;
			}
		}catch(Exception ex){
			System.err.println("CreateCustomer#isBoncardNumberRepeat Exception: "+ex.getMessage());
		}
		return returnValue;
	}
}
