package bc.payment.sberbank.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bc.payment.common.service.CommonService;
import bc.payment.exception.GeneralPaymentException;
import bc.payment.exception.SpecificApiException;
import bc.payment.sberbank.domain.Response;

@Component
public class SberbankService {
	private final static String VENDOR_NAME="SBERBANK";
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * Сбербанк - Протокол Оператора 3-5-8.pdf <br />
	 * page 2 <br />
	 * 4. Проверка параметров перед проведением платежа
	 * @param account
	 * @return
	 * @throws GeneralPaymentException 
	 */
	public Response.Parameters check(String account, int amountInCents) throws GeneralPaymentException {
		try{
			commonService.checkAccountForRecharge(VENDOR_NAME, account, amountInCents, null, null, null, null);
			return new Response.Parameters(
					0, // errorCode
					"", // errorText
					account, // account
					amountInCents, // desiredAmount
					"",// rs.getString("name_first")+" "+rs.getString("name_second"), // clientName
					"0" // balance
					);
		}catch(SpecificApiException ex){
			return new Response.Parameters(ex.getCode(), ex.getMessage(), account, 0, "", "");
		}
	}

	/**
	 * Сбербанк - Протокол Оператора 3-5-8.pdf <br />
	 * page 3 <br />
	 * 5. Проведение платежей
	 * @param account
	 * @return
	 * @throws GeneralPaymentException 
	 */
	public Response.Parameters payment(final long paymentId, final String account, final int amountInCents, final Date agentDate) throws GeneralPaymentException {
		try{
			commonService.payment(VENDOR_NAME, paymentId, account, amountInCents, agentDate, null, null, null, null);
			// TODO need to discuss:
			// reg_id – идентификатор платежа у Оператора
			// reg_date – дата регистрации платежа у Оператора
			// build positive response 
			return new Response.Parameters(account);
		}catch(SpecificApiException specificException){
			return new Response.Parameters(specificException.getCode(), specificException.getMessage(), account, 0, "", "");
		}
		
	}


}
