package bonclub.office_private.database.pool;

import java.beans.PropertyVetoException;
import java.sql.Connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public abstract class PoolConnect {

	protected void outError(Object information){
		//System.out.print("PoolConnect");
		//System.out.print("  ERROR  ");
		//System.out.println(information);
	}

	protected void outDebug(Object information){
		//System.out.print("PoolConnect");
		//System.out.print("  DEBUG  ");
		//System.out.println(information);
	}
	
	private ComboPooledDataSource field_cpds=null;
	private String userName;
	private String password;
	private Integer poolSize;
	private String databaseUrl=null;
	
	public PoolConnect(String userName, String password, Integer poolSize){
		this.databaseUrl=null;
		this.userName=userName;
		this.password=password;
		this.poolSize=poolSize;
	}
	
	public PoolConnect(String databaseUrl, String userName, String password, Integer poolSize){
		this.databaseUrl=databaseUrl;
		this.userName=userName;
		this.password=password;
		this.poolSize=poolSize;
	}
	
	/** закрыть полностью POOL */
	public void close(){
		try{
			DataSources.destroy(field_cpds);
		}catch(Exception ex){
			
		}
	}
	
	/** получить Connection из POOL */
	public Connection getConnection(){
		if(field_cpds==null){
			outDebug("getConnection: pool is not create ");
			try{
				create();
				outDebug("getConnection: NEW pool has been created, getting Connection");
				return field_cpds.getConnection();
			}catch(Exception ex){
				outError("getConnection: Exception:"+ex.getMessage());
				return null;
			}
		}else{
			try{
				outDebug("getConnection: pool has been created, getting Connection");
				return field_cpds.getConnection();
			}catch(Exception ex){
				outError("getConnection: Exception:"+ex.getMessage());
				return null;
			}
		}
	}
	
	private void create() throws PropertyVetoException{
		field_cpds = new ComboPooledDataSource();
		field_cpds.setDriverClass(this.geDriverName());
		if(this.databaseUrl==null){
			// берем путь к базе данных из потомка
			field_cpds.setJdbcUrl(this.getDataBasePath());
		}else{
			// берем путь к базе данных из переданного в конструктор параметра
			field_cpds.setJdbcUrl(this.databaseUrl);
		}
		 
		field_cpds.setUser(this.getUserName()); 
		field_cpds.setPassword(this.getPassword());
		
		field_cpds.setMinPoolSize(1);
		field_cpds.setInitialPoolSize(2);
		field_cpds.setAcquireIncrement(1); 
		field_cpds.setMaxPoolSize(this.getMaxPoolSize());
		field_cpds.setMaxConnectionAge(1);
		field_cpds.setBreakAfterAcquireFailure(true);
		field_cpds.setAcquireRetryAttempts(1);
	}
	
	/** закрыть Connection - вернуть Connection в POOL */
	public void closeConnection(Connection connection){
		try{
			connection.close();
		}catch(Exception ex){
			outError("closeConnection exception:"+ex.getMessage());
		}
	}
	
	/** получить имя класса драйвера 
	 * "org.firebirdsql.jdbc.FBDriver" 
	 * */
	protected abstract String geDriverName();

	/** получить имя класса драйвера 
	 * "jdbc:firebirdsql://localhost:3050/D:/temp.gdb?sql_dialect=3"
	 * */
	protected abstract String getDataBasePath();

	/** получить Имя пользователя  */
	protected String getUserName(){
		return this.userName;
	}

	/** получить Пароль для пользователя  */
	protected String getPassword(){
		return this.password;
	}
	
	/** получить максимальный размер POOL */
	protected Integer getMaxPoolSize(){
		return this.poolSize;
	}

	public void finalize(){
		this.close();
	}

	/** reset DataSource*/
	public void reset(){
		this.field_cpds.resetPoolManager();
	}
	
	/** проверить пароль на эквивалентность */
	public boolean isPasswordEquals(String password){
		boolean return_value=false;
		if((password!=null)&&(this.password!=null)){
			return_value=this.password.equals(password);
		}
		return return_value;
	}
	
	/** получить кол-во текущих соединений */
	public int getConnectionCount(){
		try{
			return this.field_cpds.getNumConnectionsAllUsers();
		}catch(Exception ex){
			return -1;
		}
	}
}
