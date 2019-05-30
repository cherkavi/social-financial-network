package bc.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;

/**
 * servlet for downloading files via GET/POST methods <br />
 * user should specify one vital parameter - "filename"  and file with name will be retrieved from server directory <br />
 * server directory can be specified via web.xml init parameter of servlet with name "dir" and can be a parameter from context.xml like:
 * ${name_of_parameter_from_context_xml} or direct name of directory from server like "/home/technik/my_documents/"
 * 
 */
public class FileDownloaderServlet extends JndiParamHttpServlet{

	private static final long serialVersionUID = 1L;

	/** name of the user parameter with must be specified */
	private final static String REQUEST_PARAMETER_FILENAME="filename";
	
	private final static String DIR="dir";
	
	private String pathToDocuments;

	@Override
	public void init() throws ServletException {
		super.init();
		String initParameterWebXml=this.getInitParameter(DIR);
		this.pathToDocuments=correctFolderPath(initParameterWebXml);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doRequest(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doRequest(req, resp);
	}
	
	protected void doRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName=StringUtils.trimToNull(req.getParameter(REQUEST_PARAMETER_FILENAME));
		if(fileName==null){
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		File targetFile=new File(this.pathToDocuments+fileName);
		if(!targetFile.exists()){
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		sendFile(targetFile, fileName, resp);
	}

	private void sendFile(File targetFile, String fileName, HttpServletResponse resp) throws IOException {
		resp.setContentType(ContentType.APPLICATION_OCTET_STREAM.getMimeType());
		resp.setHeader("Content-Disposition", String.format("filename=\"%s\"", fileName));
		FileUtils.copyFile(new File(this.pathToDocuments+fileName), resp.getOutputStream());	
	}


}
