package bc.payments.sberbank.report.task.messaging.email;

import java.util.List;

import bc.payments.sberbank.report.exception.GeneralReportException;

public interface MessageSource<T> {
	
	List<T> getMessages(EmailCredential credential) throws GeneralReportException;

}
