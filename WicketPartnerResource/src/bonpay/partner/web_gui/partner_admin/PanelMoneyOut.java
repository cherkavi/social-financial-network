package bonpay.partner.web_gui.partner_admin;

import java.util.List;

import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;


import bonpay.partner.PartnerApplication;
import bonpay.partner.session.PartnerSession;

/** панель, которая отображает магазины по партнеру */
public class PanelMoneyOut extends Panel{
	private final static long serialVersionUID=1L;
	private Model dateBeginModel=new Model();
	private Model dateEndModel=new Model();
	private DropDownChoice shopChoice=null;
	
	public PanelMoneyOut(String id){
		super(id);
		initComponents();
	}
	
	/** create and initComponent's*/
	private void initComponents(){
		Form formMoneyOut=new Form("form_money_out");
		List<String> shopByPartner=getShopByPartner();
		if((shopByPartner!=null)&&(shopByPartner.get(0)!=null)){
			shopChoice=new DropDownChoice("shop_choice",new Model(shopByPartner.get(0)),shopByPartner);
		}else{
			shopChoice=new DropDownChoice("shop_choice");
		}
		Button buttonMoneyOut=new Button("button_money_out"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonMoneyOut();
			}
		};
		formMoneyOut.add(shopChoice);
		formMoneyOut.add(buttonMoneyOut);
		
		Link linkHistoryMonth=new Link("link_history_month"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onHistoryMonth();
			}
		};
		Link linkHistoryYear=new Link("link_history_year"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick(){
				onHistoryYear();
			}
		};
		
		TextField textDateBegin=new TextField("date_begin",dateBeginModel,java.util.Date.class);
		textDateBegin.setRequired(false);
		textDateBegin.add(new DatePicker());
		TextField textDateEnd=new TextField("date_end",dateEndModel,java.util.Date.class);
		textDateEnd.setRequired(false);
		textDateEnd.add(new DatePicker());
		Button buttonHistory=new Button("button_history"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonHistory();
			}
		};
		
		Form formMoneyShow=new Form("form_money_show");
		formMoneyShow.add(linkHistoryMonth);
		formMoneyShow.add(linkHistoryYear);
		formMoneyShow.add(textDateBegin);
		formMoneyShow.add(textDateEnd);
		formMoneyShow.add(buttonHistory);

		this.add(formMoneyOut);
		this.add(formMoneyShow);
	}
	
	/** получить список магазинов по партнеру, который "находится" в сессии */
	@SuppressWarnings("unchecked")
	private List<String> getShopByPartner(){
		List<String> returnValue=null;
		Integer partnerId=((PartnerSession)this.getSession()).getPartnerId();
		if(partnerId!=null){
			Session session=null;
			try{
				session=((PartnerApplication)this.getApplication()).getHibernateConnection().openSession();
				returnValue=(List<String>)session.createSQLQuery("SELECT NAME FROM SATELLITE WHERE ID_PARTNER=:partner_id").addScalar("NAME").setInteger("partner_id", partnerId).list();
			}catch(Exception ex){
				System.err.println("PanelMoneyOut Exception:"+ex.getMessage());
			}finally{
				try{
					session.close();
				}catch(Exception ex){};
			}
			// returnValue=null;
		}else{
			// returnValue=null;
		}
		return returnValue;
	}
	
	/** reaction on striking button MoneyOut*/
	private void onButtonMoneyOut(){
		// TODO
		System.out.println("onbuttonMoneyOut:"+this.shopChoice.getModelObject());
	}
	
	/** show history per month */
	private void onHistoryMonth(){
		// TODO 
		System.out.println("PanelMoneyOut#onHistoryMonth: show history per month");
	}
	
	/** show history per year */
	private void onHistoryYear(){
		// TODO
		System.out.println("PanelMoneyOut#onHistoryYear: show history per year");
	}

	/** reaction on striking button History */
	private void onButtonHistory(){
		// TODO 
		System.out.println("onbuttonHistory  date begin:"+this.dateBeginModel+"   date end:"+this.dateEndModel);
	}
}
