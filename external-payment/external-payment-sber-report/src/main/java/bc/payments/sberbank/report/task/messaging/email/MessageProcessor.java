package bc.payments.sberbank.report.task.messaging.email;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * process message 
 * @param <T> - type of destination object 
 */
public interface MessageProcessor<T> {
	/**
	 * process message - retrieve additional information
	 * @param message
	 * @return
	 */
	T processMessage(Message message) throws IOException, MessagingException;
	
}
