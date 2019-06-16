package bonpay.partner.web_gui.register_information;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.hibernate.criterion.Restrictions;

import bonpay.partner.PartnerApplication;
import bonpay.partner.database.wrap.Partner;
import bonpay.partner.web_gui.PartnerEnterPoint;
import bonpay.partner.web_gui.common.RedirectAction;
import bonpay.partner.web_gui.common.RedirectActionPage;
import bonpay.partner.web_gui.common.PanelMessageError;
import bonpay.partner.web_gui.partner_admin.PartnerAdmin;

public class Registration extends WebPage{
	
	/** ввод данных по партнеру - редактирование или создание нового 
	 * @param partnerId - уникальный номер партнера, по которому необходимо проводить редактирование
	 * <br> null - если нужно проводить не редактирование а создание новой записи
	 * */
	public Registration(Integer partnerId){
		initComponents(partnerId);
	}
	
	/** Первоначальная инициализация компонентов */
	private void initComponents(Integer partnerId){
		Panel panel=null;
		if(partnerId==null){
			// registration new client
			panel=new PanelRegistration(new RedirectAction(new String[]{"save",
														        "cancel",
														        "error"},
												   new RedirectActionPage[]{new RedirectActionPage(PartnerAdmin.class),
																    new RedirectActionPage(PartnerEnterPoint.class),
												   					new RedirectActionPage(PartnerEnterPoint.class)
												   					}
									    ),"panel_main");
		}else{
			// go to "Panel of Partner"
			org.hibernate.Session session=null;
			try{
				session=((PartnerApplication)this.getApplication()).getHibernateConnection().openSession();
				Object object=session.createCriteria(Partner.class).add(Restrictions.eq("id", partnerId)).uniqueResult();
				if(object!=null){
					Partner partner=(Partner)object;
					panel=new PanelRegistration(new RedirectAction(new String[]{"save",
																	    "cancel"},
							   							   new RedirectActionPage[]{
																		new RedirectActionPage(PartnerEnterPoint.class),
																		new RedirectActionPage(PartnerEnterPoint.class)
																		   }
				    							),"panel_main",partner);
				}else{
					panel=new PanelMessageError("panel_main",this.getErrorIdentifyPartner(),"ok",new RedirectAction(new String[]{"ok"},new RedirectActionPage[]{new RedirectActionPage(PartnerEnterPoint.class)}));
				}
			}catch(Exception ex){
				panel=new PanelMessageError("panel_main",this.getErrorIdentifyPartner(),"ok",new RedirectAction(new String[]{"ok"},new RedirectActionPage[]{new RedirectActionPage(PartnerEnterPoint.class)}));
			}finally{
				session.close();
			}
		}
		this.add(panel);
	}
	
	/** ошибка загрузки информации по партнеру */
	private String getErrorIdentifyPartner(){
		return "Ошибка загрузки данных по партнеру ";
	}
}
