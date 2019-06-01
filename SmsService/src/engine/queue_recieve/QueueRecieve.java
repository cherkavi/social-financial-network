package engine.queue_recieve;

import org.apache.log4j.Logger;
import org.smslib.StatusReportMessage.DeliveryStatuses;

import engine.database.DatabaseProxy;
import engine.modem.CallInput;
import engine.modem.ICallListener;
import engine.modem.IMessageInputListener;
import engine.modem.MessageInput;
import gui.Utility.IGuiLog;


/** очередь входящих сообщений  */
public class QueueRecieve  implements ICallListener,IMessageInputListener{
	Logger logger=Logger.getLogger("QueueRecieve:");
	/** графический вывод отладочной информации */
	private IGuiLog guiLog;
	private DatabaseProxy proxy=new DatabaseProxy();
	
	/** слушатель для входящих событий (SMS сообщений) и телефонных звонков (Call)*/
	public QueueRecieve(IGuiLog guiLog){
		this.guiLog=guiLog;
	}
	
	public void inputCall(CallInput call) {
		synchronized (this.proxy) {
			proxy.incomingCall(call);
		}
	}

	public void inputMessage(MessageInput message) {
		// INFO входящие сообщения/звонки
		logger.info("inputMessage: "+message.getRecipient());
		synchronized(this.proxy){
			if(message.isDeliveryMessage()){
				logger.info(" Получено сообщение о доставке ");
				this.processDeliveredMessage(message);
			}else{
				logger.info(" Получено входящее текстовое сообщение ");
				proxy.incomingMessage(message);
			}
		}
	}
	
	/** сохранить входящий звонок в базу данных 
	private void saveCallIntoDataBase(CallInput call){
		logger.debug("сохранить входящий звонок в базу данных");
		Session session=null;
		try{
			// INFO query: save INCOMING CALL
			session=Connector.getSession();
			DSmsMessages record=new DSmsMessages();
			record.setActionDate(new Date());
			record.setCdSmsMessageType(MessageType.CALL.name());
			record.setRecepient(call.getRecipient());
			session.beginTransaction();
			session.save(record);
			session.getTransaction().commit();
			logger.debug("звонок успешно сохранен");
		}catch(Exception ex){
			logger.error("saveCallIntoDataBase: Exception:"+ex.getMessage(), ex);
			guiLog.addInformation("ошибка сохранения звонка в базе данных ");
		}finally{
			Connector.closeSession(session);
		}
	}*/
	
	/** [8] сохранить входящее сообщение в базе 
	private void saveMessageIntoDataBase(MessageInput message){
		Session session=null;
		try{
			// INFO query: save INCOMING MESSAGE
			session=Connector.getSession();
			DSmsMessages record=new DSmsMessages();
			record.setCdSmsMessageType(MessageType.RECEIVE.name());
			record.setRecepient(message.getRecipient());
			record.setTextMessage(message.getText());
			record.setActionDate(new Date());
			record.setArchiv(1);
			if(message.isDeliveryMessage()){
				record.setRefNo(message.getRefno());
			}
			session.beginTransaction();
			session.save(record);
			session.getTransaction().commit();
			logger.debug("[8] saveMessageIntoDataBase: "+record.getIdSmsMessage());
			if(message.isDeliveryMessage()){
				logger.debug("получен отчет о доставке - обработка");
				processDeliveredMessage(message);
			}
		}catch(Exception ex){
			logger.error("saveMessageIntoDataBase: Exception:"+ex.getMessage(),ex);
			this.guiLog.addInformation("ошибка сохранения входящего сообщения в базе данных");
		}finally{
			Connector.closeSession(session);
		}
	}
	*/
	
	/** получить номер телефона на основании полученной строки из модема 
	private String getRecipientByOriginal(String modemRecipient){
		// INFO номер абонента из модема сопрягается с номером абонента из базы данных
		return "+"+modemRecipient;
	}
	*/
	
	/** [9] обработка отчета о доставке */
	private void processDeliveredMessage(MessageInput message){
		logger.debug("[9] обработка отчета о доставке : Recipient <"+message.getRecipient()+">");
		try{
			if((message.getRefno()!=null)&&(message.getRefno()!="")){
				// INFO query: обработка отчета о доставке
				/*
				logger.debug("processDeliveredMessage: Find criteria: Recipient:"
							 +this.getRecipientByOriginal(message.getRecipient())
							 +"      RefNo:"
							 +new Integer(message.getRefno()));
				*/
				// получить по отчету о доставке объект, которому принадлежит данный 
				 
				// Object object=session.createCriteria(DSmsMessages.class).add(Restrictions.eq("archiv", new Integer(0)))
				// .add(Restrictions.eq("recepient", this.getRecipientByOriginal(message.getRecipient())))
				// 											  	   .add(Restrictions.eq("refNo", message.getRefno()))
				// 												  	   .uniqueResult();
				// if(object!=null){DSmsMessages messageForSend=(DSmsMessages)object;
				// }else{logger.error("processDelieveredMessage is not find: RefNo:"+message.getRefno()+"  Recipient:"+message.getRecipient());}
				// logger.debug("processDeliveredMessage: Object from DataBase:"+object);
				if(message.getDeliveryStatus()==DeliveryStatuses.DELIVERED){
					proxy.inputDeliveryStatusDelivered(message);
					/*session.beginTransaction();
					messageForSend.setIdDelivStatus(new Integer(1));
					messageForSend.setDelivStatusDate(new Date());
					messageForSend.setArchiv(new Integer(1));
					session.update(messageForSend);
					session.getTransaction().commit();
					logger.debug("processDeliveredMessage: "+message.getDeliveryStatus().name());
					*/
				}
				if(message.getDeliveryStatus()==DeliveryStatuses.ABORTED){
					proxy.inputDeliveryStatusAborted(message,"оператор вернул статус Aborted");
					/*session.beginTransaction();
					messageForSend.setIdDelivStatus(new Integer(2));
					messageForSend.setDelivStatusDate(new Date());
					//messageForSend.setArchiv(new Integer(1));
					messageForSend.setIdSendStatus(new Integer(3));// ID_STATUS=3 - wait for repeat 
					messageForSend.setSendStatusDate(new Date());
					logger.debug("MessageManagerNew: setStatus 'wait for repeat' "+messageForSend.getIdSmsMessage());
					
					session.save(messageForSend);
					session.getTransaction().commit();
					logger.debug("processDeliveredMessage: "+message.getDeliveryStatus().name());
					*/
				}
				if(message.getDeliveryStatus()==DeliveryStatuses.KEEPTRYING){
					// игнорируем, ничего не изменяя в базе данных 
					logger.debug("processDeliveredMessage: "+message.getDeliveryStatus().name());
				}
				if(message.getDeliveryStatus()==DeliveryStatuses.UNKNOWN){
					proxy.inputDeliveryStatusUnknown(message,"оператор вернул неизвестный статус доставки");
					/*session.beginTransaction();
					messageForSend.setIdDelivStatus(new Integer(3));
					messageForSend.setDelivStatusDate(new Date());
					//messageForSend.setArchiv(new Integer(1));
					messageForSend.setIdSendStatus(new Integer(3));// ID_STATUS=3 - wait for repeat 
					messageForSend.setSendStatusDate(new Date());
					logger.debug("MessageManagerNew: setStatus 'wait for repeat' "+messageForSend.getIdSmsMessage());
					session.save(messageForSend);
					session.getTransaction().commit();
					logger.debug("processDeliveredMessage: "+message.getDeliveryStatus().name());
					*/
				}
				if(message.getDeliveryStatus()==null){
					logger.error("processDeliveredMessge return NULL for delivered status - IMPOSSIBLE ERROR");
					throw new Exception("QueueRecieve IMPOSSIBLE ERROR - Message Delivery status is null ");
				}
				
			}else{
				logger.error("processDeliveredMessage is not have RefNo:");
				this.guiLog.addInformation("ошибка идентификации полученного подтверждения о доставке ");
			}
		}catch(Exception ex){
			logger.error("processDeliveredMessage Exception:"+ex.getMessage(),ex);
			this.guiLog.addInformation("ошибка идентификации подтверждения о доставке ");
		}
	}
	
	
}
