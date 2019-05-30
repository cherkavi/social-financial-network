package bc.payment.robokassa;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

import bc.util.jdbc.CommonFunction;

@Service
public class RobokassaRedirectAware {
	private final static Logger	LOGGER	= Logger.getLogger(RobokassaRedirectAware.class);

	@Autowired
	private DataSource			dataSource;

	@Value("${robokassa.redirect-page.success.default}")
	private String				defaultRedirectSuccess;

	@Value("${robokassa.redirect-page.fail.default}")
	private String				defaultRedirectFail;

	private RedirectFunction redirectFunction;
	
	@PostConstruct
	private void init(){
		this.redirectFunction=new RedirectFunction(dataSource);
	}

	/** retrieve success redirect url for transaction */
	private final static String	REDIRECT_QUERY_SUCCESS	= "SELECT external_redirect_success FROM vc_telgr_invoice_all where ninvid=";
	/** retrieve success redirect fail for transaction */
	private final static String	REDIRECT_QUERY_FAIL		= "SELECT external_redirect_fail FROM vc_telgr_invoice_all where ninvid=";

	public String successRedirectUrl(Long invoiceId) throws DataAccessException {
		String returnValue = null;
		try {
			returnValue = new JdbcTemplate(this.dataSource)
					.queryForObject(RobokassaRedirectAware.REDIRECT_QUERY_SUCCESS + invoiceId, String.class);
		} catch (DataAccessException ex) {
			RobokassaRedirectAware.LOGGER.error("can't read data from DB: " + ex.getMessage(), ex);
		}
		if (returnValue == null) {
			returnValue = this.defaultRedirectSuccess;
		}
		return returnValue;
	}

	public String failRedirectUrl(Long invoiceId) throws DataAccessException {
		String returnValue = null;
		try {
			returnValue = new JdbcTemplate(this.dataSource)
					.queryForObject(RobokassaRedirectAware.REDIRECT_QUERY_FAIL + invoiceId, String.class);
		} catch (DataAccessException ex) {
			RobokassaRedirectAware.LOGGER.error("can't read data from DB: " + ex.getMessage(), ex);
		}
		if (returnValue == null) {
			returnValue = this.defaultRedirectFail;
		}
		return returnValue;
	}

	public String failRedirectUrlDefault() {
		return this.defaultRedirectFail;
	}

	public boolean writeRedirectUrl(Long invoiceId, String successUrl, String failUrl) {
		return redirectFunction.execute(invoiceId, successUrl, failUrl);
	}

}

class RedirectFunction extends CommonFunction{
	
	public RedirectFunction(DataSource dataSource) {
		super(dataSource, 
				"pack$external_payment.set_robokassa_redirect",
				new SqlParameter("p_id_telgr", Types.NUMERIC),
				new SqlParameter("p_redirect_success", Types.VARCHAR),
				new SqlParameter("p_redirect_fail", Types.VARCHAR),
				new SqlOutParameter("p_result_message", Types.VARCHAR)
				);
	}

	public boolean execute(Long idTelgr, String urlSuccess, String urlFail) {
		Map<String, Object> in = new HashMap<String, Object>();
		in.put("p_id_telgr", idTelgr);
		in.put("p_redirect_success", urlSuccess);
		in.put("p_redirect_fail", urlFail);
		return executeAndParseResult(in);
	}
}