package bonclub.office_private.web_gui.user_area.panel_main.profile.contact_information;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import bonclub.office_private.database.wrap.UsersParent;

/** �������. ������� ������������. �����*/
public class ContactInformation extends Panel{
	private final static long serialVersionUID=1L;
	private String[] values=new String[3];
	
	/** �������. ������� ������������. �����
	 * @param id - ���������� ������������� ��� ������ 
	 * @param userId - ���������� ������������� ������������ 
	 * */
	public ContactInformation(String id, UsersParent usersParent){
		super(id);
		initData(usersParent);
		initComponents();
	}

	/** ������������� ������ */
	private void initData(UsersParent user){
		// INFO �������. ���������� ����������
		values[0]=user.getPhoneMobile();
		values[1]=user.getPhoneHome();
		values[2]=user.getEmail();
	}
	
	/** �������������� ������������� ����������� */
	private void initComponents(){
		this.add(new Label("value_1",values[0]));
		this.add(new Label("value_2",values[1]));
		this.add(new Label("value_3",values[2]));
	}
	
}
