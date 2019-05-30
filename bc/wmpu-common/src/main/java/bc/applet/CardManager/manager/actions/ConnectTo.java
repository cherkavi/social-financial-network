package bc.applet.CardManager.manager.actions;

import java.io.Serializable;

import bc.applet.CardManager.manager.actions.calculator.*;
import bc.applet.CardManager.manager.actions.condition.*;
import bc.applet.CardManager.manager.transport.SubCommand;



/** 
 * Данный класс - Action для подключения устройства по его имени
 * 1 - прием команды на подключение (прием уникального имени устройства ) <br>
 * 2 - послать команду на подключение с уникальным именем устройства
 * 3 - принять ответ с именем подключенного устройства, либо же принять "" (пустую строку)
 * */
public class ConnectTo extends Action implements Serializable{
	private static String field_action_name="ConnectTo";
	/**
	 * уникальный серийный номер инициализации  
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Получение списка доступных устройств  
	 */
	public ConnectTo(){
		super(field_action_name);
		// ---- ALGORITHM FOR STEP ----
		// -- при инициализации Action - положить все Transport.Information в хранилище 
		// putTaskToGetParameter               : положить данные из SubCommand в хранилище
		// --ENGINE--                          : очитстить команды в списке
		// addCalculator                       : отработать все калькуляторы
		// addSubCommand                       : задать новые команды для клиента	
		// putParameterDynamicSet              : задать динамические параметры для обработок
		// putSubCommandInformationFromStorage : скопировать значения для всех переменных из Action.Data в SubCommand.Information
		// ----------------------------
		ActionStep step;
		{// step 0
			step=new ActionStep(this);
			// DEVICE уже находится в хранилище
			// SubCommand:0.0
			step.addSubCommand(new SubCommand(0,
											  "CONNECTTO",
											  SubCommand.FOR_READER),
							  new ConditionStatic(true)
							  );
			// установить параметр для SubCommand из хранилища
			step.putToSubCommanFromStorage("DEVICE", 0);
			
			this.addActionStep(step);
		}
		{// step 1
			step=new ActionStep(this);
				 // сохранить полученное имя подключенного устройства, или сохранить пустую строку, или null
			step.putToStorageFromSubCommand("connected_device", 
									   0, 
									   ActionStep.PARAMETER_TYPE_STRING);

				// задать вычислители-калькуляторы
/*			step.addCalculator(new CalculatorFromByteArrayToString("temp_value","temp_value2"));
			step.addCalculator(new CalculatorWithoutTailString("temp_value2","temp_value3",4));
			step.addCalculator(new CalculatorDataBaseCheckCard("temp_value3","temp_value4"));
*/			
			step.addCalculator(new CalculatorStringToStorage("message_error","Not Connected"));
				// установить команды
			// отобразить пользователю список, если найдено хоть одно устройство для чтения
			//step.addSubCommand(new SubCommand(0,"SHOWMENU",SubCommand.FOR_DISPLAY),
			step.addSubCommand(new SubCommand(0,"SHOWMENU",SubCommand.FOR_DISPLAY),
			                   new ConditionParameterAndParameterEquals("DEVICE","connected_device",ConditionParameterAndParameterEquals.TYPE_STRING)
						       );
			// отобразить сообщение, если не найдно ни одного SmartCardReader-a
			step.addSubCommand(new SubCommand(1,"SHOWMESSAGE",SubCommand.FOR_DISPLAY),
							   new ConditionParameterAndParameterNotEquals("DEVICE","connected_device",ConditionParameterAndParameterEquals.TYPE_STRING)
							   );
			
			// установить динамические параметры для команд
			step.putToSubCommanFromStorage("connected_device", 0);
			step.putToSubCommanFromStorage("message_error", 1);

			// добавить текущий шаг в последовательность шагов, которая должна быть выполнена
			this.addActionStep(step);
		}
	}
	
	public String getActionName(){
		return field_action_name;
	}
}
