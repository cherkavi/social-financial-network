package bonpay.partner.web_gui.register_information;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import bonpay.partner.PartnerApplication;
import bonpay.partner.database.wrap.Partner;
import bonpay.partner.session.PartnerSession;
import bonpay.partner.web_gui.common.RedirectAction;

public class PanelRegistration extends Panel{
	private void Debug(Object information){
		System.out.print("PanelRegistration ");
		System.out.print("DEBUG ");
		System.out.println(information);
	}
	@SuppressWarnings("unused")
	private void Error(Object information){
		System.err.print("PanelRegistration ");
		System.err.print("ERROR ");
		System.err.println(information);
	}
	
	private final static long serialVersionUID=1L;
	private Model modelSurname=new Model();
	private Model modelName=new Model();
	private Model modelEmail=new Model();
	private Model modelPassword=new Model();
	private Model modelPasswordRepeat=new Model();
	private Model modelPhone=new Model();
	private Model modelIpLock=new Model();
	private Model modelIpLockCheckbox=new Model(new Boolean(false));
	
	private Model modelSurnameError=new Model();
	private Model modelNameError=new Model();
	private Model modelEmailError=new Model();
	private Model modelPasswordError=new Model();
	private Model modelPasswordRepeatError=new Model();
	private Model modelPhoneError=new Model();
	private Model modelIpLockError=new Model();
	
	private Partner partner=null;
	/** объкт, который содержит варианты для переходов на другие страницы, согласно текстовым значениям */
	private RedirectAction action;
	/** флаг, который говорит о редактировании профиля, а не ввода новых данных */
	private boolean isEditable=false;
	
	/** панель для отображения критериев ввода информации  - создание нового и редактирование старого 
	 * @param action объект, содержащий точки перехода на другие страницы
	 *  <table border=1>
	 *  	<tr> <td> save</td><td>успешно сохранены все данные</td></tr>
	 *  	<tr> <td> cancel</td><td>отмена редактирования данных</td></tr>
	 *  	<tr> <td> error</td><td>ошибка сохранения данных</td></tr>
	 *  </table>
	 * @param id - уникальный идентификатор данной панели   
	 * */
	public PanelRegistration(RedirectAction action,String id){
		super(id);
		this.action=action;
		initComponents();
	}
	
	public PanelRegistration(RedirectAction action, String id, Partner partner){
		super(id);
		this.isEditable=true;
		this.action=action;
		this.partner=partner;
		this.modelSurname.setObject(partner.getSurname());
		this.modelName.setObject(partner.getName());
		this.modelEmail.setObject(partner.getEmail());
		this.modelPassword.setObject(partner.getPassword());
		this.modelPhone.setObject(partner.getPhone());
		this.modelIpLock.setObject(partner.getIpLock());
		if(partner.getIpLock()!=null){
			this.modelIpLockCheckbox.setObject(new Boolean(true));
			this.modelIpLock.setObject(partner.getIpLock());
		}else{
			this.modelIpLockCheckbox.setObject(new Boolean(false));
		}
		
		initComponents();
	}

	
	private void initComponents(){
		Form formMain=new Form("form_main"){
			private final static long serialVersionUID=1L;
			public void onError(){
				onFormError();
			}
		};
		CheckBox checkboxIpLock=new CheckBox("checkbox_ip_lock",modelIpLockCheckbox);
		
		TextField surname=new TextField("surname",this.modelSurname);
		surname.setRequired(false);
		TextField name=new TextField("name",this.modelName);
		name.setRequired(false);
		TextField email=new TextField("email",this.modelEmail);
		email.setRequired(false);
		PasswordTextField password=new PasswordTextField("password",this.modelPassword);
		password.setRequired(false);
		PasswordTextField passwordRepeat=new PasswordTextField("password_repeat",this.modelPasswordRepeat);
		passwordRepeat.setRequired(false);
		TextField phone=new TextField("phone",this.modelPhone);
		phone.setRequired(false);
		TextField ipLock=new TextField("ip_lock",this.modelIpLock);
		ipLock.setRequired(false);
		
		Label labelSurnameError=new Label("surname_error",this.modelSurnameError);
		Label labelNameError=new Label("name_error",this.modelNameError);
		Label labelEmailError=new Label("email_error",this.modelEmailError);
		Label labelPasswordError=new Label("password_error",this.modelPasswordError);
		Label labelPasswordRepeatError=new Label("password_repeat_error",this.modelPasswordRepeatError);
		Label labelPhoneError=new Label("phone_error",this.modelPhoneError);
		Label labelIpLockError=new Label("ip_lock_error",this.modelIpLockError);
		
		Button buttonSave=new Button("save"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonSave();
			}
		};
		Button buttonCancel=new Button("cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonCancel();
			}
		};
		formMain.add(checkboxIpLock);
		
		formMain.add(surname);
		formMain.add(name);
		formMain.add(email);
		formMain.add(password);
		formMain.add(passwordRepeat);
		formMain.add(phone);
		formMain.add(ipLock);

		formMain.add(labelSurnameError);
		formMain.add(labelNameError);
		formMain.add(labelEmailError);
		formMain.add(labelPasswordError);
		formMain.add(labelPasswordRepeatError);
		formMain.add(labelPhoneError);
		formMain.add(labelIpLockError);
		
		formMain.add(buttonSave);
		formMain.add(buttonCancel);
		this.add(formMain);
	}
	
	private void onFormError(){
		// error on form 
		Debug("onFormError: ");
	}
	
	private void onButtonSave(){
		// try to save this partner
		Debug("onButtonSave:");
		if(this.checkAllVariable()==true){
			// попытка сохранения партнера 
			if(this.partner!=null){
				// UPDATE
				// this.partner must be not empty
			}else{
				// SAVE NEW 
				this.partner=new Partner();
			}
			this.partner.setSurname((String)this.getStringFromModel(this.modelSurname));
			this.partner.setName((String)this.getStringFromModel(this.modelName));
			this.partner.setEmail((String)this.getStringFromModel(this.modelEmail));
			this.partner.setPassword((String)this.getStringFromModel(this.modelPassword));
			this.partner.setPhone((String)this.getStringFromModel(this.modelPhone));
			if(this.getBooleanFromModel(modelIpLockCheckbox)){
				this.partner.setIpLock((String)this.getStringFromModel(this.modelIpLock));
			}else{
				this.partner.setIpLock(null);
			}
			// попытка сохранения данных
			if(this.savePartner(this.partner)==true){
				// положить номер PartnerId в сессию 
				((PartnerSession)this.getSession()).setPartnerId(this.partner.getId());
				this.action.action(this, "save",null,null,null);
			}else{
				Debug("onButtonSave: Error");
				this.action.action(this, "error",null,null,null);
			}
		}else{
			//this.setResponsePage(this);
			Debug("check all variable is not true");
		}
	}
	
	/** сохранить партнера и получить от него какое-либо значение 
	 * @param
	 * @param
	 * @return 
	 *   <li> <b>true</b> - значение положительно сохранено </li> 
	 * 	 <li> <b>false</b> - ошибка сохранения значения </li>
	 * */
	private boolean savePartner(Partner partner){
		boolean returnValue=false;
		Session session=null;
		try{
			// getSession
			session=((PartnerApplication)this.getApplication()).getHibernateConnection().openSession();
			// saveValue
			session.beginTransaction();
			session.saveOrUpdate(partner);
			session.getTransaction().commit();
			returnValue=true;
		}catch(Exception ex){
			System.err.println("PanelRegistration#savePartner Exception:"+ex.getMessage());
			try{
				session.getTransaction().rollback();
			}catch(Exception exInner){};
		}finally{
			try{
				session.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	private void onButtonCancel(){
		Debug("onButtonCancel:");
		this.action.action(this, "cancel",null,null,null);
	}
	
	/** проверка всех введенных значений 
	 * @return true - все параметры валидны для сохранения <br>
	 *  false - какие-либо данные введены неправильно, доп. информация в моделях xxxError
	 * */
	private boolean checkAllVariable(){
		boolean returnValue=true;
		this.modelSurnameError.setObject(null);
		this.modelNameError.setObject(null);
		this.modelEmailError.setObject(null);
		this.modelPasswordError.setObject(null);
		this.modelPasswordRepeatError.setObject(null);
		this.modelPhoneError.setObject(null);
		this.modelIpLockError.setObject(null);
		
		// check Surname
		if(isEmptyModel(this.modelSurname)==true){
			returnValue=false;
			this.modelSurnameError.setObject(getString("surname_error"));
		}
		// check Name
		if(isEmptyModel(this.modelName)==true){
			returnValue=false;
			this.modelNameError.setObject(getString("name_error"));
		}
		// check E-mail
		if(isEmptyModel(this.modelEmail)==true){
			returnValue=false;
			this.modelEmailError.setObject(getString("e-mail_error"));
		}
		// check Mail
		// FIXME check E-mail
		
		// check Password
		if((isEditable==false)&&(isEmptyModel(this.modelPassword)==true)){
			returnValue=false;
			this.modelPasswordError.setObject(getString("password_error"));
		}
		// check Password repeat
		if((isEditable==false)&&(isEmptyModel(this.modelPasswordRepeat)==true)){
			returnValue=false;
			this.modelPasswordRepeatError.setObject(getString("password_repeat_error"));
		}
		if(  (this.getStringFromModel(this.modelPassword)!=null)
		   &&(this.getStringFromModel(this.modelPasswordRepeat)!=null)){
			if(!this.getStringFromModel(this.modelPassword).equals(this.getStringFromModel(this.modelPasswordRepeat))){
				returnValue=false;
				this.modelPasswordRepeatError.setObject(getString("password_not_equals"));
			}
		}
		// check Phone
		if(isEmptyModel(this.modelPhone)==true){
			returnValue=false;
			this.modelPhoneError.setObject(getString("phone_error"));
		}
		// FIXME check Phone
		
		// ipLock
		if(this.modelIpLockCheckbox.getObject() instanceof Boolean){
			if(((Boolean)this.modelIpLockCheckbox.getObject()).booleanValue()==true){
				if(isEmptyModel(this.modelIpLock)==true){
					returnValue=false;
					this.modelIpLockError.setObject(getString("ip_lock_error"));
				}
				// FIXME check IP  
			}else{
				// не нуждается в проверке 
			}
		}else{
			error("#checkAllVariable modelIpLockCheckbox is not Boolean ");
		}
		return returnValue;
	}
	
	/** проверка модели на пустое значение, то есть модель не содержит значения, либо содержит пустую строку*/
	private boolean isEmptyModel(IModel model){
		boolean returnValue=true;
		if(model.getObject()==null){
			returnValue=true;
		}else{
			// Model.getObject is not empty - check for empty String
			if(model.getObject() instanceof String){
				// model is String
				if( ((String)model.getObject()).trim().equals("")){
					returnValue=true;
				}else{
					returnValue=false;
				}
			}else{
				/// model is not String
				returnValue=true;
			}
		}
		return returnValue;
	}
	
	/* получить Partner из визуальных компонентов 
	 * ( учитывая возможный вариант использования редактирования this.partner )
	private Partner getPartnerFromVisualComponents(){
		Partner returnValue=null;
		if(this.partner==null){
			// insert
			returnValue=new Partner();
			returnValue.setSurname(this.getStringFromModel(modelSurname));
			returnValue.setName(this.getStringFromModel(modelName));
			returnValue.setEmail(this.getStringFromModel(modelEmail));
			returnValue.setPassword(this.getStringFromModel(modelPassword));
			returnValue.setPhone(this.getStringFromModel(modelPhone));
			if(this.getBooleanFromModel(modelIpLockCheckbox)){
				returnValue.setIpLock(this.getStringFromModel(modelIpLock));
			}else{
				returnValue.setIpLock(null);
			}
		}else{
			// edit
		}
		return returnValue;
	}
*/
	
	/** получить из модели значение True или False - если объект модели является null - возвращаем False*/
	private boolean getBooleanFromModel(IModel model){
		boolean returnValue=false;
		if(model!=null){
			try{
				returnValue=((Boolean)model.getObject()).booleanValue();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	/** получить строку из модели */
	private String getStringFromModel(IModel model){
		String returnValue=null;
		try{
			returnValue=(String)model.getObject();
		}catch(Exception ex){
			// объект модели не является String либо же является null
		}
		return returnValue;
	}
	
}
