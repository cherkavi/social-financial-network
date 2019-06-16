package bonclub.office_private.web_gui.login;

import java.util.Date;

import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import wicket_utility.ActionExecutor;
import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;
import bonclub.office_private.database.Connector;
import bonclub.office_private.database.wrap.Parameters;
import bonclub.office_private.database.wrap.Users;
import bonclub.office_private.session.OfficePrivateSession;
import bonclub.office_private.web_gui.BasePage;
import bonclub.office_private.web_gui.login.panel_footer.PanelFooter;
import bonclub.office_private.web_gui.login.panel_header.PanelHeader;
import bonclub.office_private.web_gui.login.panel_main.about.About;
import bonclub.office_private.web_gui.login.panel_main.forget_password.ForgetPassword;
import bonclub.office_private.web_gui.login.panel_main.private_office_enter.PanelMainPrivateOfficeEnter;
import bonclub.office_private.web_gui.login.panel_main.private_office_first_enter.PanelMainPrivateOfficeFirstEnter;
import bonclub.office_private.web_gui.login.panel_main.registration.Registration;
import bonclub.office_private.web_gui.login.panel_navigator.PanelNavigator;
import bonclub.office_private.web_gui.user_area.UserArea;

/** страница, котора€ содержит всю необходимую информацию по входу клиента на ресурс */
public class Login extends BasePage implements ActionExecutor{
	protected final static String idPanelHeader="panel_header";
	protected final static String idPanelMain="panel_main";
	protected final static String idPanelFooter="panel_footer";
	protected final static String idPanelNavigator="panel_navigator";
	
	private PanelHeader panelHeader;
	private Panel panelMain;
	private PanelFooter panelFooter;
	private PanelNavigator panelNavigator;

	/** страница, котора€ содержит всю необходимую информацию по входу клиента на ресурс */
	public Login(){
		Integer customerId=((OfficePrivateSession)this.getSession()).getCustomerId();
		if(customerId!=null){
			this.setResponsePage(UserArea.class);
		}else{
			initComponents();
		}
	}
	
	/** первоначальна€ инициализаци€ компонентов */
	private void initComponents(){
		this.add(this.getPanelHeader());
		this.add(this.getPanelNavigator());
		this.add(this.getPanelMain());
		this.add(this.getPanelFooter());
	}
	
	private Panel getPanelHeader(){
		if(panelHeader==null){
			panelHeader=new PanelHeader(idPanelHeader,this);
		};
		return panelHeader;
	}

	private Panel getPanelNavigator(){
		if(panelNavigator==null){
			this.panelNavigator=new PanelNavigator(idPanelNavigator,
					   this,
					   "login_main_panel_navigator_mark",
					   "login_main_panel_navigator_unmark"
					   );
		};
		return panelNavigator;
	}
	
	private Panel getPanelMain(){
		if(panelMain==null){
			panelMain=new PanelMainPrivateOfficeEnter(idPanelMain,this);
			this.panelNavigator.setSelected(0);
			//panelMain=new ForgetPassword(idPanelMain,this);
			//panelMain=new Registration(idPanelMain,this);
			//panelMain=new About(idPanelMain,this);
		}
		return panelMain;
	}
	private Panel getPanelFooter(){
		if(panelFooter==null){
			panelFooter=new PanelFooter(idPanelFooter,this);
		}
		return panelFooter;
	}
	
	public final static String actionBonKarta="bon_karta";
	public final static String actionNakopilka="nakopilka";
	public final static String actionPrivateOffice="private_office";
	
	public final static String actionPrivateOfficeEnter="public_office_enter";
	public final static String actionForgetPassword="forget_password";
	public final static String actionRegistration="registration";
	public final static String actionAbout="about";

	public final static String actionRegisterNewUser="register_new_user";
	public final static String actionPrivateOfficeForgetPassword="private_office_forget_password";
	public final static String actionPrivateOfficeFirstEnterLoginPassword="private_office_first_enter_login_password";
	public final static String actionPrivateOfficeEnterLoginPassword="private_office_enter_login_password";
	public final static String actionShowFirstEnter="private_office_show_first_enter";
	
	private String getParameterFromDatabase(String parameter){
		String returnValue="";
		ConnectUtility connectUtility=((OfficePrivateApplication)this.getApplication()).getConnectUtility();
		Session session=connectUtility.getSession();
		try{
			returnValue=((Parameters)(session.createCriteria(Parameters.class).add(Restrictions.eq("name", parameter))).list().get(0)).getValue();
		}catch(Exception ex){
			System.out.println("Login#getParameterFromDatabase Exception: "+ex.getMessage());
		}
		connectUtility.close();
		return returnValue;
	}
	
	
	@Override
	public void action(String actionName, Object argument) {
		while(true){
			// получение действий от дочерних панелей
			if(actionName.equals(Login.actionBonKarta)){
				setResponsePage(new RedirectPage(getParameterFromDatabase("URL_BON_CARD")));
				//System.out.println("√лобальный переход на Ѕон-карту");
				break;
			}
			if(actionName.equals(Login.actionNakopilka)){
				//System.out.println("√лобальный переход на Ќакопилку");
				setResponsePage(new RedirectPage(getParameterFromDatabase("URL_BON_CARD_NAKOPILKA")));
				break;
			}
			if(actionName.equals(Login.actionPrivateOffice)){
				System.out.println("√лобальный переход в Ћичный кабинет");
				break;
			}
			//------------------
			if(actionName.equals(Login.actionPrivateOfficeEnter)){
				this.panelNavigator.setSelected(0);
				System.out.println("вход в личный кабинет");
				this.remove(idPanelMain);
				this.add(new PanelMainPrivateOfficeEnter(idPanelMain,this));
				break;
			}
			if(actionName.equals(Login.actionForgetPassword)){
				this.panelNavigator.setSelected(1);
				this.remove(idPanelMain);
				System.out.println("«абыл пароль");
				this.add(new ForgetPassword(idPanelMain,this));
				break;
			}
			
			if(actionName.equals(Login.actionRegistration)){
				this.panelNavigator.setSelected(2);
				this.remove(idPanelMain);
				this.add(new Registration(idPanelMain,this));
				break;
			};
			if(actionName.equals(Login.actionAbout)){
				this.panelNavigator.setSelected(3);
				this.remove(idPanelMain);
				System.out.println("ќ —истеме");
				this.add(new About(idPanelMain,this));
				break;
			}
			
			if(actionName.equals(actionShowFirstEnter)){
				this.remove(idPanelMain);
				this.panelMain=new PanelMainPrivateOfficeFirstEnter(idPanelMain,this);
				this.add(this.panelMain);
			}
			
			// ---------------------------
			
			//this.executor.action(, new String[]{this.modelLogin.getObject(), this.modelPassword.getObject()});
			if(actionName.equals(Login.actionPrivateOfficeEnterLoginPassword)){
				String[] arguments=(String[])argument;
				String login=arguments[0];
				String password=arguments[1];
				//System.out.println("Login: "+login+"\nPassword: "+password);
				org.apache.wicket.Session webSession=this.getSession();
				System.out.println("Session: "+webSession.getClass().getName().toString());
				((OfficePrivateSession)webSession).setCustomerId(this.getCustomerIdByLoginAndPassword(login, password));
				if(this.isFirstEnter(login, password)){
					// is first enter to resource
					this.remove(idPanelMain);
					this.panelMain=new PanelMainPrivateOfficeFirstEnter(idPanelMain,this);
					this.add(this.panelMain);
				}else{
					// client have stable login and password
					this.setResponsePage(UserArea.class);
				}
				break;
			}
			
			if(actionName.equals(Login.actionPrivateOfficeFirstEnterLoginPassword)){
				//System.out.println("private_office_first_enter_login_password");
				// был первый вход в систему и пользователь оформил необходимые данные дл€ продолжени€ - заменить логин и пароль (его номер в сессии)
				String[] arguments=(String[])argument;
				String login=arguments[0];
				String password=arguments[1];
				String cardNumber=arguments[2];
				String nick=arguments[3];
				if(this.changeLoginAndPassword(login, password, cardNumber,nick)==true){
					this.setResponsePage(UserArea.class);
				}else{
					// INFO ошибка во врем€ попытки изменени€ логина и/или парол€ после первого входа пользовател€  
				}
				break;
			}
			// реакци€ на забытие парол€ - восстановление парол€ по указанным пол€м 
			if(actionName.equals(Login.actionPrivateOfficeForgetPassword)){
				String[] arguments=(String[])argument;
				String answer=arguments[0];
				String email=arguments[1];
				String phoneNumber=arguments[2];
				System.out.println("answer:"+answer+"     email:"+email+"     phoneNumber:"+phoneNumber);
				if(this.isForgetDataIsValid(answer, email, phoneNumber)){
					this.setResponsePage(UserArea.class);
				}else{
					// INFO client don't have answer for secret questions 
				}
				break;
			};
			if(actionName.equals(Login.actionRegisterNewUser)){
				Object[] arguments=(Object[])(argument);
				String bonCard=(String)arguments[0];
				Date birthDay=(Date)arguments[1];
				String email=(String)arguments[2];
				String phoneNumber=(String)arguments[3];
				System.out.println("Ќовый пользователь зарегестрирован: BonCard: "+bonCard+" BirthDay: "+birthDay+"   Email: "+email+"    Phone Number: "+phoneNumber+" ");
				this.setResponsePage(UserArea.class);
				break;
			};
			
			System.err.println("Login#action is not found: "+actionName);
			break;
		}
	}
	
	
	/** заменить логин и пароль у текущего пользовател€ 
	 * @param login - логин 
	 * @param password - пароль
	 * @param bonCardNumber - номер Ѕон-карты
	 * @return возвращает положительный результат, в случае успешного изменени€
	 * */
	private boolean changeLoginAndPassword(String login, String password, String bonCardNumber,String nick){
		boolean returnValue=false;
		// проверить по указанному пользователю номер Ѕон-карты  
		ConnectUtility connector=getConnectUtility();
		try{
			Session session=connector.getSession();
			Users customer=(Users)session.createCriteria(Users.class).add(Restrictions.eq("kod", ((OfficePrivateSession)this.getSession()).getCustomerId()))
												   .add(Restrictions.eq("boncardNumber",bonCardNumber)).list().get(0);
			session.beginTransaction();
			customer.setLogin(login);
			customer.setPassword(password);
			customer.setNick(nick);
			customer.setLastEnter(new Date());
			session.update(customer);
			session.getTransaction().commit();
			returnValue=true;
		}catch(Exception ex){
			System.err.println("Login#changeLoginAndPassword: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}
	
	private Integer getCustomerIdByLoginAndPassword(String login, String password){
		Integer returnValue=null;
		ConnectUtility connector=getConnectUtility();
		try{
			Users customer=(Users)connector.getSession().createCriteria(Users.class).add(Restrictions.eq("login", login)).add(Restrictions.eq("password",password)).list().get(0);
			returnValue=customer.getKod();
		}catch(Exception ex){
			System.err.println("Login#isFirstEnter user is not found: "+ex.getMessage());
		}
		connector.close();
		return returnValue;
	}
	
	/** обновить данные по пользователю, введ€ его */
	@SuppressWarnings("unused")
	private void updateCustomerLastEnter(Integer customerId){
		ConnectUtility connector=this.getConnectUtility();
		try{
			Session session=connector.getSession();
			Users customer=(Users)session.get(Users.class, customerId);
			customer.setLastEnter(new Date());
			session.beginTransaction();
			session.update(customer);
			session.getTransaction().commit();
		}catch(Exception ex){
			System.err.println("Login#updateCustomerLastEnter Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
	}
	
	
	/** проверка клиента на вход - первый ли раз вошел, (если имеет запись о последнем заходе на ресурс - значит не в первый раз) 
	 * или уже имеет посто€нный логин и пароль, 
	 * который сам же себе выбрал
	 */
	private boolean isFirstEnter(String login, String password){
		boolean returnValue=false;
		ConnectUtility connector=getConnectUtility();
		try{
			Users customer=(Users)connector.getSession().createCriteria(Users.class).add(Restrictions.eq("login", login)).add(Restrictions.eq("password",password)).list().get(0);
			returnValue=(customer.getLastEnter()==null);
		}catch(Exception ex){
			System.err.println("Login#isFirstEnter user is not found: "+ex.getMessage());
		}
		connector.close();
		return returnValue;
	}
	
	/**¬осстановление парол€ -  ѕроверка на валидность введенных данных  
	 * @param answer - текст ответа на вопрос
	 * @param email - адрес электронной почты  
	 * @param phoneNumber - номер мобильного телефона  
	 * @return возвращает true, если данные валидны
	 */
	private boolean isForgetDataIsValid(String answer, String email, String phoneNumber){
		boolean returnValue=false;
		ConnectUtility connector=getConnectUtility();
		try{
			Session session=connector.getSession();
			Users customer=(Users)session
												   .createCriteria(Users.class)
												   .add(Restrictions.eq("secretAnswer", answer))
												   .add(Restrictions.eq("email",email))
												   .add(Restrictions.eq("phoneMobile",phoneNumber))
												   .list().get(0);
			if(customer!=null){
				customer.setLastEnter(new Date());
				session.beginTransaction();
				session.update(customer);
				session.getTransaction().commit();
				((OfficePrivateSession)this.getSession()).setCustomerId(customer.getKod());
				returnValue=true;
			}else{
				returnValue=false;
			}
		}catch(Exception ex){
			System.err.println("Login#isForgetDataIsValid user is not found: "+ex.getMessage());
		}
		connector.close();
		return returnValue;
	}
	
	
	
	private ConnectUtility getConnectUtility(){
		Connector connector=((OfficePrivateApplication)this.getApplication()).getHibernateConnection();
		return new ConnectUtility(connector);
	}
}


