package ua.com.nmtg.private_office.jdbc.source.oracle;

import java.sql.Connection;
import java.sql.DriverManager;

public class OracleConnection {

	/** �������� ���������� � ����� ������ Oracle
	 * @param host - (if null - 127.0.0.1) ����
	 * @param port - (if null - 1521 ) ����� �����
	 * @param sid - (if null - XE ) - SID ( Scheme Identifier )
	 * @param user - if(null - system) - User name
	 * @param pass - if(null - "") - User's Password
	 * @throws � ������ ���������� ���������� 
	 */
	public static java.sql.Connection getConnection(String host,
													Integer port,
													String sid, 
													String user, 
													String pass) throws Exception{
		
	    Class.forName("oracle.jdbc.driver.OracleDriver");
	    return getConnection("jdbc:oracle:thin:@"+((host==null)?"127.0.0.1":host)+":"+((port==null)?1521:port)+":"+((sid==null)?"XE":sid), user, pass);
	}
	
	/** �������� ���������� � ����� ������ Oracle
	 * @param url - path to DataBase 
	 * @param user - if(null - system) - User name
	 * @param pass - if(null - "") - User's Password
	 * @throws � ������ ���������� ���������� 
	 */
	public static java.sql.Connection getConnection(String url, 
													String user, 
													String pass) throws Exception{
		
	    Class.forName("oracle.jdbc.driver.OracleDriver");
	    Connection connection=DriverManager.getConnection(url,
	    								   user,
	        							   pass);
	    connection.setAutoCommit(false);
	    return connection;
	}
	
}
