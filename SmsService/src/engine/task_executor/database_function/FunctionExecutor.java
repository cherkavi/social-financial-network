package engine.task_executor.database_function;

import java.sql.Connection;

import org.apache.log4j.Logger;

import database.Connector;
import engine.database.DatabaseProxy;
import engine.task_executor.TaskExecutor;

/** вызов через определенные промежутки времени  */
public class FunctionExecutor extends TaskExecutor{
	private Logger logger=Logger.getLogger(this.getClass());
	
	public FunctionExecutor(int delayMs) {
		super(delayMs,"Repeat Controller");
	}
	
	private DatabaseProxy proxy=new DatabaseProxy();
	
	@Override
	public void action() {
		Connection connection=Connector.getConnection();
		try{
			logger.debug("begin ResetStatusTakenForSend");
			proxy.checkMaxDeliveryTime();
			logger.debug("-end- ResetStatusTakenForSend");
		}catch(Exception ex){
			String message="FunctionExecutor#action Exception:"+ex.getMessage();
			System.err.println(message);
			logger.error(message);
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
	}

}
