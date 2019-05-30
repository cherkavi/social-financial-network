package bc.applet.CardManager.manager.actions;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Класс, который отвечает за статические данные <br>
 * (уникальный имена для Action и полные имена классов) <br>
 * и создает объекты классов-потомков Action, согласно этим данным 
 * @author cherkashinv
 */
public class ActionBuilder {
	private final static Logger LOGGER=Logger.getLogger(ActionBuilder.class);
	
	/** объект, который содержит все уникальные имена Action*/
	private static final ArrayList<String> field_action_name=new ArrayList<String>();
	/** объект, который содержит все имена классов для Action */
	private static final ArrayList<String> field_action_class=new ArrayList<String>();

	static{
		// TODO Action.Name: Server Место для статической инициализации классов-потомков manager.actions.Action
		addAction("GetSerialNumber","manager.actions.GetSerialNumber");
		addAction("TestAction","manager.actions.TestAction");
		addAction("GetAllDevices","manager.actions.GetAllDevices");
		addAction("ConnectTo","manager.actions.ConnectTo");
	}
	
	/** 
	 * метод, который должен быть вызван потомком для регистрации уникального имени в объекте
	 * @param name
	 */
	protected static void addAction(String name,
									String class_name){
		try{
			/*
			if(field_action_name==null){
				field_action_name=new ArrayList<String>();
			};
			if(field_action_class==null){
				field_action_class=new ArrayList<String>();
			}
			*/
			//LOGGER.debug("ActionBuilder.addAction Class: "+class_name);
			//LOGGER.debug("ActionBuilder.addAction: Name: "+name);
			field_action_class.add(class_name);
			field_action_name.add(name);
			//LOGGER.debug("Name.size():"+field_action_name.size()+"    ClassName.size():"+field_action_class.size());
		}catch(Exception ex){
			//ex.printStackTrace();
			LOGGER.error("ActionBuilder.addAction: Exception:"+ex.getMessage());
		}
	}
	
	/** 
	 * получить индекс Action по его уникальному имени, которое было зарегестрировано
	 */
	private static int getActionNameIndex(String name){
		//LOGGER.debug("All elements:"+field_action_name.size());
		//LOGGER.debug("ActionBuilder: getActionNameIndex:"+name);
		return field_action_name.indexOf(name);
	}
	
	/** 
	 * @return возвращает положительный результат, если переданное имя найдено среди зарегистрированных потомков 
	 */
	public static boolean isActionNameExists(String name){
		//LOGGER.debug("Action: isActionNameExists:"+getActionNameIndex(name));
		return (getActionNameIndex(name)>=0);
	}
	/** 
	 * @return возвращает класс Action по его имени
	 */
	public static Action getActionClassByName(String name){
		//LOGGER.debug("ActionBuilder: getActionClassByName:"+name);
		int index=getActionNameIndex(name);
		if(index>=0){
			//LOGGER.debug("ActionBuilder: getActionClassByName: founded");
			try{
				return (Action)Class.forName((String) field_action_class.get(index)).newInstance();
			}catch(Exception ex){
				return null;
			}
			
		}else{
			//LOGGER.debug("ActionBuilder: getActionClassByName: ActionName not found:"+name);
			return null;
		}
	}
	
}
