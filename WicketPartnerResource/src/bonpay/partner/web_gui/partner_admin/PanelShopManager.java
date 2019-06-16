package bonpay.partner.web_gui.partner_admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import bonpay.partner.PartnerApplication;
import bonpay.partner.database.wrap.Satellite;
import bonpay.partner.session.PartnerSession;
import bonpay.partner.web_gui.common.CallAction;
import bonpay.partner.web_gui.common.ModalConfirm;
import bonpay.partner.web_gui.common.RedirectAction;

/** панель, которая отображает магазины по партнеру */
public class PanelShopManager extends Panel{
	private final static long serialVersionUID=1L;
	private Model modelMessageErrorAdd=new Model("");
	private Model modelShopUrl=new Model("");
	private Model modelShopName=new Model("");
	private ListView shopList;
	/** счетчик для кол-ва магазинов */
	private IntegerWrap counter;
	/** модальное окно для подтверждения */
	private ModalWindow modalWindow;
	private ModalConfirm modalConfirm;
	/** объект для получения дальнейших действий по передаче управления */
	private RedirectAction redirectAction;
	/** компонент, который несет в себе список магазинов */
	WebMarkupContainer shopListPlatform;
	
	protected final static HashMap<Integer,String> statusIcon;
	static{
		statusIcon=new HashMap<Integer,String>();
		statusIcon.put(new Integer(0),"icon_new.png");
		statusIcon.put(new Integer(1),"icon_in_look.png");
		statusIcon.put(new Integer(2),"icon_not_fact.png");
		statusIcon.put(new Integer(3),"icon_activate.png");
		statusIcon.put(new Integer(4),"icon_removed.png");
		statusIcon.put(new Integer(5),"icon_deactive.png");
	}
	
	
	/** отображает магазины, которые принадлежат данному партнеру */
	public PanelShopManager(String id,RedirectAction redirectAction){
		super(id);
		this.redirectAction=redirectAction;
		initComponents();
	}
	
	private void sendRefreshList(AjaxRequestTarget target){
		System.out.println("get list of satellite");
		//this.shopListPlatform.removeAll();
		this.shopList.setList(this.getListOfSatellite());
		//=this.getListView("shop_list",);
		this.shopListPlatform.replace(this.shopList);
		target.addComponent(this.shopListPlatform);
		//System.out.println("answer add ok listSize:"+this.getListOfSatellite().size());
		//this.redirectAction.action(this, "render", null, null, null);
	}
	
	/** инициализировать визуальные компоненты */
	private void initComponents(){
		modalConfirm=new ModalConfirm();
		modalWindow=new ModalWindow("modal_confirm");
		modalWindow.setPageMapName("modal_confirm");
		modalWindow.setCookieName("modal_confirm");
		modalWindow.setPageCreator(modalConfirm);
		modalWindow.setWindowClosedCallback(new WindowClosedCallback(){
			private final static long serialVersionUID=1L;
			@Override
			public void onClose(AjaxRequestTarget target) {
				System.out.println("ModalClose");
				sendRefreshList(target);
			}
		});
		this.add(modalWindow);
		
		Form formAdd=new Form("form_add"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonAdd();
			}
		};
		formAdd.add(new TextField("edit_shop_name",modelShopName));
		formAdd.add(new TextField("edit_shop_url",modelShopUrl));
		formAdd.add(new Button("button_shop_add"));
		formAdd.add(new Label("message_error_add",modelMessageErrorAdd));
		this.add(formAdd);
		shopList=this.getListView("shop_list",this.getListOfSatellite());
		shopListPlatform=new WebMarkupContainer("shop_list_platform");
		shopListPlatform.setOutputMarkupId(true);

		shopListPlatform.add(new Link("shop_report_all"){
			private final static long serialVersionUID=1L;
			public void onClick(){
				onShopReportAll();
			}
		});
		shopListPlatform.add(new Link("shop_statistic_all"){
			private final static long serialVersionUID=1L;
			public void onClick(){
				onShopStatisticAll();
			}
		});
		shopListPlatform.add(shopList);
		this.add(shopListPlatform);
	}

	/** получить компонент ListView */
	private ListView getListView(String id, List<?> list){
		return new ListView(id,list){
			private final static long serialVersionUID=1L;
			{
				PanelShopManager.this.counter=new IntegerWrap(0);
			}
			@Override
			protected void populateItem(ListItem item) {
				counter.inc();
				Satellite satellite=getSatelliteById((Integer)item.getModelObject());
				final int satelliteId=satellite.getId();
				item.add(new Label("number",counter.toString()));
				item.add(new Image("status",statusIcon.get(new Integer(satellite.getIdStatus()))));
				item.add(new Label("name",satellite.getName()));
				item.add(new Label("url",satellite.getUrl()));
				item.add(new Label("balance","0"));
				item.add(new Label("all","0"));
				item.add(new Link("shop_operation_settings"){
					private final static long serialVersionUID=1L;
					@Override
					public void onClick() {
						onShopOperationSettings(satelliteId);
					}
				});
				item.add(new Link("shop_operation_report"){
					private final static long serialVersionUID=1L;
					@Override
					public void onClick() {
						onShopOperationReport(satelliteId);
					}
				});
				item.add(new Link("shop_operation_statistic"){
					private final static long serialVersionUID=1L;
					@Override
					public void onClick() {
						onShopOperationStatistic(satelliteId);
					}
				});
				item.add(new Link("shop_operation_money_out"){
					private final static long serialVersionUID=1L;
					@Override
					public void onClick() {
						onShopOperationMoneyOut(satelliteId);
					}
				});
				item.add(new Link("shop_operation_export"){
					private final static long serialVersionUID=1L;
					@Override
					public void onClick() {
						onShopOperationExport(satelliteId);
					}
				});
				AjaxLink ajaxLinkRemove=new AjaxLink("shop_operation_remove"){
					private final static long serialVersionUID=1L;
					@Override
					public void onClick(AjaxRequestTarget target) {
						onShopOperationRemove(target,satelliteId);
						System.out.println("onclick");
					}
				};
				
				/*ajaxLinkRemove.add(new AjaxEventBehavior("onclick"){
					private final static long serialVersionUID=1L;
					@Override
					protected void onEvent(AjaxRequestTarget target) {
						onShopOperationRemove(target,satelliteId);
						System.out.println("onclick");
					}
				});*/
				item.add(ajaxLinkRemove);
			}
		}; 
	}
	
	/** реакция на нажатие показа отчета по продажам по всем магазинам */
	private void onShopReportAll(){
		// TODO 
		System.out.println("ShopReportAll");
		this.counter.reset();
	}
	
	/** реакция на нажатие показа статистики по всем магазинам */
	private void onShopStatisticAll(){
		// TODO 
		System.out.println("ShopStaticAll");
		this.counter.reset();
	}
	
	/** реакция на нажатие ссылки перехода на Settings магазина */
	private void onShopOperationSettings(int satelliteId){
		// TODO 
		System.out.println("Operation Settings "+satelliteId);
		this.redirectAction.action(this, "onShopOperationSettings", null, new Class<?>[]{Integer.class}, new Object[]{new Integer(satelliteId)});
		this.counter.reset();
	}
	/** реакция на нажатие ссылки перехода на Report магазина */
	private void onShopOperationReport(int satelliteId){
		// TODO
		System.out.println("Operation Report "+satelliteId);
		this.counter.reset();
	}
	/** реакция на нажатие ссылки перехода на Statistic магазина */
	private void onShopOperationStatistic(int satelliteId){
		// TODO 
		System.out.println("Operation Statistic "+satelliteId);
		this.counter.reset();
	}
	/** реакция на нажатие ссылки перехода на Money Out магазина */
	private void onShopOperationMoneyOut(int satelliteId){
		// TODO 
		System.out.println("Operation Money Out "+satelliteId);
		this.counter.reset();
	}
	/** реакция на нажатие ссылки перехода на Operation Export магазина */
	private void onShopOperationExport(int satelliteId){
		// TODO 
		System.out.println("Operation Export "+satelliteId);
		this.counter.reset();
	}
	/** reaction on striking button remove for shop */
	private void onShopOperationRemove(AjaxRequestTarget target, int satelliteId){
		// FIXME
		System.out.println("Operation remove:"+satelliteId);
		this.counter.reset();
		this.modalConfirm.setCallActionButtonOk(new CallAction(this,"removeOk",new Class[]{Integer.class},new Object[]{satelliteId}));
		this.modalConfirm.setCallActionButtonCancel(new CallAction(this,"removeCancel"));
		this.modalConfirm.setModalWindow(this.modalWindow);
		this.modalConfirm.setButtonOkCaption(this.getString("confirm.delete.button.ok"));
		this.modalConfirm.setButtonCancelCaption(this.getString("confirm.delete.button.cancel"));
		this.modalConfirm.setMessage(this.getString("confirm.delete.message"));
		this.modalWindow.setInitialHeight(200);
		this.modalWindow.setInitialWidth(300);
		//this.modalConfirm.setPageForRender(this.getPage());
		this.modalWindow.show(target);
		
		System.out.println("Operation remove: end");
	}
	
	public void removeOk(AjaxRequestTarget target,Integer satelliteId){
		System.out.println("button remove Ok:"+satelliteId);
		if(removeSatelliteId(satelliteId)==true){
			if(this.shopList!=null){
			}else{
				System.out.println("shop is null");
			}
			
		}else{
			System.err.println("PanelShopManager#removeOk: remove error");
		}
	}
	
	public void removeCancel(AjaxRequestTarget target){
		System.out.println("buttonCancel");
	}
	
	/** удалить Satellite Id по уникальному номеру */
	private boolean removeSatelliteId(Integer satelliteId){
		boolean returnValue=false;
		Session session=null;
		try{
			session=((PartnerApplication)this.getApplication()).getHibernateConnection().openSession();
			session.beginTransaction();
			session.delete(session.get(Satellite.class, satelliteId));
			session.getTransaction().commit();
			returnValue=true;
		}catch(Exception ex){
			System.err.println("PanelShopManager#removeSatelliteId:"+ex.getMessage());
		}finally{
			try{
				session.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	/** получить объект Satellite на основании уникального номера */
	private Satellite getSatelliteById(Integer satelliteId){
		Satellite returnValue=null;
		Session session=null;
		try{
			session=((PartnerApplication)this.getApplication()).getHibernateConnection().openSession();
			returnValue=(Satellite)session.get(Satellite.class, satelliteId);
		}catch(Exception ex){
			System.err.println("PanelShopManager#getSatelliteById Exception:"+ex.getMessage());
		}finally{
			try{
				session.close();
			}catch(Exception ex){};
		}
		return (returnValue==null)?(new Satellite()):returnValue;
	}
	
	/** получить список уникальных кодов для Satellite, которые принадлежат данному Partner*/
	@SuppressWarnings("unchecked")
	private List<Integer> getListOfSatellite(){
		List<Integer> returnValue=null;
		Integer partnerId=((PartnerSession)this.getSession()).getPartnerId();
		Session session=null;
		try{
			session=((PartnerApplication)this.getApplication()).getHibernateConnection().openSession();
			returnValue=(List<Integer>)session.createSQLQuery("SELECT ID FROM SATELLITE WHERE SATELLITE.ID_PARTNER=:partner").setInteger("partner", partnerId).list();
		}catch(Exception ex){
			System.err.println("PanelShopManager#getListOfSatellite: Exception:"+ex.getMessage());
		}finally{
			try{
				session.close();
			}catch(Exception ex){};
		}
		return (returnValue==null)?(new ArrayList<Integer>()):returnValue;
	}
	
	/** reaction on striking button ADD */
	private void onButtonAdd(){
		if(checkVariables()==true){
			this.modelMessageErrorAdd.setObject("");
			// параметры введены правильно - создать магазин по данному PartnerId, по Названию и по URL
			if(this.addShop((String)this.modelShopName.getObject(),(String)this.modelShopUrl.getObject())==true){
				this.modelShopName.setObject("");
				this.modelShopUrl.setObject("");
				//this.remove("shop_list");
				//this.shopList=this.getListView("shop_list", this.getListOfSatellite());
				//this.add(this.shopList);
				this.shopList.setList(this.getListOfSatellite());
			}else{
				System.err.println("PanelShopManager#onButtonAdd: Shop is not added to Database ");
			}
		}else{
			// параметры введены неправильно - отображение modelMessageErrorAdd 
		}
	}
	
	/** добавить магазин для партнера на основании введенных значений */
	private boolean addShop(String name, String url){
		boolean returnValue=false;
		Satellite satellite=new Satellite();
		satellite.setIdPartner(((PartnerSession)this.getSession()).getPartnerId());
		satellite.setIdStatus(0);
		satellite.setName((String)this.modelShopName.getObject());
		satellite.setUrl((String)this.modelShopUrl.getObject());
		Session session=null;
		try{
			session=((PartnerApplication)this.getApplication()).getHibernateConnection().openSession();
			session.beginTransaction();
			session.save(satellite);
			session.getTransaction().commit();
			returnValue=true;
		}catch(Exception ex){
			System.err.println("PanelShopManager#addShop Exception:"+ex.getMessage());
			try{
				session.getTransaction().rollback();
			}catch(Exception ex2){};
		}finally{
			try{
				session.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	/** проверка введенных значений */
	private boolean checkVariables(){
		boolean returnValue=true;
		// проверка на введенное название магазина
		if(this.isModelEmpty(this.modelShopName)==true){
			this.modelMessageErrorAdd.setObject("введите наименование магазина");
			returnValue=false;
		}
		// проверка на введенный URL кабинета
		if(this.isModelEmpty(this.modelShopUrl)==true){
			this.modelMessageErrorAdd.setObject("введите адрес магазина");
			returnValue=false;
		}
		return returnValue;
	}
	
	/** возвращает true если model==null или model=="" */
	private boolean isModelEmpty(IModel model){
		boolean returnValue=true;
		if((model!=null)&&(model.getObject()!=null)){
			if(model.getObject() instanceof String){
				return ((String)model.getObject()).trim().equals("");
			}
		}else{
			// model is null
		}
		return returnValue;
	}
}

class IntegerWrap implements Serializable{
	private final static long serialVersionUID=1L;
	private int value;
	public IntegerWrap(int startValue){
		this.value=startValue;
	}
	
	public int inc(){
		return ++value;
	}

	public void reset(){
		this.value=0;
	}
	
	public String toString(){
		return Integer.toString(value);
	}
}
