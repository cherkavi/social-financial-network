package BonCard.Reports.JavaScript;

import java.sql.*;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import BonCard.DataBase.UtilityConnector;
import bc.connection.Connector;
import bc.objects.bcObject;

/**
 * Класс, который является отображением функций JavaScript клиента на сервере
 * обращение к данному классу дает фоновую загрузку данных
 */
public class ReporterUtility {
	private final static Logger LOGGER=Logger.getLogger(ReporterUtility.class);
	private bcObject object = new bcObject();
	
	/** имя функции, которая получает запрос на заполнение данных в элементе */
	private static String field_function_fill = "FILL_ELEMENT";
	/** класс для отображения функций JavaScript клиента на сервере */

	private String generalDBScheme = "";

	/** получить запрос от клиента и отреагировать на него */
	public Fragment sendToServer(Fragment fragment) {
		LOGGER.debug("sendToServer:begin open connection");

		object.setSessionId(fragment.getSessioniId());
		generalDBScheme = object.getGeneralDBScheme();

		LOGGER.debug("sendToServer:begin");
		Fragment return_value = new Fragment();
		
		// look at function name
		if (fragment.getName().equals(field_function_fill)) {
			LOGGER.debug("find function FILL_ELEMENT");
			/**
			 * описание параметров: Values - значения необходимых элементов
			 * (Двухмерный массив): <имя параметра> <значение параметра>
			 * Parameters [0] id_destination - идентификатор HTML, который будет
			 * принимать данные с сервера [1] report_number - необходимые данные
			 * для получения параметров на сервере (получения самого запроса)
			 * [2] order_number - необходимые данные для получения параметров на
			 * сервере (получения самого запроса)
			 */
			Connection connection=null;
			try {
				connection=Connector.getConnection(fragment.getSessioniId());
				LOGGER.debug(" Trace data:");
				LOGGER.debug(" Name:" + fragment.getName());
				LOGGER.debug("Parameters");
				for (int counter = 0; counter < fragment.getParameters().length; counter++) {
					LOGGER.debug("     :" + counter + ":  =  "
							+ fragment.getParameters()[counter]);
				}
				LOGGER.debug("Values:" + fragment.getValues());
				for (int counter = 0; counter < fragment.getValues().length; counter++) {
					LOGGER.debug("values: [" + counter + "]"
							+ fragment.getValues()[counter][0] + ","
							+ fragment.getValues()[counter][1]);
				}
				return_value.setName(field_function_fill);
				SelectTagWrap select_wrap = getSelectTagWrapFromFragment(connection, fragment);
				// HTML параметры для клиента:
				// Parameters[]:[0] id-элемента
				// Parameters[]:[1] Default_value
				// Values[][]:[0] - SELECT.OPTION.values
				// Values[][]:[1] - SELECT.OPTION.text
				LOGGER.debug("DEFAULT VALUE:"
						+ select_wrap.getDefaultValue());
				return_value.setParameters(new String[] {
						fragment.getParameters()[0],
						select_wrap.getDefaultValue() }
				// this.getHtmlFillElement(fragment)
						);
				return_value.setValues(new String[][] {
						select_wrap.getAllValues(), select_wrap.getAllText() });
				LOGGER.debug("return_value Name:"
						+ return_value.getName());
			} catch (Exception ex) {
				LOGGER.debug("parameter's Error:" + ex.getMessage());
			} finally{
				UtilityConnector.closeQuietly(connection);
			}
			
		}
		LOGGER.debug("sendToServer:end");
		return return_value;
	}

	/**
	 * Получить обертку для SELECT из необходимых данных, полученных от клиента
	 */
	private SelectTagWrap getSelectTagWrapFromFragment(Connection connection, Fragment fragment) {
		SelectTagWrap return_value = null;
		LOGGER.debug("getHtmlFillElement:begin");
		// получение полной информации из базы для данной переменной
		ReportUtilityVariableParameter variable = this
				.getVariableByReportAndOrder(connection,
						fragment.getParameters()[1],
						fragment.getParameters()[2]);
		// замена в данном запросе всех необходимых значений
		return_value = this.generateSelectWrap(connection, variable,
				fragment.getValues());
		LOGGER.debug("getHtmlFillElement:end" + return_value);
		return return_value;
	}

	/**
	 * получить объект-обертку для тэга SELECT
	 */
	private SelectTagWrap generateSelectWrap(Connection connection,
			ReportUtilityVariableParameter variable, String[][] values) {
		SelectTagWrap return_value = new SelectTagWrap();
		try {
			LOGGER.debug("variable.getSQLText():" + variable.getSQLText());
			String SQL_query = variable.getSQLText();
			for (int counter = 0; counter < values.length; counter++) {
				SQL_query = SQL_query.replace(":" + values[counter][0] + ":",
						values[counter][1].replaceAll("'", "''"));
			}

			LOGGER.debug("попытка получения данных из словаря "
					+ SQL_query);
			ResultSet resultset = connection.createStatement().executeQuery(
					SQL_query);
			String current_value = null;
			if (variable.isRequired() == false) {
				return_value.addOption("null", "");
				// html_text.append("	<option value=\"null\"> </option>");
			}
			if ((variable.getDefaultValue() == null)
					|| (variable.getDefaultValue().trim().equals(""))) {
				// нет значения по умолчанию
				// наполнить данными
				while (resultset.next()) {
					// html_text.append("	<option value=\""+resultset.getString(1)+"\" selected> "+resultset.getString(2)+"</option>");
					return_value.addOption(resultset.getString(1),
							resultset.getString(2));
				}
				// установить первый из списка, если он есть
				if (return_value.getOptionCount() > 0) {
					return_value.setDefaultValue(return_value
							.getValueByNumber(0));
				}
			} else {
				// нужно установить значение по умолчанию
				while (resultset.next()) {
					current_value = resultset.getString(1).trim();
					if (current_value.equalsIgnoreCase(variable
							.getDefaultValue())) {
						// установить значение по умолчанию
						// html_text.append("	<option value=\""+resultset.getString(1)+"\" selected> "+resultset.getString(2)+"</option>");
						return_value.addOption(resultset.getString(1),
								resultset.getString(2));
						return_value
								.setDefaultValue(return_value
										.getValueByNumber(return_value
												.getOptionCount() - 1));
					} else {
						// html_text.append("	<option value=\""+resultset.getString(1)+"\">"+resultset.getString(2)+"</option>");
						return_value.addOption(resultset.getString(1),
								resultset.getString(2));
					}
				}
			}
			resultset.getStatement().close();
			LOGGER.debug("законечно получение данных по запросу");
		} catch (SQLException ex) {
			LOGGER.debug("generateHTMLSelect SQLException:"
					+ ex.getMessage());
		} catch (Exception e) {
			LOGGER.error("generateHTMLSelect Exception:" + e.getMessage());
		}

		return return_value;
	}

	/**
	 * получение "обертки" для переменной из базы данных
	 * 
	 * @param id_report
	 *            номер отчета
	 * @param id_order
	 *            порядок отчета
	 * @return текст запроса с параметром или null в случае ошибки
	 */
	private ReportUtilityVariableParameter getVariableByReportAndOrder(
			Connection connection, String id_report, String id_order) {

		ReportUtilityVariableParameter return_value = new ReportUtilityVariableParameter();
		try {
			ResultSet resultset = connection.createStatement().executeQuery(
					"select * from " + generalDBScheme
							+ ".VC_USER_REPORTS_PARAM_ALL where ID_REPORT="
							+ id_report + " and ORDER_NUMBER=" + id_order);
			if (resultset.next()) {
				return_value.setFromResultSet(resultset);
			} else {
				LOGGER.debug("не найдена переменная по заданным параметрам:  Report:"
								+ id_report + "  Order:" + id_order);
			}
			resultset.getStatement().close();
		} catch (SQLException ex) {
			LOGGER.error("SQLException при получении текста для запроса с параметром"
							+ ex.getMessage());
		} catch (Exception ex) {
			LOGGER.error("Exception при получении текста для запроса с параметром"
							+ ex.getMessage());
		}
		return return_value;
	}

}

/**
 * Класс, который является оберткой для значений, которые передаются HTML
 * клиенту в фоновом запросе, в разобранном состоянии
 * 
 * @author cherkashinv
 */
class SelectTagWrap {
	/** блок значений для value */
	private ArrayList<String> field_value;
	/** блок значений для текста */
	private ArrayList<String> field_text;
	private String field_default_value = null;

	public SelectTagWrap() {
		this.field_value = new ArrayList<String>();
		this.field_text = new ArrayList<String>();
	}

	/**
	 * добавить данные для TAG SELECT (<option value="">text</option>)
	 */
	public void addOption(String value, String text) {
		this.field_value.add(value);
		this.field_text.add(text);
	}

	/**
	 * добавить данные в начало всего списка
	 */
	public void addOptionToFirst(String value, String text) {
		this.field_value.add(0, value);
		this.field_text.add(0, text);
	}

	/**
	 * установить значение по умолчанию
	 */
	public void setDefaultValue(String value) {
		this.field_default_value = value;
	}

	/**
	 * получить значение по умолчанию
	 */
	public String getDefaultValue() {
		return this.field_default_value;
	}

	/**
	 * получить массив всех SELECT.OPTION.VALUE для тэга SELECT
	 */
	public String[] getAllValues() {
		return this.field_value.toArray(new String[] {});
	}

	/**
	 * получить массив из текста SELECT.OPTION.TEXT
	 */
	public String[] getAllText() {
		return this.field_text.toArray(new String[] {});
	}

	/**
	 * получить кол-во внесенных элементов
	 */
	public int getOptionCount() {
		return this.field_value.size();
	}

	/**
	 * получить по номеру значение из списка OPTION
	 */
	public String getValueByNumber(int number) {
		if (number < this.getOptionCount()) {
			return this.field_value.get(number);
		} else {
			return "";
		}
	}
}
