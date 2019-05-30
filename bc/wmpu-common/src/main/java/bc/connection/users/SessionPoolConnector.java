package bc.connection.users;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import BonCard.DataBase.UtilityConnector;
import bc.connection.SessionConnector;
import bc.connection.users.pool.PoolConnect;

public class SessionPoolConnector implements SessionConnector{
	private static Logger LOGGER=Logger.getLogger(SessionPoolConnector.class);

	/** pool of Connection for user's [User Name, HibernateOracleConnect] (1:1) */
	private HashMap <String,PoolConnect> connectionUserPool=new HashMap<String,PoolConnect>();
	private UserSessionManager userSession=new UserSessionManager();
	private Class<? extends PoolConnect> connectorClass;
	private Integer poolSize;
	
	public SessionPoolConnector(){
		this(HibernateOracleConnect.class,new Integer(10));
	}
	
	private int errorCode;
	private String errorMessage;
	
	@Override
	public int getErrorCode() {
		return errorCode;
	}
	
	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
	
	/** ������, ������� ������� Pool ���������� �� ���������� �������, � ���������� Connection �� SessionId
	 * @param ���������� �����, ������� ����� ����������� ���������� ���������� 
	 * (String UserName, String Password, Integer poolSize)
	 * 
	 * 
	 * */
	public SessionPoolConnector(Class<? extends PoolConnect> connectorClass, Integer poolSize){
		this.connectorClass=connectorClass;
		if(poolSize!=null){
			this.poolSize=poolSize;
		}
	}
	

	/** �������� Connection �� ��������� ��������� ������ 
	 * @param userName ��� ������������
	 * @param password ������
	 * @param sessionId ���������� ����� ������ 
	 * @return Connection 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * */
	@Override
	public Connection getConnection(String moduleName, 
									String userName, 
            						String password, 
            						String sessionId,
            						String language,
            						String lastConnectionIp,
            						String smsConfirmCode) {
		Connection returnValue;
		// проверка наличия связки - "имя пользователя"-"POOL"
		if(connectionUserPool.containsKey(userName)==true){
			LOGGER.debug("SessionPoolConnector.getConnection(): userName in pool");
			// Pool создан по данному пользователю 
			if(connectionUserPool.get(userName).isPasswordEquals(password)){
				returnValue=connectionUserPool.get(userName).getConnection();
				this.userSession.put(userName, sessionId);
				if(returnValue==null){
					// возможно произошло изменение пароля - или же внешние факторы повлияли на соединение (-я)
					this.removeSessionsByUser(userName);
					// попытаться создать Pool по данному пользователю
					try{
						// создать объект и положить его в хранилище
						Constructor<? extends PoolConnect> constructor=this.connectorClass.getConstructor(String.class,String.class,Integer.class);
						PoolConnect newConnection=constructor.newInstance(userName, password, this.poolSize);
						// получить Connection из указанного Pool
						returnValue=newConnection.getConnection();
						if(returnValue==null){
							throw new Exception();
						}
						this.connectionUserPool.put(userName, newConnection);
						this.userSession.put(userName, sessionId);
					}catch(Exception ex){
						returnValue=null;
						LOGGER.error("SessionPoolConnector Exception: "+ex.getMessage());
					}
				}
			} else {
				// Password is incorrect
				// попытаться создать Pool по данному пользователю
				try{
					// создать объект и положить его в хранилище
					Constructor<? extends PoolConnect> constructor=this.connectorClass.getConstructor(String.class,String.class,Integer.class);
					PoolConnect newConnection=constructor.newInstance(userName, password, this.poolSize);
					// получить Connection из указанного Pool
					returnValue=newConnection.getConnection();
					if(returnValue==null){
						throw new Exception();
					}
					this.connectionUserPool.put(userName, newConnection);
					this.userSession.put(userName, sessionId);
				}catch(Exception ex){
					returnValue=null;
					LOGGER.error("SessionPoolConnector Exception: "+ex.getMessage());
				}
			}
		}else{
			LOGGER.debug("SessionPoolConnector.getConnection(): userName NOT in pool");
			// Pool не создан по данному объекту
			try{
				// создать объект и положить его в хранилище
				LOGGER.debug("SessionPoolConnector.getConnection(): constructor");
				Constructor<? extends PoolConnect> constructor=this.connectorClass.getConstructor(String.class,String.class,Integer.class);
				LOGGER.debug("SessionPoolConnector.getConnection(): newConnection");
				PoolConnect newConnection=constructor.newInstance(userName, password, this.poolSize);
				LOGGER.debug("SessionPoolConnector.getConnection(): newConnection.getConnection()");
				// получить Connection из указанного Pool
				returnValue=newConnection.getConnection();
				if(returnValue==null){
					LOGGER.debug("SessionPoolConnector.getConnection(): returnValue=null");
					throw new Exception();
				}
				this.connectionUserPool.put(userName, newConnection);
				this.userSession.put(userName, sessionId);
			}catch(Exception ex){
				returnValue=null;
				LOGGER.error("SessionPoolConnector Exception: "+ex.getMessage());
			}
		}
		return returnValue;
	}


	/** ������� Connection � POOL */
	@Override
	public void closeConnection(Connection connection){
		UtilityConnector.closeQuietly(connection);
	}

	/** �������� Connection �� POOL �� ���������� ����������� �������������� 
	 * @param sessionId - ���������� ����� ������, �� �������� ����� �������� Connection
	 * */
	@Override
	public Connection getConnection(String sessionId){
		String user=this.userSession.getUser(sessionId);
		
		if(user==null){
			return null;
		}else{
			return this.connectionUserPool.get(user).getConnection();
		}
	}

	@Override
	public void removeSessionId(String sessionId){
		String user=this.userSession.getUser(sessionId);
		this.userSession.clear(sessionId);
		if(this.userSession.hasSessionsByUser(user)==false){
			try{
				this.connectionUserPool.get(user).reset();
			}catch(Exception ex){
				// user:Pool is not finded
			}
		}
	}
	
	/** �������� ���-�� �������������, �� ������� ������ PoolConnection */
	public int getUserPoolCount(){
		return this.connectionUserPool.size();
	}
	
	/** �������� ����� �������������, �� ������� ������� ���� */
	public Set<String> getUsersIntoPool(){
		return this.connectionUserPool.keySet();
	}
	
	/** �������� �� ���������� ������������ ���-�� �������� ���������� */
	public int getConnectionCountByUser(String user){
		return this.connectionUserPool.get(user).getConnectionCount();
	}
	
	public void printConnection(PrintStream out){
		if(out==null){
			out=System.out;
		}
		out.println("Connection:");
		Set<String> users=getUsersIntoPool();
		for(String user:users){
			out.println("User:"+user+"    ConnectionCount:"+getConnectionCountByUser(user));
		}
		out.println("-----------");
	}
	
	public Map<String, String> getConnectionMap(){
		Set<String> users=getUsersIntoPool();
		Map<String, String> mp = new HashMap<String, String>();
		for(String user:users){
			String cnt = "" + getConnectionCountByUser(user);
			mp.put(user, cnt);
		}
		return mp;
	}
	
	 /** удалить по указанному пользователю все соединения */
	@Override
	public void removeSessionsByUser(String userName){
        try{
            this.connectionUserPool.get(userName).reset();
            this.userSession.removeUser(userName);
        }catch(Exception ex){
            LOGGER.error("SessionPoolConnector#removeSessionByUser UserName:"+userName+"  close Exception:"+ex.getMessage());
        }
    }


}
