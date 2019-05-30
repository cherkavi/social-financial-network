package bc.applet.CardManager.manager.actions.calculator;


import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.*;

import org.apache.log4j.Logger;

import bc.applet.CardManager.manager.actions.Action;
import bc.connection.Connector;
import oracle.jdbc.driver.OracleCallableStatement;

public class CalculatorDataBaseGetParameter extends ActionStepCalculator{
	private final static Logger LOGGER=Logger.getLogger(CalculatorDataBaseGetParameter.class);
	
	/** */
	private static final long serialVersionUID = 1L;
	/** имя в базе данных */
	private String field_database_parameter_name;
	/** имя в хранилище Action.Store*/
	private String field_store_parameter_name;

	/** класс, который <b>получает</b> из базы данных значение по его имени<br> 
	 ( по его имени в базе данных для данной сессии)<br>
	 * и помещает в хранилище Action.Store, по заданному имени для хранилища<br>
	 * <i> берет из базы данных Integer, String, byte[] и помещает его в Action.Store как Integer, String, byte[] </i>
	 * @param  database_parameter_name - имя параметра-источника в базе данных
	 * @param store_parameter_name - имя параметра-приемника в хранилище
	 */
	public CalculatorDataBaseGetParameter(String database_parameter_name, 
										  String store_parameter_name){
		this.field_database_parameter_name=database_parameter_name;
		this.field_store_parameter_name=store_parameter_name;
	}

	@Override
	public boolean calculate(Action action) {
		boolean return_value=false;
		try{
			String session_id=(String)action.getParameter("SESSION_ID");
			// прочитать значение из базы данных
			Object value_from_base=read_object_from_database(session_id, this.field_database_parameter_name);
			// поместить значение в Action.Store
			action.putParameter(field_store_parameter_name, value_from_base);
			return_value=true;
		}catch(Exception ex){
			LOGGER.error(" Exception:"+ex.getMessage());
		}
		return return_value;
	}
	
	/**
	 * прочитать объект из базы данных и вернуть его 
	 * @param session_id уникальный идентификатор для сессии 
	 * @param parameter_name - имя параметра в базе данных (видимость - SESSION) 
	 * @return прочитанный объект, или null, если объект не найден, либо если произошла ошибка во время обращения к базе данных
	 */
	private Object read_object_from_database(String session_id, String parameter_name){
		Object return_value=null;
		try{
			byte[] byte_object=read_byte_from_database(session_id, parameter_name);
			return_value=readObject_from_array(byte_object);
		}catch(Exception ex){
			LOGGER.debug("read_object_from_database Error:"+ex.getMessage());
		}
		return return_value;
	}
	
	private byte[] read_byte_from_database(String session_id, String parameter_name){
		byte[] return_value=null;
		try{
			// ORACLE
 			LOGGER.debug("get connection ");
			Connection connection=Connector.getConnection("");
			LOGGER.debug("create statement");
			//String query="declare l_res number; begin l_res := PACK_BC_APPLET.get_param_value(?, ?, ?,?); end;";
			String query="{? = call PACK_BC_APPLET.get_param_value(?, ?, ?,?)}";
			OracleCallableStatement statement=(OracleCallableStatement)connection.prepareCall(query);
			LOGGER.debug("set parameters");
			statement.registerOutParameter(1, Types.VARCHAR);
        	statement.setString(2,session_id);
        	statement.setString(3,parameter_name);
        	statement.registerOutParameter(4,Types.BINARY);
        	statement.registerOutParameter(5, Types.VARCHAR);
        	LOGGER.debug("execute procedure ");
        	statement.execute();
        	LOGGER.debug("SESSION_ID:"+session_id);
        	LOGGER.debug("param_name:"+parameter_name);
        	return_value=statement.getBytes(4);
		}catch(Exception ex){
			LOGGER.error("read_byte_from_database Exception:"+ex.getMessage());
		}
		return return_value;
	}

	/** <b>прочитать объект из byte[] и вернуть объект</b>
	 * @param массив из байт, который содержит объект 
	 * @return прочитанный из данной последовательности объект
	 * */
	private Object readObject_from_array(byte[] array_of_byte){
		Object return_value=null;
		try{
			if(array_of_byte==null){
				LOGGER.error("readObject_from_array parameter is null ");
			}else{
				ByteArrayInputStream byte_array_input_stream=new ByteArrayInputStream(array_of_byte);
				ObjectInputStream ois=new ObjectInputStream(byte_array_input_stream);
				return_value=ois.readObject();
				LOGGER.debug("Object read from byte_array is OK");
			}
		}catch(Exception ex){
			LOGGER.error("readObject_from_array Error:"+ex.getMessage());
		}
		return return_value;
	}
	
}












