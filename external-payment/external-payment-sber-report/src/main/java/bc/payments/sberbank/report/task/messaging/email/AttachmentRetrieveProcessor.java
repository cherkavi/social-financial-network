package bc.payments.sberbank.report.task.messaging.email;

import java.io.File;
import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import org.apache.commons.lang3.StringUtils;

public class AttachmentRetrieveProcessor implements MessageProcessor<File> {
	
	private FileStorage fileStorage;


	public AttachmentRetrieveProcessor(FileStorage storage){
		this.fileStorage=storage;
	}

	@Override
	public File processMessage(Message message) throws IOException, MessagingException {
		return this.retriveFirstAttachment(message);
	}

	
	private File retriveFirstAttachment(Message message) throws IOException, MessagingException {
		Object messageContent=message.getContent();
		if(!(messageContent instanceof Multipart)){
			return null;
		}
	    Multipart multipart = (Multipart) messageContent;
	    // System.out.println(multipart.getCount());

	    for (int i = 0; i < multipart.getCount(); i++) {
	        BodyPart bodyPart = multipart.getBodyPart(i);
	        if(!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) &&
	               !StringUtils.isNotBlank(bodyPart.getFileName())) {
	            continue; // dealing with attachments only
	        }
	        // return first file 
	        return this.fileStorage.saveFile(bodyPart.getFileName(), bodyPart.getInputStream());
	    }

	    return null;
	}
	
	
}
