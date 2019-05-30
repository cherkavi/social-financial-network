package bc.payments.sberbank.report.task.messaging.email;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

abstract public class FileStorage {

	public File saveFile(String fileName, InputStream inputStream) throws IOException{
        File outputFile=getFile(fileName);
        
        FileOutputStream outputStream=null;
		try {
			outputStream = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e) {
		};
		
        try{
        	IOUtils.copy(inputStream, outputStream);
        }finally{
        	IOUtils.closeQuietly(inputStream);
        	IOUtils.closeQuietly(outputStream);
        }
		return outputFile;
	}

	/**
	 * @param fileName
	 * @return
	 */
	protected abstract File getFile(String fileName) throws IOException;
}
