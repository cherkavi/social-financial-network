package bonclub.office_private.web_gui.user_area.panel_main.private_data.sells_history;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;
import bonclub.office_private.database.Connector;

/** Личные данные. История покупок*/
public class SellsHistory extends Panel{
	private final static long serialVersionUID=1L;
	private String[] values=new String[2];
	
	/** Личные данные. История покупок 
	 * @param id - уникальный идентификатор для панели 
	 * @param userId - уникальный идентификатор пользователя 
	 * */
	public SellsHistory(String id, Integer userId){
		super(id);
		initData(userId);
		initComponents();
	}

	/** инициализация данных */
	private void initData(Integer userId){
		// INFO !!!Не используется Личные данные. История покупок
		values[0]="";//values[0]="Nemiroff водка";
		values[1]="";//values[1]="ТЦ Фуршет ул. Ленина 116";
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		this.add(new Label("value_1",values[0]));
		this.add(new Label("value_2",values[1]));
	}
	
	@SuppressWarnings("unused")
	private ConnectUtility getConnectUtility(){
		Connector connector=((OfficePrivateApplication)this.getApplication()).getHibernateConnection();
		return new ConnectUtility(connector);
	}
	
}
