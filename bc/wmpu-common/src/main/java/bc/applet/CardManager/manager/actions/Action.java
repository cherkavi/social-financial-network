package bc.applet.CardManager.manager.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import bc.applet.CardManager.manager.transport.SubCommand;
import bc.applet.CardManager.manager.transport.Transport;


/**
 * // TODO SubCommand.CommandName (FOR_READER, FOR_DISPLAY): Server описание команды в текстовом виде   
 * Родитель для всех Action,
 * то есть для всех объектов, которые контроллируют полностью обмен информацией между 
 * клиентом и серверов в виде сеансов связи, для получения конечной цели (объекты Action в схеме)<br>
 * данный объект инициируется и хранится только на стороне сервера
 * @author cherkashinv
 * Допустимые имена команд для использования в именах команды SubCommand.getName
 * FOR_READER byte[] SENDCOMMAND(byte[]) 
 * FOR_READER byte[] FETCHATR()
 * FOR_READER byte[] GETCARDDATA(int)
 * FOR_READER byte[] READBINARY(int)
 * FOR_READER byte[] READRECORD(int)
 * FOR_READER byte[] RESETREADER
 * FOR_READER byte[] READBINARYFILE(byte[])
 * FOR_READER byte[] SELECTFILE(int,String)
 * FOR_READER Information[] GETALLDEVICES()   // возвращается в текстовом виде все имена установленных Reader'ов; кол-во в поле Int
 * FOR_READER String CONNECTTO(String) // имя Reader-a к которому будет происходить соединение, возвращается имя Reader-a с которым было установлено соединение
 * FOR_READER void DISCONNECT() // отсодинение от карточки 
 * 
 * FOR_DISPLAY void SHOWMESSAGE(String)
 * FOR_DISPLAY void WRITETOLOG(String)
 * FOR_DISPLAY void SHOWDEVICES(Infromation[]) // должна предоставить пользователю право выбора функции для подключения
 */
public abstract class Action  implements Serializable{
	private final static Logger LOGGER=Logger.getLogger(Action.class);
	private static final long serialVersionUID = 1L;
	/** Команда читает серийный номер, номер версии ОС, данные конфигурации чипа */
	public final static byte[] getCardDataApdu = { (byte) 0x80, (byte) 0xF6, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

	/** объект, в котором хранятся все промежуточные значения */
	private HashMap <String,Object> field_data=new HashMap<String,Object>();
	
	/** флаг, который показывает на какой фазе/шаге процесс */
	private int field_process_step=0;

	/** объект, который содержит исполняемые шаги*/
	private ArrayList<ActionStep> field_action_step=new ArrayList<ActionStep>();
	
	/** добавить параметр для сохранения промежуточных параметров */
	public void putParameter(String key, Object value){
		this.field_data.put(key, value);
	}
	
	/** 
	 * @param parameter_name - имя параметра, наличие которого проверяется
	 * @return True - если параметр найден <br>
	 * False - параметр не найден
	 */
	public boolean isParameterExists(String parameter_name){
		return this.field_data.containsKey(parameter_name);
	}
	/**
	 * получить все ключи
	 * @param 
	 */
	public String[] getParameters(){
		return this.field_data.keySet().toArray(new String[]{});
	}
	
	/** возвращает параметр из хранилища по уникальному ключу
	 * @param key имя параметра по которому будет выниматься объект из хранилища 
	 * @return is Object, может быть приведен к типу: Integer, String, byte[]
	 * <br> null - если не найден
	 */
	public Object getParameter(String parameter_name){
		return this.field_data.get(parameter_name);
	}
	
	/** получить шаг данного Action*/
	protected int getStep(){
		return this.field_process_step;
	}
	/** увеличить шаг данного Action на 1*/
	protected int incStep(){
		return this.field_process_step=this.field_process_step+1;
	}
	
	/** получить кол-во всех зарегистрированных шагов */
	protected int getActionStepCount(){
		return this.field_action_step.size();
	}
	/** добавить еще один ActionStep в список необходимых для выполнения */
	protected void addActionStep(ActionStep step){
		this.field_action_step.add(step);
	}
	/** 
	 * @return ActionStep по его номеру [0..  ] <br>
	 *  возвращает null, если индекс выходит за рамки обрабатываемого элемента
	 */
	protected ActionStep getActionStep(int index){
		if(index<this.getActionStepCount()){
			return this.field_action_step.get(index);
		}else{
			return null;
		}
	}
	

	/** положить в объект, который хранит параметры промежуточные значения 
	 * (объекты простых типов сохраняются в соответствующих обертках - int в Integer, float в Float...) 
	 */
	public void setParameter(String key, Object value){
		this.field_data.put(key, value);
	}
	
	/** получить промежуточное значение в виде byte[]
	 * @return null если объект не является byte[] либо же не найден
	 */
	public byte[] getParameterByte(String key){
		byte[] return_value=null;
		if(this.field_data.get(key) instanceof byte[]){
			return_value=(byte[])this.field_data.get(key);
		}
		return return_value;
	}

	/** получить промежуточное значение в виде String
	 * @return null если объект не является String либо же не найден
	 */
	public String getParameterString(String key){
		String return_value=null;
		if(this.field_data.get(key) instanceof String){
			return_value=(String)this.field_data.get(key);
		}
		return return_value;
	}

	/** получить промежуточное значение в виде int
	 * @return 0 если объект не является Integer, либо же не найден
	 */
	public int getParameterInt(String key){
		int return_value=0;
		if(this.field_data.get(key) instanceof Integer){
			return_value=((Integer)this.field_data.get(key)).intValue();
		}
		return return_value;
	}
	
	/**
	 * заголовок для вывода отладочной информации и информации об ошибках<br>
	 * так же является уникальным идентификатором для удаленного клиента на вызов необходимого Action 
	 */
	private String field_logger_header="Action";
	
	/** объект, который обменивается с удаленным клиентом посредством исполняемых шагов*/
	public Action(String logger_header){
		setActionName(logger_header);
	}
	/** для вывода DEBUG информации */
	private void debug(String information){
		LOGGER.debug(information);
	}
	/** для вывода ERROR информации */
	private void error(String information){
		LOGGER.error(information);
	}
	/** метод, который выводит DEBUG информацию с заголовком
	 * для заданного объекта 
	 */
	protected void traceObject(String head_for_line, Object object){
		// Trace object Transport
		if(object instanceof Transport){
			Transport current_object=(Transport)object;
			debug(head_for_line+"Полученные от клиента данные: ");
			debug(head_for_line+"Direction:"+current_object.getDirection());
			debug(head_for_line+"Status:"+current_object.getStatus());
			debug(head_for_line+"SubCommandCount:"+current_object.getSubCommandCount());
			if(current_object.getSubCommandCount()>0){
				debug(head_for_line+"SubCommandParameter:"+current_object.getSubCommand(0).getParameter());
				debug(head_for_line+"SubCommandDescription:"+(current_object.getSubCommand(0).getDataDescription()==SubCommand.DATA_FOR_RESPONSE));
				debug(head_for_line+"SubCommandCommand:"+current_object.getSubCommand(0).getCommand());
			}
		}
		// Trace object 
	}
	
	/** установить заголовок, который выводится в отладочной информации*/
	protected void setActionName(String header){
		this.field_logger_header=header;
	}
	/** получить заголовок, который выводится в отладочной информации*/
	protected String getActionName(){
		return this.field_logger_header;
	}
	
	/** текущий пакет для обработки */
	private Transport field_request;
	/** флаг который говорит о том, что объект должен быть уничтожен <br>
	 * либо по причине окончания обработки <br>
	 * либо по причине ошибки во время обмена и/или потери последовательности команд<br>
	*/
	private boolean field_flag_finish=false;
	
	/** получить флаг, который говорит о ненадобности объекта <br>
	 * либо по причине окончания обработки <br>
	 * либо по причине ошибки во время обмена и/или потери последовательности команд<br>
	 */
	public boolean isFinish(){
		return this.field_flag_finish;
	}
	
	/** установить флаг, который говорит о ненадобности объекта <br>
	 * либо по причине окончания обработки <br>
	 * либо по причине ошибки во время обмена и/или потери последовательности команд<br>
	 */
	public void setFinish(boolean value){
		this.field_flag_finish=value;
	}
	
	
	/** Setter для объекта-запроса от клиента */
	protected void setRequest(Transport value){
		this.field_request=value;
	}
	/** Getter для объекта-запроса от клиента <br> 
	 * возвращает текущий транспорт, который обрабатывается
	 * */
	protected Transport getRequest(){
		return this.field_request;
	}
	
	
	/** проверка данных на запрос о действии Action*/
	private boolean checkStepFirst(Transport transport){
		boolean return_value=false;
		// первая загрузка Action: проверка на отсутствие SubCommand и на тип TYPE_REQUEST
		if(  (transport.getDirection()==Transport.TYPE_REQUEST)
		   &&(this.getRequest().getSubCommandCount()==0)){
			return_value=true;
		}else{
			error("Error in first ActionStep");
		}
		return return_value;
	}
	
	/** проверка промежуточного шага на валидность ответа от клиента */
	private boolean checkStep(int step_number,Transport transport){
		boolean return_value=true;
		// ActionStep preview_step=this.field_action_step.get(this.getStep()-1)
		// проверка на количество пришедших отработанных команд, и количество отправленных команд на предыдущем шаге
		// не нужно проводить - добавляются условия
		//if(preview_step.getSubCommandCount()==transport.getSubCommandCount()){
		//}else{return_value=false;error("checkStep Task commandCount not equals Response ");}

		for(int counter=0;counter<transport.getSubCommandCount();counter++){
			//debug(counter+" Transport Name:"+transport.getSubCommand(counter).getCommand()+"  PrevStep Name:"+preview_step.getSubCommand(counter).getCommand());
			// проверка на отработку данной команды с положительным результатом
			if(transport.getSubCommand(counter).getDataDescription()!=SubCommand.DATA_FOR_RESPONSE){
				error("checkStep command not DATA_FOR_RESPONSE");
				return_value=false;
				break;
			}
			// проверка на отработку имени посланной команды и полученной команды
			/*if(!transport.getSubCommand(counter).getCommand().equals(preview_step.getSubCommand(counter).getCommand()) ){
				error("checkStep Command Task not equals Command Response");
				return_value=false;
				break;
			}*/
		}
		return return_value;
	}

	
	/** метод, который обрабатывает запрос клиента, и отвечает на него своим обработанным Transport*/
	public Transport doAction(){
		/** флаг, который показывает был ли обработан запрос*/
		boolean flag_process=false;
		
		if(this.getStep()==0){
			debug("doAction: first step - init Action");
			if(this.checkStepFirst(this.getRequest())==true){
				// TODO: взять все данные из Transport.Information и положить в хранилище
				debug("начальные данные для инициализации, чтобы передать в Action.Information:"+this.getRequest().getInformationCount());
				String[] init_keys=this.getRequest().getInformationKeys();
				for(int counter=0;counter<init_keys.length;counter++){
					try{
						this.putParameter(init_keys[counter], (String)this.getRequest().getInformationTextByKey(init_keys[counter]));
					}catch(Exception ex){
						error("doAction: first step: "+ex.getMessage());
					}
				}
				// очистить все данные в пакете Information
				this.getRequest().clearInformation();
				//параметров нет, это первый шаг --- this.getActionStep(this.getStep()).getAllParameter(this.getRequest());
				// очитстить команды в списке
				this.getRequest().clearSubCommand();
				// отработать все калькуляторы
				this.getActionStep(this.getStep()).initAllCalculator();
				// задать начальные команды
				while(this.getActionStep(this.getStep()).hasNextSubCommand()){
					this.getRequest().addSubCommand(
													this.getActionStep(this.getStep()).nextSubCommand()
													);					
				};
				// задать динамические параметры для обработок
				this.getActionStep(this.getStep()).setAllParameterDynamicIntoSubCommand(this.getRequest());
				// скопировать значения для всех переменных из Action.Data в SubCommand.Information
				this.getActionStep(this.getStep()).putAllSubCommandInformation(this.getRequest());
				this.getRequest().setStatus(Transport.STATUS_OK);
				flag_process=true;
			}else{
				// ошибка в параметрах первого шага 
				error("doAction firstStep check parameter's error");
			}
		}else 
		if(this.getStep()==(this.getActionStepCount()-1)){
			debug("doAction: last step ");
			if(checkStep(this.getStep(),this.getRequest())){
				// обработать входящие параметры
				this.getActionStep(this.getStep()).getAllParameter(this.getRequest());
				// очистить команды в списке
				this.getRequest().clearSubCommand();
				// отработать все калькуляторы
				this.getActionStep(this.getStep()).initAllCalculator();
				// дать новые команды для клиента
				while(this.getActionStep(this.getStep()).hasNextSubCommand()){
					this.getRequest().addSubCommand(
													this.getActionStep(this.getStep()).nextSubCommand()
													);					
				}
				// задать динамические параметры для обработок
				this.getActionStep(this.getStep()).setAllParameterDynamicIntoSubCommand(this.getRequest());
				// скопировать значения для всех переменных из Action.Data в SubCommand.Information
				this.getActionStep(this.getStep()).putAllSubCommandInformation(this.getRequest());
				this.setFinish(true);
				this.getRequest().setStatus(Transport.STATUS_DONE);
				flag_process=true;
			}else{
				// ошибка в параметрах последнего шага
				error("doAction lastStep check parameter's error");
			}
		}else {
			debug("doAction: middle step ");
			if(checkStep(this.getStep(),this.getRequest())){
				// обработать входящие параметры
				this.getActionStep(this.getStep()).getAllParameter(this.getRequest());
				// очитстить команды в списке
				this.getRequest().clearSubCommand();
				// отработать все калькуляторы
				this.getActionStep(this.getStep()).initAllCalculator();
				// задать новые команды для клиента	
				while(this.getActionStep(this.getStep()).hasNextSubCommand()){
					this.getRequest().addSubCommand(
													this.getActionStep(this.getStep()).nextSubCommand()
													);					
				}
				// задать динамические параметры для обработок
				this.getActionStep(this.getStep()).setAllParameterDynamicIntoSubCommand(this.getRequest());
				// скопировать значения для всех переменных из Action.Data в SubCommand.Information
				this.getActionStep(this.getStep()).putAllSubCommandInformation(this.getRequest());
				this.getRequest().setStatus(Transport.STATUS_OK);
				flag_process=true;
			}else{
				// ошибка в параметрах промежуточного шага
				error("doAction step check parameter's error ");
			}
		}
		
		
		// последняя обработка данных, на основании флага flag_process
		// запрос не обработан - передать ошибку
		if(flag_process==false){
			error("doAction: произошла ошибка в обработке или полученных значениях");
			this.setFinish(true);
			if(this.getRequest()==null){
				this.setRequest(new Transport());
				this.getRequest().setDirection(Transport.TYPE_RESPONSE);
				this.getRequest().setStatus(Transport.STATUS_ERROR);
				this.getRequest().addInformation("About", "Security Error");
			}else{
				debug("doAction: запрос не обработан");
				this.getRequest().setDirection(Transport.TYPE_RESPONSE);
				this.getRequest().setStatus(Transport.STATUS_ERROR);
				this.getRequest().clearInformation();
			}
		}else{
			this.getRequest().setDirection(Transport.TYPE_RESPONSE);
		}
		
		
		// увеличить шаг на единицу - перейти к следующему шагу
		this.incStep();
		return this.getRequest();
	}
	/** метод, который обрабатывает переданный пользователем объект, и на основании его выдает ответ 
	 * @param request - данные, которые передал клиент
	 */
	public Transport doAction(Transport request){
		this.setRequest(request);
		return this.doAction();
	}
	
	
}
