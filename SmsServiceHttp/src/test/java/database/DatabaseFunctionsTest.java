package database;

import java.sql.Connection;

import junit.framework.Assert;

import org.junit.Test;

public class DatabaseFunctionsTest {
	
	private final static Integer PROFILE_FOR_TEST=0;
	private final static Integer STATUS_IN_PROCESS=11;// status IN PROCESS
	private final static Integer STATUS_DELIVERED=14;// status DELIVERED
	private final static String TEST_PHONE_NUMBER="+380954671434";

	private DatabaseFunctions.AddSms createSmsAndAddToQueue(String phoneNumber, Integer profileId) throws DatabaseException{
		// given 
		DatabaseFunctions.AddSms parameter=new DatabaseFunctions.AddSms();
		parameter.recipient=phoneNumber;
		parameter.type=DatabaseFunctions.AddSms.Types.send.toString();
		parameter.text="test message";
		parameter.profileId=profileId;
		
		Connection connection=ConnectionProviderTest.getTestConnection();
		// when 
		DatabaseUtility.executeFunctionFillResults(connection, 
													 DatabaseUtility.SCHEMA_DEFAULT, 
													 parameter);
		// then
		Assert.assertNotNull(parameter.id);
		Assert.assertTrue(parameter.id>0);
		Assert.assertNull(parameter.result);
		return parameter;
	}
	
	private DatabaseFunctions.DeleteSms deleteSmsById(Integer id) throws DatabaseException{
		DatabaseFunctions.DeleteSms parameterDelete=new DatabaseFunctions.DeleteSms();
		parameterDelete.deleteId=id;

		DatabaseUtility.executeFunctionFillResults(ConnectionProviderTest.getTestConnection(), DatabaseUtility.SCHEMA_DEFAULT,  parameterDelete);
		return parameterDelete;
	}

	@Test
	public void createSmsAndAddToQueueAndDeleteIt() throws DatabaseException{
		// given
		Connection connection=ConnectionProviderTest.getTestConnection();
		DatabaseFunctions.AddSms parameter=createSmsAndAddToQueue(TEST_PHONE_NUMBER, PROFILE_FOR_TEST);
		
		DatabaseFunctions.DeleteSms parameterDelete=new DatabaseFunctions.DeleteSms();
		parameterDelete.deleteId=parameter.id;
		
		DatabaseUtility.executeFunctionFillResults(connection, 
				DatabaseUtility.SCHEMA_DEFAULT, 
				 parameterDelete);
	}
	
	@Test
	public void createSmsAndChangeNoteAndAddToQueueAndDeleteIt() throws DatabaseException{
		// given
		Connection connection=ConnectionProviderTest.getTestConnection();
		DatabaseFunctions.AddSms parameter=createSmsAndAddToQueue(TEST_PHONE_NUMBER, PROFILE_FOR_TEST);
		
		
		DatabaseFunctions.UpdateSmsNote updateParameter=new DatabaseFunctions.UpdateSmsNote();
		updateParameter.smsId=parameter.id;
		updateParameter.newNote="this is test note";
		DatabaseUtility.executeFunctionFillResults(connection, 
				DatabaseUtility.SCHEMA_DEFAULT, 
				 updateParameter);
		
		
		deleteSmsById(parameter.id);
	}

	@Test
	public void createSmsAddUpdateStatus() throws DatabaseException{
		// given
		Connection connection=ConnectionProviderTest.getTestConnection();
		DatabaseFunctions.AddSms parameter=createSmsAndAddToQueue(TEST_PHONE_NUMBER, PROFILE_FOR_TEST);
		
		DatabaseFunctions.AddSmsAction addStatusParameter=new DatabaseFunctions.AddSmsAction();
		addStatusParameter.smsId=parameter.id;
		addStatusParameter.recipient=parameter.recipient;
		addStatusParameter.message=parameter.text;
		addStatusParameter.smsSendStatus=STATUS_IN_PROCESS;
		addStatusParameter.sendMessageError="";
		addStatusParameter.smsProfileId=PROFILE_FOR_TEST;

		DatabaseUtility.executeFunctionFillResults(connection, 
				 DatabaseUtility.SCHEMA_DEFAULT, 
				 addStatusParameter);

		DatabaseFunctions.UpdateSmsAction updateStatusParameter=new DatabaseFunctions.UpdateSmsAction();
		updateStatusParameter.smsActionId=addStatusParameter.idSmsAction;
		updateStatusParameter.smsSendStatus=STATUS_DELIVERED;
		updateStatusParameter.refNumber=parameter.id;
		updateStatusParameter.smsNotDeliverMessage="";
		DatabaseUtility.executeFunctionFillResults(connection, 
				 DatabaseUtility.SCHEMA_DEFAULT, 
				 updateStatusParameter);
		
		// can't delete message with linked ActionSend status(es)
		// this.deleteSmsById(parameter.id);
	}
	
	@Test
	public void addSmsAction() throws DatabaseException{
		// given 
		DatabaseFunctions.AddSms newSmsParameter=createSmsAndAddToQueue(TEST_PHONE_NUMBER, PROFILE_FOR_TEST);
		
		DatabaseFunctions.AddSmsAction addSmsAction=new DatabaseFunctions.AddSmsAction();

		addSmsAction.smsId=newSmsParameter.id;
		addSmsAction.recipient=newSmsParameter.recipient;
		addSmsAction.message=newSmsParameter.text;
		addSmsAction.smsSendStatus=STATUS_DELIVERED;
		addSmsAction.sendMessageError=null;
		addSmsAction.smsProfileId=newSmsParameter.profileId;

		// when 
		DatabaseUtility.executeFunctionFillResults(ConnectionProviderTest.getTestConnection(), "bc_admin", addSmsAction);

		// then 
		// no Exceptions
		
		// finalize
		// can't delete message, which has at least one status 
		// deleteSmsById(newSmsParameter.id);
	}
	
	
	@Test(expected=DatabaseException.class)
	public void addSmsWithNotExistingProfile() throws DatabaseException{
		
		// given 
		createSmsAndAddToQueue("+380950001122", Integer.MAX_VALUE);
				new DatabaseFunctions.AddSms();
		// then
		// wait for DatabaseException 
	}
	
}
