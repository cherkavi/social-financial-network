package bc.ws.endpoint;

import org.junit.Assert;
import org.junit.Test;


public class WebServiceResourceTest extends AbstractEmbeddedJetty {

	private final static String WS_ENDPOINT_PREFIX="<?xml version='1.0' encoding='UTF-8'?><wsdl:definitions ";
	
	@Test
	public void checkStartEndpointCardOperations(){
		// given
		String innerLink=getServerRoot()+"/services/cardOperations?wsdl";
		
		// when
		String result = bc.util.RestUtils.getString(innerLink);
		
		// then
		Assert.assertNotNull(result);
		Assert.assertTrue(result.startsWith(WS_ENDPOINT_PREFIX));
	}
	
	@Test
	public void checkStartEndpointUserParams(){
		// given
		String innerLink=getServerRoot()+"/services/userParams?wsdl";
		
		// when
		String result = bc.util.RestUtils.getString(innerLink);
		
		// then
		Assert.assertNotNull(result);
		Assert.assertTrue(result.startsWith(WS_ENDPOINT_PREFIX));
	}
}
