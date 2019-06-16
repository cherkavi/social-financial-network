package bonclub.office_private.web_gui.login.panel_main.forget_password;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;

import bonclub.office_private.web_gui.login.Login;

import wicket_utility.ActionExecutor;
import wicket_utility.PatternValidator;

/** Панель, которая отображает интерфейс возможного восстановления пароля пользователя*/
public class ForgetPassword extends Panel{
	private final static long serialVersionUID=1L;
	private ActionExecutor executor;
	private TextField<String> editAnswer;
	private TextField<String> editMail;
	private TextField<String> editPhoneNumber;
	
	public ForgetPassword(String id, ActionExecutor executor){
		super(id);
		this.executor=executor;
		initComponents();
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		Form<Object> formMain=new Form<Object>("form_main");
		this.editAnswer=new TextField<String>("edit_answer",new Model<String>(""));
		this.editAnswer.setRequired(true);
		formMain.add(this.editAnswer);
		formMain.add(new ComponentFeedbackPanel("error_answer",this.editAnswer));
		
		this.editMail=new TextField<String>("edit_mail",new Model<String>(""));
		this.editMail.setRequired(true);
		this.editMail.add(this.getEMailValidator());
		formMain.add(this.editMail);
		formMain.add(new ComponentFeedbackPanel("error_mail",this.editMail));
		
		this.editPhoneNumber=new TextField<String>("edit_phone_number",new Model<String>(""));
		this.editPhoneNumber.setRequired(true);
		this.editPhoneNumber.add(this.getPhoneNumberValidator());
		formMain.add(this.editPhoneNumber);
		formMain.add(new ComponentFeedbackPanel("error_phone_number",this.editPhoneNumber));
		
		Button buttonSend=new Button("button_send"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				onButtonSend();
			}
		};
		buttonSend.add(new SimpleAttributeModifier("value",this.getString("button_send_caption")));
		
		formMain.add(buttonSend);
		this.add(formMain);
	}
	
	/** get Validator for input E-Mail value */
	private IValidator<String> getEMailValidator(){
		return new PatternValidator("[A-Za-z]+[A-Za-z0-9]*@[A-Za-z0-9]*.[A-Za-z]{1,3}","error_mail");
	}
	
	/** get Validator for input PhoneNumber value */
	private IValidator<String> getPhoneNumberValidator(){
		return new PatternValidator("[0-9]{12}","error_phone_number"){
			private final static long serialVersionUID=1L;
			@Override
			public String prepareValue(String value) {
				return value.replaceAll("[+ - () _]", "");
			}
		};
	}
	
	/** была нажата клавиша "Send" и "пройдены" все валидаторы */
	private void onButtonSend(){
		String answer=this.editAnswer.getModelObject();
		String email=this.editMail.getModelObject();
		String phoneNumber=this.editPhoneNumber.getModelObject().replaceAll("[()+ \\[\\]\\.]", "");
		this.executor.action(Login.actionPrivateOfficeForgetPassword, new String[]{answer, email, phoneNumber});
	}
	
}
