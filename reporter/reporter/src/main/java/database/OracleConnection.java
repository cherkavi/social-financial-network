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


public class OracleConnection implements IConnector{
	private String databaseUrl; // строка вида: "jdbc:oracle:thin:@91.195.53.27:1521:demo"
	private String userName;
	private String password;
	//private FirebirdPoolConnect poolConnector;
	private PoolingDataSource dataSource;
	
	/** объект, который возвращает Connection к базе данных Oracle
	 * @param pathToDatabase - полный путь к базе данных Oracle ("jdbc:oracle:thin:@91.195.53.27:1521:demo") 
	 * @param userName - имя пользователя 
	 * @param password - пароль
	 * @param maxActiveConnection - максимальное кол-во проинициированных соединений 
	 */
	public OracleConnection(String pathToDatabase, 
							  String userName, 
							  String password,
							  int maxActiveConnection){
		if(pathToDatabase==null){
			this.databaseUrl="jdbc:oracle:thin:@91.195.53.27:1521:demo";			
		}else{
			this.databaseUrl=pathToDatabase;
		}
		if(userName==null){
			this.userName="bc_office";
		}else{
			this.userName=userName;
		}
		if(password==null){
			this.password="office";
		}else{
			this.password=password;
		}
		
		this.dataSource=this.getPoolDataSource(this.getDatabaseUrl(), this.userName, this.password, maxActiveConnection);
	}
	
	@Override
	public Connection getConnection(){
		try{
			Connection connection=this.dataSource.getConnection();
			connection.setAutoCommit(false);
			return connection;
		}catch(SQLException ex){
			System.err.println("OracleConnection#getConnection: "+ex.getMessage());
			return null;
		}
		 
	}
	
	
	private String getDatabaseUrl(){
        //String driverName = "org.firebirdsql.jdbc.FBDriver";
        return this.databaseUrl; 
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
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex){
			System.err.println("OracleConnection#getPoolDataSource Exception:"+ex.getMessage());
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
/*
	public static void main(String[] args){
		System.out.println("Begin:");
		OracleConnection connection=new OracleConnection(null, null,null,10);
		System.out.println("Get connection from Oracle pool: "+connection.getConnection());
		System.out.println("End:");
	}
*/	
}



