package bc.ws.persistent;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import bc.ws.domain.CheckCardResult;
import bc.ws.domain.ConfirmationResult;
import bc.ws.domain.PaymentResult;
import bc.ws.exception.PersistentException;
import bc.ws.persistent.CardOperations.OperationType;
import bc.ws.persistent.CardOperations.PaymentType;
import bc.ws.persistent.CardOperations.ReplenishKind;
import junit.framework.Assert;

@SuppressWarnings("unused")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-context-test.xml"})
public class CompositeOperationTest {
	private final static int C_OPERATION_OK          =0;
	private final static int C_OPERATION_ERROR       =1;
	private final static int C_NEED_END_INFO         = 20000;
	private final static int C_MEMBERSHIP_FEE_ERROR  = 20001;
	private final static int C_ISNT_ENOUGH_POINTS    = 20002;
	private final static int C_CARD_NOT_GIVEN        = 20003;
	private final static int C_CARD_NOT_QUESTIONED   = 20004;
	private final static int C_CARD_NOT_ACTIVATED    = 20005;
	private final static int C_NEED_PIN              = 20006;
	private final static int C_NEED_SMS_CONFIRMATION = 20007;
	private final static int C_NEED_ACTIVATION_CODE  = 20008;
	private final static int C_ENOUGH_MEANS          = 20009;
	private final static int C_NEED_MANY_FEE         = 20010;
	private final static int C_SMS_CONFIRM_CREATED   = 20011;
	private final static int C_CONFIRM_RETURN        = 20099;	

	@Autowired
	ConnectionManager connectionManager;

	@Test
	public void secondTest() throws SQLException, PersistentException{
		Connection connection=connectionManager.open("U20005", "1234");
		try {
			CardOperations operations = new CardOperations(connection);

			CheckCardResult checkCardResult = operations.checkCard("20001", "9900990010021", OperationType.PAYMENT, "DD.MM.RRRR");
			if (!in(checkCardResult.getCode(), C_OPERATION_OK, C_MEMBERSHIP_FEE_ERROR)) {
				Assert.fail();
			}

			PaymentResult paymentResult = operations.payment(
					"20001",
					"9900990010021",
					null, // promoCode
					null,
					PaymentType.SMPU_CARD,
					null,
					"1000.00",
					null,
					null,
					"1000.00",
					null,
					null,
					"1500.00",
					"500.00",
					checkCardResult.getMembershipNeedPaySum(),
					checkCardResult.getMembershipFeeMargin(),
					true,
					true);

			if (!in(paymentResult.getCode(), C_OPERATION_OK, C_NEED_END_INFO, C_SMS_CONFIRM_CREATED, C_ENOUGH_MEANS)) {
				Assert.fail();
			}
			if (paymentResult.getCode() == C_ENOUGH_MEANS) {
				paymentResult = operations.payment(
						"20001",
						"9900990010021",
						null,
						ReplenishKind.SHARE_FEE,
						PaymentType.CASH,
						null,
						"0.00",
						paymentResult.getSumShareFeeNeed(),
						paymentResult.getShareFeeNeedMargin(),
						"1000.00",
						null,
						null,
						"0.00",
						"0.00",
						checkCardResult.getMembershipNeedPaySum(),
						checkCardResult.getMembershipFeeMargin(),
						true,
						true);
			}
			ConfirmationResult confirmationResult = null;
			if (paymentResult.getCode() == C_NEED_END_INFO) {
				confirmationResult = operations.confirm(paymentResult.getIdTelegr(), CardOperations.ConfirmationType.NONE, null, "");
			} else if (paymentResult.getCode() == C_SMS_CONFIRM_CREATED) {
				confirmationResult = operations.confirm(paymentResult.getIdTelegr(), CardOperations.ConfirmationType.SMS, "1234", "");
			}
		} finally{
			connectionManager.close(connection);
		}
	}
	
	@Test
	public void firstTest() throws SQLException, PersistentException{
		Connection connection=connectionManager.open("U20001", "1234");
		try {
			CardOperations operations = new CardOperations(connection);

			CheckCardResult checkCardResponse = operations.checkCard(
					"20001",
					"9900990010014",
					CardOperations.OperationType.PAYMENT,
					"DD.MM.RRRR");

			if (!in(checkCardResponse.getCode(), C_OPERATION_OK, C_MEMBERSHIP_FEE_ERROR)) {
				Assert.fail();
			}


			PaymentResult paymentResult = operations.payment(
					"20001",
					"9900990010014",
					null,
					null,
					CardOperations.PaymentType.CASH,
					"",
					"",
					"",
					"",
					"100.00",
					"",
					"",
					"150.00",
					"50.00",
					checkCardResponse.getMembershipNeedPaySum(),
					checkCardResponse.getMembershipFeeMargin(),
					true,
					true);

			if (!in(paymentResult.getCode(), C_OPERATION_OK, C_NEED_END_INFO)) {
				Assert.fail();
			}

			if (paymentResult.getCode() == C_OPERATION_OK) {
				// OK
				return;
			}
			if (paymentResult.getCode() == C_NEED_END_INFO) {
				ConfirmationResult confirmationResult = operations.confirm(paymentResult.getIdTelegr(), CardOperations.ConfirmationType.NONE, "", "");
				Assert.assertNotNull(confirmationResult);
				// OK
				return;
			}
		}finally{
			connectionManager.close(connection);
		}
	}

	private boolean in(int code, int ... codes) {
		if(codes==null){
			return false;
		}
		for(int index=0;index<codes.length;index++){
			if(codes[index]==code){
				return true;
			}
		}
		return false;
	}
}
