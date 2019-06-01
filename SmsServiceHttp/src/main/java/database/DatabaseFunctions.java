package database;

import database.DatabaseUtility.DatabaseFunction;
import database.DatabaseUtility.DatabaseFunctionParameter;
import database.DatabaseUtility.FunctionParameter;
import database.DatabaseUtility.SQL_TYPE;

public class DatabaseFunctions {
	public final static String SQL_PACKAGE_NAME="bc_sms";


	/**
	 * add sms action 
	 */
	@DatabaseFunction(name="PACK_BC_SMS_SEND.add_sms_action")
	public static class AddSmsAction implements FunctionParameter{
		@DatabaseFunctionParameter(order=2, sqlType=SQL_TYPE.NUMBER, directionOut=false)
		public Integer smsId;

		@DatabaseFunctionParameter(order=3, sqlType=SQL_TYPE.VARCHAR2, directionOut=false)
		public String recipient;

		@DatabaseFunctionParameter(order=4, sqlType=SQL_TYPE.VARCHAR2, directionOut=false)
		public String message;

		/** select * from bc_admin.VC_DS_SMS_SEND_STATUS  */
		@DatabaseFunctionParameter(order=5, sqlType=SQL_TYPE.NUMBER, directionOut=false)
		public Integer smsSendStatus;
		
		@DatabaseFunctionParameter(order=6, sqlType=SQL_TYPE.VARCHAR2, directionOut=false)
		public String sendMessageError;

		@DatabaseFunctionParameter(order=7, sqlType=SQL_TYPE.NUMBER, directionOut=false)
		public Integer smsProfileId;

		@DatabaseFunctionParameter(order=8, sqlType=SQL_TYPE.NUMBER, directionOut=true)
		public Integer idSmsAction;
		
		@DatabaseFunctionParameter(order=9, sqlType=SQL_TYPE.VARCHAR2, directionOut=true)
		public String errorMessage;
	}

	/**
	 * update status of SMS
	 */
	@DatabaseFunction(name="PACK_BC_SMS_SEND.update_sms_action_state")
	public static class UpdateSmsAction implements FunctionParameter{
		public static enum SendStatus{
			
		}
		
		@DatabaseFunctionParameter(order=2, sqlType=SQL_TYPE.NUMBER, directionOut=false)
		public Integer smsActionId;

		@DatabaseFunctionParameter(order=3, sqlType=SQL_TYPE.NUMBER, directionOut=false)
		public Integer smsSendStatus;

		@DatabaseFunctionParameter(order=4, sqlType=SQL_TYPE.NUMBER, directionOut=false)
		public Integer refNumber;

		@DatabaseFunctionParameter(order=5, sqlType=SQL_TYPE.VARCHAR2, directionOut=false)
		public String smsNotDeliverMessage;
		
		@DatabaseFunctionParameter(order=6, sqlType=SQL_TYPE.VARCHAR2, directionOut=true)
		public String errorMessage;
	}
	
	
	
	
	/**
	 * change Note for certain SMS
	 */
	@DatabaseFunction(name="PACK_BC_SMS.update_sms_note")
	public static class UpdateSmsNote implements FunctionParameter{
		@DatabaseFunctionParameter(order=2, sqlType=SQL_TYPE.NUMBER, directionOut=false)
		public Integer smsId;

		@DatabaseFunctionParameter(order=3, sqlType=SQL_TYPE.VARCHAR2, directionOut=false)
		public String newNote;
		
		@DatabaseFunctionParameter(order=4, sqlType=SQL_TYPE.VARCHAR2, directionOut=true)
		public String errorMessage;
	}
	
	/**
	 * delete message
	 */
	@DatabaseFunction(name="PACK_BC_SMS.delete_sms")
	public static class DeleteSms implements FunctionParameter{
		@DatabaseFunctionParameter(order=2, sqlType=SQL_TYPE.NUMBER, directionOut=false)
		public Integer deleteId;
		
		@DatabaseFunctionParameter(order=3, sqlType=SQL_TYPE.VARCHAR2, directionOut=true)
		public String errorMessage;
	}
	
	/**
	 * add sms to queue 
	 */
	@DatabaseFunction(name="PACK_BC_SMS_SEND.add_sms")
	public static class AddSms implements FunctionParameter{
		public static enum Types{
			send("SEND"), receive("RECEIVE"), call_in("CALL IN");
			
			private String realValue;
			Types(String value){
				this.realValue=value;
			}
			@Override
			public String toString() {
				return realValue;
			}
		}
		
		@DatabaseFunctionParameter(order=2, sqlType=SQL_TYPE.VARCHAR2, directionOut=false)
		public String recipient;
		
		@DatabaseFunctionParameter(order=3, sqlType=SQL_TYPE.VARCHAR2, directionOut=false)
		public String type;
		
		@DatabaseFunctionParameter(order=4, sqlType=SQL_TYPE.VARCHAR2, directionOut=false)
		public String text;
		
		@DatabaseFunctionParameter(order=5, sqlType=SQL_TYPE.NUMBER, directionOut=false)
		public Integer profileId;
		
		@DatabaseFunctionParameter(order=6, sqlType=SQL_TYPE.NUMBER, directionOut=true)
		public Integer id;
		
		@DatabaseFunctionParameter(order=7, sqlType=SQL_TYPE.VARCHAR2, directionOut=true)
		public String result;
	}
	

}
