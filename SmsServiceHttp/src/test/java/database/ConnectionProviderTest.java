package database;

import java.sql.Connection;
import java.sql.ResultSet;

import junit.framework.Assert;

import org.junit.Test;

public class ConnectionProviderTest {
	
	final static String DRIVER_CLASS="oracle.jdbc.driver.OracleDriver";
	final static String URL="jdbc:oracle:thin:@91.195.53.27:1521:demo";
	final static String LOGIN="bc_sms";
	final static String PASS="bc_sms";
	
	public static Connection getTestConnection(){
		ConnectionProvider provider = new ConnectionProvider(DRIVER_CLASS, URL,LOGIN, PASS);
		return provider.getConnection();
	}
	
	@Test
	public void givenCredentialsCheckConnection() throws Exception {
		// given
		int controlNumber=1;
		ConnectionProvider provider = new ConnectionProvider(DRIVER_CLASS, URL,LOGIN, PASS);
		Connection connection = provider.getConnection();
		Assert.assertNotNull(connection);
		// when
		ResultSet rs=connection.createStatement().executeQuery("select "+controlNumber+" from dual");
		// then
		Assert.assertTrue(rs.next());
		Assert.assertEquals(controlNumber, rs.getInt(1));
		// Assert.assertTrue(connection.isValid(100));
		
		ConnectionProvider.close(connection);
	}

}
