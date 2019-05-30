package bc.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bc.connection.Connector;

/**
 * Servlet implementation class FileSender
 */
public class FileSender extends HttpServlet {
	/** ��������, ������� ����������� � �������� ��������� ��������� */
	private final String fileParameter="FILENAME";
	private static final long serialVersionUID = 1L;
	/** ������ �������� �� �����, � ������� ������������ ������ */
	private final int clasterSize=1024;
	/** �����, ������� ������������ ��� ������ ������ */
	private ByteBuffer buffer=ByteBuffer.allocate(clasterSize);
	
	private String errorMessage = "";
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	
	 private String decodeUtf(String value){
		  String returnValue="";
		  byte[] array=new byte[value.length()];
		  for(int counter=0;counter<value.length();counter++){
		   array[counter]=(byte)value.charAt(counter);
		  }
		  try {
		   ByteArrayInputStream bais=new ByteArrayInputStream(array);
		   InputStreamReader isr=new InputStreamReader(bais,"UTF-8");
		   BufferedReader reader=new BufferedReader(isr);
		   returnValue=reader.readLine();
		  } catch (UnsupportedEncodingException e) {
			  e.printStackTrace();
		  }catch(Exception ex){
			  setErrorMessage("decodeUtf Exception: "+ex.getMessage());
		  }
		  return returnValue;
		 }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	
	private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** �������� ������ ���� � ����� */
		
		String pathToFile=request.getParameter(fileParameter);
		pathToFile=decodeUtf(pathToFile);
		Connection con = null;
		try{
			con = Connector.getConnection(request.getSession().getId());
			if (con==null) {
				return;
			}
		}catch(Exception ex){
			setErrorMessage("Connection is closed");
			outErrorPage(response);
			return;
		} finally {
			try {
				Connector.closeConnection(con);
			}catch(Exception ex){
			}
		}
		
		
		try{
			File file=new File(pathToFile);
			if(file.exists()==false){
				throw new Exception("file is not found");
			}
			writeHeader(file,response);
			FileInputStream fis=new FileInputStream(file);
			if(readFromInputStreamToOutputStream(fis,response.getOutputStream())){
				// data read OK
			}else{
				// error in read data from file to User
			}
			fis.close();
			response.getOutputStream().close();
		}catch(Exception ex){
			setErrorMessage("doAction File: "+pathToFile);
			setErrorMessage("doAction FileSender error: "+ex.getMessage());
			outErrorPage(response);
		}
	}
	
	private boolean writeHeader(File file, HttpServletResponse response){
		boolean returnValue=false;
		try{
        	response.setContentType("application/"+this.getFileExtension(file));
        	response.addHeader("Content-Disposition", "attachment; filename=file."+this.getFileExtension(file));
        	response.setContentLength((int)file.length());
		}catch(Exception ex){
			setErrorMessage("writeHeader: Exception: "+ex.getMessage());
		}
		return returnValue;
	}
	
	private String getFileExtension(File f){
		String fileName=f.getName();
		int dotPosition=fileName.lastIndexOf(".");
		if(dotPosition>=0){
			return fileName.substring(dotPosition+1);
		}else{
			return "txt";
		}
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
	
	private void outErrorPage(HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		out.println("<html>");
		out.println("	<head>");
		out.println("<title>FileSender Error</title>");
		out.println("	</head>");
		out.println("	<body>");
		out.println("<b>FileSender Error <br></b>");
		out.println(errorMessage);
		out.println("	</body>");
		out.println("</html>");
		out.close();
	}
	
	private void setErrorMessage(String pMessage) {
		this.errorMessage = this.errorMessage + "<br>" + pMessage;
	}
}
