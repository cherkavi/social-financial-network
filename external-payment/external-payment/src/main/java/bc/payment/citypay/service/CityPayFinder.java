package bc.payment.citypay.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import bc.payment.citypay.domain.CheckingPayment;
import bc.payment.citypay.domain.CheckingResponse;
import bc.utils.ConvertUtils;

@Service
public class CityPayFinder {
	private static final Logger LOGGER = Logger.getLogger(CityPayFinder.class);

	// private static final String SQL_FIND_PAYMENTS="select
	// id_external_point_trans, payer, date_external_oper, amount_external_oper
	// from VC_CITYPAY_EXT_OPER where date_external_oper between ? and ?";
	private static final String SQL_FIND_PAYMENTS = "SELECT transactionid, account, transactiondate, amount, amount_frmt, payelementid FROM VC_EXTERNAL_PAYMENT_SUCCESS  WHERE transactiondate BETWEEN ? AND ?";
	private static final String SQL_FIND_PAYMENTS_ELEMENT_ID = "SELECT transactionid, account, transactiondate, amount, amount_frmt, payelementid FROM VC_EXTERNAL_PAYMENT_SUCCESS  WHERE transactiondate BETWEEN ? AND ? AND payelementid=? ";

	@Autowired
	private DataSource dataSource;

	/**
	 * report for external system, usually using per one day
	 * 
	 * @param checkDateBegin
	 * @param checkDateEnd
	 * @return
	 */
	public CheckingResponse findPayments(Date checkDateBegin, Date checkDateEnd, String payElementId) {
		return new CheckingResponse(readPayments(checkDateBegin, checkDateEnd, payElementId));
	}

	private final static String DATE_DELIMITER = "..";

	private List<CheckingPayment> readPayments(Date checkDateBegin, Date checkDateEnd, String payElementId) {
		LOGGER.debug(SQL_FIND_PAYMENTS);
		LOGGER.debug(checkDateBegin + DATE_DELIMITER + checkDateEnd);
		LOGGER.debug(payElementId);
		if (payElementId == null) {
			return new JdbcTemplate(dataSource).query(SQL_FIND_PAYMENTS, new Object[] { checkDateBegin, checkDateEnd }, new ReportRotMapper());
		} else {
			return new JdbcTemplate(dataSource).query(SQL_FIND_PAYMENTS_ELEMENT_ID, new Object[] { checkDateBegin, checkDateEnd, payElementId }, new ReportRotMapper());
		}
	}

	static class ReportRotMapper implements RowMapper<CheckingPayment>{
		@Override
		public CheckingPayment mapRow(ResultSet resultSet, int index) throws SQLException {
			return new CheckingPayment(
					resultSet.getString("transactionid"),
					resultSet.getLong("account"), 
					new Date(resultSet.getTimestamp("transactiondate").getTime()),
					resultSet.getString("amount_frmt")
					// ConvertUtils.divideByHundred(resultSet.getBigDecimal("amount"))
					);
		}
	}
}

