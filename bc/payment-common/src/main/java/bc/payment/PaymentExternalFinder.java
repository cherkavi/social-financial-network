package bc.payment;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import bc.payment.exception.GeneralPaymentException;

/**
 * finder of payments which belong(s) to external system
 */
public interface PaymentExternalFinder{

	/**
	 * @param userId - id of the user
	 * @param connection - JDBC connection
	 * @param paymentId - id of the payment ( should belong to this user )
	 * @return
	 * <ul>
	 * 	<li><p>null</p> - payment was not found</li>
	 * 	<li><p>{@link PaymentExternalVO}</p> - represent one certain payment </li>
	 * </ul>
	 */
	PaymentExternalVO find(Long userId, Connection connection, Long paymentId, String language, String generalDbScheme) throws GeneralPaymentException;

	
	/**
	 * @param userId - id of the user
	 * @param connection - connection with DB
	 * @param dateBeginInclude - date of begin of time range
	 * @param dateEndExclude - date of end of time range
	 * @return - 
	 */
	List<PaymentExternalVO> find(Long userId, Connection connection, Date dateBeginInclude, Date dateEndExclude, PaymentType type, int maxRows, String language, String generalDbScheme) throws GeneralPaymentException;
	
}
