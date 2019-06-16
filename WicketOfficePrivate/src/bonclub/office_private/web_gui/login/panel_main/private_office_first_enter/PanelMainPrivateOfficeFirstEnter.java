package bonclub.office_private.web_gui.login.panel_main.private_office_first_enter;

import org.apache.wicket.behavior.SimpleAttributeModifier;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import wicket_utility.ActionExecutor;
import wicket_utility.PatternValidator;
import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;
import bonclub.office_private.database.Connector;
import bonclub.office_private.database.wrap.Users;
import bonclub.office_private.session.OfficePrivateSession;
import bonclub.office_private.web_gui.login.Login;

/** панель, отображающая первый вход в систему по логину и паролю - смена логина и пароля */
public class PanelMainPrivateOfficeFirstEnter extends Panel{
	private final static long serialVersionUID=1L;
	private ActionExecutor executor;
	private Model<String> modelLogin=new Model<String>();
	private TextField<String> editLogin; 
	private Model<String> modelPassword=new Model<String>();
	private PasswordTextField editPassword;
	private Model<String> modelPasswordAgain=new Model<String>();
	private PasswordTextField editPasswordAgain;
	private Model<String> modelCardNumber=new Model<String>();
	private TextField<String> editCardNumber;
	private Model<String> modelNick=new Model<String>();
	private CheckBox checkRule;
	private Model<Boolean> modelRuleApply=new Model<Boolean>(Boolean.FALSE);
	
	private TextField<String> editNick; 
	private Form<Object> formMain;
	
	public PanelMainPrivateOfficeFirstEnter(String panelId,ActionExecutor executor){
		super(panelId);
		this.executor=executor;
		initComponents();
	}
	
	private void initComponents(){
		formMain=new Form<Object>("form_main");

		editLogin=new TextField<String>("edit_login",modelLogin);
		editLogin.setRequired(true);
		formMain.add(editLogin);
		formMain.add(new ComponentFeedbackPanel("error_login", editLogin));
		
		editPassword=new PasswordTextField("edit_password",modelPassword);
		formMain.add(editPassword);
		formMain.add(new ComponentFeedbackPanel("error_password", editPassword));

		editPasswordAgain=new PasswordTextField("edit_password_again",modelPasswordAgain);
		formMain.add(editPasswordAgain);
		formMain.add(new ComponentFeedbackPanel("error_password_again", editPasswordAgain));

		editCardNumber=new TextField<String>("edit_card_number",modelCardNumber);
		editCardNumber.add(new PatternValidator("[0-9]{19}","form_bon_card_error"));
		formMain.add(editCardNumber);
		formMain.add(new ComponentFeedbackPanel("error_card_number",editCardNumber));
		
		editNick=new TextField<String>("edit_nick",modelNick);
		editNick.setRequired(true);
		formMain.add(editNick);
		formMain.add(new ComponentFeedbackPanel("error_nick", editNick));
		
		Button buttonEnter=new Button("button_enter"){
			private final static long serialVersionUID=1L;
			public void onSubmit() {
				onButtonSubmit();
			};
		};
		buttonEnter.add(new SimpleAttributeModifier("value",this.getString("button_enter_caption")));
		editPassword.setRequired(true);
		formMain.add(buttonEnter);

		checkRule=new CheckBox("check_rule",this.modelRuleApply);
		formMain.add(checkRule);
		formMain.add(new ComponentFeedbackPanel("error_rule",checkRule));
		
		formMain.add(new ComponentFeedbackPanel("error_form", formMain));
		this.add(formMain);
		
	}
	

	/** нажата кнопка ввода - проверить логин и пароль  */
	private void onButtonSubmit(){
		boolean result=isValidData(this.modelLogin.getObject(), 
				   this.modelPassword.getObject(),
				   this.modelPasswordAgain.getObject(),
				   this.modelCardNumber.getObject(),
				   this.modelNick.getObject(),
				   this.modelRuleApply.getObject()
				   );
		if(result==true){
			this.executor.action(Login.actionPrivateOfficeFirstEnterLoginPassword, 
								 new String[]{this.modelLogin.getObject(),this.modelPassword.getObject(),this.modelCardNumber.getObject(),this.modelNick.getObject()});
		}else{
			// exception during check all values
			//this.formMain.error(result);
		}
	}
	
	/** проверить логин и пароль */
	private boolean isValidData(String login, 
								String password,
								String passwordAgain,
								String numberCard,
								String nick,
								Boolean rule){
		boolean returnValue=true;
		ConnectUtility connector=this.getConnectUtility();
		try{
			if(rule==false){
				this.checkRule.error(this.getString("rule_error"));
				returnValue=false;
			}
			if(!password.equals(passwordAgain)){
				this.editPasswordAgain.error(this.getString("password_again_error"));
				returnValue=false;
			}
			if(isLoginRepeat(login,connector.getSession())){
				this.editLogin.error(this.getString("form_login_error"));
				returnValue=false;
			}
			if(isCardNumberExists(numberCard,connector.getSession())==false){
				this.editCardNumber.error(this.getString("form_bon_card_error"));
				returnValue=false;
			}
			if(isNickUniquer(nick,connector.getSession())==false){
				this.editNick.error(this.getString("nick_error"));
				returnValue=false;
			}
		}catch(Exception ex){
			returnValue=false;
		}
		return returnValue;
	}
	
	/** проверка Nick на уникальность */
	private boolean isNickUniquer(String nick, Session session) {
		if(session.createCriteria(Users.class).add(Restrictions.eq("nick", nick)).list().size()>0){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * @param login
	 * @param session
	 * @return
	 */
	private boolean isLoginRepeat(String login,Session session){
		boolean returnValue=false;
		try{
			if(session.createCriteria(Users.class).add(Restrictions.eq("login",login)).list().size()>0){
				// проверить, логин принадлежит данному пользователю ?
				if(session.createCriteria(Users.class).add(Restrictions.eq("login",login)).add(Restrictions.eq("kod", ((OfficePrivateSession)this.getSession()).getCustomerId())).list().size()>0){
					// да принадлежит
					returnValue=false;
				}else{
					// нет, логин принадлежит другому пользователю
					returnValue=true;
				}
			}else{
				returnValue=false;
			}
		}catch(Exception ex){
			System.err.println("CreateCustomer#isLoginRepeat Exception: "+ex.getMessage());
		}
		return returnValue;
	}

	private boolean isCardNumberExists(String cardNumber,Session session){
		boolean returnValue=false;
		try{
			if(session.createCriteria(Users.class)
					  .add(Restrictions.eq("boncardNumber",cardNumber))
					  .add(Restrictions.eq("kod", ((OfficePrivateSession)this.getSession()).getCustomerId() ))
					  .list().size()>0){
				returnValue=true;
			}else{
				returnValue=false;
			}
		}catch(Exception ex){
			System.err.println("CreateCustomer#isLoginRepeat Exception: "+ex.getMessage());
		}
		return returnValue;
	}
	
	
	private ConnectUtility getConnectUtility(){
		Connector connector=((OfficePrivateApplication)this.getApplication()).getHibernateConnection();
		return new ConnectUtility(connector);
	}
	
}
