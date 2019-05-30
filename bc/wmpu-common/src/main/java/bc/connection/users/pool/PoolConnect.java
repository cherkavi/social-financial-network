package bc.connection.users.pool;

import java.beans.PropertyVetoException;
import java.sql.Connection;

import BonCard.DataBase.UtilityConnector;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public abstract class PoolConnect {

	protected void outError(Object information){
		//LOGGER.debug("PoolConnect");
		//LOGGER.debug("  ERROR  ");
		//LOGGER.debug(information);
	}

	protected void outDebug(Object information){
		//LOGGER.debug("PoolConnect");
		//LOGGER.debug("  DEBUG  ");
		//LOGGER.debug(information);
	}
	
	private ComboPooledDataSource field_cpds=null;
	private String userName;
	private String password;
	private Integer poolSize;
	
	public PoolConnect(String userName, String password, Integer poolSize){
		this.userName=userName;
		this.password=password;
		this.poolSize=poolSize;
	}
	
	
	/** ������� ��������� POOL */
	public void close(){
		try{
			DataSources.destroy(field_cpds);
		}catch(Exception ex){
			
		}
	}
	
	/** �������� Connection �� POOL */
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
		field_cpds.setJdbcUrl(this.getDataBasePath()); 
		field_cpds.setUser(this.getUserName()); 
		field_cpds.setPassword(this.getPassword());
		
		field_cpds.setMinPoolSize(0);
		field_cpds.setInitialPoolSize(2);
		field_cpds.setAcquireIncrement(1); 
		field_cpds.setMaxPoolSize(this.getMaxPoolSize());
		field_cpds.setBreakAfterAcquireFailure(true);
		field_cpds.setAcquireRetryAttempts(1);
	}
	
	/** ������� Connection - ������� Connection � POOL */
	public void closeConnection(Connection connection){
		UtilityConnector.closeQuietly(connection);
	}
	
	/** �������� ��� ������ �������� 
	 * "org.firebirdsql.jdbc.FBDriver" 
	 * */
	protected abstract String geDriverName();

	/** �������� ��� ������ �������� 
	 * "jdbc:firebirdsql://localhost:3050/D:/temp.gdb?sql_dialect=3"
	 * */
	protected abstract String getDataBasePath();

	/** �������� ��� ������������  */
	protected String getUserName(){
		return this.userName;
	}

	/** �������� ������ ��� ������������  */
	protected String getPassword(){
		return this.password;
	}
	
	/** �������� ������������ ������ POOL */
	protected Integer getMaxPoolSize(){
		return this.poolSize;
	}

	@Override
	public void finalize(){
		this.close();
	}

	/** reset DataSource*/
	public void reset(){
		this.field_cpds.resetPoolManager();
	}
	
	/** ��������� ������ �� ��������������� */
	public boolean isPasswordEquals(String password){
		boolean return_value=false;
		if((password!=null)&&(this.password!=null)){
			return_value=this.password.equals(password);
		}
		return return_value;
	}
	
	/** �������� ���-�� ������� ���������� */
	public int getConnectionCount(){
		try{
			return this.field_cpds.getNumConnectionsAllUsers();
		}catch(Exception ex){
			return -1;
		}
	}
}
