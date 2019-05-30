package bc.payment.citypay.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import bc.payment.citypay.resource.PaymentResource;
import bc.payment.citypay.service.CityPayDateConverter;

@Root(name="Payment")
public class CheckingPayment {
	/**
	 * unique transaction Id into CityPay system <br />
	 * was written by
	 * {@link PaymentResource#executeCheckPayCancel} field <b>transactionId</b>
	 */
	@Element(name="TransactionId")
	private String transactionId;
	/**
	 * identifier of client of system - wm-system card number
	 */
	@Element(name="Account")
	private Long account;
	/**
	 * date of transaction into wm-system
	 */
	@Element(name="TransactionDate") // yyyyMMddHHmmss
	@Convert(CityPayDateConverter.class)
	private Date transactionDate;
	/**
	 * TODO need to check: citypay.com.ua ( after some investigation I think it is  UAH/980 )
	 */
	@Element(name="Amount")
	private String amount;


	public CheckingPayment() {
	}

	public CheckingPayment(String transactionId, Long account,
			Date transactionDate, String amount) {
		super();
		this.transactionId = transactionId;
		this.account = account;
		this.transactionDate = transactionDate;
		this.amount = amount;
	}

	public String getTransactionId() {
		return this.transactionId;
	}
	public Long getAccount() {
		return this.account;
	}
	public Date getTransactionDate() {
		return this.transactionDate;
	}
	public String getAmount() {
		return this.amount;
	}

}
