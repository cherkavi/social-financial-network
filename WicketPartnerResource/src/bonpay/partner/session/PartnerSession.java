package bonpay.partner.session;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;
import org.hibernate.criterion.Restrictions;

import bonpay.partner.PartnerApplication;

import bonpay.partner.database.wrap.*;

public class PartnerSession extends WebSession{
	private static final long serialVersionUID = 1L;
	/** уникальный идентификатор партнера */
	private Integer partnerId;
	/** ответ от предыдущей страницы */
	private PageExchange pageExchange;
	
	/** вывод отладочной информации*/
	private void Debug(Object information){
		System.out.print("Session ");
		System.out.print("DEBUG");
		System.out.println(information);
	}

	/** вывод ошибочной информации */
	private void Error(Object information){
		System.err.println("Session ");
		System.err.println("ERROR ");
		System.err.println(information);
	}
	
	
	public PartnerSession(Request request) {
		super(request);
		initFields();
	}

	/** инициализация всех полей объекта в Default значения */
	private void initFields(){
		partnerId=null;
	}
	
	/** возвратить уникальный идентификатор партнера */
	public Integer getPartnerId(){
		return partnerId;
	}
	
	/** получить значение объекта для обмена между страницами */
	public PageExchange getPageExchange() {
		return pageExchange;
	}

	/** установить значение объекта для обмена между страницами */
	public void setPageExchange(PageExchange pageExchange) {
		this.pageExchange = pageExchange;
	}

	/** очистить объект обмена между страницами */
	public void clearPageExchange(){
		this.pageExchange.clearPageValue();
	}

	/** установить PartnerId 
	 * @param partnerId - номер партнера 
	 * */
	public Integer setPartnerId(Integer partnerId){
		this.partnerId=partnerId;
		return this.partnerId;
	}
	
	/** очистить уникальный идентификатор партнера */
	public void clearPartnerId(){
		this.partnerId=null;
	}
	
	/** установить на основании логина и пароля уникальный идентификатор партнера, и вернуть его, либо вернуть null,
	 * если данный идентификатор не установлен
	 * @param login - логин
	 * @param password - пароль
	 * @return null - партнер не установлен<br>
	 * Integer - партнер распознан и установлен 
	 * */
	public Integer setPartnerId(String login, String password){
		this.partnerId=null;
		org.hibernate.Session session=null;
		try{
			session=((PartnerApplication)this.getApplication()).getHibernateConnection().openSession();
			Object object=(Partner)session.createCriteria(Partner.class).add(Restrictions.eq("email", login)).add(Restrictions.eq("password",password)).uniqueResult();
			if(object!=null){
				this.partnerId=((Partner)object).getId();
				Debug("partner is recognized:"+this.partnerId);
			}else{
				// partner is not fined
				Debug("partner is not recognized: Login:"+login+"   Password:"+password);
			}
		}catch(Exception ex){
			Error("#setParameterId:"+ex.getMessage());
		}finally{
			try{
				session.close();
			}catch(Exception ex){};
		}
		
		return partnerId;
	}
}
