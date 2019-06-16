package bonclub.office_private.database.pool;

public class FirebirdPoolConnect extends PoolConnect{
	/**
	 * Создать пул соединений к базе данных  
	 * @param databaseUrl - URL в виде : "jdbc:firebirdsql://localhost:3050/D:/messenger.GDB?sql_dialect=3"
	 * @param userName - имя пользователя 
	 * @param password - пароль пользователя
	 * @param poolSize - размер пула 
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
		// в данной сборке не используется - так как задается через конструктор
		return null;
	}

}
