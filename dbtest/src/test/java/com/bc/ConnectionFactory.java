package com.bc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;

public class ConnectionFactory {
	
	private ConnectionFactory(){
	}
	
	private static String JDBC_DRIVER;
	private static String JDBC_URL;
	private static String JDBC_USER;
	private static String JDBC_PASSWORD;
	
	
	static{
		ConnectionPropertiesAware connectionProperties=null;
		try {
			connectionProperties = new ConnectionPropertiesAware();
		} catch (IOException e) {
			System.err.println("can't read properties file");
			System.exit(1);
		}
		JDBC_DRIVER=connectionProperties.getJdbcDriver();
		JDBC_URL=connectionProperties.getUrl();
		JDBC_USER=connectionProperties.getUser();
		JDBC_PASSWORD=connectionProperties.getPassword();
		DbUtils.loadDriver(JDBC_DRIVER);
	}
	
	public static Connection openConnection() throws SQLException{
	    return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
	}
	
}
