package gui.Utility;

import javax.swing.JTextArea;

/** класс, который с указанным временным промежутком начинает добавлять указанный символ в конструкторе к тексту в JTextArea */
public class GuiWaiter extends Thread implements IGuiWaiterBreaker{
	private JTextArea text;
	/** кол-во милисекунд, который должен ждать процесс */
	private long delayMs;
	/** флаг, который поддерживает работу потока */
	private boolean flagRun=true;
	/** переменная, которая будет добавлена в лог как индикатор ожидания */
	private String value;

	/** текст, который должен быть добавлен к данному текстовому компоненту 
	 * @param text - место вывода графической информации 
	 * @parm value - переменная, которая будет добавлена в лог как индикатор ожидания 
	 * @param delayMs - время в милисекундах через которое будет добавлена данная переменная 
	 * @param notifier - оповещатель, который ждет notify()
	 * */
	public GuiWaiter(JTextArea text, 
					 String value, 
					 long delayMs, 
					 Object notifier){
		super();
		this.value=value;
		this.text=text;
		this.delayMs=delayMs;
		new GuiWaiterBreaker(notifier, this);
		this.start();
	}
	
	public void run(){
		while(flagRun==true){
			this.text.insert(value, 0);
			this.text.append(this.value);
			try{
				Thread.sleep(this.delayMs);
			}catch(Exception ex){
				System.err.println("GuiWaiter run Exception:"+ex.getMessage());
			}
		}
	}

	public void stopProcess() {
		this.flagRun=false;
	}
	 
	public void finalize(){
		System.out.println("GuiWaiter was clear");
	}
}
