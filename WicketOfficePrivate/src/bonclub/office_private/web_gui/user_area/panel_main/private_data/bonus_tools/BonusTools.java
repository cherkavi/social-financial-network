package bonclub.office_private.web_gui.user_area.panel_main.private_data.bonus_tools;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import bonclub.office_private.database.wrap.UsersParent;

/** Личные данные. Бонусные средства*/
public class BonusTools extends Panel{
	private final static long serialVersionUID=1L;
	private String[] values=new String[2];
	
	/** Личные данные. Бонусные средства 
	 * @param id - уникальный идентификатор для панели 
	 * @param userId - уникальный идентификатор пользователя 
	 * */
	public BonusTools(String id, UsersParent usersParent){
		super(id);
		initData(usersParent);
		initComponents();
	}

	/** инициализация данных */
	private void initData(UsersParent usersParent){
		// INFO Личные данные.Бонусные средства
		values[0]=usersParent.getBonAviable();//Доступно бонов
		values[1]=usersParent.getBonStorage();//Накоплено бонов 
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		this.add(new Label("value_1",values[0]));
		this.add(new Label("value_2",values[1]));
	}
	
	
}
