package bc.applet.CardManager.manager.actions;

import java.io.Serializable;

import bc.applet.CardManager.manager.actions.calculator.*;
import bc.applet.CardManager.manager.actions.condition.*;
import bc.applet.CardManager.manager.transport.SubCommand;



/** 
 * предназначен для отправки пользователю серийного номера карты 
 * */
public class GetSerialNumber extends Action implements Serializable{
	private static String field_action_name="GetSerialNumber";
	/**
	 * уникальный серийный номер инициализации  
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Получение серийного номера из объекта  
	 */
	public GetSerialNumber(){
		super(field_action_name);
		ActionStep step;

		
		{// step 0
			step=new ActionStep(this);
/*			step.addSubCommand(new SubCommand(99,
					  						  "CONNECTTO",
					  						  "CASTLES EZMINI 0",
					  						  SubCommand.FOR_READER),
					  		   new ConditionStatic(true)
							);
*/
			// SubCommand:0.0
			step.addSubCommand(new SubCommand(0,
											  "SENDCOMMAND",
											  getCardDataApdu,
											  SubCommand.FOR_READER),
							  new ConditionStatic(true)
							  );
/*			step.addSubCommand(new SubCommand(999,
					  		   				  "DISCONNECT",
					  		   				  SubCommand.FOR_READER),
					  		   new ConditionStatic(true)
							   );
*/							   
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
			step.putToStorageFromSubCommand("temp_value", 
									   0, 
									   ActionStep.PARAMETER_TYPE_BYTEARRAY);
				// задать вычислители-калькуляторы 
			step.addCalculator(new CalculatorFromByteArrayToString("temp_value","temp_value2"));
			step.addCalculator(new CalculatorWithoutTailString("temp_value2","temp_value3",4));
			step.addCalculator(new CalculatorDataBaseCheckCard("temp_value3","temp_value4"));
			step.addCalculator(new CalculatorDataBasePutParameter("temp_value2","SERIAL_NUMBER"));
			step.addCalculator(new CalculatorDataBaseGetParameter("SERIAL_NUMBER","temp_value_from_database"));
				// установить команды
				//SubCommand:1.0
			step.addSubCommand(new SubCommand(0,"SHOWMESSAGE",SubCommand.FOR_DISPLAY),
			                   new ConditionParameterExists("temp_value2")
						       );
			
			step.addSubCommand(new SubCommand(3,"SHOWMESSAGE",SubCommand.FOR_DISPLAY),
							   new ConditionParameterExists("temp_value_from_database")
							   );
			step.addSubCommand(new SubCommand(4,"WRITETOLOG",SubCommand.FOR_DISPLAY),
						       new ConditionStatic(true));
			
			// установить динамические параметры для команд
			step.putToSubCommanFromStorage("temp_value3", 0);
			//step.putToSubCommanFromStorage("SESSION_ID", 3);
			step.putToSubCommanFromStorage("temp_value_from_database", 3);

			// установить дополнительную информацию в SubCommand для
			//step.putToSubCommandInformationFromStorage(1, "temp_value4","key1",null);
			//step.putToSubCommandInformationFromStorage(1, null,"key3","default_value");

			step.putToSubCommandInformationFromStorage(1, null,"show","text");
			// добавить текущий шаг в последовательность шагов, которая должна быть выполнена
			this.addActionStep(step);
		}
	}
	
	public String getActionName(){
		return field_action_name;
	}
}
