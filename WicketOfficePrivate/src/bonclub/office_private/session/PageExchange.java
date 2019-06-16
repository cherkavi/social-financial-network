package bonclub.office_private.session;
import java.io.Serializable;
/** ������, ������� �������� ����� �� ���������� �������� ��� ������� ��������.
 * ��� ����� ����� ���������� � ������ 
 * */
public class PageExchange implements Serializable{
	private final static long serialVersionUID=1L;
	/** ��� �������� */
	private String pageName;
	/** ��������, ������� ���������� �������� */
	private String pageValue;
	
	/** ������ ��� ����� ������� �������� ������ � ���������� ��������� ������ */
	public PageExchange(){
		this.clearPageValue();
	}

	/** ������ ��� ����� ������� �������� ������ � ���������� ��������� ������ 
	 * @param pageName - ��� �������� 
	 * @param pageValue - �������� ��������� �������� 
	 * */
	public PageExchange(String pageName, String pageValue){
		this.pageName=pageName;
		this.pageValue=pageValue;
	}
	
	/** �������� ��� �������� (��� ������ ) */
	public String getPageName() {
		return pageName;
	}

	/** ���������� ��� ��������(��� ������)*/
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	/** �������� �������� �������� (��� ������)*/
	public String getPageValue() {
		return pageValue;
	}

	/** ���������� �������� ��������(��� ������)*/
	public void setPageValue(String pageValue) {
		this.pageValue = pageValue;
	}
	
	/** �������� ������ ��������������� ������ ����� ���������� */
	public void clearPageValue(){
		this.pageName="";
		this.pageValue="";
	}
}
