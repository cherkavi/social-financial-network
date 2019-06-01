package gui.Utility;

/** класс, который получает в качестве параметров объект, над которым будет произведено Notify, и в этом случае будет необходимо остановить переданную ссылку на поток */
public class GuiWaiterBreaker extends Thread{
	private Object notifier;
	private IGuiWaiterBreaker thread;
	
	public GuiWaiterBreaker(Object notifier, IGuiWaiterBreaker thread){
		super();
		this.notifier=notifier;
		this.thread=thread;
		this.start();
	}
	
	@Override
	public void run(){
		try{
			synchronized(this.notifier){
				this.notifier.wait();
			}
		}catch(Exception ex){
			System.err.println("GuiWaiterBreaker Exception:"+ex.getMessage());
			ex.printStackTrace();
		}
		this.thread.stopProcess();
	}

	public void finalize(){
		System.out.println("GuiWaiterBreaker was clear");
	}
	
}
