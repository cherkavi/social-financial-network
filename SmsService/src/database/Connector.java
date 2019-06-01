package database;

import java.sql.Connection;

import gui.Utility.IGuiLog;

/** класс, который выдает Session */
public class Connector {
	private static String userName=null;
	private static String password=null;
	private static String databaseUrl=null;
	private static IGuiLog guiLog;
	private static ConnectionProvider provider=null;
	
	public static boolean initConnector(String databaseUrl, 
										String userName, 
										String password,
										IGuiLog guiLog){
		try{
			Connector.databaseUrl=databaseUrl;
			Connector.userName=userName;
			Connector.password=password;
			Connector.guiLog=guiLog;
			//Loader loader=new Loader("c:\\settings.xml");
			//connect=new HibernateFirebirdConnect(loader.getString("//SETTINGS/PATH_TO_DATABASE", "").trim(), "SYSDBA", "masterkey", 50);
			/*connect=new HibernateFirebirdConnect("D:\\eclipse_workspace\\SmsService\\DataBase\\smsservice.gdb",
												 "SYSDBA", 
												 "masterkey", 
												 50,
												 DSmsMessages.class
												 );*/
/*			connect=new HibernateOracleConnect("jdbc:oracle:thin:@91.195.53.27:1521:demo",
											   "bc_sms",
											   "bc_sms",
											   20,
											   DSmsMessages.class);
			connect=new HibernateOracleConnect(databaseUrl,
											   userName,
											   password,
											   20);
											   
*/
			provider=new ConnectionProvider("oracle.jdbc.driver.OracleDriver",Connector.databaseUrl,Connector.userName, Connector.password);
			return true;
		}catch(Exception ex){
			System.out.println("File settings.xml is not found: "+ex.getMessage());
			return false;
		}
	}
	
	/** Get Session from Pool 
	@SuppressWarnings("deprecation")
	public static Session getSession(){
		while(true){
			try{
				if(!connect.getSession().connection().isClosed())
					return connect.getSession();
			}catch(Exception ex){
				System.err.println("Reconnect:"+ex.getMessage());
				guiLog.addInformation("DataBase trying reconnect:");
				try{
					Thread.sleep(20000);
				}catch(Exception ex2){};
				initConnector(Connector.databaseUrl,Connector.userName, Connector.password,Connector.guiLog);
			}
		}
	}
	
	// close Session 
	public static void closeSession(Session session){
		if(session!=null){
			try{
				session.close();
			}catch(Exception ex){
			}
		}
	}
	
	// close Connector
	public static void close(){
		connect.close();
	}
	
	*/
	
	
	public static Connection getConnection(){
		while(true){
			try{
				return provider.connection();
			}catch(Exception ex){
				System.err.println("Reconnect:"+ex.getMessage());
				guiLog.addInformation("DataBase trying reconnect:");
				try{
					Thread.sleep(10000);
				}catch(Exception ex2){};
				initConnector(Connector.databaseUrl,Connector.userName, Connector.password,Connector.guiLog);
				
			}
		}
	}
	
	
	/** получить имя схемы для работы с БД Oracle<br> 
	 * @return 
	 * 	<ul> пустую строку  </ul>
	 * 	<ul> bc_admin. <i>(точка в конце присутствует, то есть разделитель не нужен ) </i></ul>
	 * */
	public static String getScheme(){
		return "bc_admin.";
	}
}
