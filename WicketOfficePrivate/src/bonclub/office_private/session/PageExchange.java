package bonclub.office_private.session;
import java.io.Serializable;
/** объект, который содержит ответ от предыдущей страницы для текущей страницы.
 * Для связи между страницами в сессии 
 * */
public class PageExchange implements Serializable{
	private final static long serialVersionUID=1L;
	/** имя страницы */
	private String pageName;
	/** значение, которое возвращает страница */
	private String pageValue;
	
	/** объект для связи текущей страницы сессии с предыдущей страницей сессии */
	public PageExchange(){
		this.clearPageValue();
	}

	/** объект для связи текущей страницы сессии с предыдущей страницей сессии 
	 * @param pageName - имя страницы 
	 * @param pageValue - значение параметра страницы 
	 * */
	public PageExchange(String pageName, String pageValue){
		this.pageName=pageName;
		this.pageValue=pageValue;
	}
	
	/** получить имя страницы (или панели ) */
	public String getPageName() {
		return pageName;
	}

	/** установить имя страницы(или панели)*/
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	/** получить значение страницы (или панели)*/
	public String getPageValue() {
		return pageValue;
	}

	/** установить значение страницы(или панели)*/
	public void setPageValue(String pageValue) {
		this.pageValue = pageValue;
	}
	
	/** очистить объект информационного обмена между страницами */
	public void clearPageValue(){
		this.pageName="";
		this.pageValue="";
	}
}
