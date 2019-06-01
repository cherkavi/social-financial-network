package bonclub.office_private.web_service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import bonclub.office_private.web_service.common_objects.ParentMessage;
import bonclub.office_private.web_service.db_function.DbException;
import bonclub.office_private.web_service.result.WsReturnValue;


public class IMessageFromParent_WS extends AbstractWebServiceFunction implements IMessageFromParent{

public IMessageFromParent_WS(){  }
	private Logger logger=Logger.getLogger(IMessageFromParent_WS.class);

	public WsReturnValue<Boolean> setMessageAsReaded(Integer messageId){
		Connection connection=null;
		try{
			connection=getConnection();
			getDbFunction().setMessageForOfficePrivateAsSended(connection, messageId);
			return new WsReturnValue<Boolean>(Boolean.TRUE);
		}catch(DbException ex){
			return new WsReturnValue<Boolean>(ex);
		}catch(SQLException ex){
			logger.error("IMessageFromParent_WS#setMessageAsReaded Exception:"+ex.getMessage());
			return new WsReturnValue<Boolean>(ex);
		}catch(NamingException ex){
			logger.error("IMessageFromParent_WS#setMessageAsReaded Exception:"+ex.getMessage());
			return new WsReturnValue<Boolean>(ex);
		}
		finally{
			safeCloseConnection(connection);
		}
    }

    
    public WsReturnValue<ParentMessage[]> getNewMessages(Integer[] userId){
		Connection connection=null;
		try{
			connection=this.getConnection();
			return new WsReturnValue<ParentMessage[]>(this.getDbFunction().getNewMessages(connection, userId));
		}catch(SQLException ex){
			logger.error("getNewMessages SQLException:"+ex.getMessage());
			return new WsReturnValue<ParentMessage[]>(ex);
		}catch(NamingException ex){
			logger.error("getNewMessages SQLException:"+ex.getMessage());
			return new WsReturnValue<ParentMessage[]>(ex);
		}
		finally{
			safeCloseConnection(connection);
		}
    }

    public WsReturnValue<Integer> sendMessageToAdmin(int idNatPrs, String titleMessage, String textMessage){
		Connection connection=null;
		try{
			connection=this.getConnection();
			return new WsReturnValue<Integer>(getDbFunction().sendLetterToAdmin(connection, idNatPrs, decode(titleMessage), decode(textMessage)));
		}catch(SQLException ex){
			logger.error("sendMessageToAdmin :"+ex.getMessage());
			return new WsReturnValue<Integer>(ex);
		}catch(NamingException ex){
			logger.error("sendMessageToAdmin :"+ex.getMessage());
			return new WsReturnValue<Integer>(ex);
		}catch(DbException ex){
			return new WsReturnValue<Integer>(ex);
		}
		finally{
			this.safeCloseConnection(connection);
		}
    }

}