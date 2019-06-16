package bonclub.office_private.database;
import java.sql.Connection;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/** класс, который создает Hibernate.Session на основании Connection */
public class HibernateConnection {
	private SessionFactory sessionFactory=null;
	private IConnector connectorFactory; 
	
	/** создать на базе Connection Hibernate надстройку 
	 * @param connectorFactory - коннектор, который может генерировать Connection к базе данных
	 * @param jdbcClassName - им€ класса JDB— дл€ доступа к данным 
	 * @param hibernateDialect - "org.hibernate.dialect.FirebirdDialect"
	 * ( example for HSQLDB - org.hsqldb.jdbcDriver)
	 * @throws выбрасывает исключение в случае, когда не удалось создать Hibernate 
	 * */
	public HibernateConnection(IConnector connectorFactory, String jdbcClassName, String hibernateDialect,Class<?> ... classes) throws Exception{
		this.connectorFactory=connectorFactory;
		sessionFactory=getAnnotationConfiguration(jdbcClassName, hibernateDialect,classes).buildSessionFactory();		
	}

	
	private AnnotationConfiguration getAnnotationConfiguration(String jdbcClassName, String hibernateDialect,Class<?> ... classes){
		AnnotationConfiguration aconf = new AnnotationConfiguration();
		Properties properties=new Properties();
		properties.put("hibernate.dialect", hibernateDialect);
		properties.put("hibernate.connection.driver_class", jdbcClassName);
		//properties.put("hibernate.connection.url", "jdbc:hsqldb:mem:baseball");
		//properties.put("hibernate.connection.username", "sa");
		//properties.put("hibernate.connection.password", "");
		properties.put("hibernate.connection.pool_size", "10");
		properties.put("hibernate.connection.autocommit", "false");
		//properties.put("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider");
		//properties.put("hibernate.hbm2ddl.auto", "create-drop");
		//properties.put("hibernate.show_sql", "true");

		//properties.put("hibernate.connection.datasource", "java:comp/env/jdbc/test");
		//hibernate.connection.datasource  	 datasource JNDI name
		//hibernate.jndi.url 	URL of the JNDI provider (optional)
		//hibernate.jndi.class 	class of the JNDI InitialContextFactory (optional) 
		
		//properties.put("hibernate.c3p0.min_size","5");
		//properties.put("hibernate.c3p0.max_size","20");
		//properties.put("hibernate.c3p0.timeout","1800");
		//properties.put("hibernate.c3p0.max_statements","50");
		
		aconf.setProperties(properties);
		for(int counter=0;counter<classes.length;counter++){
			aconf.addAnnotatedClass(classes[counter]);
		}
		return aconf;
	}
	/** get Hibernate Session */
	public Session openSession(Connection connection){
		try{
			return sessionFactory.openSession(connection);
		}catch(Exception ex){
			System.err.println("HibernateConnection GetConnection Exception: "+ex.getMessage());
			return null;
		}
	}
	
	/** get Hibernate Session */
/*	public Session openSession(Connection connection){
		try{
			return sessionFactory.openSession(this.connection);
		}catch(Exception ex){
			System.err.println("openSession Exception: "+ex.getMessage());
			return null;
		}
	}
*/	
	/** get Connection */
	public Connection getConnection(){
		Connection connection=this.connectorFactory.getConnection();
		try{
			connection.setAutoCommit(false);
		}catch(Exception ex){};
		return connection;
	}
	
	/** закрыть все соединени€ с базой данных */
	public void close(){
		System.out.println("close");
		this.sessionFactory.close();
		this.connectorFactory.closeAllConnection();
	}
	
	public void finalize(){
		this.close();
	}
}
