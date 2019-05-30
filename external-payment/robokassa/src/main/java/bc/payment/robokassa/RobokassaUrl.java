package bc.payment.robokassa;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bc.payment.ExternalSystemUrl;
import bc.payment.exception.PaymentParameterException;
import bc.util.CurrencyUtils;

@Service
public class RobokassaUrl implements ExternalSystemUrl{
	// private static Logger LOGGER=Logger.getLogger(RobokassaUrl.class);

	// private final static String URL_ENCODE="UTF-8";
	private final static String URL_DELIMITER="?";
	private final static String URL_PARAMETERS_DELIMITER="&";
	private final static String URL_PARAMETER_VALUE_DELIMITER="=";
	
	private static String TEST_PARAM_NAME="IsTest";
	private static String TEST_PARAM_VALUE="1";
	
	@Autowired
	private RobokassaParameters parameters;

	public RobokassaUrl() throws PaymentParameterException{
	}
	
	@PostConstruct
	private void init() throws PaymentParameterException{
		checkForNull(parameters.getRobokassaUrl(), "check context parameter: "+RobokassaParameters.CONTEXT_VAR_URL);
		checkForNull(parameters.getRobokassaLogin(), "check context parameter: "+RobokassaParameters.CONTEXT_VAR_LOGIN);
		checkForNull(parameters.getRobokassaPassword(), "check context parameter: "+RobokassaParameters.CONTEXT_VAR_PASSWORD);
		checkForNull(parameters.getRobokassaPassword2(), "check context parameter: "+RobokassaParameters.CONTEXT_VAR_PASSWORD2);
	}
	
	@Override
	public String makeExternalLink(Long id, BigDecimal amount, String codeOfGoods) throws PaymentParameterException {
		if(codeOfGoods!=null){
			return calculateExternalUrlWithShipItem(parameters.getRobokassaLogin(), parameters.getRobokassaPassword(), id, amount, codeOfGoods);
		}else{
			return calculateExternalUrl(parameters.getRobokassaLogin(), parameters.getRobokassaPassword(), id, amount);
		}
	}
	
	@Override
	public String makeExternalLink(Long id, BigDecimal amount)
			throws PaymentParameterException {
		return makeExternalLink(id, amount, null);
	}
	
	
	/**
	 * throw Exception when some of vital parameters were not set
	 * @param value
	 * @param message
	 * @throws PaymentParameterException
	 */
	private static void checkForNull(String value, String message) throws PaymentParameterException{
		if(value==null){
			throw new PaymentParameterException(message);
		}
	}
	
	@SuppressWarnings("unchecked")
	private String calculateExternalUrl(String merchantLogin, String merchantPassword, Long innerId, BigDecimal paymentAmount) throws PaymentParameterException {
		String crc=RobokassaUtils.crc(merchantLogin,CurrencyUtils.amountToString(paymentAmount),Long.toString(innerId),merchantPassword);
		if(parameters.getTestEndpoint()){
			return assembleUrl(parameters.getRobokassaUrl(), 
					new ImmutablePair<String, String>(TEST_PARAM_NAME,TEST_PARAM_VALUE),
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_MERCHANT_LOGIN,merchantLogin),
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_INV_ID,Long.toString(innerId)),
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_OUT_SUM, CurrencyUtils.amountToString(paymentAmount)),
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_SIGN_VALUE,crc)
					);
		}else{
			return assembleUrl(parameters.getRobokassaUrl(), 
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_MERCHANT_LOGIN,merchantLogin),
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_INV_ID,Long.toString(innerId)),
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_OUT_SUM, CurrencyUtils.amountToString(paymentAmount)),
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_SIGN_VALUE,crc)
					);
		}
	}
	
	@SuppressWarnings("unchecked")
	private String calculateExternalUrlWithShipItem(String merchantLogin, String merchantPassword, Long innerId, BigDecimal paymentAmount, String shipItem) throws PaymentParameterException {
		String crc=RobokassaUtils.crcWithShipItem(shipItem, merchantLogin,CurrencyUtils.amountToString(paymentAmount),Long.toString(innerId),merchantPassword);
		if(parameters.getTestEndpoint()){
			return assembleUrl(parameters.getRobokassaUrl(), 
					new ImmutablePair<String, String>(TEST_PARAM_NAME,TEST_PARAM_VALUE),
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_MERCHANT_LOGIN,merchantLogin),
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_INV_ID,Long.toString(innerId)),
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_OUT_SUM, CurrencyUtils.amountToString(paymentAmount)),
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_SIGN_VALUE,crc), 
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_SHIP_ITEM, shipItem)
					);
		}else{
			return assembleUrl(parameters.getRobokassaUrl(), 
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_MERCHANT_LOGIN,merchantLogin),
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_INV_ID,Long.toString(innerId)),
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_OUT_SUM, CurrencyUtils.amountToString(paymentAmount)),
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_SIGN_VALUE,crc), 
					new ImmutablePair<String, String>(RobokassaUtils.ROBOPARAM_SHIP_ITEM, shipItem)
					);
		}
	}
	
	private static String assembleUrl(String url, Pair<String, String> ... parameters) throws PaymentParameterException{
		StringBuilder returnValue=new StringBuilder();
		returnValue.append(url);
		returnValue.append(URL_DELIMITER);
		if(parameters==null || parameters.length==0){
			return returnValue.toString();
		}
		boolean needToAddAmpersand=false;
		for(Pair<String, String> eachParameter:parameters){
			if(eachParameter==null){
				continue;
			}
			if(needToAddAmpersand){
				returnValue.append(URL_PARAMETERS_DELIMITER);
			}else{
				needToAddAmpersand=true;
			}
			returnValue.append(eachParameter.getLeft());
			returnValue.append(URL_PARAMETER_VALUE_DELIMITER);
			returnValue.append(eachParameter.getRight());
//			try {
//				// returnValue.append(URLEncoder.encode(eachParameter.getRight(), URL_ENCODE));
//			} catch (UnsupportedEncodingException e) {
//				throw new PaymentParameterException();
//			}
		}
		return returnValue.toString();
	}

	
}
