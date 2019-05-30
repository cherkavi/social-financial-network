package bc.payment.robokassa;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = "classpath:spring-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RobokassaRedirectAwareTest {

	@Autowired
	DataSource				dataSource;

	@Autowired
	RobokassaRedirectAware	redirectService;
	
	@Test
	public void checkWriteRedirectUrls() {
		// given
		Long id = generateId();
		String urlSuccess = "test-url-success";
		String urlFail = "test-url-fail";
		// when
		boolean result = redirectService.writeRedirectUrl(id, urlSuccess, urlFail);
		// then
		Assert.assertTrue(result);
	}

	@Test
	public void checkReadRedirectUrls() {
		// given
		Long id = generateId();
		String urlSuccess = "test-url-success" + Long.toString(id);
		String urlFail = "test-url-fail" + Long.toString(id);

		// when
		boolean result = redirectService.writeRedirectUrl(id, urlSuccess, urlFail);

		// then
		Assert.assertTrue(result);

		// when
		String successRedirect = redirectService.successRedirectUrl(id);
		String failRedirect = redirectService.failRedirectUrl(id);

		// then
		Assert.assertEquals(urlSuccess, successRedirect);
		Assert.assertEquals(urlFail, failRedirect);
	}

	private Long generateId() {
		GenerateTestPayment testPayment = new GenerateTestPayment(this.dataSource);
		return testPayment.executeFunction();
	}

}

class GenerateTestPayment extends StoredProcedure {
	private final static String RESULT_POSITIVE = "0";

	public GenerateTestPayment(DataSource dataSource) {
		super(dataSource, "pack$webpos_test.add_test_invoice");
		declareParameter(new SqlOutParameter("result", Types.VARCHAR));
		declareParameter(new SqlOutParameter("p_id_telgr", Types.NUMERIC));
		declareParameter(new SqlOutParameter("p_result_message", Types.VARCHAR));

		setFunction(true);
		compile();
	}

	public Long executeFunction() {
		Map<String, Object> in = new HashMap<String, Object>();
		Map<String, Object> out = execute(in);
		if(out.isEmpty()){
			return null;
		}
		if (!RESULT_POSITIVE.equals(out.get("result"))) {
			return null;
		}
		return ((BigDecimal) out.get("p_id_telgr")).longValue();
	}
}
