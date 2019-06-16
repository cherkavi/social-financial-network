package bonclub.office_private.database.pool;

public class OraclePoolConnect extends PoolConnect{
	/**
	 * ������� ��� ���������� � ���� ������  
	 * @param databaseUrl - URL � ���� : "jdbc:firebirdsql://localhost:3050/D:/messenger.GDB?sql_dialect=3"
	 * @param userName - ��� ������������ 
	 * @param password - ������ ������������
	 * @param poolSize - ������ ���� 
	 */
	public OraclePoolConnect(String databaseUrl,
							   String userName, 
							   String password, 
							   Integer poolSize) {
		super(databaseUrl, userName, password, poolSize);
	}

	@Override
	protected String geDriverName() {
		return "oracle.jdbc.driver.OracleDriver";
	}

	@Override
	protected String getDataBasePath() {
		return "jdbc:oracle:thin:@91.195.53.27:1521:demo";
	}

}
