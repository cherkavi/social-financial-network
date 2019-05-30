package bc.ws.persistent;

import java.sql.Connection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

class ManagedConnection {
	protected final SingleConnectionDataSource dataSource;
	protected final JdbcTemplate template;
	
	protected ManagedConnection(Connection connection){
		this.dataSource=new SingleConnectionDataSource(connection, false);
		this.template=new JdbcTemplate(this.dataSource);
	}
/*
	@Override
	protected void finalize() throws Throwable {
		try{
			this.dataSource.destroy();
		}catch(Exception ex){
		}
		super.finalize();
	}
*/
	
}
