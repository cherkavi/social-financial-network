package bc.ws.persistent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import bc.util.ConnectorUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import bc.util.RestUtils;
import bc.ws.domain.CheckCardResult;
import bc.ws.domain.ConfirmationResult;
import bc.ws.domain.PaymentResult;
import bc.ws.exception.PersistentException;
import bc.ws.persistent.CardOperations.OperationType;
import bc.ws.persistent.CardOperations.PaymentType;
import bc.ws.persistent.CardOperations.ReplenishKind;
import junit.framework.Assert;

@SuppressWarnings("unused")
@RunWith(value = Parameterized.class)
public class CompositeOperation2Test {
	private final static Logger LOGGER = Logger.getLogger(CompositeOperation2Test.class);

	private final static String DEFAULT_USERNAME = "U20001";
	private final static String DEFAULT_PASSWORD = "1234";

	private final static int C_OPERATION_OK = 0;
	private final static int C_OPERATION_ERROR = 1;
	private final static int C_NEED_END_INFO = 20000;
	private final static int C_MEMBERSHIP_FEE_ERROR = 20001;
	private final static int C_ISNT_ENOUGH_POINTS = 20002;
	private final static int C_CARD_NOT_GIVEN = 20003;
	private final static int C_CARD_NOT_QUESTIONED = 20004;
	private final static int C_CARD_NOT_ACTIVATED = 20005;
	private final static int C_NEED_PIN = 20006;
	private final static int C_NEED_SMS_CONFIRMATION = 20007;
	private final static int C_NEED_ACTIVATION_CODE = 20008;
	private final static int C_ENOUGH_MEANS = 20009;
	private final static int C_NEED_MANY_FEE = 20010;
	private final static int C_SMS_CONFIRM_CREATED = 20011;
	private final static int C_CONFIRM_RETURN = 20099;

	private Parameter valueForTest;

	public CompositeOperation2Test(Parameter value) {
		super();
		this.valueForTest = value;
	}

	private static AbstractApplicationContext applicationContext;
	private static ConnectionManager connectionManager;

	@BeforeClass
	public static void initClass() {
	}

	@AfterClass
	public static void destroyClass() {
		applicationContext.close();
	}

	@Parameters()
	public static Iterable<Object[]> dataForTest() throws SQLException {
		applicationContext = new ClassPathXmlApplicationContext("spring-context-test.xml");
		connectionManager = applicationContext.getBean(ConnectionManager.class);

		Connection connection = null;
		List<Parameter> values = null;
		try {
			connection = connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
			JdbcTemplate template = new JdbcTemplate(new SingleConnectionDataSource(connection, true));
			values = template.query("SELECT * FROM ws$test_payment_data", new RowMapper<Parameter>() {
				@Override
				public Parameter mapRow(ResultSet rs, int rowNum) throws SQLException {
					try{
						return Parameter.parse(rs);
					}catch(IllegalArgumentException ex){
						return null;
					}
				}
			});
		} finally {
			ConnectorUtils.closeQuietly(connection);
		}
		return convertToListOfArrays(values);
	}

	private static Iterable<Object[]> convertToListOfArrays(List<Parameter> values) {
		List<Object[]> returnValue = new ArrayList<Object[]>(values.size());
		for (Parameter eachValue : values) {
			if(eachValue==null){
				continue;
			}
			returnValue.add(new Object[] { eachValue });
		}
		return returnValue;
	}

	private final static String STEP1 = "STEP#1";
	private final static String STEP2 = "STEP#2";
	private final static String STEP3 = "STEP#3";

	@Test
	public void check() throws PersistentException, SQLException {
		LOGGER.debug("begin");
		Connection connection = null;
		label1: {
			try {
				connection = connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
				CardOperations operations = new CardOperations(connection);

				LOGGER.debug("check card");
				CheckCardResult checkCardResult = operations.checkCard(this.valueForTest.getTerminal(),
						this.valueForTest.getCardNumber(), OperationType.PAYMENT, "DD.MM.RRRR");
				if (!in(checkCardResult.getCode(), C_OPERATION_OK, C_NEED_END_INFO, C_MEMBERSHIP_FEE_ERROR)) {
					// Assert.fail("check card Fail:"+this.valueForTest);
					throw new StepException(STEP1);
				}

				LOGGER.debug("payment");
				PaymentResult paymentResult = operations.payment(this.valueForTest.getTerminal(),
						this.valueForTest.getCardNumber(),  null, null, this.valueForTest.getPaymentType(), null, "1000.00",
						null, null, "1000.00", null, null, "1500.00", "500.00",
						checkCardResult.getMembershipNeedPaySum(), checkCardResult.getMembershipFeeMargin(), true,
						true);

				if (!in(paymentResult.getCode(), C_OPERATION_OK, C_NEED_END_INFO, C_SMS_CONFIRM_CREATED, C_ENOUGH_MEANS,
						C_ISNT_ENOUGH_POINTS, C_NEED_PIN, C_NEED_SMS_CONFIRMATION, C_NEED_ACTIVATION_CODE)) {
					// Assert.fail("fail payment
					// terminalId:"+this.valueForTest);
					throw new StepException(STEP2);
				}
				if (in(paymentResult.getCode(), C_ENOUGH_MEANS, C_ISNT_ENOUGH_POINTS)) {
					ReplenishKind replenishKind = paymentResult.getCode() == C_ENOUGH_MEANS ? ReplenishKind.SHARE_FEE
							: ReplenishKind.GET_FROM_SHARE_ACCOUNT;
					LOGGER.debug("payment 2");
					paymentResult = operations.payment(this.valueForTest.getTerminal(),
							this.valueForTest.getCardNumber(), null, replenishKind, this.valueForTest.getPaymentType(), null,
							"0.00", paymentResult.getSumShareFeeNeed(), paymentResult.getShareFeeNeedMargin(),
							"1000.00", null, null, "0.00", "0.00", checkCardResult.getMembershipNeedPaySum(),
							checkCardResult.getMembershipFeeMargin(), true, true);
					if (!in(paymentResult.getCode(), C_OPERATION_OK, C_NEED_END_INFO, C_SMS_CONFIRM_CREATED, C_NEED_PIN,
							C_NEED_SMS_CONFIRMATION, C_NEED_ACTIVATION_CODE)) {
						// Assert.fail("fail payment2: "+this.valueForTest);
						throw new StepException(STEP3);
					}
				}
				if (paymentResult.getCode() == C_OPERATION_OK) {
					return;
				}
				ConfirmationResult confirmationResult = null;
				LOGGER.debug("confirm");
				CardOperations.ConfirmationType confirmationType = null;
				String confirmationCode = "";
				if (paymentResult.getCode() == C_NEED_END_INFO) {
					confirmationType = CardOperations.ConfirmationType.NONE;
					confirmationCode = "";
				} else if (paymentResult.getCode() == C_SMS_CONFIRM_CREATED) {
					confirmationType = CardOperations.ConfirmationType.SMS;
					confirmationCode = "1234";
				} else if (paymentResult.getCode() == C_NEED_PIN) {
					confirmationType = CardOperations.ConfirmationType.PIN;
					confirmationCode = "1234";
				} else if (paymentResult.getCode() == C_NEED_ACTIVATION_CODE) {
					confirmationType = CardOperations.ConfirmationType.ACTIVATION_CODE;
					confirmationCode = "12345";
				} else if (paymentResult.getCode() == C_NEED_SMS_CONFIRMATION) {
					return;
				}
				confirmationResult = operations.confirm(paymentResult.getIdTelegr(),
						CardOperations.ConfirmationType.NONE, null, "");
			} catch (StepException ex) {
				// TODO check this logic
				if (ex.getMessage().equals(STEP1) && !this.valueForTest.isStep1()) {
					break label1;
				}
				if (ex.getMessage().equals(STEP2) && !this.valueForTest.isStep2()) {
					break label1;
				}
				if (ex.getMessage().equals(STEP3) && !this.valueForTest.isStep3()) {
					break label1;
				}
				Assert.fail(ex.getMessage());
			} finally {
				LOGGER.debug("close connection");
				connectionManager.close(connection);

			}
		}
	}

	private boolean in(int code, int... codes) {
		if (codes == null) {
			return false;
		}
		for (int index = 0; index < codes.length; index++) {
			if (codes[index] == code) {
				return true;
			}
		}
		return false;
	}

}

class StepException extends Exception {

	private static final long serialVersionUID = 1L;

	public StepException() {
		super();
	}

	public StepException(String message) {
		super(message);
	}

}

class Parameter {
	private String terminal;
	private String cardNumber;
	private CardOperations.PaymentType paymentType;
	private boolean step1;
	private boolean step2;
	private boolean step3;

	public Parameter(String terminal, String cardNumber, PaymentType paymentType, boolean step1, boolean step2,
			boolean step3) {
		super();
		this.terminal = terminal;
		this.cardNumber = cardNumber;
		this.paymentType = paymentType;
		this.step1 = step1;
		this.step2 = step2;
		this.step3 = step3;
	}

	public static Parameter parse(ResultSet rs) throws SQLException {
		return new Parameter(setDefault(rs.getString("ID_TERM"), "20001"), rs.getString("CD_CARD1"),
				PaymentType.valueOf(rs.getString("CD_TRANS_PAY_TYPE")), isSuccess(rs.getString("RESULT_STEP1")),
				isSuccess(rs.getString("RESULT_STEP2")), isSuccess(rs.getString("RESULT_STEP3")));
	}

	private final static String SUCCESS_VALUE = "SUCCESS";

	private static boolean isSuccess(String value) {
		return SUCCESS_VALUE.equalsIgnoreCase(value);
	}

	private static Integer setDefault(int value, int defaultValue) {
		return (value == 0) ? defaultValue : value;
	}

    private static String setDefault(String value, String defaultValue) {
        return value==null ? defaultValue : value;
    }

	public String getTerminal() {
		return terminal;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public CardOperations.PaymentType getPaymentType() {
		return paymentType;
	}

	public boolean isStep1() {
		return step1;
	}

	public boolean isStep2() {
		return step2;
	}

	public boolean isStep3() {
		return step3;
	}

	@Override
	public String toString() {
		return "ValueForTest [terminal=" + terminal + ", cardNumber=" + cardNumber + ", paymentType=" + paymentType
				+ ", step1=" + step1 + ", step2=" + step2 + ", step3=" + step3 + "]";
	}

}