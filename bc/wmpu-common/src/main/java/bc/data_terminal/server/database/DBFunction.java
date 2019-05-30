package bc.data_terminal.server.database;

import java.text.SimpleDateFormat;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.sql.*;

import oracle.jdbc.internal.OracleCallableStatement;

import org.apache.log4j.*;
import org.w3c.dom.Document;

import BonCard.DataBase.UtilityConnector;

/** �����, ������� ������ ������ ��� ������������� */
public class DBFunction {
	private static Logger LOGGER=Logger.getLogger(DBFunction.class);
	private static SimpleDateFormat sqlDateFormat=new SimpleDateFormat("dd.MM.yyyy");

	
// ------------------------ READ OBJECT FROM DATABASE:BEGIN -------------------------	
	
	/** <b>��������� ������ �� byte[] � ������� ������</b>
	 * @param ������ �� ����, ������� �������� ������ 
	 * @return ����������� �� ������ ������������������ ������
	 * */
	private Object readObjectFromByteArray(byte[] array_of_byte){
		Object return_value=null;
		try{
			if(array_of_byte==null){
				LOGGER.error("readObjectFromByteArray: parameter is null ");
			}else{
				ByteArrayInputStream byte_array_input_stream=new ByteArrayInputStream(array_of_byte);
				ObjectInputStream ois=new ObjectInputStream(byte_array_input_stream);
				return_value=ois.readObject();
				LOGGER.debug("readObjectFromByteArray: Object read from byte_array is OK");
			}
		}catch(Exception ex){
			LOGGER.error("readObjectFromByteArray: Error:"+ex.getMessage());
		}
		return return_value;
	}
	
	/** ��������� ������������� ������� ��������� �� ��������� ������ � ������, ������� ��������� � �������������� 
	 * � ������� ��� ������������, ������� ��������� �� ������ ����������
	 * */
	public String fillIdentifier(ClientIdentifier identifier, Connection connection){
		String return_value="";
		CallableStatement statement=null;
		// ��������, ���������� ��� ���� ������, ������ ��� ��������� ��������
		try{
			LOGGER.debug("getTerminalId: get connection ");
			// ORACLE
			LOGGER.debug("getTerminalId: create statement");
			statement = connection.prepareCall("{?= call pack_bc_connect.get_connection(?, ?, ?, ?, ?, ?)}");
			LOGGER.debug("getTerminalId: set parameters");
			statement.registerOutParameter(1, Types.VARCHAR); // return value
			statement.setString(2, identifier.getLogin());    // user login
            statement.setString(3,identifier.getPassword());  // user password
            statement.registerOutParameter(4, Types.VARCHAR); // id_terminal
            statement.registerOutParameter(5, Types.VARCHAR); // contact name
            statement.registerOutParameter(6, Types.VARCHAR); // id_session
            statement.registerOutParameter(7, Types.VARCHAR); // error
			LOGGER.debug("getTerminalId: execute procedure ");
			statement.executeUpdate();
			if(statement.getString(1).equals("0")){
				identifier.setTerminalId(statement.getString(4));
				return_value=statement.getString(5);
			}else{
				LOGGER.debug("getTerminalId: false");
			}
            statement.close();
		}catch(SQLException ex){
			LOGGER.error("getTerminalId: SQLException:"+ex.getMessage());
		}catch(Exception ex){
			LOGGER.error("getTerminalId Exception:"+ex.getMessage());
		}finally{
			if(statement!=null){
				try{
					statement.close();
				}catch(Exception ex){};
			}
		}
		return return_value;
	}
	
	/** ��������� �� ���� ������ �������� ��������� ��� �������
	 * @param identifier ���������� ������������� ������� 
	 * @param taskId - ���������� ����� taskId ��� ������, �� ������� �������������� �������� 
	 * @param parameter_name ���������� ��� ��������� �� ������� �������
	 * ������ ������� �������� ��������� ��������<br>
		FUNCTION get_dt_term_param(
		     p_id_term IN NUMBER
		     p_id_task IN NUMBER
		    ,p_id_param IN VARCHAR
		    ,p_value_param OUT BLOB
		    ,p_error_string OUT VARCHAR2
		) RETURN NUMBER;
	 * 
	 * */
	private byte[] readByteArrayFromDatabaseAsObject(ClientIdentifier identifier,
													 Integer taskId, 
													 String parameter_name,
													 Connection connection){
		byte[] return_value=null;
		OracleCallableStatement statement=null;
		try{
			// ORACLE
			LOGGER.debug("readByteArrayFromDatabaseAsObject: create statement");
			//String query="declare l_res number; begin l_res := PACK_BC_APPLET.get_param_value(?, ?, ?,?); end;";
			String query="{? = call "+getGeneralDBScheme(connection)+".PACK_BC_DT.get_dt_term_param(?, ?, ?, ?, ?)}";
			statement=(OracleCallableStatement)connection.prepareCall(query);
			LOGGER.debug("readByteArrayFromDatabaseAsObject: set parameters");
			statement.registerOutParameter(1, Types.VARCHAR);
        	statement.setString(2,identifier.getTerminalId());
        	statement.setString(3, taskId.toString());
        	statement.setString(4,parameter_name);
        	statement.registerOutParameter(5,Types.BINARY);
        	statement.registerOutParameter(6, Types.VARCHAR);
        	LOGGER.debug("readByteArrayFromDatabaseAsObject: execute procedure ");
        	statement.execute();
        	LOGGER.debug("readByteArrayFromDatabaseAsObject: identifier:"+identifier.getTerminalId());
        	LOGGER.debug("readByteArrayFromDatabaseAsObject: param_name:"+parameter_name);
        	if(statement.getString(1).equals("0")){
        		LOGGER.debug("readByteArrayFromDatabaseAsObject: OK");
        	}else{
        		LOGGER.debug("readByteArrayFromDatabaseAsObject: ERROR"+statement.getString(6)); 
        	}
        	return_value=statement.getBytes(5);
        	statement.close();
		}catch(SQLException ex){
			LOGGER.error("readByteArrayFromDatabaseAsObject SQLException:"+ex.getMessage());
		}catch(Exception ex){
			LOGGER.error("readByteArrayFromDatabaseAsObject    Exception:"+ex.getMessage());
		}finally{
			if(statement!=null){
				try{
					statement.close();
				}catch(Exception ex){};
			}
		}
		return return_value;
	}

	/** 
	 * ��������� ���� BLOB �� ���� ������ �� ����������� �������������� � �� ����� ���������
	 * @param identifier - ���������� ������������� 
	 * @param parameter_name - ��� ��������, �� �������� ����� ���������� ������
	 */
	public Object getObjectFromDataBase(ClientIdentifier identifier, 
											   Integer taskId, 
											   String parameter_name,
											   Connection connection){
		Object return_value=null;
		try{
			return_value=readObjectFromByteArray(readByteArrayFromDatabaseAsObject(identifier, taskId, parameter_name,connection));
		}catch(Exception ex){
			LOGGER.debug("getObjectFromDataBase: Error:"+ex.getMessage());
		}
		return return_value;
	}
	
	
	
	/** �������� �������� � ���� ������ �� ���� ������
	 * @param identifier - ���������� ������������� ������������
	 * @param taskId - ���������� ����� ������, �� ������� ����������� ������ �������� 
	 * @param parameter_name - ���������� (� ��������� ������� ������������) �������� ��������� � ���� ������
	 * */
	public String getStringFromDataBase(ClientIdentifier identifier,
											   Integer taskId,
											   String parameter_name,
											   Connection connection){
		String return_value="";
		try{
			return_value=(String)getObjectFromDataBase(identifier, taskId, parameter_name,connection);
		}catch(Exception ex){
			// object from database is not Strig
		}
		return return_value;
	}
// ------------------------ READ OBJECT FROM DATABASE:END -------------------------	
	
// ------------------------ WRITE OBJECT TO DATABASE:BEGIN -------------------------	
	/** <b>�������� ������ � byte[] � ������� ���</b> 
	 * @param object - ������, ������� ����� ��������
	 * @return ���������� ������ �� ����, ������� �������� ������ ������
	 * */
	private byte[] writeObjectToByteArray(Object object){
		byte[] return_value=null;
		try{
			ByteArrayOutputStream byte_array_output_stream=new ByteArrayOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(byte_array_output_stream);
			oos.writeObject(object);
			return_value=byte_array_output_stream.toByteArray();
			LOGGER.debug("writeObjectToByteArray is OK");
		}catch(Exception ex){
			LOGGER.error("writeObjectToByteArray Error:"+ex.getMessage());
			return_value=null;
		}
		return return_value;
	}
	/**
	 * �������� byte[] � ���� ������ ( � ���� BLOB )
	 * @param session_id - ���������� ������������� ������
	 * @param taskId - ���������� ������������� ������
	 * @param parameter_name - ���������� ��� ��������� � �������� ������
	 * @param  value ��� �������� ��� ������ � ���� byte[]
	 */
	private boolean writeByteArrayAsObjectToDataBase(ClientIdentifier identifier,
													 Integer taskId,
													 String parameter_name,
													 byte[] value,
													 Connection connection){
		boolean return_value=false;
		CallableStatement statement=null;
		// ��������, ���������� ��� ���� ������, ������ ��� ��������� ��������
		try{
			LOGGER.debug("writeByteArrayAsObjectToDataBase: get connection ");
			// ORACLE
			LOGGER.debug("writeByteArrayAsObjectToDataBase: create statement");
			statement = connection.prepareCall("{?= call "+getGeneralDBScheme(connection)+".PACK_BC_DT.set_dt_term_param(?, ?, ?, ?,?)}");
			LOGGER.debug("writeByteArrayAsObjectToDataBase: set parameters");
			statement.registerOutParameter(1, Types.VARCHAR);
            statement.setString(2,identifier.getTerminalId());
            statement.setInt(3,taskId);
            statement.setString(4,parameter_name);
            statement.setBytes(5,value);
            statement.registerOutParameter(6, Types.VARCHAR);
			LOGGER.debug("writeByteArrayAsObjectToDataBase: execute procedure ");
			statement.executeUpdate();
			if(statement.getString(1).equals("0")){
				LOGGER.debug("writeByteArrayAsObjectToDataBase: data saved");
				return_value=true;
			}else{
				return_value=false;
				LOGGER.error("writeByteArrayAsObjectToDataBase ������� �� ���������� ������� �������� "+statement.getString(6));
			}
            statement.close();
		}catch(SQLException ex){
			return_value=false;
			LOGGER.error("writeByteArrayAsObjectToDataBase: SQLException:"+ex.getMessage());
		}catch(Exception ex){
			return_value=false;
			LOGGER.error("writeByteArrayAsObjectToDataBase Exception:"+ex.getMessage());
		}finally{
			if(statement!=null){
				try{
					statement.close();
				}catch(Exception ex){};
			}
		}
		return return_value;
	}
	
	
	
	/** �������� � ���� BLOB � ���� ������ �� ����������� �������������� � �� ����� ���������
	 * @param identifier - ���������� ������������� 
	 * @param taskId - ���������� ������������� ������
	 * @param parameter_name - ��� ��������, �� �������� ����� ���������� ������
	 * @param for_write - ������, ������� ���������� ��������
	 * ������ ���������� �������� ��������� �������� <br>
		FUNCTION set_dt_term_param(
		     p_id_term IN NUMBER
		    ,p_id_param IN VARCHAR
		    ,p_value_param IN BLOB
		    ,p_error_string OUT VARCHAR2
		) RETURN NUMBER;
	 * 
	 * */
	public boolean putObjectToDataBase(ClientIdentifier identifier, 
											  Integer taskId,
											  String parameter_name,
											  Object object_for_write,
											  Connection connection){
		boolean return_value=false;
		try{
			if(object_for_write!=null){
				return_value=writeByteArrayAsObjectToDataBase(identifier,
															  taskId, 
															  parameter_name, 
															  writeObjectToByteArray(object_for_write),
															  connection);
			}
		}catch(Exception ex){
			LOGGER.error("putObjectToDataBase Error:"+ex.getMessage());
		}
		return return_value;
	}
	
	/**
	 * ��������� ������ � ���� ������ �� �� ����������� �������������� � �� ����� ���������
	 */
	public boolean putStringToDataBase(ClientIdentifier identifier, Integer taskId, String parameter_name, String string_for_write, Connection connection){
		return putObjectToDataBase(identifier, taskId, parameter_name, string_for_write,connection);
	}
	
// ------------------------ WRITE OBJECT TO DATABASE:END ------------------------- 	
	
	/** �������� ������ ������������� � ���� <id><name>*/
	public HashMap<String,String> getUsers(Connection connection){
		HashMap<String,String> return_value=new HashMap<String,String>();
		ResultSet rs=null;
		try{
			rs=connection.createStatement().executeQuery("select ID_TERM, JUR_PRS_NAME from "+getGeneralDBScheme(connection)+".vc_dt_term_all");
			LOGGER.debug("getUsers: begin:");
			while(rs.next()){
				LOGGER.debug("getUsers: next:");
				return_value.put(rs.getString(1),rs.getString(1));
			}
			LOGGER.debug("getUsers: end:");
			rs.close();
			LOGGER.debug("getUsers: OK");
		}catch(SQLException ex){
			LOGGER.error("getUsers SQLException: "+ex.getMessage());
		}
		try{
			if(rs!=null){
				rs.getStatement().close();
			}
		}catch(Exception ex){};
		return return_value;
	}
	
	/** �������� ������ ��������� ����� �� ����������� ������������ */
/*	public static HashMap<String,String> getTasks(ClientIdentifier identifier){
		String user_name=getTerminalId(identifier);
		Connection connection=Connector.getConnection();
		HashMap<String,String> return_value=new HashMap<String,String>();
		try{
			PreparedStatement ps=connection.prepareStatement("select v_dt_term_task.id_task, task_table.name_task from v_dt_term_task inner join v_dt_task task_table on (v_dt_term_task.id_task=task_table.id_task) where v_dt_term_task.id_term=?");
			ps.setString(1, user_name);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				return_value.put(rs.getString(1), rs.getString(2));
			}
			rs.close();
			ps.close();
		}catch(SQLException ex){
			LOGGER.error("getTasks: SQLException:"+ex.getMessage());
		}
		Connector.closeConnection(connection);
		return return_value;
	}
*/
	/** �������� XML ����, ������� �������� �� ������� ��� ����������
	 * @param user_name ��� ������������, �� �������� ���������� �������� ������ ���������
	 * */
	public Document getVisualXmlByUser(ClientIdentifier identifier, Connection connection){
		VisualXml xml=new VisualXml(identifier.getTerminalId());
		return xml.getVisualXml(connection);
	}
	
/*	public static void main(String[] args){
		Document doc=getVisualXmlByUser("111");
		try{
			javax.xml.transform.TransformerFactory transformer_factory = javax.xml.transform.TransformerFactory.newInstance();  
			javax.xml.transform.Transformer transformer = transformer_factory.newTransformer();  
			javax.xml.transform.dom.DOMSource dom_source = new javax.xml.transform.dom.DOMSource(doc); // Pass in your document object here  
			java.io.FileWriter out=new java.io.FileWriter("c:\\temp_error.xml");
			//string_writer = new Packages.java.io.StringWriter();  
			javax.xml.transform.stream.StreamResult stream_result = new javax.xml.transform.stream.StreamResult(out);  
			transformer.transform(dom_source, stream_result);  
		}catch(Exception ex){
			LOGGER.error("main: "+ex.getMessage());
		}
	}
*/	
	
	
	// ------------------------ Get Terminal ID by Client Identifier
	/**�����, ������� �� ClientIdentifier �������� �� ���� ������ ���������� ����� ���������<br>  
	 * ��������� ������ ClientIdentifier ������������ �������(Terminal_id), �� ��������� ��������� (Login and Password)
	 * <br>
	 * <i>
		DECLARE
			p_name_user VARCHAR2(100) := 'adfadljl'; 
			p_password VARCHAR2(100) := 'dafdf'; 
			p_id_data_terminal NUMBER; 
			p_id_session NUMBER; 
		BEGIN
			DBMS_OUTPUT.put_line(pack_bc_connect.get_connection(p_name_user,p_password,p_id_data_terminal,p_id_session));
			DBMS_OUTPUT.put_line(p_id_data_terminal);
			DBMS_OUTPUT.put_line(p_id_session);
		END;
	 * </i>
	 * 
	public static String getTerminalId(ClientIdentifier identifier){
		String return_value="";
		// ��������, ���������� ��� ���� ������, ������ ��� ��������� ��������
		Connection connection=null;
		try{
			LOGGER.debug("getTerminalId: get connection ");
			// ORACLE
			connection=Connector.getConnection();
			LOGGER.debug("getTerminalId: create statement");
			//CallableStatement statement = connection.prepareCall("{?= call pack_bc_connect.get_connection(?, ?, ?, ?)}");
			//LOGGER.debug("getTerminalId: set parameters");
			//statement.registerOutParameter(1, Types.VARCHAR);
			//statement.setString(2, identifier.getLogin());
            //statement.setString(3,identifier.getPassword());
            //statement.registerOutParameter(4, Types.VARCHAR);
            //statement.registerOutParameter(5, Types.VARCHAR);
			//LOGGER.debug("getTerminalId: execute procedure ");
			//statement.executeUpdate();
			//if(statement.getString(1).equals("0")){
			//  identifier.setTerminalId(statement.getString(4));
			//	LOGGER.debug("getTerminalId:"+identifier.getTerminalId());
			//	return_value=identifier.getTerminalId();
			//}else{
			//	LOGGER.debug("getTerminalId: false");
			//}
			CallableStatement statement = connection.prepareCall("{?= call pack_bc_connect.get_connection(?, ?, ?, ?, ?, ?)}");
			LOGGER.debug("getTerminalId: set parameters");
			statement.registerOutParameter(1, Types.VARCHAR); // return value
			statement.setString(2, identifier.getLogin());    // user login
            statement.setString(3,identifier.getPassword());  // user password
            statement.registerOutParameter(4, Types.VARCHAR); // id_terminal
            statement.registerOutParameter(5, Types.VARCHAR); // contact name
            statement.registerOutParameter(6, Types.VARCHAR); // id_session
            statement.registerOutParameter(7, Types.VARCHAR); // error
			LOGGER.debug("getTerminalId: execute procedure ");
			statement.executeUpdate();
			if(statement.getString(1).equals("0")){
				identifier.setTerminalId(statement.getString(4));
				return_value=statement.getString(4);
			}else{
				LOGGER.debug("getTerminalId: false");
			}
			
            statement.close();
		}catch(SQLException ex){
			LOGGER.error("getTerminalId: SQLException:"+ex.getMessage());
		}catch(Exception ex){
			LOGGER.error("getTerminalId Exception:"+ex.getMessage());
		}finally{
			if(connection!=null){
				Connector.closeConnection(connection);
			}
		}
		return return_value;
	}*/

	/* �������� �� �������������� ������������ � ����� ������ XML - ���� ��� ��������� 
	 * @param ���������� ������������� ������� 
	 * @param ������, ������� ��� ������� � ���� Data[0]
	 * @return ���������� Document, ������� �������� ������ ����, ���� �� null, ���� ������ �� ��������  
	 *
	public static String getXmlFormatFile(ClientIdentifier identifier, Object task){
		String return_value=null;
		if((task!=null)&&(task instanceof String)){
			if(isTerminalIdCanGetOperation(identifier, (String)task)){
				LOGGER.debug("send XML-file to client as answer");
				String terminal_id=getTerminalId(identifier);
				//String file_id=getFileIdByTaskName(terminal_id, (String)task);
				return_value=getXmlFormatFileById(terminal_id, (String)task);
			}else{
				LOGGER.debug("getXmlFormatFile Access Denied;    Task:"+task);
				// return_value=null;
			}
		}else{
			LOGGER.error("getXmlFormatFile: task is null or not recognized");
			// return_value=null;
		}
		return return_value;
	} */

	/** �������� �� �������������� ������������ � ����� ������ XML - ���� ��� ��������� 
	 * @param ���������� ������������� ������� 
	 * @param ������������� ������������� ������ 
	 * @param ���������� ������������� ������������� ������ 
	 * @return ���������� Document, ������� �������� ������ ����, ���� �� null, ���� ������ �� ��������  
	 * */
	public String getXmlFormatFile(ClientIdentifier identifier, String taskId, Connection connection){
		String return_value=null;
		if(isTerminalIdCanGetOperation(identifier, taskId)){
			LOGGER.debug("send XML-file to client as answer");
			//String file_id=getFileIdByTaskName(terminal_id, task+";"+taskId);
			return_value=getXmlFormatFileById(identifier.getTerminalId(),taskId, connection);
		}else{
			LOGGER.debug("getXmlFormatFile Access Denied;  Identifier:"+identifier.getTerminalId()+"   Task: "+taskId+";");
			// return_value=null;
		}
		return return_value;
	}
	
	
	/** �������� ������, ������� �������� XML ����, �� ��� ����������� ������
	 * @param file_id - ����� �����, ������� ����� ��������
	 * @return null - ���� ���� �� ������   
	 */
	private String getXmlFormatFileById(String file_id,String idTask, Connection connection){
		StringBuilder return_value=new StringBuilder();
		CallableStatement statement=null;
		try{
			statement=connection.prepareCall("{? = call "+getGeneralDBScheme(connection)+".PACK_BC_DT.get_xml_file_format2(?,?,?,?)}");
			statement.registerOutParameter(1, Types.INTEGER);
			statement.setInt(2, Integer.parseInt(file_id));
			statement.setInt(3, Integer.parseInt(idTask));
			statement.registerOutParameter(4,Types.CLOB);
			statement.registerOutParameter(5, Types.VARCHAR);
			statement.execute();
			LOGGER.debug("begin:");
			if(statement.getInt(1)==0){
				Reader reader=statement.getCharacterStream(4);
				//Reader reader= new InputStreamReader(new ByteArrayInputStream(statement.getBytes(3)));
				BufferedReader br=new BufferedReader(reader);
				String current_string=null;
				while((current_string=br.readLine())!=null){
					return_value.append(current_string);
					//LOGGER.LOGGER.debug(current_string);
				}
			}
			LOGGER.debug("end:");
		}catch(SQLException ex){
			LOGGER.error("getFileIdByTaskName SQLException:"+ex.getMessage());
		}catch(Exception ex){
			LOGGER.error("getFileIdByTaskName    Exception:"+ex.getMessage());
		}finally{
			UtilityConnector.closeQuietly(statement);
		}
		return (return_value.length()==0)?null:return_value.toString();
	}
	
	
	
	/** �������� �� ����� ������ ���� ������, � �������� ��� ������ ���������
	 * @param terminal_id - ���������� ������������� ���������  
	 * @param task_name - ��� Task ����� �������� ����� �������� 
	 * @return null
	 * */
	public String getFileIdByTaskName(String taskId, Connection connection){
		String return_value=null;
		PreparedStatement ps=null;
		try{
			// INFO - �������� �� ����� Task ���� ������ XML  
			ps=connection.prepareStatement("select id_exchange_file from "+getGeneralDBScheme(connection)+".VC_DT_TASK_ALL where id_task=?");
			ps.setString(1, taskId);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				return_value=rs.getString(1);
			}else{
				LOGGER.debug("getFileIdByTaskName: terminal was not founded:  task_name:"+taskId);
			}
			rs.close();
			ps.close();
		}catch(SQLException ex){
			LOGGER.error("getFileIdByTaskName SQLException:"+ex.getMessage());
		}catch(Exception ex){
			LOGGER.error("getFileIdByTaskName    Exception:"+ex.getMessage());
		}finally{
			if(ps!=null){
				try{
					ps.close();
				}catch(Exception ex){};
			}
		}
		return return_value;
	}
	
	/** ��������� ��� ������� ������������ ���������� �� ������������ �������� ���������� ����� �� ����� ������ (��� ������ = ��� ������)*/
	private boolean isTerminalIdCanGetOperation(ClientIdentifier identifier, String task){
		// INFO ����������� �������� ��������������� ����� �� ������� � ����������� �������� ������ �� ������ ��������� �������� ��� ������� ������������ �� ��������� Task (�� FormatFile)
		return true;
	}

	/** �������� ��� ����� ��� ���������� �������� */
	public String getGeneralDBScheme(Connection connection){
		String returnValue="";
		ResultSet rs=null;
		try{
			rs=connection.createStatement().executeQuery("SELECT general_db_scheme FROM v_user_param_ln");
			if(rs.next()){
				returnValue=rs.getString(1);
			}
			rs.close();
		}catch(Exception ex){
			LOGGER.error("getGeneralDBScheme Exception:"+ex.getMessage());
		}finally{
			try{
				rs.getStatement().close();
			}catch(Exception ex){};
		}
		return returnValue;
	}

	/** �������� ���������� FileTransfer ��� ������ ����������� �������� ����� */
	public Integer getIdFileTransfer(Connection connection){
		Integer returnValue=null;
		ResultSet rs=null;
		try{
			rs=connection.createStatement().executeQuery("select bc_admin.pack_bc_dt.get_id_file_transfer from dual");
			rs.next();
			returnValue=rs.getInt(1);
		}catch(Exception ex){
			LOGGER.error("getIdFileTransfer Exception: "+ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.getStatement().close();
				}
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	/** �������� ���������� ������������� ��� "�����������" �����, ������� ����������� ������ � ���� <br>
	 * <b>������ ������� </b> 
	 * @param terminalId - ID ���������
	 * @param fileNameClien - ��� ����� � ��������� �������
	 * @param fileNameServer - ��� ����� � ��������� ������� 
	 * @param dateSend - ���� ������� 
	 * @param sendState - ��������� ������� (ERROR, UPLOADED/DOWNLOADED, PREPARED)
	 * @param errorText - ��������� ��������� ������
	 * @param idPattern - e
	 * @param idClub
	 * @return ���������� ��� ��������������
	 * @throws ������� ����������, ����� ���������� ������ ������  
	 */
	public Integer getIdOfSend(String terminalId,
							   String taskId,
							   Integer idFileTransfer,
							   String fileNameClient,
							   String fileNameServer,
							   java.util.Date dateSend, 
							   String sendState, 
							   String errorText, 
							   Integer idPattern, 
							   Integer idClub,
							   Connection connection) throws Exception {
		Integer returnValue=null;
		CallableStatement statement=null;
		try{
			//String query="{? = call PACK_BC_DT.get_dt_term_param(?, ?, ?, ?)}";
			statement=connection.prepareCall("{? = call "+getGeneralDBScheme(connection)+".PACK_BC_DT.write_transfer_result(?,?,?,?,?,?,?,?, ?)}");
 														     //  1                                                          2 3 4 5 6 7 8 9 10
			/*
			 * FUNCTION write_transfer_result(
			     2 - id_file_transfer -- ���������� ����� ����������� ����� �� Sequence
     			 3 - p_id_term IN NUMBER  -- �� ���������
     			 4 - idTask - ���������� ����� ������ 
    			 5 - ,p_name_soruce_file IN VARCHAR2 -- �������� ����� �� �������
    			 6 - ,p_name_file_transfer IN VARCHAR2 -- �������� ����� (+����)
    			 7 - ,p_date_transfer IN DATE  -- ���� �������
    			 8 - ,p_state_file_transfer IN VARCHAR2 -- ��������� �������
    			 9 - ,p_error_text IN VARCHAR2  -- ����� ������
    			10 - ,p_id_exchange_file IN NUMBER -- �� �������
    			11 - ,p_id_club IN NUMBER  -- �� ����� (����� ���� null)
    			12 - ,p_id_file_transfer OUT NUMBER -- ������������ �� �������
    			13 - ,p_result_string OUT VARCHAR2 -- ���� ���� ������ ������, ������������ ����� ������
				1 - ) RETURN NUMBER    -- ������������ 0 - �������� �������, ����� - ��� ������
			 */
			statement.registerOutParameter(1, Types.INTEGER);
			statement.setInt(2,Integer.valueOf(idFileTransfer));
			statement.setInt(3,Integer.valueOf(terminalId));
			statement.setInt(4,Integer.valueOf(taskId));
			statement.setString(5, fileNameClient);
			statement.setString(6,fileNameServer);
			statement.setString(7,sendState);
			statement.setString(8,errorText);
			if(idClub==null){
				statement.setNull(9,Types.INTEGER);
			}else{
				statement.setInt(9,idClub);
			}
			statement.registerOutParameter(10, Types.VARCHAR);
			statement.execute();
			if(statement.getInt(1)==0){
				returnValue=idFileTransfer;
			}else{
				throw new Exception(statement.getString(10));
			}
		}catch(Exception ex){
			LOGGER.error("getIdOfSend Exception:"+ex.getMessage());
		}finally{
			try{
				if(statement!=null){
					statement.close();
				}
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	/** �������� ��� ������� � ���� ������ � ������� ���������� �������� ������ ����:
	 * @param patternId - ���������� ��� ������� � ��������� ���� ������  
	 * @return null - ���� �� ������� �������� ��� �������, ���� �� ��� ������� � ������ �������� ��������� ������� 
	 * */
	public String getTableNameForSaveDataFromFile(String patternId, Connection connection){
		String returnValue=null;
		ResultSet rs=null;
		try{
			rs=connection.createStatement().executeQuery("SELECT db_table_name FROM "+getGeneralDBScheme(connection)+".v_dt_exchange_file WHERE id_exchange_file ="+patternId);
			if(rs.next()){
				returnValue=rs.getString(1);
			}
		}catch(Exception ex){
			LOGGER.error("getTableNameForSaveDataFromFile: Exception: "+ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.getStatement().close();
				}
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	/** �������� ���� ������� � ���� ������ 
	 * � ������� ����� ���������� �������� �� ����� ������ �� ������� � ��������� � ����
	 * (���� �������� � ������� ����������) 
	 * @param patternId - ��� ������� �����, ��� �������� "����������" ���� �� ���� ������
	 * */
	public ArrayList<DbField> getTableFieldsForSaveDataFromFile(String patternId, Connection connection){
		ArrayList<DbField> list=new ArrayList<DbField>();
		ResultSet rs=null;
		try{
			rs=connection.createStatement().executeQuery("SELECT db_column_name,format_field, required FROM " + getGeneralDBScheme(connection) + ".v_dt_exchange_file_fields WHERE id_exchange_file ="+patternId+"  ORDER BY order_number");
			int counter=0;
			while(rs.next()){
				list.add(new DbField(counter++,
									 rs.getString(1),
									 convertStringTypeToSqlType(rs.getString(2))
									 )
					     );
			}
		}catch(Exception ex){
			LOGGER.error("getTableNameForSaveDataFromFile: Exception: "+ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.getStatement().close();
				}
			}catch(Exception ex){};
		}
		return list;
	}
	
	/** �������� java.sql.Types ������ �� ����� java.lang.* (Integer, String, Float, Date)
	 * <table>
	 * 		<tr>
	 * 			<td>java.lang.Integer</td> <td> - </td> <td>java.sql.Types.INTEGER</td>
	 * 		</tr> 
	 * 		<tr>
	 * 			<td>java.lang.Float</td> <td> - </td> <td>java.sql.Types.FLOAT</td>
	 * 		</tr> 
	 * 		<tr>
	 * 			<td>java.lang.String</td> <td> - </td> <td>java.sql.Types.VARCHAR</td>
	 * 		</tr> 
	 * 		<tr>
	 * 			<td>java.lang.Date</td> <td> - </td> <td>java.sql.Types.DATE</td>
	 * 		</tr> 
	 * */
	private int convertStringTypeToSqlType(String stringValue){
		if(stringValue.indexOf("Integer")>0){
			return Types.INTEGER;
		}
		if(stringValue.indexOf("Float")>0){
			return Types.FLOAT;
		}
		if(stringValue.indexOf("String")>0){
			return Types.VARCHAR;
		}
		if(stringValue.indexOf("Date")>0){
			return Types.DATE;
		}
		return Types.VARCHAR;
	}
	
	private final static String hexChars[] = { "0", "1", "2", "3", "4", "5","6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
	/** ������������� ��������� ������������������ �� Hex �����, ��������� ������ 
	 * @param count - ������ ��������� ������������������, ������� ���������� �������� 
	 * */
	private static String getRandomChar(int count){
        StringBuilder return_value=new StringBuilder();
        Random random=new java.util.Random();
        int temp_value;
        for(int counter=0;counter<count;counter++){
            temp_value=random.nextInt(hexChars.length);
            return_value.append(hexChars[temp_value]);
        }
        return return_value.toString();
	}

	
	/** ���������� ��� ���������� ������ ������ ���������
	 * @param list - ������ ������ 
	 * @param state - ���������, ������� ����� ���������� ��� ����� 
	 * @return ���-�� ������� ����������� ������ ( �� ������ ����������� ������ ) 

FUNCTION update_transfer_result(
     p_id_file_transfer IN NUMBER -- ���������� ����� ID_FILE_TRANSFER, ������� ������� �� [bc_admin.VC_DT_TRANSFER_STAT_CLUB_ALL]
    ,p_name_source_file IN VARCHAR2 -- ��� �����, ������� ������������ �� ������� ( null) 
    ,p_name_file_transfer IN VARCHAR2 -- ��� ����� ��� �������� �� ������� ( ��� ������������ �� ����� )
    ,p_date_transfer IN DATE -- ���� �������� ����� 
    ,p_state_file_transfer IN VARCHAR2 -- ��������� ����� (ERROR, PREPARED, DOWNLOADED, UPLOADED)
    ,p_error_text IN VARCHAR2
    ,p_result_string OUT VARCHAR2
) RETURN NUMBER;
	 */
	public int setFileState(ArrayList<CreatedFile> list, String state, Connection connection){
		int returnValue=0;
		CallableStatement statement=null;
		try{
			statement=connection.prepareCall("{? = call "+getGeneralDBScheme(connection)+".PACK_BC_DT.update_transfer_result(?,?,?,?,?,?)}");
			statement.registerOutParameter(1, Types.INTEGER);
			statement.registerOutParameter(7, Types.VARCHAR);
			for(int counter=0;counter<list.size();counter++){
				statement.clearParameters();
				statement.setInt(2, list.get(counter).getIdFileTransfer());
				statement.setString(3, list.get(counter).getThisFileName());
				statement.setString(4, list.get(counter).getRemoteFileName());
				statement.setString(5, state);
				statement.setNull(6, java.sql.Types.VARCHAR);
				statement.execute();
				if(statement.getInt(1)==0){
					returnValue++;
				}else{
					// not saved
				}
			}
		}catch(Exception ex){
			LOGGER.error("setFileState: Exception: "+ex.getMessage());
		}finally{
			try{
				if(statement!=null){
					statement.close();
				}
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	
	/** ������� ������ Path To File, ������� ����� ������������ ��� ������� ������� �� ������� ��������� 
	 * @param tempPath - ���� �� ���������� �������� 
	 * @param identifier - ������������� ������������ 
	 * @param taskId - ����� ������, �� �������� ���������� ������ �������� 
	 * @return ������ �������������� ������, ������� ����� ��������� �������
	 * 
	 *  �������� ��������� ������ :
		�������� �����, ������� ����� �������
select * from bc_admin.VC_DT_TRANSFER_STAT_CLUB_ALL where 
id_term=111
and state_file_transfer='SAVED_TO_FILE' 
and id_task=5 	
	--(ID_EXCHANGE_FILE)
	--(ID_FILE_TRANSFER)

	�������� �������, �� ������� ����� ������ ������ 
select * from BC_ADMIN.v_dt_exchange_file where ID_EXCHANGE_FILE=69
	-- (DB_TABLE_NAME)
	
	�������� ��� ����, ������� ������ �������������� � CSV �����, ������� ����������� ������������
select * from BC_ADMIN.v_dt_exchange_file_fields  where ID_EXCHANGE_FILE=69 order by order_number
	-- (DB_COLUMN_NAME)
	
	
    ��������� ������:
select * -- (DB_COLUMN_NAME)
from -- (DB_TABLE_NAME)
where ID_FILE_TRANSFER= --(ID_FILE_TRANSFER)	 *  
	 */
	public ArrayList<CreatedFile> getFileForSendToClient(String tempPath, ClientIdentifier identifier, String taskId, Connection connection){
		ArrayList<CreatedFile> returnValue=new ArrayList<CreatedFile>();
		ResultSet rs=null;
		try{
			rs=connection.createStatement().executeQuery("select * from bc_admin.VC_DT_TRANSFER_STAT_CLUB_ALL where id_term="+identifier.getTerminalId()+" and state_file_transfer='PREPARED' and id_task="+taskId);
			while(rs.next()){
				int idExchangeFile=rs.getInt("ID_EXCHANGE_FILE");
				int idFileTransfer=rs.getInt("ID_FILE_TRANSFER");
				// �������� �������, �� ������� ����� ������ ������ 
				String tableName=getTableNameByExchangeFile(connection,idExchangeFile);
				// �������� ��� ����, ������� ������ �������������� � CSV �����, ������� ����������� ������������
				ArrayList<DbField> fields=getFieldsByExchangeFile(connection, idExchangeFile);
				// ������������ ��� ����� 
				String fileName=sdf.format(new java.util.Date())+"__"+getRandomChar(4)+".csv";
				String fullPathToFile=tempPath+fileName;
				// �������� ������ � ��������� � ���������� ��� ����� �� ���������� ����   
				if(saveToCSV(connection, fullPathToFile,tableName, fields, idFileTransfer)){
					returnValue.add(new CreatedFile(fileName, fullPathToFile,idFileTransfer));
				}else{
					LOGGER.error("getFileForSendToClient file not Saved "+idExchangeFile);
				}
			}
		}catch(Exception ex){
		}finally{
			try{
				if(rs!=null){
					rs.getStatement().close();
				}
			}catch(Exception ex){};
		};
		return returnValue;
	}
	
	/** ������� ������ �� ���� ������ � ��������� �� � CSV ����� 
	 * @param connection - ���������� � ����� ������ 
	 * @param path - ������ ���� � ����� 
	 * @param tableName - ��� �������, � ������� ������� �������� ������ 
	 * @param fields - ����, ������� ���������� ����� �� ���� ������  
	 * @return
	 */
	private boolean saveToCSV(Connection connection, String path, String tableName, ArrayList<DbField> fields,int idFileTransfer){
		boolean returnValue=false;
		ResultSet rs=null;
		StringBuilder query=new StringBuilder();
		query.append("select ");
		for(int counter=0;counter<fields.size();counter++){
			query.append(fields.get(counter).getFieldName());
			if(counter!=(fields.size()-1)){
				query.append(", ");
			}
		};
		query.append(" from "+tableName+" where ID_FILE_TRANSFER="+idFileTransfer);
		com.csvreader.CsvWriter writer=null;
		try{
			rs=connection.createStatement().executeQuery(query.toString());
			writer=new com.csvreader.CsvWriter(path,',',Charset.forName("windows-1251"));
			// ������� CSV ����
			int dogWatcher=0;
			while(rs.next()){
				writer.writeRecord(getArrayStringFromDbFields(rs,fields));
				dogWatcher++;
				if(dogWatcher==10){
					dogWatcher=0;
					writer.flush();
				}
			}
			// ������� CSV ���� 
			returnValue=true;
		}catch(Exception ex){
			LOGGER.error("saveToCSV Exception:"+ex.getMessage());
		}finally{
			UtilityConnector.closeQuietly(rs);
			if(writer!=null){
				writer.flush();
				writer.close();
			}
		}
		return returnValue;
	}

	/** �������� �� ������� �� ��������� ����� ������ �� �����, ��� ���������� � CSV ����� */
	private String[] getArrayStringFromDbFields(ResultSet rs,ArrayList<DbField> fields){
		String[] returnValue=new String[fields.size()];
		Arrays.fill(returnValue, "");
		for(int counter=0;counter<fields.size();counter++){
			returnValue[counter]=fields.get(counter).getValue(rs);
		}
		return returnValue;
	}
	
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_");
	/** �������� ���� ��� ������� ������ �� ��������� ����� ������ */
	private ArrayList<DbField> getFieldsByExchangeFile(Connection connection, int idExchangeFile) throws SQLException{
		ArrayList<DbField> returnValue=new ArrayList<DbField>();
		ResultSet rs=connection.createStatement().executeQuery("select * from BC_ADMIN.v_dt_exchange_file_fields  where ID_EXCHANGE_FILE="+idExchangeFile+" order by order_number");
		while(rs.next()){
			returnValue.add(new DbField(rs.getInt("ORDER_NUMBER"),rs.getString("DB_COLUMN_NAME"),rs.getString("FORMAT_FIELD")));
		}
		rs.getStatement().close();
		return returnValue;
	}
	/** �������� ��� �������, � ������� ����� ������ �� ���������� ����� ������ */
	private String getTableNameByExchangeFile(Connection connection, int idExchangeFile) throws SQLException{
		String returnValue="";
		ResultSet rs=connection.createStatement().executeQuery("select * from BC_ADMIN.v_dt_exchange_file where ID_EXCHANGE_FILE="+idExchangeFile);
		if(rs.next()){
			returnValue=getGeneralDBScheme(connection)+"."+rs.getString("DB_TABLE_NAME");
		}
		rs.getStatement().close();
		return returnValue;
	}
	
	/** �������� ������� �������� ������ �������
	 * FUNCTION get_id_file_transfer() - ��������� ����������� ID ����� 
	 *  
FUNCTION write_transfer_result(
     p_id_file_transfer IN NUMBER      -- ���������� ��� ����� 
    ,p_id_term IN NUMBER               -- ID terminal
    ,p_id_task IN NUMBER               -- id task 
    ,p_name_source_file IN VARCHAR2    -- ���, ������� ���� ��������� ����� �� ������� 
    ,p_name_file_transfer IN VARCHAR2  -- ��� ( ������ ����) �� �������� ������ ���� �������� �� ������� ( ��� ������������� �� ������� )
    ,p_date_transfer IN DATE           -- ������� ����
    ,p_state_file_transfer IN VARCHAR2 -- ���������: 0 - 
    ,p_error_text IN VARCHAR2
    ,p_id_club IN NUMBER
    ,p_result_string OUT VARCHAR2
) RETURN NUMBER	 * 
	 * */

	/** �������� ������� 'UPLOAD'/'DOWNLOAD' �� ��������� ������
	 * @param terminalId - ���������� ������������� ���������
	 * @param taskId - ���������� ������������� ������ 
	 * @param dateBegin - ���� ������ 
	 * @param dateEnd - ���� ���������
	 * @param sqlParameter - 'UPLOAD' ��� 'DOWNLOAD'
	 * @return
	 */
	public Serializable[][] getHistory(String terminalId, 
											  String taskId,
											  Date dateBegin, 
											  Date dateEnd, 
											  String sqlParameter,
											  Connection connection) {
		ArrayList<String[]> list=new ArrayList<String[]>();
		ResultSet rs=null;
		try{
			String query="select * from bc_admin.VC_DT_TRANSFER_STAT_CLUB_ALL  where id_term="+terminalId+" and state_file_transfer='"+sqlParameter+"' and id_task="+taskId+" and DATE_TRANSFER BETWEEN TO_DATE('"+sqlDateFormat.format(dateBegin)+"','dd.mm.rrrr') AND TO_DATE('"+sqlDateFormat.format(dateEnd)+"','dd.mm.rrrr')";
			rs=connection.createStatement().executeQuery(query);
			while(rs.next()){
				list.add(new String[]{rs.getString("DATE_TRANSFER_FRMT"),rs.getString("NAME_FILE_TRANSFER"),rs.getString("NAME_SOURCE_FILE")});
			}
		}catch(Exception ex){
			LOGGER.error("getHistory Exception:"+ex.getMessage());
		}finally{
			try{
				if(rs!=null){
					rs.getStatement().close();
				}
			}catch(Exception ex){};
		};
		Serializable[][] returnValue=new Serializable[list.size()][];
		for(int counter=0;counter<list.size();counter++){
			returnValue[counter]=list.get(counter);
		}
		return returnValue;
	}
	
	/*
					new Serializable[][]{
						new Serializable[]{new Integer(234),"file_01.bin",new Date()},
						new Serializable[]{new Integer(235),"file_02.bin",new Date()},
						new Serializable[]{new Integer(236),"file_03.bin",new Date()},
						new Serializable[]{new Integer(237),"file_04.bin",new Date()}
				};
	 */
}






