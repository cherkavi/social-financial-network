package engine.modem;

import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.smslib.ICallNotification;
import org.smslib.IInboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Message.MessageTypes;

public abstract class AbstractModem implements ISender, Runnable, IInboundMessageNotification,ICallNotification{
	
	/** ссылка на поток, который запущен в этом объекте */
	private Thread threadMain;
	
	/** логгер */
	private Logger logger=Logger.getLogger(this.getClass());
	/** задержка в милисекундах для потока, который работает с модемом  */
	private int delayThread=1000;
	/** флаг, который говорит о выполнении потока*/
	private boolean flagRun=false;
	
	/** слушатели, которых нужно оповещать о входящих сообщениях */
	private ArrayList<IMessageInputListener> listeners=new ArrayList<IMessageInputListener>();
	/** сообщения, которые находятся в очереди на отправку */
	private LinkedList<MessageOutput> queueMessageOutput=new LinkedList<MessageOutput>();

	/** слушатели, которых нужно оповещать о входящих звонках */
	private ArrayList<ICallListener> callListener=new ArrayList<ICallListener>();
	
	/** messages that are recieved */
	private LinkedList<MessageInput> queueMessageInput=new LinkedList<MessageInput>();
	/** calls that are dropped */
	private LinkedList<CallInput> queueCallInput=new LinkedList<CallInput>();
	/** слушатели успешно переданных, либо не успешно переданных исходящих сообщения */
	private ArrayList<IMessageOutputListener> messageOutputNotifiers=new ArrayList<IMessageOutputListener>();

	/** объект, который должен быть вызван сразу после отправки Сообщения в модем и получения уникального номера в контексте модема */
	private IActionAfterSend postSendProcessor;
	
	
	/** запуск объекта по работе с GSM модемом на выполнение 
	 * 
	 * */
	public void startService(){
		threadMain=new Thread(this);
		this.flagRun=true;
		threadMain.setName("Modem Worker");
		threadMain.start();
	}
	
	public void stopService(){
		this.flagRun=false;
	}
	
	/** clear all message output listener's*/
	public void messageOutputListenersClear(){
		this.messageOutputNotifiers.clear();
	}

	/** add message listener's*/
	public void addMessageOutputListener(IMessageOutputListener messageOutputListener){
		this.messageOutputNotifiers.add(messageOutputListener);
	}

	/** remove message listener's*/
	public void removeMessageOutputListener(IMessageOutputListener messageOutputListener){
		this.messageOutputNotifiers.remove(messageOutputListener);
	}

	
	/** оповещение всех слушателей исходящих сообщений об успешной передаче данных 
	 * @param message - исходящее сообщение, над которым происходило данное действие  
	 * @param sended - <b><li>(true - сообщение было успешно передано )</li>
	 * <li>(false - сообщение было передано с ошибкой)</li></b>
	 * */
	private void notifyMessageOutputListener(MessageOutput message, boolean sended){
		for(IMessageOutputListener listener:this.messageOutputNotifiers){
			listener.messageOutput(message, sended);
		}
	}
	
	/** clear all call listener */
	public void callListenersClear(){
		this.callListener.clear();
	}
	
	/** add call listener */
	public void addCallListener(ICallListener listener){
		this.callListener.add(listener);
	}
	
	/** remove Call listener */
	public void removeCallListener(ICallListener listener){
		this.callListener.remove(listener);
	}

	
	/**  
	 * notify all listeners about the input call 
	 * */
	private void notifyCallListeners(CallInput call){
		synchronized(this.callListener){
			for(ICallListener listener:this.callListener){
				listener.inputCall(call);
			}
		}
	}
	
	/** to clear all the listeners that recieve the input messages*/
	public void listenersClear(){
		synchronized(this.listeners){
			this.listeners.clear();
		}
	}
	
	/** add listener */
	public void listenerAdd(IMessageInputListener listener){
		synchronized(this.listeners){
			this.listeners.add(listener);
		}
	}
	
	/** remove listener */
	public void listenerRemove(IMessageInputListener listener){
		synchronized(this.listeners){
			this.listeners.remove(listener);
		}
	}
	
	/** notify message listener's */
	protected void notifyMessageInputListener(MessageInput message){
		synchronized(this.listeners){
			for(IMessageInputListener listener:this.listeners){
				try{
					listener.inputMessage(message);
				}catch(Exception ex){
					this.logger.error("Ошибка обработки входящего сообщения: "+ex.getMessage());
				}
			}
		}
	}
	
	/** установить объект, который имплементирует метод, необходимый для обработки сообщения после его отправки */
	public void setActionAfterSend(IActionAfterSend actionAfterSend){
		this.postSendProcessor=actionAfterSend;
	}

	
	public void sendMessage(MessageOutput message) {
		logger.debug("Modem: add message for send:");
		synchronized(this.queueMessageOutput){
			this.queueMessageOutput.addLast(message);
		}
	}

	public void run() {
		while(this.flagRun==true){
			logger.info("начало цикла опроса модема ");
			
			logger.debug("обработка очереди сообщений на отправку");
			this.processQueueMessageOutput();
			
			logger.debug("обработка входящих сообщений"); 
			this.processQueueMessageInput();

			logger.debug("обработка входящих звонков");
			this.processQueueCallInput();
			
			try{
				logger.debug("засыпание потока");
				Thread.sleep(this.delayThread);
			}catch(InterruptedException ex){
				logger.error(" run "+ex.getMessage(),ex);
			}catch(Exception ex){
				logger.error(" run "+ex.getMessage(),ex);
			}
		}
		this.disconnect();
	}

	
	/** обработка сообщений на отправку */
	protected void processQueueMessageOutput(){
		MessageOutput message;
		while(this.queueMessageOutput.size()>0){
			logger.info("получить сообщение из очереди SMSService-a ");
			synchronized (this.queueMessageOutput){
				message=this.queueMessageOutput.removeFirst();
			}
			sendOutputMessage(message);
		}
	}
	
	
	protected void sendOutputMessage(MessageOutput message){
		logger.info("отправить полученное сообщение ");
		/*logger.info("Before: Id:"+message.getOutboundMessage().getId());
		logger.info("Before: MessageId:"+message.getOutboundMessage().getMessageId());
		logger.info("Before: RefNo:"+message.getOutboundMessage().getRefNo());*/
		if(this.sendMessageHardware(message)==true){
			// INFO сообщение отправлено
			logger.info("модем успешно отправил сообщение");
			this.postSendProcessor.actionAfterSend(message,null);
			this.notifyMessageOutputListener(message, true);
		}else{
			// INFO ошибка отправки сообщения модемом
			logger.info("ошибка отправки сообщения модемом");
			
			this.postSendProcessor.actionAfterSend(message,
											       "модем не отправил сообщение, FailureCause:"+message.getOutboundMessage().getFailureCause().name()+
											       " MessageStatus:"+message.getOutboundMessage().getMessageStatus().name());
			this.notifyMessageOutputListener(message, false);
			// throw new Exception("Message was not sended:");
		}
	}
	
	protected abstract boolean sendMessageHardware(MessageOutput message);


	/** обработка полученных сообщений*/
	protected void processQueueMessageInput(){
		MessageInput message;
		while(this.queueMessageInput.size()>0){
			synchronized(this.queueMessageInput){
				message=this.queueMessageInput.removeFirst();
			}
			logger.debug("оповестить слушателей о входящем сообщении");
			this.notifyMessageInputListener(message);
			// remove message from device
			processQueueMessageInputPost(message);
		}
		
	}
	
	
	protected abstract void processQueueMessageInputPost(MessageInput message);

	/** обработка входящих звонков */
	protected void processQueueCallInput(){
		CallInput call;
		//logger.debug("processQueueCallInput: "+this.queueCallInput.size());
		while(this.queueCallInput.size()>0){
			synchronized(this.queueCallInput){
				call=this.queueCallInput.removeFirst();
			}
			logger.debug("оповестить о входящем звонке");
			this.notifyCallListeners(call);
		}
	}
	
	
	public void finalize() {
		this.disconnect();
	}
	
	public abstract void disconnect();
	
	
	public void process(String gatewayId, 
						MessageTypes msgType,
						InboundMessage msg) {
		synchronized(this.queueMessageInput){
			logger.debug("Service input message:"+msg);
			this.queueMessageInput.addLast(new MessageInput(msgType,msg));
		}
	}
	
	public void process(String gatewayId, 
						String callerId) {
		synchronized(this.queueCallInput){
			logger.debug("Service input call:"+callerId);
			this.queueCallInput.addLast(new CallInput(callerId));
		}
	}

}
