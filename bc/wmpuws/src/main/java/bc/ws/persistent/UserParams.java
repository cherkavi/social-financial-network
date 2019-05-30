package bc.ws.persistent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import bc.ws.domain.AccessType;
import bc.ws.domain.TerminalMenuResult;
import bc.ws.exception.PersistentException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import bc.ws.domain.EnvironmentSettings;

import javax.sql.DataSource;

public class UserParams extends ManagedConnection{
	private final static Logger LOGGER=Logger.getLogger(UserParams.class);
	private final UserSettings userSettings;

	public UserParams(Connection connection) {
		super(connection);
		userSettings=new UserSettings(this.dataSource);
	}

	/**
	 * @return Id of terminal by current user
	 */
	public Integer getTerminalId(){
		return template.queryForObject("SELECT id_term FROM VC_USER_CURRENT", Integer.class);
	}
	
	/**
	 * 
	 * @return environment settings for user 
	 */
	public EnvironmentSettings getSettings() throws PersistentException {
		return this.userSettings.executeFunction();
	}
	
	public List<TerminalMenuResult> getOperations(String terminalId){
		LOGGER.debug("SELECT dateformat as dateFormat, uil as userInterfaceLang, report_format as reportFormat, general_db_scheme as dbSchema, rows_on_page as rowsOnPage  FROM v_user_param_ln");
		return template.query("SELECT a.id_menu_element, a.name_menu_element, a.title_menu_element, a.tabname_menu_element, a.id_menu_element_parent, a.order_number, a.relative_path, a.exec_file,  a.id_privilege_type, a.name_privilege_type, a.img_src, a.has_help, a.is_enable, a.is_visible  FROM vws$user_menu_all a", 
				new RowMapper<TerminalMenuResult>(){
					@Override
					public TerminalMenuResult mapRow(ResultSet rs, int rowNum) throws SQLException {
						TerminalMenuResult returnValue=new TerminalMenuResult();
						returnValue.setIdMenu(RowMapperUtils.getIntegerFromResultSet(rs, "id_menu_element"));
						returnValue.setName(rs.getString("name_menu_element"));
						returnValue.setTitle(rs.getString("title_menu_element"));
						returnValue.setTabName(rs.getString("tabname_menu_element"));
						returnValue.setIdParent(RowMapperUtils.getIntegerFromResultSet(rs, "id_menu_element_parent"));
						returnValue.setIdOrder(RowMapperUtils.getIntegerFromResultSet(rs, "order_number"));
						// a., -- ИД уровня доступа (0 - доступ закрыт, 1 - только для чтения, 2 и 9 - выполнение
						returnValue.setAccessType(AccessType.getByInteger(RowMapperUtils.getIntegerFromResultSet(rs, "id_privilege_type")));
						returnValue.setAccessName(rs.getString("name_privilege_type"));
						returnValue.setEnabled(RowMapperUtils.getBooleanFromString(rs, "is_enable"));
						returnValue.setEnabled(RowMapperUtils.getBooleanFromString(rs, "is_visible"));
						return returnValue;
					}
		});
	}
}


class UserSettings extends StringParamsFunction<EnvironmentSettings>{

	UserSettings(DataSource dataSource){
		super(dataSource, "pack$ws_ui.get_current_user_settings",
				new String[]{},
				new String[]{"p_id_term","p_id_user","p_date_format","p_report_format", "p_language","p_terminal_cd_currency","p_calc_point_on_terminal"});
	}

	@Override
	protected EnvironmentSettings convert(Map<String, Object> out) {
        EnvironmentSettings returnValue=new EnvironmentSettings(getString(out, RESULT_PARAM), getString(out, RESULT_MESSAGE));
		returnValue.setTerminalId(getString(out, "p_id_term"));
		returnValue.setUserId(getInteger(out, "p_id_user"));
		returnValue.setDateFormat(getString(out, "p_date_format"));
		returnValue.setReportFormat(getString(out, "p_report_format"));
		returnValue.setUserInterfaceLang(getString(out, "p_language"));
		returnValue.setTerminalCurrencyCode(getString(out, "p_terminal_cd_currency"));
		returnValue.setCalcPointOnTerminal(getBoolean(out, "p_calc_point_on_terminal"));
		return returnValue;
	}

}

