package bonclub.office_private.web_gui.user_area.panel_main.private_data.user_data;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import bonclub.office_private.database.wrap.UsersParent;

/** Личные данные. Данные пользователя */
public class UserData extends Panel{
	private final static long serialVersionUID=1L;
	private String[] values=new String[5];
	
	/** Личные данные. Данные пользователя 
	 * @param id - уникальный идентификатор для панели 
	 * @param userId - уникальный идентификатор пользователя 
	 * */
	public UserData(String id, UsersParent usersParent){
		super(id);
		initData(usersParent);
		initComponents();
	}

	/** инициализация данных */
	private void initData(UsersParent usersParent){
		// INFO Личные данные.Данные пользователя
		values[0]=usersParent.getFullName();       // values[0]="Прокопьева Надежда";
		values[1]=usersParent.getBoncardNumber();  // номер бон-карты 
		values[2]=usersParent.getClub();		   // Клуб 
		values[3]=usersParent.getBonCategory();	   // Категория карты (Боны)
		values[4]=usersParent.getDiscontCategory();// Категория карты (Скидки)
	}

	
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		this.add(new Label("value_1",values[0]));
		this.add(new Label("value_2",values[1]));
		this.add(new Label("value_3",values[2]));
		this.add(new Label("value_4",values[3]));
		this.add(new Label("value_5",values[4]));
	}
	
	
}
