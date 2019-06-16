package bonpay.partner.web_gui.partner_admin;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.hibernate.Session;

import bonpay.partner.PartnerApplication;
import bonpay.partner.database.wrap.Partner;
import bonpay.partner.session.PartnerSession;
import bonpay.partner.web_gui.common.RedirectAction;

public class PanelInformation extends Panel{
	private final static long serialVersionUID=1L;
	private RedirectAction action;
	
	/** панель, которая отображает информацию по партнеру, 
	 * а так же содержит ссылки для отображения других панелей
	 * @param panelId - уникальный идентификатор данной панели
	 * @param action - объект, который содержит ссылки-переходы на другие страницы
	 * <table>
	 * 	<tr> 
	 * 		<td> shop_manager</td>
	 * 		<td> partner</td>
	 * 		<td> money_out</td>
	 * 		<td> profile</td>
	 * 		<td> exit</td>
	 * 	</tr>
	 * </table>
	 * 
	 * */
	public PanelInformation(String panelId,RedirectAction action){
		super(panelId);
		this.action=action;
		initComponent();
	}
	
	private void initComponent(){
		// get partner Id from Session
		Integer partnerId=this.getPartnerIdFromSession();
		if(partnerId!=null){
			// получить данные по партнеру
			Session session=null;
			try{
				session=((PartnerApplication)this.getApplication()).getHibernateConnection().openSession();
				Partner partner=(Partner)session.get(Partner.class, partnerId);
				this.add(new Label("partner_name",this.getPartnerName(session,partner)));
				this.add(new Label("pay",this.getPartnerPay(session,partner)));
				this.add(new Label("money_balance",this.getPartnerMoneyBalance(session,partner)));
				this.add(new Label("money_all",this.getPartnerMoneyAll(session,partner)));
			}catch(Exception ex){
				System.err.println("PanelInformation#initComponent Error:"+ex.getMessage());
			}finally{
				try{
					session.close();
				}catch(Exception ex){};
			}
			// create link
			Link linkShopManager=new Link("link_shop_manager"){
				private final static long serialVersionUID=1L;
				@Override
				public void onClick() {
					onShopManager();
				}
			};
			this.add(linkShopManager);
			
			Link linkPartner=new Link("link_partner"){
				private final static long serialVersionUID=1L;
				@Override
				public void onClick() {
					onPartner();
				}
			};
			this.add(linkPartner);

			Link linkMoneyOut=new Link("link_money_out"){
				private final static long serialVersionUID=1L;
				@Override
				public void onClick() {
					onMoneyOut();
				}
			};
			this.add(linkMoneyOut);
			
			Link linkProfile=new Link("link_profile"){
				private final static long serialVersionUID=1L;
				@Override
				public void onClick() {
					onProfile();
				}
			};
			this.add(linkProfile);

			Link linkExit=new Link("link_exit"){
				private final static long serialVersionUID=1L;
				@Override
				public void onClick() {
					onExit();
				}
			};
			this.add(linkExit);
		}else{
			// PartnerId is not exists 
		}
		setEmptyLabelIfNotExists("partner_name","pay","money_balance","money_all",
								 "link_shop_manager",
								 "link_partner",
								 "link_money_out",
								 "link_profile",
								 "link_exit");
	}
	
	/** получить уникальный идентификатор партнера из сессии */
	private Integer getPartnerIdFromSession(){
		return ((PartnerSession)this.getSession()).getPartnerId();
	}
	
	/** проверка элементов на наличие на странице, и если элементы не найдены - заменить их пустыми значениями*/
	private void setEmptyLabelIfNotExists(String ... elementNames){
		for(String element:elementNames){
			if(this.get(element)==null){
				try{
					this.add(new Label(element,""));
				}catch(Exception ex){
					System.err.println("PanelInformation#setEmptyLabelIfNotExists:"+ex.getMessage());
				}
			}
		}
	}
	
	private void onShopManager(){
		action.action(this, "shop_manager",null,null,null);
	}
	
	private void onPartner(){
		action.action(this, "partner",null,null,null);
	}
	
	private void onMoneyOut(){
		action.action(this, "money_out",null,null,null);
	}
	
	private void onProfile(){
		action.action(this, "profile",null,null,null);
	}
	
	private void onExit(){
		action.action(this, "exit",null,null,null);
	}
	
	/** получить имя партнера */
	private String getPartnerName(Session session, Partner partner){
		return ((partner.getSurname()==null)?"":partner.getSurname())+" "+((partner.getName()==null)?"":partner.getName());
	}
	
	/** получить сумму платежей */
	private String getPartnerPay(Session session,Partner partner){
		// TODO получить по партнеру сумму платежей 
		return "";
	}
	
	/** получить баланс платежей */
	private String getPartnerMoneyBalance(Session session, Partner partner){
		// TODO получить по партнеру баланс
		return "";
	}
	
	/** получить оборот */
	private String getPartnerMoneyAll(Session session, Partner partner){
		// TODO получить по партнеру оборот
		return "";
	}
	
}
