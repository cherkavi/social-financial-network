package bonclub.office_private.web_gui.user_area.panel_main.profile.professional_group;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import bonclub.office_private.database.wrap.UsersParent;

/** �������. ������� ������������. �����*/
public class ProfessionalGroup extends Panel{
	private final static long serialVersionUID=1L;
	private String[] values=new String[1];
	
	/** �������. ������� ������������. �����
	 * @param id - ���������� ������������� ��� ������ 
	 * @param userId - ���������� ������������� ������������ 
	 * */
	public ProfessionalGroup(String id, UsersParent usersParent){
		super(id);
		initData(usersParent);
		initComponents();
	}

	/** ������������� ������ */
	private void initData(UsersParent usersParent){
		// INFO �������. ���������������� ������
		values[0]=usersParent.getProf();
	}
	
	/** �������������� ������������� ����������� */
	private void initComponents(){
		this.add(new Label("value_1",values[0]));
	}
	
}
