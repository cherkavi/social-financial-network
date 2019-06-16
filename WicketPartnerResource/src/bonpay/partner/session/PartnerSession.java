package bonpay.partner.session;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;
import org.hibernate.criterion.Restrictions;

import bonpay.partner.PartnerApplication;

import bonpay.partner.database.wrap.*;

public class PartnerSession extends WebSession{
	private static final long serialVersionUID = 1L;
	/** ���������� ������������� �������� */
	private Integer partnerId;
	/** ����� �� ���������� �������� */
	private PageExchange pageExchange;
	
	/** ����� ���������� ����������*/
	private void Debug(Object information){
		System.out.print("Session ");
		System.out.print("DEBUG");
		System.out.println(information);
	}

	/** ����� ��������� ���������� */
	private void Error(Object information){
		System.err.println("Session ");
		System.err.println("ERROR ");
		System.err.println(information);
	}
	
	
	public PartnerSession(Request request) {
		super(request);
		initFields();
	}

	/** ������������� ���� ����� ������� � Default �������� */
	private void initFields(){
		partnerId=null;
	}
	
	/** ���������� ���������� ������������� �������� */
	public Integer getPartnerId(){
		return partnerId;
	}
	
	/** �������� �������� ������� ��� ������ ����� ���������� */
	public PageExchange getPageExchange() {
		return pageExchange;
	}

	/** ���������� �������� ������� ��� ������ ����� ���������� */
	public void setPageExchange(PageExchange pageExchange) {
		this.pageExchange = pageExchange;
	}

	/** �������� ������ ������ ����� ���������� */
	public void clearPageExchange(){
		this.pageExchange.clearPageValue();
	}

	/** ���������� PartnerId 
	 * @param partnerId - ����� �������� 
	 * */
	public Integer setPartnerId(Integer partnerId){
		this.partnerId=partnerId;
		return this.partnerId;
	}
	
	/** �������� ���������� ������������� �������� */
	public void clearPartnerId(){
		this.partnerId=null;
	}
	
	/** ���������� �� ��������� ������ � ������ ���������� ������������� ��������, � ������� ���, ���� ������� null,
	 * ���� ������ ������������� �� ����������
	 * @param login - �����
	 * @param password - ������
	 * @return null - ������� �� ����������<br>
	 * Integer - ������� ��������� � ���������� 
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
