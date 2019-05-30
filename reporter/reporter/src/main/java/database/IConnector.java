package database;
import java.sql.Connection;

/** интерфейс, который позволяет получить соединение из пула */
public interface IConnector {
	/** получить соединение с базой данных*/
	public Connection getConnection();
	
	/** закрыть все соединения с базой данных */
	public void closeAllConnection();
}
