package database;

public class StaticConnector {
	/** полный URL к базе данных  jdbc:oracle:thin:@91.195.53.27:1521:demo */
	public static String databaseUrl;
	/** логин к базе данных  */
	public static String login;
	/** пароль к базе данных  */
	public static String password;

	static{
		// статический конструктор
		// load();
	}

	public static void load(){
		try {
			// INFO OfficePrivatePartnerReporter JDBC.connect
			//String login="bc_reports";
			//String password="bc_reports";
			//String databaseUrl="jdbc:oracle:thin:@91.195.53.27:1521:demo";
			hibernateConnection=ReportConnector.getInstance(databaseUrl, login, password,20);
			System.out.println("Connect OK:"+databaseUrl+" ("+login+")");
		} catch (Exception e) {
			System.err.println("OfficePrivatePartnerApplication init() Connector: "+e.getMessage());
		}
	}
	
	//@SpringBean(name="hibernateConnection")
	private static ReportConnector hibernateConnection=null;
	
	/** получить класс-обертку для соединения с базой данных */
	public static ConnectWrap getConnectWrap(){
		if(hibernateConnection==null){
			load();
		}
		return new ConnectWrap(hibernateConnection);
	}
	
}
