package bonclub.office_private.web_gui.user_area.panel_main.profile.main;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;
import bonclub.office_private.database.Connector;
import bonclub.office_private.database.wrap.Users;
import bonclub.office_private.database.wrap.UsersParent;
import bonclub.office_private.session.OfficePrivateSession;

/** �������. ������� ������������. */
public class Main extends Panel{
	private final static long serialVersionUID=1L;
	private String[] values=new String[6];
	
	/** �������. ������� ������������.  
	 * @param id - ���������� ������������� ��� ������ 
	 * @param userId - ���������� ������������� ������������ 
	 * */
	public Main(String id, UsersParent user){
		super(id);
		initData(user);
		initComponents();
	}

	/** ������������� ������ */
	private void initData(UsersParent user){
		// INFO �������. ������� ������������. ������� �����
		Integer userId=((OfficePrivateSession)this.getSession()).getCustomerId();
		ConnectUtility connector=this.getConnectUtility();
		try{
			Users customer=(Users)connector.getSession().get(Users.class, userId);
			values[0]=user.getName();
			values[1]=user.getSurname();
			values[2]=user.getFatherName();
			values[3]=(customer.getNick()==null)?"":customer.getNick();
			values[4]=user.getBirthDay();
			values[5]=user.getSex();
		}catch(Exception ex){
			System.err.println("Main#initData");
		}finally{
			connector.close();
		}
	}
	
	/** �������������� ������������� ����������� */
	private void initComponents(){
		this.add(new Label("value_1",values[0]));
		this.add(new Label("value_2",values[1]));
		this.add(new Label("value_3",values[2]));
		this.add(new Label("value_4",values[3]));
		this.add(new Label("value_5",values[4]));
		this.add(new Label("value_6",values[5]));
	}

	private ConnectUtility getConnectUtility(){
		Connector connector=((OfficePrivateApplication)this.getApplication()).getHibernateConnection();
		return new ConnectUtility(connector);
	}
}
