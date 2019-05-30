package bc.payments.sberbank.report.task.messaging.email;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import bc.payments.sberbank.report.exception.GeneralReportException;

public class EmailSingleAttachmentReaderTest {
	
	private static final String emailHost="pop3.yandex.ru";
	private static final int emailPort=995;
	private static final String emailLogin="tsberrep@wmup.com";
	private static final String emailPassword="NtRightE";

	
	@Test
	public void readDataFromEmail() throws GeneralReportException{
		// given
		EmailSingleAttachmentReader reader=new EmailSingleAttachmentReader(new EmailCredential(emailHost, emailPort, emailLogin, emailPassword), new Pop3SslSource<File>(new AttachmentRetrieveProcessor(new TemporaryFileStorage())));
		
		// when
		List<File> files=reader.read();
		
		// then
		Assert.assertNotNull(files);
		// Assert.assertTrue(files.size()>0);
	}
	
}
