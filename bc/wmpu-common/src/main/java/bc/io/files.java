package bc.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class files {
	private final static Logger LOGGER=Logger.getLogger(files.class);
	
	public String getFileListOption(String pElementName, String pRootDir) {
		StringBuilder html = new StringBuilder();
		try {
			String list[] = new File(pRootDir).list();
			html.append("<select name=\"" + pElementName + "\" class=\"inputfield\">\n");
			for(int i = 0; i < list.length; i++) {
				//LOGGER.debug(list[i]);
				if (!("imported".equalsIgnoreCase(list[i]))) {
					html.append("<option value=\"" + list[i] + "\">" + list[i] + "</option>\n");
				}
			}
			html.append("</select>\n");
		} catch (Exception el) {
			return "Error: " + el.toString() + "\n";
        }
		return html.toString();
	}
	
	public static boolean copyFile(String source, String destination){
        boolean return_value=false;
        try{
            File file_source = new File(source);
            File file_destination = new File(destination);
            InputStream in = new FileInputStream(file_source);

           OutputStream out = new FileOutputStream(file_destination);

            // buffer size
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return_value=true;
            file_source.delete();
        }catch(FileNotFoundException ex){
          LOGGER.debug("File ("+source+") not found: "+ex.getMessage());
        }catch(IOException e){
          LOGGER.debug("IOException:"+e.getMessage());      
        }
        return return_value;
    }

	public static boolean moveFile(String source, String destination){
        boolean return_value=false;
        try{
            File file_source = new File(source);
            File file_destination = new File(destination);
            InputStream in = new FileInputStream(file_source);

           OutputStream out = new FileOutputStream(file_destination);

            // buffer size
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return_value=true;
            file_source.delete();
        }catch(FileNotFoundException ex){
          LOGGER.debug("File ("+source+") not found: "+ex.getMessage());
        }catch(IOException e){
          LOGGER.debug("IOException:"+e.getMessage());      
        }
        return return_value;
    }

}
