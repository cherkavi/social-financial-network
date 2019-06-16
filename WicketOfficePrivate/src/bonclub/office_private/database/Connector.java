package bonclub.office_private.database;
import org.hibernate.Session;
import bonclub.office_private.database.wrap.*;


import java.sql.Connection;

/** класс, который производит соединение с базой данных, посредстом Hibernate и Connection */
public class Connector {
	/** соединение с локальной базой данных */
	private HibernateConnection hibernateConnection=null;
	/** соединение с родительской базой данных, которая содержит все */
	private IConnector oracleConnection=null;
	
	// INFO - место присоединения всех классов для активации отображения объектов базы данных на объекты программы
	private static Class<?>[] classOfDatabase=new Class[]{
		   Message_Recieve.class,
		   Message_Send.class,
		   Message_Text.class,
		   Reference_Recipient.class,
		   Users.class,
		   Parameters.class
		   };
	
	public Connector() throws Exception{
		//this("D:\\eclipse_workspace\\OfficePrivate\\DataBase\\office_private.gdb","SYSDBA","masterkey",10);
		this("messenger","SYSDBA","masterkey",10);
		oracleConnection=new OracleConnection(null, null,null,10);
	}

	
	/**  
	 * @param pathToDataBase - путь к базе данных ("D:\\eclipse_workspace\\BonPay\\DataBase\\bonpay.gdb") 
	 * @param userName - имя пользователя 
	 * @param password - пароль 
	 * @param poolSize - размер пула 
	 * @throws Exception - если не удалось создать Connector  
	 */
	public Connector(String pathToDataBase,
					 String userName, 
					 String password, 
					 int poolSize) throws Exception {
		IConnector connector=new FirebirdConnection(null,pathToDataBase,userName,password,poolSize);
		hibernateConnection=new HibernateConnection(connector,
													"org.firebirdsql.jdbc.FBDriver",
												    "org.hibernate.dialect.FirebirdDialect",
												    classOfDatabase);
		
		
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
	
	/** получить соединение с родительской базой данных */
	public Connection getParentDatabaseConnection(){
		return this.oracleConnection.getConnection();
	}
	
}
