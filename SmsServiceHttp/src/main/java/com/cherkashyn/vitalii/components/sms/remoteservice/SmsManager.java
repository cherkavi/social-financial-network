package com.cherkashyn.vitalii.components.sms.remoteservice;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.cherkashyn.vitalii.components.sms.remoteservice.client.Client;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.PacketResponse;
import com.cherkashyn.vitalii.components.sms.remoteservice.domain.Result;
import com.cherkashyn.vitalii.components.sms.remoteservice.transport.HttpTransport;
import com.cherkashyn.vitalii.components.sms.remoteservice.transport.TransportException;

import database.ConnectionProvider;
import database.ConnectionProviderTest;
import database.DatabaseException;
import database.DatabaseFunctions;
import database.DatabaseQueries;
import database.DatabaseUtility;
import database.domain.DatabaseMessage;
import database.domain.EMessageActionSendState;
import database.domain.EMessageDeliveredStatus;
import xml_ini.Settings;

/**
 * main block 
 */
public class SmsManager extends Thread 
{
    public static void main( String[] args )
    {
        new SmsManager(loadSettings()).start();
    }

    
    static Settings loadSettings() {
		// TODO Auto-generated method stub
		return null;
	}


    HttpTransport transport;
    
    SmsManager(Settings initSettings){
    	this.settings=initSettings;
    	transport=new HttpTransport();
    }
    
    
    private final Settings settings;
    
    /**
     * !!! MAIN LOGIC !!! 
     */
    @Override
    public void run() {
    	Connection connection=getConnection(settings);

    	while(true){
    		// read new
    		List<DatabaseMessageWithAction> processedList=null;
			try {
				processedList = getDataForSend(connection, settings);
			} catch (DatabaseException e) {
				throw new RuntimeException("can't read data from DB with new messages for send ", e);
			}

			// send new
    		try {
				sendMessagesUpdateStatus(processedList, connection, settings);
			} catch (TransportException ex) {
				throw new RuntimeException("can't send messages via HTTP remote service", ex);
			} catch ( DatabaseException ex) {
				throw new RuntimeException("can't change message status to WAIT_FOR_RESPONSE/ERROR", ex);
			}
    		
        	// FIXME
    		// read for update statuses
    		List<DatabaseMessage> forCheckStatus=getMessagesForDeliveryConfirm(connection, settings);
    		changeStatusIfNeed(forCheckStatus, connection);

    		// delay before next iteration 
    		delay(settings);
    	}
    }


    /**
     * change statuses of messages into Database 
     * @param forCheckStatus
     * @param connection
     */
    private void changeStatusIfNeed(List<DatabaseMessage> forCheckStatus,
			Connection connection) {
		// TODO Auto-generated method stub
	}



	/**
     * delay before next iteration 
     * @param settings
     */
	private void delay(Settings settings) {
		try {
			TimeUnit.SECONDS.sleep(settings.getWorkiterationDelay());
		} catch (InterruptedException e) {
			throw new RuntimeException("can't delay application for wait next iteration ");
		}
	}



	/**
	 * read messages for status 
	 * @param connection
	 * @param settings
	 * @return
	 */
	private List<DatabaseMessage> getMessagesForDeliveryConfirm(
			Connection connection, Settings settings) {
		
		EMessageActionSendState.WAIT_FOR_CONFIRM.getKod();
		EMessageDeliveredStatus.NONE.getKod();
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * send messages and update status ( depends on result of sending ) 
	 * @param processedList
	 * @param connection
	 * @param settings
	 * @throws TransportException
	 * @throws DatabaseException
	 */
	private void sendMessagesUpdateStatus(List<DatabaseMessageWithAction> processedList,
			Connection connection, Settings settings) throws TransportException, DatabaseException {
		// check for empty list 
		if(processedList==null || processedList.size()==0){
			return;
		}
		
		for(DatabaseMessageWithAction eachMessage:processedList){
			// send message 
			PacketResponse packet=Client.createSendMessage(new Long(eachMessage.message.getId_sms_message()), digitsOnly(eachMessage.message.getRecepient()), eachMessage.message.getRecepient(), eachMessage.message.getText_message()).execute(this.transport);

			// check send result 
			if(packet.getResult()==null || packet.getResult().isValid()==false){
				// send message ERROR 
				updateSmsAction(connection, eachMessage, EMessageActionSendState.SEND_ERROR.getKod(), Result.Case.getMessage(packet.getResult().getType()));
			}else{
				// send message OK - wait for confirm
				updateSmsAction(connection, eachMessage, EMessageActionSendState.WAIT_FOR_CONFIRM.getKod(), "");
			}
		}
	}

	/**
	 * utility method for update each Messages
	 * @param connection
	 * @param eachMessage
	 * @param status
	 * @param errorMessage
	 * @throws DatabaseException 
	 */
	private void updateSmsAction(Connection connection, DatabaseMessageWithAction eachMessage, int status, String errorMessage) throws DatabaseException{
		DatabaseFunctions.UpdateSmsAction updateStatusParameter=new DatabaseFunctions.UpdateSmsAction();
		updateStatusParameter.smsActionId=eachMessage.actionId;
		updateStatusParameter.refNumber=eachMessage.message.getId_sms_message();
		updateStatusParameter.smsSendStatus=status;
		updateStatusParameter.smsNotDeliverMessage=errorMessage;
		DatabaseUtility.executeFunctionFillResults(connection, 
				 DatabaseUtility.SCHEMA_DEFAULT, 
				 updateStatusParameter);
		
	}
	

	/**
	 * clear all symbols, except digits 
	 * @param recepient
	 * @return
	 */
	private long digitsOnly(String recepient) {
		return Long.parseLong(recepient.replaceAll("^[0-9]", ""));
	}


	private static class DatabaseMessageWithAction{
		DatabaseMessage message; 
		Integer actionId;
		
		public DatabaseMessageWithAction(DatabaseMessage message, Integer actionId){
			this.message=message;
			this.actionId=actionId;
		}
	}
	
	/**
	 * read data from Database, change status of Messages to IN_PROCESS
	 * @param connection
	 * @param settings
	 * @return
	 * @throws DatabaseException
	 */
	private List<DatabaseMessageWithAction> getDataForSend(Connection connection,
			Settings settings) throws DatabaseException {
		// retrieve from DB 
		List<DatabaseMessage> forSend=DatabaseQueries.getForSent(connection, settings.getProfileId(), settings.getMessagesCountForSend());
		// change status IN_PROCESS
		if(forSend!=null && forSend.size()>0){
			List<DatabaseMessageWithAction> returnValue=new ArrayList<DatabaseMessageWithAction>(forSend.size());
			for(DatabaseMessage eachMessage:forSend){
				DatabaseFunctions.AddSmsAction addSmsAction=new DatabaseFunctions.AddSmsAction();
				addSmsAction.smsId=eachMessage.getId_sms_message();
				addSmsAction.recipient=eachMessage.getRecepient();
				addSmsAction.message=eachMessage.getText_message();
				addSmsAction.smsSendStatus=database.domain.EMessageActionSendState.IN_PROCESS.getKod();
				addSmsAction.sendMessageError=null;
				addSmsAction.smsProfileId=settings.getProfileId();
				DatabaseUtility.executeFunctionFillResults(connection, DatabaseUtility.SCHEMA_DEFAULT, addSmsAction);
				returnValue.add(new DatabaseMessageWithAction(eachMessage, addSmsAction.idSmsAction));
			}
			return returnValue;
		}else{
			return new ArrayList<DatabaseMessageWithAction>(0);
		}
	}

	
	private Connection getConnection(Settings settings) {
		ConnectionProvider provider=new ConnectionProvider(settings.getJdbcUrl(), settings.getJdbcUrlLogin(), settings.getJdbcUrlPassword());
		return provider.getConnection();
	}
    
    
}
