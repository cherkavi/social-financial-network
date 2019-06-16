package bonpay.partner.web_gui.access_panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import bonpay.partner.session.PartnerSession;
import bonpay.partner.web_gui.partner_admin.PartnerAdmin;
import bonpay.partner.web_gui.register_information.Registration;

public class AccessPanel extends Panel{
	private static final long serialVersionUID = 1L;

	public AccessPanel(String panelId){
		super(panelId);
		initComponents();
	}

	private Model modelLogin=new Model();
	private Model modelPassword=new Model();
	private Label labelLoginWarning;
	private Label labelPasswordWarning;
	
	/** инициализация компонентов */
	private void initComponents(){
		// create components
		Form formMain=new Form("form_main");
		TextField login=new TextField("login",modelLogin);
		login.setRequired(false);
		PasswordTextField password=new PasswordTextField("password",modelPassword);
		password.setRequired(false);
		labelLoginWarning=new Label("label_login_warning",new Model());
		labelPasswordWarning=new Label("label_password_warning",new Model());
		Button buttonEnter=new Button("enter"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonEnter();
			}
		};
		Button buttonRegistration=new Button("registration"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit(){
				onButtonRegistration();
			}
		};
		// placing components
		this.add(formMain);
		formMain.add(login);
		formMain.add(password);
		formMain.add(labelLoginWarning);
		formMain.add(labelPasswordWarning);
		formMain.add(buttonEnter);
		formMain.add(buttonRegistration);
	}
	
	/** reaction on strking button Enter */
	private void onButtonEnter(){
		//setResponsePage(cls);
		if((this.modelLogin.getObject()!=null)&&(this.modelPassword.getObject()!=null)){
			if(this.labelLoginWarning.getModel().getObject()!=null){
				this.labelLoginWarning.setModel(new Model());
			}
			if(this.labelPasswordWarning.getModel().getObject()!=null){
				this.labelPasswordWarning.setModel(new Model());
			}
			
			if(((PartnerSession)this.getSession()).setPartnerId((String)this.modelLogin.getObject(), (String)this.modelPassword.getObject())!=null){
				// user is recognized
				this.setResponsePage(PartnerAdmin.class);
			}else{
				// login or password is not recognized
			}
			System.out.println("onButtonEnter   Login:"+this.modelLogin.getObject()+"   Password:"+this.modelPassword.getObject());
		}else{
			if(this.modelLogin.getObject()==null){
				this.labelLoginWarning.setModel(new ResourceModel("label_login_warning"));
				System.out.println("change Login Warnings value:");
			}
			if(this.modelPassword.getObject()==null){
				this.labelPasswordWarning.setModel(new ResourceModel("label_password_warning"));
				System.out.println("change Password Warnings value");
			}
		}
	}
	/** reaction on striking button Registration */
	private void onButtonRegistration(){
		setResponsePage(new Registration(null));
	}
}
