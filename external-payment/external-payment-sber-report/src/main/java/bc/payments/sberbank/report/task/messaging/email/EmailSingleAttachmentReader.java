package bc.payments.sberbank.report.task.messaging.email;

import java.io.File;
import java.util.List;

import org.springframework.batch.item.ItemReader;

import bc.payments.sberbank.report.exception.GeneralReportException;

/**
 * read all emails from mailbox,
 * from each email retrieve first attachment
 */
public class EmailSingleAttachmentReader implements ItemReader<List<File>>{
	private EmailCredential emailCredential;
	private MessageSource<File> emailSource;
	
//	@Autowired(required=true)
//  private SharedInformation sharedInformation;
	
	public EmailSingleAttachmentReader(EmailCredential credentials) {
		this(credentials, new Pop3SslSource<File>(new AttachmentRetrieveProcessor(new TemporaryFileStorage())));
	}

	public EmailSingleAttachmentReader(EmailCredential credential, MessageSource<File> source) {
		super();
		this.emailCredential=credential;
		this.emailSource=source;
	}

	@Override
	public List<File> read() throws GeneralReportException  {
		return this.emailSource.getMessages(this.emailCredential);
	}


}