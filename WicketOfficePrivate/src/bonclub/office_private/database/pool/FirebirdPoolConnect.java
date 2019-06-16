package bonclub.office_private.database.pool;

public class FirebirdPoolConnect extends PoolConnect{
	/**
	 * ������� ��� ���������� � ���� ������  
	 * @param databaseUrl - URL � ���� : "jdbc:firebirdsql://localhost:3050/D:/messenger.GDB?sql_dialect=3"
	 * @param userName - ��� ������������ 
	 * @param password - ������ ������������
	 * @param poolSize - ������ ���� 
	 */
	public FirebirdPoolConnect(String databaseUrl,
							   String userName, 
							   String password, 
							   Integer poolSize) {
		super(databaseUrl, userName, password, poolSize);
	}

	@Override
	protected String geDriverName() {
		return "org.firebirdsql.jdbc.FBDriver";
	}

	@Override
	protected String getDataBasePath() {
		// � ������ ������ �� ������������ - ��� ��� �������� ����� �����������
		return null;
	}

}
