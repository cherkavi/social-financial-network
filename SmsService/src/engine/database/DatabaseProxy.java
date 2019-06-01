package engine.database;

import java.sql.CallableStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import database.Connector;

import engine.EngineSettings;
import engine.modem.CallInput;
import engine.modem.MessageInput;
import engine.modem.MessageOutput;
import engine.modem.message.EMessageActionSendState;
import engine.modem.message.EMessageDeliveredStatus;
import engine.modem.message.EMessageType;
import engine.modem.message.message_text.MessageOut;

/** класс, который содержит все функции для работы с базой данных,<br> 
 * то есть любой информационный обмен с базой данных нужно производить только через этот класс 
 * */
public class DatabaseProxy {
	private Logger logger=Logger.getLogger(this.getClass());
	

	/** получение параметров профиля  
	 * @param conneciton - соединение с базой данных 
	 * @param deviceIMEI - уникальный код устройства 
	 * */
	public ParamEngineSettings loadEngineSettings(Connection connection, String deviceIMEI){
		ParamEngineSettings param=new ParamEngineSettings();
		try{
			//                                                     1                                                                    2  3  4  5  6  7  8  9 10
			CallableStatement statement = connection.prepareCall("{? = call "+Connector.getScheme()+"PACK_BC_SMS_SEND.get_profile_param(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
	        statement.registerOutParameter(1, Types.VARCHAR);
	        statement.setString(2,deviceIMEI); // Серийный номер устройства
	        statement.registerOutParameter(3, Types.INTEGER); // ИД профиля
	        statement.registerOutParameter(4, Types.VARCHAR); // Код состояния профиля
	        statement.registerOutParameter(5, Types.INTEGER); // Максимальное поличество повторов
	        statement.registerOutParameter(6, Types.INTEGER); // Задержка отправки (мс) 
	        statement.registerOutParameter(7, Types.INTEGER); // Максимальное время ожидания подтверждения (sec)
	        statement.registerOutParameter(8, Types.INTEGER); // Время, через которое база данных будет опрошена на наличие сообщений на отправку (мс) 
	        statement.registerOutParameter(9, Types.INTEGER); // Время, через которое будет вызвана процедура по анализу устаревших сообщений (мс) 
	        statement.registerOutParameter(10, Types.VARCHAR); // Сообщение об ошибке 
	        logger.info("Execute "+Connector.getScheme()+"PACK_BC_SMS_SEND.get_profile_param ");
	        statement.execute();
	        logger.info("the procedure was Executed "+Connector.getScheme()+"PACK_BC_SMS_SEND.get_profile_param ");
	        String executeResult=statement.getString(1);
	        if((executeResult!=null)&&(executeResult.equals("0"))){
	        	param.setIdProfile(statement.getInt(3));
	        	param.setProfileState(statement.getString(4));
	        	param.setMaxRepeatCount(statement.getInt(5));
	        	param.setDelayForSendMs(statement.getInt(6));
	        	param.setDelayForGetDelivery(statement.getInt(7));
	        	param.setDelayForGetMessageForSend(statement.getInt(8));
	        	param.setDelayForExecuteRepeatController(statement.getInt(9));
	        	logger.info("loadEngineSettings was successfull");
	        }else{
	        	logger.error("loadEngineSettings Exception: "+statement.getString(10));
	        }
		}catch(Exception ex){
			logger.error("loadEngineSettings Exception:"+ex.getMessage());		
		}finally{
		}
		return param;
	}

	/** получить список SMS сообщений которые готовы к отправке в данный момент, 
	 * <small>( получить список для отправки в модем )</small>
	*/
	public ArrayList<MessageOut> getMessageForSend() {
		ArrayList<MessageOut> returnValue=new ArrayList<MessageOut>();
		if(EngineSettings.isActive()){
			Connection connection=Connector.getConnection();
			try{
				logger.info("Select from "+Connector.getScheme()+"vc_ds_sms_new_all - получение всех SMS сообщений, для отправки на данный момент ");
				// другими словами учитывается поля ActionDate при работе 
				ResultSet rs=connection.createStatement().executeQuery("select * from "+Connector.getScheme()+"vc_ds_sms_new_all where id_sms_profile = "+EngineSettings.getProfileId());
				while(rs.next()){
					MessageOut message=new MessageOut();
					message.setId(rs.getInt("ID_SMS_MESSAGE")); // уникальный идентификатор сообщения
					logger.info("Message for send: "+message.getId());
					try{
						message.setActionDate(new Date(rs.getTimestamp("BEGIN_ACTION_DATE").getTime())); // Дата начала действия
					}catch(Exception ex){};
					// rs.getString("BEGIN_ACTION_DATE_FRMT");// Дата начала действия (форматировано)
					// rs.getString("CD_SMS_MESSAGE_TYPE");   // Код типа сообщения
					// rs.getString("NAME_SMS_MESSAGE_TYPE"); // Название типа сообщения
					try{
						message.setState(EMessageActionSendState.valueOf(rs.getString("CD_SMS_STATE"))); // Код состояния сообщения
					}catch(Exception ex){};
					// rs.getString("NAME_SMS_STATE");        // Название состояния сообщения
					message.setRecipient(rs.getString("RECEPIENT")); // Получатель
					message.setText(rs.getString("TEXT_MESSAGE"));  // Текст сообщения
					try{
						message.setDateEvent(rs.getTimestamp("EVENT_DATE")); // Дата события (отправки, получения и т.д)
					}catch(Exception ex){};
					// rs.getString("EVENT_DATE_FRMT");       // Дата события (отправки, получения и т.д)
					try{
						message.setRefNo(Integer.parseInt(rs.getString("REFNO_INPUT"))); // REFNO входящий
					}catch(Exception ex){};
					message.setRepeatCount(rs.getInt("REPEAT_COUNT"));// Количество повторов
					message.setProfileId(rs.getInt("ID_SMS_PROFILE"));// ID профиля
					/*rs.getTimestamp("CREATION_DATE");        
					rs.getInt("CREATED_BY");
					rs.getTimestamp("LAST_UPDATE_DATE");
					rs.getInt("LAST_UPDATE_BY");
					rs.getInt("ID_NAT_PRS_MESSAGE");
					rs.getInt("ID_NAT_PRS");
					rs.getInt("ID_QUEST_RECORD");
					rs.getInt("ID_SMS_PATTERN"); 
					rs.getString("LANGUAGE");
					*/
					returnValue.add(message);
				}
				logger.info("Всего: "+returnValue.size());
			}catch(Exception ex){
				logger.error("getMessageForSend:"+ex.getMessage());
			}finally{
				try{
					connection.close();
				}catch(Exception ex){};
			}
		}else{
			logger.info("profile is NOT ACTIVE:"+EngineSettings.getProfileId());
			// System.out.println("profile is NOT ACTIVE:"+EngineSettings.getProfileId());
			// профиль не активен 
		}
		return returnValue;
	}
	
	/** сохранить в базу данных входящий звонок  
	 * @param call - входящий звонок
	 * @return 
	 * <ul> <b>value</b> - номер, под которым данное событие вставлено в базу данных </ul>
	 * <ul> <b>null </b> - ошибка при добавлении </ul>
	 * */
	public Integer incomingCall(CallInput call) {
		Connection connection=Connector.getConnection();
		try{
			/*
			FUNCTION add_sms(
			     p_recepient IN VARCHAR2            << 2: Отримувач
			    ,p_cd_sms_message_type IN VARCHAR2  << 3: Тип повідомлення
								--   SEND - вихідне повідомлення
								--   RECEIVE - прийняте повідомлення
								--   CALL_IN - вхідний дзвінок
								--   CALL_OUT - вихідний дзвінок
			    ,p_text_message IN VARCHAR2         << 4: Повідомлення 
			    ,p_id_sms_profile IN NUMBER         << 5: ІД профіля
			    ,p_id_sms_message OUT NUMBER        >> 6: ІД збереженого повідомлення
			    ,p_error_msg OUT VARCHAR2           >> 7: 
			) RETURN VARCHAR2                       >> 1: 
			 * 
			 */
			//                                                     1                                                          2  3  4  5  6  7
			CallableStatement statement = connection.prepareCall("{? = call "+Connector.getScheme()+"PACK_BC_SMS_SEND.add_sms(?, ?, ?, ?, ?, ?)}");
	        statement.registerOutParameter(1, Types.VARCHAR);
	        statement.setString(2, call.getRecipient());
	        statement.setString(3, EMessageType.CALL_IN.toString());
	        statement.setNull(4, Types.VARCHAR);
	        statement.setInt(5, EngineSettings.getProfileId());
	        statement.registerOutParameter(6, Types.INTEGER);
	        statement.registerOutParameter(7, Types.VARCHAR);
	        
	        logger.info("Execute "+Connector.getScheme()+"PACK_BC_SMS_SEND.add_sms (CALL_IN)");
	        statement.execute();
	        logger.info("the procedure was Executed "+Connector.getScheme()+"PACK_BC_SMS_SEND.add_sms (CALL_IN)");
	        String executeResult=statement.getString(1);
	        if((executeResult!=null)&&(executeResult.equals("0"))){
	        	int messageId=statement.getInt(6);
	        	logger.info("Message added with id:"+messageId);
	        }else{
	        	logger.error("Procedure executed with error: "+statement.getString(7));
	        }
	        return statement.getInt(9);
		}catch(Exception ex){
			logger.error("Execute procedure PACK_BC_SMS_SEND.add_sms (CALL_IN) Exception: "+ex.getMessage());
			return null;
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
	}
	
	/** установить состояние для сообщение как ошибочное, то есть во время выполнения алгоритма действий с сообщением произошла неисправимая ( самим алгоритмом ) ошибка, 
	 * которая привела к невозможности отправки сообщения
	 * <br>
	 * например, не удалось изменить очередной статус для сообщения - нужно вызвать данную процедуру, в которой записать текст ошибки
	 * @param messageId - код сообщения, полученный из базы данных 
	 * @param actionId - (nullable) код состояния, полученный из базы данных ( может отсутствовать )
	 * @return 
	 * <ol type="disc">
	 * 	<li><b>true</b> - статус усешно установлен </li>
	 * 	<li><b>false</b> - ошибка установки статуса </li>
	 * </ol>
	 *  */
	public boolean setMessageAsError(Integer messageId, Integer actionId, String messageError ){
		boolean returnValue=false;
		Connection connection=Connector.getConnection();
		try{
			/*
			 * Фукнція записує інформацію про фатальну помилку при відправці SMS
			 * FUNCTION update_sms_unknown_error(
     			p_id_sms_message IN NUMBER          -- ІД повідомлення
    			,p_id_sms_action IN NUMBER          -- ІД дії
    			,p_error_message IN VARCHAR2        -- Повідомлення про помилку
    			,p_result_msg OUT VARCHAR2
				) RETURN VARCHAR2
			 */
			CallableStatement statement=connection.prepareCall("{? = call "+Connector.getScheme()+"PACK_BC_SMS_SEND.update_sms_unknown_error(?, ?, ?, ?)}");
			statement.registerOutParameter(1, Types.VARCHAR);
			statement.setInt(2, messageId);
			if(actionId==null){
				statement.setNull(3, Types.INTEGER);
			}else{
				statement.setInt(3, actionId);
			}
			if(messageError==null){
				statement.setNull(4,Types.VARCHAR);
			}else{
				statement.setString(4, (messageError.length()>1000)?messageError.substring(0,1000):messageError);
			}
			statement.registerOutParameter(5, Types.VARCHAR);
			
			statement.executeUpdate();
			if(statement.getString(1).equals("0")){
				returnValue=true;
				this.logger.debug("setMessageAsError Save OK");
			}else{
				this.logger.error("setMessageAsError SQL save Error: "+statement.getString(5));
				returnValue=false;
			}
		}catch(Exception ex){
			this.logger.error("#setMessageAsError Exception: "+ex.getMessage());
			returnValue=false;
		}finally{
			try{
				connection.close();
			}catch(Exception ex){
			}
		}
		return returnValue;
	}
	
	/** сохранение в базу данных входящего сообщения 
	 * @param message - входящее сообщение
	 * @return 
	 * <ul><b>null</b> - ошибка при записи </ul>
	 * <ul><b>value</b> - номер в базе данных, под которым записано данное сообщение </ul> 
	 * */
	public Integer incomingMessage(MessageInput message) {
		if(message.isDeliveryMessage()){
			return 0;
		}
		logger.info("InputMessage: Recipient"+message.getRecipient()+"    RefNo:"+message.getRefno()+"  Text:"+message.getRecipient());
		Connection connection=Connector.getConnection();
		CallableStatement statement = null;
		try{
			/*
			FUNCTION add_sms(
			     p_recepient IN VARCHAR2            << 2: Отримувач
			    ,p_cd_sms_message_type IN VARCHAR2  << 3: Тип повідомлення
								--   SEND - вихідне повідомлення
								--   RECEIVE - прийняте повідомлення
								--   CALL_IN - вхідний дзвінок
								--   CALL_OUT - вихідний дзвінок
			    ,p_text_message IN VARCHAR2         << 4: Повідомлення 
			    ,p_id_sms_profile IN NUMBER         << 5: ІД профіля
			    ,p_id_sms_message OUT NUMBER        >> 6: ІД збереженого повідомлення
			    ,p_error_msg OUT VARCHAR2           >> 7: 
			) RETURN VARCHAR2                       >> 1: 
			 * 
			 */
			//                                                     1                                                          2  3  4  5  6  7
			statement = connection.prepareCall("{? = call "+Connector.getScheme()+"PACK_BC_SMS_SEND.add_sms(?, ?, ?, ?, ?, ?)}");
	        statement.registerOutParameter(1, Types.VARCHAR);
	        statement.setString(2, message.getRecipient());
	        statement.setString(3, EMessageType.RECEIVE.toString());
	        statement.setString(4, message.getText());
	        statement.setInt(5, EngineSettings.getProfileId());
	        statement.registerOutParameter(6, Types.INTEGER);
	        statement.registerOutParameter(7, Types.VARCHAR);
	        logger.info("Execute "+Connector.getScheme()+"PACK_BC_SMS_SEND.add_sms (RECEIVE)");
	        statement.execute();
	        logger.info("the procedure was Executed "+Connector.getScheme()+"PACK_BC_SMS_SEND.add_sms (RECEIVE)");
	        String executeResult=statement.getString(1);
	        if((executeResult!=null)&&(executeResult.equals("0"))){
	        	int messageId=statement.getInt(6);
	        	logger.info("Message added with id:"+messageId);
	        }else{
	        	logger.error("Procedure executed with error: "+statement.getString(7));
	        }
	        return statement.getInt(6);
		}catch(Exception ex){
			logger.error("Execute procedure PACK_BC_SMS_SEND.add_sms (RECEIVE) Exception: "+ex.getMessage());
			return null;
		}finally{
			try{
				statement.close();
			}catch(Exception ex){};
			try{
				connection.close();
			}catch(Exception ex){};
		}
	}
	
	/** создать сообщение для отправки и добавить его в базу данных  
	 * @param recipient - получатель сообщения, желательно в следующем формате, пример: "+380979204671"
	 * @param text - текст сообщения, который следует записать в базу данных ( то есть этот текст будет изъят и преобразован в нужный формат) другими словами допустимы русские символы
	 * */
	public Integer putMessageForSend(String recipient, String text) {
		Connection connection=Connector.getConnection();
		try{
			/*
			FUNCTION add_sms(
			     p_recepient IN VARCHAR2            << 2: Отримувач
			    ,p_cd_sms_message_type IN VARCHAR2  << 3: Тип повідомлення
								--   SEND - вихідне повідомлення
								--   RECEIVE - прийняте повідомлення
								--   CALL_IN - вхідний дзвінок
								--   CALL_OUT - вихідний дзвінок
			    ,p_text_message IN VARCHAR2         << 4: Повідомлення 
			    ,p_id_sms_profile IN NUMBER         << 5: ІД профіля
			    ,p_id_sms_message OUT NUMBER        >> 6: ІД збереженого повідомлення
			    ,p_error_msg OUT VARCHAR2           >> 7: 
			) RETURN VARCHAR2                       >> 1: 
			 * 
			 */
			//                                                     1                                                          2  3  4  5  6  7
			CallableStatement statement = connection.prepareCall("{? = call "+Connector.getScheme()+"PACK_BC_SMS_SEND.add_sms(?, ?, ?, ?, ?, ?)}");
	        statement.registerOutParameter(1, Types.VARCHAR);
	        statement.setString(2, recipient);
	        statement.setString(3, EMessageType.SEND.toString());
	        statement.setString(4, text);
	        statement.setInt(5, EngineSettings.getProfileId());
	        statement.registerOutParameter(6, Types.INTEGER);
	        statement.registerOutParameter(7, Types.VARCHAR);
	        
	        logger.info("Execute "+Connector.getScheme()+"PACK_BC_SMS_SEND.add_sms (SEND)");
	        statement.execute();
	        logger.info("the procedure was Executed "+Connector.getScheme()+"PACK_BC_SMS_SEND.add_sms (SEND)");
	        String executeResult=statement.getString(1);
	        int messageId=0;
	        if((executeResult!=null)&&(executeResult.equals("0"))){
	        	messageId=statement.getInt(6);
	        	logger.info("Message added with id:"+messageId);
	        }else{
	        	logger.error("Message does not added: "+statement.getString(7));
	        }
	        return messageId;
		}catch(Exception ex){
			logger.error("Execute procedure PACK_BC_SMS_SEND.add_sms (SEND) Exception: "+ex.getMessage());
			return null;
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
	}

	
	/** установить для данных сообщений "взято в процесс отправки", <br> 
	 * другими словами - данное сообщение взято из базы данных, и теперь будет двигаться по алгоритмам к модему для отправки
	 * {@link EMessageActionSendState#IN_PROCESS}
	 */
	public void setMessageStatusWasTaken(List<MessageOut> listForSend) {
		Connection connection=Connector.getConnection();
		try{
			
			if(listForSend!=null){
				/*
				 * FUNCTION add_sms_action(
				     p_id_sms_message IN NUMBER         <<  2: ІД сообщения
				    ,p_recepient IN VARCHAR2            <<  3: Отримувач
				    ,p_text_message IN VARCHAR2         <<  4: Повідомлення
				    ,p_id_send_status IN NUMBER         <<  5: ІД статусу отримання повідомлення
				    ,p_error_message IN VARCHAR2        <<  6: Повідомлення про помилку
				    ,p_id_sms_profile IN NUMBER         <<  7: ІД профіля SMS
				    ,p_id_sms_action OUT NUMBER         >>  8: ІД результату відсилання повідомлення
				    ,p_result_msg OUT VARCHAR2			>>  9: ошибка текста 
				) RETURN VARCHAR2; 						>> 	1: возвращаемое значение
				 */
				//                                                   1                                                                 2  3  4  5  6  7  8  9 
				CallableStatement statement=connection.prepareCall("{? = call "+Connector.getScheme()+"PACK_BC_SMS_SEND.add_sms_action(?, ?, ?, ?, ?, ?, ?, ?)}");
				ArrayList<MessageOut> removeList=new ArrayList<MessageOut>();
				// вызвать для всех сообщений функцию add_sms_action
				// добавить во все сообщений уникальный код, который будет идентифицировать Action
				for(int counter=0;counter<listForSend.size();counter++){
					try{
						statement.clearParameters();
						logger.info("Message was taken for send to modem:"+listForSend.get(counter).getId());
						MessageOut message=listForSend.get(counter);
						statement.registerOutParameter(1, Types.VARCHAR);
						statement.setInt(2, message.getId());
						statement.setString(3, message.getRecipient());
						statement.setString(4, message.getText());
						statement.setInt(5, EMessageActionSendState.IN_PROCESS.getKod());
						statement.setNull(6, Types.VARCHAR);
						statement.setInt(7, EngineSettings.getProfileId());
						statement.registerOutParameter(8, Types.INTEGER);
						statement.registerOutParameter(9, Types.VARCHAR);
						logger.info("Set message as IN_PROCESS: "+message.getId());
						statement.execute();
						String result=statement.getString(1);
						if((result!=null)&&(result.equals("0"))){
							logger.info("Функция установки статуса сообщения в IN_PROCESS успешно выполнена: (id_action="+statement.getInt(8)+")");
							listForSend.get(counter).setActionId(statement.getInt(8));
						}else{
							listForSend.get(counter).setActionId(0);
							String textOfErrorMessage="Ошибка выполенения функции установки IN_PROCESS для сообщения:"+statement.getString(9);
							logger.error(textOfErrorMessage);
							this.setMessageAsError(message.getId(), null, textOfErrorMessage);
						}
					}catch(Exception ex){
						// добавить сообщение для удаления из базы 
						removeList.add(listForSend.get(counter));
						String message="setMessageStatusWasTaken iteration Exception: "+ex.getMessage();
						logger.error(message);
						System.err.println(message);
					}
				}
				if(removeList.size()>0){
					logger.info("Не удалось записать сообщения как взятые в процесс: "+removeList.size());
					for(int counter=0;counter<removeList.size();counter++){
						logger.debug("Удалить сообщение из очереди: "+removeList.get(counter));
						listForSend.remove(removeList.get(counter));
					}
				}
			}
		}catch(Exception ex){
			String message="setMessageStatusWasTaken Exception: "+ex.getMessage();
			System.err.println(message);
			logger.error(message);
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
		
	}

	/** для данного сообщения установить тип отправлено через модем - ожидание доставки ( то есть модем отправил данное сообщение ) 
	 * <br>
	 * {@link EMessageActionSendState#WAIT_FOR_CONFIRM}
	 * */
	public void setMessageStatusWasSended(MessageOutput message) {
		Connection connection=Connector.getConnection();
		try{
			/*
			 * FUNCTION update_sms_action_state(
			     p_id_sms_action IN NUMBER			<< 2: полученный из add_sms_action[8] код данного Action
			    ,p_id_send_status IN NUMBER         << 3: ІД статусу отримання повідомлення
			    ,p_refno IN NUMBER                  << 4: REFNO
			    ,p_error_message IN VARCHAR2        << 5: Повідомлення про помилку
			    ,p_result_msg OUT VARCHAR2			>> 6: ошибка 
			   ) RETURN VARCHAR2;					>> 1: код выполнения ("0" - успешное завершение ) 
			 */
			logger.info("Message was sended: "+message.getIdDataBase()+"   message RefNo:"+message.getRefNo());
			//                                                   1                                                                          2  3  4  5  6
			CallableStatement statement=connection.prepareCall("{? = call "+Connector.getScheme()+"PACK_BC_SMS_SEND.update_sms_action_state(?, ?, ?, ?, ?)}");
			statement.registerOutParameter(1, Types.VARCHAR);
			statement.setInt(2, message.getActionId());
			statement.setInt(3, EMessageActionSendState.WAIT_FOR_CONFIRM.getKod());
			int refNo=Integer.parseInt(message.getRefNo());
			statement.setInt(4, refNo);
			statement.setNull(5, Types.VARCHAR);
			statement.registerOutParameter(6, Types.VARCHAR);
			statement.execute();
			String result=statement.getString(1);
			if((result!=null)&&(result.equals("0"))){
				logger.info("Статус "+EMessageActionSendState.WAIT_FOR_CONFIRM.toString()+"  успешно установлен для сообщения: ActionId="+message.getActionId());
			}else{
				String errorMessage="Ошибка выполенения функции PACK_BC_SMS_SEND.update_sms_action_state ("+result+")  Error Message: "+statement.getString(6);
				System.err.println(errorMessage);
				logger.error(errorMessage);
			}
		}catch(Exception ex){
			String errorMessage="setMessageStatusWasSended Exception:"+ex.getMessage();
			this.setMessageAsError(message.getIdDataBase(), null, errorMessage);
			System.err.println(errorMessage);
			logger.error(errorMessage);
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
	}
	
	/** для данного сообщения установить ошибка отправки сообщения через модем 
	 * <br>
	 * {@link EMessageActionSendState#SEND_ERROR}
	 * @param message - ошибочное сообщение 
	 * @param errorSendMessage - текстовое сообщение об ошибке
	 * */
	public void setMessageStatusWasSendedError(MessageOutput message, String errorSendMessage) {
		Connection connection=Connector.getConnection();
		try{
			/*
			 * FUNCTION update_sms_action_state(
			     p_id_sms_action IN NUMBER			<< 2: полученный из add_sms_action[8] код данного Action
			    ,p_id_send_status IN NUMBER         << 3: ІД статусу отримання повідомлення
			    ,p_refno IN NUMBER                  << 4: REFNO
			    ,p_error_message IN VARCHAR2        << 5: Повідомлення про помилку
			    ,p_result_msg OUT VARCHAR2			>> 6: ошибка 
			   ) RETURN VARCHAR2;					>> 1: код выполнения ("0" - успешное завершение ) 
			 */
			//                                                   1                                                                          2  3  4  5  6
			CallableStatement statement=connection.prepareCall("{? = call "+Connector.getScheme()+"PACK_BC_SMS_SEND.update_sms_action_state(?, ?, ?, ?, ?)}");
			statement.registerOutParameter(1, Types.VARCHAR);
			statement.setInt(2, message.getActionId());
			statement.setInt(3, EMessageActionSendState.SEND_ERROR.getKod());
			try{
				statement.setInt(4, Integer.parseInt(message.getRefNo()));
			}catch(Exception ex){
				statement.setNull(4, Types.INTEGER);
			}
			if(errorSendMessage!=null){
				statement.setString(5, errorSendMessage);
			}else{
				statement.setNull(5, Types.VARCHAR);
			}
			statement.registerOutParameter(6, Types.VARCHAR);
			statement.execute();
			String result=statement.getString(1);
			if((result!=null)&&(result.equals("0"))){
				logger.info("Статус "+EMessageActionSendState.SEND_ERROR.toString()+"  успешно установлен для сообщения: ActionId="+message.getActionId());
			}else{
				String errorMessage="Ошибка выполенения функции PACK_BC_SMS_SEND.update_sms_action_state ("+result+")  Error Message: "+statement.getString(6);
				System.err.println(errorMessage);
				logger.error(errorMessage);
			}
		}catch(Exception ex){
			String errorMessage="setMessageStatusWasSendedError Exception:"+ex.getMessage();
			this.setMessageAsError(message.getIdDataBase(), null, errorMessage);
			System.err.println(errorMessage);
			logger.error(errorMessage);
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
	}
	

	/** вызвать процедуру установки статуса доставки по REF_NO */
	private void inputDeliveryStatus(Connection connection, 
									 MessageInput message, 
									 EMessageDeliveredStatus status,
									 String addErrorMessage) throws Exception {
		/*
		 * 
		   Фукнція змінює інформацію про відсилання повідомлення за REFNO
			FUNCTION update_sms_action_for_refno(
			     p_id_sms_profile IN NUMBER         << 2: ІД профіля SMS
			    ,p_recepient IN VARCHAR2            << 3: телефон
			    ,p_refno IN NUMBER                  << 4: REFNO
			    ,p_id_deliv_status IN NUMBER        << 5: Статус доставки select * from bc_admin.vc_ds_sms_deliv_status
			    ,p_error_message IN VARCHAR2        << 6: Повідомлення про помилку
			    ,p_result_msg OUT VARCHAR2			>> 7: текст ошибки 
			) RETURN VARCHAR2						>> 1: результат выполнения
		 */
		logger.info("Сообщение было доставлено: RefNo:"+message.getRefno()+"   Recipient: "+message.getRecipient());
		//                       1                                                                                                          2  3  4  5  6  7
		CallableStatement statement=connection.prepareCall("{? = call "+Connector.getScheme()+"PACK_BC_SMS_SEND.update_sms_action_for_refno(?, ?, ?, ?, ?, ?)}");
		statement.registerOutParameter(1, Types.VARCHAR);
		statement.setInt(2, EngineSettings.getProfileId());
		statement.setString(3, message.getRecipient());
		statement.setInt(4, Integer.parseInt(message.getRefno()));
		statement.setInt(5, status.getKod());
		statement.setString(6, addErrorMessage);
		statement.registerOutParameter(7, Types.VARCHAR);
		statement.execute();
		String returnValue=statement.getString(1);
		if((returnValue!=null)&&(returnValue.equals("0"))){
			logger.info("Статус "+status.toString()+" для абонента "+message.getRecipient()+"  RefNo: "+message.getRefno()+"  успешно установлен ");
		}else{
			String errorMessage="inputDeliveryStatus "+status.toString()+" ReturnValue="+returnValue+"   SQLException:"+statement.getString(7);
			System.err.println(errorMessage);
			logger.error(errorMessage);
		}
	}
	
	/** обработать входящее сообщение о доставке как DELIVERED подтверждение 
	 * <br>
	 * {@link EMessageActionSendState#DELIVERED}
	 * */
	public void inputDeliveryStatusDelivered(MessageInput message) {
		Connection connection=Connector.getConnection();
		try{
			inputDeliveryStatus(connection, message, EMessageDeliveredStatus.DELIVERED, null);
		}catch(Exception ex){
			String errorMessage="inputDeliveryStatusDeliverd Exception: "+ex.getMessage();
			System.err.println(errorMessage);
			logger.error(errorMessage);
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
	}

	/** обработать входящее сообщение о доставке как ABORTED подтврждение
	 * @param message - сообщение
	 * @param sendErrorMessage - текст ошибки, при которой сообщение установлено в ABORTED 
	 * <br>
	 * {@link EMessageDeliveredStatus#ABORTED}
	 * */
	public void inputDeliveryStatusAborted(MessageInput message, String sendErrorMessage) {
		Connection connection=Connector.getConnection();
		try{
			inputDeliveryStatus(connection, message, EMessageDeliveredStatus.ABORTED, sendErrorMessage);
		}catch(Exception ex){
			String errorMessage="inputDeliveryStatusDeliverd (ошибка установки статуса Delivered) Exception: "+ex.getMessage();
			System.err.println(errorMessage);
			logger.error(errorMessage);
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
	}

	/** обработать входящее сообщение о доставке как UNKNOWN подтврждение
	 * @param message - сообщение
	 * @param sendErrorMessage - текст ошибки, при которой сообщение установлено в ABORTED 
	 * <br>
	 * {@link EMessageDeliveredStatus#UNKNOWN}
	 * */
	public void inputDeliveryStatusUnknown(MessageInput message, String sendErrorMessage) {
		Connection connection=Connector.getConnection();
		try{
			inputDeliveryStatus(connection, message, EMessageDeliveredStatus.UNKNOWN, sendErrorMessage);
		}catch(Exception ex){
			String errorMessage="inputDeliveryStatusDeliverd Exception: "+ex.getMessage();
			System.err.println(errorMessage);
			logger.error(errorMessage);
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
	}


	/** старт цикла отправки сообщений, сбросить статус "Взято в процесс работы" на статус "Новое сообщение для отправки"
	 * <br>
	 * исключает случай потери сообщений, которые были взяты алгоритмом для отправки, но данные сообщения не были отправлены через модем  
	 */ 
	public void resetStatusTakenForSend() {
		Connection connection=Connector.getConnection();
		try{
			//                                                   1                                                                  2  3
			CallableStatement statement=connection.prepareCall("{? = call "+Connector.getScheme()+"PACK_BC_SMS_SEND.sms_state_reset(?, ?)}");
			statement.registerOutParameter(1, Types.VARCHAR);
			statement.setInt(2, EngineSettings.getProfileId());
			statement.registerOutParameter(3, Types.VARCHAR);
			logger.info("execute sms_state_reset - сбросить статус \"Взято в процесс работыэ\" на статус \"Новое сообщение для отправки\"");
			statement.execute();
			String returnValue=statement.getString(1);
			if((returnValue!=null)&&(returnValue.equals("0"))){
				logger.info("execute sms_state_reset OK");
			}else{
				logger.error("execute sms_state_reset SQL Exception:"+statement.getString(3));
			}
		}catch(Exception ex){
			String errorMessage="DatabaseProxy#resetStatusTakenForSend Exception:"+ex.getMessage();
			System.err.println(errorMessage);
			logger.error(errorMessage);
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
	}
	
	/** проверяет все сообщения, статус отправки которых {@link EMessageActionSendState#WAIT_FOR_CONFIRM} на истекшее время ожидания подтверждение,
	 * и перевода данного сообщения либо в архивное, либо же увеличение счетчика повторов  */
	public void checkMaxDeliveryTime(){
		/*
		FUNCTION check_max_delivery_time(
			     p_id_sms_profile IN NUMBER         -- ІД профіля SMS
			    ,p_result_msg OUT VARCHAR2
			) RETURN VARCHAR2;
		*/
		Connection connection=Connector.getConnection();
		try{
			//                                                   1                                                                          2  3
			CallableStatement statement=connection.prepareCall("{? = call "+Connector.getScheme()+"PACK_BC_SMS_SEND.check_max_delivery_time(?, ?)}");
			statement.registerOutParameter(1, Types.VARCHAR);
			int profileId=EngineSettings.getProfileId();
			statement.setInt(2, profileId);
			statement.registerOutParameter(3, Types.VARCHAR);
			logger.info("execute check_max_delivery_time - проверить сообщения, которые ожидают доставки на статус \"недоставленные\"");
			statement.execute();
			String returnValue=statement.getString(1);
			if((returnValue!=null)&&(returnValue.equals("0"))){
				logger.info("execute check_max_delivery_time OK");
			}else{
				logger.error("ProfileId: "+profileId);
				logger.error("execute check_max_delivery_time SQL Exception:"+statement.getString(3));
			}
		}catch(Exception ex){
			String errorMessage="DatabaseProxy#checkMaxDeliveryTime Exception:"+ex.getMessage();
			System.err.println(errorMessage);
			logger.error(errorMessage);
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
		
	}
	
	
	public static void main(String[] args){
		System.out.println("begin");
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.ERROR);
		Connector.initConnector("jdbc:oracle:thin:@91.195.53.27:1521:demo","bc_sms","bc_sms",null);
		EngineSettings.loadAndInitReader("354745030567640", 60000 );
		
		// создать сообщение, которо нужно послать 
		// addMessageForSend("+380979204671", "hello, this is message from database");
		resetStatus();
		checkTimeOut();
		
		// получить сообщения, которые нужно послать
		readAllMessageForSend();
		/*
		// установить для сообщения статус IN_PROCESS
		getMessageInProcess(readMessageById(341));
		
		// установить для сообщения статус WAIT_FOR_CONFIRM
		getMessageWaitForConfirm(readMessageById(341));
		
		// установить для сообщения статус SEND_ERROR
		getMessageSendError(readMessageById(341));
		*/
		System.out.println("end");
	}

	
	/** попытка чтения всех сообщений для отправки */
	private static void readAllMessageForSend(){
		System.out.println(" Attempt to read:");
		DatabaseProxy proxy=new DatabaseProxy();
		ArrayList<MessageOut> list=proxy.getMessageForSend();
		System.out.println("Message Count (for send): "+list.size());
		for(int counter=0;counter<list.size();counter++){
			System.out.println("Item: "+list.get(counter).getId());
		}
	}
	
/*	
	private static void getMessageInProcess(final MessageOut messageOut){
		DatabaseProxy proxy=new DatabaseProxy();
		ArrayList<MessageOut> list=new ArrayList<MessageOut>();
		list.add(messageOut);
		proxy.setMessageStatusWasTaken(list);
		System.out.println("IN_PROCESS");
	}

	
	private static void getMessageWaitForConfirm(MessageOut messageOut){
		DatabaseProxy proxy=new DatabaseProxy();
		MessageOutput message=new MessageOutput(messageOut.getRecipient(), messageOut.getText(), messageOut.getId(), messageOut.getActionId());
		proxy.setMessageStatusWasSended(message);
		System.out.println("Message Wait Confirm");
	}

	private static void getMessageSendError(MessageOut messageOut){
		DatabaseProxy proxy=new DatabaseProxy();
		MessageOutput message=new MessageOutput(messageOut.getRecipient(), messageOut.getText(), messageOut.getId(), messageOut.getActionId());
		proxy.setMessageStatusWasSendedError(message);
		System.out.println("Message Wait Confirm");
	}
	
	
	private static MessageOut readMessageById(int id){
		System.out.println(" Attempt to read:");
		DatabaseProxy proxy=new DatabaseProxy();
		MessageOut messageOut=null;
		ArrayList<MessageOut> list=proxy.getMessageForSend();
		// System.out.println("Message Count (for send): "+list.size());
		for(int counter=0;counter<list.size();counter++){
			if(list.get(counter).getId()==341){
				messageOut=list.get(counter);
				break;
			}
		}
		return messageOut;
	}
	
	добавить сообщение в базу данных для отправки 
	private static void addMessageForSend(String recipient, String text){
		DatabaseProxy proxy=new DatabaseProxy();
		Integer messageId=proxy.putMessageForSend(recipient, text);
		if(messageId==null){
			System.out.println(" Error add to database");
		}else{
			System.out.println(" Message added to database with number:"+messageId);
			
		}
	}
*/	
	/** обновить статус отправки */
	private static void resetStatus(){
		DatabaseProxy proxy=new DatabaseProxy();
		proxy.resetStatusTakenForSend();
		System.out.println("resetStatus OK");
	}
	
	private static void checkTimeOut(){
		DatabaseProxy proxy=new DatabaseProxy();
		proxy.checkMaxDeliveryTime();
		System.out.println("checkTimeOut OK");
	}

}

/*
 * 
 * Table: bc_sms_send.ds_send_state
	Acronim            Рус                                 Description
11	IN_PROCESS          в процессе                         ( взято алгоритмом )
12	WAIT_FOR_CONFIRM   ожидание подтверждения о доставке  ( отправлено модемом )
13	SEND_ERROR         ошибка отправки                    ( ошибка отправки)

где p_id_send_status будет установлен в 
	(функция add_sms_action будет ВСЕГДА устанавливать только IN_PROCESS)
	(функция update_sms_action_state будет ВСЕГДА устанавливать только WAIT_FOR_CONFIRM/SEND_ERROR)


Алгоритм работы:
   1. запрос получения данных на отправку  vc_ds_sms_new_all
   2. записать для всех полученных сообщений IN_PROCESS (add_sms_action)
   3. отпрвить через модем
   4.1 записать для отправленных сообщений статус WAIT_FOR_CONFIRM
   4.2 записать для не отправленных сообщений статус SEND_ERROR
 
------------
   
Table: bc_sms_send.ds_deliv_status
	Acronim             Рус
11	DELIVERED    	   доставлено           Получено подтверждение о доставке
12	ABORTED      	   не доставлено        Получено подтверждение об ошибочной доставке
13	UNKNOWN      	   неизвестная ошибка   Получено неизвестное состояние сообщения
14  REPORT_NOT_RECIEVE  отчет не получен     Отчет о доставке сообщения не был получен 

Алгоритм работы:
	1 Получено подтверждение о доставке:
		1.1 - DELIVERED
		1.2 - ABORTED
		1.3 - UNKNOWN
	
	2.1 Вызана процедура (PACK_BC_SMS_SEND.check_max_delivery_time), которая определяет выход за пределы времени ожидания
		2.1 - сообщение ожидает доставку
		2.2 - REPORT_NOT_RECIEVE ( должен устанавливаться в процедуре check_max_delivery_time )
------------
 * 
 */


// ------------------------------------------------------------------
/** проверить данное сообщение на установленный флаг доставки
 * @return 
 * <ul> <b>true</b> - доставлено </ul>
 * <ul> <b>false</b> - не доставлено </ul>
 *  
public boolean isMessageDelivered(MessageOut element){
	return false;
}
 */

/** получить сообщения из базы данных, время ожидания подтверждения для которых истекло
 * (получить записи из базы данных для принятия решения об их устаревании, в следствие выхода времени ожидания)  
 *  <br>
 *  \Information\Scheme\SMS\SMS_c_истекшим_временем_ожидания.jpg
 *  {@link ControllerRepeat} 
public List<MessageOut> getOutMessageForWaitDelivery() {
	// получить временной интервал, после которого сообщение считается с истекшим временем ожидания
	// получить сообщения, которые были отправлены, но по ним нет подтверждения о доставке
	return null;
}
 */

/** из указанного списка удалить такие сообщения, по которым в данный момент ожидается доставка по указанному в них получателю
 * <br>
 * другими словами чтобы не "заваливать", например, отключенного в данный момент от сети GSM пользователя сообщениями, которые ему прийдут ВСЕ одновременно, как только появится сеть 
public List<MessageOut> getNotProcessMessage(List<MessageOut> listForSend) {
	return null;
}
 */

/** 
 * оставить только первые сообщения по получателям, убрав повторы по получателям, оставить только по одному получателю в пакете
 * <br>
 * то есть список сгруппировать по получателям и удалить из этого списка такие сообщения, которые по указанному получателю повторяются не один раз
public List<MessageOut> getNotRepeatRecipientMessage(
		List<MessageOut> listForSend) {
	return null;
}
 */

/** по указанному сообщению не пришло подтверждение о доставке - повторить, если повторы возможны, или же записать как не доставленное 
public void setMessageForRepeat(MessageOut element) {
}
*/


