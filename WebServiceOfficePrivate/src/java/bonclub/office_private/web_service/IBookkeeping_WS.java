package bonclub.office_private.web_service;

import bonclub.office_private.web_service.common_objects.BookkeepingTransactionItem;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


public class IBookkeeping_WS implements IBookkeeping{

public IBookkeeping_WS(){  }
	private static Logger logger=Logger.getLogger(IBookkeeping_WS.class);
	
	private DataSource getDataSource() throws NamingException{
		Context initContext = new InitialContext();
		return (DataSource)initContext.lookup("java:/comp/env/jdbc/data_source");
	}

    public int getTransactionSize(String cardSerialNumber, Integer idEmitentCard, Integer idPaySystemCard, Date dateBegin, Date dateEnd){
        cardSerialNumber=URLDecoder.decode(cardSerialNumber);
        
		int returnValue=0;
		Connection connection=null;
		try{
			connection=getDataSource().getConnection();
			StringBuffer query=new StringBuffer();
			query.append("select ROWNUM rn, count(*) over () as row_count \n");
			// query.append("SELECT count (*) \n ");
			query.append("FROM bc_admin.vc_trans_joffice_all \n "); 
			query.append("WHERE card_serial_number = ? \n "); 
			query.append("AND id_issuer = ?  \n ");
			query.append("AND id_payment_system = ?  \n ");
			query.append("AND trunc(sys_date) between ? and ? \n ");
			PreparedStatement ps=connection.prepareStatement(query.toString());
			ps.setString(1, cardSerialNumber);
			ps.setInt(2, idEmitentCard);
			ps.setInt(3, idPaySystemCard);
			if(dateBegin!=null){
				ps.setDate(4, new java.sql.Date(dateBegin.getTime()));
			}else{
				ps.setNull(4, java.sql.Types.DATE);
			}
			if(dateEnd!=null){
				ps.setDate(5, new java.sql.Date(dateEnd.getTime()+1000*59*59*23));
			}else{
				ps.setNull(5, java.sql.Types.DATE);
			}
			
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				returnValue=rs.getInt(2);
			}
		}catch(Exception ex){
			logger.error("#getTransactionSize Exception: "+ex.getMessage());
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
		return returnValue;
    }

    public BookkeepingTransactionItem[] getTransactions(String cardSerialNumber, Integer idEmitentCard, Integer idPaySystemCard, Date dateBegin, Date dateEnd, int begin, int count, String orderValue){
        cardSerialNumber=URLDecoder.decode(cardSerialNumber);
        orderValue=URLDecoder.decode(orderValue);
        
		BookkeepingTransactionItem[] returnValue=null;
		Connection connection=null;
		
		try{
			connection=this.getDataSource().getConnection();
			StringBuffer query=new StringBuffer();
			query.append("SELECT ");
			query.append(this.getFields());
			query.append("	from ( select ROWNUM rn, a.* FROM (select bc_admin.vc_trans_joffice_all.* FROM bc_admin.vc_trans_joffice_all \n"); 
			query.append("		WHERE card_serial_number = ? \n"); 
			query.append("		AND id_issuer = ? \n");
			query.append("		AND id_payment_system = ? \n");
			query.append("		 and trunc(sys_date) between ? and ? \n");
			query.append("		ORDER BY sys_date, id_trans");
			query.append("	) a	)\n");
			if((begin>=0)&&(count>0)){
				query.append("		where rn>=? and rn<(?) \n");
			}
			
			PreparedStatement ps=connection.prepareStatement(query.toString());
			// System.out.println(query.toString());
			ps.setString(1, cardSerialNumber);
			ps.setInt(2, idEmitentCard);
			ps.setInt(3, idPaySystemCard);
			if(dateBegin!=null){
				ps.setDate(4, new java.sql.Date(dateBegin.getTime()));
			}else{
				ps.setNull(4, java.sql.Types.DATE);
			}
			if(dateEnd!=null){
				ps.setDate(5, new java.sql.Date(dateEnd.getTime()));
			}else{
				ps.setNull(5, java.sql.Types.DATE);
			}
			if((begin>=0)&&(count>0)){
				ps.setInt(6, begin+1);
				ps.setInt(7, begin+count+1);
			}
			
			ResultSet rs=ps.executeQuery();
			List<BookkeepingTransactionItem> list=new ArrayList<BookkeepingTransactionItem>();
			
			while(rs.next()){
				list.add(this.getBookkeepingTransactionItemFromResultSet(rs));
			}
			returnValue=list.toArray(new BookkeepingTransactionItem[]{});
		}catch(Exception ex){
			logger.error("#getTransactionSize Exception: "+ex.getMessage());
		}finally{
			try{
				connection.close();
			}catch(Exception ex){};
		}
		return returnValue;
    }
    
    
	/** преобразование данных из ResultSet в {@link BookkeepingTransactionItem} */
	private BookkeepingTransactionItem getBookkeepingTransactionItemFromResultSet(ResultSet rs) throws SQLException {
		BookkeepingTransactionItem returnValue=new BookkeepingTransactionItem();
		/*
		// получить все set-ры
		GetterSetterAnalisator analisator=new GetterSetterAnalisator();
		String[] setters=analisator.getSetterNames(returnValue);
		for(int counter=0;counter<setters.length;counter++){
			// вызвать set-ер c указанным условием
			analisator.setObject(returnValue, setters[counter], rs.getObject(setters[counter]));
		}*/
		returnValue.setID_TERM_SES(this.getIntegerFromResultSet(rs, "ID_TERM_SES"));
		returnValue.setID_TELGR(this.getIntegerFromResultSet(rs,"ID_TELGR"));
		returnValue.setID_TERM(this.getIntegerFromResultSet(rs,"ID_TERM"));
		returnValue.setID_SERVICE_PLACE(this.getIntegerFromResultSet(rs,"ID_SERVICE_PLACE"));
		
		returnValue.setNAME_SERVICE_PLACE(this.getStringFromResultSet(rs, "NAME_SERVICE_PLACE"));
		returnValue.setID_SAM(this.getIntegerFromResultSet(rs,"ID_SAM"));
		returnValue.setNT_SAM(this.getIntegerFromResultSet(rs,"NT_SAM"));
		returnValue.setID_TRANS(this.getIntegerFromResultSet(rs,"ID_TRANS"));
		returnValue.setTYPE_TRANS_TXT(this.getStringFromResultSet(rs, "TYPE_TRANS_TXT"));
	    returnValue.setVER_TRANS(this.getIntegerFromResultSet(rs,"VER_TRANS"));
	    returnValue.setCARD_SERIAL_NUMBER(this.getStringFromResultSet(rs, "CARD_SERIAL_NUMBER"));
		returnValue.setCD_CURRENCY(this.getIntegerFromResultSet(rs,"CD_CURRENCY"));
		returnValue.setNAME_CURRENCY(this.getStringFromResultSet(rs, "NAME_CURRENCY"));
		returnValue.setNT_ICC(this.getIntegerFromResultSet(rs,"NT_ICC"));
		returnValue.setNT_EXT(this.getIntegerFromResultSet(rs,"NT_EXT"));
		returnValue.setTC_P(this.getIntegerFromResultSet(rs,"TC_P"));
		returnValue.setVK_ENC(this.getIntegerFromResultSet(rs,"VK_ENC"));
		returnValue.setACTION(this.getStringFromResultSet(rs, "ACTION")); 
		returnValue.setCLUB_ST_PRV(this.getIntegerFromResultSet(rs,"CLUB_ST_PRV"));
		returnValue.setERR_TX(this.getIntegerFromResultSet(rs,"ERR_TX"));
		returnValue.setERR_TX_DESC(this.getStringFromResultSet(rs, "ERR_TX_DESC")); 
		returnValue.setMAC_ICC(this.getIntegerFromResultSet(rs,"MAC_ICC"));
		returnValue.setMAC_PDA(this.getIntegerFromResultSet(rs,"MAC_PDA"));
		returnValue.setID_STORNED_TRANS(this.getIntegerFromResultSet(rs,"ID_STORNED_TRANS")); 
		returnValue.setID_DOUBLE_TRANS(this.getIntegerFromResultSet(rs,"ID_DOUBLE_TRANS"));
		returnValue.setID_ISSUER(this.getIntegerFromResultSet(rs,"ID_ISSUER"));
		returnValue.setID_PAYMENT_SYSTEM(this.getIntegerFromResultSet(rs,"ID_PAYMENT_SYSTEM")); 
		returnValue.setID_PURCHASE(this.getIntegerFromResultSet(rs,"ID_PURCHASE"));
		returnValue.setCHANGED_BY_USER(this.getStringFromResultSet(rs, "CHANGED_BY_USER")); 
		returnValue.setUSER_WHO_HAS_CHANGED(this.getStringFromResultSet(rs, "USER_WHO_HAS_CHANGED")); 
		returnValue.setBASES_FOR_CHANGES(this.getStringFromResultSet(rs, "BASES_FOR_CHANGES"));
		returnValue.setENTERED_MANUALLY(this.getStringFromResultSet(rs, "ENTERED_MANUALLY"));
		returnValue.setIMID(this.getStringFromResultSet(rs, "IMID"));
		returnValue.setSPECID(this.getStringFromResultSet(rs, "SPECID"));
		returnValue.setCLUBCARD(this.getStringFromResultSet(rs, "CLUBCARD"));
		returnValue.setCARDID(this.getStringFromResultSet(rs, "CARDID"));
		returnValue.setID_CLUB(this.getStringFromResultSet(rs, "ID_CLUB"));
		returnValue.setLANGUAGE(this.getStringFromResultSet(rs, "LANGUAGE"));
		returnValue.setCREATION_DATE(this.getStringFromResultSet(rs, "CREATION_DATE")); 
		returnValue.setCREATED_BY(this.getStringFromResultSet(rs, "CREATED_BY"));
		returnValue.setLAST_UPDATE_DATE(this.getStringFromResultSet(rs, "LAST_UPDATE_DATE")); 
		returnValue.setLAST_UPDATE_BY(this.getIntegerFromResultSet(rs,"LAST_UPDATE_BY"));
		returnValue.setIS_REJECTED(this.getIntegerFromResultSet(rs,"IS_REJECTED"));
		returnValue.setID_TERMINAL_MANUFACTURER(this.getIntegerFromResultSet(rs,"ID_TERMINAL_MANUFACTURER")); 
		returnValue.setID_DEVICE_TYPE(this.getIntegerFromResultSet(rs,"ID_DEVICE_TYPE"));
		returnValue.setID_DEALER(this.getIntegerFromResultSet(rs,"ID_DEALER"));
		returnValue.setID_TERM_OWNER(this.getIntegerFromResultSet(rs,"ID_TERM_OWNER"));
		returnValue.setID_FINANCE_ACQUIRER(this.getIntegerFromResultSet(rs,"ID_FINANCE_ACQUIRER")); 
		returnValue.setID_TECHNICAL_ACQUIRER(this.getIntegerFromResultSet(rs,"ID_TECHNICAL_ACQUIRER"));
		returnValue.setID_CARD_STATUS(this.getIntegerFromResultSet(rs,"ID_CARD_STATUS"));
		returnValue.setID_BON_CATEGORY(this.getIntegerFromResultSet(rs,"ID_BON_CATEGORY"));
		returnValue.setID_DISC_CATEGORY(this.getIntegerFromResultSet(rs,"ID_DISC_CATEGORY"));
		returnValue.setID_LOYALITY_HISTORY(this.getIntegerFromResultSet(rs,"ID_LOYALITY_HISTORY")); 
		returnValue.setID_LOYALITY_BON_LINE(this.getIntegerFromResultSet(rs,"ID_LOYALITY_BON_LINE"));
		returnValue.setID_LOYALITY_DISC_LINE(this.getIntegerFromResultSet(rs,"ID_LOYALITY_DISC_LINE"));
		returnValue.setSTATE_TRANS_TSL(this.getStringFromResultSet(rs, "STATE_TRANS_TSL"));
		returnValue.setSYS_DATE_FULL_FRMT(this.getStringFromResultSet(rs, "SYS_DATE_FULL_FRMT")); 
		returnValue.setSYS_DATE_FRMT(this.getDateTimeFromResultSet(rs, "SYS_DATE"));
		returnValue.setSYS_TIME_FRMT(this.getStringFromResultSet(rs, "SYS_TIME_FRMT"));
		returnValue.setCLUB_DISC_PRV_FRMT(this.getStringFromResultSet(rs, "CLUB_DISC_PRV_FRMT")); 
		returnValue.setCLUB_BON_PRV_FRMT(this.getStringFromResultSet(rs, "CLUB_BON_PRV_FRMT"));
		returnValue.setDATE_ACC_PRV_FRMT(this.getStringFromResultSet(rs, "DATE_ACC_PRV_FRMT"));
		returnValue.setDATE_MOV_PRV_FRMT(this.getStringFromResultSet(rs, "DATE_MOV_PRV_FRMT"));
		returnValue.setDATE_CALC_PRV_FRMT(this.getStringFromResultSet(rs, "DATE_CALC_PRV_FRMT"));
		returnValue.setBAL_ACC_PRV_FRMT(this.getStringFromResultSet(rs, "BAL_ACC_PRV_FRMT"));
		returnValue.setBAL_CUR_PRV_FRMT(this.getStringFromResultSet(rs, "BAL_CUR_PRV_FRMT"));
		returnValue.setBAL_BON_PER_PRV_FRMT(this.getStringFromResultSet(rs, "BAL_BON_PER_PRV_FRMT"));
		returnValue.setBAL_DISC_PER_PRV_FRMT(this.getStringFromResultSet(rs, "BAL_DISC_PER_PRV_FRMT"));
		returnValue.setOPR_SUM_FRMT(this.getStringFromResultSet(rs, "OPR_SUM_FRMT"));
		returnValue.setSUM_PAY_CASH_FRMT(this.getStringFromResultSet(rs, "SUM_PAY_CASH_FRMT")); 
		returnValue.setSUM_PAY_CARD_FRMT(this.getStringFromResultSet(rs, "SUM_PAY_CARD_FRMT"));
		returnValue.setSUM_PAY_BON_FRMT(this.getStringFromResultSet(rs, "SUM_PAY_BON_FRMT"));
		returnValue.setFL_EXT_LOYL(this.getFloatFromResultSet(rs, "FL_EXT_LOYL"));
		returnValue.setCLUB_SUM_FRMT(this.getStringFromResultSet(rs, "CLUB_SUM_FRMT"));
		returnValue.setSUM_BON_FRMT(this.getStringFromResultSet(rs, "SUM_BON_FRMT"));
		returnValue.setSUM_BON_CASH_FRMT(this.getStringFromResultSet(rs, "SUM_BON_CASH_FRMT")); 
		returnValue.setSUM_BON_CARD_FRMT(this.getStringFromResultSet(rs, "SUM_BON_CARD_FRMT"));
		returnValue.setSUM_BON_BON_FRMT(this.getStringFromResultSet(rs, "SUM_BON_BON_FRMT"));
		returnValue.setSUM_BON_DISC_FRMT(this.getStringFromResultSet(rs, "SUM_BON_DISC_FRMT"));
		returnValue.setSUM_DISC_FRMT(this.getStringFromResultSet(rs, "SUM_DISC_FRMT"));
		returnValue.setCASH_CARD_NR(this.getStringFromResultSet(rs, "CASH_CARD_NR"));
		returnValue.setC_NR(this.getStringFromResultSet(rs, "C_NR"));
		returnValue.setC_CHECK_NR(this.getStringFromResultSet(rs, "C_CHECK_NR")); 
		returnValue.setCLUB_ST(this.getStringFromResultSet(rs, "CLUB_ST"));
		returnValue.setCLUB_DISC_FRMT(this.getStringFromResultSet(rs, "CLUB_DISC_FRMT"));
		returnValue.setCLUB_BON_FRMT(this.getStringFromResultSet(rs, "CLUB_BON_FRMT"));
		returnValue.setDATE_ACC_FRMT(this.getStringFromResultSet(rs, "DATE_ACC_FRMT"));
		returnValue.setDATE_MOV_FRMT(this.getStringFromResultSet(rs, "DATE_MOV_FRMT"));
		returnValue.setBAL_ACC_FRMT(this.getStringFromResultSet(rs, "BAL_ACC_FRMT"));
		returnValue.setBAL_CUR_FRMT(this.getStringFromResultSet(rs, "BAL_CUR_FRMT"));
		returnValue.setBAL_BON_PER_FRMT(this.getStringFromResultSet(rs, "BAL_BON_PER_FRMT"));
		returnValue.setBAL_DISC_PER_FRMT(this.getStringFromResultSet(rs, "BAL_DISC_PER_FRMT"));
		returnValue.setIS_POSTING_TSL(this.getStringFromResultSet(rs, "IS_POSTING_TSL"));
		returnValue.setDATE_LAST_USERS_CHANGES_FRMT(this.getStringFromResultSet(rs, "DATE_LAST_USERS_CHANGES_FRMT"));
		returnValue.setSUM_1_1_3_1_FRMT(this.getStringFromResultSet(rs, "SUM_1_1_3_1_FRMT")); 
		returnValue.setSUM_1_1_3_2_FRMT(this.getStringFromResultSet(rs, "SUM_1_1_3_2_FRMT"));
		returnValue.setSUM_1_1_3_3_FRMT(this.getStringFromResultSet(rs, "SUM_1_1_3_3_FRMT"));
		returnValue.setSUM_1_1_3_4_FRMT(this.getStringFromResultSet(rs, "SUM_1_1_3_4_FRMT"));
		returnValue.setSUM_1_1_8_1_FRMT(this.getStringFromResultSet(rs, "SUM_1_1_8_1_FRMT"));
		returnValue.setSUM_1_1_8_2_FRMT(this.getStringFromResultSet(rs, "SUM_1_1_8_2_FRMT"));
		returnValue.setSUM_1_1_8_3_FRMT(this.getStringFromResultSet(rs, "SUM_1_1_8_3_FRMT"));
		returnValue.setSUM_1_1_8_4_FRMT(this.getStringFromResultSet(rs, "SUM_1_1_8_4_FRMT"));
		return returnValue;
	}

	private int getIntegerFromResultSet(ResultSet rs, String fieldName){
		try{
			return rs.getInt(fieldName);
		}catch(Exception ex){
			return 0;
		}
	}
	
	private float getFloatFromResultSet(ResultSet rs, String fieldName){
		try{
			return rs.getFloat(fieldName);
		}catch(Exception ex){
			return 0;
		}
	}
	
	/** получить строку из набора данных на основании имени поля 
	 * @return вернуть пустую строку, если запись содержит null
	 *  */
	private String getStringFromResultSet(ResultSet rs, String fieldName){
		try{
			return rs.getString(fieldName);
		}catch(Exception ex){
			return "";
		}
	}
	/** получить дату и время из набора данных  
	 * @return (nullable)
	 *  */
	private Date getDateTimeFromResultSet(ResultSet rs, String fieldName){
		try{
			return new Date(rs.getTimestamp(fieldName).getTime());
		}catch(Exception ex){
			return null;
		}
	}
	
	
	/** получить все поля, которые предназначены для этого запроса  */
	private String getFields(){
		StringBuffer returnValue=new StringBuffer();
		returnValue.append("	ID_TERM_SES, ID_TELGR, ID_TERM, ID_SERVICE_PLACE, 	 \n");
		returnValue.append("	NAME_SERVICE_PLACE, ID_SAM, NT_SAM, ID_TRANS, 	 \n");
		returnValue.append("	TYPE_TRANS, TYPE_TRANS_TXT, 	 \n");
		returnValue.append("	VER_TRANS,  CARD_SERIAL_NUMBER, CD_CURRENCY, NAME_CURRENCY, NT_ICC, NT_EXT, 	 \n");
		returnValue.append("	TC_P, VK_ENC, ACTION, CLUB_ST_PRV,	 \n");
		returnValue.append("	ERR_TX, ERR_TX_DESC, MAC_ICC, MAC_PDA,	 \n");
		returnValue.append("	ID_STORNED_TRANS, ID_DOUBLE_TRANS, ID_ISSUER, ID_PAYMENT_SYSTEM, ID_PURCHASE,	 \n");
		returnValue.append("	CHANGED_BY_USER, USER_WHO_HAS_CHANGED, BASES_FOR_CHANGES, 	 \n");
		returnValue.append("	ENTERED_MANUALLY, IMID, SPECID, CLUBCARD, CARDID,	 \n");
		returnValue.append("	ID_CLUB, LANGUAGE, CREATION_DATE, CREATED_BY, LAST_UPDATE_DATE, LAST_UPDATE_BY,	 \n");
		returnValue.append("	IS_REJECTED, ID_TERMINAL_MANUFACTURER, ID_DEVICE_TYPE, ID_DEALER, ID_TERM_OWNER,	 \n");
		returnValue.append("	ID_FINANCE_ACQUIRER, ID_TECHNICAL_ACQUIRER, ID_CARD_STATUS, ID_BON_CATEGORY,	 \n");
		returnValue.append("	ID_DISC_CATEGORY, ID_LOYALITY_HISTORY, ID_LOYALITY_BON_LINE, ID_LOYALITY_DISC_LINE,	 \n");
		returnValue.append("	STATE_TRANS, STATE_TRANS_TSL,	 \n");
		returnValue.append("	SYS_DATE_FULL_FRMT,	 \n");
		returnValue.append("	SYS_DATE,  SYS_DATE_FRMT, 	 \n");
		returnValue.append("	SYS_TIME_FRMT, 	 \n");
		returnValue.append("	CLUB_DISC_PRV, CLUB_DISC_PRV_FRMT, 	 \n");
		returnValue.append("	CLUB_BON_PRV, CLUB_BON_PRV_FRMT,	 \n");
		returnValue.append("	DATE_ACC_PRV, DATE_ACC_PRV_FRMT, 	 \n");
		returnValue.append("	DATE_MOV_PRV, DATE_MOV_PRV_FRMT, 	 \n");
		returnValue.append("	DATE_CALC_PRV, DATE_CALC_PRV_FRMT, 	 \n");
		returnValue.append("	BAL_ACC_PRV, BAL_ACC_PRV_FRMT,	 \n");
		returnValue.append("	BAL_CUR_PRV, BAL_CUR_PRV_FRMT, 	 \n");
		returnValue.append("	BAL_BON_PER_PRV, BAL_BON_PER_PRV_FRMT,	 \n");
		returnValue.append("	BAL_DISC_PER_PRV, BAL_DISC_PER_PRV_FRMT, 	 \n");
		returnValue.append("	OPR_SUM, OPR_SUM_FRMT,	 \n");
		returnValue.append("	SUM_PAY_CASH, SUM_PAY_CASH_FRMT, 	 \n");
		returnValue.append("	SUM_PAY_CARD, SUM_PAY_CARD_FRMT,	 \n");
		returnValue.append("	SUM_PAY_BON, SUM_PAY_BON_FRMT, 	 \n");
		returnValue.append("	FL_EXT_LOYL, 	 \n");
		returnValue.append("	CLUB_SUM, CLUB_SUM_FRMT, 	 \n");
		returnValue.append("	SUM_BON, SUM_BON_FRMT, 	 \n");
		returnValue.append("	SUM_BON_CASH, SUM_BON_CASH_FRMT, 	 \n");
		returnValue.append("	SUM_BON_CARD, SUM_BON_CARD_FRMT,	 \n");
		returnValue.append("	SUM_BON_BON, SUM_BON_BON_FRMT, 	 \n");
		returnValue.append("	SUM_BON_DISC, SUM_BON_DISC_FRMT, 	 \n");
		returnValue.append("	SUM_DISC, SUM_DISC_FRMT, 	 \n");
		returnValue.append("	CASH_CARD_NR, 	 \n");
		returnValue.append("	C_NR, 	 \n");
		returnValue.append("	C_CHECK_NR, 	 \n");
		returnValue.append("	CLUB_ST, 	 \n");
		returnValue.append("	CLUB_DISC, CLUB_DISC_FRMT,	 \n");
		returnValue.append("	CLUB_BON, CLUB_BON_FRMT, 	 \n");
		returnValue.append("	DATE_ACC, DATE_ACC_FRMT, 	 \n");
		returnValue.append("	DATE_MOV, DATE_MOV_FRMT,	 \n");
		returnValue.append("	BAL_ACC, BAL_ACC_FRMT, 	 \n");
		returnValue.append("	BAL_CUR, BAL_CUR_FRMT, 	 \n");
		returnValue.append("	BAL_BON_PER, BAL_BON_PER_FRMT,	 \n");
		returnValue.append("	BAL_DISC_PER, BAL_DISC_PER_FRMT, 	 \n");
		returnValue.append("	IS_POSTING, IS_POSTING_TSL,	 \n");
		returnValue.append("	DATE_LAST_USERS_CHANGES, DATE_LAST_USERS_CHANGES_FRMT,	 \n");
		returnValue.append("	SUM_1_1_3_1, SUM_1_1_3_1_FRMT,    	 \n");
		returnValue.append("	SUM_1_1_3_2, SUM_1_1_3_2_FRMT,	 \n");
		returnValue.append("	SUM_1_1_3_3, SUM_1_1_3_3_FRMT,	 \n");
		returnValue.append("	SUM_1_1_3_4, SUM_1_1_3_4_FRMT,	 \n");
		returnValue.append("	SUM_1_1_8_1, SUM_1_1_8_1_FRMT,    	 \n");
		returnValue.append("	SUM_1_1_8_2, SUM_1_1_8_2_FRMT,	 \n");
		returnValue.append("	SUM_1_1_8_3, SUM_1_1_8_3_FRMT,	 \n");
		returnValue.append("	SUM_1_1_8_4, SUM_1_1_8_4_FRMT	 \n");
		return returnValue.toString();
	}
    

}