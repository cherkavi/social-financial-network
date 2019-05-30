package bc.applet.CardManager.manager.actions.calculator;


import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

import org.apache.log4j.Logger;

import BonCard.DataBase.UtilityConnector;
import bc.applet.CardManager.manager.actions.Action;
import bc.connection.Connector;

public class CalculatorDataBasePutParameter extends ActionStepCalculator{
	private final static Logger LOGGER=Logger.getLogger(CalculatorDataBasePutParameter.class);
	
	/** */
	private static final long serialVersionUID = 1L;
	/** имя в базе данных */
	private String field_database_parameter_name;
	/** имя в хранилище Action.Store*/
	private String field_store_parameter_name;

	/** класс, который <b>записывает</b> в базу данных<br> 
	 * из хранилища (Action.Store) заданный параметр ( по его имени в базе данных для данной сессии)<br>
	 * помещает в базу данных тот тип (Integer, byte[], String), который он взял из Action.Store
	 * @param store_parameter_name  - имя параметра-источника в хранилище
	 * @param database_parameter_name - имя параметра-приемника в базе данных
	 */
	public CalculatorDataBasePutParameter(String store_parameter_name,
										  String database_parameter_name 
										  ){
		this.field_database_parameter_name=database_parameter_name;
		this.field_store_parameter_name=store_parameter_name;
	}

	@Override
	public boolean calculate(Action action) {
		boolean return_value=false;
		try{
			String session_id=(String)action.getParameter("SESSION_ID");
			// записать значение в базу данных
			Object for_write=action.getParameter(field_store_parameter_name);
			return_value=this.write_to_database(session_id, field_database_parameter_name, for_write);
			// записать значение в базу по заданному имени
			return_value=true;
		}catch(Exception ex){
			LOGGER.error(" Exception:"+ex.getMessage());
		}
		return return_value;
	}
	
	/** 
	 * операция по записи в базу данных необходимого параметра, для обмена данными между Action 
	 * @param session_id уникальный идентификатор сессии
	 * @param parameter_name уникальное имя в пределах данной сессии ( на базе данных )
	 * @param parameter сам параметр для записи (Integer, String, byte[]);
	 * @return
	 */
	private boolean write_to_database(String session_id, String parameter_name, Object parameter){
		boolean return_value=false;
		try{
			if(parameter!=null){
				byte[] object_byte=writeObject_to_array(parameter);
				return_value=write_object_to_database(session_id, parameter_name, object_byte);
			}
		}catch(Exception ex){
			LOGGER.error("write_to_database Error:"+ex.getMessage());
		}
		return return_value;
	}
	
	/** <b>записать объект в byte[] и вернуть его</b> 
	 * @param object - объект, который нужно записать
	 * @return возвращает массив из байт, который содержит данный объект
	 * */
	private byte[] writeObject_to_array(Object object){
		byte[] return_value=null;
		try{
			ByteArrayOutputStream byte_array_output_stream=new ByteArrayOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(byte_array_output_stream);
			oos.writeObject(object);
			return_value=byte_array_output_stream.toByteArray();
			LOGGER.debug("writeObject_to_array is OK");
		}catch(Exception ex){
			LOGGER.error("writeObject_to_array Error:"+ex.getMessage());
			return_value=null;
		}
		return return_value;
	}
	
	/**
	 * Записать byte[] в базу данных ( в поле BLOB )
	 * @param session_id - уникальный идентификатор сессии
	 * @param parameter_name - уникальное имя параметра в пределах сессии
	 * @param  value сам параметр для записи в виде byte[]
	 */
	private boolean write_object_to_database(String session_id, String parameter_name,byte[] value){
		boolean return_value=false;
		// параметр, уникальный для базы данных, точнее для созданных процедур
		Connection connection=null;
		try{
			LOGGER.debug("get connection ");
			// ORACLE
			connection=Connector.getConnection(session_id);
			LOGGER.debug("create statement");
			CallableStatement statement = connection.prepareCall("{?= call PACK_BC_APPLET.set_param_value(?, ?, ?, ?)}");
			LOGGER.debug("set parameters");
			statement.registerOutParameter(1, Types.VARCHAR);
            statement.setString(2,session_id);
            statement.setString(3,parameter_name);
            statement.setBytes(4,value);
            statement.registerOutParameter(5, Types.VARCHAR);
			LOGGER.debug("execute procedure ");
			statement.executeUpdate();
            // close connection
			UtilityConnector.closeQuietly(statement);
            return_value=true;
		}catch(Exception ex){
			return_value=false;
			LOGGER.error(" Exception:"+ex.getMessage());
		}finally{
			Connector.closeConnection(connection);
		}
		return return_value;
	}
	
}