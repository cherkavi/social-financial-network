package gui.Frames.Main;

import java.util.Calendar;


import org.apache.log4j.Logger;
import org.smslib.AGateway.GatewayStatuses;

import database.Connector;
import engine.EngineSettings;
import engine.modem.CallInput;
import engine.modem.ICallListener;
import engine.modem.IGatewayMessageNotify;
import engine.modem.IGatewayStatusNotifier;
import engine.modem.IMessageInputListener;
import engine.modem.IMessageOutputListener;
import engine.modem.MessageInput;
import engine.modem.MessageOutput;
import engine.modem.Modem;
import engine.queue_recieve.QueueRecieve;
import engine.queue_send.QueueSend;
import engine.task_executor.database_function.FunctionExecutor;
import gui.Utility.CommonObject;
import gui.Utility.IGuiLog;

public class ConsoleMain	implements IMessageInputListener, 
	  								   ICallListener,
	  								   IMessageOutputListener,
	  								   IGatewayStatusNotifier,IGuiLog,
	  								   IGatewayMessageNotify{

	Logger logger=Logger.getLogger(this.getClass());
	private int recieveCounter=0;
	private int callCounter=0;
	/** представление модема  */
	private Modem modem;
	/** представление очереди отправки  */
	private QueueSend queueSend;
	/** представление очереди приема  */
	private QueueRecieve queueRecieve;
	
	public ConsoleMain(CommonObject common_object,
			 				Calendar processNotDeliveredTime,
			 				Calendar processRepeatTime){
		/** объект, который служит индикатором для начала и продолжения работы потоков-выводов графической информации */
		Object notifier=new Object();
		// start element's
		try{
			// INFO инициализация всех объектов, и их связки друг с другом
			String comPortName=common_object.getComPortName();
			String comPortSpeed=common_object.getComPortSpeed();
			
			logger.info(" запуск программы ");
			//new GuiWaiter(this.textLog,".",1000,notifier);
			modem=new Modem(comPortName,
								 new Integer(comPortSpeed),
								 (IGatewayStatusNotifier)this,
								 (IGatewayMessageNotify)this,
								 (IGuiLog)this);
			logger.debug("контекст модема успешно создан");
			synchronized(notifier){
				notifier.notify();
			}
			
			logger.info("Попытка подключения к базе данных ");
			if(Connector.initConnector(common_object.getDataBaseUrl(), 
									   common_object.getDataBaseUser(), 
									   common_object.getDataBasePassword(),
									   (IGuiLog)this)){
				logger.info("Подключение к базе данных прошло успешно: "+common_object.getDataBaseUrl());
			}else{
				logger.error("Ошибка подключения к базе данных");
			}
			logger.debug("modem created");
			
			/** настройки для програмы  */
			EngineSettings.loadAndInitReader(common_object.getModemNumber(), 15000);
			
			// TODO EngineSettings.getDelayForGetMessageForSend()
			this.queueSend=new QueueSend(modem, // интерфейс для отправки сообщений
										 20000, // время в милисекундах для опроса базы данных на наличие заданий 
										 (IGuiLog)this // интерфейс для вывода логгируемых сообщений 
										 );
			logger.info("Контролер отправки создан");
			
			// создание демонов-контроллеров ( возможно им нужно дать статус демонов )
				// через какое время должны быть обработаны не подтвержденные о доставке сообщения
			// controllerSended=new ControllerSended(1000*60*1,new MessageManagerNew((IGuiLog)this));
			// logger.info("Контролер поиска повторных SMS сообщений создан");
			// controllerSended.start();
			// logger.info("Контролер не доставленных сообщений создан");
			
				// через какое время должны быть повторены сообщения, которые поставлены в очередь на повторение
			logger.info("Контролер поиска повторных SMS сообщений запущен");
			// TODO контроллер функций повторных SMS сообщений EngineSettings.getDelayForExecuteRepeatController()
			new FunctionExecutor(1000*60*1);
			
			this.queueSend.startService();
			logger.info("Контролер отправки SMS сообщений запущен");
			
			logger.debug("queue started created ");
			
			queueRecieve=new QueueRecieve((IGuiLog)this);
			logger.info("Контролер доставки SMS сообщений создан");
			
			// установить оповещатель звонков
			modem.addCallListener(this);
			modem.addCallListener(queueRecieve);
			
			// установить оповещатель входящих сообщений
			modem.listenerAdd(this);
			modem.listenerAdd(queueRecieve);
			// установить пост обработчик посланных сообщений
			modem.addMessageOutputListener(this);
			// установить оброботчик сообщений после отправки  
			modem.setActionAfterSend(queueSend);
			
			logger.debug("modem started");
			logger.info("попытка запуска контекста модема ... ");
			modem.startService();
			logger.info("контекст модема запущен");
		}catch(Exception ex){    
			synchronized(notifier){
				notifier.notify();
			}
			logger.error(ex.getMessage());
			logger.error("System.exit");
			try{
				System.exit(1);
			}catch(Exception ex1){
				
			}
		}

	}

	public void inputMessage(MessageInput message) {
		this.recieveCounter++; 
		// this.labelMessageRecieve.setText(Integer.toString(this.recieveCounter));
		logger.info("Входящее сообщение: "+message.getRecipient()+" : "+message.getText()+"  порядковый номер:"+this.recieveCounter);
		if(message.isDeliveryMessage()){
			this.addInformation("Delivery Report:"+message.getRecipient());
			logger.info("Delivery Report:"+message.getRecipient());
			logger.info(message);
		}else{
			this.addInformation("Входящее сообщение от:"+message.getRecipient());
			logger.info("Входящее сообщение от:"+message.getRecipient());
			logger.info(message);
		}
	}

	public void inputCall(CallInput call) {
		this.callCounter++;
		//this.labelCallInput.setText(Integer.toString(this.callCounter));
		logger.info("Входящий звонок: "+call.getRecipient()+" порядковый номер:"+this.callCounter);
		logger.info(call);
		this.addInformation("Input Call:"+call.getRecipient());
	}

	public void messageOutput(MessageOutput message, boolean sended) {
		if(sended==true){
			// this.labelMessageSend.setText(Integer.toString(this.sendCounter));
			// this.labelMessageSend.setForeground(Color.BLACK);
			this.addInformation("Sended SMS:"+message.getOutboundMessage().getRecipient());
		}else{
			// действие если сообщение было передано с ошибкой
			// this.labelMessageSend.setForeground(Color.RED);
			this.addInformation("ERROR Sended SMS for:"+message.getOutboundMessage().getRecipient());
		}
	}

	public void gateWayNotifier(GatewayStatuses status) {
		if(status.equals(GatewayStatuses.FAILURE)){
			this.logger.info("MODEM STATUS:"+status.toString());
			//this.setBackground(Color.red);
			logger.info(status.toString());
		}
		if(status.equals(GatewayStatuses.RESTART)){
			this.logger.info("MODEM STATUS:"+status.toString());
			//this.setBackground(Color.GREEN);
			logger.info(status.toString());
		}
		if(status.equals(GatewayStatuses.RUNNING)){
			this.logger.info("MODEM STATUS:"+status.toString());
			//this.setBackground(Color.BLUE);
			logger.info(status.toString());
		}
		if(status.equals(GatewayStatuses.STOPPED)){
			this.logger.info("MODEM STATUS:"+status.toString());
			//this.setBackground(Color.PINK);
			logger.info(status.toString());
		}
	}

	public void notifyMessage(String message) {
		this.addInformation(message);
	}

	public void addInformation(String information) {
		logger.info(information);
	}
}
