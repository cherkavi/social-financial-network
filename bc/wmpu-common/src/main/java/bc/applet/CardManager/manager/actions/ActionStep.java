package bc.applet.CardManager.manager.actions;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import bc.applet.CardManager.manager.actions.calculator.ActionStepCalculator;
import bc.applet.CardManager.manager.actions.condition.ActionStepCondition;
import bc.applet.CardManager.manager.actions.converter.ActionStepConverter;
import bc.applet.CardManager.manager.transport.SubCommand;
import bc.applet.CardManager.manager.transport.Transport;


/** данный класс является минимальным шагом по обмену с клиентом: <br>  
 *  анализ входящего пакета
 *  выдать ответ, согласно текущего шага
 */
public class ActionStep implements Serializable{
	private final static Logger LOGGER=Logger.getLogger(ActionStep.class);
	/** SubCommand.getParameter()*/
	public final static int PARAMETER_TYPE_BYTEARRAY=0;
	/** SubCommand.getParameterString */
	public static final int PARAMETER_TYPE_STRING=1;
	/** SubCommand.getParameterInt() */
	public static final int PARAMETER_TYPE_INT=2;
	/** SubCommand.getInformationKeys[] SubCommand.getInformationValues[] */
	public static final int PARAMETER_TYPE_STRINGARRAY=3;
	/** уникальный номер для сериализации */
	private static final long serialVersionUID = 1L;
	/** объект, в котором хранятся все команды, которые необходимо выполнить по данному шагу*/
	private ArrayList<SubCommand> field_sub_command=new ArrayList<SubCommand>();
	
	/** объект, в котором хранятся все условия (Condition), которые соответствуют командам */
	private ArrayList<ActionStepCondition> field_condition=new ArrayList<ActionStepCondition>();
	
	/** объект, в котором хранятся все вычислители (Calculator), которые должны быть обработаны перед отправкой пользователю */
	private ArrayList<ActionStepCalculator> field_calculator=new ArrayList<ActionStepCalculator>();
	
	/** Объект Action в контексте которого находится данный ActionStep*/
	Action field_current_action=null;

	/** Объект, который хранит данные о необходимости сохранения промежуточных значений 
	 * получить из SubCommand 
	 */
	private ArrayList<ParameterKey> field_parameter_for_get=new ArrayList<ParameterKey>();

	/** Объект, который хранит данные о необходимости установки параметров в промежуточные значения SubCommand*/
	private ArrayList<ParameterSet> field_parameter_for_set=new ArrayList<ParameterSet>();
	
	/** Объект, который хранит данные о необходимости добавления в SubCommand.Information одного значения из хранилища Action.Data*/
	private ArrayList<ParameterInformationPut> field_parameter_information=new ArrayList<ParameterInformationPut>();
	
	/** счетчик, который служит прообразом Iterator для Enumeration - внутренний указатель на текущую команду*/
	private int field_sub_command_cursor=(-1);
	
	public ActionStep(Action action){
		this.field_current_action=action;
	}
	
	/** добавить SubCommand в список необходимых команд по данному шагу */
	public void addSubCommand(SubCommand command,ActionStepCondition condition){
		this.field_sub_command.add(command);
		this.field_condition.add(condition);
	}
	
	/** <b>ENGINE</b>
	 * <br> 
	 * перевести указатель на следующую итерацию Action, счетчик SubCommand в начальное положение
	 */
	public void resetCursorSubCommand(){
		this.field_sub_command_cursor=(-1);
		
	}

	/** проверить существование следующей SubCommand в списке */
	public boolean hasNextSubCommand(){
		boolean return_value=false;
		// может ли производиться поиск, или сразу вернуть отрицательный результат
		// есть ли еще команды, которые следуют за текущим указателем
		if((this.field_sub_command_cursor+1)<this.field_sub_command.size()){
			int temp_cursor=this.field_sub_command_cursor+1;
			while(temp_cursor<this.field_sub_command.size()){
				if(this.field_condition.get(temp_cursor).isCondition(field_current_action)){
					return_value=true;
					break;
				}
				temp_cursor++;
			}
		}
		return return_value;
	}
	
	/** получить следующую SubCommand из списка */
	public SubCommand nextSubCommand(){
		SubCommand return_value=null;
		this.field_sub_command_cursor++;
		if(this.field_sub_command_cursor<this.field_sub_command.size()){
			int temp_cursor=this.field_sub_command_cursor;
			while(temp_cursor<this.field_sub_command.size()){
				if(this.field_condition.get(temp_cursor).isCondition(field_current_action)){
					return_value=this.field_sub_command.get(temp_cursor);
					this.field_sub_command_cursor=temp_cursor;
					break;
				}
				temp_cursor++;
			}
			
		}
		return return_value;
	}
	
	/** <b>положить в хранилище (по заданному имени) объект из SubCommand </b>
	 * <br>
	 * кладет задание в объект, чтобы потом вытащить и занести его во время выполнения в объект Action на данном шаге выполнения  
	 * @param parameter_name - имя создаваемого параметра
	 * @param sub_command_index - уникальный индекс SubCommand в контексте ActionStep, из которого будет выниматься данный параметр
	 * @param PARAMETER_TYPE - тип параметра, который кладем в виде параметра <br><br> 
	 * если PARAMETER_TYPE==PARAMETER_TYPE_STRINGARRAY тогда: <br>
	 * @param parameter_name - указывает на начальное имя параметров, которые будут положены в хранилище с ключом [parameter_name][counter]
	 * <br>
	 * если @param parameter_name==null || =="" тогда в хранилище будут положены все переданные значения в виде (SubCommand.InformationKey) (SubCommand.InformationValue)
	 * */
	public void putToStorageFromSubCommand(String parameter_name, int sub_command_index,int PARAMETER_TYPE){
		this.field_parameter_for_get.add(new ParameterKey(parameter_name,sub_command_index, PARAMETER_TYPE));
	}
	
	/** <b>положить в хранилище (по заданному имени) объект из SubCommand </b>
	 * <br>
	 * кладет задание в объект, чтобы потом вытащить и занести его во время выполнения в объект Action на данном шаге выполнения  
	 * @param parameter_name - имя создаваемого параметра
	 * @param sub_command_index - уникальный индекс SubCommand в контексте ActionStep, из которого будет выниматься данный параметр
	 * @param PARAMETER_TYPE - тип параметра, который кладем в виде параметра
	 * @param converter - необходимый преобразователь для данных
	 * если PARAMETER_TYPE==PARAMETER_TYPE_STRINGARRAY тогда: <br>
	 * @parm converter игнорируется 
	 * @param parameter_name - указывает на начальное имя параметров, которые будут положены в хранилище с ключом [parameter_name][counter]
	 * <br>
	 * если @param parameter_name==null || =="" тогда в хранилище будут положены все переданные значения в виде (SubCommand.InformationKey) (SubCommand.InformationValue)
	 * 
	 * */
	public void putTaskToGetParameter(String parameter_name, int sub_command_index,int PARAMETER_TYPE,ActionStepConverter converter){
		this.field_parameter_for_get.add(new ParameterKey(parameter_name,sub_command_index, PARAMETER_TYPE,converter));
	}
	
	/** получить параметр из SubCommand и добавить его в Action.Parameter (хранилище промежуточных переменных)
	 * @param transport текущий Transport, который содержит ответ от клиента, и из него нужно получать по уникальным кодам переданные клиентом параметры
	 * @param parameter_name - имя для данного параметра
	 * @param sub_command_index - уникальный индекс SubCommand из которого нужно вынимать данный параметр
	 * @param PARAMETER_TYPE тип параметра, который нужно взять из SubCommand и положить в Промежуточное хранилище Action.Parameter
	 * @param converter - если !=null то нужно применить его к получаемому значению ( сделать преобразование )
	 */
	private void putParameterToAction(Transport transport, 
									  String parameter_name, 
									  int sub_command_index,
									  int PARAMETER_TYPE, 
									  ActionStepConverter converter){
		try{
			LOGGER.debug("command for get Value exists ");
			if(PARAMETER_TYPE==PARAMETER_TYPE_BYTEARRAY){
				if(converter==null){
					this.field_current_action.putParameter(parameter_name, 
							   transport.getSubCommandByUniqueIndex(sub_command_index).getParameter()
							   );
				}else{
					try{
						this.field_current_action.putParameter(parameter_name, 
								   							   converter.convert(transport.getSubCommandByUniqueIndex(sub_command_index).getParameter())
								                               );
						LOGGER.debug("putParameterToAction byte[] OK");
					}catch(Exception ex){
						LOGGER.error("putParameterToAction byte[] Error:"+ex.getMessage());
					}
				}
			}else 
			if(PARAMETER_TYPE==PARAMETER_TYPE_STRING){
				if(converter==null){
					this.field_current_action.putParameter(parameter_name, 
														   transport.getSubCommandByUniqueIndex(sub_command_index).getParameterString()
													       );
				}else{
					try{
						this.field_current_action.putParameter(parameter_name, 
								   							   converter.convert(transport.getSubCommandByUniqueIndex(sub_command_index).getParameterString())
							       							   );
						LOGGER.debug("putParameterToAction String OK");
					}catch(Exception ex){
						LOGGER.error("putParameterToAction String Error:"+ex.getMessage());
					}
				}
			}else
			if(PARAMETER_TYPE==PARAMETER_TYPE_INT){
				if(converter==null){
					this.field_current_action.putParameter(parameter_name, 
							   							   new Integer(transport.getSubCommandByUniqueIndex(sub_command_index).getParameterInt())
							   							   );
				}else{
					try{
						this.field_current_action.putParameter(parameter_name, 
	   							   converter.convert(new Integer(transport.getSubCommandByUniqueIndex(sub_command_index).getParameterInt()))
	   							   );
						LOGGER.debug("putParameterToAction Integer OK");
					}catch(Exception ex){
						LOGGER.error("putParameterToAction Integer Error:"+ex.getMessage());
					}
				}
			}else
			if(PARAMETER_TYPE==PARAMETER_TYPE_STRINGARRAY){
				// возможно, могут понадобиться конвертеры для StringArray
				if((parameter_name==null)||(parameter_name.equals(""))){
					// нужно заносить в хранилище <key> <value> из <SubCommand.InformationKey> <SubCommand.InformationValue>
					for(int counter=0;counter<transport.getSubCommandByUniqueIndex(sub_command_index).getInformationCount();counter++){
						this.field_current_action.putParameter(transport.getSubCommandByUniqueIndex(sub_command_index).getInformationKey(counter), 
															   transport.getSubCommandByUniqueIndex(sub_command_index).getInformationValue(counter));
						LOGGER.debug(transport.getSubCommand(sub_command_index).getInformationKey(counter)+"  >>>  "+transport.getSubCommand(sub_command_index).getInformationValue(counter));
					}
				}else{
					// нужно заносить в хранилище <key> <value> из <parameter_name>[counter] <SubCommand.InformationValue>
					for(int counter=0;counter<transport.getSubCommandByUniqueIndex(sub_command_index).getInformationCount();counter++){
						this.field_current_action.putParameter(parameter_name+(new Integer(counter)).toString(), 
															   transport.getSubCommandByUniqueIndex(sub_command_index).getInformationValue(counter));
						LOGGER.debug(parameter_name+(new Integer(counter)).toString()+"  >>>  "+transport.getSubCommand(sub_command_index).getInformationValue(counter));
					}
				}
				
			}
		}catch(Exception ex){
			ex.printStackTrace();
			LOGGER.error("putParameterToAction Exception:"+ex.getMessage());
		};
	}
	
	/** метод, который пробегает по всем поставленным задачам на занесение всех промежуточных значений в объект Action */
	public void getAllParameter(Transport transport){
		for(int counter=0;counter<this.field_parameter_for_get.size();counter++){
			this.putParameterToAction(transport,
									  this.field_parameter_for_get.get(counter).getParameterName(), 
									  this.field_parameter_for_get.get(counter).getCommandIndex(), 
									  this.field_parameter_for_get.get(counter).getCommandParameterType(),
									  this.field_parameter_for_get.get(counter).getConverter()
									  );
		}
	}

	
	/** <b>взять параметр из хранилища и положить его в SubCommand</b>
	 * <br>
	 * метод, который устанавливает задачу для динамической установки параметра
	 * @param parameter_name имя вынимаемого параметра
	 * @param index_sub_command индекс SubCommand в который кладется данный параметр
	 * */
	public void putToSubCommanFromStorage(String parameter_name,int index_sub_command){
		this.field_parameter_for_set.add(new ParameterSet(parameter_name,index_sub_command));
	}
	
	/** метод, который устанавливает в SubCommand все необходимые данные из промежуточных переменных*/
	private void setParameterDynamicIntoSubCommand(Transport transport, ParameterSet value){
		// TODO --- попытаться просто класть параметр Object без преобразования
		try{
			if(this.field_current_action.getParameter(value.getParameterName()) instanceof byte[]){
				transport.getSubCommandByUniqueIndex(value.getParameterSubCommandIndex()).setParameter(
																					 (byte[])this.field_current_action.getParameter(value.getParameterName())
																					 );
			}else
				if(this.field_current_action.getParameter(value.getParameterName()) instanceof String ){
					transport.getSubCommandByUniqueIndex(value.getParameterSubCommandIndex()).setParameterString(
																							   (String)this.field_current_action.getParameter(value.getParameterName())
																							   );
				}else
					if(this.field_current_action.getParameter(value.getParameterName()) instanceof Integer ){
						transport.getSubCommandByUniqueIndex(value.getParameterSubCommandIndex()).setParameterInt(
																								((Integer)this.field_current_action.getParameter(value.getParameterName())).intValue()
																								);
					};
		}catch(Exception ex){
			LOGGER.error("setParameterDynamicIntoSubCommand Exception:"+ex.getMessage());
		}
	}
	/**  
	 * <b> ENGINE </b>
	 * <br>
	 * метод, который устанавливает динамические параметры во все заявленные SubCommand
	 * @param parameter_name имя вынимаемого параметра
	 * @param index_sub_command индекс SubCommand в который кладется данный параметр
	 * */
	public void setAllParameterDynamicIntoSubCommand(Transport transport){
		for(int counter=0;counter<this.field_parameter_for_set.size();counter++){
			setParameterDynamicIntoSubCommand(transport, this.field_parameter_for_set.get(counter));
		}
	}
	
	/**<b>ENGINE</b>
	 * <br>
	 * метод, который вычисляет все дополнительные промежуточные значения 
	 * на основании полученных от пользователия и/или промежуточных значений из базы данных
	 */
	public void initAllCalculator(){
		try{
			for(int counter=0;counter<this.field_calculator.size();counter++){
				this.field_calculator.get(counter).calculate(field_current_action);
			}
		}catch(Exception ex){
			LOGGER.error("initCalculator: Exception:"+ex.getMessage());
		}
	}
	/** 
	 * метод, который добавляет калькулятор (вычислитель) в порядок задач
	 */
	public void addCalculator(ActionStepCalculator calculator){
		this.field_calculator.add(calculator);
	}
	/**<b>взять все ключи из хранилища, которые начинаются на (storage_name_begin) и положить в SubCommand </b>
	 * <br>
	 * метод, который добавляет еще одну задачу в список по установке в SubCommand.Information 
	 * значения из хранилища Action.Data 
	 * @param subcommand_unique_index - уникальный индекс SubCommand в пакете ActionStep
	 * @param storage_name - имя переменной в хранилище 
	 * @param information_key - ключ SubCommand.InformationKey
	 * @param information_default значение которое будет установлено в SubCommand.InformationValue, если !=null
	 * если information_default==null, тогда кладем только ([information_key] [Action.Data(storage_name)])
	 */
	public void putToSubCommandInformationFromStorage(int subcommand_unique_index, 
												    String storage_name,
												    String information_key,
												    String information_default){
		if(information_default==null){
			this.field_parameter_information.add(new ParameterInformationPut(subcommand_unique_index,
					 storage_name,
					 information_key,
					 null,
					 ParameterInformationPut.TYPE_STORAGE_VALUE));
		}else{
			this.field_parameter_information.add(new ParameterInformationPut(subcommand_unique_index,
					 null,
					 information_key,
					 information_default,
					 ParameterInformationPut.TYPE_KEY_VALUE));
		}
	}

	/**<b>взять все ключи из хранилища, которые начинаются на (storage_name_begin) и положить в SubCommand </b>
	 * <br>
	 * метод, который добавляет задачу в список по установке в SubCommand.Information значений из хранилища
	 * @param subcommand_unique_index - уникальный индекс SubCommand в пакете ActionStep
	 * @param storage_name_begin - начальные символы, если параметр в хранилище начинается с них, тогда положить в SubCommandInformation  
	 */
	public void putSubCommandInformationFromStorage(int subcommand_unique_index, 
		    										String storage_name_begin){
		this.field_parameter_information.add(new ParameterInformationPut(subcommand_unique_index,
																		 storage_name_begin,
																		 null,
																		 null,
																		 ParameterInformationPut.TYPE_STORAGE_KEYS_VALUES)
											 );
	}
	
	/** делает анализ объекта, который нужно получить из хранилища (byte[], String, Integer) и кладет в SubCommand.Information(String key, String value)*/
	private void putSubCommandInformation_put_object(SubCommand sub_command,String key,Object object_from_storage){
		if(object_from_storage instanceof byte[]){
			sub_command.putInformation(key, "byte[]");
		}else
		if(object_from_storage instanceof String){
			sub_command.putInformation(key, (String)object_from_storage);
		}else
		if(object_from_storage instanceof Integer ){
			sub_command.putInformation(key,((Integer)object_from_storage).toString());
		};
	}
	
	/** положить в заданный SubCommand один параметр из Action.Data по заданному ключу, предварительно преобразовав его в String */
	private void putSubCommandInformation(Transport transport,
										  ParameterInformationPut parameter){
		try{
			if(parameter.isValueFromStorage()){
				putSubCommandInformation_put_object(transport.getSubCommand(parameter.getUniqueSubCommandIndex()),parameter.getInformationKey(), this.field_current_action.getParameter(parameter.getStorageName()));
			};
			if(parameter.isKeyAndValue()){
				transport.getSubCommandByUniqueIndex(parameter.getUniqueSubCommandIndex()).putInformation(parameter.getInformationKey(), 
											                                                              parameter.getDefaultValue());
			};
			if(parameter.isKeysAndValues()){
				String[] keys=this.field_current_action.getParameters();
				for(int counter=0;counter<keys.length;counter++){
					if(keys[counter].startsWith(parameter.getStorageName())){
						putSubCommandInformation_put_object(transport.getSubCommandByUniqueIndex(parameter.getUniqueSubCommandIndex()),
															keys[counter],
														    this.field_current_action.getParameter(keys[counter]));
					}
				}
			}
			if(parameter.getDefaultValue()==null){
			}else{
				
			}
		}catch(Exception ex){
			LOGGER.error("setParameterDynamicIntoSubCommand Exception:"+ex.getMessage());
		}
	}
	/** 
	 * <b>FOR ENGINE </b>
	 * <br>
	 * обработать все задания для извлечения данных из хранилища Action.Data и копирования данных в SubCommand.Information
	 * для дополнительной информации в SubCommand*/
	public void putAllSubCommandInformation(Transport transport){
		try{
			for(int counter=0;counter<this.field_parameter_information.size();counter++){
				putSubCommandInformation(transport, this.field_parameter_information.get(counter));
			}
		}catch(Exception ex){
			LOGGER.error("putAllSubCommandInformation: Exception:"+ex.getMessage());
		}
	}
}
 
/** класс для хранения данных, которые точно идентифицируют объект в списке команд ActionStep <br>
 * имя параметра в Промежуточном хранилище для сохранения данных <br>
 * тип параметра, который будет сохранен в хранилище <br>
 * конверер, который будет использован для преобразования значений (если он(конвертер) != null)
 */
class ParameterKey implements Serializable{
	/**
	 */
	private static final long serialVersionUID = 1L;
	private String field_parameter_name=null;
	
	private int field_index=0;
	private int field_parameter_type=0;
	private ActionStepConverter field_converter=null;
	
	/** параметры для динамического получения данных из объекта и сохранения этих параметров в Промежуточном хранилище
	 * @param parameter_name 
	 */
	
	public ParameterKey(String parameter_name,int command_index, int parameter_type){
		this.field_parameter_name=parameter_name;
		this.field_index=command_index;
		this.field_parameter_type=parameter_type;
	}
	/** параметры для динамического получения данных из объекта и сохранения этих параметров в Промежуточном хранилище <br>
	 * c дополнительной конвертацией
	 */
	public ParameterKey(String parameter_name,int command_index, int parameter_type,ActionStepConverter converter){
		this.field_parameter_name=parameter_name;
		this.field_index=command_index;
		this.field_parameter_type=parameter_type;
		this.field_converter=converter;
	}
	
	/** получить конвертер для преобразования значения */
	public ActionStepConverter getConverter(){
		return this.field_converter;
	}
	
	/** получить уникальный индекс команды */
	public int getCommandIndex(){
		return this.field_index;
	}
	
	/** получить тип параметра */
	public int getCommandParameterType(){
		return this.field_parameter_type;
	}
	
	/** получить уникальное имя параметра в хранилище данных */
	public String getParameterName(){
		return this.field_parameter_name;
	}
}

/** класс для хранения данных, которые необходимы для извлечения промежуточных параметров из объекта Action
 * и их установки в SubCommand в качестве параметров 
 */
class ParameterSet implements Serializable{
	/**
	 */
	private static final long serialVersionUID = 1L;
	private String field_parameter_name=null;
	private int field_index=0;
	
	/**
	 * 
	 * @param parameter_name имя переменной в Action
	 * @param command_index индекс SubCommand, в который необходимо ее установить
	 */
	public ParameterSet(String parameter_name, int command_index){
		this.field_parameter_name=parameter_name;
		this.field_index=command_index;
	}
	/** получить имя переменной в Action */
	public String getParameterName(){
		return this.field_parameter_name;
	}
	/** получить индекс нужного SubCommand */
	public int getParameterSubCommandIndex(){
		return this.field_index;
	}
}

/** данный класс хранит информацию для извлечения значения из хранилища Action.data 
 * и помещения данного значения в строковом представлении в SubCommand.Information <br>
 * (применяется на последней стадии ответа клиенту для перемещения клиенту дополнительной информации)*/
class ParameterInformationPut implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int field_unique_index;
	private String field_storage_name=null;
	private String field_information_key=null;
	private String field_information_default=null;
	private int field_flag=0;
	/** кладем в SubCommand значение по уникальному имени в хранилище*/
	public static final int TYPE_STORAGE_VALUE=0;
	/** кладем в SubCommand указанные ключ, значение */
	public static final int TYPE_KEY_VALUE=1;
	/** кладем в SubCommand все ключи, которые начинаются с указанного в field_storage_name имени*/
	public static final int TYPE_STORAGE_KEYS_VALUES=2;
	/**
	 * данный класс хранит информацию для извлечения значения из хранилища Action.data 
	 * и помещения данного значения в строковом представлении в SubCommand.Information <br>
	 * (применяется на последней стадии ответа клиенту для перемещения клиенту дополнительной информации) 
	 * @param subcommand_unique_index - уникальный индекс SubCommand в пакете ActionStep
	 * @param storage_name - имя параметра в хранилище Action.Data
	 * @param information_key - SubCommand.InformationKey 
	 * @param information_default_value - постоянное значение, которое будет передано в SubCommand, невзирая на наличие Storage_name
	 * @param flag - флаг, который говорит о том что должны положить в SubCommand: <br> 
	 * ключ из information_key, значение из хранилища по имени storage_name, <br> 
	 * ключ из information_key, значение из information_default_value <br>
	 * все ключи, которые начинаются в хранилище с имени storage_name положить в SubCommand.Information 
	 */
	public ParameterInformationPut(int subcommand_unique_index, 
								   String storage_name,
								   String information_key,
								   String information_default_value,
								   int flag){
		this.field_unique_index=subcommand_unique_index;
		this.field_storage_name=storage_name;
		this.field_information_key=information_key;
		this.field_information_default=information_default_value;
		this.field_flag=flag;
	}
	
	/** возвращает уникальный индекс для SubCommand в пакете ActionStep*/
	public int getUniqueSubCommandIndex(){
		return this.field_unique_index;
	}
	/** возвращает уникальное имя переменной из хранилища Action.Data*/
	public String getStorageName(){
		return this.field_storage_name;
	}
	/** возвращает ключ, по которому стоит положить в SubCommand.Information значение из Action.Data */
	public String getInformationKey(){
		return this.field_information_key;
	}
	/** возвращает null, если нужно получить значение из хранилища Action.Data <br>
	 * возвращает !=null если нужно передать пользователю постоянное значение 
	 */
	public String getDefaultValue(){
		return this.field_information_default;
	}
	/** 
	 * возвращает true, если нужно взять переменную из хранилища и положить в SubCommand 
	 */
	public boolean isValueFromStorage(){
		return this.field_flag==ParameterInformationPut.TYPE_STORAGE_VALUE;
	}
	/** 
	 * возвращает true, если нужно взять переменную и значение из текущего объекта
	 */
	public boolean isKeyAndValue(){
		return this.field_flag==ParameterInformationPut.TYPE_KEY_VALUE;
	}
	
	/**
	 * возвращает true, если нужно положить переменные из хранилища по указанному заголовку param_name
	 */
	public boolean isKeysAndValues(){
		return this.field_flag==ParameterInformationPut.TYPE_STORAGE_KEYS_VALUES;
	}
	
}





