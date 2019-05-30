package bc.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
//import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import bc.objects.bcUserLoadedImageObject;


/**
 * The Image servlet for serving from absolute path.
 * @author BalusC
 * @link http://balusc.blogspot.com/2007/04/imageservlet.html
 */
public class LoadedImageServlet  extends JndiParamHttpServlet {
	private final static Logger LOGGER=Logger.getLogger(LoadedImageServlet.class);

    // Properties ---------------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
    
	private final int clasterSize=1024;
	private ByteBuffer buffer=ByteBuffer.allocate(clasterSize);
	private String errorMessage = "";

    // Init ---------------------------------------------------------------------------------------

    public void init() throws ServletException {

        // Define base path somehow. You can define it as init-param of the servlet.
        // In a Windows environment with the Applicationserver running on the
        // c: volume, the above path is exactly the same as "c:\var\webapp\images".
        // In Linux/Mac/UNIX, it is just straightforward "/var/webapp/images".
    }

    // Actions ------------------------------------------------------------------------------------

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Get requested image by path info.
        //String requestedImage = request.getPathInfo();
    	String photoFileName = "";
        String id_file = StringUtils.trimToNull(request.getParameter("id_file"));
		if(id_file==null || "".equalsIgnoreCase(id_file)){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		} else {
			bcUserLoadedImageObject loaded_file = new bcUserLoadedImageObject(id_file);
			photoFileName = loaded_file.getValue("FULL_FILE_NAME");
		}
		
		
		LOGGER.debug("TargetProgramImageFileName="+photoFileName);
        // Check if file name is actually supplied to the request URI.
        if (photoFileName == null) {
            // Do your thing if the image is not supplied to the request URI.
            // Throw an exception, or send 404, or show default/warning image, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        // Decode the file name (might contain spaces and on) and prepare file object.
        File image = new File(URLDecoder.decode(photoFileName, "UTF-8"));

        // Check if file actually exists in filesystem.
        if (!image.exists()) {
            // Do your thing if the file appears to be non-existing.
            // Throw an exception, or send 404, or show default/warning image, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        // Get content type by filename.
        String contentType = getServletContext().getMimeType(image.getName());

        // Check if file is actually an image (avoid download of other files by hackers!).
        // For all content types, see: http://www.w3schools.com/media/media_mimeref.asp
        if (contentType == null || !contentType.startsWith("image")) {
            // Do your thing if the file appears not being a real image.
            // Throw an exception, or send 404, or show default/warning image, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        // Init servlet response.
        response.reset();
        response.setContentType(contentType);
        response.setHeader("Content-Length", String.valueOf(image.length()));

        // Write image content to response.

		FileInputStream fis=new FileInputStream(image);
		if(readFromInputStreamToOutputStream(fis,response.getOutputStream())){
			// data read OK
		}else{
			response.sendError(HttpServletResponse.SC_NOT_FOUND, errorMessage);
			LOGGER.error("file was't loaded");
		}
		fis.close();
		response.getOutputStream().flush();
		response.getOutputStream().close();
        //Files.copy(image.toPath(), response.getOutputStream());
    }


	
	private boolean readFromInputStreamToOutputStream(FileInputStream input, ServletOutputStream output){
		boolean returnValue=false;
		FileChannel inputChannel=input.getChannel();
		try{
			while(inputChannel.read(this.buffer)>0){
				output.write(this.buffer.array(),0,this.buffer.limit());
				this.buffer.clear();
			}
			returnValue=true;
		}catch(Exception ex){
			setErrorMessage("readFromInputStreamToOutputStream: Exception: "+ex.getMessage());
			returnValue=false;
		}
		return returnValue;
	}
	
	private void setErrorMessage(String pMessage) {
		this.errorMessage = this.errorMessage + "<br>" + pMessage;
	}
}