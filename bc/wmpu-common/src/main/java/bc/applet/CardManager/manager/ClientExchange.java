package bc.applet.CardManager.manager;



import org.apache.log4j.Logger;

import bc.applet.CardManager.manager.actions.*;
import bc.applet.CardManager.manager.store.FileStore;
import bc.applet.CardManager.manager.store.Store;
import bc.applet.CardManager.manager.transport.Transport;

/** класс, который общается с сервером посредством передачи-приема объекта Transport <br> 
 * так же отвечает и за сохранение объекта класса Manager в хранилище, 
 * и восстановление данного объекта из хранилища   
 * 
 */
public class ClientExchange {
	private final static Logger LOGGER=Logger.getLogger(ClientExchange.class);
	
	/** объект, который отвечает за сохранение/чтение из хранилище*/
	private Store field_store=new FileStore();
	/** уникальный идентификатор клиента */
	private ClientIdentifier field_client_identifier;
	/** данный объект содержит текущее состояние информационного обмена пользователя и сервера*/
	private Action field_action;
	/** флаг, который говорит о том, что пользователь определен и его информационный обмен валидный - <br>
	 * пользователь прошел по безопасности 
	 */
	private boolean field_security=false;
	
	/**   
	 * @param unique_name - уникальное имя для клиента
	 */
	public ClientExchange(String unique_name){
		// может быть инициирован Connection к базе данных
		// создание уникального для клиента объекта
		this.field_client_identifier=new ClientIdentifier(unique_name);

		this.field_security=this.checkSecurity();
	}
	
	
	/** получить информацию о прохождении безопасности */
	private boolean isSecurityOK(){
		return this.field_security;
	}
	
	/** проверка клиента на безопасность*/
	private boolean checkSecurity(){
		// TODO --- пройти безопасность
		//this.field_client_identifier
		return true;
	}
	
	private boolean actionExists(String action){
		return ActionBuilder.isActionNameExists(action);
	}
	
	/** получить/создать объект Action для текущего пользователя и/или его запроса <br>
	 * в поле field_action 
	 *	<b>алгоритм:</b><br> 
	 *  <b> 1 </b> - создать объект для запрошенного Action, если хранилище возвращает null<br>
	 *  <b> 2 </b> - вернуть объект из хранилища, если хранящийся объект !=null <br>
	 */
	public void readAction(Transport transport){
		LOGGER.debug("readAction: получение объекта Action");
		try{
			// прочитать состояние файла из хранилища
			LOGGER.debug("readAction: попытка чтения состояния из хранилища ");
			field_action=this.field_store.readObject(field_client_identifier);
			LOGGER.debug("readAction: прочитанный объект из хранилища:"+field_action);
		}catch(Exception ex){
			// объект не был сохранен для данного клиента
			LOGGER.error("readAction: Ошибка при получении объекта из хранилища: "+ex.getMessage());
			field_action=null;
		}
		
		if(field_action==null){
			LOGGER.debug("readAction: field_action is null");
			// либо первый вход, либо уже было отработано предыдущее действие, либо произошел сбой при информационном обмене
			// создать объект, в зависимости от transport
			if((transport.getActionName()!=null)&&(this.actionExists(transport.getActionName()))){
				// Action detected - create new Action по уникальному имени transport
				try{
					LOGGER.debug("readAction: создание нового Action для клиента Name:"+transport.getActionName());
					field_action=ActionBuilder.getActionClassByName(transport.getActionName());
					field_action.putParameter("SESSION_ID", this.field_client_identifier.getUniqueName());
					LOGGER.debug("readAction: объект Action создан "+field_action);
				}catch(Exception ex){
					LOGGER.error("readAction: ошибка создания объекта Action по имени:<"+transport.getActionName()+">   TextException:"+ex.getMessage());
				}
				
			}else{
				// field_action==null
				LOGGER.error("readAction: не удалось создать новый Action");
			}
		}
	}
	
	/** передать клиенту ответ на его запрос */
	public Transport Decode(Transport transport){
		LOGGER.debug("Decode: Полученный объект:");
		LOGGER.debug("Decode: ActionName:"+transport.getActionName());
		LOGGER.debug("Decode: InformationCount:"+transport.getInformationCount());
		LOGGER.debug("Decode: SubCommand:"+transport.getSubCommandCount());
		Transport return_value=null;
		//разрешение от безопасности получено ?
		if(this.isSecurityOK()==true){
			LOGGER.debug("Decode: разрешение безопасности получено");
			LOGGER.debug("Decode: ЗАПРОС НОВОГО ACTION или ЧТЕНИЕ уже сохраненного ");
			try{
				if((transport!=null)&&(transport.isSessionIdPresent()==true)){
					LOGGER.debug("Уникальный номер соединения получен:"+transport.getSessionId());
					this.field_client_identifier=new ClientIdentifier(transport.getSessionId());
				}else{
					LOGGER.debug("Номера сессии в пакете нет - создание нового");
					transport.setSessionId(this.field_client_identifier.getUniqueName());
				}
				LOGGER.debug("получение/создание ACTION");
				this.readAction(transport);
				LOGGER.debug("обработать текущий запрос");
				return_value=this.field_action.doAction(transport);
			}catch(Exception ex){
				// если произошла фатальная ошибка при обработке
				// если this.readAction вернул null - не удалось создать новый Action
				LOGGER.error("Decode: exception:"+ex.getMessage(), ex);
			}
			// --- если закончено действие - записать NULL вместо объекта
			LOGGER.debug("Decode: записать объект в хранилище");
			if(this.field_action!=null){
				if(this.field_action.isFinish()){
					LOGGER.debug("Debug: Записать null");
					this.field_store.deleteObject(field_client_identifier);
					//this.field_store.writeObject(field_client_identifier,null);
				}else{
					LOGGER.debug("Debug: Записать объект:"+field_action);
					this.field_store.writeObject(field_client_identifier,this.field_action);
				}
			}else{
				// произошла фатальная ошибка, объект Action не был создан
				this.field_store.deleteObject(field_client_identifier);
			}
			LOGGER.debug("Debug: Обработка окончена");
		}else{
			LOGGER.debug("Decode: разрешение безопасности НЕ получено");
			// ответ клиенту null - не пройдена безопасность
		}
		return return_value;
	}
	
}











