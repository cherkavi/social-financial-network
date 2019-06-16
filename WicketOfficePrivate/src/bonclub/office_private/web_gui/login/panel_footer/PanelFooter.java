package bonclub.office_private.web_gui.login.panel_footer;

import org.apache.wicket.markup.html.link.Link;

import org.apache.wicket.markup.html.panel.Panel;

import bonclub.office_private.web_gui.login.Login;

import wicket_utility.ActionExecutor;

/** панель, которая отображает Footer для Login*/
public class PanelFooter extends Panel{
	private final static long serialVersionUID=1L;
	private ActionExecutor executor;
	
	public PanelFooter(String panelId, ActionExecutor executor){
		super(panelId);
		this.executor=executor;
		initComponents();
	}
	
	private void initComponents(){
		this.add(new Link<Object>("link_private_office_enter"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onButtonPrivateOfficeEnter();
			}
		});
		this.add(new Link<Object>("link_forget_password"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onButtonForgetPassword();
			}
		});
		this.add(new Link<Object>("link_registration"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onButtonRegistration();
			}
		});
		this.add(new Link<Object>("link_about"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onButtonAbout();
			}
		});
	}
	
	private void onButtonPrivateOfficeEnter(){
		this.executor.action(Login.actionPrivateOffice, null);
	}
	private void onButtonForgetPassword(){
		this.executor.action(Login.actionForgetPassword, null);
	}
	private void onButtonRegistration(){
		this.executor.action(Login.actionRegistration, null);
	}
	private void onButtonAbout(){
		this.executor.action(Login.actionAbout, null);
	}
}
