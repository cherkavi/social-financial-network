package bonclub.office_private.web_gui.login.panel_main.private_office_enter;

import org.apache.wicket.behavior.SimpleAttributeModifier;


import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.hibernate.criterion.Restrictions;

import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;
import bonclub.office_private.database.Connector;
import bonclub.office_private.database.wrap.Users;
import bonclub.office_private.web_gui.login.Login;

import wicket_utility.ActionExecutor;

/** панель, отображающая вход в систему по логину и паролю */
public class PanelMainPrivateOfficeEnter extends Panel{
	private final static long serialVersionUID=1L;
	private ActionExecutor executor;
	private Model<String> modelLogin=new Model<String>();
	private Model<String> modelPassword=new Model<String>();
	private Form<Object> formMain;
	
	public PanelMainPrivateOfficeEnter(String panelId,ActionExecutor executor){
		super(panelId);
		this.executor=executor;
		initComponents();
	}
	
	private void initComponents(){
		formMain=new Form<Object>("form_main");
		TextField<String> editLogin=new TextField<String>("edit_login",modelLogin);
		editLogin.setRequired(true);
		formMain.add(editLogin);
		formMain.add(new ComponentFeedbackPanel("error_login", editLogin));
		
		PasswordTextField editPassword=new PasswordTextField("edit_password",modelPassword);
		formMain.add(editPassword);
		formMain.add(new ComponentFeedbackPanel("error_password", editPassword));

		Button buttonEnter=new Button("button_enter"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonSubmit();
			};
		};
		buttonEnter.add(new SimpleAttributeModifier("value",this.getString("button_enter_caption")));
		editPassword.setRequired(true);
		formMain.add(buttonEnter);
		this.add(formMain);
		formMain.add(new ComponentFeedbackPanel("error_form", formMain));
	}
	

	/** нажата кнопка ввода - проверить логин и пароль  */
	private void onButtonSubmit(){
		if(isValidLoginAndPassword(this.modelLogin.getObject(), this.modelPassword.getObject())){
			this.executor.action(Login.actionPrivateOfficeEnterLoginPassword, new String[]{this.modelLogin.getObject(), this.modelPassword.getObject()});
		}else{
			// login and password is not valid
			this.formMain.error(this.getString("form_error"));
		}
	}
	
	/** проверить логин и пароль */
	private boolean isValidLoginAndPassword(String login, String password){
		boolean returnValue=false;
		ConnectUtility connector=getConnectUtility();
		try{
			Users customer=(Users)connector.getSession()
												   .createCriteria(Users.class)
												   .add(Restrictions.eq("login", login))
												   .add(Restrictions.eq("password",password))
												   .list().get(0);
			if(customer!=null){
				returnValue=true;
			}else{
				returnValue=false;
			}
		}catch(Exception ex){
			System.err.println("PanelMainPrivateOfficeEnter#isValidLoginAndPassword user is not found: "+ex.getMessage());
		}
		connector.close();
		return returnValue;
	}
	
	private ConnectUtility getConnectUtility(){
		Connector connector=((OfficePrivateApplication)this.getApplication()).getHibernateConnection();
		return new ConnectUtility(connector);
	}
	
}
