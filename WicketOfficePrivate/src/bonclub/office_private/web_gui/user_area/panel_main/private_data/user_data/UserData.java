package bonclub.office_private.web_gui.user_area.panel_main.private_data.user_data;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import bonclub.office_private.database.wrap.UsersParent;

/** ������ ������. ������ ������������ */
public class UserData extends Panel{
	private final static long serialVersionUID=1L;
	private String[] values=new String[5];
	
	/** ������ ������. ������ ������������ 
	 * @param id - ���������� ������������� ��� ������ 
	 * @param userId - ���������� ������������� ������������ 
	 * */
	public UserData(String id, UsersParent usersParent){
		super(id);
		initData(usersParent);
		initComponents();
	}

	/** ������������� ������ */
	private void initData(UsersParent usersParent){
		// INFO ������ ������.������ ������������
		values[0]=usersParent.getFullName();       // values[0]="���������� �������";
		values[1]=usersParent.getBoncardNumber();  // ����� ���-����� 
		values[2]=usersParent.getClub();		   // ���� 
		values[3]=usersParent.getBonCategory();	   // ��������� ����� (����)
		values[4]=usersParent.getDiscontCategory();// ��������� ����� (������)
	}

	
	
	/** �������������� ������������� ����������� */
	private void initComponents(){
		this.add(new Label("value_1",values[0]));
		this.add(new Label("value_2",values[1]));
		this.add(new Label("value_3",values[2]));
		this.add(new Label("value_4",values[3]));
		this.add(new Label("value_5",values[4]));
	}
	
	
}
