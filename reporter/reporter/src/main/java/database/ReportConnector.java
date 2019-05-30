package database;
import org.hibernate.Session;

import java.sql.Connection;

/** класс, который производит соединение с базой данных, посредстом Hibernate и Connection 
 * */
public class ReportConnector {
	/** соединение с локальной базой данных */
	private HibernateConnection hibernateConnection=null;
	
	// INFO OfficePrivatePartnerReport.место присоединения всех классов для активации отображения объектов базы данных на объекты программы
	private static Class<?>[] classOfDatabase=new Class[]{
		   };
	
	/*public static ReportConnector getInstance() throws Exception{
		return new ReportConnector();
	}*/

	public static ReportConnector getInstance(String pathToDatabase, String userName, String password, int poolSize) throws Exception{
		return new ReportConnector(pathToDatabase, userName, password, poolSize);
	}
	
	/*private ReportConnector() throws Exception{
		//this("D:\\eclipse_workspace\\OfficePrivate\\DataBase\\office_private.gdb","SYSDBA","masterkey",10);
		this("messenger","SYSDBA","masterkey",10);
	}*/

	
	/**  
	 * @param pathToDataBase - путь к базе данных (jdbc:oracle:thin:@91.195.53.27:1521:demo) 
	 * @param userName - имя пользователя 
	 * @param password - пароль 
	 * @param poolSize - размер пула 
	 * @throws Exception - если не удалось создать Connector  
	 */
	private ReportConnector(String pathToDataBase,
					 String userName, 
					 String password, 
					 int poolSize) throws Exception {
		IConnector connector=new OracleConnection(pathToDataBase,userName,password,poolSize);
		
		hibernateConnection=new HibernateConnection(connector,
												    "oracle.jdbc.driver.OracleDriver",
												    "org.hibernate.dialect.OracleDialect",
												    classOfDatabase);
		// IConnector connector=new FirebirdConnection(null, pathToDataBase,userName,password,poolSize);
		/*hibernateConnection=new HibernateConnection(connector,
													"org.firebirdsql.jdbc.FBDriver",
												    "org.hibernate.dialect.FirebirdDialect",
												    classOfDatabase);
		*/
	}
	
	
	/** получить Hibernate Session */
	public Session openSession(Connection connection){
		return hibernateConnection.openSession(connection);
	}
	
	/** получить соединение с базой данных */
	public Connection getConnection(){
		return hibernateConnection.getConnection(); 
	}
	
	/** закрыть Hibernate сессию */
/*	public static void closeSession(Session session){
		try{
			session.disconnect();
			session.close();
		}catch(Exception ex){
			System.out.println("Connector#closeSession Exception: "+ex.getMessage());
		};
	}
*/	
	public void closeSession(Session session, Connection connection){
		try{
			session.disconnect();
		}catch(Exception ex){
			System.out.println("Connector#disconnectSession Exception: "+ex.getMessage());
		};
		try{
			session.close();
		}catch(Exception ex){
			System.out.println("Connector#closeSession Exception: "+ex.getMessage());
		};
		try{
			connection.close();
		}catch(Exception ex){
			System.out.println("Connector#closeConnection Exception: "+ex.getMessage());
		};
	}
	
	
}
