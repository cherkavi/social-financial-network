package bonclub.reports.engine;

import database.ConnectWrap;
import database.StaticConnector;

public class ReportEngine {
	private static ReportMaker reportMaker;
	static{
		// static constructor;
		// все отчеты с состоянием "IN_PROCESS" перевести в состояние NEW
		updateParametersIntoTable();
		// создать объект и запустить его
		reportMaker=new ReportMaker();
		reportMaker.start();
	}
	
	/** оповестить "создатель отчетов" о наличии отчета на выполнение  */
	public static void notifyAboutNewReport(){
		reportMaker.notifyAboutNewReport();
	}
	
	/** все отчеты с состоянием "IN_PROCESS" перевести в состояние NEW */
	private static void updateParametersIntoTable(){
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			
			connector.getConnection().createStatement().executeUpdate("update "+ConnectWrap.schemePrefix+"SYS$REPORT_TASK set CD_REPORT_TASK_STATE='NEW' where CD_REPORT_TASK_STATE='IN_PROCESS'");
			connector.getConnection().commit();
		}catch(Exception ex){
			System.err.println("ReportEngine#updateParameterIntoTable Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
	}
}
