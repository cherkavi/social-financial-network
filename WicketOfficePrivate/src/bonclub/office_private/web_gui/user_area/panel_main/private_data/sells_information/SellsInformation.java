package bonclub.office_private.web_gui.user_area.panel_main.private_data.sells_information;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;
import bonclub.office_private.database.Connector;

/** ������ ������. ���������� ��������*/
public class SellsInformation extends Panel{
	private final static long serialVersionUID=1L;
	private String[] values=new String[4];
	
	/** ������ ������. ���������� �������� 
	 * @param id - ���������� ������������� ��� ������ 
	 * @param userId - ���������� ������������� ������������ 
	 * */
	public SellsInformation(String id, Integer userId){
		super(id);
		initData(userId);
		initComponents();
	}

	/** ������������� ������ */
	private void initData(Integer userId){
		// INFO !!!�� ������������ ������ ������. ���������� ��������
		values[0]="";//values[0]="�� ������ ��. ������ 116";
		values[1]="";//values[1]="125 ���";
		values[2]="";//values[2]="0 �����";
		values[3]="";//values[3]="5 �����";
	}
	
	/** �������������� ������������� ����������� */
	private void initComponents(){
		this.add(new Label("value_1",values[0]));
		this.add(new Label("value_2",values[1]));
		this.add(new Label("value_3",values[2]));
		this.add(new Label("value_4",values[3]));
	}

	@SuppressWarnings("unused")
	private ConnectUtility getConnectUtility(){
		Connector connector=((OfficePrivateApplication)this.getApplication()).getHibernateConnection();
		return new ConnectUtility(connector);
	}
	
}
