package bonclub.office_private.web_gui.user_area.panel_main.private_data.add_info;

import java.util.Calendar;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import bonclub.office_private.database.wrap.UsersParent;

/** ������ ������. �������������� ���������� */
public class AddInfo extends Panel{
	private final static long serialVersionUID=1L;
	private String[] values=new String[3];
	
	/** ������ ������. �������������� ���������� 
	 * @param id - ���������� ������������� ��� ������ 
	 * @param userId - ���������� ������������� ������������ 
	 * */
	public AddInfo(String id, UsersParent userParent){
		super(id);
		initData(userParent);
		initComponents();
	}

	/** ������������� ������ */
	private void initData(UsersParent userParent){
		// INFO ������ ������.�������������� ����������
		try{
			values[0]=this.getString("year")+": "+(userParent.getTimeUse().get(Calendar.YEAR)-1970)+"   " 
					 +this.getString("month")+": "+userParent.getTimeUse().get(Calendar.MONTH)+"   "
					 +this.getString("day")+": "+userParent.getTimeUse().get(Calendar.DAY_OF_MONTH);
		}catch(Exception ex){
			values[0]="";
		}
	}
	
	/** �������������� ������������� ����������� */
	private void initComponents(){
		this.add(new Label("value_1",values[0]));
	}
	
}
