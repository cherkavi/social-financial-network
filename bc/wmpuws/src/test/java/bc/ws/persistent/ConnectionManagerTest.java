package bc.ws.persistent;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import bc.ws.service.IpDetector;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-context-test.xml"})
public class ConnectionManagerTest {

	@Autowired
	ConnectionManager connectionManager;
	
	@Test
	public void checkConnection() throws SQLException{
		// given
		
		// when
		Connection connection=connectionManager.open("U20001", "1234");
		// then
		Assert.assertNotNull(connection);
		Assert.assertFalse(connection.isClosed());

		// when 
		connectionManager.close(connection);
		// then
		Assert.assertTrue(connection.isClosed());
	}
	
	
	@Test
	public void checkConnectionHash() throws SQLException{
		// given
		IpDetector.PARAMETER.set("127.0.0.1");
        // when
		Connection connection=connectionManager.openHash("U20005", "2016-10-22T22:25:28+03:00", "4958044741735035BF516DC083731684");
		// then
		Assert.assertNotNull(connection);
		Assert.assertFalse(connection.isClosed());

		// when 
		connectionManager.close(connection);
		// then
		Assert.assertTrue(connection.isClosed());
	}
	
	
	@Test
	public void checkTerminalId() throws SQLException{
		Connection connection1=null;
		Connection connection2=null;
		try{
			// given 
			connection1=connectionManager.open("U20001", "1234");
			Integer terminalId1=20002;
			UserParams userParams1=new UserParams(connection1);
			
			connection2=connectionManager.open("U20005", "1234");
			Integer terminalId2=20001;
			UserParams userParams2=new UserParams(connection2);
			
			// when
			Integer userTerminal1=userParams1.getTerminalId();
			Integer userTerminal2=userParams2.getTerminalId();
			
			// then
			Assert.assertEquals(terminalId1, userTerminal1);
			Assert.assertEquals(terminalId2, userTerminal2);
			
			// finally
			
		}finally{
			connectionManager.close(connection1);
			connectionManager.close(connection2);
		}
	}

	
	@Test
	public void checkDateFormat() throws SQLException{
		ConnectionManager.checkDate("2016-10-22T22:25:28+03:00");
	}
	/*
	@Test(expected=SQLException.class)
	public void checkIncorrectDateFormat() throws SQLException{
		ConnectionManager.checkDate("2016-10-22T22:25:28+0");
	}
	
	@Test(expected=SQLException.class)
	public void checkIncorrectTimeZoneDateFormat() throws SQLException{
		ConnectionManager.checkDate("2016-10-22T22:25:28 03:00");
	}
	*/
}
