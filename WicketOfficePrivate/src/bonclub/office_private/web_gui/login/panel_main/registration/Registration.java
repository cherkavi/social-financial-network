package bonclub.office_private.web_gui.login.panel_main.registration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Random;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;
import bonclub.office_private.database.Connector;
import bonclub.office_private.database.wrap.Users;
import bonclub.office_private.database.wrap.UsersParent;
import bonclub.office_private.session.OfficePrivateSession;
import bonclub.office_private.web_gui.login.Login;
import wicket_utility.ActionExecutor;
import wicket_utility.PatternValidator;

public class Registration extends Panel{
	private final static long serialVersionUID=1L;
	private ActionExecutor executor;
	private TextField<String> editBonCardNumber;
	private DateTextField editBirthDay;
	private TextField<String> editMail;
	private TextField<String> editPhoneNumber;
	private TextField<String> editControlValue;
	private CaptchaImageResource captchaImageResource;
	private final Model<String> codeModel=new Model<String>();
	private Form<Object> formMain;
	
	public Registration(String id, ActionExecutor executor){
		super(id);
		this.executor=executor;
		codeModel.setObject(this.getRandomString(5));
		this.captchaImageResource=new CaptchaImageResource(codeModel);
		initComponents();
	}
	
	private void updateCaptcha(){
		codeModel.setObject(this.getRandomString(5));
		this.captchaImageResource=new CaptchaImageResource(codeModel);
		//System.out.println("New code: "+codeModel.getObject());
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		formMain=new Form<Object>("form_main");
		
		this.editBonCardNumber=new TextField<String>("edit_bon_card_number",new Model<String>());
		this.editBonCardNumber.setRequired(true);
		formMain.add(this.editBonCardNumber);
		formMain.add(new ComponentFeedbackPanel("error_bon_card_number",this.editBonCardNumber));
			
		this.editBirthDay=new DateTextField("edit_birth_day",new Model<Date>(new Date()),new PatternDateConverter("dd.MM.yyyy",true));
		this.editBirthDay.setRequired(true);
		this.editBirthDay.add(new DatePicker());
		formMain.add(this.editBirthDay);
		formMain.add(new ComponentFeedbackPanel("error_birth_day",this.editBirthDay));
		
		this.editMail=new TextField<String>("edit_mail",new Model<String>());
		this.editMail.setRequired(true);
		this.editMail.add(this.getEMailValidator("error_email"));
		formMain.add(this.editMail);
		formMain.add(new ComponentFeedbackPanel("error_mail", this.editMail));
		
		this.editPhoneNumber=new TextField<String>("edit_mobile_phone",new Model<String>());
		this.editPhoneNumber.setRequired(true);
		this.editPhoneNumber.add(this.getPhoneNumberValidator("error_mobile_phone"));
		formMain.add(this.editPhoneNumber);
		formMain.add(new ComponentFeedbackPanel("error_mobile_phone",this.editPhoneNumber));
		
		this.editControlValue=new TextField<String>("edit_control_value",new Model<String>());
		this.editControlValue.setRequired(true);
		formMain.add(this.editControlValue);
		formMain.add(new ComponentFeedbackPanel("error_control_value",this.editControlValue));

		final WebMarkupContainer controlImageWrap=new WebMarkupContainer("wrap_control_image");
		controlImageWrap.setOutputMarkupId(true);
		controlImageWrap.add(new Image("control_image",this.captchaImageResource));
		//img
		formMain.add(controlImageWrap);
		// refreshButton
		Button buttonControlImageRefresh=new Button("button_control_image_refresh");
		buttonControlImageRefresh.add(new SimpleAttributeModifier("value",this.getString("button_control_image_refresh")));
		buttonControlImageRefresh.add(new AjaxEventBehavior("onclick"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				//System.out.println("need update");
				updateCaptcha();
				controlImageWrap.removeAll();
				controlImageWrap.add(new Image("control_image",captchaImageResource));
				target.addComponent(controlImageWrap);
			}
		});
/*		AjaxButton buttonControlImageRefresh=new AjaxButton(){
			private final static long serialVersionUID=1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				
			}
		};
*/		
		formMain.add(buttonControlImageRefresh);
		
		Button buttonRegister=new Button("button_register"){
			private static final long serialVersionUID=1L;
			public void onSubmit() {
				onButtonRegistration();
			};
		};
		buttonRegister.add(new SimpleAttributeModifier("value",this.getString("button_register_caption")));
		formMain.add(buttonRegister);
		
		formMain.add(new ComponentFeedbackPanel("form_main_error",formMain));
		this.add(formMain);
	}
	
	/**  
	 * @param size - размер 
	 * @return случайный набор символов в HEX строке из указанного кол-ва символов
	 * */
	private String getRandomString(int size){
		Random random=new Random((new Date()).getTime());
		StringBuffer returnValue=new StringBuffer();
		for(int counter=0;counter<size;counter++){
			int value=random.nextInt();
			value=Math.abs(value%10);
			returnValue.append((char)(value+48));
		}
		return returnValue.toString();
	}
	
	
	/** get Validator for input E-Mail value */
	private IValidator<String> getEMailValidator(String resourceString){
		return new PatternValidator("[A-Za-z]+[A-Za-z0-9]*@[A-Za-z0-9]*.[A-Za-z]{1,3}",
									resourceString);
	}
	
	/** get Validator for input PhoneNumber value */
	private IValidator<String> getPhoneNumberValidator(String resourceString){
		return new PatternValidator("[0-9]{12}",resourceString){
			private final static long serialVersionUID=1L;
			@Override
			public String prepareValue(String value) {
				return value.replaceAll("[+ - () _]", "");
			}
		};
	}
	
	
	/** является ли введенный в поле контрольный текст эквивалентным картинке */
	private boolean isImageTextValid(){
		return this.codeModel.getObject().equals(this.editControlValue.getModelObject());
	}
	
	/** проверка номера БонКарты на существование в основной базе данных
	 * @param boncardNumber номер бон карты  
	 * @return полную ФИО 
	 * */
	private String isBonCardNumberExists(String boncardNumber){
		String returnValue=null;
		Connection connection=null;
		try{
			connection=((OfficePrivateApplication)this.getApplication()).getDatabaseParentConnection();
			String query="select vc_nat.full_name  from bc_admin.vc_club_card_club_all vc_club inner join bc_admin.vc_nat_prs_club_all vc_nat on vc_club.id_nat_prs=vc_nat.id_nat_prs where vc_club.cd_card1='"+boncardNumber+"'";
			ResultSet rs=connection.createStatement().executeQuery(query);
			if(rs.next()){
				returnValue=rs.getString(1);
			}
		}catch(Exception ex){
			System.out.println("Registration#isBonCardNumberExists Exception: "+ex.getMessage());
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	
	private void onButtonRegistration(){
		if(isImageTextValid()){
			// все валидаторы пройдены - отправка родительской странице
			String bonCard=this.editBonCardNumber.getModelObject();
			Date birthDay=this.editBirthDay.getModelObject();
			String email=this.editMail.getModelObject();
			String phoneNumber=this.editPhoneNumber.getModelObject();
			// проверить Номер "BonCard" на наличие в системе и не наличие по нему последней даты входа 
			ConnectUtility connector=this.getConnectUtility();
			try{
				Users customer=null;
				Session session=connector.getSession();
				String fullName=null;
				if((fullName=this.isBonCardNumberExists(bonCard))==null){
					throw new Exception("BonCard is not found");
				}else{
					// проверка на существование пользователя в локальной базе данных, иначе - создание этого пользователя
					try{
						customer=(Users)session.createCriteria(Users.class).add(Restrictions.eq("boncardNumber", bonCard)).list().get(0);
					}catch(Exception ex){
						// такой пользователь не существует - создать его
						// INFO место "перетягивания" пользователя из основной базы данных
						customer=new Users();
						customer.setBoncardNumber(bonCard);
						customer.setCreateTime(new Date());
						customer.setLastEnter(null);
						customer.setRegistrationDate(new Date());
						customer.setFullName(fullName);
						session.beginTransaction();
						session.save(customer);
						session.getTransaction().commit();
					}
				}
				if(customer.getLastEnter()==null){
					// INFO попытка регистрации пользователя - вход в систему
					if(this.tryRegister(bonCard, birthDay, email, phoneNumber)){
						executor.action(Login.actionShowFirstEnter, new String[]{bonCard});
					}else{
						// INFO не удалось зарегестрировать пользователя
						formMain.error(this.getString("user_register_error"));
					}
					
				}else{
					// INFO Registration данный пользователь уже зарегестрирован и не может войти только по номеру карты
					formMain.error(this.getString("user_exists_error"));
				}
				
			}catch(Exception ex){
				System.err.println("Registration Exception: "+ex.getMessage());
				formMain.error(this.getString("card_was_not_found"));
			}finally{
				connector.close();
			}
		}else{
			// есть неточности
			this.editControlValue.error(this.getString("error_control_value"));
		}
	}
	
	private ConnectUtility getConnectUtility(){
		Connector connector=((OfficePrivateApplication)this.getApplication()).getHibernateConnection();
		return new ConnectUtility(connector);
	}

	/**
	 * попытка зарегестрировать нового пользователя в системе на основании данных, полученных из формы  
	 * @return
	 */
	private boolean tryRegister(String bonCard, Date birthDay, String email, String phoneNumber){
		boolean returnValue=false;
		// INFO "регистрация нового пользователя" проверить данного пользователя на наличие в системе по номеру карты, при чем логин и пароль должны отсутствовать
		ConnectUtility connector=this.getConnectUtility();
		Connection connection=null;
		try{
			Session session=connector.getSession();
			Users customer=(Users)session.createCriteria(Users.class).add(Restrictions.eq("boncardNumber", bonCard)).list().get(0);
			if((birthDay!=null)&&(email!=null)&&(phoneNumber!=null)&&(!email.equals(""))&&(!phoneNumber.equals(""))){
				// попытка сохранить новые данные
				connection=((OfficePrivateApplication)this.getApplication()).getDatabaseParentConnection();
				UsersParent user=this.getUsersParent(connection,bonCard);
				//customer.setBirthday(birthDay);
				user.setEmail(email);
				user.setPhoneMobile(phoneNumber);
				String result=user.updateObjectInDatabase(connection);
				if(result!=null){
					throw new Exception("updateObjectInDatabase Exception: "+result);
				}
			}
			// получить номер Customer.KOD и положить его в сессию
			((OfficePrivateSession)this.getSession()).setCustomerId(customer.getKod());
			returnValue=true;
		}catch(Exception ex){
			System.err.println("Login#tryRegister Exception:"+ex.getMessage());
		}finally{
			connector.close();
			try{
				connection.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	
	
	
	/** получить данные пользователя на основании номера карты 
	 * @param connection - соединение с базой данных 
	 * @param bonCardNumber - номер бон-карты 
	 * @return объект с пользователем 
	 */
	private UsersParent getUsersParent(Connection connection,String bonCardNumber){
		UsersParent returnValue=new UsersParent();
		try{
			returnValue.fillData(connection, bonCardNumber);
		}catch(Exception ex){
			System.err.println("profile_edit.Address error get object UsersParent: "+ex.getMessage());
		}
		return returnValue;
	}
}
