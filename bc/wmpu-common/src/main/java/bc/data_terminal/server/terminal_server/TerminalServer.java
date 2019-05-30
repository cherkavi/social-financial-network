package bc.data_terminal.server.terminal_server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bc.data_terminal.server.terminal.transport.Transport;


/**
 * �������, ������� ������������ �������������� ����� � ��������, �������� �� ���� �������, ������ ������
 */
public class TerminalServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String tempDirectory;
	private final static Logger LOGGER=Logger.getLogger(TerminalServer.class);
	
	@Override
	public void init() throws ServletException {
		super.init();
		tempDirectory=this.getInitParameter("temp_directory");
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TerminalServer() {
        super();
    }

    public void service(HttpServletRequest request, HttpServletResponse response){
    	doAction(request,response);
    }
    
    private void doAction(HttpServletRequest request, HttpServletResponse response){
		LOGGER.debug("doAction:begin");
		try{
			ServletInputStream input_stream=request.getInputStream();
			ObjectInputStream object_input_stream=new ObjectInputStream(input_stream);
			LOGGER.debug("������� ��������� ������ �� ������� ");
			Object temp_object=object_input_stream.readObject();
			LOGGER.debug("���������� ������:"+temp_object);
			Transport client_request=(Transport)temp_object;
			input_stream.close();
			LOGGER.debug("�������� �������� ��� ������ ������ ");
			ServletOutputStream output_stream=response.getOutputStream();
			LOGGER.debug("�������� ObjectOutputStream ");
			ObjectOutputStream object_output=new ObjectOutputStream(output_stream);
			LOGGER.debug("�������� ������ � ����� ��� ������ �������");
			// ���������� ���������� ������������� ��� ������� - ������
			ExchangeClient client_exchange=new ExchangeClient(tempDirectory);
			// ������� ����� �� ������, �������������� ��� ���������
			object_output.writeObject(client_exchange.decode(client_request));
			object_output.close();
			output_stream.close();
		}catch(IOException ex){
			LOGGER.error("IOException: "+ex.getMessage());
		}catch(ClassNotFoundException ex){
			LOGGER.error("ClassNotFoundException:"+ex.getMessage());
		}catch(Exception ex){
			LOGGER.error("Exception:"+ex.getMessage());
		}
		/*
		PrintWriter out=response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<h1> hello from servlet </h1>");
		out.println("</body>");
		out.println("</html>");
		*/
		LOGGER.debug("doAction:end");
    }
}
