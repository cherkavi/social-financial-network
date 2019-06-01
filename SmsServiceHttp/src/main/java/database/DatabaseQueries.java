package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import database.domain.DatabaseMessage;

public class DatabaseQueries {
	
	private final static String GET_FOR_SEND="select * from bc_admin.VC_DS_SMS_NEW_ALL where id_sms_profile={0}";
	private final static String GET_FOR_SEND_WITH_LIMIT="select * from (select * from bc_admin.VC_DS_SMS_NEW_ALL where id_sms_profile={0} order by id_sms_message ) where rownum<{1}";

	/**
	 * @param connection - database connection
	 * @param profileId - id of profile for retrieve records
	 * @param limit - limit of records
	 * @return
	 * @throws DatabaseException
	 */
	public static List<DatabaseMessage> getForSent(Connection connection, int profileId, Long limit) throws DatabaseException{
		try {
			String query=null;
			if(limit!=null && limit>0){
				query=MessageFormat.format(GET_FOR_SEND_WITH_LIMIT, Long.toString(profileId), Long.toString(limit));
			}else{
				query=MessageFormat.format(GET_FOR_SEND, Long.toString(profileId));
			}
			return new QueryRunner().query(connection, query, new BeanListHandler<DatabaseMessage>(DatabaseMessage.class));
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
}
