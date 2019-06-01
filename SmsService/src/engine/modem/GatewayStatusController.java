package engine.modem;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.smslib.AGateway.GatewayStatuses;
import org.smslib.modem.SerialModemGateway;

/** класс-поток который просмотривает в указанные промежутки времени в отдельном потоке статус Gateway, и оповещает в случае изменения слушателей*/
public class GatewayStatusController implements Runnable,IGatewayMessageNotify{
	private Logger logger=Logger.getLogger(this.getClass());
	private ArrayList<IGatewayStatusNotifier> listeners=new ArrayList<IGatewayStatusNotifier>();
	private Thread thread;
	
	/** добавить слушателя */
	public void addListener(IGatewayStatusNotifier listener){
		synchronized(listeners){
			this.listeners.add(listener);
		}
	}
	/** удалить слушателя */
	public void removeListener(IGatewayStatusNotifier listener){
		synchronized(listeners){
			this.listeners.remove(listener);
		}
	}
	/** очистить всех слушателей */
	public void clearListener(){
		synchronized(listeners){
			this.listeners.clear();
		}
	}
	
	/** шлюз, за которым необходимо следить */
	private SerialModemGateway gateWay;
	/** кол-во милисекунд, на которые останавливается поток выполнения */
	private long delayMs;
	/** флаг, который позволяет потоку продолжать свое управление */
	private boolean flagRun=false;
	
	/** объект, который следит за изменением состояния шлюза, и оповещение слушателей об изменениях в статусе 
	 * @param gateWay
	 * @param delayMs
	 * */
	public GatewayStatusController(SerialModemGateway gateWay, long delayMs){
		this.gateWay=gateWay;
		this.delayMs=delayMs;
	}
	
	/** останавить сервис */
	public void stopService(){
		this.flagRun=false;
	}
	
	/** запустить сервис */
	public void startService(){
		this.flagRun=true;
		this.thread=new Thread(this);
		//this.thread.setDaemon(true);
		this.thread.start();
	}
	
	/** оповестить всех слушателей */
	public void notifyAllListeners(GatewayStatuses status){
		try{
			for(IGatewayStatusNotifier element:this.listeners){
				try{
					element.gateWayNotifier(status);
				}catch(NullPointerException nex){};
			}
		}catch(Exception ex){
			
		}
	}
	
	private GatewayStatuses currentStatus=GatewayStatuses.RESTART;

	
	public void run() {
		GatewayControllerNotifier controller=new GatewayControllerNotifier(this, 5000,"Модем отключён", 2000); 
		while(this.flagRun){
			logger.debug("запуск тела потока");
			logger.debug("старт контроллера, который выдёргивает данный поток из залипания");
			controller.startService();
			try{
				logger.debug("если нет ответа от GateWay - падает в состояние ожидания, и контроллер его выдернет ");
				//System.out.println();
				this.gateWay.getSignalLevel();
				logger.debug("получить кол-во сообщений, которые были посланы через модем");
				this.gateWay.getOutboundMessageCount();
				logger.debug("проверить на различные состояния данного объекта и модема ");
				if(!this.currentStatus.equals(this.gateWay.getStatus())){
					logger.debug("состояния различны ( not RUNNING, то есть либо выключается либо уже выключен ) - остановить сервис ");
					controller.stopService();
					this.currentStatus=this.gateWay.getStatus();
					logger.debug("состояние: "+this.currentStatus.toString());
					notifyAllListeners(this.currentStatus);
				}
				try{
					Thread.sleep(this.delayMs);
				}catch(Exception ex){}
			}catch(Exception ex){
				logger.warn("Exception(возможно произошло выключение): "+ex.getMessage(), ex);
				controller.stopService();
				// попадает при выключении
				if(ex!=null){
					logger.error("GatewayStatusController:"+ex.getMessage(),ex);
					try{
						notifyAllListeners(GatewayStatuses.RESTART);
						this.gateWay.stopGateway();
						this.gateWay.startGateway();
						notifyAllListeners(GatewayStatuses.RUNNING);
						this.currentStatus=GatewayStatuses.RUNNING;
					}catch(Exception exStartStop){
						logger.error("Exception: "+exStartStop.getMessage(),ex);
					}
				}
			}
			controller.stopService();
		}
	}
	
	public void finalize(){
		try{
			logger.error("GetawayStatusControl was cleared");
		}catch(Exception ex){
			System.err.println("GetawayStatusControl was cleared");
		}
	}
	
	private ArrayList<IGatewayMessageNotify> messageNotifiers=new ArrayList<IGatewayMessageNotify>();

	/** добавить текстового слушателя */
	public void addMessageNotify(IGatewayMessageNotify messanger){
		messageNotifiers.add(messanger);
	}
	
	/** удалить  текстового слушателя */
	public void removeMessageNotify(IGatewayMessageNotify messanger){
		messageNotifiers.remove(messanger);
	}
	
	/** очистить всех текстовых слушателей */
	public void clearAllMessageNotify(IGatewayMessageNotify messanger){
		messageNotifiers.clear();
	}
	
	
	public void notifyMessage(String message) {
		for(IGatewayMessageNotify notifier:messageNotifiers){
			notifier.notifyMessage(message);
		}
	}
	
}
