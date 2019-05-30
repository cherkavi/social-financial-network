package bc.applet.CardManager.Server;


import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bc.applet.CardManager.manager.ClientExchange;
import bc.applet.CardManager.manager.transport.Transport;

/**
 * Данный сервлет служит сервером для запросов удаленных клиентов,
 * которые являются обертками над SmartCardReader и SmartCard в этом Reader-е
 */
public class CardServer extends HttpServlet {
	private final static Logger LOGGER=Logger.getLogger(CardServer.class);
	
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CardServer() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
/*	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
*/
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
/*	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
*/
    protected void service(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
    	LOGGER.debug("connect client");
    	/*if(request.getSession().isNew()){
    		LOGGER.warn("NEW USER:"+request.getSession().getId());
    	}else{
    		LOGGER.warn("old_user:"+request.getSession().getId());
    	}*/
    	doAction(request,response);
    }
	
	private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doAction:begin");
		try{
			ServletInputStream input_stream=request.getInputStream();
			ObjectInputStream object_input_stream=new ObjectInputStream(input_stream);
			LOGGER.debug("попытка получения данных от клиента ");
			Object temp_object=object_input_stream.readObject();
			LOGGER.debug("полученный объект:"+temp_object);
			Transport client_request=(Transport)temp_object;
			input_stream.close();
			LOGGER.debug("Получить контекст для вывода данных ");
			ServletOutputStream output_stream=response.getOutputStream();
			LOGGER.debug("Получить ObjectOutputStream ");
			ObjectOutputStream object_output=new ObjectOutputStream(output_stream);
			LOGGER.debug("Записать объект в поток для ответа клиенту");
			// передавать уникальный идентификатор для клиента - сессию
			ClientExchange client_exchange=new ClientExchange(request.getSession().getId());
			// послать ответ на запрос, предварительно его обработав
			object_output.writeObject(client_exchange.Decode(client_request));
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
