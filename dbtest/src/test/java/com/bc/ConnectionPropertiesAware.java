package com.bc;

import java.io.IOException;
import java.util.Properties;

class ConnectionPropertiesAware {
	private String user;
	private String password;
	private String url;
	private String jdbcDriver;

	private static String PATH_TO_PROPERTIES="settings/jdbc-connection.properties";
	
	ConnectionPropertiesAware() throws IOException{
		Properties properties=new Properties();
		properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PATH_TO_PROPERTIES));
		jdbcDriver=properties.getProperty("jdbc.driverClassName");
		url=properties.getProperty("jdbc.url");
		user=properties.getProperty("jdbc.username");
		password=properties.getProperty("jdbc.password");
	}

	String getUser() {
		return user;
	}

	String getPassword() {
		return password;
	}

	String getUrl() {
		return url;
	}

	String getJdbcDriver() {
		return jdbcDriver;
	}

}
