package bonclub.office_private.web_gui.user_area.panel_main.private_data.bonus_tools;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import bonclub.office_private.database.wrap.UsersParent;

/** ������ ������. �������� ��������*/
public class BonusTools extends Panel{
	private final static long serialVersionUID=1L;
	private String[] values=new String[2];
	
	/** ������ ������. �������� �������� 
	 * @param id - ���������� ������������� ��� ������ 
	 * @param userId - ���������� ������������� ������������ 
	 * */
	public BonusTools(String id, UsersParent usersParent){
		super(id);
		initData(usersParent);
		initComponents();
	}

	/** ������������� ������ */
	private void initData(UsersParent usersParent){
		// INFO ������ ������.�������� ��������
		values[0]=usersParent.getBonAviable();//�������� �����
		values[1]=usersParent.getBonStorage();//��������� ����� 
	}
	
	/** �������������� ������������� ����������� */
	private void initComponents(){
		this.add(new Label("value_1",values[0]));
		this.add(new Label("value_2",values[1]));
	}
	
	
}
