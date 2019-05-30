package bc.connection.users;

import bc.connection.users.pool.PoolConnect;
import bc.objects.bcParamXML;
import org.apache.log4j.Logger;

class HibernateOracleConnect extends PoolConnect{

	private final static String JDBC_ORACLE_DRIVER_NAME="oracle.jdbc.driver.OracleDriver";
	private static bcParamXML connXML = bcParamXML.getInstance();
    
	private static String serverName = "" + connXML.getValue("SERVER_NAME");
	private static String databaseName = "" + connXML.getValue("DATABASE_NAME");
	private static String portNumber = connXML.getValue("PORT_NUMBER");
	private final static Logger LOGGER=Logger.getLogger(HibernateOracleConnect.class);
	
	/** ������, ������� �������� ������
	 * @param login �����
	 * @param password ������
	 * @param pool_size ������ ����
	 * */
	public HibernateOracleConnect(String login, 
								  String password, 
								  Integer poolSize){
		super(login,password, poolSize);
	}

	/**
	 * @return will return something like "jdbc:oracle:thin:@192.168.15.254:1521:demo";
	 */
	@Override
	protected String getDataBasePath() {
		String dbPath = "jdbc:oracle:thin:@"+ serverName + ":" + portNumber + ":" + databaseName;
		LOGGER.debug("dbPath=" + dbPath);
		return dbPath;
	}

	@Override
	protected String geDriverName() {
		return JDBC_ORACLE_DRIVER_NAME;
	}

}
