package bc.payment;


/**
 * type of payment into current system ( flag of payment )
 */
public enum PaymentType {
	/**
	 * it was payed ( can be moved to after {@link PaymentType#WAITING} )
	 */
	PAID,
	/**
	 * waiting for payment ( init state )
	 */
	WAITING,
	/**
	 * it was cancelled ( can be moved to after {@link PaymentType#WAITING} )
	 */
	CANCELLED;
}
