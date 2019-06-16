package bonclub.office_private.database;

import java.sql.Connection;

import org.hibernate.Session;

/** POJO класс, который содержит Connection и Session, а так же успешно закрывает их */
public class ConnectUtility {
	private Connection connection;
	private Session session;
	
	public ConnectUtility(){
	}
	
	public ConnectUtility(Connector connector){
		connection=connector.getConnection();
		session=connector.openSession(connection);
	}
	
	public ConnectUtility(Connection connection, Session session){
		this.connection=connection;
		this.session=session;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
	public void close(){
		try{
			session.close();
			session=null;
		}catch(Exception ex){
		}
		try{
			connection.close();
			connection=null;
		}catch(Exception ex){
		}
	}

	public void finalize(){
		if((session!=null)||(connection!=null)){
			this.close();
		}
	}
}
