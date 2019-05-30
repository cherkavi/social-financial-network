package bc.payment;

import java.math.BigDecimal;

import bc.payment.exception.PaymentParameterException;

public interface ExternalSystemUrl {
	/**
	 * create link for external system, according payment 
	 * @param id
	 * @param amount 
	 * @return
	 * @throws PaymentParameterException
	 */
	String makeExternalLink(Long id, BigDecimal amount) throws PaymentParameterException;

	/**
	 * 
	 * @param id - inner id
	 * @param amount - amount of payment
	 * @param codeOfGoods - code of good(s) which are payment
	 * @return
	 * @throws PaymentParameterException
	 */
	String makeExternalLink(Long id, BigDecimal amount, String codeOfGoods)
			throws PaymentParameterException;
}
