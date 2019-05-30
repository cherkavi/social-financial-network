package bc.payments.sberbank.report.task.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.BeforeClass;

public class DataSourceAware {
	
	protected static DataSource dataSource;
	
	@BeforeClass
	public static void init() throws FileNotFoundException, IOException{
		dataSource=getDataSource();
	}
	
	
	private static BasicDataSource getDataSource() throws FileNotFoundException, IOException{
		Properties properties=new Properties();
		URL resource=Thread.currentThread().getContextClassLoader().getResource("db-test.connection");
		properties.load(new FileInputStream(new File(resource.getFile())));
		BasicDataSource dataSource=new BasicDataSource();
		dataSource.setDriverClassName(properties.getProperty("jdbc-class"));
		dataSource.setUrl(properties.getProperty("jdbc-url"));
		dataSource.setUsername(properties.getProperty("jdbc-login"));
		dataSource.setPassword(properties.getProperty("jdbc-password"));
		return dataSource;
	}

}
