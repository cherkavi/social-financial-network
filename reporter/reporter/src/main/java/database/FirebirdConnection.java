package database;

import java.sql.Connection;


import java.sql.SQLException;
import java.util.Properties;


import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;


public class FirebirdConnection implements IConnector{
	private String pathToServer;
	private String pathToDatabase;
	private String userName;
	private String password;
	//private FirebirdPoolConnect poolConnector;
	private PoolingDataSource dataSource;
	
	/** объект, который возвращает Connection к базе данных Firebird
	 * @param pathToServer - путь к серверу 
	 * @param pathToDatabase - путь к базе данных 
	 * @param userName - имя пользователя 
	 * @param password - пароль
	 */
	public FirebirdConnection(String pathToServer, 
							  String pathToDatabase, 
							  String userName, 
							  String password,
							  int maxActiveConnection){
		this.pathToServer=pathToServer;
		this.pathToDatabase=pathToDatabase;
		this.userName=userName;
		this.password=password;
		this.dataSource=this.getPoolDataSource(this.getDatabaseUrl(), this.userName, this.password, maxActiveConnection);
		/*this.poolConnector=new FirebirdPoolConnect(this.getDatabaseUrl(),
												 this.userName, 
												 this.password, 
												 10);*/
	}
	
	@Override
	public Connection getConnection(){
		try{
			return this.dataSource.getConnection();
		}catch(SQLException ex){
			System.err.println("FirebirdConnection#getConnection: "+ex.getMessage());
			return null;
		}
		 
	}
	
	
	private String getDatabaseUrl(){
        //String driverName = "org.firebirdsql.jdbc.FBDriver";
        String database_protocol="jdbc:firebirdsql://";
        String database_dialect="?sql_dialect=3";
        String database_server=null;
        String database_port="3050";
        //String databaseURL = "jdbc:firebirdsql://localhost:3050/d:/work/sadik/sadik.gdb?sql_dialect=3";
        if((pathToServer=="")||(pathToServer==null)){
            database_server="localhost";
        }else{
            database_server=pathToServer;
        };
        return database_protocol+database_server+":"+database_port+"/"+this.pathToDatabase+database_dialect;
	}
	
	@Override
	public void closeAllConnection() {
		//this.poolConnector.close();
		/*try{
	        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
	        driver.closePool("BonPay");
		}catch(Exception ex){
			System.err.println("FirebridConnection#closeAllConnection:"+ex.getMessage());
		}*/
	}

	
	private PoolingDataSource getPoolDataSource(String connectURI, String user, String password, int maxActiveConnection) {
		try{
			Class.forName("org.firebirdsql.jdbc.FBDriver");
		}catch(Exception ex){
			System.err.println("FirebirdConnection#getPoolDataSource Exception:"+ex.getMessage());
		}
		
        //
        // First, we'll need a ObjectPool that serves as the
        // actual pool of connections.
        //
        // We'll use a GenericObjectPool instance, although
        // any ObjectPool implementation will suffice.
        //
        ObjectPool connectionPool = new GenericObjectPool(null,maxActiveConnection);
        /*
        				Integer.parseInt( props.getProperty(Environment.DBCP_MAXACTIVE) ), 
                        Byte.parseByte( props.getProperty(Environment.DBCP_WHENEXHAUSTED) ),
                        Long.parseLong( props.getProperty(Environment.DBCP_MAXWAIT) ),
                        Integer.parseInt( props.getProperty(Environment.DBCP_MAXIDLE) )
		*/
        //
        // Next, we'll create a ConnectionFactory that the
        // pool will use to create Connections.
        // We'll use the DriverManagerConnectionFactory,
        // using the connect string passed in the command line
        // arguments.
        //
        Properties properties=new Properties();
        properties.put("user", user);
        properties.put("password", password);
        
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI,
        																		 properties);
        // ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI,
        //																		    userName,
        //																		    password,
		//		 																	null);

        //
        // Now we'll create the PoolableConnectionFactory, which wraps
        // the "real" Connections created by the ConnectionFactory with
        // the classes that implement the pooling functionality.
        //
        @SuppressWarnings("unused")
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,
        																				    connectionPool,
        																				    null,
        																				    null,
        																				    false,// readOnly
        																				    true// autocommit
        																				    );

        //
        // Finally, we create the PoolingDriver itself,
        // passing in the object pool we created.
        //
        PoolingDataSource dataSource = new PoolingDataSource(connectionPool);

        // For close
        /*
        try{
            PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
            driver.registerPool("BonPay",connectionPool);
        }catch(Exception ex){};
		*/
        
        return dataSource;
    }

	
/*	public static void main(String[] args) throws Exception{
		String dataBaseName="D:/eclipse_workspace/BonPay/DataBase/bonpay.gdb";
		Class.forName("org.firebirdsql.jdbc.FBDriver");
		FirebirdConnection connector=new FirebirdConnection(null,dataBaseName,"SYSDBA","masterkey",20);
		connector.getConnection();
		System.out.println(connector.getConnection());
	}
*/
}



/*	
private Connection getConnectionToDatabase() {
	java.sql.Connection connection=null;
	String databaseURL=this.getDatabaseUrl();
	try{
    	//System.out.println("Попытка загрузить драйвер");
    	Class.forName(driverName);
    	//System.out.println("Попытка соеднинения="+databaseURL);
    	connection=java.sql.DriverManager.getConnection(databaseURL,userName,password);
    	connection.setAutoCommit(false);
	}catch(SQLException sqlexception){
    	System.err.println("не удалось подключиться к базе данных");
	}catch(ClassNotFoundException classnotfoundexception){
    	System.err.println("не найден класс");
	}
	return connection;
}
*/
