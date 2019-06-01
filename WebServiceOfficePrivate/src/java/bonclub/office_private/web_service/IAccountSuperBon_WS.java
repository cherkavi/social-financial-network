package bonclub.office_private.web_service;

import bonclub.office_private.web_service.common_objects.GiftInformation;
import bonclub.office_private.web_service.common_objects.GiftOrderResult;
import bonclub.office_private.web_service.common_objects.PurseHistory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import bonclub.office_private.web_service.common_objects.Purse;
import bonclub.office_private.web_service.db_function.JdbcHelper;


public class IAccountSuperBon_WS implements IAccountSuperBon{

public IAccountSuperBon_WS(){  }
	private final static Logger logger=Logger.getLogger(IAccountSuperBon_WS.class);
	
	private DataSource getDataSource() throws NamingException{
		Context initContext = new InitialContext();
		return (DataSource)initContext.lookup("java:/comp/env/jdbc/data_source");
	}


    public List<Purse> getBalance(Integer id_club_card_purse){
    	List<Purse> returnValue=new ArrayList<Purse>();
		Connection connection=null;
		PreparedStatement statement=null;
		try{
			connection=this.getDataSource().getConnection();
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT cd_currency, name_currency, sname_currency, value_club_card_purse \n");
			sql.append("FROM bc_office.vc_club_card_purse_all\n");
			sql.append("WHERE id_club_card_purse = ?\n");
			sql.append("ORDER BY number_club_card_purse\n");
			ResultSet rs=JdbcHelper.getResultSet(connection, sql.toString(), id_club_card_purse);
			while(rs.next()){
				returnValue.add((Purse)JdbcHelper.getPojoFromResultSet(rs, Purse.class));
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


	@Override
	public List<GiftInformation> getGiftInformation(Integer idClubCardPurse) {
    	List<GiftInformation> returnValue=new ArrayList<GiftInformation>();
		Connection connection=null;
		ResultSet rs=null;
		try{
			connection=this.getDataSource().getConnection();
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT id_nat_prs_gift, cd_nat_prs_gift_state, name_nat_prs_gift_state,\n");
			sql.append("is_club_event_gift, id_club_event_gift, id_club_event,\n");
			sql.append("name_club_event, id_gift, cd_gift, name_gift,\n");
			sql.append("cd_currency, sname_currency, cost_gift, cost_gift_frmt,\n");
			sql.append("date_reserve, date_reserve_frmt, id_nat_prs_gift_request,\n");
			sql.append("basis_for_gift, date_given, date_given_frmt, id_lg_gift,\n");
			sql.append("desc_lg_gift, cd_lg_currency, sname_lg_currency,\n");
			sql.append("cost_lg_gift, cost_lg_gift_frmt, id_gifts_given_place,\n");
			sql.append("write_off_goods_action, card_serial_number, card_id_issuer,\n");
			sql.append("card_id_payment_system, id_club_card_purse, date_returned,\n");
			sql.append("date_returned_frmt, reason_return, date_canceled,\n");
			sql.append("date_canceled_frmt, reason_cancel, write_off_amount,\n");
			sql.append("write_off_amount_frmt, id_club\n");
			sql.append("FROM bc_office.vc_nat_prs_gifts_all\n");
			sql.append("WHERE id_club_card_purse = ? \n");
			sql.append("ORDER BY date_reserve\n");
			rs=JdbcHelper.getResultSet(connection, sql.toString(), idClubCardPurse);
			while(rs.next()){
				returnValue.add((GiftInformation)JdbcHelper.getPojoFromResultSet(rs, GiftInformation.class));
			}
		}catch(Exception ex){
			logger.error("getGiftInformation Exception: "+ex.getMessage());
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


	@Override
	public GiftOrderResult sendOrderOfGift(Integer pIdNatPrs,String pIdClubCardPurse, String pIdClubEventGift) {
		Connection connection=null;
		CallableStatement statement=null;
		try{
			connection=this.getDataSource().getConnection();
			statement = connection.prepareCall("{?= call bc_office.pack_ui_office.add_office_gift_request(?, ?, ?, ?, ?)}");
			statement.registerOutParameter(1, Types.VARCHAR);
			statement.setInt(2, pIdNatPrs);
			statement.setString(3, pIdClubCardPurse);
			statement.setString(4, pIdClubEventGift);
			statement.registerOutParameter(5, Types.INTEGER);
			statement.registerOutParameter(6, Types.INTEGER);
			statement.executeUpdate();
			GiftOrderResult returnValue=new GiftOrderResult();
			returnValue.setP_id_nat_prs_gift_request(statement.getBigDecimal(5));
			returnValue.setP_result_msg(statement.getString(6));
			return returnValue;
		}catch(Exception ex){
			logger.error("sendOrderOfGift Exception:"+ex.getMessage(), ex);
			return null;
		}
	}

}