package bc.payment;

/**
 * result of payment from external service <br />
 * result of payment from point of view of the external Payment System
 */
public enum PaymentResultOfExternalService {
	ACKNOWLEDGED,
	PAYMENT_NOT_FOUND,
	RESULT_NOT_RECOGNIZED,
	INTERNAL_ERROR;
}