package bonclub.reports.web_service.interf;

import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

/** сервис XFire, который предоставляет удаленные интерфейсы  */
public abstract class ServiceFactory<T> {
	/** получить тип данных, который должен возвращаться в качестве удаленного метода */
	public abstract Class<T> getType();
	private XFireProxyFactory factory = new XFireProxyFactory(XFireFactory.newInstance().getXFire());
	private Service serviceModel= null;

	/**   получить интерфейс к удаленному сервису 
	 * @param pathToService полный путь к сервису (http://localhost:8080/OfficePrivatePartnerReporter/services)
	 * @throws - выбрасывает исключение если не удалось получить ссылку на удаленный интерфейс 
	 * */
	@SuppressWarnings("unchecked")
	public T getRemoteService(String pathToService) throws Exception {
		if(serviceModel==null){
			serviceModel= new ObjectServiceFactory().create(this.getType());
		}
	    Object object=factory.create(serviceModel, pathToService);
	    return (T)object; 
	}
}
