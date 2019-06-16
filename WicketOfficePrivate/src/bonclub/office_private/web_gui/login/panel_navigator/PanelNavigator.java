package bonclub.office_private.web_gui.login.panel_navigator;

import org.apache.wicket.behavior.SimpleAttributeModifier;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import bonclub.office_private.web_gui.login.Login;

import wicket_utility.ActionExecutor;

/** панель, отображающая текущее действие пользователя (в каком именно меню он находится ) */
public class PanelNavigator extends Panel{
	private final static long serialVersionUID=1L;
	private ActionExecutor executor;
	private final Model<String> modelPrivateOffice;
	private final Model<String> modelForgetPassword;
	private final Model<String> modelRegistration;
	private final Model<String> modelAbout;
	private final String classMark;
	private final String classUnmark;
	
	/** панель, отображающая текущее действие пользователя (в каком именно меню он находится ) 
	 * @param panelId - уникальный идентификатор панели 
	 * @param executor - исполнитель для передачи данных о выборе в родительские панели 
	 * @param classMark - класс, который маркирует выделенный раздел меню 
	 * @param classUnmark - класс, который маркирует не выделенный раздел меню 
	 */
	public PanelNavigator(String panelId, 
						  ActionExecutor executor,
						  String classMark,
						  String classUnmark){
		super(panelId);
		this.executor=executor;

		this.classMark=classMark;
		this.classUnmark=classUnmark;
		modelPrivateOffice=new Model<String>(this.classUnmark);
		modelForgetPassword=new Model<String>(this.classUnmark);
		modelRegistration=new Model<String>(this.classUnmark);
		modelAbout=new Model<String>(this.classUnmark);

		initComponents();
	}
	
	private void initComponents(){
		
		WebMarkupContainer wrapPrivateOffice=new WebMarkupContainer("wrap_link_private_office_enter"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				this.add(new SimpleAttributeModifier("class",modelPrivateOffice.getObject()));
			}
		};
		wrapPrivateOffice.add(new Link<Object>("link_private_office_enter"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onButtonPrivateOfficeEnter();
			}
		});
		this.add(wrapPrivateOffice);
		
		WebMarkupContainer wrapForgetPassword=new WebMarkupContainer("wrap_link_forget_password"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				this.add(new SimpleAttributeModifier("class",modelForgetPassword.getObject()));
			}
		};
		wrapForgetPassword.add(new Link<Object>("link_forget_password"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onButtonForgetPassword();
			}
		});
		this.add(wrapForgetPassword);
		
		WebMarkupContainer wrapRegistration=new WebMarkupContainer("wrap_link_registration"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				this.add(new SimpleAttributeModifier("class",modelRegistration.getObject()));
			}
		};
		wrapRegistration.add(new Link<Object>("link_registration"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onButtonRegistration();
			}
		});
		this.add(wrapRegistration);
		
		WebMarkupContainer wrapAbout=new WebMarkupContainer("wrap_link_about"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				this.add(new SimpleAttributeModifier("class",modelAbout.getObject()));
			}
		};
		wrapAbout.add(new Link<Object>("link_about"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onButtonAbout();
			}
		});
		this.add(wrapAbout);
	}
	
	/** установить выделение для 
	 * <table border=1>
	 * 	<tr>
	 * 		<td> 0 </td> <td> Вход в кабинет </td> 
	 * 	</tr>
	 * 	<tr>
	 * 		<td> 1 </td> <td> Забыли пароль? </td> 
	 * 	</tr>
	 * 	<tr>
	 * 		<td> 2 </td> <td> Регистрация </td> 
	 * 	</tr>
	 * 	<tr>
	 * 		<td> 3 </td> <td> О системе </td> 
	 * 	</tr>
	 * </table>
	 * */
	public void setSelected(int index){
		this.modelPrivateOffice.setObject(this.classUnmark);
		this.modelForgetPassword.setObject(this.classUnmark);
		this.modelRegistration.setObject(this.classUnmark);
		this.modelAbout.setObject(this.classUnmark);
		switch(index){
			case 0:  this.modelPrivateOffice.setObject(this.classMark);break;
			case 1:  this.modelForgetPassword.setObject(this.classMark);break;
			case 2:  this.modelRegistration.setObject(this.classMark);break;
			case 3:  this.modelAbout.setObject(this.classMark);break;
		}
	}
	
	private void onButtonPrivateOfficeEnter(){
		this.setSelected(0);
		this.executor.action(Login.actionPrivateOfficeEnter, null);
	}
	private void onButtonForgetPassword(){
		this.setSelected(1);
		this.executor.action(Login.actionForgetPassword, null);
	}
	private void onButtonRegistration(){
		this.setSelected(2);
		this.executor.action(Login.actionRegistration, null);
	}
	private void onButtonAbout(){
		this.setSelected(3);
		this.executor.action(Login.actionAbout, null);
	}
	
}
