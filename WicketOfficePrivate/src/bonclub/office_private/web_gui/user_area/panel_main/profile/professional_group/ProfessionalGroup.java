package bonclub.office_private.web_gui.user_area.panel_main.profile.professional_group;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import bonclub.office_private.database.wrap.UsersParent;

/** Профиль. Профиль пользователя. Адрес*/
public class ProfessionalGroup extends Panel{
	private final static long serialVersionUID=1L;
	private String[] values=new String[1];
	
	/** Профиль. Профиль пользователя. Адрес
	 * @param id - уникальный идентификатор для панели 
	 * @param userId - уникальный идентификатор пользователя 
	 * */
	public ProfessionalGroup(String id, UsersParent usersParent){
		super(id);
		initData(usersParent);
		initComponents();
	}

	/** инициализация данных */
	private void initData(UsersParent usersParent){
		// INFO Профиль. Профессиональная группа
		values[0]=usersParent.getProf();
	}
	
	/** первоначальная инициализация компонентов */
	private void initComponents(){
		this.add(new Label("value_1",values[0]));
	}
	
}
