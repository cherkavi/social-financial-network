package engine.modem;

import org.apache.log4j.Logger;
import org.smslib.InboundMessage;
import org.smslib.StatusReportMessage;
import org.smslib.Message.MessageTypes;
import org.smslib.StatusReportMessage.DeliveryStatuses;

/** входящее сообщение */
public class MessageInput {
	/** тип сообщения */
	private MessageInputType type;
	/** сообщение формата smsLib */
	private InboundMessage message;
	/** флаг, который говорит, является ли данное сообщение отчетом о доставке */
	private boolean reportDelivery=false;
	
	private Logger logger=Logger.getLogger(this.getClass());
	
	public MessageInput(MessageTypes msgType,InboundMessage msg){
		this.message=msg;
		type=MessageInputType.UNKNOWN;
		if(msgType==MessageTypes.STATUSREPORT){
			type=MessageInputType.REPORT;
		}
		if(msgType==MessageTypes.INBOUND){
			type=MessageInputType.TEXT;
		}
		if(this.message instanceof StatusReportMessage){
			this.reportDelivery=true;
		}
	}
	
	/**
	 * is this message are Delivery Reports 
	 */
	public boolean isDeliveryMessage(){
		return reportDelivery;
	}
	
	/**
	 * get delivered status only for Delivery Reports
	 */
	public DeliveryStatuses getDeliveryStatus(){
		if(isDeliveryMessage()){
			return ((StatusReportMessage)this.message).getStatus();
		}else{
			return null;
		}
	}
	
	/** get recipient for this message 
	 * @return null, if message is UNKNOWN type, or message format is unknown 
	 * */
	public String getRecipient(){
		String returnValue=null;
		if(type==MessageInputType.UNKNOWN){
			returnValue=null;
		}
		if(type==MessageInputType.TEXT){
			returnValue=this.message.getOriginator();
		}
		if(type==MessageInputType.REPORT){
			if(this.message instanceof StatusReportMessage){
				returnValue=((StatusReportMessage)this.message).getRecipient();
				/*logger.info("ID:"+((StatusReportMessage)this.message).getId());
				logger.info("Message ID:"+((StatusReportMessage)this.message).getMessageId());
				logger.info("RefNo:"+((StatusReportMessage)this.message).getRefNo());
				logger.info("MpRefNo:"+((StatusReportMessage)this.message).getMpRefNo());
				logger.info("MpSeqNo:"+((StatusReportMessage)this.message).getMpSeqNo());
				logger.info("Recipient:"+((StatusReportMessage)this.message).getRecipient());
				logger.info("Status:"+((StatusReportMessage)this.message).getStatus().name());*/
				//org.smslib.StatusReportMessage.DeliveryStatuses.
				//((StatusReportMessage)this.message).gets
			}else{
				logger.error("getRecipient: is not Delivered Status");
			}
		}
		return returnValue;
	}
	
	/** get RefNo only for DeliveredMessage else return null*/
	public String getRefno(){
		if(isDeliveryMessage()){
			return ((StatusReportMessage)this.message).getRefNo();
		}else{
			return null;
		}
	}
	
	/** get message text */
	public String getText(){
		String returnValue=null;
		if(type==MessageInputType.UNKNOWN){
			returnValue=null;
		}
		if(type==MessageInputType.TEXT){
			returnValue=this.message.getText();
		}
		if(type==MessageInputType.REPORT){
			returnValue="REPORT";
		}
		return returnValue;
	}
	
	/** get Inbound Message */
	public InboundMessage getInboundMessage(){
		return this.message;
	}
}

/** possible types of input messages */
enum MessageInputType{
	/** text message */
	TEXT,
	/** delivery report */
	REPORT,
	/** maybe bianary message or unknown format */
	UNKNOWN;
}
