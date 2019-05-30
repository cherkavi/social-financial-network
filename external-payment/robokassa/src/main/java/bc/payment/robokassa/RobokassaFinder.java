package bc.payment.robokassa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import bc.payment.PaymentExternalFinder;
import bc.payment.PaymentExternalVO;
import bc.payment.PaymentType;
import bc.payment.exception.GeneralPaymentException;
import bc.util.ConnectorUtils;

/**
 * finder for elements from Database
 */
@Service
public class RobokassaFinder implements PaymentExternalFinder{
	private final static Logger LOGGER=Logger.getLogger(RobokassaFinder.class);

	@Value("${robokassa.jdbc.schema}")
	private String  generalDbScheme;
	
	private static PaymentExternalVO readValue(Long userId, ResultSet resultSet) throws SQLException{
    	// ПЕРЕДЕЛАТЬ: в процедуру не передаются параметры sShpItem, sCulture, Email, ExpirationDate
		return new PaymentExternalVO(
				Long.toString(userId), 
				resultSet.getString("nInvId"), 
				resultSet.getString("sDesc"), 
				resultSet.getString("sOutSum"), 
				resultSet.getString("cd_telgr_external_state"));
	}
	

	private String getSqlHeader(String language){
		String sql =
		" SELECT ninvid, sdesc, soutsum, sshpitem, sculture, " +
		"'" + language + "' sCulture, " +
		" email, expirationdate, cd_telgr_external_state, " +
		" count(*) over () as all_row_count " +
		" FROM " + generalDbScheme + "vc_telgr_invoice_all ";		
        return sql;
	}
	
	@Override
	public PaymentExternalVO find(Long userId, Connection connection, Long paymentId, String language, String generalDbScheme) throws GeneralPaymentException  {
		PaymentExternalVO returnValue=null;
        
    	String sql=getSqlHeader(language);
    	sql+=" WHERE nInvId="+Long.toString(paymentId)+" ";

    	ResultSet resultSet=null;
    	try {
    		resultSet = connection.createStatement().executeQuery(sql);
	        if (resultSet.next()){
	        	// ПЕРЕДЕЛАТЬ: в процедуру не передаются параметры sShpItem, sCulture, Email, ExpirationDate
	        	returnValue=readValue(userId, resultSet);
	         }else{
	        	 return null;
	         }
	    }catch (SQLException e) {
	    	LOGGER.error("finder sql isn't working properly ", e);
	    	throw new GeneralPaymentException("check SQL part", e);
	    }catch (Exception e) {
	    	LOGGER.error("some general was happend ", e);
	    	throw new GeneralPaymentException("finder isn't working", e);
	    }finally {
	    	ConnectorUtils.closeQuietly(resultSet);
	    } 
		return fixDescription(returnValue);
	}

	public List<PaymentExternalVO> find(Long userId, Connection connection, Date dateBeginInclude, Date dateEndExclude, PaymentType type, int maxRows, String language, String generalDbScheme) throws GeneralPaymentException {
		List<PaymentExternalVO> returnValue=new ArrayList<PaymentExternalVO>();
		
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Map<Integer, Object> parameters=new HashMap<Integer, Object>();
        
        String mySQL=getSqlHeader(language);
        mySQL+=" WHERE 1=1 ";
    	int counter=0;
    	if (!(dateBeginInclude==null)) {
    		mySQL = mySQL + 
            	"                  AND TRUNC(date_telgr) >= TO_DATE(?,'DD.MM.RRRR')";
    		parameters.put(++counter, sdf.format(dateBeginInclude));
    	}
    	if (!(dateEndExclude==null)) {
        	mySQL = mySQL + 
        		"                  AND TRUNC(date_telgr) <= TO_DATE(?,'DD.MM.RRRR')";
        	parameters.put(++counter, sdf.format(dateEndExclude));
    	}
        
        if (!(type==null)) {
        	mySQL = mySQL +
            	"        AND cd_telgr_external_state = ? ";
        	parameters.put(++counter, type.toString());
       	}
        mySQL = mySQL + "        and rownum<? ";
        mySQL = mySQL + "        ORDER BY ninvid ";
        
    	parameters.put(++counter, Integer.toString(maxRows));
		PreparedStatement st = null;
    	
    	try {
	    	st = connection.prepareStatement(mySQL);
	    	for(Entry<Integer, Object> eachEntry:parameters.entrySet()){
	    		st.setObject(eachEntry.getKey(), eachEntry.getValue());
	    	}
	    	ResultSet rset = st.executeQuery();
	
	        while (rset.next()){
	        	returnValue.add(readValue(userId, rset));
	         }
	    }catch (SQLException e) {
	    	LOGGER.error("finder sql isn't working properly ", e);
	    	throw new GeneralPaymentException("check SQL part", e);
	    }catch (Exception e) {
	    	LOGGER.error("some general was happend ", e);
	    	throw new GeneralPaymentException("finder isn't working", e);
	    }finally {
	    	ConnectorUtils.closeQuietly(st);
	    } 
		return fixDescription(returnValue);
	}

	private List<PaymentExternalVO> fixDescription(List<PaymentExternalVO> payments) {
		if(payments==null || payments.size()==0){
			return payments;
		}
		List<PaymentExternalVO> returnValue=new ArrayList<PaymentExternalVO>(payments.size());
		for(PaymentExternalVO eachPayment:payments){
			returnValue.add(fixDescription(eachPayment));
		}
		return payments;
	}

	private PaymentExternalVO fixDescription(PaymentExternalVO payment) {
		if(PaymentType.WAITING.equals(payment.getType())){
			// change for that payments, which can be processed via external service
			payment.setDescription(RobokassaUtils.correctDescription(payment.getDescription()));
		}
		return payment;
	}
	
}
