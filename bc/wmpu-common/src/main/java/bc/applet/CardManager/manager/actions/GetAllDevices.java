package bc.applet.CardManager.manager.actions;

import java.io.Serializable;

import bc.applet.CardManager.manager.actions.calculator.*;
import bc.applet.CardManager.manager.actions.condition.*;
import bc.applet.CardManager.manager.transport.SubCommand;



/** 
 * Данный класс - Action для получения списка всех доступных устройств
 * */
public class GetAllDevices extends Action implements Serializable{
	private static String field_action_name="GetSerialNumber";
	/**
	 * уникальный серийный номер инициализации  
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Получение списка доступных устройств  
	 */
	public GetAllDevices(){
		super(field_action_name);
		ActionStep step;
		{// step 0
			step=new ActionStep(this);
			// SubCommand:0.0
			step.addSubCommand(new SubCommand(0,
											  "GETALLDEVICES",
											  SubCommand.FOR_READER),
							  new ConditionStatic(true)
							  );
			this.addActionStep(step);
		}
		{// step 1
			step=new ActionStep(this);
				// сохранить отработанные значения в промежуточном хранилище, преобразовав byte[] в String 
/*			step.putToStorageFromSubCommand("temp_value", 
									   0, 
									   ActionStep.PARAMETER_TYPE_BYTEARRAY,
									   new ConverterByteArrayToString());
*/									   
			/** кладем параметры в хранилище с именами device_0..device_n*/
			step.putToStorageFromSubCommand("device_", 
									   0, 
									   ActionStep.PARAMETER_TYPE_STRINGARRAY);

				// задать вычислители-калькуляторы 
/*			step.addCalculator(new CalculatorFromByteArrayToString("temp_value","temp_value2"));
			step.addCalculator(new CalculatorWithoutTailString("temp_value2","temp_value3",4));
			step.addCalculator(new CalculatorDataBaseCheckCard("temp_value3","temp_value4"));
*/			
			step.addCalculator(new CalculatorStringToStorage("message_not_found","No Device Found"));
				// установить команды
			// отобразить пользователю список, если найдено хоть одно устройство для чтения
			step.addSubCommand(new SubCommand(0,"SHOWDEVICES",SubCommand.FOR_DISPLAY),
			                   new ConditionParameterExists("device_0")
						       );

			// отобразить сообщение, если не найдно ни одного SmartCardReader-a
			step.addSubCommand(new SubCommand(1,"SHOWMESSAGE",SubCommand.FOR_DISPLAY),
							   new ConditionParameterNotExists("device_0")
							   );
			
			// установить динамические параметры для команд
			step.putToSubCommanFromStorage("message_not_found", 1);
			// установить дополнительную информацию в SubCommand для
			step.putSubCommandInformationFromStorage(0, "device_");

			// добавить текущий шаг в последовательность шагов, которая должна быть выполнена
			this.addActionStep(step);
		}
	}
	
	public String getActionName(){
		return field_action_name;
	}
}
