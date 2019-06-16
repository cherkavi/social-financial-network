package bonpay.partner.web_gui.register_information;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;

import bonpay.partner.web_gui.PartnerEnterPoint;

/** ��������, ������� ���������� ������������� ���������� ������ � ���� ��-�� ��������� ������ */
public class RegistrationError extends WebPage{
	private String information=null;
	public RegistrationError(){
		initComponents(null);
	}
	
	public RegistrationError(String information){
		this.setInformation(information);
		initComponents(information);
	}
	
	private void initComponents(String information) {
		if(information!=null){
			// information is null
		}else{
			// information is not null
		}
		this.add(new Label("page_header",this.getHeaderValue()));
		this.add(new Label("error_information",this.getTextInformation()));
		Button buttonOk=new Button("ok"){
			private final static long serialVersionUID=1L;
			@Override
			public void onSubmit() {
				RegistrationError.this.setResponsePage(new PartnerEnterPoint());
			}
		};
		this.add(buttonOk);
	}
	
	
	private String getHeaderValue(){
		return "������ ���������� ������";
	}
	
	/** �������� �������� �������� ��������� ���������� */
	private String getTextInformation(){
		return (information==null)?"������ ���������� ������":information;
	}
	
	/** ���������� �������� �������� ��������� ���������� */
	private void setInformation(String information){
		this.setInformation(information);
	}
}
