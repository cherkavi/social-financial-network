package engine.queue_send;

import java.util.List;

import org.apache.log4j.Logger;

import engine.database.DatabaseProxy;
import engine.modem.IActionAfterSend;
import engine.modem.ISender;
import engine.modem.MessageOutput;
import engine.modem.message.message_text.MessageOut;
import gui.Utility.IGuiLog;

/** цель данного объекта - в отдельном потоке опрашивать с заданным интервалом таблицу базы данных и отправлять сообщения 
 * через присоединенный интерфейс отправки 
 * */
public class QueueSend implements Runnable,IActionAfterSend{
	private Logger logger=Logger.getLogger(this.getClass());
	
	/** объект, через который осуществляется отправка сообщений */
	private ISender sender;
	
	/** время в милисекундах для опроса базы данных */
	private int timeDatabaseDelay;
	
	/** поток выполнения программы */
	private Thread threadMain;
	
	/** флаг работы программы */
	private boolean flagRun=false;
	
	/** вывод информации в графический Log */
	@SuppressWarnings("unused")
	private IGuiLog guiLog;
	
	/** установить объект, через который осуществляется отправка сообщений */
	public void setSender(ISender sender){
		this.sender=sender;
	}
	/** установить время опроса базы данных */
	public void setTimeDatabaseDelay(int time){
		this.timeDatabaseDelay=time;
	}
	
	
	/** объект, который смотрит базу на наличие в ней задач на отправку и отправляет найденные сообщения 
	 * @param sender объект, через который необходимо отправлять сообщения 
	 * @param timeDatabaseDelay время в милисекундах для опроса базы данных на наличие заданий
	 * @param guiLog графический логгер 
	 * */
	public QueueSend(ISender sender, 
					 int timeDatabaseDelay,
					 IGuiLog guiLog){
		this.sender=sender;
		this.timeDatabaseDelay=timeDatabaseDelay;
		this.guiLog=guiLog;
	}
	
	/** объект, который смотрит базу на наличие в ней задач на отправку и отправляет найденные сообщения 
	 * @param timeDatabaseDelay время в милисекундах для опроса базы данных на наличие заданий 
	 * @param guiLog графический логгер 
	 * */
	public QueueSend(int timeDatabaseDelay,
					 IGuiLog guiLog){
		this.timeDatabaseDelay=timeDatabaseDelay;
		this.guiLog=guiLog;
	}

	/** запуск данного сервиса
	 * @throws Exception если не найден объект для отправки сообщений   
	 * */
	public void startService() throws Exception {
		if(this.sender==null){
			throw new Exception("Sender is not set");
		}
		this.threadMain=new Thread(this);
		this.threadMain.setName("Message Sender");
		this.flagRun=true;
		this.threadMain.start();
	}
	
	/** остановка сервиса */
	public void stopService(){
		this.flagRun=false;
	}
	
	/** [7] послать записи из базы на Sender */
	private void sendToSender(List<MessageOut> list){
		logger.debug("[7] sendToSender:"+list.size());
		for(MessageOut element:list){
			logger.debug("[7]      :"+element.getRecipient());
			// INFO место сопряжения {@link MessageOutput} и {@link MessgeOut} 
			MessageOutput outputMessage=new MessageOutput(element.getRecipient(), 
														  element.getText(), 
														  element.getId(),
														  element.getActionId()
														  );
			this.sender.sendMessage(outputMessage);
		}
	}
	
	
	

	/** <b>[1]</b> просмотреть таблицу (TEST_FOR_SEND.ID_STATUS=1) на наличие статусов "Taken to Send" 
	 * return null если произошла какая-либо ошибка во время передачи данных 
	@SuppressWarnings("unchecked")
	private List<DSmsMessages> getTakenForSend(){
		Session session=null;
		try{
			session=Connector.getSession();
			// query:  просмотреть таблицу (TEST_FOR_SEND.ID_STATUS=1) на наличие статусов "Taken to Send"
			List<DSmsMessages> list=session.createCriteria(DSmsMessages.class)
										   .add(Restrictions.eq("archiv",new Integer(0)))
										   .add(Restrictions.eq("idSendStatus", new Integer(1)))
										   .list();
			logger.debug("[1] getTakenForSend:"+list.size());
			return list;
		}catch(Exception ex){
			logger.error("getTakenForSend Exception:"+ex.getMessage());
			this.guiLog.addInformation("ошибка получения объектов из базы данных со статусом \"Taken to Send\"");
		}finally{
			Connector.closeSession(session);
		}
		return null;
	}
	*/
			
	/** <b>[2]</b> получить список записей для отправки <br>
	 * просмотреть таблицу (TEST_FOR_SEND.ID_STATUS="New Message") на наличие статуса "New Message", не архивных (ARCHIV==0) 
	@SuppressWarnings("unchecked")
	private List<DSmsMessages> getMessageForSend(){
		List<DSmsMessages> returnValue=null;
		Session session=null;
		try{
			// query: просмотреть таблицу (TEST_FOR_SEND.ID_STATUS="New Message") на наличие статуса "New Message", не архивных (ARCHIV==0) 
			session=Connector.getSession();
			returnValue=(List<DSmsMessages>)session.createCriteria(DSmsMessages.class)
													.add(Restrictions.eq("idSendStatus", new Integer(0)))
													.add(Restrictions.eq("archiv",new Integer(0)))
													.addOrder(Order.asc("idSmsMessage")).list();
			if(returnValue.size()>0){
				logger.info("[2] есть сообщения на отправку:"+returnValue.size());
			}else{
				logger.info("[2] нет сообщений для отправки");
			}
			
		}catch(Exception ex){
			logger.error("isMessageForSendExists Exception:"+ex.getMessage(),ex);
			this.guiLog.addInformation("ошибка получения объектов из базы данных со статусом \"New Message\" ");
		}finally{
			Connector.closeSession(session);
		}
		return returnValue;
	}
	*/
	
	
	/** [3] проверить сообщения для отправки на наличие по указанным адресатам не доставленных сообщений <br>
	 * анализ стоп-листа: проанализировать все сообщения на наличие в них таких, по которым ожидается доставка либо которые в процессе 
	 *
	private List<DSmsMessages> getNotProcessMessage(List<DSmsMessages> list){
		ArrayList<DSmsMessages> returnValue=new ArrayList<DSmsMessages>();
		if(list!=null){
			logger.debug("[3] getNotProcessMessage: before:"+list.size());
			for(DSmsMessages element:list){
				element.getRecepient();
				List<DSmsMessages> allMessage=getAllProcessMessagesByRecipient(element.getRecepient(),element.getIdSmsMessage());
				if((allMessage!=null)&&(allMessage.size()>0)){
					// есть недоставленные сообщения по указанному номеру - не добавляем в список
				}else{
					// нет недоставленных сообщений по указанному номеру - добавляем в список 
					returnValue.add(element);
				}
			}
			logger.debug("[3] getNotProcessMessage: after:"+returnValue.size());
		}
		return returnValue;
	} 
	*/
	
	/** получить все сообщения для отправки, не архивные, у которых статус "sended" (idStatus=2) либо "wait for repeat" (idStatus=3) 
	 * @param recipient - получатель, который должен быть 
	@SuppressWarnings("unchecked")
	private List<DSmsMessages> getAllProcessMessagesByRecipient(String recipient,Integer uniqueId){
		List<DSmsMessages> returnValue=null;
		Session session=null;
		try{
			// query: получить все сообщения для отправки, не архивные, у которых статус "sended" (idStatus=2) либо "wait for repeat" (idStatus=3)
			session=Connector.getSession();
			Criterion idStatus=Restrictions
							   .or(Restrictions.eq("idSendStatus", new Integer(3)), 
								   Restrictions.eq("idSendStatus", new Integer(2))
							    );
			return session.createCriteria(DSmsMessages.class).add(idStatus).add(Restrictions.eq("archiv",new Integer(0))).add(Restrictions.like("recepient", recipient)).add(Restrictions.lt("idSmsMessage", uniqueId)).list();
		}catch(Exception ex){
			logger.error("getAllMessagesByRecipient Exception:"+ex.getMessage());
			guiLog.addInformation("Ошибка получения объектов из базы данных, которые находятся в состоянии ожидания по пользователю:"+recipient);
		}finally{
			Connector.closeSession(session);
		}
		return returnValue;
	}
	*/
	
	
	/** [4] удалить повторяющихся получателей, то есть в пакете на отправку может быть только один получатель 
	 * удалить более поздние сообщения из очереди 
	private List<DSmsMessages> getNotRepeatRecipientMessage(List<DSmsMessages> list){
		logger.debug("[4] getNotRepeatRecipientMessage before:"+list.size());
		ArrayList<DSmsMessages> returnValue=new ArrayList<DSmsMessages>();
		if(list!=null){
			for(DSmsMessages element:list){
				if(returnValue.indexOf(element)<0){
					returnValue.add(element);
				}
			}
		}
		logger.debug("[4] getNotRepeatRecipientMessage after:"+list.size());
		return returnValue;
	}
	*/
	
	/**
	 * [5] изменить статус сообщения с "new message" на "taken for send" для указанных сообщений 
	private void setMessageStatusWasTaken(List<DSmsMessages> list){
		Session session=null;
		try{
			logger.debug("[5] setMessageStatusWasTaken:"+list.size());
			session=Connector.getSession();
			for(DSmsMessages element:list){
				try{
					// query: set ID_STATUS=1
					session.beginTransaction();
					element.setIdSendStatus(1); 
					element.setSendStatusDate(new Date());
					session.update(element);
					session.getTransaction().commit();
					logger.debug("[5]    :"+element.getIdSmsMessage());
				}catch(Exception ex){
					logger.error("Exception: "+ex.getMessage());
					this.guiLog.addInformation("ошибка изменения статуса сообщений с с \"new message\" на \"taken for send\" ");
				}
			}
		}catch(Exception ex){
			logger.error("setMessageStatusWasTaken: Exception:"+ex.getMessage());
		}finally{
			Connector.closeSession(session);
		}
	}
	*/
	
	/** [6] изменить статус сообщения на "Sended" и установить "RefNo" для данного сообщения 
	private void setMessageStatusSended(MessageOutput message){
		logger.debug("[6] setMessageStatusSended: Id:"+message.getIdDataBase()+"   RefNo:"+message.getRefNo());
		Session session=null;
		try{
			session=Connector.getSession();
			// query: set status IS_STATUS=2 (sended)
			DSmsMessages record=(DSmsMessages)session.createCriteria(DSmsMessages.class)
													 .add(Restrictions.eq("idSmsMessage", message.getIdDataBase()))
													 .uniqueResult();
			record.setIdSendStatus(2); 
			record.setSendStatusDate(new Date());
			record.setRefNo(message.getRefNo());
			session.beginTransaction();
			session.update(record);
			session.getTransaction().commit();
			logger.debug("статус изменен на Sended и установлен RefNo ");
		}catch(Exception ex){
			logger.error("actionAfterSend: Exception:"+ex.getMessage());
			this.guiLog.addInformation("ошибка изменения статуса сообщения на Sended");
		}finally{
			Connector.closeSession(session);
		}
	}
	 */

	/** действия, которые необходимо сделать с объектом после отправки */
	public void actionAfterSend(MessageOutput message, String messageError) {
		if(messageError==null){
			// сообщение успешно отправлено
			logger.info("сообщение было отправлено: "+message.getOutboundMessage());
			DatabaseProxy proxy=new DatabaseProxy();
			proxy.setMessageStatusWasSended(message);
		}else{
			// сообщение отправлено с ошибкой
			logger.warn("сообщение было отправлено с ошибкой:"+messageError);
			DatabaseProxy proxy=new DatabaseProxy();
			proxy.setMessageStatusWasSendedError(message, messageError);
		}
	}
	
	
	
	public void run() {
		DatabaseProxy proxy=new DatabaseProxy();
		
		/*logger.debug("получить не отрправленные сообщения:");
		List<MessageOut> listNotSended=proxy.resetStatusTakenForSend();
		if(listNotSended!=null){
			logger.debug("was not sended messages size:"+listNotSended);
		}
		this.sendToSender(listNotSended);*/
		logger.debug("Сбросить статус \"взято в процесс\" ");
		proxy.resetStatusTakenForSend();
		
		while(this.flagRun==true){
			// INFO рабочий цикл программы 
			logger.info("начало цикла опроса таблицы с сообщениями для отправки");
				logger.debug("получить список записей для отправки");
			List<MessageOut> listForSend=proxy.getMessageForSend();
			/*
				logger.debug("удалить все сообщения, которые ожидают доставки по получателю");
			listForSend=proxy.getNotProcessMessage(listForSend);
				logger.debug("оставить только первые сообщения по получателям, убрав повторы по получателям, оставить только по одному получателю в пакете");
			listForSend=proxy.getNotRepeatRecipientMessage(listForSend);
			*/
			logger.debug("изменить в данных сообщениях, готовых к отправке статус с New Message на статус Сообщение взято в работу ");
			proxy.setMessageStatusWasTaken(listForSend);
			logger.debug("послать список на отправителя"); 
			this.sendToSender(listForSend);
			logger.debug("message for send is SENDED:"+listForSend.size());
			// Thread sleep - wait for next data
			try{
				Thread.sleep(this.timeDatabaseDelay);
			}catch(InterruptedException ex){
				logger.error("run: "+ex.getMessage());
			}catch(Exception ex){
				logger.error("run: "+ex.getMessage());
			}
		}
	}
	
}
