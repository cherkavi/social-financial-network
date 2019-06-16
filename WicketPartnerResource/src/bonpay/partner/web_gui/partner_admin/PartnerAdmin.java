package bonpay.partner.web_gui.partner_admin;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.hibernate.Session;

import bonpay.partner.PartnerApplication;
import bonpay.partner.database.wrap.Partner;
import bonpay.partner.session.PartnerSession;
import bonpay.partner.web_gui.PartnerEnterPoint;
import bonpay.partner.web_gui.common.RedirectAction;
import bonpay.partner.web_gui.common.RedirectActionPage;
import bonpay.partner.web_gui.common.PanelMessageError;
import bonpay.partner.web_gui.common.PanelMessageInformation;
import bonpay.partner.web_gui.partner_admin.panel_link.AboutSystem;
import bonpay.partner.web_gui.partner_admin.panel_link.Connect;
import bonpay.partner.web_gui.partner_admin.panel_link.Contacts;
import bonpay.partner.web_gui.partner_admin.panel_link.Documentation;
import bonpay.partner.web_gui.partner_admin.panel_link.Faq;
import bonpay.partner.web_gui.partner_admin.panel_link.ForPartner;
import bonpay.partner.web_gui.partner_admin.panel_link.News;
import bonpay.partner.web_gui.partner_admin.panel_link.Service;
import bonpay.partner.web_gui.partner_admin.panel_settings.PanelSettings;
import bonpay.partner.web_gui.register_information.PanelRegistration;

public class PartnerAdmin extends WebPage {
	private String panelInformationId="panel_information";
	private String panelMainId="panel_main";
	private String panelLinkId="panel_link";
	
	/** панель, которая содержит все ссылки на цетральные страницы */
	private Panel panelLink;
	/** панель, которая содержит ссылки по администированию */
	private Panel panelInformation;
	private Panel panelMain;
	
	public PartnerAdmin(){
		initComponents();
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		
		this.panelInformation=new PanelInformation(this.panelInformationId,new RedirectAction(new String[]{"shop_manager",
																								   "partner",
																								   "money_out",
																								   "profile",
																								   "exit"},
																			          new RedirectActionPage[]{
																								   new RedirectActionPage(this,this,"showPanelShowManager",new Class[]{},new Object[]{}),
																								   new RedirectActionPage(this,this,"showPanelPartners",new Class[]{},new Object[]{}),
																								   new RedirectActionPage(this,this,"showPanelCashOut",new Class[]{},new Object[]{}),
																								   new RedirectActionPage(this,this,"showPanelProfile",new Class[]{},new Object[]{}),
																								   new RedirectActionPage(PartnerEnterPoint.class),
																								   	  }
																					  )
												   );
		this.panelMain=new PanelShopManager(this.panelMainId,
										    new RedirectAction(
												new String[]{
													"onShopOperationSettings",
													"render"
													},
												new RedirectActionPage[]{
													new RedirectActionPage(this,this,"showPanelShopManagerSettings",new Class[]{},new Object[]{}),
			    									new RedirectActionPage(this,this,"renderPanelMain",new Class[]{},new Object[]{})
													}
												)
											);
		this.panelLink=new PanelLink(this.panelLinkId,
									 new RedirectAction(
									    new String[]{
									    	PanelLink.LINK_ABOUT_SYSTEM,
									    	PanelLink.LINK_CONNECT,
									    	PanelLink.LINK_CONTACTS,
									    	PanelLink.LINK_DOCUMENTATION,
									    	PanelLink.LINK_FAQ,
									    	PanelLink.LINK_FOR_PARTNER,
									    	PanelLink.LINK_NEWS,
									    	PanelLink.LINK_SERVICE
									    	},
									    new RedirectActionPage[]{
									    		new RedirectActionPage(this,this,"showPanelLinkAboutSystem",null,null),
									    		new RedirectActionPage(this,this,"showPanelLinkConnect",null,null),
									    		new RedirectActionPage(this,this,"showPanelLinkContacts",null,null),
									    		new RedirectActionPage(this,this,"showPanelLinkDocumentation",null,null),
									    		new RedirectActionPage(this,this,"showPanelLinkFaq",null,null),
									    		new RedirectActionPage(this,this,"showPanelLinkForPartner",null,null),
									    		new RedirectActionPage(this,this,"showPanelLinkNews",null,null),
									    		new RedirectActionPage(this,this,"showPanelLinkService",null,null),
									    	}
									    )
									);
		this.add(panelLink);
		this.add(panelInformation);
		this.add(panelMain);
	}
	
	public void showPanelLinkAboutSystem(){
		this.remove(panelMain);
		this.panelMain=new AboutSystem(this.panelMainId,
									   new RedirectAction(new String[]{},
											   			  new RedirectActionPage[]{}
									                      )
									   );		
		this.add(panelMain);
	}

	public void showPanelLinkNews(){
		this.remove(panelMain);
		this.panelMain=new News(this.panelMainId,
									   new RedirectAction(new String[]{},
											   			  new RedirectActionPage[]{}
									                      )
									   );		
		this.add(panelMain);
	}

	public void showPanelLinkService(){
		this.remove(panelMain);
		this.panelMain=new Service(this.panelMainId,
									   new RedirectAction(new String[]{},
											   			  new RedirectActionPage[]{}
									                      )
									   );		
		this.add(panelMain);
	}
	
	public void showPanelLinkConnect(){
		this.remove(panelMain);
		this.panelMain=new Connect(this.panelMainId,
									   new RedirectAction(new String[]{},
											   			  new RedirectActionPage[]{}
									                      )
									   );		
		this.add(panelMain);
	}
	public void showPanelLinkDocumentation(){
		this.remove(panelMain);
		this.panelMain=new Documentation(this.panelMainId,
									   new RedirectAction(new String[]{},
											   			  new RedirectActionPage[]{}
									                      )
									   );		
		this.add(panelMain);
	}
	public void showPanelLinkForPartner(){
		this.remove(panelMain);
		this.panelMain=new ForPartner(this.panelMainId,
									   new RedirectAction(new String[]{},
											   			  new RedirectActionPage[]{}
									                      )
									   );		
		this.add(panelMain);
	}

	public void showPanelLinkFaq(){
		this.remove(panelMain);
		this.panelMain=new Faq(this.panelMainId,
									   new RedirectAction(new String[]{},
											   			  new RedirectActionPage[]{}
									                      )
									   );		
		this.add(panelMain);
	}
	
	public void showPanelLinkContacts(){
		this.remove(panelMain);
		this.panelMain=new Contacts(this.panelMainId,
									   new RedirectAction(new String[]{},
											   			  new RedirectActionPage[]{}
									                      )
									   );		
		this.add(panelMain);
	}
	
	public void showPanelShowManager(){
		this.remove(panelMain);
		this.panelMain=new PanelShopManager(this.panelMainId,
			    							new RedirectAction(
			    								new String[]{
			    									"onShopOperationSettings",
			    									"render"
			    									},
			    								new RedirectActionPage[]{
			    									new RedirectActionPage(this,this,"showPanelShopManagerSettings",new Class[]{},new Object[]{}),
			    									new RedirectActionPage(this,this,"renderPanelMain",new Class[]{},new Object[]{})
			    									}
			    								)
											);
		this.add(panelMain);
	}
	
	public void renderPanelMain(){
		System.out.println("renderPanelMain");
		PartnerAdmin.this.renderPage();
	}
	
	/** отобразить в качестве главной панели Настройки для указанного магазина */
	public void showPanelShopManagerSettings(Integer satelliteId){
		this.remove(panelMain);
		this.panelMain=new PanelSettings(this.panelMainId,
										 satelliteId,
										 new RedirectAction(
												 new String[]{"saveOk",
														 	  "saveCancel",
														 	  "saveError"},
												 new RedirectActionPage[]{
														 	  new RedirectActionPage(this,this,"showPanelMessageInformation",new Class[]{String.class},new Object[]{this.getString("save_ok")}),
														 	  new RedirectActionPage(this,this,"showPanelMessageInformation",new Class[]{String.class},new Object[]{this.getString("save_cancel")}),
														 	  new RedirectActionPage(this,this,"showPanelMessageError",new Class[]{String.class},new Object[]{this.getString("save_error")})
												 }
										     )
									     );
		this.add(panelMain);
	}
	
	public void showPanelPartners(){
		this.remove(panelMain);
		this.panelMain=new PanelPartner(this.panelMainId);
		this.add(panelMain);
	}
	public void showPanelCashOut(){
		this.remove(panelMain);
		this.panelMain=new PanelMoneyOut(this.panelMainId);
		this.add(panelMain);
	}
	public void showPanelProfile(){
		this.remove(panelMain);
		this.panelMain=new PanelRegistration(new RedirectAction(new String[]{"save",
																     "cancel",
																     "error"},
														new RedirectActionPage[]{
																     new RedirectActionPage(this,this,"showPanelMessageInformation",new Class[]{String.class},new Object[]{this.getString("save_ok")}),
																     new RedirectActionPage(this,this,"showPanelMessageInformation",new Class[]{String.class},new Object[]{this.getString("save_cancel")}),
																     new RedirectActionPage(this,this,"showPanelMessageError",new Class[]{String.class},new Object[]{this.getString("save_error")}),
																         }
														),
											 this.panelMainId,
											 this.getPartnerFromSession());
		this.add(panelMain);
	}
	
	/** получить объект с данными текущего партнера */
	private Partner getPartnerFromSession(){
		Partner returnValue=null;
		Session session=null;
		try{
			session=((PartnerApplication)this.getApplication()).getHibernateConnection().openSession();
			returnValue=(Partner)session.get(Partner.class, ((PartnerSession)this.getSession()).getPartnerId());
		}catch(Exception ex){
			System.err.println("PartnerAdmin#getPartnerFromSession Exception:"+ex.getMessage());
		}finally{
			try{
				session.close();
			}catch(Exception ex2){};
		}
		return returnValue;
	}
	
	/** отобразить в качестве главной панели информационное сообщение с кнопкой OK */
	public void showPanelMessageInformation(String message){
		this.remove(panelMain);
		this.panelMain=new PanelMessageInformation(this.panelMainId,
												   message,
												   "Ok",
												   new RedirectAction(new String[]{"ok"},
														   	  new RedirectActionPage[]{new RedirectActionPage(this,this,"showPanelShowManager",new Class[]{},new Object[]{}),}
												   			  )
												   );
		this.add(panelMain);
	}

	/** отобразить в качестве главной панели ошибочное сообщение с кнопкой OK */
	public void showPanelMessageError(String message){
		this.remove(panelMain);
		this.panelMain=new PanelMessageError(this.panelMainId,
												   message,
												   "Ok",
												   new RedirectAction(new String[]{"ok"},
														   	  new RedirectActionPage[]{new RedirectActionPage(this,this,"showPanelShowManager",new Class[]{},new Object[]{}),}
												   			  )
												   );
		this.add(panelMain);
	}
}
