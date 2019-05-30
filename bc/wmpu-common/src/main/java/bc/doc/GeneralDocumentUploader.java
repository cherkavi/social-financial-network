package bc.doc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bc.servlet.JndiParamHttpServlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.directwebremoting.util.Logger;
  
public class GeneralDocumentUploader extends JndiParamHttpServlet{
	private final static Logger LOGGER=Logger.getLogger(GeneralDocumentUploader.class);
	
	private static final long serialVersionUID = 1L;
	private final static String INIT_PARAM_REDIRECT_SUCCESS="page_upload_success";
	private final static String INIT_PARAM_REDIRECT_ERROR="page_upload_error";
	private final static String INIT_PARAM_UPLOAD_DIR="upload_dir";
	private final static String INIT_PARAM_MAX_FILE_SIZE="max_file_size";
	private final static String INIT_PARAM_TEMP_DIR="temp_dir";
	private final static int MAX_MEMORY_SIZE=1024*1000;
	
	private String redirectPageSuccess;
	private String redirectPageError;
	private String dirOutput;
	private Long maxFileSize; 
	private File dirTemp;

	@Override
	public void init() throws ServletException {
		super.init();
		redirectPageSuccess=this.getInitParameter(INIT_PARAM_REDIRECT_SUCCESS);
		redirectPageError=this.getInitParameter(INIT_PARAM_REDIRECT_ERROR);
		dirOutput=this.correctFolderPath(this.getInitParameter(INIT_PARAM_UPLOAD_DIR));
		maxFileSize=Long.parseLong(this.getInitParameter(INIT_PARAM_MAX_FILE_SIZE));
		dirTemp=new File(this.correctFolderPath(this.getInitParameter(INIT_PARAM_TEMP_DIR)));
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!ServletFileUpload.isMultipartContent(request)){
			response.sendRedirect(this.redirectPageError);
		}
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
	    factory.setSizeThreshold(MAX_MEMORY_SIZE);
	    // Location to save data that is larger than maxMemSize.
	    factory.setRepository(dirTemp);  

	    // Create a new file upload handler
	    ServletFileUpload upload = new ServletFileUpload(factory);
	    // maximum file size to be uploaded.
	    upload.setSizeMax( maxFileSize );

	    try{
	    	InputStream inputStream=null;
	    	OutputStream outputStream=null;
	    	// Parse the request to get file items.
	    	for(FileItem eachFileItem:upload.parseRequest(request)){
	    		if(eachFileItem.isFormField()){
	    			continue;
	    		}
	    		try{
	    			inputStream=eachFileItem.getInputStream();
	    			outputStream=createFileOutputStream(eachFileItem.getName(), request.getSession().getId());
	    			IOUtils.copy(inputStream, outputStream);
	    			outputStream.flush();
	    			LOGGER.info("file was uploaded: "+eachFileItem.getName());
	    		}finally{
	    			IOUtils.closeQuietly(inputStream);
	    			IOUtils.closeQuietly(outputStream);
	    		}
	    	}
	    	
	    	response.sendRedirect(this.redirectPageSuccess);
	    }catch(Exception ex){
	    	LOGGER.error(ex.getMessage());
	    	response.sendRedirect(this.redirectPageError);
	    }
	}

	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	
	private OutputStream createFileOutputStream(String name, String sessionId) throws FileNotFoundException {
		String pathToOutputFile=this.dirOutput+sdf.format(new Date())+name;
		return new FileOutputStream(pathToOutputFile);
	}
	
}
