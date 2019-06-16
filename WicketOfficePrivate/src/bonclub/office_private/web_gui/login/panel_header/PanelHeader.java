package bonclub.office_private.web_gui.login.panel_header;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import bonclub.office_private.web_gui.login.Login;

import wicket_utility.ActionExecutor;

/** панель-заголовок для страницы Login */
public class PanelHeader extends Panel{
	private final static long serialVersionUID=1L;
	private ActionExecutor executer;
	/** панель-заголовок для страницы Login 
	 * @param panelId - уникальный идентификатор для страницы
	 * @param executer - объект, которому следует передавать управление после получения управляющего воздействия 
	 */
	public PanelHeader(String panelId, ActionExecutor executer){
		super(panelId);
		this.executer=executer;
		initComponents();
	}
	
	private void initComponents(){
		Link<Object> linkBonCard=new Link<Object>("link_bon_card"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onBonCard();
			}
		};
		Link<Object> linkNakopilka=new Link<Object>("link_nakopilka"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onNakopilka();
			}
		};
		Link<Object> linkPrivateOffice=new Link<Object>("link_private_office"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onPrivateOffice();
			}
		};
		this.add(linkBonCard);
		this.add(linkNakopilka);
		this.add(linkPrivateOffice);
	}
	private void onBonCard(){
		this.executer.action(Login.actionBonKarta, null);
	}
	private void onNakopilka(){
		this.executer.action(Login.actionNakopilka, null);
	}
	private void onPrivateOffice(){
		this.executer.action(Login.actionPrivateOffice, null);
	}
	
}
