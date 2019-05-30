package bc.payments.sberbank.report.task.messaging.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import bc.payments.sberbank.report.exception.GeneralReportException;

public class Pop3SslSource<ProcessorType> implements MessageSource<ProcessorType> {
	private final static Logger	LOGGER = Logger.getLogger(Pop3SslSource.class);
	private final static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	private static Properties createProperties(EmailCredential credential) {
		/* Set the mail properties */
		Properties props = System.getProperties();
		// Set manual Properties
		props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.pop3.socketFactory.fallback", "false");
		props.setProperty("mail.pop3.port", Integer.toString(credential.getEmailPort()));
		props.setProperty("mail.pop3.socketFactory.port", Integer.toString(credential.getEmailPort()));
		props.put("mail.pop3.host", credential.getEmailHost()); // pop.gmail.com
		return props;
	}

	private final static String				EMAIL_PROTOCOL	= "pop3";
	private final static String				FOLDER_DEFAULT	= "INBOX";
	private String							folder;
	private MessageProcessor<ProcessorType>	messageProcessor;
	
	@Value("${popDelayBeforeReadingInSec}")
	private long popDelayBeforeReadingInSec;

	/** count of attempt for reconnecting to remote service */
	private int reConnectCount=3;
	/** delay time between attempts of reconnecting */
	private long reConnectIntervalSec=60;
	
	public Pop3SslSource(MessageProcessor<ProcessorType> processor) {
		this(processor, FOLDER_DEFAULT);
	}

	public Pop3SslSource(MessageProcessor<ProcessorType> processor, String folderName) {
		this.messageProcessor = processor;
		this.folder = folderName;
	}

	@Override
	public List<ProcessorType> getMessages(EmailCredential credential) throws GeneralReportException {
		LOGGER.info("pop3 next iteration begin");
		try {
			TimeUnit.SECONDS.sleep(popDelayBeforeReadingInSec);
		} catch (InterruptedException e1) {
		}
		
		Store store = null;
		Folder inbox = null;
		try {
			/* Create the session and get the store for read the mail. */
			Session session = Session.getDefaultInstance(createProperties(credential), null);

			try {
				store = session.getStore(EMAIL_PROTOCOL);
			} catch (NoSuchProviderException e) {
				throw new GeneralReportException("can't init protocol:" + EMAIL_PROTOCOL);
			}
			
			connectToPop3(credential, store);

			/* Mention the folder name which you want to read. */
			LOGGER.debug("pop3 retrieve folder");
			// inbox = store.getDefaultFolder();
			// inbox = inbox.getFolder("INBOX");
			try {
				inbox = store.getFolder(this.folder);
			} catch (MessagingException e) {
				throw new GeneralReportException("can't open to 'defaul' folder: " + this.folder, e);
			}

			LOGGER.debug("pop3 open folder");
			/* Open the inbox using store. */
			try {
				inbox.open(Folder.READ_WRITE); // inbox.open(Folder.READ_WRITE);
			} catch (MessagingException e) {
				throw new GeneralReportException("can't access read-write to folder: " + this.folder, e);
			}

			/* Get the messages which is unread in the Inbox */
			Message messages[];
			try {
				LOGGER.debug("pop3 search for new e-mail");
				messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
			} catch (MessagingException e) {
				throw new GeneralReportException("can't search messages into folder", e);
			}

			/* Use a suitable FetchProfile */
			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			fp.add(FetchProfile.Item.CONTENT_INFO);

			try {
				LOGGER.debug("pop3 retrieve messageS");
				inbox.fetch(messages, fp);
			} catch (MessagingException e) {
				throw new GeneralReportException("can't fetch messages from folder", e);
			}

			List<ProcessorType> returnValue=null;
			try {
				LOGGER.debug("pop3 convert to inner representation");
				returnValue=processEachMessage(messages);
			} catch (IOException e) {
				throw new GeneralReportException("Can't write message", e);
			} catch (MessagingException e) {
				throw new GeneralReportException("Can't process message", e);
			}

			// remove messages
			try {
				LOGGER.debug("pop3 mark as removed");
				markMessagesAsDeleted(messages);
			} catch (MessagingException e) {
				throw new GeneralReportException("can't remove messages from folder:"+FOLDER_DEFAULT, e);
			}
			
			try {
				inbox.expunge();
			} catch (MessagingException e) {
				// for some realization it does not supported 
			}
			
			return returnValue;
		} finally {
			closeSilently(inbox);
			closeSilently(store);
			LOGGER.debug("pop3 next iteration end");
		}
	}

	/**
	 * attempt to connect to Pop3 server
	 * @param credential
	 * @param store
	 * @throws GeneralReportException
	 */
	private void connectToPop3(EmailCredential credential, Store store) throws GeneralReportException {
		LOGGER.debug("pop3 connecting...");
		int counter=0;
		Exception lastException=null;
		
		while(counter<reConnectCount){
			try {
				store.connect(credential.getEmailHost(), credential.getEmailPort(), credential.getEmailLogin(),
						credential.getEmailPassword());
				return;
			} catch (MessagingException e) {
				counter++;
				lastException=e;
				try {
					TimeUnit.SECONDS.sleep(reConnectIntervalSec);
				} catch (InterruptedException e1) {
				}
				LOGGER.warn("connection exception: "+e.getMessage(), e);
			}
		}
		throw new GeneralReportException("can't connect to email server:" + credential,lastException);
	}

	private void markMessagesAsDeleted(Message[] messages) throws MessagingException {
		for(Message eachMessage:messages){
			eachMessage.setFlag(Flags.Flag.DELETED, true);
		}
	}

	private void closeSilently(Store store) {
		if(store==null){
			return;
		}
		try {
			store.close();
		} catch (MessagingException me) {
		}
	}

	private void closeSilently(Folder inbox) {
		if (inbox == null) {
			return;
		}
		try {
			inbox.close(true);
		} catch (MessagingException me) {
		}
	}

	private List<ProcessorType> processEachMessage(Message[] messages) throws IOException, MessagingException {
		if (messages == null) {
			return new ArrayList<ProcessorType>(0);
		}
		List<ProcessorType> returnValue = new ArrayList<ProcessorType>(0);
		for (Message eachMessage : messages) {
			ProcessorType processedValue = this.messageProcessor.processMessage(eachMessage);
			if (processedValue != null) {
				returnValue.add(processedValue);
			}
		}
		return returnValue;
	}

}
