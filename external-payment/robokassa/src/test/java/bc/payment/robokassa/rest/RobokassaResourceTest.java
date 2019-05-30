package bc.payment.robokassa.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.jayway.jsonpath.JsonPath;

import bc.payment.PaymentType;
import bc.payment.exception.PaymentParameterException;
import bc.util.RestUtils;

public class RobokassaResourceTest extends AbstractEmbeddedJetty {

	@Test
	public void checkGenerateUrl(){
		// given
		String nInvId = "101";
		String sOutSum = "10.50";
		String sShpItem = "RRN";
		
		String innerRobokassaLink=getServerRoot()+"/payment/generateUrl?id="+nInvId+"&amount="+sOutSum+"&shipItem="+sShpItem;
		
		String expectedValue="https://auth.robokassa.ru/Merchant/Index.aspx?IsTest=1&MerchantLogin=P4elka&InvId=101&OutSum=10.50&SignatureValue=7dc851632743e6fb0314629f1e07a52d&shp_Item=RRN";

		// when
		String robokassaLink = bc.util.RestUtils.getString(innerRobokassaLink);
		
		// then
		Assert.assertNotNull(robokassaLink);
		Assert.assertEquals(expectedValue, robokassaLink);
	}
	
	@Test
	public void checkPaymentForSuccessfull(){
		// given
		String nInvId = "101";
		
		String innerRobokassaLink=getServerRoot()+"/payment/checkPayment?paymentId="+nInvId;
		
		String expectedValue="false";

		// when
		String robokassaReturnValue= bc.util.RestUtils.getString(innerRobokassaLink);
		
		// then
		Assert.assertNotNull(robokassaReturnValue);
		Assert.assertEquals(expectedValue, robokassaReturnValue);
	}
	
	@Test
	public void calculateComission(){
		// given
		String amount = "101.2"; // !!!! DOT delimiter !!! 
		
		String innerRobokassaLink=getServerRoot()+"/payment/calculateComission?amount="+amount;
		
		Integer expectedValue=new Integer(0);

		// when
		String robokassaReturnValue= bc.util.RestUtils.getString(innerRobokassaLink);
		String jsonExp = "$.result.code";
		Integer jsonResult= JsonPath.read(robokassaReturnValue, jsonExp);
		 
		// then
		Assert.assertNotNull(jsonResult);
		
		//  {"result":{"code":0,"description":null},"groups":{"group":[{"items":{"currency":[{"rate":{"incSum":101.2},"label":"ElecsnetWalletRIBR","alias":"ElecsnetWallet","name":"Кошелек Элекснет","minValue":null,"maxValue":14999}]},"code":"EMoney","description":"Электронным кошельком"},{"items":{"currency":[{"rate":{"incSum":101.20},"label":"BANKOCEAN3R","alias":"BankCard","name":"Банковская карта","minValue":1,"maxValue":null}]},"code":"BankCard","description":"Банковской картой"},{"items":{"currency":[{"rate":{"incSum":101.20},"label":"AlfaBankRIBR","alias":"AlfaBank","name":"Альфа-Клик","minValue":null,"maxValue":null},{"rate":{"incSum":101.20},"label":"RussianStandardBankRIBR","alias":"BankRSB","name":"Банк Русский Стандарт","minValue":null,"maxValue":null},{"rate":{"incSum":101.20},"label":"BSSNationalBankTRUSTR","alias":"BankTrust","name":"Национальный банк ТРАСТ","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"BSSTatfondbankR","alias":"BankTatfondbank","name":"Татфондбанк","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"PSKBR","alias":"BankPSB","name":"Промсвязьбанк","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"HandyBankMerchantOceanR","alias":"HandyBank","name":"HandyBank","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"HandyBankBO","alias":"HandyBankBO","name":"Банк Образование","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"HandyBankFB","alias":"HandyBankFB","name":"ФлексБанк","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"HandyBankFU","alias":"HandyBankFU","name":"ФьючерБанк","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"HandyBankKB","alias":"HandyBankKB","name":"КранБанк","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"HandyBankKSB","alias":"HandyBankKSB","name":"Костромаселькомбанк","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"HandyBankLOB","alias":"HandyBankLOB","name":"Липецкий областной банк","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"HandyBankNSB","alias":"HandyBankNSB","name":"Независимый строительный банк","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"HandyBankTB","alias":"HandyBankTB","name":"Русский Трастовый Банк","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"HandyBankVIB","alias":"HandyBankVIB","name":"ВестИнтерБанк","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"BSSMezhtopenergobankR","alias":"BankMTEB","name":"Межтопэнергобанк","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"MINBankR","alias":"BankMIN","name":"Московский Индустриальный Банк","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"BSSFederalBankForInnovationAndDevelopmentR","alias":"BankFBID","name":"ФБ Инноваций и Развития","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"BSSIntezaR","alias":"BankInteza","name":"Банк Интеза","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"BSSBankGorodR","alias":"BankGorod","name":"Банк Город","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"BSSAvtovazbankR","alias":"BankAVB","name":"Банк АВБ","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"FacturaR","alias":"BankFactura","name":"Factura.ru","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"FacturaBinBank","alias":"BankBin","name":"Factura.ru","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"FacturaSovCom","alias":"BankSovCom","name":"Factura.ru","minValue":1,"maxValue":null},{"rate":{"incSum":101.20},"label":"KUBankR","alias":"KUBank","name":"Кредит Урал Банк","minValue":1,"maxValue":null}]},"code":"Bank","description":"Через интернет-банк"},{"items":{"currency":[{"rate":{"incSum":101.20},"label":"RapidaRIBEurosetR","alias":"StoreEuroset","name":"Евросеть","minValue":null,"maxValue":15000},{"rate":{"incSum":101.20},"label":"RapidaRIBSvyaznoyR","alias":"StoreSvyaznoy","name":"Связной","minValue":null,"maxValue":15000},{"rate":{"incSum":101.20},"label":"BANKOCEAN3CHECKR","alias":"MobileRobokassa","name":"Мобильная ROBOKASSA","minValue":1,"maxValue":null}]},"code":"Other","description":"Другие способы"}]}}
		Assert.assertEquals(expectedValue, jsonResult);
	}

	@Test
	public void getCurrencies(){
		// given
		String amount = "101.2"; // !!!! DOT delimiter !!! 
		
		String innerRobokassaLink=getServerRoot()+"/payment/getCurrencies?amount="+amount;
		
		Integer expectedValue=new Integer(0);

		// when
		String robokassaReturnValue= bc.util.RestUtils.getString(innerRobokassaLink);
		String jsonExp = "$.result.code";
		Integer jsonResult= JsonPath.read(robokassaReturnValue, jsonExp);
		 
		// then
		Assert.assertNotNull(jsonResult);
		
		// {"result":{"code":0,"description":null},"groups":{"group":[{"items":{"currency":[{"rate":null,"label":"ElecsnetWalletRIBR","alias":"ElecsnetWallet","name":"Кошелек Элекснет","minValue":null,"maxValue":14999}]},"code":"EMoney","description":"Электронным кошельком"},{"items":{"currency":[{"rate":null,"label":"BANKOCEAN3R","alias":"BankCard","name":"Банковская карта","minValue":1,"maxValue":null}]},"code":"BankCard","description":"Банковской картой"},{"items":{"currency":[{"rate":null,"label":"AlfaBankRIBR","alias":"AlfaBank","name":"Альфа-Клик","minValue":null,"maxValue":null},{"rate":null,"label":"RussianStandardBankRIBR","alias":"BankRSB","name":"Банк Русский Стандарт","minValue":null,"maxValue":null},{"rate":null,"label":"BSSNationalBankTRUSTR","alias":"BankTrust","name":"Национальный банк ТРАСТ","minValue":1,"maxValue":null},{"rate":null,"label":"BSSTatfondbankR","alias":"BankTatfondbank","name":"Татфондбанк","minValue":1,"maxValue":null},{"rate":null,"label":"PSKBR","alias":"BankPSB","name":"Промсвязьбанк","minValue":1,"maxValue":null},{"rate":null,"label":"HandyBankMerchantOceanR","alias":"HandyBank","name":"HandyBank","minValue":1,"maxValue":null},{"rate":null,"label":"HandyBankBO","alias":"HandyBankBO","name":"Банк Образование","minValue":1,"maxValue":null},{"rate":null,"label":"HandyBankFB","alias":"HandyBankFB","name":"ФлексБанк","minValue":1,"maxValue":null},{"rate":null,"label":"HandyBankFU","alias":"HandyBankFU","name":"ФьючерБанк","minValue":1,"maxValue":null},{"rate":null,"label":"HandyBankKB","alias":"HandyBankKB","name":"КранБанк","minValue":1,"maxValue":null},{"rate":null,"label":"HandyBankKSB","alias":"HandyBankKSB","name":"Костромаселькомбанк","minValue":1,"maxValue":null},{"rate":null,"label":"HandyBankLOB","alias":"HandyBankLOB","name":"Липецкий областной банк","minValue":1,"maxValue":null},{"rate":null,"label":"HandyBankNSB","alias":"HandyBankNSB","name":"Независимый строительный банк","minValue":1,"maxValue":null},{"rate":null,"label":"HandyBankTB","alias":"HandyBankTB","name":"Русский Трастовый Банк","minValue":1,"maxValue":null},{"rate":null,"label":"HandyBankVIB","alias":"HandyBankVIB","name":"ВестИнтерБанк","minValue":1,"maxValue":null},{"rate":null,"label":"BSSMezhtopenergobankR","alias":"BankMTEB","name":"Межтопэнергобанк","minValue":1,"maxValue":null},{"rate":null,"label":"MINBankR","alias":"BankMIN","name":"Московский Индустриальный Банк","minValue":1,"maxValue":null},{"rate":null,"label":"BSSFederalBankForInnovationAndDevelopmentR","alias":"BankFBID","name":"ФБ Инноваций и Развития","minValue":1,"maxValue":null},{"rate":null,"label":"BSSIntezaR","alias":"BankInteza","name":"Банк Интеза","minValue":1,"maxValue":null},{"rate":null,"label":"BSSBankGorodR","alias":"BankGorod","name":"Банк Город","minValue":1,"maxValue":null},{"rate":null,"label":"BSSAvtovazbankR","alias":"BankAVB","name":"Банк АВБ","minValue":1,"maxValue":null},{"rate":null,"label":"FacturaR","alias":"BankFactura","name":"Factura.ru","minValue":1,"maxValue":null},{"rate":null,"label":"FacturaBinBank","alias":"BankBin","name":"Factura.ru","minValue":1,"maxValue":null},{"rate":null,"label":"FacturaSovCom","alias":"BankSovCom","name":"Factura.ru","minValue":1,"maxValue":null},{"rate":null,"label":"KUBankR","alias":"KUBank","name":"Кредит Урал Банк","minValue":1,"maxValue":null}]},"code":"Bank","description":"Через интернет-банк"},{"items":{"currency":[{"rate":null,"label":"RapidaRIBEurosetR","alias":"StoreEuroset","name":"Евросеть","minValue":null,"maxValue":15000},{"rate":null,"label":"RapidaRIBSvyaznoyR","alias":"StoreSvyaznoy","name":"Связной","minValue":null,"maxValue":15000},{"rate":null,"label":"BANKOCEAN3CHECKR","alias":"MobileRobokassa","name":"Мобильная ROBOKASSA","minValue":1,"maxValue":null}]},"code":"Other","description":"Другие способы"}]}}  
		Assert.assertEquals(expectedValue, jsonResult);
	}

	@Test
	public void findPayment(){
		// given
		String userId="201";
		String paymentId = "190393"; // !!!! DOT delimiter !!! 
		
		String innerRobokassaLink=getServerRoot()+"/payment/findCertainPayment?userId="+userId+"&paymentId="+paymentId;
		
		// when
		String robokassaReturnValue= bc.util.RestUtils.getString(innerRobokassaLink);
		
		// then
		// {"userId":201,"id":190393,"description":null,"amount":100.00,"type":"CANCELLED"}
		Assert.assertNotNull(robokassaReturnValue);

		Integer resultUserId= JsonPath.read(robokassaReturnValue, "$.userId");
		Assert.assertEquals(new Integer(userId), resultUserId);
		
		Integer resultId= JsonPath.read(robokassaReturnValue, "$.id");
		Assert.assertEquals(new Integer(paymentId), resultId);
		  
		String resultType= JsonPath.read(robokassaReturnValue, "$.type");
		Assert.assertEquals("PAID", resultType);
	}
	
	//	@RequestMapping(value="/findPayment", method = RequestMethod.GET)
	@Test
	public void findListOfPayments(){
		// given
		String userId="201";
		String paymentType=PaymentType.PAID.toString();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		@SuppressWarnings("deprecation")
		String dateBegin=sdf.format(new Date(116,3,18,12,00,00));
		@SuppressWarnings("deprecation")
		String dateEnd=sdf.format(new Date(116,4,25,12,00,00));
		String innerRobokassaLink=getServerRoot()+"/payment/findPayment?userId="+userId+"&dateBeginInclude="+dateBegin+"&dateEndExclude="+dateEnd+"&paymentType="+paymentType+"&maxRows=10";
		
		// when
		String robokassaReturnValue= bc.util.RestUtils.getString(innerRobokassaLink);
		
		// then
		// [{"userId":201,"id":190542,"description":null,"amount":100.00,"type":"PAID"},{"userId":201,"id":190545,"description":null,"amount":100.00,"type":"PAID"},{"userId":201,"id":190546,"description":null,"amount":100.00,"type":"PAID"},{"userId":201,"id":190549,"description":null,"amount":100.00,"type":"PAID"},{"userId":201,"id":190550,"description":null,"amount":100.00,"type":"PAID"},{"userId":201,"id":190553,"description":null,"amount":100.00,"type":"PAID"},{"userId":201,"id":190554,"description":null,"amount":100.00,"type":"PAID"},{"userId":201,"id":190555,"description":null,"amount":100.00,"type":"PAID"},{"userId":201,"id":190556,"description":null,"amount":100.00,"type":"PAID"}]

		Assert.assertNotNull(robokassaReturnValue);

		Integer resultUserId= JsonPath.read(robokassaReturnValue, "$[0].userId");
		Assert.assertEquals(new Integer(userId), resultUserId);
		
		String resultType= JsonPath.read(robokassaReturnValue, "$[0].type");
		Assert.assertEquals("PAID", resultType);
	}
	
	//	@RequestMapping(value="/writeRedirect", method = RequestMethod.GET)
	@Test
	public void writeRedirect(){
		// given
		String paymentId="190393";
		String urlSuccessful="google.com";
		String urlFail="ukr.net"; 
				
		String robokassaLink=getServerRoot()+"/payment/writeRedirect?paymentId="+paymentId+"&urlSuccessful="+urlSuccessful+"&urlFail="+urlFail;
		
		// when 
		String robokassaReturnValue= bc.util.RestUtils.getString(robokassaLink);

		// then
		Assert.assertEquals("true", robokassaReturnValue);
	}

	
	@Test
	public void checkOutputUrl() throws PaymentParameterException, UnsupportedEncodingException{
		// given
		String description=URLEncoder.encode("привет", "UTF-8");
		// String description="привет";
		// String innerRobokassaLink=getServerRoot()+"/payment/generateUrl?id="+nInvId+"&amount="+sOutSum+"&shipItem="+sShpItem;
		String url=getServerRoot()+"/payment/generateUrl?id=10&description="+description+"&amount=12.5&shipItem=10";
		
		// when
		String returnValue=RestUtils.getString(url);
		
		// System.out.println(">>>");
		// System.out.println(returnValue);
		// then
		Assert.assertNotNull(returnValue);
		Assert.assertEquals("https://auth.robokassa.ru/Merchant/Index.aspx?IsTest=1&MerchantLogin=P4elka&InvId=10&OutSum=12.50&SignatureValue=97900ba6426da5b9a5f4961b84736a38&shp_Item=10", returnValue);
	}
	
}
