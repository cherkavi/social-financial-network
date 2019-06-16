package bonclub.office_private.web_gui.user_area.panel_messenger;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/** ������, ��� ����������� ��������� ��� ������������ */
public class MessengerPanelHello extends Panel{
	private final static long serialVersionUID=1L;
	private Model<String> labelMainModel=new Model<String>();
	
	/** ������, ��� ����������� ��������� ��� ������������ 
	 * @param panelId - ���������� ������������� ������ 
	 * @param message - ���������, ������� ����� ������� �� ����� ��� ������������ 
	 */
	public MessengerPanelHello(String panelId, String message){
		super(panelId);
		String messageForShow=null;
		if(message==null){
			// ���������� ����������
			labelMainModel.setObject(this.getString("label_hello"));
		}else{
			messageForShow=message;
			labelMainModel.setObject(message);
		}
		
		initComponents(messageForShow);
	}
	
	/** ���������� ����� ��������� �� ������ */
	public void setMessage(String message){
		if(message==null){
			this.labelMainModel.setObject("");
		}else{
			this.labelMainModel.setObject(message);
		}
	}
	
	/** ������������� ���� ���������� ����������� */
	private void initComponents(String message){
		this.add(new Label("label_hello",labelMainModel));
	}
	
}
