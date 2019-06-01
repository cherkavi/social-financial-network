package engine.modem;
import gui.Utility.IGuiLog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.smslib.GatewayException;
import org.smslib.ICallNotification;
import org.smslib.IInboundMessageNotification;
import org.smslib.IOutboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Service;
import org.smslib.StatusReportMessage;
import org.smslib.TimeoutException;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.modem.SerialModemGateway;

/** класс, который отвечает за работу с модемом - отправка SMS и оповещение о приеме SMS сообщений <br> 
 * данный класс является автономным потоком, который обрабатывает входящие сообщения и оповещает об исходящих 
 * */
public class Modem extends AbstractModem {
	
	/** логгер */
	Logger logger=Logger.getLogger(this.getClass());

	/** провайдер SMS услуг */
	private Service smsService;
	

	/** графический вывод лога */
	private IGuiLog guiLog;

	/** notify all listeners about recieve the message
	 * @param statusNotifier - оповещатель статуса 
	 * @param guiLog - вывод графического лога
	 * */
	public Modem(IGatewayStatusNotifier statusNotifier,
			IGatewayMessageNotify messageNotifier,
				 IGuiLog guiLog) throws Exception{
		this("COM1",
			 115200,
			 statusNotifier,
			 messageNotifier,
			 guiLog);
	}

	/** notify all listeners about recieve the message
	 * @param comPortName - имя COM порта 
	 * @param baudrate - скорость инициализации COM порта
	 * @param statusNotifier - оповещатель статуса
	 * @param messageNotifier - оповещатель текстового сообщения  
	 * @param guiLog - вывод графического лога
	 * */
	public Modem(String comPortName, 
				 int baudrate,
				 IGatewayStatusNotifier statusNotifier,
				 IGatewayMessageNotify messageNotifier,
				 IGuiLog guiLog) throws Exception{
		initModem(comPortName,baudrate,statusNotifier,messageNotifier,guiLog);
	}
	

	/** первоначальная инициализация модема 
	 * @throws если не удалось проинициализировать модем по каким либо причинам 
	 * */
	private void initModem(String comPortName, 
						   Integer baudrate,
						   IGatewayStatusNotifier statusNotifier,
						   IGatewayMessageNotify messageNotifier,
						   IGuiLog guiLog) throws Exception {
		this.guiLog=guiLog;
		smsService=new Service();
		// INFO старт SMS сервиса - PIN код
		startSmsService(null,
					    this,
					    this,
					    statusNotifier,
					    messageNotifier,
					    "smsManager",
					    comPortName,
					    baudrate,
					    "SIEMENS",
					    "TC65",
					    "8299");
		// запуск службы контроля переполнения
		startControlOldMessage(smsService);
	}

	
	/** start SMS Service 
	 * @param outMessageListener - слушатель для исходящих сообщений
	 * @param inMessageListener - слушатель для входящих сообщений
	 * @param callListener - слушатель для входящих звонков ( только оповещение )
	 * @param statusNotification - оповещатель для статуса 
	 * @param messageNotifier - оповещатель текстового сообщения  
	 * @param comConnectionName - уникальное имя соедиенения для работы с COM портом  
	 * @param portName - имя порта с которым происходит соединение (COM1, COM2, COM3... )
	 * @param baud - скорость, на которую настроен модем
	 * @param brand - имя производителя модема 
	 * @param model - наименование модели производителя 
	 */
	private void startSmsService(IOutboundMessageNotification outMessageListener,
								 IInboundMessageNotification inMessageListener,
								 ICallNotification callListener,
								 IGatewayStatusNotifier statusNotification,
								 IGatewayMessageNotify messageNotification,
								 String comConnectionName,
								 String portName, 
								 int baud, 
								 String brand, 
								 String model,
								 String simPIN) throws Exception {
		// INFO инициализация модема
		SerialModemGateway gateway = new SerialModemGateway(comConnectionName,
															portName, 
															baud, 
															brand, 
															model);
		gateway.setInbound(true);
		gateway.setOutbound(true);
		gateway.setSimPin(simPIN);
		gateway.setFrom("BonClub");
		// gateway.setSmscNumber(mySmscNumber); // установка SMS Center для модема
		smsService.addGateway(gateway);
		if (outMessageListener != null) {
			smsService.setOutboundNotification(outMessageListener);
		}
		if (inMessageListener != null) {
			smsService.setInboundNotification(inMessageListener);
		}
		if (callListener != null) {
			smsService.setCallNotification(callListener);
		}
		// INFO инициализация слушателя соединения с модемом
		/*gateway.setStatusNotification(new IGatewayStatusNotification(){
			@Override
			public void process(String gtwId, 
								GatewayStatuses oldStatus,
								GatewayStatuses newStatus) {
				statusNotification.gateWayNotifier(newStatus);
			}
		});*/
		GatewayStatusController controller=new GatewayStatusController(gateway, 2000);
		controller.addListener(statusNotification);
		controller.addMessageNotify(messageNotification);
		controller.startService();
		logger.debug("Modem context controller was started");
		smsService.startService();
		logger.debug("SmsService was started");
	}
	
	
	private void startControlOldMessage(Service smsService){
		Thread controlDaemon=new ControlOldMessage(smsService, 1000*60*60*2);
		controlDaemon.setDaemon(true);
		controlDaemon.start();
	}
	

	protected void processQueueMessageInputPost(MessageInput message){
		try{
			this.smsService.deleteMessage(message.getInboundMessage());
		}catch(Exception ex){
			logger.error("ошибка при удалении сообщения из модема :"+ex.getMessage(), ex);
			this.guiLog.addInformation("ошибка при удалении сообщения из модема");
		}
	}
	
	/** notify message listener's */
	@Override
	protected void notifyMessageInputListener(MessageInput message){
		super.notifyMessageInputListener(message);
		// TODO удалить сообщение о доставке 
		// if(message.isDeliveryMessage()){
			try {
				String number=message.getInboundMessage().getId();
				if(smsService.deleteMessage(message.getInboundMessage())==true){
					this.logger.info("Сообщение успешно удалено: "+number);
				}else{
					this.logger.error("Ошибка удаления сообщения на уровне центрального объекта: "+number);
				}
			} catch (TimeoutException e) {
				this.logger.error("Ошибка удаления отчета о доставке: ("+e.getClass().getName()+")");
				e.printStackTrace();
			} catch (GatewayException e) {
				this.logger.error("Ошибка удаления отчета о доставке: ("+e.getClass().getName()+")");
				e.printStackTrace();
			} catch (IOException e) {
				this.logger.error("Ошибка удаления отчета о доставке: ("+e.getClass().getName()+")");
				e.printStackTrace();
			} catch (InterruptedException e) {
				this.logger.error("Ошибка удаления отчета о доставке: ("+e.getClass().getName()+")");
				e.printStackTrace();
			}
		// }
	}
	
	protected boolean sendMessageHardware(MessageOutput message){
		boolean sendResult=false;
		try{
			this.smsService.sendMessage(message.getOutboundMessage());
			/*logger.info("After: Id:"+message.getOutboundMessage().getId());
			logger.info("After: MessageId:"+message.getOutboundMessage().getMessageId());
			logger.info("After: RefNo:"+message.getOutboundMessage().getRefNo());*/
		}catch(RuntimeException re){
			logger.warn("ошибка, возможно, не критичная "+re.getMessage(),re);
		}catch(Exception ex){
			logger.error("ошибка при попытке установки сообщения в очередь "+ex.getMessage(),ex);
			this.guiLog.addInformation("ошибка при попытке установки сообщения в очередь");
		}
		return sendResult;
	}
	
	
	
	/**  остановить сервис прослушивания данных */
	public void disconnect(){
		try {
			smsService.stopService();
			logger.debug("SMS сервис был успешно остановлен");
		} catch (Exception ex) {
			logger.error("stopSmsService: is not stopped: " + ex.getMessage());
			this.guiLog.addInformation("Модем: остановка сервиса");
		}
	}

// --------------------          utility classes       -------------------- 	
	/** контроль за SMS сообщениями, которые имеют дату выше чем один день  */
	private class ControlOldMessage extends Thread{
		private Service smsService;
		private long timeWait;
		
		public ControlOldMessage(Service smsService, long timeWait){
			super("Control Old Message");
			this.smsService=smsService;
			this.timeWait=timeWait;
		}
	
		@Override
		public void run(){
			System.out.println("Model");
			while(true){
				logger.debug("получение всех сообщений и их обработка");
				ArrayList<InboundMessage> list=new ArrayList<InboundMessage>();
				try{
					logger.debug("Чтение доступных сообщений");
					smsService.readMessages(list, MessageClasses.ALL);
					logger.debug("Сообщения прочитаны: "+list.size());
				}catch(Exception ex){
					logger.error("Read All Message Exception:"+ex.getMessage());
				}
				controlOldMessages(smsService, list);
				logger.debug("ожидание"); 
				try{
					Thread.sleep(timeWait);
				}catch(Exception ex){};
			}
		}

		/** контроль за сообщениями, которые, возможно, не были удалены   */
		private void controlOldMessages(Service smsService, ArrayList<InboundMessage> list){
			while(list.size()>0){
				checkForOldMessage(smsService, list.get(0));
				list.remove(0);
			}
		}
		
		private void checkForOldMessage(Service smsService, InboundMessage message){
			if(isInboundMessageOutOfDate(message)){
				try{
					if(smsService.deleteMessage(message)==true){
						logger.debug("Сообщение удалено: "+message.getId());
					}else{
						logger.error("Ошибка удаления сообщения:"+message.getId());
					}
				} catch (TimeoutException e) {
					logger.error("Ошибка удаления старого сообщения : ("+e.getClass().getName()+")");
					e.printStackTrace();
				} catch (GatewayException e) {
					logger.error("Ошибка удаления старого сообщения : ("+e.getClass().getName()+")");
					e.printStackTrace();
				} catch (IOException e) {
					logger.error("Ошибка удаления старого сообщения : ("+e.getClass().getName()+")");
					e.printStackTrace();
				} catch (InterruptedException e) {
					logger.error("Ошибка удаления старого сообщения : ("+e.getClass().getName()+")");
					e.printStackTrace();
				}
			}
		}
		
		private SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		
		private boolean isInboundMessageOutOfDate(InboundMessage message){
			Calendar calendar=Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			java.util.Date date=message.getDate();
			if(date==null){
				if(message instanceof StatusReportMessage){
					date=((StatusReportMessage)message).getReceived();
				}
			}
			// проверить сообщение на устаревание
			logger.debug("Check Message Date:"+((date==null)?"":sdf.format(date)));
			if(date==null){
				logger.debug("Message Date is null:"+message.getId());
				return true;
			}else{
				if(date.before(calendar.getTime())){
					return true;
				}else{
					return false;
				}
			}
		}
	}

}
