package bc.ws.persistent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import bc.util.ConnectorUtils;
import bc.ws.exception.PersistentException;
import org.junit.Test;

import bc.ws.domain.EnvironmentSettings;
import bc.ws.domain.TerminalMenuResult;
import junit.framework.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-context-test.xml"})
public class UserParamsTest {
	private final static String DEFAULT_USERNAME = "U20001";
	private final static String DEFAULT_PASSWORD = "1234";

	@Autowired
	ConnectionManager connectionManager;

	private final static Integer TERMINAL_DEFAULT=20001;
	private final static EnvironmentSettings DEFAULT_SETTINGS=new EnvironmentSettings();
	static{
		DEFAULT_SETTINGS.setTerminalId("20002");
		DEFAULT_SETTINGS.setUserId(516);
		DEFAULT_SETTINGS.setTerminalCurrencyCode("643");
		DEFAULT_SETTINGS.setDateFormat("DD.MM.RRRR");
		DEFAULT_SETTINGS.setUserInterfaceLang("RU");
		DEFAULT_SETTINGS.setReportFormat("HTML");
//		DEFAULT_SETTINGS.setDbSchema(null);
//		DEFAULT_SETTINGS.setRowsOnPage(null);
		DEFAULT_SETTINGS.setCalcPointOnTerminal(false);
		DEFAULT_SETTINGS.setUserInterfaceLang("RU");
	}
	
	
	@Test
	public void checkCurrentTerminal() throws SQLException{
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try{
			UserParams userParams=new UserParams(connection);

			// when
			Integer terminalId=userParams.getTerminalId();

			// then
			Assert.assertNotNull(terminalId);
			Assert.assertEquals("20002", terminalId.toString());
		}finally{
			ConnectorUtils.closeQuietly(connection);
		}
	}

	
	@Test
	public void checkEnvironmentSettings() throws SQLException, PersistentException {
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try{
			// given
			UserParams userParams=new UserParams(connection);

			// when
			EnvironmentSettings settings=userParams.getSettings();

			// then
			Assert.assertNotNull(settings);
			Assert.assertEquals(DEFAULT_SETTINGS, settings);
		}finally {
			ConnectorUtils.closeQuietly(connection);
		}
	}
	
	@Test
	public void checkOperations() throws SQLException{
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try{
			// given
			UserParams userParams=new UserParams(connection);
            String terminalId="20002";

			// when
			List<TerminalMenuResult> operations=userParams.getOperations(terminalId);

			// then
			Assert.assertNotNull(operations);
			Assert.assertFalse(operations.isEmpty());
			Assert.assertEquals(48, operations.size());
			// Assert.assertEquals(DEFAULT_SETTINGS, operations);
		}finally{
			ConnectorUtils.closeQuietly(connection);
		}
	}
	
}
