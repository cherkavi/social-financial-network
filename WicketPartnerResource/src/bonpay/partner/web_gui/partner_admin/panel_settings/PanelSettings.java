package bonpay.partner.web_gui.partner_admin.panel_settings;

import java.util.ArrayList;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import bonpay.partner.PartnerApplication;
import bonpay.partner.database.wrap.Satellite;
import bonpay.partner.session.PartnerSession;
import bonpay.partner.web_gui.common.RedirectAction;

/** панель, которая содержит настройки для SATELLITE (магазина) по партнеру */
public class PanelSettings extends Panel{
	private final static long serialVersionUID=1L;
	private RedirectAction redirectAction;
	private Integer satelliteId;
	/** панель, которая содержит настройки для SATELLITE (магазина) по партнеру 
	 * @param wicketId - уникальный идентификатор данной панели на странице магазина
	 * @param satelliteId - уникальный идентификатор клиента, по которому происходят настройки 
	 */
	public PanelSettings(String wicketId, Integer satelliteId,RedirectAction redirectAction){
		super(wicketId);
		this.satelliteId=satelliteId;
		this.redirectAction=redirectAction;
		initComponents();
		this.setSatelliteToModel(satelliteId);
	}
	private ArrayList<String> methodList=new ArrayList<String>();
	{
		methodList.add("GET");
		methodList.add("POST");
	}
	private Model modelName=new Model();
	private Model modelUrl=new Model();
	private Model modelDescription=new Model();
	private Model modelUrlOk=new Model();
	private Model modelUrlOkMethod=new Model(methodList.get(0));
	private Model modelUrlCancel=new Model();
	private Model modelUrlCancelMethod=new Model(methodList.get(0));
	private Model modelUrlError=new Model();
	private Model modelUrlErrorMethod=new Model(methodList.get(0));
	private Model modelParameterKey=new Model();
	private Model modelEMail=new Model();
	private Model modelPhoneForSms=new Model();
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		Form formMain=new Form("form_main");
		TextField name=new TextField("textfield_name",modelName);
		TextField url=new TextField("textfield_url",modelUrl);
		TextField description=new TextField("textfield_description",modelDescription);
		TextField urlOk=new TextField("textfield_url_ok",modelUrlOk);
		DropDownChoice urlOkMethod=new DropDownChoice("dropdown_url_ok_method",modelUrlOkMethod,methodList);
		TextField urlCancel=new TextField("textfield_url_cancel",modelUrlCancel);
		DropDownChoice urlCancelMethod=new DropDownChoice("dropdown_url_cancel_method",modelUrlCancelMethod,methodList);
		TextField urlError=new TextField("textfield_url_error",modelUrlError);
		DropDownChoice urlErrorMethod=new DropDownChoice("dropdown_url_error_method",modelUrlErrorMethod,methodList);
		TextField parameterKey=new TextField("textfield_parameter_key",modelParameterKey);
		TextField eMail=new TextField("textfield_e_mail",modelEMail);
		TextField phoneForSms=new TextField("textfield_phone_for_sms",modelPhoneForSms);
		Button buttonSave=new Button("button_save"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonSave();
			}
		};
		Button buttonCancel=new Button("button_cancel"){
			private final static long serialVersionUID=1L;
			public void onSubmit(){
				onButtonCancel();
			}
		};
		formMain.add(name);
		formMain.add(url);
		formMain.add(description);
		formMain.add(urlOk);
		formMain.add(urlOkMethod);
		formMain.add(urlCancel);
		formMain.add(urlCancelMethod);
		formMain.add(urlError);
		formMain.add(urlErrorMethod);
		formMain.add(parameterKey);
		formMain.add(eMail);
		formMain.add(phoneForSms);
		formMain.add(buttonSave);
		formMain.add(buttonCancel);
		this.add(formMain);
	}
	
	/** реакция на нажатие кнопки Сохранить */
	private void onButtonSave(){
		// TODO - место проверки введенных значений, передача управления
		System.out.println("onButtonSave");
		Satellite satellite=this.getSatelliteByKod(this.satelliteId);
		if(satellite==null){
			satellite=new Satellite();
			satellite.setIdPartner(((PartnerSession)this.getSession()).getPartnerId());
		}
		satellite=this.getSatelliteFromVisualComponent(this.getSatelliteByKod(satelliteId));
		if(saveSatellite(satellite)==true){
			this.redirectAction.action(this, "saveOk", null, null, null);
		}else{
			this.redirectAction.action(this, "saveError", null, null, null);
		}
		
	}
	
	/** получить объект Satellite 
	 * @param satelliteId - уникальный номер Satellite ( магазина )э
	 * @return Satellite по уникальному коду SatelliteId
	 * */
	private Satellite getSatelliteByKod(Integer satelliteId){
		Satellite returnValue=null;
		if(satelliteId!=null){
			Session session=null;
			try{
				session=((PartnerApplication)this.getApplication()).getHibernateConnection().openSession();
				returnValue=(Satellite)session.get(Satellite.class, satelliteId);
			}catch(Exception ex){
				System.err.println();
				returnValue=null;
			}finally{
				try{
					session.close();
				}catch(Exception ex){};
			}
		}
		return returnValue;
	}

	/** сохранить объект Satellite*/
	private boolean saveSatellite(Satellite satellite){
		boolean returnValue=false;
		Session session=null;
		try{
			session=((PartnerApplication)this.getApplication()).getHibernateConnection().openSession();
			session.beginTransaction();
			session.saveOrUpdate(satellite);
			session.getTransaction().commit();
			returnValue=true;
		}catch(Exception ex){
			returnValue=false;
		}finally{
			try{
				session.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	/** реакция на нажатие кнопки Отменить */
	private void onButtonCancel(){
		// TODO 
		System.out.println("onButtonCancel");
		this.redirectAction.action(this, "saveCancel", null, null, null);
	}
	
	/** прочесть объект Satellite и установить его значения в поля компонентов */
	public void setSatelliteToModel(Integer satelliteId){
		Session session=null;
		try{
			session=((PartnerApplication)this.getApplication()).getHibernateConnection().openSession();
			Satellite currentSatellite=(Satellite)session.get(Satellite.class,satelliteId);
			this.setSatelliteToModel(currentSatellite);
		}catch(Exception ex){
			System.err.println("setSatelliteToModel:"+ex.getMessage());
		}finally{
			try{
				session.close();
			}catch(Exception ex){};
		}
	}
	
	/** загрузить в визуальные поля данные на основании объекта SATELLITE */
	public void setSatelliteToModel(Satellite satellite){
		this.setStringToModel(this.modelName, satellite.getName(),null);
		this.setStringToModel(this.modelUrl, satellite.getUrl(),null);
		this.setStringToModel(this.modelDescription, satellite.getDescription(),null);
		this.setStringToModel(this.modelUrlOk, satellite.getUrlOk(),null);
		this.setStringToModel(this.modelUrlOkMethod, satellite.getUrlOkMethod(),this.methodList.get(0));
		this.setStringToModel(this.modelUrlCancel, satellite.getUrlCancel(),null);
		this.setStringToModel(this.modelUrlCancelMethod, satellite.getUrlCancelMethod(),this.methodList.get(0));
		this.setStringToModel(this.modelUrlError, satellite.getUrlError(),null);
		this.setStringToModel(this.modelUrlErrorMethod, satellite.getUrlErrorMethod(),this.methodList.get(0));
		this.setStringToModel(this.modelParameterKey, satellite.getParameterKey(),null);
		this.setStringToModel(this.modelEMail,satellite.getEMail(),null);
		this.setStringToModel(this.modelPhoneForSms,satellite.getPhoneForSms(),null);
	}
	
	/** получить наполненный данными объект Satellite
	 * @param satellite (nullable)- объект, в который нужно вставить значения из визуальных компонентов 
	 * */
	public Satellite getSatelliteFromVisualComponent(Satellite satellite){
		if(satellite==null){
			satellite=new Satellite();
		}
		satellite.setName(this.getStringFromModel(this.modelName));
		satellite.setUrl(this.getStringFromModel(this.modelUrl));
		satellite.setDescription(this.getStringFromModel(this.modelDescription));
		satellite.setUrlOk(this.getStringFromModel(this.modelUrlOk));
		satellite.setUrlOkMethod(this.getStringFromModel(this.modelUrlOkMethod));
		satellite.setUrlCancel(this.getStringFromModel(this.modelUrlCancel));
		satellite.setUrlCancelMethod(this.getStringFromModel(this.modelUrlCancelMethod));
		satellite.setUrlError(this.getStringFromModel(this.modelUrlError));
		satellite.setUrlErrorMethod(this.getStringFromModel(this.modelUrlErrorMethod));
		satellite.setParameterKey(this.getStringFromModel(this.modelParameterKey));
		satellite.setEMail(this.getStringFromModel(this.modelEMail));
		satellite.setPhoneForSms(this.getStringFromModel(this.modelPhoneForSms));
		return satellite;
	}
	
	
	/** получить строку из модели, или вернуть null */
	private String getStringFromModel(Model model){
		if(model.getObject()!=null){
			try{
				return (String)model.getObject();
			}catch(Exception ex){
				return null;
			}
		}else{
			return null;
		}
	}

	/** установить в модель текстовую строку 
	 * @param model - модель в которую нужно установить значение
	 * @param value - значение, которое нужно установить в модель
	 * @param valueIfNull - значение, которое нужно установить в модель, если value==null
	 */
	private void setStringToModel(Model model, String value,String valueIfNull){
		try{
			if(value==null){
				model.setObject(valueIfNull);
			}else{
				model.setObject(value);
			}
		}catch(Exception ex){
			System.err.println("PanelSettings#setStringToModel Exception:"+ex.getMessage());
		}
	}
}
