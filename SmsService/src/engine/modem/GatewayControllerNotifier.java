package engine.modem;

import org.apache.log4j.Logger;

/** данный класс служит для проверки нахождения модема в состоянии OnLine */
public class GatewayControllerNotifier implements Runnable{
	private Logger logger=Logger.getLogger(this.getClass());
	private Thread thread;
	/** слушатели, которых нужно оповестить */
	private IGatewayMessageNotify notifyAll;
	/** задержка в милисекундах, после которой нужно начать оповещать слушателей с указанным интервалом */
	private long delayMs;
	/** статус, которым нужно всех оповещать */
	private String message;
	/** флаг запуска, который говорит о продолжении работы потока */
	private volatile boolean flagRun;
	/** пауза в оповещениях */
	private long notifyPause;
	
	/** объект, который оповещает указанных слушателей, в случае "залипания" потока о новом статусе 
	 * @param notifyAll - объект, который содержит оповещатель для указанных переменных
	 * @param delayMs - время задержки, после которого будет происходить оповещение 
	 * @param message - сообщение, которое нужно передавать слушателям 
	 * @param notifyPause - пауза между оповещениями 
	 */
	public GatewayControllerNotifier(IGatewayMessageNotify notifyAll, 
									 long delayMs, 
									 String message, 
									 long notifyPause){
		this.notifyAll=notifyAll;
		this.delayMs=delayMs;
		this.message=message;
		this.notifyPause=notifyPause;
	}

	/** старт потока */
	public void startService(){
		logger.debug("старт потока");
		this.thread=new Thread(this);
		this.flagRun=true;
		this.thread.start();
		// System.out.println("method startService call ");
	}
	
	/** остановка потока */
	public void stopService(){
		logger.debug("остановка потока");
		// System.out.println("method stopService call");
		this.flagRun=false;
		try{
			this.thread.join();
		}catch(Exception ex){
		}
	}

	public void run() {
		/** время, по прошествии которого начнется оповещение*/
		long timeForNotify=System.currentTimeMillis()+this.delayMs;
		/** время, по прошествии которого начнется очередное оповещение */
		long nextTimeNotify=System.currentTimeMillis();
		boolean notifyFlag=false;
		while(this.flagRun){
			if(notifyFlag==true){
				if(System.currentTimeMillis()>nextTimeNotify){
					//System.out.println("режим оповещения");
					this.notifyAll.notifyMessage(this.message);
					nextTimeNotify=System.currentTimeMillis()+this.notifyPause;
				}else{
					//System.out.println("еще не наступило время повторения"); 
				}
			}else{
				// проверка на завершение времени ожидания  
				if(System.currentTimeMillis()>timeForNotify){
					//System.out.println("время ожидания вышло - включен режим оповещения ");
					notifyFlag=true;
				}
			}
			try{
				Thread.sleep(10);
			}catch(Exception ex){
			}
		}
		//System.out.println("GatewayControllerNotifier stop");
	}
}
