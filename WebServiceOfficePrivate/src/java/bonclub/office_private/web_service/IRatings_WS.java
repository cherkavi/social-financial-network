package bonclub.office_private.web_service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import org.apache.log4j.Logger;

import bonclub.office_private.web_service.common_objects.RatingsUser;
import bonclub.office_private.web_service.result.WsReturnValue;


public class IRatings_WS extends AbstractWebServiceFunction implements IRatings{

public IRatings_WS(){  }

	private static Logger logger=Logger.getLogger(IRatings_WS.class);

    public WsReturnValue<RatingsUser[]> getUserRatings(){
    	Connection connection=null;
		try{
			return new WsReturnValue<RatingsUser[]>(this.getDbFunction().getUserRatings(connection=this.getConnection()));
		}catch(SQLException ex){
			logger.error("#getUserRatings Exception:"+ex.getMessage());
			return new WsReturnValue<RatingsUser[]>(ex);
		}catch(NamingException ex){
			logger.error("#getUserRatings Exception:"+ex.getMessage());
			return new WsReturnValue<RatingsUser[]>(ex);
		}finally{
			safeCloseConnection(connection);
		}
    }

}