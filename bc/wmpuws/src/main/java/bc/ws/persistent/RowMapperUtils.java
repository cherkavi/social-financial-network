package bc.ws.persistent;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

class RowMapperUtils {
	
	static Integer getIntegerFromResultSet(ResultSet resultSet, String fieldName) throws SQLException{
		Integer returnValue=resultSet.getInt(fieldName);
		if(resultSet.wasNull()){
			return null;
		}
		return returnValue;
	}

	private final static String BOOLEAN_TRUE="Y";
	private final static String BOOLEAN_FALSE="N";
	
	/**
	 * convert String value ( 'Y', 'N' ) from result set to boolean representation 
	 * @param rs
	 * @param columnName
	 * @return
	 * @throws SQLException 
	 */
	public static Boolean getBooleanFromString(ResultSet rs, String columnName) throws SQLException {
		String value=StringUtils.trimToNull(rs.getString(columnName));
		return fromStringToBoolean(value);
	}
	
	public static Boolean fromStringToBoolean(String value){
		if(value==null){
			return Boolean.FALSE;
		}
		return BOOLEAN_TRUE.equalsIgnoreCase(value);
	}

	public static String toString(boolean changeToShareAccount) {
		return (changeToShareAccount)?BOOLEAN_TRUE:BOOLEAN_FALSE;
	}

	public static Integer fromStringToInteger(String value) {
		if(value==null){
			return null;
		}
		return Integer.parseInt(value);
	}
	
}
