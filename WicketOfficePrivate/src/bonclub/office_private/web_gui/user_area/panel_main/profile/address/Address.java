package bonclub.office_private.web_gui.user_area.panel_main.profile.address;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import bonclub.office_private.database.wrap.UsersParent;

/** �������. ������� ������������. �����*/
public class Address extends Panel{
	private final static long serialVersionUID=1L;
	private String[] values=new String[8];
	
	/** �������. ������� ������������. �����
	 * @param id - ���������� ������������� ��� ������ 
	 * @param userId - ���������� ������������� ������������ 
	 * */
	public Address(String id, UsersParent usersParent){
		super(id);
		initData(usersParent);
		initComponents();
	}

	/** ������������� ������ */
	private void initData(UsersParent user){
		// INFO �������. ������� ������������. �������� �����
		values[0]=user.getPostIndex();
		values[1]=user.getOblast();
		values[2]=user.getRegion();
		values[3]=user.getCity();
		values[4]=user.getStreet();
		values[5]=user.getHouse();
		values[6]=user.getFlatNumber();
		values[7]=user.getHousing();
	}
	
	/** �������������� ������������� ����������� */
	private void initComponents(){
		this.add(new Label("value_1",values[0]));
		this.add(new Label("value_2",values[1]));
		this.add(new Label("value_3",values[2]));
		this.add(new Label("value_4",values[3]));
		this.add(new Label("value_5",values[4]));
		this.add(new Label("value_6",values[5]));
		this.add(new Label("value_7",values[6]));
		this.add(new Label("value_8",values[7]));
	}
	
	
}
