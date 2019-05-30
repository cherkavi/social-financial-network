package bc.applet.CardManager.manager.actions;

import java.io.Serializable;

import bc.applet.CardManager.manager.actions.condition.ConditionStatic;
import bc.applet.CardManager.manager.transport.SubCommand;



public class TestAction extends Action implements Serializable{
	private static String field_action_name="TestAction";
	/**
	 * уникальный серийный номер инициализации  
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Получение серийного номера из объекта  
	 */
	public TestAction(){
		super(field_action_name);
		ActionStep step;
		SubCommand command;

		
		{// -------- step 0
			step=new ActionStep(this);
			
			// SubCommand:0.0
			//command=new SubCommand("RESETREADER",SubCommand.FOR_READER);
			
			//command.setParameter(new byte[]{0x00,(byte)0xA4,0x00,0x00,0x023,(byte)0xF00,0x00});
			command=new SubCommand("SENDCOMMAND",SubCommand.FOR_READER);
			command.setParameter(new byte[]{(byte)0x80,(byte)0xE0,0x02,0x00});
			step.addSubCommand(command,
							   new ConditionStatic(true));
			
			command=new SubCommand("SENDCOMMAND",new byte[]{0x00,(byte)0xA4,0x00,0x00,0x02,(byte)0xF0,0x10,0x00},SubCommand.FOR_READER);
			step.addSubCommand(command,
							   new ConditionStatic(true));
			
			this.addActionStep(step);
		}

		{// --------- step 1
			step=new ActionStep(this);
	 
			step.putToStorageFromSubCommand("value", 1, ActionStep.PARAMETER_TYPE_BYTEARRAY);
			//step.putTaskToGetParameter("value_dff1", 3, ActionStep.PARAMETER_TYPE_BYTEARRAY);
				// установить команды
				//SubCommand:1.0
			step.addSubCommand(new SubCommand("SHOWMESSAGE",SubCommand.FOR_DISPLAY),
							   new ConditionStatic(true));
			// установить динамические параметры для команд
			step.putToSubCommanFromStorage("value", 0);

		}
		
		this.addActionStep(step);
	}
	
	public String getActionName(){
		return field_action_name;
	}
}
