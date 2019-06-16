package bonclub.office_private.web_gui.login.panel_main.about;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.panel.Panel;

import bonclub.office_private.session.OfficePrivateSession;
import bonclub.office_private.web_gui.login.Login;

import wicket_utility.ActionExecutor;

/** Объект, отображающий страницу About */
public class About extends Panel{
	private final static long serialVersionUID=1L;
	private ActionExecutor executor;
	
	/** Объект, отображающий страницу About 
	 * @param id - уникальный идентификатор
	 * @param executor - объект, который принимает переданное управление  
	 * */
	public About(String id, ActionExecutor executor){
		super(id);
		this.executor=executor;
		this.initComponents();
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		Form<Object> formMain=new Form<Object>("form_main");
		SubmitLink submitLink=new SubmitLink("submit_link"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				About.this.onButtonClose();
				super.onSubmit();
			}
		};
		formMain.add(submitLink);
		this.add(formMain);
	}
	
	/** кнопка для перехода к регистрации */
	private void onButtonClose(){
		((OfficePrivateSession)this.getSession()).setCustomerId(null);
		this.executor.action(Login.actionRegistration, null);
	}
}
