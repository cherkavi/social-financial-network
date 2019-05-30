package bc.payment;

import java.sql.Connection;

import bc.payment.exception.GeneralPaymentException;

public interface PaymentExternalService {
	
	/**
	 * set payment result ( external system sent result to current system, need to set record with invoice )
	 * @param connection - connection to DB
	 * @param paymentId - id of payment 
	 * @param paymentResult - result of payment
	 * @return
	 */
	void setPaymentExternalSystemResult(Connection connection, String paymentId, PaymentResultOfExternalService paymentResult) throws GeneralPaymentException;
	
}
