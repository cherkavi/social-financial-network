package bc.payments.sberbank.report.task.messaging.email;

import java.io.File;
import java.io.IOException;

public class TemporaryFileStorage extends FileStorage{
	private final static String TEMP_FILE_SUFFIX="sberbank-report";
	
	@Override
	protected File getFile(String fileName) throws IOException {
		return File.createTempFile(fileName, TEMP_FILE_SUFFIX);
	}

}
