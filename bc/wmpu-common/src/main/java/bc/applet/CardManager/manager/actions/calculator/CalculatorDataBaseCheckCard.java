package bc.applet.CardManager.manager.actions.calculator;

import java.sql.*;

import org.apache.log4j.Logger;

import bc.applet.CardManager.manager.actions.Action;
import bc.connection.Connector;

public class CalculatorDataBaseCheckCard extends ActionStepCalculator{
	private final static Logger LOGGER=Logger.getLogger(CalculatorDataBaseCheckCard.class);
	/** */
	private static final long serialVersionUID = 1L;
	private String field_name_source;
	private String field_name_destination;

	/** класс, который проверяет наличие номера SmartCard в базе данных, <br> 
	 * номер берет из Параметра-источника, <br>
	 * результат кладет в параметр-приемник <br>
	 */
	public CalculatorDataBaseCheckCard(String parameter_name_source, String parameter_name_destination){
		this.field_name_source=parameter_name_source;
		this.field_name_destination=parameter_name_destination;
	}

	@Override
	public boolean calculate(Action action) {
		boolean return_value=false;
		try{
			String source_value=action.getParameterString(field_name_source);
			LOGGER.debug("create connector (param="+source_value+")");
			LOGGER.debug("get connection ");
			Connection connection=Connector.getConnection(action.getParameterString("sessionId"));
            CallableStatement statement = connection.prepareCall("{? = call PACK_BC_ARM.get_cards_count(?)}");
            statement.setString(2,source_value);
            statement.registerOutParameter(1, Types.VARCHAR);
			LOGGER.debug("execute procedure ");
            statement.execute();
            String result=statement.getString(1);
			LOGGER.debug("result="+result);
            action.setParameter(field_name_destination, result);
			return_value=true;
		}catch(Exception ex){
			LOGGER.error(" Exception:"+ex.getMessage(), ex);
		}
		return return_value;
	}
}
