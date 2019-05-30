package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.hibernate.Session;

/** POJO класс, который содержит Connection и Session, а так же успешно закрывает их */
public class ConnectWrap {
	/** префикс схемы, который нужно использовать  */
	public static String schemePrefix=null;
	
	private Connection connection;
	private Session session;
	
	// private final static String SCHEMA_QUERY="SELECT SYS_CONTEXT('bc', 'general_db_scheme') FROM dual";
	private final static String SCHEMA_QUERY="SELECT fnc_get_db_scheme FROM dual";
	
	private void loadSchemePrefix(Connection connection) {
		Statement statement=null;
		try{
			statement=connection.createStatement();
			ResultSet rs=statement.executeQuery(SCHEMA_QUERY);
			if(rs.next()==false){
				throw new IllegalArgumentException("check access query: "+SCHEMA_QUERY);
			};
			schemePrefix=rs.getString(1)+".";
		}catch(Exception ex){
			System.err.println("loadSchemePrefix Exception:"+ex.getMessage());
		}finally{
			try{
				statement.close();
			}catch(Exception ex){};
		}
		
		
	}
	
	/** POJO класс, который содержит Connection и Session, а так же успешно закрывает их */
	public ConnectWrap(ReportConnector connector){
		/** получить соединение из Pool-а */
		connection=connector.getConnection();
		/** создать по данному соединению HibernateSession */
		session=connector.openSession(connection);

		if(schemePrefix==null){
			loadSchemePrefix(connection);
		}
		
	}
	
	public ConnectWrap(Connection connection, Session session){
		this.connection=connection;
		this.session=session;

		if(schemePrefix==null){
			loadSchemePrefix(connection);
		}
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

	public static void close(Connection connection){
		if(connection==null){
			return;
		}
		try {
			connection.close();
		} catch (Exception e) {
		}
	}

	public static void close(Statement statement){
		if(statement==null){
			return;
		}
		try {
			statement.close();
		} catch (Exception e) {
		}
	}

	public static void close(ResultSet rs){
		if(rs==null){
			return;
		}
		try {
			rs.close();
		} catch (Exception e) {
		}
	}
	
	@Override
	public void finalize(){
		if((session!=null)||(connection!=null)){
			this.close();
		}
	}
}
