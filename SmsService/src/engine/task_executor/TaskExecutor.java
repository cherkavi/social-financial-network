package engine.task_executor;

/** объект, который выполняет определенные задачи через определенные промежутки времени  */
public abstract class TaskExecutor implements Runnable{
	
	/** объект, который выполняет определенные задачи через определенные промежутки времени
	 * <br>
	 * <b> объект стартует сразу же после создания </b>
	 * @param delayMs - задержка в Милисекундах между вызовами метода {@link #action()}
	 * */
	public TaskExecutor(int delayMs){
		this(delayMs, null, true);
	}

	/** объект, который выполняет определенные задачи через определенные промежутки времени
	 * <br>
	 * <b> объект стартует сразу же после создания </b>
	 * @param delayMs - задержка в Милисекундах между вызовами метода {@link #action()}
	 * */
	public TaskExecutor(int delayMs, String threadName){
		this(delayMs, null, true);
	}
	
	/** остановить выполнение потока  */
	public void stopThread(){
		this.flagRun=false;
	}
	
	/** флаг, который сигнализирует о необходимости продолжения работы потока */
	private volatile boolean flagRun=false;
	/** время задержки перед очередным вызовом функции */
	private int delayMs=0;
	
	private TaskExecutor(int delayMs, String threadName, boolean isDaemon){
		this.delayMs=delayMs;
		Thread thread=new Thread(this);
		if(isDaemon==true){
			thread.setDaemon(true);
		}
		if(threadName!=null){
			thread.setName("TaskExecutor");
		}
		flagRun=true;
		thread.start();
	}
	
	/** действие, которое будет выполняться через заданные моменты времени */
	public abstract void action();
	
	@Override
	public void run() {
		while(flagRun==true){
			try{
				action();
				Thread.sleep(this.delayMs);
			}catch(Exception ex){
				System.err.println("TaskExecutor#run Exception: "+ex.getMessage());
			}
		}
	}

}
