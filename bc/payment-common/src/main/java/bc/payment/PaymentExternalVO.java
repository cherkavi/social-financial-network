package bc.payment;

import java.math.BigDecimal;

/**
 * Payment Visual Object, for system with redirection <br />
 * payment just will wait for move to external system, and after this, payment will be provided
 */
public class PaymentExternalVO {
	private final Long userId;
	/**
	 * inner id of current system
	 */
	private final Long id;
	/**
	 * description of the payment 
	 */
	private String description;
	/**
	 * amount of the payment 
	 */
	private final BigDecimal amount;

	private final PaymentType type;
	
	private final String codeOfGoods;

	public PaymentExternalVO(Long userId, Long id, String description, BigDecimal amount, PaymentType type) {
		this(userId, id, description, amount, type, null);
	}
	
	public PaymentExternalVO(Long userId, Long id, String description, BigDecimal amount, PaymentType type, String codeOfGoods) {
		this.userId=userId;
		this.id = id;
		this.description = description;
		this.amount = amount;
		this.type=type;
		this.codeOfGoods=codeOfGoods;
	}

	public PaymentExternalVO(String userId, String id, String description, String amount, String type) {
		this( (userId==null?null:Long.parseLong(userId, 10)),  Long.parseLong(id, 10), description, new BigDecimal(amount), ("PAID".equalsIgnoreCase(type)?(PaymentType.PAID):("WAITING".equalsIgnoreCase(type)?(PaymentType.WAITING):(PaymentType.CANCELLED))));
	}
	
	/**
	 * @return id of the user
	 */
	public Long getUserId(){
		return userId;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * set new description
	 * @param newValue
	 */
	public void setDescription(String newValue){
		this.description=newValue;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @return the type
	 */
	public PaymentType getType() {
		return type;
	}
	
}
