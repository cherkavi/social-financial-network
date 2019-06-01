package bonclub.office_private.web_service;

import bonclub.office_private.web_service.common_objects.PurseHistory;
import bonclub.office_private.web_service.common_objects.BalanceResult;
import bonclub.office_private.web_service.db_function.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


public class IAccountIbon_WS implements IAccountIbon{
	private final static Logger logger=Logger.getLogger(IAccountIbon_WS.class);

	public IAccountIbon_WS(){  }
	
	private DataSource getDataSource() throws NamingException{
		Context initContext = new InitialContext();
		return (DataSource)initContext.lookup("java:/comp/env/jdbc/data_source");
	}


    public BalanceResult getBalance(Integer id_club_card_purse){
    	BalanceResult returnValue=null;
		Connection connection=null;
		PreparedStatement statement=null;
		try{
			connection=this.getDataSource().getConnection();
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT cd_currency, name_currency, sname_currency, value_club_card_purse\n");
			sql.append("FROM bc_office.vc_club_card_purse_all\n");
			sql.append("WHERE id_club_card_purse = ?\n");
			sql.append("ORDER BY number_club_card_purse\n");
			ResultSet rs=JdbcHelper.getResultSet(connection, sql.toString(), id_club_card_purse);
			if(rs.next()){
				returnValue= (BalanceResult)JdbcHelper.getPojoFromResultSet(rs, BalanceResult.class);
			}else{
				returnValue=new BalanceResult();
			}
		}catch(Exception ex){
			logger.error("getBalance Exception: "+ex.getMessage());
		}finally{
			if(statement!=null){
				try{
					statement.close();
				}catch(Exception ex){};
			}
			try{
				connection.close();
			}catch(Exception ex){};
		}
		return returnValue;
    }

    public List<PurseHistory> getPurseHistory(Integer id_club_card_purse){
    	List<PurseHistory> returnValue=new ArrayList<PurseHistory>();
		Connection connection=null;
		ResultSet rs=null;
		try{
			connection=this.getDataSource().getConnection();
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT id_oper, source_oper, type_oper, type_oper_tsl,\n");
			sql.append("basis_for_oper, state_oper, state_oper_tsl,\n");
			sql.append("cd_currency, name_currency,\n");
			sql.append("sname_currency, oper_date, oper_amount\n");
			sql.append("FROM bc_office.vc_club_card_purse_oper_all a\n");
			sql.append("WHERE id_club_card_purse = ?\n");
			sql.append("ORDER BY oper_date\n");
			rs=JdbcHelper.getResultSet(connection, sql.toString(), id_club_card_purse);
			while(rs.next()){
				returnValue.add((PurseHistory)JdbcHelper.getPojoFromResultSet(rs, PurseHistory.class));
			}
		}catch(Exception ex){
			logger.error("getPurseHistory Exception: "+ex.getMessage());
		}finally{
			if(rs!=null){
				try{
					rs.getStatement().close();
				}catch(Exception ex){};
			}
			try{
				connection.close();
			}catch(Exception ex){};
		}
		return returnValue;
   }

}