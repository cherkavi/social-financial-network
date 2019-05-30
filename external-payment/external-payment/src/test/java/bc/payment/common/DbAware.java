package bc.payment.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;

public class DbAware {

	protected BasicDataSource dataSource;

	protected JdbcTemplate dataTemplate;
	
	private static String CONNECTION_FILE="common/app-test.properties";
	
	@Before
	public void initDb(){
		JdbcDataConnector connectorData=JdbcDataConnector.readFromPropertyFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONNECTION_FILE));
		this.dataSource=new BasicDataSource();
		this.dataSource.setUrl(connectorData.url);
		this.dataSource.setUsername(connectorData.login);
		this.dataSource.setPassword(connectorData.password);
		this.dataSource.setDriverClassName(connectorData.className);
	
		this.dataTemplate=new JdbcTemplate(this.dataSource);
	}
	
}


class JdbcDataConnector{
	String url;
	String login;
	String password;
	String className;
	
	static JdbcDataConnector readFromPropertyFile(InputStream inputStream){
		Properties properties=new Properties();
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		
		JdbcDataConnector returnValue=new JdbcDataConnector();
		returnValue.url=properties.getProperty("jdbc-url");
		returnValue.login=properties.getProperty("jdbc-login");
		returnValue.password=properties.getProperty("jdbc-password");
		returnValue.className=properties.getProperty("jdbc-driver");
		return returnValue;
	}
	
}
