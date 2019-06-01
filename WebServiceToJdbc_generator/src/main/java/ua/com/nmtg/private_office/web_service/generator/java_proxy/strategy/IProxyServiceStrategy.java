package ua.com.nmtg.private_office.web_service.generator.java_proxy.strategy;

/**
 * strategy for different type of Service level ( Connection, DataSource, Session, EntityManager ) 
 */
public interface IProxyServiceStrategy {
	/**
	 * @return @nullable
	 */
	public String getAdditionalImport();
	
	/**
	 * must return the empty constructor 
	 * @param className
	 * @return
	 */
	public String getConstructorAndFields(String className, String fieldName);
}
