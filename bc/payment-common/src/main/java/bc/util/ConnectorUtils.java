package bc.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectorUtils {
	public final static String PROCEDURE_RESULT_OK="0";
	
	private ConnectorUtils(){}

	public static void closeQuietly(ResultSet cs) {
		if(cs==null){
			return;
		}
		
		try {
			cs.close();
		} catch (SQLException w) {
		}
	}

	public static void closeQuietly(PreparedStatement cs) {
		if(cs==null){
			return;
		}
		
		try {
			cs.close();
		} catch (SQLException w) {
		}
	}
	
	public static void closeQuietly(CallableStatement cs) {
		if(cs==null){
			return;
		}
		
		try {
			cs.close();
		} catch (SQLException w) {
		}
	};
	
	public static void closeQuietly(Connection connection) {
		if(connection==null){
			return;
		}
		
		try {
			connection.close();
		} catch (SQLException w) {
		}
	};
	
	
}
