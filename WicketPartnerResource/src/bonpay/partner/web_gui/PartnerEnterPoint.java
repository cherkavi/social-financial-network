package bonpay.partner.web_gui;

import org.apache.wicket.markup.html.WebPage;

import bonpay.partner.web_gui.access_panel.AccessPanel;

/** ��������� ��������, ������� ���������� ������ ������� ��� ������������� ������ */
public class PartnerEnterPoint extends WebPage{
	public PartnerEnterPoint(){
		initComponent();
	}
	
	/** �������������� ������������� ����������� */
	private void initComponent(){
		this.getSession().setLocale(this.getRequest().getLocale());
		System.out.println("Locale in request:"+this.getRequest().getLocale().getCountry());
		System.out.println("Locale in request:"+this.getLocale().getCountry());
		this.add(new AccessPanel("access_panel"));
	}
}
