package bonclub.office_private.database;
import java.sql.Connection;

/** ���������, ������� ��������� �������� ���������� �� ���� */
public interface IConnector {
	/** �������� ���������� � ����� ������*/
	public Connection getConnection();
	
	/** ������� ��� ���������� � ����� ������ */
	public void closeAllConnection();
}
