package bonpay.partner.web_gui.partner_admin;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import bonpay.partner.web_gui.common.RedirectAction;

/** панель, которая отображается в верхней части и содержит информацию для переходов */
public class PanelLink extends Panel{
	private final static long serialVersionUID=1L;
	public static String LINK_ABOUT_SYSTEM="link_about_system";
	public static String LINK_NEWS="link_news";
	public static String LINK_SERVICE="link_service";
	public static String LINK_CONNECT="link_connect";
	public static String LINK_DOCUMENTATION="link_documentation";
	public static String LINK_FOR_PARTNER="link_for_partner";
	public static String LINK_FAQ="link_faq";
	public static String LINK_CONTACTS="link_contacts";
	/** объект, который содержит ссылки для перенаправления данного вызова */
	private RedirectAction action;
	
	public PanelLink(String panelId, RedirectAction action){
		super(panelId);
		this.action=action;
		initComponents();
	}
	
	/** первоначальная инициализация всех компонентов */
	private void initComponents(){
		this.add(new Link("link_about_system"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onLinkAboutSystem();
			}
		});
		this.add(new Link("link_news"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onLinkNews();
			}
		});
		this.add(new Link("link_service"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onLinkService();
			}
		});
		this.add(new Link("link_connect"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onLinkConnect();
			}
		});
		this.add(new Link("link_documentation"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onLinkDocumentation();
			}
		});
		this.add(new Link("link_for_partner"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onLinkForPartner();
			}
		});
		this.add(new Link("link_faq"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onLinkFaq();
			}
		});
		this.add(new Link("link_contacts"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onLinkContacts();
			}
		});
	}
	
	private void onLinkAboutSystem(){
		this.action.action(this, PanelLink.LINK_ABOUT_SYSTEM, null, null, null);
	}
	private void onLinkNews(){
		this.action.action(this, PanelLink.LINK_NEWS, null, null, null);
	}
	private void onLinkService(){
		this.action.action(this, PanelLink.LINK_SERVICE, null, null, null);
	}
	private void onLinkConnect(){
		this.action.action(this, PanelLink.LINK_CONNECT, null, null, null);
	}
	private void onLinkDocumentation(){
		this.action.action(this, PanelLink.LINK_DOCUMENTATION, null, null, null);
	}
	private void onLinkForPartner(){
		this.action.action(this, PanelLink.LINK_FOR_PARTNER, null, null, null);
	}
	private void onLinkFaq(){
		this.action.action(this, PanelLink.LINK_FAQ, null, null, null);
	}
	private void onLinkContacts(){
		this.action.action(this, PanelLink.LINK_CONTACTS, null, null, null);
	}
}
