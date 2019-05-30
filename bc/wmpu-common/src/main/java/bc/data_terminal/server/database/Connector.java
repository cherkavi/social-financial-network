package bc.data_terminal.server.database;
import java.sql.*;

import bc.data_terminal.server.database.oracle.HibernateOracleConnect;


/** Singelton for HibernateOracleConnect*/
public class Connector {
	private static HibernateOracleConnect field_hibernate=null;
	
	private static void getHibernateOracleConnect(){
		if(field_hibernate==null){
			field_hibernate=new HibernateOracleConnect("jdbc:oracle:thin:@91.195.53.27:1521:club",
					 "bc_reports",
					 "reports",
					 1);
		}
	}
	
	public static Connection getConnection(){
		if(field_hibernate==null){
			getHibernateOracleConnect();
		}
		return field_hibernate.getConnection();
	}

	public static void closeConnection(Connection connection){
		if(field_hibernate!=null){
			field_hibernate.closeConnection(connection);
		}
	}
	
}
