package database;

import java.sql.Connection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import database.domain.DatabaseMessage;

public class DatabaseQueriesTest {

	private static final int PROFILE_ID=10;
	
	@Test
	public void testReadAllMessages() throws DatabaseException{
		// given
		Connection connection=ConnectionProviderTest.getTestConnection();
		// when 
		List<DatabaseMessage> result=DatabaseQueries.getForSent(connection, PROFILE_ID, 2L);
		// then
		Assert.assertNotNull(result);
		Assert.assertTrue(result.size()>0);
		Assert.assertTrue(result.size()<=2);
		Assert.assertNotNull(result.get(0).getId_sms_message());
		Assert.assertTrue(result.get(0).getId_sms_message()>0);
	}
}
