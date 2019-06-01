package gui.Frames.Main;

import database.Connector;

import engine.EngineSettings;
import engine.database.DatabaseProxy;
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
import gui.Utility.GuiWaiter;
import gui.Utility.IGuiLog;
import gui.Utility.JInternalFrameParent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayDeque;
import java.util.Calendar;

import javax.swing.*;

import org.apache.log4j.Logger;
import org.smslib.AGateway.GatewayStatuses;

/** главная форма, которая отображает окно рабочей программы */
public class FrameMain extends JInternalFrameParent 
					   implements IMessageInputListener, 
					   			  ICallListener,
					   			  IMessageOutputListener,
					   			  IGatewayStatusNotifier,IGuiLog,
					   			  IGatewayMessageNotify{
	private static final long serialVersionUID=1L;
	private Logger logger=Logger.getLogger(this.getClass());
	private JLabel labelMessageSend;
	private int recieveCounter=0;
	private int sendCounter=0;
	private JLabel labelMessageRecieve;
	private int callCounter=0;
	private JLabel labelCallInput;
	private Modem modem;
	private QueueSend queueSend;
	private JTextField textFieldRecipient;
	private JTextArea textAreaMessage;
	private QueueRecieve queueRecieve;
	// private ControllerSended controllerSended;
	private JButton buttonSend;
	private JTextArea textLog;
	
	
	/** главная форма, которая отображает окно рабочей программы  
	 *  @param desktop - главное окно работы программы 
	 *  @param common_object - объект, в котором содержится вся необходимая информация
	 *  @param processNotDeliveredTime - время через какое должны быть обработаны не подтвержденные о доставке сообщения (получают флаг не доставлены)
	 *  ( Учитываются: Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE,Calendar.SECOND) 
	 *  @param processRepeatTime - время, через какое время должны быть заново поставлены в очередь не подтвержденные о доставке сообщения ( все не доставленные сообщения должны быть либо повторены, либо списаны в архив) 
	 *  ( Учитываются: Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE,Calendar.SECOND) 
	 */
	public FrameMain(JDesktopPane desktop,
					 CommonObject common_object,
					 Calendar processNotDeliveredTime,
					 Calendar processRepeatTime){
		super(desktop,common_object, "Main", 400, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initComponents();
		/** объект, который служит индикатором для начала и продолжения работы потоков-выводов графической информации */
		Object notifier=new Object();
		// start element's
		try{
			// INFO инициализация всех объектов, и их связки друг с другом
			String comPortName=common_object.getComPortName();
			String comPortSpeed=common_object.getComPortSpeed();
			
			this.addInformation(" Создание контекста для модема ");
			new GuiWaiter(this.textLog,
						  ".",
						  1000,
						  notifier);
			this.modem=new Modem(comPortName,
								 new Integer(comPortSpeed),
								 (IGatewayStatusNotifier)this,
								 (IGatewayMessageNotify)this,
								 (IGuiLog)this);
			synchronized(notifier){
				notifier.notify();
			}
			
			this.addInformation("Попытка подключения к базе данных ");
			if(Connector.initConnector(common_object.getDataBaseUrl(), 
									   common_object.getDataBaseUser(), 
									   common_object.getDataBasePassword(),(IGuiLog)this)){
				this.addInformation("Подключение к базе данных прошло успешно");
			}else{
				this.addInformation("Ошибка подключения к базе данных");
			}
			logger.debug("modem created");

			/** настройки для програмы  */
			EngineSettings.loadAndInitReader(common_object.getModemNumber(), 15000);

			// TODO EngineSettings.getDelayForGetMessageForSend()
			this.queueSend=new QueueSend(modem,
										 20000,
										 (IGuiLog)this
										 );
			logger.debug("Queue created ");
			this.addInformation("Контроллер отправки создан");
			
			// создание демонов-контроллеров ( возможно им нужно дать статус демонов )
				// через какое время должны быть обработаны не подтвержденные о доставке сообщения
			// controllerSended=new ControllerSended(1000*60*1,new MessageManagerNew((IGuiLog)this));
			// this.addInformation("Контролер не доставленных сообщений создан");			
			// controllerSended.start();
			
			// через какое время должны быть повторены сообщения, которые поставлены в очередь на повторение
			this.addInformation("Контролер поиска повторных SMS сообщений запущен");
			// TODO EngineSettings.getDelayForExecuteRepeatController()
			new FunctionExecutor(1000*60*1);

			this.queueSend.startService();
			this.addInformation("Контролер отправки SMS сообщений запущен");
			
			logger.debug("queue started created ");
			
			queueRecieve=new QueueRecieve((IGuiLog)this);
			this.addInformation("Контролер доставки SMS сообщений создан");
			
			modem.addCallListener(this);
			modem.listenerAdd(this);
			modem.addMessageOutputListener(this);
			
			// установить оповещатель звонков
			modem.addCallListener(queueRecieve);
			// установить оповещатель входящих сообщений 
			modem.listenerAdd(queueRecieve);
			// установить обработчик посланных сообщений 
			modem.setActionAfterSend(queueSend);
			
			logger.debug("modem started");
			this.buttonSend.setEnabled(true);
			
			this.addInformation("попытка запуска контекста модема ... ");
			modem.startService();
			this.addInformation("контекст модема запущен");

		}catch(Exception ex){    
			synchronized(notifier){
				notifier.notify();
			}
			JOptionPane.showMessageDialog(this, ex.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
			try{
				this.setClosed(true);
			}catch(Exception ex1){
				
			}
		}
	}
	
	/** return JPanel with component's */
	protected void initComponents(){
		// create component's
		labelMessageSend=new JLabel("0");
		labelMessageRecieve=new JLabel("0");
		labelCallInput=new JLabel("0");
		this.textAreaMessage=new JTextArea("SMS service");
		this.textFieldRecipient=new JTextField("+380979204671");
		buttonSend=new JButton("Put to Database for Send");
		buttonSend.setEnabled(false);
		this.textLog=new JTextArea(10,10);
		
		// add listener's
		buttonSend.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onButtonSend();
			}
		});  
		// placing component's
		JPanel panel_information=new JPanel();
		panel_information.setLayout(new GridLayout(3,2));

		panel_information.add(new JLabel("Message Send:"));
		panel_information.add(labelMessageSend);
		panel_information.add(new JLabel("Message Recieve:"));
		panel_information.add(labelMessageRecieve);
		panel_information.add(new JLabel("Message Call Input:"));
		panel_information.add(labelCallInput);
		
		JPanel panelManager=new JPanel(new BorderLayout());
		panelManager.add(getTitledPanel(this.textFieldRecipient,"Recipient"),BorderLayout.NORTH);
		panelManager.add(getTitledPanel(this.textAreaMessage,"Message Text"),BorderLayout.CENTER);
		
		JPanel panelLog=new JPanel(new BorderLayout());
		panelLog.setBorder(javax.swing.BorderFactory.createTitledBorder("Event LOG"));
		panelLog.add(buttonSend, BorderLayout.NORTH);
		panelLog.add(new JScrollPane(this.textLog),BorderLayout.CENTER);
		panelManager.add(panelLog,BorderLayout.SOUTH);
		
		final JPopupMenu popupLog=new JPopupMenu();
		JMenuItem popupLogClear=new JMenuItem("clear");
		popupLogClear.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				FrameMain.this.clearInformation();
			}
		});
		popupLog.add(popupLogClear);
		this.textLog.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON3){
					popupLog.show(FrameMain.this.textLog, e.getX(), e.getY());
				}
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}
		});
		
		
		JPanel panelMain=new JPanel(new BorderLayout());
		panelMain.add(panelManager,BorderLayout.CENTER);
		panelMain.add(panel_information,BorderLayout.NORTH);
		this.getContentPane().add(panelMain);
		this.revalidate();
	}

	private JPanel getTitledPanel(JComponent component, String title){
		JPanel returnValue=new JPanel(new GridLayout(1,1));
		returnValue.setBorder(javax.swing.BorderFactory.createTitledBorder(title));
		returnValue.add(component);
		return returnValue;
	}
	
	private void onButtonSend(){
		this.putMessageIntoDataBase(this.textFieldRecipient.getText(), this.textAreaMessage.getText());
	}
	
	private void putMessageIntoDataBase(String recipient, String text){
		String currentAction="";
		try{
			DatabaseProxy proxy=new DatabaseProxy();
			// INFO query: create new message 
			proxy.putMessageForSend(recipient, text);
			/*currentAction="get Session";
			session=Connector.getSession();
			DSmsMessages message=new DSmsMessages();
			message.setCdSmsMessageType(MessageType.SEND.name());
			message.setRecepient(recipient);
			message.setTextMessage(text);
			message.setIdSendStatus(new Integer(0));
			message.setSendStatusDate(new Date());
			message.setRepeatCount(new Integer(3)); // кол-во повторов для отправки сообщения
			message.setArchiv(new Integer(0));
			message.setActionDate(new Date());

			currentAction="begin Transaction";
			session.beginTransaction();
			currentAction="save to Database";
			session.save(message);
			currentAction="commit transaction";
			session.getTransaction().commit();
			*/
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this, ex.getMessage()+"\n("+currentAction+")","ERROR",JOptionPane.ERROR_MESSAGE);
		}	
	}
	
	
	public void inputMessage(MessageInput message) {
		this.recieveCounter++; 
		this.labelMessageRecieve.setText(Integer.toString(this.recieveCounter));
		logger.debug("InputMessage: "+message.getRecipient()+" : "+message.getText());
		if(message.isDeliveryMessage()){
			this.addInformation("Recieve Delivery for :"+message.getRecipient());
		}else{
			this.addInformation("Recieve SMS from:"+message.getRecipient());
		}
		
	}

	public void inputCall(CallInput call) {
		this.callCounter++;
		this.labelCallInput.setText(Integer.toString(this.callCounter));
		logger.debug("InputCall: "+call.getRecipient()+" : ");
		this.addInformation("Input Call:"+call.getRecipient());
	}

	public void messageOutput(MessageOutput message, boolean sended) {
		if(sended==true){
			// действие если сообщение было успешно послано
			this.sendCounter++;
			this.labelMessageSend.setText(Integer.toString(this.sendCounter));
			this.labelMessageSend.setForeground(Color.BLACK);
			this.addInformation("Sended SMS:"+message.getOutboundMessage().getRecipient());
		}else{
			// действие если сообщение было передано с ошибкой
			this.labelMessageSend.setForeground(Color.RED);
			this.addInformation("ERROR Sended SMS for:"+message.getOutboundMessage().getRecipient());
		}
	}
	
	private ArrayDeque<String> listInformation=new ArrayDeque<String>();
	
	private int limit=50;
	
	public void addInformation(String value){
		if(listInformation.size()>=limit){
			listInformation.removeLast();
		}
		listInformation.addFirst(value);
		fillTextArea(this.textLog);
	}
	
	private void clearInformation(){
		this.listInformation.clear();
		fillTextArea(this.textLog);
	}
	
	private void fillTextArea(JTextArea area){
		area.setText("");
		for(String element: listInformation){
			area.append(element);
			area.append("\n");
		}
	}
	
	public void finalize(){
		this.modem.disconnect();
	}

	public void gateWayNotifier(GatewayStatuses status) {
		if(status.equals(GatewayStatuses.FAILURE)){
			this.logger.debug("STATUS:"+status.toString());
			this.setBackground(Color.red);
			this.setTitle(status.toString());
		}
		if(status.equals(GatewayStatuses.RESTART)){
			this.logger.debug("STATUS:"+status.toString());
			this.setBackground(Color.GREEN);
			this.setTitle(status.toString());
		}
		if(status.equals(GatewayStatuses.RUNNING)){
			this.logger.debug("STATUS:"+status.toString());
			this.setBackground(Color.BLUE);
			this.setTitle(status.toString());
		}
		if(status.equals(GatewayStatuses.STOPPED)){
			this.logger.debug("STATUS:"+status.toString());
			this.setBackground(Color.PINK);
			this.setTitle(status.toString());
		}
		
	}

	public void notifyMessage(String message) {
		this.addInformation(message);
	}
}
